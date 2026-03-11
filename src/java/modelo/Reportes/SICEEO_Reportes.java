package modelo.Reportes;

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
    Creado el : 15/06/2017, 08:13:34 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Reportes {
    private SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    //private final ServletResponse response;
    
    private final String superUsuario, txtUsuario;
    private final boolean tienePrivilegios, esUsrAdmin;
    
    public SICEEO_Reportes (Map datosReturn, HttpServletRequest request/*, HttpServletResponse response*/)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.r = new SICEEO_HttpServletRequest(request);
        this.sesion = request.getSession(false);
        this.request = request;
        //this.response = response;
        dr.put("returnCase", 1);
        
        superUsuario = ""+sesion.getAttribute("superUsuario");
        txtUsuario = ""+sesion.getAttribute("userName");
        tienePrivilegios = ( (esUsrAdmin=(txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ"))) || superUsuario.equals("si"));
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
        if (metodo.equals("foAc"))
            FormActivate (r.gP("usuario"));
        else if (metodo.equals("ReDeEv"))
            reporteDeEvaluacion (r.gP("cicescini"), r.gP("idcct"), r.gP("cveplan"), r.gP("grado"), r.gP("grupo"));
        else if (metodo.equals("Ce"))
            certificado (r.gP("cicescini"), r.gP("cicescinilib"), r.gP("idcct"), r.gP("cveplan"), r.gP("grado"), r.gP("grupo"));
        /*else if (metodo.equals("seMeCo"))
            selMesCompl ( r.gP("cicescini"), r.gP("idcct"), r.gP("grado"), r.gP("grupo"), r.gP("mesComplem"));*/
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void FormActivate (String usuario)
    {
        Map QCiclo;
        int cicescini_aux,i=0;
        String cbxCambioDeCiclo="";
        try
        {
            qryIfx.conectar();
            dr.put ("cbxCambioDeCiclo_setVisible", this.tienePrivilegios);
            QCiclo = qryIfx.Ciclo();
            cicescini_aux = Integer.parseInt(""+QCiclo.get("cicescini"));
            if(usuario.equals("IVALLE") || usuario.equals("ELYLOPEZ") || usuario.equals("POBLETEVL") || usuario.equals("VICTORPS")
                    || usuario.equals("MRAMIREZ") || usuario.equals("HZAVALA"))
                cicescini_aux -= 8;
            if(usuario.equals("DCEPUERTO"))
                cicescini_aux -= 2;
            else cicescini_aux -= 1;            
            for(i=Integer.parseInt(""+QCiclo.get("cicescini")); i>=cicescini_aux; i--)
                cbxCambioDeCiclo += "<option value='"+i+"'>"+i+" - "+(i+1)+"</option>";
            dr.put ("cbxCambioDeCiclo", cbxCambioDeCiclo);
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }       
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
        //finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void reporteDeEvaluacion (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        Map QCiclo;        
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            
            //Si no es de privilegios, todos debe estar en regla para que el sistema no lo bloquee
            if (!tienePrivilegios && !qryIfx.isCalEvalGradoGrupoOficSinAlusDesofic(tblPrincipal_idcct, tblPrincipal_cicescini, tblPrincipal_grado, tblPrincipal_grupo, (tblPrincipal_cveplan.equals("3"))?"EVALUACION":"CALIFS BIM"))
                throw new SICEEO_Excepcion (0,"FALTA_OFIC_GRADOGRUPO",(tblPrincipal_cveplan.equals("3") ? "Reporte de Evaluación" : "Boleta de Evaluación"), "evaluaciones");
                //throw new SICEEO_Excepcion (0,"FALTA_OFIC_GRADOGRUPO","Reporte de Evaluación", tblPrincipal_cveplan.equals("3")?"evaluaciones":"calificaciones bimestrales");
            
            QCiclo = qryIfx.Ciclo();
            if (!QCiclo.get("cicescini").equals(tblPrincipal_cicescini) && !tienePrivilegios)
                mensaje.Reportes("CICLO_NO_PERMITIDO", "", "", dr);            
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  
            this.dr.put("returnCase",ex.getNumError());  
            mensaje.Reportes(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  
        }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void certificado (String tblPrincipal_cicescini, String tblPrincipal_cicescinilib, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        //Map QCiclo;
        boolean validar;
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();
            
            //Si no es de privilegios, todos debe estar en regla para que el sistema no lo bloquee
            validar = qryIfx.isCalEvalGradoGrupoOficSinAlusDesofic(tblPrincipal_idcct, tblPrincipal_cicescini, tblPrincipal_grado, tblPrincipal_grupo, (tblPrincipal_cveplan.equals("3"))?"EVALUACION":"CALIFS BIM");
            if (!tienePrivilegios && !validar)
                throw new SICEEO_Excepcion (0,"FALTA_OFIC_GRADOGRUPO","certificado","evaluaciones");
                //throw new SICEEO_Excepcion (0,"FALTA_OFIC_GRADOGRUPO","certificado",tblPrincipal_cveplan.equals("3")?"evaluaciones":"calificaciones bimestrales");                
            else if(!qryIfx.isFirmaElecAlu(tblPrincipal_idcct, tblPrincipal_cicescini, tblPrincipal_cicescinilib, tblPrincipal_grado, tblPrincipal_grupo))
                throw new SICEEO_Excepcion (0,"FALTA_FIRMA","certificado","");
            //QCiclo = qryIfx.Ciclo();
            //if (!QCiclo.get("cicescini").equals(tblPrincipal_cicescini) && !tienePrivilegios)
            //    mensaje.Reportes("CICLO_NO_PERMITIDO", "", "", dr);
            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  
            this.dr.put("returnCase",ex.getNumError());  
            mensaje.Reportes(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  
        }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void isCalEvalGradoGrupoOficializado (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, 
            String tblPrincipal_cveplan, String nombreReporte, SICEEO_QueriesInformix qryIfx2)
    {
        Map QCiclo;
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            if (qryIfx2 == null)
                qryIfx.conectar();
            else
                qryIfx = qryIfx2;
            
            QCiclo = qryIfx.Ciclo();   
            
            if((""+QCiclo.get("cicescini")).equals(tblPrincipal_cicescini) &&
                !tienePrivilegios && !qryIfx.isCalEvalGradoGrupoOficSinAlusDesofic(tblPrincipal_idcct, tblPrincipal_cicescini, tblPrincipal_grado, tblPrincipal_grupo, (tblPrincipal_cveplan.equals("3"))?"EVALUACION":"CALIFS BIM"))            
                throw new SICEEO_Excepcion (0,"FALTA_OFIC_GRADOGRUPO",nombreReporte, tblPrincipal_cveplan.equals("3")?"evaluaciones":"calificaciones bimestrales");            
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  
            this.dr.put("returnCase",ex.getNumError());  
            mensaje.Reportes(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  
        }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { if (qryIfx2 == null) qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void getPaqueteDeMaterias (Map parameters, String cicescini, String modalidad, String cveplan, String grado, SICEEO_QueriesInformix qryIfx2) throws ClassNotFoundException, SQLException
    {
        ArrayList<Map> paqueteDeMaterias;
        int numMats, i, numObjsEnReporte=10;         //tenia 9                 //Ojo: Verificar el número en numObjsEnReporte, ya que es la cantidad de objetos estáticos en el reporte
        String subquery="";
        
        try{
            if (qryIfx2 == null)
                qryIfx.conectar();
            else
                qryIfx = qryIfx2;
            paqueteDeMaterias = qryIfx.getPaqueteDeMateriasDeCiclo (cicescini, modalidad, cveplan, grado);

            numMats = paqueteDeMaterias.size();

            for (i=0; i<numMats; i++)
            {   
                parameters.put("nombreMat"+(i+1), ""+paqueteDeMaterias.get(i).get("desmat"));
                
                subquery += "( SELECT (case when califant>0.0 then califant else promedio end) FROM alumnomaterias WHERE idalu=a.idalu AND cicescini=g.cicescini AND cveplan=g.cveplan AND grado=g.grado AND "
                            + "cvetipmat='"+paqueteDeMaterias.get(i).get("cvetipmat")+"'";
                if ("CBA, LEX, FA, DPS, CF1, CF2, CF3, CF4".contains(""+paqueteDeMaterias.get(i).get("cvetipmat")))
                    subquery += " AND cvemat='"+paqueteDeMaterias.get(i).get("cvemat")+"'";

                subquery += " ) AS prommat"+(i+1);
                subquery += (i < numMats-1)?", ":" ";
            }

            //Si las materias no fueron el total especificados en numObjsEnReporte, entonces terminamos de rellenar
            if (i < numObjsEnReporte)
                for (;i<numObjsEnReporte; i++){
                    parameters.put("nombreMat"+(i+1), "mat"+(i+1));
                    subquery += ", -1 AS prommat"+(i+1);
                }

            parameters.put("sqryMaterias", subquery);
        }finally { try { if (qryIfx2 == null) qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void getPaqueteDeMateriasRa (Map parameters, String cicescini, String modalidad, String cveplan, String grado, SICEEO_QueriesInformix qryIfx2) throws ClassNotFoundException, SQLException
    {
        ArrayList<Map> paqueteDeMaterias;
        int numMats, i, numObjsEnReporte=10;         //tenia 9                 //Ojo: Verificar el número en numObjsEnReporte, ya que es la cantidad de objetos estáticos en el reporte
        String subquery="";
        
        try{
            if (qryIfx2 == null)
                qryIfx.conectar();
            else
                qryIfx = qryIfx2;
            paqueteDeMaterias = qryIfx.getPaqueteDeMateriasDeCiclo (cicescini, modalidad, cveplan, grado);

            numMats = paqueteDeMaterias.size();

            for (i=0; i<numMats; i++)
            {   
                parameters.put("nombreMat"+(i+1), ""+paqueteDeMaterias.get(i).get("desmat"));
                // Para la primera evaluación 
                subquery += "( SELECT calif1 FROM evaluaciones WHERE idalu=a.idalu AND cicescini=g.cicescini AND cveplan=g.cveplan AND grado=g.grado AND "
                            + "cvetipmat='"+paqueteDeMaterias.get(i).get("cvetipmat")+"'";
                if ("CBA, LEX, FA, DPS, CF1, CF2, CF3, CF4".contains(""+paqueteDeMaterias.get(i).get("cvetipmat")))
                    subquery += " AND cvemat='"+paqueteDeMaterias.get(i).get("cvemat")+"'";

                subquery += " AND numeval=1 ) AS Mat"+(i+1)+"Eval1, ";
                
                //Para la segunda evaluacion
                subquery += "( SELECT calif1 FROM evaluaciones WHERE idalu=a.idalu AND cicescini=g.cicescini AND cveplan=g.cveplan AND grado=g.grado AND "
                            + "cvetipmat='"+paqueteDeMaterias.get(i).get("cvetipmat")+"'";
                if ("CBA, LEX, FA, DPS, CF1, CF2, CF3, CF4".contains(""+paqueteDeMaterias.get(i).get("cvetipmat")))
                    subquery += " AND cvemat='"+paqueteDeMaterias.get(i).get("cvemat")+"'";

                subquery += " AND numeval=2 ) AS Mat"+(i+1)+"Eval2, ";
                
                //Para la tercera evaluacion
                subquery += "( SELECT calif1 FROM evaluaciones WHERE idalu=a.idalu AND cicescini=g.cicescini AND cveplan=g.cveplan AND grado=g.grado AND "
                            + "cvetipmat='"+paqueteDeMaterias.get(i).get("cvetipmat")+"'";
                if ("CBA, LEX, FA, DPS, CF1, CF2, CF3, CF4".contains(""+paqueteDeMaterias.get(i).get("cvetipmat")))
                    subquery += " AND cvemat='"+paqueteDeMaterias.get(i).get("cvemat")+"'";

                subquery += " AND numeval=3 ) AS Mat"+(i+1)+"Eval3 ";
                
                                                
                subquery += (i < numMats-1)?", ":" ";
            }

            //Si las materias no fueron el total especificados en numObjsEnReporte, entonces terminamos de rellenar
            if (i < numObjsEnReporte)
                for (;i<numObjsEnReporte; i++){
                    parameters.put("nombreMat"+(i+1), "mat"+(i+1));
                    subquery += ", -1 AS prommat"+(i+1);
                }

            parameters.put("sqryMaterias", subquery);
        }finally { try { if (qryIfx2 == null) qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void isAluConExmExtOfic (Map parameters, String cicescini, String cveplan, String grado, SICEEO_QueriesInformix qryIfx2) throws SQLException
    {
        ArrayList<Map> paqueteDeMaterias;
        int numMats, i, numObjsEnReporte=10;         //tenia 9                 //Ojo: Verificar el número en numObjsEnReporte, ya que es la cantidad de objetos estáticos en el reporte
        String subquery="";
        
        try{
            if (qryIfx2 == null)
                qryIfx.conectar();
            else
                qryIfx = qryIfx2;
            paqueteDeMaterias = qryIfx.getPaqueteDeMateriasDeCiclo (cicescini, "", cveplan, grado);

            numMats = paqueteDeMaterias.size();

            for (i=0; i<numMats; i++)
            {   
                parameters.put("nombreMat"+(i+1), ""+paqueteDeMaterias.get(i).get("desmat"));
                
                subquery += "( SELECT (case when califant>0.0 then califant else promedio end) FROM alumnomaterias WHERE idalu=a.idalu AND cicescini=g.cicescini AND cveplan=g.cveplan AND grado=g.grado AND "
                            + "cvetipmat='"+paqueteDeMaterias.get(i).get("cvetipmat")+"'";
                if ("CBA, LEX, FA, DPS, CF1, CF2, CF3, CF4".contains(""+paqueteDeMaterias.get(i).get("cvetipmat")))
                    subquery += " AND cvemat='"+paqueteDeMaterias.get(i).get("cvemat")+"'";

                subquery += " ) AS prommat"+(i+1);
                subquery += (i < numMats-1)?", ":" ";
            }

            //Si las materias no fueron el total especificados en numObjsEnReporte, entonces terminamos de rellenar
            if (i < numObjsEnReporte)
                for (;i<numObjsEnReporte; i++){
                    parameters.put("nombreMat"+(i+1), "mat"+(i+1));
                    subquery += ", -1 AS prommat"+(i+1);
                }

            parameters.put("sqryMaterias", subquery);
        }finally { try { if (qryIfx2 == null) qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void isAluConExmExtOfCons (Map parameters, String cicescini, String cveplan, String grado, SICEEO_QueriesInformix qryIfx2) throws SQLException
    {
        ArrayList<Map> paqueteDeMaterias;
        int numMats, i, numObjsEnReporte=10;         //tenia 9                 //Ojo: Verificar el número en numObjsEnReporte, ya que es la cantidad de objetos estáticos en el reporte
        String subquery="";
        
        try{
            if (qryIfx2 == null)
                qryIfx.conectar();
            else
                qryIfx = qryIfx2;
            
             parameters.put("sqryMaterias", subquery);
        }finally { try { if (qryIfx2 == null) qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    public void selMesCompl (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String idperexmext, 
            String idalusPorAgregar, String idalusPorFiltrar, SICEEO_QueriesInformix qryIfx2)
    {
        String idalusMesCompl="";
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            if (!idalusPorAgregar.equals("") && !idalusPorAgregar.equals("null") && !tienePrivilegios)
                throw new SICEEO_Excepcion(0, "USUARIO_REESTRINGIDO");
            //qryIfx.conectar();
            idalusMesCompl = qryIfx2.getIdalusConExtraordinarioAprobado(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idperexmext, idalusPorFiltrar);
            
            if (!idalusMesCompl.equals("") && !idalusPorAgregar.equals("") && !idalusPorAgregar.equals("null"))
                idalusMesCompl += ",";
            if (!idalusPorAgregar.equals("null"))
                idalusMesCompl += idalusPorAgregar;
            
            dr.put("idalus", idalusMesCompl);
            if (dr.get("idalus").equals(""))
                throw new SICEEO_Excepcion(0,"SIN_ALU_REPS");
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Reportes(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr); }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        //finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
