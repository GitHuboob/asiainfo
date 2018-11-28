package com.asiainfo.workbench.message.domain;

public class Message {

    private int messageId;
    private String tableName;
    private String createTime;
    private int repeatCount;
    private String remark;
    private String orderName;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", tableName='" + tableName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", repeatCount=" + repeatCount +
                ", remark='" + remark + '\'' +
                ", orderName='" + orderName + '\'' +
                '}';
    }

}
