/* 
    Creado el : 4/12/2015, 07:59:12 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/
var jsMasDeUno;
function frmwMasDeUno_Show (nameParentForm, dataTblCurpsRep, parametrosAEnviar, funcionALLamar)
{
    jsMasDeUno = {
        nameParentForm: nameParentForm,
        parametrosAEnviar: parametrosAEnviar,
        funcionALLamar: funcionALLamar
    };
    object_setVisible (false, nameParentForm);                                  // Ocultamos el gridTable
    $("#frmwMasDeUno").css("display", "block");                                 // Mostramos el formulario correspondiente
    
    frmwMasDeUno_Create ();
    frmwMasDeUno_FormActivate (dataTblCurpsRep);
}

function frmwMasDeUno_Close()
{
    var nombreFormularioQueLlama = jsMasDeUno.nameParentForm;
    
    if($("#frmfMasDeUno").length)
        $("#frmfMasDeUno").remove();
    object_setVisible(true, nombreFormularioQueLlama);
}

function frmwMasDeUno_Create ()
{
    if($('#frmfMasDeUno').length) // Verificando si la tabla Existe
        $('#frmfMasDeUno').remove();
    $('#frmwMasDeUno').append('<fieldset id="frmfMasDeUno"><legend>Alumnos con curps parecidas</legend><div id="btnRegresar_MasDeUno" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfMasDeUno").append('<div id="pnlMasDeUno"></div>');
        
            $("#pnlMasDeUno").append('<div id="pnlCurpsRep" class="panel"><label class="tituloPanel">Selecione un alumno</label></div>');
                $("#pnlCurpsRep").append('<div id="pnlTblCurpsRep">  <div id="scrlCurpsRep" class="scrollTable"></div>  </div>');
            $("#pnlMasDeUno").append('<div id="pnlHistorialAcad" class="panel">  <label class="tituloPanel">Historial académico</label>  </div>');
                $("#pnlHistorialAcad").append('<div id="pnlTblHistorialAcad">  <div id="scrlHistorialAcad" class="scrollTable"></div>  </div>');
            $("#pnlMasDeUno").append('<div id="pnlBotonesDecontrol_MasDeUno"></div>');
                $('#pnlBotonesDecontrol_MasDeUno').append('<ul class="buttonBar">'
                                                                +'<li><a href="#" id="btnAceptarMDU">Aceptar</a></li> '
                                                                +'<li><a href="#" id="btnCancelarMDU">Cancelar</a></li>'
                                                         +'</ul>');
        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    $("#btnRegresar_MasDeUno").on("click",function(){ frmwMasDeUno_Close(); return false; });
    
    $("#btnAceptarMDU").on("keydown", function(e){ if(e.which === 13){  btnAceptarMDU_ActionPerformed (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnAceptarMDU').on("click",function(){ btnAceptarMDU_ActionPerformed (); });
    $("#btnCancelarMDU").on("keydown", function(e){ if(e.which === 13){  btnCancelarMDU_ActionPerformed (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnCancelarMDU').on("click",function(){ btnCancelarMDU_ActionPerformed (); });
}

function frmwMasDeUno_FormActivate (dataTblCurpsRep)
{
    var tabla = new Tabla();
    tabla.create("scrlCurpsRep","tblCurpsRep",dataTblCurpsRep, ["CURP","Alumno"], ["curp","nom_tot"], null, ["center","center"], true, null, tblCurpsRep_MouseClicked, null);
    tabla.create("scrlHistorialAcad","tblHistorialAcad",null, ["idcct","zona","cct","cveturno","cveunidad","cicescini","grado","grupo","promedio","promediogral","estatusgrado"], ["idcct","cvezona","cct","cveturno","cveunidad","cicescini","grado","grupo","promedio","promediogral","estatusgrado"], null, null, true, null, null, null);
}

function btnAceptarMDU_ActionPerformed ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var posSel = tabla.getSelectedIndexRow("tblCurpsRep");

    if (posSel >= 0){
        jsMasDeUno.funcionALLamar ("ACEPTAR", posSel, tabla.getSelectedRow("tblCurpsRep", ["curp","nom_tot","idalu"], null, "JSON"), jsMasDeUno.parametrosAEnviar);
        frmwMasDeUno_Close();
    }else
        mensaje.General("NO_SELEC","a CURP","aceptar su elección");
}

function btnCancelarMDU_ActionPerformed ()
{
    if (typeof jsMasDeUno.funcionALLamar !== "undefined")
        jsMasDeUno.funcionALLamar ("CANCELAR", -1, null, jsMasDeUno.parametrosAEnviar);
    frmwMasDeUno_Close();
}

function tblCurpsRep_MouseClicked ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"MaDeUn", metodo:"tbCuRe_MoCl", idalu:tabla.getSelectedRow("tblCurpsRep",["idalu"])
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({
       url:"../sis_web/siS1",
       type:"POST",
       dataType:"JSON",
       data: datos,
       async: true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    tabla.create("scrlHistorialAcad","tblHistorialAcad",result.tblHistorialAcad, ["idcct","zona","cct","cveturno","cveunidad","cicescini","grado","grupo","promedio","promediogral","estatusgrado"], ["idcct","cvezona","cct","cveturno","cveunidad","cicescini","grado","grupo","promedio","promediogral","estatusgrado"], null, null, true, null, null, null);
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
            break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}