package modelo.Ubicar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 2/12/2015, 01:16:28 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Ajustar {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_Ajustar (Map datosReturn)
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
    public void ejecutarPeticion (HttpServletRequest request, String metodo)
    {
        this.r = new SICEEO_HttpServletRequest(request);
        
        if (metodo.equals("foAc")){
            formActivate ( r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("cicesciniPreinsc") );
        }else if (metodo.equals("btBuAl_Cl")){
            btnBuskAlum_Click ( dm.toBool(r.gP("rbnCurp_isChecked")), dm.toBool(r.gP("rbnIdaluX_isChecked")), r.gP("txtCurp"), r.gP("txtIdalu") );
        }else if (metodo.equals("cbCu_ChSeIt"))
            cbxCurps_ChangeSelectedItem (r.gP("idalu"));
        else if (metodo.equals("tbAl_MoCl"))
            tblAlumgrado_MouseClicked (r.gP("tblAlumgrado_cicescini"), r.gP("tblAlumgrado_idalu"));
    }
    
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void formActivate (String tblPrincipal_idcct, String tblPrincipal_modalidad, String tblPrincipal_cveplan, String tblPrincipal_grado, 
            String cicesciniPreinsc)
    {
        
    }
    
    private ArrayList<Map> btnBuskAlum_Click (boolean rbnCurp_isChecked, boolean rbnIdaluX_isChecked, String txtCurp, String txtIdalu)
    {
        String siBusca, condicionQBuskAlum1="";
        ArrayList<Map> QBuskAlum1 = null;
        //*****1********
        //if e_permiso.Text='DAIMASTERDAI' THEN BEGIN
        //B_ELIMINA_ALU.Visible:= tRUE;
        //B_ELIMINAmOV.Visible := tRUE;
        //END
        //ELSE BEGIN
        //B_ELIMINA_ALU.Visible:= FALSE;
        //B_ELIMINAmOV.Visible := FALSE;
        //END;
        siBusca ="N";
        //    if length(trim(copy(E_curp.Text,5,6))) =0 then
        //    DM.Q_BuskAlum1.MacroByName('CONDICI').AsString := '(a.curp[1,4]= '+#39+ copy(e_curp.Text,1,4)+#39+' AND a.curp16[11,16] = '+#39+ copy(e_curp.Text,11,6)+#39 +')' ;
        //    ELSE
        try
        {
            if (rbnCurp_isChecked){
                if (txtCurp.trim().length()>=10){
                    siBusca = "S";
                    condicionQBuskAlum1 = "a.curp like '"+txtCurp+"%'";
                }else
                    throw new SICEEO_Excepcion (0,"TAM_CURP");
            }
            
            if ( rbnIdaluX_isChecked && txtIdalu.trim().length()>0){
                siBusca = "S";
                condicionQBuskAlum1 = "a.IDALU = '"+ txtIdalu +"'";
            }
            
            if (siBusca.equals("N"))
                throw new SICEEO_Excepcion (-100,"Exit");                       //Solo salir y cancelar las acciones
            
            qryIfx.conectar();
            QBuskAlum1 = qryIfx.BuskAlum1(condicionQBuskAlum1);
            
            if (QBuskAlum1.isEmpty() )
            {
                throw new SICEEO_Excepcion (0,"ALUMNO_NO_EXISTE"); //Simplemente que ya no haga nada
                //*--> exit;
            }
            //***
            //    if dm.Q_BuskAlum1.RecordCount=1 then
            //       begin
            //  RxDBLookupCombo1.LookupDisplayIndex
            if ( !QBuskAlum1.get(0).get("estatusalu").equals("BD") && !QBuskAlum1.get(0).get("estatusalu").equals("BT") && !QBuskAlum1.get(0).get("estatusalu").equals("I") )
                dr.put("btnCambSituacion_Enabled", false);
            else
                dr.put("btnCambSituacion_Enabled", true);
            
            if ( QBuskAlum1.get(0).get("estatusalu").equals("BD") || QBuskAlum1.get(0).get("estatusalu").equals("BT") )
                dr.put("btnCambSituacion_Text", "Cambio de Estatus a Inscrito");
                //b_cambsituacion.Font.Color:= clYellow;
            if (QBuskAlum1.get(0).get("estatusalu").equals("I") )
                dr.put("btnCambSituacion_Text", "Cambio de Estatus a Baja");
              // b_cambsituacion.Font.Color:= clwhite;
            
            dr.put("txtIdalu_Text",QBuskAlum1.get(0).get("idalu"));
            dr.put("txtCurp_text",QBuskAlum1.get(0).get("curp"));
            
            //L_nom_tot.Caption := DM.Q_BuskAlum1nom_tot.AsString;
            dr.put("tblAlumgrado",qryIfx.susEstud(""+QBuskAlum1.get(0).get("idalu")));
            //--Vista--> dm.Q_susEstud.Last;
            dr.put("tblMaterias",qryIfx.susMat(""+((ArrayList<Map>)dr.get("tblAlumgrado")).get(((ArrayList<Map>)dr.get("tblAlumgrado")).size()-1).get("cicescini"), ""+QBuskAlum1.get(0).get("idalu")));
            // Dm.Q_enAlumMat.Open;
            // Dm.Q_Movi.Open;
            // dM2.Q_curpValida.Open;

            // if dm.Q_curpValida.RecordCount=1 then
            //     DBT_curpValida.Visible:= True
            //else DBT_curpValida.Visible:= False;
            
            dr.put("tblCertificados",qryIfx.suFolio(""+QBuskAlum1.get(0).get("idalu")));
            dr.put("suFolioC",qryIfx.suFolioC(""+QBuskAlum1.get(0).get("idalu")));
            
            //22/01/2013 agregado x solicitud
            dr.put("tblComentarios",qryIfx.Comentario(""+QBuskAlum1.get(0).get("idalu")));

            // if dm.v_tipo_usuario<>' ' then // cuando es un usuario tipo CCT
            //    begin
            //      if dm.Q_susEstudidcct.AsInteger  <> dm.Q_Principalidcct.AsInteger then
            //      begin
            //      Dm.Q_SusEstud.Close;
            //      Dm2.Q_SuFolio.Close;
            //      Dm2.Q_SuFolioC.Close;
            //      Dm.Q_Movi.Close ;
            //      dm.q_susMat.Close;
            //      Dm.Q_enAlumMat.Close;
            //      MessageDlg('El Alumno existe en la Base de Datos pero no pertenece a la '+#13+#10+'Escuela', mtConfirmation, [mbOK], 0);
            //      exit;
            //      end;
            //    end;
            //Dm.Q_enAlumMat.Last;
               // Dm.Q_Movi.Last;


               // Dm.Q_ESCUsegun.Open;

           // DM.Q_ynoenCicloAct.CLOSE;
 //      end;
//    end;
            
            if (QBuskAlum1.size()>1)
            {
                this.dr.put("returnCase",1);
                mensaje.Ajustar("MAS_DE_UNO", "", "", this.dr);
                
                this.dr.put("cbxCurps_Focus",true);
                //--Vista--> RxDBLookupCombo1.Perform(lb_SetTopIndex,1,0); //  LookupDisplayIndex:=1;
            }
            
            int numFilas = QBuskAlum1.size();
            ArrayList<Map> cbxCurps = new ArrayList<Map>();
            for (int i=0; i<numFilas; i++){
                Map curpEIdalu = new HashMap();
                curpEIdalu.put("idalu",""+QBuskAlum1.get(i).get("idalu"));
                curpEIdalu.put("curp",(i+1)+") "+QBuskAlum1.get(i).get("curp"));
                cbxCurps.add(curpEIdalu);
            }
            dr.put("cbxCurps", cbxCurps);
            dr.put("txtNombreCompleto", ""+QBuskAlum1.get(0).get("nom_tot"));
            dr.put("lblEstatusAlu", ""+QBuskAlum1.get(0).get("estatusalu"));
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Ajustar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        return QBuskAlum1;
    }
    
    public void cbxCurps_ChangeSelectedItem (String idalu)
    {
        ArrayList<Map> QBuskAlum1;
        QBuskAlum1 = btnBuskAlum_Click (false, true, "", idalu);
        if ( dm.toInt(dr.get("returnCase"))> 0 )
        {
            if (!QBuskAlum1.get(0).get("estatusalu").equals("BD") && !QBuskAlum1.get(0).get("estatusalu").equals("BT") && !QBuskAlum1.get(0).get("estatusalu").equals("I") )
                dr.put("btnCambSituacion_Enabled",false);
            else
                dr.put("btnCambSituacion_Enabled",true);
                
            if ( QBuskAlum1.get(0).get("estatusalu").equals("BD") || QBuskAlum1.get(0).get("estatusalu").equals("BT") ) {
                dr.put("btnCambSituacion_Text","Cambio de Estatus a Inscrito");
                dr.put("btnCambSituacion_FontColor","black");
            }
           
            if ( QBuskAlum1.get(0).get("estatusalu").equals("I") )
                dr.put("btnCambSituacion_Text","Cambio de Estatus a Baja");
                //b_cambsituacion.Font.Color:= clWhite;
        }
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
}
