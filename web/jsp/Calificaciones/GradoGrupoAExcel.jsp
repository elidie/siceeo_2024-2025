<%-- 
    Document   : GradoGrupoAExcel
    Created on : 07-ago-2015, 22:49:43
    Author     : maai
--%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="modelo.Calificaciones.SICEEO_BimXAlum"%>
<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <!--================= CONFIGURACI?N DE LA P?GINA ================-->
        <% response.addHeader("pragma", "no-cache"); %>
        <% response.addHeader("cache-control", "no-store");%>
        <title>Exportaci?n a Excel</title>
        <meta charset="iso-8859-1" />
        <meta name="viewport" content="width=device-width, user-scalable=1, initial-scale=1.0, minimum-scale=1.0"/> <!-- Para que funcione en dispositivos moviles-->
        <!--meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Cache-Control" content="no-store" />
        <meta http-equiv="Expires" content="-1" /-->

        <!--========================== ESTILOS ==========================-->
        <!----- Librer?as ------------------------------------------------->
        
        <!----- Sistema --------------------------------------------------->
        <link rel="icon" type="image/gif" href="../../imagenes/generales/2.gif" />
        <!--link rel="stylesheet" href="estilos/GlobalCss/EstilosGenerales.css" /-->
        <link rel="stylesheet" href="../../estilos/Calificaciones/GradoGrupoAExcel.css" />

        <!--========================== SCRIPTS ==========================-->
        <!----- Librer?as ------------------------------------------------->
        <script src="../../js/libs/prefixfree.min.js"></script> <!--Siempre debe ir despues de los css y antes de la librer?a jquery-->
        <script src="../../js/libs/jq.1.9.1_development.js"></script>

        <!----- Sistema --------------------------------------------------->
        <script src="../../js/GlobalJs/Mensajes.js"></script>
        <script src="../../js/GlobalJs/JsGenerales.js"></script>
        <script src="../../js/GlobalJs/Tabla.js"></script>
        
        <script src="../../js/Calificaciones/GradoGrupoAExcel.js"></script>

    </head>
    
    <body>
        <%
            //Map datosReturn = new HashMap();
            //response.setHeader("Content-Disposition", "attachment;filename=miArchivoExcel.xls");
            //SICEEO_BimXAlum biXAl = new SICEEO_BimXAlum(datosReturn);
            //biXAl.ejecutarPeticion(request, "exDaEx");
            //out.print("Documento creado con ?xito.");
        
        
                /*String exportToExcel = request.getParameter("exportToExcel");
		if (exportToExcel != null	&& exportToExcel.toString().equalsIgnoreCase("YES")) {
			response.setContentType("application/vnd.ms-excel");
			//response.setHeader("Content-Disposition", "inline; filename=excel.xls");
                        response.setHeader("Content-Disposition", "attachment;filename=miArchivoExcel.xls");

		}*/
        %>
        <div id="divCalifsToExcel">
        </div>
        
    </body>
</html>
