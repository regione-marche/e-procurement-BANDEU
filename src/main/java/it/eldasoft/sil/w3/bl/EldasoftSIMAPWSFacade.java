package it.eldasoft.sil.w3.bl;

import it.eldasoft.gene.tags.utils.UtilityTags;
import it.eldasoft.simap.ws.EsitoSimapWS;

import org.apache.log4j.Logger;

public class EldasoftSIMAPWSFacade {

  Logger                         logger = Logger.getLogger(EldasoftSIMAPWSFacade.class);

  private EldasoftSIMAPWSManager eldasoftSIMAPWSManager;

  public EldasoftSIMAPWSManager getEldasoftSIMAPWSManager() {
    return eldasoftSIMAPWSManager;
  }

  public void setEldasoftSIMAPWSManager(EldasoftSIMAPWSManager eldasoftSIMAPWSManager) {
    this.eldasoftSIMAPWSManager = eldasoftSIMAPWSManager;
  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoPreinformazione(java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciAvvisoPreinformazione: inizio metodo");

    EsitoSimapWS esitoSimapWS = new EsitoSimapWS();
    try {
      this.eldasoftSIMAPWSManager.inserisciAvvisoPreinformazione(login, password, datiXML);
      esitoSimapWS.setEsito(true);
    } catch (Exception e) {
      esitoSimapWS.setEsito(false);
      esitoSimapWS.setMessaggio(e.getMessage());
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciAvvisoPreinformazione: fine metodo");

    return esitoSimapWS;

  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGara(java.lang.String login, java.lang.String password, java.lang.String datiXML)
      throws java.rmi.RemoteException {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciBandoGara: inizio metodo");

    EsitoSimapWS esitoSimapWS = new EsitoSimapWS();
    try {
      this.eldasoftSIMAPWSManager.inserisciBandoGara(login, password, datiXML);
      esitoSimapWS.setEsito(true);
    } catch (Exception e) {
      esitoSimapWS.setEsito(false);
      esitoSimapWS.setMessaggio(e.getMessage());
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciBandoGara: fine metodo");

    return esitoSimapWS;

  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoAggiudicazione(java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciAvvisoAggiudicazione: inizio metodo");

    EsitoSimapWS esitoSimapWS = new EsitoSimapWS();
    try {
      this.eldasoftSIMAPWSManager.inserisciAvvisoAggiudicazione(login, password, datiXML);
      esitoSimapWS.setEsito(true);
    } catch (Exception e) {
      esitoSimapWS.setEsito(false);
      esitoSimapWS.setMessaggio(e.getMessage());
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciAvvisoAggiudicazione: fine metodo");

    return esitoSimapWS;

  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciAvvisoProfiloCommittente(java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciAvvisoProfiloCommittente: inizio metodo");

    EsitoSimapWS esitoSimapWS = new EsitoSimapWS();
    try {
      this.eldasoftSIMAPWSManager.inserisciAvvisoProfiloCommittente(login, password, datiXML);
      esitoSimapWS.setEsito(true);
    } catch (Exception e) {
      esitoSimapWS.setEsito(false);
      esitoSimapWS.setMessaggio(e.getMessage());
    }

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciAvvisoProfiloCommittente: fine metodo");

    return esitoSimapWS;

  }

  public it.eldasoft.simap.ws.EsitoSimapWS inserisciBandoGaraSemplificato(java.lang.String login, java.lang.String password,
      java.lang.String datiXML) throws java.rmi.RemoteException {
    EsitoSimapWS esitoSimapWS = new EsitoSimapWS();
    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciBandoGaraSemplificato: inizio metodo");

    esitoSimapWS.setEsito(false);
    esitoSimapWS.setMessaggio(UtilityTags.getResource("eldasoftsimapws.operazionenondisponibile", new String[] {}, false));

    if (logger.isDebugEnabled()) logger.debug("EldasoftSIMAPWSFacade.inserisciBandoGaraSemplificato: fine metodo");

    return esitoSimapWS;

  }

}
