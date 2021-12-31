/**
 * EldasoftSimapWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eldasoft.simap.ws;

public class EldasoftSimapWSServiceLocator extends org.apache.axis.client.Service implements it.eldasoft.simap.ws.EldasoftSimapWSService {

    public EldasoftSimapWSServiceLocator() {
    }


    public EldasoftSimapWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EldasoftSimapWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EldasoftSimapWS
    private java.lang.String EldasoftSimapWS_address = "http://localhost:8080/SchedeAutorita/services/EldasoftSimapWS";

    public java.lang.String getEldasoftSimapWSAddress() {
        return EldasoftSimapWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EldasoftSimapWSWSDDServiceName = "EldasoftSimapWS";

    public java.lang.String getEldasoftSimapWSWSDDServiceName() {
        return EldasoftSimapWSWSDDServiceName;
    }

    public void setEldasoftSimapWSWSDDServiceName(java.lang.String name) {
        EldasoftSimapWSWSDDServiceName = name;
    }

    public it.eldasoft.simap.ws.EldasoftSimapWS getEldasoftSimapWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EldasoftSimapWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEldasoftSimapWS(endpoint);
    }

    public it.eldasoft.simap.ws.EldasoftSimapWS getEldasoftSimapWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.eldasoft.simap.ws.EldasoftSimapWSBindingStub _stub = new it.eldasoft.simap.ws.EldasoftSimapWSBindingStub(portAddress, this);
            _stub.setPortName(getEldasoftSimapWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEldasoftSimapWSEndpointAddress(java.lang.String address) {
        EldasoftSimapWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.eldasoft.simap.ws.EldasoftSimapWS.class.isAssignableFrom(serviceEndpointInterface)) {
                it.eldasoft.simap.ws.EldasoftSimapWSBindingStub _stub = new it.eldasoft.simap.ws.EldasoftSimapWSBindingStub(new java.net.URL(EldasoftSimapWS_address), this);
                _stub.setPortName(getEldasoftSimapWSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
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
        java.lang.String inputPortName = portName.getLocalPart();
        if ("EldasoftSimapWS".equals(inputPortName)) {
            return getEldasoftSimapWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "EldasoftSimapWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "EldasoftSimapWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EldasoftSimapWS".equals(portName)) {
            setEldasoftSimapWSEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
