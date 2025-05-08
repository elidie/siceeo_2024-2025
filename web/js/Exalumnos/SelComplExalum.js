
/* 
    Creado el : 18/10/2017, 12:16:29 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsSelComplExalum;

function mwfSelComplExalum_Show(tblPrincipal_selRow)
{
    jsSelComplExalum={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,        
        tblPrincipal_cicescinilib: tblPrincipal_selRow.cicescinilib,     
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        cicescini_act: tblPrincipal_selRow.cicescini_act
    };
    object_setVisible (true,"mwfmSelComplExalum");                                      // Ocultamos el gridTable
    //$("#mwffSelComplExalum").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfSelComplExalum_Create();
    //mwffSelComplExalum_FormActivate ();
    //event.stopPropagation();
}

function mwfSelComplExalum_Close()
{
    modalWindow_Close ("SelComplExalum");
    $("#txtBusquedaCCT").focus();
}

function mwfSelComplExalum_Create ()
{
    modalWindow_Create ("SelComplExalum", "Mes de etapa complementaria",mwfSelComplExalum_Close, 200, {anchoAutoajustable:false});
    $('#mwfpSelComplExalum').append('<div id="pnlSelComplExalum"></div>');
        
        $('#pnlSelComplExalum').append('<label id="lblInstrucciones">Seleccione el mes que corresponda a la etapa complementaria:</label>');
        $('#pnlSelComplExalum').append('<div id="pnlComboMes" class="combobox">'
                                        + '<select id="cbxMesCompl">'                                            
                                            + '<option value="ENERO">ENERO</option>'                                            
                                            + '<option value="AGOSTO">AGOSTO</option>'
                                            + '<option value="SEPTIEMBRE">SEPTIEMBRE</option>'                                            
                                            + '<option value="OCTUBRE">OCTUBRE</option>'
                                        + '</select>'
                                    +'</div>');
        $('#pnlSelComplExalum').append('<label id="lblSelEspAlus"> <input id="chkSelEspExalus" type="checkbox" name="chkSelEspExalus" value="espAlus">Seleccionar alumnos en específico</label>');
        $('#pnlSelComplExalum').append('<div id="pnlListadoDeAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos para mandar a complementaria</label>'
                                            + '<div id="scrlAlumnos" class="scrollTable"></div>'
                                        +'</div>');
        $('#pnlSelComplExalum').append('<ul class="buttonBar"> '
                                            + '<li><a href="#" id="btnImpMesComplExalum"><label class="icon-impresora"></label> Imprimir</a></li>'
                                            + '<li><a href="#" id="btnCancelarMesComplExalum">Cancelar</a></li>'
                                    +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#pnlSelComplExalum #chkSelEspExalus").on('click',function() { chkSelEspExalum_Click ();  });
    $("#cbxMesCompl").on('change',function(){ cbxMesCompl_Change($(this).val()); });
    $("#btnImpMesComplExalum").on('click',function(){ btnImpMesComplExalum_Click(); });
    $("#btnCancelarMesComplExalum").on('click',function(){ mwfSelComplExalum_Close(); });
    
    
    //------------------------------------------ --------------------- -------------------------------------------------
    object_setVisible(false,"pnlSelComplExalum #pnlListadoDeAlumnos");
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

function initTablaAlumnosParaMesComplExalum (tblAlumnos)
{
    var tabla = new Tabla();
    
    tabla.create("scrlAlumnos","tblAlumnos",tblAlumnos, ["Seleccionar","idalu","CURP","NOMBRE COMPLETO","cicescini"], ["selec","idalu","curp","nom_tot","cicescini"], ["checkbox","","","",""], ["center","","","","center"], true, null, null ,
    function (f,c){
        $("#pnlSelComplExalum #tblAlumnos_chk_f"+f+"_c0").on('click', function(e){ 
            //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
            document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });

        $("#pnlSelComplExalum #tblAlumnos_td_f"+f+"_c0").on('click', function(e){
            if (!$("#pnlSelComplExalum #tblAlumnos_chk_f"+f+"_c0").prop("disabled"))
                document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
        });
    });
    
    if (typeof tblAlumnos !== "undefined"){
        for (var i=0; i<tblAlumnos.length; i++)
            if (tblAlumnos[i].selec === 't')
                document.getElementById("tblAlumnos_chk_f"+i+"_c0").checked = true;
    }
}

function cbxMesCompl_Change(mesComplem)
{
    var mensaje = new Mensajes();  
    if (document.getElementById('chkSelEspExalus').checked){        
        var datos = {
            modulo:"SeMeCoEx", metodo:"geExSeMeCo", tblPrincipal_cicescini:jsSelComplExalum.tblPrincipal_cicescinilib, tblPrincipal_idcct:jsSelComplExalum.tblPrincipal_idcct, 
            tblPrincipal_grado:jsSelComplExalum.tblPrincipal_grado, tblPrincipal_grupo:jsSelComplExalum.tblPrincipal_grupo, mesComplem:mesComplem
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
                        initTablaAlumnosParaMesComplExalum  (result.tblAlumnos);
                        //object_setVisible(document.getElementById('chkSelEspExalus').checked,"pnlSelComplExalum #pnlListadoDeAlumnos");
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

function chkSelEspExalum_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    
    if (tabla.getNumRows("tblAlumnos")>0)
        object_setVisible(document.getElementById('chkSelEspExalus').checked,"pnlSelComplExalum #pnlListadoDeAlumnos");
    else {
        //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"SeMeCoEx", metodo:"geExSeMeCo", tblPrincipal_cicescini:jsSelComplExalum.tblPrincipal_cicescini, tblPrincipal_idcct:jsSelComplExalum.tblPrincipal_idcct, 
        tblPrincipal_grado:jsSelComplExalum.tblPrincipal_grado, tblPrincipal_grupo:jsSelComplExalum.tblPrincipal_grupo, mesComplem:$("#cbxMesCompl").val()
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
                    initTablaAlumnosParaMesComplExalum  (result.tblAlumnos);
                    object_setVisible(document.getElementById('chkSelEspExalus').checked,"pnlSelComplExalum #pnlListadoDeAlumnos");
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

function btnImpMesComplExalum_Click ()
{
    var tabla = new Tabla();
    var idalus="";
    
    var tblAlumnos = tabla.getTable("tblAlumnos",["selec","idalu"], null,"JSON");
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === "true")
            idalus += (idalus===""?"":", ") + tblAlumnos[i].idalu;
    
    showReport ("mwfpSelComplExalum", "Reportes/FinDeCurso.jsp", 
                {cicescini:jsSelComplExalum.tblPrincipal_cicescini, cicescinilib:jsSelComplExalum.tblPrincipal_cicescinilib, 
                cveplan:jsSelComplExalum.tblPrincipal_cveplan,idcct:jsSelComplExalum.tblPrincipal_idcct, 
                modalidad:jsSelComplExalum.tblPrincipal_modalidad,grado:jsSelComplExalum.tblPrincipal_grado, 
                grupo:jsSelComplExalum.tblPrincipal_grupo, caso:"RELc",cicescini_act: jsSelComplExalum.cicescini_act, 
                mesComplem:$("#cbxMesCompl").val(), idExalusCompl:idalus });
    mwfSelComplExalum_Close();
}