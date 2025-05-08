/* 
    Creado el : 19-may-2017, 18:12:40
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaptuTutorAlum;

function frmwTutor_Show(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo)
{
    jsCaptuTutorAlum = {
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_cct: tblPrincipal_cct,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo:tblPrincipal_grupo,
        
        cicescin:null,
        capRepOf: null,
        tblAlumCapTutor_idalu:null,
        califCicEscIn:null,
        hayCambios:false
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwCaptuTutorAlum").css("display", "block");                              // Mostramos el formulario correspondiente
    
    frmwCaptuTutorAlum_Create ();
    frmwCaptuTutorAlum_FormActivate ();
}

function frmwCaptuTutorAlum_Close()
{
    jsCaptuTutorAlum = null;
    irAVentanaPrincipal();
}

function frmwCaptuTutorAlum_Create()
{
    if($('#frmfCaptuTutorAlum').length)
        $('#frmfCaptuTutorAlum').remove();
    $('#frmwCaptuTutorAlum').append('<fieldset id="frmfCaptuTutorAlum"><legend>Datos del tutor</legend><div id="btnRegresar_CaptuRepEval" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCaptuTutorAlum").append('<div id="pnlCaptuTutorAlum"></div>');
        
            $('#pnlCaptuTutorAlum').append('<div id="pnlListadoAlumnos"></div>');
                $('#pnlListadoAlumnos').append('<div id="pnlDatosGenerales"></div>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapTutor_cveprograma"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapTutor_idalu"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapTutor_curp"> ... </label>');
                $('#pnlListadoAlumnos').append('<div id="pnlTblAlumCapTutor">  <div id="scrlAlumCapTutor" class="scrollTable"></div>  </div>');
                    $('#pnlTblAlumCapTutor').append('');
    //..................................................................................................................................................................
            $('#pnlCaptuTutorAlum').append('<div id="pnlCapturaDeTutores" class="panel">'+
                                            '<div class="panel"><div id="pnlScrlCapturaDeTutores"></div></div>' +
                                            '<div id="pnlBotonesDeGestion" ></div>'+
                                         '</div>');
                                 
                $('#pnlScrlCapturaDeTutores').append('<div id="pnlRecomendGrales" class="panel"><div class="tituloPanel">DATOS PERSONALES DEL TUTOR.</div></div>');
                    $('#pnlRecomendGrales').append('<div id="pnlRecomendGrales_instruc"><label>Se deberá capturar en su totalidad la CURP del tutor.</label></div>');

                    $('#pnlRecomendGrales').append('<table id="ftblAlumnoTutor"></table>');
                        $('#ftblAlumnoTutor').append("<tr><th>* CURP</th><td>"
                                                    +"<input type='text' id='txtCurp' class='alinearHoriz' maxlength='18' style='text-transform:uppercase;' tabindex='100' placeholder='18 dígitos' value=''/>"                                                
                                                +"</td></tr>"); 

                        $('#ftblAlumnoTutor').append("<tr><th>* Primer Apellido</th>           <td><input type='text' id='txtApe1' maxlength='50' style='text-transform:uppercase;' tabindex='101' value='' required/></td></tr>");
                        $('#ftblAlumnoTutor').append("<tr><th>Segundo Apellido</th>           <td><input type='text' id='txtApe2' maxlength='50' style='text-transform:uppercase;' tabindex='102' value=''/></td></tr>");
                        $('#ftblAlumnoTutor').append("<tr><th>* Nombre(s)</th>                  <td><input type='text' id='txtNombre' maxlength='100' tabindex='103' style='text-transform:uppercase;' value='' required/></td></tr>");                
                        $('#ftblAlumnoTutor').append("<tr><th>Teléfono</th>                  <td><input type='text' id='txtTelefono' size='10' maxlength='10' tabindex='104' style='text-transform:uppercase;' value='' /></td></tr>"); 
                        $('#ftblAlumnoTutor').append("<tr><th>* Parentesco</th>  <td><select id='cbxParent' tabindex='105'></select></td></tr>");
                    $('#pnlRecomendGrales').append('<br><div id="pnlRecomendGrales_instruc"><label>* Campos obligatorios.</label></div>');    
    
                $('#pnlBotonesDeGestion').append('<ul class="buttonBar">'+
                                                    '<li><a href="#" id="btnCargaTutorGpo"><label class="icon-insertar"></label>Cargar tutores ciclo anterior</a></li>'+
                                                    '<li><a href="#" id="btnGuardarCapTutor"><label class="middleHoriz icon-disquete"></label>Guardar captura</a></li>'+
                                                    '<li><a href="#" id="btnLimpiarCapRepEval"><label class="middleHoriz icon-brocha"></label>Limpiar captura</a></li>'+
                                                    //'<li><a href="#" id="btnAnteriorGrupo"><label class="iconBtnGpoAnt middleHoriz iconBtnRedondo icon-arrow-left4"></label>Gpo. anterior</a></li>' +
                                                    //'<li><a href="#" id="btnSiguienteGrupo">Siguiente gpo.<label class="iconBtnGpoSig middleHoriz iconBtnRedondo icon-arrow-right4"></label></a></li>' +
                                                '</ul>');
                
  
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------

    $("#btnRegresar_CaptuRepEval").on("click",function(){ frmwCaptuTutorAlum_Close(); });
    $("#btnGuardarCapTutor").on('click',function(){ btnGuardarCapTutor_Click ();  return false;});
    $("#btnLimpiarCapRepEval").on('click',function(){ limpiarDatosDeCapturaTutor (); });
    //$("#btnAnteriorGrupo").on('click',function(){ btnAnteriorGrupo_CapTutor_Click(); return false; });
    //$("#btnSiguienteGrupo").on('click',function(){ btnSiguienteGrupo_CapTutor_Click();  return false;});     
    $('#btnCargaTutorGpo').click(function(e){ btnCargaTutorGpo_ActionPerformed (e); });
}

function frmwCaptuTutorAlum_FormActivate ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"DaTut", metodo:"foAc", califCicEscIn:sisVars.cicescin, cicescin:sisVars.cicescin, tblPrincipal_idcct:jsCaptuTutorAlum.tblPrincipal_idcct,
        tblPrincipal_cveplan:jsCaptuTutorAlum.tblPrincipal_cveplan, tblPrincipal_grado:jsCaptuTutorAlum.tblPrincipal_grado, 
        tblPrincipal_grupo:jsCaptuTutorAlum.tblPrincipal_grupo
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
                    jsCaptuTutorAlum.cicescin = sisVars.cicescin;
                    jsCaptuTutorAlum.capRepOf=result.capRepOf;
                    jsCaptuTutorAlum.califCicEscIn=datos.califCicEscIn;
                    jsCaptuTutorAlum.tipoCambCic = result.tipoCambCic;
                    
                    //----------- Activamos o desactivamos componentes -------------
                    //if (jsCaptuTutorAlum.tblPrincipal_cveplan!=="2")
                        //object_setVisible(false,"pnlTutoria");
                    boton_setVisible(result.btnSiguienteCiclo_Visible,'btnSiguienteCiclo');
                    boton_setVisible(result.btnAnteriorCiclo_Visible,'btnAnteriorCiclo');
                    if (!result.btnAnteriorGpo_Enabled)     boton_setEnabled(result.btnAnteriorGpo_Enabled,'btnAnteriorGpo');
                    if (!result.btnSiguienteGpo_Enabled)    boton_setEnabled(result.btnSiguienteGpo_Enabled,'btnSiguienteGpo');
                    
                    //--------------- Asignamos datos a componentes ----------------
                    $("#lblCiclo").text(result.lblCiclo);
                    //Cargamos las lenguas
                    $("#cbxParent").append("<option value='' ></option>");
                    $.each(result.parentesco,function(clave,valor) {                        
                        /*if(clave===result.tutor.cveparent)
                            $("#cbxParent").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"' selected>"+valor.trim()+"</option>");                        */
                        $("#cbxParent").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"'>"+valor.trim()+"</option>");                        
                    });
                    
                    insertarTablaTblAlumCapTutor (result.tblAlumCapTutor);
                    insertarDatosDeCaptura_Tutor (result);
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



function btnCargaTutorGpo_ActionPerformed ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    /*jsNvoIngreso.alumEstatus = "X";*/
    var datos = {
            modulo:"DaTut", metodo:"caAluTut", usuario:sisVars.usuario, cveunidad:sisVars.unidad,             
            cicescini:jsCaptuTutorAlum.tblPrincipal_cicescini, idcct:jsCaptuTutorAlum.tblPrincipal_idcct, 
            cveplan:jsCaptuTutorAlum.tblPrincipal_cveplan, grado: jsCaptuTutorAlum.tblPrincipal_grado, 
            grupo:jsCaptuTutorAlum.tblPrincipal_grupo, cct:jsCaptuTutorAlum.tblPrincipal_cct
        };
                    
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
                    mensaje.General("PROCESO_EXITOSO", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    frmwCaptuTutorAlum_FormActivate();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
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

function insertarDatosDeCaptura_Tutor (result)
{

     //--------------- Asignamos datos a componentes ----------------
    var tblAlumCapTutorAlum={cveprograma:"", idalu:"", curp:""};
        
    if (result.tblAlumCapTutor.length>0){
    
        tblAlumCapTutorAlum.cveprograma = result.tblAlumCapTutor[0].cveprograma;
        tblAlumCapTutorAlum.idalu = result.tblAlumCapTutor[0].idalu;
        tblAlumCapTutorAlum.curp= result.tblAlumCapTutor[0].curp;
    }
    jsCaptuTutorAlum.tblAlumCapTutor_idalu = tblAlumCapTutorAlum.idalu;

    setDatosLabel_CapTutorAlum ("AlumCapTutor", tblAlumCapTutorAlum.cveprograma, tblAlumCapTutorAlum.idalu==="-1"?"":tblAlumCapTutorAlum.idalu, tblAlumCapTutorAlum.curp);
    
    //--------------------- Rellenamos tablas ----------------------
    insertDatosTutor(result);    
    $("#cbxParent").val(result.tutor.cveparent.trim());
    $("#pnlScrlCapturaDeTutores").scrollTop (0);    
}

function insertDatosTutor(result)
{        
    $('#txtCurp').val(result.tutor.curp);
    $('#txtApe1').val(result.tutor.apepat);
    $('#txtApe2').val(result.tutor.apemat);
    $("#txtNombre").val(result.tutor.nombre);    
    $("#txtTelefono").val(result.tutor.telefono);    //cbxParent            
}


function insertarTablaTblAlumCapTutor (tblAlumCapTutor)
{
    var tabla = new Tabla();
    //---------------------- Dibujamos la tabla tblAlumCapTutor ----------------------\\
    tabla.create("scrlAlumCapTutor","tblAlumCapTutor",tblAlumCapTutor, ["Nombre"], ["nom_tot"], null, null, false, null, function(index){
        tblAlumCapTutor_ChangeSelectedItem (index);
    }, null);
    
    tabla.setSelectedRow ('tblAlumCapTutor', 0);
}





function tblAlumCapTutor_ChangeSelectedItem (index)
{    
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    var tblAlumCapTutor_selRow = tabla.getRow ("tblAlumCapTutor", index, ['cveprograma','idalu','curp'], null, "JSON");
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"DaTut", metodo:"tblAlumCapTutor_ChSeIt", califCicEscIn:jsCaptuTutorAlum.califCicEscIn, tblPrincipal_idcct:jsCaptuTutorAlum.tblPrincipal_idcct, 
        tblPrincipal_cveplan:jsCaptuTutorAlum.tblPrincipal_cveplan, tblPrincipal_grado:jsCaptuTutorAlum.tblPrincipal_grado, 
        tblAlumCapTutor_idalu:tblAlumCapTutor_selRow.idalu
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
                    tabla.setSelectedRow ('tblAlumCapTutor', index);
                    
                    //-------------------- Asignamos variables ---------------------
                    /*for (var i=0; i<result.matsAlumno.length; i++){
                        jsCaptuTutorAlum.cbxMateriasAlumno[i]=result.matsAlumno[i].desmat;
                        jsCaptuTutorAlum.cveMats[i]=result.matsAlumno[i].cvemats;
                    }*/
                    //--------------- Asignamos datos a componentes ----------------
                    limpiarDatosDeCapturaTutor ();
                    result.tblAlumCapTutor = new Array();
                    result.tblAlumCapTutor[0] = tblAlumCapTutor_selRow;
                    insertarDatosDeCaptura_Tutor (result);
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

function setDatosLabel_CapTutorAlum (caso, cveprograma, idalu, curp)
{
    if (caso==="AlumCapTutor"){
        $('#lblTblPrincipal_cct').text(jsCaptuTutorAlum.tblPrincipal_cct);
        $('#lblTblPrincipal_grado').text(jsCaptuTutorAlum.tblPrincipal_grado);
        $('#lblTblPrincipal_grupo').text(jsCaptuTutorAlum.tblPrincipal_grupo);
        $('#lblTblAlumCapTutor_cveprograma').text(cveprograma);
        $('#lblTblAlumCapTutor_idalu').text(idalu);
        $('#lblTblAlumCapTutor_curp').text(curp);
    }
}

function limpiarDatosDeCapturaTutor ()
{     
    $("#txtCurp").val("");
    $("#txtApe1").val("");
    $("#txtApe2").val("");
    $("#txtNombre").val("");
    $("#txtTelefono").val("");
    $($("#pnlCaptuTutorAlum #pnlCapturaDeTutores select")).prop('selectedIndex', "");
    document.getElementById('txtCurp').focus();
}

function btnGuardarCapTutor_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
  
    if (jsCaptuTutorAlum.capRepOf)
        mensaje.CapRepEval("CAP_REP_OFICIALIZADA");
    else if ( tabla.getSelectedIndexRow("tblAlumCapTutor") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar sus avances");
    else if (validarEntradas_CapTutor ()){
                
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"DaTut", metodo:"btGdDaTu", califCicEscIn:jsCaptuTutorAlum.califCicEscIn, tblPrincipal_idcct:jsCaptuTutorAlum.tblPrincipal_idcct,
            tblPrincipal_grado:jsCaptuTutorAlum.tblPrincipal_grado, tblPrincipal_grupo:jsCaptuTutorAlum.tblPrincipal_grupo, 
            idalu:jsCaptuTutorAlum.tblAlumCapTutor_idalu, cveplan: jsCaptuTutorAlum.tblPrincipal_cveplan,
            cveparent:$('#cbxParent').val(), txtTel:$("#txtTelefono").val().trim(), txtCurp:$("#txtCurp").val().trim().toUpperCase(),             
            txtApe1:$("#txtApe1").val().trim().toUpperCase(), txtApe2:$("#txtApe2").val().trim().toUpperCase(), txtNombre:$("#txtNombre").val().trim().toUpperCase()
        };
        
        //------------------------- Hacemos la llamada -------------------------
        cargarLoading();          
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:true                                                                                                                                        //Sí es forsoso que no sea asíncrono, para que espere a que se carguen todas las materias
        })
        .done(function(result){
            switch(result.returnCase){
                case 1:
                        mensaje.General("GUARDADO_EXITOSO");
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

function validarEntradas_CapTutor ()
{
    var mensaje = new Mensajes ();
    var curp = $("#txtCurp").val().trim();
    var fecha = curp.substring(8,10)+"/"+curp.substring(6,8)+"/"+(curp.substring(4,6)<23 ? "20"+curp.substring(4,6) : "19"+curp.substring(4,6));
    
    if ($("#txtCurp").val().trim() === ""){
        mensaje.Tutor ("SIN_DATO","la Curp");    $("#txtCurp").focus();  return false;    
    } else {
        if($("#txtCurp").val().trim().length<18){
            mensaje.Tutor ("INCORR_LONG","la Curp");$("#txtCurp").focus();  return false;
        } 
        else if(!valCURP($("#txtCurp").val().trim().toUpperCase()) || !isFecha(fecha)){
            mensaje.Tutor ("FORMATO_INCOR","la Curp");
            $("#txtCurp").focus();  return false;
        }
    } 
    
    if ($("#txtApe1").val().trim() === ""){
        mensaje.Tutor ("SIN_DATO","el Primer apellido");    $("#txtApe1").focus();  return false;
    }/*else if(isNombreOApellido($("#txtApe1").val().trim(),50)) {
        mensaje.Tutor ("FORMATO_INCOR","el Primer apellido");    $("#txtApe1").focus();  return false;
    }*/
    
    if ($("#txtNombre").val().trim() === ""){
        mensaje.Tutor ("SIN_DATO","el Nombre");    $("#txtNombre").focus();  return false;    
    }/*else if(isNombreOApellido($("#txtNombre").val().trim(),$("#txtNombre").val().trim().length)) {
        mensaje.Tutor ("FORMATO_INCOR","el Nombre");    $("#txtNombre").focus();  return false;
    }*/
    
    if ($("#cbxParent").val().trim() === ""){
        mensaje.Tutor ("SIN_DATO","el parentesco");    $("#cbxParent").focus();  return false;
    }
    
    if ($("#txtTelefono").val().trim() !== "" && !($("#txtTelefono").val().trim()).match(/^\d{10}$/) ){        
            mensaje.Tutor ("FORMATO_INCOR","el número telefónico");    
            $("#txtTelefono").focus();  
            return false;        
    }
        
    return true;
}

   
function valCURP(curp)
{
    //var re = /^([a-z]{4})([0-9]{6})([a-z]{6})([0-9]{2})$/i;
    var re = /^([A-Z][AEIOUX][A-Z]{2}\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d])(\d)$/,
      //  validado = curp.match(re);
    
    validado = curp.match(re);
	
    if (!validado)  //Coincide con el formato general?
    	return false;   
    return true;
}

function btnAnteriorGrupo_CapTutor_Click()
{
    var tabla = new Tabla();
    
    if (jsCaptuTutorAlum.hayCambios) 
        btnGuardarCapTutor_Click ();
    
    if ( tabla.getSelectedIndexRow ('tblPrincipal')-1 >= 0 ){
        var datos = { 
            modulo:"DaTut", metodo:"btAnGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","cveplan"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaptuTutorAlum.califCicEscIn
        };
        sigAntGrupo_CapTutor (datos);
    }
}

function btnSiguienteGrupo_CapTutor_Click()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows ('tblPrincipal');
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    
    
    if (posSelActual < (numFilas-1))
    {
        if (jsCaptuTutorAlum.hayCambios)
            btnGuardarCapTutor_Click ();

        var datos = { 
            modulo:"DaTut", metodo:"btSiGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","cveplan"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaptuTutorAlum.califCicEscIn
        };

        sigAntGrupo_CapTutor (datos);
    }
}

function sigAntGrupo_CapTutor (datos)
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
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
                    tabla.setSelectedRow ('tblPrincipal', result.tblPrincipal_selectedRow);
                    jsCaptuTutorAlum.tblPrincipal_grado = result.tblPrincipal_grado; 
                    jsCaptuTutorAlum.tblPrincipal_grupo = result.tblPrincipal_grupo;

                    
                    insertarTablaTblAlumCapTutor (result.tblAlumCapTutor);
                    insertarDatosDeCaptura_Tutor (result);
                    //cerrarLoading();
                break;
            case 0: case -1:
                    //cerrarLoading();
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