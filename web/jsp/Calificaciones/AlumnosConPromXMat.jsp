<%-- 
    Document   : alumnosConPromPorMat
    Created on : 09-sep-2016, 11:52:02
    Author     : maai
--%>

<%@page import="modelo.Calificaciones.SICEEO_CalifSecXBim"%>
<%@page import="net.sf.jasperreports.engine.JREmptyDataSource"%>
<%@page import="net.sf.jasperreports.engine.JRException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.File"%>

<%@page import="modelo.DAO.SICEEO_QueriesInformix"%>
<%@page import="modelo.Calificaciones.SICEEO_AlumnosConPromXMat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alumnos con prom x mat</title>
    </head>
    <body>
<%
    /********** DECLARACIÓN DE VARIABLES **************/
    SICEEO_QueriesInformix qryIfx=null;
    SICEEO_AlumnosConPromXMat alumnosConPromXMat;
    SICEEO_CalifSecXBim califSecXBim = new SICEEO_CalifSecXBim(null, request);
    String r, cicescini, modalidad, cveplan, grado;
    Map parameters = new HashMap();
    File reportFile;
    
    
try{
    /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
    HttpSession sesionOk = request.getSession(false);
    if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !califSecXBim.ejecutarPeticion("peVeRe"))
        throw new Exception ("SIN_SESION");
    
    /* *********************** ASIGNACIÓN DE VARIABLES ********************** */
    r=request.getParameter("r");
    parameters.put("cicescini", cicescini=request.getParameter("cicescini") );
    parameters.put("modalidad", modalidad=request.getParameter("modalidad") );
    parameters.put("cveplan", cveplan=request.getParameter("cveplan") );
    parameters.put("idcct", request.getParameter("idcct") );
    parameters.put("grado", grado=request.getParameter("grado") );
    parameters.put("grupo", request.getParameter("grupo") );
    /* ********************* CONEXION A LA BASE DE DATOS ******************** */
    qryIfx = new SICEEO_QueriesInformix();
    qryIfx.conectar();
    alumnosConPromXMat = new SICEEO_AlumnosConPromXMat(qryIfx);
    /* *********************** PROCESAMIENTO DE DATOS *********************** */
    alumnosConPromXMat.getPaqueteDeMaterias (parameters, cicescini, modalidad, cveplan, grado);
    
    /******** SE CREA EL DOCUMENTO PDF Y SE LE INSERTAN LOS DATOS **************/
    reportFile = new File(application.getRealPath("")+"/../reportes/siceeo/"+r+".jasper");
    byte[] bytes =JasperRunManager.runReportToPdf(reportFile.getPath(),parameters, qryIfx.getConexion());
    /* *********************** SE MUESTRA EL DOCUMENTO *********************** */
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
    </body>
</html>
