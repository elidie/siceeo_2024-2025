
package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 16/02/2016, 08:48:51 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_SecHist 
{
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_SecHist (Map datosReturn)
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
        
        if (metodo.equals("foAc"))
            FormActivate (r.gP("tblAlumCapCalif_idalu"), r.gP("tblAlumCapCalif_frma"), r.gP("usuarioNo22"), r.gP("txtUsuario"), r.gP("cicescini"));
        else if (metodo.equals("tbCa_ChSeIt"))
            tblCalif_ChangeSelectedItem ( r.gP("tblAlumCapCalif_idalu"), dm.toInt(r.gP("grado")), r.gP("cvetipmat"), r.gP("cvemat"), r.gP("msgExmExt") );
//..................................................................................................................................................................................................................
        else if (metodo.equals("btGdCa1"))
            btnGdaCalif1ro_Click(dm.vstrToArrMap(r.gPV("tblCalif1ro"), "~", new String[]{"cvemat","cvetipmat","califant","promedio","promedio_oldValue"}), 
                    dm.toInt(r.gP("cicescin")), r.gP("tblCalif1ro_cicescini"), dm.toInt(r.gP("tblPrincipal_cveplan")), r.gP("tblCalif1ro_idalu"), 
                    r.gP("tblCalif1ro_almextrj"), dm.toInt(r.gP("tblCalif1ro_matrepact")), dm.toInt(r.gP("tblCalif2do_matrepact")), r.gP("tblCalif2do_cicescini"),
                    dm.toInt(r.gP("tblCalif3ro_cicescini")), dm.toInt(r.gP("tblCalif2do_size")), dm.toInt(r.gP("tblCalif3ro_size")), r.gP("txtUsuario"));
        else if (metodo.equals("btCaDBi1"))
            btnCalifDBim1ro_Click (dm.vstrToArrMap(r.gPV("tblCalif1ro"), "~", new String[]{"cvemat","cvetipmat","exm","eer","promedio"}), 
                    r.gP("tblCalif1ro_cicescini"), r.gP("tblCalif1ro_idalu"), r.gP("txtUsuario"));
        else if (metodo.equals("btGuOp1"))
            btnGuardaExamExt1ro_Click(dm.vstrToArrMap(r.gPV("tblExmExt1ro"), "~", new String[]{"idarray","dia","mes","anio","promedio","cct","idcct_apl"}),
                    dm.vstrToArrMap(r.gPV("tblExmExt1ro_OldValues"), "~", new String[]{"idarray","dia","mes","anio","promedio","cct","idcct_apl"}),
                    r.gP("idalu_oldValue"), r.gP("tblCalif1ro_cicescini"), r.gP("grado_oldValue"), r.gP("cvetipmat_oldValue"), r.gP("cvemat_oldValue"), 
                    r.gP("txtUsuario"));
        else if(metodo.equals("btOfExmExt1"))
            btnOfExmExt1ro_Click(dm.vstrToArrMap(r.gPV("tblCalif1ro"), "~", new String[]{"cvemat","cvetipmat","califant","promedio","promedio_oldValue"}), 
                    dm.toInt(r.gP("cicescin")), r.gP("tblCalif1ro_cicescini"), dm.toInt(r.gP("tblPrincipal_cveplan")), r.gP("tblCalif1ro_idalu"), 
                    r.gP("tblCalif1ro_almextrj"), dm.toInt(r.gP("tblCalif1ro_matrepact")), dm.toInt(r.gP("tblCalif2do_matrepact")), r.gP("tblCalif2do_cicescini"),
                    dm.toInt(r.gP("tblCalif3ro_cicescini")), dm.toInt(r.gP("tblCalif2do_size")), dm.toInt(r.gP("tblCalif3ro_size")), r.gP("txtUsuario"));
//..................................................................................................................................................................................................................        
        else if (metodo.equals("btGdCa2"))
            btnGdaCalif2do_Click(dm.vstrToArrMap(r.gPV("tblCalif2do"), "~", new String[]{"cvemat","cvetipmat","califant","promedio","promedio_oldValue"}), 
                    dm.toInt(r.gP("cicescin")), r.gP("tblCalif2do_cicescini"), dm.toInt(r.gP("tblPrincipal_cveplan")), r.gP("tblCalif2do_idalu"), 
                    r.gP("tblCalif2do_almextrj"), dm.toInt(r.gP("tblCalif2do_matrepact")), dm.toInt(r.gP("tblCalif1ro_matrepact")), 
                    dm.toInt(r.gP("tblCalif3ro_cicescini")),dm.toInt(r.gP("tblCalif1ro_size")), dm.toInt(r.gP("tblCalif3ro_size")), r.gP("txtUsuario"));
        else if (metodo.equals("btCaDBi2"))
            btnCalifDBim2do_Click (dm.vstrToArrMap(r.gPV("tblCalif2do"), "~", new String[]{"cvemat","cvetipmat","exm","eer","promedio"}), 
                    r.gP("tblCalif2do_cicescini"), r.gP("tblCalif2do_idalu"), r.gP("txtUsuario"));
        else if (metodo.equals("btGuOp2"))
            btnGuardaExamExt2do_Click(dm.vstrToArrMap(r.gPV("tblExmExt2do"), "~", new String[]{"idarray","dia","mes","anio","promedio","cct","idcct_apl"}),
                    dm.vstrToArrMap(r.gPV("tblExmExt2do_OldValues"), "~", new String[]{"idarray","dia","mes","anio","promedio","cct","idcct_apl"}),
                    r.gP("idalu_oldValue"), r.gP("tblCalif2do_cicescini"), r.gP("grado_oldValue"), r.gP("cvetipmat_oldValue"), r.gP("cvemat_oldValue"), 
                    r.gP("txtUsuario"));
//..................................................................................................................................................................................................................        
        else if (metodo.equals("btGdCa3"))
            btnGdaCalif3ro_Click(dm.vstrToArrMap(r.gPV("tblCalif3ro"), "~", new String[]{"cvemat","cvetipmat","califant","promedio","promedio_oldValue"}), 
                    dm.toInt(r.gP("cicescin")), dm.toInt(r.gP("tblCalif3ro_cicescini")), dm.toInt(r.gP("tblPrincipal_cveplan")), r.gP("tblCalif3ro_idalu"), 
                    r.gP("tblCalif3ro_almextrj"), dm.toInt(r.gP("tblCalif1ro_matrepact")), dm.toInt(r.gP("tblCalif2do_matrepact")), 
                    dm.toInt(r.gP("tblCalif2do_cicescini")),dm.toInt(r.gP("tblCalif1ro_size")), dm.toInt(r.gP("tblCalif2do_size")), r.gP("txtUsuario"));
        else if (metodo.equals("btCaDBi3"))
            btnCalifDBim3ro_Click (dm.vstrToArrMap(r.gPV("tblCalif3ro"), "~", new String[]{"cvemat","cvetipmat","exm","eer","promedio"}), 
                    r.gP("tblCalif3ro_cicescini"), r.gP("tblCalif3ro_idalu"), r.gP("txtUsuario"));
        else if (metodo.equals("btGuOp3"))
            btnGuardaExamExt3ro_Click(dm.vstrToArrMap(r.gPV("tblExmExt3ro"), "~", new String[]{"idarray","dia","mes","anio","promedio","cct","idcct_apl"}),
                    dm.vstrToArrMap(r.gPV("tblExmExt3ro_OldValues"), "~", new String[]{"idarray","dia","mes","anio","promedio","cct","idcct_apl"}),
                    r.gP("idalu_oldValue"), r.gP("tblCalif3ro_cicescini"), r.gP("grado_oldValue"), r.gP("cvetipmat_oldValue"), r.gP("cvemat_oldValue"), 
                    r.gP("txtUsuario"));
//..................................................................................................................................................................................................................        
        else if (metodo.equals("btAcRe_Cl"))
            btnActualizaReprobadas_Click (r.gP("tblAlumCapCalif_idalu"), r.gP("tblCalif2do_cicescini"), dm.toInt(r.gP("tblCalif3ro_cicescini")), 
                dm.toInt(r.gP("tblCalif1ro_matrepact")), dm.toInt(r.gP("tblCalif2do_matrepact")), dm.toInt(r.gP("tblCalif1ro_size")), 
                dm.toInt(r.gP("tblCalif2do_size")), dm.toInt(r.gP("tblCalif3ro_size")));
        
        else if (metodo.equals("btnExmOf"))
            ModalActive(r.gP("tblAlumCapCalif_idalu"),r.gP("grado"),r.gP("tblCalif_cicescini") );
        
        else if (metodo.equals("btOfExmExt_cl"))
            btnOficializaExmExt(dm.vstrToArrMap(r.gPV("tblExmExtXMat"), "~", new String[]{"selec","idalu","grado","cvemat","cvetipmat","cicescini","mes","anio"}),r.gP("matsel"));
        
    }
    
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void FormActivate (String tblAlumCapCalif_idalu, String tblAlumCapCalif_frma, String usuarioNo22, String txtUsuario, String cicescini)
    {
        ArrayList<Map> QCalif1ro, QCalif2do, QCalif3ro, QExmExt1ro, QExmExt2do, QExmExt3ro;
        ArrayList<String> ccts1, idccts1, ccts2, idccts2, ccts3, idccts3, meses1, meses2, meses3, meses;
        Map QUsuario;
        ArrayList<Map> mesesExmExt;
        int matrepact1ro = 0, matrepact2do = 0, matrepact3ro = 0;
        try
        {
            if(!tblAlumCapCalif_frma.equals("null") && !tblAlumCapCalif_frma.equals("false")){
                throw new SICEEO_Excepcion (-1,"ALUM_CON_CERTIFICADO");
            }
            qryIfx.conectar();
            dr.put("tblCalif1ro",QCalif1ro=qryIfx.calif1ro (tblAlumCapCalif_idalu));
            dr.put("tblCalif2do",QCalif2do=qryIfx.calif2do (tblAlumCapCalif_idalu));
            dr.put("tblCalif3ro",QCalif3ro=qryIfx.calif3ro (tblAlumCapCalif_idalu));
            
            dr.put("tblExmExt1ro",QExmExt1ro=(!QCalif1ro.isEmpty())?qryIfx.exmExt1ro_onSelect (tblAlumCapCalif_idalu, ""+QCalif1ro.get(0).get("cvetipmat"), ""+QCalif1ro.get(0).get("cvemat")):new ArrayList<Map>());
            dr.put("tblExmExt2do",QExmExt2do=(!QCalif2do.isEmpty())?qryIfx.exmExt2do_onSelect (tblAlumCapCalif_idalu, ""+QCalif2do.get(0).get("cvetipmat"), ""+QCalif2do.get(0).get("cvemat")):new ArrayList<Map>());
            dr.put("tblExmExt3ro",QExmExt3ro=(!QCalif3ro.isEmpty())?qryIfx.exmExt3ro_onSelect (tblAlumCapCalif_idalu, ""+QCalif3ro.get(0).get("cvetipmat"), ""+QCalif3ro.get(0).get("cvemat")):new ArrayList<Map>());
            
            dr.put("desmat1ro", !QCalif1ro.isEmpty() ? ""+QCalif1ro.get(0).get("desmat") : "");
            dr.put("desmat2do", !QCalif2do.isEmpty() ? ""+QCalif2do.get(0).get("desmat") : "");
            dr.put("desmat3ro", !QCalif3ro.isEmpty() ? ""+QCalif3ro.get(0).get("desmat") : "");
            
            ccts1 = new ArrayList<String>();
            idccts1 = new ArrayList<String>();
            ccts2 = new ArrayList<String>();
            idccts2 = new ArrayList<String>();
            ccts3 = new ArrayList<String>();
            idccts3 = new ArrayList<String>();
            meses1 = new ArrayList<String>();
            meses2 = new ArrayList<String>();
            meses3 = new ArrayList<String>();
            meses = new ArrayList<String>();
                        
            QUsuario = qryIfx.usuario (txtUsuario);
            mesesExmExt = qryIfx.getMesExt(cicescini);
            //-------PRIMERO -------------------------------------------------------------------------------------------
            if ( QCalif1ro.size()>=1 )
            {
                ccts1.add(""+QCalif1ro.get(0).get("cct"));
                idccts1.add(""+QCalif1ro.get(0).get("idcct"));                
                
                dr.put("pnlExamExtraord1ro_Visible", true); //mostrar si es 22      
                matrepact1ro = Integer.parseInt(""+QCalif1ro.get(0).get("matrepact"));
                if(matrepact1ro ==0 || matrepact1ro>=5){                    
                    dr.put("tblCalif1ro_editable","");
                    dr.put("pnlExamenes1ro_visible",false);
                    dr.put("msgExm1ro",(matrepact1ro==0 ? "No tiene materias reprobadas":"Tiene más de 4 materias reprobadas"));
                } else {
                    dr.put("pnlExamenes1ro_visible",true);
                    dr.put("tblCalif1ro_editable","textbox");
                    dr.put("msgExm1ro","");
                }                                                
            }
            else
                dr.put("pnlExamExtraord1ro_Visible", false);
            
            //-----SEGUNDO---------------------------------------------------------------------------------------------
            if ( QCalif2do.size()>=1 )
            {
                ccts1.add( ""+QCalif2do.get(0).get("cct") );
                idccts1.add( ""+QCalif2do.get(0).get("idcct") );
                ccts2.add( ""+QCalif2do.get(0).get("cct") );
                idccts2.add( ""+QCalif2do.get(0).get("idcct") );               
                //  p_2do.Visible:= true;

                //no mostrar si es de la 5nueve
                /*if ( (""+QCalif2do.get(0).get("grupo")).contains("_") )
                {
                    //          if (f_password.E_Usuario.Text='IVALLE') or
                    //             (f_password.E_Usuario.Text='MARIOCIMM') or

                    if ( (""+QUsuario.get("modulos")).contains(",4,") || usuarioNo22.equals("si") )
                        dr.put("pnlExamExtraord2do_Visible", true);             //mostrar a estos usuarios
                    else
                        dr.put("pnlExamExtraord2do_Visible", false);
                }else*/
                dr.put("pnlExamExtraord2do_Visible", true); //mostrar si es 22
                matrepact2do = Integer.parseInt(""+QCalif2do.get(0).get("matrepact"));
                if(matrepact2do==0 ||matrepact2do>=5){
                    dr.put("pnlExamenes2do_visible",false);
                    dr.put("tblCalif2do_editable","");
                    dr.put("msgExm2do",(matrepact2do==0 ? "No tiene materias reprobadas":"Tiene más de 4 materias reprobadas"));
                } else {
                    dr.put("pnlExamenes2do_visible",true);
                    dr.put("tblCalif2do_editable","textbox");
                    dr.put("msgExm2do","");
                }
                    

                /*if (dm2.q_Calif2docicescini.AsInteger<=2006) then
                    {
                    meses2.Add('AGOSTO');
                    meses2.Add('SEPTIEMBRE');
                    meses2.Add('FEBRERO')
                    }
                 if (dm2.q_Calif2docicescini.AsInteger=2007) then
                    {
                    meses2.Add('AGOSTO');
                    meses2.Add('SEPTIEMBRE');
                    meses2.Add('ENERO')
                    }
                 if dm2.q_Calif2docicescini.AsInteger>=2008 then
                    {
                    meses2.Add('AGOSTO');
                    meses2.Add('SEPTIEMBRE');
                    meses2.Add('ENERO')
                    }
                */

            } else
                dr.put("pnlExamExtraord2do_Visible", false);
            
            //-------TERCERO-------------------------------------------------------------------------------------------
            if ( QCalif3ro.size()>=1 )
            {
                ccts1.add( ""+QCalif3ro.get(0).get("cct") );
                idccts1.add( ""+QCalif3ro.get(0).get("idcct") );
                ccts2.add( ""+QCalif3ro.get(0).get("cct") );
                idccts2.add( ""+QCalif3ro.get(0).get("idcct") );
                ccts3.add( ""+QCalif3ro.get(0).get("cct") );
                idccts3.add( ""+QCalif3ro.get(0).get("idcct") );
                
                //   p_3ro.Visible:= true;
                //no mostrar si es de la 5nueve
                /*if ( (""+QCalif3ro.get(0).get("grupo")).contains("_") )
                {
                    //          if (f_password.E_Usuario.Text='IVALLE') or
                    //             (f_password.E_Usuario.Text='MARIOCIMM') or
                    //             (dm.v_UsuarioNo22 ='si')    then
                    if ( (""+QUsuario.get("modulos")).contains(",4,") || usuarioNo22.equals("si") )
                      dr.put("pnlExamExtraord3ro_Visible", true);  //mostrar a estos usuarios
                    else
                      dr.put("pnlExamExtraord3ro_Visible", false);
                }
                else*/
                dr.put("pnlExamExtraord3ro_Visible", true); //mostrar si es 22
                matrepact3ro = Integer.parseInt(""+QCalif3ro.get(0).get("matrepact"));
                if(matrepact3ro==0 || matrepact3ro>=5){
                    dr.put("pnlExamenes3ro_visible",false);
                    dr.put("tblCalif3ro_editable","");
                    dr.put("msgExm3ro",(matrepact3ro==0 ? "No tiene materias reprobadas":"Tiene más de 4 materias reprobadas"));
                } else {
                    dr.put("pnlExamenes3ro_visible",true);
                    dr.put("tblCalif3ro_editable","textbox");
                    dr.put("msgExm3ro","");
                }    
                    


               //3RO manda a 2do y a 1ro
            /*if (dm2.q_Calif3rocicescini.AsInteger<=2006) then
               {
               meses3.Add('AGOSTO');
               meses3.Add('SEPTIEMBRE');
               meses3.Add('FEBRERO')
               }
            if (dm2.q_Calif3rocicescini.AsInteger=2007) then
               {
               meses3.Add('AGOSTO');
               meses3.Add('SEPTIEMBRE');
               meses3.Add('ENERO')
               }
            if dm2.q_Calif3rocicescini.AsInteger>=2008 then
               {

               meses1.Clear;
               meses1.Add('JULIO');
               meses1.Add('AGOSTO');
               meses1.Add('SEPTIEMBRE');
               meses1.Add('ENERO');

               meses2.Clear;
               meses2.Add('JULIO');
               meses2.Add('AGOSTO');
               meses2.Add('SEPTIEMBRE');
               meses2.Add('ENERO');


               meses3.Add('JULIO');
               meses3.Add('AGOSTO');
               meses3.Add('SEPTIEMBRE');
               meses3.Add('ENERO')
               }
            */
            } else
                dr.put("pnlExamExtraord3ro_Visible", false);
            
            //------------------------------------------------------------------------------------------------
            
            
            //meses1.add("JULIO");
            meses1.add("AGOSTO");
            meses1.add("SEPTIEMBRE");       
            meses1.add("OCTUBRE");
            meses1.add("ENERO");

            //meses2.add("JULIO");
            meses2.add("AGOSTO");
            meses2.add("SEPTIEMBRE");
            meses2.add("OCTUBRE");
            meses2.add("ENERO");
            
            //meses3.add("JULIO");
            meses3.add("AGOSTO");
            meses3.add("SEPTIEMBRE");
            meses3.add("OCTUBRE");
            meses3.add("ENERO");
                        
            dr.put("cbxTExm1ro_Column1",meses1);
            dr.put("cbxTExm2do_Column1",meses2);
            dr.put("cbxTExm3ro_Column1",meses3);

            dr.put("cbxTExm1ro_Column3",ccts1);
            dr.put("cbxTExm2do_Column3",ccts2);
            dr.put("cbxTExm3ro_Column3",ccts3);
            
            dr.put("cbxTExm1ro_idccts",idccts1);
            dr.put("cbxTExm2do_idccts",idccts2);
            dr.put("cbxTExm3ro_idccts",idccts3);
            
            for(int i=0; i<mesesExmExt.size(); i++){
                if(!meses.contains(""+mesesExmExt.get(i).get("mes")))
                    meses.add(""+mesesExmExt.get(i).get("mes"));
            }
            
            dr.put("mesesExmExt",mesesExmExt);                                    
            dr.put("meses",meses);    
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",-1);  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        
    }
    
    private void tblCalif_ChangeSelectedItem (String tblAlumCapCalif_idalu, int grado, String cvetipmat, String cvemat, String msgExmExt)
    {
        try {
            qryIfx.conectar();
            if (grado == 1)
                dr.put("tblExmExt",qryIfx.exmExt1ro_onSelect (tblAlumCapCalif_idalu, cvetipmat, cvemat));
            else if (grado == 2)
                dr.put("tblExmExt",qryIfx.exmExt2do_onSelect (tblAlumCapCalif_idalu, cvetipmat, cvemat));
            else if (grado == 3)
                dr.put("tblExmExt",qryIfx.exmExt3ro_onSelect (tblAlumCapCalif_idalu, cvetipmat, cvemat));
            dr.put("msgExmExt",msgExmExt);
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void actualizaReprobadas (SICEEO_QueriesInformix conn, String tblAlumCapCalif_idalu, String tblCalif2do_cicescini, int tblCalif3ro_cicescini, 
            int tblCalif1ro_matrepact, int tblCalif2do_matrepact, int tblCalif1ro_size, int tblCalif2do_size, int tblCalif3ro_size) throws SQLException
    {
        dr.putAll(conn.actualizaReprobadas(tblAlumCapCalif_idalu, tblCalif2do_cicescini, tblCalif3ro_cicescini, tblCalif1ro_matrepact,
                    tblCalif2do_matrepact, tblCalif1ro_size, tblCalif2do_size, tblCalif3ro_size));
        dr.put("tblCalif2do",conn.calif2do (tblAlumCapCalif_idalu));
        dr.put("tblCalif3ro",conn.calif3ro (tblAlumCapCalif_idalu));
    }
    
    private boolean _verificarEx_Extraordinario(SICEEO_QueriesInformix conn,ArrayList<Map>tblCalifNGdo, int cicescin, String tblCalifNGdo_cicescini, int tblPrincipal_cveplan, 
            String tblCalifNGdo_idalu, String tblCalifNGdo_almextrj, String txtUsuario) throws SQLException{
        int tblCalifNGdo_numRows = tblCalifNGdo.size();
        int f=0;
        boolean reg_extra=false;
        
        while (f<tblCalifNGdo_numRows) {
            if ( dm.toFloat(tblCalifNGdo.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalifNGdo.get(f).get("promedio_oldValue"))<6.0
                    && dm.toFloat(tblCalifNGdo.get(f).get("promedio"))!=dm.toFloat(tblCalifNGdo.get(f).get("promedio_oldValue"))
               && !conn.verificaExExtra(tblCalifNGdo_idalu, ""+tblCalifNGdo.get(f).get("cvemat"), ""+tblCalifNGdo.get(f).get("cvetipmat"), ""+tblCalifNGdo.get(f).get("grado"), tblCalifNGdo_cicescini, ""+tblCalifNGdo.get(f).get("promedio")) )                                                                                       
                return true;  // verdadero, le falta registro en la base de extra
           f++;
        }
        return reg_extra;
    }        
    
    //PRIMER GRADO -----------------------------------------------------------------------------------------------------------------------------------------------
    private void btnGdaCalif1ro_Click(ArrayList<Map>tblCalif1ro, int cicescin, String tblCalif1ro_cicescini, int tblPrincipal_cveplan, String tblCalif1ro_idalu, 
            String tblCalif1ro_almextrj, int tblCalif1ro_matrepact, int tblCalif2do_matrepact, String tblCalif2do_cicescini, int tblCalif3ro_cicescini, 
            int tblCalif2do_size, int tblCalif3ro_size, String txtUsuario)
    {
        boolean hacerCommit=false, algun_extra = false;
        boolean errorCalcPromGral[]={false};
        ArrayList<Map> QCalif1ro;
        int matrepact1ro=0;
        String msgExmExt;
        try {
            qryIfx.conectarConTransaccion();
            QCalif1ro = qryIfx.calif1ro (tblCalif1ro_idalu);
            /*Inicio ********************************** Metodo para verificar si ya se capturo el extraordinario *********************/            
            dr.put("tblCalif1ro",QCalif1ro); 
            msgExmExt = qryIfx.verificarExmExt(tblCalif1ro,cicescin, tblCalif1ro_cicescini,tblPrincipal_cveplan, tblCalif1ro_idalu, "1", dm);
            if(!msgExmExt.isEmpty()){            
                throw new SICEEO_Excepcion (-11,"ALUM_SIN_REGISTRO_EXTRA", msgExmExt);            
            }
            
            qryIfx.guardaTablaCalifNGdo (tblCalif1ro, cicescin, tblCalif1ro_cicescini, tblPrincipal_cveplan, tblCalif1ro_idalu, tblCalif1ro_almextrj, 
                    txtUsuario, dm);
            dr.put("tblCalif1ro",QCalif1ro=qryIfx.calif1ro (tblCalif1ro_idalu));
            
            actualizaReprobadas(this.qryIfx, tblCalif1ro_idalu, tblCalif2do_cicescini, tblCalif3ro_cicescini, tblCalif1ro_matrepact,
                    tblCalif2do_matrepact, tblCalif1ro.size(), tblCalif2do_size, tblCalif3ro_size);                        
            
            matrepact1ro = Integer.parseInt(""+QCalif1ro.get(0).get("matrepact"));            
            if(matrepact1ro ==0 || matrepact1ro>=5){                    
                dr.put("tblCalif1ro_editable","");
                dr.put("pnlExamenes1ro_visible",false);
                dr.put("msgExm1ro",(matrepact1ro==0 ? "No tiene materias reprobadas":"Tiene más de 4 materias reprobadas"));
            } else {
                dr.put("pnlExamenes1ro_visible",true);
                dr.put("tblCalif1ro_editable","textbox");
                dr.put("msgExm1ro","");
            } 
            
            dr.put("tblCalif3ro",qryIfx.actualizaPromedioGeneral (tblCalif3ro_size, tblCalif3ro_cicescini, tblCalif1ro_idalu, txtUsuario, errorCalcPromGral, this.dm));
            
            if (errorCalcPromGral[0]) {
                this.dr.put("returnCase", 0); 
                mensaje.SecHist("SIN_CALC_PROM_GRAL", "", "", this.dr);
            }
            //hacerCommit = true; //Comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",-1);  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
        //if dm.Q_PromGralGrados.AsInteger = 3 then // ya tiene sus 3 grado
    }
   
    private void btnCalifDBim1ro_Click (ArrayList<Map>tblCalif1ro, String tblCalif1ro_cicescini, String tblCalif1ro_idalu, String txtUsuario)
    {
        boolean hacerCommit = false;
        int numFilas = tblCalif1ro.size();
        Map ciclo;
        try{
            qryIfx.conectarConTransaccion();
            ciclo = qryIfx.Ciclo();
            for (int i=0; i<numFilas; i++)                
                qryIfx.actualizaAlumnoMaterias(tblCalif1ro_cicescini, "2", "1", tblCalif1ro_idalu, ""+tblCalif1ro.get(i).get("cvemat"), 
                        ""+tblCalif1ro.get(i).get("cvetipmat"), ""+tblCalif1ro.get(i).get("exm"), dm.toInt(""+tblCalif1ro.get(i).get("eer")), 
                        ""+tblCalif1ro.get(i).get("promedio"), txtUsuario, ""+ciclo.get("cicescini"));
            dr.put("tblCalif1ro",qryIfx.calif1ro (tblCalif1ro_idalu));
            //hacerCommit = true; //Comentando el hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnGuardaExamExt1ro_Click(ArrayList<Map>tblExmExt1ro, ArrayList<Map>tblExmExt1ro_OldValues, String idalu_oldValue, String tblCalif1ro_cicescini,
            String grado_oldValue, String cvetipmat_oldValue, String cvemat_oldValue, String txtUsuario)
    {
        int numFilas = tblExmExt1ro.size(), idperexmext=0;
        boolean hacerCommit = false;
        try{
            qryIfx.conectarConTransaccion();
            dr.put("tblExmExt1ro",qryIfx.exmExt1ro_onSelect (idalu_oldValue, cvetipmat_oldValue, cvemat_oldValue) );

            if(!qryIfx.verificarMateriaReprobada(idalu_oldValue, tblCalif1ro_cicescini, grado_oldValue, cvemat_oldValue, cvetipmat_oldValue))
                    throw new SICEEO_Excepcion (0,"MAT_SIN_REPROB",cvetipmat_oldValue+"-"+cvemat_oldValue);
            
            for (int i=0; i<numFilas; i++){                
                if (tblExmExt1ro.get(i).get("idarray").equals("")) {
                    if(dm.toFloat(tblExmExt1ro.get(i).get("promedio")) < 6.0 )
                        throw new SICEEO_Excepcion (0,"EXM_EXT_SIN_APROB");
                    if((idperexmext=qryIfx.verificarFechaValidaExmExt(idalu_oldValue, tblCalif1ro_cicescini, ""+tblExmExt1ro.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt1ro.get(i).get("dia"), ""+tblExmExt1ro.get(i).get("mes"),""+tblExmExt1ro.get(i).get("anio")))==0)
                        throw new SICEEO_Excepcion (0,"FECHA_INVALIDA");
                
                    qryIfx.exmExt1ro_onInsert (idalu_oldValue, tblCalif1ro_cicescini, ""+tblExmExt1ro.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt1ro.get(i).get("dia"), ""+tblExmExt1ro.get(i).get("mes"),""+tblExmExt1ro.get(i).get("anio"), 
                        ""+tblExmExt1ro.get(i).get("promedio"), txtUsuario);
                } else {
                    for (int j=0; j<tblExmExt1ro_OldValues.size(); j++)         //Buscamos la posición del idarray en el arreglo tblExmExt1ro_OldValues
                        if (tblExmExt1ro.get(i).get("idarray").equals(tblExmExt1ro_OldValues.get(j).get("idarray"))){
                            /* No se debe editar el registro ya capturado */
                            /* qryIfx.exmExt1ro_onUpdate (""+tblExmExt1ro.get(i).get("idcct_apl"), ""+tblExmExt1ro.get(i).get("dia"), ""+tblExmExt1ro.get(i).get("mes"), 
                                    ""+tblExmExt1ro.get(i).get("anio"), ""+tblExmExt1ro.get(i).get("promedio"), txtUsuario, idalu_oldValue, 
                                    ""+tblExmExt1ro_OldValues.get(j).get("idcct_apl"), grado_oldValue, cvetipmat_oldValue, cvemat_oldValue, 
                                    ""+tblExmExt1ro_OldValues.get(j).get("dia"), ""+tblExmExt1ro_OldValues.get(j).get("mes"), ""+tblExmExt1ro_OldValues.get(j).get("anio"), 
                                    ""+tblExmExt1ro_OldValues.get(j).get("promedio")); */
                            tblExmExt1ro_OldValues.remove(j);                   //Si ya lo analizamos lo quitamos
                            break;
                        }
                }
            }
            if (tblExmExt1ro_OldValues != null)
                for (int i=0; i<tblExmExt1ro_OldValues.size(); i++) {
                    if(!qryIfx.exmExt_Ofic(idalu_oldValue, tblCalif1ro_cicescini, ""+tblExmExt1ro_OldValues.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt1ro_OldValues.get(i).get("dia"), ""+tblExmExt1ro_OldValues.get(i).get("mes"), 
                        ""+tblExmExt1ro_OldValues.get(i).get("anio"), ""+tblExmExt1ro_OldValues.get(i).get("promedio")))
                        qryIfx.exmExt1ro_onDelete (idalu_oldValue, tblCalif1ro_cicescini, ""+tblExmExt1ro_OldValues.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                            cvetipmat_oldValue, ""+tblExmExt1ro_OldValues.get(i).get("dia"), ""+tblExmExt1ro_OldValues.get(i).get("mes"), 
                            ""+tblExmExt1ro_OldValues.get(i).get("anio"), ""+tblExmExt1ro_OldValues.get(i).get("promedio"));
                    else
                        throw new SICEEO_Excepcion(0,"EXM_EXT_OFIC","["+cvetipmat_oldValue+"-"+cvemat_oldValue+"]");
                }
            dr.put("tblExmExt1ro",qryIfx.exmExt1ro_onSelect (idalu_oldValue, cvetipmat_oldValue, cvemat_oldValue) );
            //hacerCommit = true;  //Comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnOfExmExt1ro_Click(ArrayList<Map>tblCalif1ro, int cicescin, String tblCalif1ro_cicescini, int tblPrincipal_cveplan, String tblCalif1ro_idalu, 
            String tblCalif1ro_almextrj, int tblCalif1ro_matrepact, int tblCalif2do_matrepact, String tblCalif2do_cicescini, int tblCalif3ro_cicescini, 
            int tblCalif2do_size, int tblCalif3ro_size, String txtUsuario)
    {
        /*boolean hacerCommit=false, algun_extra = false;
        boolean errorCalcPromGral[]={false};
        ArrayList<Map> QCalif1ro, QExmExt;
        int matrepact1ro=0;
        String msgExmExt;
        try {
            qryIfx.conectarConTransaccion();
            QExmExt = qryIfx.getMatAprobadas(tblCalif1ro_cicescini, tblCalif1ro_idalu, cicescin);            
            hacerCommit = true; //Comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",-1);  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }        
        */
    }
    
    //SEGUNDO GRADO --------------------------------------------------------------------------------------------------------------------------------------------
    private void btnGdaCalif2do_Click(ArrayList<Map>tblCalif2do, int cicescin, String tblCalif2do_cicescini, int tblPrincipal_cveplan, String tblCalif2do_idalu, 
            String tblCalif2do_almextrj, int tblCalif2do_matrepact, int tblCalif1ro_matrepact, int tblCalif3ro_cicescini, int tblCalif1ro_size, int tblCalif3ro_size, 
            String txtUsuario)
    {
        boolean hacerCommit=false;
        boolean errorCalcPromGral[]={false};
        ArrayList<Map> QCalif2do;
        String msgExmExt;
        int matrepact2do=0;
        
        try {
            qryIfx.conectarConTransaccion();
            QCalif2do = qryIfx.calif2do (tblCalif2do_idalu);
            /*Inicio ********************************** Metodo para verificar si ya se capturo el extraordinario *********************/
            dr.put("tblCalif2do",QCalif2do);                
            msgExmExt = qryIfx.verificarExmExt(tblCalif2do,cicescin, tblCalif2do_cicescini,tblPrincipal_cveplan, tblCalif2do_idalu, "2", dm);
            if(!msgExmExt.isEmpty())
                throw new SICEEO_Excepcion (-11,"ALUM_SIN_REGISTRO_EXTRA", msgExmExt);                        
            /*Fin ******************************************************************************************************************/            
            qryIfx.guardaTablaCalifNGdo (tblCalif2do, cicescin, tblCalif2do_cicescini, tblPrincipal_cveplan, tblCalif2do_idalu, tblCalif2do_almextrj, 
                    txtUsuario, dm);
            dr.put("tblCalif2do",QCalif2do = qryIfx.calif2do (tblCalif2do_idalu));
            
            actualizaReprobadas(this.qryIfx, tblCalif2do_idalu, tblCalif2do_cicescini, tblCalif3ro_cicescini, tblCalif1ro_matrepact,
                    tblCalif2do_matrepact, tblCalif1ro_size, tblCalif2do.size(), tblCalif3ro_size);
            
            matrepact2do = Integer.parseInt(""+QCalif2do.get(0).get("matrepact"));            
            if(matrepact2do ==0 || matrepact2do>=5){                    
                dr.put("tblCalif2do_editable","");
                dr.put("pnlExamenes2do_visible",false);
                dr.put("msgExm2do",(matrepact2do==0 ? "No tiene materias reprobadas":"Tiene más de 4 materias reprobadas"));
            } else {
                dr.put("pnlExamenes2do_visible",true);
                dr.put("tblCalif2do_editable","textbox");
                dr.put("msgExm2do","");
            }        
            
            dr.put("tblCalif3ro",qryIfx.actualizaPromedioGeneral (tblCalif3ro_size, tblCalif3ro_cicescini, tblCalif2do_idalu, txtUsuario, errorCalcPromGral, this.dm));
            if (errorCalcPromGral[0]){
                this.dr.put("returnCase", 0); 
                mensaje.SecHist("SIN_CALC_PROM_GRAL", "", "", this.dr);
            }
            //hacerCommit = true; //Comentado hoy 22-07-2025
       } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){              
            this.dr.put("returnCase",ex.getNumError());  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
        //if dm.Q_PromGralGrados.AsInteger = 3 then // ya tiene sus 3 grado
    }
    
    private void btnCalifDBim2do_Click (ArrayList<Map>tblCalif2do, String tblCalif2do_cicescini, String tblCalif2do_idalu, String txtUsuario)
    {
        boolean hacerCommit = false;
        int numFilas = tblCalif2do.size();
        Map ciclo; 
        
        try{            
            qryIfx.conectarConTransaccion();
            ciclo = qryIfx.Ciclo();
            for (int i=0; i<numFilas; i++)
                qryIfx.actualizaAlumnoMaterias(tblCalif2do_cicescini, "2", "2", tblCalif2do_idalu, ""+tblCalif2do.get(i).get("cvemat"), 
                        ""+tblCalif2do.get(i).get("cvetipmat"), ""+tblCalif2do.get(i).get("exm"), dm.toInt(""+tblCalif2do.get(i).get("eer")), 
                        ""+tblCalif2do.get(i).get("promedio"), txtUsuario, ""+ciclo.get("cicescini"));
            dr.put("tblCalif2do",qryIfx.calif2do (tblCalif2do_idalu));
            //hacerCommit = true; //comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnGuardaExamExt2do_Click(ArrayList<Map>tblExmExt2do, ArrayList<Map>tblExmExt2do_OldValues, String idalu_oldValue, String tblCalif2do_cicescini,
            String grado_oldValue, String cvetipmat_oldValue, String cvemat_oldValue, String txtUsuario)
    {
        int numFilas = tblExmExt2do.size(), idperexmext=0;
        boolean hacerCommit = false;
        try {
            qryIfx.conectarConTransaccion();
            dr.put("tblExmExt2do",qryIfx.exmExt2do_onSelect (idalu_oldValue, cvetipmat_oldValue, cvemat_oldValue) );
            
            if(!qryIfx.verificarMateriaReprobada(idalu_oldValue, tblCalif2do_cicescini, grado_oldValue, cvemat_oldValue, cvetipmat_oldValue))
                    throw new SICEEO_Excepcion (0,"MAT_SIN_REPROB",cvetipmat_oldValue+"-"+cvemat_oldValue);
            
            for (int i=0; i<numFilas; i++){                                
                /*********  Solo guardamos el nuevo registro *************/
                if (tblExmExt2do.get(i).get("idarray").equals("") ) {
                    if(dm.toFloat(tblExmExt2do.get(i).get("promedio")) < 6.0 )
                        throw new SICEEO_Excepcion (0,"EXM_EXT_SIN_APROB");
                    if((idperexmext = qryIfx.verificarFechaValidaExmExt(idalu_oldValue, tblCalif2do_cicescini, ""+tblExmExt2do.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt2do.get(i).get("dia"), ""+tblExmExt2do.get(i).get("mes"),""+tblExmExt2do.get(i).get("anio")))==0)
                        throw new SICEEO_Excepcion (0,"FECHA_INVALIDA");
                    
                    qryIfx.exmExt2do_onInsert (idalu_oldValue, tblCalif2do_cicescini, ""+tblExmExt2do.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt2do.get(i).get("dia"), ""+tblExmExt2do.get(i).get("mes"),""+tblExmExt2do.get(i).get("anio"), 
                        ""+tblExmExt2do.get(i).get("promedio"), idperexmext, txtUsuario);
                } else {
                    for (int j=0; j<tblExmExt2do_OldValues.size(); j++)         //Buscamos la posición del idarray en el arreglo tblExmExt2do_OldValues
                        if (tblExmExt2do.get(i).get("idarray").equals(tblExmExt2do_OldValues.get(j).get("idarray"))){
                            /* No se debe editar el registro ya capturado */
                            /*qryIfx.exmExt2do_onUpdate (""+tblExmExt2do.get(i).get("idcct_apl"), ""+tblExmExt2do.get(i).get("dia"), ""+tblExmExt2do.get(i).get("mes"), 
                                    ""+tblExmExt2do.get(i).get("anio"), ""+tblExmExt2do.get(i).get("promedio"), txtUsuario, idalu_oldValue, 
                                    ""+tblExmExt2do_OldValues.get(j).get("idcct_apl"), grado_oldValue, cvetipmat_oldValue, cvemat_oldValue, 
                                    ""+tblExmExt2do_OldValues.get(j).get("dia"), ""+tblExmExt2do_OldValues.get(j).get("mes"), ""+tblExmExt2do_OldValues.get(j).get("anio"), 
                                    ""+tblExmExt2do_OldValues.get(j).get("promedio"));*/
                            tblExmExt2do_OldValues.remove(j);                   //Si ya lo analizamos lo quitamos
                            break;
                        }
                }
            }
            if (tblExmExt2do_OldValues != null)
                for (int i=0; i<tblExmExt2do_OldValues.size(); i++) {
                    if(!qryIfx.exmExt_Ofic (idalu_oldValue, tblCalif2do_cicescini, ""+tblExmExt2do_OldValues.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt2do_OldValues.get(i).get("dia"), ""+tblExmExt2do_OldValues.get(i).get("mes"), 
                        ""+tblExmExt2do_OldValues.get(i).get("anio"), ""+tblExmExt2do_OldValues.get(i).get("promedio")))
                        qryIfx.exmExt2do_onDelete (idalu_oldValue, tblCalif2do_cicescini, ""+tblExmExt2do_OldValues.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                            cvetipmat_oldValue, ""+tblExmExt2do_OldValues.get(i).get("dia"), ""+tblExmExt2do_OldValues.get(i).get("mes"), 
                            ""+tblExmExt2do_OldValues.get(i).get("anio"), ""+tblExmExt2do_OldValues.get(i).get("promedio"));
                    else 
                        throw new SICEEO_Excepcion(0,"EXM_EXT_OFIC","["+cvetipmat_oldValue+"-"+cvemat_oldValue+"]");
                }
            dr.put("tblExmExt2do",qryIfx.exmExt2do_onSelect (idalu_oldValue, cvetipmat_oldValue, cvemat_oldValue) );
            //hacerCommit = true; // comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    
    //TERCER GRADO ---------------------------------------------------------
    private void btnGdaCalif3ro_Click(ArrayList<Map>tblCalif3ro, int cicescin, int tblCalif3ro_cicescini, int tblPrincipal_cveplan, String tblCalif3ro_idalu, 
            String tblCalif3ro_almextrj, int tblCalif1ro_matrepact, int tblCalif2do_matrepact, int tblCalif2do_cicescini, 
            int tblCalif1ro_size, int tblCalif2do_size, String txtUsuario)
    {
        boolean hacerCommit=false;
        boolean errorCalcPromGral[]={false};
        ArrayList<Map> QCalif3ro;
        int matrepact3ro=0;
        String msgExmExt="";
        try{
            qryIfx.conectarConTransaccion();
            QCalif3ro = qryIfx.calif3ro (tblCalif3ro_idalu);
            dr.put("tblCalif3ro",QCalif3ro);                
            /*Inicio ********************************** Metodo para verificar si ya se capturo el extraordinario *********************/
            msgExmExt = qryIfx.verificarExmExt(tblCalif3ro,cicescin, ""+tblCalif3ro_cicescini,tblPrincipal_cveplan, tblCalif3ro_idalu, "1", dm);
            if(!msgExmExt.isEmpty())               
                throw new SICEEO_Excepcion (-11,"ALUM_SIN_REGISTRO_EXTRA", msgExmExt);                        
            
            qryIfx.guardaTablaCalifNGdo (tblCalif3ro, cicescin, ""+tblCalif3ro_cicescini, tblPrincipal_cveplan, tblCalif3ro_idalu, tblCalif3ro_almextrj, 
                    txtUsuario, dm);
            dr.put("tblCalif3ro",QCalif3ro = qryIfx.calif3ro (tblCalif3ro_idalu));
            
            actualizaReprobadas(this.qryIfx, tblCalif3ro_idalu, ""+tblCalif2do_cicescini, tblCalif3ro_cicescini, tblCalif1ro_matrepact,
                    tblCalif2do_matrepact, tblCalif1ro_size, tblCalif2do_size, tblCalif3ro.size());
            
            matrepact3ro = Integer.parseInt(""+QCalif3ro.get(0).get("matrepact"));            
            if(matrepact3ro ==0 || matrepact3ro>=5){                    
                dr.put("tblCalif3ro_editable","");
                dr.put("pnlExamenes3ro_visible",false);
                dr.put("msgExm3ro",(matrepact3ro==0 ? "No tiene materias reprobadas":"Tiene más de 4 materias reprobadas"));
            } else {
                dr.put("pnlExamenes3ro_visible",true);
                dr.put("tblCalif3ro_editable","textbox");
                dr.put("msgExm3ro","");
            }        
            
            dr.put("tblCalif3ro",qryIfx.actualizaPromedioGeneral (tblCalif3ro.size(), tblCalif3ro_cicescini, tblCalif3ro_idalu, txtUsuario, errorCalcPromGral, this.dm));
            if (errorCalcPromGral[0]){
                this.dr.put("returnCase", 0); 
                mensaje.SecHist("SIN_CALC_PROM_GRAL", "", "", this.dr);
            }
            //hacerCommit = true; // comentado hoy 22-07-2025
       } catch (SQLException ex) { 
           this.dr.put("returnCase", -1);
           if(ex.getMessage().contains("alumnogrado_ckprom"))           
                mensaje.General("PROM_INVALIDO", "No cuenta con las calificaciones suficientes para realizar el promedio de su grado.", "", this.dr);             
           else
                mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  
       }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
        //if dm.Q_PromGralGrados.AsInteger = 3 then // ya tiene sus 3 grado
    }
    
    private void btnCalifDBim3ro_Click (ArrayList<Map>tblCalif3ro, String tblCalif3ro_cicescini, String tblCalif3ro_idalu, String txtUsuario)
    {
        boolean hacerCommit = false;
        int numFilas = tblCalif3ro.size();
        Map ciclo;
        try{
            
            qryIfx.conectarConTransaccion();
            ciclo = qryIfx.Ciclo();
            for (int i=0; i<numFilas; i++)
                qryIfx.actualizaAlumnoMaterias(tblCalif3ro_cicescini, "2", "3", tblCalif3ro_idalu, ""+tblCalif3ro.get(i).get("cvemat"), 
                        ""+tblCalif3ro.get(i).get("cvetipmat"), ""+tblCalif3ro.get(i).get("exm"), dm.toInt(""+tblCalif3ro.get(i).get("eer")), 
                        ""+tblCalif3ro.get(i).get("promedio"), txtUsuario, ""+ciclo.get("cicescini"));
            dr.put("tblCalif3ro",qryIfx.calif3ro (tblCalif3ro_idalu));
            //hacerCommit = true; // Comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnGuardaExamExt3ro_Click(ArrayList<Map>tblExmExt3ro, ArrayList<Map>tblExmExt3ro_OldValues, String idalu_oldValue, String tblCalif3ro_cicescini,
            String grado_oldValue, String cvetipmat_oldValue, String cvemat_oldValue, String txtUsuario)
    {
        int numFilas = tblExmExt3ro.size(), idperexmext=0;
        boolean hacerCommit = false;
        
        try{
            qryIfx.conectarConTransaccion();
            dr.put("tblExmExt3ro",qryIfx.exmExt3ro_onSelect (idalu_oldValue, cvetipmat_oldValue, cvemat_oldValue) );
            
            if(!qryIfx.verificarMateriaReprobada(idalu_oldValue, tblCalif3ro_cicescini, grado_oldValue, cvemat_oldValue, cvetipmat_oldValue))
                    throw new SICEEO_Excepcion (0,"MAT_SIN_REPROB", cvetipmat_oldValue+"-"+cvemat_oldValue);
            
            for (int i=0; i<numFilas; i++){                
                /*********  Solo guardamos el nuevo registro *************/
                if (tblExmExt3ro.get(i).get("idarray").equals("")) {
                    if(dm.toFloat(tblExmExt3ro.get(i).get("promedio")) < 6.0 )
                        throw new SICEEO_Excepcion (0,"EXM_EXT_SIN_APROB");
                    if((idperexmext = qryIfx.verificarFechaValidaExmExt(idalu_oldValue, tblCalif3ro_cicescini, ""+tblExmExt3ro.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt3ro.get(i).get("dia"), ""+tblExmExt3ro.get(i).get("mes"),""+tblExmExt3ro.get(i).get("anio")))==0)
                        throw new SICEEO_Excepcion (0,"FECHA_INVALIDA");
                    qryIfx.exmExt3ro_onInsert (idalu_oldValue, tblCalif3ro_cicescini, ""+tblExmExt3ro.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt3ro.get(i).get("dia"), ""+tblExmExt3ro.get(i).get("mes"),""+tblExmExt3ro.get(i).get("anio"), 
                        ""+tblExmExt3ro.get(i).get("promedio"), txtUsuario);
                } else{
                    for (int j=0; j<tblExmExt3ro_OldValues.size(); j++)         //Buscamos la posición del idarray en el arreglo tblExmExt3ro_OldValues
                        if (tblExmExt3ro.get(i).get("idarray").equals(tblExmExt3ro_OldValues.get(j).get("idarray"))){
                            /* No se debe editar el registro ya capturado */
                            /* qryIfx.exmExt3ro_onUpdate (""+tblExmExt3ro.get(i).get("idcct_apl"), ""+tblExmExt3ro.get(i).get("dia"), ""+tblExmExt3ro.get(i).get("mes"), 
                                    ""+tblExmExt3ro.get(i).get("anio"), ""+tblExmExt3ro.get(i).get("promedio"), txtUsuario, idalu_oldValue, 
                                    ""+tblExmExt3ro_OldValues.get(j).get("idcct_apl"), grado_oldValue, cvetipmat_oldValue, cvemat_oldValue, 
                                    ""+tblExmExt3ro_OldValues.get(j).get("dia"), ""+tblExmExt3ro_OldValues.get(j).get("mes"), ""+tblExmExt3ro_OldValues.get(j).get("anio"), 
                                    ""+tblExmExt3ro_OldValues.get(j).get("promedio")); */
                            tblExmExt3ro_OldValues.remove(j);                   //Si ya lo analizamos lo quitamos
                            break;
                        }
                }
            }
            if (tblExmExt3ro_OldValues != null)
                for (int i=0; i<tblExmExt3ro_OldValues.size(); i++) {
                    if(!qryIfx.exmExt_Ofic(idalu_oldValue, tblCalif3ro_cicescini, ""+tblExmExt3ro_OldValues.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                        cvetipmat_oldValue, ""+tblExmExt3ro_OldValues.get(i).get("dia"), ""+tblExmExt3ro_OldValues.get(i).get("mes"), 
                        ""+tblExmExt3ro_OldValues.get(i).get("anio"), ""+tblExmExt3ro_OldValues.get(i).get("promedio")))
                        qryIfx.exmExt3ro_onDelete (idalu_oldValue, tblCalif3ro_cicescini, ""+tblExmExt3ro_OldValues.get(i).get("idcct_apl"), grado_oldValue, cvemat_oldValue, 
                            cvetipmat_oldValue, ""+tblExmExt3ro_OldValues.get(i).get("dia"), ""+tblExmExt3ro_OldValues.get(i).get("mes"), 
                            ""+tblExmExt3ro_OldValues.get(i).get("anio"), ""+tblExmExt3ro_OldValues.get(i).get("promedio"));
                    else
                        throw new SICEEO_Excepcion(0,"EXM_EXT_OFIC","["+cvetipmat_oldValue+"-"+cvemat_oldValue+"]");
                }
            dr.put("tblExmExt3ro",qryIfx.exmExt3ro_onSelect (idalu_oldValue, cvetipmat_oldValue, cvemat_oldValue) );
            //hacerCommit = true; //Comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr); }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnActualizaReprobadas_Click (String tblAlumCapCalif_idalu, String tblCalif2do_cicescini, int tblCalif3ro_cicescini, 
            int tblCalif1ro_matrepact, int tblCalif2do_matrepact, int tblCalif1ro_size, int tblCalif2do_size, int tblCalif3ro_size)
    {
        boolean hacerCommit = false;
        try{
            qryIfx.conectarConTransaccion();
            actualizaReprobadas (qryIfx, tblAlumCapCalif_idalu, tblCalif2do_cicescini, tblCalif3ro_cicescini, 
                                tblCalif1ro_matrepact, tblCalif2do_matrepact, tblCalif1ro_size, tblCalif2do_size, tblCalif3ro_size);
            //hacerCommit = true; //Comentado hoy 22-07-2025
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void ModalActive (String tblAlumCapCalif_idalu, String grado, String tblCalif_cicescini ) {
        ArrayList<Map> QExmExtXMat;
        ArrayList<String> meses;                                
        try
        {
            qryIfx.conectar();
            QExmExtXMat = qryIfx.getExamenesExt(tblCalif_cicescini, tblAlumCapCalif_idalu, grado);
            if(QExmExtXMat.isEmpty())
                throw new SICEEO_Excepcion(0,"SIN_MAT_X_OFIC","");
            dr.put("tblExmExtXMat", QExmExtXMat);
    
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",-1);  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        
    }
    
    private void btnOficializaExmExt (ArrayList<Map>tblExmExtXMat, String matOfic) {        
        String[] tblOfExmExt,tblregistro;
        String strQry="";
        int i;
        try
        {
            qryIfx.conectar();                        
            if(!matOfic.isEmpty()){
                tblOfExmExt = matOfic.split(",");
                for(i=0; i<tblOfExmExt.length; i++) {
                    tblregistro = (tblOfExmExt[i]).split("~"); //idalu,grado,cvemat,cvetipmat,cicescini,idperexmext
                    qryIfx.oficExmExtXMat(tblregistro[0],tblregistro[1],tblregistro[2],tblregistro[3],tblregistro[4],tblregistro[5]); 
                }                
            }
            else 
                throw new SICEEO_Excepcion(-1,"SIN_MAT_SEL");                        
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",-1);  mensaje.SecHist(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }        
    }    
}
