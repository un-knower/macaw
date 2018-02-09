package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "online_data_source")
public class OnlineDataSource extends BaseEntity
{

    /**
     * 1：内部Kafka，2：Hive，3：Oracle，4：Redis，5：Mysql，6：Ftp,7：HBase，8：外部Kafka
     */
    @Column(name = "KIND")
    private Integer kind;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 连接串
     */
    @Column(name = "CONNECT_URL")
    private String connectUrl;

    /**
     * 驱动名
     */
    @Column(name = "DRIVER_NAME")
    private String driverName;

    /**
     * 驱动包
     */
    @Column(name = "DRIVER_JAR")
    private String driverJar;

    /**
     * 用户名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * topic名
     */
    @Column(name = "TOPIC")
    private String topic;

    /**
     * 列数
     */
    @Column(name = "COL_NUM")
    private Integer colNum;

    /**
     * 分割符
     */
    @Column(name = "DECOLLATOR")
    private String decollator;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    /**
     * 所属文件id
     */
    @Column(name = "FILE_ID")
    private String fileId;

    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;


    /**
     * 获取1：内部Kafka，2：Hive，3：Oracle，4：Redis，5：Mysql，6：Ftp,7：HBase，8：外部Kafka
     *
     * @return KIND - 1：内部Kafka，2：Hive，3：Oracle，4：Redis，5：Mysql，6：Ftp,7：HBase，8：外部Kafka
     */
    public Integer getKind() {
        return kind;
    }

    /**
     * 设置1：内部Kafka，2：Hive，3：Oracle，4：Redis，5：Mysql，6：Ftp,7：HBase，8：外部Kafka
     *
     * @param kind 1：内部Kafka，2：Hive，3：Oracle，4：Redis，5：Mysql，6：Ftp,7：HBase，8：外部Kafka
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
     * 获取地址
     *
     * @return ADDRESS - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取连接串
     *
     * @return CONNECT_URL - 连接串
     */
    public String getConnectUrl() {
        return connectUrl;
    }

    /**
     * 设置连接串
     *
     * @param connectUrl 连接串
     */
    public void setConnectUrl(String connectUrl) {
        this.connectUrl = connectUrl == null ? null : connectUrl.trim();
    }

    /**
     * 获取驱动名
     *
     * @return DRIVER_NAME - 驱动名
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * 设置驱动名
     *
     * @param driverName 驱动名
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName == null ? null : driverName.trim();
    }

    /**
     * 获取驱动包
     *
     * @return DRIVER_JAR - 驱动包
     */
    public String getDriverJar() {
        return driverJar;
    }

    /**
     * 设置驱动包
     *
     * @param driverJar 驱动包
     */
    public void setDriverJar(String driverJar) {
        this.driverJar = driverJar == null ? null : driverJar.trim();
    }

    /**
     * 获取用户名
     *
     * @return USERNAME - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取密码
     *
     * @return PASSWORD - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取topic名
     *
     * @return TOPIC - topic名
     */
    public String getTopic() {
        return topic;
    }

    /**
     * 设置topic名
     *
     * @param topic topic名
     */
    public void setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
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
     * @return PROJECT_CODE
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * @param projectCode
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode == null ? null : projectCode.trim();
    }

    /**
     * 获取所属文件id
     *
     * @return FILE_ID - 所属文件id
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 设置所属文件id
     *
     * @param fileId 所属文件id
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
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
}