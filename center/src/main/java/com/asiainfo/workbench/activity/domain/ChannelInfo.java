package com.asiainfo.workbench.activity.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChannelInfo {

    @JsonProperty(value = "CHANNELS")
    private List<Channel> channels;
    @JsonProperty(value = "IP")
    private String ip;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "channels=" + channels +
                ", ip='" + ip + '\'' +
                '}';
    }
}
