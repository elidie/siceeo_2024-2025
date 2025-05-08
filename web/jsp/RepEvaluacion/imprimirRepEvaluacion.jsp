<%-- 
    Document   : imprimirAPDF3
    Created on : 27-jun-2013, 11:08:11
    Author     : dai
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="modelo.DAO.SICEEO_QueriesInformix"%>
<%@page import="modelo.RepEvaluacion.SICEEO_ImprimeRepoteEvaluacion"%>
<%@page import="modelo.ClasesGlobales.SICEEO_Mensajes"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.awt.Graphics2D"%>
<%@page import="java.awt.Color"%>
<%@page import="java.io.*, com.lowagie.text.*, com.lowagie.text.pdf.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Impresión de Reporte de Evaluación.</title>
    </head>
    <body>
<%  
    //********************* CONFIGURACIONES PARA IMPRIMIR EN PDF ********************
    DefaultFontMapper mapper = new DefaultFontMapper();
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    //Document document = new Document();
    Document document = new Document(new com.lowagie.text.Rectangle(0, 0, 600, 782), -0, 0, 0, 0);
    com.lowagie.text.Rectangle rect = PageSize.LETTER;
    PdfWriter writer = PdfWriter.getInstance(document,buffer);
    //*******************************************************************************
    try
    {
        SICEEO_ImprimeRepoteEvaluacion printRepEvaluacion = new SICEEO_ImprimeRepoteEvaluacion();
        /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
        HttpSession sesionOk = request.getSession(false);
        if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !printRepEvaluacion.permisosVerReporteRepEvaluacion(request) )
            throw new Exception ("SIN_SESION");
        
        /********** ASIGNACIÓN DE VARIABLES **************/
        String idalus= request.getParameter("arrayIdAlu");
        String cveturno = request.getParameter("cveturno");
        String nombrecct = request.getParameter("nombrecct");
        String cct = request.getParameter("cct");
        String idcct = request.getParameter("idcct");
        String modalidad = request.getParameter("modalidad");
        String cveprograma = request.getParameter("cveprograma");
        String cveplan = request.getParameter("cveplan");
        String grado = request.getParameter("grado");
        String grupo = request.getParameter("grupo");
        String configImpre = request.getParameter("configImpre");               //Antes había varias configuraciones para cada impresora, pero sólo la vamos a reestringir a una
        String tipoImpresion = request.getParameter("tipoImpresion");
        String cicescini = request.getParameter("cicescini");
        
        printRepEvaluacion.initData(cicescini, idcct, modalidad, cveplan, grado, configImpre, "REPORTE_EVALUACION");
        /**********************************************************************/
        if(tipoImpresion.equals("0")) 
            printRepEvaluacion.getDatosImpresion(cveturno, nombrecct, cct, grupo, idalus);

        printRepEvaluacion.setTipoImpresion(Integer.parseInt(tipoImpresion));
        document.open();

        printRepEvaluacion.crearDocumentoPDF(document, writer, mapper);
        document.close();

        byte[] bytes = buffer.toByteArray(); 
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length);
        ouputStream.flush();
        ouputStream.close();
    }catch(SQLException ex){ out.print("Error de consulta: "+ex);  }
    catch (ClassNotFoundException e) { out.print("Error de driver de conexión: "+e);  }
    catch(Exception ex){ 
        if (ex!=null && ex.getMessage()!=null && ex.getMessage().equals("SIN_SESION")){
            response.sendRedirect("../../cerrarSesion.jsp");
        }else
            out.print("ERROR: "+ex);  
    }
%>
</body>
</html>
