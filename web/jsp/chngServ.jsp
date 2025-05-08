<%-- 
    Document   : cambiarDeServidor
    Created on : 29-jun-2017, 3:04:11
    Author     : maai
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
HttpSession sesion = request.getSession(false);
Enumeration san = sesion.getAttributeNames();
while (san.hasMoreElements()) 
{
    String element = (String) san.nextElement();
    if (element.equals("usuario"))
        session.setAttribute("usuario", false);
    session.removeAttribute(element);
}
sesion.invalidate();
response.setHeader("Pragma", "No-chache");
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache", "no-cache"); 
//response.setHeader("charset", "UTF-8");
response.sendRedirect("http://www.ieepoplaneacionedu.info:"+request.getParameter("puerto")+"/siceeo/jsp/main.jsp");
    
%>