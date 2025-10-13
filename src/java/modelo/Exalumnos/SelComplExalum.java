package modelo.Exalumnos;

import java.sql.SQLException;
import java.util.ArrayList;
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
            formActivate (r.gP("cicescini_act"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"),
                    r.gP("tblPrincipal_grupo"), r.gP("casoRep"));
        else if (metodo.equals("geExSeMeCo"))
            getExalumnosSelMesCompl ( r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), 
                    r.gP("idperexmext"));
        else if (metodo.equals("geExSeMeCons"))
            getExalumnosSelMesCons (r.gP("tblPrincipal_cicescinilib"), r.gP("tblPrincipal_cicescini"), 
                    r.gP("cicescini_ex"),  r.gP("tblPrincipal_idcct"), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("idperexmext"));
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate (String tblPrincipal_cicescini, String tblPrincipal_cicescini_ex, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String casoRep)
    {
        ArrayList<Map> tblPeriodosExmExt;
        String cbxMesExCompl;            
        
        try
        {
            qryIfx.conectar();
            
            if (casoRep.equals("oficCertCompl"))
                dr.put("tblAlumnos",qryIfx.getAlumnosConExtraordinarioAprobado(tblPrincipal_cicescini_ex, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            else if (casoRep.equals("selMesCompl")) {
                int cicescini = (tblPrincipal_cicescini.length()>0 ?
                        Integer.parseInt(tblPrincipal_cicescini)-1 : 0);
                cbxMesExCompl ="<select id='cbxMesExCompl'>";
                tblPeriodosExmExt = qryIfx.getPerdiodosExmExt(""+cicescini, tblPrincipal_cicescini_ex);
                int tam = tblPeriodosExmExt.size();
                for (int i=0; i<tam; i++) {
                    cbxMesExCompl += "<option value='"+tblPeriodosExmExt.get(i).get("idperexmext")+"'>"
                        + tblPeriodosExmExt.get(i).get("num_periodo")+(tblPeriodosExmExt.get(i).get("num_periodo").equals("2") ? "DO": "ER")
                        + " PERIODO ("+ tblPeriodosExmExt.get(i).get("mes").toString().substring(0, 3)
                        + ")</option>";
                }
                cbxMesExCompl += "</select>";
                dr.put("cbxMesExCompl",cbxMesExCompl);        
                dr.put("canSelEspAlus",tienePrivilegios);
            }                
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
    
    public void getExalumnosSelMesCons (String tblPrincipal_cicescinilib, String tblPrincipal_cicescini, String cicescini_ex, 
            String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String idperexmext)
    {
        try
        {
            qryIfx.conectar();
            //String idalusMesCons = qryIfx.getIdalusConExtraordinarioOficializado(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idperexmext, "");
            dr.put("tblAlumnos",qryIfx.getAlumnosParaMesExCons(tblPrincipal_cicescini, cicescini_ex, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idperexmext));
            
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        //catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Complementaria(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
