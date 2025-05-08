package modelo.Configuraciones;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.SICEEO_SessionManager;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/**
 *
 * Creado el : 10/07/2015, 07:15:11 PM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

public class SICEEO_Configuraciones {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_Configuraciones (Map datosReturn)
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
        HttpSession session = request.getSession(false);
        
        if(request.getParameter("txtUsuario")!=null && ( request.getParameter("txtUsuario").equals("IVALLE") && session.getAttribute("userName").equals("IVALLE") 
                            || request.getParameter("txtUsuario").equals("ELYLOPEZ") && session.getAttribute("userName").equals("ELYLOPEZ") 
                            || request.getParameter("txtUsuario").equals("MRAMIREZ") && session.getAttribute("userName").equals("MRAMIREZ") )  )
        {
            if (metodo.equals("geSe"))
                getSesiones (request );
            else if (metodo.equals("seEsMa"))
                setEstatusMantenimiento (request );
            else if (metodo.equals("geEsMa"))
                getEstatusMantenimiento ();
            else if (metodo.equals("ceSe"))
                cerrarSesiones (request);
            /*else if (metodo.equals("piWeSe"))
                pingWebService (request.getParameter("txtUsuario"));*/
        }else {
            this.dr.put("returnCase", -10); 
            mensaje.Configuraciones("USUARIO_REESTRINGIDO", "", "", this.dr);
        }
        
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    public void getSesiones (HttpServletRequest request)
    {
        
        HttpSession session = request.getSession(false);
        SICEEO_SessionManager sessionManager = (SICEEO_SessionManager)session.getServletContext().getAttribute("activeUsers");
        if (sessionManager!=null)
            this.dr.put("activeUsers", sessionManager.getActiveUsers());
        
    }
    
    public void setEstatusMantenimiento (HttpServletRequest request)
    {
        boolean mantenimiento = request.getParameter("casoMantenimiento").equals("Activar mantenimiento");
        try 
        {
            qryIfx.conectar();
            qryIfx.setEstatusMantenimiento(mantenimiento);
            HttpSession session = request.getSession(false);
            SICEEO_SessionManager sessionManager = (SICEEO_SessionManager)session.getServletContext().getAttribute("activeUsers");
            if (sessionManager!=null){
                if (mantenimiento)
                    sessionManager.invalidateAllSessions ();
                this.dr.put("activeUsers", sessionManager.getActiveUsers());
            }
            dr.put("btnActivDesactivMantenimiento_Text",  (mantenimiento?"Desactivar mantenimiento":"Activar mantenimiento") );
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ 
            this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); 
        }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void getEstatusMantenimiento ()
    {
        String estatus;
        dr.put("returnCase", 1);
        
        try 
        {
            qryIfx.conectar();
            estatus = qryIfx.getEstatusMantenimiento ();
            dr.put("btnActivDesactivMantenimiento_Visible", true);
            if (estatus.equals("MANTENIMIENTO"))
                dr.put("btnActivDesactivMantenimiento_Visible", false);
            if (estatus.equals("MANTENIMIENTOWEB"))
                dr.put("btnActivDesactivMantenimiento_Text", "Desactivar mantenimiento");
            else
                dr.put("btnActivDesactivMantenimiento_Text", "Activar mantenimiento");
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void cerrarSesiones (HttpServletRequest request)
    {
        try 
        {
            HttpSession session = request.getSession(false);
            SICEEO_SessionManager sessionManager = (SICEEO_SessionManager)session.getServletContext().getAttribute("activeUsers");
            if (sessionManager!=null){
                sessionManager.invalidateAllSessions ();
                this.dr.put("activeUsers", sessionManager.getActiveUsers());
            }
        }
        catch (Exception ex){ 
            this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); 
        }
    }
    
    /*private void pingWebService (String usuario)
    {
        try {
            throw new Exception(hello(usuario));
        }catch (Exception ex){ this.dr.put("returnCase", 1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    }

    private static String hello(java.lang.String name) {
        webservices.WSGestionFirma_Service service = new webservices.WSGestionFirma_Service();
        webservices.WSGestionFirma port = service.getWSGestionFirmaPort();
        return port.hello(name);
    }*/


}


