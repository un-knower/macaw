package cn.migu.macaw.schedule.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.common.NetUtils;
import cn.migu.macaw.schedule.PlatformAttr;
import cn.migu.macaw.schedule.task.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.nirmata.workflow.models.Task;
import com.nirmata.workflow.models.TaskId;
import com.nirmata.workflow.models.TaskType;

import cn.migu.macaw.common.ApplicationContextProvider;
import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.dag.alg.PathsLib;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.dag.idgraph.IdDagLib;
import cn.migu.macaw.schedule.api.model.*;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.dao.*;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.service.IJobTasksService;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;
import cn.migu.macaw.schedule.workflow.TaskGroupWorkflow;

/**
 * task序列执行类
 * 
 * @author soy
 */
@Service("jobTasksService")
public class JobTasksServiceImpl implements IJobTasksService
{
    @Resource
    private IJobCommService jobCommService;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private ProcContextCache procCtxCache;
    
    @Resource
    private JobNodeMapper jobNodeDao;
    
    @Resource
    private TaskMapper taskDao;
    
    @Resource
    private JobParamMapper jobParamDao;
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    @Resource
    private ProcVariableMapMapper procVarsMapDao;
    
    @Resource
    private RunningResMgr runningResMgr;
    
    @Resource
    private DataSourceAdapter dataSourceMgr;
    
    @Resource
    private SparkEventHandler sparkEventHander;
    
    @Resource
    private TaskTraceLogUtil taskTraceLogUtil;
    
    @Resource
    private TaskTemplateMapper taskTempletMapper;
    
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private PlatformAttr platformAttr;
    
    private Log log = LogFactory.getLog("scheduler");
    
    /**
     * 创建job运行时tasks组成的workflow 运行时DAG
     */
    public Task buildRunDag(String jobCode, String batchNo, IdDag<String> dags, Map<String, TaskNodeBrief> nsc)
    {
        
        //拓扑排序
        ImmutableList<String> sortList = dags.topsortIdList().reverse();
        
        Map<String, Task> taskObjs = Maps.newHashMap();
        List<Task> cnodes = Lists.newArrayList();
        
        TaskType taskType =
            new TaskType(DataConstants.WORKFLOW_TYPE, DataConstants.WORKFLOW_VERSION, false);
        
        Map<String, String> metadata = Maps.newHashMap();
        metadata.put(DataConstants.JOB_CODE, jobCode);
        metadata.put(DataConstants.BATCH_NO, batchNo);
        
        for (String n : sortList)
        {
            if (!cnodes.isEmpty())
            {
                cnodes.clear();
            }
            
            metadata.put(DataConstants.TASK_NODE_TYPE, nsc.get(n).getTaskClassType());
            metadata.put(DataConstants.TASK_CODE, nsc.get(n).getTaskCode());
            
            // 区分叶节点和中间节点创建task对象方式
            if (dags.isLeaf(n))
            {
                
                Task lfn = new Task(new TaskId(n), taskType, Lists.newArrayList(), metadata);
                taskObjs.put(n, lfn);
            }
            else
            {
                ImmutableSet<String> childs = dags.childIdSet(n);
                
                for (String child : childs)
                {
                    cnodes.add(taskObjs.get(child));
                }
                Task mfn = new Task(new TaskId(n), taskType, cnodes, metadata);
                taskObjs.put(n, mfn);
            }
        }
        
        //所有根节点
        ImmutableSet<String> roots = dags.rootIdSet();
        cnodes.clear();
        for (String root : roots)
        {
            cnodes.add(taskObjs.get(root));
        }
        
        Task root = new Task(new TaskId(), cnodes);
        
        return root;
    }
    
    /**
     * 清除job运行过程中产生缓存等临时信息
     */
    public void clearJobContext(String jobCode)
    {
        try
        {
            
            if (jobTasksCache.contains(jobCode))
            {
                jobTasksCache.remove(jobCode);
            }
            
        }
        catch (Exception e)
        {
            LogUtils.runLogError("clearJobContext failed:" + ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
        
    }
    
    /**
     * 删除提交的app
     */
    public void stopApp(String jobCode, String batchNo)
    {
        runningResMgr.stopSparkApps(jobCode, batchNo);
    }
    
    /**
     * 中断执行子job
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    public void stopSubJob(String jobCode)
    {
        runningResMgr.stopSubJobs(jobCode);
    }
    
    /**
     * 资源回收
     * @param jobCode
     * @param batchNo
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void freeResource(String jobCode, String batchNo)
    {
        try
        {
            //stop运行app
            this.stopApp(jobCode, batchNo);
            
            //中断执行子job
            this.stopSubJob(jobCode);
            
            //释放计算中心申请driver
            this.freeSparkDriver(jobCode, batchNo);
            
            //释放其他业务资源
            this.freeServiceResource(jobCode, batchNo);
            
            //释放阻塞http请求
            this.closeHttpReq(jobCode);
            
        }
        catch (Exception e)
        {
            LogUtils.runLogError(
                StringUtils.join("jobCode=", jobCode, ",batchNo=", batchNo, ",", ExceptionUtils.getStackTrace(e)));
            
        }
        finally
        {
            //清空job内容信息
            this.clearJobContext(jobCode);
            
            //存储过程临时变量清除
            this.clearProcCusTmpVars(jobCode);
            
            //job临时变量清除
            this.clearJobTmpVars(jobCode);
        }
        
    }
    
    /**
     * 释放阻塞的http请求
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void closeHttpReq(String jobCode)
    {
        
    }
    
    /**
     * 删除unify_proc_variable_map表数据
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    public void clearProcCusTmpVars(String jobCode)
    {
        if (StringUtils.isNotEmpty(jobCode))
        {
            ProcVariableMap varMap = new ProcVariableMap();
            varMap.setJobCode(jobCode);
            
            procVarsMapDao.delete(varMap);
        }
    }
    
    /**
     * 任务临时变量清空
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    public void clearJobTmpVars(String jobCode)
    {
        if (StringUtils.isNotEmpty(jobCode))
        {
            JobParam jobParam = new JobParam();
            jobParam.setJobCode(jobCode);
            jobParam.setKind(3);
            
            jobParamDao.delete(jobParam);
        }
    }
    
    /**
     *
     * @param url
     * @param params
     * @param batchNo
     */
    private void httpRequest(String url, Map<String, String> params, String batchNo)
    {
        try
        {
            taskTraceLogUtil.reqPostLog(url, params, "", batchNo);
            String resp = RestTemplateProvider.postFormForEntity(restTemplate, url, String.class, params);
            taskTraceLogUtil.resqPostLog(url, resp, batchNo);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
    }
    
    /**
     * 释放指定业务资源
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    public void freeServiceResource(String jobCode, String batchNo)
    {
        boolean hasCrossdata = jobTasksCache.queryForHashKeyCounterExisted(jobCode, FieldKey.CROSSDATA_JOB_EXISTED);
        if (hasCrossdata)
        {
            Map<String, String> params = Maps.newHashMap();
            
            String crossdataServiceUrl = StringUtils.join(platformAttr.getBasePlatformUrl(),RequestServiceUri.DB_CROSSDATA_KILL);
                //StringUtils.join("http://", ServiceName.DATA_SYN_AND_HT, "/crossdatajob/shutdown.do");
            params.put("batchNum", jobCode);
            httpRequest(crossdataServiceUrl, params, batchNo);
        }
        
    }
    
    /**
     *
     * @param jobCode
     * @param batchNo
     */
    public void freeSparkDriver(String jobCode, String batchNo)
    {
        Job job = jobCommService.getJob(jobCode);
        //类型是存储过程的job
        if (null != job && 0 == job.getKind())
        {
            
            String appId = jobTasksCache.get(jobCode, "-", DataConstants.SPARK_CONTEXT_APPID);
            
            //spark context继承处理
            String pJobCode = procCtxCache.get(jobCode, StringUtils.join("${", DataConstants.PARENT_JOB_CODE, "}"));
            if (StringUtils.isNotEmpty(pJobCode))
            {
                String isInherited = jobTasksCache.get(pJobCode, "-", DataConstants.SPARK_CONTEXT_INHERITED);
                
                String pAppid = jobTasksCache.get(pJobCode, "-", DataConstants.SPARK_CONTEXT_APPID);
                
                if (StringUtils.equals(pAppid, appId))
                {
                    if (StringUtils.isNotEmpty(isInherited))
                    {
                        jobTasksCache.remove(pJobCode, "-", DataConstants.SPARK_CONTEXT_INHERITED);
                        
                        return;
                    }
                }
                
            }
            
            if (!StringUtils.isEmpty(appId))
            {
                TaskNodeBrief brief = new TaskNodeBrief();
                brief.setBatchCode(batchNo);
                brief.setJobCode(jobCode);
                
                sparkEventHander.releaseDriverNode(jobCode, batchNo, appId);
                
            }
        }
    }
    
    /**
     * 查询quartz cluster中的job是否运行
     * @param jobName
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean isJobRunning(String jobName)
    {
        StringBuffer querySql = new StringBuffer("select count(1) from qrtz_fired_triggers where job_name='");
        querySql.append(jobName).append("'");
        
        int count = jdbcTemplate.queryForObject(querySql.toString(), Integer.class);
        
        //在job提交后查询,正常情况下查询结果为1
        if (count > 1)
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * job任务运行接口
     */
    @Override
    public void jobRun(String jobCode, String batchNo)
        throws Exception
    {
        this.clearJobContext(jobCode);
        
        if (this.isJobRunning(jobCode))
        {
            ScheduleLogTrace.scheduleInfoLog(StringUtils.join("job[", jobCode, "]==>已有任务被调度运行"));
            return;
        }
        
        List<JobNode> jns = this.getNodesForJob(jobCode);
        if (CollectionUtils.isEmpty(jns))
        {
            ScheduleLogTrace.scheduleInfoLog(StringUtils.join("job[", jobCode, "]==>任务未配置task node"));
            return;
        }
        
        this.workflow(jobCode, batchNo, jns);
        
    }
    
    /**
     * 业务节点运行
     */
    @Override
    public void runNode(TaskNodeBrief brief, IdDag<String> dag, String classType)
        throws Exception
    {
        
        if (null == dag)
        {
            if (StringUtils.equals(classType, DataConstants.TASK_TYPE_DECISION)
                || StringUtils.equals(classType, DataConstants.TASK_TYPE_SINGLESQLOUT))
            {
                ScheduleLogTrace.scheduleWarnLog(brief, "不支持单节点运行");
                throw new RuntimeException(
                    StringUtils.join("[", brief.getJobCode(), "]-[", brief.getNodeId(), "]不支持单节点运行"));
            }
        }
        
        ITask taskIns = (ITask)ApplicationContextProvider.getBean(classType);
        
        taskIns.run(brief, dag);
    }
    
    /**
     * 运行指定区域图
     * @param jobCode 任务编码
     * @param firstNode 开始节点
     * @throws Exception
     */
    @Override
    public void runSpecRegion(String jobCode, String firstNode, String batchNo)
        throws Exception
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jobCode), "任务编码为空");
        
        Preconditions.checkArgument(StringUtils.isNotEmpty(firstNode), "指定开始节点为空");
        
        Preconditions.checkState(!jobTasksCache.contains(jobCode), "当前job已在运行状态");
        
        Thread.currentThread().setName(StringUtils.join("region_", jobCode));
        
        //查询任务节点集
        List<JobNode> allNodes = this.getNodesForJob(jobCode);
        if (CollectionUtils.isEmpty(allNodes))
        {
            throw new IllegalStateException(StringUtils.join("在[", jobCode, "]任务下没有配置运行节点"));
        }
        
        List<String> regionNodes = Lists.newArrayList();
        
        regionNodes.add(firstNode);
        
        this.getRegionNodes(allNodes, regionNodes, firstNode);
        
        //设置失效节点
        Set<String> ans = allNodes.stream().map(JobNode::getCode).collect(Collectors.toSet());
        
        Set<String> rns = regionNodes.stream().collect(Collectors.toSet());
        
        Set<String> failedNodes = Sets.difference(ans, rns);
        failedNodes.forEach(fn -> {
            this.setJobTaskCtxFlag(jobCode, fn, DataConstants.CUSTOM_CANCEL_RUN, "1");
        });
        
        jobTasksCache.put(jobCode, "", DataConstants.JOB_MODE_REGION, "1");
        
        this.workflow(jobCode, batchNo, allNodes);
        
    }
    
    /**
     * 任务执行完成回调接口
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void jobSuccCallbackIntf(String jobCode)
    {
        /*String callUrl = "";
        
        Map<String, String> params = Maps.newHashMap();
        params.put("jobcode", jobCode);
        params.put("status", "complete");
        
        try
        {
            HttpPostUtil.post(callUrl, params);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }*/
    }
    
    /**
     * 任务执行异常回调接口
     * @param jobCode
     * @param info
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void jobExcepCallbackIntf(String jobCode, String info)
    {
        /*String callUrl = "";
        
        Map<String, String> params = Maps.newHashMap();
        params.put("jobcode", jobCode);
        params.put("status", StringUtils.join("exception:", info));
        
        try
        {
            HttpPostUtil.post(callUrl, params);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }*/
    }
    
    /**
     * 设置job运行时标记信息
     * @param jobCode
     * @param nodeId
     * @param key
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void setJobTaskCtxFlag(String jobCode, String nodeId, String key, String value)
    {
        jobTasksCache.put(jobCode, nodeId, key, value);
    }
    
    /**
     * 获取job下的所有节点
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    private List<JobNode> getNodesForJob(String jobCode)
    {
        if (StringUtils.isEmpty(jobCode))
        {
            return null;
        }
        
        JobNode qn = new JobNode();
        qn.setJobCode(jobCode);
        
        return jobNodeDao.select(qn);
        
    }
    
    /**
     * 同步系统参数到redis
     * @throws CloneNotSupportedException 
     * @see [类、类#方法、类#成员]
     */
    private void synSysParam(String jobCode)
        throws CloneNotSupportedException
    {
        
        dataSourceMgr.synDataSourceInfoToRedis(jobCode);
        
        //job循环变量加载
        JobParam condition = new JobParam();
        condition.setKind(3);
        condition.setJobCode(jobCode);
        List<JobParam> loopVars = jobParamDao.select(condition);
        if (CollectionUtils.isNotEmpty(loopVars))
        {
            JobParam jp = loopVars.get(0);
            
            jobTasksCache.put(jobCode, "-", DataConstants.VAR_NAME_FOR_JOB_LOOP, jp.getPkey());
            jobTasksCache.put(jobCode, "-", DataConstants.VAR_VAL_FOR_JOB_LOOP, jp.getValue());
            
        }

        //job运行时保存当前实例运行的服务器地址信息
        this.jobInstanceAddress(jobCode);
        
    }
    
    /**
     * 创建局部有向图
     * @param allNodes
     * @param regNodes
     * @param pNode
     * @see [类、类#方法、类#成员]
     */
    private void getRegionNodes(final List<JobNode> allNodes, List<String> regNodes, String pNode)
    {
        
        List<String> cns =
            allNodes.stream().filter(n -> StringUtils.equals(n.getParentCode(), pNode)).map(JobNode::getCode).collect(
                Collectors.toCollection(ArrayList::new));
        
        if (CollectionUtils.isEmpty(cns))
        {
            return;
        }
        
        cns.forEach(cn -> {
            regNodes.add(cn);
            getRegionNodes(allNodes, regNodes, cn);
        });
    }
    
    /**
     * 根据输入顶点集创建有向图
     * @param jobCode
     * @param jns
     * @param tnbs
     * @return
     * @see [类、类#方法、类#成员]
     */
    private IdDag<String> buildGraph(String jobCode, List<JobNode> jns, Map<String, TaskNodeBrief> tnbs)
    {
        
        //task编码与task class映射关系
        /*List<UnifyTask> tasks = taskDao.select(null);
        Map<String, String> taskMap = tasks.stream().collect(HashMap::new, (m, t) -> {
            m.put(t.getCode(), t.getTaskClass());
        }, HashMap::putAll);*/
        List<TaskTemplate> tasks = taskTempletMapper.select(null);
        Map<String, String> taskMap = tasks.stream().collect(HashMap::new, (m, t) -> {
            m.put(t.getTaskCode(), t.getTaskClass());
        }, HashMap::putAll);
        
        // 节点集
        Set<String> nodeSets = jns.stream().map(JobNode::getCode).collect(Collectors.toCollection(HashSet::new));
        
        // 节点关系
        Multimap<String, String> idChildIds = HashMultimap.create();
        
        jns.forEach(jn -> {
            if (StringUtils.isNotEmpty(jn.getParentCode()))
            {
                idChildIds.put(jn.getParentCode(), jn.getCode());
            }
        });
        
        // 节点简明属性
        jns.forEach(jn -> {
            /*String taskCode = jn.getTaskCode();*/
            String taskCode = jn.getTaskCode();
            tnbs.put(jn.getCode(), new TaskNodeBrief(taskCode, taskMap.get(taskCode)));
        });
        
        // 创建自定义管理有向图
        IdDag<String> ids = IdDagLib.fromChildMap(nodeSets, idChildIds);
        
        // 缓存失效节点
        List<String> fjns = jns.stream().filter(jn -> (jn.getState() == 0)).map(JobNode::getCode).collect(
            Collectors.toCollection(ArrayList::new));
        fjns.forEach(fjn -> {
            this.setJobTaskCtxFlag(jobCode, fjn, DataConstants.CUSTOM_CANCEL_RUN, "1");
        });
        
        //缓存task node类型
        tnbs.forEach((k, v) -> {
            this.setJobTaskCtxFlag(jobCode, k, DataConstants.TASK_NODE_TYPE, v.getTaskClassType());
        });
        
        // 缓存路径上必须生效节点
        // 算法描述:查找从根节点至指定节点的路径上是否有决策节点,如果没有则指定节点为不能取消执行
        //       如果有,则在运行时动态规划是否需要取消
        // 适用场景举例:
        //   d----->t  
        //  / \      \
        // t   t    |t|->t
        //  \       /
        //   t----->t
        Set<String> subSet = new HashSet<>();
        ImmutableSet<String> roots = ids.rootIdSet();
        roots.forEach(root -> {
            idChildIds.put(DataConstants.FAKE_ROOT, root);
        });
        nodeSets.add(DataConstants.FAKE_ROOT);
        subSet.add(DataConstants.FAKE_ROOT);
        IdDag<String> tDag = IdDagLib.fromChildMap(nodeSets, idChildIds);
        
        Set<String> leftNodes = Sets.difference(nodeSets, subSet);
        
        for (String ln : leftNodes)
        {
            boolean valid = false;
            List<List<String>> paths = PathsLib.getAllPaths(DataConstants.FAKE_ROOT, ln, tDag);
            for (List<String> path : paths)
            {
                if (this.isConnected(path, tnbs, subSet))
                {
                    valid = true;
                    break;
                }
            }
            
            if (valid)
            {
                this.setJobTaskCtxFlag(jobCode, ln, DataConstants.CANNOT_CANCEL, "1");
            }
        }
        return ids;
    }
    
    /**
     * 执行流程
     * @param jobCode
     * @param batchNo
     * @param jns
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    private void workflow(String jobCode, String batchNo, List<JobNode> jns)
        throws Exception
    {
        
        Map<String, TaskNodeBrief> tnbs = Maps.newHashMap();
        
        IdDag<String> dags = this.buildGraph(jobCode, jns, tnbs);
        
        ScheduleLogTrace.scheduleInfoLog(StringUtils.join("[", jobCode, "]-[", batchNo, "]调度节点信息:", dags.toString()));
        
        // 同步系统参数至redis缓存
        this.synSysParam(jobCode);
        
        //创建运行workflow使用的DAG
        Task root = this.buildRunDag(jobCode, batchNo, dags, tnbs);
        
        TaskGroupWorkflow wf = new TaskGroupWorkflow(dags);
        wf.schedule(root);
        wf.close();
    }
    
    /**
     * 单节点运行
     * @param jobCode
     * @param nodeCode
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void runSingleNode(String jobCode, String nodeCode, String batchNo)
        throws Exception
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jobCode), "任务编码为空");
        
        Preconditions.checkArgument(StringUtils.isNotEmpty(nodeCode), "指定开始节点为空");
        
        Preconditions.checkState(!jobTasksCache.contains(jobCode), "当前job正在运行状态");
        
        Thread.currentThread().setName(StringUtils.join("single_", jobCode));
        
        jobTasksCache.put(jobCode, "", DataConstants.JOB_MODE_SINGLE, "1");
        
        this.synSysParam(jobCode);
        
        JobNode node = new JobNode();
        node.setJobCode(jobCode);
        node.setCode(nodeCode);
        
        List<JobNode> jns = jobNodeDao.select(node);
        if (CollectionUtils.isEmpty(jns))
        {
            return;
        }
        JobNode tNode = jobNodeDao.select(node).get(0);
        
        if (StringUtils.isNotEmpty(tNode.getTemplateCode()))
        {
            /*UnifyTask t = new UnifyTask();
            t.setCode(_node.getTaskCode());
            
            UnifyTask _t = taskDao.selectOne(t);*/
            
            TaskTemplate t = new TaskTemplate();
            t.setCode(tNode.getTemplateCode());
            List<TaskTemplate> uttLs = taskTempletMapper.select(t);
            TaskTemplate template = uttLs.get(0);
            
            if (StringUtils.isNotEmpty(template.getTaskClass()))
            {
                TaskNodeBrief brief = new TaskNodeBrief();
                brief.setJobCode(jobCode);
                brief.setNodeId(nodeCode);
                brief.setTaskCode(template.getCode());
                brief.setTaskClassType(template.getTaskClass());
                brief.setBatchCode(batchNo);
                
                IJobTasksService jobTasksService =
                    (IJobTasksService)ApplicationContextProvider.getBean("jobTasksService");
                jobTasksService.runNode(brief, null, template.getTaskClass());
            }
        }
        
    }
    
    /**
     * 判断从根节点到指定节点路径是否直连
     * 即路径上没有判断分支节点
     * @param path
     * @param tnbs
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isConnected(List<String> path, Map<String, TaskNodeBrief> tnbs, final Set<String> excludeNodes)
    {
        boolean connected = true;
        
        for (String n : path)
        {
            if (excludeNodes.contains(n))
            {
                continue;
            }
            TaskNodeBrief tnb = tnbs.get(n);
            
            if (StringUtils.equals(tnb.getTaskClassType(), DataConstants.TASK_TYPE_DECISION))
            {
                connected = false;
                break;
            }
        }
        
        return connected;
    }
    
    /**
     * job执行结束回调接口
     */
    @Override
    public void jobCallbackIntf(String jobCode, String info, boolean excep)
    {
        
    }
    
    /**
     * 是否为残留job上下文
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    @Override
    public boolean isResidualCtx(String jobCode)
    {
        //如果没有系统参数但有异常记录,则说明主job已停止
        if (!jobTasksCache.contains(jobCode))
        {
            return true;
        }
        
        return false;
    }

    /**
     * 存job运行实例所在的地址端口
     * @param jobCode
     */
    private void jobInstanceAddress(String jobCode)
    {
        try
        {
            int port = env.getProperty("server.port",Integer.class);
            String ipPort = NetUtils.ipAddressAndPortToUrlString(InetAddress.getLocalHost(),port);
            jobTasksCache.put(jobCode,"-",DataConstants.JOB_INSTANCE_ADDRESS,StringUtils.join("http://",ipPort));
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
    }

    /**
     * 取job运行实例所在的地址端口
     * @param jobCode
     * @return
     */
    @Override
    public String getJobInstanceAddress(String jobCode)
    {
        String address = jobTasksCache.get(jobCode,"-",DataConstants.JOB_INSTANCE_ADDRESS);
        if(StringUtils.isNotEmpty(address))
        {
            String ip = StringUtils.substringsBetween(address,"http://",":")[0];
            try
            {
                String localIp = NetUtils.ipAddressToUrlString(InetAddress.getLocalHost());
                if(StringUtils.equals(ip,localIp))
                {
                    return "local";
                }

                return address;
            }
            catch (UnknownHostException e)
            {
                LogUtils.runLogError(e);

            }
        }
        return null;
    }
    
}
