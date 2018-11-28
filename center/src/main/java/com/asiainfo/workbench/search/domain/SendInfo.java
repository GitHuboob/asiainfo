package com.asiainfo.workbench.search.domain;


import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "sendinfo",type = "info")
public class SendInfo implements Serializable {

    private Long id;
    private String emailReceiver;
    private String emailContent;
    private String messageReceiver;
    private String messageContent;
    private String description;

    public SendInfo() {
    }

    public SendInfo(Long id, String emailReceiver, String emailContent, String messageReceiver, String messageContent, String description) {
        this.id = id;
        this.emailReceiver = emailReceiver;
        this.emailContent = emailContent;
        this.messageReceiver = messageReceiver;
        this.messageContent = messageContent;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SendInfo{" +
                "id=" + id +
                ", emailReceiver='" + emailReceiver + '\'' +
                ", emailContent='" + emailContent + '\'' +
                ", messageReceiver='" + messageReceiver + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
