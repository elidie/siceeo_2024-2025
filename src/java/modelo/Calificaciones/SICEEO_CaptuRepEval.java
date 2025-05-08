package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
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
            formActivate ( r.gP("califCicEscIn"), ""+sesion.getAttribute("userName"), r.gP("cicescin") );
            FormCreate (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));
        }else if (metodo.equals("btGdCaReEv_Cl"))
            btnGuardarCapRepEval_Click (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("idalu"),
                        r.gP("cveplan"),r.gP("cvelengua"),r.gP("chkRiesgosAlerta1"), r.gP("chkRiesgosAlerta2"), r.gP("chkRiesgosAlerta3"), r.gP("tutoria"), 
                        r.gP("txtaHabEscritura"), r.gP("txtaHabLectura"),r.gP("txtaHabMatematica"), 
                        dm.vstrToArrMap(r.gPV("chkHabEscritura"), "~", new String[]{"1","2","3","4","5"}),
                        dm.vstrToArrMap(r.gPV("chkHabLectura"), "~", new String[]{"1","2","3","4","5"}),
                        dm.vstrToArrMap(r.gPV("chkHabMatematica"), "~", new String[]{"1","2","3","4","5"}),
                        dm.vstrToArrMap(r.gPV("tblObsYRecomXBimYAsig"), "~", new String[]{"bimestre","cvetipmat","cvemat","obserEsp","apoyo","cvetipmat_oldValue","cvemat_oldValue"}),
                        r.gP("txtaRecomendGrales_eval1"),r.gP("txtaRecomendGrales_eval2"),r.gP("txtaRecomendGrales_eval3"), 
                        dm.vstrToArrMap(r.gPV("tblEvalLec"), "~", new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","idpregunta"})
                    );
        else if (metodo.equals("tbAlCaReEv_ChSeIt"))
            tblAlumCapRepEval_ChangeSelectedItem (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblAlumCapRepEval_idalu"));
        else if (metodo.equals("btAnGp"))
            btnAnteriorGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","cveplan"}),  dm.toInt(r.gP("posSelActual")),  r.gP("califCicEscIn"));
        else if (metodo.equals("btSiGp"))
            btnSiguienteGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","cveplan"}),  dm.toInt(r.gP("posSelActual")) ,  r.gP("califCicEscIn") ,  r.gP("numeval") );
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
        ArrayList<Map> QAlumCapRepEval, QMateriasAlumno = new ArrayList<Map>(), QObsYRecomXBimYAsig = new ArrayList<Map>(), QPreguntasCompLectora = new ArrayList<Map>();

        dr.put("paTras", "no");
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();

            QAlumCapRepEval = qryIfx.alumCaptuRepEval(CalifCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de evaluaciones
            if (QAlumCapRepEval.size()>0) {
                dr.putAll(qryIfx.getCaptuRepEval(CalifCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu")));
                //QMateriasAlumno = qryIfx.getMateriasAlumno(CalifCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu"));
                //QObsYRecomXBimYAsig = qryIfx.getTablaObsYRecomXBimYAsig (CalifCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu"));
            }
            //QPreguntasCompLectora = qryIfx.getTablaPreguntasCompLectora (tblPrincipal_cveplan, tblPrincipal_grado);
            dr.put("lenguas",qryIfx.getLenguas());
            //dr.put("obsrecgral",qryIfx.getObsRecGral(CalifCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu")));
            dr.put("tblAlumCapRepEval",QAlumCapRepEval);
            //dr.put("matsAlumno",QMateriasAlumno);
            //dr.put("tblObsYRecomXBimYAsig",QObsYRecomXBimYAsig);
            //dr.put("preguntasCompLectora",QPreguntasCompLectora);        
            
            //dr.put("capRepOf",qryIfx.isOficializado(tblPrincipal_idcct, CalifCicEscIn,"EVALUACION "+eval));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnGuardarCapRepEval_Click (String CalifCicEscIn, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String idalu,
            String cveplan,String cvelengua, String chkRiesgosAlerta1, String chkRiesgosAlerta2, String chkRiesgosAlerta3, String tutoria, 
            String txtaHabEscritura, String txtaHabLectura, String txtaHabMatematica, ArrayList<Map>chkHabEscritura, ArrayList<Map> chkHabLectura, 
            ArrayList<Map> chkHabMatematica, ArrayList<Map> tblObsYRecomXBimYAsig, String txtaRecomendGrales_eval1, String txtaRecomendGrales_eval2, 
            String txtaRecomendGrales_eval3,  ArrayList<Map> tblEvalLec)
    {
        String txtaRecomedGrales="";
        int numeval=0;
        boolean hacerCommit = false;
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();

            /*qryIfx.guardarCaptuRepEval(CalifCicEscIn, idalu, cvelengua, chkRiesgosAlerta1, chkRiesgosAlerta2, chkRiesgosAlerta3, tutoria, txtaHabEscritura, 
                    txtaHabLectura, txtaHabMatematica, chkHabEscritura,  chkHabLectura,  chkHabMatematica, tblObsYRecomXBimYAsig, 
                    txtaRecomendGrales_eval1, txtaRecomendGrales_eval2, txtaRecomendGrales_eval3, 
                    tblEvalLec, ""+sesion.getAttribute("userName"));*/
            for(int i=0; i < 3; i++){
                numeval = i+1;
                if(i==0)
                    txtaRecomedGrales = txtaRecomendGrales_eval1;                                    
                else if(i==1)
                    txtaRecomedGrales = txtaRecomendGrales_eval2;                
                else if(i==2)
                    txtaRecomedGrales = txtaRecomendGrales_eval3;
                
                qryIfx.guardarCaptuRepEval (CalifCicEscIn, cveplan, idalu,  numeval, cvelengua, txtaRecomedGrales, ""+sesion.getAttribute("userName"));
            }                        
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void tblAlumCapRepEval_ChangeSelectedItem (String CalifCicEscIn, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, 
            String tblAlumCapRepEval_idalu)
    {
        ArrayList<Map> QMateriasAlumno, QObsYRecomXBimYAsig, QPreguntasCompLectora;
        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();

            dr.putAll(qryIfx.getCaptuRepEval(CalifCicEscIn, tblAlumCapRepEval_idalu));
            /*QMateriasAlumno = qryIfx.getMateriasAlumno(CalifCicEscIn, tblAlumCapRepEval_idalu);
            //QObsYRecomXBimYAsig = qryIfx.getTablaObsYRecomXBimYAsig (CalifCicEscIn, tblAlumCapRepEval_idalu);
            //QPreguntasCompLectora = qryIfx.getTablaPreguntasCompLectora (tblPrincipal_cveplan, tblPrincipal_grado);

            dr.put("matsAlumno",QMateriasAlumno);
            dr.put("tblObsYRecomXBimYAsig",QObsYRecomXBimYAsig);
            dr.put("preguntasCompLectora",QPreguntasCompLectora);*/
            
            
            //dr.put("capRepOf",qryIfx.isOficializado(tblPrincipal_idcct, CalifCicEscIn,"EVALUACION "+eval));
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnAnteriorGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn) 
{ 
    ArrayList<Map> QAlumCapRepEval=new ArrayList<Map>(), QMateriasAlumno = new ArrayList<Map>(), QObsYRecomXBimYAsig = new ArrayList<Map>(), QPreguntasCompLectora = new ArrayList<Map>();
    //Map QLenguas = new HashMap();
    
    dr.put("tblPrincipal_selectedRow", posSelActual);
    
    try
    {
        qryIfx.conectar();
        posSelActual--;
        while ( posSelActual>=0 && QAlumCapRepEval.isEmpty() )
        {
            QAlumCapRepEval = qryIfx.alumCaptuRepEval(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de evaluaciones
            if (QAlumCapRepEval.size()>0){
                dr.putAll(qryIfx.getCaptuRepEval(califCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu")));
                //QMateriasAlumno = qryIfx.getMateriasAlumno(califCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu"));
                //QObsYRecomXBimYAsig = qryIfx.getTablaObsYRecomXBimYAsig (califCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu"));
                //QPreguntasCompLectora = qryIfx.getTablaPreguntasCompLectora (""+tblPrincipal.get(posSelActual).get("cveplan"), ""+tblPrincipal.get(posSelActual).get("grado"));
                //QLenguas = qryIfx.getLenguas();
            }
            
            //dr.put("lenguas",QLenguas);
            dr.put("tblAlumCapRepEval",QAlumCapRepEval);
            /*
            dr.put("matsAlumno",QMateriasAlumno);
            dr.put("tblObsYRecomXBimYAsig",QObsYRecomXBimYAsig);
            dr.put("preguntasCompLectora",QPreguntasCompLectora);*/
            
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
    ArrayList<Map> QAlumCapRepEval=new ArrayList<Map>(), QMateriasAlumno = new ArrayList<Map>(), QObsYRecomXBimYAsig = new ArrayList<Map>(), QPreguntasCompLectora = new ArrayList<Map>();
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
                //QMateriasAlumno = qryIfx.getMateriasAlumno(califCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu"));
                //QObsYRecomXBimYAsig = qryIfx.getTablaObsYRecomXBimYAsig (califCicEscIn, ""+QAlumCapRepEval.get(0).get("idalu"));
                //QPreguntasCompLectora = qryIfx.getTablaPreguntasCompLectora (""+tblPrincipal.get(posSelActual).get("cveplan"), ""+tblPrincipal.get(posSelActual).get("grado"));
                //QLenguas = qryIfx.getLenguas();
            }
            
            //dr.put("lenguas",QLenguas);
            dr.put("tblAlumCapRepEval",QAlumCapRepEval);
            //dr.put("matsAlumno",QMateriasAlumno);
            //dr.put("tblObsYRecomXBimYAsig",QObsYRecomXBimYAsig);
            //dr.put("preguntasCompLectora",QPreguntasCompLectora);
            
            dr.put("tblPrincipal_selectedRow", posSelActual);
            dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
            dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
            posSelActual++;
        }
        
        if ( posSelActual >= numFilas && QAlumCapRepEval.isEmpty() ) {
            qryIfx.cerrarConexion();
           btnAnteriorGpo_Click(tblPrincipal, posSelActual, califCicEscIn);
        }
        
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}
}
