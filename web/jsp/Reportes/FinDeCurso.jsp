<%-- 
    Document   : FinDeCurso
    Created on : 16-jul-2017, 21:03:50
    Author     : maai
--%>

<%@page import="net.sf.jasperreports.engine.JRException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="modelo.Reportes.SICEEO_Reportes"%>
<%@page import="java.io.File"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="modelo.ClasesGlobales.SICEEO_DataModule"%>
<%@page import="modelo.DAO.SICEEO_QueriesInformix"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <!--meta http-equiv="Content-Type" content="text/html; charset=UTF-8"-->
        <title>Fin de curso</title>
<%
    /********** DECLARACIÓN DE VARIABLES **************/
    SICEEO_QueriesInformix qryIfx=null;
    SICEEO_DataModule dm = new SICEEO_DataModule();
    Map dr = new HashMap();
    String r="Reportes/FinDeCurso/", cicescini, cicescinilib,modalidad, cveplan, idcct, grado, grupo, caso;
    Map parameters = new HashMap();
    int cicescinilib_int = Integer.parseInt(""+request.getParameter("cicescinilib"));
    File reportFile;
    
    try{
        /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
        HttpSession sesionOk = request.getSession(false);
        if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !dm.permisoVerReportes(request) )
            throw new Exception ("SIN_SESION");
        
        /********** ASIGNACIÓN DE VARIABLES **************/
        modalidad=request.getParameter("caso").equals("Ra") ? request.getParameter("cct").substring(2, 5) : request.getParameter("modalidad");
        parameters.put("modalidad", modalidad);
        cicescini=request.getParameter("cicescini");
        
        if(modalidad.equals("DBA"))
            cicescini = ""+(Integer.parseInt(cicescini)+1);
        
        parameters.put("cicescini", cicescini );
        parameters.put("cicescinilib", cicescinilib=request.getParameter("cicescinilib") );
        parameters.put("cveplan", cveplan=request.getParameter("cveplan") );        
        parameters.put("idcct", idcct=request.getParameter("idcct") );
        parameters.put("grado", grado=request.getParameter("grado") );
        parameters.put("grupo", grupo=request.getParameter("grupo") );
        caso=request.getParameter("caso");
        parameters.put("isComplem", caso.equals("IAEc") || caso.equals("CRELc") || caso.equals("RELc") || caso.equals("Rc") );
        parameters.put("sqryIdalusCompl", caso.equals("IAEc") || caso.equals("CRELc") || caso.equals("RELc") || caso.equals("Rc")?" AND g.idalu IN ("+request.getParameter("idalusCompl")+")":"" );
        parameters.put("mesComplem", caso.equals("RELc")? request.getParameter("mesComplem"):"" );
        //parameters.put("tipo_comp", caso.equals("RELc")? request.getParameter("tipo_comp"):"" );
        sesionOk.setAttribute("modulo", "Rep"+caso);
        parameters.put("sqryTipoCompl", "");
        
        
        /* ********************* CONEXION A LA BASE DE DATOS ******************** */
        qryIfx = new SICEEO_QueriesInformix();
        qryIfx.conectar();
        
        /* *********************** PROCESAMIENTO DE DATOS *********************** */
        SICEEO_Reportes re = new SICEEO_Reportes(dr, request);
        re.isCalEvalGradoGrupoOficializado (cicescini, idcct, grado, grupo, cveplan,"reporte", qryIfx );
        if (!dr.get("returnCase").equals(1) && !caso.equals("Ra"))  // caso=Ra para reporte de avances 2023-2024
                throw new Exception (""+dr.get("mensaje"));
        
        if (caso.equals("IAE") || caso.equals("IAEc") || caso.equals("R") || caso.equals("Rc")){
            re.getPaqueteDeMaterias (parameters, cicescinilib, modalidad, cveplan, grado, qryIfx);
            if (!dr.get("returnCase").equals(1) )
                    throw new Exception (""+dr.get("mensaje"));            
        }else if (caso.equals("RELc")){                        
            if((""+request.getParameter("tipo_comp")).equals("c_PR"))
                parameters.put("sqryTipoCompl", " AND g.promovido='PR'");
            else {
                if(cicescinilib_int < Integer.parseInt(""+request.getParameter("cicescini_act")))
                    parameters.put("cicescini_reg", Integer.parseInt(""+request.getParameter("cicescini_act")));
                else
                    parameters.put("cicescini_reg",cicescinilib_int);
                re.selMesCompl (cicescinilib, idcct, grado, grupo, ""+request.getParameter("mesComplem"), ""+request.getParameter("idalus"), ""+request.getParameter("idExalusCompl"), qryIfx);
                if (!dr.get("returnCase").equals(1))
                    throw new Exception (""+dr.get("mensaje"));            
                parameters.put("sqryIdalusCompl", " AND g.idalu IN ("+dr.get("idalus")+")");
            }    
        } else if(caso.equals("Ra")){
            re.getPaqueteDeMateriasRa (parameters, cicescinilib, modalidad, cveplan, grado, qryIfx);
        }
        
        
        r += ""+cicescinilib+"_p/";
        /******** SE CREA EL DOCUMENTO PDF Y SE LE INSERTAN LOS DATOS **************/        
        //r += ""+(caso.equals("REL") || caso.equals("RELc") ? "_p/" : "/");
        /*if(cveplan.equals("1") && cicescini.equals("2023") && (caso.equals("CREL") || caso.equals("CRELc")))
            r+="__123789978";*/
        
        
        if (caso.equals("IAR"))
            r+="IARPre";
        else if (caso.equals("IAE") || caso.equals("IAEc")){
            if(Integer.parseInt(cicescinilib) < 2023 ) {
                if (grado.equals("1") || grado.equals("2"))
                    r += (modalidad.equals("DPB") || modalidad.equals("DCI"))?"IAEPrimIndigG1-2":"IAEPrimG1-2";
                else if (grado.equals("3"))
                    r += (modalidad.equals("DPB") || modalidad.equals("DCI"))?"IAEPrimIndigG3":"IAEPrimG3";
                else if (grado.equals("4") || grado.equals("5") || grado.equals("6"))
                    r += (modalidad.equals("DPB") || modalidad.equals("DCI"))?"IAEPrimIndigG4-6":"IAEPrimG4-6";
            } else 
                r += "IAEPrimG1-6";
        }else if (caso.equals("CREL") || caso.equals("CRELc"))
            r+="CRELPrim";
        else if (caso.equals("R") || caso.equals("Rc")) 
            r+="RSecG"+grado;        
        else if (caso.equals("REL") || caso.equals("RELc"))
            r+="RELSec";
        else if (caso.equals("RELP") && grado.equals("3"))
            r+="RELPPrees"; 
        else if (caso.equals("Ra")){  // Para reporte de avances 2023-2024
            parameters.put("str_of", ""+dr.get("returnCase"));
            if(cveplan.equals("1") ) {                
                r+= "RepAvaPrimG1-6";
            } else {
                if(grado.equals("2") || grado.equals("3"))
                    r+="RepAvaSecG2-3";     
                else 
                    r+="RepAvaSecG1";     
            }
        }

        reportFile = new File(application.getRealPath("")+"/../reportes/siceeo/"+r+".jasper");
        byte[] bytes =JasperRunManager.runReportToPdf(reportFile.getPath(),parameters, /*new JREmptyDataSource()*/qryIfx.getConexion());
        /******** SE MUESTRA EL DOCUMENTO **************/
        response.setContentType("application/pdf"); 
        response.setContentLength(bytes.length); 
        ServletOutputStream ouputStream = response.getOutputStream(); 
        ouputStream.write(bytes, 0, bytes.length); 
        ouputStream.flush(); 
        ouputStream.close();
    }catch(SQLException ex){
        if ( ex==null || ex.getMessage()==null)
            out.print("ERROR DE CONSULTA: "+ex+". ");
        else 
            out.print("PROBLEMA EN CONSULTA: "+ex.getMessage()); 
    }catch(JRException ex){
        if ( ex==null || ex.getMessage()==null)
            out.print("ERROR EN REPORTE: "+ex+". ");
        else if (ex.getMessage().contains("jdbc:informix-sqli:"))
            out.print("ERROR EN REPORTE: Error evaluating expression : Source text : java.sql.DriverManager.getConnection(jdbc:informix-sqli://...)");
        else if (ex.getMessage().contains("FileNotFoundException"))
            out.print("ERROR EN REPORTE: No se encontró el archivo de reporte.");
        else if (ex.getMessage().toUpperCase().contains("SELECT") || ex.getMessage().toUpperCase().contains("INSERT") || ex.getMessage().toUpperCase().contains("UPDATE") || ex.getMessage().toUpperCase().contains("FROM"))
            out.print("ERROR EN REPORTE: La consulta a los datos tiene inconsistencias.");
        else
            out.print("ERROR EN REPORTE: "+ex.getMessage()+". ");  
    }
    catch(Exception ex){
        if ( ex==null || ex.getMessage()==null)
            out.print("ERROR: "+ex);
        else if (ex.getMessage().equals("SIN_SESION"))
            response.sendRedirect("../../cerrarSesion.jsp");
        else
            out.print("ERROR: "+ex.getMessage());  
    }
    finally { try { qryIfx.cerrarConexion(); } catch(Exception ex){ } }
%>
    </head>
    <body>
    </body>
</html>
