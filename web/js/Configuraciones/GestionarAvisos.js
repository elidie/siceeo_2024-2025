/* 
    Creado el : 25-ene-2017, 16:55:29
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsGeAv;
function frmwGestionarAvisos_Show ()
{
    jsGeAv={
        
    };
    
    object_setVisible (false,'frmwConfiguraciones');
    $("#frmwGestionarAvisos").css("display", "block");                          // Mostramos el formulario correspondiente

    frmwGestionarAvisos_Create();
    frmwGestionarAvisos_FormActivate ();
}

function frmwGestionarAvisos_Close()
{
    jsGeAv=null;
    if($("#frmfGestionarAvisos").length)
        $("#frmfGestionarAvisos").remove();
    
    object_setVisible (false,'frmwGestionarAvisos');
    object_setVisible (true,'frmwConfiguraciones');
}

function frmwGestionarAvisos_Create() 
{
    if($('#frmfGestionarAvisos').length)                                                // Verificando si la tabla Existe
        $('#frmfGestionarAvisos').remove();
    $('#frmwGestionarAvisos').append('<fieldset id="frmfGestionarAvisos"><legend>Gestionar avisos</legend><div id="btnRegresar_GA" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfGestionarAvisos").append('<div id="pnlGestionarAvisos"></div>');
//.........................................................................................................................................................................................................................................................................
            $("#pnlGestionarAvisos").append('<div id="pnlTablaAvisos" class="panel"><label class="tituloPanel">Mensajes registrados</label>'
                                                +'<div id="scrlAvisos" class="scrollTable"></div>'
                                                +'<ul class="buttonBar"> '
                                                    +'<li><a href="#" id="btnPrevMensaje">Previsualizar mensaje</a></li>'
                                                    +'<li><a href="#" id="btnEditarMensaje">Editar mensaje</a></li>'
                                                    +'<li><a href="#" id="btnEliminarMensaje">Eliminar mensaje</a></li>'
                                                    +'<li><a href="#" id="btnSetEstatusActivo" title="Pone en estatus activo al mensaje">Activar mensaje</a></li>'
                                                    +'<li><a href="#" id="btnSetEstatusInactivo"  title="Pone en estatus inactivo al mensaje">Desactivar mensaje</a></li>'
                                                +'</ul>'
                                            +'</div>');
            $('#pnlGestionarAvisos').append('<div id="pnlEdicionTexto" class="panel"><div class="tituloPanel">Edicion de texto</div>'
                                                 +'<textarea id="txtaMensaje"></textarea>'
                                             +'</div>');
            $('#pnlGestionarAvisos').append('<ul class="buttonBar"> '
                                                +'<li><a href="#" id="btnVerMensaje">Ver mensaje</a></li>'
                                                +'<li><a href="#" id="btnReemplazarMensaje" title="Reemplaza el texto escrito por el mensaje seleccionado en la tabla.">Reemplazar</a></li>'
                                                +'<li><a href="#" id="btnInsertarMensaje" title="Crea un nuevo mensaje basado en el texto escrito.">Insertar</a></li>'
                                            +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    $("#btnRegresar_GA").on("click",function(){ frmwGestionarAvisos_Close(); });
    
    $("#btnPrevMensaje").on('click', function(){ btnPrevMensaje_Click (); });
    $("#btnEditarMensaje").on('click', function(){ btnEditarMensaje_Click (); });
    $("#btnEliminarMensaje").on('click', function(){ btnEliminarMensaje_Click (); });
    $("#btnSetEstatusActivo").on('click', function(){ setEstatusActivoInactivo ("t"); });
    $("#btnSetEstatusInactivo").on('click', function(){ setEstatusActivoInactivo ("f"); });
    $("#btnVerMensaje").on('click', function(){ btnVerMensaje_Click (); });
    $("#btnReemplazarMensaje").on('click', function(){ btnReemplazarMensaje_Click (); });
    $("#btnInsertarMensaje").on('click', function(){ btnInsertarMensaje_Click (); });
}

function frmwGestionarAvisos_FormActivate ()
{
    var mensaje = new Mensajes();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"GeAv", metodo:"foAc"
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
                    //--------------------- Rellenamos tablas ----------------------
                    insertarTabla_Avisos(result.tblAvisos);
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
    })
    .always(function() {
        cerrarLoading();
    });
}

function insertarTabla_Avisos(tblAvisos)
{
    var tabla = new Tabla();
    tabla.create("scrlAvisos","tblAvisos",tblAvisos, ["idaviso","estatus","mensaje","icono","Fecha"], ["idaviso","activo","mensaje","icono","fechainsert"], ["","","label","",""], null, true, null, null, null);
}

function btnPrevMensaje_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var avisos = null, tblAvisos=null;
    
    if (tabla.getSelectedIndexRow("tblAvisos") !== -1){
        var tblAvisos = tabla.getSelectedRow("tblAvisos", ["mensaje"], null, "JSON");
        avisos = {mensaje:tblAvisos.mensaje, icono:tblAvisos.icono };

        sisVars['avisos']=avisos;
        sessionStorage.sistemVars = JSON.stringify(sisVars);
        mwffAvisos_Show ();
    }else
        mensaje.General("NO_SELEC"," mensaje","previsualizarlo");
}

function btnEditarMensaje_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    
    if (tabla.getSelectedIndexRow("tblAvisos") !== -1){
        var tblAvisos = tabla.getSelectedRow("tblAvisos", ["mensaje"], null, "JSON");
        $("#txtaMensaje").val(tblAvisos.mensaje);
    }else
        mensaje.General("NO_SELEC"," mensaje","previsualizarlo");
}

function btnEliminarMensaje_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var posSel = tabla.getSelectedIndexRow("tblAvisos");
    
    if (posSel === -1)
        mensaje.General("NO_SELEC"," mensaje","eliminarlo");
    else if (mensaje.confirmDialog("ELIMINAR","el mensaje"))
    {
        var tblAvisos = tabla.getSelectedRow("tblAvisos", ["idaviso"], null, "JSON");
        
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"GeAv", metodo:"btElMe_Cl", idaviso:tblAvisos.idaviso
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
                        tabla.removeRow ("tblAvisos", posSel);
                        mensaje.General("PROCESO_EXITOSO");
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    break;
                case -10:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        btnCerrarSesion_ActionPerformed ();
                    break;
                default:break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
        })
        .always(function() {
            cerrarLoading();
        });
    }
}

function setEstatusActivoInactivo (caso)
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var posSel = tabla.getSelectedIndexRow("tblAvisos");
    
    if (posSel === -1)
        mensaje.General("NO_SELEC"," mensaje","eliminarlo");
    else if (mensaje.confirmDialog("ELIMINAR","el mensaje"))
    {
        var tblAvisos = tabla.getSelectedRow("tblAvisos", ["idaviso"], null, "JSON");
        
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"GeAv", metodo:"seEsAcIn", idaviso:tblAvisos.idaviso, caso:caso
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
                        tabla.updateRow ("tblAvisos", posSel, {activo:caso});
                        mensaje.General("PROCESO_EXITOSO");
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    break;
                case -10:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        btnCerrarSesion_ActionPerformed ();
                    break;
                default:break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
        })
        .always(function() {
            cerrarLoading();
        });
    }
}

function btnVerMensaje_Click ()
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var avisos = {mensaje:$("#txtaMensaje").val(), icono:"" };

    sisVars['avisos']=avisos;
    sessionStorage.sistemVars = JSON.stringify(sisVars);
    mwffAvisos_Show ();
}

function btnReemplazarMensaje_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var posSel = tabla.getSelectedIndexRow("tblAvisos");
    
    if (posSel === -1)
        mensaje.General("NO_SELEC"," mensaje","reemplazarlo");
    else if (mensaje.GestionarAvisos("REEMPLAZAR_MENSAJE","el mensaje","","CONFIRM_DIALOG"))
    {
        var tblAvisos = tabla.getSelectedRow("tblAvisos", ["idaviso"], null, "JSON");
        
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"GeAv", metodo:"btReMe", idaviso:tblAvisos.idaviso, mensaje:$("#txtaMensaje").val()
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
                        tabla.updateRow ("tblAvisos", posSel, {mensaje:$("#txtaMensaje").val()});
                        mensaje.General("PROCESO_EXITOSO");
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    break;
                case -10:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        btnCerrarSesion_ActionPerformed ();
                    break;
                default:break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
        })
        .always(function() {
            cerrarLoading();
        });
    }
}

function btnInsertarMensaje_Click ()
{
    var mensaje = new Mensajes();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"GeAv", metodo:"btInMe", mensaje:$("#txtaMensaje").val()
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
                    insertarTabla_Avisos(result.tblAvisos);
                    mensaje.General("PROCESO_EXITOSO");
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
    })
    .always(function() {
        cerrarLoading();
    });
}