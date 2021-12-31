package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import java.util.Vector;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.tags.decorators.campi.AbstractGestoreCampo;

/**
 * Gestore del campo Si/No senza il valore null.
 * Questo gestore visualizza nella pagina JSP una select con le solo due option
 * 'Si' e 'No', valorizzate rispettivamente con 1 e 2.
 * Questo è stato possibile
 * implementando i metodi getHTML e initGestore  
 * 
 * @author Luca.Giacomazzo
 */
public class GestoreCampoSiNoImportazione extends AbstractGestoreCampo {

  public String getValore(String valore) {
    return null;
  }

  public String getValorePerVisualizzazione(String valore) {
    return null;
  }

  public String getValorePreUpdateDB(String valore) {
    return null;
  }

  public String preHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

  public String getHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

  public String postHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

  public String getClasseEdit() {
    return null;
  }

  public String getClasseVisua() {
    return null;
  }

  /**
   * Inizializzo il campo Si/No
   */
  protected void initGestore() {
    // Dominio SN Lo tratto come un enumerato
    String tipo = this.getCampo().getTipo();
    if (tipo == null || tipo.length() == 0) tipo = "ET1";
    if (tipo.charAt(0) != 'E') tipo = "E" + tipo;
    this.getCampo().setTipo(tipo);
    this.getCampo().getValori().clear();
    this.getCampo().addValore("1", "Si");
    this.getCampo().addValore("2", "No");
  }

  public String gestisciDaTrova(Vector params, DataColumn colWithValue, String conf, SqlManager manager) {
    return null;
  }

}
