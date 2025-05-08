<%-- 
    Document   : ReporteDeEvaluacion
    Created on : 15/06/2017, 08:07:15 PM
    Author     : Maai
--%>

<%@page import="modelo.Reportes.SICEEO_Reportes"%>
<%@page import="net.sf.jasperreports.engine.JREmptyDataSource"%>
<%@page import="net.sf.jasperreports.engine.JRException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
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
        <title>Reporte de evaluación</title>
<%
    /********** DECLARACIÓN DE VARIABLES **************/
    SICEEO_QueriesInformix qryIfx=null;
    SICEEO_DataModule dm = new SICEEO_DataModule();
    Map dr = new HashMap();
    String r="Reportes/RepsEvaluacion/cicloEscolar", cicescini, cicescinilib, cveplan, idcct, modalidad, grupo;
    String strQry="",statusIngles="";
    int grado;
    String nivel[]=new String[]{"","Prim","Secu","Preesc"};
    Map parameters = new HashMap();
    File reportFile;
    
    try {
        /********** ANTES DE CUALQUIER COSA VERIFICAMOS QUE TENGA SESIÓN INICIADA **************/
        HttpSession sesionOk = request.getSession(false);
        if  (sesionOk==null || sesionOk.getAttribute("userName")==null || !dm.permisoVerReportes(request) )
            throw new Exception ("SIN_SESION");
        sesionOk.setAttribute("modulo", "RepEv");
        
        SICEEO_Reportes re = new SICEEO_Reportes(dr, request);
        re.ejecutarPeticion("ReDeEv");
        if (!dr.get("returnCase").equals(1))
            throw new Exception (""+dr.get("mensaje"));
        
        parameters.put("modalidad", modalidad=request.getParameter("modalidad") );
        cicescini=request.getParameter("cicescini");  // tenia el dato de cicescinilib en el ciclo 2022
        
        if(modalidad.equals("DBA"))
            cicescini = ""+(Integer.parseInt(cicescini)+1);
        
        /********** ASIGNACIÓN DE VARIABLES **************/        
        cicescinilib=request.getParameter("cicescinilib");
        cveplan=request.getParameter("cveplan");
        idcct=request.getParameter("idcct");
        //modalidad=request.getParameter("modalidad");
        grado=Integer.parseInt(""+request.getParameter("grado"));
        grupo=request.getParameter("grupo");
        
        if( Integer.parseInt(cicescinilib)<2022 )
            r+="/noexiste/";
        
        r+=cicescinilib+"/repEval" + nivel[Integer.parseInt(cveplan)] + (cveplan.equals("1") && Integer.parseInt(cicescinilib)<2023 ? ("DPB, DCI".contains(modalidad)?"Indig":"Formal"):"");
        
        if (cveplan.equals("1")){
            if(Integer.parseInt(cicescinilib)>=2018 && Integer.parseInt(cicescinilib) < 2023 && (grado==1 || grado==2))
                r+="_G1-2";            
            else if (Integer.parseInt(cicescinilib) < 2023 && (grado==4 || grado==5))
                r+="_G4-5";
            else if (Integer.parseInt(cicescinilib) >= 2023 && grado>=2 && grado<=5)
                r+= "_G2-5";
            else
                r+="_G"+grado;            
        } else if (cveplan.equals("2")){            
            r+="_G"+grado;
        } else if (cveplan.equals("3")) {
            if(Integer.parseInt(cicescinilib)>=2018 && Integer.parseInt(cicescinilib)<2023) {
                if(grado!=3)
                    r+="_G1-2";
                else
                    r+="_G3";
            }
            else                
                r+="_G1-3";        
        }
        
        
        parameters.put("cicescini", cicescini);
        parameters.put("cicescinilib", cicescinilib);
        parameters.put("idcct", idcct);
        parameters.put("grado", grado);
        parameters.put("grupo", grupo);
        if(cicescini.equals("2016")){
            parameters.put("anioExp", "2017"); // 2017
            parameters.put("mesExp", "07");
            parameters.put("diaExp", "25"); //25
        }
        else if(cicescini.equals("2017") || cicescini.equals("2020")) {
            if(cicescini.equals("2017"))
                parameters.put("anioExp", "2018"); 
            else 
                parameters.put("anioExp", "2021");
            
            parameters.put("mesExp", "07");
            parameters.put("diaExp", "09"); 
        }        
        else if(cicescini.equals("2018")) {
            parameters.put("anioExp", "2019"); 
            parameters.put("mesExp", "07");
            parameters.put("diaExp", "08"); 
        }
        else if(cicescini.equals("2019")) {
            parameters.put("anioExp", "2020"); 
            parameters.put("mesExp", "06");
            parameters.put("diaExp", "19"); 
        }
        
        qryIfx = new SICEEO_QueriesInformix();
        qryIfx.conectar();
        statusIngles = qryIfx.getIngles(cicescini, ""+grado, idcct, grupo);
        if((""+statusIngles).equals("f") || (""+statusIngles).equals("null"))
                strQry = " AND cvemat NOT IN ('084','ING','087','088') ";
        parameters.put("strQry", strQry);
        /******** SE CREA EL DOCUMENTO PDF Y SE LE INSERTAN LOS DATOS **************/
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
            out.print("ERROR: "+ex.getMessage());  
    }
    finally { try { qryIfx.cerrarConexion(); } catch(Exception ex){ } }
%>
    </head>
    <body>
    </body>
</html>
