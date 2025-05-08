
/* 
    Creado el : 11-ago-2017, 14:48:13
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsSelMesCompl;

function mwfSelMesCompl_Show(tblPrincipal_selRow)
{
    jsSelMesCompl={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_cicescinilib: tblPrincipal_selRow.cicescinilib,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo
    };
    object_setVisible (true,"mwfmSelMesCompl");                                      // Ocultamos el gridTable
    //$("#mwffSelMesCompl").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfSelMesCompl_Create();
    mwffSelMesCompl_FormActivate ();
    //event.stopPropagation();
}

function mwfSelMesCompl_Close()
{
    modalWindow_Close ("SelMesCompl");
    $("#txtBusquedaCCT").focus();
}

function mwfSelMesCompl_Create ()
{
    modalWindow_Create ("SelMesCompl", "Mes de etapa complementaria",mwfSelMesCompl_Close, 200, {anchoAutoajustable:false});
    $('#mwfpSelMesCompl').append('<div id="pnlSelMesCompl"></div>');
        
        $('#pnlSelMesCompl').append('<div id="pnlCompRec_alum"><input type="radio" id="rbnRec" name="rbgRecExt_Compl" value="rbnRec"><label>Complementaria de alumnos de recuperación</label></div>');
        $('#pnlSelMesCompl').append('<label id="lineaComp"></label>');
        $('#pnlSelMesCompl').append('<div id="pnlCompExt_alum"><input type="radio" id="rbnExt" name="rbgRecExt_Compl" value="rbnExt"><label>Complementaria alumnos Extraordinarios.</label></div>');
                                         
        $('#pnlSelMesCompl').append('<label id="lblInstrucciones">Seleccione el mes que corresponda a la etapa complementaria:</label>');
        $('#pnlSelMesCompl').append('<div id="pnlComboMes" class="combobox">'
                                        + '<select id="cbxMesCompl">'
                                            /*+ '<option value="JULIO">JULIO</option>'*/
                                            + '<option value="AGOSTO">AGOSTO</option>'
                                            + '<option value="SEPTIEMBRE">SEPTIEMBRE</option>'  
                                            + '<option value="OCTUBRE">OCTUBRE</option>'  
                                            + '<option value="ENERO">ENERO</option>'
                                        + '</select>'
                                    +'</div>');
        $('#pnlSelMesCompl').append('<label id="lblSelEspAlus"> <input id="chkSelEspAlus" type="checkbox" name="chkSelEspAlus" value="espAlus">Agregar alumnos en específico</label>');
        $('#pnlSelMesCompl').append('<div id="pnlListadoDeAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos para agregar en complementaria</label>'
                                            + '<div id="scrlAlumnos" class="scrollTable"></div>'
                                        +'</div>');
        $('#pnlSelMesCompl').append('<ul class="buttonBar"> '
                                            + '<li><a href="#" id="btnImpMesCompl"><label class="icon-impresora"></label> Imprimir</a></li>'
                                            + '<li><a href="#" id="btnCancelarMesCompl">Cancelar</a></li>'
                                    +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnImpMesCompl").on('click',function(){ btnImpMesCompl_Click(); });
    $("#btnCancelarMesCompl").on('click',function(){ mwfSelMesCompl_Close(); });
    $("#pnlSelMesCompl #chkSelEspAlus").on('click',function() { chkSelEspAlus_Click ();  });
    
    //------------------------------------------ --------------------- -------------------------------------------------
    object_setVisible(false,"pnlSelMesCompl #pnlListadoDeAlumnos");
}

function mwffSelMesCompl_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Compl", metodo:"foAc", tblPrincipal_cicescini:jsSelMesCompl.tblPrincipal_cicescini, tblPrincipal_idcct:jsSelMesCompl.tblPrincipal_idcct, 
        tblPrincipal_grado:jsSelMesCompl.tblPrincipal_grado, tblPrincipal_grupo:jsSelMesCompl.tblPrincipal_grupo, casoRep:"selMesCompl"
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

function initTablaAlumnosParaMesCompl (tblAlumnos)
{
    var tabla = new Tabla();
    
    tabla.create("scrlAlumnos","tblAlumnos",tblAlumnos, ["Seleccionar","idalu","CURP","NOMBRE COMPLETO"], ["selec","idalu","curp","nom_tot"], ["checkbox","","",""], ["center","","",""], true, null, null ,
    function (f,c){
        $("#pnlSelMesCompl #tblAlumnos_chk_f"+f+"_c0").on('click', function(e){ 
            //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
            document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });

        $("#pnlSelMesCompl #tblAlumnos_td_f"+f+"_c0").on('click', function(e){
            if (!$("#pnlSelMesCompl #tblAlumnos_chk_f"+f+"_c0").prop("disabled"))
                document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });
    });
    
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === 't'){
            document.getElementById("tblAlumnos_chk_f"+i+"_c0").checked = true;
            $("#pnlSelMesCompl #tblAlumnos_chk_f"+i+"_c0").prop('disabled', true);
        }
}

function chkSelEspAlus_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();

    if (tabla.getNumRows("tblAlumnos")>0)
        object_setVisible(document.getElementById('chkSelEspAlus').checked,"pnlSelMesCompl #pnlListadoDeAlumnos");
    else {
        //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Compl", metodo:"geAlSeMeCo", tblPrincipal_cicescinilib:jsSelMesCompl.tblPrincipal_cicescinilib, tblPrincipal_idcct:jsSelMesCompl.tblPrincipal_idcct, 
        tblPrincipal_grado:jsSelMesCompl.tblPrincipal_grado, tblPrincipal_grupo:jsSelMesCompl.tblPrincipal_grupo
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
                    initTablaAlumnosParaMesCompl  (result.tblAlumnos);
                    object_setVisible(document.getElementById('chkSelEspAlus').checked,"pnlSelMesCompl #pnlListadoDeAlumnos");
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

function btnImpMesCompl_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var idalus="",tipo_comp="";
    
    if(!document.getElementById('rbnRec').checked && !document.getElementById('rbnExt').checked)
        return mensaje.General ("ELEGIR_OPCION","de la complementaria deseada.");
    else if(document.getElementById('rbnRec').checked)
        tipo_comp = "c_PR";
    else tipo_comp = "c_EX";
        
    var tblAlumnos = tabla.getTable("tblAlumnos",["selec","idalu"], null,"JSON");
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === "true")
            idalus += (idalus===""?"":", ") + tblAlumnos[i].idalu;
    
    showReport ("mwfpSelMesCompl", "Reportes/FinDeCurso.jsp", 
                {cicescini:jsSelMesCompl.tblPrincipal_cicescini, cicescinilib:jsSelMesCompl.tblPrincipal_cicescinilib,
                cveplan:jsSelMesCompl.tblPrincipal_cveplan, 
                idcct:jsSelMesCompl.tblPrincipal_idcct, modalidad:jsSelMesCompl.tblPrincipal_modalidad, 
                grado:jsSelMesCompl.tblPrincipal_grado, grupo:jsSelMesCompl.tblPrincipal_grupo, 
                caso:"RELc", mesComplem:$("#cbxMesCompl").val(), idalus:idalus, tipo_comp:tipo_comp, 
                cicescini_act:jsSelMesCompl.tblPrincipal_cicescini });
    mwfSelMesCompl_Close();
}