package com.asiainfo.workbench.space.domain;

public class Space {

    private int id;
    private String spaceName;
    private String megsAlloc;
    private String megsFree;
    private String megsUsed;
    private String pctFree;
    private String pctUsed;
    private String extendMax;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getMegsAlloc() {
        return megsAlloc;
    }

    public void setMegsAlloc(String megsAlloc) {
        this.megsAlloc = megsAlloc;
    }

    public String getMegsFree() {
        return megsFree;
    }

    public void setMegsFree(String megsFree) {
        this.megsFree = megsFree;
    }

    public String getMegsUsed() {
        return megsUsed;
    }

    public void setMegsUsed(String megsUsed) {
        this.megsUsed = megsUsed;
    }

    public String getPctFree() {
        return pctFree;
    }

    public void setPctFree(String pctFree) {
        this.pctFree = pctFree;
    }

    public String getPctUsed() {
        return pctUsed;
    }

    public void setPctUsed(String pctUsed) {
        this.pctUsed = pctUsed;
    }

    public String getExtendMax() {
        return extendMax;
    }

    public void setExtendMax(String extendMax) {
        this.extendMax = extendMax;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Space{" +
                "id=" + id +
                ", spaceName='" + spaceName + '\'' +
                ", megsAlloc='" + megsAlloc + '\'' +
                ", megsFree='" + megsFree + '\'' +
                ", megsUsed='" + megsUsed + '\'' +
                ", pctFree='" + pctFree + '\'' +
                ", pctUsed='" + pctUsed + '\'' +
                ", extendMax='" + extendMax + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
