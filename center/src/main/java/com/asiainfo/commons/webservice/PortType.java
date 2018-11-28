/**
 * PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.asiainfo.commons.webservice;

public interface PortType extends java.rmi.Remote {
    public String recv_msg(String devNo, String source, String msg, String remarks) throws java.rmi.RemoteException;
}
