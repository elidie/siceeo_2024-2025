
package modelo.Discap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 20/01/2016, 09:19:36 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Discap {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    
    public SICEEO_Discap (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.r = new SICEEO_HttpServletRequest(request);
        this.sesion = request.getSession(false);
    }
    
/*******************************************************************************/
/****************************** ÁREA PARA EL CONTROLADOR ***********************/
/*******************************************************************************/
    /**
     *
     * @param request Solicitud del servidor
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (String metodo)
    {
        if (metodo.equals("foAc"))
            formActivate ( r.gP("tipoUsuario"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"));
        else if (metodo.equals("btGu_AcPe"))
            btnGuardarDiscap_ActionPerformed ( dm.vstrToArrMap(r.gPV("tblDiscap"), "~", new String[]{"idalu","cvedefsuf_newvalue"}), r.gP("tblPrincipal_cicescini") );
            
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void formActivate (String tipoUsuario, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, 
            String tblPrincipal_grupo )
    {
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            
            if (qryIfx.isOficializado(tblPrincipal_idcct, tblPrincipal_cicescini,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(tblPrincipal_cicescini)+1), "31/10/"+(dm.toInt(tblPrincipal_cicescini)+1)))
                throw new SICEEO_Excepcion (-11,"INSCRIPCION_OFICIALIZADA");
            
            dr.put("catDiscap",qryIfx.catDiscap (tblPrincipal_cicescini, tblPrincipal_cveplan));
            dr.put("tblDiscap", qryIfx.discap (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            if (tipoUsuario.equals("consulta") || tipoUsuario.equals("mesa"))
                dr.put("btnGuardar_Enabled", false);
            else
                dr.put("btnGuardar_Enabled", true);
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoSec(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void btnGuardarDiscap_ActionPerformed (ArrayList<Map> tblDiscap, String tblPrincipal_cicescini)
    {
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.guardarDiscap (tblDiscap, tblPrincipal_cicescini);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
