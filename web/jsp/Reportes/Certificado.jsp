<%-- 
    Document   : Certificado
    Created on : 14-jul-2017, 16:53:33
    Author     : maai
--%>

<%@page import="net.sf.jasperreports.engine.JasperPrintManager"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@page import="com.lowagie.text.pdf.PdfAction"%>
<%@page import="com.lowagie.text.pdf.PdfWriter"%>
<%@page import="com.lowagie.text.pdf.PdfStamper"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="com.lowagie.text.pdf.PdfReader"%>
<%@page import="net.sf.jasperreports.engine.JRException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="modelo.Reportes.SICEEO_Reportes"%>
<%@page import="java.io.File"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="modelo.ClasesGlobales.SICEEO_DataModule"%>
<%@page import="modelo.DAO.SICEEO_QueriesInformix"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>t
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <!--meta http-equiv="Content-Type" content="text/html; charset=UTF-8"-->
        <title>Certificado</title>
<%
    /********** DECLARACIÓN DE VARIABLES **************/
    SICEEO_QueriesInformix qryIfx=null;
    SICEEO_DataModule dm = new SICEEO_DataModule();
    Map dr = new HashMap();
    String r="Reportes/Certificado/", cveplan, idcct, grado, grupo, idalus,cicescinilib;
    int cicescini;
    Map parameters = new HashMap();
    File reportFile;
    
    try {
        /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
        HttpSession sesionOk = request.getSession(false);
        if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !dm.permisoVerReportes(request) )
            throw new Exception ("SIN_SESION");
        sesionOk.setAttribute("modulo", "RepCer");
        
        SICEEO_Reportes re = new SICEEO_Reportes(dr, request);
        re.ejecutarPeticion("Ce");   
        r+= "certificadoSemielectronico_"+request.getParameter("cicescinilib")+"";
        
                
        if (!dr.get("returnCase").equals(1))
            throw new Exception (""+dr.get("mensaje")); //OJO - Comentado para prueba hoy 01-07-2025
       
        /********** ASIGNACIÓN DE VARIABLES **************/
        /*parameters.put("cicescinilib", 0 ); 
        parameters.put("idcct", 0 );  
        parameters.put("grado", 0 );  
        parameters.put("grupo", "");  
        parameters.put("idalus", 0);       
        parameters.put("sqryIdalus", "");  */
        
        cicescinilib = ""+request.getParameter("cicescinilib");
        if(cicescinilib.length() == 0) {
            parameters.put("cicescinilib", dm.toInt(request.getParameter("cicescini")));
        }
        else 
            parameters.put("cicescinilib", dm.toInt(request.getParameter("cicescinilib")));
        
        if((idcct=request.getParameter("idcct_c"))==null)
            idcct=request.getParameter("idcct");
        
        parameters.put("cicescini", cicescini=dm.toInt(request.getParameter("cicescini")) );
        parameters.put("idcct", idcct );
        parameters.put("grado", grado=request.getParameter("grado") );
        parameters.put("grupo", grupo=request.getParameter("grupo") );
        parameters.put("cveplan", cveplan=request.getParameter("cveplan") );
        parameters.put("idalus", request.getParameter("idalus"));
        parameters.put("sqryIdalus", (idalus=request.getParameter("idalus"))==null ? "" : "AND fi.idalu IN ("+idalus+")" ); 
        
        
        if(cicescinilib.equals("2024")){  //Agregado para prueba 2024-2025
            r+="_p2425";            
        }
        
        qryIfx = new SICEEO_QueriesInformix();
        qryIfx.conectar();
        
        /******** SE CREA EL DOCUMENTO PDF Y SE LE INSERTAN LOS DATOS **************/
        reportFile = new File(application.getRealPath("")+"/../reportes/siceeo/"+r+".jasper");
        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,qryIfx.getConexion()); 
        
        /******** SE MUESTRA EL DOCUMENTO **************/
        response.setContentType("application/pdf"); 
        response.setContentLength(bytes.length); 
        ServletOutputStream ouputStream = response.getOutputStream(); 
        ouputStream.write(bytes, 0, bytes.length); 
        ouputStream.flush(); 
        ouputStream.close();
        
        //opm String report = ""+application.getRealPath("")+"/../reportes/siceeo/"+r+".jasper";
        //opm JasperPrint certi = JasperFillManager.fillReport(report, parameters, qryIfx.getConexion());
        //opm JasperPrintManager.printReport(certi, true);
        
        /*PdfReader reader = new PdfReader(bytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, baos);
        PdfWriter writer = stamper.getWriter();
        PdfAction action = new PdfAction(PdfAction.PRINTDIALOG);
        writer.setOpenAction(action);
        stamper.close();*/
    }catch(SQLException ex){
        if ( ex==null || ex.getMessage()==null)
            out.print("ERROR DE CONSULTA: "+ex+". ");
        else 
            out.print("PROBLEMA EN CONSULTA: "+ex.getMessage()); 
    } catch(JRException ex){
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
            out.print(" "+ex.getMessage());  
    }
    finally { try { qryIfx.cerrarConexion(); } catch(Exception ex){ } }
%>
    </head>
    <body>
    </body>
</html>
