/* 
    Creado el : 18/07/2017, 05:26:19 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsDesoficializar;

function mwfDesoficializar_Show(event, tblPrincipal_selRow, bimeval, tituloBimEval, nombreBotonOficializar, nombreBotonDesoficializar)
{
    jsDesoficializar={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        bimeval:bimeval,
        tituloBimEval:tituloBimEval,
        nombreBotonOficializar:nombreBotonOficializar, 
        nombreBotonDesoficializar:nombreBotonDesoficializar,
        chkTodos: false
    };
    object_setVisible (true,"mwfmDesoficializar");                                      // Ocultamos el gridTable
    //$("#mwffDesoficializar").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfDesoficializar_Create();
    mwffDesoficializar_FormActivate ();
    //event.stopPropagation();
}

function mwfDesoficializar_Close()
{
    jsDesoficializar = null;
    modalWindow_Close ("Desoficializar");
    object_setVisible (false,"mwfmDesoficializar");
    $("#txtBusquedaCCT").focus();
}

function mwfDesoficializar_Create ()
{
    modalWindow_Create ("Desoficializar", "Desoficialización por alumno",mwfDesoficializar_Close, 90, {anchoAutoajustable:true});
    $('#mwfpDesoficializar').append('<div id="pnlDesoficializar"></div>');
    
        $('#pnlDesoficializar').append('<div id="pnlDatosGenerales"></div>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+jsDesoficializar.tblPrincipal_cct+'</label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+jsDesoficializar.tblPrincipal_grado+'</label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+jsDesoficializar.tblPrincipal_grupo+'</label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_tituloBimEval">'+jsDesoficializar.tituloBimEval+'</label>');
        $('#pnlDesoficializar').append('<div id="pnlListadoDeAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos a desoficializar</label>'
                                            + '<div id="pnlTblAlumnos">  <div id="scrlAlumnos" class="scrollTable"></div>  </div>'
                                        +'</div>');
        $('#pnlDesoficializar').append('<ul class="buttonBar"> '
                                            +'<li><a href="#" id="btnDesoficAlumnos"><label class="icon-disquete"></label> Desoficializar</a></li>'
                                        +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnDesoficAlumnos").on('click',function(){ btnDesoficAlumnos_Click(); });
}
function mwffDesoficializar_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"De", metodo:"foAc", tblPrincipal_cicescini:jsDesoficializar.tblPrincipal_cicescini, tblPrincipal_cveplan:jsDesoficializar.tblPrincipal_cveplan, 
        tblPrincipal_idcct:jsDesoficializar.tblPrincipal_idcct, tblPrincipal_grado:jsDesoficializar.tblPrincipal_grado, 
        tblPrincipal_grupo:jsDesoficializar.tblPrincipal_grupo, bimeval:jsDesoficializar.bimeval
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
                    initTablaAlumnosParaDesoficializar (result.tblAlumnos);
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

function initTablaAlumnosParaDesoficializar (tblAlumnos)
{    
    var tabla = new Tabla();
        
    tabla.create("scrlAlumnos","tblAlumnos",tblAlumnos, ["Seleccionar todos","idalu","CURP","NOMBRE COMPLETO"], ["selec","idalu","curp","nom_tot"], ["checkbox","","",""], ["center","","",""], true, null, null ,
        function (f,c){
            $("#tblAlumnos_chk_f"+f+"_c0").on('click', function(e){ 
                //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
                document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
            });

            $("#tblAlumnos_td_f"+f+"_c0").on('click', function(e){
                if (!$("#tblAlumnos_chk_f"+f+"_c0").prop("disabled"))
                    document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
            });
        });
    $('#tblAlumnos_divth0').append(" <input type='checkbox' id='chkSelTodos' name='chkSelTodos' value='false'>");
    $('#chkSelTodos').on("change",function(){ chkSelTodosAlusParaDesofic_Click(); });
    
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === 't'){
            document.getElementById("tblAlumnos_chk_f"+i+"_c0").checked = true;
            $("#tblAlumnos_chk_f"+i+"_c0").prop('disabled', true);
        }
    //if (jsDesoficializar.cveplan === "3")
    //    $("#pnlDesoficializar .tblAlumnos_col0").css("display","none");
}

function btnDesoficAlumnos_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"De", metodo:"btDe_Cl", tblPrincipal_cicescini:jsDesoficializar.tblPrincipal_cicescini, tblPrincipal_cveplan:jsDesoficializar.tblPrincipal_cveplan,
        tblPrincipal_idcct:jsDesoficializar.tblPrincipal_idcct, tblPrincipal_grado:jsDesoficializar.tblPrincipal_grado, chkTodos:jsDesoficializar.chkTodos,
        tblPrincipal_grupo:jsDesoficializar.tblPrincipal_grupo, bim:jsDesoficializar.bimeval, tblAlumnos:tabla.getTable("tblAlumnos",["selec","idalu"])
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
                    permisosParaDesoficializar(null, jsDesoficializar.nombreBotonOficializar, jsDesoficializar.nombreBotonDesoficializar, jsDesoficializar.bimeval);
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

function chkSelTodosAlusParaDesofic_Click()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows("tblAlumnos");
    var chkSelTodos_isSelected = document.getElementById("chkSelTodos").checked;
    jsDesoficializar.chkTodos = chkSelTodos_isSelected;
    
    if(chkSelTodos_isSelected){              
        for (var i=0; i<numFilas; i++)
            if (!$("#tblAlumnos_chk_f"+i+"_c0").prop("disabled"))
                document.getElementById('tblAlumnos_chk_f'+(i)+'_c0').checked = chkSelTodos_isSelected;
        //$("#txtSelDesde").val(1);
        //$("#txtSelHasta").val(numFilas);
    } else {
        for (var i=0; i<numFilas; i++)
            if (!$("#tblAlumnos_chk_f"+i+"_c0").prop("disabled"))
                document.getElementById('tblAlumnos_chk_f'+(i)+'_c0').checked = chkSelTodos_isSelected;
        
        //$(".coltblAlumnos_col0 input:checkbox").attr('checked', chkSelTodos_isSelected);
        //$("#txtSelDesde").val("");
        //$("#txtSelHasta").val("");
    }
}

function permisosParaDesoficializar_Desif (permiso, botonOficializar, botonDesoficializar)
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
