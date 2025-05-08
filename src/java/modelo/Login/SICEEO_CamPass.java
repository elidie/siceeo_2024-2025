/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo.Login;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 3/02/2016, 12:30:57 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_CamPass {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final Map dr;
    private final SICEEO_DataModule dm;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_CamPass(Map datosReturn)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dr = datosReturn;
        this.dm = new SICEEO_DataModule ();
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
        if (metodo.equals("btBu_AcPe")){
            btnBuscar_ActionPerformed (r.gP("txtUsuario"),r.gP("pwdPassword"));
        }/*else if (metodo.equals("btCaPa_AcPe")){  //Comentado para desactivar el boton de cambiar pass
            btnCamPassw_ActionPerformed (r.gP("txtUsuario"), r.gP("pwdPassword"), r.gP("pwdNewPass"), r.gP("pwdNewPass2"), r.gP("txtNombre"), r.gP("txtApepat"), r.gP("txtApemat"), 
            r.gP("txtCurp"), r.gP("txtPuesto"), r.gP("txtTelefono"), r.gP("txtCorreo"));
        }*/
    }
/* **************************************************************************** */
/* **************************************************************************** */
/* **************************************************************************** */

    public void btnBuscar_ActionPerformed (String txtUsuario, String pwdPassword)
    {
        Map QUsuario;
        String y;
        
        dr.put("txtNewpass_Enabled", false);
        dr.put("txtNewpass2_Enabled", false);
        dr.put("txtNombre_Enabled", false);
        dr.put("txtApepat_Enabled", false);
        dr.put("txtApemat_Enabled", false);
        dr.put("txtCurp_Enabled", false);
        dr.put("txtPuesto_Enabled", false);
        dr.put("txtTelefono_Enabled", false);
        dr.put("txtCorreo_Enabled", false);
        dr.put("btnCamPassw_Enabled", false);

        try {
            qryIfx.conectar();
            QUsuario = qryIfx.usuario(txtUsuario);
            if ( !QUsuario.isEmpty() )
            {
                y = qryIfx.decodificarBase64(""+QUsuario.get("pasword"));
                if (y.equals(pwdPassword.trim().toUpperCase()))
                {
                    dr.put("txtNewpass_Enabled", true);
                    dr.put("txtNewpass2_Enabled", true);
                    dr.put("txtNombre_Enabled", true);
                    dr.put("txtApepat_Enabled", true);
                    dr.put("txtApemat_Enabled", true);
                    dr.put("txtCurp_Enabled", true);
                    dr.put("txtPuesto_Enabled", true);
                    dr.put("txtTelefono_Enabled", true);
                    dr.put("txtCorreo_Enabled", true);
                    dr.put("btnCamPassw_Enabled", true);

                    dr.put("txtNombre_Text", QUsuario.get("nombreu"));
                    dr.put("txtApepat_Text", QUsuario.get("apepat"));
                    dr.put("txtApemat_Text", QUsuario.get("apemat"));
                    dr.put("txtCurp_text", QUsuario.get("curp"));
                    dr.put("txtPuesto_Text", QUsuario.get("puesto"));
                    dr.put("txtTelefono_text", QUsuario.get("telefono"));
                    dr.put("txtCorreo_text", QUsuario.get("email"));
                } else
                    throw new SICEEO_Excepcion (0,"USUARIO_NO_ENCONTRADO");
            } else
                throw new SICEEO_Excepcion (0,"USUARIO_NO_ENCONTRADO");
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.camPass(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        try { qryIfx.cerrarConexion(); } catch (SQLException ex) { }
    }
    
    public void btnCamPassw_ActionPerformed (String txtUsuario, String pwdPassword, String pwdNewPass, String pwdNewPass2, String txtNombre, String txtApepat, String txtApemat, 
            String txtCurp, String txtPuesto, String txtTelefono, String txtCorreo)
    {
        Map QUsuario;
        String y;
        
        try {
            qryIfx.conectar();
            QUsuario = qryIfx.usuario(txtUsuario);
            if ( !QUsuario.isEmpty() )
            {
                y = qryIfx.decodificarBase64(""+QUsuario.get("pasword"));
                if (y.equals(pwdPassword.trim().toUpperCase()))
                {
                    if (pwdNewPass.trim().length() == 5)
                    {
                        if(!(pwdNewPass.trim().toUpperCase()).matches("[A-Z]{1}[0-9]{5}"))
                            throw new SICEEO_Excepcion (0,"CONTRASENIA_NO_NOMENCLATURA");
                        
                        else if (pwdNewPass.trim().toUpperCase().equals(pwdNewPass2.trim().toUpperCase()))
                            qryIfx.actualizaUsuario(qryIfx.codificarABase64(pwdNewPass.trim().toUpperCase()), txtNombre.trim(), txtApepat.trim(), txtApemat.trim(), 
                                    txtCurp.trim(), txtPuesto.trim(), txtTelefono.trim(), txtCorreo.trim(), ""+QUsuario.get("idusuario"));
                        else
                            throw new SICEEO_Excepcion (0,"CONTRASENIA_NO_COINCIDE");
                    }else
                        throw new SICEEO_Excepcion (0,"TAM_CONTRASENIA");
                }else
                    throw new SICEEO_Excepcion (0,"CONTRASENIA_ORIGINAL_INCORRECTA");
            }else
                throw new SICEEO_Excepcion (0,"CONTRASENIA_ORIGINAL_INCORRECTA");
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.camPass(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        try { qryIfx.cerrarConexion(); } catch (SQLException ex) { }
    }
}
