<%-- 
    Document   : Kardex
    Created on : 03-Jul-2015, 15:35:18
    Author     : Ing. Maai Nolasco Sánchez
--%>

<%@page import="net.sf.jasperreports.engine.JREmptyDataSource"%>
<%@page import="net.sf.jasperreports.engine.JRException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.File"%>

<%@page import="modelo.DAO.SICEEO_QueriesInformix"%>
<%@page import="modelo.Preinscripcion.SICEEO_Preinscripcion"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <title>Acuse de Oficialización</title>
        <link rel="stylesheet" href="../../estilos/libs/normalize.css" />
   
<%
/********** DECLARACIÓN DE VARIABLES **************/
    String r, cicesciniPreinsc, idcct;
    Map parameters = new HashMap();
    SICEEO_QueriesInformix qryIfx=null;
    SICEEO_Preinscripcion peVeReAcOf = new SICEEO_Preinscripcion(null);
    File reportFile;
    
try{
    /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
    HttpSession sesionOk = request.getSession(false);
    if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !peVeReAcOf.ejecutarPeticion(request, "peVeReAcOf") )
        throw new Exception ("SIN_SESION");
    sesionOk.setAttribute("modulo", "RepAcOfPr");
    
    /********** ASIGNACIÓN DE VARIABLES **************/
    r=request.getParameter("r");
    cicesciniPreinsc=request.getParameter("cicesciniPreinsc");
    idcct=request.getParameter("idcct");
   
    parameters.put("cicesciniPreinsc", cicesciniPreinsc);
    parameters.put("idcct", idcct);

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