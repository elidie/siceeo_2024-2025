package modelo.nuevoIngreso;

import modelo.DAO.SICEEO_QueriesInformix;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.*;

public class SICEEO_NewIngresoPre {
    
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    

    public SICEEO_NewIngresoPre ( Map datosReturn, HttpServletRequest request)
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
            formActivate(r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("tblPrincipal_modalidad"), 
                    r.gP("tblPrincipal_cicescini"));
        else if(metodo.equals("gu")) 
            Guardar(r.gP("numLlamada"), r.gP("alumEstatus"), r.gP("existeUno"), r.gP("txtNombre"),r.gP("txtApe1"),r.gP("txtApe2"),r.gP("cbxSexo"),
                    r.gP("cbxEntidad"),dm.toInt(r.gP("tblPrincipal_grado")),r.gP("fechaNacimiento"),r.gP("tblPrincipal_modalidad"), r.gP("txt18"), 
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cct"), r.gP("tblPrincipal_cveturno"),r.gP("tblPrincipal_cveplan"),
                    r.gP("tblPrincipal_cveprograma"), dm.toInt(r.gP("tblPrincipal_cicescini")), r.gP("tblPrincipal_grupo"),r.gP("cbxDiscap"),r.gP("cbxKrta"),
                    r.gP("cbxProbem"),r.gP("cbxExtJ"), r.gP("idaluDeCurpRep"),
                    r.gP("numLlamada").equals("0") ? null : dm.vstrToArrMap(r.gPV("filaSelQBuskAlum"), "~", 
                    r.gP("nomColsQBuskAlum").split("~")), ""+sesion.getAttribute("userName"),r.gP("cbxLenguas"), r.gP("cbxEtnia"), r.gP("fechaIngreso")); //
    }
/* ******************************************************************************/
/* ******************************************************************************/
/* ******************************************************************************/
    
    private void Guardar (String numLlamada, String alumEstatus, String existeUno, String txtNombre, String txtApe1, String txtApe2, String cbxSexo, 
            String cbxEntidad, int tblPrincipal_grado, String fechaNacimiento, String tblPrincipal_modalidad, String txt18, String tblPrincipal_idcct, 
            String tblPrincipal_cct, String tblPrincipal_cveturno, String tblPrincipal_cveplan, String tblPrincipal_cveprograma, int tblPrincipal_cicescini,
            String tblPrincipal_grupo, String cbxDiscap, String cbxKrta, String cbxProbem, String cbxExtJ, String idaluDeCurpRep, ArrayList<Map> QBuskAlum,
            String txtUsuario,String cbxLengua, String cbxEtnia, String fechaIngreso) 
    {
        String edadValida, newIdTutor, idalu="", entNac="", probem="", extj="",  txtCurp="", curpRaiz="",cveprograma, fechaIng="", fecini;
        int edad=0, escalonAct, restDEscalones, restDCiclos, QMaxIdXEsc_libreidalu, QMaximosA_maxlibrea;
        boolean hacerCommit = false;
        ArrayList<Map> datosDiscap;
        Map QMaximosP,planMod;
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();
            if(!qryIfx.isValidaCapacidadGpo(""+tblPrincipal_cicescini, ""+tblPrincipal_cicescini, ""+tblPrincipal_idcct, ""+tblPrincipal_grado, tblPrincipal_grupo))     //agregado para validad la capacidad limite del grupo                   
                throw new SICEEO_Excepcion (0,"CAPGPO_INVALIDA");    //agregado para validad la capacidad limite del grupo
            
            if (numLlamada.equals("0"))
            {
                try { edad = edad(fechaNacimiento,""+tblPrincipal_cicescini); }catch (ParseException ex) {  throw new SICEEO_Excepcion (-1,"FECHA_NAC");  }
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
                if ( txtApe1.contains("  ") )
                    throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","primer apellido");
                if ( txtApe2.contains("  ") )
                    throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","segundo apellido");
                //++++++++++++++++++++++++++++++++
                if ( txtNombre.contains("|") || txtNombre.contains("/") || txtNombre.contains("*") || txtNombre.contains("`") || txtNombre.contains("_")) // || txtNombre.contains(".") 
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                if ( txtApe1.contains("|") || txtApe1.contains("/") || txtApe1.contains("*") || txtApe1.contains("`") || txtApe1.contains("_")) // || txtApe1.contains(".")  
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
                if ( txtApe2.contains("|") || txtApe2.contains("/") || txtApe2.contains("*") || txtApe2.contains("`") || txtApe2.contains("_")) // || txtApe2.contains(".")  
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
                //++++++++++++++++++++++++++++++++
                if ( txtNombre.trim().length()<=1 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","nombre");
                if ( txtApe1.trim().length()<=1 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","apellido");
                //++++++++++++++++++++++++++++++++                              //Condición agregada en SICEEO
                if ( !dm.isNombreOApellido (txtNombre.trim(),40) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                if ( !dm.isNombreOApellido (txtApe1.trim(),30) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
                if ( !txtApe2.equals("") && !dm.isNombreOApellido (txtApe2.trim(),30) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
                //++++++++++++++++++++++++++++++++

                //++++++++++++++++++++++++++++++++
                if (txt18.contains("O"))
                    throw new SICEEO_Excepcion (0,"2ULT_CURP");
                if (txt18.contains("@"))
                    throw new SICEEO_Excepcion (0,"2ULT_CURP"); 
                if (txt18.length()==1)
                    throw new SICEEO_Excepcion (0,"TAM_CURP");      
                try{
                    if (txt18.length()==2)
                        Integer.parseInt(txt18.substring(1,2));                        
                }catch(NumberFormatException ex){ throw new SICEEO_Excepcion (0,"DIG_VERIF"); }
                            
                txtCurp = verCurp(txtNombre, txtApe1, txtApe2, fechaNacimiento, cbxSexo, cbxEntidad, txt18);
                curpRaiz = txtCurp+txt18;                                
                if (curpRaiz.contains(" "))
                    throw new SICEEO_Excepcion (0,"EXISTE_ESPACIO","la CURP");
                
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
                //************************************inicia validar k el alumno no exista en la base de datos
                existeUno="x";
                if (txtCurp.length()<16)
                    throw new SICEEO_Excepcion (0,"CURP_MAL_GENERADA");
                
                if (txtCurp.length()>=10 && existeUno.equals("x"))
                {
                    QBuskAlum = qryIfx.BuskAlum(1,txtCurp, txtNombre, txtApe1, txtApe2);
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
                restDCiclos = -88;

                if ( QBuskAlum.size() ==  1) 
                {
                    if ( QBuskAlum.get(0).get("estatusgrado").equals("RC") )        //SI REPROBO PUEDE REPETIR EL RENGLON
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
                       if ( tblPrincipal_cicescini.equals(""+QBuskAlum.get(0).get("maxcicescini")) )
                       {
                           if ( escalonAct > (dm.toInt(QBuskAlum.get(0).get("escalon"))-1) )
                                restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon"))-1 ;

                           if (dm.toInt(tblPrincipal_cicescini) > dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 )
                                restdCiclos = dm.toInt(tblPrincipal_cicescini) - dm.toInt(QBuskAlum.get(0).get("maxcicescini"))-1 ;
                       }
                       else
                       {
                           if ( escalonAct > dm.toInt(QBuskAlum.get(0).get("escalon")) )
                                restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon")) ;

                           if ( dm.toInt(tblPrincipal_cicescini) > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                                restdCiclos = dm.toInt(tblPrincipal_cicescini) - dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ;
                       }
                    }
                    else //es de la 22
                    {
                        if ( escalonAct >= dm.toInt(QBuskAlum.get(0).get("escalon")) )
                             restDEscalones = escalonAct - dm.toInt(QBuskAlum.get(0).get("escalon")) ;

                        if ( dm.toInt(tblPrincipal_cicescini) > dm.toInt(QBuskAlum.get(0).get("maxcicescini")) )
                             restdCiclos = dm.toInt(tblPrincipal_cicescini) - dm.toInt(QBuskAlum.get(0).get("maxcicescini")) ;
                    }*/
                }

                // if (dm.Q_BuskAlum.RecordCount =  1) and (dm.Q_Principalgrado.AsInteger =1) then existeUno := 'ok' ; // solo para 1er grado

                if ( QBuskAlum.size() ==  1 && restDCiclos >= restDEscalones && restDEscalones!=-99 && restDCiclos!=-88 && dm.toInt(QBuskAlum.get(0).get("escalon"))<9 )
                    this.dr.put("existeUno", (existeUno="ok"));

                //11/03/2016 quitamos la gandallez
                //if ( QBuskAlum.size() == 1 && (""+QBuskAlum.get(0).get("grupo")).contains("_") )
                //    this.dr.put("existeUno", (existeUno="ok"));


                if ( !existeUno.equals("ok") )
                {
                    if ( QBuskAlum.size() ==  1 ) {
                        this.dr.put("curpD10",txtCurp.substring(0,10));
                        this.dr.put("kienLlamas","NEWINGRESOpre");
                        this.dr.put("casoRequerido","winAjustar");
                        this.dr.put("btnLimpiar_Click",true);
                        
                        if (QBuskAlum.get(0).get("estatusgrado").equals("I"))   //Condición sólo para siceeo
                        { 
                            String escuela = "";
                            if(!tblPrincipal_cct.equals((""+QBuskAlum.get(0).get("cctmax")).trim()))
                                escuela = ""+QBuskAlum.get(0).get("cctmax");    
                            throw new SICEEO_Excepcion (0,"ALUMNO_AUN_INSCRITO",escuela, txtCurp.length()>10?(txtCurp.toUpperCase().charAt(10)=='H'?"o":"a"):"o(a)" );
                        }
                        else
                            throw new SICEEO_Excepcion (10,"CHECAR_HISTORIAL");
                    } else {
                        if ( QBuskAlum.size() >  1)
                            if ( tblPrincipal_grado==1 ){
                                traeDatos(QBuskAlum,existeUno); //k alumno kieres // solo para 1er grado
                            }else
                                throw new SICEEO_Excepcion (0,"VARIOS_REGISTROS");
                    }
                }
            }
            //termina buscar al alumno en toda la BD

            if ( existeUno.equals("ok") ) {
                if (numLlamada.equals("0"))
                    idalu = ""+QBuskAlum.get(0).get("idalu");
                else
                    idalu = idaluDeCurpRep;
            }

            if ( existeUno.equals("rechazados") )
                throw new SICEEO_Excepcion (-100,"Exit");                       //Solo salir y cancelar las acciones

            //if (existeUno = 'x') then begin  //PARA cuarkier grado, si no existe se agrega como nuevo
            if ( existeUno.equals("x") || existeUno.equals("no") ) {
                existeUno = "x";
                //   MessageDlg('No existe en la Base de Datos!', mtInformation, [mbOK], 0);
                alumEstatus ="I"; //Insert
                //   exit ;
            }
            
            //           if (length(COPY(DM.Q_BuskAlumcurp.AsString,17,2))=2 ) and (length(E_18.Text)<> 2) then
            //              E_18.Text         := COPY(DM.Q_BuskAlumcurp.AsString,17,2);
            /*           if (length(COPY(DM.Q_BuskAlumcurp.AsString,17,2))<>2) and
                       (length(E_18.Text)= 2 ) then begin
                          E_18.Font.Color :=clred;
                          alum_Estatus:= 'U'; // update los 2 digitos de la curp
                       end;
                       if b_guardar.Enabled then
                          b_guardar.SetFocus;
             */
            
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
            
            entNac = cbxEntidad.substring(cbxEntidad.length()-6, cbxEntidad.length()-6+2);
            if (!cbxProbem.equals("")) probem = ""+cbxProbem.charAt(0);
            if (!cbxExtJ.equals("")) extj = ""+cbxExtJ.charAt(0);
            
            planMod = qryIfx.getPlanMod(tblPrincipal_cveplan, ""+tblPrincipal_grado, tblPrincipal_modalidad, ""+tblPrincipal_cicescini, "20");
            if (!planMod.isEmpty())
               cveprograma = ""+planMod.get("cveprograma");
            else
            {
                throw new SICEEO_Excepcion (0,"VERIF_PLAN");
               //*dm.a_informix.Rollback;
            }
                                    
            qryIfx.actualizaAlumno(alumEstatus, (QBuskAlum.size()==1?""+QBuskAlum.get(0).get("estatusalu"):null), (QBuskAlum.size()==1?""+QBuskAlum.get(0).get("curp"):null), idalu, "20", tblPrincipal_idcct, tblPrincipal_cct, cbxDiscap, newIdTutor, curpRaiz, txtNombre, txtApe1, txtApe2, fechaNacimiento, cbxSexo, cbxKrta, entNac, probem, extj, tblPrincipal_cicescini, 
                    tblPrincipal_grado, tblPrincipal_cveturno, "3", cveprograma, tblPrincipal_grupo, edad, txtUsuario,"","", existeUno, cbxLengua, cbxEtnia, fechaIng);
            
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
            
            hacerCommit = true; //comentado hoy 17/06/2025
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPre(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
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
        this.dr.put("existeUno",existeUno);
        this.dr.put("casoRequerido","winMasDeUno");                             //Esta ventana debe regresar "ok" o "rechazados";
        throw new SICEEO_Excepcion (2,"PASAR_A_NUMLLAMADA=1","retornar: existeUno y idalu");
    }
    
    private int edad(String fechaNacimiento,String cicescini) throws ParseException
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd"); 
        df.setLenient(false);
        Calendar calendario = Calendar.getInstance();
        
        int itemp,itemp2;
        Date FechaNac,FechaAct;
        
        //    FechaNac:=StrToDate(FechaNacimiento);
        FechaNac = df.parse(fechaNacimiento); 
        FechaAct = df.parse(cicescini+"/12/31");
        
        calendario.setTime(FechaAct);        itemp = calendario.get(Calendar.YEAR);
        calendario.setTime(FechaNac);       itemp2 = calendario.get(Calendar.YEAR);
        
        if ( FechaAct.before(FechaNac) ) 
            return itemp-itemp2-1;
        else 
            return itemp-itemp2;
    }

    private void formActivate (String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo, String tblPrincipal_modalidad, 
            String tblPrincipal_cicescini)
    {
        ArrayList<String> discap = new ArrayList<String>();    
        Map QNormatividad,QPlanMod;
        ArrayList<Map>QCatDiscap;
        String superUsuario = ""+sesion.getAttribute("superUsuario");
                
        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            //********************************** Para cierre de captura de particulares  *******************************************//
            /*if (superUsuario.trim().equals("") && (""+sesion.getAttribute("seccion")).equals("PVD"))
                throw new SICEEO_Excepcion (-11,"PVD_SIN_PERMISO_CAPTURA"); */ 
            if (qryIfx.isOficializado(tblPrincipal_idcct, tblPrincipal_cicescini,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(tblPrincipal_cicescini)+1), "31/10/"+(dm.toInt(tblPrincipal_cicescini)+1)))
                throw new SICEEO_Excepcion (-11,"INSCRIPCION_OFICIALIZADA");
            else if (qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, tblPrincipal_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION"))
                throw new SICEEO_Excepcion (-11,"ULTBIM_OFICIALIZADO","3ra evaluación");
            else if(!qryIfx.isValidaCapacidadGpo(""+tblPrincipal_cicescini, ""+tblPrincipal_cicescini, ""+tblPrincipal_idcct, ""+tblPrincipal_grado, tblPrincipal_grupo))     //agregado para validad la capacidad limite del grupo                   
                throw new SICEEO_Excepcion (-11,"CAPGPO_INVALIDA",tblPrincipal_grupo);    //agregado para validad la capacidad limite del grupo
            
            QPlanMod = qryIfx.getPlanMod(tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_modalidad, tblPrincipal_cicescini,"20");
            if (!QPlanMod.isEmpty())
                this.dr.put("cveprograma",QPlanMod.get("cveprograma"));
            else
                throw new SICEEO_Excepcion (0,"VERIF_PLAN");
            
            QNormatividad=qryIfx.getNormatividad(tblPrincipal_cveplan, ""+QPlanMod.get("cveprograma"), ""+tblPrincipal_grado);
            this.dr.putAll(QNormatividad);
            
            this.dr.put("alumEstatus","X") ; //sirve para que guardad no haga nada
            this.dr.put("lblRangoEdad", "("+QPlanMod.get("cveprograma")+") Edad permitida: de "+QNormatividad.get("edadadmmin") +" a "+QNormatividad.get("edadadmmax")+" años cumplidos al 31 de diciembre de "+tblPrincipal_cicescini+".");
            
            this.dr.put("estados",qryIfx.getEstados());                                    //El default seleccionado debe ser 20
            //--Vista-> Box_ent.ItemIndex   :=20;
            //--Vista-> BitBtn1.Click;
            //--Vista-> Box_krta.ItemIndex:=0;
            
            QCatDiscap = qryIfx.getCatDiscap(tblPrincipal_cicescini,tblPrincipal_cveplan);           //El default seleccionado debe ser 0
            discap.add("");
            for (int i=0; i<QCatDiscap.size(); i++)
                discap.add(""+QCatDiscap.get(i).get("cveydes"));
            this.dr.put("discap",discap);
            //--Vista-> Box_discap.ItemIndex   := 0 ;//  13;
            
            this.dr.put("lenguas",qryIfx.getLenguas()); 
            //box_discap.KeyValue:='N';
            //dm2.q_CatDiscap.Locate('cvedefsuf', 'N', []) ;
            //Box_discap.ListFieldIndex := box_discap.IndexOf ('NO APLICA');  // seleccionamos el grupo de la lista del combo
            //if not dm.Q_Gpos_Val.Locate('grupo', dm.Q_camDgpogrupo.Value, []) then
            
        }catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.NewIngresoPri(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private String verCurp(String nombre, String primerApe, String segundoApe, String fechaNacimiento, String sexo, String entidad, String digVerif)
    {
        //*L_avisoGuard.Visible:= fALSE;
        SICEEO_ValidarCurp validarCurp = new SICEEO_ValidarCurp(nombre, primerApe, segundoApe, fechaNacimiento.substring(2,4), fechaNacimiento.substring(5,7), fechaNacimiento.substring(8,10), sexo, entidad.trim().substring(entidad.trim().length()-2,entidad.trim().length()));
        return validarCurp.curp()/* + digVerif*/;
    }
    
}
