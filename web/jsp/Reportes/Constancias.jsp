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
    String r="Reportes/Constancia/", cicescini, cicescinilib,modalidad, cveplan, idcct, grado, grupo, caso;
    Map parameters = new HashMap();
    int cicescinilib_int = Integer.parseInt(""+request.getParameter("cicescinilib"));
    File reportFile;
    boolean str_of= false;
    
    try{
        /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
        HttpSession sesionOk = request.getSession(false);
        if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !dm.permisoVerReportes(request) )
            throw new Exception ("SIN_SESION");
        
        /********** ASIGNACIÓN DE VARIABLES **************/        
        cicescini=request.getParameter("cicescini");        
        parameters.put("cicescini", cicescini );
        parameters.put("cicescinilib", cicescinilib=request.getParameter("cicescinilib") );
        parameters.put("cveplan", cveplan=request.getParameter("cveplan") );        
        parameters.put("idcct", idcct=request.getParameter("idcct") );
        parameters.put("grado", grado=request.getParameter("grado") );
        parameters.put("grupo", grupo=request.getParameter("grupo") );
        caso=request.getParameter("caso");
        
        sesionOk.setAttribute("modulo", "Rep"+caso);
                       
        /* ********************* CONEXION A LA BASE DE DATOS ******************** */
        qryIfx = new SICEEO_QueriesInformix();
        qryIfx.conectar();
        
        /* *********************** PROCESAMIENTO DE DATOS *********************** */
        SICEEO_Reportes re = new SICEEO_Reportes(dr, request);
        re.isCalEvalGradoGrupoOficializado (cicescini, idcct, grado, grupo, cveplan,"reporte", qryIfx );
        if (!dr.get("returnCase").equals(1) && !caso.equals("Ra"))  // caso=Ra para reporte de avances 2023-2024
                throw new Exception (""+dr.get("mensaje"));
        
        
        r += ""+cicescinilib+"/";
        /******** SE CREA EL DOCUMENTO PDF Y SE LE INSERTAN LOS DATOS **************/        
        
        if (caso.equals("IAR"))
            r+="IARPre";
        if (caso.equals("Ra")){  // Para reporte de avances 2023-2024
            str_of = qryIfx.isCalEvalGradoGrupoOficSinAlusDesofic(idcct, cicescini, grado, grupo, cveplan.equals("3")?"EVALUACION":"CALIFS BIM");
            parameters.put("str_of", (str_of==true ? 1 : 0));
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
