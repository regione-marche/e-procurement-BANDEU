package it.eldasoft.sil.w3.utils;

import java.sql.SQLException;

import it.eldasoft.gene.bl.SqlManager;
import it.eldasoft.gene.commons.web.domain.ProfiloUtente;
import it.eldasoft.gene.web.struts.tags.gestori.GestoreException;
import it.eldasoft.utils.metadata.cache.DizionarioCampi;
import it.eldasoft.utils.metadata.domain.Campo;

public class UtilityPermessi {

  /**
   * Inserisci permessi (principale) per l'utente proprietario.
   * 
   * @param syscon
   * @param tblname
   * @param clm1name
   * @param clm1value
   * @param sqlManager
   * @throws SQLException
   */
  public static void inserisciPermessi(Long syscon, String tblname, String clm1name, Object clm1value, SqlManager sqlManager)
      throws GestoreException {

    try {
      String insertPERM = null;

      if (clm1value instanceof String) {
        insertPERM = "insert into perm (idperm, guperm, syscon, r, w, x, tblname, clm1name, clm1value_vc, rwx) values (?,?,?,?,?,?,?,?,?,?)";
      } else if (clm1value instanceof Long) {
        insertPERM = "insert into perm (idperm, guperm, syscon, r, w, x, tblname, clm1name, clm1value_nu, rwx) values (?,?,?,?,?,?,?,?,?,?)";
      }

      if (insertPERM != null) {
        Object[] obj = new Object[10];
        obj[0] = _getNextIdPerm(sqlManager);
        obj[1] = "USR";
        obj[2] = syscon;
        obj[3] = new Long(1);
        obj[4] = new Long(1);
        obj[5] = new Long(1);
        obj[6] = tblname;
        obj[7] = clm1name;
        obj[8] = clm1value;
        obj[9] = new Long(7);

        sqlManager.update(insertPERM, obj);
      }
    } catch (SQLException e) {
      throw new GestoreException("Errore nell'inserimento dei permessi", null, e);
    }
  }

  /**
   * Costruisce il filtro da aggiungere alla query.
   * 
   * @param profilo
   * @param tblname
   * @param clm1name
   * @param sqlManager
   * @return
   * @throws SQLException
   */
  public static String getFiltroPermessi(ProfiloUtente profilo, String tblname, String clm1name, SqlManager sqlManager) throws SQLException {
    return _getFiltroPermessi(new Long(profilo.getId()), profilo.getAbilitazioneStd(), tblname, clm1name, sqlManager);
  }

  /**
   * Costruisce il filtro da aggiungere alla query.
   * 
   * @param syscon
   * @param sysabg
   * @param tblname
   * @param clm1name
   * @param slqManager
   * @return
   * @throws SQLException
   */
  private static String _getFiltroPermessi(Long syscon, String sysab3, String tblname, String clm1name, SqlManager sqlManager)
      throws SQLException {

    String filtroPermessi = null;

    if ("U".equals(sysab3)) {
      Campo c = DizionarioCampi.getInstance().getCampoByNomeFisico(tblname + "." + clm1name);

      String clm1value_perm = "";
      if (c.getTipoColonna() == 0) {
        clm1value_perm = "clm1value_vc";
      } else if (c.getTipoColonna() == 2) {
        clm1value_perm = "clm1value_nu";
      }

      filtroPermessi = " exists (select v_perm.tblname from v_perm where ";
      filtroPermessi += " v_perm.syscon = " + syscon.toString();
      filtroPermessi += " and v_perm.tblname = '" + tblname + "'";
      filtroPermessi += " and v_perm.clm1name = '" + clm1name + "'";
      filtroPermessi += " and v_perm." + clm1value_perm + " = " + tblname + "." + clm1name + ")";
    }

    return filtroPermessi;

  }

  /**
   * Restituisce il valore massimo di RWX.
   * 
   * @param profilo
   * @param tblname
   * @param clm1name
   * @param clm1value
   * @param sqlManager
   * @return
   * @throws SQLException
   */
  public static Long getRWXPermessi(ProfiloUtente profilo, String tblname, String clm1name, Object clm1value, SqlManager sqlManager)
      throws SQLException {
    return _getRWXPermessi(new Long(profilo.getId()), profilo.getAbilitazioneStd(), tblname, clm1name, clm1value, sqlManager);
  }

  /**
   * Restituisce il valore massimo di RWX.
   * 
   * @param syscon
   * @param sysab3
   * @param tblname
   * @param clm1name
   * @param clm1value
   * @param sqlManager
   * @return
   * @throws SQLException
   */
  private static Long _getRWXPermessi(Long syscon, String sysab3, String tblname, String clm1name, Object clm1value, SqlManager sqlManager)
      throws SQLException {

    Long rwx = new Long(0);

    if ("A".equals(sysab3)) {
      rwx = new Long(7);
    } else {
      
      String selectV_PERM = null;

      Campo c = DizionarioCampi.getInstance().getCampoByNomeFisico(tblname + "." + clm1name);
      if (c.getTipoColonna() == 0) {
        selectV_PERM = "select max(rwx) from v_perm where syscon = ? and tblname = ? and clm1name = ? and clm1value_vc = ?";
      } else if (c.getTipoColonna() == 2) {
        selectV_PERM = "select max(rwx) from v_perm where syscon = ? and tblname = ? and clm1name = ? and clm1value_nu = ?";
      }

      if (selectV_PERM != null) {
        rwx = (Long) sqlManager.getObject(selectV_PERM, new Object[] { syscon, tblname, clm1name, clm1value });
      }
    }

    return rwx;

  }

  /**
   * Calcolo campo chiave tabella PERM.
   * 
   * @param sqlManager
   * @return
   * @throws SQLException
   */
  private static Long _getNextIdPerm(SqlManager sqlManager) throws SQLException {
    Long nextIdPerm = (Long) sqlManager.getObject("select max(idperm) from perm", new Object[] {});
    if (nextIdPerm == null) nextIdPerm = new Long(0);
    nextIdPerm = new Long(nextIdPerm.longValue() + 1);
    return nextIdPerm;
  }

}
