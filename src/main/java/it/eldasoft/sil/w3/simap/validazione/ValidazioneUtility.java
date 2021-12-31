package it.eldasoft.sil.w3.simap.validazione;

import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.utils.metadata.cache.DizionarioCampi;
import it.eldasoft.utils.metadata.domain.Campo;
import it.eldasoft.utils.utility.UtilityDate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

public class ValidazioneUtility {

  static Logger logger = Logger.getLogger(ValidazioneUtility.class);

  /**
   * Aggiunge un consiglio alla lista dei messaggi
   * 
   * @param listaControlli
   * @param entita
   * @param campo
   * @param pagina
   */
  public static void addCampoConsigliato(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "E' consigliata la compilazione del campo";
    listaControlli.add(((Object) (new Object[] { "W", pagina, descrizione, messaggio })));
  }

  /**
   * Aggiunge un messaggio bloccante alla listaControlli
   * 
   * @param listaControlli
   * @param entita
   * @param campo
   * @param pagina
   */
  public static void addCampoObbligatorio(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il campo &egrave; obbligatorio";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  /**
   * 
   * @param listaControlli
   * @param entita
   * @param campo
   * @param pagina
   */
  public static void addNotForeseenF01PriOnly(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il presente avviso &egrave; soltanto un avviso di preinformazione: il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF04PriOnly(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il presente avviso &egrave; soltanto un avviso di preinformazione: il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF01PriCallCompetition(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il presente avviso &egrave; un avviso di indizione di gara: il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF07(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il presente avviso non &egrave; un avviso di indizione di gara: il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF04PriCallCompetition(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il presente avviso &egrave; un avviso di indizione di gara: il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF01PriReducingTimeLimits(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Lo scopo del presente avviso &egrave; ridurre i termini di ricezione delle offerte: il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF02(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF04PriReducingTimeLimits(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Lo scopo del presente avviso &egrave; ridurre i termini di ricezione delle offerte: il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  public static void addNotForeseenF05(List<Object> listaControlli, String entita, String campo, String pagina) {
    String descrizione = getDescrizioneCampo(entita, campo);
    String messaggio = "Il campo non deve essere valorizzato";
    listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
  }

  /**
   * Aggiunge un messaggio di avviso alla listaControlli
   * 
   * @param listaControlli
   * @param messaggio
   */
  public static void addAvviso(List<Object> listaControlli, String entita, String campo, String tipo, String pagina, String messaggio) {
    String descrizione = getDescrizioneCampo(entita, campo);
    listaControlli.add(((Object) (new Object[] { tipo, pagina, descrizione, messaggio })));

  }

  /**
   * Restituisce la descrizione WEB del campo
   * 
   * @param entita
   * @param campo
   * @return
   */
  public static String getDescrizioneCampo(String entita, String campo) {
    String descrizione = "";
    try {
      Campo c = DizionarioCampi.getInstance().getCampoByNomeFisico(entita + "." + campo);
      descrizione = c.getDescrizioneWEB();
    } catch (Throwable t) {

    }

    return descrizione;
  }

  /**
   * Restituisce la rappresentazione in stringa dell'importo passato
   * 
   * @param importo
   * @return
   */
  public static String importoToStringa(Double importo) {

    String ret = "";

    double valore = importo.doubleValue();
    if (valore != 0) {
      DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
      simbolo.setDecimalSeparator(',');
      simbolo.setGroupingSeparator('.');
      DecimalFormat decFormat = new DecimalFormat("###,###,###,##0.00", simbolo);
      ret = decFormat.format(valore) + "&nbsp;&euro;";
    } else {
      ret = "0.00&nbsp;&euro;";
    }

    return ret;

  }

  /**
   * Utilizzata per settare il tipo T ossia il titolo all'interno di una tabella
   * 
   * @param listaControlli
   * @param pagina
   */
  public static void setTitolo(List<Object> listaControlli, String titolo) {
    listaControlli.add(((Object) (new Object[] { "T", titolo, "", "" })));
  }

  /**
   * Confronto tra due date (Date)
   * 
   * @param data1
   * @param parametro
   * @param data2
   * @param entita2
   * @param campo2
   * @return
   */
  public static String getMessaggioConfrontoDate(Date data1, String parametro, Date data2, String entita2, String campo2) {

    String descrizione = "La data indicata (" + UtilityDate.convertiData(data1, UtilityDate.FORMATO_GG_MM_AAAA) + ") deve essere ";

    if ("<".equals(parametro)) descrizione += "precedente";
    if ("<=".equals(parametro)) descrizione += "precedente o uguale";
    if ("=".equals(parametro)) descrizione += "uguale";
    if (">".equals(parametro)) descrizione += "successiva";
    if (">=".equals(parametro)) descrizione += "successiva o uguale";

    descrizione += " alla \""
        + getDescrizioneCampo(entita2, campo2)
        + "\" ("
        + UtilityDate.convertiData(data2, UtilityDate.FORMATO_GG_MM_AAAA)
        + ")";

    return descrizione;

  }

  /**
   * Confronto tra due importi (Double)
   * 
   * @param importo_confronto1
   * @param parametro_confronto
   * @param importo_confronto2
   * @param entita_confronto2
   * @param campo_confronto2
   * @return
   */
  public static String getMessaggioConfrontoImporti(Double importo1, String parametro, Double importo2, String entita2, String campo2) {
    String descrizione = "L\'importo digitato (" + importoToStringa(importo1) + ") deve essere ";

    if ("<".equals(parametro)) descrizione += "inferiore";
    if ("<=".equals(parametro)) descrizione += "inferiore o uguale";
    if ("=".equals(parametro)) descrizione += "uguale";
    if (">".equals(parametro)) descrizione += "superiore";
    if (">=".equals(parametro)) descrizione += "superiore o uguale";

    descrizione += " al valore del campo \"" + getDescrizioneCampo(entita2, campo2) + "\" (" + importoToStringa(importo2) + ")";
    return descrizione;
  }

  /**
   * Confronto tra due numeri (Long)
   * 
   * @param long_confronto1
   * @param parametro_confronto
   * @param long_confronto2
   * @param entita_confronto2
   * @param campo_confronto2
   * @return
   */
  public static String getMessaggioConfrontoNumeri(Long numero1, String parametro, Long numero2, String entita2, String campo2) {
    String descrizione = "Il valore digitato (" + numero1.longValue() + ") deve essere ";

    if ("<".equals(parametro)) descrizione += "inferiore";
    if ("<=".equals(parametro)) descrizione += "inferiore o uguale";
    if ("=".equals(parametro)) descrizione += "uguale";
    if (">".equals(parametro)) descrizione += "superiore";
    if (">=".equals(parametro)) descrizione += "superiore o uguale";

    descrizione += " al valore del campo \"" + getDescrizioneCampo(entita2, campo2) + "\" (" + numero2.longValue() + ")";

    return descrizione;
  }

  /**
   * Validazione Numero OJS
   * 
   * @param numberOJ
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneNumberOJ(String numberOJ, String entita, String campo, String pagina, List<Object> listaControlli)
      throws GestoreException {

    try {
      // Pattern XSD --> (19|20)\d{2}/S \d{3}-\d{6}
      // The notice number pattern must be (19|20)yy/S sss-nnnnnn, with yy for
      // the year, sss for the OJS number between 001 and 259, and nnnnnn a six
      // digits number.
      String regex = "20[0-9]{2}/S (0[0-9][1-9]|0[1-9][0-9]|1[0-9][0-9]|2[0-5][0-9])-[0-9]{6}";
      String messaggioNumberOJ = "Il valore non rispetta il formato previsto yyyy/S sss-nnnnnn dove yyyy e' l'anno, sss e' il numero di pubblicazione che deve iniziare con 001, nnnnnn e' un progressivo di sei cifre. <br>Esempio:<br>[2020/S 123-123456]";
      if (numberOJ != null) {
        if (!numberOJ.matches(regex)) {
          addAvviso(listaControlli, entita, campo, "E", pagina, messaggioNumberOJ);
        }
      }
    } catch (PatternSyntaxException pse) {
      throw new GestoreException("Errore in validazione del numero OJS", "validazioneNumberOJ", pse);
    }
  }

  /**
   * Validazione del numero di telefono
   * 
   * @param phone
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazionePhoneFax(String phoneFax, String entita, String campo, String pagina, List<Object> listaControlli)
      throws GestoreException {

    try {
      // Pattern XSD -->
      // (\+\d{1,3}\s\d+(\-\d+)*((\s)?/(\s)?(\+\d{1,3}\s)?\d+(\-\d+)*)*)
      String regex = "(\\+[0-9]{1,3} [0-9]+(\\-[0-9]+)*(( )?/( )?(\\+[0-9]{1,3} )?[0-9]+(\\-[0-9]+)*)*)";
      String messaggio = "Il valore non rispetta il formato previsto.";
      messaggio += "<br>Esempi:<br>";
      messaggio += "+39 12345678<br>";
      messaggio += "+39 12345678-0001<br>";

      if (phoneFax != null && !"".equals(phoneFax)) {
        if (!phoneFax.matches(regex)) {
          addAvviso(listaControlli, entita, campo, "E", pagina, messaggio);
        }
        validazioneS100(phoneFax, entita, campo, pagina, listaControlli);
      }
    } catch (PatternSyntaxException pse) {
      throw new GestoreException("Errore in validazione del numero OJS", "validazionePhoneFax", pse);
    }
  }

  /**
   * Validazione email
   * 
   * @param email
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneEmail(String email, String entita, String campo, String pagina, List<Object> listaControlli)
      throws GestoreException {

    try {
      // Pattern XSD --> [^@]+@[^@]+\.[^@]+([,]\s[^@]+@[^@]+\.[^@]+)*
      String regex = "[^@]+@[^@]+[.][^@]+([,] [^@]+@[^@]+[.][^@]+)*";
      String messaggio = "Il valore non rispetta il formato previsto.";
      if (email != null && !"".equals(email)) {
        if (!email.matches(regex)) {
          addAvviso(listaControlli, entita, campo, "E", pagina, messaggio);
        }
        if (email.indexOf(" ") >= 0) {
          addAvviso(listaControlli, entita, campo, "W", pagina, messaggio);
        }
        validazioneS250(email, entita, campo, pagina, listaControlli);
      }
    } catch (PatternSyntaxException pse) {
      throw new GestoreException("Errore in validazione del numero OJS", "validazioneEmail", pse);
    }
  }

  /**
   * Validazione URL.
   * 
   * @param url
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   * @throws GestoreException
   */
  public static void validazioneURL(String url, String entita, String campo, String pagina, List<Object> listaControlli)
      throws GestoreException {

    try {
      String regex = "(((http|HTTP|https|HTTPS|ftp|FPT|ftps|FTPS|sftp|SFTP)://)|((w|W){3}(\\d)?\\.))[\\w\\?!\\./:;\\-_=#+*%@&quot;\\(\\)&amp;]+";
      String messaggio = "Il valore non rispetta il formato previsto.";
      messaggio += "<br>E' possibile inserire un solo indirizzo URL che inizi con 'ftp://', 'ftps://', 'sftp://', 'http://', 'https://' o 'www.' e non contenga spazi o virgole";

      if (url != null && !"".equals(url)) {
        if (!url.matches(regex)) {
          addAvviso(listaControlli, entita, campo, "E", pagina, messaggio);
        }
        validazioneS250(url, entita, campo, pagina, listaControlli);
      }

    } catch (PatternSyntaxException pse) {
      throw new GestoreException("Errore in validazione dell'indirizzo URL", "validazioneURL", pse);
    }

  }

  /**
   * Confronto tra due date di entita' diverse. Confronta ed aggiunge alla lista
   * dei controlli automaticamente.
   * 
   * @param data1
   * @param campo1
   * @param parametro
   * @param data2
   * @param campo2
   * @param entita
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneConfrontoDate(Date data1, String entita1, String campo1, String parametro, Date data2, String entita2,
      String campo2, String pagina, List<Object> listaControlli) {

    if (data1 != null && data2 != null) {
      boolean valid = false;

      if ("<".equals(parametro)) valid = (data1.getTime() < data2.getTime());
      if ("<=".equals(parametro)) valid = (data1.getTime() <= data2.getTime());
      if ("=".equals(parametro)) valid = (data1.getTime() == data2.getTime());
      if (">".equals(parametro)) valid = (data1.getTime() > data2.getTime());
      if (">=".equals(parametro)) valid = (data1.getTime() >= data2.getTime());

      if (!valid) {
        String messaggio = getMessaggioConfrontoDate(data1, parametro, data2, entita2, campo2);
        addAvviso(listaControlli, entita1, campo1, "E", pagina, messaggio);
      }
    }
  }

  /**
   * Controllo dimensione massima stringa.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   * @param l
   */
  public static void validazioneS(String in, String entita, String campo, String pagina, List<Object> listaControlli, int l) {
    if (in != null && in.length() > l) {
      String messaggio = "Non sono ammessi pi&ugrave; di " + l + " caratteri.";
      addAvviso(listaControlli, entita, campo, "E", pagina, messaggio);
    }
  }

  /**
   * Controllo dimensione massima stringa: 400 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS400(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 400);
  }

  /**
   * Controllo dimensione massima stringa: 20 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS20(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 20);
  }

  /**
   * Controllo dimensione massima stringa: 300 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS300(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 300);
  }

  /**
   * Controllo dimensione massima stringa: 500 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS500(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 500);
  }

  /**
   * Controllo dimensione massima stringa: 200 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS200(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 200);
  }

  /**
   * Controllo dimensione massima stringa: 250 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS250(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 250);
  }

  /**
   * Controllo dimensione massima stringa: 100 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS100(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 100);
  }

  /**
   * Controllo dimensione massima stringa: 1000 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS1000(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 1000);
  }

  /**
   * Controllo dimensione massima stringa: 1500 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS1500(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 1500);
  }

  /**
   * Controllo dimensione massima stringa: 2500 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS2500(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 2500);
  }

  /**
   * Controllo dimensione massima stringa: 4000 caratteri.
   * 
   * @param in
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneS4000(String in, String entita, String campo, String pagina, List<Object> listaControlli) {
    validazioneS(in, entita, campo, pagina, listaControlli, 4000);
  }

  /**
   * Controllo degli importi (puo' essere zero).
   * 
   * @param cost
   * @param entita
   * @param campo
   * @param pagina
   * @param listaControlli
   */
  public static void validazioneCost(Double cost, String entita, String campo, String pagina, List<Object> listaControlli) {
    if (cost != null && cost.doubleValue() <= 0) {
      String messaggio = "L'importo indicato deve essere maggiore di zero.";
      addAvviso(listaControlli, entita, campo, "E", pagina, messaggio);
    }
  }

  /**
   * Controllo della corrispondenza del codice CPV con il tipo di appalto R388:
   * S2-01-02: main CPV code inconsistent with the type of contract:
   * <ul>
   * <li>Supplies corresponds to CPV code starting from 0 to 44 and with 48</li>
   * <li>Works with 45</li>
   * <li>Services from 49 to 98</li>
   * </ul>
   * 
   * Tabellato W3z40
   * <ul>
   * <li>SERV Servizi</li>
   * <li>SUPP Forniture</li>
   * <li>WORK Lavori</li>
   * </ul>
   * 
   * @param listaControlli
   * @param pagina
   * @param cpv
   * @param type_contract
   * @param entita
   * @param campo
   */
  public static void validazioneCorrispondenzaCPV(List<Object> listaControlli, String pagina, String cpv, String type_contract,
      String entita, String campo) {
    if (cpv != null && type_contract != null) {
      boolean _cpvok = false;
      String _d = cpv.substring(0, 2);
      int divisione = Integer.parseInt(_d);

      if (divisione >= 0 && divisione <= 44 && "SUPP".equals(type_contract)) {
        _cpvok = true;
      } else if (divisione == 48 && "SUPP".equals(type_contract)) {
        _cpvok = true;
      } else if (divisione == 45 && "WORK".equals(type_contract)) {
        _cpvok = true;
      } else if (divisione >= 49 && divisione <= 98 && "SERV".equals(type_contract)) {
        _cpvok = true;
      }

      if (_cpvok == false) {
        String descrizione = ValidazioneUtility.getDescrizioneCampo(entita, campo);
        String messaggio = "Il codice CPV indicato non e' consistente con il tipo di appalto. Per le forniture i primi due caratteri del codice CPV devono essere compresi tra '00' e '44' oppure devono essere uguali a '48', per i lavori i primi due caratteri del codice CPV devono essere uguali a '45', per i servizi i primi due caratteri del codice CPV devono essere compresi tra '49' e '98'";
        listaControlli.add(((Object) (new Object[] { "E", pagina, descrizione, messaggio })));
      }
    }
  }

}