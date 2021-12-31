/**
 * EldasoftSimapWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eldasoft.simap.ws;

public interface EldasoftSimapWS extends java.rmi.Remote {
    public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoPreinformazione(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException;
    public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGara(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException;
    public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoAggiudicazione(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException;
    public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoProfiloCommittente(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException;
    public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGaraSemplificato(java.lang.String login, java.lang.String password, java.lang.String datiXML) throws java.rmi.RemoteException;
}
