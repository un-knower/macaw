package cn.migu.macaw.sparkdrivermgr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置参数
 *
 * @author soy
 */
@Component("globalParam")
public class GlobalParam
{
    @Value("${server.port}")
    private String port;

    @Value("${spark-driver.port-pool}")
    private String[] driverPortPool;

    @Value("${spark-driver.restart-all}")
    private boolean restartAllDriver;

    @Value("${spark-job.max-cores}")
    private int sparkMaxCores;

    @Value("${spark-job.max-mem-size}")
    private int sparkMaxMem;

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String[] getDriverPortPool()
    {
        return driverPortPool;
    }

    public void setDriverPortPool(String[] driverPortPool)
    {
        this.driverPortPool = driverPortPool;
    }

    public int getSparkMaxCores()
    {
        return sparkMaxCores;
    }

    public void setSparkMaxCores(int sparkMaxCores)
    {
        this.sparkMaxCores = sparkMaxCores;
    }

    public int getSparkMaxMem()
    {
        return sparkMaxMem;
    }

    public void setSparkMaxMem(int sparkMaxMem)
    {
        this.sparkMaxMem = sparkMaxMem;
    }

    public boolean isRestartAllDriver()
    {
        return restartAllDriver;
    }

    public void setRestartAllDriver(boolean restartAllDriver)
    {
        this.restartAllDriver = restartAllDriver;
    }
}
