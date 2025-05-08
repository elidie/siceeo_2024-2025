
package modelo.CambioDeTaller;

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

/* 
    Creado el : 20/01/2016, 09:18:56 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_CamDTaller_Clubes {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final HttpSession sesion;
    private SICEEO_HttpServletRequest r;
    private final HttpServletRequest request;
    
    public SICEEO_CamDTaller_Clubes (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.sesion = request.getSession(false);
        this.request = request;
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
        String txtUsuario = ""+sesion.getAttribute("userName");
        
        if (metodo.equals("foAc"))
            formActivate ( r.gP("tipoUsuario"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"),
                    r.gP("tblPrincipal_grupo"), ""+sesion.getAttribute("seccion"), txtUsuario, ""+sesion.getAttribute("usuarioNo22"));
        else if (metodo.equals("btInNvoClub_AcPe"))
            btnInsertTall_ActionPerformed ( r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("txtUsuario") );
        else if (metodo.equals("btGuCaDTa_AcPe"))
            btnGuardarCamDTaller_ActionPerformed ( dm.vstrToArrMap(r.gPV("tblCamDTaller"), "~", new String[]{"idalu","cvemat_oldvalue","cvemat_newvalue"}), r.gP("tblPrincipal_cicescini") );
        else if(metodo.equals("btInCl_Ac"))
            btnAgregarClubAlumno_ActionPerformed( r.gP("tblCamDTaller_idalu"),r.gP("tblPrincipal_cicescin"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"),
             r.gP("tblPrincipal_grupo"));
        else if(metodo.equals("btnInClOax"))
            btnInsertClubOaxaca_ActionPerformed(r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("txtClubOax"), r.gP("txtcvetipmat"),r.gP("txtUsuario"));           
        else if(metodo.equals("btnAddClCt")) 
            btnAddClubCt_ActionPerformed(dm.vstrToArrMap(r.gPV("tblClubOax"), "~", new String[]{"cvetipmat","cvemat"}),r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));        
        else if(metodo.equals("selAmbOax"))        
            selAmbitoClubOax_ActionPerformed(r.gP("cvetipmat"));        
        else if(metodo.equals("btGuClAl"))        
            btnGuardarClubAlumno_ActionPerformed(r.gP("tblCamDTaller_idalu"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"),r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"), r.gP("cveclub"),r.gP("tipoUsuario"));
        else if (metodo.equals("btAnGp")){            
            btnAnteriorGpo_Click (""+sesion.getAttribute("seccion"), dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"grado","grupo"}),  
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cct"), r.gP("tblPrincipal_cveplan"), dm.toInt(r.gP("posSelActual")), r.gP("califCicEscIn"),
                    r.gP("cicescini"), ""+sesion.getAttribute("tipo_usuario"), ""+sesion.getAttribute("usuarioNo22"), r.gP("xprimeraves"), txtUsuario);            
        }
        else if (metodo.equals("btSiGp")){
            btnSiguienteGpo_Click (""+sesion.getAttribute("seccion"), dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"grado","grupo"}),  
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cct"), r.gP("tblPrincipal_cveplan"),  dm.toInt(r.gP("posSelActual")), r.gP("califCicEscIn"), 
                    r.gP("cicescini"), ""+sesion.getAttribute("tipo_usuario"), txtUsuario, ""+sesion.getAttribute("usuarioNo22"), r.gP("xprimeraves"));
        } 
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void formActivate (String tipoUsuario, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String seccion,String usuario,String usuarioNo22)
    {
        boolean filtro;
        ArrayList<Map> tblAlumnos;
        try
        {
            qryIfx.conectar();
            tblAlumnos = qryIfx.camDTaller (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo);  
            
            if (usuario.equals("IVALLE")  || usuario.equals("ELYLOPEZ")  || usuario.equals("MARIOCIMM") || usuarioNo22.equals("si"))
                    filtro = false; //lo apagamos para mostrar todos
                else
                    filtro = true; //solo mostramos grupos de la 22
            
            dr.put("tblCamDTaller",tblAlumnos);
            dr.put("catEdt", qryIfx.tallValX(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, new String[]{"cvemat","desmat"}));
            dr.put("catAmbito", qryIfx.tblAmbito(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, new String[]{"cvetipmat","destipmat"}));
            dr.put("tblClubOax", qryIfx.tblClubOaxaca("AC1"));
            dr.put("tblClubCct", qryIfx.tblClubEscuela(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, null));
            dr.put("gposL",qryIfx.getGpos_Val (tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_cicescini, filtro, seccion));
            if ( tipoUsuario.equals("consulta") || tipoUsuario.equals("mesa") )
            {
                dr.put("btnInsertTall_Enabled", false);
                dr.put("btnGuardar_Enabled", false);
            }else{
                dr.put("btnInsertTall_Enabled", true);
                dr.put("btnGuardar_Enabled", true);
            }
                
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void btnAgregarClubAlumno_ActionPerformed (String tblCamDTaller_idalu, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
            String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        try
        {
            qryIfx.conectar();            
            dr.put("tblClubAlumno",qryIfx.tblClubAlumno (tblCamDTaller_idalu, tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_cveplan));              
            dr.put("tblClubCct", qryIfx.tblClubEscuela(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo,new String[]{"cvetipmat","cvemat","desmat"}));
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void btnInsertClubOaxaca_ActionPerformed (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_modalidad, 
            String tblPrincipal_grado, String tblPrincipal_grupo, String txtClub, String cvetipmat,String txtUsuario)
    {
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            if ( txtClub.trim().length()==0 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","Elija un club");
            qryIfx.insertNvoClubOax(txtClub,cvetipmat,tblPrincipal_idcct,tblPrincipal_cveplan, tblPrincipal_modalidad, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cicescini);                             
            dr.put("tblClubOax", qryIfx.tblClubOaxaca("AC1"));
            dr.put("tblClubCct", qryIfx.tblClubEscuela(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo,null));                                                                            
            hacerCommit = true;                            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex) { this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMensaje2(), ex.getMensaje(), this.dr); }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    private void selAmbitoClubOax_ActionPerformed (String cvetipmat)
    {        
        try
        {
            qryIfx.conectar();                        
            dr.put("tblClubOax", qryIfx.tblClubOaxaca(cvetipmat));                                                                                                                        
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void btnAddClubCt_ActionPerformed (ArrayList<Map> tblClubOax, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_modalidad, 
            String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        boolean hacerCommit=false;
        int numFilas = tblClubOax.size();
        String cvetipmat,cvemat;
        try
        {
            qryIfx.conectarConTransaccion();
            if ( numFilas ==0 )
                    throw new SICEEO_Excepcion (0,"SIN_DATO","ELEGIR UN REGLON");
            for (int i=0; i<numFilas; i++) {
                cvemat=""+tblClubOax.get(i).get("cvemat");
                cvetipmat=""+tblClubOax.get(i).get("cvetipmat");                    
                if(qryIfx.clubEscuela(cvemat,cvetipmat,tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_modalidad, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cicescini))
                    throw new SICEEO_Excepcion (0,"DATO_DUP","Ya existe este club en la escuela");                            
                qryIfx.insertNvoClubCct(cvemat,cvetipmat,tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_modalidad, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cicescini);
                dr.put("tblClubOax", qryIfx.tblClubOaxaca("AC1"));
                dr.put("tblClubCct", qryIfx.tblClubEscuela(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo,null));
            }                       
            hacerCommit = true;                            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex) { this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMensaje2(),ex.getMensaje() , this.dr); }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    public void btnInsertTall_ActionPerformed (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_modalidad, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String txtUsuario)
    {
    /*    boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.insertarTalleres (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_modalidad, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } } */
    }
    private void btnGuardarClubAlumno_ActionPerformed (String tblClub_idalu, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
            String tblPrincipal_modalidad,String tblPrincipal_grado, String tblPrincipal_grupo, String cveClub, String txtUsuario)
    {
        String club[] = cveClub.split(",");
        String cvetipmat = club[0];
        String cvemat = club[1];
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();   
            qryIfx.insertarClubAlumno(tblClub_idalu, cvetipmat.trim(), cvemat.trim(),tblPrincipal_modalidad,tblPrincipal_cicescini,tblPrincipal_idcct,tblPrincipal_cveplan,tblPrincipal_grado,txtUsuario);
            dr.put("tblClubAlumno",qryIfx.tblClubAlumno (tblClub_idalu, tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_cveplan));              
            //dr.put("tblClubCct", qryIfx.tblClubEscuela(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo,new String[]{"cvetipmat","cvemat","desmat"}));
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnGuardarCamDTaller_ActionPerformed (ArrayList<Map> tblCamDTaller, String tblPrincipal_cicescini)
    {
        boolean hacerCommit=false;
        try
        {
            qryIfx.conectarConTransaccion();
            qryIfx.guardarCamDTaller (tblCamDTaller, tblPrincipal_cicescini);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnAnteriorGpo_Click (String seccion, ArrayList<Map>tblPrincipal, String tblPrincipal_idcct, String tblPrincipal_cct, 
            String tblPrincipal_cveplan, int posSelActual, String califCicEscIn, String cicescini, String tipo_usuario, String xprimeraves,
            String usuarioNo22,String txtUsuario) 
    { 
        ArrayList<Map> camDTaller = new ArrayList<Map>();
        dr.put("tblPrincipal_selectedRow", posSelActual);
        

        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            
            /*if (hayCambios)
                btnCamDGpo_Click (tblCamDeGpo, tblCamDeGpo_cvedefsuf, califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario);
            
            if (dr.get("returnCase").equals(1))                                                                                                  //Por si se llamó a guardar cambios y no hubo errores
            { */
                qryIfx.conectar();
                //this.dr.put("hayCambios", false);
                posSelActual--;
                while ( posSelActual>=0 && camDTaller.isEmpty() )
                {
                    camDTaller = qryIfx.camDTaller (cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  
                    
                    if (camDTaller.size()>0)                        
                        dr.put("tblCamDTaller",camDTaller);
                    if (!dr.get("returnCase").equals(1))                                                                                              //Si hubo un error en el formActive
                        break;                                                                                                                                   //Nos salimos                    

                    dr.put("tblPrincipal_selectedRow", posSelActual);
                    dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                    dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));                    
                    posSelActual--;
                }

           // }
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnSiguienteGpo_Click (String seccion, ArrayList<Map>tblPrincipal, String tblPrincipal_idcct, String tblPrincipal_cct, 
            String tblPrincipal_cveplan, int posSelActual, String califCicEscIn, String cicescini, String tipo_usuario, String txtUsuario, 
            String usuarioNo22, String xprimeraves) 
    { 
        int numFilas;
        ArrayList<Map> camDTaller=new ArrayList<Map>();
        
        numFilas = tblPrincipal.size();
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            
            /*if (hayCambios)
                btnCamDGpo_Click ( tblCamDeGpo, tblCamDeGpo_cvedefsuf, califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario);
            
            if (dr.get("returnCase").equals(1))                                                                                                  //Por si se llamó a guardar cambios y no hubo errores
            {*/
                qryIfx.conectar();
                //this.dr.put("hayCambios", false);
                posSelActual++;
                while ( posSelActual<numFilas && camDTaller.isEmpty()  )
                {
                    camDTaller = qryIfx.camDTaller (cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  
                    /*if (QCamDeGpo.size()>0){
                        FormActivate( false, seccion, califCicEscIn, cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, 
                                ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tipo_usuario, txtUsuario, 
                                usuarioNo22, xprimeraves);
                    }

                    if (!dr.get("returnCase").equals(1))                                                                                              //Si hubo un error en el formActive
                        break;    */                                                                                                                               //Nos salimos

                    dr.put("tblCamDTaller",camDTaller);

                    dr.put("tblPrincipal_selectedRow", posSelActual);
                    dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                    dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
                    //dr.put("isTodoCalOf", qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM"));
                    //dr.put("idalusDeof", qryIfx.getAlumnosDesoficEnGrupoEnBimeval(califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tblPrincipal_cveplan.equals("3")?"EVALUACION 3":"CALIFS BIM 5"));
                    //dr.put("ofs", qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), "", tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM", tblPrincipal_cveplan.equals("3")?"3":"5"));
                    posSelActual++;
                }

                if ( posSelActual >= numFilas && camDTaller.isEmpty() ) {
                    btnAnteriorGpo_Click (""+sesion.getAttribute("seccion"), dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"grado","grupo"}),  
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cct"), r.gP("tblPrincipal_cveplan"), dm.toInt(r.gP("posSelActual")), r.gP("califCicEscIn"),
                    r.gP("cicescini"), ""+sesion.getAttribute("tipo_usuario"), ""+sesion.getAttribute("usuarioNo22"), r.gP("xprimeraves"), txtUsuario);
                }
            //}

        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
