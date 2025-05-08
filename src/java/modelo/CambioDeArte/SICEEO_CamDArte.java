
package modelo.CambioDeArte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 20/01/2016, 09:18:06 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_CamDArte {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_CamDArte (Map datosReturn)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
    }
    
/*******************************************************************************/
/****************************** ÁREA PARA EL CONTROLADOR ***********************/
/*******************************************************************************/
    /**
     *
     * @param request Solicitud del servidor
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (HttpServletRequest request, String metodo)
    {
        this.r = new SICEEO_HttpServletRequest(request);
        
        if (metodo.equals("foAc"))
            formActivate ( r.gP("tipoUsuario"), r.gP("tblPrincipal_cicescini"),r.gP("tblPrincipal_idcct"),r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"));
        else if (metodo.equals("btGuCaDAr_AcPe"))
            btnGuardarCamDArte_ActionPerformed ( dm.vstrToArrMap(r.gPV("tblCamDArte"), "~", new String[]{"idalu","cvemat_oldvalue","cvemat_newvalue"}), 
                    r.gP("tblPrincipal_cicescini"), r.gP("txtUsuario"));
        else if (metodo.equals("btInAr_AcPe"))
            btnInsertArteCamDArte_ActionPerformed ( r.gP("tblPrincipal_cicescini"),r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"),
                    r.gP("tblPrincipal_grupo"), r.gP("txtUsuario"));
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void formActivate (String tipoUsuario, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo )
    {
        ArrayList<Map> tblCamDArte;
        try
        {
            qryIfx.conectar();
            dr.put("isTodoCalOf", qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, tblPrincipal_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM"));
            dr.put("tblCamDArte", tblCamDArte=qryIfx.camDArte (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            if (!tblCamDArte.isEmpty())
                dr.put("catArte", qryIfx.catArte(tblPrincipal_cicescini, tblPrincipal_grado, ""+tblCamDArte.get(0).get("cveprograma"), new String[]{"cvemat","desmat"}));
            else
                dr.put("catArte", new ArrayList<Map>());
            
            if ( tipoUsuario.equals("consulta") || tipoUsuario.equals("mesa") )
            {
                dr.put("btnInsertArte_Enabled", false);
                dr.put("btnGuardar_Enabled", false);
            }else{
                dr.put("btnInsertArte_Enabled", true);
                dr.put("btnGuardar_Enabled", true);
            }
                
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void btnInsertArteCamDArte_ActionPerformed (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String txtUsuario)
    {
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.insertarCamDArte (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnGuardarCamDArte_ActionPerformed (ArrayList<Map> tblCamDArte, String tblPrincipal_cicescini,String txtUsuario)
    {
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.guardarCamDArte (tblCamDArte, tblPrincipal_cicescini, txtUsuario);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
