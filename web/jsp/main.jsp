<!DOCTYPE html>
<html lang="es">
    <head>
        <%
            java.util.Calendar fecha = java.util.Calendar.getInstance();
            String hora = "?t="+fecha.get(java.util.Calendar.YEAR)+"."+fecha.get((java.util.Calendar.MONTH))+"."+fecha.get(java.util.Calendar.DATE)+"."+fecha.get(java.util.Calendar.HOUR_OF_DAY)+"."+fecha.get(java.util.Calendar.MINUTE);
            hora = "?v=13.1.2";
            //hora = "";
        %>
        <meta http-equiv="Expires" content="0"/>
        <meta http-equiv="Last-Modified" content="0"/>
        <meta http-equiv="Cache-Control" content="no-cache, mustrevalidate"/>
        <meta http-equiv="Pragma" content="no-cache"/>
        <meta http-equiv="Cache" content="no-cache"/>
        <!--================= CONFIGURACIÓN DE LA PÁGINA ================-->
        <!--% response.addHeader("pragma", "no-cache"); %-->
        <!--% response.addHeader("cache-control", "no-store");%-->
        <title>SICEEO</title>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, user-scalable=1, initial-scale=1.0, minimum-scale=1.0"/> <!-- Para que funcione en dispositivos moviles-->
        <!--meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Cache-Control" content="no-store" />
        <meta http-equiv="Expires" content="-1" /-->

        <!--========================== ESTILOS ==========================-->
        <!----- Librerías ------------------------------------------------->
        <!--::::::::::::: Para los Alerts, Prompt, Confirm y mas --> <!-- Dependencies -->
        <link rel="stylesheet" href="../estilos/libs/jquery.alerts.css"/>
        <!--::::::::::::: Para Ventanas Modales cuando Cargan Los Datos -->
        <!--link rel='stylesheet' href='estilos/basic.css'/-->
        <!--link rel="stylesheet" href="../estilos/libs/icomoons/webfonts.css" /-->
        <link rel="stylesheet" href="../estilos/libs/IconFontSICEEO/iconfontSICEEO.css" />
        <!--link rel="stylesheet" href="../estilos/libs/normalize.css" /-->
        <!----- Sistema --------------------------------------------------->
        <link rel="stylesheet" href="../estilos/GlobalCss/EstilosGenerales.css<%=hora%>" />
        <link rel="icon" type="image/gif" href="../imagenes/generales/2.gif" />
        <link rel="stylesheet" href="../estilos/main.css<%=hora%>"/>
        
        <link rel="stylesheet" href="../estilos/Preinscripcion/Preinscripcion.css<%=hora%>" >
        <link rel="stylesheet" href="../estilos/NuevoIngreso/NuevoIngreso.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/NuevoIngreso/MasDeUno.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Grupo/CamDeGpo.css<%=hora%>" >
        <link rel="stylesheet" href="../estilos/Discap/discap.css<%=hora%>" >
        <link rel="stylesheet" href="../estilos/CambioDeTaller/camDTaller.css<%=hora%>" >
        <link rel="stylesheet" href="../estilos/CambioDeAes/camDAes.css<%=hora%>" >
        <link rel="stylesheet" href="../estilos/CambioDeArte/camDArte.css<%=hora%>" >
        <link rel="stylesheet" href="../estilos/Calificaciones/XBimOPromFin.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Calificaciones/CalifSecXBim.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Calificaciones/PromGralSec2012.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Calificaciones/PromGralSec.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Calificaciones/BimXAlum.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Calificaciones/SecHist.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Calificaciones/CaptuRepEval.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Ubicar/Ajustar.css<%=hora%>" >
        <link rel="stylesheet" href="../estilos/RepEvaluacion/RepEvaluacion.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/CambioDeCurp/ModifiCurp.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Configuraciones/Configuraciones.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Configuraciones/GestionarAvisos.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Configuraciones/Permisos.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Contacto/Contacto.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Avisos/Avisos.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/EvalPreescolar/EvalPreescolar.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Oficializaciones/Oficializar.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Personal/Personal.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Tutor/CaptuTutorAlum.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Reportes/Reportes.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Oficializaciones/Desoficializar.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Reportes/Complementaria.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Reportes/SelMesCompl.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Calificaciones/RevisaGpo.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Exalumnos/Exalumnos.css<%=hora%>" />
        <link rel="stylesheet" href="../estilos/Exalumnos/SelComplExalum.css<%=hora%>" />
    </head>
    <body>
        <div id="winPrincipal">
            <nav id="mnuSICEEO">
                <ul data-liffect="slideLeft" ></ul>
            </nav>
            <div id="pnlDatosInfoSistema">
                <ul>
                    <li title="Ciclo escolar actual en el que estará ingresando toda su información en SICEEO."><label id="iconCicloActivo" class="icon-banderin iconInfo"></label><label id="lblCicloActivo"> ... </label>  </li>
                    <li title="Usuario"><label id="iconUsuario" class="icon-usuarios iconInfo"></label><label id="lblUsuario"> ... </label>  </li>
                    <li title="Región administrativa"><label id="iconUnidad" class="icon-oaxaca iconInfo"></label><label id="lblUnidad"> ... </label>  </li>
                    <li title="Hora de ingreso a SICEEO"><label id="iconFecha" class="icon-calendreloj iconInfo"></label><label id="lblFecha"> ... </label></li>
                    <li title="Versión"><label id="iconVersionSis" class="icon-engrane iconInfo"></label><label id="lblVersionSis"> Versión 15.8.5 </label></li>
                </ul>
            </div>
            <div id="pnlBusquedaCCT">
                <label>CCT:  </label>
                <input type="text" id="txtBusquedaCCT" tabindex="50"  value="" maxlength="10" autofocus="autofocus"/>
                <a id="btnBusquedaCCT" tabindex="51" ><label id="iconBusquedaPrincipal" class="icon-lupa" title="Ejecutar búsqueda de cct"></label></a>
                <div id="btnCerrarSesion" tabindex="55" title="Cerrar sesión"><label id="ibtnCerrarSesion" class="icon-outdoor"></label><label id="lbtnCerrarSesion">Salir</label></div>
                <div id="btnContacto" tabindex="54" title="Ver datos de contacto"><label id="ibtnContacto" class="icon-telefono"></label><label id="lbtnContacto">Contacto</label></div>
                <!--div id="btnManual" tabindex="53" title="Manual de operación del SICEEO"><label id="ibtnManual" >M</label><label id="lbtnManual">Manual</label></div-->
                <div id="btnCalendarioProcesos" tabindex="52" title="Calendario de actividades de Control Escolar">
                    <label id="ibtnCalendarioProcesos" >C</label><label id="lbtnCalendarioProcesos">Calendario</label>
                    <div id='globoAyudaCalenProc' class='globoayuda_pointright'><div class='btnCerrarGlobo'>X</div>CONOZCA EL CALENDARIO<BR><label id="lblCalendario"></label></div>
                </div>
                <div id="btnNormatividad" tabindex="56" title="Normatividad del SICEEO"><label id="ibtnNormatividad" >N</label><label id="lbtnNormatividad">Normatividad</label></div>
                <div id="btnCircular" tabindex="56" title="Circular"><label id="ibtnCircular" >Ci</label><label id="lbtnCircular">Circular</label></div>
                <div id="btnAnexos" tabindex="56" title="Anexos (formatos)"><label id="ibtnAnexos" >A</label><label id="lbtnAnexos">Anexos</label></div>  
                <div id="btnEER" tabindex="56" title="Secudarias EER"><label id="ibtnEER" >E</label><label id="lbtnEER">EER Secundarias</label></div>
            </div>

            <div id="pnlPrincipalWorkArea">
                <div id="gridTable"> <div id="pnlTblPrincipal"> <div id="scrlPrincipal" class="scrollTable"></div> </div> </div>
                <div id="frmwNuevoIngreso" style="display:none"></div>
                <div id="frmwCamDeGpo" style="display:none"></div>
                <div id="frmwDiscapa" style="display:none"></div>
                <div id="frmwCamDTaller" style="display:none"></div>
                <div id="frmwCamDAes" style="display:none"></div>
                <div id="frmwCamDArte" style="display:none"></div>
                <div id="frmwRepEvaluacion" style="display:none"></div>
                <div id="frmwXBimOPromFin" style="display:none"></div>
                <div id="frmwXCalevalORepeval" style="display:none"></div>
                <div id="frmwCalifSecXBim" style="display:none"></div>
                <div id="frmwCaptuRepEval" style="display:none"></div>
                <div id="frmwCaptuTutorAlum" style="display:none"></div>
                <div id="frmwEvalPreescolar" style="display:none"></div>
                <div id="frmwSecHist" style="display:none"></div>
                <div id="frmwConfiguraciones" style="display:none"></div>
                <div id="frmwGestionarAvisos" style="display:none"></div>
                <div id="frmwPreinscripcion" style="display:none"></div>
                <div id="frmwContacto" style="display:none"></div>
                <div id="frmwMasDeUno" style="display:none"></div>
                <div id="frmwAjustar" style="display:none"></div>
                <div id="frmwModifCurp" style="display:none"></div>
                <div id="frmwOficializar" style="display:none"></div>
                <div id="frmwPersonal" style="display:none"></div>
                <div id="frmwReportes" style="display:none"></div>
                <div id="frmwExalumnos" style="display:none"></div>
                
                <div id="mwfmPermisos" class="mwfModal" style="display:none"></div>
                <div id="mwfmDesoficializar" class="mwfModal" style="display:none"></div>
                <div id="mwfmComplementaria" class="mwfModal" style="display:none"></div>
                <div id="mwfmExmExtraordinarios" class="mwfModal" style="display:none"></div>
                <div id="mwfmAvisos" class="mwfModal" style="display:none"></div>
            </div>
            
        </div>
        <footer id="pieImagenes"></footer>
        <!--div style="width:0;height:0;">
            <svg  height='0' width='0' version="1.1" baseProfile="full" xmlns="http://www.w3.org/2000/svg">
                <filter id="grayscale">
                    <fecolormatrix type="matrix" values="0.3333 0.3333 0.3333 0 0 0.3333 0.3333 0.3333 0 0 0.3333 0.3333 0.3333 0 0 0 0 0 1 0"/>
                </filter>
            </svg>
        </div-->
        
        
        <!--========================== SCRIPTS ==========================-->
        <!----- Librerías ------------------------------------------------->
        <script src="../js/libs/prefixfree.min.js"></script> <!--Siempre debe ir despues de los css y antes de la librería jquery-->
        <script src="../js/libs/jq.1.9.1_development.js"></script>
        <script src="../js/libs/jquery.hotkeys.js"></script>
        <!--::::::::::::: Para los Alerts, Prompt, Confirm y mas --> <!-- Dependencies -->
        <script src="../js/libs/jquery.ui.draggable.js"></script>    
        <script src="../js/libs/jquery.alerts.js"></script>
        <!--::::::::::::: Para Ventanas Modales cuando Cargan Los Datos -->
        <!--script src='js/basic.js'></script-->
        <!--script src='js/libs/jquery.simplemodal.js'></script-->
        <script src='../js/libs/jquery.maskedinput-1.4.0.min.js'></script>

        <!----- Sistema --------------------------------------------------->
        <script src="../js/GlobalJs/Tabla.js"></script>
        <script src="../js/GlobalJs/Mensajes.js<%=hora%>"></script>
        <script src="../js/GlobalJs/JsGenerales.js<%=hora%>"></script>
        <script type='text/javascript'>
            var t, i;
            t = "3c73637269707420747970653d22746578742f6a61766173637269707422207372633d222e2e2f6a732f476c6f62616c4a732f637572702e6a73223e3c2f7363726970743e"; for (i = 0; i < t.length; i += 2) { document.write(String.fromCharCode(parseInt(t.substr(i, 2), 16))); }
        </script>
        
        <script src="../js/main.js"></script>
        <script src="../js/Preinscripcion/Preinscripcion.js<%=hora%>"></script>
        <script src="../js/NuevoIngreso/NuevoIngreso.js<%=hora%>"></script>
        <script src="../js/NuevoIngreso/NuevoIngresoPre.js<%=hora%>"></script>
        <script src="../js/NuevoIngreso/NuevoIngresoPri.js<%=hora%>"></script>
        <script src="../js/NuevoIngreso/NuevoIngresoSec.js<%=hora%>"></script>
        <script src="../js/NuevoIngreso/MasDeUno.js<%=hora%>"></script>
        <script src="../js/Grupo/CamDeGpo.js<%=hora%>"></script>
        <script src="../js/Discap/discap.js<%=hora%>"></script>
        <script src="../js/CambioDeTaller/camDTaller.js<%=hora%>"></script>
        <script src="../js/CambioDeAes/camDAes.js<%=hora%>"></script>
        <script src="../js/CambioDeArte/camDArte.js<%=hora%>"></script>
        <script src="../js/Calificaciones/XBimOPromFin.js<%=hora%>"></script>
        <script src="../js/Calificaciones/CalifSecXBim.js<%=hora%>"></script>
        <script src="../js/Calificaciones/PromGralSec2012.js<%=hora%>"></script>
        <script src="../js/Calificaciones/PromGralSec.js<%=hora%>"></script>
        <script src="../js/Calificaciones/BimXAlum.js<%=hora%>"></script>
        <script src="../js/Calificaciones/SecHist.js<%=hora%>"></script>
        <script src="../js/Calificaciones/CaptuRepEval.js<%=hora%>"></script>
        <script src="../js/RepEvaluacion/RepEvaluacion.js<%=hora%>"></script>
        <script src="../js/CambioDeCurp/ModifiCurp.js<%=hora%>"></script>
        <script src="../js/Configuraciones/Configuraciones.js<%=hora%>"></script>
        <script src="../js/Configuraciones/GestionarAvisos.js<%=hora%>"></script>
        <script src="../js/Configuraciones/Permisos.js<%=hora%>"></script>
        <script src="../js/Contacto/Contacto.js<%=hora%>"></script>
        <script src="../js/Ubicar/Ajustar.js<%=hora%>"></script>
        <script src="../js/Avisos/Avisos.js<%=hora%>"></script>
        <script src="../js/EvalPreescolar/EvalPreescolar.js<%=hora%>"></script>
        <script src="../js/Oficializaciones/Oficializar.js<%=hora%>"></script>
        <script src="../js/Personal/Personal.js<%=hora%>"></script>
        <script src="../js/Tutor/Tutor.js<%=hora%>"></script>
        <script src="../js/Reportes/Reportes.js<%=hora%>"></script>
        <script src="../js/Oficializaciones/Desoficializar.js<%=hora%>"></script>
        <script src="../js/Reportes/Complementaria.js<%=hora%>"></script>
        <script src="../js/Reportes/SelMesCompl.js<%=hora%>"></script>
        <script src="../js/Calificaciones/RevisaGpo.js<%=hora%>"></script>
        <script src="../js/Exalumnos/Exalumnos.js<%=hora%>"></script>
        <script src="../js/Exalumnos/SelComplExalum.js<%=hora%>"></script>
    </body>
</html>