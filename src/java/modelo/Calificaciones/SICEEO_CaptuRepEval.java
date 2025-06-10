package modelo.Calificaciones;

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
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_CaptuRepEval {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_CaptuRepEval (Map datosReturn, HttpServletRequest request)
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
            FormCreate (r.gP("califCicEscIn"), r.gP("tblPrincipal_modalidad"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("eval"));
        } else if (metodo.equals("btAnNuEv"))
            btnAnteriorNumEval_Click (r.gP("tblAlumCapEval_cicescini"), r.gP("tblPrincipal_idcct"),  r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"), 
                    dm.toInt(r.gP("numeval")), r.gP("tblAlumCapRepEval_idalu"), r.gP("evalMin"), r.gP("evalMax"), r.gP("cveprograma"));
        else if (metodo.equals("btSiNuEv"))
            btnSiguienteNumEval_Click (r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"),dm.toInt(r.gP("numeval")), 
                    r.gP("tblAlumCapRepEval_idalu"), r.gP("tblAlumCapEval_cicescini") ,r.gP("evalMin"), r.gP("evalMax"), r.gP("cveprograma") );
        else if (metodo.equals("btGdCaReEv_Cl"))
            btnGuardarCapRepEval_Click (dm.vstrToArrMap(r.gPV("tblAvancesXEvalYMat"), "~", new String[]{"cveprograma","cvetipmat","cvemat","avances"}),
                    r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("tblAlumCapRepEval_idalu"),
                        r.gP("cveplan"),r.gP("cvelengua"),r.gP("chkRiesgosAlerta1"), r.gP("chkRiesgosAlerta2"), r.gP("chkRiesgosAlerta3"), r.gP("tutoria"), 
                        r.gP("txtaHabEscritura"), r.gP("txtaHabLectura"),r.gP("txtaHabMatematica"),                         
                        r.gP("txtaRecomendGrales_eval1"),r.gP("txtaRecomendGrales_eval2"),r.gP("txtaRecomendGrales_eval3"),                         
                        r.gP("eval"),""+sesion.getAttribute("userName")
                    );
            /*btnGuardarCapRepEval_Click (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("idalu"),
                        r.gP("cveplan"),r.gP("cvelengua"),r.gP("chkRiesgosAlerta1"), r.gP("chkRiesgosAlerta2"), r.gP("chkRiesgosAlerta3"), r.gP("tutoria"), 
                        r.gP("txtaHabEscritura"), r.gP("txtaHabLectura"),r.gP("txtaHabMatematica"), 
                        dm.vstrToArrMap(r.gPV("chkHabEscritura"), "~", new String[]{"1","2","3","4","5"}),
                        dm.vstrToArrMap(r.gPV("chkHabLectura"), "~", new String[]{"1","2","3","4","5"}),
                        dm.vstrToArrMap(r.gPV("chkHabMatematica"), "~", new String[]{"1","2","3","4","5"}),
                        dm.vstrToArrMap(r.gPV("tblObsYRecomXBimYAsig"), "~", new String[]{"bimestre","cvetipmat","cvemat","obserEsp","apoyo","cvetipmat_oldValue","cvemat_oldValue"}),
                        r.gP("txtaRecomendGrales_eval1"),r.gP("txtaRecomendGrales_eval2"),r.gP("txtaRecomendGrales_eval3"), 
                        dm.vstrToArrMap(r.gPV("tblEvalLec"), "~", new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","idpregunta"})
                    ); //Se comento en ciclo escolar 2024-2025 */            
        else if (metodo.equals("tbAlCaReEv_ChSeIt"))
            tblAlumCapRepEval_ChangeSelectedItem (r.gP("eval"),r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblAlumCapRepEval_idalu"), r.gP("cveprograma"));
        else if (metodo.equals("btAnGp"))
            btnAnteriorGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","cveplan"}),  dm.toInt(r.gP("posSelActual")),  r.gP("califCicEscIn"),
                    r.gP("eval"), r.gP("tblPrincipal_cveprograma"));
        else if (metodo.equals("btSiGp"))
            btnSiguienteGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","cveplan"}),  dm.toInt(r.gP("posSelActual")) ,  r.gP("califCicEscIn"),
                    r.gP("eval"), r.gP("tblPrincipal_cveprograma") );
        else if (metodo.equals("btGuDaCo"))
            btnGuardarDatosComplementarios_Click (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblAlumCapRepEval_idalu"), r.gP("cvelengua"), 
                    ""+sesion.getAttribute("userName"));
    }
    
    private void formActivate (String califCicEscIn, String txtUsuario, String cicescin, String eval)
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
        
        if ( eval.equals("1") ) dr.put("lblNumEval","1a EVALUACIÓN");
        else if ( eval.equals("2") ) dr.put("lblNumEval","2a EVALUACIÓN");
        else if ( eval.equals("3") ) dr.put("lblNumEval","3a EVALUACIÓN");

        if ( !cicescin.equals(califCicEscIn) ) { //handa en otro ciclo menos el actual
           dr.put("btnAnteriorGpo_Enabled",false);
           dr.put("btnSiguienteGpo_Enabled",false);
        }else{
           dr.put("btnAnteriorGpo_Enabled",true);
           dr.put("btnSiguienteGpo_Enabled",true);
        }
    }
    
    private void FormCreate (String CalifCicEscIn, String tblPrincipal_modalidad, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo, String eval)
    {
        ArrayList<Map> QAlumCapRepEval, QObsYRecomXBimYMat = new ArrayList<Map>();
        ArrayList<Map> QPaqueteMatsDefault = new ArrayList<Map>();
        dr.put("paTras", "no");
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            if(eval.equals(""))     //agregado para el caso de inactivo los 3 trim
               eval="0";           //agregado para el caso de inactivo los 3 trim     
            qryIfx.conectar();
            
            QPaqueteMatsDefault = qryIfx.getPaqueteDeMateriasDeCiclo(CalifCicEscIn, tblPrincipal_modalidad, tblPrincipal_cveplan, tblPrincipal_grado);                            
            QAlumCapRepEval = qryIfx.alumCaptuRepEval(CalifCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de evaluaciones
            
            dr.put("eval", 1);
            if (QAlumCapRepEval.size()>0) {
                dr.putAll(qryIfx.getCaptuRepEval(CalifCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu")));
                
                // Extraer la información capturada de las recomendaciones por materia
                QObsYRecomXBimYMat = qryIfx.obsvRecomXMat(eval, ""+QAlumCapRepEval.get(0).get("idalu"), CalifCicEscIn, tblPrincipal_grado, ""+QPaqueteMatsDefault.get(0).get("cveprograma"));
                /*tblAvancesXEval = qryIfx.MatAvenceXEval(eval, ""+tblAlumCapEval.get(0).get("idalu"), califCicEscIn, tblPrincipal_grado);*/
            }
            //QPreguntasCompLectora = qryIfx.getTablaPreguntasCompLectora (tblPrincipal_cveplan, tblPrincipal_grado);
            dr.put("paqueteMatsDefault",QPaqueteMatsDefault);
            dr.put("lenguas",qryIfx.getLenguas());
            //dr.put("obsrecgral",qryIfx.getObsRecGral(CalifCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu")));
            dr.put("tblAlumCapRepEval",QAlumCapRepEval);
            dr.put("tblRecomXMat",QObsYRecomXBimYMat);            
            
            //dr.put("capRepOf",qryIfx.isOficializado(tblPrincipal_idcct, CalifCicEscIn,"EVALUACION "+eval));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnGuardarCapRepEval_Click (ArrayList<Map> tblAvancesXEvalYMat, String CalifCicEscIn, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String idalu,
            String cveplan,String cvelengua, String chkRiesgosAlerta1, String chkRiesgosAlerta2, String chkRiesgosAlerta3, String tutoria, 
            String txtaHabEscritura, String txtaHabLectura, String txtaHabMatematica, String txtaRecomendGrales_eval1, String txtaRecomendGrales_eval2, 
            String txtaRecomendGrales_eval3, String numeval, String txtusuario)
    {
        String textoEscrito="";
        
        boolean hacerCommit = false;
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            
            
            for (int f=0; f<tblAvancesXEvalYMat.size(); f++){                       //Revisamos que no venga sin datos
                textoEscrito += (""+tblAvancesXEvalYMat.get(f).get("avances")).trim();
                if (textoEscrito.length()>2)
                    break;
            }
            if (textoEscrito.equals(""))
                throw new SICEEO_Excepcion (0,"NADA_QUE_GUARDAR");
            qryIfx.conectarConTransaccion();
            
            /*if (qryIfx.isOficializado(tblPrincipal_idcct, CalifCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+numeval))
                throw new SICEEO_Excepcion (0,"EVAL_OFICIALIZADA");*/ /* Checar si esta oficializado num de eval */
            
            int f=0;
            
            for (f=0; f<tblAvancesXEvalYMat.size(); f++)
                qryIfx.actualizarRecomXEvalYMat ( tblAvancesXEvalYMat.get(f), idalu, CalifCicEscIn, tblPrincipal_grado, numeval, txtusuario );
            
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CaptuRepEval(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnSiguienteNumEval_Click (String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, 
            int eval, String idalu, String cicescini, String evalMin, String evalMax, String cveprograma)
    {
        ArrayList<Map> QObsYRecomXBimYMat;

        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);

            if ( eval < Integer.parseInt(evalMax) )
               eval=eval+1; //Comentado para ciclo 2023-2024
            dr.put("eval", eval);

            if ( eval==1 ) dr.put("lblNumEval","1a EVALUACIÓN");
            else if ( eval==2 ) dr.put("lblNumEval","2a EVALUACIÓN");
            else if ( eval==3 ) dr.put("lblNumEval","3a EVALUACIÓN");

            qryIfx.conectar();
            QObsYRecomXBimYMat = qryIfx.obsvRecomXMat(""+eval, idalu, cicescini, tblPrincipal_grado, cveprograma);
            dr.put("tblRecomXMat", QObsYRecomXBimYMat);
            dr.put("evalOf",qryIfx.isOficializado(tblPrincipal_idcct, cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+eval));
            
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }

    private void btnAnteriorNumEval_Click (String tblAlumCapEval_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, int eval, String tblAlumCapRepEval_idalu, String evalMin, String evalMax, String cveprograma)
    {
        ArrayList<Map> QObsYRecomXBimYMat;

        try {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);

            if ( eval > Integer.parseInt(evalMin) )  //Comentado para ciclo 2023-2024, para eval especifico
                eval=eval-1;
            dr.put("eval", eval);

            if ( eval==1 ) dr.put("lblNumEval","1a EVALUACIÓN");
            else if ( eval==2 ) dr.put("lblNumEval","2a EVALUACIÓN");
            else if ( eval==3 ) dr.put("lblNumEval","3a EVALUACIÓN");

            qryIfx.conectar();
            
            QObsYRecomXBimYMat = qryIfx.obsvRecomXMat(""+eval, tblAlumCapRepEval_idalu, tblAlumCapEval_cicescini, tblPrincipal_grado, cveprograma);
            dr.put("tblRecomXMat", QObsYRecomXBimYMat);
            dr.put("evalOf",qryIfx.isOficializado(tblPrincipal_idcct, tblAlumCapEval_cicescini, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION "+eval));
            
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void tblAlumCapRepEval_ChangeSelectedItem (String eval,String CalifCicEscIn, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, 
            String tblAlumCapRepEval_idalu, String cveprograma)
    {
        ArrayList<Map> QObsYRecomXBimYMat;
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();

            dr.putAll(qryIfx.getCaptuRepEval(CalifCicEscIn, tblAlumCapRepEval_idalu));                                        
            QObsYRecomXBimYMat = qryIfx.obsvRecomXMat(eval, tblAlumCapRepEval_idalu, CalifCicEscIn, tblPrincipal_grado, cveprograma);
                        
            dr.put("tblRecomXMat",QObsYRecomXBimYMat);
            
            //dr.put("capRepOf",qryIfx.isOficializado(tblPrincipal_idcct, CalifCicEscIn,"EVALUACION "+eval));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnAnteriorGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn, String numeval, String cveprograma) 
{ 
    ArrayList<Map> QAlumCapRepEval=new ArrayList<Map>(), QObsYRecomXBimYMat = new ArrayList<Map>();
    //Map QLenguas = new HashMap();
    
    dr.put("tblPrincipal_selectedRow", posSelActual);
    
    try
    {
        qryIfx.conectar();
        posSelActual--;
        while ( posSelActual>=0 && QAlumCapRepEval.isEmpty() )
        {   
            QAlumCapRepEval = qryIfx.alumCaptuRepEval(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de evaluaciones
            if (QAlumCapRepEval.size()>0) {
                dr.putAll(qryIfx.getCaptuRepEval(califCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu")));
                QObsYRecomXBimYMat = qryIfx.obsvRecomXMat(numeval, ""+QAlumCapRepEval.get(0).get("idalu"), califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"), cveprograma);
            }
            
            dr.put("tblAlumCapRepEval",QAlumCapRepEval);
            dr.put("tblRecomXMat",QObsYRecomXBimYMat);            
            dr.put("tblPrincipal_selectedRow", posSelActual);
            dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
            dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
            posSelActual--;
        }
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

private void btnSiguienteGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn, String numeval, String cveprograma) 
{ 
    int numFilas;
    ArrayList<Map> QAlumCapRepEval=new ArrayList<Map>(), QObsYRecomXBimYMat = new ArrayList<Map>();
    //Map QLenguas = new HashMap();
    
    numFilas = tblPrincipal.size();
    try
    {
        qryIfx.conectar();
        posSelActual++;        
        while ( posSelActual<numFilas && QAlumCapRepEval.isEmpty()  )
        {
            QAlumCapRepEval = qryIfx.alumCaptuRepEval(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de evaluaciones
            if (QAlumCapRepEval.size()>0){
                dr.putAll(qryIfx.getCaptuRepEval(califCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu")));
                QObsYRecomXBimYMat = qryIfx.obsvRecomXMat(numeval, ""+QAlumCapRepEval.get(0).get("idalu"), califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"), cveprograma);
            }
            
            dr.put("tblAlumCapRepEval",QAlumCapRepEval);
            dr.put("tblRecomXMat",QObsYRecomXBimYMat);            
            dr.put("tblPrincipal_selectedRow", posSelActual);
            dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
            dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
            posSelActual++;
        }
        
        if ( posSelActual >= numFilas && QAlumCapRepEval.isEmpty() ) {
            qryIfx.cerrarConexion();
           btnAnteriorGpo_Click(tblPrincipal, posSelActual, califCicEscIn, numeval, cveprograma);
        }
        
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

private void btnGuardarDatosComplementarios_Click (String califCicEscIn, String tblPrincipal_idcct, String idalu,  String cvelengua, 
            String txtUsuario) //,boolean chkConlcuyo_checked, String recomendaciones, String trim1, String trim2, String trim3
    {
        boolean hacerCommit = false;
        Map isOficBimEval = new HashMap();
        
        try{
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();

            /*if (qryIfx.isConceptoTodoOficializado(tblPrincipal_idcct, califCicEscIn, "EVALUACION"))
                throw new SICEEO_Excepcion (0,"TODO_OFICIALIZADO");*/

            //qryIfx.guardarRecomendacionesEvalPrescolar (califCicEscIn, idalu, cvelengua, txtUsuario); //chkConlcuyo_checked, recomendaciones,
            qryIfx.guardarLengua (califCicEscIn, idalu, cvelengua, txtUsuario); 
            //OJO HAY QUE IMPLEMENTAR ESTO CON ALUMNO DESOFICIALIZADO
            /*isOficBimEval.put("bimOf1",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 1"));
            isOficBimEval.put("bimOf2",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 2"));
            isOficBimEval.put("bimOf3",qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, "EVALUACION 3"));
            isOficBimEval.put("alDeofB1",false);
            isOficBimEval.put("alDeofB2",false);
            isOficBimEval.put("alDeofB3",false);  */
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }        
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
