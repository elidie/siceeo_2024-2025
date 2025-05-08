package modelo.Exalumnos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.DAO.SICEEO_QueriesInformix;
//import webservices.WsfirmaDatosReturn;

/**
 *
 * Creado el 13/09/2017, 11:58:08 AM
 * @author Ing. Maai Nolasco Sánchez
 */
public class SICEEO_Exalumnos {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_Exalumnos (Map datosReturn, HttpServletRequest request)
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
        
        if (metodo.equals("foAc"))
            FormActivate (superUsuario.equals("si"));
        else if (metodo.equals("btBuAl_AcPe"))
            btnBuskAlum_Click ( dm.toBool(r.gP("rbnCurp_isChecked")), dm.toBool(r.gP("rbnIdaluX_isChecked")), r.gP("txtCicesciniEstud"), r.gP("txtCurp"), 
                    r.gP("txtIdalu"), txtUsuario, superUsuario.equals("si") );
        else if (metodo.equals("tbAl_MoCl"))
            tblAlumgrado_MouseClicked (r.gP("tblAlumgrado_cicescini"), r.gP("tblAlumgrado_idalu"));
        else if (metodo.equals("tbFoYFiEx_Cl")){
            //btnFoliarYFirmarExalumno_Click ( r.gP("cicesciniestud"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), 
              //      r.gP("grado"), r.gP("grupo"), r.gP("idalu"), txtUsuario, superUsuario.equals("si"));
        }
        /*else if (metodo.equals("btUpPrFo_Cl"))
            btnUpdatePromFol_Click ( r.gP("idalu"), txtUsuario, superUsuario.equals("si")); */
        
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void FormActivate (boolean superUsuario)
    {
        try
        {
            dr.put ("cicescinilib", calcuarCicescinilib ());
            dr.put ("btnUpdatePromFol_setVisible", superUsuario);
        } /*catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }*/
        //catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Exalumnos(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        //finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private ArrayList<Map> btnBuskAlum_Click (boolean rbnCurp_isChecked, boolean rbnIdaluX_isChecked, String txtCicesciniEstud, String txtCurp, 
            String txtIdalu, String txtUsuario, boolean esSuperusuario)
    {
        String condicionQBuskExalum="";
        ArrayList<Map> QBuskExalum = null, QSusEstud=null, QSusFolios=null;
        Map QEstadisticaDePermisos, QCicloActivo;
        int tamHistorial;
        
        try
        {
            if (txtCicesciniEstud.equals(""))
                throw new SICEEO_Excepcion (0,"CAMPO_VACIO","'CICLO ESCOLAR'");            
            if (!dm.isInt(txtCicesciniEstud))
                throw new SICEEO_Excepcion (0,"DATO_NUMERICO","'CICLO ESCOLAR'");
            
            if (rbnCurp_isChecked){
                if (txtCurp.trim().length()>=10)
                    condicionQBuskExalum = "a.curp like '"+txtCurp+"%'";
                else
                    throw new SICEEO_Excepcion (0,"TAM_CURP");
            }else if ( rbnIdaluX_isChecked && txtIdalu.trim().length()>0)
                condicionQBuskExalum = "a.IDALU = "+ txtIdalu;
            
            if (condicionQBuskExalum.equals(""))
                throw new SICEEO_Excepcion (-100,"Exit");                       //Solo salir y cancelar las acciones
            
            qryIfx.conectar();
            QCicloActivo = qryIfx.Ciclo();
            QBuskExalum = qryIfx.buscarExalumno(txtCicesciniEstud, condicionQBuskExalum);
            
            if (QBuskExalum.isEmpty() )
                throw new SICEEO_Excepcion (0,"ALUMNO_NO_EXISTE"); //Simplemente que ya no haga nada
            
            /*if ( !QBuskAlum1.get(0).get("estatusalu").equals("BD") && !QBuskAlum1.get(0).get("estatusalu").equals("BT") && !QBuskAlum1.get(0).get("estatusalu").equals("I") )
                dr.put("btnCambSituacion_Enabled", false);
            else
                dr.put("btnCambSituacion_Enabled", true);*/
            
            dr.put("txtIdalu_Text",QBuskExalum.get(0).get("idalu"));
            dr.put("txtCurp_text",QBuskExalum.get(0).get("curp"));
            
            dr.put("tblAlumgrado",QSusEstud=qryIfx.susEstud(""+QBuskExalum.get(0).get("idalu")));
            dr.put("tblMaterias",qryIfx.susMat(""+((ArrayList<Map>)dr.get("tblAlumgrado")).get(((ArrayList<Map>)dr.get("tblAlumgrado")).size()-1).get("cicescini"), ""+QBuskExalum.get(0).get("idalu")));
            
            dr.put("tblFoliosCert",QSusFolios=qryIfx.suFolio(""+QBuskExalum.get(0).get("idalu")));
            
            //===== Ely para extraer el cicescinilib 
            String cicescinilib_ultimo = "",cicescini_ex="";            
            if(QSusFolios.size() > 0) {                
                for (int i=0; i<QSusFolios.size(); i++){
                    if(QSusFolios.get(i).get("grado").equals("3") && QSusFolios.get(i).get("cveplan").equals("2")){
                        cicescinilib_ultimo = ""+QSusFolios.get(i).get("cicescinilib");
                        cicescini_ex = ""+QSusFolios.get(i).get("cicescini");
                    }
                }
            }
            dr.put("cicescinilib_ex", cicescinilib_ultimo);
            dr.put("cicescini_ex", cicescini_ex);
            
            tamHistorial = QSusEstud.size();
            if (QBuskExalum.size()>1)
                throw new SICEEO_Excepcion (0,"MAS_DE_UNO");
            else {
                QEstadisticaDePermisos = qryIfx.estadisticaDePermisosExmExt (""+QBuskExalum.get(0).get("idalu"),txtCicesciniEstud);
                if (!QEstadisticaDePermisos.get("numgradosestud").equals("3") && Integer.parseInt(""+QEstadisticaDePermisos.get("alusolicitud"))==0 )  //Historial secundaria incompleto
                    throw new SICEEO_Excepcion (0,"HISTORIAL_INCOMPLETO");
                if (QEstadisticaDePermisos.get("maxestatusgrado").equals("BD")) //Dado de baja
                    throw new SICEEO_Excepcion (0,"ALUMNO_EN_BAJA");
                if (!(""+sesion.getAttribute("tblPrincipal_distinctIdscct")).contains(""+QEstadisticaDePermisos.get("maxidcct")))        //Está en otra escuela  
                    throw new SICEEO_Excepcion (0,"ESCUELA_NO_PERMITIDA");
                if (QEstadisticaDePermisos.get("maxcicescini").equals(QCicloActivo.get("cicescini"))) //No es exalumno
                    throw new SICEEO_Excepcion (0,"NO_ES_EXALUMNO");
                /*if (!QEstadisticaDePermisos.get("numgradosestud").equals("3"))  //Tiempo agotado
                    throw new SICEEO_Excepcion (0,"MAS_DE_UNO");*/
            }
                        
            
            int numFilas = QBuskExalum.size();
            ArrayList<Map> cbxCurps = new ArrayList<Map>();
            for (int i=0; i<numFilas; i++){
                Map curpEIdalu = new HashMap();
                curpEIdalu.put("idalu",""+QBuskExalum.get(i).get("idalu"));
                curpEIdalu.put("curp",(i+1)+") "+QBuskExalum.get(i).get("curp"));
                cbxCurps.add(curpEIdalu);
            }
            dr.put("cbxCurps", cbxCurps);
            dr.put("lblNombreCompleto", ""+QBuskExalum.get(0).get("nom_tot"));
            dr.put("idaluEncontrado", ""+QBuskExalum.get(0).get("idalu"));
            
            
            dr.put("canEditExmExt", esSuperusuario?true:qryIfx.canEditExmExtExalumno (txtCicesciniEstud, ""+QBuskExalum.get(0).get("idalu"), txtUsuario, this.dm));
            dr.put("canFoliarYFirmar", esSuperusuario?true:qryIfx.canFoliarYFirmarExalumno (txtCicesciniEstud, ""+QBuskExalum.get(0).get("idalu"), txtUsuario, this.dm));
            //dr.put ("cicescinilib", calcuarCicescinilib ());
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Exalumnos(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        return QBuskExalum;
    }
    
    public void tblAlumgrado_MouseClicked (String tblAlumgrado_cicescini, String tblAlumgrado_idalu)
    {
        try {
            qryIfx.conectar();
            dr.put("tblMaterias",qryIfx.susMat(tblAlumgrado_cicescini, tblAlumgrado_idalu));
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnFoliarYFirmarExalumno_Click (String cicesciniEstud, String tblPrincipal_cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, 
            String tblPrincipal_grado, String tblPrincipal_grupo, String idalu, String txtUsuario, boolean esSuperusuario)
    {
        //WsfirmaDatosReturn datosReturn;
        boolean hacerCommit=false;
        String cicescinilib="";
        Map fecExRegular = new HashMap();
        
        try
        {
            if (!tblPrincipal_cveplan.equals("2"))
                throw new SICEEO_Excepcion(0, "NIVEL_EDUC_NO_PERMITITO");
            if (!(""+sesion.getAttribute("tblPrincipal_distinctIdscct")).contains(tblPrincipal_idcct)) //Forzamos a que quien quiere firmar se la escuela donde estdió el alumno.
                throw new SICEEO_Excepcion(0, "ESCUELA_NO_PERMITIDA");
            
            qryIfx.conectarConTransaccion();
            if (!esSuperusuario && !qryIfx.canFoliarYFirmarExalumno (cicesciniEstud, idalu, txtUsuario, this.dm))
                throw new SICEEO_Excepcion(0,"HISTORIAL_INCOMPLETO");
            
            fecExRegular = qryIfx.fechaRegularizacion(idalu,cicesciniEstud,this.dm);
            //Calculamos el ciclo escolar en curso
            cicescinilib = calcuarCicescinilib (fecExRegular);
            
            //Llevamos a cabo el firmado, haciendo mediante el webservice
            
            /*   Comentado por Ely 25-09-2018
            datosReturn = escuelaFirmandoCertCompl(cicesciniEstud, cicescinilib, tblPrincipal_cveplan, tblPrincipal_idcct, tblPrincipal_grupo, idalu, txtUsuario);
            if (datosReturn==null)
                throw new SICEEO_Excepcion (-1,"NO_PUDO_FIRMAR");
            else if (datosReturn.getReturnCase() == 0)
                throw new SICEEO_Excepcion (datosReturn.getReturnCase(), "MENSAJE_WEBSERVICE", datosReturn.getMensaje());
            else if (datosReturn.getReturnCase() == -1)
                throw new SICEEO_Excepcion (datosReturn.getReturnCase(), "ERROR_WEBSERVICE", datosReturn.getMensaje());
            */ // Fin comentado por Ely 25-09-2018
            
            dr.put("tblFoliosCert",qryIfx.suFolio(idalu));
            dr.put("noHasAluCompl", !qryIfx.hayAlumnosParaComplementaria(cicesciniEstud, cicescinilib, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            dr.put("canImprimirCert", canImprimirCertEnModuloExlumnos(idalu, txtUsuario, esSuperusuario));
            this.dr.put("returnCase",1);
            //dr.put("tblAlumnos",qryIfx.getAlumnosParaComplementaria(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            hacerCommit=true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Exalumnos(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnUpdatePromFol_Click (String idalu, String txtUsuario, boolean esSuperusuario)
    {
        try{
            if (!esSuperusuario)
                throw new SICEEO_Excepcion (0,"");
            
            qryIfx.conectar();
            qryIfx.actualizarPromediogralDeFolio (calcuarCicescinilib (), idalu);
            dr.put("tblFoliosCert",qryIfx.suFolio(idalu));
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Exalumnos(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
        
    }
    
    private boolean canImprimirCertEnModuloExlumnos ( String idalu, String txtUsuario, boolean esSuperusuario) throws SQLException, SICEEO_Excepcion, ParseException
    {
        //Calculamos el ciclo escolar en curso
        String cicescinilib = calcuarCicescinilib ();
        int diasTranscurridos;
        Map ciclo = qryIfx.Ciclo ();
        
        if (ciclo.get("cicescini").equals(cicescinilib))                        //Si el ciclo todavía sigue abierto, entonces no es necesario imprimir desde el módulo Exalumnos sino desde el módulo Reportes
            return false;
        
        if (esSuperusuario)
            return true; 
        
        diasTranscurridos = qryIfx.tiempoTranscurridoDeFirmado (cicescinilib, idalu, txtUsuario, dm);
        if (diasTranscurridos>=0 && diasTranscurridos<=4)                       //Si está en el rango de 4 días de haber firmado, entonces puede imprimir su certificado. desde el módulo Exalumnos.
            return true;
        
        return false;
    }
    
    private String calcuarCicescinilib ()
    {
        String cicescinilib="";
        int mesActual, anioActual;
        
        //Calculamos el ciclo escolar en curso
        mesActual = dm.toInt(dm.getFechaHoy ("MM"));
        anioActual = dm.toInt(dm.getFechaHoy ("yyyy"));
        if (mesActual>=7 && mesActual<=12)
            cicescinilib = ""+(anioActual-1);
        else if (mesActual>=1 && mesActual<=2)
            cicescinilib = ""+(anioActual-2);
        
        return cicescinilib;
    }

    private String calcuarCicescinilib (Map fecExRegul)
    {
        String cicescinilib="";
        int mes, anio;
        
        //Calculamos el ciclo escolar en curso
        mes = dm.toInt(fecExRegul.get("mes"));
        anio = dm.toInt(fecExRegul.get("anio"));
        if (mes>=7 && mes<=12)
            cicescinilib = ""+(anio-1);
        else if (mes>=1 && mes<=2)
            cicescinilib = ""+(anio-2);
        
        return cicescinilib;
    }
        
    /*private static WsfirmaDatosReturn escuelaFirmandoCertCompl(String cicescini, String cicescinilib, String cveplan, String idcct, String grupo, String idalus, String usuario) {
        webservices.WSGestionFirma_Service service = new webservices.WSGestionFirma_Service();
        webservices.WSGestionFirma port = service.getWSGestionFirmaPort();
        return port.escuelaFirmandoCertCompl(cicescini, cicescinilib, cveplan, grupo, idalus, idcct, usuario);
    }*/

}
