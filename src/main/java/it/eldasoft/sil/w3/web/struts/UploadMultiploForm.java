package it.eldasoft.sil.w3.web.struts;

import it.eldasoft.gene.commons.web.struts.UploadFileForm;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * Form bean for a Struts application.
 * Users may access 1 field on this form:
 * <ul>
 * <li>testFile - [your comment here]
 * </ul>
 * @version 	1.0
 * @author      Luca Giacomazzo
 */
public class UploadMultiploForm extends UploadFileForm {

  private FormFile selFile   = null;
  private HashMap  formFiles = null;
  private int      index;

  /**
   * Constructor!
   */
  public UploadMultiploForm() {
    formFiles = new HashMap(100);
    index = 0;
  }

  /**
   * Get FormFiles
   * 
   * @return ArrayList
   */
  public HashMap getFormFiles() {
    return formFiles;
  }

  /**
   * Get getSelFile
   * 
   * @return FormFile
   */
  public FormFile getSelFile(int in) {
    return selFile;
  }

  /**
   * Set testFile
   * 
   * @param <code>FormFile</code>
   */
  public void setSelFile(int in, FormFile t) {
    try {
      this.selFile = t;
      setFormFiles(t, in);
      index++;
    } catch (Exception e) {
      System.out.println("Exception in setTestFile!" + e);
    }
  }

  public void setFormFiles(FormFile t, int i) {
    this.formFiles.put(new Long(i), t);
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    selFile = null;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();
    return errors;
  }

}