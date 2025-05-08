/* 
    Creado el : 2/02/2016, 11:53:02 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/


var jsCaPa;

function mwffCamPass_Show ()
{
    jsCaPa = {
        
    };
    
    object_setVisible (true,"mwfmCamPass");    
    mwffCamPass_Create ();
}
function mwffCamPass_Close()
{
    jsCaPa=null;
    $('#mwfmCamPass #mwffCamPass').remove();
    object_setVisible (false,"mwfmCamPass");
}

function mwffCamPass_Create ()
{
    var tabindexIni = 20, tabindexReturn=35;
    
    modalWindow_Create ("CamPass", "Cambio de contraseña", mwffCamPass_Close, tabindexIni, {anchoAutoajustable:false, anchoFijoCentrado:true});
    tabindexIni+=2;
    
    $('#mwfpCamPass').append('<div id="pnlCamPass"></div>');
        $("#pnlCamPass").append('<div id="pnlUsuario"> <div class="anchoFijo alinearHoriz"><label>Usuario:</label></div> <input type="text" id="txtUsuario" class="alinearHoriz" maxlength="12" tabindex="'+(tabindexIni++)+'" tabindex="'+(tabindexReturn)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlPassword"> <div class="anchoFijo alinearHoriz"><label>Contraseña actual:</label></div> <input type="password" id="pwdPassword" class="alinearHoriz" maxlength="10" tabindex="'+(tabindexIni++)+'"/> <div id="btnBuscar" class="singleButton alinearHoriz" tabindex="'+(tabindexIni++)+'"><a href="#" >Buscar</a></div> </div>');
        $("#pnlCamPass").append('<div id="pnlNewPass"> <div class="anchoFijo alinearHoriz"><label>Nueva contraseña:</label></div> <input type="password" id="pwdNewPass" class="alinearHoriz" maxlength="10" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlNewPass2"> <div class="anchoFijo alinearHoriz"><label>Confirmar contraseña:</label></div> <input type="password" id="pwdNewPass2" class="alinearHoriz" maxlength="10" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlNombre"> <div class="anchoFijo alinearHoriz"><label>Nombre:</label></div> <input type="text" id="txtNombre" class="alinearHoriz" maxlength="40" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlApepat"> <div class="anchoFijo alinearHoriz"><label>Primer apellido:</label></div> <input type="text" id="txtApepat" class="alinearHoriz" maxlength="30" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlApemat"> <div class="anchoFijo alinearHoriz"><label>Segundo apellido:</label></div> <input type="text" id="txtApemat" class="alinearHoriz" maxlength="30" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlCurp"> <div class="anchoFijo alinearHoriz"><label>CURP:</label></div> <input type="text" id="txtCurp" class="alinearHoriz" maxlength="18" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlPuesto"> <div class="anchoFijo alinearHoriz"><label>Puesto:</label></div> <input type="text" id="txtPuesto" class="alinearHoriz" maxlength="25" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlTelefono"> <div class="anchoFijo alinearHoriz"><label>Teléfono:</label></div> <input type="text" id="txtTelefono" class="alinearHoriz" maxlength="17" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlCorreo"> <div class="anchoFijo alinearHoriz"><label>Correo electrónico:</label></div> <input type="text" id="txtCorreo" class="alinearHoriz" maxlength="100" tabindex="'+(tabindexIni++)+'"/> </div>');
        $("#pnlCamPass").append('<div id="pnlCamPassw"> <ul id="ubtnCamPassw" class="buttonBar">  <li><a href="#" id="btnCamPassw" tabindex="'+(tabindexIni++)+'">Cambiar contraseña</a></li>  </ul> </div>');
        $("#pnlCamPass").append('<div id="pnlCondicion"> <label>De existir falsificación en los datos proporcionados se cancelará el usuario</label> </div>');
        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    //$("#mwfcCamPass").on("click",function(){ mwffCamPass_Close(); });
    
    $("#pwdPassword").on("keydown", function(e){  if(e.which === 13){ btnBuscar_mwffCamPass_ActionPerformed (); e.preventDefault(); } });      //El preventDefault es porque en el método btnEliminarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnBuscar").on("click", function(){ btnBuscar_mwffCamPass_ActionPerformed (); });
    $("#btnBuscar").on("keydown", function(e){  if(e.which === 13){ btnBuscar_mwffCamPass_ActionPerformed (); e.preventDefault(); } });      //El preventDefault es porque en el método btnEliminarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnCamPassw").on("click", function(){ btnCamPassw_mwffCamPass_ActionPerformed (); });
    $("#btnCamPassw").on("keydown", function(e){  
        if(e.which === 13){                                                     //Enter
            btnCamPassw_mwffCamPass_ActionPerformed (); 
            e.preventDefault();                                                 //El preventDefault es porque en el método btnEliminarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
        } else if(e.which === 9){                                               //Tab
            document.getElementById('txtUsuario').focus();
            e.preventDefault(); 
        }
    });      
    
    $("#ubtnCamPassw").on("keydown", function (e){ 
        if(e.which === 9){
            document.getElementById('txtUsuario').focus(); 
            e.preventDefault(); 
        }
    });
    document.getElementById('txtUsuario').focus();
    boton_setEnabled(false,"btnCamPassw");
}

function btnBuscar_mwffCamPass_ActionPerformed ()
{
    var mensaje = new Mensajes();
    
    var datos = {  modulo:"CaPa", metodo:"btBu_AcPe", txtUsuario:$("#txtUsuario").val(), pwdPassword:$("#pwdPassword").val() };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    object_setEnabled(result.txtNewpass_Enabled,"txtNewpass");
                    object_setEnabled(result.txtNewpass2_Enabled,"txtNewpass2");
                    object_setEnabled(result.txtNombre_Enabled,"txtNombre");
                    object_setEnabled(result.txtApepat_Enabled,"txtApepat");
                    object_setEnabled(result.txtApemat_Enabled,"txtApemat");
                    object_setEnabled(result.txtCurp_Enabled,"txtCurp");
                    object_setEnabled(result.txtPuesto_Enabled,"txtPuesto");
                    object_setEnabled(result.txtTelefono_Enabled,"txtTelefono");
                    object_setEnabled(result.txtCorreo_Enabled,"txtCorreo");
                    object_setEnabled(result.btnCamPassw_Enabled,"btnCamPassw");
                    boton_setEnabled (true, "btnCamPassw", "click", btnCamPassw_mwffCamPass_ActionPerformed, "keydown", function(e){ if(e.which === 13){ btnCamPassw_mwffCamPass_ActionPerformed (); e.preventDefault(); }} );

                    $("#txtNombre").val(result.txtNombre_Text);
                    $("#txtApepat").val(result.txtApepat_Text);
                    $("#txtApemat").val(result.txtApemat_Text);
                    $("#txtCurp").val(result.txtCurp_text);
                    $("#txtPuesto").val(result.txtPuesto_Text);
                    $("#txtTelefono").val(result.txtTelefono_text);
                    $("#txtCorreo").val(result.txtCorreo_text);
                    cerrarLoading();
                break;
            case 0: case -1: 
                    object_setEnabled(result.txtNewpass_Enabled,"txtNewpass");
                    object_setEnabled(result.txtNewpass2_Enabled,"txtNewpass2");
                    object_setEnabled(result.txtNombre_Enabled,"txtNombre");
                    object_setEnabled(result.txtApepat_Enabled,"txtApepat");
                    object_setEnabled(result.txtApemat_Enabled,"txtApemat");
                    object_setEnabled(result.txtCurp_Enabled,"txtCurp");
                    object_setEnabled(result.txtPuesto_Enabled,"txtPuesto");
                    object_setEnabled(result.txtTelefono_Enabled,"txtTelefono");
                    object_setEnabled(result.txtCorreo_Enabled,"txtCorreo");
                    boton_setEnabled(result.btnCamPassw_Enabled,"btnCamPassw");
                    
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, ""); 
                    cerrarLoading();
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                    cerrarLoading();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function btnCamPassw_mwffCamPass_ActionPerformed ()
{
    var mensaje = new Mensajes();
    
    var datos = {  modulo:"CaPa", metodo:"btCaPa_AcPe", txtUsuario:$("#txtUsuario").val(), pwdPassword:$("#pwdPassword").val(), 
        pwdNewPass:$("#pwdNewPass").val(), pwdNewPass2:$("#pwdNewPass2").val(), txtNombre:$("#txtNombre").val(), txtApepat:$("#txtApepat").val(), txtApemat:$("#txtApemat").val(),
        txtCurp:$("#txtCurp").val(), txtPuesto:$("#txtPuesto").val(), txtTelefono:$("#txtTelefono").val(), txtCorreo:$("#txtCorreo").val()
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1: 
                    mensaje.General("GUARDADO_EXITOSO", "", ""); 
                    cerrarLoading();
                break;
            case 0: case -1: 
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, ""); 
                    cerrarLoading();
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                    cerrarLoading();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}


