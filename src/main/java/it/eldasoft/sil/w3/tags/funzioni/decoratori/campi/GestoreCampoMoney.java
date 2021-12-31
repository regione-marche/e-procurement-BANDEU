package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.db.datautils.DataColumn;
import it.eldasoft.gene.tags.decorators.campi.AbstractGestoreCampo;
import it.eldasoft.gene.tags.utils.UtilityTags;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

public class GestoreCampoMoney extends AbstractGestoreCampo {

  public String getValore(String valore) {

    if (valore == null || valore.length() == 0) return "";
    double value = new Double(valore).doubleValue();
    DecimalFormatSymbols sombols = new DecimalFormatSymbols();
    sombols.setDecimalSeparator('.');
    sombols.setGroupingSeparator(' ');
    DecimalFormat decFormat = new DecimalFormat("################0.00", sombols);

    return decFormat.format(value);
  }

  public String getValorePerVisualizzazione(String valore) {

    if (valore == null || valore.length() == 0) return "";
    double value = new Double(valore).doubleValue();
    //if (value == 0) return "";
    DecimalFormatSymbols sombols = new DecimalFormatSymbols();
    sombols.setDecimalSeparator(',');
    sombols.setGroupingSeparator('.');
    DecimalFormat decFormat = new DecimalFormat("###,###,###,##0.00", sombols);

    String ret = decFormat.format(value) + "&nbsp;&euro;";
    if (ret.charAt(0) == '-')
      ret = "<span class=\"importoNegativo\">&#8209;"
          + ret.substring(1)
          + "</span>";
    else
      ret = "<span class=\"importo\">" + ret + "</span>";
    return ret;
  }

  public String getValorePreUpdateDB(String valore) {
    return null;
  }

  public String preHTML(boolean visualizzazione, boolean abilitato) {
    return null;
  }

  public String getHTML(boolean visualizzazione, boolean abilitato) {
    String result = null;
    String tipoPagina = (String) this.getPageContext().getRequest().getAttribute(
        UtilityTags.REQUEST_VAR_TIPO_PAGINA);

    if (!UtilityTags.PAGINA_LISTA.equalsIgnoreCase(tipoPagina)) {
      if (visualizzazione && !abilitato) {
        if (!UtilityTags.SCHEDA_MODO_VISUALIZZA.equals((String) this.getPageContext().getRequest().getAttribute(
            UtilityTags.DEFAULT_HIDDEN_PARAMETRO_MODO))) {
          StringBuffer buf = new StringBuffer("<input type=\"text\" ");
          buf.append(UtilityTags.getHtmlAttrib("name",
              this.getCampo().getNome() + "edit"));
          buf.append(UtilityTags.getHtmlAttrib("id", this.getCampo().getNome()
              + "edit"));
          buf.append(UtilityTags.getHtmlAttrib("readOnly", "readOnly"));
          buf.append(UtilityTags.getHtmlAttrib("value",
              this.getCampo().getValue()));
          buf.append(UtilityTags.getHtmlAttrib("class", "importoNoEdit"));

          buf.append(UtilityTags.getHtmlAttrib("size",
              String.valueOf(this.getCampo().getLenForInput())));
          buf.append("/> &euro;");
          result = buf.toString();
        }
      }
    }
    return result;
  }

  public String postHTML(boolean visualizzazione, boolean abilitato) {
    String ret = null;
    
    if(this.campo.isVisibile())
      if (!visualizzazione) 
        ret = " &euro;";
    return ret;
  }

  public String getClasseEdit() {
    return null;
  }

  public String getClasseVisua() {
    return null;
  }

  /**
   * Inizializzo il tipo monei come decimale a 2 cifre
   */
  protected void initGestore() {
    this.getCampo().setTipo("F15.2");
    this.getCampo().setDominio("MONEY", this.getPageContext());
  }

  public String gestisciDaTrova(Vector params, DataColumn colWithValue,
      String conf, SqlManager manager) {
    return null;
  }

}
