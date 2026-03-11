package modelo.Oficializaciones;

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
//import webservices.WsfirmaDatosReturn;

/* 
    Creado el : 09-mar-2017, 9:48:59
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Oficializar {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    
    public SICEEO_Oficializar (Map datosReturn, HttpServletRequest request)
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
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (String metodo)
    {
        String superUsuario = ""+sesion.getAttribute("superUsuario");
        String txtUsuario = ""+sesion.getAttribute("userName");
        String puedeDesof = ""+sesion.getAttribute("puedeDesof");
        String quitarFolio = ""+sesion.getAttribute("quitarFolio");
        
        boolean esUsrAdmin;
        boolean puedeDesoficializar = ( (esUsrAdmin=(txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ"))) || (superUsuario.equals("si") && puedeDesof.equals("si")));
        boolean puedeQuitarFolio = ( txtUsuario.equals("ELYLOPEZ") || quitarFolio.equals("si"));
        
        if (metodo.equals("foAc")) //Ciclos anteriores al 2025-2026
            formActivate (r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), txtUsuario, puedeDesoficializar, esUsrAdmin);
        if( metodo.equals("foAcXalu")) //Ciclos anteriores al 2025-2026
            formActivate1 (r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("bimeval"));
        else if (metodo.equals("ofPr"))
            oficializaPreinscripcion (r.gP("caso"), r.gP("tblPrincipal_idcct"), r.gP("cicesciniPreinsc"), txtUsuario, puedeDesoficializar, esUsrAdmin);
        else if (metodo.equals("ofCa"))
            oficializaCalificaciones (r.gP("caso"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("bimestre"), r.gP("idalus"), txtUsuario, puedeDesoficializar, esUsrAdmin);            
        else if (metodo.equals("of_CaXAlu"))
            btnOficializarXAlu_Click (r.gP("tblPrincipal_cicescini"),r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("bim"), dm.vstrToArrMap(r.gPV("tblAlumnos"), "~", new String[]{"selec","idalu"}), 
                    ""+sesion.getAttribute("userName"), puedeDesoficializar, puedeQuitarFolio,r.gP("chkTodos"));        
        else if (metodo.equals("ofPrPr"))
            oficializaEvaluacionesPreesc (r.gP("caso"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("evaluacion"), r.gP("idalus"), txtUsuario, puedeDesoficializar, esUsrAdmin);
        /*else if (metodo.equals("ofIn"))
            oficializaInscripcion (r.gP("caso"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cicescini"), txtUsuario, puedeDesoficializar, esUsrAdmin);*/
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    public void formActivate (String tblPrincipal_idcct, String tblPrincipal_cicescini, String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario, boolean puedeDesoficializar, boolean esUsrAdmin)
    {   
        try {
            qryIfx.conectar();
            
            dr.put("verDesofic",puedeDesoficializar); 
            
            dr.put("esPreinscOficializada",qryIfx.isOficializado(tblPrincipal_idcct, ""+(dm.toInt(tblPrincipal_cicescini)+1),"","","PREINSCRIPCION"));
           
            dr.put("ofsCal", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM"));
            dr.put("ofsEval", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION"));
            dr.put("esInscripOficializada",qryIfx.isOficializado(tblPrincipal_idcct, tblPrincipal_cicescini,"","","INSCRIPCION"));
            dr.put("noHasAluCompl", !qryIfx.hayAlumnosParaComplementaria(tblPrincipal_cicescini, tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            dr.put("masDeUnIdcct", (""+sesion.getAttribute("tblPrincipal_distinctIdscct")).split("~").length>1);
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        //catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoSec(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void formActivate1 (String tblPrincipal_cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String bimeval)
    {
        try
        {
            qryIfx.conectar();
            dr.put("tblAlumnos",qryIfx.getAlumnosParaOficializar(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan.equals("3")?"EVALUACION "+bimeval:"CALIFS BIM "+bimeval, bimeval));
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void oficializaPreinscripcion (String caso, String tblPrincipal_idcct, String cicesciniPreinsc, String txtUsuario, boolean puedeDesoficializar, 
            boolean isUsrAdmin)
    {
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            if (caso.equals("oficializar"))
                qryIfx.oficializar(cicesciniPreinsc, "", "", tblPrincipal_idcct, "", "", "PREINSCRIPCION", null, txtUsuario);
            else {
                if (!puedeDesoficializar)
                    throw new SICEEO_Excepcion (0,"SIN_PERMISO_DESOFIC");
                qryIfx.desoficializar(puedeDesoficializar, "PREINSCRIPCION", cicesciniPreinsc, tblPrincipal_idcct, "", txtUsuario);
            }
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Oficializar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void oficializaCalificaciones (String caso, String tblPrincipal_modalidad, String tblPrincipal_idcct, String tblPrincipal_cicescini, String tblPrincipal_cveplan, 
            String tblPrincipal_grado, String tblPrincipal_grupo, String bimestre, String idalus, String txtUsuario, boolean puedeDesoficializar, 
            boolean isUsrAdmin)
    {
        //WsfirmaDatosReturn datosReturn;
        boolean hacerCommit=false;
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            
            if (!tblPrincipal_cveplan.equals("1") && !tblPrincipal_cveplan.equals("2"))
                throw new SICEEO_Excepcion (0,(caso.equals("oficializar")?"NIVEL_SIN_PERMISO_OFIC":"NIVEL_SIN_PERMISO_DESOFIC"),"Primaria o Secundaria","calificaciones bimestrales");
            
            qryIfx.conectarConTransaccion();
            
            //Llevamos a cabo el firmado, haciendo mediante el webservice
            // Se comento el firmado por escuela --  ely (25-05-2018)
            
            /*if (caso.equals("oficializar") && bimestre.equals("5") && (tblPrincipal_cveplan.equals("1") && tblPrincipal_grado.equals("6") || tblPrincipal_cveplan.equals("2") && tblPrincipal_grado.equals("3"))){
                qryIfx.canOficCalifBim (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, bimestre);
                
                datosReturn = escuelaFirmando(tblPrincipal_cicescini, tblPrincipal_cveplan, tblPrincipal_grupo, tblPrincipal_idcct, txtUsuario);
                if (datosReturn==null)
                    throw new SICEEO_Excepcion (-1,"NO_PUDO_FIRMAR");
                else if (datosReturn.getReturnCase() == 0)
                    throw new SICEEO_Excepcion (datosReturn.getReturnCase(), "MENSAJE_WEBSERVICE", datosReturn.getMensaje());
                else if (datosReturn.getReturnCase() == -1)
                    throw new SICEEO_Excepcion (datosReturn.getReturnCase(), "ERROR_WEBSERVICE", datosReturn.getMensaje());
            }*/
            
            
            if (caso.equals("oficializar"))
                qryIfx.oficializar(tblPrincipal_cicescini, tblPrincipal_modalidad, tblPrincipal_cveplan, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM "+bimestre, bimestre, txtUsuario);
            else {
                if (!puedeDesoficializar)
                    throw new SICEEO_Excepcion (0,"SIN_PERMISO_DESOFIC");
                qryIfx.desoficializar(puedeDesoficializar, "CALIFS BIM "+bimestre, tblPrincipal_cicescini, tblPrincipal_idcct, idalus, txtUsuario);
            }
            
            dr.put("ofsCal", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM"));
            dr.put("ofsEval", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION"));
            //hacerCommit=true; 
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Oficializar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    private void btnOficializarXAlu_Click (String tblPrincipal_cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, 
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
    
    private void oficializaEvaluacionesPreesc (String caso, String tblPrincipal_modalidad, String tblPrincipal_idcct, String tblPrincipal_cicescini, String tblPrincipal_cveplan, 
            String tblPrincipal_grado, String tblPrincipal_grupo, String eval, String idalus, String txtUsuario, boolean puedeDesoficializar, boolean isUsrAdmin)
    {
        boolean hacerCommit = false;
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            
            if (!tblPrincipal_cveplan.equals("3"))
                throw new SICEEO_Excepcion (0,(caso.equals("oficializar")?"NIVEL_SIN_PERMISO_OFIC":"NIVEL_SIN_PERMISO_DESOFIC"),"Preescolar","evaluaciones");
            
            qryIfx.conectarConTransaccion();
            if (caso.equals("oficializar"))
                qryIfx.oficializar(tblPrincipal_cicescini, tblPrincipal_modalidad, tblPrincipal_cveplan, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+eval, eval, txtUsuario);
            else {
                if (!puedeDesoficializar)
                    throw new SICEEO_Excepcion (0,"SIN_PERMISO_DESOFIC");
                qryIfx.desoficializar(puedeDesoficializar, "EVALUACION "+eval, tblPrincipal_cicescini, tblPrincipal_idcct, idalus, txtUsuario );
            }
            dr.put("ofsCal", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM"));
            dr.put("ofsEval", qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION"));
            hacerCommit = true;  
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Oficializar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void oficializaInscripcion (String caso, String tblPrincipal_idcct, String tblPrincipal_cicescini, String txtUsuario, boolean puedeDesoficializar,
            boolean isUsrAdmin)
    {
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            
            if (caso.equals("oficializar"))
                qryIfx.oficializar(tblPrincipal_cicescini, "", "", tblPrincipal_idcct, "", "", "INSCRIPCION", null, txtUsuario);
            else {
                if (!puedeDesoficializar)
                    throw new SICEEO_Excepcion (0,"SIN_PERMISO_DESOFIC");
                qryIfx.desoficializar(puedeDesoficializar, "INSCRIPCION", tblPrincipal_cicescini, tblPrincipal_idcct, "", txtUsuario );
            }
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Oficializar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }

/* ************************************************************************************************************************************ */
/* ***************************************** PARA INTERACCIÓN DEL FIRMADO CON EL WEB SERVICE  ***************************************** */
/* ************************************************************************************************************************************ */
    /*private static WsfirmaDatosReturn escuelaFirmando(java.lang.String cicescini, java.lang.String cveplan, java.lang.String grupo, java.lang.String idcct, java.lang.String usuario) {
        webservices.WSGestionFirma_Service service = new webservices.WSGestionFirma_Service();
        webservices.WSGestionFirma port = service.getWSGestionFirmaPort();
        return port.escuelaFirmando(cicescini, cveplan, grupo, idcct, usuario);
    }*/
}
