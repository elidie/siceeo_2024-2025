/* 
    Creado el : 1/06/2015, 15:38:02 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/
var jsPromGralSec2012;

function frmfPromGralSec2012_Show (tblAlumCapCalif_promdp, QPromGral2012_promd1ros, QPromGral2012_promd2dos, QPromGral2012_promd3ros, tblAlumCapCalif_debe1ro, tblAlumCapCalif_debe2do, tblAlumCapCalif_matrepact, tblAlumCapCalif_promediogral, tblAlumCapCalif_promedioeb, tblAlumCapCalif_idalu, txtUsuario, califCicEscIn)
{
    jsPromGralSec2012={
        tblAlumCapCalif_promediogral_OldValue: tblAlumCapCalif_promediogral,
        tblAlumCapCalif_promedioeb_OldValue: tblAlumCapCalif_promedioeb,
        tblAlumCapCalif_idalu:tblAlumCapCalif_idalu,
        txtUsuario:txtUsuario, 
        califCicEscIn:califCicEscIn
    };
    
    object_setVisible (false,'frmfCalifSecXBim');
    
    frmfPromGralSec2012_Create();
    frmfPromGralSec2012_InitData(tblAlumCapCalif_promdp, QPromGral2012_promd1ros, QPromGral2012_promd2dos, QPromGral2012_promd3ros, tblAlumCapCalif_debe1ro, tblAlumCapCalif_debe2do, tblAlumCapCalif_matrepact, tblAlumCapCalif_promediogral, tblAlumCapCalif_promedioeb);
    frmfPromGralSec2012_FormActivate ();
}

function frmfPromGralSec2012_Close()
{
    jsPromGralSec2012 = null;
    if($("#frmfPromGralSec2012").length)
        $("#frmfPromGralSec2012").remove();
    object_setVisible (true,'frmfCalifSecXBim');
}

function frmfPromGralSec2012_Create ()
{
    if($('#frmfPromGralSec2012').length)                                           // Verificando si la tabla Existe
        $('#frmfPromGralSec2012').remove();
    $('#frmwCalifSecXBim').append('<fieldset id="frmfPromGralSec2012"><legend>Promedio final de Educación Secundaria </legend></fieldset>');
        $("#frmfPromGralSec2012").append('<div id="pnlEduPrim" class="panelesEdu"></div>');
            $("#pnlEduPrim").append('<label>Promedio final de Educación Primaria:</label>');
            $("#pnlEduPrim").append('<input type="text" id="txtPromdp" value=""/>');
        $("#frmfPromGralSec2012").append('<div id="pnlEduSec" class="panelesEdu"></div>');
            $("#pnlEduSec").append(
                      '<div id="pnl2EduSec" class="renglonPnlEduSec">'
                        + '<div id="pnl5EduSec" class="leyenRenglonPnlEduSec"><label>Promedio de 1ro, 2do y 3o de Secundaria:</label></div>'
                        + '<div id="pnl6EduSec">  <input type="text" id="txtPromd1ros" value=""/><input type="text" id="txtPromd2dos" value=""/><input type="text" id="txtPromd3ros" value=""/>  </div>'
                    + '</div>'
                    + '<div id="pnl3EduSec" class="renglonPnlEduSec">'
                        + '<div id="pnl7EduSec" class="leyenRenglonPnlEduSec"><label>Materias reprobadas de 1ro, 2do y 3ro:</label></div>'
                        + '<div id="pnl8EduSec">  <input type="text" id="txtDebe1ro" value=""/><input type="text" id="txtDebe2do" value=""/><input type="text" id="txtMatrepact" value=""/>  </div>'
                    + '</div>'
                    + '<div id="pnl4EduSec" class="renglonPnlEduSec">'
                        + '<div id="pnl9EduSec" class="leyenRenglonPnlEduSec"><label>Promedio final de Educación Secundaria:</label></div>'
                        + '<div id="pnl10EduSec">  <input type="text" id="txtPromediogral" value=""/>  </div>'
                    + '</div>'
            );
        $("#frmfPromGralSec2012").append('<div id="pnlEduBas" class="panelesEdu"></div>');
            $("#pnlEduBas").append('<label>Promedio de Educación Básica:</label>');
            $("#pnlEduBas").append('<input type="text" id="txtPromedioeb" value=""/>');
        $("#frmfPromGralSec2012").append('<div id="pnlControles2"></div>');
            //$("#pnlControles2").append('<label><input type="checkbox" id="chkEditProm" name="chkEditProm" value="editarPromedios"/>Editar promedios</label>');
            $("#pnlControles2").append('<ul class="buttonBar"> <li><a href="#" id="btnGuardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar y regresar</a></li> </ul>');
    
    
    //---------------------- ACTIVACIÓN DE EVENTOS -----------------------------
    
    $("#btnRegresar_PromGralSec2012").unbind("click");
    $("#btnRegresar_PromGralSec2012").bind("click",function()
    {
        frmfPromGralSec2012_Close();
    });
    $("#txtPromediogral").on("keyup", function(e){ if(e.which === 13)  { btnGuardar_PGS2012_Click(); } });
    $("#btnGuardar").on('click',function(e){ btnGuardar_PGS2012_Click(); e.preventDefault(); });
    $("#btnGuardar").on("keydown", function(e){ if(e.which === 13)  { btnGuardar_PGS2012_Click();  } });
    $("#chkEditProm").on('click',function(){ chkEditProm_Click(); });
}

function frmfPromGralSec2012_InitData(tblAlumCapCalif_promdp, QPromGral2012_promd1ros, QPromGral2012_promd2dos, QPromGral2012_promd3ros, tblAlumCapCalif_debe1ro, tblAlumCapCalif_debe2do, tblAlumCapCalif_matrepact, tblAlumCapCalif_promediogral, tblAlumCapCalif_promedioeb)
{
    $("#txtPromdp").val(tblAlumCapCalif_promdp);
    
    $("#txtPromd1ros").val(QPromGral2012_promd1ros);
    $("#txtPromd2dos").val(QPromGral2012_promd2dos);
    $("#txtPromd3ros").val(QPromGral2012_promd3ros);
    
    $("#txtDebe1ro").val(tblAlumCapCalif_debe1ro);
    $("#txtDebe2do").val(tblAlumCapCalif_debe2do);
    $("#txtMatrepact").val(tblAlumCapCalif_matrepact);
    
    $("#txtPromediogral").val(tblAlumCapCalif_promediogral);
    
    $("#txtPromedioeb").val(tblAlumCapCalif_promedioeb);
}

function frmfPromGralSec2012_FormActivate ()
{
    $("#txtPromdp").prop("readonly", true);
    
    $("#txtPromd1ros").prop("readonly", true);
    $("#txtPromd2dos").prop("readonly", true);
    $("#txtPromd3ros").prop("readonly", true);
    
    $("#txtDebe1ro").prop("readonly", true);
    $("#txtDebe2do").prop("readonly", true);
    $("#txtMatrepact").prop("readonly", true);
    
    $("#txtPromediogral").prop("readonly", true);
    $("#txtPromedioeb").prop("readonly", true);
    //*dbEdit2.Color :=  clSilver ;
    //*dbEdit1.Color :=  clSilver ;
   
    if (jsPromGralSec2012.califCicEscIn === 2012 ) //solo para 2012
    {
        object_setVisible (true,"pnlEduPrim");
        object_setVisible (true,"pnlEduBas");
        $("#txtPromedioeb").focus();
        $("#txtPromedioeb").select();
    } else {
        object_setVisible (false,"pnlEduPrim");
        object_setVisible (false,"pnlEduBas");
        $("#txtPromediogral").focus();
        $("#txtPromediogral").select();
    }
}

function btnGuardar_PGS2012_Click(e)
{
    var mensaje = new Mensajes();
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"prGrSe2012", metodo:"btGu", tblAlumCapCalif_matrepact:$("#txtMatrepact").val().trim(), 
        tblAlumCapCalif_promediogral: $("#txtPromediogral").val().trim(), califCicEscIn:jsPromGralSec2012.califCicEscIn, 
        tblAlumCapCalif_promedioeb:$("#txtPromedioeb").val().trim(), chkEditProm_isChecked:false,//document.getElementById('chkEditProm').checked, 
        txtUsuario:jsPromGralSec2012.txtUsuario, tblAlumCapCalif_idalu:jsPromGralSec2012.tblAlumCapCalif_idalu
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
                            frmfPromGralSec2012_Close();
                            //..................................................
                        });
                    }else{                                                      //Se tendrá que poner en un else para que no se ejecute si entra al subproceso tblAlumCapCalif_ChangeSelectedItem
                        jsCaSeXBi.hayCambiosXBim = false;
                        if (posSelActual < (numFilas-1)){
                            $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
                            $( "#tblMatCalifXBim_txt_f0_c1" ).select();
                        }
                        //cerrarLoading();                                      //OJO (08Jun17):Verificar si es necesario llamar a esta funcion antes de cerrar el formulario o el subproceso es capaz de hacerlo
                        frmfPromGralSec2012_Close();
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

function chkEditProm_Click()
{
    if ( document.getElementById('chkEditProm').checked )
    {
        $("#txtPromediogral").prop("readonly", false);
        $("#txtPromedioeb").prop("readonly", false);
       //*dbEdit2.Color :=  clWhite;
       //*dbEdit1.Color :=  clWhite;
       $("#txtPromediogral").select();
    } else {
        $("#txtPromediogral").val(jsPromGralSec2012.tblAlumCapCalif_promediogral_OldValue);
        $("#txtPromedioeb").val(jsPromGralSec2012.tblAlumCapCalif_promedioeb_OldValue);
        $("#txtPromediogral").prop("readonly", true);
        $("#txtPromedioeb").prop("readonly", true);
        //*dbEdit2.Color :=  clSilver ;
        //*dbEdit1.Color :=  clSilver ;
    }
}