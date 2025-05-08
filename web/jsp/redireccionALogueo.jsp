<%-- 
    Document   : redireccionALogueo
    Created on : 13/05/2016, 01:28:08 PM
    Author     : Maai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    response.setHeader("Pragma", "No-chache");
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache", "no-cache"); 
    //response.setHeader("charset", "UTF-8"); 
    response.sendRedirect("../html/Inicio/logueo.html");
%>
