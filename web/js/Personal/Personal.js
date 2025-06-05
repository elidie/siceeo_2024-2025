/* 
    Creado el : 5/06/2017, 05:53:11 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/


var jsPersonal;

function frmwPersonal_Show(tblPrincipal_selRow)
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    jsPersonal = {
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,        
        tblPrincipal_cambioDir: sisVars.cambioDir,        
        hayCambios:false
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwPersonal").css("display", "block");                                 // Mostramos el formulario correspondiente
    
    frmwPersonal_Create ();
    frmwPersonal_FormActivate ();
}

function frmwPersonal_Close()
{
    irAVentanaPrincipal();
}

function frmwPersonal_Create()
{
    var tabindexIni = 50, tabindexReturn=35;
    
    if($('#frmfPersonal').length)
        $('#frmfPersonal').remove();
    $('#frmwPersonal').append('<fieldset id="frmfPersonal"><legend>Personal directivo y docente</legend><div id="btnRegresar_Personal" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfPersonal").append('<div id="pnlPersonal"></div>');
        
            $('#pnlPersonal').append('<div id="pnlDatosEscuelaYdirector"></div>');
                $('#pnlDatosEscuelaYdirector').append('<div id="pnlDatosEscuela" class="panel alinearHoriz"><div class="tituloPanel">Escuela</div>'
                                                        +'<div><label class="lblTituloCampos">CCT:</label><label id="lblCct"></label></div>'
                                                        +'<div><label class="lblTituloCampos">Director:</label><label id="lblDirector"></label></div>'
                                                        +'<div><label class="lblTituloCampos">Teléfono:</label><label id="lblTelefono"></label></div>'
                                                        +'<div><label class="lblTituloCampos">Correo electrónico:</label><label id="lblEmail"></label></div>'
                                                        +'<div><label class="lblTituloCampos">Domicilio:</label><label id="lblDomicilio"></label></div>'
                                                        +'<div><label class="lblTituloCampos">Colonia:</label><label id="lblColonia"></label></div>'                                                           
                                                    +'</div>');
            /*if((""+jsPersonal.tblPrincipal_modalidad).substr(0,1)!=='P' && ""+jsPersonal.tblPrincipal_cambioDir==="1"){
                $('#pnlDatosEscuelaYdirector').append('<div id="pnlDatosDirector" class="panel alinearHoriz"><div class="tituloPanel">Director</div>'
                                                        +'<div>'
                                                            //+'<label class="lblTituloCampos">CURP:</label>'
                                                            //+'<input type="text" id="txtCurpDir" class="" maxlength="18">'
                                                            //+'<div id="btnBuscarRFCDir" class="btnBuscarRFC singleButton"><label><span class="iconButton icon-lupa"></span>Buscar</label></div>'
                                                        +'</div>'
                                                        +'<div><label class="lblTituloCampos">Nombre(s):</label><input type="text" id="txtNombreDir" maxlength="40" required></div>'
                                                        +'<div><label class="lblTituloCampos">*Primer apellido:</label><input type="text" id="txtPrimerApeDir" maxlength="30" required></div>'
                                                        +'<div><label class="lblTituloCampos">Segundo apellido:</label><input type="text" id="txtSegundoApeDir" maxlength="30"></div>'
                                                        //+'<div><label class="lblTituloCampos">Género:</label><select id="cbxGeneroDir" name="cbxGeneroDir"><option value=""></option><option value="M">MUJER</option><option value="H">HOMBRE</option></select></div>'
                                                        +'<ul class="buttonBar">'
                                                            +'<li><a href="#" id="btnGuardarDir"><label class="icon-disquete"></label> Guardar </a></li>'
                                                            +'<li><a href="#" id="btnLimpiarDatosDir"><label class="icon-brocha"></label> Limpiar</a></li>'
                                                        +'</ul>'
                                                    +'</div>');
            }*/
            
                $('#pnlDatosEscuelaYdirector').append('<div id="pnlMensajeInfo" class="alinearHoriz">');
                                    //$('#pnlMensajeInfo').append('<label id="pnlMensajeInfo_lbl1">Es importante que los datos del personal los ingrese correctamente (sin abreviaturas).<br></label>');
                                    //if((""+jsPersonal.tblPrincipal_modalidad).substr(0,1)==='P'){
                                        $('#pnlMensajeInfo').append('<br><label id="pnlMensajeInfo_lbl1">El nombre del director(a) debe estar registrado en el catálogo de centros de trabajo.'
                                                    +' Para actualizar del nombre del director(a) debe comunicarse con el Departamento de Estadística al correo electrónico estadisticaeducativa@ieepo.gob.mx y/o al número teléfono 51 5 09 36.'
                                                    +'<br><br>Las escuelas públicas deben anexar su orden de presentación oficial emitido por el nivel educativo del IEEPO.</label>');                                                                                          
                                    //}
                $('#pnlDatosEscuelaYdirector').append('</div>');
                                                                                                                                                                           
            /*if ( jsPersonal.tblPrincipal_cveplan !== "2" )
            {*/
                $('#pnlPersonal').append('<div id="pnlProfesores" class="panel">'
                        +'<div id="pnlTituloProfesores" class="tituloPanel">'+ (jsPersonal.tblPrincipal_cveplan === "2" ? 'Asignación de docente/tutor(a) de grupo' : 'Asignación de profesor a cada grupo' )+'</div></div>');
                    $('#pnlProfesores').append('<div id="pnlListadoGdosGpos"><div id="scrlGdosGposPersonal" class="scrollTable"></div></div>');
                    $('#pnlProfesores').append('<div id="pnlDatosProfesor" class="panel"><div class="tituloPanel">'
                        + (jsPersonal.tblPrincipal_cveplan === "2" ? 'Docente/Tutor(a) del ': 'Profesor del ') + '<label id="lblProfGdoGpo"></label></div>'
                                    //+'<div>'
                                        //+'<label class="lblTituloCampos">CURP:</label>'
                                        //+'<input type="text" id="txtCurpProf" class="" maxlength="18">'
                                        //+'<div id="btnBuscarRFCProf" class="btnBuscarRFC singleButton"><label><span class="iconButton icon-lupa"></span>Buscar</label></div>'
                                    //+'</div>'
                                    +'<div><label class="lblTituloCampos">Nombre(s):</label><input type="text" id="txtNombreProf" maxlength="40" class="txtProf" required></div>'
                                    +'<div><label class="lblTituloCampos">*Primer apellido:</label><input type="text" id="txtPrimerApeProf" maxlength="30" class="txtProf" required></div>'
                                    +'<div><label class="lblTituloCampos">Segundo apellido:</label><input type="text" id="txtSegundoApeProf" maxlength="30" class="txtProf"></div>'
                                    +'<ul class="buttonBar">'
                                            +'<li><a href="#" id="btnGuardarProf"><label class="icon-disquete"></label> Guardar cambios</a></li>'
                                            +'<li><a href="#" id="btnLimpiarDatosProf"><label class="icon-brocha"></label> Limpiar</a></li>'
                                        +'</ul>'
                                +'</div>');
            /*}*/
                            
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnRegresar_Personal").on("click",function(){ frmwPersonal_Close(); });
    $("#txtCurpDir").bind("keydown", function(e){ if(e.which === 13) {btnBuscarRFCDir_Click (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $("#btnBuscarRFCDir").on("click",function(){ btnBuscarRFCDir_Click(); });
    $("#btnGuardarDir").on("click",function(){ btnGuardarDatosDirector_Click(); });
    $("#btnLimpiarDatosDir").on("click",function(){ btnLimpiarDatos_Click("DIRECTOR"); });
    $("#txtCurpProf").bind("keydown", function(e){ if(e.which === 13) {btnBuscarRFCProf_Click (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $("#btnBuscarRFCProf").on("click",function(){ btnBuscarRFCProf_Click(); });
    $("#btnGuardarProf").on("click",function(){ btnGuardarDatosProfesor_Click(); });
    $("#btnLimpiarDatosProf").on("click",function(){ btnLimpiarDatos_Click("PROFESOR"); });
}

function frmwPersonal_FormActivate ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pers", metodo:"foAc", califCicEscIn:sisVars.cicescin, tblPrincipal_cveplan:jsPersonal.tblPrincipal_cveplan, tblPrincipal_idcct:jsPersonal.tblPrincipal_idcct,
        tblPrincipal_grado:jsPersonal.tblPrincipal_grado, tblPrincipal_grupo:jsPersonal.tblPrincipal_grupo
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
                    jsPersonal.califCicEscIn=datos.califCicEscIn;
                    jsPersonal.tblPrincipal_grado = datos.tblPrincipal_grado;
                    jsPersonal.tblPrincipal_grupo = datos.tblPrincipal_grupo;
                    jsPersonal.tblPrincipal_grupo = datos.tblPrincipal_grupo;
                    
                    //--------------- Asignamos datos a componentes ----------------
                    
                    tabla.create("scrlGdosGposPersonal","tblGdosGposPersonal",result.tblGdosGposPersonal, ["idcct","turno","grado","grupo"], ["idcct","cveturno","grado","grupo"], null, null, false, null, function(index){
                        tblGdosGposPersonal_ChangeSelectedItem (index);
                    }, null);
                    
                    //Buscamos el grado grupo que seleccionó desde la ventana principal
                    var i;
                    for (i=0; i<result.tblGdosGposPersonal.length; i++)
                        if (result.tblGdosGposPersonal[i].grado===jsPersonal.tblPrincipal_grado && result.tblGdosGposPersonal[i].grupo===jsPersonal.tblPrincipal_grupo)
                            break;
                    tabla.setSelectedRow ('tblGdosGposPersonal', i);
                    
                    insertDatosEscuela(result);
                    insertDatosDirector(result);
                    insertDatosProfesor(result);
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

function insertDatosEscuela (result)
{
    $('#lblCct').text(result.escuela.cct);
    $('#lblDirector').text(result.escuela.director);
    $('#lblTelefono').text(result.escuela.telefono);
    $('#lblEmail').text(result.escuela.email);
    $('#lblDomicilio').text(result.escuela.domicilio);
    $('#lblColonia').text(result.escuela.colonia);
}

function insertDatosDirector (result)
{
    $('#txtNombreDir').val(result.director.nombre);
    $('#txtPrimerApeDir').val(result.director.apepat);
    $('#txtSegundoApeDir').val(result.director.apemat);
    //$('#txtCurpDir').val(result.director.rfc);
    //$("#cbxGeneroDir").val(result.director.sexo);
}

function insertDatosProfesor(result)
{
    $('#txtNombreProf').val(result.profesor.nombre);
    $('#txtPrimerApeProf').val(result.profesor.apepat);
    $('#txtSegundoApeProf').val(result.profesor.apemat);
    //$("#txtCurpProf").val(result.profesor.rfc);    
    $("#lblProfGdoGpo").text("Grado: "+jsPersonal.tblPrincipal_grado+" y Grupo: "+jsPersonal.tblPrincipal_grupo);    
}

function tblGdosGposPersonal_ChangeSelectedItem (index)
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    var tblGdosGposPersonal_selRow = tabla.getRow ("tblGdosGposPersonal", index, ["idcct","grado","grupo"], null, "JSON");
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pers", metodo:"tbPe_ChSeIt", califCicEscIn:jsPersonal.califCicEscIn, tblPrincipal_cveplan:jsPersonal.tblPrincipal_cveplan, 
        tblPrincipal_idcct:tblGdosGposPersonal_selRow.idcct, tblPrincipal_grado:tblGdosGposPersonal_selRow.grado, tblPrincipal_grupo:tblGdosGposPersonal_selRow.grupo
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
                    //------ Establecemos la selección de la fila en la tabla ------
                    tabla.setSelectedRow ('tblGdosGposPersonal', index);
                    jsPersonal.tblPrincipal_grado = datos.tblPrincipal_grado;
                    jsPersonal.tblPrincipal_grupo = datos.tblPrincipal_grupo;
                    
                    //-------------------- Asignamos variables ---------------------
                    
                    //--------------- Asignamos datos a componentes ----------------
                    insertDatosProfesor(result);
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

function btnBuscarRFCDir_Click ()
{
    var mensaje = new Mensajes();
    
    if ( $("#txtCurpDir").val().trim() === "")
        mensaje.General("ESPECIFIQUE_DATO","RFC");
    else {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Pers", metodo:"btBuRfDi_Cl", txtRfcDir:$('#txtCurpDir').val()
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
                        //------ Establecemos la selección de la fila en la tabla ------

                        //-------------------- Asignamos variables ---------------------

                        //--------------- Asignamos datos a componentes ----------------
                        if (result.encontrado === true){
                            $('#txtNombreDir').val(result.nombre);
                            $('#txtPrimerApeDir').val(result.apepat);
                            $('#txtSegundoApeDir').val(result.apemat);
                            //$('#cbxGeneroDir').val(result.sexo);
                        }else
                            mensaje.General("INFO_NO_ENCONTRADA");
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
}

function btnBuscarRFCProf_Click ()
{
    var mensaje = new Mensajes();
    
    if ( $("#txtCurpProf").val().trim() === "")
        mensaje.General("ESPECIFIQUE_DATO","RFC");
    else {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Pers", metodo:"btBuRfPr_Cl", txtRfcProf:$('#txtCurpProf').val()
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
                        //------ Establecemos la selección de la fila en la tabla ------

                        //-------------------- Asignamos variables ---------------------

                        //--------------- Asignamos datos a componentes ----------------
                        if (result.encontrado === true){
                            $('#txtNombreProf').val(result.nombre);
                            $('#txtPrimerApeProf').val(result.apepat);
                            $('#txtSegundoApeProf').val(result.apemat);
                        }else
                            mensaje.General("INFO_NO_ENCONTRADA");
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
}

function btnGuardarDatosDirector_Click ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblGdosGposPersonal") === -1)
        mensaje.General("NO_SELEC"," grado-grupo","guardar sus datos");
    else if (validarEntradas_Personal("DIRECTOR")){
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Pers", metodo:"btGuDaDi_Cl", califCicEscIn:jsPersonal.tblPrincipal_cicescini,tblPrincipal_idcct:jsPersonal.tblPrincipal_idcct, tblPrincipal_cveturno:jsPersonal.tblPrincipal_cveturno, 
            tblPrincipal_cveplan:jsPersonal.tblPrincipal_cveplan, txtNombreDir:$('#txtNombreDir').val(), txtPrimerApeDir:$('#txtPrimerApeDir').val(), 
            txtSegundoApeDir:$('#txtSegundoApeDir').val(), cbxGeneroDir:$('#cbxGeneroDir').val()
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
                        mensaje.General("GUARDADO_EXITOSO");
                        //------ Establecemos la selección de la fila en la tabla ------

                        //-------------------- Asignamos variables ---------------------

                        //--------------- Asignamos datos a componentes ----------------
                        //insertDatosEscuela(result);
                        //insertDatosDirector(result);
                        //insertDatosProfesor(result);
                        
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
}

function btnLimpiarDatos_Click (caso)
{
    if (caso === "DIRECTOR")
    {
        //$('#txtCurpDir').val("");
        $('#txtNombreDir').val("");
        $('#txtPrimerApeDir').val("");
        $('#txtSegundoApeDir').val("");
        $('#cbxGeneroDir').val("");
    }else if (caso === "PROFESOR")
    {
        //$('#txtCurpProf').val("");
        $('#txtNombreProf').val("");
        $('#txtPrimerApeProf').val("");
        $('#txtSegundoApeProf').val("");
    }
}

function btnGuardarDatosProfesor_Click ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblGdosGposPersonal") === -1)
        mensaje.General("NO_SELEC"," grado-grupo","guardar sus datos");
    else if (validarEntradas_Personal("PROFESOR")){
        var tblGdosGposPersonal_selRow = tabla.getSelectedRow ("tblGdosGposPersonal", ["idcct","grado","grupo","cveturno"])+"~"+jsPersonal.tblPrincipal_cveplan;
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Pers", metodo:"btGuDaPr_Cl", califCicEscIn:jsPersonal.califCicEscIn, tblGdosGposPersonal_selRow: tblGdosGposPersonal_selRow, 
            txtNombreProf:$('#txtNombreProf').val(), txtPrimerApeProf:$('#txtPrimerApeProf').val(), txtSegundoApeProf:$('#txtSegundoApeProf').val()
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
                        mensaje.General("GUARDADO_EXITOSO");
                        var tblGdosGposPersonal_SelecRow = tabla.getSelectedIndexRow ('tblGdosGposPersonal');
                        var tblGdosGposPersonal_size = tabla.getNumRows('tblGdosGposPersonal');
                        if(tblGdosGposPersonal_SelecRow < tblGdosGposPersonal_size-1)
                            tblGdosGposPersonal_ChangeSelectedItem(tblGdosGposPersonal_SelecRow+1);
                        else 
                            break;
                        //------ Establecemos la selección de la fila en la tabla ------
                        

                        //-------------------- Asignamos variables ---------------------

                        //--------------- Asignamos datos a componentes ----------------
                        
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
}

function validarEntradas_Personal (caso)
{   
    var mensaje = new Mensajes (); 
    var formatoCURP=new RegExp("^[A-Z,Ñ]{4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])(M|H)[A-Z,Ñ]{5}[A-Z,Ñ|0-9]{2}$");
    
    if (caso === "DIRECTOR")
    {
        if ($('#txtNombreDir').val().trim() === ""){
            mensaje.General ("ESPECIFIQUE_DATO","nombre del director");    $("#txtNombreDir").focus();  return false;
        }else if ($('#txtPrimerApeDir').val().trim() === ""){
            mensaje.General ("ESPECIFIQUE_DATO","primer apellido del director");    $("#txtPrimerApeDir").focus();  return false;
        }/*else if ($('#txtRfcDir').val().trim() === ""){
            mensaje.General ("ESPECIFIQUE_DATO","RFC del director");    $("#txtRfcDir").focus();  return false;
        }else if ($('#cbxGeneroDir').val().trim() === ""){
            mensaje.General ("ESPECIFIQUE_DATO","género del director");    $("#cbxGeneroDir").focus();  return false;
        }else if($('#txtCurpDir').val().trim().length > 0 && !formatoCURP.test($('#txtCurpDir').val().trim().toUpperCase())){
            mensaje.General ("FORMATO_INCORRECTO","CURP del director");    $("#txtCurpDir").focus();  return false;
        }*/
    } else if (caso === "PROFESOR")
    {        
        if ($('#txtNombreProf').val().trim() === ""){
            mensaje.General ("ESPECIFIQUE_DATO","nombre del profesor");    $("#txtNombreProf").focus();  return false;
        }else if ($('#txtPrimerApeProf').val().trim() === ""){
            mensaje.General ("ESPECIFIQUE_DATO","primer apellido del profesor");    $("#txtPrimerApeProf").focus();  return false;
        }/*else if ($('#txtCurpProf').val().trim() === ""){            
            mensaje.General ("ESPECIFIQUE_DATO","CURP del profesor");    $("#txtRfcProf").focus();  return false;
        }else if($('#txtCurpProf').val().trim().length > 0 && !formatoCURP.test($('#txtCurpProf').val().trim().toUpperCase())){
            mensaje.General ("FORMATO_INCORRECTO","CURP del profesor");    $("#txtCurpProf").focus();  return false;
        }*/
    }
    
    return true;
}