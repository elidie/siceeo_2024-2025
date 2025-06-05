/*
    Creado el : 18/03/2015, 06:06:57 PM

    Autor     : Ing. Maai Nolasco Sánchez

*/
var jsBimXAlum;
function frmfBimXAlum_Show (tblAlumCapCalif_nom_tot, tblPrincipal_cct, tblPrincipal_nombre, tblPrincipal_grado, tblPrincipal_cveplan, tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini)
{
    jsBimXAlum = {
        tblPrincipal_cct:tblPrincipal_cct,
        tblPrincipal_nombre:tblPrincipal_nombre
    };
    object_setVisible (false,'frmfCalifSecXBim');

    frmfBimXAlum_Create();
    //frmfBimXAlum_FormActivate (tblAlumCapCalif_nom_tot, tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_cveplan, tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini);
}

function frmfBimXAlum_Close()
{
    if($("#frmfBimXAlum").length)
        $("#frmfBimXAlum").remove();
    jsBimXAlum = null;
    object_setVisible (true,'frmfCalifSecXBim');
    $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
    $( "#tblMatCalifXBim_txt_f0_c1" ).select();
}

function frmfBimXAlum_Create ()
{
    if($('#frmfBimXAlum').length)                                           // Verificando si la tabla Existe
        $('#frmfBimXAlum').remove();
    $('#frmwCalifSecXBim').append('<fieldset id="frmfBimXAlum"><legend>Calificaciones del alumno</legend><div id="btnRegresar_BXA" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div></fieldset>');
        $("#frmfBimXAlum").append('<div id="pnlCicAct"></div>');
            $("#pnlCicAct").append('<div id="pnlAlumAntSig"></div>');
                $("#pnlAlumAntSig").append(
                          '<label id="lblNom_tot">...</label>'
                        +'<ul class="buttonBar"> <li><a href="#" id="btnAnteriorAlum">Alum Anterior</a></li> <li><a href="#" id="btnSiguienteAlum">Alum Siguiente</a></li>  </ul>'
                        +'<label id="lblCct">...</label>'
                        +'<ul class="buttonBar"> <li><a href="#" id="btnExportarAExcel">Exportar a excel</a></li>  </ul>'
                );
            $("#pnlCicAct").append('<div id="pnlCalifBimXAlum"></div>');
                $('#pnlCalifBimXAlum').append('<div id="scrlCalifBimXAlum" class="scrollTable"></div>');
        $("#frmfBimXAlum").append('<div id="pnl1ro"></div>');
            $("#pnl1ro").append('<div id="pnl1ro_GrupoCic"></div>');
                $("#pnl1ro_GrupoCic").append(
                           '<label id="lblGradoGrupo_1ro">Primer grado, Grupo:</label>'
                        + '<label id="lblGrupo_1ro">...</label>'
                        + '<label id="lblCicescini_1ro">...</label>'
                );
            $("#pnl1ro").append('<div id="pnl1ro_DelegCct"></div>');
                $("#pnl1ro_DelegCct").append(
                           '<label id="lblDeleg_1ro">...</label>'
                        + '<label id="lblCct_1ro">...</label>'
                );
            $("#pnl1ro").append('<div id="pnlCalif1ro"></div>');
                $("#pnlCalif1ro").append('<div id="scrlCalif1ro" class="scrollTable"></div>');
        $("#frmfBimXAlum").append('<div id="pnl2do"></div>');
            $("#pnl2do").append('<div id="pnl2do_GrupoCic"></div>');
                $("#pnl2do_GrupoCic").append(
                           '<label id="lblGradoGrupo_2do">Segundo grado, Grupo:</label>'
                        + '<label id="lblGrupo_2do">...</label>'
                        + '<label id="lblCicescini_2do">...</label>'
                );
            $("#pnl2do").append('<div id="pnl2do_DelegCct"></div>');
                $("#pnl2do_DelegCct").append(
                           '<label id="lblDeleg_2do">...</label>'
                        + '<label id="lblCct_2do">...</label>'
                );
            $("#pnl2do").append('<div id="pnlCalif2do"></div>');
                $("#pnlCalif2do").append('<div id="scrlCalif2do" class="scrollTable"></div>');
        $("#frmfBimXAlum").append('<div id="divCalifsToExcel"></div>');
                
                
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnRegresar_BXA").unbind("click");
    $("#btnRegresar_BXA").on("click",function(){ frmfBimXAlum_Close(); return false; });
    $("#btnAnteriorAlum").on("click",function(){ 
        btnAnteriorAlum_BXA_Click(); 
        return false; 
    });
    $("#btnSiguienteAlum").on("click",function(){ 
        btnSiguienteAlum_BXA_Click(); 
        return false; 
    });
    $("#btnExportarAExcel").on ("click", function(){
        btnExportarAExcel_Click();
        return false; 
    });
}

function frmfBimXAlum_FormActivate (tblAlumCapCalif_nom_tot, tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_cveplan, tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini)
{
    var mensaje = new Mensajes();
    $("#lblNom_tot").text(tblAlumCapCalif_nom_tot);
    $("#lblCct").text(tblPrincipal_cct);
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"biXAl", metodo:"foAc", tblPrincipal_grado:tblPrincipal_grado, tblPrincipal_cveplan:tblPrincipal_cveplan,
        tblAlumCapCalif_idalu:tblAlumCapCalif_idalu===""?"0":tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini:tblAlumCapCalif_cicescini
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
                    
                    tabla.create("scrlCalifBimXAlum","tblCalifBimXAlum",result.tblCalifBimXAlum,["cicescini","cveprograma","Gdo","desmat","b11","b21","b31","b12","b22","b32","promedio"],
                                    ["cicescini","cveprograma","grado","desmat","b11","b21","b31","b12","b22","b32","promedio"],null,
                                    ["center","","center","","right","right","right","right","right","right","center"],true,null, null,null);
                    
                    if (result.pnl1ro_Visible && result.pnl2do_Visible) {
                        tabla.create("scrlCalif1ro","tblCalif1ro",result.tblCalif1ro,["Materia","prom"], ["desmat","promedio"],null,["","center"],true,null,null,null);
                        if (result.tblCalif1ro.length){
                            $("#lblGrupo_1ro").text(result.tblCalif1ro[0].grupo);
                            $("#lblCicescini_1ro").text(result.tblCalif1ro[0].cicescini);
                            $("#lblDeleg_1ro").text(result.tblCalif1ro[0].deleg);
                            $("#lblCct_1ro").text(result.tblCalif1ro[0].cct);
                        }
                        
                        tabla.create("scrlCalif2do","tblCalif2do",result.tblCalif2do,["Materia","prom"], ["desmat","promedio"],null,["","center"],true,null,null,null);
                        if (result.tblCalif2do.length){
                            $("#lblGrupo_2do").text(result.tblCalif2do[0].grupo);
                            $("#lblCicescini_2do").text(result.tblCalif2do[0].cicescini);
                            $("#lblDeleg_2do").text(result.tblCalif2do[0].deleg);
                            $("#lblCct_2do").text(result.tblCalif2do[0].cct);
                        }
                    }else {
                        object_setVisible (false,"pnl2do");
                        object_setVisible (false,"pnl1ro");
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

function btnAnteriorAlum_BXA_Click()
{
    var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapCalif');
    
    if (posSelActual > 0){
        //-------------------- Seleccionamos la siguiente fila de tblAlumCapCalif y obtenemos los datos correspondientes
        tblAlumCapCalif_ChangeSelectedItem (posSelActual-1, function (){
            //-------------------- -------------------- -------------------- -------------------- -------------------- 
            tabla.setSelectedRow ('tblMatCalifXBim', 0);
            $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
            $( "#tblMatCalifXBim_txt_f0_c1" ).select();
            jsCaSeXBi.tblMatCalifXBim_calif1_OldValue = tabla.getTable ('tblMatCalifXBim', ["calif1"]);
            jsCaSeXBi.tblMatCalifXBim_calif2_OldValue = tabla.getTable ('tblMatCalifXBim', [2]);                //Forzosamente necesitamos el que está visible al usuario

            jsCaSeXBi.hayCambiosXBim = false;
            var tblAlumCapCalif_nom_tot = tabla.getSelectedRow("tblAlumCapCalif", ["nom_tot"]);
            frmfBimXAlum_FormActivate (tblAlumCapCalif_nom_tot, jsCaSeXBi.tblPrincipal_cct, jsCaSeXBi.tblPrincipal_grado, jsCaSeXBi.tblPrincipal_cveplan, jsCaSeXBi.tblAlumCapCalif_idalu, jsCaSeXBi.califCicEscIn);
        });
    }
    
}
function btnSiguienteAlum_BXA_Click()
{
    var numFilas = tabla.getNumRows ('tblAlumCapCalif');
    var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapCalif');
    
    if (posSelActual < (numFilas-1)){
        //-------------------- Seleccionamos la siguiente fila de tblAlumCapCalif y obtenemos los datos correspondientes
        tblAlumCapCalif_ChangeSelectedItem (posSelActual+1, function () {
            tabla.setSelectedRow ('tblMatCalifXBim', 0);
            $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
            $( "#tblMatCalifXBim_txt_f0_c1" ).select();
            jsCaSeXBi.tblMatCalifXBim_calif1_OldValue = tabla.getTable ('tblMatCalifXBim', ["calif1"]);
            jsCaSeXBi.tblMatCalifXBim_calif2_OldValue = tabla.getTable ('tblMatCalifXBim', [2]);                //Forzosamente necesitamos el que está visible al usuario

            jsCaSeXBi.hayCambiosXBim = false;
            var tblAlumCapCalif_nom_tot = tabla.getSelectedRow("tblAlumCapCalif", ["nom_tot"]);
            frmfBimXAlum_FormActivate (tblAlumCapCalif_nom_tot, jsCaSeXBi.tblPrincipal_cct, jsCaSeXBi.tblPrincipal_grado, jsCaSeXBi.tblPrincipal_cveplan, jsCaSeXBi.tblAlumCapCalif_idalu, jsCaSeXBi.califCicEscIn);
        });
    }
    
}

function btnExportarAExcel_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var ancho=450, alto=200, posicion_x, posicion_y; 
    var tblAlumCapCalif, datos;
    
    posicion_x=(screen.width/2)-(ancho/2); 
    posicion_y=(screen.height/2)-(alto/2);
    
    mensaje.General("EXCEL_EN_CREACION");
    tblAlumCapCalif = tabla.getSelectedRow("tblAlumCapCalif",["cicescini","grado","grupo"], null, "JSON");
        
    datos = {
        llamada:0, idalu:tabla.getTable ("tblAlumCapCalif",["idalu"]), cicescini: tblAlumCapCalif.cicescini, tblPrincipal_cct:jsBimXAlum.tblPrincipal_cct, 
        tblPrincipal_nombre:jsBimXAlum.tblPrincipal_nombre, grado:tblAlumCapCalif.grado, grupo:tblAlumCapCalif.grupo
    };
    sessionStorage.tblAlumCapCalif = JSON.stringify(datos);
    //inicioDeGradoGrupoAExcel ();
    //window.open("jsp/Calificaciones/GradoGrupoAExcel.jsp","","status=0, toolbar=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
    window.open("../html/Calificaciones/GradoGrupoAExcel.html","","status=0, toolbar=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
}