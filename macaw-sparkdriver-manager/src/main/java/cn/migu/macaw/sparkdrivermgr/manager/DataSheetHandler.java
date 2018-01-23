package cn.migu.macaw.sparkdrivermgr.manager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import cn.migu.macaw.sparkdrivermgr.model.SparkDriverMetaData;
import cn.migu.macaw.sparkdrivermgr.model.SparkJobMetaData;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.sparkdrivermgr.api.model.Jar;
import cn.migu.macaw.sparkdrivermgr.api.model.Process;
import cn.migu.macaw.sparkdrivermgr.api.model.ProcessLog;
import cn.migu.macaw.sparkdrivermgr.api.model.SparkApplicationLog;
import cn.migu.macaw.sparkdrivermgr.dao.JarMapper;
import cn.migu.macaw.sparkdrivermgr.dao.ProcessLogMapper;
import cn.migu.macaw.sparkdrivermgr.dao.ProcessMapper;
import cn.migu.macaw.sparkdrivermgr.dao.SparkApplicationLogMapper;
import cn.migu.macaw.sparkdrivermgr.model.AvailableSparkDriverProcess;

/**
 * 数据表项操作类
 * 
 * @author soy
 */
@Component("dataSheetHandler")
public class DataSheetHandler
{
    @Resource
    private SparkApplicationLogMapper appDao;
    
    @Resource
    private ProcessMapper processDao;
    
    @Resource
    private JarMapper jarDao;
    
    @Resource
    private ProcessLogMapper processLogDao;


    
    /**
     * 通过objId获取进程对象
     * @param id 进程主键
     * @return Process - 进程实例
     * @see [类、类#方法、类#成员]
     */
    public Process getProcessById(String id)
    {
        if (StringUtils.isEmpty(id))
        {
            return null;
        }
        
        Process p = new Process();
        p.setObjId(id);
        
        return processDao.selectOne(p);
    }
    
    /**
     * 通过objId获取jar对象
     * @param id jar表主键
     * @return JAR - jar实例
     */
    public Jar getJarById(String id)
    {
        if (StringUtils.isEmpty(id))
        {
            return null;
        }
        
        Jar p = new Jar();
        p.setObjId(id);
        
        return jarDao.selectOne(p);
    }
    
    /**
     * 通过app名称查询
     * @param appName spark app名称
     * @return SparkApplicationLog - spark app日志记录
     * @see [类、类#方法、类#成员]
     */
    public SparkApplicationLog getAppByName(String appName)
    {
        if (StringUtils.isEmpty(appName))
        {
            return null;
        }
        
        SparkApplicationLog app = new SparkApplicationLog();
        app.setAppName(appName);
        
        return appDao.selectOne(app);
    }
    
    /**
     * 根据appid查询
     * @param appId spark appid
     * @return SparkApplicationLog - spark app日志记录
     * @see [类、类#方法、类#成员]
     */
    public SparkApplicationLog getAppById(String appId)
    {
        if (StringUtils.isEmpty(appId))
        {
            return null;
        }
        
        SparkApplicationLog app = new SparkApplicationLog();
        app.setAppid(appId);
        
        return appDao.selectOne(app);
    }
    
    /**
     * 获取最新的app log
     * @param appId spark appid
     * @return SparkApplicationLog - spark app日志记录
     * @see [类、类#方法、类#成员]
     */
    public SparkApplicationLog getLatestAppById(String appId)
    {
        if (StringUtils.isEmpty(appId))
        {
            return null;
        }
        
        return appDao.getLatestAppLogByAppId(appId);
    }

    
    /**
     * 更新spark app log
     * @param app spark application日志
     */
    public synchronized void updateAppLog(SparkApplicationLog app)
    {
        appDao.updateByPrimaryKeySelective(app);
    }
    
    /**
     * 根据主键删除进程
     * @param id 进程主键
     */
    public void deleteProcessById(String id)
    {
        if (StringUtils.isEmpty(id))
        {
            return;
        }
        
        processDao.deleteByPrimaryKey(id);
    }
    
    /**
     * 按jarid删除进程对象
     * @param jar
     */
    public void deleteProcess(Jar jar)
    {
        if (null == jar)
        {
            return;
        }

        processDao.deleteProcessByJarId(jar.getObjId());
    }
    
    /**
     * 恢复进程状态
     * @param id 进程主键
     */
    public void resumeProcessStatus(String id)
    {
        if (StringUtils.isEmpty(id))
        {
            return;
        }
        
        Process pro = new Process();
        pro.setObjId(id);
        pro.setStatus(0);
        processDao.updateByPrimaryKeySelective(pro);
    }

    /**
     * 更新进程状态
     * @param processId 进程主键
     * @param status 进程状态
     */
    public void updateProcessStatus(String processId, int status)
    {
        Process p = new Process();
        p.setObjId(processId);
        p.setStatus(status);
        
        processDao.updateByPrimaryKeySelective(p);
    }
    
    /**
     * 更新spark app记录日志
     * @param res spark job信息
     */
    public synchronized void updateAppLog(SparkJobMetaData res)
    {
        SparkApplicationLog sparkApp = new SparkApplicationLog();
        String uuid = UUID.randomUUID().toString().replace("-","");
        sparkApp.setObjId(uuid);
        sparkApp.setDriverIp(res.getDriverIp());
        sparkApp.setDriverPort(res.getPort());
        sparkApp.setStartTime(new Date());
        sparkApp.setStatus(SysRetCode.SPARK_APP_RUNNING);
        
        sparkApp.setBatchno(res.getBatchNo());
        
        sparkApp.setJobCode(res.getJobCode());
        
        sparkApp.setCores(res.getCoreNum());
        
        sparkApp.setExecutorMemory(String.valueOf(res.getMemSize()));

        sparkApp.setUserName(String.valueOf(res.getProcessId()));
        
        sparkApp.setAppid(res.getAppId());
        sparkApp.setAppName(res.getAppName());

        SparkApplicationLog appLog = this.getLatestAppById(res.getAppId());
        if (null == appLog)
        {
            LogUtils.runLogError(StringUtils.join("提交spark app后查询不到spark_application记录[", res.getAppId(), "]"));
        }
        
        sparkApp.setObjId(appLog.getObjId());
        
        appDao.updateByPrimaryKeySelective(sparkApp);
        
    }
    
    /**
     * 进程日志
     * @param bootParams jar启动数据信息
     * @see [类、类#方法、类#成员]
     */
    public void addProcessLog(SparkDriverMetaData bootParams)
    {
        ProcessLog upl = new ProcessLog();
        
        upl.setAppId(bootParams.getDriverType());
        upl.setServerId(bootParams.getServerId());
        upl.setJarId(bootParams.getJarId());
        upl.setPort(bootParams.getDriverPort());
        upl.setStatus("END");
        upl.setKind("2");
        upl.setProcessNo(bootParams.getPid());
        
        try
        {
            processLogDao.insertSelective(upl);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
        
    }
    
    /**
     * 增加进程
     * @param process 进程实例
     * @see [类、类#方法、类#成员]
     */
    public synchronized void addProcess(Process process)
    {
        processDao.insertSelective(process);
    }

    
    /**
     * 增加app记录
     * @param app spark application日志实例
     * @see [类、类#方法、类#成员]
     */
    public synchronized void addAppLog(SparkApplicationLog app)
    {
        if (null == app)
        {
            return;
        }
        
        appDao.insertSelective(app);
    }
    
    /**
     * 所有spark driver部署服务器的信息
     *
     */
    public List<Jar> getAllDriverDeployment()
    {
        return jarDao.getAllDriverDeployment();
    }
    
    /**
     * 根据spark driver类型获取driver的资源部署信息
     * 1.类型为JAR表中的app_id字段
     * 2.app_id数据格式为[ip-类型]
     * @param ip 部署主机地址
     * @param type 类型
     * @return List<Jar> jar配置信息
     */
    public List<Jar> queryDriverDeployment(String ip, String type)
    {
        String driverType = StringUtils.join(ip, "%");
        
        if (StringUtils.isNotEmpty(type))
        {
            driverType = StringUtils.join(ip, "-", type);
        }
        
        return jarDao.getDriverDeploymentByType(driverType);
    }


    /**
     * 获取空闲driver
     * @param driverType spark driver类型
     * @return AvailableSparkDriverProcess - 可用的spark driver进程
     */
    public AvailableSparkDriverProcess getIdleDriver(String driverType)
    {
        return processDao.queryAvailableDriver(driverType);
    }

    /**
     * 获取spark driver元数据
     * @param processId
     * @return
     */
    public SparkDriverMetaData getSparkDriverMeta(String processId)
    {
        return processDao.queryDriverMeta(processId);
    }

}
