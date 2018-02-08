package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "data_file")
public class DataFile extends BaseEntity
{
    /**
     * 应用系统id
     */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * 服务器id
     */
    @Column(name = "SERVER_ID")
    private String serverId;

    /**
     * jar id
     */
    @Column(name = "JAR_ID")
    private String jarId;

    /**
     * 1：数据文件，2：运行文件，3：校验文件，4：xlsxlsx，5：csv，10：第三方
     */
    @Column(name = "KIND")
    private Integer kind;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 编码
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 原始路径
     */
    @Column(name = "INIT_PATH")
    private String initPath;

    /**
     * 采集路径
     */
    @Column(name = "COLLECT_PATH")
    private String collectPath;

    /**
     * 备份路径
     */
    @Column(name = "COPY_PATH")
    private String copyPath;

    /**
     * 清理周期,秒
     */
    @Column(name = "CLEAN_PERIOD")
    private Integer cleanPeriod;

    /**
     * 采集周期,秒
     */
    @Column(name = "COLLECT_PERIOD")
    private Integer collectPeriod;

    /**
     * 文件单位,字节
     */
    @Column(name = "UNIT_SIZE")
    private Long unitSize;

    /**
     * 生成周期,秒
     */
    @Column(name = "UNIT_PERIOD")
    private Integer unitPeriod;

    /**
     * 分割符
     */
    @Column(name = "DECOLLATOR")
    private String decollator;

    /**
     * 对应表名
     */
    @Column(name = "MAP_TABLE")
    private String mapTable;

    /**
     * 字符编码,1：UTF8，2：GBK，3：ISO-9958-1，4：GB2312
     */
    @Column(name = "CHARACTER")
    private Integer character;

    /**
     * 列数
     */
    @Column(name = "COL_NUM")
    private Integer colNum;

    /**
     * 字符长度
     */
    @Column(name = "CHAR_NUM")
    private Long charNum;

    /**
     * 清空点
     */
    @Column(name = "CLEAR_TIME")
    private String clearTime;

    /**
     * 是否校验,0:不校验,1:要校验
     */
    @Column(name = "IS_VERIFY")
    private Integer isVerify;

    /**
     * 监控周期,毫秒
     */
    @Column(name = "MONITOR_PERIOD")
    private Integer monitorPeriod;

    /**
     * 起始行号
     */
    @Column(name = "START_LINE")
    private Integer startLine;

    /**
     * 分隔数量
     */
    @Column(name = "SPLIT_NUM")
    private Integer splitNum;

    /**
     * 更新周期
     */
    @Column(name = "PERIOD")
    private Integer period;

    /**
     * 更新振幅
     */
    @Column(name = "SCOPE")
    private Integer scope;

    /**
     * 状态，1-正常，2-异常
     */
    @Column(name = "STATUS")
    private Integer status;

    /**
     * 文件最长等待时间,单位:秒
     */
    @Column(name = "WAIT_TIME")
    private Long waitTime;

    /**
     * 自定义jar的合并文件数
     */
    @Column(name = "MERGE_NUM")
    private Long mergeNum;

    /**
     * 自定义jar的合并等待时间,秒
     */
    @Column(name = "MERGE_TIME")
    private Long mergeTime;

    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;

    /**
     * 外部函数
     */
    @Column(name = "EXT_FUNCTION")
    private String extFunction;

    /**
     * 获取应用系统id
     *
     * @return APP_ID - 应用系统id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置应用系统id
     *
     * @param appId 应用系统id
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * 获取服务器id
     *
     * @return SERVER_ID - 服务器id
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * 设置服务器id
     *
     * @param serverId 服务器id
     */
    public void setServerId(String serverId) {
        this.serverId = serverId == null ? null : serverId.trim();
    }

    /**
     * 获取jar id
     *
     * @return JAR_ID - jar id
     */
    public String getJarId() {
        return jarId;
    }

    /**
     * 设置jar id
     *
     * @param jarId jar id
     */
    public void setJarId(String jarId) {
        this.jarId = jarId == null ? null : jarId.trim();
    }

    /**
     * 获取1：数据文件，2：运行文件，3：校验文件，4：xlsxlsx，5：csv，10：第三方
     *
     * @return KIND - 1：数据文件，2：运行文件，3：校验文件，4：xlsxlsx，5：csv，10：第三方
     */
    public Integer getKind() {
        return kind;
    }

    /**
     * 设置1：数据文件，2：运行文件，3：校验文件，4：xlsxlsx，5：csv，10：第三方
     *
     * @param kind 1：数据文件，2：运行文件，3：校验文件，4：xlsxlsx，5：csv，10：第三方
     */
    public void setKind(Integer kind) {
        this.kind = kind;
    }

    /**
     * 获取名称
     *
     * @return NAME - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取编码
     *
     * @return CODE - 编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取原始路径
     *
     * @return INIT_PATH - 原始路径
     */
    public String getInitPath() {
        return initPath;
    }

    /**
     * 设置原始路径
     *
     * @param initPath 原始路径
     */
    public void setInitPath(String initPath) {
        this.initPath = initPath == null ? null : initPath.trim();
    }

    /**
     * 获取采集路径
     *
     * @return COLLECT_PATH - 采集路径
     */
    public String getCollectPath() {
        return collectPath;
    }

    /**
     * 设置采集路径
     *
     * @param collectPath 采集路径
     */
    public void setCollectPath(String collectPath) {
        this.collectPath = collectPath == null ? null : collectPath.trim();
    }

    /**
     * 获取备份路径
     *
     * @return COPY_PATH - 备份路径
     */
    public String getCopyPath() {
        return copyPath;
    }

    /**
     * 设置备份路径
     *
     * @param copyPath 备份路径
     */
    public void setCopyPath(String copyPath) {
        this.copyPath = copyPath == null ? null : copyPath.trim();
    }

    /**
     * 获取清理周期,秒
     *
     * @return CLEAN_PERIOD - 清理周期,秒
     */
    public Integer getCleanPeriod() {
        return cleanPeriod;
    }

    /**
     * 设置清理周期,秒
     *
     * @param cleanPeriod 清理周期,秒
     */
    public void setCleanPeriod(Integer cleanPeriod) {
        this.cleanPeriod = cleanPeriod;
    }

    /**
     * 获取采集周期,秒
     *
     * @return COLLECT_PERIOD - 采集周期,秒
     */
    public Integer getCollectPeriod() {
        return collectPeriod;
    }

    /**
     * 设置采集周期,秒
     *
     * @param collectPeriod 采集周期,秒
     */
    public void setCollectPeriod(Integer collectPeriod) {
        this.collectPeriod = collectPeriod;
    }

    /**
     * 获取文件单位,字节
     *
     * @return UNIT_SIZE - 文件单位,字节
     */
    public Long getUnitSize() {
        return unitSize;
    }

    /**
     * 设置文件单位,字节
     *
     * @param unitSize 文件单位,字节
     */
    public void setUnitSize(Long unitSize) {
        this.unitSize = unitSize;
    }

    /**
     * 获取生成周期,秒
     *
     * @return UNIT_PERIOD - 生成周期,秒
     */
    public Integer getUnitPeriod() {
        return unitPeriod;
    }

    /**
     * 设置生成周期,秒
     *
     * @param unitPeriod 生成周期,秒
     */
    public void setUnitPeriod(Integer unitPeriod) {
        this.unitPeriod = unitPeriod;
    }

    /**
     * 获取分割符
     *
     * @return DECOLLATOR - 分割符
     */
    public String getDecollator() {
        return decollator;
    }

    /**
     * 设置分割符
     *
     * @param decollator 分割符
     */
    public void setDecollator(String decollator) {
        this.decollator = decollator == null ? null : decollator.trim();
    }

    /**
     * 获取对应表名
     *
     * @return MAP_TABLE - 对应表名
     */
    public String getMapTable() {
        return mapTable;
    }

    /**
     * 设置对应表名
     *
     * @param mapTable 对应表名
     */
    public void setMapTable(String mapTable) {
        this.mapTable = mapTable == null ? null : mapTable.trim();
    }

    /**
     * 获取字符编码,1：UTF8，2：GBK，3：ISO-9958-1，4：GB2312
     *
     * @return CHARACTER - 字符编码,1：UTF8，2：GBK，3：ISO-9958-1，4：GB2312
     */
    public Integer getCharacter() {
        return character;
    }

    /**
     * 设置字符编码,1：UTF8，2：GBK，3：ISO-9958-1，4：GB2312
     *
     * @param character 字符编码,1：UTF8，2：GBK，3：ISO-9958-1，4：GB2312
     */
    public void setCharacter(Integer character) {
        this.character = character;
    }

    /**
     * 获取列数
     *
     * @return COL_NUM - 列数
     */
    public Integer getColNum() {
        return colNum;
    }

    /**
     * 设置列数
     *
     * @param colNum 列数
     */
    public void setColNum(Integer colNum) {
        this.colNum = colNum;
    }

    /**
     * 获取字符长度
     *
     * @return CHAR_NUM - 字符长度
     */
    public Long getCharNum() {
        return charNum;
    }

    /**
     * 设置字符长度
     *
     * @param charNum 字符长度
     */
    public void setCharNum(Long charNum) {
        this.charNum = charNum;
    }

    /**
     * 获取清空点
     *
     * @return CLEAR_TIME - 清空点
     */
    public String getClearTime() {
        return clearTime;
    }

    /**
     * 设置清空点
     *
     * @param clearTime 清空点
     */
    public void setClearTime(String clearTime) {
        this.clearTime = clearTime == null ? null : clearTime.trim();
    }

    /**
     * 获取是否校验,0:不校验,1:要校验
     *
     * @return IS_VERIFY - 是否校验,0:不校验,1:要校验
     */
    public Integer getIsVerify() {
        return isVerify;
    }

    /**
     * 设置是否校验,0:不校验,1:要校验
     *
     * @param isVerify 是否校验,0:不校验,1:要校验
     */
    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    /**
     * 获取监控周期,毫秒
     *
     * @return MONITOR_PERIOD - 监控周期,毫秒
     */
    public Integer getMonitorPeriod() {
        return monitorPeriod;
    }

    /**
     * 设置监控周期,毫秒
     *
     * @param monitorPeriod 监控周期,毫秒
     */
    public void setMonitorPeriod(Integer monitorPeriod) {
        this.monitorPeriod = monitorPeriod;
    }

    /**
     * 获取起始行号
     *
     * @return START_LINE - 起始行号
     */
    public Integer getStartLine() {
        return startLine;
    }

    /**
     * 设置起始行号
     *
     * @param startLine 起始行号
     */
    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    /**
     * 获取分隔数量
     *
     * @return SPLIT_NUM - 分隔数量
     */
    public Integer getSplitNum() {
        return splitNum;
    }

    /**
     * 设置分隔数量
     *
     * @param splitNum 分隔数量
     */
    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }

    /**
     * 获取更新周期
     *
     * @return PERIOD - 更新周期
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * 设置更新周期
     *
     * @param period 更新周期
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * 获取更新振幅
     *
     * @return SCOPE - 更新振幅
     */
    public Integer getScope() {
        return scope;
    }

    /**
     * 设置更新振幅
     *
     * @param scope 更新振幅
     */
    public void setScope(Integer scope) {
        this.scope = scope;
    }

    /**
     * 获取状态，1-正常，2-异常
     *
     * @return STATUS - 状态，1-正常，2-异常
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态，1-正常，2-异常
     *
     * @param status 状态，1-正常，2-异常
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取文件最长等待时间,单位:秒
     *
     * @return WAIT_TIME - 文件最长等待时间,单位:秒
     */
    public Long getWaitTime() {
        return waitTime;
    }

    /**
     * 设置文件最长等待时间,单位:秒
     *
     * @param waitTime 文件最长等待时间,单位:秒
     */
    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * 获取自定义jar的合并文件数
     *
     * @return MERGE_NUM - 自定义jar的合并文件数
     */
    public Long getMergeNum() {
        return mergeNum;
    }

    /**
     * 设置自定义jar的合并文件数
     *
     * @param mergeNum 自定义jar的合并文件数
     */
    public void setMergeNum(Long mergeNum) {
        this.mergeNum = mergeNum;
    }

    /**
     * 获取自定义jar的合并等待时间,秒
     *
     * @return MERGE_TIME - 自定义jar的合并等待时间,秒
     */
    public Long getMergeTime() {
        return mergeTime;
    }

    /**
     * 设置自定义jar的合并等待时间,秒
     *
     * @param mergeTime 自定义jar的合并等待时间,秒
     */
    public void setMergeTime(Long mergeTime) {
        this.mergeTime = mergeTime;
    }

    /**
     * 获取说明
     *
     * @return NOTE - 说明
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置说明
     *
     * @param note 说明
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * 获取外部函数
     *
     * @return EXT_FUNCTION - 外部函数
     */
    public String getExtFunction() {
        return extFunction;
    }

    /**
     * 设置外部函数
     *
     * @param extFunction 外部函数
     */
    public void setExtFunction(String extFunction) {
        this.extFunction = extFunction == null ? null : extFunction.trim();
    }
}