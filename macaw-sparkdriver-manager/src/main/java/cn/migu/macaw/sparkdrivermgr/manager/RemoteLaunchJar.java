package cn.migu.macaw.sparkdrivermgr.manager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.NetUtils;
import cn.migu.macaw.common.SSHManager;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.sparkdrivermgr.GlobalParam;
import cn.migu.macaw.sparkdrivermgr.api.model.Jar;
import cn.migu.macaw.sparkdrivermgr.api.model.SparkApplicationLog;
import cn.migu.macaw.sparkdrivermgr.cache.SparkJobContext;
import cn.migu.macaw.sparkdrivermgr.common.DriverType;
import cn.migu.macaw.sparkdrivermgr.model.SparkDriverMetaData;
import cn.migu.macaw.sparkdrivermgr.model.SparkJobMetaData;

/**
 * 远程启动/关闭jar
 * 
 * @author soy
 */
@Component("remoteLaunchJar")
public class RemoteLaunchJar
{
    @Resource
    private DataSheetHandler dataSheetHandler;
    
    @Resource
    private SparkJobContext sparkJobCtx;

    @Autowired
    private GlobalParam globalParam;
    
    private static final Log sparkDriverLaunchLog = LogFactory.getLog("spark-driver-launch");
    
    /**
     * 启动单个spring boot jar
     * @param param
     * @see [类、类#方法、类#成员]
     */
    public void start(SparkDriverMetaData param)
    {
        
        String localIp = "";
        try
        {
            localIp = NetUtils.ipAddressToUrlString(InetAddress.getLocalHost());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
            return;
        }

        String bootShellString =
            StringUtils.join(param.getLaunchCmd(),
                " ",
                String.valueOf(param.getDriverPort()),
                " ",
                param.getDriverIp(),
                " ",
                localIp,
                " ",
                globalParam.getPort(),
                " ",
                param.getJarId(),
                " ",
                param.getDriverType(),
                " ",
                param.getServerId(),
                " >/dev/null 2>&1 &");
        
        try
        {
            sparkDriverLaunchLog.info(StringUtils.join("restart=>", bootShellString));
            SSHManager.execCommand(param.getDriverIp(), param.getUsername(), param.getPassword(), bootShellString);
        }
        catch (Exception e)
        {
            sparkDriverLaunchLog.error("",e);
        }
        
        /*String result =
            RemoteCmdUtil.execCmd(param.getHostIp(), param.getUsername(), param.getPassword(), bootShellString);*/
        
    }
    
    /**
     * 启动单个spring boot jar
     * @param driver
     * @param port
     * @see [类、类#方法、类#成员]
     */
    public void start(Jar driver, String port)
    {
        
        String localIp = "";
        try
        {
            localIp = NetUtils.ipAddressToUrlString(InetAddress.getLocalHost());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
            return;
        }
        
        String bootShellString =
            StringUtils.join(driver.getPath(),
                " ",
                port,
                " ",
                driver.getIp(),
                " ",
                localIp,
                " ",
                globalParam.getPort(),
                " ",
                driver.getObjId(),
                " ",
                driver.getAppId(),
                " ",
                driver.getServerId(),
                " >/dev/null 2>&1 &");
        
        try
        {
            sparkDriverLaunchLog.info(StringUtils.join("init=>", bootShellString));

            SSHManager.execCommand(driver.getIp(), driver.getUsername(), driver.getPassword(), bootShellString);
        }
        catch (Exception e)
        {
            sparkDriverLaunchLog.error("",e);
        }
        
    }
    
    /**
     * 停止spring boot jar
     * @param param
     * @see [类、类#方法、类#成员]
     */
    public void stop(SparkDriverMetaData param)
    {
        SSHManager.execCommand(param.getDriverIp(),
            param.getUsername(),
            param.getPassword(),
            StringUtils.join("kill -9 ", param.getPid()));
    }
    
    /**
     * 重启单个jar
     * @param param
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public void restart(SparkDriverMetaData param)
        throws Exception
    {
        this.stop(param);
        
        Thread.sleep(1000);
        
        this.start(param);
    }
    
    /**
     * 停止单个主机上的drivers
     * @param driver
     * @see [类、类#方法、类#成员]
     */
    public void stopAll(Jar driver)
    {
        final String killAllShell = "ps -ef|grep JarLauncher |grep -v grep| awk '{print $2}' | xargs kill -9";
        
        final String ip = driver.getIp();
        final String userName = driver.getUsername();
        final String passWord = driver.getPassword();

        SSHManager.execCommand(ip, userName, passWord, killAllShell);
        
    }
    
    /**
     * 停止所有drivers
     * @see [类、类#方法、类#成员]
     */
    public void stopAllDrivers(boolean isRestartDrivers)
    {
        List<Jar> driverList = dataSheetHandler.getAllDriverDeployment();

        driverList.stream().forEach(driver ->{
            if (isRestartDrivers)
            {
                this.stopAll(driver);
                dataSheetHandler.deleteProcess(driver);
            }
        });
        
    }
    
    /**
     * 启动所有drivers
     * @see [类、类#方法、类#成员]
     */
    public void startAllDrivers(boolean isRestartDrivers)
    {
        if (!isRestartDrivers)
        {
            return;
        }
        
        List<Jar> driverList = dataSheetHandler.getAllDriverDeployment();
        for (Jar driver : driverList)
        {
            int max = driver.getMaxJvm().intValue();
            for (int i = 0; i < max; i++)
            {
                
                String port = globalParam.getDriverPortPool()[i];
                
                this.start(driver, port);
                
            }
        }
        
    }
    
    /**
     * 启动参数
     * @param resCtx
     * @return
     * @see [类、类#方法、类#成员]
     */
    public SparkDriverMetaData genJarBootParam(SparkJobMetaData resCtx)
    {
        String appName = resCtx.getAppName();
        
        int port = Integer.valueOf(resCtx.getPort());
        
        return genJarBootParam(appName, port);
        
    }
    
    /**
     * 根据进程表和jar全局配置信息搜集启动参数
     * @param processId 进程主键
     */
    private SparkDriverMetaData setProcessBootParam(String processId)
    {
        return dataSheetHandler.getSparkDriverMeta(processId);
    }
    
    /**
     * driver上下文->启动信息
     * @param resCtx
     * @return
     * @see [类、类#方法、类#成员]
     */
    public SparkDriverMetaData resCtxToJarBootParam(SparkJobMetaData resCtx)
    {
        String processId = resCtx.getProcessId();
        
        if (StringUtils.isEmpty(processId))
        {
            return null;
        }

        return this.setProcessBootParam(processId);

    }
    
    /**
     * 启动参数
     * @param appName
     * @param port
     * @return
     * @see [类、类#方法、类#成员]
     */
    public SparkDriverMetaData genJarBootParam(String appName, int port)
    {
        
        SparkApplicationLog app = dataSheetHandler.getAppByName(appName);
        //spark application log中user_name字段存放进程id
        if (null != app && StringUtils.isNotEmpty(app.getUserName()))
        {
            
            return this.setProcessBootParam(app.getUserName());
        }
        else
        {
            return null;
        }
        
    }
    
    /**
     * 任务回调
     * @param appName
     * @param status
     * @param remotePort
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public String callback(String appName, String status, int remotePort, HttpServletRequest request)
        throws Exception
    {
        SparkDriverMetaData bootParams = this.genJarBootParam(appName, remotePort);
        if (null == bootParams)
        {
            return SysRetCode.SPARK_RES_PARAM_ERROR;
        }
        
        //更新app记录
        String appId = this.updateApp(appName, status, request);
        if (sparkJobCtx.contains(appId))
        {
            return SysRetCode.SUCCESS;
        }
        
        //进程日志
        dataSheetHandler.addProcessLog(bootParams);
        
        if (this.isReboot(bootParams))
        {
            //删除进程
            dataSheetHandler.deleteProcessById(bootParams.getProcessId());
            
            //重启jar
            this.restart(bootParams);
        }
        else
        {
            dataSheetHandler.resumeProcessStatus(bootParams.getProcessId());
        }
        
        return SysRetCode.SUCCESS;
    }
    
    /**
     * 更新app记录
     * @param appName
     * @param status
     * @see [类、类#方法、类#成员]
     */
    private String updateApp(String appName, String status, HttpServletRequest request)
    {
        SparkApplicationLog app = dataSheetHandler.getAppByName(appName);
        
        if (null == app)
        {
            try
            {
                Thread.sleep(2000);
                app = dataSheetHandler.getAppByName(appName);
            }
            catch (InterruptedException e)
            {
                
            }
            if (null == app)
            {
                LogUtils.runLogError(StringUtils.join("更新spark application状态时查询不到记录[", appName, "]"));
                return null;
            }
            
        }
        
        Date nowTime = new Date();
        
        app.setStatus(status);
        app.setEndTime(nowTime);
        
        long dur = nowTime.getTime() / 1000 - app.getStartTime().getTime() / 1000;
        
        app.setDuration(dur);
        
        if (null != request)
        {
            String errorStackStr = request.getParameter("errorStack");
            if (StringUtils.isNotEmpty(errorStackStr))
            {
                String oldNote = app.getNote();
                String newNote = StringUtils.join(oldNote, "errorStack=>", errorStackStr);
                app.setNote(newNote);
            }
        }
        
        dataSheetHandler.updateAppLog(app);
        
        return app.getAppid();
    }
    
    /**
     * 是否重启进程
     * @param bootParams
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean isReboot(SparkDriverMetaData bootParams)
    {
        if (StringUtils.equals(bootParams.getDriverType(), DriverType.ALGO))
        {
            return false;
        }
        
        return true;
    }
    
}
