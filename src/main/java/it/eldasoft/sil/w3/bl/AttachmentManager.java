package it.eldasoft.sil.w3.bl;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import it.eldasoft.gene.db.domain.BlobFile;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.sil.w3.db.dao.AttachmentDao;
import it.eldasoft.utils.utility.UtilityWeb;

import org.apache.log4j.Logger;

public class AttachmentManager {

  static Logger           logger = Logger.getLogger(AttachmentManager.class);

  private AttachmentDao attachmentDao;

  /**
   * 
   * @param fileAllegatoDao
   */
  public void setAttachmentDao(AttachmentDao attachmentDao) {
    this.attachmentDao = attachmentDao;
  }

  /**
   * 
   * @param params
   * @param response
   * @throws IOException
   * @throws GestoreException
   */
  public void downloadAttachment(String dignomdoc, HashMap params, HttpServletResponse response)
      throws IOException, GestoreException {
    if (logger.isDebugEnabled())
      logger.debug("downloadAttachment: inizio metodo");

    BlobFile attachmentBlob = this.attachmentDao.getAttachment(params);
    UtilityWeb.download(dignomdoc, attachmentBlob.getStream(), response);

    if (logger.isDebugEnabled())
      logger.debug("downloadAttachment: fine metodo");
  }

}
