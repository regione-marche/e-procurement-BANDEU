package it.eldasoft.sil.w3.db.dao.ibatis;

import it.eldasoft.gene.commons.web.spring.SqlMapClientDaoSupportBase;
import it.eldasoft.gene.db.domain.BlobFile;
import it.eldasoft.sil.w3.db.dao.AttachmentDao;


import java.util.HashMap;

import org.springframework.dao.DataAccessException;



public class SqlMapAttachmentDao extends SqlMapClientDaoSupportBase implements
    AttachmentDao {
  
  public BlobFile getAttachment(HashMap params) throws DataAccessException {
	    return (BlobFile) this.getSqlMapClientTemplate().queryForObject("getAttachment", params);
	  }

}
