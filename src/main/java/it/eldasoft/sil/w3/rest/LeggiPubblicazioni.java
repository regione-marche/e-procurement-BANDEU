package it.eldasoft.sil.w3.rest;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.utils.spring.UtilitySpring;
import it.eldasoft.utils.utility.UtilityDate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Path("/leggipubblicazioni")
public class LeggiPubblicazioni {

  static Logger                logger    = Logger.getLogger(LeggiPubblicazioni.class);

  private static final String  ERROR_MSG = "Si e' verificato il seguente errore: ";
  private static final String  NO_DATA   = "Nessun dato trovato";

  private static final boolean ESITO_OK  = true;
  private static final boolean ESITO_KO  = false;

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response leggiPubblicazioni(String in, @Context HttpServletRequest request) {
    return Response.status(200).entity(_leggiPubblicazioni(in, request)).build();
  }

  private JSONObject _leggiPubblicazioni(String in, HttpServletRequest request) {

    JSONObject output = new JSONObject();
    SqlManager sqlManager = (SqlManager) UtilitySpring.getBean("sqlManager", request.getSession().getServletContext(), SqlManager.class);

    String selectW3SIMAP = "select w3simap.id, w3simap.form, w3simap.notice_number_oj, w3simap.date_oj, w3simap.ted_links from w3simap where uuid = ?"
        + " union "
        + " select w3simap.id, w3simap.form, w3simap.notice_number_oj, w3simap.date_oj, w3simap.ted_links from w3simap, w3fs14 "
        + " where w3simap.id = w3fs14.id and w3fs14.id_rif in (select w3simap.id from w3simap where uuid = ?)";

    try {
      JSONArray jsonArrayOut = new JSONArray();
      JSONArray jsonArrayIn = new ObjectMapper().readValue(in, JSONArray.class);
      for (int u = 0; u < jsonArrayIn.size(); u++) {
        String uuid = (String) jsonArrayIn.get(u);
        List<?> datiW3SIMAP = sqlManager.getListVector(selectW3SIMAP, new Object[] { uuid, uuid });
        if (datiW3SIMAP != null && datiW3SIMAP.size() > 0) {
          for (int w = 0; w < datiW3SIMAP.size(); w++) {
            Long id = (Long) SqlManager.getValueFromVectorParam(datiW3SIMAP.get(w), 0).getValue();
            String form = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP.get(w), 1).getValue();
            String notice_number_oj = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP.get(w), 2).getValue();
            Date date_oj = (Date) SqlManager.getValueFromVectorParam(datiW3SIMAP.get(w), 3).getValue();
            String ted_links = (String) SqlManager.getValueFromVectorParam(datiW3SIMAP.get(w), 4).getValue();

            JSONObject pubblicazione = new JSONObject();
            pubblicazione.put("form", form);

            Date notice_date = (Date) sqlManager.getObject("select notice_date from v_w3simap where id = ?", new Object[] { id });
            if (notice_date != null) pubblicazione.put("notice_date", UtilityDate.convertiData(notice_date, UtilityDate.FORMATO_AAAAMMGG));
            if (notice_number_oj != null) pubblicazione.put("notice_number_oj", notice_number_oj);
            if (date_oj != null) pubblicazione.put("date_oj", UtilityDate.convertiData(date_oj, UtilityDate.FORMATO_AAAAMMGG));
            if (ted_links != null) pubblicazione.put("ted_links", ted_links);
            pubblicazione.put("uuid", uuid);
            jsonArrayOut.add(pubblicazione);
          }
        }
      }
      if (jsonArrayOut.size() > 0) {
        output.put("esito", ESITO_OK);
        output.put("pubblicazioni", jsonArrayOut);
      } else {
        output.put("esito", ESITO_KO);
        output.put("messaggio", NO_DATA);
      }

    } catch (JsonParseException e) {
      output.put("esito", ESITO_KO);
      output.put("messaggio", ERROR_MSG + e.getMessage());
      logger.error("Errore durante la richiesta", e);
    } catch (JsonMappingException e) {
      output.put("esito", ESITO_KO);
      output.put("messaggio", ERROR_MSG + e.getMessage());
      logger.error("Errore durante la richiesta", e);
    } catch (IOException e) {
      output.put("esito", ESITO_KO);
      output.put("messaggio", ERROR_MSG + e.getMessage());
      logger.error("Errore durante la richiesta", e);
    } catch (SQLException e) {
      output.put("esito", ESITO_KO);
      output.put("messaggio", ERROR_MSG + e.getMessage());
      logger.error("Errore durante la richiesta", e);
    }

    return output;
  }
}
