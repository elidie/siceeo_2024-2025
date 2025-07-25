package modelo.nuevoIngreso;

import modelo.DAO.SICEEO_QueriesInformix;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.*;

/**
 *
 * @author dai
 */
public class SICEEO_NewIngresoSec {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
        
    public SICEEO_NewIngresoSec ( Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm =  new SICEEO_DataModule();
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
        if(metodo.equals("foAc"))
            formActivate(r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), Integer.parseInt(r.gP("tblPrincipal_grado")), r.gP("tblPrincipal_grupo"), r.gP("tblPrincipal_modalidad"), 
                    r.gP("tblPrincipal_cicescini"));
        else if(metodo.equals("buAl"))
            buscarAlumno(r.gP("txtCurp"), dm.toInt(r.gP("tblPrincipal_grado")), dm.toInt(r.gP("tblPrincipal_cveplan")), dm.toInt(r.gP("cicescini")), 
                    r.gP("txt18"), r.gP("primerApe"), r.gP("segundoApe"), r.gP("nombre"), r.gP("cicescini"), r.gP("cct")    
            );
        else if(metodo.equals("gu")) {
            Guardar(r.gP("txtNombre"), r.gP("txtApe1"), r.gP("txtApe2"), r.gP("cbxSexo"), r.gP("cbxEntidad"), dm.toInt(r.gP("tblPrincipal_grado")), 
                    r.gP("fechaNacimiento"), r.gP("tblPrincipal_modalidad"), r.gP("txt18"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveturno"), 
                    r.gP("cicescini"), r.gP("tblPrincipal_grupo"), r.gP("cbxDiscap"), r.gP("cbxKrta"), r.gP("cbxProbem"), r.gP("cbxExtJ"), r.gP("alumEstatus"), 
                    dm.toInt(r.gP("edadMin")), dm.toInt(r.gP("edadMax")), r.gP("idalu"), r.gP("tblPrincipal_cct"),r.gP("taller") ,r.gP("arte"), dm.toInt(""),
                    r.gP("QBuskAlum_grado"), r.gP("QBuskAlum_maxCicEscIni"), r.gP("QPlanMod_cveprograma"), 
                    ""+sesion.getAttribute("userName"),r.gP("cbxLenguas"), r.gP("cbxEtnia"), r.gP("fechaIngreso")
            );                   
            /*btnChekMat_Click ("false", r.gP("cicescini"), "1", r.gP("tblPrincipal_idcct"), r.gP("idalu"), 
                r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("cicescini"), "2", ""+sesion.getAttribute("userName"));*/
        }
    }
/* ******************************************************************************/
/* ******************************************************************************/
/* ******************************************************************************/
    private void Guardar (String txtNombre, String txtApe1, String txtApe2, String cbxSexo, String cbxEntidad, int tblPrincipal_grado, String fechaNacimiento, 
            String tblPrincipal_modalidad, String txtCurp17y18, String tblPrincipal_idcct, String tblPrincipal_cveturno, String cicescini, String tblPrincipal_grupo, 
            String cbxDiscap, String cbxKrta, String cbxProbem, String cbxExtJ, String alumEstatus, int QNormatividad_edadmin, int QNormatividad_edadmax,
            String idalu, String tblPrincipal_cct, String taller, String arte, int cbxArtes_length, 
            String QBuskAlum_grado, String QBuskAlum_maxcicescini, String QPlanMod_cveprograma, String txtUsuario,String cbxLengua, String cbxEtnia, String fechaIngreso)
    {
        String edadValida, newIdTutor="", entNac="",existeUno,Probem="", Extj="", cveDis;
        boolean hacerCommit=false, cambDeEscu = false;
        String txtCurp, curpRaiz="", fechaIng="",fecini;
        int edad, QMaxIdXEsc_libreidalu;
        ArrayList<Map> QBuskAlum = null;
        ArrayList<Map> datosDiscap;
        Map planMod;
        int numFilas;
        ArrayList<Map> QSusMaterias, QSusEval, QMatCalif, tblMatCalifXBim; 

        
        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            edadValida = "N";
            
            qryIfx.conectarConTransaccion();
            if(!qryIfx.isValidaCapacidadGpo(cicescini, cicescini, ""+tblPrincipal_idcct, ""+tblPrincipal_grado, tblPrincipal_grupo))     //agregado para validad la capacidad limite del grupo                   
                throw new SICEEO_Excepcion (0,"CAPGPO_INVALIDA");    //agregado para validad la capacidad limite del grupo
                        
            try { edad = edad(fechaNacimiento,cicescini); }catch (ParseException ex) {  throw new SICEEO_Excepcion (-1,"FECHA_NAC");  }
            this.dr.put("lblEdad","Edad: "+edad);
            if (edad >= QNormatividad_edadmin &&  edad <= QNormatividad_edadmax)
                edadValida = "S";
            
            //si termino primaria, puede continuar aun sin tener la edad permitida
            if ( tblPrincipal_grado==1 && dm.toInt(QBuskAlum_grado)==6 && dm.toInt(QBuskAlum_maxcicescini) < dm.toInt(cicescini) )//lo keremos en 1ro de secundaria y viene de 6to de primaria del un ciclo anterior
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
            if ( txtApe1.indexOf("  ")>0 )
                throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","apellido");
            if ( txtApe2.indexOf("  ")>0 )
                throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","apellido");
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.contains("|") || txtNombre.contains("*") || txtNombre.contains("/") || txtNombre.contains("-") || txtNombre.contains("_") || txtNombre.contains(".") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
            if ( txtApe1.contains("|") || txtApe1.contains("*") || txtApe1.contains("/") || txtApe1.contains("-") || txtApe1.contains("_") || txtApe1.contains(".") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
            if ( txtApe2.contains("|") || txtApe2.contains("*") || txtApe2.contains("/") || txtApe2.contains("-") || txtApe2.contains("_") || txtApe2.contains(".") )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.trim().length()<=1 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","nombre");
            if ( txtApe1.trim().length()<=1 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","apellido");
            //++++++++++++++++++++++++++++++++                                  //Condición agregada en SICEEO
            if ( !dm.isNombreOApellido (txtNombre.trim(),40) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
            if ( !dm.isNombreOApellido (txtApe1.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
            if ( !txtApe2.equals("") && !dm.isNombreOApellido (txtApe2.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
            //++++++++++++++++++++++++++++++++
            if (txtCurp17y18.length()==1)
                throw new SICEEO_Excepcion (0,"TAM_CURP");
            try{
                if (txtCurp17y18.length()==2)
                    Integer.parseInt(txtCurp17y18.substring(1,2));
            } catch(NumberFormatException ex){ throw new SICEEO_Excepcion (0,"DIG_VERIF"); }
            
            if ( fechaIngreso.trim().length()<=1 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","dato de Fecha Ingreso");
            /*if (fechaIngreso.equals("") || fechaIngreso.equals("null"))
                fechaIngreso="2024/08/26";*/
            if (fechaIngreso.length()>0) {
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

                if(!fechaIngreso.substring(4, 5).equals("/") || !fechaIngreso.substring(7, 8).equals("/"))                        
                    throw new SICEEO_Excepcion (0,"FECHA_FOR_INCORR");
                fechaIng = fechaIngreso;
                fecini = ""+sesion.getAttribute("feciniciclo");                 
                Date fecha1 = formato.parse(fechaIng.replace('/', '-'));
                Date fecha2 = formato.parse(fecini);
                
                if( fecha1.before(fecha2))
                    throw new SICEEO_Excepcion (0,"FECHA_NO_VALIDO");
            }
            
            txtCurp = verCurp(txtNombre, txtApe1, txtApe2, fechaNacimiento, cbxSexo, cbxEntidad, txtCurp17y18);
            curpRaiz = txtCurp + txtCurp17y18;
            
            if (curpRaiz.contains(" "))
                    throw new SICEEO_Excepcion (0,"EXISTE_ESPACIO","la CURP");
            if (alumEstatus.equals("X") )//&& grado==1                          //solo primer grado
                throw new SICEEO_Excepcion (0,"BUSCAR");
            
            //************************************inicia validar k el alumno no exista en la base de datos
            
            existeUno = "x";
            if (txtCurp.length()<16)
                throw new SICEEO_Excepcion (0,"CURP_MAL_GENERADA");
                        
            if (txtCurp.length()>=10 && existeUno.equals("x")) {
                QBuskAlum = qryIfx.BuskAlum(1,txtCurp, txtNombre, txtApe1, txtApe2);
                if (QBuskAlum.size() == 1 && (""+QBuskAlum.get(0).get("crip")).trim().length()==0) //pos 36:crip
                    existeUno = "ok" ;
                else if (QBuskAlum.size() > 1)
                    existeUno = "ok";
            }
            
            /*if (existeUno.equals("ok") && tblPrincipal_grado!=1) 
            {
                if( QBuskAlum.get(0).get("cveplan").equals("2") && QBuskAlum.get(0).get("grado").equals(""+tblPrincipal_grado)  && QBuskAlum.get(0).get("maxcicescini").equals(cicescini)  && !QBuskAlum.get(0).get("cctmax").equals(tblPrincipal_cct)  && QBuskAlum.get(0).get("cveunidadmax").equals(cveunidad) && (QBuskAlum.get(0).get("estatusalu").equals("I")  || QBuskAlum.get(0).get("estatusalu").equals("DB"))) { 
                    cambDeEscu=true;
                    alumEstatus = "O";
                }
                else {
                    //DM.idaluX := DM.Q_BuskAlumidalu.AsString;
                    this.dr.put("curpD10",txtCurp.substring(0,10));
                    this.dr.put("kienLlamas","NEWINGRESOsec");
                    this.dr.put("casoRequerido","winAjustar");
                    throw new SiCEEB_Excepcion (10,"ALUMNO_EXISTENTE");
                    // BitBtn1.Click;// limpia los campos
                }
            }*/
            
            //if (dm.Q_Principalgrado.AsInteger <>1)then  // solo para grado >1
            //    alum_estatus:='I' ;
            
            if (QBuskAlum.size()<1)  // busco y no encontro
                this.dr.put("alumEstatus", (alumEstatus="I") );
            
            //*******************************************termina validar k no exista el alumno
            
            if (taller.equals("") || taller.substring(0, taller.indexOf(" ")).equals("9004"))
                throw new SICEEO_Excepcion (0,"SEL_TALLER");
            if (arte.equals("") || arte.substring(0, arte.indexOf(" ")).equals("555"))
                throw new SICEEO_Excepcion (0,"SEL_ARTE");
            if (cbxEntidad.equals(""))
                throw new SICEEO_Excepcion (0,"SEL_ENTIDAD");
            
            //v_cveprograma:='REGSEST';  //ya todos llevan el nuevo plan

            //if dm.Q_Principalmodalidad.AsString='DSN' then
            //   v_cveprograma:='TRABSEST';  // TRABAJADORES NO LLEVAN EDU FISICA
             
            //-------------------------------------------------------------------------------------------------
            if (!alumEstatus.equals("X"))
            {
                //if (alumEstatus.equals("U")){                                   //U = actualiza la curp con los 2 ultimos digitos
                if ( !QBuskAlum.isEmpty() && (""+QBuskAlum.get(0).get("curp")).trim().length()==16 && txtCurp17y18.trim().length()==2 )
                    qryIfx.actualiza2ultDigCurp (txtCurp17y18, tblPrincipal_idcct, txtUsuario, idalu);
                else if (alumEstatus.equals("O")){                             //0= OK, SE ENCONTRO un alumno
                    if (!QBuskAlum.isEmpty() && !QBuskAlum.get(0).get("estatusalu").equals("I"))
                        qryIfx.setIdcctYEstatusalu_Alumno (tblPrincipal_idcct, txtUsuario, idalu);
                }else if (alumEstatus.equals("I")){                             //no se encontro el alumno y hay k insertarlo
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
                   
                   entNac= cbxEntidad.substring(cbxEntidad.length()-6, cbxEntidad.length()-6+2);
                   if (!cbxProbem.equals("")) Probem = ""+cbxProbem.charAt(0);
                   if (!cbxExtJ.equals("")) Extj = ""+cbxExtJ.charAt(0);
                   
                }
                
                qryIfx.actualizaAlumno(alumEstatus, (QBuskAlum.size()==1?""+QBuskAlum.get(0).get("estatusalu"):null),
                        null, idalu, "20", tblPrincipal_idcct, tblPrincipal_cct, cbxDiscap, 
                        newIdTutor, curpRaiz, txtNombre, txtApe1, txtApe2, fechaNacimiento, cbxSexo, cbxKrta, 
                        entNac, Probem, Extj, dm.toInt(cicescini), tblPrincipal_grado, tblPrincipal_cveturno, 
                        "2", QPlanMod_cveprograma, tblPrincipal_grupo, edad, txtUsuario,taller,arte, "",
                        cbxLengua,cbxEtnia, fechaIng);
                
                
                /***************************************************************************************************/
                /**************************** Corrige materias en tabla evaluaciones *******************************/   
                QSusMaterias = qryIfx.susMaterias (cicescini, idalu); //8
                for (int bim=1; bim<=2; bim++) {
                    QSusEval = qryIfx.susEval (cicescini,idalu,""+bim);
                    if(QSusEval.isEmpty())
                        break;  //termina el proceso del for
                    else  {                    
                        qryIfx.actualizaSusEvaluaciones(QSusMaterias, QSusEval, this.dm, ""+bim, txtUsuario);     
                        QMatCalif=qryIfx.matCalif (idalu, cicescini);
                        tblMatCalifXBim=qryIfx.matCalifXBim (""+bim, idalu, cicescini, "2", ""+tblPrincipal_grado); //sin tomar en cuenta si lleva o no ingles
                        numFilas = QMatCalif.size();
                        for (int i=0; i<numFilas; i++)
                        {
                            if ( -1 == dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cveprograma","cvetipmat","cvemat"}, new Object[]{QMatCalif.get(i).get("cveprograma"),QMatCalif.get(i).get("cvetipmat"),QMatCalif.get(i).get("cvemat")}))
                            {
                                if ( (QMatCalif.get(i).get("cvetipmat").equals("EDT") || QMatCalif.get(i).get("cvetipmat").equals("TEC")) && -1!=dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cvetipmat"}, new Object[]{QMatCalif.get(i).get("cvetipmat")}) )  //LO K ESTOY BUSCANDO ES EL TALLER PERO EXISTE CON OTRO TALLER
                                    qryIfx.actualizaEvaluaciones (""+QMatCalif.get(i).get("cvemat"), ""+QMatCalif.get(i).get("cveprograma"), idalu, cicescini,""+QMatCalif.get(i).get("cvetipmat"), txtUsuario);
                                else { 
                                    if (QMatCalif.get(i).get("cvetipmat").equals("ART") && -1!=dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cvetipmat"}, new Object[]{QMatCalif.get(i).get("cvetipmat")}))  //LO K ESTOY BUSCANDO ES ARTE //PERO EXISTE CON OTRO ARTE
                                        qryIfx.actualizaEvaluaciones (""+QMatCalif.get(i).get("cvemat"), ""+QMatCalif.get(i).get("cveprograma"), idalu, cicescini,"ART", txtUsuario);
                                    else
                                        qryIfx.insertaEvaluaciones (""+bim, txtUsuario, idalu, cicescini, ""+QMatCalif.get(i).get("cvetipmat"), ""+QMatCalif.get(i).get("cvemat"));
                                }
                               // MessageDlg('Materia Agregada!', mtInformation, [mbOK], 0);
                            }
                        }                    
                    }
                }                                
                /***************************************************************************************************/
                /***************************************************************************************************/
                
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
                //--Vista--> v_idalu     :='';
                //--Vista--> E_18.Font.Color :=clBlack ;
                //--Vista--> L_curp.Caption:='';
                //--Vista--> E_CURP.SetFocus;
                this.dr.put("alumEstatus", "X");
                //--Vista--> l_idalu.Caption:='';
            }
            //*******************************************termina validar k no exista el alumno
            hacerCommit = true;   // comentado hoy 17-06-2025
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoSec(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){  this.dr.put("returnCase", -1);   mensaje.General("GENERAL", ex.getMessage(), "", this.dr);   }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit); } catch (SQLException ex) { } }
    }
    
    private void btnChekMat_Click (String tblMatCalifXBim_isEmpty, String califCicEscIn, String bim1, String tblPrincipal_idcct, String tblAlumCapCalif_idalu, 
        String tblAlumCapCalif_grado, String tblAlumCapCalif_grupo, String tblAlumCapCalif_cicescini, String tblAlumCapCalif_cveplan, String txtUsuario)
    {
        boolean hacerCommit=false;
        int numFilas;
        ArrayList<Map> QSusMaterias, QSusEval, tblMatCalifXBim, QMatRepetida, QMatRepet2, QMatCalif, QMatKSobran;

    //*************************************************************************************************************************
        try 
        {
            dm.isIdcctAutorizada(sesion, tblPrincipal_idcct);

            qryIfx.conectarConTransaccion();
            qryIfx.conectar();            
            

            //empatamos materias papa e hijo ----------------------------------------------------------------------------------------------------------------------------------------------
            //MATERIAS papa
            QSusMaterias = qryIfx.susMaterias (tblAlumCapCalif_cicescini, tblAlumCapCalif_idalu); //8                
            //buscamos los registros que NO coincicen con el papa alumnomaterias
            for (int bim=1; bim <=3; bim++) {
                QSusEval = qryIfx.susEval (tblAlumCapCalif_cicescini,tblAlumCapCalif_idalu,""+bim);
                if(QSusEval.isEmpty())
                    break;
                
                qryIfx.actualizaSusEvaluaciones(QSusMaterias, QSusEval, this.dm, ""+bim, txtUsuario);
                //------chekamos materia repetida k está más de una vez
                QMatRepetida = qryIfx.matRepetida(tblAlumCapCalif_idalu, califCicEscIn, ""+bim);
                if ((numFilas=QMatRepetida.size())>0 )
                    for(int i=0; i<numFilas; i++)
                        qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+QMatRepetida.get(i).get("cvetipmat"), ""+QMatRepetida.get(i).get("cvemat"), "", "", ""+QMatRepetida.get(i).get("numeval"));
                //--MATERIAS REPETIDAS Y QUE NO SON CBA verficar para secundarias ----------------------------
                QMatRepet2=qryIfx.matRepet2(tblAlumCapCalif_idalu,califCicEscIn,""+bim);
                if ( (numFilas=QMatRepet2.size())>0 )
                    for(int i=0; i<numFilas; i++)                    
                        qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+QMatRepet2.get(i).get("cvetipmat"), "", "", "", ""+QMatRepet2.get(i).get("numeval"));
                //------------------------------
                //------------------------------
                QMatCalif=qryIfx.matCalif (tblAlumCapCalif_idalu, califCicEscIn);
                tblMatCalifXBim=qryIfx.matCalifXBim (""+bim, tblAlumCapCalif_idalu, califCicEscIn, tblAlumCapCalif_cveplan, tblAlumCapCalif_grado); //sin tomar en cuenta si lleva o no ingles
                numFilas = QMatCalif.size();
                for (int i=0; i<numFilas; i++)
                {
                    if ( -1 == dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cveprograma","cvetipmat","cvemat"}, new Object[]{QMatCalif.get(i).get("cveprograma"),QMatCalif.get(i).get("cvetipmat"),QMatCalif.get(i).get("cvemat")}))
                    {
                        if ( (QMatCalif.get(i).get("cvetipmat").equals("EDT") || QMatCalif.get(i).get("cvetipmat").equals("TEC")) && -1!=dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cvetipmat"}, new Object[]{QMatCalif.get(i).get("cvetipmat")}) )  //LO K ESTOY BUSCANDO ES EL TALLER PERO EXISTE CON OTRO TALLER
                            qryIfx.actualizaEvaluaciones (""+QMatCalif.get(i).get("cvemat"), ""+QMatCalif.get(i).get("cveprograma"), tblAlumCapCalif_idalu, califCicEscIn,""+QMatCalif.get(i).get("cvetipmat"),txtUsuario);
                        else { 
                            if (QMatCalif.get(i).get("cvetipmat").equals("ART") && -1!=dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cvetipmat"}, new Object[]{QMatCalif.get(i).get("cvetipmat")}))  //LO K ESTOY BUSCANDO ES ARTE //PERO EXISTE CON OTRO ARTE
                                qryIfx.actualizaEvaluaciones (""+QMatCalif.get(i).get("cvemat"), ""+QMatCalif.get(i).get("cveprograma"), tblAlumCapCalif_idalu, califCicEscIn,"ART", txtUsuario);
                            else
                                qryIfx.insertaEvaluaciones (""+bim, txtUsuario, tblAlumCapCalif_idalu, califCicEscIn, ""+QMatCalif.get(i).get("cvetipmat"), ""+QMatCalif.get(i).get("cvemat"));
                        }
                       // MessageDlg('Materia Agregada!', mtInformation, [mbOK], 0);
                    }
                }
                //*************************************************************************************************************************
                //Materias que sobran
                QMatKSobran=qryIfx.matksobran (tblAlumCapCalif_idalu, califCicEscIn, ""+bim);
                numFilas = QMatKSobran.size();
                for (int i=0; i<numFilas; i++)
                    if ( -1 == dm.indexOfArrMap(QMatCalif, new String[]{"cveprograma","cvetipmat","cvemat"}, new Object[]{QMatKSobran.get(i).get("cveprograma"),QMatKSobran.get(i).get("cvetipmat"),QMatKSobran.get(i).get("cvemat")}))
                        qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+QMatKSobran.get(i).get("cvetipmat"), ""+QMatKSobran.get(i).get("cvemat"), ""+QMatKSobran.get(i).get("cveprograma"), "", "");
                        //    MessageDlg('Materia Eliminada!', mtInformation, [mbOK], 0);
                //*************************************************************************************************************************
                tblMatCalifXBim=qryIfx.matCalifXBim (""+bim, tblAlumCapCalif_idalu, califCicEscIn, tblAlumCapCalif_cveplan, "");  //grado en vacio para saber si tiene materias que no le corresponde, se añadio grado al query para ciclo 2024-2025
                numFilas = tblMatCalifXBim.size();
                for (int i=0; i<numFilas; i++)
                    if ( !tblMatCalifXBim.get(i).get("grado").equals(tblAlumCapCalif_grado) )
                        qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+tblMatCalifXBim.get(i).get("cvetipmat"), ""+tblMatCalifXBim.get(i).get("cvemat"), "", ""+tblMatCalifXBim.get(i).get("grado"), "");
                        //  MessageDlg('Materia Eliminada!', mtInformation, [mbOK], 0);
            }
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);/* qryIfx.cerrarConexion(); */ } catch (SQLException ex) { } }
    }
           
    private void formActivate(String tblPrincipal_idcct, String tblPrincipal_cveplan, int tblPrincipal_grado, String tblPrincipal_grupo, String tblPrincipal_modalidad, String tblPrincipal_cicescini)
    {                
        ArrayList<Map> QCatDiscap, QCatArtes, QCatTalleres;
        ArrayList<String> talleres = new ArrayList<String>(), artes= new ArrayList<String>();     
        ArrayList<String> QEstados, discap = new ArrayList<String>();    
        Map QNormatividad,QPlanMod;
        String tallerDefault="", superUsuario = ""+sesion.getAttribute("superUsuario");
        
        
        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            
            //********************************** Para cierre de captura de particulares  *******************************************//
            /* if (superUsuario.trim().equals("") && (""+sesion.getAttribute("seccion")).equals("PVD"))
                throw new SICEEO_Excepcion (-11,"PVD_SIN_PERMISO_CAPTURA"); */
            if (qryIfx.isOficializado(tblPrincipal_idcct, tblPrincipal_cicescini,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(tblPrincipal_cicescini)+1), "31/10/"+(dm.toInt(tblPrincipal_cicescini)+1)))
                throw new SICEEO_Excepcion (-11,"INSCRIPCION_OFICIALIZADA");
            else if (qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, tblPrincipal_cicescini, ""+tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM"))
                throw new SICEEO_Excepcion (-11,"ULTBIM_OFICIALIZADO","5º bimestre");
            else if(!qryIfx.isValidaCapacidadGpo(""+tblPrincipal_cicescini, ""+tblPrincipal_cicescini, ""+tblPrincipal_idcct, ""+tblPrincipal_grado, tblPrincipal_grupo))     //agregado para validad la capacidad limite del grupo                   
                throw new SICEEO_Excepcion (-11,"CAPGPO_INVALIDA",tblPrincipal_grupo);    //agregado para validad la capacidad limite del grupo
            
            QPlanMod = qryIfx.getPlanMod(tblPrincipal_cveplan, ""+tblPrincipal_grado, tblPrincipal_modalidad, tblPrincipal_cicescini,"20");
            if (!QPlanMod.isEmpty())
                this.dr.put("cveprograma",QPlanMod.get("cveprograma"));
            else
                throw new SICEEO_Excepcion (0,"VERIF_PLAN");
            QNormatividad=qryIfx.getNormatividad(tblPrincipal_cveplan, ""+QPlanMod.get("cveprograma"), ""+tblPrincipal_grado);
            
            //  03 DE febrero 2017, lavy pidio que antes de iniciar el ciclo escolar
            //                       el alumnos aun no cumpla 15 años
            if ( (tblPrincipal_modalidad.equals("DES") || tblPrincipal_modalidad.equals("PES") || tblPrincipal_modalidad.equals("DST") 
                    || tblPrincipal_modalidad.equals("PST") ) && tblPrincipal_grado==1 )
                QNormatividad.put("edadadmmax", ""+(dm.toInt(QNormatividad.get("edadadmmax"))+1));
                        
            this.dr.putAll(QNormatividad);
            this.dr.put("alumEstatus","X"); //sirve para que guardar no haga nada
            
            //26 de noviembre,
            if ( (tblPrincipal_modalidad.equals("DES") || tblPrincipal_modalidad.equals("PES") || tblPrincipal_modalidad.equals("DST") 
                    || tblPrincipal_modalidad.equals("PST") || tblPrincipal_modalidad.equals("DTV") ) && tblPrincipal_grado==1 )
                this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: de "+QNormatividad.get("edadadmmin")+" a "+QNormatividad.get("edadadmmax")+" años cumplidos al 31 de agosto de "+tblPrincipal_cicescini+".");
                //this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: A partir del 1ro de agosto de "+(dm.toInt(tblPrincipal_cicescini)-dm.toInt(QNormatividad.get("edadadmmax")))+" hasta el 31 de diciembre de "+(dm.toInt(tblPrincipal_cicescini)-dm.toInt(QNormatividad.get("edadadmmin")))+".");
            else
                this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: de "+QNormatividad.get("edadadmmin")+" a "+QNormatividad.get("edadadmmax")+" años cumplidos al 31 de agosto de "+ tblPrincipal_cicescini+".");
            
            QCatArtes = qryIfx.getCatArtes(""+QPlanMod.get("cveprograma"), tblPrincipal_grado, tblPrincipal_cicescini);
            for (Map fila : QCatArtes)
                artes.add(fila.get("cvemat")+" "+fila.get("desmat"));
            this.dr.put("artes",artes);

            QCatTalleres=qryIfx.getTalleres(tblPrincipal_cicescini, ""+QPlanMod.get("cveprograma") , tblPrincipal_grado); // Cambio tblPrincipal_idcct por cveprograma
            for (Map fila : QCatTalleres)
                talleres.add(fila.get("cvemat")+" "+fila.get("desmat"));
            this.dr.put("talleres",talleres);
            
            // un poco de ayuda para telesecundarias
            if (tblPrincipal_modalidad.equals("DTV")) this.dr.put("tallerDefault", "1013 AGRICULTURA");  // seleccionamos el grupo de la lista del combo
            /*else if (tblPrincipal_modalidad.equals("DTV") && tblPrincipal_grado!= 1) this.dr.put("tallerDefault", "952 AGRICULTURA 1ER.SEM,FRUTICULTURA 2DO.SEM");*/
            else if (tblPrincipal_modalidad.equals("HMC")) this.dr.put("tallerDefault", "NAP NO APLICA");
            //*****************************************
            this.dr.put("estados", qryIfx.getEstados());                      //El default seleccionado debe ser 20
            //--Vista--> BitBtn1.Click;
            //--Vista--> Box_krta.ItemIndex:=0;

            QCatDiscap = qryIfx.getCatDiscap(tblPrincipal_cicescini,tblPrincipal_cveplan);                //El default seleccionado debe ser 0
            discap.add("");
            for (int i=0; i<QCatDiscap.size(); i++)
                discap.add(""+QCatDiscap.get(i).get("cveydes"));
            this.dr.put("discap",discap);
            this.dr.put("lenguas",qryIfx.getLenguas()); 
             
            //--Vista--> Box_discap.ItemIndex   := 0 ; // 14;
            
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoSec(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void buscarAlumno (String txtCurp, int tblPrincipal_grado, int tblPrincipal_cveplan, int tblPrincipal_cicescini, String txt18, String txtApe1, 
            String txtApe2, String txtNombre, String cicescini, String cct)
    {
        int escalonAct, restDEscalones, restDCiclos, QFolioIdalu_RecordCount;
        String existeUno;
        ArrayList<Map> QBuskAlum=null;
        
        try
        {
            dr.put("alumEstatus","X");                                                                                                               //NOTA: Esta linea de código es mía (Maai), no de Robert.
            //buscar al alumno en toda la BD
            existeUno = "x";
            if (txtCurp.length()<10)
                throw new SICEEO_Excepcion (0,"CURP_INCOMPLETA");
            if (txtCurp.contains(" "))
                throw new SICEEO_Excepcion (0,"CARACTER_NO_VALIDO"," en lacurp. Existe un espacio");
            
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
            if ( txtCurp.length()==16 && txt18.length()==2 && existeUno.equals("x") )
            {
                QBuskAlum = qryIfx.BuskAlum(5,txtCurp+txt18, "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
                else existeUno = "no";
            }
            if ( txtCurp.length()<=16 && existeUno.equals("x") )
            {
                QBuskAlum = qryIfx.BuskAlum(2,txtCurp, "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
            }
            if ( txtCurp.length()>=10 && existeUno.equals("x") )
            {
                QBuskAlum = qryIfx.BuskAlum(2,txtCurp.substring(0,10), "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
            }
            if ( txtCurp.trim().length()>=10 && txtCurp.substring(4,10).trim().length()==0 && existeUno.equals("x")) {
                QBuskAlum = qryIfx.BuskAlum(4,"(a.curp[1,4]= '"+txtCurp.trim().substring(0,4)+"' AND a.curp16[11,16] = '"+txtCurp.trim().substring(10,16)+"'" +")", "", "", "");
                if (QBuskAlum.size()==1 ) existeUno = "si";
            }
            
            restDEscalones = -99 ;
            restDCiclos = -88 ;
            
            if ( QBuskAlum.size()==1 )
            {                                                                   //04/08/2016
                this.dr.put("QBuskAlum_grado", QBuskAlum.get(0).get("grado"));
                this.dr.put("QBuskAlum_maxcicescini", QBuskAlum.get(0).get("maxcicescini"));
                
                if ( QBuskAlum.get(0).get("estatusgrado").equals("RC") )        //SI REPROBO PUEDE REPETIR EL RENGLON
                {                                                               //11/03/2016
                    if ( escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                        restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon")) ;
                    if ( tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) );
                        restDCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ;
                }else {    //SI APROBO O CONCLUYO SU NIVEL, NO PUEDE REPETIR EL RENGLON
                    if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                        restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                    if ( tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                        restDCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ;
                }
              
                /*if ( (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                { //es de la 59
                   if ( tblPrincipal_cicescini == dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ) 
                   {
                        if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon"))-1 )
                            restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"))-1;
                        if ( tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 )
                            RestdCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 ;
                   } else {
                        if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                            restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                        if ( tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                            RestdCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                   }
               } else {//es de la 22 //26 de mayo 2015
                   if ( escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                        restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                   if (tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                        RestdCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
               }*/
            }
            
            // if (dm.Q_BuskAlum.RecordCount =  1) and (dm.Q_Principalgrado.AsInteger =1) then existeUno := 'ok' ; // solo para 1er grado
            if ( QBuskAlum.size()==1 && restDCiclos >= restDEscalones && restDEscalones!=-99 && restDCiclos!=-88 && dm.toInt(QBuskAlum.get(0).get("escalon"))<12 )
                existeUno = "ok";

            //-------------------------------------------------------------------------------
            //11/03/2016 quitamos la gandallez
            //if ( QBuskAlum.size() == 1 && (""+QBuskAlum.get(0).get("grupo")).contains("_") )
            //    existeUno = "ok";
            
            //si es grado terminal checar si tiene folio impreso
            if ( QBuskAlum.size() == 1 && escalonAct == 12) 
            {
                QFolioIdalu_RecordCount = qryIfx.QFolioIdalu (""+QBuskAlum.get(0).get("idalu"), "2"); //25 DE JULIO 2016 SE ENCONTRO UN ALUMNO QUE EGRESO DE PREESCOLAR 05-06 Y REGRESA A 3RO DE SEC. EN EL 15-16
                if (QFolioIdalu_RecordCount>=1){
                    existeUno = "f";
                    //MessageDlg('El alumno tiene un folio asignado', mtWarning, [mbOK], 0);
                }
            }

            //--------------------------------------------------
            
            if ( !existeUno.equals("ok")  )
            {
                if ( QBuskAlum.size()==1 )
                {
                    this.dr.put("curpD10",txtCurp.substring(0,10));
                    this.dr.put("kienLlamas","NEWINGRESOsec");
                    this.dr.put("casoRequerido","winAjustar");
                    this.dr.put("btnLimpiar_Click",true);
                    if (existeUno.equals("f"))
                        throw new SICEEO_Excepcion (10,"TIENE_FOLIO_ASIGNADO");
                    else if (QBuskAlum.get(0).get("estatusgrado").equals("I"))   //Condición sólo para siceeo
                    { 
                        String escuela = "";
                        if(!cct.equals((""+QBuskAlum.get(0).get("cctmax")).trim()))
                            escuela = ""+QBuskAlum.get(0).get("cctmax");
                        throw new SICEEO_Excepcion (0,"ALUMNO_AUN_INSCRITO",escuela, txtCurp.length()>10?(txtCurp.toUpperCase().charAt(10)=='H'?"o":"a"):"o(a)" );
                    }
                    else
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
               if ( (txtApe1.equals(QBuskAlum.get(0).get("apepat")) && txtApe2.equals(QBuskAlum.get(0).get("apemat")) && txtNombre.equals(QBuskAlum.get(0).get("nombre")) ) || (txtApe1.equals("") && txtApe2.equals("") && txtNombre.equals("")) )
               {
                    if ( QBuskAlum.get(0).get("maxcicescini").equals(cicescini)  /*&&  !(""+QBuskAlum.get(0).get("grupo")).contains("_")*/ ) //11/03/2016
                    {
                        this.dr.put("curpD10",txtCurp.substring(0,10));
                        this.dr.put("idaluX","DM.Q_ynoenCicloActidalu.AsString");
                        this.dr.put("kienLlamas","NEWINGRESOsec");
                        this.dr.put("casoRequerido","winAjustar");
                        throw new SICEEO_Excepcion (10,"EXISTE_EN_CICLOACT");
                    }
                    //  DM.Q_ynoenCicloAct.CLOSE;
                    this.dr.put("curpD10",txtCurp.substring(0,10));
                    this.dr.put("idaluX",QBuskAlum.get(0).get("idalu"));
                    this.dr.put("kienLlamas","NEWINGRESOsec");


                    if (existeUno.equals("ok") ) {
                        // dm2.Q_curpValida.open;  cambio 16/oct/2012 maai
                        dr.put("alumEstatus","O"); //ok;
                        dr.put("idalu",QBuskAlum.get(0).get("idalu"));
                        dr.put("curp", QBuskAlum.get(0).get("curp"));
                        dr.put("lblIdalu", QBuskAlum.get(0).get("idalu"));
                        dr.put("txtApe1",QBuskAlum.get(0).get("apepat"));
                        dr.put("txtApe2", QBuskAlum.get(0).get("apemat"));
                        dr.put("txtNombre",QBuskAlum.get(0).get("nombre"));
                        dr.put("cbxSexo", QBuskAlum.get(0).get("sexo"));
                        dr.put("txtEdad",(""+QBuskAlum.get(0).get("fecnac")).substring(0,4)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(5,7)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(8,10));
                        // BOX_ENT.ItemIndex := DM.Q_BuskAlumcveentfednac.AsInteger;
                        if ( dm.toInt(QBuskAlum.get(0).get("cveentfednac"))==34 )
                            dr.put("cbxEntidad_SelectedIndex", 33);
                        else
                            dr.put("cbxEntidad_SelectedIndex", QBuskAlum.get(0).get("cveentfednac"));

                        dr.put ("txtEdad_onBlur","llamarEvento");
                        
                        if ( (""+QBuskAlum.get(0).get("curp")).length()==18  &&  txt18.length()!= 2 ) 
                            dr.put("txt18", (""+QBuskAlum.get(0).get("curp")).substring(16,18));
                        if ( (""+QBuskAlum.get(0).get("curp")).length()!=18 && txt18.length()== 2 ) 
                        {
                           dr.put("txt18_FontColor", "red");
                           //dr.put("alumEstatus", "U"); // update los 2 digitos de la curp
                        }
                        dr.put("cbxOtrasLenguas",(QBuskAlum.get(0).get("cvelengua").equals("ESP")?"":QBuskAlum.get(0).get("cvelengua")));
                        dr.put("cbxEtnia",QBuskAlum.get(0).get("afromexicana"));
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
    
    private int edad(String fechaNacimiento,String cicescini) throws ParseException
    {
        int anioNac, anioAct;
        /*String fechaNac, fechaAct;
        
        fechaNac = dm.invFecha(fechaNacimiento, "/");
        fechaAct = "31/12/"+cicescini;
        return (int)dm.getDifFechas(fechaNac, fechaAct, 0);*/
        
        /*SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd"); 
        df.setLenient(false);
        java.util.Date fechaNac = df.parse(fechaNacimiento); 
        java.util.Date fechaAct = df.parse(cicescini+"/"+"12/31");*/
        
        anioNac = dm.toInt(fechaNacimiento.substring(0,4));
        anioAct = dm.toInt(cicescini);
        
        if (anioAct<anioNac)
            return anioAct-anioNac-1;
        else
            return anioAct-anioNac;
    }
    
    private String verCurp(String nombre, String primerApe, String segundoApe, String fechaNacimiento, String sexo, String entidad, String digVerif)
    {
        //*L_avisoGuard.Visible:= fALSE;
        SICEEO_ValidarCurp validarCurp = new SICEEO_ValidarCurp(nombre, primerApe, segundoApe, fechaNacimiento.substring(2,4), fechaNacimiento.substring(5,7), fechaNacimiento.substring(8,10), sexo, entidad.substring(entidad.length()-2,entidad.length()));
        return validarCurp.curp()/* + digVerif*/;
    }
    
    private void traeDatos(ArrayList<Object[]> datosAlumno) throws SICEEO_Excepcion, SQLException
    {
        //dr.put("susEstud",qryIfx.getSusEstud(""+datosAlumno.get(0)[0]));        
        this.dr.put("datosAlumno", datosAlumno);
        this.dr.put("kienLlamas","NEWINGRESOpri");
        this.dr.put("casoRequerido","winMasDeUno");                                 //Esta ventana debe regresar "ok" o "rechazados";
        //*BitBtn1.Click;
        throw new SICEEO_Excepcion (11,"GENERAL");
    }
}
