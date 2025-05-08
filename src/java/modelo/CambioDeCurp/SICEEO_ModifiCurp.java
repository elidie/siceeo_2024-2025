package modelo.CambioDeCurp;

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
import modelo.ClasesGlobales.SICEEO_ValidarCurp;

/* 
    Creado el : 14/06/2016, 21:11 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/
public class SICEEO_ModifiCurp {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    
    public SICEEO_ModifiCurp (Map datosReturn, HttpServletRequest request)
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
     * @param request Solicitud del servidor
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (String metodo)
    {
        String txtUsuario = ""+sesion.getAttribute("userName");
        
        if (metodo.equals("foAc"))
            formActivate (r.gP("kienLlamas"), r.gP("idaluX"), r.gP("cicescini"), r.gP("tblPrincipal_idcct"));
        else if (metodo.equals("btBuAl_Cl"))
            btnBuskAlum_Click(r.gP("cicescini"), r.gP("txtPermiso"), r.gP("unidad"),dm.toBool(r.gP("rbnCurp_isChecked")), 
                dm.toBool(r.gP("rbnIdalu_isChecked")), r.gP("txtIdalu"), r.gP("txtCurp16"), r.gP("txtCurp17y18"), r.gP("tblPrincipal_modalidad"),
                r.gP("tblPrincipal_grado"), txtUsuario );
        else if (metodo.equals("btGu_AcPe"))
            btnGuardar_ActionPerfomed (r.gP("laU"), dm.toInt(r.gP("cicescin")), r.gP("cicescini"), dm.toInt(r.gP("edadMin")), dm.toInt(r.gP("edadMax")), 
                    r.gP("enCicloAnterior"), r.gP("gdoTerminal"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), 
                    r.gP("modifCurp"), r.gP("txtPermiso"), r.gP("kienLlamas"), r.gP("alumEstatus"), r.gP("curp_ant"), r.gP("nombre_ant"), r.gP("apepat_ant"), 
                    r.gP("apemat_ant"), r.gP("regCivCert_ant"), r.gP("valida_ant"), r.gP("idalu"), r.gP("lblCurp"), r.gP("txtCurp16"), r.gP("txtCurp17y18"), 
                    r.gP("txtCrip"), r.gP("lblCct"),  r.gP("txtNombre"), r.gP("txtApe1"), r.gP("txtApe2"), r.gP("cbxSexo"), r.gP("txtFechaNac"), r.gP("cbxKrta"), 
                    r.gP("cbxEntidad"), r.gP("cbxProbem"), r.gP("cbxExtj"), r.gP("txaObservaciones"), txtUsuario );
            
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate (String kienLlamas, String idaluX, String cicescini, String tblPrincipal_idcct)
    {
        
        String EnCicloAnterior ="";
        String alumEstatus="X"; //sirve para que guardad no haga nada
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            if (qryIfx.isOficializado(tblPrincipal_idcct, cicescini,"", "", "INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(cicescini)+1), "31/10/"+(dm.toInt(cicescini)+1)))
                throw new SICEEO_Excepcion (-11,"INSCRIPCION_OFICIALIZADA");
            
            this.dr.put("estados", qryIfx.getEstados());
            this.dr.put("alumEstatus", alumEstatus);
            //--vista--> BitBtn1.Click;                                         //btnLimpiar
            
            //dm2.q_CatDiscap.Open;  <= chekar
            //box_discap.KeyValue:='N';
            
            if ( kienLlamas.equals("DESDEBAJAS") || kienLlamas.equals("DESDECALIF") )
            {
                this.dr.put("txtIdalu_Text",idaluX);
                this.dr.put("rbnIdalu_Checked",true);
                //--Vista--> B_BUSKALUM.OnClick(SELF);
                
                String tipo_usuario = ""+sesion.getAttribute("tipo_usuario");
                
                if ( !tipo_usuario.equals(" ") ) // cuando es un usuario tipo CCT
                {
                    this.dr.put("btnBuskAlum_Visible",false);
                    this.dr.put("txtCurp16_ReadOnly",true);
                    this.dr.put("txtIdalu_ReadOnly",true);
                } else {
                    this.dr.put("btnBuskAlum_Visible",true);
                    this.dr.put("txtCurp16_ReadOnly",false);
                    this.dr.put("txtIdalu_ReadOnly",false);
                }
            }
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.ModifCurp(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void btnBuskAlum_Click(String cicescini, String txtPermiso, String unidad, boolean rbnCurp_isChecked, 
            boolean rbnIdalu_isChecked, String txtIdalu, String txtCurp16, String txtCurp17y18, String tblPrincipal_modalidad, String tblPrincipal_grado, 
            String txtUsuario)
    {
        String discap;
        ArrayList<Map> QBuskAlum = new ArrayList<Map>(), QYEsDLaReg, QCurpRenapo;
        Map QYNoEnCicloAct, QNormatividad, QUsuario;
        
        try 
        {
            
            if (rbnCurp_isChecked){
                if (txtCurp16.length()<10)
                    throw new SICEEO_Excepcion (0,"CURP_INCOMPLETA");
                if (txtCurp16.contains(" "))
                    throw new SICEEO_Excepcion (0,"CARACTER_NO_VALIDO"," en lacurp. Existe un espacio");
            }
            
            this.dr.put("laU", "");                                            // es la variable k avisa cuando el nombre contiene una Ü
            this.dr.put("lblCurp_Text", "");
            //    E_18.Text:=''   ;
            //*****1********
            qryIfx.conectar();
            if ( rbnCurp_isChecked )
            {
                if (txtCurp16.trim().substring(4,10).length()==0 )
                    QBuskAlum = qryIfx.BuskAlum(4,"(a.curp[1,4]= '"+ txtCurp16.trim().substring(0,4)+"' AND a.curp16[11,16] = '"+txtCurp16.trim().substring(10,16)+")", "", "", "");
                else
                    QBuskAlum = qryIfx.BuskAlum(2,txtCurp16, "", "", "");
                if ( txtCurp16.trim().length()==16 && txtCurp17y18.trim().length()==2 )
                    QBuskAlum = qryIfx.BuskAlum(5,txtCurp16+txtCurp17y18, "", "", "");
            }
            else if ( rbnIdalu_isChecked )
                QBuskAlum = qryIfx.BuskAlum(4,"a.IDALU="+ txtIdalu, "", "", "");

            if ( QBuskAlum.isEmpty() )
                throw new SICEEO_Excepcion (0,"ALUMNO_NO_EXISTE");
            //***
            if ( QBuskAlum.size()==1 )
            {

                dr.put("maxCicEscIni",QBuskAlum.get(0).get("maxcicescini"));
                QYNoEnCicloAct = qryIfx.yNoEnCicloAct (""+QBuskAlum.get(0).get("idalu"), cicescini);
                //  DM.Q_ynoenCicloAct.Open;
                dr.put("enCicloAnterior", "" );

                //if dm.Q_ynoenCicloAct.RecordCount=0 then begin
                if ( !QBuskAlum.get(0).get("maxcicescini").equals(cicescini) )
                {
                    //   MessageDlg('Existe en la Base de Datos pero no esta inscrito en el Ciclo '+#13+#10+'Actual', mtWarning, [mbOK], 0);
                   dr.put("enCicloAnterior","SI" );
                   //exit ;
                }
                //               else
                //             begin

                QYEsDLaReg = qryIfx.yEsDLaReg("a.IDALU="+ QBuskAlum.get(0).get("idalu")+ " AND g.cicescini= "+cicescini);

                if ( QYEsDLaReg.size()==1 && !txtPermiso.equals("CAMPEON") && !QYEsDLaReg.get(0).get("cveunidad").equals(unidad) )  //and
                /*    (UPPERCASE(f_password.E_Usuario.Text)<>'PALEHDEZ')  AND
                      (UPPERCASE(f_password.E_Usuario.Text)<>'IVALLE'  )  AND
                      (UPPERCASE(f_password.E_Usuario.Text)<>'EMMANUEL')  AND
                      (UPPERCASE(f_password.E_Usuario.Text)<>'MARIOCIMM') AND
                      (UPPERCASE(f_password.E_Usuario.Text)<>'LPOBLETE')
              */{
                    throw new SICEEO_Excepcion (0,"ALUMNO_EN_OTRA_DELEG");
                }

                /*if ( !(""+QBuskAlum.get(0).get("grupo")).contains("_") )
                {*/
                this.dr.put("lblGrado_Text", QBuskAlum.get(0).get("grado")); // Dm.Q_ynoenCicloActgrado.AsString;
                this.dr.put("lblGrupo_Text", QBuskAlum.get(0).get("grupo")); //    Dm.Q_ynoenCicloActgrupo.AsString;
                this.dr.put("lblCct_Text", QYNoEnCicloAct.get("cct")) ; //flojera, ya no traje el cct
                this.dr.put("lblCiclo_Text", QBuskAlum.get(0).get("maxcicescini") +"-"+ (dm.toInt(""+QBuskAlum.get(0).get("maxcicescini"))+1) ) ;
                this.dr.put("lblRangoEdad_Visible", true);
                /*}
                else
                {
                    //el alumno es de la 59
                    this.dr.put("lblGrado_Text", " ");
                    this.dr.put("lblGrupo_Text", " ");
                    this.dr.put("blCct_Text", " ") ;
                    this.dr.put("lblCiclo_Text", " " ) ;
                    //apaga el rango de edad permitida
                    this.dr.put("lblRangoEdad_Visible", false);
                }*/

                this.dr.put("alumEstatus","U");
                QNormatividad = qryIfx.getNormatividad(""+QBuskAlum.get(0).get("cveplan"), ""+QBuskAlum.get(0).get("cveprograma"), ""+QBuskAlum.get(0).get("grado"));
                
                //  03 DE febrero 2017, lavy pidio que antes de iniciar el ciclo escolar
                //                       el alumnos aun no cumpla 15 años
                if ( (tblPrincipal_modalidad.equals("DES") || tblPrincipal_modalidad.equals("PES") || tblPrincipal_modalidad.equals("DST") || tblPrincipal_modalidad.equals("PST") ) && dm.toInt(tblPrincipal_grado)==1 )
                    QNormatividad.put("edadadmmax", ""+(dm.toInt(QNormatividad.get("edadadmmax"))+1));
            
                this.dr.put("edadMin", QNormatividad.get("edadadmmin") );
                this.dr.put("edadMax", QNormatividad.get("edadadmmax") );
                
                if ( (tblPrincipal_modalidad.equals("DES") || tblPrincipal_modalidad.equals("PES") || tblPrincipal_modalidad.equals("DST") || tblPrincipal_modalidad.equals("PST") || tblPrincipal_modalidad.equals("DTV") ) && dm.toInt(tblPrincipal_grado)==1 )  //26 de noviembre,
                    //this.dr.put("lblRangoEdad","("+QBuskAlum.get(0).get("cveprograma")+") Edad permitida: de "+QNormatividad.get("edadadmmin")+" a "+QNormatividad.get("edadadmmax")+" años a partir del 1ro. de agosto de "+cicescini+".");
                    this.dr.put("lblRangoEdad","("+QBuskAlum.get(0).get("cveprograma")+") Edad permitida: A partir del 1ro de agosto de "+(dm.toInt(cicescini)-dm.toInt(QNormatividad.get("edadadmmax")))+" hasta el 31 de diciembre de "+(dm.toInt(cicescini)-dm.toInt(QNormatividad.get("edadadmmin")))+".");
                else if ( tblPrincipal_modalidad.equals("DPR") || tblPrincipal_modalidad.equals("DPB"))
                    this.dr.put("lblRangoEdad","("+QBuskAlum.get(0).get("cveprograma")+") Edad permitida: A partir del 1ro de enero de "+(dm.toInt(cicescini)-dm.toInt(QNormatividad.get("edadadmmax")))+" hasta el 31 de diciembre de "+(dm.toInt(cicescini)-dm.toInt(QNormatividad.get("edadadmmin")))+".");
                else
                    this.dr.put("lblRangoEdad","("+QBuskAlum.get(0).get("cveprograma")+") Edad permitida: de "+QNormatividad.get("edadadmmin")+" a "+QNormatividad.get("edadadmmax")+" años cumplidos al 31 de diciembre de "+ QBuskAlum.get(0).get("maxcicescini") ); // inttostr(v_cicescini);
 //               else L_Edad.Caption:='Edad: '+intToStr(iTemp-iTemp2)+' años (al '+inttostr(v_cicescini)+')';

 //            alum_Estatus := 'O' ; //ok;
                this.dr.put("lblCurp_Text", QBuskAlum.get(0).get("curp") );
                this.dr.put("idalu",QBuskAlum.get(0).get("idalu") );
                if ( rbnIdalu_isChecked )
                    this.dr.put("txtCurp17y18_Text", (txtCurp17y18=(""+QBuskAlum.get(0).get("curp")).substring(16)) );

                if ( (""+QBuskAlum.get(0).get("curp")).length()==18 && txtCurp17y18.trim().length()!=2 )
                    this.dr.put("txtCurp17y18_Text", (txtCurp17y18=(""+QBuskAlum.get(0).get("curp")).substring(16)) );

                if ( (""+QBuskAlum.get(0).get("curp")).length()!=18 && txtCurp17y18.trim().length()==2 )
                {
                    //E_18.Font.Color :=clred;
 //                    alum_Estatus:= 'U'; // update
                }
                //CHEKAMOS LA CURP--------------------------------------------------------------------------------------------
                QCurpRenapo = qryIfx.curpRenapo(""+QBuskAlum.get(0).get("idalu"));
                
                //    (UPPERCASE(f_password.E_Usuario.Text)='FLALUISSA') OR
                
                QUsuario = qryIfx.usuario (txtUsuario);
                if  (/*(UPPERCASE(f_password.E_Usuario.Text)='PALEHDEZ') OR
                    (UPPERCASE(f_password.E_Usuario.Text)='MARIOCIMM') OR
                    (UPPERCASE(f_password.E_Usuario.Text)='LPOBLETE') OR
                    (UPPERCASE(f_password.E_Usuario.Text)<>'IVALLE')
                    */
                        (""+QUsuario.get("modulos")).contains(",14,") )
                    this.dr.put("modifCurp","SI");
                else
                {
                    this.dr.put("modifCurp","SI");
                    if ( QCurpRenapo.size()==1 )
                    {
                        if ( QBuskAlum.get(0).get("curp").equals(QCurpRenapo.get(0).get("curp")) && QBuskAlum.get(0).get("nombre").equals(QCurpRenapo.get(0).get("nombre")) && QBuskAlum.get(0).get("apepat").equals(QCurpRenapo.get(0).get("apepat")) && QBuskAlum.get(0).get("apemat").equals(QCurpRenapo.get(0).get("apemat")) )
                        {
                            //     v_ModifCurp :='NO';
                        }
                    }
                }
                //--------------------------------------------------------------------------------------------------------------
                // PERMITIR MODIFICAR CURP VALIDAD SOLO GRADOS TERMINALES Y SIN LOS 2 ULTIMOS DIGITOS
                this.dr.put("gdoTerminal","NO");
                if ( (QYNoEnCicloAct.get("cveplan").equals("3") && QYNoEnCicloAct.get("grado").equals("3")) ||
                    (QYNoEnCicloAct.get("cveplan").equals("1") && QYNoEnCicloAct.get("grado").equals("6")) ||
                    (QYNoEnCicloAct.get("cveplan").equals("2") && QYNoEnCicloAct.get("grado").equals("3")) )
                {
                    this.dr.put("gdoTerminal","SI");
                    this.dr.put("modifCurp","SI"); //volvemos a activar la variable para permitir el cambio
                }
                //----------------------------------------------------------------------------------------------------------------

                // L_difenom.Visible := False ;
                //   b_guarda_renapo.Visible:= False;
                //   b_guardar.Visible:= True;
                 //  DBT_nom_tot.Color :=  $00DD7631;
                if ( QCurpRenapo.size()==1 )
                {
                    if ( !QCurpRenapo.get(0).get("nom_tot").equals(QBuskAlum.get(0).get("nom_tot")) )
                    {
                        //  DBT_nom_tot.Color :=  clMaroon;
                        this.dr.put("lblDifeNom_Visible",true);
                    }
                    else {
                         // DBT_nom_tot.Color :=  $00DD7631;
                    }
                    
                    if ( !QCurpRenapo.get(0).get("curp").equals(QBuskAlum.get(0).get("curp")) )
                    {
                        // DBT_curp.Color :=  clMaroon;
                        this.dr.put("lblRenapo_Visible",true);
                    }
                    else {
                          //DBT_curp.Color :=  $00DD7631;
                    }
                    
                    /* e_18.ReadOnly    := True;
                        E_APL1.ReadOnly  := True;
                        E_APL2.ReadOnly  := True;
                        E_NOM.ReadOnly   := True;
                        E_SEXO.ReadOnly  := True;
                        E_EDAD.ReadOnly  := True;
                        BOX_ENT.Enabled  := False;
                        b_guardar.Enabled:= False;
                    */
                    this.dr.put("regCivCertAnt", QCurpRenapo.get(0).get("regcivcert"));
                    this.dr.put("validaAnt","SI");
                }
                else {
                    this.dr.put("txtCurp17y18_ReadOnly",false);
                    this.dr.put("txtApe1_ReadOnly",false);
                    this.dr.put("txtApe2_ReadOnly",false);
                    this.dr.put("txtNombre_ReadOnly",false);
                    this.dr.put("cbxSexo_ReadOnly",false);
                    this.dr.put("txtFechaNac_ReadOnly",false);
                    this.dr.put("cbxEntidad_Enabled",true);
                    this.dr.put("btnGuardar_Enabled",true);
                    
                    this.dr.put("regCivCertAnt", "");
                    this.dr.put("validaAnt","NO");
                }
                
                this.dr.put("txtIdalu_Text",QBuskAlum.get(0).get("idalu"));
                this.dr.put("txtApe1_Text",QBuskAlum.get(0).get("apepat"));
                this.dr.put("txtApe2_Text",QBuskAlum.get(0).get("apemat"));
                this.dr.put("txtNombre_Text",QBuskAlum.get(0).get("nombre"));
                this.dr.put("cbxSexo_SelectedItem",QBuskAlum.get(0).get("sexo"));
                this.dr.put("txtFechaNac_Text", dm.invFecha (""+QBuskAlum.get(0).get("fecnac"),"-").replace("-", "/") );
                this.dr.put("txtCrip_Text",QBuskAlum.get(0).get("crip"));
                this.dr.put("cbxEntidad_SelectedItem",QBuskAlum.get(0).get("cveentfednac"));
                this.dr.put("txaObservaciones",qryIfx.getObservacionesAluGdo (""+QBuskAlum.get(0).get("idalu"), ""+cicescini));
                    
                if ( (""+QBuskAlum.get(0).get("curp")).substring(11,13).equals("NE") )
                    this.dr.put("cbxEntidad_SelectedIndex",33); //NACIDO EN EL EXTRANJERO  posicion 33

                  //*
                //QCatDiscap = qryIfx.catDiscap (""+QBuskAlum.get(0).get("maxcicescini"), ""+QBuskAlum.get(0).get("cveplan"));

                //  WHILE NOT dm2.q_CatDiscap.Eof DO
                //  BEGIN
                //     BOX_discap.Items.Add(dm2.q_CatDiscapcveydes.AsString);
                //     dm2.q_CatDiscap.Next;
                //  END;
                //  Box_discap.ItemIndex   := 8;
                //  dm2.q_CatDiscap.Close;
                //*


   //              IF dm.Q_BuskAlumcvedefsuf.AsString <>'' THEN
   //                 box_discap.KeyValue:= dm.Q_BuskAlumcvedefsuf.AsString ; //    'N';
   //              ELSE
   //                 box_discap.KeyValue:= 'N';

                    //              dm2.q_CatDiscap.Locate('cvedefsuf',dm.Q_BuskAlumcvedefsuf.AsString, [loPartialKey]);
   //              v_discap := dm2.q_CatDiscapcveydes.AsString;

   //              MessageDlg('dis '+ v_discap, mtWarning, [mbOK], 0);
   //              box_discap.KeyValue:= v_discap ;
                 //              box_discap.text := dm.Q_BuskAlumcvedefsuf.AsString;
   //              box_discap.ItemIndex := box_discap.Items.IndexOf ('[ '+dm.Q_BuskAlumcvedefsuf.AsString);  // seleccionamos el grupo de la lista del combo


                this.dr.put("cbxProbem_SelectedItem",QBuskAlum.get(0).get("probem"));
                this.dr.put("cbxExtj_SelectedItem",QBuskAlum.get(0).get("extranjero"));

                if ( QBuskAlum.get(0).get("krtacompromiso").equals("SU") )
                    this.dr.put("cbxKrta_SelectedItem","SUSTITUIDA");
                else
                    this.dr.put("cbxKrta_SelectedItem",QBuskAlum.get(0).get("krtacompromiso"));


                //box_probem.ItemIndex:= box_probem.Items.IndexOf (DM.Q_BuskAlumprobem.AsString);

                //variables paraguardarlo en la tabla curpvit
                // v_idalu           := dm.Q_BuskAlumidalu.AsInteger;
                this.dr.put("curp_ant",QBuskAlum.get(0).get("curp"));
                this.dr.put("apepat_ant",QBuskAlum.get(0).get("apepat"));
                this.dr.put("apemat_ant",QBuskAlum.get(0).get("apemat"));
                this.dr.put("nombre_ant",QBuskAlum.get(0).get("nombre"));
                
                //ya encontrado ahora lo buscamos en la tabla de RENAPOencontrados
                // select * from renapoEncontrados where col003='DIRG970807MOCZZR03'
                
                //--Vista--> E_EdadExit(sender);
                //--Vista--> if length(E_18.Text)<> 2 then
                //--Vista-->    E_18.SetFocus
                //--Vista--> else
                //--Vista-->    E_apl1.SetFocus;
                
                //b_guardar.SetFocus;
            }else
            //       }
            //    }
            if ( QBuskAlum.size() > 1 )
                throw new SICEEO_Excepcion (0,"MAS_DE_UNO");
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.ModifCurp(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

    private void btnGuardar_ActionPerfomed (String laU, int cicescin, String cicescini, int edadMin, int edadMax, 
            String enCicloAnterior, String gdoTerminal, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String modifCurp, 
            String txtPermiso, String kienLlamas, String alumEstatus, String curp_ant, String nombre_ant, String apepat_ant, String apemat_ant, 
            String regCivCert_ant, String valida_ant, String idalu, String lblCurp, String txtCurp16, String txtCurp17y18, String txtCrip, String lblCct,  
            String txtNombre, String txtApe1, String txtApe2, String cbxSexo, String txtFechaNac, String cbxKrta, String cbxEntidad, String cbxProbem, 
            String cbxExtj, String observaciones, String txtUsuario )
    {
        String edadValida, entNac, probem="", extj="";
        int conFolio = 0;
        ArrayList <Map> QAlumCapCalif;
        boolean hacerCommit=false;
        
        edadValida="N";
        this.dr.put("lblEdad_Text","Edad: "+Edad(txtFechaNac,cicescini)+" años (al "+cicescini+")");
        
        this.dr.put("alumEstatus",alumEstatus);

        try
        {
            if ( edadMin==0 && enCicloAnterior.equals("") )
                throw new SICEEO_Excepcion (0,"BUSCAR");

            if ( Edad(txtFechaNac, cicescini) >= edadMin &&  Edad(txtFechaNac, cicescini) <= edadMax ) //dm.Q_normatividadedadadmmin.AsInteger) and  //dm.Q_normatividadedadadmmax.AsInteger) then
                edadValida="S";

            if ( txtUsuario.trim().toUpperCase().equals("IVALLEyano") )
                edadValida = "S";

            if ( lblCct.length()>5 && lblCct.substring(0,5).equals("20DML") )                            // los de educacion especial rebasan la edad permitida
                edadValida = "S";

            qryIfx.conectarConTransaccion();

            if ( kienLlamas.equals("DESDEBAJAS") || kienLlamas.equals("DESDECALIF") )
            {
                conFolio = qryIfx.QFolioIdalu(idalu, "GPO");
                if(conFolio>0)
                    throw new SICEEO_Excepcion (0,"CON_FOLIO");
                String estatusAlu = ( dm.toInt(cicescini) == cicescin )? "I":"BD";
                QAlumCapCalif = qryIfx.alumCapCalif(estatusAlu, cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
                if ( !QAlumCapCalif.isEmpty() && dm.toInt(QAlumCapCalif.get(0).get("cicescini")) < cicescin )
                   edadValida="S";
            }

            if ( enCicloAnterior.equals("SI") )    //si encontramos al alumno en algun cicilo anterior ya no se
                edadValida = "S";         // le aplica la regla de la edad

            if ( edadValida.equals("N") && !txtUsuario.trim().toUpperCase().equals("IVALLE") && !txtUsuario.trim().toUpperCase().equals("MARIOCIMM") && !txtCurp16.trim().substring(4,10).toUpperCase().equals(lblCurp.trim().toUpperCase().substring(4,10)) && !txtUsuario.trim().toUpperCase().equals("PALEHDEZchekarxxx") ) // AND
               throw new SICEEO_Excepcion (0,"EDAD_INVALIDA");
            
            this.dr.put("laU", (laU="no") );
            txtCurp16 = verCurp(txtNombre, txtApe1, txtApe2, txtFechaNac, cbxSexo, cbxEntidad, txtCurp17y18);
            
            if (alumEstatus.equals("U") )
            {
                if ( txtCurp17y18.contains("O") )
                    throw new SICEEO_Excepcion (0,"2ULT_CURP");
                if ( txtNombre.indexOf("  ")>0 )
                    throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","nombre");
                if ( txtApe1.indexOf("  ")>0 )
                    throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","apellido");
                if ( txtApe2.indexOf("  ")>0 )
                    throw new SICEEO_Excepcion (0,"ESPACIO_EXTRA","apellido");
                //++++++++++++++++++++++++++++++++
                if ( txtNombre.contains("|") )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                if ( txtApe1.contains("|") )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
                if ( txtApe2.contains("|") )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
                //++++++++++++++++++++++++++++++++
                if ( txtNombre.trim().length()==0 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","nombre");
                if ( txtApe1.trim().length()==0 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","primer apellido");
                //++++++++++++++++++++++++++++++++                              //Condición agregada en SICEEO
                if ( !dm.isNombreOApellido (txtNombre.trim(),40) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");
                if ( !dm.isNombreOApellido (txtApe1.trim(),30) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
                if ( !txtApe2.equals("") && !dm.isNombreOApellido (txtApe2.trim(),30) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
                //++++++++++++++++++++++++++++++++
                if ( txtCurp17y18.trim().length() == 1 )
                    throw new SICEEO_Excepcion (0,"TAM_CURP");

                try{
                    if (txtCurp17y18.length()==2)
                        Integer.parseInt(txtCurp17y18.trim().substring(1,2));
                }catch(Exception ex){
                    throw new SICEEO_Excepcion (0,"DIG_VERIF");
                }

                if ( txtCurp16.substring(11,13).equals("ND") && txtCurp17y18.trim().length()==2 )
                   throw new SICEEO_Excepcion (0,"CURP_CON_NO_DEF");
                
                
                this.dr.put("txtNombre_Text", ( txtNombre=txtNombre.trim().toUpperCase()) );
                this.dr.put("txtApe1_Text", ( txtApe1=txtApe1.trim().toUpperCase()) );
                this.dr.put("txtApe2_Text", ( txtApe2=txtApe2.trim().toUpperCase()) );

                if ( txtNombre.trim().equals("") )
                    throw new SICEEO_Excepcion (0,"NIÑO_SIN_NOMBRE");
                
                entNac= cbxEntidad.substring(cbxEntidad.length()-6, cbxEntidad.length()-6+2);
                if (!cbxProbem.equals("")) probem = ""+cbxProbem.charAt(0);
                if (!cbxExtj.equals("")) extj = ""+cbxExtj.charAt(0);
                  
                qryIfx.modificarCurp(gdoTerminal, modifCurp, curp_ant, nombre_ant, apepat_ant, apemat_ant, regCivCert_ant, valida_ant, 
                        txtPermiso, idalu, txtCurp16, txtCurp17y18, txtCrip, txtNombre, txtApe1, txtApe2, cbxSexo, txtFechaNac, cbxKrta, 
                        entNac, probem, extj, txtUsuario, dr);
                qryIfx.setObservacionesAluGdo(idalu, cicescini, tblPrincipal_grado, observaciones, txtUsuario);
                
                //-------------- DATOS SÓLO PARA USO EN SICEEO -----------------
                this.dr.put("nombreActualizado", txtApe1+"/"+txtApe2+"*"+txtNombre);
                this.dr.put("curpActualizada", txtCurp16+txtCurp17y18);
                //---------------------------------------------------------------------------------------------------------------
                //IF v_ModifCurp = 'SI' THEN
                if ( curp_ant.equals(txtCurp16+txtCurp17y18) && nombre_ant.equals(txtNombre) && apepat_ant.equals(txtApe1) && apemat_ant.equals(txtApe2) ){  //If implentado sólo en SICEEO
                    this.dr.put("returnCase",1);
                    mensaje.General("ACTUALIZACION_EXITOSA", "", "", this.dr);
                }else if ( this.dr.get("laU").equals("si") ){
                    this.dr.put("returnCase",1);
                    mensaje.ModifCurp("CURP_ACTUALIZADA", "", "", this.dr);
                    this.dr.put("laU", (laU="") );
                }else if ( modifCurp.equals("SI") ){
                    this.dr.put("returnCase",1); 
                    mensaje.ModifCurp("CURP_ACTUALIZADA_RENAPO", "", "", this.dr);
                }

                this.dr.put("txtNombre_Text", "");
                this.dr.put("txtApe1_Text", "");
                this.dr.put("txtApe2_Text", "");
                this.dr.put("txtFechaNac_Text", "");
                this.dr.put("cbxSexo_SelectedItem", "");

                if ( modifCurp.equals("NO") ){
                    this.dr.put("txtCurp16_Text",lblCurp.substring(0,16) );
                    this.dr.put("txtCurp17y18_Text",lblCurp.substring(16,18) );
                }

                this.dr.put("idalu","");
                this.dr.put("txtCrip_Text","");
                //E_18.Font.Color :=clBlack ;
                this.dr.put("lblCurp_Text","");
                
                //--Vista--> E_CURP.SetFocus;
                
                this.dr.put("alumEstatus","X");
                
                if ( kienLlamas.equals("DESDEBAJAS") || kienLlamas.equals("DESDECALIF") )
                   this.dr.put("WINDOW_CLOSE",true);
            
                if ( modifCurp.equals("NO") && !txtPermiso.equals("CAMPEON") )
                   this.dr.put("btnBuskAlum_Click",true);
                
                hacerCommit = true;
            }    
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.ModifCurp(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private int Edad(String fechaNacimiento, String cicescini) //La fecha en formato dd/MM/yyyy
    {
        int anioNac, anioAct;
        anioNac = dm.toInt(fechaNacimiento.substring(6,10));
        anioAct = dm.toInt(cicescini);
        
        if (anioAct<anioNac)
            return anioAct-anioNac-1;
        else
            return anioAct-anioNac;
    }
    
    private String verCurp(String nombre, String primerApe, String segundoApe, String fechaNacimiento, String sexo, String entidad, String digVerif) //La fecha en formato dd/MM/yyyy
    {
        //*L_avisoGuard.Visible:= fALSE;
        SICEEO_ValidarCurp validarCurp = new SICEEO_ValidarCurp(nombre, primerApe, segundoApe, fechaNacimiento.substring(8,10), fechaNacimiento.substring(3,5), fechaNacimiento.substring(0,2), sexo, entidad.substring(entidad.length()-2,entidad.length()));
        return validarCurp.curp()/* + digVerif*/;
    }

}
