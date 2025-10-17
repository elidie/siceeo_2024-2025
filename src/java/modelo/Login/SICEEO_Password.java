package modelo.Login;

import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.ClasesGlobales.SICEEO_SessionManager;
import modelo.ClasesGlobales.SICEEO_FileReader;
import modelo.ClasesGlobales.SICEEO_SourcePath;

/**
 *
 * @author dai
 */
public class SICEEO_Password {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final Map dr;
    private final SICEEO_DataModule dm;
    private final String pathConfigsSICEEO;
       
    public SICEEO_Password(Map datosReturn)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dr = datosReturn;
        this.dm = new SICEEO_DataModule ();
        //this.pathConfigsSICEEO = "/opt/tomcat/siceeoConfigs/configsSICEEO.sco";  // puerto 88
        this.pathConfigsSICEEO = "/var/lib/tomcat/siceeoConfigs/configsSICEEO.sco";  // puerto 80 
    }
/*******************************************************************************/
/****************************** ÁREA PARA EL CONTROLADOR ***********************/
/*******************************************************************************/
    /**
     *
     * @param request Solicitud del servidor
     * @param metodo Caso de método al que se va a llamar
     */    
    public void ejecutarPeticion (HttpServletRequest request, HttpServletResponse response, String metodo) throws UnknownHostException, ServletException, IOException, Exception
    {                
        if (metodo.equals("lo")) {
            loguearse (request);
            if (dm.toInt(this.dr.get("returnCase"))>0){
                FormActive (request, request.getParameter("usuario").toUpperCase().trim());
                if (dm.toInt(this.dr.get("returnCase"))!=2 && dm.toInt(this.dr.get("returnCase"))!=-1)
                    getNumUsuariosLogueados (request, response);
            }
        }
    }
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    
    private void getNumUsuariosLogueados (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        SICEEO_FileReader fileReader;
        SICEEO_SourcePath sourcePath;
        JsonObject jsonObj;
        String path="";
        
        try {
            HttpSession session = request.getSession(false);
            SICEEO_SessionManager sessionManager = (SICEEO_SessionManager)session.getServletContext().getAttribute("activeUsers");
            if (sessionManager!=null){
                if (sessionManager.getNumUsuariosLogueados()>dm.toInt(this.dr.get("maxSesionesXServer"))) {
                    this.dr.remove("maxSesionesXServer");
                    sourcePath = new SICEEO_SourcePath();
                    //path = sourcePath.getLevelUpPath(6)+sourcePath.getSeparator()+"siceeoConfigs/configsSICEEO.sco";
                    path = pathConfigsSICEEO;
                    fileReader = new SICEEO_FileReader(path);
                    jsonObj = dm.getJson(fileReader.leerFichero(true));
                        
                    if (jsonObj.get("redirectServer").getAsString().equals("server3")){
                        dr.put("puerto",8088);
                        throw new SICEEO_Excepcion (-11,"CAMBIAR_DE_SERVIDOR");
                    }else if (jsonObj.get("redirectServer").getAsString().equals("server6")){
                        dr.put("puerto",8080);
                        throw new SICEEO_Excepcion (-11,"CAMBIAR_DE_SERVIDOR");
                    }else                                                       //redirectServer es "local"
                        throw new SICEEO_Excepcion (0,"SERVIDOR_SATURADO");
                }
            }else
                throw new SICEEO_Excepcion (0,"USR_PASS_INCORRECTO");
        }catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.loginUser(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (FileNotFoundException ex){ dr.put("returnCase",-1); mensaje.loginUser("SIN_ARCHIVO_CONFIG", "", "", dr); }
        catch (Exception ex){ dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
    }
    
    private void loguearse (HttpServletRequest request) throws UnknownHostException, Exception
    {
        HttpSession sesion = request.getSession(true);
        String ipAddress,nameUsser;
        
        ipAddress = request.getParameter("HTTP_X_FORWARDED_FOR");
        if (ipAddress == null) 
            ipAddress = request.getRemoteAddr();             
        InetAddress addr = InetAddress.getByName(ipAddress);
        nameUsser = addr.getHostName();
        
        this.dr.put("ip", ipAddress);
        this.dr.put("nomPc",nameUsser );
        
        //setTipoConexion (request);
        
        btnAceptar_Click(request.getParameter("usuario").toUpperCase().trim(), request.getParameter("password").toUpperCase().trim(), request.getParameter("cambUsu"));
        
        if (this.dr.get("returnCase").equals(1)) {
            sesion.setAttribute("usuario", true);
            sesion.setAttribute("userName", request.getParameter("usuario").toUpperCase().trim());
            sesion.setAttribute("cveunidad", this.dr.get("unidad"));
            sesion.setAttribute("QUsuario_idcct", this.dr.get("QUsuario_idcct"));
            sesion.setAttribute("tipo_usuario", this.dr.get("tipo_usuario"));
            sesion.setAttribute("usuarioNo22", this.dr.get("usuarioNo22"));
            sesion.setAttribute("superUsuario", this.dr.get("superUsuario"));
            sesion.setAttribute("puedeDesof", this.dr.get("puedeDesof"));
            sesion.setAttribute("quitarFolio", this.dr.get("quitarFolio"));
            sesion.setAttribute("seccion", this.dr.get("seccion"));
            sesion.setAttribute("feciniciclo", this.dr.get("feciniciclo"));
            sesion.setAttribute("fecfinciclo", this.dr.get("fecfinciclo"));
            sesion.setAttribute("tblPrincipal_idcct", "-1");                    //Esta variable guarda el idcct en el que estará trabajando el usuario. Por default tendrá -1 hasta que se ejecute la búsqueda de un cct.
            sesion.setMaxInactiveInterval(Integer.parseInt(""+this.dr.get("tiempoEspera")));                                  //Especifica el tiempo en segundos
           
            this.dr.put("fecha", dm.getFechaHoy("dd/MM/yyyy h:mm a"));
            
            permisos (request.getParameter("usuario").toUpperCase().trim(), ""+dr.get("tipo_usuario"), ""+dr.get("QEscuela_cveplan"), ""+dr.get("QEscuela_cveunidad"), ""+dr.get("seccion"));                        
        }
    }
    
    public void FormActive (HttpServletRequest request, String txtUsuario) 
    {
        Map QCiclo;
        Map QAvisos;
        try {
            qryIfx.conectar();
            QCiclo = qryIfx.Ciclo();
            QAvisos = qryIfx.getAvisosSisWeb ();
            
            //if ( (!txtUsuario.equals("ELYLOPEZ") || !txtUsuario.equals("MRAMIREZ")) && ( (""+QCiclo.get("descicesc")).toUpperCase().equals("MANTENIMIENTO")  ||  ( (""+QCiclo.get("descicesc")).toUpperCase().equals("MANTENIMIENTOWEB") && !txtUsuario.equals("IVALLE") && !txtUsuario.equals("FLALUISSA") && !txtUsuario.equals("POBLETEVL")) ) )
            if ( ( (""+QCiclo.get("descicesc")).toUpperCase().equals("MANTENIMIENTO")  ||  
                    ( (""+QCiclo.get("descicesc")).toUpperCase().equals("MANTENIMIENTOWEB") 
                        && !txtUsuario.equals("IVALLE") && !txtUsuario.equals("VICTORPS") && !txtUsuario.equals("POBLETEVL") && 
                        !txtUsuario.equals("ELYLOPEZ") && !txtUsuario.equals("MRAMIREZ") && !txtUsuario.equals("HZAVALA")) ) )
            {
                dr.put("lblSiceeb10_Caption","En mantenimiento el");
                dr.put("btnAccesar_Enabled",false);
                //------------- Cerramos las sesión java --------------------
                HttpSession session = request.getSession(false);
                session.invalidate();                                                                                                                   //SiCEEO_SessionListener junto con SICEEO_SessionManager se encargan de eliminar lo correspondiente a la sesión
                //---------------------------------------------------------------
                if ((""+QCiclo.get("descicesc")).toUpperCase().equals("MANTENIMIENTO"))
                    throw new SICEEO_Excepcion (-1,"EN_MANTENIMIENTO");
                else
                    throw new SICEEO_Excepcion (2,"EN_MANTENIMIENTO");
            }
            dr.put("versionSis",QCiclo.get("version_siceeo"));
            dr.put("avisos",QAvisos);
        }catch (SQLException ex){ dr.put("returnCase",-1); mensaje.General("CONEXION",ex.getMessage(),"", dr); }
        catch (SICEEO_Excepcion ex){ this.dr.put("returnCase",ex.getNumError());  mensaje.loginUser(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void btnAceptar_Click (String txtUsuario, String pass, String cambUsu) 
    {
        String x, y="" , delaReg;
        this.dr.put("cambUsu", cambUsu);
        dr.put("returnCase",0); 
        Map QUsuario, QEscuela, QCiclo;
        
        ArrayList <String> regiones = new ArrayList<String>();
        regiones.add("DSRAYU");
        regiones.add("DSRCAN");
        regiones.add("DSRHUA");
        regiones.add("DSRIST");
        regiones.add("DSRIXT");
        regiones.add("DSRPIN");
        regiones.add("DSRPUE");
        regiones.add("DSRTLA");
        regiones.add("DSRTUX");
        regiones.add("DSRVAL");
        regiones.add("DSRISN");
        /*regiones.add("CAPTU");
        regiones.add("LECTU");
        regiones.add("MESA");*/
        
        try {
            qryIfx.conectar();
            QCiclo = qryIfx.Ciclo();
            this.dr.put("cicescini",QCiclo.get("cicescini"));
            this.dr.put("feciniciclo",QCiclo.get("fecini"));
            this.dr.put("fecfinciclo",QCiclo.get("fecfin"));
            QUsuario = qryIfx.usuario(txtUsuario);
            
            if (!QUsuario.isEmpty())
            {
                if (QUsuario.get("estatus").equals("I") && !QUsuario.get("seccion").equals("59"))
                {
                    if((""+QUsuario.get("cveunidad")).substring(0,3).equals("XCT"))
                    {
                        if((""+QUsuario.get("modalidad")).substring(0,1).equals("P"))
                            throw new SICEEO_Excepcion (0,"USR_SIN_REFRENDO");
                        else 
                            throw new SICEEO_Excepcion (-2,"SERVER-ERROR");
                    }
                    else
                        throw new SICEEO_Excepcion (-2,"SERVER-ERROR");
                }                    
                else if (QUsuario.get("estatus").equals("I") && (""+QUsuario.get("cveunidad")).substring(0,3).equals("CCT") 
                        && !(""+QUsuario.get("modulos")).equals(",23,"))                
                    throw new SICEEO_Excepcion (-2,"SERVER-ERROR");
                
                else {
                    x = qryIfx.codificarABase64(pass);
                    y = qryIfx.decodificarBase64(""+QUsuario.get("pasword"));
                }
                
                if (!QUsuario.isEmpty() && y.equals(pass) && !(""+QUsuario.get("cveunidad")).substring(0,3).equals("CXT") && !(""+QUsuario.get("cveunidad")).substring(0,3).equals("XCT"))
                {
                    //------------------------------ Código personal para un bloqueo específico -----------------------------\\
                    /*if (!( QUsuario.get("loginuser").equals("IVALLE") && QUsuario.get("pasword").equals("MzA4U0lDRUVCOU==")))
                    {
                        if (QUsuario.get("CveUniEsc").toString().equals("DSRVAL") && QUsuario.get("cveplanEsc").toString().equals("1"))
                            throw new SiCEEB_Excepcion (0,"USR_SIN_ACCESO");
                    }*/
                    //--------------------------------------------------------------------------------------------------------------//
                    this.dr.put("cctdefault",QUsuario.get("cctdefault"));  //<--- OJO: Esta variable se agregó sólo para SICEEO, sirve para la búsqueda principal de escuela
                    
                    if ( regiones.contains(""+QUsuario.get("cveunidad")) || (""+QUsuario.get("cveunidad")).substring(0,3).equals("CCT") ||  (""+QUsuario.get("cveunidad")).substring(0,3).equals("CXT"))
                    {
                        
                        //para usuarios tipo CCT
                        this.dr.put("tipo_usuario"," ");
                        this.dr.put("superUsuario"," ");
                        this.dr.put("usuarioNo22"," ");
                        this.dr.put("puedeDesof"," ");
                        this.dr.put("quitarFolio"," ");
                        this.dr.put("modulos", QUsuario.get("modulos"));
                        this.dr.put("seccion", QUsuario.get("seccion"));
                        this.dr.put("eunidad", QUsuario.get("cveunidad"));
                                    
                        if (QUsuario.get("seccion").equals("22") && ((""+QUsuario.get("cveunidad")).substring(0,3).equals("CCT") || (""+QUsuario.get("modulos")).contains(",29,")) )
                           this.dr.put("permisoDir", 1); 
                        if (QUsuario.get("usertipo").equals("1") && (""+QUsuario.get("modulos")).contains(",26,"))
                           this.dr.put("puedeDesof","si");
                        
                        if (QUsuario.get("usertipo").equals("1") && (""+QUsuario.get("modulos")).contains(",28,"))
                           this.dr.put("quitarFolio","si");
                        
                        if (QUsuario.get("usertipo").equals("1"))
                           this.dr.put("superUsuario","si");
                        else if (QUsuario.get("usertipo").equals("no22"))
                           this.dr.put("usuarioNo22","si");
                        
                        if ( (""+QUsuario.get("cveunidad")).substring(0,3).equals("CCT") ||  (""+QUsuario.get("cveunidad")).substring(0,3).equals("CXT") || QUsuario.get("cveunidad").equals("LECTU") || QUsuario.get("cveunidad").equals("CAPTU") || QUsuario.get("cveunidad").equals("MESA") )   //solo usuarios de regiones x el momento
                        {
                             this.dr.put("tipo_usuario",""+QUsuario.get("idcct"));
                             if (QUsuario.get("cveunidad").equals("LECTU"))
                                this.dr.put("tipo_usuario","consulta");
                             if (QUsuario.get("cveunidad").equals("MESA"))
                                this.dr.put("tipo_usuario","mesa");
                             if (QUsuario.get("cveunidad").equals("CAPTU"))
                                this.dr.put("tipo_usuario","captura");
                            
                             //QUsuario.put("tiempo",10000);  //OJO: Solo para pruebas
                             this.dr.put("tiempoEspera",Integer.parseInt(""+QUsuario.get("tiempo"))); //despues de Una hora;
                             delaReg = ""+QUsuario.get("cveuniesc");
                             this.dr.put("maxSesionesXServer",QCiclo.get("maxsesserv_siceeo")); //Sólo de uso en siceeo, para controlar la cantidad de sesiones que puede haber en el servidor 3, si se pasa se debe mandar al usuario al servidor 6
                        }else
                        {
                           delaReg = ""+QUsuario.get("cveunidad");
                           this.dr.put("tiempoEspera",10000);
                           this.dr.put("maxSesionesXServer",1000);               //Sólo de uso en siceeo, los superusuarios tienen acceso ilimitado
                        }

                        this.dr.put("vista",delaReg);
                        //Dm.Q_Certi.MacroByName('vista').AsString     := 'V_'+Dm.Q_Usuariocveunidad.AsString;
                        this.dr.put("unidad",delaReg);                              // la necesitamos para buscar directores solo de las escuelas k pertenecen

                        if (this.dr.get("cambUsu").equals("sinusuario")){
                           this.dr.put("cambUsu","CONusuario");
                        } else
                            this.dr.put("F_PRINCIPAL.L_DSR.Caption","Region: "+ dr.get("unidad")+", "+txtUsuario);
                        qryIfx.log(txtUsuario, ""+dr.get("unidad"), ""+dr.get("ip"), ""+dr.get("nomPc"));   // lo repetirmos para cuando sin salir del sistema se cambia de usuario
                        
                        //----------------- Aunque no está en el código de robert sí necesitamos estos datos  ---------------\\
                        if ( !dr.get("tipo_usuario").equals(" ") && !dr.get("tipo_usuario").equals("consulta") && !dr.get("tipo_usuario").equals("captura") && !dr.get("tipo_usuario").equals("mesa") ){
                            QEscuela = qryIfx.escuela("idcct = '"+QUsuario.get("idcct")+"' ");
                            this.dr.put("QEscuela_cveplan",QEscuela.get("cveplan"));                                                          //Se usa en Principal en el formActive
                            this.dr.put("QEscuela_cveunidad",QEscuela.get("cveunidad"));                                                   //Se usa en Principal en el formActive
                        }
                        this.dr.put("QUsuario_idcct",QUsuario.get("idcct"));                                                                   //se queda abierto y se usa en formulario CambDeGpo 
                        //--------------------------------------------------------------------------------------------------------------//
                        dr.put("returnCase",1); 
                    } else
                        throw new SICEEO_Excepcion (0,"USR_SIN_DELEG");
                } else {
                    String[] arregloMod = { "20P", "PPR", "PST", "PES", "PDI", "PTV","PNS", "PJN" };
                    String modaStr = (""+QUsuario.get("loginuser")).substring(0,3);
                    int estatus = 0;
                    for(int i=0; i<arregloMod.length; i++)
                        if((""+arregloMod[i]).equals(modaStr))
                        { estatus=1; break;}                                        
                    
                    if((""+QUsuario.get("cveunidad")).substring(0,3).equals("XCT") && estatus==1)                                            
                        throw new SICEEO_Excepcion (0,"USR_SIN_REFRENDO");  
                    else if ( (""+QUsuario.get("cveunidad")).substring(0,3).equals("CXT") || (""+QUsuario.get("cveunidad")).substring(0,3).equals("XCT") )
                        throw new SICEEO_Excepcion (0,"SIS_CERRADO_IMPRE");  
                    else
                        throw new SICEEO_Excepcion (0,"USR_PASS_INCORRECTO");
                }
            } else
                throw new SICEEO_Excepcion (0,"USR_PASS_INCORRECTO");
            
        }catch (SQLException ex){ dr.put("returnCase",-1); mensaje.General("CONEXION",ex.getMessage(),"", dr); }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.loginUser(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void btnCancelar_Click(String cambUsu, String cambiarUsuario, String usuarioAct)
    {
        if (cambUsu.equals("sinusuario") ){
            //--Vista--> F_Password.Release;    //Close and Free
        } else {
            if ( cambiarUsuario.equals("si") )
               dr.put("txtUsuario_Text", usuarioAct);
            //--Vista--> close;
        }
    }
    
    private void permisos (String usuario, String tipo_usuario, String QEscuela_cveplan, String QEscuela_cveunidad, String seccion)
    {
        ArrayList<String> QPermisos, QPermisosCalif=null;   
        String strTipoUsuario = tipo_usuario;
        try {
            qryIfx.conectar();
            
            if (usuario.toUpperCase().equals("IVALLE") || usuario.toUpperCase().equals("ELYLOPEZ") || usuario.toUpperCase().equals("MRAMIREZ"))
            {   strTipoUsuario = "ADMIN";
                crearBotonesDeMenu (qryIfx.getPermisos ("ADMIN",usuario,"PRINCIPAL",QEscuela_cveplan ).toArray());
                crearBotonesDeGpo (qryIfx.getPermisos ("ADMIN",usuario,"GRUPO", QEscuela_cveplan).toArray());
                crearBotonesDeExmExt (qryIfx.getPermisos ("ADMIN",usuario,"CALIFICACIONES", QEscuela_cveplan).toArray());
                QPermisosCalif = qryIfx.getPermisos ("ADMIN",usuario, "CALIFICACIONES", QEscuela_cveplan);                
            }else if (seccion.equals("59")){
                strTipoUsuario = "CCT 59";
                crearBotonesDeMenu (qryIfx.getPermisos ("CCT 59",usuario,"PRINCIPAL", QEscuela_cveplan).toArray());
                crearBotonesDeGpo (qryIfx.getPermisos ("CCT 59",usuario,"GRUPO", QEscuela_cveplan).toArray());
                crearBotonesDeExmExt (qryIfx.getPermisos ("CCT 59",usuario,"CALIFICACIONES", QEscuela_cveplan).toArray());
                QPermisosCalif = qryIfx.getPermisos ("CCT 59",usuario,"CALIFICACIONES", QEscuela_cveplan);
                dr.put("btnBusquedaCCT_Click", true);
            //Apagar botones para usuarios tipo CCT
            }else if (!tipo_usuario.equals(" ") && !tipo_usuario.equals("consulta") && !tipo_usuario.equals("captura") && !tipo_usuario.equals("mesa") )   // cuando es un usuario tipo CCT
            {
                strTipoUsuario = "CCT";
                QPermisos = qryIfx.getPermisos ("CCT",usuario,"PRINCIPAL", QEscuela_cveplan);
                QPermisosCalif = qryIfx.getPermisos ("CCT",usuario,"CALIFICACIONES", QEscuela_cveplan); 
                if (!QEscuela_cveplan.equals("2"))
                    QPermisos.remove("btnCartilla");
                
                crearBotonesDeMenu (QPermisos.toArray());
                dr.put("btnBusquedaCCT_Click", true);
                crearBotonesDeGpo (qryIfx.getPermisos ("CCT",usuario,"GRUPO", QEscuela_cveplan).toArray());
                crearBotonesDeExmExt (qryIfx.getPermisos ("CCT",usuario,"CALIFICACIONES", QEscuela_cveplan).toArray());
            }
            //tipo usuario region
            else if (tipo_usuario.equals(" ") ){                
                crearBotonesDeMenu (qryIfx.getPermisos (tipo_usuario,usuario,"PRINCIPAL", QEscuela_cveplan).toArray());
                crearBotonesDeGpo (qryIfx.getPermisos (tipo_usuario,usuario,"GRUPO", QEscuela_cveplan).toArray());
                crearBotonesDeExmExt (qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan).toArray());
                QPermisosCalif = qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan);
            }else if ( tipo_usuario.equals("consulta") ) {                
                crearBotonesDeMenu (qryIfx.getPermisos (tipo_usuario,usuario,"PRINCIPAL", QEscuela_cveplan).toArray());
                crearBotonesDeGpo (qryIfx.getPermisos (tipo_usuario,usuario,"GRUPO", QEscuela_cveplan).toArray());
                crearBotonesDeExmExt (qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan).toArray());
                QPermisosCalif = qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan);
            }else if ( tipo_usuario.equals("mesa") ) {                
                crearBotonesDeMenu (qryIfx.getPermisos (tipo_usuario,usuario,"PRINCIPAL", QEscuela_cveplan).toArray());
                QPermisosCalif = qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan);
                crearBotonesDeGpo (qryIfx.getPermisos (tipo_usuario,usuario,"GRUPO", QEscuela_cveplan).toArray());
                crearBotonesDeExmExt (qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan).toArray());
            }else if ( tipo_usuario.equals("captura") ) {                
                crearBotonesDeMenu (qryIfx.getPermisos (tipo_usuario,usuario,"PRINCIPAL", QEscuela_cveplan).toArray());    
                crearBotonesDeGpo (qryIfx.getPermisos (tipo_usuario,usuario,"GRUPO", QEscuela_cveplan).toArray());
                crearBotonesDeExmExt (qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan).toArray());
                QPermisosCalif = qryIfx.getPermisos (tipo_usuario,usuario,"CALIFICACIONES", QEscuela_cveplan);
            }
            if(dr.get("permisoDir")!=null && dr.get("permisoDir").equals(1))
                QPermisoDirector(qryIfx.getPermisos (strTipoUsuario,usuario,"OTROS", QEscuela_cveplan).toArray());                                    
            
            dr.put("botonesDeCalif", QPermisosCalif);
            
        }catch (SQLException ex){ dr.put("returnCase",-1); mensaje.General("CONEXION",ex.getMessage(),"", dr); }
        catch (Exception ex){ dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }

    private void crearBotonesDeMenu (Object []permisos)
    {
        Map boton = new LinkedHashMap(), botonesPermitidos = new LinkedHashMap();
        Object dato;
        int tabIndex=3;

        //boton.put("btnImprimeActual", "<li id='btnImprimeActual' title='Reportes'><a href='#' tabindex='3'><img src='../imagenes/navegacion/imprimeActual.png'/><br><label class='lblBotonMenu'>Reportes</label></a></li>");
        
        /*******************  btnPreinscripcion Se descomenta para 01-feb-2024  *****************************/
        boton.put("btnPreinscripcion", "<li id='btnPreinscripcion' title='Preinscripción'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconPreinscripcion' class='icon-preinscripcion iconMenu'></label><br><label class='lblBotonMenu'>Preinscripción</label></a></li>");
        
        /****************** btnNuevoIngreso Comentado el 31-01-2024 se cierra   *****************************/
        boton.put("btnNuevoIngreso", "<li id='btnNuevoIngreso' title='Nuevo ingreso'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconNuevoIngreso' class='icon-nvoingreso iconMenu'></label><br><label class='lblBotonMenu'>Nvo. Ingreso</label></a></li>");
        boton.put("btnCamDeGpo", "<li id='btnCamDeGpo' title='Grupo'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconGrupo' class='icon-grupo iconMenu'></label><br><label class='lblBotonMenu'>Grupo</label></a></li>");
        boton.put("btnDiscapa", "<li id='btnDiscapa' title='Necesidades especiales'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconNecEspeciales' class='icon-necespeciales iconMenu'></label><br><label class='lblBotonMenu'>Nec Esp</label></a></li>");
        boton.put("btnCamDTaller", "<li id='btnCamDTaller' title='Tecnología'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconTaller' class='icon-taller iconMenu'></label><br><label class='lblBotonMenu'>Tecnología</label></a></li>");
        //boton.put("btnCamDAes", "<li id='btnCamDAes' title='Asignatura estatal'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconAsigEstatal' class='icon-asigestatal iconMenu'></label><br><label class='lblBotonMenu'>Asig Estatal</label></a></li>");
        boton.put("btnCamDArte", "<li id='btnCamDArte' title='Arte'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconArtes' class='icon-artes iconMenu'></label><br><label class='lblBotonMenu'>Arte</label></a></li>");
        
        /********************** btnCalif Comentado a inicio de 2023-2024   *****************************/
        boton.put("btnCalif", "<li id='btnCalif' title='Calificaciones'>"
                                    + "<a href='#' tabindex='"+(tabIndex++)+"'><label id='iconCalificaciones' class='icon-calificaciones iconMenu'></label><br><label class='lblBotonMenu'>Calif</label></a>" 
                                    // + "<div class='globoayuda_pointup'><div class='btnCerrarGlobo'>X</div>CAPTURE AQUÍ<BR>Reportes de Evaluación</div>" 
                            + "</li>");   //Comentado SIN_CAPTURA  01-02-2024
        boton.put("btnExalumnos", "<li id='btnExalumnos' title='Exalumnos con examen de regularización para complementaria'>"
                                    + "<a href='#' tabindex='"+(tabIndex++)+"'><label id='iconExalumnos' class='icon-exalumnos iconMenu'></label><br><label class='lblBotonMenu'>Exalumnos</label></a>"
                                    + "<div id='globoAyudaExcalumnos' class='globoayuda_pointup'><div class='btnCerrarGlobo'>X</div>CAPTURE E IMPRIMA AQUÍ<BR>Exámenes de regularización, certificados y REL complementaria de ciclos anteriores.</div>"
                            + "</li>");
        //boton.put("btnDirector", "<li id='btnDirector' title='Escuela'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconEscuela' class='icon-escuela iconMenu'></label><br><label class='lblBotonMenu'>Escuela</label></a></li>");
        //boton.put("btnAlumno", "<li id='btnAlumno' title='Situación del alumno'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconSitAlumno' class='icon-sitalumno iconMenu'></label><br><label class='lblBotonMenu'>Sit Alum</label></a></li>");
        //boton.put("btnGrupos", "<li id='btnGrupos' title='Grupos'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconGrupos' class='icon-grupos iconMenu'></label><br><label class='lblBotonMenu'>Grupos</label></a></li>");
        //boton.put("btnBuskAlum", "<li id='btnBuskAlum' title='Búsqueda de alumno'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconBuscAlum' class='icon-buscalum iconMenu'></label><br><label class='lblBotonMenu'>Busq Alum</label></a></li>");
        //boton.put("btnCertifi", "<li id='btnCertifi' title='Certificados'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconCertifi' class='icon-certifi iconMenu'></label><br><label class='lblBotonMenu'>Certific</label></a></li>");
        //boton.put("btnBoletas", "<li id='btnBoletas' title='Boletas'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconBoletas' class='icon-boletas iconMenu'></label><br><label class='lblBotonMenu'>Rep. Eval.</label></a></li>");
        boton.put("btnReAjustar", "<li id='btnReAjustar' title='Ubicar'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconUbicar' class='icon-ubicar iconMenu'></label><br><label class='lblBotonMenu'>Ubicar</label></a></li>");
        //boton.put("btnModifiCurp", "<li id='btnModifiCurp' title='CURP'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconModifiCurp' class='icon-modificurp iconMenu'></label><br><label class='lblBotonMenu'>CURP</label></a></li>");
        //boton.put("btnRepEvaluacion", "<li id='btnRepEvaluacion' title='Reporte de Evaluación'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconRepEvaluacion' class='icon-repevaluacion iconMenu'></label><br><label class='lblBotonMenu'>Rep. Eval.</label></a></li>");
        boton.put("btnPersonal", "<li id='btnPersonal' title='Personal'>"
                                    + "<a href='#' tabindex='"+(tabIndex++)+"'><label id='iconPersonal' class='icon-personal iconMenu'></label><br><label class='lblBotonMenu'>Personal</label></a>"
                                    //+ "<div class='globoayuda_pointup'><div class='btnCerrarGlobo'>X</div>NUEVO MÓDULO<BR>PERSONAL DIRECTIVO Y DOCENTE</div>"
                                + "</li>");
        //*********************   Opcion para captura de datos del Tutor *************************************/
        boton.put("btnTutor", "<li id='btnTutor' title='Datos del Tutor'>"
                                    + "<a href='#' tabindex='"+(tabIndex++)+"'><label id='iconTutor' class='icon-grupo iconMenu'></label>"                                
                                + "<br><label class='lblBotonMenu'>Tutor</label></a>"                                    
                                + "</li>"); 
        
        boton.put("btnOficializaciones", "<li id='btnOficializaciones' title='Oficializaciones'>"
                                            + "<a href='#' tabindex='"+(tabIndex++)+"'><label id='iconOfics' class='icon-sello iconMenu'></label><br><label class='lblBotonMenu'>Ofic.</label></a>"
                                            //+ "<div class='globoayuda_pointup'><div class='btnCerrarGlobo'>X</div>NUEVO MÓDULO<BR>OFICIALIZACIÓN</div>"
                                        + "</li>");
        boton.put("btnReportes", "<li id='btnReportes' title='Reportes'>"
                                    + "<a href='#' tabindex='"+(tabIndex++)+"'><label id='iconReportes' class='icon-impresora iconMenu'></label><br><label class='lblBotonMenu'>Reportes</label></a>"
                                    //+ "<div class='globoayuda_pointup'><div class='btnCerrarGlobo'>X</div>NUEVO MÓDULO<BR>REPORTES</div>"
                                + "</li>");
        boton.put("btnConfiguraciones", "<li id='btnConfiguraciones' title='Configuraciones'><a href='#' tabindex='"+(tabIndex++)+"'><label id='iconConfigs' class='icon-configs iconMenu'></label><br><label class='lblBotonMenu'>Configs</label></a></li>");

        for (Object permiso : permisos)
            if ((dato = boton.get(permiso))!=null)
                botonesPermitidos.put(permiso, dato);

        dr.put("botonesDeMenu", botonesPermitidos);
    }
    
    private void crearBotonesDeGpo (Object []permisos)
    {
        Map boton = new LinkedHashMap(), botonesPermitidos = new LinkedHashMap();
        Object dato;
        int tabIndex=3;

        
        /*******************  btnPreinscripcion Se descomenta para 01-feb-2024  *****************************/
                      
        boton.put("btnRevGdo", "<li id='libtnRevGdo' title='Para alumnos con promedio de 6 y 7'><a href='#' id='btnRevocacionGdo'>Revocación Gdo</a></li>");
        boton.put("btnConstancia", "<li id='libtnConstancia' title='Para alumnos dados de baja con calificación oficializada'><a href='#' id='btnConstancia'>Const.De Baja</a></li>");
        boton.put("btnEliminarAlu", "<li id='libtnEliminarAlu' title='Elimina de la Base de Datos al alumno seleccionado'><a href='#' id='btnEliminarAlu'>Eliminar</a></li>");

        for (Object permiso : permisos)
            if ((dato = boton.get(permiso))!=null)
                botonesPermitidos.put(permiso, dato);

        dr.put("botonesDeGpo", botonesPermitidos);
    }
    
    private void crearBotonesDeExmExt (Object []permisos)
    {
        Map boton = new LinkedHashMap();
        Object dato;        
        /*******************  btnOfExmExt1ro, btnOfExmExt2do, btnOfExmExt3ro  *****************************/                      
        if(dr.get("superUsuario").equals("si")) {
            boton.put("btnCalifDBim1ro", "<li><a href='#' id='btnCalifDBim1ro' tabindex='202' title='Calificación obtenida de bimestres'>Calif. obtenida de bimestres</a></li>");
            boton.put("btnCalifDBim2do", "<li><a href='#' id='btnCalifDBim2do' tabindex='202' title='Calificación obtenida de bimestres'>Calif. obtenida de bimestres</a></li>");
            boton.put("btnCalifDBim3ro", "<li><a href='#' id='btnCalifDBim3ro' tabindex='202' title='Calificación obtenida de bimestres'>Calif. obtenida de bimestres</a></li>");
            boton.put("btnOfExmExt1ro", "<li><a href='#' id='btnOfExmExt1ro' tabindex='203' title='Oficialización de examenes extraordinarios de 1er grado.' class='icon-sello'>Oficializar Exm.Ext. 1ro</a></li>");
            boton.put("btnOfExmExt2do", "<li><a href='#' id='btnOfExmExt2do' tabindex='203' title='Oficialización de examenes extraordinarios de 2do grado.' class='icon-sello'>Oficializar Exm.Ext. 2do</a></li>");
            boton.put("btnOfExmExt3ro", "<li><a href='#' id='btnOfExmExt3ro' tabindex='203' title='Oficialización de examenes extraordinarios de 3er grado.' class='icon-sello'>Oficializar Exm.Ext. 3ro</a></li>");
            boton.put("btnActualizaPromNivel", "<li><a href='#' id='btnActualizaPromNivel'  tabindex='201' title='Calcula y actualiza el promedio del nivel.'><label class='iconBtnSincronizar iconBtnRedondo  middleHoriz '></label>Promedio de Nivel</a></li>");
                        
            for (Object permiso : permisos)
                if ((dato = boton.get(permiso))!=null)
                    dr.put(permiso, dato);        
        }
    }
    
    private void QPermisoDirector(Object []permisos){
        for (Object permiso : permisos)
            if(permiso.toString().equals("cambioDir"))
                dr.put("cambioDir",1);
    }
    
    private void setTipoConexion (HttpServletRequest request) throws Exception
    {
        String path;
        SICEEO_SourcePath sourcePath;
        SICEEO_FileReader fileReader;
        JsonObject jsonObj;

        try{
            HttpSession session = request.getSession(false);
            Object tipoconexion = session.getServletContext().getAttribute("tipoconexion");
            if (tipoconexion==null){
                fileReader = new SICEEO_FileReader(this.pathConfigsSICEEO);
                try {
                    jsonObj = dm.getJson(fileReader.leerFichero(true));
                }catch (FileNotFoundException ex){
                    sourcePath = new SICEEO_SourcePath();
                    path = sourcePath.getLevelUpPath(6)+sourcePath.getSeparator()+"siceeoConfigs"+sourcePath.getSeparator()+"configsSICEEO.sco";
                    fileReader.setUrl(path);
                    jsonObj = dm.getJson(fileReader.leerFichero(true));
                }
                try{
                    session.getServletContext().setAttribute("tipoconexion", jsonObj.get("connectionType").getAsString());
                }catch (NullPointerException ex) {
                    mensaje.loginUser("SIN_VAR_CONFIG", "", "", dr); 
                    throw new Exception (""+dr.get("mensaje"));
                }
            }
        }catch (FileNotFoundException ex){ 
            mensaje.loginUser("SIN_ARCHIVO_CONFIG", "", "", dr); 
            throw new Exception (""+dr.get("mensaje"));
        }
    }
}
