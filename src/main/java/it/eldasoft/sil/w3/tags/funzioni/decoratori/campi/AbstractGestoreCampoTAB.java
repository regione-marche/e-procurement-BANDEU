/*
 * Created on 03/dic/08
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.funzioni.decoratori.campi;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.utils.spring.UtilitySpring;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Gestore di base per i tabellati dei CPV Questo gestore prevede la
 * ridefinizione di initGestore rispetto alla classe padre, ed utilizza il primo
 * campo della select come codice, mentre concatena il secondo ed il terzo
 * 
 * @author Stefano.Sabbadin
 */
abstract class AbstractGestoreCampoTAB extends GestoreCampoTabellatoGen {

  public AbstractGestoreCampoTAB() {
    super(false, "T5");
  }

  protected void initGestore() {
    SqlManager sql = (SqlManager) UtilitySpring.getBean("sqlManager",
        this.getPageContext(), SqlManager.class);
    this.getCampo().setTipo("E" + this.getTipoCampo());
    this.getCampo().getValori().clear();
    SqlSelect select = this.getSql();
    if (select != null
        && select.getSql() != null
        && select.getSql().length() > 0) {
      if (this.isAddNull()) this.getCampo().addValore("", "");
      try {
        List ret = sql.getListVector(select.getSql(), select.getParam());
        for (int i = 0; i < ret.size(); i++) {
          Vector row = (Vector) ret.get(i);
          String cod = row.get(0).toString();
          String descr = row.get(1).toString() + " - " + row.get(2).toString();

          if (descr != null && descr.length() > 80)
            descr = descr.substring(0, 80) + "...";
          this.getCampo().addValore(cod, descr);
        }
      } catch (SQLException e) {

      }
    }
  }

}
