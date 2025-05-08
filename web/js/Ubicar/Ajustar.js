/* 
    Creado el : 1/12/2015, 09:08:04 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsAjustar;

function frmwAjustar_Show ( nameParentForm, kienLlamas, idaluX, CURPd10 )
{
    jsAjustar = {
        QSusEstud_Filtered: null,
        QSuFolio_Filtered: null,
        nameParentForm: nameParentForm
    };
    
    object_setVisible (false, nameParentForm);
    $("#frmwAjustar").css("display", "block");                                  // Mostramos el formulario correspondiente
    
    frmwAjustar_Create ();
    frmwAjustar_FormActivate (kienLlamas, idaluX, CURPd10);
    ocultarBotonesNoImplementados ();  //OJO: Esta función será temporal mientras no estén implementados los botones
}

function frmwAjustar_Close()
{
    var nombreFormularioQueLlama = jsAjustar.nameParentForm;
    jsAjustar=null;
    if (typeof nombreFormularioQueLlama!=="undefined" && nombreFormularioQueLlama!==null && nombreFormularioQueLlama!==""){
        if($("#frmfAjustar").length)
            $("#frmfAjustar").remove();
        object_setVisible(true, nombreFormularioQueLlama);
    }else
        irAVentanaPrincipal ();
}

function frmwAjustar_Create ()
{
    if($('#frmfAjustar').length) // Verificando si la tabla Existe
        $('#frmfAjustar').remove();
    $('#frmwAjustar').append('<fieldset id="frmfAjustar"><legend>ReUbicar al alumno</legend><div id="btnRegresar_Ajustar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfAjustar").append('<div id="pnlAjustar"></div>');
        
            $("#pnlAjustar").append('<div id="pnlOpcBusqueda"></div>');
                $("#pnlOpcBusqueda").append(
                          '<div id="pnlCurp_Ubicar">   <input type="radio" id="rbnCurp" name="rbgCurpIdalu_Ubicar" value="rbnCurp" tabindex="12"><label id="lblCurpA">CURP: </label>   <input type="text" id="txtCurpA" maxlength="18" tabindex="13"/>   </div>'
                        + '<div id="pnlIdalu_Ubicar">   <input type="radio" id="rbnIdaluX" name="rbgCurpIdalu_Ubicar" value="rbnIdaluX" tabindex="14"> <label id="lblIdaluA">IdALU: </label>   <input type="text" id="txtIdaluA" maxlength="18" tabindex="15"/>   </div>'
                        + '<ul class="buttonBar"> '
                            +'<li><a href="#" id="btnBuskAlumA" tabindex="16"><label class="icon-lupa"></label> Buscar</a></li> '
                            +'<li><a href="#" id="btnBusFolio" tabindex="17">Buscar folio...</a></li>'
                        + '</ul>'
                    );
            $("#pnlAjustar").append('<div id="pnlCurpsYNombre"></div>');
                $("#pnlCurpsYNombre").append(
                          '<select id="cbxCurps" name="cbxCurps"  tabindex="18"></select>  '
                        + '<input type="text" id="txtNombreCompleto" maxlength="18" tabindex="15"/>'
                        + '<div id="pnlEstatusAlu"><label id="lblEstatusAlu">-</label><label>estatus</label></div>'
                    );
            $("#pnlAjustar").append('<div id="pnlAlumgrado" class="panel">  <label class="tituloPanel">Historial académico</label>  </div>');
                $("#pnlAlumgrado").append('<div id="pnlCamestatYEliminar"></div>');
                    $("#pnlCamestatYEliminar").append(
                            //  '<label>Historial Académico:</label>'
                              '<ul class="buttonBar"> '
                                +'<li><a href="#" id="btnCambSituacion" tabindex="16">Cambio de Estatus a Baja</a></li>   '
                                +'<li><a href="#" id="btnEliminaAlu" tabindex="17">Eliminar Alumno</a></li>'
                            + '</ul>'
                        );
                $("#pnlAlumgrado").append('<div id="pnlTblAlumgrado">  <div id="scrlAlumgrado" class="scrollTable"></div>  </div>');
                
            $("#pnlAjustar").append('<div id="pnlMaterias" class="panel"> <label class="tituloPanel">Materias</label> </div>');
                $("#pnlMaterias").append(
                          '<ul class="buttonBar"> '
                            +'<li><a href="#" id="btnCambEsc" tabindex="16">Cambio de escuela</a></li>   '
                            +'<li><a href="#" id="btnRecursarEnOtraEsc" tabindex="17">Cursará el grado</a></li>   '
                            +'<li><a href="#" id="btnAprobo" tabindex="17">Aprobó en el</a></li>   '
                            +'<li><a href="#" id="btnReprobar" tabindex="17">Reprobó en el</a></li>   '
                            +'<li><a href="#" id="btnImpHist" tabindex="17">Imprimir Historial</a></li>'
                        + '</ul>'
                        + '<div id="pnlTblMaterias">   <div id="scrlMaterias" class="scrollTable"></div>   </div>'
                    );
            $("#pnlAjustar").append('<div id="pnlCertifYComentarios"></div>');
                $("#pnlCertifYComentarios").append('<div id="pnlCertificados" class="panel"><label class="tituloPanel">Certificados</label></div>');
                    $("#pnlCertificados").append(
                            '<div id="pnlCertimp">'
                                //+'<label>Certificado:</label>'
                                + '<ul class="buttonBar"> '
                                    +'<li><a href="#" id="btnCancelaFolio" tabindex="16">Cancelar folio</a></li>   '
                                    +'<li><a href="#" id="btnVerSusRodacs" tabindex="16">Ver sus RODAC</a></li>   '
                                + '</ul>'
                            + '</div>'
                            + '<div id="pnlTblCertificados">   <div id="scrlCertificados" class="scrollTable"></div>   </div>'
                        );
                $("#pnlCertifYComentarios").append('<div id="pnlComentarios" class="panel"><label class="tituloPanel">Comentarios</label></div>');
                    $("#pnlComentarios").append(
                                  '<ul class="buttonBar"> '
                                    +'<li><a href="#" id="btnComenta" tabindex="16">Comentarios</a></li>'
                                + '</ul>'
                            + '<div id="pnlTblComentarios">   <div id="scrlComentarios" class="scrollTable"></div>   </div>'
                        );
        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar_Ajustar").on("click",function(){ frmwAjustar_Close(); return false; });
    
    $("#rbnCurp").on("click", function(){ rbnCurp_Click (); });
    $("#rbnIdaluX").on("click", function(){ rbnIdaluX_Click (); });
    $("#lblCurpA").on("click", function(){ lblCurpA_Click (); });
    $("#lblIdaluA").on("click", function(){ lblIdaluA_Click (); });
    $("#txtCurpA").on("focus", function(){ txtCurpA_Focus (); });
    $("#txtIdaluA").on("focus", function(){ txtIdaluA_Focus (); });
    $("#txtCurpA").on("keydown", function(e){ txtCurpA_KeyDown (e); });
    $("#txtIdaluA").on("keydown", function(e){ txtIdaluA_KeyDown (e); });
    $("#btnBuskAlumA").on("keydown", function(e){ if(e.which === 13){  btnBuskAlumA_ActionPerformed (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnBuskAlumA').on("click",function(){ btnBuskAlumA_ActionPerformed (); });
    $("#cbxCurps").on("change", function(e){ cbxCurps_ChangeSelectedItem (); });
}

function frmwAjustar_FormActivate (kienLlamas, idaluX, CURPd10)
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    if (sisVars.modulos.indexOf(",1,")>0)
        jsAjustar.QSusEstud_Filtered = false;
    else
        jsAjustar.QSusEstud_Filtered = true;
    
    if (!(sisVars.modulos.indexOf(",7,")>0))
        boton_setVisible (false, "btnImpHist");
    
    if (kienLlamas ==='NEWINGRESO' ){ // creo k ya nadie entra en este IF
        $('#txtIdaluA').val(idaluX);
        document.getElementById('rbnIdaluX').checked = true;
         btnBuskAlumA_ActionPerformed ();
    }else if (kienLlamas ==='NEWINGRESOca'){ //se kiso insertar en ciclo anterior
        $('#txtCurpA').val(CURPd10);
        document.getElementById('rbnCurp').checked = true;
         btnBuskAlumA_ActionPerformed ();
    }else if ( kienLlamas ==="NEWINGRESOpre" ||  //viene de nuevo ingreso de preescolar
        kienLlamas === "NEWINGRESOpri" ||  //viene de nuevo ingreso de primaria
        kienLlamas ==="NEWINGRESOsec") { //viene de nuevo ingreso de secundari l buscamos x los 10 primeros digitos de la CURP
        
        $('#txtCurpA').val(CURPd10);
        document.getElementById('rbnCurp').checked = true;
         btnBuskAlumA_ActionPerformed ();
        //grid_AlumGrado.SetFocus;
        tabla.setSelectedRow("tblAlumgrado",tabla.getNumRows("tblAlumgrado")-1,false);
    }else if (kienLlamas === "CERT_A_MAKINA" ){ //en prrimaria grado >1 lo buscamos x los 10 primeros digitos de la CURP
        $('#txtCurpA').val(CURPd10);
        document.getElementById('rbnCurp').checked = true;
         btnBuskAlumA_ActionPerformed ();
    }else
        insertarTablas_AlumMatsCertiYComen (null, null, null, null, null);      //limpiamos todas las tablas
    
    if ( sisVars.tipo_usuario!==" " && sisVars.tipo_usuario!=="consulta" && sisVars.tipo_usuario!=="mesa" ) // cuando es un usuario tipo CCT
    {
        boton_setVisible(false,"btnEliminaAlu");
        boton_setVisible(false,"btnCancelaFolio");
        boton_setVisible(false,"btnCambSituacion");
        boton_setVisible(false,"btnComenta");
        boton_setVisible(false,"btnReprobar");
        boton_setVisible(false,"btnAprobo");
        //--Imp--> Grid_alumGRado.Columns[5].PopupMenu :=  nil;
        //--Imp--> Grid_alumGRado.Columns[10].PopupMenu :=  nil;

        boton_setVisible(false,"btnBusFolio");
        object_setEnabled(false,'txtCurpA');
        object_setEnabled(false,'txtIdaluA');
        boton_setVisible(true,"btnCambEsc");
        boton_setVisible(true,"btnRecursarEnOtraEsc");
    } else {
        if ( sisVars.tipo_usuario==="consulta" || sisVars.tipo_usuario === "mesa" )
        {
            boton_setVisible(false,"btnEliminaAlu");
            boton_setVisible(false,"btnCancelaFolio");
            boton_setVisible(false,"btnCambSituacion");
            boton_setVisible(false,"btnComenta");
            boton_setVisible(false,"btnReprobar");
            boton_setVisible(false,"btnAprobo");
            //--Imp--> Grid_alumGRado.Columns[5].PopupMenu :=  nil;
            //--Imp--> Grid_alumGRado.Columns[10].PopupMenu :=  nil;

            boton_setVisible(false,"btnBusFolio");
            object_setEnabled(true,"txtCurpA");
            object_setEnabled(true,"txtIdaluA");
            boton_setVisible(false,"btnCambEsc");
            boton_setVisible(false,"btnRecursarEnOtraEsc");
        } 
        else {
            boton_setVisible(true,"btnEliminaAlu");
            boton_setVisible(true,"btnCancelaFolio");
            boton_setVisible(true,"btnCambSituacion");
            boton_setVisible(true,"btnComenta");
            //  btnReprobar.Visible     := true;
            //  btnAprobo.Visible       := true;
            //--Imp--> Grid_alumGRado.Columns[5].PopupMenu  := MenuElimina ;
            //--Imp--> Grid_alumGRado.Columns[10].PopupMenu :=  ModifEstatus;

            boton_setVisible(true,"btnBusFolio");
            object_setEnabled(true,"txtCurpA");
            object_setEnabled(true,"txtIdaluA");
            boton_setVisible(true,"btnCambEsc");
            boton_setVisible(true,"btnRecursarEnOtraEsc");
        }
    }
    
    $("#btnReprobar").text("Reprobó en el "+(parseInt(sisVars.cicescin)-1));
    $("#btnAprobo").text("Aprobó en el "+(parseInt(sisVars.cicescin)-1));
    
    jsAjustar.QSuFolio_Filtered = true;
    
    lblCurpA_Click ();
}

function ocultarBotonesNoImplementados ()
{
    boton_setVisible(false,"btnBusFolio");
    boton_setVisible(false,"btnCambSituacion");
    boton_setVisible(false,"btnEliminaAlu");
    boton_setVisible(false,"btnCambEsc");
    boton_setVisible(false,"btnRecursarEnOtraEsc");
    boton_setVisible(false,"btnAprobo");
    boton_setVisible(false,"btnReprobar");
    boton_setVisible(false,"btnImpHist");
    boton_setVisible(false,"btnCancelaFolio");
    boton_setVisible(false,"btnVerSusRodacs");
    boton_setVisible(false,"btnComenta");
    
    object_setVisible (false,"pnlCamestatYEliminar");
    object_setVisible (false,"pnlMaterias ul");
    object_setVisible (false,"pnlCertimp");
    object_setVisible (false,"pnlComentarios ul");
}

function rbnCurp_Click ()
{
    document.getElementById('txtCurpA').focus();
}

function rbnIdaluX_Click ()
{
    document.getElementById('txtIdaluA').focus();
}

function lblCurpA_Click ()
{
    document.getElementById('txtCurpA').focus();
    document.getElementById('rbnCurp').checked = true;
}

function lblIdaluA_Click ()
{
    document.getElementById('txtIdaluA').focus();
    document.getElementById('rbnIdaluX').checked = true;
}

function txtCurpA_Focus ()
{
    document.getElementById('txtCurpA').focus();
    document.getElementById('rbnCurp').checked = true;
}

function txtIdaluA_Focus ()
{
    lblIdaluA_Click ();
}
    
function txtCurpA_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
         btnBuskAlumA_ActionPerformed ();
}

function txtIdaluA_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
         btnBuskAlumA_ActionPerformed ();
}

function insertarTablas_AlumMatsCertiYComen (tblAlumgrado, tblAlumgrado_MouseClick, tblMaterias, tblCertificados, tblComentarios)
{
    var tabla = new Tabla();
    tabla.create("scrlAlumgrado","tblAlumgrado",tblAlumgrado, ["idcct","zona","cct","tno","unidad","ciclo","Gdo","Gpo","Acred","Prom","PG","PEB","Est","Pmv","MRAn","MRac","cveprog"], ["idcct","cvezona","cct","cveturno","cveunidad","inifin","grado","grupo","tipoacred","promedio","promediogral","promedioeb","estatusgrado","promovido","matrepant","matrepact","cveprograma"], null, null, true, null, tblAlumgrado_MouseClick, null);
    insertarTabla_Materias (tblMaterias);
    tabla.create("scrlCertificados","tblCertificados",tblCertificados, ["cveunidad","Libro","Letra","folio","estatus","cct","grupo","curp","nom_tot","usuario","fecha","hora"], ["cveunidad","inifin","folioletra","folionum","estatus","cct","grupo","curp","nom_tot","usuario","fechaf","hora"], null, null, true, null, null, null);
    tabla.create("scrlComentarios","tblComentarios",tblComentarios, ["Comentario","usuario"], ["comentario","usuario"], null, null, true, null, null, null);
    
    tblCertificados_ApplyCellAttribute();
}

function tblCertificados_ApplyCellAttribute()
{
    var tabla = new Tabla ();
    var ini, fin;
    var tblCertificados = tabla.getTable("tblCertificados",["cveunidad","inifin","folioletra","folionum","estatus","cct","grupo","curp","nom_tot","usuario","fechaf","hora"],null,"JSON");
    
    ini = 0;
    fin = tblCertificados.length;
    for (var i=ini; i<fin; i++)
    {
        if(tblCertificados[i].estatus==="I") {
            $(".tblCertificados_estatus_f"+i).css("font-weight","bold");
            $(".tblCertificados_estatus_f"+i).css("background-color", "red");
            $(".tblCertificados_estatus_f"+i).css("color", "white"); 
            $(".tblCertificados_estatus_f"+i).css("text-align", "center"); 
        }
    }
}

function insertarTabla_Materias (tblMaterias)
{
    var tabla = new Tabla();
    tabla.create("scrlMaterias","tblMaterias",tblMaterias, ["cveprog","TipM","CveM","Materia","promedio","usuario","fecha","hora"], ["cveprograma","cvetipmat","cvemat","desmat","promedio","usuario","fecha","hora"], null, null, true, null, null, null);
}


function btnBuskAlumA_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Aj", metodo:"btBuAl_Cl", rbnCurp_isChecked:document.getElementById('rbnCurp').checked, rbnIdaluX_isChecked:document.getElementById('rbnIdaluX').checked, 
        txtCurp:$("#txtCurpA").val().trim().toUpperCase(), txtIdalu:$("#txtIdaluA").val().trim().toUpperCase()
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
                    boton_setEnabled (result.btnCambSituacion_Enabled, 'btnCambSituacion');
                    $("#btnCambSituacion").text(result.btnCambSituacion_Text);
                    $('#txtCurpA').val(result.txtCurp_text);
                    $('#txtIdaluA').val(result.txtIdalu_Text);
                    $('#txtNombreCompleto').val(result.txtNombreCompleto);
                    $('#lblEstatusAlu').text(result.lblEstatusAlu);
                    
                     
                    if (typeof result.mensaje!=="undefined")
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    
                    /* Si ya jala y no da problemas eliminar el código comentado de crear tablas (15Mar17)
                     * tabla.create("scrlAlumgrado","tblAlumgrado",result.tblAlumgrado, ["idcct","zona","cct","tno","unidad","ciclo","Gdo","Gpo","Prom","PG","PEB","Est","Pmv","MRAn","MRac","cveprog"], ["idcct","cvezona","cct","cveturno","cveunidad","inifin","grado","grupo","promedio","promediogral","promedioeb","estatusgrado","promovido","matrepant","matrepact","cveprograma"], null, null, true, null, tblAlumgrado_MouseClicked, null);
                    tabla.create("scrlMaterias","tblMaterias",result.tblMaterias, ["cveprog","TipM","CveM","Materia","promedio","usuario","fecha","hora"], ["cveprograma","cvetipmat","cvemat","desmat","promedio","usuario","fecha","hora"], null, null, true, null, null, null);
                    tabla.create("scrlCertificados","tblCertificados",result.tblCertificados, ["cveunidad","Libro","Letra","folio","folioRodac","cct","grupo","curp","nom_tot","usuario","fecha","hora"], ["cveunidad","inifin","folioletra","folionum","foliorodac","cct","grupo","curp","nom_tot","usuario","fechaf","hora"], null, null, true, null, null, null);
                    tabla.create("scrlComentarios","tblComentarios",result.tblComentarios, ["Comentario","usuario"], ["comentario","usuario"], null, null, true, null, null, null);*/
                    insertarTablas_AlumMatsCertiYComen (result.tblAlumgrado, tblAlumgrado_MouseClicked, result.tblMaterias, result.tblCertificados, result.tblComentarios);
                    tabla.setSelectedRow("tblAlumgrado",tabla.getNumRows("tblAlumgrado")-1,false);
                    
                    // Cargamos el combo de curps
                    //$("#cbxEntidad").append("<option value=''  selected></option>");
                    $("#cbxCurps").empty();
                    $.each(result.cbxCurps,function(i,valor) {
                        $("#cbxCurps").append("<option value='"+valor["idalu"]+"'>"+valor["curp"]+"</option>");                        
                    });
                    
                    cerrarLoading();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
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

function cbxCurps_ChangeSelectedItem ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Aj", metodo:"cbCu_ChSeIt", idalu:$("#cbxCurps").val()
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
                    boton_setEnabled (result.btnCambSituacion_Enabled, 'btnCambSituacion');
                    $("#btnCambSituacion").text(result.btnCambSituacion_Text);
                    if (typeof result.btnCambSituacion_FontColor !== "undefined")
                        $("#btnCambSituacion").css("color",result.btnCambSituacion_FontColor);
                    $('#txtCurpA').val(result.txtCurp_text);
                    $('#txtIdaluA').val(result.txtIdalu_Text);
                    $('#txtNombreCompleto').val(result.txtNombreCompleto);
                    $('#lblEstatusAlu').text(result.lblEstatusAlu);
                     
                    if (typeof result.mensaje!=="undefined")
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    
                    /* Si ya jala y no da problemas eliminar el código comentado de crear tablas (15Mar17)
                     * tabla.create("scrlAlumgrado","tblAlumgrado",result.tblAlumgrado, ["idcct","zona","cct","tno","unidad","ciclo","Gdo","Gpo","Prom","PG","PEB","Est","Pmv","MRAn","MRac","cveprog"], ["idcct","cvezona","cct","cveturno","cveunidad","inifin","grado","grupo","promedio","promediogral","promedioeb","estatusgrado","promovido","matrepant","matrepact","cveprograma"], null, null, true, null, tblAlumgrado_MouseClicked, null);
                    tabla.create("scrlMaterias","tblMaterias",result.tblMaterias, ["cveprog","TipM","CveM","Materia","promedio","usuario","fecha","hora"], ["cveprograma","cvetipmat","cvemat","desmat","promedio","usuario","fecha","hora"], null, null, true, null, null, null);
                    tabla.create("scrlCertificados","tblCertificados",result.tblCertificados, ["cveunidad","Libro","Letra","folio","folioRodac","cct","grupo","curp","nom_tot","usuario","fecha","hora"], ["cveunidad","inifin","folioletra","folionum","foliorodac","cct","grupo","curp","nom_tot","usuario","fechaf","hora"], null, null, true, null, null, null);
                    tabla.create("scrlComentarios","tblComentarios",result.tblComentarios, ["Comentario","usuario"], ["comentario","usuario"], null, null, true, null, null, null);
                    */insertarTablas_AlumMatsCertiYComen (result.tblAlumgrado, tblAlumgrado_MouseClicked, result.tblMaterias, result.tblCertificados, result.tblComentarios);
                    tabla.setSelectedRow("tblAlumgrado",tabla.getNumRows("tblAlumgrado")-1,false);
                    cerrarLoading();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
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

function tblAlumgrado_MouseClicked ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var dataRow = tabla.getSelectedRow("tblAlumgrado",["idalu","cicescini"],null,"JSON");
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Aj", metodo:"tbAl_MoCl", tblAlumgrado_cicescini:dataRow.cicescini, tblAlumgrado_idalu:dataRow.idalu
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
                    // Si ya jala y no da problemas eliminar el código comentado de crear tablas (15Mar17)
                    //tabla.create("scrlMaterias","tblMaterias",result.tblMaterias, ["cveprog","TipM","CveM","Materia","promedio","usuario","fecha","hora"], ["cveprograma","cvetipmat","cvemat","desmat","promedio","usuario","fecha","hora"], null, null, true, null, null, null);
                    insertarTabla_Materias (result.tblMaterias);
                    cerrarLoading();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
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