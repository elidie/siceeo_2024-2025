package modelo.EvalPreescolar;

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
    Creado el : 12-dic-2016, 17:44:21
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_EvalPreescolar {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_EvalPreescolar (Map datosReturn, HttpServletRequest request)
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
        if (metodo.equals("foAc")){
            formActivate ( r.gP("califCicEscIn"), ""+sesion.getAttribute("userName"), r.gP("cicescin"), r.gP("eval") );
            FormCreate (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), 
                    r.gP("eval"));
        }else if (metodo.equals("tbMaCa_ChSeIt"))
            tblAlumCapEval_ChangeSelectedItem (r.gP("numeval"),r.gP("tblAlumCapEval_idalu"),r.gP("califCicEscIn"),r.gP("tblPrincipal_grado"));
        else if (metodo.equals("btAnNuEv"))
            btnAnteriorNumEval_Click (r.gP("tblAlumCapEval_cicescini"), r.gP("tblPrincipal_idcct"),  r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"), 
                    dm.toInt(r.gP("numeval")), r.gP("tblAlumCapEval_idalu"), r.gP("evalMin"), r.gP("evalMax"));
        else if (metodo.equals("btSiNuEv"))
            btnSiguienteNumEval_Click (r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"),dm.toInt(r.gP("numeval")), 
                    r.gP("tblAlumCapEval_idalu"), r.gP("tblAlumCapEval_cicescini") ,r.gP("evalMin"), r.gP("evalMax") );
        else if (metodo.equals("btGdEv_Cl"))
            btnGuardarEvaluaciones_Click ( dm.vstrToArrMap(r.gPV("tblAvancesXEval"), "~", new String[]{"cveprograma","cvetipmat","cvemat","avances"}), r.gP("idalu"), 
                    r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"),r.gP("numeval"), 
                    ""+sesion.getAttribute("userName")
            );
        else if (metodo.equals("btGuIn"))
            btnGuardaInasistencias_Click (r.gP("tblAlumCapEval_idalu"), r.gP("tblAlumCapEval_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"),dm.reqToMap(request, new String[]{"inst1", "inst2", "inst3"},null)
            );
        else if (metodo.equals("btAnGp"))
            btnAnteriorGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","nombre","cveplan","modalidad"}),  dm.toInt(r.gP("posSelActual")) ,  r.gP("califCicEscIn") ,  r.gP("numeval") );
        else if (metodo.equals("btSiGp"))
            btnSiguienteGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","nombre","cveplan","modalidad"}),  dm.toInt(r.gP("posSelActual")) ,  r.gP("califCicEscIn") ,  r.gP("numeval") );
        else if (metodo.equals("btGeIn"))
            btnGenInasis_Click(dm.vstrToArrMap(r.gPV("tblAlumCapEval"), "~", new String[]{"idalu"}), r.gP("tblAlumCapEval_cicescini"), r.gP("tblPrincipal_idcct"), 
                    r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado")
            );
        else if (metodo.equals("btGuDaCo"))
            btnGuardarDatosComplementarios_Click (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblAlumCapEval_idalu"), r.gP("cvelengua"), 
                   r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), ""+sesion.getAttribute("userName")); //,dm.toBool(r.gP("chkConlcuyo_checked")), r.gP("recomendaciones"), r.gP("trim1"), r.gP("trim2"), r.gP("trim3")
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate (String califCicEscIn, String txtUsuario, String cicescin, String eval )
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

        if ( eval.equals("1") ) dr.put("lblNumEval","1a EVALUACIÓN");
        else if ( eval.equals("2") ) dr.put("lblNumEval","2a EVALUACIÓN");
        else if ( eval.equals("3") ) dr.put("lblNumEval","3a EVALUACIÓN");

        dr.put("tblAvancesXEval_Columns2_Visible",false);
        //formato
        if ( dm.toInt(califCicEscIn)>=2013 ) 
        {
            dr.put("tblAvancesXEval_calif1_DisplayFormat","9.9");
            dr.put("tblAvancesXEval_calif2_DisplayFormat","9.9");
        }else{
            dr.put("tblAvancesXEval_calif1_DisplayFormat","9");
            dr.put("tblAvancesXEval_calif2_DisplayFormat","9");
        }

        dr.put("tblAvancesXEval_Visible",true);        
    }

    private void FormCreate (String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_modalidad, String tblPrincipal_grado, String tblPrincipal_grupo, 
            String eval)
    {
        Map datosComplementarios = null;
        ArrayList<Map> QPaqueteMatsDefault, tblAlumCapEval, tblAvancesXEval = new ArrayList<Map>(), tblInasistencias = new ArrayList<Map>(), tblComDocAlum = new ArrayList<Map>();
        String superUsuario = ""+sesion.getAttribute("superUsuario");
        
        dr.put("returnCase", 1);
        dr.put("paTras", "no");
        try
        {
            //***********************  Validacion para escuelas particulares **************************//
            /*if(superUsuario.trim().equals("") && (""+sesion.getAttribute("seccion")).equals("PVD"))            
                throw new SICEEO_Excepcion (-11,"PVD_SIN_PERMISO_CAPTURA");*/
            
            if(eval.equals(""))     //agregado para el caso de inactivo los 3 trim
                eval="0";           //agregado para el caso de inactivo los 3 trim        
            qryIfx.conectar();                        
            QPaqueteMatsDefault = qryIfx.getPaqueteMats_EvalPree(califCicEscIn, tblPrincipal_modalidad, tblPrincipal_grado);
            tblAlumCapEval = qryIfx.alumCapEvalPree(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de evaluaciones

            dr.put("eval", 1);
            if (tblAlumCapEval.size()>0){
                tblAvancesXEval = qryIfx.MatAvenceXEval(eval, ""+tblAlumCapEval.get(0).get("idalu"), califCicEscIn, tblPrincipal_grado);
                tblInasistencias = qryIfx.inasistencias (califCicEscIn, tblPrincipal_grado, ""+tblAlumCapEval.get(0).get("idalu"));  //qry de captura de calificaciones
                datosComplementarios = qryIfx.getDatoscomplementariosEvalPreesc (califCicEscIn, ""+tblAlumCapEval.get(0).get("idalu"));
                tblComDocAlum = qryIfx.comunicacionAlumDoc(califCicEscIn, ""+tblAlumCapEval.get(0).get("idalu"));
            }
            dr.put("lenguas",qryIfx.getLenguas());
            dr.put("paqueteMatsDefault",QPaqueteMatsDefault);
            dr.put("tblAlumCapEval",tblAlumCapEval);
            dr.put("tblAvancesXEval",tblAvancesXEval);
            dr.put("tblAlumCapEval_rowCount",tblAlumCapEval.size());
            dr.put("tblInasistencias",tblInasistencias);
            dr.put("tblComDocAlum",tblComDocAlum);
            //dr.put("concluyo", datosComplementarios==null?false:(Boolean)datosComplementarios.get("concluyo"));
            dr.put("cvelengua", datosComplementarios==null?"":""+datosComplementarios.get("cvelengua"));
            //dr.put("recomendaciones", datosComplementarios==null?"":""+datosComplementarios.get("recomendaciones"));
            dr.put("evalOf",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+eval));
            dr.put("evalOf1",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 1"));
            dr.put("evalOf2",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 2"));
            dr.put("evalOf3",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 3"));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        //catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.EvalPreescolar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }  //Para privadas
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }        
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }

    private void tblAlumCapEval_ChangeSelectedItem (String eval, String tblAlumCapEval_idalu, String califCicEscIn, String tblPrincipal_grado)
    {
        ArrayList<Map> tblAvancesXEval, tblInasistencias;
        try
        {
            qryIfx.conectar();
            tblAvancesXEval = qryIfx.MatAvenceXEval(eval, tblAlumCapEval_idalu, califCicEscIn,tblPrincipal_grado);
            dr.put("tblAvancesXEval",tblAvancesXEval);
            tblInasistencias = qryIfx.inasistencias (califCicEscIn, tblPrincipal_grado, tblAlumCapEval_idalu);
            dr.put("tblInasistencias", tblInasistencias);
            dr.put("tblComDocAlum",qryIfx.comunicacionAlumDoc(califCicEscIn, tblAlumCapEval_idalu));
            dr.putAll(qryIfx.getDatoscomplementariosEvalPreesc (califCicEscIn, tblAlumCapEval_idalu));
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }

    private void btnSiguienteNumEval_Click (String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, int eval, String idalu, String cicescini, String evalMin, String evalMax)
    {
        ArrayList<Map> tblAvancesXEval;

        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);

            if ( eval < Integer.parseInt(evalMax) )
               eval=eval+1; //Comentado para ciclo 2023-2024
            dr.put("eval", eval);

            if ( eval==1 ) dr.put("lblNumEval","1a EVALUACIÓN");
            else if ( eval==2 ) dr.put("lblNumEval","2a EVALUACIÓN");
            else if ( eval==3 ) dr.put("lblNumEval","3a EVALUACIÓN");

            qryIfx.conectar();
            tblAvancesXEval = qryIfx.MatAvenceXEval (""+eval, idalu, cicescini, tblPrincipal_grado);
            dr.put("tblAvancesXEval", tblAvancesXEval);
            dr.put("evalOf",qryIfx.isOficializado(tblPrincipal_idcct, cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+eval));
            dr.put("tblComDocAlum",qryIfx.comunicacionAlumDoc(cicescini, idalu));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }

    private void btnAnteriorNumEval_Click (String tblAlumCapEval_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, 
            int eval, String tblAlumCapEval_idalu, String evalMin, String evalMax)
    {
        ArrayList<Map> tblAvancesXEval;

        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);

            if ( eval > Integer.parseInt(evalMin) )  //Comentado para ciclo 2023-2024, para eval especifico
                eval=eval-1;
            dr.put("eval", eval);

            if ( eval==1 ) dr.put("lblNumEval","1a EVALUACIÓN");
            else if ( eval==2 ) dr.put("lblNumEval","2a EVALUACIÓN");
            else if ( eval==3 ) dr.put("lblNumEval","3a EVALUACIÓN");

            qryIfx.conectar();
            tblAvancesXEval = qryIfx.MatAvenceXEval (""+eval, tblAlumCapEval_idalu, tblAlumCapEval_cicescini, tblPrincipal_grado);
            dr.put("tblAvancesXEval", tblAvancesXEval);
            dr.put("evalOf",qryIfx.isOficializado(tblPrincipal_idcct, tblAlumCapEval_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+eval));
            dr.put("tblComDocAlum",qryIfx.comunicacionAlumDoc(tblAlumCapEval_cicescini, tblAlumCapEval_idalu));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }

    private void btnGuardarEvaluaciones_Click (ArrayList<Map> tblAvancesXEval, String idalu, String califCicEscIn, String tblPrincipal_idcct, 
            String tblPrincipal_grado, String tblPrincipal_grupo, String numeval, String txtUsuario)
    {
        boolean hacerCommit=false;
        String textoEscrito="";
        try{
            //try{
            for (int f=0; f<tblAvancesXEval.size(); f++){                       //Revisamos que no venga sin datos
                textoEscrito += (""+tblAvancesXEval.get(f).get("avances")).trim();
                if (textoEscrito.length()>2)
                    break;
            }
            //}catch (Exception ex){ throw new Exception ("Error en bloque 1: "+ex);}
            
            //try{
            if (textoEscrito.equals(""))
                throw new SICEEO_Excepcion (0,"NADA_QUE_GUARDAR");
            //}catch (Exception ex){ throw new Exception ("Error en bloque 2: "+ex);}
            
            //try{
            qryIfx.conectarConTransaccion();
            //}catch (Exception ex){ throw new Exception ("Error en bloque 3: "+ex);}

            //try{
            if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+numeval))
                throw new SICEEO_Excepcion (0,"EVAL_OFICIALIZADA");
            //}catch (Exception ex){ throw new Exception ("Error en bloque 4: "+ex);}

            int f=0;
            //try{
            for (f=0; f<tblAvancesXEval.size(); f++)
                qryIfx.actualizarEvalPreescolar ( tblAvancesXEval.get(f), idalu, califCicEscIn, tblPrincipal_grado, numeval, txtUsuario );
            //}catch (Exception ex){ throw new Exception ("Error en bloque 5: (f="+f+")");}
            
            hacerCommit=true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.EvalPreescolar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }

    private void btnGuardaInasistencias_Click (String tblAlumCapEval_idalu, String tblAlumCapEval_cicescini, String tblPrincipal_idcct, 
            String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo, Map tblInasistencias)
    {
        Map isOficBimEval = new HashMap();
        Map oficYdesofic;

        dr.put("returnCase", 1);
        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            
            //OJO HAY QUE IMPLEMENTAR ESTO CON ALUMNO DESOFICIALIZADO
            isOficBimEval.put("bimOf1",qryIfx.isOficializado(tblPrincipal_idcct, tblAlumCapEval_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 1"));
            isOficBimEval.put("bimOf2",qryIfx.isOficializado(tblPrincipal_idcct, tblAlumCapEval_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 2"));
            isOficBimEval.put("bimOf3",qryIfx.isOficializado(tblPrincipal_idcct, tblAlumCapEval_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 3"));
            isOficBimEval.put("alDeofB1",false);
            isOficBimEval.put("alDeofB2",false);
            isOficBimEval.put("alDeofB3",false);
            
            //oficYdesofic = qryIfx.oficYDesoficEnCalif(tblAlumCapEval_cicescini, tblPrincipal_idcct, tblAlumCapEval_idalu, "");

            qryIfx.guardaInasistencias (tblAlumCapEval_idalu, tblAlumCapEval_cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_grado, ""+tblInasistencias.get("inst1"), ""+tblInasistencias.get("inst2"), ""+tblInasistencias.get("inst3"), isOficBimEval);
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }

    private void btnGenInasis_Click(ArrayList<Map> tblAlumCapEval, String tblAlumCapEval_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
            String tblPrincipal_grado)
    {
        int numFilas;
        boolean hacerCommit = false;
        ArrayList<Map> tblInasistencias=null;

        numFilas = tblAlumCapEval.size();
        try{
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();

            if (qryIfx.isConceptoTodoOficializado(tblPrincipal_idcct, tblAlumCapEval_cicescini, "EVALUACION"))
                throw new SICEEO_Excepcion (0,"TODO_OFICIALIZADO");

            for (int i=0; i<numFilas; i++)
            {
                tblInasistencias = qryIfx.inasistencias (tblAlumCapEval_cicescini, tblPrincipal_grado, ""+tblAlumCapEval.get(i).get("idalu"));
                if ( tblInasistencias.isEmpty() )                                         //osea k aun no tiene registros en la tabla inasistencias
                    qryIfx.insertarInasistencias (""+tblAlumCapEval.get(i).get("idalu"), tblAlumCapEval_cicescini, tblPrincipal_cveplan, tblPrincipal_grado);
            }
            tblInasistencias = qryIfx.inasistencias (tblAlumCapEval_cicescini, tblPrincipal_grado, ""+tblAlumCapEval.get(0).get("idalu"));
            dr.put("tblInasistencias", tblInasistencias);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.EvalPreescolar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit); } catch (SQLException ex) { } }
    }

    private void btnGuardarDatosComplementarios_Click (String califCicEscIn, String tblPrincipal_idcct, String idalu,  String cvelengua, 
            String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario) //,boolean chkConlcuyo_checked, String recomendaciones, String trim1, String trim2, String trim3
    {
        boolean hacerCommit = false;
        Map isOficBimEval = new HashMap();
        
        try{
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();

            if (qryIfx.isConceptoTodoOficializado(tblPrincipal_idcct, califCicEscIn, "EVALUACION"))
                throw new SICEEO_Excepcion (0,"TODO_OFICIALIZADO");

            //qryIfx.guardarRecomendacionesEvalPrescolar (califCicEscIn, idalu, cvelengua, txtUsuario); //chkConlcuyo_checked, recomendaciones,
            qryIfx.guardarLengua (califCicEscIn, idalu, cvelengua, txtUsuario); 
            //OJO HAY QUE IMPLEMENTAR ESTO CON ALUMNO DESOFICIALIZADO
            isOficBimEval.put("bimOf1",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 1"));
            isOficBimEval.put("bimOf2",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 2"));
            isOficBimEval.put("bimOf3",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 3"));
            isOficBimEval.put("alDeofB1",false);
            isOficBimEval.put("alDeofB2",false);
            isOficBimEval.put("alDeofB3",false);
            
            //qryIfx.guardaComDocAlum (idalu, califCicEscIn, trim1, trim2,trim3,isOficBimEval);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.EvalPreescolar(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnAnteriorGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn, String numeval) 
    { 
        Map datosComplementarios = null;
        ArrayList<Map> QPaqueteMatsDefault, QAlumCapEval=new ArrayList<Map>(), tblAvancesXEval=new ArrayList<Map>(), tblInasistencias=new ArrayList<Map>(), tblComDocAlum=new ArrayList<Map>();
        dr.put("tblPrincipal_selectedRow", posSelActual);

        try
        {
            qryIfx.conectar();
            posSelActual--;
            while ( posSelActual>=0 && QAlumCapEval.isEmpty() )
            {
                QPaqueteMatsDefault = qryIfx.getPaqueteMats_EvalPree(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("modalidad"), ""+tblPrincipal.get(posSelActual).get("grado"));
                QAlumCapEval = qryIfx.alumCapEvalPree(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de calificaciones
                if (QAlumCapEval.size()>0){
                    tblAvancesXEval = qryIfx.MatAvenceXEval(numeval, ""+QAlumCapEval.get(0).get("idalu"), califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"));
                    tblInasistencias = qryIfx.inasistencias (califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"), ""+QAlumCapEval.get(0).get("idalu"));
                    datosComplementarios = qryIfx.getDatoscomplementariosEvalPreesc (califCicEscIn, ""+QAlumCapEval.get(0).get("idalu"));
                    //tblComDocAlum = qryIfx.comunicacionAlumDoc(califCicEscIn, ""+QAlumCapEval.get(0).get("idalu"));
                }
                //dr.put("tblComDocAlum",tblComDocAlum);
                dr.put("paqueteMatsDefault",QPaqueteMatsDefault);
                dr.put("tblAlumCapEval",QAlumCapEval);
                dr.put("tblAvancesXEval",tblAvancesXEval);
                dr.put("tblInasistencias",tblInasistencias);
                
                //dr.put("concluyo", datosComplementarios==null?false:(Boolean)datosComplementarios.get("concluyo"));
                dr.put("cvelengua", datosComplementarios==null?"":""+datosComplementarios.get("cvelengua"));
                //dr.put("recomendaciones", datosComplementarios==null?"":""+datosComplementarios.get("recomendaciones"));

                dr.put("tblPrincipal_selectedRow", posSelActual);
                dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
                posSelActual--;
            }
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnSiguienteGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn, String numeval) 
    { 
        int numFilas;
        Map datosComplementarios = null;
        ArrayList<Map> QPaqueteMatsDefault, QAlumCapEval=new ArrayList<Map>(), tblAvancesXEval=new ArrayList<Map>(), tblInasistencias=new ArrayList<Map>(), tblComDocAlum=new ArrayList<Map>();

        numFilas = tblPrincipal.size();
        try
        {
            qryIfx.conectar();
            posSelActual++;
            while ( posSelActual<numFilas && QAlumCapEval.isEmpty()  )
            {
                QPaqueteMatsDefault = qryIfx.getPaqueteMats_EvalPree(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("modalidad"), ""+tblPrincipal.get(posSelActual).get("grado"));
                QAlumCapEval = qryIfx.alumCapEvalPree(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de calificaciones
                if (QAlumCapEval.size()>0){
                    tblAvancesXEval = qryIfx.MatAvenceXEval(numeval, ""+QAlumCapEval.get(0).get("idalu"), califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"));
                    tblInasistencias = qryIfx.inasistencias (califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"), ""+QAlumCapEval.get(0).get("idalu"));
                    datosComplementarios = qryIfx.getDatoscomplementariosEvalPreesc (califCicEscIn, ""+QAlumCapEval.get(0).get("idalu"));
                    //tblComDocAlum = qryIfx.comunicacionAlumDoc(califCicEscIn, ""+QAlumCapEval.get(0).get("idalu"));
                }
                //dr.put("tblComDocAlum",tblComDocAlum);
                dr.put("paqueteMatsDefault",QPaqueteMatsDefault);
                dr.put("tblAlumCapEval",QAlumCapEval);
                dr.put("tblAvancesXEval",tblAvancesXEval);
                dr.put("tblInasistencias",tblInasistencias);
                //dr.put("concluyo", datosComplementarios==null ? false:(Boolean)datosComplementarios.get("concluyo"));
                dr.put("cvelengua", datosComplementarios==null?"":""+datosComplementarios.get("cvelengua"));
                //dr.put("recomendaciones", datosComplementarios==null?"":""+datosComplementarios.get("recomendaciones"));

                dr.put("tblPrincipal_selectedRow", posSelActual);
                dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
                posSelActual++;
            }

            if ( posSelActual >= numFilas && QAlumCapEval.isEmpty() ) {
                qryIfx.cerrarConexion();
               btnAnteriorGpo_Click(tblPrincipal, posSelActual, califCicEscIn, numeval);
            }

        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
