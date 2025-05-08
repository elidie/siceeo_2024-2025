package modelo.Preinscripcion;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.ClasesGlobales.SICEEO_ValidarCurp;

/* 
    Creado el : 18/11/2015, 01:34:59 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/
public class SICEEO_Preinscripcion {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_Preinscripcion (Map datosReturn)
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
    public boolean ejecutarPeticion (HttpServletRequest request, String metodo)
    {
        this.r = new SICEEO_HttpServletRequest(request);
        
        if (metodo.equals("foAc")){
            formActivate ( r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), 
                    r.gP("cicesciniPreinsc"), r.gP("txtUsuario"), request);
            //FormCreate (r.gP("califCicEscIn"), r.gP("tblAlumCapCalif_grado"), r.gP("tblAlumCapCalif_idalu"));
        }else if (metodo.equals("buAlPr")){
            buscarAlumnoPrimaria ( dm.toInt(r.gP("numLlamada")), r.gP("existeUno"), r.gP("txtNombre"), r.gP("txtPrimerApe"), r.gP("txtSegundoApe"), r.gP("txtCurp16"), 
                    r.gP("txtCurp17y18"), dm.toInt(r.gP("cicesciniPreinsc")), dm.toInt(r.gP("tblPrincipal_cveplan")), r.gP("tblPrincipal_idcct"), 
                    dm.toInt(r.gP("tblPrincipal_grado")), r.gP("numLlamada").equals("0") ? null : dm.vstrToArrMap(r.gPV("filaSelQBuskAlum"), "~", r.gP("nomColsQBuskAlum").split("~")) );
        }else if (metodo.equals("buAlSe")){
            buscarAlumnoSecundaria ( r.gP("txtNombre"), r.gP("txtPrimerApe"), r.gP("txtSegundoApe"), r.gP("txtCurp16"), r.gP("txtCurp17y18"), 
                    dm.toInt(r.gP("cicesciniPreinsc")),dm.toInt(r.gP("tblPrincipal_cveplan")), r.gP("tblPrincipal_idcct"),dm.toInt(r.gP("tblPrincipal_grado")));
        }else if (metodo.equals("guAlPri")){
            GuardarPrimaria(r.gP("alumEstatus"), dm.toInt(r.gP("edadMin")), dm.toInt(r.gP("edadMax")), r.gP("idalu"),r.gP("txtNombre"), r.gP("txtPrimerApe"), 
                    r.gP("txtSegundoApe"), r.gP("txtCurp17y18"), r.gP("fechaNacimiento"), r.gP("cbxGenero"), r.gP("cbxEntidad"), r.gP("tblPrincipal_idcct"), 
                    r.gP("tblPrincipal_modalidad"), dm.toInt(r.gP("tblPrincipal_grado")), r.gP("cicesciniPreinsc"), 
                    /*r.gP("cbxDiscap"), r.gP("cbxKrta"), r.gP("cbxProbem"), r.gP("cbxExtj"),*/ dm.toBool(r.gP("hablaEspaniol")), r.gP("cveLengua"), 
                    r.gP("cbxEtnia"),r.gP("txtUsuario"));
        }else if (metodo.equals("guAlSec")){
            GuardarSecundaria(r.gP("alumEstatus"), dm.toInt(r.gP("edadMin")), dm.toInt(r.gP("edadMax")), r.gP("idalu"),r.gP("txtNombre"), r.gP("txtPrimerApe"), 
                    r.gP("txtSegundoApe"), r.gP("txtCurp17y18"), r.gP("fechaNacimiento"), r.gP("cbxGenero"), r.gP("cbxEntidad"), r.gP("tblPrincipal_idcct"), 
                    r.gP("tblPrincipal_cct"), r.gP("tblPrincipal_modalidad"), dm.toInt(r.gP("tblPrincipal_grado")), r.gP("cicesciniPreinsc"), 
                    r.gP("QBuskAlum_grado"), r.gP("QBuskAlum_maxcicescini"), /*r.gP("cbxDiscap"), r.gP("cbxKrta"), r.gP("cbxProbem"), r.gP("cbxExtj"),*/ 
                    dm.toBool(r.gP("hablaEspaniol")), r.gP("cveLengua"),r.gP("cbxEtnia"),r.gP("txtUsuario"));
        }else if (metodo.equals("guAlPre")){
            GuardarPreescolar(r.gP("numLlamada"), r.gP("alumEstatus"), r.gP("existeUno"), r.gP("txtNombre"), r.gP("txtPrimerApe"), 
                    r.gP("txtSegundoApe"), r.gP("txtCurp17y18Pre"), r.gP("fechaNacimiento"), r.gP("cbxGenero"), r.gP("cbxEntidad"), r.gP("tblPrincipal_idcct"), 
                    r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_cveplan"), dm.toInt(r.gP("tblPrincipal_grado")), r.gP("cicesciniPreinsc"), 
                    /*r.gP("cbxDiscap"), r.gP("cbxKrta"), r.gP("cbxProbem"), r.gP("cbxExtj"),*/ dm.toBool(r.gP("hablaEspaniol")), r.gP("cveLengua"), r.gP("txtUsuario"),
                    r.gP("numLlamada").equals("0") ? null : dm.vstrToArrMap(r.gPV("filaSelQBuskAlum"), "~", r.gP("nomColsQBuskAlum").split("~")),r.gP("cbxEtnia") );
        }else if (metodo.equals("el"))
            eliminarAlumnoPreinscrito (r.gP("idalu"), r.gP("tblPrincipal_idcct"), r.gP("cicesciniPreinsc"), r.gP("tblPrincipal_grado"));
        else if (metodo.equals("acLiPr"))
            actualizaListaDePreinscritos (r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("cicesciniPreinsc"));
        else if (metodo.equals("gePrPaEx"))
            getPreinscParaExcel (r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("cicesciniPreinsc"));
        //else if (metodo.equals("ofPr"))
        //    oficializaPreinscripcion (r.gP("tblPrincipal_idcct"), r.gP("cicesciniPreinsc"), r.gP("txtUsuario"));
        //else if (metodo.equals("dePr"))
        //    desoficializarPreinscripcion (r.gP("tblPrincipal_idcct"), r.gP("cicesciniPreinsc"), esUsrAdmin);
        else if (metodo.equals("peVeReAcOf"))
            return permisosVerReporteAcuseOfic (request);
        return false;    
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void formActivate (String tblPrincipal_idcct, String tblPrincipal_modalidad, String tblPrincipal_cveplan, String tblPrincipal_grado, 
            String cicesciniPreinsc, String txtUsuario, HttpServletRequest request)
    {
        Map QPlanMod, QNormatividad;
        String cveprograma="", edadMin, edadMax;
        HttpSession sesion = request.getSession(false);
        String superUsuario = ""+sesion.getAttribute("superUsuario");
        try
        {
            qryIfx.conectar();
            QPlanMod = qryIfx.getPlanMod(tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_modalidad, cicesciniPreinsc,"20");
            if (!QPlanMod.isEmpty())
                cveprograma=""+QPlanMod.get("cveprograma");
            else
                throw new SICEEO_Excepcion (0,"VERIF_PLAN");
            QNormatividad=qryIfx.getNormatividad(tblPrincipal_cveplan, cveprograma, tblPrincipal_grado);
            
            //  03 DE febrero 2017, lavy pidio que antes de iniciar el ciclo escolar
            //                       el alumnos aun no cumpla 15 años
            if ( (tblPrincipal_modalidad.equals("DES") || tblPrincipal_modalidad.equals("PES") || tblPrincipal_modalidad.equals("DST") || tblPrincipal_modalidad.equals("PST") ) && dm.toInt(tblPrincipal_grado)==1 )
                QNormatividad.put("edadadmmax", ""+(dm.toInt(QNormatividad.get("edadadmmax"))+1));
            
            this.dr.putAll(QNormatividad);                                       //edadMin y edadMax
            edadMin = ""+QNormatividad.get("edadadmmin");
            edadMax = ""+QNormatividad.get("edadadmmax");
            
            this.dr.put("alumEstatus","X"); //sirve para que guardad no haga nada
            
            if ( (tblPrincipal_modalidad.equals("DES") || tblPrincipal_modalidad.equals("PES") || tblPrincipal_modalidad.equals("DST") || tblPrincipal_modalidad.equals("PST") || tblPrincipal_modalidad.equals("DTV") ) && dm.toInt(tblPrincipal_grado)==1 )  //26 de noviembre,
                //this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: de "+edadMin+" a "+edadMax+" años a partir del 1ro. de agosto de "+cicesciniPreinsc+".");
                this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: A partir del 1ro de agosto de "+(dm.toInt(cicesciniPreinsc)-dm.toInt(edadMax))+" hasta el 31 de diciembre de "+(dm.toInt(cicesciniPreinsc)-dm.toInt(edadMin))+".");
            else if (tblPrincipal_modalidad.equals("DBA") || tblPrincipal_modalidad.equals("HMC") )
                this.dr.put("lblRangoEdad","("+cveprograma+") Edad permitida: de 13 a 99 años cumplidos al 31 de diciembre de "+ cicesciniPreinsc+".");
            else if ( tblPrincipal_modalidad.equals("DPR") || tblPrincipal_modalidad.equals("DPB"))
                this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: A partir del 1ro de enero de "+(dm.toInt(cicesciniPreinsc)-dm.toInt(edadMax))+" hasta el 31 de diciembre de "+(dm.toInt(cicesciniPreinsc)-dm.toInt(edadMin))+".");
            else if (tblPrincipal_cveplan.equals("3"))
                this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: A partir del 1ro de enero hasta el 31 de diciembre de "+(dm.toInt(cicesciniPreinsc)-dm.toInt(edadMin))+".");
            else
                this.dr.put("lblRangoEdad","("+cveprograma+") Edad permitida: de "+edadMin+" a "+edadMax+" años cumplidos al 31 de diciembre de "+ cicesciniPreinsc+".");
            
            dr.put("tblPreinscripcion",qryIfx.getPreinscripcion(tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_grado, cicesciniPreinsc));
            dr.put("lenguas",qryIfx.getLenguas());
            dr.put("estados",qryIfx.getEstados());                               //El default seleccionado debe ser 20
            //dr.put("discap",qryIfx.getCatDiscap(cicesciniPreinsc,tblPrincipal_cveplan));             //El default seleccionado debe ser 0
            dr.put("preinscOficializada",qryIfx.isOficializado(tblPrincipal_idcct, cicesciniPreinsc, "", "", "PREINSCRIPCION"));
            
            dr.put("verDesofic",(txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ") || superUsuario.equals("si"))); 
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoSec(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void buscarAlumnoPrimaria (int numLlamada, String existeUno, String txtNombre, String txtPrimerApe, String txtSegundoApe, String txtCurp16, 
            String txtCurp17y18, int cicesciniPreinsc, int tblPrincipal_cveplan, String tblPrincipal_idcct, int tblPrincipal_grado, ArrayList<Map> QBuskAlum)    
    {
        int escalonAct, restDEscalones, restDCiclos;
        
        try 
        {
            if (numLlamada == 0 || numLlamada == 1)
            {
                if (numLlamada == 0)
                {
                    QBuskAlum=new ArrayList<Map>();
                    //buscar al alumno en toda la BD
                    existeUno="x";
                    if (txtCurp16.length()<10)
                        throw new SICEEO_Excepcion (0,"CURP_INCOMPLETA");
                    if (txtCurp16.contains(" "))
                        throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","curp");

                    qryIfx.conectar();
                    if (txtCurp16.trim().length()==16 && txtCurp17y18.trim().length()==2 && existeUno.equals("x"))
                    {
                        QBuskAlum=qryIfx.BuskAlum(5, txtCurp16.trim()+txtCurp17y18.trim(), "", "", "");
                        if ( QBuskAlum.size() == 1 )
                            existeUno="si";
                        else
                            existeUno="no";
                    }

                    if (txtCurp16.trim().length()<=16 && existeUno.equals("x"))
                    {
                        QBuskAlum = qryIfx.BuskAlum(2,txtCurp16, "", "", "");
                        if ( QBuskAlum.size() == 1 ) existeUno="si";
                    }
                    if (txtCurp16.trim().length()>=10 && existeUno.equals("x"))
                    {
                        QBuskAlum = qryIfx.BuskAlum(2,txtCurp16.substring(0,10), "", "", "");
                        if ( QBuskAlum.size() == 1 ) existeUno="si";        // solo para 1er grado
                    }
                    if (txtCurp16.trim().length()==16 && txtCurp17y18.trim().length()==2 && existeUno.equals("x"))
                    {
                        QBuskAlum = qryIfx.BuskAlum(5,txtCurp16+txtCurp17y18, "", "", "");
                        if ( QBuskAlum.size() == 1 ) existeUno="si";       // solo para 1er grado
                    }
                    if (txtCurp16.trim().length()>=10 && txtCurp16.substring(4,10).trim().length()==0 && existeUno.equals("x"))
                    {
                        QBuskAlum = qryIfx.buscarAlumno("(a.curp[1,4]= '"+txtCurp16.substring(0,4)+"' AND a.curp16[11,16] = '"+txtCurp16.substring(10,16)+"')");
                        if ( QBuskAlum.size() == 1 ) existeUno="si";                                                               // solo para 1er grado
                    }
                    
                    if (QBuskAlum.size() >  1)
                        traeDatos (QBuskAlum, existeUno); //k alumno kieres
                }
                
                if ( existeUno.equals("rechazados") )
                    throw new SICEEO_Excepcion (-100,"Exit");                   //Solo salir y cancelar las acciones
                
                escalonAct = 0;
                if (tblPrincipal_cveplan==1){
                    if (tblPrincipal_grado==1) escalonAct = 4;
                    else if (tblPrincipal_grado==2) escalonAct = 5;
                    else if (tblPrincipal_grado==3) escalonAct = 6;
                    else if (tblPrincipal_grado==4) escalonAct = 7;
                    else if (tblPrincipal_grado==5) escalonAct = 8;
                    else if (tblPrincipal_grado==6) escalonAct = 9;
                }

                restDEscalones = -99 ;
                restDCiclos = -88 ;

                if ( QBuskAlum.size() == 1 )
                {
                    if (escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                        restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                    if ( cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                        restDCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                    /*if ( (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                    { //es de la 59
                        if ( cicesciniPreinsc == dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                        {
                            if (escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                                restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"))-1 ;
                            if (cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 )
                                restDCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 ;
                        }
                        else
                        {
                            if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                                 restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                            if ( cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                                 restDCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                        }
                    }
                    else //es de la 22
                    {
                        if (escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                            restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                        if ( cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                             restDCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                    }*/
                }

                // if (dm.Q_BuskAlum.RecordCount =  1) and (dm.Q_Principalgrado.AsInteger =1) then existeUno := 'ok' ; // solo para 1er grado

                if ( QBuskAlum.size() == 1  && restDEscalones>0 && restDCiclos >= restDEscalones && restDEscalones!=-99 && restDCiclos!=-88 && dm.toInt(QBuskAlum.get(0).get("escalon"))<9 )
                    existeUno="ok";

                //-------------------------------------------------------------------------------
                //11/03/2016 quitamos la gandallez
                //if ( QBuskAlum.size() == 1 && (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                //    existeUno="ok";

                if ( !existeUno.equals("ok") )
                {
                    if ( QBuskAlum.size() == 1 )
                    {
                        this.dr.put("curpD10",txtCurp16.substring(0, 10));
                        this.dr.put("kienLlamas", "NEWINGRESOpri");
                        this.dr.put("casoRequerido","winAjustar");
                        this.dr.put("btnLimpiar_Click",true);
                        throw new SICEEO_Excepcion (10,"CHECAR_HISTORIAL");
                    }
                    /*else
                    {
                        if (QBuskAlum.size() >  1)
                            //*if ( tblPrincipal_grado == 1 ) {
                                traeDatos (QBuskAlum, existeUno); //k alumno kieres // solo para 1er grado
                            //*}else
                            //*    throw new SiCEEB_Excepcion (0,"VARIOS_REGISTROS");
                    }*/
                }
            }
            //termina buscar al alumno en toda la BD
            
            if ( existeUno.equals("rechazados") )
                throw new SICEEO_Excepcion (-100,"Exit");                       //Solo salir y cancelar las acciones
            
            //if (existeUno = 'x') then begin  //PARA cuarkier grado, si no existe se agrega como nuevo
            if ( existeUno.equals("x") || existeUno.equals("no") ) {
                existeUno="x";
                this.dr.put("alumEstatus","I");
                throw new SICEEO_Excepcion (1,"ALUMNO_INEXISTENTE");
            }
            
            if (existeUno.equals("ok"))  // and (dm.Q_Principalgrado.AsInteger=1) then //inicia el ifOK
            {
                if ( (txtPrimerApe.equals(QBuskAlum.get(0).get("apepat")) && txtSegundoApe.equals(QBuskAlum.get(0).get("apemat")) && txtNombre.equals(QBuskAlum.get(0).get("nombre"))) || (txtPrimerApe.equals("") && txtSegundoApe.equals("") && txtNombre.equals("") ) )
                {    
                    if ( QBuskAlum.get(0).get("maxcicescini").equals(cicesciniPreinsc) /*&& !(""+QBuskAlum.get(0).get("grupo")).contains("_")*/ )  //11/03/2016
                    {
                        //MessageDlg('ya Existe en en ciclo actual!!'+#13+#10+'en el CCT: '+dm.Q_ynoenCicloActcct.AsString+' Turno: '+dm.Q_ynoenCicloActcveturno.AsString +#13+#10+'en '+dm.Q_ynoenCicloActgrado.AsString+'º Grado, Grupo: '+dm.Q_ynoenCicloActgrupo.AsString+#13+#10+'idAlu: '+dm.Q_ynoenCicloActidalu.AsString, mtWarning, [mbOK], 0);
                        //QYNoEnCicloAct = qryIfx.yNoEnCicloAct(""+QBuskAlum.get(0).get("idalu"), cveunidad);
                        dr.put("curpD10", txtCurp16.substring(0,10));
                        dr.put("idaluX", QBuskAlum.get(0).get("idalu") );
                        dr.put("kienLlamas","NEWINGRESOpri");
                        this.dr.put("casoRequerido","winAjustar");
                        throw new SICEEO_Excepcion (10,"EXISTE_EN_CICLOACT");
                    }
                    //  DM.Q_ynoenCicloAct.CLOSE;
                    dr.put("curpD10", txtCurp16.substring(0,10));
                    dr.put("idaluX", QBuskAlum.get(0).get("idalu"));
                    dr.put("kienLlamas", "NEWINGRESOpri");

                    //Si ya está en la misma escuela y tiene estatusbd='I' no se puede preinscribir
                    if (QBuskAlum.size() == 1  && QBuskAlum.get(0).get("idcctmax").equals(tblPrincipal_idcct) && QBuskAlum.get(0).get("estatusgradomax").equals("I")){
                        dr.put("curpD10", txtCurp16.substring(0,10));
                        dr.put("idaluX", QBuskAlum.get(0).get("idalu") );
                        dr.put("kienLlamas","NEWINGRESOpri");
                        this.dr.put("casoRequerido","winAjustar");
                        throw new SICEEO_Excepcion (10,"NO_PREINSCRIBIBLE_MISMO_CCT");
                    }
                        
                    if ( existeUno.equals("ok") ) 
                    {
                        // dm2.Q_curpValida.open;  cambio 16/oct/2012 maai
                        dr.put("alumEstatus","O"); //ok;
                        dr.put("idalu",QBuskAlum.get(0).get("idalu"));
                        dr.put("curp",QBuskAlum.get(0).get("curp"));
                        dr.put("lblIdalu",QBuskAlum.get(0).get("idalu"));
                        dr.put("txtPrimerApe",QBuskAlum.get(0).get("apepat"));
                        dr.put("txtSegundoApe",QBuskAlum.get(0).get("apemat"));
                        dr.put("txtNombre",QBuskAlum.get(0).get("nombre"));
                        dr.put("cbxGenero",QBuskAlum.get(0).get("sexo"));
                        dr.put("cbxOtrasLenguas",(QBuskAlum.get(0).get("cvelengua").equals("ESP")?"":QBuskAlum.get(0).get("cvelengua")));
                        dr.put("cbxEtnia",QBuskAlum.get(0).get("afromexicana"));
                        dr.put("txtFechaNac",(""+QBuskAlum.get(0).get("fecnac")).substring(0,4)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(5,7)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(8,10));
                        //         BOX_ENT.ItemIndex := DM.Q_BuskAlumcveentfednac.AsInteger;
                        if ( QBuskAlum.get(0).get("cveentfednac").equals("34") )
                            dr.put("cbxEntidad_SelectedIndex", 33);
                        else
                            dr.put("cbxEntidad_SelectedIndex", QBuskAlum.get(0).get("cveentfednac"));

                        dr.put ("txtFechaNac_onBlur","llamarEvento");

                        if ( (""+QBuskAlum.get(0).get("curp")).length()==18 && txtCurp17y18.length()!=2 )
                           dr.put("txtCurp17y18",(""+QBuskAlum.get(0).get("curp")).substring(16));
                        if ( (""+QBuskAlum.get(0).get("curp")).length()!=18 && txtCurp17y18.length()==2 ) 
                        {
                           dr.put("txt18_FontColor","red");
                           //this.dr.put("alumEstatus","U"); // update los 2 digitos de la curp
                        }
                        //--Vista--> if b_guardar.Enabled then
                        //--Vista-->    b_guardar.SetFocus;
                    }
                }
                else
                {
                     this.dr.put("alumEstatus","I"); //Insert
                     throw new SICEEO_Excepcion (1,"ALUMNO_INEXISTENTE");
                }
            } //termina el ifOK
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPri(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } 
        }
    }
    
    private void buscarAlumnoSecundaria (String txtNombre, String txtPrimerApe, String txtSegundoApe, String txtCurp16, String txtCurp17y18, int cicesciniPreinsc, 
            int tblPrincipal_cveplan, String tblPrincipal_idcct, int tblPrincipal_grado)
    {
        int escalonAct, restDEscalones, RestdCiclos;
        String existeUno;
        ArrayList<Map> QBuskAlum = new ArrayList<Map>();
        
        try
        {
            dr.put("alumEstatus","X");                                                                                                               //NOTA: Esta linea de código es mía (Maai), no de Robert.
            //buscar al alumno en toda la BD
            existeUno = "x";
            if (txtCurp16.length()<10)
                throw new SICEEO_Excepcion (0,"CURP_INCOMPLETA");
            escalonAct=0;
            if ( tblPrincipal_cveplan==2 )
            {
                if (tblPrincipal_grado==1 )
                    escalonAct = 10;
                else if ( tblPrincipal_grado==2 )
                    escalonAct = 11;
                else if ( tblPrincipal_grado==3 )
                    escalonAct = 12;
            }
            
            qryIfx.conectar();
            if ( txtCurp16.length()==16 && txtCurp17y18.length()==2 && existeUno.equals("x") )
            {
                QBuskAlum = qryIfx.BuskAlum(5,txtCurp16+txtCurp17y18, "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
                else existeUno = "no";
            }
            if ( txtCurp16.length()<=16 && existeUno.equals("x") )
            {
                QBuskAlum = qryIfx.BuskAlum(2,txtCurp16, "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
            }
            if ( txtCurp16.length()>=10 && existeUno.equals("x") )
            {
                QBuskAlum = qryIfx.BuskAlum(2,txtCurp16.substring(0,10), "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
            }
            if ( txtCurp16.trim().length()>=10 && txtCurp16.substring(4,10).trim().length()==0 && existeUno.equals("x")) {
                QBuskAlum = qryIfx.BuskAlum(4,"(a.curp[1,4]= '"+txtCurp16.trim().substring(0,4)+"' AND a.curp16[11,16] = '"+txtCurp16.trim().substring(10,16)+"'" +")", "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
            }
            
            restDEscalones = -99 ;
            RestdCiclos = -88 ;
            
            if ( QBuskAlum.size()==1 )
            {
                this.dr.put("QBuskAlum_grado", QBuskAlum.get(0).get("grado"));
                this.dr.put("QBuskAlum_maxcicescini", QBuskAlum.get(0).get("maxcicescini"));
                
                if ( escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                    restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                if (cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                    RestdCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                
                /*if ( (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                { //es de la 59
                   if ( cicesciniPreinsc == dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ) 
                   {
                        if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon"))-1 )
                            restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"))-1;
                        if ( cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 )
                            RestdCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 ;
                   } else {
                        if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                            restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                        if ( cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                            RestdCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                   }
               } else {//es de la 22 
                   if ( escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                        restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                   if (cicesciniPreinsc > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                        RestdCiclos = cicesciniPreinsc - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
               }/**/
            }
            
            // if (dm.Q_BuskAlum.RecordCount =  1) and (dm.Q_Principalgrado.AsInteger =1) then existeUno := 'ok' ; // solo para 1er grado
            if ( QBuskAlum.size()==1 &&  restDEscalones>0 && RestdCiclos >= restDEscalones && restDEscalones!=-99 && RestdCiclos!=-88 && dm.toInt(QBuskAlum.get(0).get("escalon"))<12 )
                existeUno = "ok";

            //-------------------------------------------------------------------------------
            //11/03/2016 quitamos la gandallez
            //if ( QBuskAlum.size()==1 && (""+QBuskAlum.get(0).get("grupo")).contains("_") )
            //    existeUno = "ok";
            
            if ( !existeUno.equals("ok")  )
            {
                if ( QBuskAlum.size()==1 )
                {
                    
                    this.dr.put("curpD10",txtCurp16.substring(0,10));
                    this.dr.put("kienLlamas","NEWINGRESOsec");
                    this.dr.put("casoRequerido","winAjustar");
                    this.dr.put("btnLimpiar_Click",true);
                    throw new SICEEO_Excepcion (10,"CHECAR_HISTORIAL");
                } else if ( QBuskAlum.size() > 1)
                    throw new SICEEO_Excepcion (0,"VARIOS_REGISTROS");
            }
            
            //termina buscar al alumno en toda la BD
            
            if ( existeUno.equals("rechazados") )
                throw new SICEEO_Excepcion (-100,"Exit");                       //Solo salir y cancelar las acciones
            
            if ( existeUno.equals("x") || existeUno.equals("no")  )
            {
               existeUno = "x";
               dr.put("alumEstatus","I"); //Insert sino existe el alumno en la base de datos
               throw new SICEEO_Excepcion (1,"ALUMNO_INEXISTENTE");
            }
                        
            if ( existeUno.equals("ok") ) // and (dm.Q_Principalgrado.AsInteger=1) then //inicia el ifOK
            {
               if ( (txtPrimerApe.equals(QBuskAlum.get(0).get("apepat")) && txtSegundoApe.equals(QBuskAlum.get(0).get("apemat")) && txtNombre.equals(QBuskAlum.get(0).get("nombre")) ) || (txtPrimerApe.equals("") && txtSegundoApe.equals("") && txtNombre.equals("")) )
               {
                    if ( QBuskAlum.get(0).get("maxcicescini").equals(cicesciniPreinsc)  /*&&  !(""+QBuskAlum.get(0).get("grupo")).contains("_")*/ ) //11/03/2016
                    {
                        //MessageDlg('ya Existe en en ciclo actual!!'+#13+#10+'en el CCT: '+dm.Q_ynoenCicloActcct.AsString+' Turno: '+dm.Q_ynoenCicloActcveturno.AsString +#13+#10+'en '+dm.Q_ynoenCicloActgrado.AsString+'º Grado, Grupo: '+dm.Q_ynoenCicloActgrupo.AsString+#13+#10+'idAlu: '+dm.Q_ynoenCicloActidalu.AsString, mtWarning, [mbOK], 0);
                        this.dr.put("curpD10",txtCurp16.substring(0,10));
                        this.dr.put("idaluX","DM.Q_ynoenCicloActidalu.AsString");
                        this.dr.put("kienLlamas","NEWINGRESOsec");
                        this.dr.put("casoRequerido","winAjustar");
                        throw new SICEEO_Excepcion (10,"EXISTE_EN_CICLOACT");
                    }
                    //  DM.Q_ynoenCicloAct.CLOSE;
                    this.dr.put("curpD10",txtCurp16.substring(0,10));
                    this.dr.put("idaluX",QBuskAlum.get(0).get("idalu"));
                    this.dr.put("kienLlamas","NEWINGRESOsec");
                    
                    //Si ya está en la misma escuela y tiene estatusbd='I' no se puede preinscribir
                    if (QBuskAlum.size() == 1  && QBuskAlum.get(0).get("idcctmax").equals(tblPrincipal_idcct) && QBuskAlum.get(0).get("estatusgradomax").equals("I")){
                        dr.put("curpD10", txtCurp16.substring(0,10));
                        dr.put("idaluX", QBuskAlum.get(0).get("idalu") );
                        dr.put("kienLlamas","NEWINGRESOpri");
                        this.dr.put("casoRequerido","winAjustar");
                        throw new SICEEO_Excepcion (10,"NO_PREINSCRIBIBLE_MISMO_CCT");
                    }
                    
                    if (existeUno.equals("ok") ) {
                        // dm2.Q_curpValida.open;  cambio 16/oct/2012 maai
                        dr.put("alumEstatus","O"); //ok;
                        dr.put("idalu",QBuskAlum.get(0).get("idalu"));
                        dr.put("curp", QBuskAlum.get(0).get("curp"));
                        dr.put("lblIdalu", QBuskAlum.get(0).get("idalu"));
                        dr.put("txtPrimerApe",QBuskAlum.get(0).get("apepat"));
                        dr.put("txtSegundoApe", QBuskAlum.get(0).get("apemat"));
                        dr.put("txtNombre",QBuskAlum.get(0).get("nombre"));
                        dr.put("cbxGenero", QBuskAlum.get(0).get("sexo"));
                        dr.put("cbxOtrasLenguas",(QBuskAlum.get(0).get("cvelengua").equals("ESP")?"":QBuskAlum.get(0).get("cvelengua")));
                        dr.put("cbxEtnia",QBuskAlum.get(0).get("afromexicana"));
                        dr.put("txtFechaNac",(""+QBuskAlum.get(0).get("fecnac")).substring(0,4)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(5,7)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(8,10));
                        // BOX_ENT.ItemIndex := DM.Q_BuskAlumcveentfednac.AsInteger;
                        if ( dm.toInt(QBuskAlum.get(0).get("cveentfednac"))==34 )
                            dr.put("cbxEntidad_SelectedIndex", 33);
                        else
                            dr.put("cbxEntidad_SelectedIndex", QBuskAlum.get(0).get("cveentfednac"));

                        dr.put ("txtFechaNac_onBlur","llamarEvento");
                        
                        if ( (""+QBuskAlum.get(0).get("curp")).length()==18  &&  txtCurp17y18.length()!= 2 ) 
                            dr.put("txtCurp17y18", (""+QBuskAlum.get(0).get("curp")).substring(16,18));
                        if ( (""+QBuskAlum.get(0).get("curp")).length()!=18 && txtCurp17y18.length()== 2 ) 
                        {
                           dr.put("txtCurp17y18_FontColor", "red");
                           //dr.put("alumEstatus", "U"); // update los 2 digitos de la curp
                        }
                        //--Vista-->  if b_guardar.Enabled then
                        //--Vista-->      b_guardar.SetFocus;
                    }
                } else {
                    dr.put("alumEstatus","I"); //Insert
                    throw new SICEEO_Excepcion (1,"ALUMNO_INEXISTENTE");
                }
            } //termina el ifOK

        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoSec(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void GuardarPreescolar (String numLlamada, String alumEstatus, String existeUno, String txtNombre, String txtPrimerApe, String txtSegundoApe, 
            String txtCurp17y18, String fechaNacimiento, String cbxGenero, String cbxEntidad, String tblPrincipal_idcct, String tblPrincipal_modalidad, 
            String tblPrincipal_cveplan, int tblPrincipal_grado, String cicesciniPreinsc, /*String cbxKrta,*/ 
            boolean hablaEspaniol, String cveLengua, String txtUsuario, ArrayList<Map> QBuskAlum, String etnia) 
    {
        String edadValida, newIdTutor, idalu="", entNac,/* probem="", extj="",  */txtCurp="";
        int edad=0, escalonAct, restDEscalones, restdCiclos, QMaxIdXEsc_libreidalu, QMaximosA_maxlibrea;
        boolean hacerCommit = false;
        Map QMaximosP;
        
        try
        {
            qryIfx.conectarConTransaccion();
            if (numLlamada.equals("0"))
            {
                try { edad = edad(fechaNacimiento,cicesciniPreinsc); }catch (ParseException ex) {  throw new SICEEO_Excepcion (-1,"FECHA_NAC");  }
                edadValida="N";

                this.dr.put("lblEdad","Edad: "+edad);
                if (tblPrincipal_grado == 1){
                    if (edad>= 3 && edad<= 5)
                        edadValida = "S";
                }else if (tblPrincipal_grado == 2){
                    if (edad>= 4 && edad<= 6)
                        edadValida = "S";
                }else if (tblPrincipal_grado == 3){
                    if (edad>= 5 && edad<= 7)
                        edadValida = "S";
                }
                if (!tblPrincipal_modalidad.equals("DML")){                                      // los de educacion especial rebasan la edad permitida
                    if (edadValida.equals("N"))                                     // pero deben de entrar
                        throw new SICEEO_Excepcion (0,"EDAD_INVALIDA");
                }

                if ( txtNombre.contains("  ") )
                    throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","nombre");
                if ( txtPrimerApe.contains("  ") )
                    throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","primer apellido");
                if ( txtSegundoApe.contains("  ") )
                    throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","segundo apellido");
                //++++++++++++++++++++++++++++++++
                if ( txtNombre.contains("|") )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                if ( txtPrimerApe.contains("|") )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
                if ( txtSegundoApe.contains("|") )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
                //++++++++++++++++++++++++++++++++
                if ( txtNombre.trim().length()==0 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","nombre");
                if ( txtPrimerApe.trim().length()==0 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","apellido");
                //++++++++++++++++++++++++++++++++
                String temp;
                if ( !dm.isNombreOApellido (txtNombre.trim(),40) ){
                    if (alumEstatus.equals("I"))                                //Si es nuevesito le cerramos la puerta
                        throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                    else {                                                      //Si ya estaba, lo dejamos pasar sólo si el problema es de acentos
                        temp = txtNombre.trim().toUpperCase().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");  //Descartamos que no sea por motivo de acentos
                        if ( !dm.isNombreOApellido (temp,40) )
                            throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                    }
                }if ( !dm.isNombreOApellido (txtPrimerApe.trim(),30) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
                if ( !txtSegundoApe.equals("") && !dm.isNombreOApellido (txtSegundoApe.trim(),30) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
                //++++++++++++++++++++++++++++++++
                

                //++++++++++++++++++++++++++++++++
                if (txtCurp17y18.contains("O"))
                    throw new SICEEO_Excepcion (0,"2ULT_CURP");            

                if (txtCurp17y18.length()==1)
                    throw new SICEEO_Excepcion (0,"TAM_CURP");      
                try{
                    if (txtCurp17y18.length()==2)
                        Integer.parseInt(txtCurp17y18.substring(1,2));
                }catch(NumberFormatException ex){ throw new SICEEO_Excepcion (0,"DIG_VERIF"); }

                txtCurp = verCurp(txtNombre, txtPrimerApe, txtSegundoApe, fechaNacimiento, cbxGenero, cbxEntidad, txtCurp17y18);
                if (txtCurp.contains(" "))
                    throw new SICEEO_Excepcion (0,"EXISTE_ESPACIO","la CURP");
                //************************************inicia validar k el alumno no exista en la base de datos
                existeUno="x";
                if (txtCurp.length()<16)
                    throw new SICEEO_Excepcion (0,"CURP_MAL_GENERADA");
                
                if (txtCurp.length()>=10 && existeUno.equals("x"))
                {
                    QBuskAlum = qryIfx.BuskAlum(1,txtCurp, txtNombre, txtPrimerApe, txtSegundoApe);
                    if (QBuskAlum.size() == 1 && (QBuskAlum.get(0).get("crip").equals("null") || (""+QBuskAlum.get(0).get("crip")).trim().length()==0))
                        this.dr.put("existeUno", (existeUno="si"));  //;  ok
                    else 
                        this.dr.put("existeUno", (existeUno="no"));
                    //if (dm.Q_BuskAlum.RecordCount > 1) then
                    //   existeUno := 'ok' ;
                }

                //++++++++copiado de primaria inicia

                escalonAct = 0;

                if ( tblPrincipal_cveplan.equals("3") )
                {
                    if ( tblPrincipal_grado == 1 ) escalonAct = 1;
                    else if ( tblPrincipal_grado == 2 ) escalonAct = 2;
                    else if ( tblPrincipal_grado == 3 ) escalonAct = 3;
                }

                restDEscalones = -99;
                restdCiclos = -88;

                if ( QBuskAlum.size() ==  1) 
                {
                    if ( escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                        restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                   if ( dm.toInt(cicesciniPreinsc) > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                        restdCiclos = dm.toInt(cicesciniPreinsc) - dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ;
                    
                   /*if ( (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                    { //es de la 59
                       if ( cicesciniPreinsc.equals(""+QBuskAlum.get(0).get("maxcicescini")) )
                       {
                           if ( escalonAct > (dm.toInt(QBuskAlum.get(0).get("escalon"))-1) )
                                restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"))-1 ;
                           if (dm.toInt(cicesciniPreinsc) > dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 )
                                restdCiclos = dm.toInt(cicesciniPreinsc) - dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 ;
                       }
                       else
                       {
                           if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                                restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                           if ( dm.toInt(cicesciniPreinsc) > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                                restdCiclos = dm.toInt(cicesciniPreinsc) - dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ;
                       }
                    }
                    else //es de la 22
                    {
                        if ( escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                             restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                        if ( dm.toInt(cicesciniPreinsc) > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                             restdCiclos = dm.toInt(cicesciniPreinsc) - dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ;
                    }/**/
                }

                // if (dm.Q_BuskAlum.RecordCount =  1) and (dm.Q_Principalgrado.AsInteger =1) then existeUno := 'ok' ; // solo para 1er grado

                if ( QBuskAlum.size() ==  1 &&  restDEscalones>0 && restdCiclos >= restDEscalones && restDEscalones!=-99 && restdCiclos!=-88 && dm.toInt(QBuskAlum.get(0).get("escalon"))<9 )
                    this.dr.put("existeUno", (existeUno="ok"));

                //-------------------------------------------------------------------------------
                //11/03/2016 quitamos la gandallez
                //if ( QBuskAlum.size()==1 && (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                //    this.dr.put("existeUno", (existeUno="ok"));


                if ( !existeUno.equals("ok") )
                {
                    if ( QBuskAlum.size() ==  1 ) {
                        this.dr.put("curpD10",txtCurp.substring(0,10));
                        this.dr.put("kienLlamas","NEWINGRESOpre");
                        this.dr.put("casoRequerido","winAjustar");
                        this.dr.put("btnLimpiar_Click",true);
                        throw new SICEEO_Excepcion (10,"CHECAR_HISTORIAL");
                    } else {
                        if ( QBuskAlum.size() >  1)
                            if ( tblPrincipal_grado==1 )
                                traeDatos (QBuskAlum, existeUno); //k alumno kieres // solo para 1er grado
                            else
                                throw new SICEEO_Excepcion (0,"VARIOS_REGISTROS");
                    }
                }
            }
            //termina buscar al alumno en toda la BD

            if ( existeUno.equals("ok") )
                idalu = ""+QBuskAlum.get(0).get("idalu");

            if ( existeUno.equals("rechazados") )
                throw new SICEEO_Excepcion (-100,"Exit");                       //Solo salir y cancelar las acciones

            //if (existeUno = 'x') then begin  //PARA cuarkier grado, si no existe se agrega como nuevo
            if ( existeUno.equals("x") || existeUno.equals("no") ) {
                existeUno = "x";
                //   MessageDlg('No existe en la Base de Datos!', mtInformation, [mbOK], 0);
                alumEstatus ="I"; //Insert
                //   exit ;
            }
            
            //++++++++copiado de primaria termina

            //*******************************************termina validar k no exista el alumno
            //v_idalu    := Dm.Q_MaximosAmaxlibrea.AsString ;
            newIdTutor = "1";
            
            
            if ( alumEstatus.equals("I") )
            {
                QMaxIdXEsc_libreidalu = qryIfx.maxidxesc(tblPrincipal_idcct);
                
                if ( QMaxIdXEsc_libreidalu > 0 )
                {
                      idalu = ""+QMaxIdXEsc_libreidalu;
                      qryIfx.actualizaTablaidalus_con_O(idalu, tblPrincipal_idcct);
                } else {
                    QMaximosP = qryIfx.getMaximosP ();
                    QMaximosA_maxlibrea = dm.toInt(qryIfx.getMaximosA ());
                      
                    if ( dm.toInt(QMaximosP.get("maxlibrea")) > QMaximosA_maxlibrea )
                        idalu = ""+QMaximosP.get("maxlibrea");
                    else 
                        idalu = ""+QMaximosA_maxlibrea;
                    
                    qryIfx.actualizaTablaParametros (idalu);
                }

            }
            
            //Si ya está en la misma escuela y tiene estatusbd='I' no se puede preinscribir
            if (QBuskAlum.size() == 1  && QBuskAlum.get(0).get("idcctmax").equals(tblPrincipal_idcct) && QBuskAlum.get(0).get("estatusgradomax").equals("I"))
                throw new SICEEO_Excepcion (0,"NO_PREINSCRIBIBLE_MISMO_CCT");
            
            entNac = cbxEntidad.substring(cbxEntidad.length()-6, cbxEntidad.length()-6+2);
            //if (!cbxProbem.equals("")) probem = ""+cbxProbem.charAt(0);
            //if (!cbxExtJ.equals("")) extj = ""+cbxExtJ.charAt(0);
            
            //para la discapacidad era tomada de la tabla  dm2.q_CatDiscapcvedefsuf.AsString
            qryIfx.insertarAlumnoPreinscrito(alumEstatus, idalu, "20", tblPrincipal_idcct, /*cbxDiscap,*/ newIdTutor, txtCurp, txtNombre, txtPrimerApe, 
                        txtSegundoApe, fechaNacimiento, cbxGenero, /*cbxKrta,*/ entNac, /*probem, extj, */dm.toInt(cicesciniPreinsc), tblPrincipal_grado, "3", 
                        hablaEspaniol,cveLengua, etnia,txtUsuario);
            
            ArrayList<Map> rowsPreinscripcion = new ArrayList<Map>();
            Map rowPreinscipcion = new HashMap();
            rowPreinscipcion.put("idalu", idalu);
            rowPreinscipcion.put("curp", txtCurp);
            rowPreinscipcion.put("nom_tot", txtPrimerApe+" / "+txtSegundoApe+" * "+txtNombre);
            rowPreinscipcion.put("krtacompromiso", "NO");
            //rowPreinscipcion.put("cvedefsufx", cbxDiscap);
            //rowPreinscipcion.put("probem", probem);
            //rowPreinscipcion.put("extranjero", extj);
            rowPreinscipcion.put("hablaespaniol", hablaEspaniol?"SÍ":"NO");
            rowPreinscipcion.put("cveotralengua", cveLengua.equals("ESP")?"":cveLengua);
            rowPreinscipcion.put("etnia", etnia);
            
            rowsPreinscripcion.add(rowPreinscipcion);
            dr.put("rowPreinscripcion",rowsPreinscripcion);
            
            //--Vista--> dr.put("lblAvisoGuard_Visible", true);
            //--Vista--> Box_discap.ItemIndex   := 0; // 13; //lo regresamos al valor de no //
            //--Vista--> Box_krta.ItemIndex     := 0;
            //--Vista--> Box_Probem.ItemIndex   := 0;
            //--Vista--> Box_Extj.ItemIndex     := 0;
            dr.put("alumEstatus", "X");
            //--Vista--> Box_ent.ItemIndex      :=20; //REGRESAMOS la entidad a oaxaca
               //MessageDlg('****************************** Se guardo Correctamente ******************************', mtInformation, [mbOK], 0);
            
            //--Vista--> E_nom.Text  :='';
            //--Vista--> E_apl1.Text :='';
            //--Vista--> E_apl2.Text :='';
            //--Vista--> e_EDAD.Text :='';
            //--Vista--> E_sexo.Text :='';
            //--Vista--> E_CURP.Text :='';
            //--Vista--> E_18.Text   :='';
            //--Vista--> E_Apl1.SetFocus;
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPre(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void GuardarPrimaria (String alumEstatus, int QNormatividad_edadmin, int QNormatividad_edadmax, String idalu, String txtNombre, String txtPrimerApe, 
            String txtSegundoApe, String txtCurp17y18, String fechaNacimiento, String cbxGenero, String cbxEntidad, String tblPrincipal_idcct, 
            String tblPrincipal_modalidad, int tblPrincipal_grado, String cicesciniPreinsc, /*String cbxDiscap, String cbxKrta, 
            String cbxProbem, String cbxExtj,*/ boolean hablaEspaniol, String cveLengua, String etnia, String txtUsuario)
    {
        String edadValida, newIdTutor="", entNac, existeUno/*, probem="", extj=""*/;
        boolean hacerCommit=false;
        String txtCurp16;
        int edad, QMaxIdXEsc_libreidalu, QMaximosA_maxlibrea;
        ArrayList<Map> QBuskAlum = null;
        Map QMaximosP;
        
        try {
            try { edad = edad(fechaNacimiento,cicesciniPreinsc); }catch (ParseException ex) {  throw new SICEEO_Excepcion (-1,"FECHA_NAC");  }
            edadValida = "N";
            this.dr.put("lblEdad","Edad: "+edad);
            if (edad>= QNormatividad_edadmin &&  edad<= QNormatividad_edadmax)
                edadValida = "S";
            if (!tblPrincipal_modalidad.equals("DML") &&                                     // los de educacion especial rebasan la edad permitida
                !tblPrincipal_modalidad.equals("DBA") &&                                     // los de CEBAS rebasan la edad permitida
                !tblPrincipal_modalidad.equals("HMC") && !tblPrincipal_modalidad.equals("HSL") ){
                    if (edadValida.equals("N"))                                 // pero deben de entrar
                        throw new SICEEO_Excepcion (0,"EDAD_INVALIDA");
            }
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.contains("|") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
            if ( txtPrimerApe.contains("|") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
            if ( txtSegundoApe.contains("|") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.trim().length()==0 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","nombre");
            if ( txtPrimerApe.trim().length()==0 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","apellido");
            //++++++++++++++++++++++++++++++++
            if (txtCurp17y18.contains("O"))
                    throw new SICEEO_Excepcion (0,"2ULT_CURP");     
            if ( txtNombre.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","nombre");
            if ( txtPrimerApe.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","primer apellido");
            if ( txtSegundoApe.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","segundo apellido");
            //++++++++++++++++++++++++++++++++
            String temp;
            if ( !dm.isNombreOApellido (txtNombre.trim(),40) ){
                if (alumEstatus.equals("I"))                                    //Si es nuevesito le cerramos la puerta
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                else {                                                          //Si ya estaba, lo dejamos pasar sólo si el problema es de acentos
                    temp = txtNombre.trim().toUpperCase().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");  //Descartamos que no sea por motivo de acentos
                    if ( !dm.isNombreOApellido (temp,40) )
                        throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                }
            }if ( !dm.isNombreOApellido (txtPrimerApe.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
            if ( !txtSegundoApe.equals("") && !dm.isNombreOApellido (txtSegundoApe.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
            //++++++++++++++++++++++++++++++++
            
            if (txtCurp17y18.length()==1)
                throw new SICEEO_Excepcion (0,"TAM_CURP");      
            try{
                if (txtCurp17y18.length()==2)
                    dm.toInt(txtCurp17y18.substring(1,2));
            }catch(NumberFormatException ex){ throw new SICEEO_Excepcion (0,"DIG_VERIF"); }
            
            if (cbxEntidad.equals(""))
                throw new SICEEO_Excepcion (0,"ENTIDAD");
            if(fechaNacimiento.length() !=10)           
                throw new SICEEO_Excepcion (-1,"FORMATO_FECHA_NAC");  
            
            txtCurp16 = verCurp(txtNombre, txtPrimerApe, txtSegundoApe, fechaNacimiento, cbxGenero, cbxEntidad, txtCurp17y18);
            
            if (alumEstatus.equals("X"))//&& grado==1)                      // solo para primer grado
                throw new SICEEO_Excepcion (0,"BUSCAR");
            //************************************inicia validar k el alumno no exista en la base de datos
            existeUno = "x";
            if (txtCurp16.length()<10)
                throw new SICEEO_Excepcion (0,"CURP_INCOMPLETA");
            
            qryIfx.conectarConTransaccion();
            if (txtCurp16.length()>=10 && existeUno.equals("x"))
                QBuskAlum = qryIfx.BuskAlum(1,txtCurp16, txtNombre, txtPrimerApe, txtSegundoApe);
            
            if (QBuskAlum.size()<1)  // busco y no encontro
                dr.put("alumEstatus", alumEstatus="I");
             
             //*******************************************termina validar k no exista el alumno
             
            if (!alumEstatus.equals("X"))
            {
                //if (alumEstatus.equals("U")){   //U = actualiza la curp con los 2 ultimos digitos
                if ( !QBuskAlum.isEmpty() && (""+QBuskAlum.get(0).get("curp")).trim().length()==16 && txtCurp17y18.trim().length()==2 )
                    qryIfx.actualiza2ultDigCurp ( txtCurp17y18,  tblPrincipal_idcct,  txtUsuario,  idalu);

                /* Ya no se usará
                if (alumEstatus.equals("O")){  //0= OK, SE ENCONTRO un alumno
                    if ( !QBuskAlum.get(0).get("estatusalu").equals("I") )
                        qryIfx.setEstatusalu_Alumno(tblPrincipal_idcct, txtUsuario, idalu);
                }
                */
                if (alumEstatus.equals("I")){
                    newIdTutor = "1";
                    QMaxIdXEsc_libreidalu = qryIfx.maxidxesc(tblPrincipal_idcct);
                    if (QMaxIdXEsc_libreidalu>0){
                        idalu = ""+QMaxIdXEsc_libreidalu;
                        qryIfx.actualizaTablaidalus_con_O(idalu, tblPrincipal_idcct);
                    }else{
                        QMaximosP=qryIfx.getMaximosP();
                        QMaximosA_maxlibrea = dm.toInt(qryIfx.getMaximosA ());
                        if (dm.toInt(QMaximosP.get("maxlibrea")) > QMaximosA_maxlibrea )
                            idalu = ""+QMaximosP.get("maxlibrea");
                       else 
                            idalu = ""+QMaximosA_maxlibrea;
                       qryIfx.actualizaTablaParametros(idalu);
                    }
                }
                entNac= cbxEntidad.substring(cbxEntidad.length()-6, cbxEntidad.length()-6+2);
                //if (!cbxProbem.equals(""))  probem = ""+cbxProbem.charAt(0);
                //if (!cbxExtj.equals("")) extj = ""+cbxExtj.charAt(0);
                
                //*******************************************************************************************
                qryIfx.insertarAlumnoPreinscrito(alumEstatus, idalu, "20", tblPrincipal_idcct, /*cbxDiscap, */newIdTutor, txtCurp16, txtNombre, txtPrimerApe, 
                        txtSegundoApe, fechaNacimiento, cbxGenero, /*cbxKrta,*/ entNac, /*probem, extj,*/ dm.toInt(cicesciniPreinsc), tblPrincipal_grado, "1", 
                        hablaEspaniol,cveLengua,etnia,txtUsuario);

                ArrayList<Map> rowsPreinscripcion = new ArrayList<Map>();
                Map rowPreinscipcion = new HashMap();
                rowPreinscipcion.put("idalu", idalu);
                rowPreinscipcion.put("curp", txtCurp16);
                rowPreinscipcion.put("nom_tot", txtPrimerApe+" / "+txtSegundoApe+" * "+txtNombre);
                //rowPreinscipcion.put("krtacompromiso", cbxKrta);
                //rowPreinscipcion.put("cvedefsufx", cbxDiscap);
                //rowPreinscipcion.put("probem", probem);
                //rowPreinscipcion.put("extranjero", extj);
                rowPreinscipcion.put("hablaespaniol", hablaEspaniol?"SÍ":"NO");
                rowPreinscipcion.put("cveotralengua", cveLengua.equals("ESP")?"":cveLengua);
                rowPreinscipcion.put("etnia", etnia);
                rowsPreinscripcion.add(rowPreinscipcion);
                dr.put("rowPreinscripcion",rowsPreinscripcion);
                
                //--Vista--> L_avisoGuard.Visible:= True;
                //--Vista--> Box_discap.ItemIndex   := 0; //14;
                //--Vista--> Box_krta.ItemIndex     := 0;
                //--Vista--> Box_Probem.ItemIndex   := 0;
                //--Vista--> Box_Extj.ItemIndex     := 0;
                //MessageDlg('****************************** Se guardo Correctamente ******************************', mtInformation, [mbOK], 0);
                //--Vista--> E_nom.Text  :='';
                //--Vista--> E_apl1.Text :='';
                //--Vista--> E_apl2.Text :='';
                //--Vista--> e_EDAD.Text :='';
                //--Vista--> E_sexo.Text :='';
                //--Vista--> E_CURP.Text :='';
                //--Vista--> E_18.Text   :='';
                this.dr.put("idalu","");
                //--Vista--> E_18.Font.Color :=clBlack ;
                //--Vista--> L_curp.Caption:='';
                //--Vista--> E_CURP.SetFocus;
                this.dr.put("alumEstatus", "X");
                //--Vista--> l_idalu.Caption:='';
                hacerCommit=true;
            }
        }catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPri(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { }
    }
    
    private void GuardarSecundaria (String alumEstatus, int QNormatividad_edadmin, int QNormatividad_edadmax, String idalu, String txtNombre, String txtPrimerApe,
            String txtSegundoApe, String txtCurp17y18, String fechaNacimiento, String cbxGenero, String cbxEntidad, String tblPrincipal_idcct, 
            String tblPrincipal_cct, String tblPrincipal_modalidad, int tblPrincipal_grado, String cicesciniPreinsc, String QBuskAlum_grado, 
            String QBuskAlum_maxcicescini, /*String cbxDiscap, String cbxKrta, String cbxProbem, String cbxExtj, */boolean hablaEspaniol, String cveLengua, 
            String etnia, String txtUsuario)
    {
        String edadValida, newIdTutor="", entNac="",existeUno/*, probem="", extj=""*/;
        boolean hacerCommit=false;
        String txtCurp;
        int edad, QMaxIdXEsc_libreidalu;
        ArrayList<Map> QBuskAlum = null;
        
        try {
            edadValida = "N";
            try { edad = edad(fechaNacimiento,cicesciniPreinsc); }catch (ParseException ex) {  throw new SICEEO_Excepcion (-1,"FECHA_NAC");  }
            this.dr.put("lblEdad","Edad: "+edad);
            if (edad >= QNormatividad_edadmin &&  edad <= QNormatividad_edadmax)
                edadValida = "S";
            
            //si termino primaria, puede continuar aun sin tener la edad permitida
            if ( tblPrincipal_grado==1 && dm.toInt(QBuskAlum_grado)==6 && dm.toInt(QBuskAlum_maxcicescini) < dm.toInt(cicesciniPreinsc) )//lo keremos en 1ro de secundaria y viene de 6to de primaria del un ciclo anterior
                edadValida = "S";
   
            if (!tblPrincipal_modalidad.equals("DML") && // los de educacion especial rebasan la edad permitida
                !tblPrincipal_modalidad.equals("DBA") && // los de CEBAS rebasan la edad permitida
                !tblPrincipal_modalidad.equals("HMC") && !tblPrincipal_modalidad.equals("HSL") && !tblPrincipal_cct.equals("20DTV0053O")){  // ESTA DTV solo atiende a alumnos del PENAL
                    if (edadValida.equals("N"))                                 // pero deben de entrar
                        throw new SICEEO_Excepcion (0,"EDAD_INVALIDA");
            }
            
            if (txtCurp17y18.contains("O"))
                throw new SICEEO_Excepcion (0,"2ULT_CURP");
            if ( txtNombre.indexOf("  ")>0 )
                throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","nombre");
            if ( txtPrimerApe.indexOf("  ")>0 )
                throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","apellido");
            if ( txtSegundoApe.indexOf("  ")>0 )
                throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","apellido");
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.contains("|") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
            if ( txtPrimerApe.contains("|") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
            if ( txtSegundoApe.contains("|") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.trim().length()==0 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","nombre");
            if ( txtPrimerApe.trim().length()==0 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","apellido");
            //++++++++++++++++++++++++++++++++
            String temp;
            if ( !dm.isNombreOApellido (txtNombre.trim(),40) ){
                if (alumEstatus.equals("I"))                                    //Si es nuevesito le cerramos la puerta
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                else {                                                          //Si ya estaba, lo dejamos pasar sólo si el problema es de acentos
                    temp = txtNombre.trim().toUpperCase().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");  //Descartamos que no sea por motivo de acentos
                    if ( !dm.isNombreOApellido (temp,40) )
                        throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                }
            }if ( !dm.isNombreOApellido (txtPrimerApe.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
            if ( !txtSegundoApe.equals("") && !dm.isNombreOApellido (txtSegundoApe.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
            //++++++++++++++++++++++++++++++++
            if (txtCurp17y18.length()==1)
                throw new SICEEO_Excepcion (0,"TAM_CURP");
            
            try {
                if (txtCurp17y18.length()==2)
                    Integer.parseInt(txtCurp17y18.substring(1,2));
            } catch(NumberFormatException ex){ throw new SICEEO_Excepcion (0,"DIG_VERIF"); }
            
            /************  Validar la longitud de la fecha de nacimiento  ************/
            if(fechaNacimiento.length() != 10)
                throw new SICEEO_Excepcion (-1,"FORMATO_FECHA_NAC");
            
            txtCurp = verCurp(txtNombre, txtPrimerApe, txtSegundoApe, fechaNacimiento, cbxGenero, cbxEntidad, txtCurp17y18);
            
            if (alumEstatus.equals("X") )//&& grado==1                          //solo primer grado
                throw new SICEEO_Excepcion (0,"BUSCAR");
            
            //************************************inicia validar k el alumno no exista en la base de datos
            
            existeUno = "x";
            if (txtCurp.length()<10)
                throw new SICEEO_Excepcion (0,"CURP_INCOMPLETA");
            
            qryIfx.conectarConTransaccion();
            if (txtCurp.length()>=10 && existeUno.equals("x")) {
                QBuskAlum = qryIfx.BuskAlum(1,txtCurp, txtNombre, txtPrimerApe, txtSegundoApe);
                if (QBuskAlum.size() == 1 && (""+QBuskAlum.get(0).get("crip")).trim().length()==0) //pos 36:crip
                    existeUno = "ok" ;
                else if (QBuskAlum.size() > 1)
                    existeUno = "ok";
            }
            
            if (QBuskAlum.size()<1)  // busco y no encontro
                this.dr.put("alumEstatus", (alumEstatus="I") );
            
            //*******************************************termina validar k no exista el alumno
            if (cbxEntidad.equals(""))
                throw new SICEEO_Excepcion (0,"SEL_ENTIDAD");
            
            //-------------------------------------------------------------------------------------------------
            if (!alumEstatus.equals("X"))
            {
                //if (alumEstatus.equals("U")){                                   //U = actualiza la curp con los 2 ultimos digitos
                if (  !QBuskAlum.isEmpty() && (""+QBuskAlum.get(0).get("curp")).trim().length()==16 && txtCurp17y18.trim().length()==2 )
                    qryIfx.actualiza2ultDigCurp (txtCurp17y18, tblPrincipal_idcct, txtUsuario, idalu);
                    
                /*else if (alumEstatus.equals("O")){                             //0= OK, SE ENCONTRO un alumno
                    if (!QBuskAlum.get(0).get("estatusalu").equals("I"))
                        qryIfx.setEstatusalu_Alumno (tblPrincipal_idcct, txtUsuario, idalu);
                 
                }*/else if (alumEstatus.equals("I")){                             //no se encontro el alumno y hay k insertarlo
                    newIdTutor = "1";
                    QMaxIdXEsc_libreidalu = qryIfx.maxidxesc(tblPrincipal_idcct);
                    if (QMaxIdXEsc_libreidalu>0)
                    {
                        this.dr.put("idalu", (idalu=""+QMaxIdXEsc_libreidalu) );
                        qryIfx.actualizaTablaidalus_con_O (idalu, tblPrincipal_idcct);
                    }
                    else {
                        int QMaximosA_maxlibrea = dm.toInt(qryIfx.getMaximosA());
                        Map QMaximosP = qryIfx.getMaximosP();
                        if ( dm.toInt(QMaximosP.get("maxlibrea"))> QMaximosA_maxlibrea )
                            this.dr.put("idalu", (idalu=""+QMaximosP.get("maxlibrea")));
                        else
                            this.dr.put("idalu", (idalu=""+QMaximosA_maxlibrea));
                        qryIfx.actualizaTablaParametros (idalu);
                   }
                }
                
                entNac= cbxEntidad.substring(cbxEntidad.length()-6, cbxEntidad.length()-6+2);
                //if (!cbxProbem.equals("")) probem = ""+cbxProbem.charAt(0);
                //if (!cbxExtj.equals("")) extj = ""+cbxExtj.charAt(0);
                
                qryIfx.insertarAlumnoPreinscrito(alumEstatus, idalu, "20", tblPrincipal_idcct,/* cbxDiscap, */newIdTutor, txtCurp, txtNombre, txtPrimerApe, 
                        txtSegundoApe, fechaNacimiento, cbxGenero, /*cbxKrta,*/ entNac, /*probem, extj,*/ dm.toInt(cicesciniPreinsc), tblPrincipal_grado, "2", 
                        hablaEspaniol,cveLengua, etnia,txtUsuario);

                ArrayList<Map> rowsPreinscripcion = new ArrayList<Map>();
                Map rowPreinscipcion = new HashMap();
                rowPreinscipcion.put("idalu", idalu);
                rowPreinscipcion.put("curp", txtCurp);
                rowPreinscipcion.put("nom_tot", txtPrimerApe+" / "+txtSegundoApe+" * "+txtNombre);
                //rowPreinscipcion.put("krtacompromiso", cbxKrta);
                //rowPreinscipcion.put("cvedefsufx", cbxDiscap);
                //rowPreinscipcion.put("probem", probem);
                //rowPreinscipcion.put("extranjero", extj);
                rowPreinscipcion.put("hablaespaniol", hablaEspaniol?"SÍ":"NO");
                rowPreinscipcion.put("etnia", etnia);
                rowPreinscipcion.put("cveotralengua", cveLengua.equals("ESP")?"":cveLengua);
                rowsPreinscripcion.add(rowPreinscipcion);
                dr.put("rowPreinscripcion",rowsPreinscripcion);
                
                //MessageDlg('****************************** Se guardo Correctamente ******************************', mtInformation, [mbOK], 0);
                //--Vista--> L_avisoGuard.Visible:= True;
                //--Vista--> Box_discap.ItemIndex   := 0; //14;
                //--Vista--> Box_krta.ItemIndex     := 0;
                //--Vista--> Box_Probem.ItemIndex   := 0;
                //--Vista--> Box_Extj.ItemIndex     := 0;
                //--Vista--> E_nom.Text  :='';
                //--Vista--> E_apl1.Text :='';
                //--Vista--> E_apl2.Text :='';
                //--Vista--> e_EDAD.Text :='';
                //--Vista--> E_sexo.Text :='';
                //--Vista--> E_CURP.Text :='';
                //--Vista--> E_18.Text   :='';
                this.dr.put("idalu","");
                //--Vista--> E_18.Font.Color :=clBlack ;
                //--Vista--> L_curp.Caption:='';
                //--Vista--> E_CURP.SetFocus;
                this.dr.put("alumEstatus", "X");
                //--Vista--> l_idalu.Caption:='';
            }
            //*******************************************termina validar k no exista el alumno
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoSec(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){  this.dr.put("returnCase", -1);   mensaje.General("GENERAL", ex.getMessage(), "", this.dr);   }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit); } catch (SQLException ex) { } }
    }
    
    private void traeDatos (ArrayList<Map> QBuskAlum, String existeUno) throws SICEEO_Excepcion
    {
        ArrayList<Map> curpsRep = new ArrayList<Map>();
        
        //----- Extraemos las curps repetidas -----
        int numCurpsRep = QBuskAlum.size();
        for (int i=0; i<numCurpsRep; i++){
            Map idaluCurpYNombre = new HashMap ();
            idaluCurpYNombre.put("curp", QBuskAlum.get(i).get("curp"));
            idaluCurpYNombre.put("nom_tot", QBuskAlum.get(i).get("nom_tot"));
            idaluCurpYNombre.put("idalu", QBuskAlum.get(i).get("idalu"));
            curpsRep.add(idaluCurpYNombre);
        }
        //-----------------------------------------
        this.dr.put("tblCurpsRep",curpsRep);
        this.dr.put("QBuskAlum",QBuskAlum);
        this.dr.put("existeUno",existeUno);
        this.dr.put("casoRequerido","winMasDeUno");                             //Esta ventana debe regresar "ok" o "rechazados";
        throw new SICEEO_Excepcion (2,"PASAR_A_NUMLLAMADA=1","retornar: existeUno y idalu");
    }
    
    private void eliminarAlumnoPreinscrito (String idalu, String tblPrincipal_idcct, String cicesciniPreinsc, String tblPrincipal_grado)
    {
        boolean hacerCommit = false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.eliminarAlumnoPreinscrito(idalu, tblPrincipal_idcct, cicesciniPreinsc, tblPrincipal_grado);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void actualizaListaDePreinscritos (String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String cicesciniPreinsc)
    {
        try
        {
            qryIfx.conectar();
            dr.put("tblPreinscripcion",qryIfx.getPreinscripcion(tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_grado, cicesciniPreinsc));
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void getPreinscParaExcel (String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String cicesciniPreinsc)
    {
        try
        {
            qryIfx.conectar();
            dr.put("tblPreinscripcion",qryIfx.getPreinscripcionParaExcel(tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_grado, cicesciniPreinsc));
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    /*private void oficializaPreinscripcion (String tblPrincipal_idcct, String cicesciniPreinsc, String txtUsuario)
    {
        try
        {
            qryIfx.conectar();
            qryIfx.oficializar(tblPrincipal_idcct, cicesciniPreinsc, "", "", "PREINSCRIPCION", null, txtUsuario);
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }*/
    
    /* Ya no se usa, borrar si despues de un tiempo ya de plano no se usa (01Jun17)
    private void desoficializarPreinscripcion(String tblPrincipal_idcct, String cicesciniPreinsc, boolean isUsrAdmin) {
        try
        {
            qryIfx.conectar();
            qryIfx.desoficializar(tblPrincipal_idcct, cicesciniPreinsc, "PREINSCRIPCION", isUsrAdmin);
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }*/
    
    private int edad(String fechaNacimiento,String cicescini) throws ParseException
    {
        /*String FechaNac,FechaAct;
        FechaNac = dm.invFecha(fechaNacimiento, "/");
        FechaAct = "31/12/"+cicescini;
        return (int)dm.getDifFechas(FechaNac, FechaAct, 0);*/
        
        int anioNac, anioAct;
        anioNac = dm.toInt(fechaNacimiento.substring(0,4));
        anioAct = dm.toInt(cicescini);
        
        if (anioAct<anioNac)
            return anioAct-anioNac-1;
        else
            return anioAct-anioNac;
    }
    
    private String verCurp(String nombre, String primerApe, String segundoApe, String fechaNacimiento, String sexo, String entidad, String digVerif) 
    {               
        SICEEO_ValidarCurp validarCurp = new SICEEO_ValidarCurp(nombre, primerApe, segundoApe, fechaNacimiento.substring(2,4), fechaNacimiento.substring(5,7), fechaNacimiento.substring(8,10), sexo, entidad.substring(entidad.length()-2,entidad.length()));
        return validarCurp.curp() + digVerif;
    }
    
    public boolean permisosVerReporteAcuseOfic (HttpServletRequest request)
    {
        HttpSession sesion = request.getSession(false);
        String tipo_usuario = ""+sesion.getAttribute("tipo_usuario");
        String usuario =  ""+sesion.getAttribute("userName");

        if (tipo_usuario.equals(" ") || tipo_usuario.equals("consulta") || tipo_usuario.equals("mesa") || tipo_usuario.equals("captura") || usuario.equals("IVALLE") || usuario.equals("LPOBLETE") || usuario.equals("JULIANCRUZ"))
            return true;
        else                                                                                                                                                      // cuando es un usuario tipo CCT
        {
            if (tipo_usuario.equals(request.getParameter("idcct")))
                return true;
            else
                return false;
        }
    }
}
