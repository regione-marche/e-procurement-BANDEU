/**
 * EldasoftSimapWSBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eldasoft.simap.ws;

import it.eldasoft.sil.w3.bl.EldasoftSIMAPWSFacade;
import it.eldasoft.utils.spring.SpringAppContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class EldasoftSimapWSBindingImpl implements
    it.eldasoft.simap.ws.EldasoftSimapWS {

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoPreinformazione(
      java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SpringAppContext.getServletContext());
    EldasoftSIMAPWSFacade eldasoftSIMAPWSFacade = (EldasoftSIMAPWSFacade) ctx.getBean("eldasoftSIMAPWSFacade");
    return eldasoftSIMAPWSFacade.inserisciAvvisoPreinformazione(login,
        password, datiXML);
  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGara(
      java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SpringAppContext.getServletContext());
    EldasoftSIMAPWSFacade eldasoftSIMAPWSFacade = (EldasoftSIMAPWSFacade) ctx.getBean("eldasoftSIMAPWSFacade");
    return eldasoftSIMAPWSFacade.inserisciBandoGara(login, password, datiXML);
  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoAggiudicazione(
      java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SpringAppContext.getServletContext());
    EldasoftSIMAPWSFacade eldasoftSIMAPWSFacade = (EldasoftSIMAPWSFacade) ctx.getBean("eldasoftSIMAPWSFacade");
    return eldasoftSIMAPWSFacade.inserisciAvvisoAggiudicazione(login, password,
        datiXML);
  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoProfiloCommittente(
      java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SpringAppContext.getServletContext());
    EldasoftSIMAPWSFacade eldasoftSIMAPWSFacade = (EldasoftSIMAPWSFacade) ctx.getBean("eldasoftSIMAPWSFacade");
    return eldasoftSIMAPWSFacade.inserisciAvvisoProfiloCommittente(login,
        password, datiXML);

  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGaraSemplificato(
      java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SpringAppContext.getServletContext());
    EldasoftSIMAPWSFacade eldasoftSIMAPWSFacade = (EldasoftSIMAPWSFacade) ctx.getBean("eldasoftSIMAPWSFacade");
    return eldasoftSIMAPWSFacade.inserisciBandoGaraSemplificato(login,
        password, datiXML);
  }

}
