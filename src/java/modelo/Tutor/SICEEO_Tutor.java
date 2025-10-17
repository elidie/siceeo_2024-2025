package modelo.Tutor;

import java.sql.SQLException;
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

/* 
    Creado el : 22-may-2017, 10:37:08
    Autor     : Elizabeth
*/

public class SICEEO_Tutor {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_Tutor (Map datosReturn, HttpServletRequest request)
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
        if (metodo.equals("foAc")) {
            formActivate ( r.gP("califCicEscIn"), ""+sesion.getAttribute("userName"), r.gP("cicescin") );
            FormCreate (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));
        }
        else if(metodo.equals("caAluTut"))
            btnCargaAlumnoTutor_Click (r.gP("cicescini"), r.gP("tblPrincipal_cveplan"), r.gP("idcct"), r.gP("grado"), 
                    r.gP("grupo"),""+sesion.getAttribute("userName"));
        else if (metodo.equals("btGdDaTu"))
            btnGuardarTutor_Click (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("idalu"), r.gP("txtCurp"), r.gP("cveparent"), 
                    r.gP("txtApe1"), r.gP("txtApe2"), r.gP("txtNombre"), r.gP("txtTel"), ""+sesion.getAttribute("userName")
                    );
        else if (metodo.equals("tblAlumCapTutor_ChSeIt"))
            tblAlumCapTutor_ChangeSelectedItem (r.gP("califCicEscIn"),r.gP("tblPrincipal_idcct"), r.gP("tblAlumCapTutor_idalu"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));
        else if (metodo.equals("btAnGp"))
            btnAnteriorGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","cveplan"}),  dm.toInt(r.gP("posSelActual")),  r.gP("califCicEscIn"));
        else if (metodo.equals("btSiGp"))
            btnSiguienteGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","cveplan"}),  dm.toInt(r.gP("posSelActual")) ,  r.gP("califCicEscIn") );
        else if (metodo.equals("btnBuskTu"))
            btnBuskTutor_Click (r.gP("txtCurp"), r.gP("tblPrincipal_idcct"));    
    }
    
    private void formActivate (String califCicEscIn, String txtUsuario, String cicescin)
    {
        String superUsuario = ""+sesion.getAttribute("superUsuario");
        boolean tipoCambCic1=false, tipoCambCic2=false;

        dr.put("lblCiclo",califCicEscIn+'-'+(dm.toInt(califCicEscIn)+1));
        dr.put("tblAvancesXEval_Visible",false);
        dr.put("tipoCambCic",-1);                                                   //Si tipoCambCic es -1 no hay permisos para desplazarse entre ciclos
              //---------------- Tambien as escuelas de secundaria de 3er grado pueden ver el botón de ciclo anterior
        if ( (tipoCambCic2=(txtUsuario.toUpperCase().equals("IVALLE") || txtUsuario.toUpperCase().equals("ELYLOPEZ") || txtUsuario.toUpperCase().equals("EMMANUEL") || txtUsuario.toUpperCase().equals("EDITHEGON") || txtUsuario.toUpperCase().equals("MARIOCIMM") || superUsuario.equals("si"))) )
        {
            dr.put("btnAnteriorCiclo_Visible",true);
            dr.put("btnSiguienteCiclo_Visible",true);
            if (tipoCambCic2)
                dr.put("tipoCambCic",1);                                            //Si tipoCambCic es 1, es superusuario y tiene todos los accesos
            else if (tipoCambCic1)
                dr.put("tipoCambCic",0);                                            //Si tipoCambCic es 0, es CCT y puede moverse al ciclo anterior con restricciones
        }else{
            dr.put("btnAnteriorCiclo_Visible",false);
            dr.put("btnSiguienteCiclo_Visible",false);
        }

        if ( !cicescin.equals(califCicEscIn) ) { //handa en otro ciclo menos el actual
           dr.put("btnAnteriorGpo_Enabled",false);
           dr.put("btnSiguienteGpo_Enabled",false);
        }else{
           dr.put("btnAnteriorGpo_Enabled",true);
           dr.put("btnSiguienteGpo_Enabled",true);
        }
    }
    
    private void FormCreate (String CalifCicEscIn, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        ArrayList<Map> QAlumCapTutor;

        dr.put("paTras", "no");
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();

            QAlumCapTutor = qryIfx.alumCaptuRepEval(CalifCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de evaluaciones
            dr.put("tblAlumCapTutor",QAlumCapTutor);
                  
            dr.put("parentesco",qryIfx.getParentesco());
            qryIfx.getTutorAlumno(""+QAlumCapTutor.get(0).get("idalu"), CalifCicEscIn, dr);
            
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnGuardarTutor_Click (String CalifCicEscIn, String tblPrincipal_idcct, String idalu,String curp,String cveparent, 
            String txtPrimerApe, String txtSegundoApe, String txtNombre, String txtTelefono, String usuario)
    {
        String idtutor="";
        boolean hacerCommit = false;
        Map QMaximosP;
        int QMaximosT_maxlibret=0;
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();

            if ( txtPrimerApe.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","primer apellido");
            if ( txtSegundoApe.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","segundo apellido");
            if ( txtNombre.contains("  ") )
                throw new SICEEO_Excepcion (0,"ESPACIO_BLANCO","nombre");
            //++++++++++++++++++++++++++++++++
            if ( txtNombre.trim().length()==0 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","nombre");
            if ( txtPrimerApe.trim().length()==0 )
                throw new SICEEO_Excepcion (0,"SIN_DATO","apellido");

            //++++++++++++++++++++++++++++++++
            String temp;
            if ( !dm.isNombreOApellido (txtPrimerApe.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","primer apellido");
            if ( !txtSegundoApe.equals("") && !dm.isNombreOApellido (txtSegundoApe.trim(),30) )
                throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","segundo apellido");
            if ( !dm.isNombreOApellido (txtNombre.trim(),40) ){                                                    //Si ya estaba, lo dejamos pasar sólo si el problema es de acentos
                temp = txtNombre.trim().toUpperCase().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");  //Descartamos que no sea por motivo de acentos
                if ( !dm.isNombreOApellido (temp,40) )
                    throw new SICEEO_Excepcion (0,"CARACTER_INVALIDO","nombre");            
            }
            //++++++++++++++++++++++++++++++++
            /********************** Verificar si existe tutor ********************************/
            idtutor = qryIfx.buscarTutor (curp.trim().toUpperCase());
            if(idtutor.isEmpty()) {            
                QMaximosP = qryIfx.getMaximosP ();
                QMaximosT_maxlibret = dm.toInt(qryIfx.getMaximosT ());

                if ( dm.toInt(QMaximosP.get("maxlibret")) > QMaximosT_maxlibret )
                    idtutor = ""+QMaximosP.get("maxlibret");
                else 
                    idtutor = ""+QMaximosT_maxlibret;

                qryIfx.actualizaTablaParametrosT (idtutor);
                qryIfx.insertarTutor(idtutor,curp.trim().toUpperCase(),txtPrimerApe.trim().toUpperCase(),txtSegundoApe.trim().toUpperCase(),txtNombre.trim().toUpperCase(),txtTelefono);
            }
            else {
                qryIfx.actualizarTutor(CalifCicEscIn,idtutor, cveparent, txtPrimerApe, txtSegundoApe, txtNombre, txtTelefono);
            }
            
            qryIfx.actualizarAlumnoTutor(idtutor, idalu, CalifCicEscIn, cveparent, usuario);
            
            hacerCommit = true; 
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.TutorAlum("INDISPUESTO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.TutorAlum(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnCargaAlumnoTutor_Click ( String tblPrincipal_cicescini, String cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario)
    {
        boolean hacerCommit = false;
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();
            
            /********************** insertar tutor ciclo anterior ********************************/
            qryIfx.insertarAluTutor (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario);

            hacerCommit = true; 
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.TutorAlum("OCUPADO", ex.getMessage(), "", this.dr);  }
        /*catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.TutorAlum(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }*/
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnBuskTutor_Click ( String txtCurp, String tblPrincipal_idcct)
    {        
        int caso=0;
        ArrayList<Map> tutor;
        Map datosTutor = new HashMap();
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            if(txtCurp.trim().isEmpty())
                throw new SICEEO_Excepcion(0, "CURP_VACIO");
            else if(txtCurp.trim().length()<10)
                throw new SICEEO_Excepcion(0, "CURP10");
            else if(txtCurp.trim().length()>=10 && txtCurp.trim().length()<18)
                caso = 1; //curp incompleta
            else if (txtCurp.trim().length()==18)
                caso = 2;
            else             
                throw new SICEEO_Excepcion(0, "VERF_CURP");        
            tutor = qryIfx.buscarTutor(txtCurp.trim().toUpperCase(), caso); 
            if(tutor.isEmpty())
                throw new SICEEO_Excepcion(0, "NOHAY_TUTOR");
            else if(tutor.size()>1)
                throw new SICEEO_Excepcion(0, "MASDE1_TUTOR");
                        
            datosTutor.put("nombre", tutor.get(0).get("nombre"));
            datosTutor.put("apepat", tutor.get(0).get("apepat"));
            datosTutor.put("apemat", tutor.get(0).get("apemat"));            
            datosTutor.put("curp", tutor.get(0).get("curp"));
            datosTutor.put("telefono", "");
                                                        
            dr.put("existeTutor", 'O');
            dr.put("tutor", datosTutor);
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.TutorAlum("OCUPADO", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.TutorAlum(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally {try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void tblAlumCapTutor_ChangeSelectedItem (String CalifCicEscIn, String tblPrincipal_idcct,String tblAlumCapTutor_idalu, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        
        ArrayList<Map> QAlumCapTutor;
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            QAlumCapTutor = qryIfx.alumCaptuRepEval(CalifCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de evaluaciones
            dr.put("tblAlumCapTutor",QAlumCapTutor);
            
            qryIfx.getTutorAlumno(tblAlumCapTutor_idalu, CalifCicEscIn, dr);
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnAnteriorGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn) 
{ 
    ArrayList<Map> QAlumCapTutor=new ArrayList<Map>();    
    dr.put("tblPrincipal_selectedRow", posSelActual);
    
    try
    {
        qryIfx.conectar();
        posSelActual--;
        while ( posSelActual>=0 && QAlumCapTutor.isEmpty() )
        {    
            QAlumCapTutor = qryIfx.alumCaptuRepEval(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  
            dr.put("tblAlumCapTutor",QAlumCapTutor);
                  
            dr.put("parentesco",qryIfx.getParentesco());
            qryIfx.getTutorAlumno(""+QAlumCapTutor.get(0).get("idalu"), califCicEscIn, dr);
            
            dr.put("tblPrincipal_selectedRow", posSelActual);
            dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
            dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
            posSelActual--;
        }
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

private void btnSiguienteGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn) 
{ 
    int numFilas;
    ArrayList<Map> QAlumCapTutor=new ArrayList<Map>();    
    
    numFilas = tblPrincipal.size();
    try
    {
        qryIfx.conectar();
        posSelActual++;        
        while ( posSelActual<numFilas && QAlumCapTutor.isEmpty()  )
        {                        
            QAlumCapTutor = qryIfx.alumCaptuRepEval(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de evaluaciones
            dr.put("tblAlumCapTutor",QAlumCapTutor);
                  
            dr.put("parentesco",qryIfx.getParentesco());
            qryIfx.getTutorAlumno(""+QAlumCapTutor.get(0).get("idalu"), califCicEscIn, dr);
            
            dr.put("tblPrincipal_selectedRow", posSelActual);
            dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
            dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
            posSelActual++;
        }
        
        if ( posSelActual >= numFilas && QAlumCapTutor.isEmpty() ) {
            qryIfx.cerrarConexion();
           btnAnteriorGpo_Click(tblPrincipal, posSelActual, califCicEscIn);
        }        
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

}
