/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
var jsSelMesCons;

function mwfSelMesCons_Show(tblPrincipal_selRow)
{
    jsSelMesCons={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_cicescinilib: tblPrincipal_selRow.cicescinilib,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        tblPrincipal_caso:tblPrincipal_selRow.caso
    };
    object_setVisible (true,"mwfmSelMesCons");                                      // Ocultamos el gridTable    
    mwfSelMesCons_Create();    
}

function mwfSelMesCons_Close()
{
    modalWindow_Close ("SelMesCons");
    $("#txtBusquedaCCT").focus();
}

function mwfSelMesCons_Create ()
{
    modalWindow_Create ("SelMesCons", "Mes de etapa de aplicación de EER",mwfSelMesCons_Close, 200, {anchoAutoajustable:false});
    $('#mwfpSelMesCons').append('<div id="pnlSelMesCons"></div>');
                                                 
        $('#pnlSelMesCons').append('<label id="lblInstrucciones">Seleccione el mes que corresponda a la etapa EER:</label>');
        $('#pnlSelMesCons').append('<div id="pnlComboMes" class="combobox">'
                                        + '<select id="cbxMesAlCo">'                                            
                                            + '<option value="28">1ER PERIODO (AGO)</option>'                                            
                                            + '<option value="29">2DO PERIODO (SEP)</option>'
                                            + '<option value="30">3ER PERIODO (SEP)</option>'
                                        + '</select>'
                                    +'</div>');
        $('#pnlSelMesCons').append('<label id="lblSelEspAlus"> <input id="chkSelEspAlusCons" type="checkbox" name="chkSelEspAlusCons" value="espAlus">Agregar alumnos en específico</label>');
        $('#pnlSelMesCons').append('<div id="pnlListadoDeAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos para agregar en complementaria</label>'
                                            + '<div id="scrlAlumnos" class="scrollTable"></div>'
                                        +'</div>');
        $('#pnlSelMesCons').append('<ul class="buttonBar"> '
                                            + '<li><a href="#" id="btnImpMesCons"><label class="icon-impresora"></label> Imprimir</a></li>'
                                            + '<li><a href="#" id="btnCancelarMesCons">Cancelar</a></li>'
                                    +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnImpMesCons").on('click',function(){ btnImpMesCons_Click(); });
    $("#btnCancelarMesCons").on('click',function(){ mwfSelMesCons_Close(); });
    $("#pnlSelMesCons #chkSelEspAlusCons").on('click',function() { chkSelEspAlusCons_Click ();  });    
    $("#cbxMesAlCo").on('change',function(){ cbxMesAlCo_Change($(this).val()); });
    
    //------------------------------------------ --------------------- -------------------------------------------------
    object_setVisible(false,"pnlSelMesCons #pnlListadoDeAlumnos");
}

function mwffSelMesCons_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Compl", metodo:"foAcCo", tblPrincipal_cicescini:jsSelMesCons.tblPrincipal_cicescini, tblPrincipal_idcct:jsSelMesCons.tblPrincipal_idcct, 
        tblPrincipal_grado:jsSelMesCons.tblPrincipal_grado, tblPrincipal_grupo:jsSelMesCons.tblPrincipal_grupo, casoRep:"selMesCons"
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
                    if (!result.canSelEspAlus)
                        $("#lblSelEspAlus").remove();
                break;
            case 0: case -1:
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
    })
    .always(function() {
        cerrarLoading();
    });
}

function initTablaAlumnosParaMesCons (tblAlumnos)
{
    var tabla = new Tabla();
    
    tabla.create("scrlAlumnos","tblAlumnos",tblAlumnos, ["Seleccionar","idalu","CURP","NOMBRE COMPLETO"], ["selec","idalu","curp","nom_tot"], ["checkbox","","",""], ["center","","",""], true, null, null ,
    function (f,c){
        $("#pnlSelMesCons #tblAlumnos_chk_f"+f+"_c0").on('click', function(e){ 
            //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
            document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });

        $("#pnlSelMesCons #tblAlumnos_td_f"+f+"_c0").on('click', function(e){
            if (!$("#pnlSelMesCons #tblAlumnos_chk_f"+f+"_c0").prop("disabled"))
                document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });
    });
    
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === 't'){
            document.getElementById("tblAlumnos_chk_f"+i+"_c0").checked = true;
            $("#pnlSelMesCons #tblAlumnos_chk_f"+i+"_c0").prop('disabled', true);
        }
}

function chkSelEspAlusCons_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();

    if (tabla.getNumRows("tblAlumnos")>0)
        object_setVisible(document.getElementById('chkSelEspAlusCons').checked,"pnlSelMesCons #pnlListadoDeAlumnos");
    else {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Compl", metodo:"geAlSeMeCons", tblPrincipal_cicescinilib:jsSelMesCons.tblPrincipal_cicescinilib, 
            tblPrincipal_idcct:jsSelMesCons.tblPrincipal_idcct, tblPrincipal_cicescini: jsSelMesCons.tblPrincipal_cicescini, 
            tblPrincipal_grado:jsSelMesCons.tblPrincipal_grado, tblPrincipal_grupo:jsSelMesCons.tblPrincipal_grupo,
            idperexmext:$("#cbxMesAlCo").val()
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
                        initTablaAlumnosParaMesCons  (result.tblAlumnos);
                        object_setVisible(document.getElementById('chkSelEspAlusCons').checked,"pnlSelMesCons #pnlListadoDeAlumnos");
                    break;
                case 0: case -1:
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
        })
        .always(function() {
            cerrarLoading();
        });
    }
}

function cbxMesAlCo_Change (idperexmext)
{    
    var mensaje = new Mensajes();    
    if (document.getElementById('chkSelEspAlusCons').checked) {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Compl", metodo:"geAlSeMeCons", tblPrincipal_cicescinilib:jsSelMesCons.tblPrincipal_cicescinilib, 
            tblPrincipal_idcct:jsSelMesCons.tblPrincipal_idcct, tblPrincipal_cicescini: jsSelMesCons.tblPrincipal_cicescini, 
            tblPrincipal_grado:jsSelMesCons.tblPrincipal_grado, tblPrincipal_grupo:jsSelMesCons.tblPrincipal_grupo,
            idperexmext:idperexmext
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
                        initTablaAlumnosParaMesCons  (result.tblAlumnos);
                        //object_setVisible(document.getElementById('chkSelEspAlusCons').checked,"pnlSelMesCons #pnlListadoDeAlumnos");
                    break;
                case 0: case -1:
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
        })
        .always(function() {
            cerrarLoading();
        });
    }
}

function btnImpMesCons_Click ()
{
    var tabla = new Tabla();    
    var idalus="";
    
        
    var tblAlumnos = tabla.getTable("tblAlumnos",["selec","idalu"], null,"JSON");
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === "true")
            idalus += (idalus===""?"":", ") + tblAlumnos[i].idalu;
    
    showReport ("mwfpSelMesCons", "Reportes/Constancias.jsp", 
                {cicescini:jsSelMesCons.tblPrincipal_cicescini, cicescinilib:jsSelMesCons.tblPrincipal_cicescinilib,
                cveplan:jsSelMesCons.tblPrincipal_cveplan, idcct:jsSelMesCons.tblPrincipal_idcct, 
                grado:jsSelMesCons.tblPrincipal_grado, grupo:jsSelMesCons.tblPrincipal_grupo, 
                caso:"EER", idperexmext:$("#cbxMesAlCo").val(), idalusCons:idalus,
                cicescini_act:jsSelMesCons.tblPrincipal_cicescini});
    mwfSelMesCons_Close();
}

