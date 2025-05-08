/* 
    Creado el : 10/07/2015, 02:29:55 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

function frmwConfiguraciones_Show()
{
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwConfiguraciones").css("display", "block");                          // Mostramos el formulario correspondiente
    frmwConfiguraciones_Create();
    frmwConfiguraciones_FormActivate ();
}

function frmwConfiguraciones_Close()
{
    irAVentanaPrincipal();
}

function frmwConfiguraciones2_Create ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        metodo:"pr", txtUsuario:sisVars.usuario
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_webc/siSC",
        type:"POST",
        dataType:"html",
        data: datos,
        async:true
    })
    .done(function(result){
        if($('#frmfConfiguracione').length)                                                                                                           // Verificando si la tabla Existe
            $('#frmfConfiguraciones').remove();
        /*$('#frmwConfiguraciones').append('<fieldset id="frmfConfiguraciones"><legend>Configuraciones</legend><div id="btnRegresar_Configuraciones" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
            $("#frmfConfiguraciones").append('<div id="pnlConfiguraciones"></div>');
                $('#pnlConfiguraciones').append('<div id="pnlEstatusMantenimiento" class="panel"><label class="tituloPanel">Sesiones</label></div>');
                    $('#pnlEstatusMantenimiento').append('<ul class="buttonBar"> '
                                                            +'<li><a href="#" id="btnActualizarLista"><label class="icon-sincronizar"></label> Actualizar lista</a></li>'
                                                            +'<li id="libtnActivDesactivMantenimiento"><a href="#" id="btnActivDesactivMantenimiento">Verificando estatus...</a></li>'
                                                        +'</ul>');
                    $("#pnlEstatusMantenimiento").html(result);
                    $('#pnlEstatusMantenimiento').append('<div id="pnlTblSesiones">   <div id="scrlSesiones" class="scrollTable"></div>   </div>');
                    $('#pnlEstatusMantenimiento').append('<label id="lblEtiqNumUsuarios">Número de usuarios:  <label id="lblNumUsuarios"></label></label>');
        */
        $( "#frmwConfiguraciones" ).load( "codigo.html" );
        $("#btnRegresar_Configuraciones").on("click",function(){ frmwConfiguraciones_Close(); });
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
    })
    .always(function() {
        cerrarLoading();
    });
}

function frmwConfiguraciones_Create()
{
    if($('#frmfConfiguracione').length)                                                                                                           // Verificando si la tabla Existe
        $('#frmfConfiguraciones').remove();
    $('#frmwConfiguraciones').append('<fieldset id="frmfConfiguraciones"><legend>Configuraciones</legend><div id="btnRegresar_Configuraciones" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfConfiguraciones").append('<div id="pnlConfiguraciones"></div>');
            $('#pnlConfiguraciones').append('<div id="pnlEstatusMantenimiento" class="panel"><label class="tituloPanel">Sesiones</label></div>');
                $('#pnlEstatusMantenimiento').append('<ul class="buttonBar"> '
                                                        +'<li><a href="#" id="btnActualizarLista"><label class="icon-sincronizar"></label> Actualizar lista</a></li>'
                                                        +'<li id="libtnActivDesactivMantenimiento"><a href="#" id="btnActivDesactivMantenimiento" class="imponer-hover">Verificando estatus...</a></li>'
                                                        +'<li id="libtnCerrarSesiones"><a href="#" id="btnCerrarSesiones" class="imponer-hover">Cerrar sesiones</a></li>'
                                                        +'<li id="libtnPermisos"><a href="#" id="btnPermisos">Permisos</a></li>'
                                                        +'<li id="libtnAvisos"><a href="#" id="btnAvisos">Avisos</a></li>'
                                                        +'<li><a href="#" id="btnPingWebService">Ping WebService</a></li>'
                                                    +'</ul>');
                $('#pnlEstatusMantenimiento').append('<div id="pnlTblSesiones">   <div id="scrlSesiones" class="scrollTable"></div>   </div>');
                $('#pnlEstatusMantenimiento').append('<label id="lblEtiqNumUsuarios">Número de usuarios:  <label id="lblNumUsuarios"></label></label>');
                
    $("#btnRegresar_Configuraciones").on("click",function(){ frmwConfiguraciones_Close(); });
    $("#btnActualizarLista").on('click',function(){ btnActualizarLista_Click(); });
    $("#btnActivDesactivMantenimiento").on('click',function()
    {
        if ( $("#btnActivDesactivMantenimiento").text()!=="Verificando estatus..." )
            btnActivDesactivMantenimiento_Click();
    });
    
    $("#btnCerrarSesiones").on('click', function(){ btnCerrarSesiones_Click (); });
    $("#btnPermisos").on('click', function(){ mwfPermisos_Show (); });
    $("#btnAvisos").on('click', function(){ frmwGestionarAvisos_Show (); });
    $("#btnPingWebService").on('click', function(){ btnPingWebService_Click (); });
}

function frmwConfiguraciones_FormActivate ()
{
    btnActualizarLista_Click ();
    getEstatusMantenimiento ();
}

function getEstatusMantenimiento ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Co", metodo:"geEsMa", txtUsuario:sisVars.usuario
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    if (!result.btnActivDesactivMantenimiento_Visible)
                        $("#libtnActivDesactivMantenimiento").remove();
                    else {
                        $("#btnActivDesactivMantenimiento").text(result.btnActivDesactivMantenimiento_Text);
                        if (result.btnActivDesactivMantenimiento_Text==='Activar mantenimiento'){
                            $("#btnActivDesactivMantenimiento").addClass("colorActivarMantenimiento");
                            $("#btnActivDesactivMantenimiento").removeClass("colorDesactivarMantenimiento");
                        }else{
                            $("#btnActivDesactivMantenimiento").addClass("colorDesactivarMantenimiento");
                            $("#btnActivDesactivMantenimiento").removeClass("colorActivarMantenimiento");
                        }
                    }
                    cerrarLoading();
                break;
            case 0: case -1:
                    cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function insertarTablaTblSesiones (tblSesiones)
{
    var tabla = new Tabla();
    
    tabla.create("scrlSesiones","tblSesiones",tblSesiones, ["cveunidad","Usuario","idcct","tipo_usuario","Sección","Módulo","Última petición","Inicio de sesión","Sesión"], ["cveunidad","userName","QUsuario_idcct","tipo_usuario","seccion","modulo","lastAccessedTime","creationTime","sessionId"], null, ["center","center","center","center","center","center","","center","center","center"], true, null, null, null);
}

function btnActualizarLista_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Co", metodo:"geSe", txtUsuario:sisVars.usuario
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    insertarTablaTblSesiones (result.activeUsers);
                    $("#lblNumUsuarios").text(tabla.getNumRows("tblSesiones"));
                    cerrarLoading();
                break;
            case 0: case -1:
                    cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function btnActivDesactivMantenimiento_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Co", metodo:"seEsMa", txtUsuario:sisVars.usuario, casoMantenimiento:$("#btnActivDesactivMantenimiento").text()
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    insertarTablaTblSesiones(result.activeUsers);
                    $("#lblNumUsuarios").text(tabla.getNumRows("tblSesiones"));
                    $("#btnActivDesactivMantenimiento").text(result.btnActivDesactivMantenimiento_Text);
                    if (result.btnActivDesactivMantenimiento_Text==='Activar mantenimiento'){
                        $("#btnActivDesactivMantenimiento").addClass("colorActivarMantenimiento");
                        $("#btnActivDesactivMantenimiento").removeClass("colorDesactivarMantenimiento");
                    }else{
                        $("#btnActivDesactivMantenimiento").addClass("colorDesactivarMantenimiento");
                        $("#btnActivDesactivMantenimiento").removeClass("colorActivarMantenimiento");
                    }
                    cerrarLoading();
                break;
            case 0: case -1:
                    cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function btnCerrarSesiones_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Co", metodo:"ceSe", txtUsuario:sisVars.usuario
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    insertarTablaTblSesiones(result.activeUsers);
                    $("#lblNumUsuarios").text(tabla.getNumRows("tblSesiones"));
                    cerrarLoading();
                break;
            case 0: case -1:
                    cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}


function btnPingWebService_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Co", metodo:"piWeSe", txtUsuario:sisVars.usuario
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        mensaje.General("GENERAL", "INFORMACIÓN" +"\n\n"+result.mensaje, "");
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        //cerrarLoading();
    })
    .always(function() {
        cerrarLoading();
    });
}