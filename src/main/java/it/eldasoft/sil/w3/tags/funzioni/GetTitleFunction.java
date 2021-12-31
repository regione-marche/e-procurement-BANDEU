/*
 * Created on 20/ott/08
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.eldasoft.sil.w3.tags.funzioni;

import javax.servlet.jsp.PageContext;

import it.eldasoft.gene.tags.functions.AbstractGetTitleFunction;

public class GetTitleFunction extends AbstractGetTitleFunction {

  protected String getTitleInserimento(PageContext pageContext, String table) {

    return null;
  }

  protected String getTitleModifica(PageContext pageContext, String table,
      String keys) {

    return null;
  }

  public String[] initFunction() {
    return new String[] {
        "W3AMMI|Nuova amministrazione aggiudicatrice|Amministrazione aggiudicatrice - {0}"
            + "||select uffint.nomein from uffint, w3ammi where uffint.codein = w3ammi.codein and w3ammi.codamm = #W3AMMI.CODAMM#",
        "W3FS1|Nuovo avviso di preinformazione [2014/24/EU]|Avviso di preinformazione [2014/24/EU] - {0}"
            + "||select w3fs1s2.title_contract from w3fs1s2 where w3fs1s2.id = #W3FS1.ID# and w3fs1s2.num = 1",
        "W3ANNEXB|Nuovo lotto|Lotto n. {0} - {1}"
            + "||select w3annexb.lotnum, w3annexb.title from w3annexb where w3annexb.id = #W3ANNEXB.ID# and w3annexb.num = #W3ANNEXB.NUM#",
        "W3FS2|Nuovo bando di gara [2014/24/EU]|Bando di gara [2014/24/EU] - {0}"
            + "||select w3fs2.title_contract from w3fs2 where w3fs2.id = #W3FS2.ID#",
        "W3FS3|Nuovo avviso di aggiudicazione [2014/24/EU]|Avviso di aggiudicazione [2014/24/EU] - {0}"
            + "||select w3fs3.title_contract from w3fs3 where w3fs3.id = #W3FS3.ID#",
        "W3FS4|Nuovo avviso periodico indicativo [2014/25/EU]|Avviso periodico indicativo [2014/25/EU] - {0}"
            + "||select w3fs4s2.title_contract from w3fs4s2 where w3fs4s2.id = #W3FS4.ID# and w3fs4s2.num = 1",
        "W3FS5|Nuovo bando di gara [2014/25/EU]|Bando di gara [2014/25/EU] - {0}"
            + "||select w3fs5.title_contract from w3fs5 where w3fs5.id = #W3FS5.ID#",
        "W3FS6|Nuovo avviso di aggiudicazione [2014/25/EU]|Avviso di aggiudicazione [2014/25/EU] - {0}"
            + "||select w3fs6.title_contract from w3fs6 where w3fs6.id = #W3FS6.ID#",
        "W3FS7|Sistema di qualificazione [2014/25/EU]|Sistema di qualificazione [2014/25/EU] - {0}"
            + "||select w3fs7.title_contract from w3fs7 where w3fs7.id = #W3FS7.ID#",
        "W3FS8|Nuovo avviso relativo al profilo di committente|Avviso relativo al profilo di committente - {0}"
            + "||select w3fs8s2.title_contract from w3fs8s2 where w3fs8s2.id = #W3FS8.ID# and w3fs8s2.num = 1",
        "W3FS9|Nuovo bando di gara semplificato [2014/24/EU]|Bando di gara semplificato [2014/24/EU] - {0}"
            + "||select w3fs9.title_contract from w3fs9 where w3fs9.id = #W3FS9.ID#",
        "W3FS3AWARD|Nuova aggiudicazione di appalto|Aggiudicazione di appalto - {0}"
            + "||select w3fs3award.contract_title from w3fs3award where "
            + "w3fs3award.id = #W3FS3AWARD.ID# and w3fs3award.item = #W3FS3AWARD.ITEM#",
        "W3FS6AWARD|Nuova aggiudicazione di appalto|Aggiudicazione di appalto - {0}"
            + "||select w3fs6award.contract_title from w3fs6award where "
            + "w3fs6award.id = #W3FS6AWARD.ID# and w3fs6award.item = #W3FS6AWARD.ITEM#",            
        "W3FS14|Nuova rettifica|Rettifica - {0}"
            + "||select v_w3simap.title_contract from v_w3simap, w3fs14 where "
            + " v_w3simap.id = w3fs14.id_rif and w3fs14.id = #W3FS14.ID# ",
        "W3FS20|Nuovo avviso di modifica durante il periodo di validità|Avviso di modifica durante il periodo di validità - {0}"
            + "||select w3fs20.title_contract from w3fs20 where w3fs20.id = #W3FS20.ID#",            
        "W3SIMAPEMAIL|Nuovo invio SIMAP|Invio SIMAP n. {0}"
            + "||select num from w3simapemail where id = #W3SIMAPEMAIL.ID# and num = #W3SIMAPEMAIL.NUM# ",
        "GRP|Nuovo gruppo|Gruppo n. {0} - {1}"
            + "||select idgrp, descgrp from grp where idgrp = #GRP.IDGRP#",
        "UNIT|Nuova unit&agrave; organizzativa|Unit&agrave; organizzativa n. {0} - {1}"
            + "||select idunit, descunit from unit where idunit = #UNIT.IDUNIT#"};

  }

}
