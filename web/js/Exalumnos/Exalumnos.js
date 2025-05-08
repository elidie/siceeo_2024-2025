/* 
 Creado el : 13/09/2017, 11:57:18 AM
 Autor     : Ing. Maai Nolasco Sánchez
 */

var jsExalu;
function frmwExalumnos_Show (tblPrincipal_selRow)
{
    jsExalu={
        tblPrincipal_cicescini:tblPrincipal_selRow.cicescini,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        cicescinilib:null
    };
    
    object_setVisible (false,'gridTable');
    $("#frmwExalumnos").css("display", "block");                          // Mostramos el formulario correspondiente

    frmwExalumnos_Create();
    frmwExalumnos_FormActivate ();
}

function frmwExalumnos_Close()
{
    jsExalu=null;
    irAVentanaPrincipal ();
}

function frmwExalumnos_Create() 
{
    var tabindexIni = 100, tabindexReturn=100;
    
    if($('#frmfExalumnos').length)                                                // Verificando si la tabla Existe
        $('#frmfExalumnos').remove();
    $('#frmwExalumnos').append('<fieldset id="frmfExalumnos"><legend>Exalumnos con examen de regularización</legend><div id="btnRegresar_Exa" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfExalumnos").append('<div id="pnlExalumnos"></div>');
//.........................................................................................................................................................................................................................................................................
            $("#pnlExalumnos").append('<div id="pnlOpcBusqueda" class="panel"><label class="tituloPanel">Área de búsqueda</label></div>');
                $("#pnlOpcBusqueda").append(
                        '<div>'
                            +'<label id="lblCicloFinal">Ciclo escolar en el que estudió 3er grado: </label>'
                            +'<input type="text" id="txtCicesciniEstud" title="Ciclo escolar en el que estudió su último gado de estudios" maxlength="4" tabindex="'+(tabindexIni++)+'"/>'
                            +'<label id="lblCicesciniEstud"></label>'
                        +'</div>'
                        +'<div id="pnlCurp_Ubicar">'
                              +'<input type="radio" id="rbnCurp" name="rbgCurpIdalu_Ubicar" value="rbnCurp" tabindex="'+(tabindexIni++)+'">'
                              +'<label id="lblCurpA">CURP: </label> <input type="text" id="txtCurpA" maxlength="18" tabindex="'+(tabindexIni++)+'"/>'
                        +'</div>'
                        + '<div id="pnlIdalu_Ubicar">'
                            +'<input type="radio" id="rbnIdaluX" name="rbgCurpIdalu_Ubicar" value="rbnIdaluX" tabindex="'+(tabindexIni++)+'">'
                            +'<label id="lblIdaluA">IdALU: </label> <input type="text" id="txtIdaluA" maxlength="18" tabindex="'+(tabindexIni++)+'"/>'
                        +'</div>'
                        + '<ul class="buttonBar"> '
                            +'<li><a href="#" id="btnBuskAlumA" tabindex="'+(tabindexIni++)+'"><label class="icon-lupa"></label> Buscar</a></li> '
                        + '</ul>'
                    );
                $("#pnlOpcBusqueda").append('<div id="pnlCurpsYNombre"></div>');
                    $("#pnlCurpsYNombre").append(
                              '<select id="cbxCurps" name="cbxCurps"  tabindex="'+(tabindexIni++)+'"></select>  '
                            + '<label id="lblNombreCompleto"></label>'
                        );
            
            $("#pnlExalumnos").append('<div id="pnlHistorialYMaterias"></div>');
                $("#pnlHistorialYMaterias").append('<div id="pnlAlumgrado" class="panel"> <label class="tituloPanel">Historial académico</label>'
                                                    +'<div id="scrlAlumgrado" class="scrollTable"></div>'
                                                  +'</div>');
                $("#pnlHistorialYMaterias").append('<div id="pnlMaterias" class="panel"> <label class="tituloPanel">Materias</label>'
                                                        + '<div id="scrlMaterias" class="scrollTable"></div>'
                                                    +'</div>');
                $("#pnlHistorialYMaterias").append('<div id="pnlFoliosCert" class="panel"> <label class="tituloPanel">Certificados</label>'
                                                        + '<div id="scrlFoliosCert" class="scrollTable"></div>'
                                                    +'</div>');
            
            $('#pnlExalumnos').append('<div id="pnlBotonesDeControl"></div>');
                $('#pnlBotonesDeControl').append('<ul id="ulHistFolImp" class="buttonBar"> '
                                            +'<li><a href="#" id="btnHistAcad" title="Muestra la ventana para ingresar calificaciones de exámenes extraordinarios">Exam. Extraord.</a></li>'
                                            //+'<li><a href="#" id="btnFoliarYFirmar" title="Folea y firma a este alumno">Foliar y firmar.</a></li>'
                                            +'<li><a href="#" id="btnImprimirCert" title="Imprime el certificado del alumno">Imp. Certificado</a></li>'
                                            //+'<li><a href="#" id="btnUpdatePromFol" title="Actualiza el promedio faltante al folio de secundaria">Actualiza prom. de folio</a></li>'
                                        +'</ul>');
                $('#pnlBotonesDeControl').append('<ul id="ulImprel" class="buttonBar"> '
                                            +'<li><a href="#" id="btnImpRelComp" title="Imprime REL complementaria del 3er grado grupo '+jsExalu.tblPrincipal_grupo+'">Imp. REL comp. ('+jsExalu.tblPrincipal_grado+jsExalu.tblPrincipal_grupo+')</a></li>'
                                        +'</ul>');
            //$('#pnlExalumnos').append('<label id="lblRecomendaciones">¡NOTA IMPORTANTE! Revise su captura, cuenta con 5 días para foliar y firmar e imprimir a partir de que introduce los datos del examen, si rebasa el tiempo deberá acudir a su UDSE para este proceso.</label>');
        $("#frmfExalumnos").append('<div id="mwfmSelComplExalum" class="mwfModal" style="display:none"></div>');  
    
    insertarTablas_AlumMatsCertiYComen_Exal (null, null, null, null);
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    $("#btnRegresar_Exa").on("click",function(){ frmwExalumnos_Close(); });
    
    $("#txtCicesciniEstud").on("keyup",function (e){ txtCicesciniEstud_Keyup (); });
    $("#rbnCurp").on("click", function(){ rbnCurp_Exal_Click (); });
    $("#rbnIdaluX").on("click", function(){ rbnIdalu_Exal_Click (); });
    $("#lblCurpA").on("click", function(){ lblCurp_Exal_Click (); });
    $("#lblIdaluA").on("click", function(){ lblIdalu_Exal_Click (); });
    $("#txtCurpA").on("focus", function(){ txtCurp_Exal_Focus (); });
    $("#txtIdaluA").on("focus", function(){ txtIdalu_Exal_Focus (); });
    $("#txtCurpA").on("keydown", function(e){ txtCurp_Exal_KeyDown (e); });
    $("#txtIdaluA").on("keydown", function(e){ txtIdalu_Exal_KeyDown (e); });
    
    $("#btnBuskAlumA").on("keydown", function(e){ if(e.which === 13){  btnBuskAlum_Exal_ActionPerformed (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnBuskAlumA').on("click",function(){ btnBuskAlum_Exal_ActionPerformed (); });
    
    $("#btnHistAcad").on('click',function(){ btnHistAcad_Exal_Click();  return false;});
    //$("#btnFoliarYFirmar").on('click',function(){ btnFoliarYFirmarExalumno_Click();  return false;});
    $("#btnImprimirCert").on('click',function(){ btnImprimirCert_Click();  return false;});
    //$("#btnUpdatePromFol").on('click',function(){ btnUpdatePromFol_Click();  return false;});
    $("#btnImpRelComp").on('click',function(){ btnImpRelComp_Click();  return false;});
    
}

function frmwExalumnos_FormActivate ()
{
    var mensaje = new Mensajes();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Exal", metodo:"foAc"
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
                    //-------------------- Asignamos variables ---------------------                    
                    jsExalu.cicescinilib=result.cicescinilib;
                    if (!result.btnUpdatePromFol_setVisible)
                        $('#pnlExalumnos #btnUpdatePromFol').remove();
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
        //cerrarLoading();
    })
    .always(function() {
        cerrarLoading();
    });
}

function txtCicesciniEstud_Keyup ()
{
    if ( esNumeroEntero($("#txtCicesciniEstud").val()) )
        $("#lblCicesciniEstud").text(' - '+(parseInt($("#txtCicesciniEstud").val())+1));
    else
        $("#lblCicesciniEstud").text("");
}

function rbnCurp_Exal_Click ()
{
    document.getElementById('txtCurpA').focus();
}

function rbnIdalu_Exal_Click ()
{
    document.getElementById('txtIdaluA').focus();
}

function lblCurp_Exal_Click ()
{
    document.getElementById('txtCurpA').focus();
    document.getElementById('rbnCurp').checked = true;
}

function lblIdalu_Exal_Click ()
{
    document.getElementById('txtIdaluA').focus();
    document.getElementById('rbnIdaluX').checked = true;
}

function txtCurp_Exal_Focus ()
{
    document.getElementById('txtCurpA').focus();
    document.getElementById('rbnCurp').checked = true;
}

function txtIdalu_Exal_Focus ()
{
    lblIdalu_Exal_Click ();
}
    
function txtCurp_Exal_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
         btnBuskAlum_Exal_ActionPerformed ();
}

function txtIdalu_Exal_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
         btnBuskAlum_Exal_ActionPerformed ();
}

function insertarTablas_AlumMatsCertiYComen_Exal (tblAlumgrado, tblAlumgrado_MouseClick, tblMaterias, tblFoliosCert)
{
    var tabla = new Tabla();
    tabla.create("scrlAlumgrado","tblAlumgrado",tblAlumgrado, ["idcct","zona","cct","tno","unidad","ciclo","Gdo","Gpo","Acred","Prom","PG","PEB","Est","Pmv","MRAn","MRac"], ["idcct","cvezona","cct","cveturno","cveunidad","inifin","grado","grupo","tipoacred","promedio","promediogral","promedioeb","estatusgrado","promovido","matrepant","matrepact"], null, null, true, null, tblAlumgrado_MouseClick, null);
    insertarTabla_Materias_Exal (tblMaterias);
    insertarTabla_FoliosCert(tblFoliosCert);
    
}

function insertarTabla_Materias_Exal (tblMaterias)
{
    var tabla = new Tabla();
    tabla.create("scrlMaterias","tblMaterias",tblMaterias, ["Materia","promedio"], ["desmat","promedio"], null, ["","center"], true, null, null, null);
}

function insertarTabla_FoliosCert(tblFoliosCert)
{
    var tabla = new Tabla();
    tabla.create("scrlFoliosCert","tblFoliosCert",tblFoliosCert, ["FIRMADO","Libro","Letra","folio","Promedio Gral.","cct","grupo","curp","Nombre completo", "Fecha de expedición"], ["tienefirma","inifin","follet","folionum","promediogral","cct","grupo","curp","nom_tot","alos"], null, ["center","center","center","center","center","center","curp","nom_tot"], true, null, null, null);
}

var  btnBuskAlum_Exal_ActionPerformed = function ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ($("#txtCicesciniEstud").val().trim() === "")
        return mensaje.General ("CAMPO_VACIO","CICLO ESCOLAR");
    if ( $("#txtCurpA").val().trim()==="" && $("#txtIdaluA").val().trim()==="" )
        return mensaje.General ("ESPECIFIQUE_DATO","una curp o un idalu");
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Exal", metodo:"btBuAl_AcPe", rbnCurp_isChecked:document.getElementById('rbnCurp').checked, txtCicesciniEstud:$("#txtCicesciniEstud").val().trim(),
        rbnIdaluX_isChecked:document.getElementById('rbnIdaluX').checked, txtCurp:$("#txtCurpA").val().trim().toUpperCase(), 
        txtIdalu:$("#txtIdaluA").val().trim()
    };
    //------------------------- Hacemos la llamada -------------------------
     cargarLoading();
     $.ajax({
        url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async: true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    jsExalu.cicescinilib_ex = result.cicescinilib_ex;
                    jsExalu.cicescini_ex = result.cicescini_ex;
                    jsExalu.idaluEncontrado = result.idaluEncontrado;
                    //boton_setEnabled (result.btnCambSituacion_Enabled, 'btnCambSituacion');
                    //$("#btnCambSituacion").text(result.btnCambSituacion_Text);
                    boton_setEnabled(result.canEditExmExt,"btnHistAcad", "click", btnHistAcad_Exal_Click);
                    boton_setEnabled(result.canFoliarYFirmar,"btnFoliarYFirmar", "click", btnFoliarYFirmarExalumno_Click);
                    
                    $('#txtCurpA').val(result.txtCurp_text);
                    $('#txtIdaluA').val(result.txtIdalu_Text);
                    $('#lblNombreCompleto').text(result.lblNombreCompleto);
                     
                    if (typeof result.mensaje!=="undefined")
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    
                    
                    insertarTablas_AlumMatsCertiYComen_Exal (result.tblAlumgrado, tblAlumgrado_Exal_MouseClicked, result.tblMaterias, result.tblFoliosCert);
                    tabla.setSelectedRow("tblAlumgrado",tabla.getNumRows("tblAlumgrado")-1,false);
                    
                    // Cargamos el combo de curps
                    //$("#cbxEntidad").append("<option value=''  selected></option>");
                    $("#cbxCurps").empty();
                    $.each(result.cbxCurps,function(i,valor) {
                        $("#cbxCurps").append("<option value='"+valor["idalu"]+"'>"+valor["curp"]+"</option>");                        
                    });
                    
                    //cerrarLoading();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    //cerrarLoading();
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
};

var tblAlumgrado_Exal_MouseClicked = function()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var dataRow = tabla.getSelectedRow("tblAlumgrado",["idalu","cicescini"],null,"JSON");
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Exal", metodo:"tbAl_MoCl", tblAlumgrado_cicescini:dataRow.cicescini, tblAlumgrado_idalu:dataRow.idalu
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({
       url:"../sis_web/siS1",
       type:"POST",
       dataType:"JSON",
       data: datos,
       async: true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    insertarTabla_Materias_Exal (result.tblMaterias);
                    //cerrarLoading();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    //cerrarLoading();
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
};

var btnHistAcad_Exal_Click = function()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    
    if (tabla.getNumRows("tblAlumgrado")>0){
        var tblAlumgrado_row = tabla.getRow("tblAlumgrado",0,["idalu","cicescini"],null,"JSON");
        frmwSecHist_Show (tblAlumgrado_row.cicescini,tblAlumgrado_row.idalu, $("#txtCurpA").val().trim().toUpperCase(), $("#lblNombreCompleto").text().trim().toUpperCase(), "2", 
                        false, false, {nameParentForm:"Exalumnos", onClose:btnBuskAlum_Exal_ActionPerformed},"Exalumnos");
    }else
        mensaje.Exalumnos("SIN_HISTORIAL");
};

var btnFoliarYFirmarExalumno_Click = function()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    
    var tblFoliosCert_lastRow, numFoliosCert, tblAlumgrado_rowCount=tabla.getNumRows("tblAlumgrado");
    var ejecutarFoliado = true;
    
    if (tblAlumgrado_rowCount<=0)
        mensaje.Exalumnos("SIN_HISTORIAL");
    else
    {
        var tblAlumgrado_row = tabla.getRow("tblAlumgrado",tblAlumgrado_rowCount-1,["idalu","grado","grupo", "cicescini"],null,"JSON");
        numFoliosCert = tabla.getNumRows("tblFoliosCert");
        if (numFoliosCert>0)
            tblFoliosCert_lastRow = tabla.getRow("tblFoliosCert",numFoliosCert-1,["cveplan","grado","promediogral","tienefirma"], null, "JSON");
        
        if (tblAlumgrado_row.grado !== "3")
            return mensaje.Exalumnos("HISTORIAL_INCOMPLETO");
        else if (numFoliosCert>0 && tblFoliosCert_lastRow.cveplan==="2" && tblFoliosCert_lastRow.grado==="3") //Si ya tiene un folio
            ejecutarFoliado = mensaje.Exalumnos("CANCELAR_FOL_CERTIF", "","","CONFIRM_DIALOG"); //Preguntamos si lo quiere cancelar

        if (ejecutarFoliado)
        {
            //------------------ Establecemos los datos a enviar -------------------
            var datos = {
                modulo:"Exal", metodo:"tbFoYFiEx_Cl", cicesciniestud:tblAlumgrado_row.cicescini, tblPrincipal_cicescini:jsExalu.tblPrincipal_cicescini, 
                tblPrincipal_idcct:jsExalu.tblPrincipal_idcct, tblPrincipal_cveplan:jsExalu.tblPrincipal_cveplan, grado:tblAlumgrado_row.grado, 
                grupo:tblAlumgrado_row.grupo, idalu:tblAlumgrado_row.idalu
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
                            insertarTabla_FoliosCert(result.tblFoliosCert);
                            mensaje.General("FIRMAYFOLIADO");
                            //permisosParaDesoficializar (result.noHasAluCompl, "btnOficCertCompl", "btnSinCertCompl");
                            //mwfComplementaria_Close();
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
};

var btnImprimirCert_Click = function ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    var tblFoliosCert_lastRow=null;
    var numFoliosCert = tabla.getNumRows("tblFoliosCert");
    
    if (numFoliosCert === 0)
        mensaje.Exalumnos("NO_HAY_NINGUN_FOLIO");
    else {
        tblFoliosCert_lastRow = tabla.getRow("tblFoliosCert",numFoliosCert-1,["cveplan","grado","promediogral","tienefirma", "alos","grupo","idcct"], null, "JSON");
        if (tblFoliosCert_lastRow.cveplan!=="2" && tblFoliosCert_lastRow.grado!=="3")
            mensaje.Exalumnos("SIN_FOLIO_SECU");
        else if (tblFoliosCert_lastRow.promediogral === "")
            mensaje.Exalumnos("FOLIO_SIN_PROMEDIO");
        else if (tblFoliosCert_lastRow.tienefirma !== "SI")
            mensaje.Exalumnos("SIN_FIRMA");
        else if (tblFoliosCert_lastRow.alos.indexOf("ocho días del mes de julio") >= 0)
            mensaje.Exalumnos("MESEXPED_NO_ACEPTADO");
        else{            
            mensaje.General("REPORTE_EN_CREACION","","","");
            showReport ("frmfExalumnos", "Reportes/Certificado.jsp", {cicescini:jsExalu.cicescini_ex, cicescinilib:jsExalu.cicescinilib_ex, 
                        cveplan:jsExalu.tblPrincipal_cveplan,idcct_c:tblFoliosCert_lastRow.idcct, idcct:jsExalu.tblPrincipal_idcct, 
                        grado:jsExalu.tblPrincipal_grado, grupo:tblFoliosCert_lastRow.grupo,idalus:jsExalu.idaluEncontrado
                        });
        }
    }
};

function btnUpdatePromFol_Click ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblFoliosCert_lastRow=null, numFoliosCert;
    
    numFoliosCert = tabla.getNumRows("tblFoliosCert");
    if (numFoliosCert<=0)
        mensaje.Exalumnos ("NO_HAY_NINGUN_FOLIO");
    else{
        tblFoliosCert_lastRow = tabla.getRow("tblFoliosCert",numFoliosCert-1,["idalu","cveplan","grado","promediogral","tienefirma"], null, "JSON");
        if ( !(tblFoliosCert_lastRow.cveplan==="2" && tblFoliosCert_lastRow.grado==="3") ) //Si no tiene un folio de secundaria
            mensaje.Exalumnos("SIN_FOLIO_SECU");
        else
        {
            //------------------ Establecemos los datos a enviar -------------------
            var datos = {
                modulo:"Exal", metodo:"btUpPrFo_Cl", idalu:tblFoliosCert_lastRow.idalu
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
                            insertarTabla_FoliosCert(result.tblFoliosCert);
                            mensaje.General("PROCESO_EXITOSO");
                            //permisosParaDesoficializar (result.noHasAluCompl, "btnOficCertCompl", "btnSinCertCompl");
                            //mwfComplementaria_Close();
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
}

var btnImpRelComp_Click = function ()
{
    mwfSelComplExalum_Show ( {cicescini:jsExalu.cicescini_ex, cveplan:jsExalu.tblPrincipal_cveplan, cct:jsExalu.tblPrincipal_cct, 
                            idcct:jsExalu.tblPrincipal_idcct, modalidad:jsExalu.tblPrincipal_modalidad, grado:jsExalu.tblPrincipal_grado, 
                            grupo:jsExalu.tblPrincipal_grupo, cicescinilib:jsExalu.cicescinilib_ex, cicescini_act:jsExalu.tblPrincipal_cicescini  });
};

