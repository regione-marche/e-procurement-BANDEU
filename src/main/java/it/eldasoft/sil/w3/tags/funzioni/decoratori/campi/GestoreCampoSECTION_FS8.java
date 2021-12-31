package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.tags.decorators.campi.AbstractGestoreCampo;
import it.eldasoft.gene.tags.utils.UtilityTags;

public class GestoreCampoSECTION_FS8 extends AbstractGestoreCampo {



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

    String resource = CostantiGenerali.RESOURCE_BUNDLE_APPL_GENERALE;
    Enumeration resourceEnumeration = ResourceBundle.getBundle(resource).getKeys();

    Vector chiaviFS8 = new Vector();
    
    while (resourceEnumeration.hasMoreElements()) {
      String chiave = (String) resourceEnumeration.nextElement();
      if (chiave.startsWith("label.simap.fs8.")) chiaviFS8.add(chiave);
    }
    
    Enumeration enumerationFS8 = chiaviFS8.elements();
    List listFS8 = Collections.list(enumerationFS8);
    Collections.sort(listFS8);
    
    this.getCampo().addValore("", "");
    
    for (int i = 0; i < listFS8.size(); i++) {
      String chiave = (String) listFS8.get(i);
      String valore = UtilityTags.getResource(chiave, null, false);
      
      if (!"label.simap.fs8.II.1.3".equals(chiave)) {
        if (valore.length() > 100) valore = valore.substring(0, 100);
        this.getCampo().addValore(chiave.replace("label.simap.fs8.", ""), valore);
      }
    }    
    

    

  }

  public String postHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

  public String preHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

}
