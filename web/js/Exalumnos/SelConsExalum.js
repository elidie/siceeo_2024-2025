
/* 
    Creado el : 18/10/2017, 12:16:29 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsSelConsExalum;

function mwfSelConsExalum_Show(tblPrincipal_selRow)
{
    jsSelConsExalum={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,        
        tblPrincipal_cicescinilib: tblPrincipal_selRow.cicescinilib,     
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        cicescini_ex: tblPrincipal_selRow.cicescini_ex
    };
    
    object_setVisible (true,"mwfmSelConsExalum");                                      // Ocultamos el gridTable
    //$("#mwffSelComplExalum").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfSelConsExalum_Create();
    //mwffSelComplExalum_FormActivate ();
    //event.stopPropagation();
}

function mwfSelConsExalum_Close()
{
    modalWindow_Close ("SelConsExalum");
    $("#txtBusquedaCCT").focus();
}

function mwfSelConsExalum_Create ()
{
    modalWindow_Create ("SelConsExalum", "Mes de etapa de aplicación de EER",mwfSelConsExalum_Close, 200, {anchoAutoajustable:false});
    $('#mwfpSelConsExalum').append('<div id="pnlSelConsExalum"></div>');
        
        $('#pnlSelConsExalum').append('<label id="lblInstrucciones">Seleccione el mes que corresponda a la etapa EER:</label>');
        $('#pnlSelConsExalum').append('<div id="pnlComboMes" class="combobox">'
                                        + '<select id="cbxMesExCons">'                                            
                                            + '<option value="28">1ER PERIODO (AGO)</option>'                                            
                                            + '<option value="29">2DO PERIODO (SEP)</option>'
                                            + '<option value="30">3ER PERIODO (SEP)</option>'                                                                                        
                                        + '</select>'
                                    +'</div>');
        $('#pnlSelConsExalum').append('<label id="lblSelEspAlus"> <input id="chkSelEspExAlCo" type="checkbox" name="chkSelEspExAlCo" value="espAlus">Seleccionar alumnos en específico</label>');
        $('#pnlSelConsExalum').append('<div id="pnlListadoDeAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos para mandar a constancia</label>'
                                            + '<div id="scrlAlumnos" class="scrollTable"></div>'
                                        +'</div>');
        $('#pnlSelConsExalum').append('<ul class="buttonBar"> '
                                            + '<li><a href="#" id="btnImpMesConsExalum"><label class="icon-impresora"></label> Imprimir</a></li>'
                                            + '<li><a href="#" id="btnCancelarMesConsExalum">Cancelar</a></li>'
                                    +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#pnlSelConsExalum #chkSelEspExAlCo").on('click',function() { chkSelEspExalumCons_Click ();  });
    $("#cbxMesExCons").on('change',function(){ cbxMesExCons_Change($(this).val()); });
    $("#btnImpMesConsExalum").on('click',function(){ btnImpMesConsExalum_Click(); });
    $("#btnCancelarMesConsExalum").on('click',function(){ mwfSelConsExalum_Close(); });
    
    
    //------------------------------------------ --------------------- -------------------------------------------------
    object_setVisible(false,"pnlSelConsExalum #pnlListadoDeAlumnos");
}

/*function mwffSelComplExalum_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"SeMeCoEx", metodo:"foAc", tblPrincipal_cicescini:jsSelComplExalum.tblPrincipal_cicescini, tblPrincipal_idcct:jsSelComplExalum.tblPrincipal_idcct, 
        tblPrincipal_grado:jsSelComplExalum.tblPrincipal_grado, tblPrincipal_grupo:jsSelComplExalum.tblPrincipal_grupo, casoRep:"selMesCompl"
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
}*/

function initTablaAlumnosParaMesConsExalum (tblAlumnos)
{
    var tabla = new Tabla();
        
    tabla.create("scrlAlumnos","tblAlumnos",tblAlumnos, ["Seleccionar","idalu","CURP","NOMBRE COMPLETO","cicescini"], ["selec","idalu","curp","nom_tot","cicescini"], ["checkbox","","","",""], ["center","","","","center"], true, null, null ,
    function (f,c){
        $("#pnlSelConsExalum #tblAlumnos_chk_f"+f+"_c0").on('click', function(e){ 
            //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
            document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });

        $("#pnlSelConsExalum #tblAlumnos_td_f"+f+"_c0").on('click', function(e){
            if (!$("#pnlSelConsExalum #tblAlumnos_chk_f"+f+"_c0").prop("disabled"))
                document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });
    });
    
    if (typeof tblAlumnos !== "undefined"){
        for (var i=0; i<tblAlumnos.length; i++)
            if (tblAlumnos[i].selec === 't')
                document.getElementById("tblAlumnos_chk_f"+i+"_c0").checked = true;
    }
}

function cbxMesExCons_Change(idperexmext)
{
    var mensaje = new Mensajes();  
    if (document.getElementById('chkSelEspExAlCo').checked) {
        var datos = {
            modulo:"SeMeCoEx", metodo:"geExSeMeCons", tblPrincipal_cicescini:jsSelConsExalum.tblPrincipal_cicescini, 
            tblPrincipal_cicescinilib:jsSelConsExalum.tblPrincipal_cicescinilib, tblPrincipal_idcct:jsSelConsExalum.tblPrincipal_idcct, 
            tblPrincipal_grado:jsSelConsExalum.tblPrincipal_grado, tblPrincipal_grupo:jsSelConsExalum.tblPrincipal_grupo, 
            idperexmext:idperexmext, cicescini_ex: jsSelConsExalum.cicescini_ex
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
                        initTablaAlumnosParaMesConsExalum  (result.tblAlumnos);
                        //object_setVisible(document.getElementById('chkSelEspExAlCo').checked,"pnlSelComplExalum #pnlListadoDeAlumnos");
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

function chkSelEspExalumCons_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
        
    if (tabla.getNumRows("tblAlumnos")>0)
        object_setVisible(document.getElementById('chkSelEspExAlCo').checked,"pnlSelConsExalum #pnlListadoDeAlumnos");
    else {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"SeMeCoEx", metodo:"geExSeMeCons", tblPrincipal_cicescini:jsSelConsExalum.tblPrincipal_cicescini, 
            tblPrincipal_cicescinilib:jsSelConsExalum.tblPrincipal_cicescinilib, tblPrincipal_idcct:jsSelConsExalum.tblPrincipal_idcct, 
            tblPrincipal_grado:jsSelConsExalum.tblPrincipal_grado, tblPrincipal_grupo:jsSelConsExalum.tblPrincipal_grupo, 
            idperexmext:$("#cbxMesExCons").val(), cicescini_ex: jsSelConsExalum.cicescini_ex
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
                    initTablaAlumnosParaMesConsExalum(result.tblAlumnos);
                    object_setVisible(document.getElementById('chkSelEspExAlCo').checked,"pnlSelConsExalum #pnlListadoDeAlumnos");
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

function btnImpMesConsExalum_Click ()
{
    var tabla = new Tabla();
    var idalus="";
    
    var tblAlumnos = tabla.getTable("tblAlumnos",["selec","idalu"], null,"JSON");
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === "true")
            idalus += (idalus===""?"":", ") + tblAlumnos[i].idalu;
    
    showReport ("mwfpSelConsExalum", "Reportes/Constancias.jsp", 
                {cicescini:jsSelConsExalum.tblPrincipal_cicescini, cicescinilib:jsSelConsExalum.tblPrincipal_cicescinilib, 
                cveplan:jsSelConsExalum.tblPrincipal_cveplan,idcct:jsSelConsExalum.tblPrincipal_idcct, 
                caso:"EERc",cicescini_ex: jsSelConsExalum.cicescini_ex, grado:jsSelConsExalum.tblPrincipal_grado, 
                grupo: jsSelConsExalum.tblPrincipal_grupo, idperexmext:$("#cbxMesExCons").val(), idalusCons:idalus });
    mwfSelConsExalum_Close();
}