/* 
    Creado el : 18/07/2017, 05:26:19 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsOfiXalu;

function mwfOficializarXalu_Show(event, tblPrincipal_selRow, bimeval, tituloBimEval, nombreBotonOficializar, nombreBotonDesoficializar)
{
    jsOfiXalu={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado: tblPrincipal_selRow.grado,
        tblPrincipal_grupo: tblPrincipal_selRow.grupo,
        bimeval: bimeval,
        tituloBimEval: tituloBimEval,
        nombreBotonOficializar: nombreBotonOficializar, 
        nombreBotonDesoficializar: nombreBotonDesoficializar,
        chkTodos: false
    };
    object_setVisible (true,"mwfmOficializarXalu");                                      // Ocultamos el gridTable
    //$("#mwffDesoficializar").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfOficializarXalu_Create();
    mwffOficializarXalu_FormActivate ();
    //event.stopPropagation();
}

function mwfOficializarXalu_Close()
{
    jsOfiXalu = null;
    modalWindow_Close ("Oficializar");
    object_setVisible (false,"mwfmOficializarXalu");
    $("#txtBusquedaCCT").focus();
}

function mwfOficializarXalu_Create ()
{
    modalWindow_Create ("OficializarXalu", "Oficialización por alumno",mwfOficializarXalu_Close, 90, {anchoAutoajustable:true});
    $('#mwfpOficializarXalu').append('<div id="pnlOficializarXalu"></div>');
    
        $('#pnlOficializarXalu').append('<div id="pnlDatosGrales"></div>');
                            $('#pnlDatosGrales').append('<label id="lblTblPrincipal_cct">'+jsOfiXalu.tblPrincipal_cct+'</label>');
                            $('#pnlDatosGrales').append('<label id="lblTblPrincipal_grado">'+jsOfiXalu.tblPrincipal_grado+'</label>');
                            $('#pnlDatosGrales').append('<label id="lblTblPrincipal_grupo">'+jsOfiXalu.tblPrincipal_grupo+'</label>');
                            $('#pnlDatosGrales').append('<label id="lblTblPrincipal_tituloBimEval">'+jsOfiXalu.tituloBimEval+'</label>');
        $('#pnlOficializarXalu').append('<div id="pnlListadoDeXAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos a Oficializar</label>'
                                            + '<div id="pnlTblXAlumnos">  <div id="scrlXAlumnos" class="scrollTable"></div>  </div>'
                                        +'</div>');
        $('#pnlOficializarXalu').append('<ul class="buttonBar"> '
                                            +'<li><a href="#" id="btnOficAlumnos"><label class="icon-disquete"></label> Oficializar</a></li>'
                                        +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnOficAlumnos").on('click',function(){ btnOficAlumnos_Click(); });
}
function mwffOficializarXalu_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Of", metodo:"foAcXalu", tblPrincipal_cicescini:jsOfiXalu.tblPrincipal_cicescini, tblPrincipal_cveplan:jsOfiXalu.tblPrincipal_cveplan, 
        tblPrincipal_idcct:jsOfiXalu.tblPrincipal_idcct, tblPrincipal_grado:jsOfiXalu.tblPrincipal_grado, 
        tblPrincipal_grupo:jsOfiXalu.tblPrincipal_grupo, bimeval:jsOfiXalu.bimeval
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
                    initTablaAlumnosParaOfXAlu (result.tblAlumnos);
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

function initTablaAlumnosParaOfXAlu (tblAlumnos)
{    
    var tabla = new Tabla();  
    tabla.create("scrlXAlumnos","tblXAlumnos",tblAlumnos, ["Seleccionar todos","idalu","CURP","NOMBRE COMPLETO"], ["selec","idalu","curp","nom_tot"], ["checkbox","","",""], ["center","","",""], true, null, null ,
        function (f,c){
            $("#tblXAlumnos_chk_f"+f+"_c0").on('click', function(e){ 
                //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
                document.getElementById("tblXAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblXAlumnos_chk_f"+f+"_c0").checked;
            });

            $("#tblXAlumnos_td_f"+f+"_c0").on('click', function(e){
                if (!$("#tblXAlumnos_chk_f"+f+"_c0").prop("disabled"))
                    document.getElementById("tblXAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblXAlumnos_chk_f"+f+"_c0").checked;
            });
        });
    $('#tblXAlumnos_divth0').append(" <input type='checkbox' id='chkSelTodos' name='chkSelTodos' value='false'>");
    $('#chkSelTodos').on("change",function(){ chkSelTodosAlusParaOfic_Click(); });
    
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === 't'){
            document.getElementById("tblXAlumnos_chk_f"+i+"_c0").checked = true;
            $("#tblXAlumnos_chk_f"+i+"_c0").prop('disabled', true);
        }
    //if (jsOfiXalu.cveplan === "3")
    //    $("#pnlDesoficializar .tblAlumnos_col0").css("display","none");
}

function btnOficAlumnos_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Of", metodo:"of_CaXAlu", tblPrincipal_cicescini:jsOfiXalu.tblPrincipal_cicescini, tblPrincipal_cveplan:jsOfiXalu.tblPrincipal_cveplan,
        tblPrincipal_idcct:jsOfiXalu.tblPrincipal_idcct, tblPrincipal_grado:jsOfiXalu.tblPrincipal_grado, chkTodos:jsOfiXalu.chkTodos,
        tblPrincipal_grupo:jsOfiXalu.tblPrincipal_grupo, bim:jsOfiXalu.bimeval, tblAlumnos:tabla.getTable("tblXAlumnos",["selec","idalu"])
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
                    mensaje.General("PROCESO_EXITOSO");
                    jsOficializar.ofsCal=result.ofsCal;
                    jsOficializar.ofsEval=result.ofsEval;
                    permisosParaDesoficializar(null, jsOfiXalu.nombreBotonOficializar, jsOfiXalu.nombreBotonDesoficializar, jsOfiXalu.bimeval);
                    mwfDesoficializar_Close();
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

function chkSelTodosAlusParaOfic_Click()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows("tblXAlumnos");
    var chkSelTodos_isSelected = document.getElementById("chkSelTodos").checked;
    jsOfiXalu.chkTodos = chkSelTodos_isSelected;
    
    if(chkSelTodos_isSelected){              
        for (var i=0; i<numFilas; i++)
            if (!$("#tblXAlumnos_chk_f"+i+"_c0").prop("disabled"))
                document.getElementById('tblXAlumnos_chk_f'+(i)+'_c0').checked = chkSelTodos_isSelected;
        //$("#txtSelDesde").val(1);
        //$("#txtSelHasta").val(numFilas);
    } else {
        for (var i=0; i<numFilas; i++)
            if (!$("#tblXAlumnos_chk_f"+i+"_c0").prop("disabled"))
                document.getElementById('tblXAlumnos_chk_f'+(i)+'_c0').checked = chkSelTodos_isSelected;
        
        //$(".coltblAlumnos_col0 input:checkbox").attr('checked', chkSelTodos_isSelected);
        //$("#txtSelDesde").val("");
        //$("#txtSelHasta").val("");
    }
}

function permisosParaOficializar_Desif (permiso, botonOficializar, botonDesoficializar)
{
    botonOfic_setVisible_Desif(!permiso,botonOficializar);
    botonOfic_setVisible_Desif(permiso,botonDesoficializar);
}

function botonOfic_setVisible_Desif (enabled, nombreObjeto)
{
    if (enabled === true)
        $( '#'+nombreObjeto).css("display","");
    else
        $( '#'+nombreObjeto).css("display","none");
}
