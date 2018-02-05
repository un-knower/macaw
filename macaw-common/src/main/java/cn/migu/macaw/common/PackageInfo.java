package cn.migu.macaw.common;

/**
 * 包信息
 * @author soy
 */
public interface PackageInfo
{
    String COMPONENT_SCAN_BASE = "cn.migu";

    String SCHEDULE_DAO_BASE = COMPONENT_SCAN_BASE + ".macaw.schedule.dao";

    String SPARK_DRIVER_MGR_DAO_BASE = COMPONENT_SCAN_BASE + ".macaw.sparkdrivermgr.dao";

    String JAR_BOOT_DAO_BASE = COMPONENT_SCAN_BASE + ".macaw.jarboot.dao";
}
