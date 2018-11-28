package com.asiainfo.workbench.contacts.domain;

/**
 * 联系人和市场活动关联关系实体类
 * 
 * @author Administrator
 *
 */
public class StaffGroupRelation {

    private String staffId;
	private String groupId;
	private String state;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
