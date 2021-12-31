package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.CostantiGenerali;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.tags.decorators.campi.AbstractGestoreCampo;
import it.eldasoft.gene.tags.utils.UtilityTags;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public class GestoreCampoSECTION_FS4 extends AbstractGestoreCampo {

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

    Vector chiaviFS4 = new Vector();

    while (resourceEnumeration.hasMoreElements()) {
      String chiave = (String) resourceEnumeration.nextElement();
      if (chiave.startsWith("label.simap.fs4.")) chiaviFS4.add(chiave);
    }

    Enumeration enumerationFS4 = chiaviFS4.elements();
    List listFS4 = Collections.list(enumerationFS4);
    Collections.sort(listFS4);

    this.getCampo().addValore("", "");

    for (int i = 0; i < listFS4.size(); i++) {
      String chiave = (String) listFS4.get(i);
      String valore = UtilityTags.getResource(chiave, null, false);

      if (!"label.simap.fs4.II.1.3".equals(chiave) && !"label.simap.fs4.IV.1.1".equals(chiave)) {
        if (valore.length() > 100) valore = valore.substring(0, 100);
        this.getCampo().addValore(chiave.replace("label.simap.fs4.", ""), valore);
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
