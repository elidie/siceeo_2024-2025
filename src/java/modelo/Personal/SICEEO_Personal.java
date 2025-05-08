
package modelo.Personal;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 5/06/2017, 05:57:55 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Personal {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_Personal (Map datosReturn, HttpServletRequest request)
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
        if (metodo.equals("foAc"))
            formActivate ( r.gP("califCicEscIn"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));
        else if (metodo.equals("tbPe_ChSeIt"))
            tblGdosGposPersonal_ChangeSelectedItem (r.gP("califCicEscIn"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));
        /*else if (metodo.equals("btBuRfDi_Cl"))
            btnBuscarPersonal ("DIRECTOR", r.gP("txtRfcDir"));*/
        else if (metodo.equals("btBuRfPr_Cl"))
            btnBuscarPersonal ("PROFESOR", r.gP("txtRfcProf"));
        else if (metodo.equals("btGuDaDi_Cl"))
            btnGuardarDatosDirector_Click (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveturno"), r.gP("tblPrincipal_cveplan"), r.gP("txtNombreDir"), 
                   r.gP("txtPrimerApeDir"), r.gP("txtSegundoApeDir"), r.gP("cbxGeneroDir"), ""+sesion.getAttribute("userName")); 
        else if (metodo.equals("btGuDaPr_Cl"))
            btnGuardarDatosProfesor_Click (r.gP("califCicEscIn"), dm.strToMap(r.gP("tblGdosGposPersonal_selRow"), "~", new String[]{"idcct","grado","grupo","cveturno","cveplan"}), 
                    r.gP("txtNombreProf"), r.gP("txtPrimerApeProf"), r.gP("txtSegundoApeProf"), ""+sesion.getAttribute("userName"));
    }
    
    private void formActivate (String califCicEscIn, String tblPrincipal_cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            dr.put("tblGdosGposPersonal", qryIfx.getGradosGrupos (tblPrincipal_idcct, califCicEscIn));
            dr.put("escuela", qryIfx.getDatosEscuelaDePersonal (tblPrincipal_idcct,califCicEscIn));
            qryIfx.getPersonal(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, dr);
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void tblGdosGposPersonal_ChangeSelectedItem (String califCicEscIn, String tblPrincipal_cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            qryIfx.getPersonal(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, dr);
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnBuscarPersonal (String caso, String RFC)
    {
        try
        {
            qryIfx.conectar();
            dr.putAll(qryIfx.buscarRFCPersonal(caso,RFC));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnGuardarDatosDirector_Click (String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_cveturno, String tblPrincipal_cveplan, String txtNombreDir, 
            String txtPrimerApeDir, String txtSegundoApeDir, String cbxGeneroDir, String txtUsuario)
    {
        boolean hacerCommit=false;
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            if (!(""+sesion.getAttribute("seccion")).equals("22") && !(""+sesion.getAttribute("cveunidad")).equals("CCT"))
                throw new SICEEO_Excepcion(0, "SIN_PERMISO", "");
            if (txtNombreDir.trim().equals(""))
                throw new SICEEO_Excepcion(0, "CAMPO_VACIO", "nombre del director");
            else if (txtPrimerApeDir.trim().equals(""))
                throw new SICEEO_Excepcion(0, "CAMPO_VACIO", "primer apellido del director");
            else if (!dm.isNombreOApellido(txtNombreDir.trim(),50))
                throw new SICEEO_Excepcion(0, "CARACTER_INVALIDO", "nombre del director",txtNombreDir);
            else if (!dm.isNombreOApellido(txtPrimerApeDir.trim(),50))
                throw new SICEEO_Excepcion(0, "CARACTER_INVALIDO", "primer apellido del director",txtPrimerApeDir);
            else if (!txtSegundoApeDir.trim().equals("") && !dm.isNombreOApellido(txtSegundoApeDir.trim(),50))
                throw new SICEEO_Excepcion(0, "CARACTER_INVALIDO", "segundo apellido del director",txtSegundoApeDir);
            
            qryIfx.conectarConTransaccion();
            qryIfx.guardarDatosDirector (califCicEscIn, tblPrincipal_idcct, tblPrincipal_cveturno, tblPrincipal_cveplan, txtNombreDir, txtPrimerApeDir, txtSegundoApeDir, 
                    cbxGeneroDir, ""+sesion.getAttribute("userName"));                 
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  
            this.dr.put("returnCase",ex.getNumError());
            if (ex.getMensaje().equals("CAMPO_VACIO"))
                mensaje.General("CAMPO_VACIO", ex.getMessage(), ex.getMensaje2(), this.dr);
            else if (ex.getMensaje().equals("CARACTER_INVALIDO"))
                mensaje.General("CARACTER_INVALIDO", ex.getMensaje2(), ex.getMensaje3(), this.dr);
            else
                mensaje.General("GENERAL", ex.getMessage(), "", this.dr);
        }catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnGuardarDatosProfesor_Click (String califCicEscIn, Map tblGdosGposPersonal_selRow, String txtNombreProf, String txtPrimerApeProf, 
            String txtSegundoApeProf, String txtUsuario)
    {
        boolean hacerCommit=false;
        
        try
        {
            dm.isIdcctAutorizada (sesion, ""+tblGdosGposPersonal_selRow.get("idcct"));
            
            if (txtNombreProf.trim().equals(""))
                throw new SICEEO_Excepcion(0, "CAMPO_VACIO", "nombre del profesor");
            else if (txtPrimerApeProf.trim().equals(""))
                throw new SICEEO_Excepcion(0, "CAMPO_VACIO", "primer apellido del profesor");
            else if (!dm.isNombreOApellido(txtNombreProf.trim(),50))
                throw new SICEEO_Excepcion(0, "CARACTER_INVALIDO", "nombre del profesor",txtNombreProf);
            else if (!dm.isNombreOApellido(txtPrimerApeProf.trim(),50))
                throw new SICEEO_Excepcion(0, "CARACTER_INVALIDO", "primer apellido del profesor",txtPrimerApeProf);
            else if (!txtSegundoApeProf.trim().equals("") && !dm.isNombreOApellido(txtSegundoApeProf.trim(),50))
                throw new SICEEO_Excepcion(0, "CARACTER_INVALIDO", "segundo apellido del profesor",txtSegundoApeProf);
            /*else if (txtCurpProf.trim().equals(""))
                throw new SICEEO_Excepcion(0, "CAMPO_VACIO", "curp del profesor");
            else if (!txtCurpProf.trim().equals("") && txtCurpProf.trim().length()<18)
                throw new SICEEO_Excepcion(0, "LONG_INVALIDA", "curp del profesor");*/
    
            qryIfx.conectarConTransaccion();
            qryIfx.guardarDatosProfesor(califCicEscIn, tblGdosGposPersonal_selRow, txtNombreProf, txtPrimerApeProf, txtSegundoApeProf, txtUsuario);            
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  
            this.dr.put("returnCase",ex.getNumError());
            if (ex.getMensaje().equals("CAMPO_VACIO"))
                mensaje.General("CAMPO_VACIO", ex.getMessage(), ex.getMensaje2(), this.dr);
            else if (ex.getMensaje().equals("CARACTER_INVALIDO"))
                mensaje.General("CARACTER_INVALIDO", ex.getMensaje2(), ex.getMensaje3(), this.dr);
            else
                mensaje.General("GENERAL", ex.getMessage(), "", this.dr);
        }catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
