package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import java.util.Vector;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.tags.decorators.campi.AbstractGestoreCampo;
import it.eldasoft.utils.properties.ConfigManager;

public class GestoreCampoEMAILPHASE extends AbstractGestoreCampo {

  private static final String PROP_SIMAP_TEDESENDERCLASS = "it.eldasoft.simap.tedesenderclass";
  private static final String PROP_SIMAP_DESTINATARIO_ALFA  = "it.eldasoft.simap.destinatario.alfa";
  private static final String PROP_SIMAP_DESTINATARIO_BETA  = "it.eldasoft.simap.destinatario.beta";
  private static final String PROP_SIMAP_DESTINATARIO_GAMMA = "it.eldasoft.simap.destinatario.gamma";
  
  public String gestisciDaTrova(Vector params, DataColumn col, String conf, SqlManager manager) {
    return null;
  }

  public String getClasseEdit() {
    return null;
  }

  public String getClasseVisua() {
    return null;
  }

  public String getHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

  public String getValore(String valore) {
    return null;
  }

  public String getValorePerVisualizzazione(String valore) {
    return null;
  }

  public String getValorePreUpdateDB(String valore) {
    return null;
  }

  protected void initGestore() {

    String tipo = this.getCampo().getTipo();
    if (tipo == null || tipo.length() == 0) tipo = "ET500";
    if (tipo.charAt(0) != 'E') tipo = "E" + tipo;
    this.getCampo().setTipo(tipo);
    this.getCampo().getValori().clear();

    String tedsenderclass = ConfigManager.getValore(PROP_SIMAP_TEDESENDERCLASS);
    String destinatarioalfa = ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_ALFA);
    String destinatariobeta = ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_BETA);
    String destinatariogamma = ConfigManager.getValore(PROP_SIMAP_DESTINATARIO_GAMMA);
    
    if (destinatarioalfa != null && !"".equals(destinatarioalfa)) this.getCampo().addValore("ALFA", "Fase ALFA");
    if (destinatariobeta != null && !"".equals(destinatariobeta)) this.getCampo().addValore("BETA", "Fase BETA");
    if (destinatariogamma != null && !"".equals(destinatariogamma)) this.getCampo().addValore("GAMMA", "Fase GAMMA");
    
  }

  public String postHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

  public String preHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

}
