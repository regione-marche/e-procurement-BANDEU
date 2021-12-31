package it.eldasoft.sil.w3.rest;

import it.eldasoft.sil.w3.bl.EldasoftSIMAPWSManager;
import it.eldasoft.utils.spring.UtilitySpring;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

@Path("/avvisoaggiudicazionesettorispeciali")
public class AvvisoAggiudicazioneSettoriSpeciali {

  static Logger              logger                = Logger.getLogger(AvvisoAggiudicazioneSettoriSpeciali.class);

  public static final String AUTHENTICATION_HEADER = "Authorization";

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response setAvvisoAggiudicazioneSettoriSpeciali(String xml, @Context HttpServletRequest request) {

    EldasoftSIMAPWSManager eldasoftSIMAPWSManager = (EldasoftSIMAPWSManager) UtilitySpring.getBean("eldasoftSIMAPWSManager",
        request.getSession().getServletContext(), EldasoftSIMAPWSManager.class);

    JSONObject output = new JSONObject();
    try {
      String auth = request.getHeader(AUTHENTICATION_HEADER);
      if (auth == null) {
        output.accumulate("esito", false);
        output.accumulate("messaggio", "Informazioni di autenticazione non presenti");
      } else {
        final String usernameAndPasswordEncoded = auth.replaceFirst("Basic" + " ", "");
        byte[] usernameAndPasswordDecoded = Base64.decodeBase64(usernameAndPasswordEncoded);
        String usernameAndPassword = new String(usernameAndPasswordDecoded, "UTF-8");
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        String xmlDecoded = new String(Base64.decodeBase64(xml));
        eldasoftSIMAPWSManager.inserisciAvvisoAggiudicazioneSettoriSpeciali(username, password, xmlDecoded);
        output.accumulate("esito", true);
      }
    } catch (Exception e) {
      output.accumulate("esito", false);
      output.accumulate("messaggio", e.getMessage());
    }

    return Response.status(200).entity(output).build();
  }

}
