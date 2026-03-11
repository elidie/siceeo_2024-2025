package modelo.Oficializaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.DAO.SICEEO_QueriesInformix;

/* 
    Creado el : 18/07/2017, 05:44:07 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Desoficializar {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_Desoficializar (Map datosReturn, HttpServletRequest request)
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
        String puedeDesof = ""+sesion.getAttribute("puedeDesof");
        String quitarFolio = ""+sesion.getAttribute("quitarFolio");
        
        boolean esUsrAdmin;
        boolean puedeDesoficializar = (esUsrAdmin=(txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ")) || (superUsuario.equals("si") && puedeDesof.equals("si")));
        boolean puedeQuitarFolio = ( txtUsuario.equals("ELYLOPEZ") || quitarFolio.equals("si"));
        
        if (metodo.equals("foAc")){
            formActivate ( r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("bimeval"));
        } else if (metodo.equals("btDe_Cl"))
            btnDesoficializar_Click (r.gP("tblPrincipal_cicescini"),r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("bim"), dm.vstrToArrMap(r.gPV("tblAlumnos"), "~", new String[]{"selec","idalu"}), 
                    ""+sesion.getAttribute("userName"), puedeDesoficializar, puedeQuitarFolio,r.gP("chkTodos"));
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate (String tblPrincipal_cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String bimeval)
    {
        try
        {
            qryIfx.conectar();
            dr.put("tblAlumnos",qryIfx.getAlumnosParaDesoficializar(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan.equals("3")?"EVALUACION "+bimeval:"CALIFS BIM "+bimeval, bimeval));
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnDesoficializar_Click (String tblPrincipal_cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String bimeval, ArrayList<Map> tblAlumnos, String txtUsuario, boolean puedeDesoficializar, boolean puedeQuitarFolio,String chkTodos)
    {
        boolean hacerCommit = false;
        try
        {
            if (!puedeDesoficializar)
                throw new SICEEO_Excepcion (0,"SIN_PERMISO_DESOFIC");
            qryIfx.conectarConTransaccion();
            qryIfx.desoficXAlumno (puedeDesoficializar, puedeQuitarFolio, tblPrincipal_cicescini, tblPrincipal_cveplan, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblAlumnos, txtUsuario, tblPrincipal_cveplan.equals("3")?"EVALUACION "+bimeval:"CALIFS BIM "+bimeval, bimeval,Boolean.valueOf(chkTodos));
            dr.put("ofsCal", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM"));
            dr.put("ofsEval", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION"));
            //hacerCommit = true;  
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Oficializar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
