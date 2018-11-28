/**
 * Recv__msgLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.asiainfo.commons.webservice;

public class Recv__msgLocator extends org.apache.axis.client.Service implements Recv__msg {

    public Recv__msgLocator() {
    }


    public Recv__msgLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Recv__msgLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SOAPEventSource
    private String SOAPEventSource_address = "http://10.4.44.239:8815/recv_msg/recv_msg";

    public String getSOAPEventSourceAddress() {
        return SOAPEventSource_address;
    }

    // The WSDD service name defaults to the port name.
    private String SOAPEventSourceWSDDServiceName = "SOAPEventSource";

    public String getSOAPEventSourceWSDDServiceName() {
        return SOAPEventSourceWSDDServiceName;
    }

    public void setSOAPEventSourceWSDDServiceName(String name) {
        SOAPEventSourceWSDDServiceName = name;
    }

    public PortType getSOAPEventSource() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SOAPEventSource_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSOAPEventSource(endpoint);
    }

    public PortType getSOAPEventSource(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SOAPEventSourceBindingStub _stub = new SOAPEventSourceBindingStub(portAddress, this);
            _stub.setPortName(getSOAPEventSourceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSOAPEventSourceEndpointAddress(String address) {
        SOAPEventSource_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                SOAPEventSourceBindingStub _stub = new SOAPEventSourceBindingStub(new java.net.URL(SOAPEventSource_address), this);
                _stub.setPortName(getSOAPEventSourceWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("SOAPEventSource".equals(inputPortName)) {
            return getSOAPEventSource();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://xmlns.example.com/1108622653470/recv_msgImpl", "recv__msg");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://xmlns.example.com/1108622653470/recv_msgImpl", "SOAPEventSource"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("SOAPEventSource".equals(portName)) {
            setSOAPEventSourceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
