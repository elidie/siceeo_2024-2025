/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.RepEvaluacion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/**
 *
 * @author dai
 */
public class SICEEO_RepEvaluacion 
{
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_RepEvaluacion (Map datosReturn)
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
        
        if ( metodo.equals("foAc") ){
            formActivate( true, r.gP("Califcicescin"),r.gP("cicescini"), r.gP("idcct"), r.gP("modalidad"), r.gP("cveplan"),r.gP("grado"),r.gP("grupo"), 
                    r.gP("formato"));
        }else if ( metodo.equals("grSiAn") ){//muestra el grupo siguiente o anterior del actual
            String grupoSigAnt = r.gP("grupoSigAnt");
            if(grupoSigAnt.equals("siguiente"))
                siguienteGrupo(r.gP("Califcicescin"), r.gP("cicescin"), r.gP( "idcct"), r.gP("modalidad"), r.gP( "grado"), r.gP( "grupo"), r.gP("cveplan"), r.gP("formato") ) ;                            
            else
                grupoAnterior(r.gP("Califcicescin"), r.gP( "cicescin"), r.gP( "idcct"), r.gP("modalidad"), r.gP( "grado"), r.gP( "grupo"), r.gP("cveplan"), r.gP("formato") ) ;                            
        }else if ( metodo.equals("btAnGp_Cl") ){
            btnAnteriorGpo_Click (dm.toInt(r.gP("posSelActual")), dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"grado","grupo"}), 
                r.gP("Califcicescin"), r.gP( "cicescin"), r.gP( "idcct"), r.gP("modalidad"), r.gP("cveplan"), r.gP("formato") );
        }else if ( metodo.equals("btSiGp_Cl") ){
            btnSiguienteGpo_Click (dm.toInt(r.gP("posSelActual")), dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"grado","grupo"}), 
                r.gP("Califcicescin"), r.gP( "cicescin"), r.gP( "idcct"), r.gP("modalidad"), r.gP("cveplan"), r.gP("formato") );
        }else if ( metodo.equals("btGeFo_Cl") ){
            btnGenFolio_Click (r.gP("txtFolioNum"), r.gP("txtFolioLet"), dm.toInt(r.gP("posSelActual")), 
                dm.vstrToArrMap(r.gPV("tblRepEvaluacion"), "~", new String[]{"idalu","foliolet","folionum","nombre","apepat","apemat","curp"}), 
                r.gP( "tblRepEvaluacion_cvezona"), r.gP( "tblRepEvaluacion_idcct"), r.gP("tblRepEvaluacion_cct"), r.gP("tblPrincipal_modalidad"), 
                r.gP( "tblRepEvaluacion_cveturno"), r.gP( "tblRepEvaluacion_cveplan"), r.gP( "tblRepEvaluacion_grado"), 
                r.gP("tblRepEvaluacion_grupo"), r.gP("tblRepEvaluacion_cicescini"), r.gP("txtUsuario"), r.gP("tblRepEvaluacion_cveunidad") );
        }else if ( metodo.equals("btQuFo_Cl") )
            btnQuitaFolios_Click(dm.toInt(r.gP("posSelActual")), dm.vstrToArrMap(r.gPV("tblRepEvaluacion"), "~", new String[]{"idalu","foliolet","folionum","cicescini"}), 
                    r.gP("tblRepEvaluacion_cicescini"), r.gP("tblRepEvaluacion_cveplan"), r.gP("tblRepEvaluacion_idcct"), 
                    r.gP("tblRepEvaluacion_grado"), r.gP("tblRepEvaluacion_grupo") );
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    
    public void formActivate (boolean llamadaDesdeVista, String Califcicescin, String cicescini, String idcct, String modalidad, String cveplan, String grado, 
            String grupo, String formato)
    {
        try{
            if (llamadaDesdeVista)
                qryIfx.conectar();
            //******** PARA ANTES DE LLAMAR A ESTE FORMULARIO *********
            //** if (!idcct.equals(""))
            //**     Califcicescin = cicescini;                                // la variable se ocupa en Grupos
            //** else
            //**     throw new SiCEEB_Excepcion (0,"SEL_ESCUELA");
            //*********************************************************            
            if (llamadaDesdeVista)
                dr.put("tblRepEvaluacion",qryIfx.boleta(cicescini, cveplan, idcct, grado, grupo, "", ""));
            dr.put("configImpre","Config Impre 1"/*qryIfx.getConfigsImpreAsignadasAlFormato(cveplan, grado, idcct, modalidad, formato)*/);
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { if (llamadaDesdeVista) qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    private void cambiarDeCiclo (String Califcicescin, String cicescin, String idcct, String modalidad, String grado, String grupo, String cveplan,String formato)
    {
        try {
            qryIfx.conectar();
            dr.put("lblCiclo", Califcicescin+'-'+(Integer.parseInt(Califcicescin)+1));
            dr.put("tblRepEvaluacion",qryIfx.boleta(Califcicescin, cveplan, idcct, grado, grupo, "", ""));
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    public void siguienteGrupo (String Califcicescin, String cicescin, String idcct, String modalidad, String grado, String grupo,String cveplan,String formato) 
    {                       
        cambiarDeCiclo(Califcicescin, cicescin, idcct, modalidad, grado, grupo,cveplan,formato);
        dr.put("grado",grado);
        dr.put("grupo",grupo);        
    }
 
    public void grupoAnterior (String Califcicescin, String cicescin, String idcct, String modalidad, String grado, String grupo,String cveplan,String formato) 
    {                
        cambiarDeCiclo(Califcicescin, cicescin, idcct, modalidad, grado, grupo,cveplan,formato);
        dr.put("grado",grado);
        dr.put("grupo",grupo);        
    }
    
    public void btnAnteriorGpo_Click (int posSelActual, ArrayList<Map>tblPrincipal, String califCicEscIn, String cicescini, String tblPrincipal_idcct, 
            String tblPrincipal_modalidad, String tblPrincipal_cveplan,  String formato) 
    { 
        ArrayList<Map> QGrupoRepEvaluacion=new ArrayList<Map>();
        dr.put("tblPrincipal_selectedRow", posSelActual);

        try
        {
            qryIfx.conectar();
            posSelActual--;
            while ( posSelActual>=0 && QGrupoRepEvaluacion.isEmpty() )
            {
                QGrupoRepEvaluacion = qryIfx.boleta(cicescini, tblPrincipal_cveplan, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), "", "");
                if (QGrupoRepEvaluacion.size()>0)
                    formActivate (false, califCicEscIn, cicescini, tblPrincipal_idcct, tblPrincipal_modalidad, tblPrincipal_cveplan, ""+tblPrincipal.get(posSelActual).get("grado"),
                            ""+tblPrincipal.get(posSelActual).get("grupo"), formato);
                
                if (!dr.get("returnCase").equals(1))                                                                                              //Si hubo un error en el formActive
                    break;                                                                                                                                   //Nos salimos

                dr.put("tblRepEvaluacion",QGrupoRepEvaluacion);

                dr.put("tblPrincipal_selectedRow", posSelActual);
                dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
                posSelActual--;
            }
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void btnSiguienteGpo_Click (int posSelActual, ArrayList<Map>tblPrincipal, String califCicEscIn, String cicescini, 
            String tblPrincipal_idcct, String tblPrincipal_modalidad, String tblPrincipal_cveplan,  String formato) 
    { 
        int numFilas;
        ArrayList<Map> QGrupoRepEvaluacion=new ArrayList<Map>();
        
        numFilas = tblPrincipal.size();
        try
        {
            qryIfx.conectar();
            posSelActual++;
            while ( posSelActual<numFilas && QGrupoRepEvaluacion.isEmpty()  )
            {
                QGrupoRepEvaluacion = qryIfx.boleta(cicescini, tblPrincipal_cveplan, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"),
                            ""+tblPrincipal.get(posSelActual).get("grupo"), "", "");
                if (QGrupoRepEvaluacion.size()>0)
                    formActivate (false, califCicEscIn, cicescini, tblPrincipal_idcct, tblPrincipal_modalidad, tblPrincipal_cveplan, ""+tblPrincipal.get(posSelActual).get("grado"),
                            ""+tblPrincipal.get(posSelActual).get("grupo"), formato);

                if (!dr.get("returnCase").equals(1))                                                                                              //Si hubo un error en el formActive
                    break;                                                                                                                                   //Nos salimos

                dr.put("tblRepEvaluacion",QGrupoRepEvaluacion);

                dr.put("tblPrincipal_selectedRow", posSelActual);
                dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
                posSelActual++;
            }

            if ( posSelActual >= numFilas && QGrupoRepEvaluacion.isEmpty() ) {
                btnAnteriorGpo_Click(posSelActual, tblPrincipal, califCicEscIn, cicescini, tblPrincipal_idcct, tblPrincipal_modalidad, tblPrincipal_cveplan, formato );
            }

        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnGenFolio_Click(String txtFolioNum, String txtFolioLet, int posSelActual, ArrayList<Map>tblRepEvaluacion, String tblRepEvaluacion_cvezona, 
            String tblRepEvaluacion_idcct, String tblRepEvaluacion_cct, String tblPrincipal_modalidad, String tblRepEvaluacion_cveturno, 
            String tblRepEvaluacion_cveplan, String tblRepEvaluacion_grado, String tblRepEvaluacion_grupo, String tblRepEvaluacion_cicescini, String txtUsuario, 
            String tblRepEvaluacion_cveunidad )
    {
        String nivel="";
        int folio;
        
        //dm2.Q_boleta.First;
        if ( tblRepEvaluacion_cveplan.equals("3") ) nivel = "K";
        else if ( tblRepEvaluacion_cveplan.equals("1") ) nivel = "P";
        else if ( tblRepEvaluacion_cveplan.equals("2") ) nivel = "S";

        try{
            if ( txtFolioNum.trim().length() == 0 )
                throw new SICEEO_Excepcion(0, "SIN_FOLIO_INI");

            if ( !txtFolioLet.toUpperCase().matches("[A-ZÑ]{1}") )
                throw new SICEEO_Excepcion(0, "SIN_FOLIO_LET");

            //if copy(combo_libro.Items[combo_libro.ItemIndex],1,4)='' then BEGIN
            //   MessageDlg('Indica a que LIBRO pertenecen los Folios', mtWarning, [mbOK], 0);
            //   eXIT;
            //end;

            //dm.v_Lcicescini := copy(combo_libro.Items[combo_libro.ItemIndex],1,4); //el folio debe tomarse del libro del ciclo seleccionado en el combo_libro

            //if strtoint(copy(combo_libro.Items[combo_libro.ItemIndex],1,4))< strtoint(copy(combo_ciclo.Items[combo_ciclo.ItemIndex],1,4)) then
            //begin
            //  MessageDlg('No se pueden asignar folios de un Libro Anterior al Ciclo '+#13+#10+'Escolar', mtWarning, [mbOK], 0);
            //  Exit;
            //end;

            dr.put("txtFolioNum_text", (folio = dm.toInt(txtFolioNum.trim())) );
            
            qryIfx.conectar();
            qryIfx.generaFolioReporteEvaluacion (nivel, txtFolioLet.toUpperCase(), folio, posSelActual, tblRepEvaluacion, tblRepEvaluacion_cvezona, 
                    tblRepEvaluacion_idcct, tblRepEvaluacion_cct, tblPrincipal_modalidad, tblRepEvaluacion_cveturno, tblRepEvaluacion_cveplan, 
                    tblRepEvaluacion_grado, tblRepEvaluacion_grupo, tblRepEvaluacion_cicescini, txtUsuario, tblRepEvaluacion_cveunidad, dr);
            
            dr.put("tblRepEvaluacion",qryIfx.boleta(tblRepEvaluacion_cicescini, tblRepEvaluacion_cveplan, 
                    tblRepEvaluacion_idcct, tblRepEvaluacion_grado, tblRepEvaluacion_grupo, "", ""));
            
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.RepEvaluacion(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }

        
    }

    private void btnQuitaFolios_Click(int posSelActual, ArrayList<Map>tblRepEvaluacion, String tblRepEvaluacion_cicescini, 
            String tblRepEvaluacion_cveplan, String tblRepEvaluacion_idcct, String tblRepEvaluacion_grado, String tblRepEvaluacion_grupo )
    {
        boolean hacerCommit = false;
        //--Vista-->ACTUAL :TBookmark;
        //--Vista-->if f_aviso.ShowModal = mrCancel then Exit;
        //dm2.Q_boleta.First;

        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.eliminaFolioReporteEvaluacion (posSelActual, tblRepEvaluacion);
            dr.put("tblRepEvaluacion",qryIfx.boleta(tblRepEvaluacion_cicescini, tblRepEvaluacion_cveplan, tblRepEvaluacion_idcct, tblRepEvaluacion_grado, 
                    tblRepEvaluacion_grupo, "", ""));
            //--Vista--> dm2.Q_boleta.GotoBookmark(pointer(Actual));
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
        
    }
}
