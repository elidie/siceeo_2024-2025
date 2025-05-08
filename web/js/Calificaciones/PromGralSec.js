/* 
    Creado el : 2/06/2015, 07:24:04 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsPromGralSec;

function frmfPromGralSec_Show (txtUsuario, califCicEscIn, tblAlumCapCalif_idalu, tblAlumCapCalif_matrepact, tblAlumCapCalif_promedio, tblAlumCapCalif_promediogral)
{
    jsPromGralSec = {
        txtUsuario: txtUsuario,
        califCicEscIn: califCicEscIn, 
        tblAlumCapCalif_idalu: tblAlumCapCalif_idalu,
        tblAlumCapCalif_matrepact: tblAlumCapCalif_matrepact
    };
   
   object_setVisible (false,'frmfCalifSecXBim');

    frmfPromGralSec_Create();
    frmfPromGralSec_FormActivate (tblAlumCapCalif_promedio, tblAlumCapCalif_promediogral);
}

function frmfPromGralSec_Close()
{
    jsPromGralSec = null;
    if($("#frmfPromGralSec").length)
        $("#frmfPromGralSec").remove();
    object_setVisible (true,'frmfCalifSecXBim');
}

function frmfPromGralSec_Create()
{
    if($('#frmfPromGralSec').length)                                           // Verificando si la tabla Existe
        $('#frmfPromGralSec').remove();
    $('#frmwCalifSecXBim').append('<fieldset id="frmfPromGralSec"><legend>Promedio de aprovechamiento </legend></fieldset>');
        $('#frmfPromGralSec').append('<div id="pnlPromGralSec"></div>');
        $('#pnlPromGralSec').append(
                  '<label id="lblPromAnual">Promedio Anual:</label>'
                +'<input type="text" id="txtPromedio" value=""/>'
                +'<label id="lblPromAprov">Prom. Aprovechamiento:</label>'
                +'<input type="text" id="txtPromediogral" value=""/>'
                +'<ul class="buttonBar"> <li><a href="#" id="btnGuardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar y regresar</a></li> </ul>'
        );
    
    //---------------------- ACTIVACIÓN DE EVENTOS -----------------------------
    
    $("#btnRegresar_PGS").unbind("click");
    $("#btnRegresar_PGS").bind("click",function(){ frmfPromGralSec_Close(); });
    $("#txtPromedio").on("keyup", function(e){ if(e.which === 13)  { btnGuardar_PGS_Click();  } });
    $("#btnGuardar").on('click',function(){ btnGuardar_PGS_Click(); });
    $("#btnGuardar").on("keyup", function(e){ if(e.which === 13)  { btnGuardar_PGS_Click();  } });
}

function frmfPromGralSec_FormActivate (tblAlumCapCalif_promedio, tblAlumCapCalif_promediogral)
{
    $("#txtPromedio").val(tblAlumCapCalif_promedio.trim());
    $("#txtPromediogral").val(tblAlumCapCalif_promediogral.trim());
    $("#txtPromedio").focus();
}

function btnGuardar_PGS_Click ()
{
    var mensaje = new Mensajes();
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"prGrSe", metodo:"btGu", tblAlumCapCalif_matrepact:jsPromGralSec.tblAlumCapCalif_matrepact, tblAlumCapCalif_promediogral: $("#txtPromediogral").val(), txtUsuario:jsPromGralSec.txtUsuario,
        califCicEscIn:jsPromGralSec.califCicEscIn, tblAlumCapCalif_idalu:jsPromGralSec.tblAlumCapCalif_idalu
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading(true);                                                        //Le indicamos que bloquee pantalla mientras halla subprocesos
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    tabla.updateRow ('tblAlumCapCalif',tabla.getSelectedIndexRow ('tblAlumCapCalif'),result.tblAlumCapCalif);
                    var numFilas = tabla.getNumRows ('tblAlumCapCalif');
                    var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapCalif');
                    if (posSelActual < (numFilas-1)){
                        //-------------------- Seleccionamos la siguiente fila de tblAlumCapCalif y obtenemos los datos correspondientes
                        tblAlumCapCalif_ChangeSelectedItem (posSelActual+1, function(){
                            tabla.setSelectedRow ('tblMatCalifXBim', 0);
                            $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
                            $( "#tblMatCalifXBim_txt_f0_c1" ).select();
                            jsCaSeXBi.tblMatCalifXBim_calif1_OldValue = tabla.getTable ('tblMatCalifXBim', ["calif1"]);
                            jsCaSeXBi.tblMatCalifXBim_calif2_OldValue = tabla.getTable ('tblMatCalifXBim', [2]);                //Forzosamente necesitamos el que está visible al usuario
                            //..................................................//Forsozo repetir esta sección de código por cuestión de llamada a subproceso
                            jsCaSeXBi.hayCambiosXBim = false;                   
                            if (posSelActual < (numFilas-1)){
                                $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
                                $( "#tblMatCalifXBim_txt_f0_c1" ).select();
                            }
                            //cerrarLoading();                                  //OJO (08Jun17):Verificar si es necesario llamar a esta funcion antes de cerrar el formulario o el subproceso es capaz de hacerlo
                            frmfPromGralSec_Close();
                            //..................................................
                        });
                    }else {                                                     //Se tendrá que poner en un else para que no se ejecute si entra al subproceso tblAlumCapCalif_ChangeSelectedItem
                        jsCaSeXBi.hayCambiosXBim = false;
                        if (posSelActual < (numFilas-1)){
                            $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
                            $( "#tblMatCalifXBim_txt_f0_c1" ).select();
                        }
                        //cerrarLoading();                                      //OJO (08Jun17):Verificar si es necesario llamar a esta funcion antes de cerrar el formulario o el subproceso es capaz de hacerlo
                        frmfPromGralSec_Close();
                    }
                break;
            case 0: case -1:
                    //cerrarLoading();
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
        //cerrarLoading();
    })
    .always(function() {
        cerrarLoading();
    });
}