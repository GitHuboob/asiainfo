package com.asiainfo.workbench.activity.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Channel {

    @JsonProperty(value = "STATE")
    private String state;
    @JsonProperty(value = "JOB_NAME")
    private String jobName;

    @JsonProperty(value = "ID")
    private Integer id;
    @JsonProperty(value = "GROUP_CODE")
    private String groupCode;
    @JsonProperty(value = "REMARK")
    private String remark;
    @JsonProperty(value = "MAX_SEND_COUNT")
    private String maxSendCount;

    @JsonProperty(value = "WORK_TYPE")
    private String workType;
    /*@JsonProperty(value = "FILE_NAME_PREFIX")
    private String fileNamePrefix;
    @JsonProperty(value = "FILE_NAME_SUFFIX")
    private String fileNameSuffix;*/
    @JsonProperty(value = "FILE_NAME")
    private String fileName;
    @JsonProperty(value = "FILE_PATH")
    private String filePath;

    @JsonProperty(value = "SEND_TIME")
    private String sendTime;
    @JsonProperty(value = "NEXT_START_TIME")
    private String nextStartTime;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(String nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMaxSendCount() {
        return maxSendCount;
    }

    public void setMaxSendCount(String maxSendCount) {
        this.maxSendCount = maxSendCount;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "state='" + state + '\'' +
                ", jobName='" + jobName + '\'' +
                ", id=" + id +
                ", groupCode='" + groupCode + '\'' +
                ", remark='" + remark + '\'' +
                ", maxSendCount='" + maxSendCount + '\'' +
                ", workType='" + workType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", nextStartTime='" + nextStartTime + '\'' +
                '}';
    }
}
