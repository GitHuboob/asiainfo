/**
 * Recv__msg.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.asiainfo.commons.webservice;

public interface Recv__msg extends javax.xml.rpc.Service {
    public String getSOAPEventSourceAddress();

    public PortType getSOAPEventSource() throws javax.xml.rpc.ServiceException;

    public PortType getSOAPEventSource(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
