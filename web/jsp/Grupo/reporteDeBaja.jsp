<%-- 
    Document   : Kardex
    Created on : 03-Jul-2015, 15:35:18
    Author     : Ing. Maai Nolasco Sánchez
--%>


<%@page import="java.util.ArrayList"%>
<%@page import="modelo.Grupo.SICEEO_CambioDeGrupo"%>
<%@page import="net.sf.jasperreports.engine.JREmptyDataSource"%>
<%@page import="net.sf.jasperreports.engine.JRException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.File"%>
<%@page import="modelo.ClasesGlobales.SICEEO_DataModule"%>
<%@page import="modelo.DAO.SICEEO_QueriesInformix"%>
<%@page import="modelo.Preinscripcion.SICEEO_Preinscripcion"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <title>Reporte de Inscripción</title>
        <link rel="stylesheet" href="../../estilos/libs/normalize.css" />
   
<%
    /********** DECLARACIÓN DE VARIABLES **************/
    SICEEO_QueriesInformix qryIfx=null;
    SICEEO_CambioDeGrupo camDeGpo = new SICEEO_CambioDeGrupo(null, request);
    String r="Grupo/constanciaBaja/", cicescini,cicescinilib, cveplan, idcct, grupo,idalus;
    int grado;
    String nivel[]=new String[]{"","Pri","Sec","Pre"};
    Map parameters = new HashMap();
    File reportFile;    
    
    try {
        /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
        HttpSession sesionOk = request.getSession(false);
        if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !camDeGpo.ejecutarPeticion("peVeRe") )
            throw new Exception ("SIN_SESION");

        /********** ASIGNACIÓN DE VARIABLES **************/
        cicescini = request.getParameter("cicescini");
        cicescinilib = request.getParameter("cicescinilib");
        cveplan = request.getParameter("cveplan");
        idcct = request.getParameter("idcct");
        grado = Integer.parseInt(""+request.getParameter("grado"));
        grupo = request.getParameter("grupo");        
        idalus = request.getParameter("idalus"); 
        
        r+=cicescini+"/constanciaBajaTraslado";

        parameters.put("cicescini", cicescini);
        parameters.put("cicescinilib", cicescinilib);
        parameters.put("cveplan", cveplan);
        parameters.put("idcct", idcct);
        parameters.put("grado", grado);
        parameters.put("grupo", grupo);
        parameters.put("strQry", idalus.length()>0 ? " AND a.idalu IN ("+idalus.substring(0, idalus.length()-1)+") ": "");
        
        qryIfx = new SICEEO_QueriesInformix();
        qryIfx.conectar();

        /******** SE CREA EL DOCUMENTO PDF Y SE LE INSERTAN LOS DATOS **************/
        reportFile = new File(application.getRealPath("")+"/../reportes/siceeo/"+r+".jasper");
        byte[] bytes =JasperRunManager.runReportToPdf(reportFile.getPath(),parameters, qryIfx.getConexion());
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
        if (ex!=null && ex.getMessage()!=null && ex.getMessage().equals("SIN_SESION")){
            response.sendRedirect("../../cerrarSesion.jsp");
        }else
            out.print("ERROR: "+ex);  
    }
    finally { try { qryIfx.cerrarConexion(); } catch(Exception ex){ } }

%>
    </head>
    <body>
    </body>
</html>