/**
 * EldasoftSimapWSBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eldasoft.simap.ws;

public class EldasoftSimapWSBindingSkeleton implements it.eldasoft.simap.ws.EldasoftSimapWS, org.apache.axis.wsdl.Skeleton {
    private it.eldasoft.simap.ws.EldasoftSimapWS impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "login"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiXML"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("inserisciAvvisoPreinformazione", _params, new javax.xml.namespace.QName("", "esito"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "esitoSimapWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "inserisciAvvisoPreinformazione"));
        _oper.setSoapAction("http://ws.simap.eldasoft.it/inserisciAvvisoPreinformazione");
        _myOperationsList.add(_oper);
        if (_myOperations.get("inserisciAvvisoPreinformazione") == null) {
            _myOperations.put("inserisciAvvisoPreinformazione", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("inserisciAvvisoPreinformazione")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "login"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiXML"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("inserisciBandoGara", _params, new javax.xml.namespace.QName("", "esito"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "esitoSimapWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "inserisciBandoGara"));
        _oper.setSoapAction("http://ws.simap.eldasoft.it/inserisciBandoGara");
        _myOperationsList.add(_oper);
        if (_myOperations.get("inserisciBandoGara") == null) {
            _myOperations.put("inserisciBandoGara", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("inserisciBandoGara")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "login"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiXML"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("inserisciAvvisoAggiudicazione", _params, new javax.xml.namespace.QName("", "esito"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "esitoSimapWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "inserisciAvvisoAggiudicazione"));
        _oper.setSoapAction("http://ws.simap.eldasoft.it/inserisciAvvisoAggiudicazione");
        _myOperationsList.add(_oper);
        if (_myOperations.get("inserisciAvvisoAggiudicazione") == null) {
            _myOperations.put("inserisciAvvisoAggiudicazione", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("inserisciAvvisoAggiudicazione")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "login"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiXML"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("inserisciAvvisoProfiloCommittente", _params, new javax.xml.namespace.QName("", "esito"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "esitoSimapWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "inserisciAvvisoProfiloCommittente"));
        _oper.setSoapAction("http://ws.simap.eldasoft.it/inserisciAvvisoProfiloCommittente");
        _myOperationsList.add(_oper);
        if (_myOperations.get("inserisciAvvisoProfiloCommittente") == null) {
            _myOperations.put("inserisciAvvisoProfiloCommittente", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("inserisciAvvisoProfiloCommittente")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "login"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiXML"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("inserisciBandoGaraSemplificato", _params, new javax.xml.namespace.QName("", "esito"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "esitoSimapWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ws.simap.eldasoft.it/", "inserisciBandoGaraSemplificato"));
        _oper.setSoapAction("http://ws.simap.eldasoft.it/inserisciBandoGaraSemplificato");
        _myOperationsList.add(_oper);
        if (_myOperations.get("inserisciBandoGaraSemplificato") == null) {
            _myOperations.put("inserisciBandoGaraSemplificato", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("inserisciBandoGaraSemplificato")).add(_oper);
    }

    public EldasoftSimapWSBindingSkeleton() {
        this.impl = new it.eldasoft.simap.ws.EldasoftSimapWSBindingImpl();
    }

    public EldasoftSimapWSBindingSkeleton(it.eldasoft.simap.ws.EldasoftSimapWS impl) {
        this.impl = impl;
    }
    public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoPreinformazione(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException
    {
        it.eldasoft.simap.ws.EsitoSimapWS ret = impl.inserisciAvvisoPreinformazione(login, password, datiXML);
        return ret;
    }

    public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGara(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException
    {
        it.eldasoft.simap.ws.EsitoSimapWS ret = impl.inserisciBandoGara(login, password, datiXML);
        return ret;
    }

    public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoAggiudicazione(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException
    {
        it.eldasoft.simap.ws.EsitoSimapWS ret = impl.inserisciAvvisoAggiudicazione(login, password, datiXML);
        return ret;
    }

    public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoProfiloCommittente(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException
    {
        it.eldasoft.simap.ws.EsitoSimapWS ret = impl.inserisciAvvisoProfiloCommittente(login, password, datiXML);
        return ret;
    }

    public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGaraSemplificato(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException
    {
        it.eldasoft.simap.ws.EsitoSimapWS ret = impl.inserisciBandoGaraSemplificato(login, password, datiXML);
        return ret;
    }

}
