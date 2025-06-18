package modelo.nuevoIngreso;

import modelo.DAO.SICEEO_QueriesInformix;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.*;

/**
 *
 * @author dai
 */
public class SICEEO_NewIngresoPri {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;    
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
  
    public SICEEO_NewIngresoPri ( Map datosReturn, HttpServletRequest request)
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
     * @param request Solicitud del servidor
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (String metodo)
    {
        if(metodo.equals("foAc"))
            formActivate(r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"),  r.gP("tblPrincipal_grupo"),  r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_cicescini")); 
        else if(metodo.equals("buAl")){
            buscarAlumno(r.gP("numLlamada"), r.gP("existeUno"), dm.toInt(r.gP("tblPrincipal_grado")), r.gP("txt18"), r.gP("nombre"), r.gP("primerApe"), 
                    r.gP("segundoApe"), dm.toInt(r.gP("cicescini")), r.gP("txtCurp"), r.gP("cveunidad"), r.gP("cct"), dm.toInt(r.gP("tblPrincipal_cveplan")), 
                    r.gP("numLlamada").equals("0") ? null : dm.vstrToArrMap(r.gPV("filaSelQBuskAlum"), "~", r.gP("nomColsQBuskAlum").split("~")) );
        }else if(metodo.equals("gu"))
            Guardar(r.gP("txtNombre"), r.gP("txtApe1"), r.gP("txtApe2"), r.gP("cbxSexo"), r.gP("cbxEntidad"), dm.toInt(r.gP("tblPrincipal_grado")), 
                    r.gP("fechaNacimiento"), r.gP("tblPrincipal_modalidad"),r.gP("txt18"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cct"), 
                    r.gP("tblPrincipal_cveturno"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_grupo"), r.gP("cbxDiscap"), 
                    r.gP("cbxKrta"), r.gP("cbxProbem"), r.gP("cbxExtJ"), r.gP("alumEstatus"), dm.toInt(r.gP("edadMin")), dm.toInt(r.gP("edadMax")), r.gP("idalu"), 
                     ""+sesion.getAttribute("userName"),r.gP("cbxLenguas"), r.gP("cbxEtnia"),r.gP("fechaIngreso"),r.gP("maxCicEscIni"));
    }
/* ******************************************************************************/
/* ******************************************************************************/
/* ******************************************************************************/
    
    private void Guardar (String txtNombre, String txtApe1, String txtApe2, String cbxSexo, String cbxEntidad, int tblPrincipal_grado, String fechaNacimiento, 
            String tblPrincipal_modalidad, String txtCurp17y18, String tblPrincipal_idcct, String tblPrincipal_cct, String tblPrincipal_cveturno, 
            String tblPrincipal_cveplan, String tblPrincipal_cicescini, String tblPrincipal_grupo, String cbxDiscap, String cbxKrta, String cbxProbem, 
            String cbxExtJ, String alumEstatus, int edadMin, int edadMax, String idalu, String txtUsuario,String cbxLengua, String cbxEtnia, String fechaIngreso, String maxCicEscIni)
    {
        String edadValida, newIdTutor="", entNac="", cveprograma, existeUno, Probem="", Extj="", fechaIng="";
        boolean hacerCommit=false;
        String txtCurp, curpRaiz, fecini;
        int edad, QMaxIdXEsc_libreidalu, QMaximosA_maxlibrea;
        ArrayList<Map> QBuskAlum = null;
        Map planMod, QMaximosP;
        
        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();
            
            if(!qryIfx.isValidaCapacidadGpo(""+tblPrincipal_cicescini, ""+tblPrincipal_cicescini, ""+tblPrincipal_idcct, ""+tblPrincipal_grado, tblPrincipal_grupo))     //agregado para validad la capacidad limite del grupo                   
                throw new SICEEO_Excepcion (0,"CAPGPO_INVALIDA");    //agregado para validad la capacidad limite del grupo
                        
            try { edad = edad(fechaNacimiento,tblPrincipal_cicescini); }catch (ParseException ex) {  throw new SICEEO_Excepcion (-1,"FECHA_NAC");  }
            edadValida = "N";
            this.dr.put("lblEdad","Edad: "+edad);
            if (edad>= edadMin &&  edad<= edadMax)
                edadValida = "S";
            if (!tblPrincipal_modalidad.equals("DML") &&                                     // los de educacion especial rebasan la edad permitida
                !tblPrincipal_modalidad.equals("DBA") &&                                     // los de CEBAS rebasan la edad permitida
                !tblPrincipal_modalidad.equals("HMC") && !tblPrincipal_modalidad.equals("HSL") ){
                    if (edadValida.equals("N"))                                 // pero deben de entrar
                        throw new SICEEO_Excepcion (0,"EDAD_INVALIDA");
            }
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.contains("|") || txtNombre.contains("*") || txtNombre.contains("/") || txtNombre.contains("-") || txtNombre.contains("_")  || txtNombre.contains(".") )
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
                        
            //++++++++++++++++++++++++++++++++
            if (txtCurp17y18.contains("O"))
                throw new SICEEO_Excepcion (0,"2ULT_CURP");     
            if ( txtNombre.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","nombre");
            if ( txtApe1.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","primer apellido");
            if ( txtApe2.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","segundo apellido");
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
                    dm.toInt(txtCurp17y18.substring(1,2));
            }catch(NumberFormatException ex){ throw new SICEEO_Excepcion (0,"DIG_VERIF"); }
            
            if (cbxEntidad.equals(""))
                throw new SICEEO_Excepcion (0,"ENTIDAD");
            
            if ( fechaIngreso.trim().length()<=1 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","dato de Fecha Ingreso");
                                
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
            if (alumEstatus.equals("X"))//&& grado==1)                      // solo para primer grado
                throw new SICEEO_Excepcion (0,"BUSCAR");
            //************************************inicia validar k el alumno no exista en la base de datos
            existeUno = "x";
            if (txtCurp.length()<16)
                throw new SICEEO_Excepcion (0,"CURP_MAL_GENERADA");
            
            
            if (txtCurp.length()>=10 && existeUno.equals("x")) {
                QBuskAlum = qryIfx.BuskAlum(1,txtCurp, txtNombre, txtApe1, txtApe2);
                if (QBuskAlum.size() == 1 && (""+QBuskAlum.get(0).get("crip")).trim().length()==0)
                    existeUno = "ok" ;
                else if (QBuskAlum.size() > 1)
                    existeUno = "ok";
            }
            
            /**************************************************************************************************************/
            /** Si el alumno existe, verificamos si tiene historial completo, en caso de que no enviar mensaje a usuario **/
            /**************************************************************************************************************/
            if((existeUno.equals("ok") || alumEstatus.equals("O"))&& tblPrincipal_grado > 1 && tblPrincipal_grado<=6 && QBuskAlum.size()==1){
                Map historialP = new HashMap();
                String cadGdos[] = {"1ro","2do","3er","4to","5to"};
                Double promGdos[] = new Double[5];
                String msgGdos = "";
                
                historialP = qryIfx.verificaHistorialPrim(idalu,maxCicEscIni);
                promGdos[0]  = Double.parseDouble(historialP.get("Promd1roP").toString());
                promGdos[1]  = Double.parseDouble(historialP.get("Promd2doP").toString());
                promGdos[2]  = Double.parseDouble(historialP.get("Promd3roP").toString());
                promGdos[3]  = Double.parseDouble(historialP.get("Promd4toP").toString());
                promGdos[4]  = Double.parseDouble(historialP.get("Promd5toP").toString());
                
                for(int i=0; i< tblPrincipal_grado-1; i++) { 
                    if(promGdos[i]<6.0)
                        msgGdos += msgGdos.length()>0 ? ", "+cadGdos[i] : cadGdos[i];
                }
                if(msgGdos.length()>0)
                    throw new SICEEO_Excepcion (0,"HIST_INCOMPLETO",msgGdos);
            }
            /******************************* Finaliza el proceso de verificar historial del alumno ************************/
            /**************************************************************************************************************/
            if (QBuskAlum.size()<1) {// busco y no encontro
                dr.put("alumEstatus", alumEstatus="I");
                if(tblPrincipal_grado>1)
                    throw new SICEEO_Excepcion (0,"NVO_ALUMNO",""+tblPrincipal_grado);
            } 
             //*******************************************termina validar k no exista el alumno
             
            if (!alumEstatus.equals("X"))
            {
                //if (alumEstatus.equals("U")){                                   //U = actualiza la curp con los 2 ultimos digitos
                if ( !QBuskAlum.isEmpty() && (""+QBuskAlum.get(0).get("curp")).trim().length()==16 && txtCurp17y18.trim().length()==2 )
                    qryIfx.actualiza2ultDigCurp ( txtCurp17y18,  tblPrincipal_idcct,  txtUsuario,  idalu);

                if (alumEstatus.equals("O")){                                   //0= OK, SE ENCONTRO un alumno
                    if ( !QBuskAlum.isEmpty() && !QBuskAlum.get(0).get("estatusalu").equals("I") )
                        qryIfx.setIdcctYEstatusalu_Alumno(tblPrincipal_idcct, txtUsuario, idalu);
                }
                
                if (alumEstatus.equals("I")){                                   //el alumno no existe, hay k insertarlo en la tabla alumno
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
                    
                   entNac= cbxEntidad.substring(cbxEntidad.length()-6, cbxEntidad.length()-6+2);
                   if (!cbxProbem.equals(""))  Probem = ""+cbxProbem.charAt(0);
                   if (!cbxExtJ.equals("")) Extj = ""+cbxExtJ.charAt(0);
                }
                //*******************************************************************************************
                planMod = qryIfx.getPlanMod(tblPrincipal_cveplan, ""+tblPrincipal_grado, tblPrincipal_modalidad, tblPrincipal_cicescini, "20");
                if (!planMod.isEmpty())
                   cveprograma = ""+planMod.get("cveprograma");
                else
                {
                    throw new SICEEO_Excepcion (0,"VERIF_PLAN");
                   //*dm.a_informix.Rollback;
                }
                //*******************************************************************************************
                qryIfx.actualizaAlumno(alumEstatus, (QBuskAlum.size()==1?""+QBuskAlum.get(0).get("estatusalu"):null), null, idalu, "20", tblPrincipal_idcct, tblPrincipal_cct, cbxDiscap, newIdTutor, curpRaiz, txtNombre, txtApe1, txtApe2, fechaNacimiento, cbxSexo, cbxKrta, entNac, Probem, Extj, dm.toInt(tblPrincipal_cicescini), tblPrincipal_grado, tblPrincipal_cveturno, "1", cveprograma, tblPrincipal_grupo, edad, txtUsuario,"","", "",cbxLengua, cbxEtnia, fechaIng);

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
                
                hacerCommit=true; //comentado hoy 17-06-2025
            }
        }catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPri(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { }
    }
    
    private void buscarAlumno (String numLlamada, String existeUno, int tblPrincipal_grado, String txt18, String txtNombre, String txtApe1, String txtApe2, int tblPrincipal_cicescini, String txtCurp, String cveunidad,String cct, 
            int tblPrincipal_cveplan, ArrayList<Map> QBuskAlum)
    {
        int escalonAct, restDEscalones, restDCiclos, QFolioIdalu_RecordCount;
        
        try 
        {
            if (numLlamada.equals("0"))
            {
                QBuskAlum=new ArrayList<Map>();
                
                //buscar al alumno en toda la BD
                existeUno="x";
                if (txtCurp.length()<10)
                    throw new SICEEO_Excepcion (0,"CURP_INCOMPLETA");
                if (txtCurp.contains(" "))
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","curp");

                escalonAct = 0;
                if (tblPrincipal_cveplan==1){
                    if (tblPrincipal_grado==1) escalonAct = 4;
                    else if (tblPrincipal_grado==2) escalonAct = 5;
                    else if (tblPrincipal_grado==3) escalonAct = 6;
                    else if (tblPrincipal_grado==4) escalonAct = 7;
                    else if (tblPrincipal_grado==5) escalonAct = 8;
                    else if (tblPrincipal_grado==6) escalonAct = 9;
                }

                qryIfx.conectar();
                if (txtCurp.trim().length()==16 && txt18.trim().length()==2 && existeUno.equals("x"))
                {
                    QBuskAlum=qryIfx.BuskAlum(5, txtCurp.trim()+txt18.trim(), "", "", "");
                    if ( QBuskAlum.size() == 1 )
                        existeUno="si";
                    else
                        existeUno="no";
                }

                if (txtCurp.trim().length()<=16 && existeUno.equals("x"))
                {
                    QBuskAlum = qryIfx.BuskAlum(2,txtCurp, "", "", "");
                    if ( QBuskAlum.size() == 1 ) existeUno="si";
                }
                if (txtCurp.trim().length()>=10 && existeUno.equals("x"))
                {
                    QBuskAlum = qryIfx.BuskAlum(2,txtCurp.substring(0,10), "", "", "");
                    if ( QBuskAlum.size() == 1 ) existeUno="si";        // solo para 1er grado
                }
                if (txtCurp.trim().length()==16 && txt18.trim().length()==2 && existeUno.equals("x"))
                {
                    QBuskAlum = qryIfx.BuskAlum(5,txtCurp+txt18, "", "", "");
                    if ( QBuskAlum.size() == 1 ) existeUno="si";       // solo para 1er grado
                }
                if (txtCurp.trim().length()>=10 && txtCurp.substring(4,10).trim().length()==0 && existeUno.equals("x"))
                {
                    QBuskAlum = qryIfx.buscarAlumno("(a.curp[1,4]= '"+txtCurp.substring(0,4)+"' AND a.curp16[11,16] = '"+txtCurp.substring(10,16)+"')");
                    if ( QBuskAlum.size() == 1 ) existeUno="si";                                                               // solo para 1er grado
                }

                restDEscalones = -99 ;
                restDCiclos = -88 ;

                if ( QBuskAlum.size() == 1 )
                {
                    this.dr.put("QBuskAlum_maxcicescini", QBuskAlum.get(0).get("maxcicescini"));
                    if ( QBuskAlum.get(0).get("estatusgrado").equals("RC") )    //SI REPROBO PUEDE REPETIR EL RENGLON
                    {      //11/03/2016
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
                            if (escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon"))-1 )
                                restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"))-1 ;
                            if (tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 )
                                restDCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 ;
                        }
                        else
                        {
                            if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                                 restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));
                            if ( tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                                 restDCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                        }
                    }
                    else //es de la 22
                    {
                        if (escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                            restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"));

                        if ( tblPrincipal_cicescini > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                             restDCiclos = tblPrincipal_cicescini - dm.toInt(QBuskAlum.get(0).get("maxcicescini"));
                    }*/
                }

                // if (dm.Q_BuskAlum.RecordCount =  1) and (dm.Q_Principalgrado.AsInteger =1) then existeUno := 'ok' ; // solo para 1er grado

                if ( QBuskAlum.size() == 1  && restDCiclos >= restDEscalones && restDEscalones!=-99 && restDCiclos!=-88 && dm.toInt(QBuskAlum.get(0).get("escalon"))<9 )
                    existeUno="ok";
                
                //11/03/2016 quitamos la gandallez
                //if (  QBuskAlum.size() == 1 && (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                //    existeUno="ok";
                
                //si es grado terminal checar si tiene folio impreso
                if ( QBuskAlum.size() == 1 && escalonAct == 9)
                {
                    QFolioIdalu_RecordCount = qryIfx.QFolioIdalu (""+QBuskAlum.get(0).get("idalu"), ""+QBuskAlum.get(0).get("cveplan"));
                    if (QFolioIdalu_RecordCount>=1){
                        existeUno="f";
                        //MessageDlg('El alumno tiene un folio asignado', mtWarning, [mbOK], 0);
                    }
                }

                if ( !existeUno.equals("ok") )
                {
                    if ( QBuskAlum.size() == 1 )
                    {
                        this.dr.put("curpD10",txtCurp.substring(0,10));
                        this.dr.put("kienLlamas","NEWINGRESOpri");
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
                    }
                    else
                    {
                        if (QBuskAlum.size() >  1)
                            if ( tblPrincipal_grado == 1 ) traeDatos(QBuskAlum,existeUno); //k alumno kieres // solo para 1er grado
                            else
                                throw new SICEEO_Excepcion (0,"VARIOS_REGISTROS");
                    }
                }
                //termina buscar al alumno en toda la BD
            }
            
            if ( existeUno.equals("rechazados") )
                throw new SICEEO_Excepcion (-100,"Exit");                       //Solo salir y cancelar las acciones
            
            //if (existeUno = 'x') then begin  //PARA cuarkier grado, si no existe se agrega como nuevo
            if ( existeUno.equals("x") || existeUno.equals("no") ) {
                existeUno="x";
                this.dr.put("alumEstatus","I");
                throw new SICEEO_Excepcion (1,"ALUMNO_NO_EXISTE");
            }
            
            if (existeUno.equals("ok"))  // and (dm.Q_Principalgrado.AsInteger=1) then //inicia el ifOK
            {
                if ( (txtApe1.equals(QBuskAlum.get(0).get("apepat")) && txtApe2.equals(QBuskAlum.get(0).get("apemat")) && txtNombre.equals(QBuskAlum.get(0).get("nombre"))) || (txtApe1.equals("") && txtApe2.equals("") && txtNombre.equals("") ) )
                {
                    if ( QBuskAlum.get(0).get("maxcicescini").equals(tblPrincipal_cicescini) /*&& !(""+QBuskAlum.get(0).get("grupo")).contains("_")*/ ) //11/03/2016
                    {
                        //MessageDlg('ya Existe en en ciclo actual!!'+#13+#10+'en el CCT: '+dm.Q_ynoenCicloActcct.AsString+' Turno: '+dm.Q_ynoenCicloActcveturno.AsString +#13+#10+'en '+dm.Q_ynoenCicloActgrado.AsString+'º Grado, Grupo: '+dm.Q_ynoenCicloActgrupo.AsString+#13+#10+'idAlu: '+dm.Q_ynoenCicloActidalu.AsString, mtWarning, [mbOK], 0);
                        //QYNoEnCicloAct = qryIfx.yNoEnCicloAct(""+QBuskAlum.get(0).get("idalu"), cveunidad);
                        dr.put("curpD10", txtCurp.substring(0,10));
                        dr.put("idaluX", QBuskAlum.get(0).get("idalu") );
                        dr.put("kienLlamas","NEWINGRESOpri");
                        this.dr.put("casoRequerido","winAjustar");
                        throw new SICEEO_Excepcion (10,"EXISTE_EN_CICLOACT");
                    }
                    //  DM.Q_ynoenCicloAct.CLOSE;
                    dr.put("curpD10", txtCurp.substring(0,10));
                    dr.put("idaluX", QBuskAlum.get(0).get("idalu"));
                    dr.put("kienLlamas", "NEWINGRESOpri");


                    if ( existeUno.equals("ok") ) 
                    {
                        // dm2.Q_curpValida.open;  cambio 16/oct/2012 maai
                        dr.put("alumEstatus","O"); //ok;
                        dr.put("idalu",QBuskAlum.get(0).get("idalu"));
                        dr.put("curp",QBuskAlum.get(0).get("curp"));
                        dr.put("lblIdalu",QBuskAlum.get(0).get("idalu"));
                        dr.put("txtApe1",QBuskAlum.get(0).get("apepat"));
                        dr.put("txtApe2",QBuskAlum.get(0).get("apemat"));
                        dr.put("txtNombre",QBuskAlum.get(0).get("nombre"));
                        dr.put("cbxSexo",QBuskAlum.get(0).get("sexo"));
                        dr.put("txtEdad",(""+QBuskAlum.get(0).get("fecnac")).substring(0,4)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(5,7)+"/"+(""+QBuskAlum.get(0).get("fecnac")).substring(8,10));
                        //         BOX_ENT.ItemIndex := DM.Q_BuskAlumcveentfednac.AsInteger;
                        if ( QBuskAlum.get(0).get("cveentfednac").equals("34") )
                            dr.put("cbxEntidad_SelectedIndex", 33);
                        else
                            dr.put("cbxEntidad_SelectedIndex", QBuskAlum.get(0).get("cveentfednac"));

                        dr.put ("txtEdad_onBlur","llamarEvento");

                        if ( (""+QBuskAlum.get(0).get("curp")).length()==18 && txt18.length()!=2 )
                           dr.put("txt18",(""+QBuskAlum.get(0).get("curp")).substring(16));
                        if ( (""+QBuskAlum.get(0).get("curp")).length()!=18 && txt18.length()==2 ) 
                        {
                           dr.put("txt18_FontColor","red");
                           //this.dr.put("alumEstatus","U"); // update los 2 digitos de la curp
                        }
                        //--Vista--> if b_guardar.Enabled then
                        //--Vista-->    b_guardar.SetFocus;
                        dr.put("cbxOtrasLenguas",(QBuskAlum.get(0).get("cvelengua").equals("ESP")?"":QBuskAlum.get(0).get("cvelengua")));
                        dr.put("cbxEtnia",QBuskAlum.get(0).get("afromexicana"));
                    }
                }
                else
                {
                     this.dr.put("alumEstatus","I"); //Insert
                     throw new SICEEO_Excepcion (1,"ALUMNO_NO_EXISTE");
                }
            } //termina el ifOK
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPri(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } 
        }
    }
    
    private void formActivate(String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo, String tblPrincipal_modalidad, 
            String tblPrincipal_cicescini)
    {                
        ArrayList<Map> datosDiscap;
        ArrayList<String> discap = new ArrayList<String>();
        Map QPlanMod, QNormatividad;
        String cveprograma="", edadMin, edadMax, superUsuario = ""+sesion.getAttribute("superUsuario");
        
        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            
            //********************************** Para cierre de captura de particulares  *******************************************//
            /*if (superUsuario.trim().equals("") && (""+sesion.getAttribute("seccion")).equals("PVD"))
                throw new SICEEO_Excepcion (-11,"PVD_SIN_PERMISO_CAPTURA");     */                   
            if (qryIfx.isOficializado(tblPrincipal_idcct, tblPrincipal_cicescini,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(tblPrincipal_cicescini)+1), "31/10/"+(dm.toInt(tblPrincipal_cicescini)+1)))
                throw new SICEEO_Excepcion (-11,"INSCRIPCION_OFICIALIZADA");
            else if (qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, tblPrincipal_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM"))
                throw new SICEEO_Excepcion (-11,"ULTBIM_OFICIALIZADO","5º bimestre");
            else if(!qryIfx.isValidaCapacidadGpo(""+tblPrincipal_cicescini, ""+tblPrincipal_cicescini, ""+tblPrincipal_idcct, ""+tblPrincipal_grado, tblPrincipal_grupo))     //agregado para validad la capacidad limite del grupo                   
                throw new SICEEO_Excepcion (-11,"CAPGPO_INVALIDA",tblPrincipal_grupo);    //agregado para validad la capacidad limite del grupo        
            
            QPlanMod = qryIfx.getPlanMod(tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_modalidad, tblPrincipal_cicescini,"20");
            if (!QPlanMod.isEmpty())
                cveprograma=""+QPlanMod.get("cveprograma");
            else
                throw new SICEEO_Excepcion (0,"VERIF_PLAN");
            
            QNormatividad=qryIfx.getNormatividad(tblPrincipal_cveplan, cveprograma, tblPrincipal_grado);
            this.dr.putAll(QNormatividad);                                       //edadMin y edadMax
            edadMin = ""+QNormatividad.get("edadadmmin");
            edadMax = ""+QNormatividad.get("edadadmmax");
            
            this.dr.put("alumEstatus","X"); //sirve para que guardad no haga nada
            
            if (tblPrincipal_modalidad.equals("DBA") || tblPrincipal_modalidad.equals("HMC") )
                this.dr.put("lblRangoEdad","("+cveprograma+") Edad permitida: de 13 a 99 años cumplidos al 31 de diciembre de "+ tblPrincipal_cicescini+".");
            else if ( tblPrincipal_modalidad.equals("DPR") || tblPrincipal_modalidad.equals("DPB"))
                this.dr.put("lblRangoEdad","("+QPlanMod.get("cveprograma")+") Edad permitida: A partir del 1ro de enero de "+(dm.toInt(tblPrincipal_cicescini)-dm.toInt(edadMax))+" hasta el 31 de diciembre de "+(dm.toInt(tblPrincipal_cicescini)-dm.toInt(edadMin))+".");
            else
                this.dr.put("lblRangoEdad","("+cveprograma+") Edad permitida: de "+edadMin+" a "+edadMax+" años cumplidos al 31 de diciembre de "+ tblPrincipal_cicescini+".");
            
            this.dr.put("estados",qryIfx.getEstados());                               //El default seleccionado debe ser 20
            //--Vista--> BitBtn1.Click;
            
            //--Vista--> Box_krta.ItemIndex:=0;
            
            datosDiscap = qryIfx.getCatDiscap(tblPrincipal_cicescini,tblPrincipal_cveplan);             //El default seleccionado debe ser 0
            discap.add("");
            for (int i=0; i<datosDiscap.size(); i++)
                discap.add(""+datosDiscap.get(i).get("cveydes"));
            this.dr.put("discap",discap);
            //--Vista--> Box_discap.ItemIndex   := 0 ;//  14;
            this.dr.put("lenguas",qryIfx.getLenguas()); 
            
            /*E_nom.Text :='';
            E_apl1.Text :='';
            E_apl2.Text :='';
            e_EDAD.Text :='';
            E_sexo.Text :='';
            E_CURP.Text :='';
            E_18.Text   :='';

            l_idalu.Caption:='';
            L_curp.Caption:='';
            dm2.Q_curpValida.Close;
            E_curp.SetFocus;*/
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPri(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private int edad(String fechaNacimiento,String cicescini) throws ParseException
    {
        String FechaNac,FechaAct;
        FechaNac = dm.invFecha(fechaNacimiento, "/");
        FechaAct = "31/12/"+cicescini;
        return (int)dm.getDifFechas(FechaNac, FechaAct, 0);
    }
    
    private String verCurp(String nombre, String primerApe, String segundoApe, String fechaNacimiento, String sexo, String entidad, String digVerif)
    {
        //*L_avisoGuard.Visible:= fALSE;
        SICEEO_ValidarCurp validarCurp = new SICEEO_ValidarCurp(nombre, primerApe, segundoApe, fechaNacimiento.substring(2,4), fechaNacimiento.substring(5,7), fechaNacimiento.substring(8,10), sexo, entidad.substring(entidad.length()-2,entidad.length()));
        return validarCurp.curp()/* + digVerif*/;
    }
    
    private void traeDatos(ArrayList<Map> QBuskAlum, String existeUno) throws SICEEO_Excepcion, SQLException
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
        throw new SICEEO_Excepcion (2,"PASAR_A_NUMLLAMADA=1","retornar: existeUno y QBuskAlum");
    }
}
