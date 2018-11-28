package com.asiainfo.workbench.autorun.domain;

public class AutoRunRecord {

    private String jobName;
    private String fileName;
    private String lastTime;
    private String nextStartTime;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(String nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    @Override
    public String toString() {
        return "AutoRunRecord{" +
                "jobName='" + jobName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", nextStartTime='" + nextStartTime + '\'' +
                '}';
    }
}
