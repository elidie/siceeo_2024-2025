/* 
    Creado el : 3/08/2017, 04:08:39 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/


var jsCompl;

function mwfComplementaria_Show(tblPrincipal_selRow, casoRep)
{
    jsCompl={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_cicescinilib: tblPrincipal_selRow.cicescinilib,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        
        casoRep:casoRep
    };
    object_setVisible (true,"mwfmComplementaria");                                      // Ocultamos el gridTable
    //$("#mwffComplementaria").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfComplementaria_Create();
    mwffComplementaria_FormActivate ();
    //event.stopPropagation();
}

function mwfComplementaria_Close()
{
    $('#mwfmComplementaria #mwffComplementaria').remove();
    object_setVisible (false,"mwfmComplementaria");
    $("#txtBusquedaCCT").focus();
}

function mwfComplementaria_Create ()
{
    modalWindow_Create ("Complementaria", "Alumnos para mostrarse en complementaria",mwfComplementaria_Close, 90, {anchoAutoajustable:true});
    $('#mwfpComplementaria').append('<div id="pnlComplementaria"></div>');
    
        $('#pnlComplementaria').append('<div id="pnlDatosGenerales"></div>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+jsCompl.tblPrincipal_cct+'</label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+jsCompl.tblPrincipal_grado+'</label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+jsCompl.tblPrincipal_grupo+'</label>');
        $('#pnlComplementaria').append('<div id="pnlListadoDeAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos para Complementaria</label>'
                                            + '<div id="pnlTblAlumnos">  <div id="scrlAlumnos" class="scrollTable"></div>  </div>'
                                        +'</div>');
        $('#pnlComplementaria').append('<ul class="buttonBar"> '
                                            + ((jsCompl.casoRep === "oficCertCompl")
                                                ?'<li><a href="#" id="btnOficCerCompl"><label class="icon-sello"></label> Foliar y enviar a SIGED</a></li>'
                                                :'<li><a href="#" id="btnImpCompl"><label class="icon-disquete"></label>Imprimir</a></li>')
                                        +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnImpCompl").on('click',function(){ btnImpCompl_Click(); });
    $("#btnOficCerCompl").on('click',function(){ btnOficCerCompl_Click(); });
}
function mwffComplementaria_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Compl", metodo:"foAc",tblPrincipal_cicescinilib:jsCompl.tblPrincipal_cicescinilib, tblPrincipal_idcct:jsCompl.tblPrincipal_idcct, 
        tblPrincipal_grado:jsCompl.tblPrincipal_grado, tblPrincipal_grupo:jsCompl.tblPrincipal_grupo, casoRep:jsCompl.casoRep
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
                    initTablaAlumnosParaComplementaria (result.tblAlumnos);
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

function initTablaAlumnosParaComplementaria (tblAlumnos)
{
    var tabla = new Tabla();
    var tituloColumnas, colsNameToShow, claseColumnas, textAlign;
    if (jsCompl.casoRep === "oficCertCompl"){
        tituloColumnas = ["Seleccionar","idalu","mes","promediogral","CURP","NOMBRE COMPLETO"];
        colsNameToShow = ["selec","idalu","mes","promediogral","curp","nom_tot"];
        claseColumnas = ["checkbox","","","","",""];
        textAlign = ["center","","","","",""];
    }else{
        tituloColumnas = ["Seleccionar","idalu","CURP","NOMBRE COMPLETO"];
        colsNameToShow = ["selec","idalu","curp","nom_tot"];
        claseColumnas = ["checkbox","","",""];
        textAlign = ["center","","",""];
    }
        
        tabla.create("scrlAlumnos","tblAlumnos",tblAlumnos, tituloColumnas, colsNameToShow, claseColumnas, textAlign, true, null, null ,
        function (f,c){
            $("#pnlComplementaria #tblAlumnos_chk_f"+f+"_c0").on('click', function(e){ 
                //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
                document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
            });

            $("#pnlComplementaria #tblAlumnos_td_f"+f+"_c0").on('click', function(e){
                if (!$("#pnlComplementaria #tblAlumnos_chk_f"+f+"_c0").prop("disabled"))
                    document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked = !document.getElementById("tblAlumnos_chk_f"+f+"_c0").checked;
            });
        });
    //$('#pnlComplementaria #tblAlumnos_divth0').append(" <input type='checkbox' id='chkSelTodos' name='chkSelTodos' value='false'>");
    //$('#pnlComplementaria #chkSelTodos').on("change",function(){ chkSelTodosAlusParaCompl_Click(); });
    
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === 't'){
            document.getElementById("tblAlumnos_chk_f"+i+"_c0").checked = true;
            $("#pnlComplementaria #tblAlumnos_chk_f"+i+"_c0").prop('disabled', true);
        }
    //if (jsComplementaria.cveplan === "3")
    //    $("#pnlComplementaria .tblAlumnos_col0").css("display","none");
}

function btnImpCompl_Click()
{    
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var idalus="";
    
    var tblAlumnos = tabla.getTable("tblAlumnos",["selec","idalu"], null,"JSON");
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === "true")
            idalus += (idalus===""?"":", ") + tblAlumnos[i].idalu;
    
    if (idalus === "")
        mensaje.General("ESPECIFIQUE_DATO","por lo menos un alumno");
    else {        
        mensaje.General("REPORTE_EN_CREACION","","","");
        showReport ("mwfpComplementaria", "Reportes/FinDeCurso.jsp", {cicescinilib:jsCompl.tblPrincipal_cicescinilib,cicescini:jsCompl.tblPrincipal_cicescini, cveplan:jsCompl.tblPrincipal_cveplan, 
                                                                idcct:jsCompl.tblPrincipal_idcct, modalidad:jsCompl.tblPrincipal_modalidad, 
                                                                grado:jsCompl.tblPrincipal_grado, grupo:jsCompl.tblPrincipal_grupo, 
                                                                caso:jsCompl.casoRep, idalusCompl:idalus});
         mwfComplementaria_Close();
    }
}

function btnOficCerCompl_Click ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var idalus="";
    
    var tblAlumnos = tabla.getTable("tblAlumnos",["selec","idalu"], null,"JSON");
    for (var i=0; i<tblAlumnos.length; i++)
        if (tblAlumnos[i].selec === "true")
            idalus += (idalus===""?"":", ") + tblAlumnos[i].idalu;
    
    if (idalus === "")
        mensaje.General("ESPECIFIQUE_DATO","por lo menos un alumno");
    else {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Compl", metodo:"btOfCeCo_cl", tblPrincipal_cicescini:jsCompl.tblPrincipal_cicescini, tblPrincipal_idcct:jsCompl.tblPrincipal_idcct, 
            tblPrincipal_cveplan:jsCompl.tblPrincipal_cveplan, tblPrincipal_grado:jsCompl.tblPrincipal_grado, tblPrincipal_grupo:jsCompl.tblPrincipal_grupo,
            idalus:idalus
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
                        //mensaje.General("PROCESO_EXITOSO");
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        permisosParaDesoficializar (result.noHasAluCompl, "btnOficCertCompl", "btnSinCertCompl");
                        mwfComplementaria_Close();
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

function chkSelTodosAlusParaCompl_Click()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows("tblAlumnos");
    var chkSelTodos_isSelected = document.getElementById("chkSelTodos").checked;
    
    if(chkSelTodos_isSelected){
        for (var i=0; i<numFilas; i++)
            if (!$("#tblAlumnos_chk_f"+i+"_c0").prop("disabled"))
                document.getElementById('tblAlumnos_chk_f'+(i)+'_c0').checked = chkSelTodos_isSelected;
        //$("#txtSelDesde").val(1);
        //$("#txtSelHasta").val(numFilas);
    }else {
        for (var i=0; i<numFilas; i++)
            if (!$("#tblAlumnos_chk_f"+i+"_c0").prop("disabled"))
                document.getElementById('tblAlumnos_chk_f'+(i)+'_c0').checked = chkSelTodos_isSelected;
        
        //$(".coltblAlumnos_col0 input:checkbox").attr('checked', chkSelTodos_isSelected);
        //$("#txtSelDesde").val("");
        //$("#txtSelHasta").val("");
    }
}