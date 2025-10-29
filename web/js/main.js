var tiempoInactivo;
 var preLoadTimer; 
 
 /****** Variales Globales por revisar *******/
var usuario;
var idalu="";
var ciclocalendario="";
var Trim1=false,Trim2=false,Trim3=false;
/* **********************************/

$(document).on("ready",inicioDeMain);

function inicioDeMain ()
{
    resetLoading ();                                                             //Por si se queda bloqueado el cerrarLoading, cuando refresque el usuario entonces desbloqueamos
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    if (sisVars === null){
        btnCerrarSesion_ActionPerformed ();
    }else{
        cargarLoading();
        permisos ();
        initEventsMain();
        initInformacion ();
        iniciarTiempoInactivo();
        if (typeof sisVars.btnBusquedaCCT_Click!=="undefined"){
            $("#txtBusquedaCCT").val(sisVars.cctdefault);
            btnBusquedaCCT_Click ();
        }
        cerrarLoading();
        mostrarGlogoAyuda_Principal(); 
        mwffAvisos_Show ({onClose:mostrarGlogoAyuda_Principal}); //estaba comentado - Decomentado el 10-julio-2018(ely)
    }
}

function initEventsMain()
{        
    $("#txtBusquedaCCT").bind("keydown", function(e){ if(e.which === 13)  btnBusquedaCCT_Click (e); });
    $("#btnBusquedaCCT").bind("keydown", function(e){ if(e.which === 13)  btnBusquedaCCT_Click (e); });
    // Eventos de la barra de navegación  (---MOUSE----)
    $('#btnNuevoIngreso').on ('click', function(){ btnNuevoIngreso_Click(); }); // COmentado SIN_CAPTURA 31-01-2024
    $('#btnCamDeGpo').on ('click', function(){ btnCamDeGpo_Click(); });
    $('#btnDiscapa').on ('click', function(){ btnDiscapa_Click(); });
    $('#btnCamDTaller').on ('click', function(){ btnCamDTaller_Click(); });
    //$('#btnCamDAes').on ('click', function(){ btnCamDAes_Click(); });
    $('#btnCamDArte').on ('click', function(){ btnCamDArte_Click(); });
    //$('#btnRepEvaluacion').on ('click', function(){ btnRepEvaluacion_Click(); });
    $('#btnCalif').on ('click', function (){ btnCalif_Click(); });  //Se inhabilito 01-02-2024
    $('#btnExalumnos').on('click',function(){ btnExalumnos_Click(); });
    $('#btnConfiguraciones').on('click',function(){ btnConfiguraciones_Click(); });
    
    $('#btnPreinscripcion').on('click',function(){ btnPreinscripcion_Click(); }); // habilitado el 01-02-2024
    $('#btnReAjustar').on('click',function(){ btnReAjustar_Click(); });
    $('#btnOficializaciones').on('click',function(){ btnOficializaciones_Click(); });
    $('#btnPersonal').on('click',function(){ btnPersonal_Click(); });
    $('#btnTutor').on('click',function(){ btnTutor_Click(); });   // para datos del tutor 2023-2024*/
    $('#btnReportes').on('click',function(){ btnReportes_Click(); });
    
    $('#btnBusquedaCCT').on('click',function(e){ btnBusquedaCCT_Click (e); });
    $('#btnCerrarSesion').on('click',function(){ btnCerrarSesion_ActionPerformed(); });
    $('#btnContacto').on('click',function(){ btnContacto_Click(); });
    /*$('#btnManual').on('click',function(){ btnManual_Click(); });*/
    $('#btnNormatividad').on('click',function(){ btnNormatividad_Click(); });
    $('#btnCircular').on('click',function(){ btnCircular_Click(); });
    $('#btnAnexos').on('click',function(){ btnAnexos_Click(); });
    $('#btnEER').on('click',function(){ btnEER_Click(); });
    $('#btnCalendarioProcesos').on('click',function(){ btnCalendarioProcesos_Click(); });
    
    
    
    // Efectos de la Lista de Botones
    var retardo=100;
    $("ul[data-liffect] li").each(function (i) {
        $(this).attr("style", "-webkit-animation-delay:" + i * retardo + "ms;"
                + "-moz-animation-delay:" + i * retardo + "ms;"
                + "-o-animation-delay:" + i * retardo + "ms;"
                + "animation-delay:" + i * retardo + "ms;");
        if (i === $("ul[data-liffect] li").size() -1) {
            $("ul[data-liffect]").addClass("play");
        }
    });
}

function permisos ()
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    if (sisVars){        
        for (var boton in sisVars.botonesDeMenu) {                    
            $('#mnuSICEEO ul').append(sisVars.botonesDeMenu[boton]);
        }
        /*if(sisVars.QEscuela_cveplan==="2"){            //Para ocultar boton de Calificaciones en secundaria
                      $("#btnCalif").remove();              
        }*/
    }
}

function initInformacion ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars"));
    $('#lblCalendario').text('de actividades de Control Escolar ciclo '+sisVars.cicescini+'-'+(parseInt(sisVars.cicescini)+1)+".");
    $('#lblCicloActivo').text(sisVars.cicescini+' - '+(parseInt(sisVars.cicescini)+1));
    $('#lblUsuario').text(sisVars.usuario.toUpperCase());
    $('#lblUnidad').text(sisVars.unidad);
    $('#lblIp').text(sisVars.ip);
    $('#lblFecha').text(sisVars.fecha);
    if ( $('#lblVersionSis').html().trim().indexOf(sisVars.versionSis) === -1  || sisVars.versionSis!=="15.9.3") {
        $("#lblVersionSis").css("color","red");
        mensaje.Principal("VERSION_NO_ACTUALIZADA",sisVars.versionSis);
    } else {
        $("#lblVersionSis").css("color","blue");
    }
    
    if(sisVars.superUsuario!=="si" && sisVars.QEscuela_cveplan!=="2"){
        //alert(sisVars.superUsuario); //comentado hoy 06-09-2023
        $('#btnEER').addClass("ocultarDIV");
    }
}

mostrarGlogoAyuda_Principal = function ()
{
    //var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //if (sisVars.showGloboNewModulo){
        aparecerGlogoAyuda ("#btnExalumnos .globoayuda_pointup",4000);
        aparecerGlogoAyuda ("#btnCalendarioProcesos .globoayuda_pointright",19000);
        aparecerGlogoAyuda ("#txtFechaIng .globoayuda_pointup",4000);
        //$("#btnCalendarioProcesos .globoayuda_pointright").fadeIn(1000).delay(7000);
        /*aparecerGlogoAyuda ("#btnCalif .globoayuda_pointup",1000);
        aparecerGlogoAyuda ("#btnOficializaciones .globoayuda_pointup",14500);
        aparecerGlogoAyuda ("#btnPersonal .globoayuda_pointup",26500);
        aparecerGlogoAyuda ("#btnReportes .globoayuda_pointup",38500);*/
        
    /*    sisVars.showGloboNewModulo=false;
        sessionStorage.sistemVars = JSON.stringify(sisVars);
    }*/
};

function iniciarTiempoInactivo() 
{
    $(this).on("mousemove",function(){ 
         reiniciarTiempoInactivo() ;
    });
    $(this).on("keypress",function(){ 
         reiniciarTiempoInactivo() ;
    });
    $(this).on("click",function(){ 
         reiniciarTiempoInactivo() ;
    });
}
 
function reiniciarTiempoInactivo() 
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //Limpiamos si es que existe un tiempodefinido
    window.clearTimeout(tiempoInactivo);
    //Reiniciamos el tiempo
    tiempoInactivo=window.setTimeout("location.href='../cerrarSesion.jsp'", sisVars.tiempoEspera*1000); //Lo convertimos a milisegundos (La variable tiempoEspera debe estar en segundos)
}

/********************************************************************************/

function remueveContenidoDeFormularios(){
    var contenedoresDeFormulario = Array(
            "frmfPreescolar","frmfPrimaria","frmfSecundaria", "frmfMasDeUno",                                                               // FORMULARIOS PARA NUEVOS INGRESOS
            "formularioGrupo","formularioCurpsIguales",                                                                                     // FORMULARIO DE GRUPO
            "frmfXBimOPromFin","frmfCalifSecXBim","frmfBimXAlum","frmfPromGralSec","frmfPromGralSec2012","frmfSecHist",  // FORMULARIO DE Calificaciones
            "frmfConfiguraciones", "frmfCamDeGpo", "frmfDiscapa", "frmfCamDTaller", "frmfCamDAes", "frmfCamDArte", 
            "frmfPreinscripcion", "frmfAjustar", "frmfRepEvaluacion", "frmfContacto", "frmfModifCurp", "frmfEvalPreescolar", "frmfOficializar",
            "frmfCaptuRepEval", "frmfPersonal", "frmfReportes, frmfGestionarAvisos", "frmfExalumnos"
        );
    for (var i=0; i<contenedoresDeFormulario.length; i++)
    {
        if($('#'+contenedoresDeFormulario[i]).length)                           // Verificando si el contenedor existe
            $('#'+contenedoresDeFormulario[i]).remove();                        // eliminar el contenedor que existe
    }
}
function ocultarFormulariosGenerales(){
    var formularios = Array( "frmwNuevoIngreso","frmwCamDeGpo", "frmwDiscapa", "frmwCamDTaller", "frmwCamDAes", "frmwCamDArte",
                        "frmwXBimOPromFin","frmwCalifSecXBim","frmwSecHist","frmwConfiguraciones","frmwCamDeGpo","frmwPreinscripcion",
                        "frmwAjustar", "frmwMasDeUno", "frmwRepEvaluacion", "frmwContacto","frmwModifCurp","frmwEvalPreescolar", "frmwOficializar",
                        "frmwCaptuRepEval","frmwCaptuTutorAlum", "frmwPersonal", "frmwReportes, frmwGestionarAvisos", "frmwExalumnos");
    for (var i=0; i<formularios.length; i++)
    {
        if($('#'+formularios[i]).length)                                        // Verificando si el formulario está visible
            object_setVisible (false,formularios[i]);                                     // Ocultamos el formulario
    }
}

function irAVentanaPrincipal (){
    remueveContenidoDeFormularios();
    ocultarFormulariosGenerales();
    object_setVisible (true,"gridTable");
}

/********************************************************************************/

function btnNuevoIngreso_Click()
{
    var mensaje = new Mensajes ();
    
    if (!$("#btnNuevoIngreso").hasClass("btnMnuSiCEEBDeshabilitado")){
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            
            if (posSelActual>=0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","grado","grupo","cveplan","cveprograma","cveturno","modalidad","cicescini"], null, "JSON");
                NuevoIngreso_Show(tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.grado, tblPrincipal.grupo, tblPrincipal.cveplan, tblPrincipal.cveprograma, tblPrincipal.cveturno, tblPrincipal.modalidad, tblPrincipal.cicescini);
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        
        
        //event.preventDefault(); 
        //event.stopImmediatePropagation();
    }
}
//************************  Modulo de datos del tutor 2023-2024 *******************//
function btnTutor_Click()
{
    if (!$("#btnTutor").hasClass("btnMnuSiCEEBDeshabilitado")){
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            if (posSelActual>=0) {
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","cveturno","grado","grupo","cveplan","cicescini","modalidad"], null, "JSON");
                //frmwPersonal_Show(tblPrincipal_selRow);
                //Aqui va datos del tutor
                frmwTutor_Show(tblPrincipal.cicescini, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.cveplan, tblPrincipal.grado, tblPrincipal.grupo);
                
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        
        event.preventDefault(); 
        event.stopImmediatePropagation();
    }
}


function btnCamDeGpo_Click()
{
    if (!$("#btnCamDGpo").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            
            if (posSelActual>=0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","modalidad","cct","grado","grupo","cveplan","cveturno","nombre","cicescini"], null, "JSON");
                frmwCamDeGpo_Show(tblPrincipal.idcct, tblPrincipal.modalidad, tblPrincipal.cct, tblPrincipal.grado, tblPrincipal.grupo, tblPrincipal.cveplan, tblPrincipal.cveturno, tblPrincipal.nombre, tblPrincipal.cicescini);
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        //event.preventDefault();
    }
}

function btnDiscapa_Click ()
{
    if (!$("#btnDiscapa").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            
            if (posSelActual>=0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","grado","grupo","cveplan","cicescini"], null, "JSON");
                frmwDiscapa_Show(tblPrincipal.cicescini, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.cveplan, tblPrincipal.grado, tblPrincipal.grupo);
            }else 
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        //event.preventDefault();
    }
}

function btnCamDTaller_Click ()
{
    if (!$("#btnCamDTaller").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            
            if (posSelActual>=0) {
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","modalidad","grado","grupo","cveplan","cicescini","cveprograma"], null, "JSON");
                if (tblPrincipal.cveplan === "2")
                    frmwCamDTaller_Show(tblPrincipal.cicescini, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.modalidad, tblPrincipal.cveplan, tblPrincipal.grado, tblPrincipal.grupo,tblPrincipal.cveprograma);
                else {
                    mensaje.Principal("NO_ES_SECU");
                    irAVentanaPrincipal();
                }
            } else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
    }
}

function btnCamDAes_Click ()
{
    if (!$("#btnCamDAes").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            
            if (posSelActual>=0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","grado","grupo","cveplan","cicescini"], null, "JSON");
                
                if (tblPrincipal.cveplan === "2" && tblPrincipal.grado === "1")
                    frmwCamDAes_Show(tblPrincipal.cicescini, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.grado, tblPrincipal.grupo);
                else {
                    mensaje.Principal("NO_ES_SECU_O_1G");
                    irAVentanaPrincipal();
                }
                    
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
    }
}

function btnCamDArte_Click ()
{
    if (!$("#btnCamDArte").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            
            if (posSelActual>=0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","grado","grupo","cveplan","cicescini"], null, "JSON");
                if (tblPrincipal.cveplan === "2")
                    frmwCamDArte_Show(tblPrincipal.cicescini, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.grado, tblPrincipal.grupo);
                else {
                    mensaje.Principal("NO_ES_SECU");
                    irAVentanaPrincipal();
                }
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
    }
}

function btnRepEvaluacion_Click()
{
    if (!$("#btnRepEvaluacion").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            
            if (posSelActual>=0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["cveunidad","cvezona","idcct","cct","grado","grupo","cicescini","modalidad","nombre","cveturno","cveplan","cveprograma"], null, "JSON");
                frmwRepEvaluacion_Show(tblPrincipal.cveunidad, tblPrincipal.cvezona, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.modalidad, tblPrincipal.grado, tblPrincipal.grupo, tblPrincipal.cicescini, tblPrincipal.nombre, tblPrincipal.cveturno, tblPrincipal.cveplan, tblPrincipal.cveprograma);
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        //event.preventDefault();
        //event.stopImmediatePropagation();
    }
}

function btnCalif_Click()
{
    if (!$("#btnCalif").hasClass("btnMnuSiCEEBDeshabilitado"))
    {        
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            if (posSelActual>=0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","modalidad","cct","nombre","cveplan","grado","grupo","cicescini"], null, "JSON");
                if (tblPrincipal.cveplan==='3')
                    frmwEvalPreescolar_Show (true, tblPrincipal.idcct, tblPrincipal.modalidad, tblPrincipal.cct, tblPrincipal.nombre, tblPrincipal.cveplan, tblPrincipal.grado, tblPrincipal.grupo, tblPrincipal.cicescini);
                else //if(tblPrincipal.cveplan==='1') // Condicion temporal para secundarias, mientras quedan los talleres. despues del else
                    frmwXBimOPromFin_Show ();
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        event.preventDefault();
    }
}

function btnExalumnos_Click ()
{
    if (!$("#btnExalumnos").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var numRows = tabla.getNumRows('tblPrincipal');
            if (numRows>0){
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","modalidad","cct","cveplan","cicescini","grado","grupo"], null, "JSON"); //tabla.getRow("tblPrincipal",numRows-1,["idcct","modalidad","cct","cveplan","cicescini"], null, "JSON")
                if (tblPrincipal.grado === "3")
                    frmwExalumnos_Show (tblPrincipal);/*{idcct:"1826", modalidad:"DES", cct:"20DES0083C", cveplan:"2", cicescini:"2016"}*/
                else
                    mensaje.Exalumnos ("GRADO_SEL_INCORRECTO");
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        event.preventDefault();
    }
}

function btnConfiguraciones_Click()
{
    if (!$("#btnConfiguraciones").hasClass("btnMnuSiCEEBDeshabilitado")){
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();        
        frmwConfiguraciones_Show();
        event.preventDefault(); 
        event.stopImmediatePropagation();
    }
}

function btnPreinscripcion_Click()
{
    if (!$("#btnPreinscripcion").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            if (posSelActual>=0) {
                var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","modalidad","cct","nombre","cveplan","grado","cicescini"], null, "JSON");
                
                if (tblPrincipal.grado === "1" || (tblPrincipal.cveplan==="3"))
                    frmwPreinscripcion_Show (tblPrincipal.idcct, tblPrincipal.modalidad, tblPrincipal.cct, tblPrincipal.nombre, tblPrincipal.cveplan, tblPrincipal.grado, tblPrincipal.cicescini);
                else {
                    mensaje.Principal("NO_PREINSCRIBIBLE");
                    //irAVentanaPrincipal();
                }
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        event.preventDefault();
    }
}

function btnOficializaciones_Click()
{
    if (!$("#btnOficializaciones").hasClass("btnMnuSiCEEBDeshabilitado")){
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var tblPrincipal_selRow = tabla.getSelectedRow("tblPrincipal",["idcct","cct","cveturno","grado","grupo","cveplan","cicescini","modalidad"], null, "JSON");
            frmwOficializar_Show(tblPrincipal_selRow);
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        
        event.preventDefault(); 
        event.stopImmediatePropagation();
    }
}

function btnPersonal_Click()
{
    if (!$("#btnPersonal").hasClass("btnMnuSiCEEBDeshabilitado")){
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            if (posSelActual>=0) {
                var tblPrincipal_selRow = tabla.getSelectedRow("tblPrincipal",["idcct","cct","cveturno","grado","grupo","cveplan","cicescini","modalidad"], null, "JSON");
                frmwPersonal_Show(tblPrincipal_selRow);
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        
        event.preventDefault(); 
        event.stopImmediatePropagation();
    }
}

function btnReportes_Click()
{
    if (!$("#btnReportes").hasClass("btnMnuSiCEEBDeshabilitado")){
        var mensaje = new Mensajes ();
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        
        if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
            var tabla = new Tabla();
            var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
            if (posSelActual>=0) {
                var tblPrincipal_selRow = tabla.getSelectedRow("tblPrincipal",["cicescini","cveplan","idcct","cct","modalidad","cveturno","grado","grupo","cveunidad"], null, "JSON");
                frmwReportes_Show(tblPrincipal_selRow);
            }else
                mensaje.Principal("SELEC", "centro de trabajo");
        }else // La tabla no existe
            mensaje.Principal("SN_BUSQ", "");
        
        event.preventDefault(); 
        event.stopImmediatePropagation();
    }
}

function btnReAjustar_Click()
{
    if (!$("#btnReAjustar").hasClass("btnMnuSiCEEBDeshabilitado"))
    {
        remueveContenidoDeFormularios();
        ocultarFormulariosGenerales();
        frmwAjustar_Show ("gridTable", null, null, null);
        event.preventDefault();
    }
}

function btnContacto_Click ()
{
    remueveContenidoDeFormularios();
    ocultarFormulariosGenerales();
    frmwContacto_Show ();
}

function btnCerrarSesion_ActionPerformed ()
{
    $.each(sessionStorage,function(c,v) {
        sessionStorage[c] = null;
        delete sessionStorage[c];
    });
    window.open('../cerrarSesion.jsp','_parent');
}

function btnManual_Click ()
{
    window.open('http://ieepoplaneacionedu.info/descargas/siceeo/manual/Manual_SICEEO.pdf');
}

function btnNormatividad_Click ()
{
    window.open('http://ieepoplaneacionedu.info/descargas/siceeo/normatividad/Normatividad.zip');
}

function btnCircular_Click ()
{
    window.open('http://ieepoplaneacionedu.info/descargas/siceeo/OFICIOS_ACUERDOS_CIRCULARES.pdf');
}

function btnAnexos_Click ()
{
    window.open('http://ieepoplaneacionedu.info/descargas/siceeo/Anexos.zip');
}

function btnEER_Click ()
{
    window.open('http://ieepoplaneacionedu.info/descargas/siceeo/EER.zip');
}

function btnCalendarioProcesos_Click ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var nombreArchivo=""; 
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars"));
    
    
    if($('#tblPrincipal').length){ // Verificando si la tabla ya existe
        var tblPrincipal_selRow = tabla.getRow("tblPrincipal",0,["cveplan"], null, "JSON");
        switch (tblPrincipal_selRow.cveplan)
        {
            case '1': nombreArchivo="Prim"; break;
            case '2': nombreArchivo="Sec"; break;
            case '3': nombreArchivo="Pree"; break;
        }
        var NomCalendario = 'CalendarioProcesos'+sisVars.cicescini+'-'+(parseInt(sisVars.cicescini)+1);
        window.open('http://ieepoplaneacionedu.info/descargas/siceeo/calendarioProcesos/'+NomCalendario+'.pdf');
    }else // La tabla no existe
        mensaje.Principal("SN_BUSQ", "");           
}

function btnBusquedaCCT_Click (e)
{
     // Limpiamos el área de trabajo.
    //remueveContenidoDeFormularios();
    //ocultarFormulariosGenerales();
    //object_setVisible (true,"gridTable");
    irAVentanaPrincipal();
    if($('#tblPrincipal').length) // Verificando si la tabla Existe
        $('#tblPrincipal').remove();
    
    ejecutarBusquedaCCT(e);
    if (typeof e!=="undefined")
        e.preventDefault();
}

function ejecutarBusquedaCCT(e)
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse(sessionStorage.getItem("sistemVars"));
    var texto = document.getElementById('txtBusquedaCCT').value;
    var tabla = new Tabla();
    
    /*if(texto.length < 9 ) {
        mensaje.Principal("TAM_CCT", "");
        if($('#tblPrincipal').length) // Verificando si la tabla ya existe
            $('#tblPrincipal').remove(); // eliminar la Tabla que Existe
    } else {
        if(!validarExpresion(texto.toUpperCase(),'CCT')) 
            mensaje.Principal("CCT_INCORR", "");
        else {  */         
            var datos = { modulo:"Prin", metodo:"haBu", orden:sisVars.orden, unidad:sisVars.unidad, vista:sisVars.vista,
                campo:"cct", operador:"=", texto:texto.toUpperCase(), btnPor1:"",campo1:"", operador1:"", texto1:"",
                btnPor2:"", campo2:"", operador2:"", texto2:"", cicescini:sisVars.cicescini
            };
            //showProcessing("gridTable");        
            cargarLoading();
            $.ajax({url:"../sis_web/siS1",
                type:"POST",
                dataType:"JSON",
                data: datos,
                async: true
            })
            .done(function(result){
                //hidePreocessing();
                switch(result.returnCase) {
                    case 1:
                        tabla.create("scrlPrincipal","tblPrincipal",result.tabla, ["ZONA","CCT","TURNO","NOMBRE","GRADO","GRUPO","TOT ALUM","MPIO","MUNICIPIO","LOC","LOCALIDAD","cvenivel"],["cvezona","cct","cveturno","nombre","grado","grupo","alum","cvemunicipio","desmunicipio","cvelocalidad","deslocalidad","cvenivel"], null, ["","","","","center","center","center","","","","","center","center"], true, null, null, null);
                        if (tabla.getNumRows("tblPrincipal")>0) {
                            tabla.setSelectedRow("tblPrincipal",0);
                            var permisosSecu = ((result.tabla[0].cveplan === "2") ? true : false);
                            boton_setVisible (permisosSecu, "btnCamDAes");
                            boton_setVisible (permisosSecu, "btnExalumnos");
                            boton_setVisible (permisosSecu, "btnCamDTaller");
                            boton_setVisible (permisosSecu, "btnCamDArte");
                            
                            sisV = JSON.parse(sessionStorage.getItem("sistemVars"));                            
                            var permisosPree = (result.tabla[0].cveplan === "3");
                            if(permisosPree) {
                                if (sisV.botonesDeCalif){                                  
                                    for (var boton in sisV.botonesDeCalif) {                                              
                                        if(sisVars.botonesDeCalif[boton]==="btnTrim1")                                        
                                            Trim1 = true;
                                        else if (sisVars.botonesDeCalif[boton]==="btnTrim2")
                                            Trim2 = true;
                                        else if (sisVars.botonesDeCalif[boton]==="btnTrim3")
                                            Trim3 = true;
                                    } 
                                    if(Trim1 || Trim2 || Trim3)
                                        boton_setVisible (true, "btnCalif"); 
                                    else 
                                        boton_setVisible (false, "btnCalif"); 
                                }
                            }
                                                                                                                                         
                            /*//--------------- CUANDO ES PREESCOLAR EL BOTÓN DE CALIFICACIONES SE LLAMARÁ EVALUACIONES ---------------
                            //$("#btnCalif .lblBotonMenu").text(result.tabla[0].cveplan === "3"?'Eval':'Calif');
                            //$('#btnCalif').prop('title', result.tabla[0].cveplan === "3"?'Evaluaciones':'Calificaciones');
                            //-------------------------------------------------------------------------------------------------------*/
                        }
                        break;
                    case 2:
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                            $('#lblUnidad').text(" "+result.unidad);
                             //---------- Reflejamos los cambios en sistemVars del sessionStorage ----------\\
                            sisVars.vista=result.vista;
                            sisVars.unidad=result.unidad;
                            sessionStorage.sistemVars = JSON.stringify(sisVars);      
                            //-----------------------------------------------------------------------------------------//
                            tabla.create("scrlPrincipal","tblPrincipal",result.tabla, ["ZONA","CCT","TURNO","NOMBRE","GRADO","GRUPO","TOT ALUM","MPIO","MUNICIPIO","LOC","LOCALIDAD","cvenivel"],["cvezona","cct","cveturno","nombre","grado","grupo","alum","cvemunicipio","desmunicipio","cvelocalidad","deslocalidad","cvenivel"], null, ["","","","","center","center","center","","","","","center","center"], true, null, null, null);
                            if (tabla.getNumRows("tblPrincipal")>0){
                                tabla.setSelectedRow("tblPrincipal",0);
                                var permisosSecu = (result.tabla[0].cveplan === "2");
                                //boton_setVisible (permisosSecu, "btnCamDTaller");
                                boton_setVisible (permisosSecu, "btnCamDArte");
                                boton_setVisible (permisosSecu, "btnCamDAes");

                                //--------------- CUANDO ES PREESCOLAR EL BOTÓN DE CALIFICACIONES SE LLAMARÁ EVALUACIONES ---------------
                                //$("#btnCalif .lblBotonMenu").text(result.tabla[0].cveplan === "3"?'Eval':'Calif');
                                //$('#btnCalif').prop('title',result.tabla[0].cveplan === "3"?'Evaluaciones':'Calificaciones');
                                //-------------------------------------------------------------------------------------------------------
                            }
                            if (typeof e!=="undefined") e.preventDefault();
                        break;
                    case 0: case -1:
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                            if (typeof e!=="undefined") e.preventDefault();
                        break;
                    case -10:
                            //e.preventDefault();
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                            btnCerrarSesion_ActionPerformed ();
                        break;
                    default:break;
                }
                cerrarLoading();
            })
            .fail(function() {
                //hidePreocessing();
                cerrarLoading();
                mensaje.General("ERROR_AJAX", "", "");
            });
      //  }
    //}
}

function validarExpresion(campo,tipo)
{
    switch(tipo){
        case 'CCT':
            if ((campo.match(/^20[A-Z]{3}[0-9]{4}[A-Z]*$/))) 
                return 1;
            else 
                return 0;
    break;
    }
}

function probarFormulario ()
{
    remueveContenidoDeFormularios();
    ocultarFormulariosGenerales();
    //si es para ventana modal, comentar las dos líneas si es ventana principal de módulo
    //object_setVisible (false,'gridTable');                                  // Ocultamos el gridTable
    //$("#frmwContacto").css("display", "block");                           // Mostramos el formulario correspondiente
}