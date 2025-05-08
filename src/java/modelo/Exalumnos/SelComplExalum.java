package modelo.Exalumnos;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.DAO.SICEEO_QueriesInformix;

/**
 *
 * Creado el 18/10/2017, 12:16:29 PM
 * @author Ing. Maai Nolasco Sánchez
 */
public class SelComplExalum {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    private final String superUsuario, txtUsuario;
    private final boolean tienePrivilegios, esUsrAdmin;
    
    public SelComplExalum (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.r = new SICEEO_HttpServletRequest(request);
        this.sesion = request.getSession(false);
        this.request = request;
        
        superUsuario = ""+sesion.getAttribute("superUsuario");
        txtUsuario = ""+sesion.getAttribute("userName");
        tienePrivilegios = ( (esUsrAdmin=(txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ"))) || superUsuario.equals("si"));
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
            formActivate ( r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("casoRep"));
        else if (metodo.equals("geExSeMeCo"))
            getExalumnosSelMesCompl ( r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), 
                    r.gP("mesComplem"));
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String casoRep)
    {
        try
        {
            qryIfx.conectar();
            
            if (casoRep.equals("oficCertCompl"))
                dr.put("tblAlumnos",qryIfx.getAlumnosConExtraordinarioAprobado(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            else if (casoRep.equals("selMesCompl"))
                dr.put("canSelEspAlus",tienePrivilegios);
            else
                dr.put("tblAlumnos",qryIfx.getAlumnosParaComplementaria(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void getExalumnosSelMesCompl (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String mesComplem)
    {
        try
        {
            qryIfx.conectar();

            String idalusMesCompl = qryIfx.getIdalusConExtraordinarioAprobado(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, mesComplem, "");
            dr.put("tblAlumnos", idalusMesCompl.equals("")?null:qryIfx.getEstosExalumnosCompl (idalusMesCompl));
            
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        //catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Complementaria(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
