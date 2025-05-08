package modelo.Configuraciones;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.DAO.SICEEO_QueriesInformix;

/* 
    Creado el : 29/08/2017, 11:40:10 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_GestionarAvisos {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_GestionarAvisos (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.r = new SICEEO_HttpServletRequest(request);
        this.sesion = request.getSession(false);
        this.request = request;
    }
    
/*******************************************************************************/
/****************************** ÁREA PARA EL CONTROLADOR ***********************/
/*******************************************************************************/
    
    /**
     *
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (String metodo)
    {
        String superUsuario = ""+sesion.getAttribute("superUsuario");
        String txtUsuario = ""+sesion.getAttribute("userName");
        
        if(txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ") )
        {
            if (metodo.equals("foAc"))
                formActivate ();
            else if (metodo.equals("btElMe_Cl"))
                btnEliminarMensaje_Click (r.gP("idaviso"));
            else if (metodo.equals("seEsAcIn"))
                setEstatusActivoInactivo (r.gP("idaviso"), r.gP("caso"));
            else if (metodo.equals("btReMe"))
                btnReemplazarMensaje_Click (r.gP("idaviso"), r.gP("mensaje"));
            else if (metodo.equals("btInMe"))
                btnInsertarMensaje_Click (r.gP("mensaje"));
        }else {
            this.dr.put("returnCase", -10); 
            mensaje.Configuraciones("USUARIO_REESTRINGIDO", "", "", this.dr);
        }
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate ()
    {
        try
        {
            qryIfx.conectar();
            dr.put("tblAvisos",qryIfx.getAvisos ());
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnEliminarMensaje_Click (String idaviso)
    {
        try
        {
            qryIfx.conectar();
            qryIfx.eliminarAviso (idaviso);
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void setEstatusActivoInactivo (String idaviso, String caso)
    {
        try
        {
            qryIfx.conectar();
            qryIfx.setMensajeActivoInactivo (idaviso, caso);
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnReemplazarMensaje_Click (String idaviso, String aviso)
    {
        try
        {
            qryIfx.conectar();
            qryIfx.reemplazarMensaje (idaviso, aviso);
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnInsertarMensaje_Click (String aviso)
    {
        try
        {
            qryIfx.conectar();
            qryIfx.insertarMensaje (aviso);
            dr.put("tblAvisos",qryIfx.getAvisos ());
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
