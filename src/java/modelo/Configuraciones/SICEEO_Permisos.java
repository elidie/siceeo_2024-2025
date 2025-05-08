package modelo.Configuraciones;

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
    Creado el : 26-ene-2017, 8:48:46
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Permisos {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_Permisos (Map datosReturn)
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
        if(request.getParameter("txtUsuario")!=null && ( request.getParameter("txtUsuario").equals("IVALLE") && session.getAttribute("userName").equals("IVALLE") || request.getParameter("txtUsuario").equals("ELYLOPEZ") && session.getAttribute("userName").equals("ELYLOPEZ") )  )
        {
            if (metodo.equals("geTiUsCoPe"))
                getTipoUsuariosConPermiso ();
            else if (metodo.equals("guTiUsCoPe"))
            {
                String[] tiposUsr = r.gPV("tiposUsr");
                String[] nombresCol = new String[tiposUsr.length*2+1];
                nombresCol[0]="idobjeto";
                for (int i=0, j=1; i<tiposUsr.length; i++, j++){                //Creamos los nombres de columna
                    nombresCol[j]=tiposUsr[i];
                    nombresCol[tiposUsr.length+j]=tiposUsr[i]+"_oldValue";
                }
                guardarTipoUsuariosConPermiso (dm.vstrToArrMap(r.gPV("tblTipoUsuarios"), "~", nombresCol));
            }else if (metodo.equals("btFiUs_Cl"))
                btnFiltrarUsuario_Click (r.gP("txtFiltrarUsuario"));
            else if (metodo.equals("btGuUsEs_Cl"))
                btnGuardaUsuariosEsp_Click (dm.vstrToArrMap(r.gPV("tblUsuariosEsp"), "~", new String[]{"idaccesoespecifico","permiso","permiso_oldValue"}));
            else if (metodo.equals("btElUsEs_Cl"))
                btnEliminarUsuariosEsp_Click (r.gP("idaccesoespecifico"));
            else  if (metodo.equals("btBuUs_Cl"))
                btnBuscarUsuario_Click (r.gP("txtBuscarUsuario"));
            else  if (metodo.equals("btInUsEs_Cl"))
                btnInsertUsuariosEsp_Click (r.gP("loginuser"), r.gP("idobjeto"), r.gP("permiso"));
        }else {
            this.dr.put("returnCase", -10); 
            mensaje.Configuraciones("USUARIO_REESTRINGIDO", "", "", this.dr);
        }
        
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    public void getTipoUsuariosConPermiso ()
    {
        
        dr.put("returnCase", 1);
        
        try 
        {
            qryIfx.conectar();
            dr.put("tblTipoUsuarios", qryIfx.getPermisosTipoUsuario ());
            dr.put("tblUsuariosEsp", qryIfx.getUsuarioConPermisosEspecificos(""));
            dr.put("tiposUsr", qryIfx.getTiposUsuario ());
            dr.put("cbxComponenteAPermitir", qryIfx.getNombreComponentesParaPermiso());
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void guardarTipoUsuariosConPermiso (ArrayList<Map> tblTipoUsuarios)
    {
        
        dr.put("returnCase", 1);
        boolean hacerCommit = false;
        
        try 
        {
            qryIfx.conectarConTransaccion();
            qryIfx.guardarTipoUsuariosConPermiso (tblTipoUsuarios);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    public void btnFiltrarUsuario_Click (String txtFiltrarUsuario)
    {
        dr.put("returnCase", 1);
        
        try 
        {
            qryIfx.conectar();
            dr.put("tblUsuariosEsp", qryIfx.getUsuarioConPermisosEspecificos(txtFiltrarUsuario));
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void btnGuardaUsuariosEsp_Click (ArrayList<Map> tblUsuariosEsp)
    {
        boolean hacerCommit = false;
        dr.put("returnCase", 1);
        
        try 
        {
            qryIfx.conectarConTransaccion();
            qryIfx.guardarExcepcionDePermiso (tblUsuariosEsp);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    public void btnEliminarUsuariosEsp_Click (String idaccesoespecifico)
    {
        dr.put("returnCase", 1);
        
        try 
        {
            qryIfx.conectar();
            qryIfx.eliminarExcepcionDePermiso (idaccesoespecifico);
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void btnBuscarUsuario_Click (String txtBuscarUsuario)
    {
        dr.put("returnCase", 1);
        
        try 
        {
            qryIfx.conectar();
            dr.put("cbxUsrEncontrados", qryIfx.geUsuariosFiltrados(txtBuscarUsuario));
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void btnInsertUsuariosEsp_Click (String loginuser, String idobjeto, String permiso)
    {
        boolean hacerCommit = false;
        dr.put("returnCase", 1);
        
        try 
        {
            qryIfx.conectarConTransaccion();
            dr.put("idaccesoespecifico",qryIfx.insertExcepcionDePermiso(loginuser, idobjeto, permiso));
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Permisos(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
