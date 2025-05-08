
package modelo.CambioDeAes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 20/01/2016, 09:17:27 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_CamDAes {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_CamDAes (Map datosReturn)
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
        else if (metodo.equals("btGuCaDTa_AcPe"))
            btnGuardarCamDAes_ActionPerformed ( dm.vstrToArrMap(r.gPV("tblCamDAes"), "~", new String[]{"idalu","cvemat_oldvalue","cvemat_newvalue"}), r.gP("tblPrincipal_cicescini"));
        else if (metodo.equals("btInAe_AcPe"))
            btnInsertAesCamDAes_ActionPerformed ( r.gP("tblPrincipal_cicescini"),r.gP("tblPrincipal_idcct"),r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"), 
                    r.gP("txtUsuario"));
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void formActivate (String tipoUsuario, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo )
    {
        ArrayList<Map> tblCamDAes;
        try
        {
            qryIfx.conectar();
            dr.put("tblCamDAes", tblCamDAes=qryIfx.camDAes (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            if (!tblCamDAes.isEmpty())
                dr.put("catAes", qryIfx.catAes(tblPrincipal_cicescini, tblPrincipal_grado, ""+tblCamDAes.get(0).get("cveprograma"), new String[]{"cvemat","desmat"}));
            else
                dr.put("catAes", new ArrayList<Map>());
            
            if ( tipoUsuario.equals("consulta") || tipoUsuario.equals("mesa") )
            {
                dr.put("btnInsertAes_Enabled", false);
                dr.put("btnGuardar_Enabled", false);
            }else{
                dr.put("btnInsertAes_Enabled", true);
                dr.put("btnGuardar_Enabled", true);
            }
                
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void btnInsertAesCamDAes_ActionPerformed (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String txtUsuario)
    {
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.insertarCamDAes (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnGuardarCamDAes_ActionPerformed (ArrayList<Map> tblCamDAes, String tblPrincipal_cicescini)
    {
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.guardarCamDAes (tblCamDAes, tblPrincipal_cicescini);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
