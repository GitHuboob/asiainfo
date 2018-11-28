package com.asiainfo.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusInfo {

    @JsonProperty(value = "INTERFACE_NAME")
    private String interfaceName;
    @JsonProperty(value = "STATUS")
    private String status;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusInfo{" +
                "interfaceName='" + interfaceName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
