<%-- 
    Document   : Constancias
    Created on : 22/08/2025, 11:17:16 AM
    Author     : Eli
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
        <title>Constancias</title>
<%
    SICEEO_QueriesInformix qryIfx=null;
    SICEEO_DataModule dm = new SICEEO_DataModule();
    Map dr = new HashMap();
    String r="Reportes/Constancias/", cicescini, cicescinilib, cicescini_ex, cicescini_rep, cveplan, idcct, grado, caso;
    Map parameters = new HashMap();
    
    File reportFile;
    boolean str_of= false;
    
    try{
        /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
        HttpSession sesionOk = request.getSession(false);
        if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !dm.permisoVerReportes(request) )
            throw new Exception ("SIN_SESION");
        
        /********** ASIGNACIÓN DE VARIABLES **************/        
        cicescini=request.getParameter("cicescini");                
        cicescini_ex = request.getParameter("cicescini_ex");
        cicescinilib=request.getParameter("cicescinilib");
        parameters.put("cveplan", cveplan=request.getParameter("cveplan") );        
        parameters.put("idcct", idcct=request.getParameter("idcct") );
        parameters.put("idperexmext", request.getParameter("idperexmext"));
        parameters.put("cicescini_ex", cicescini_ex);
        
        parameters.put("grado", request.getParameter("grado"));
        parameters.put("grupo", request.getParameter("grupo"));
        
        
        
        caso=request.getParameter("caso");        
        sesionOk.setAttribute("modulo", "Rep"+caso);
        /*cicescini_rep = ""+(caso.equals("EER") ? 
            (Integer.parseInt(cicescini)==Integer.parseInt(cicescini_act) ? Integer.parseInt(cicescini)-1 : cicescini): cicescini);
        */
        /*parameters.put("cicescini", cicescini_rep);*/
        if(caso.equals("EER") || caso.equals("EERc")){
            parameters.put("sqryIdalusCons",  request.getParameter("idalusCons").length()>0 ?
                    " AND fo.idalu IN ("+request.getParameter("idalusCons")+")" :
                    " AND ag.grado="+ request.getParameter("grado") + " AND ag.grupo='"+request.getParameter("grupo")+"' ");
            if(caso.equals("EERc")){        
                cicescinilib = cicescini;
                cicescini = cicescini_ex;
            }
        }
        parameters.put("cicescini", cicescini);
        parameters.put("cicescinilib", cicescinilib);
        /* ********************* CONEXION A LA BASE DE DATOS ******************** */
        qryIfx = new SICEEO_QueriesInformix();
        qryIfx.conectar();
        
        /************************* PROCESAMIENTO DE DATOS ****************************/
        SICEEO_Reportes re = new SICEEO_Reportes(dr, request);
        /******* Verificar si existen Alumnos con Examenes Extraordinarios ***********/
        re.isAluConExmExtOfCons(parameters, cicescini, cveplan, "", qryIfx);
        r += ""+cicescinilib+"/";
        /******** SE CREA EL DOCUMENTO PDF Y SE LE INSERTAN LOS DATOS **************/        
        
        if (caso.equals("EER") || caso.equals("EERc"))
            r+="constanciaExRegularizacion";        

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
