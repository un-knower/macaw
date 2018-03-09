package cn.migu.macaw.schedule.workflow;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.MapUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

import cn.migu.macaw.dag.idgraph.IdDag;

import com.google.common.collect.ImmutableSet;
import com.nirmata.workflow.WorkflowManager;
import com.nirmata.workflow.WorkflowManagerBuilder;
import com.nirmata.workflow.events.WorkflowEvent;
import com.nirmata.workflow.events.WorkflowListener;
import com.nirmata.workflow.events.WorkflowListenerManager;
import com.nirmata.workflow.models.Task;
import com.nirmata.workflow.models.TaskType;

/**
 * tasks调度运行类
 * 
 * @author soy
 */
public class TaskGroupWorkflow implements Closeable
{
    private final TestingServer testingServer;
    
    private final CuratorFramework curator;
    
    private final IdDag<String> customDag;
    
    public TaskGroupWorkflow(IdDag<String> customDag)
        throws Exception
    {
        // 内存zookeeper实例
        testingServer = new TestingServer();
        
        // 创建 Curator实例
        curator =
            CuratorFrameworkFactory.builder()
                .connectString(testingServer.getConnectString())
                .retryPolicy(new ExponentialBackoffRetry(100, 3))
                .build();
        
        this.customDag = customDag;
    }
    
    /**
     * job-task图调度
     * @param root
     * @see [类、类#方法、类#成员]
     */
    public void schedule(Task root)
    {
        curator.start();
        
        TaskType taskType =
            new TaskType(DataConstants.WORKFLOW_TYPE, DataConstants.WORKFLOW_VERSION, false);
        
        TaskExecutorShell taskExecutor = new TaskExecutorShell(this.customDag);
        
        // workflow命名空间
        String namespace = UUID.randomUUID().toString();
        
        //调度并发度
        int qty = this.getExecutorNum(this.customDag);
        
        //分配workflow manager
        WorkflowManager workflowManager =
            WorkflowManagerBuilder.builder()
                .addingTaskExecutor(taskExecutor, qty, taskType)
                .withCurator(curator, namespace, DataConstants.WORKFLOW_VERSION)
                .build();
        
        WorkflowListenerManager workflowListenerManager = workflowManager.newWorkflowListenerManager();
        try
        {
            // 监听运行
            final CountDownLatch doneLatch = new CountDownLatch(1);
            WorkflowListener listener = new WorkflowListener()
            {
                @Override
                public void receiveEvent(WorkflowEvent event)
                {
                    if (event.getType() == WorkflowEvent.EventType.RUN_UPDATED)
                    {
                        doneLatch.countDown();
                    }
                }
            };
            workflowListenerManager.getListenable().addListener(listener);
            
            // 启动管理和监听
            workflowManager.start();
            workflowListenerManager.start();
            
            // 提交task
            workflowManager.submitTask(root);
            
            // 等待完成
            doneLatch.await();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        finally
        {
            CloseableUtils.closeQuietly(workflowListenerManager);
            CloseableUtils.closeQuietly(workflowManager);
        }
    }
    
    @Override
    public void close()
        throws IOException
    {
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        CloseableUtils.closeQuietly(curator);
        CloseableUtils.closeQuietly(testingServer);
    }
    
    /**
     * workflow执行最大并行度
     * 实际计算值可能比使用过程中用到的executor数量要多
     * @param dag
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int getExecutorNum(IdDag<String> dag)
    {
        ImmutableSet<String> nodes = dag.idSet();
        
        int qty = 1;
        
        //获取每个结点的出度
        Map<String, Integer> nodeOutDegree = nodes.stream().collect(HashMap::new, (m, n) -> {
            if (dag.childIdSet(n).size() > 1)
            {
                m.put(n, dag.childIdSet(n).size());
            }
        }, HashMap::putAll);
        
        if (MapUtils.isNotEmpty(nodeOutDegree))
        {
            qty = nodeOutDegree.values().stream().mapToInt(Number::intValue).sum();
        }
        
        //根节点单独处理
        int roots = dag.rootIdSet().size();
        if (roots > 1)
        {
            if (qty != 1)
            {
                qty += roots;
            }
            else
            {
                qty = roots;
            }
            
        }

        /*ScheduleLogTrace.scheduleInfoLog(StringUtils.join("本次调度分配的executor数量为", String.valueOf(qty)));*/
        
        return qty;
    }
    
}
