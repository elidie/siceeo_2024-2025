/* 
    Creado el : 13/05/2015, 11:07:21 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

function NuevoIngresoPre_Create(cct, grado, grupo)
{
    /*Empezamos a crear la tabla utilizando la Libreria Jquery dentro del fieldset con identificador frmwNuevoIngreso*/
    $('#frmwNuevoIngreso').append('<fieldset id="frmfPreescolar"><legend>Captura de alumnos a Preescolar</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $('#frmfPreescolar').append('<table id="ftblPreescolar"></table>');
            $('#ftblPreescolar').append("<tr><th class='estiloEncabezados' colspan='2' >"+cct+" - "+grado+" - "+grupo+"</th></tr>");

            $('#ftblPreescolar').append("<tr><th>* Primer Apellido</th>         <td><input type='text' id='txtApe1'  maxlength='30' onBlur='verCurp();' required tabindex='100'  value=''/></td></tr>");
            $('#ftblPreescolar').append("<tr><th>Segundo Apellido</th>         <td><input type='text' id='txtApe2'  maxlength='30' onBlur='verCurp();' tabindex='101' value=''/></td></tr>");
            $('#ftblPreescolar').append("<tr><th>* Nombre(s)</th>                <td><input type='text' id='txtNombre'  maxlength='40' onBlur='verCurp();' tabindex='102' value='' required/></td></tr>");
            $('#ftblPreescolar').append("<tr><th>* Género</th>                     <td><select id='cbxSexo' onBlur='verCurp();' tabindex='103'>  <option value=''></option>  <option value='M'>MUJER</option>  <option value='H'>HOMBRE</option>  </select></td></tr>");
            $('#ftblPreescolar').append("<tr><th>* Fecha de Nacimiento</th>  <td><input type='text' id='txtEdad'  maxlength='10'  pattern='(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}' placeholder='aaaa/mm/dd' required tabindex='104' value=''/>  <label id='lblEdad'></label>  </td></tr>");
            $('#ftblPreescolar').append("<tr><th>* Entidad de Nacimiento</th><td><select id='cbxEntidad' onBlur='verCurp();' tabindex='105'></select></td></tr>");
            $('#ftblPreescolar').append("<tr><th>CURP</th>                           <td><input type='text' id='txtCurp' maxlength='17' readonly value=''/> <input id='txt18' maxlength='2' tabindex='106' type='text' value=''/></td></tr>");
            $('#ftblPreescolar').append("<tr><th>Carta Compromiso</th>        <td><select id='cbxKrta' tabindex='107'> <option value='NO'>NO</option> <option value='SI'>SI</option> </select></td></tr>");
            $('#ftblPreescolar').append("<tr><th>Nec. Educ. Especiales</th>    <td><select id='cbxDiscap' tabindex='108'></select></td></tr>");
            $('#ftblPreescolar').append("<tr><th>Nacionales (PROBEM)</th>    <td><select id='cbxProbem' tabindex='109'> <option></option> <option>T) NAC PROV DE EUA CON DOC DE TRANSF</option> <option>N) NAC PROV DE EUA SIN DOC DE TRNASF</option> <option>E) NAC PROV DE OTROS PAISES</option> <option>D) ALUMNOS QUE SE DIRIGEN A LOS EUA CON D</option></select></td></tr>");
            $('#ftblPreescolar').append("<tr><th>Alumnos Extranjeros</th>     <td><select id='cbxExtj' tabindex='110'> <option></option> <option>1) AFRICA</option> <option>2) ASIA</option> <option>3) CANADA</option> <option>4) CENTROAMERICA Y EL CARIBE</option> <option>5) EUA</option> <option>6) EUROPA</option> <option>7) OCEANIA</option> <option>8) SUDAMERICA</option></select></td></tr>");
            $('#ftblPreescolar').append("<tr><th>Lengua indígena</th>     <td><select id='cbxOtrasLenguas' name='cbxOtrasLenguas' tabindex='111'></select></td></tr>");           
            $('#ftblPreescolar').append("<tr><th>Pertenece a la etnia afromexicana</th>     <td><select id='cbxEtnia' tabindex='112'></option><option value='NO' selected>NO</option> <option value='SI'>SI</option> </select></td></tr>");
            $('#ftblPreescolar').append("<tr><th id='resaltado' ><label>* Fecha de Incripción o de traslado a la escuela (AAAA/MM/DD):</label></th><td><input type='text' id='txtFechaIng' maxlength='10' pattern='[0-9]{4}/(0[1-9]|1[012])/(0[1-9]|1[0-9]|2[0-9]|3[01])' placeholder='aaaa/mm/dd' required tabindex='113' value=''/><label id='lblFechaIng'></label>  </td></tr>");
            
            $('#ftblPreescolar').append("<tr><td colspan='2'><center> <input type='button' id='btnGuardar' class='buttonGuardar' tabindex='113' value='Guardar'/> <input type='button' id='btnLimpiar' class='buttonLimpiar' tabindex='112' value='Limpiar'/></center></td></tr>");
            $('#ftblPreescolar').append("<tr><td colspan='2'><label class='lblRangoEdad'>"+jsNvoIngreso.lblRangoEdad+"</label></td></tr>");                
            $('#ftblPreescolar').append("<tr><td colspan='2'><center><label id='lblAvisoGuard'></label></center></td></tr>");                
            $('#fieldPreescolar').append('');
            
    //------------------------------------------ INICIALIZACIÓN DE DATOS -------------------------------------------------
    
    // Creamos el SELECT que contiene las entidades
    $.each(jsNvoIngreso.entidades,function(indice,valor) {
           $("#cbxEntidad").append("<option value='"+indice+"'>"+valor+"</option>");                        
    });        
    // Creamos el SELECT que contiene a las Necesidades Especiales
    $.each(jsNvoIngreso.necesidadesEspeciales,function(indice,valor) {
        $("#cbxDiscap").append("<option value='"+valor+"'>"+valor+"</option>");
    });
    // Creamos el SELECT que contiene las entidades
    $("#cbxOtrasLenguas").append("<option value='' selected></option>");
    $.each(jsNvoIngreso.lenguas,function(clave,valor) {
           $("#cbxOtrasLenguas").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"'>"+valor.trim()+"</option>");                        
    });
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
     $("#txtEdad").on("blur", function(e){ txtEdad_Pre_Exit(); });
    $("#cbxExtj").bind("keydown", function(e){ cbxExtj_Pre_KeyDown (e); });
    $("#btnGuardar").bind("keydown", function(e){ if(e.which === 13)  { btnGuardar_Pre_ActionPerformed(); e.preventDefault(); } });     //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnGuardar").bind("click", function(e){ btnGuardar_Pre_ActionPerformed(); /*e.preventDefault();*/ });
    $("#btnLimpiar").bind("keydown", function(e){ btnLimpiar_Pre_KeyDown (e); /*e.preventDefault();*/ });
    $("#btnLimpiar").bind("click", function(e){ btnLimpiar_Pre_ActionPerformed (); });
    $('#btnRegresar').click(function(){ jsNvoIngreso=null; irAVentanaPrincipal(); });
    $('#frmfPreescolar').change(function(){ $('#lblAvisoGuard').text(""); });
    
    $("#txtApe1").focus();
    //initEventsMain();
}

function txtEdad_Pre_Exit ()
{
    var mensaje = new Mensajes();
    try{
        verCurp();
        var strFecha = ""+$("#txtEdad").val().substring(0,4)+"-"+parseInt($("#txtEdad").val().substring(5,7))+"-"+$("#txtEdad").val().substring(8,10);
        //var fechaNac = new Date( $("#txtEdad").val().substring(0,4), parseInt($("#txtEdad").val().substring(5,7))-1, $("#txtEdad").val().substring(8,10) );        
        var fechaNac = new Date(strFecha);    
        
        if (fechaNac.toString() === "Invalid Date") {
            mensaje.General("FECHA_INVALIDA", "\n\nFecha de Nacimiento", "");
            $('#btnGuardar').addClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",true);
            document.getElementById('txtEdad').value="";            
        }
        
        var anioNac = fechaNac.getFullYear();                                                                                                  //OJO: Enero empieza en 0, Diciembre es 11
        var anioAct = jsNvoIngreso.tblPrincipal_cicescini;
        
        var itemp = anioAct;
        var iTemp2 = anioNac;

        if ( anioAct < anioNac )
            $('#lblEdad').text("Edad: "+(itemp-iTemp2-1));
        else 
            $('#lblEdad').text("Edad: "+(itemp-iTemp2));

        if ( ((itemp-iTemp2)< jsNvoIngreso.edadMin) || ((itemp-iTemp2)> jsNvoIngreso.edadMax) ) {
            $('#btnGuardar').addClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",true);
        }else {
            $('#btnGuardar').removeClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",false);
        }

         if ( jsNvoIngreso.tblPrincipal_modalidad==="DML" && ((itemp-iTemp2)>=jsNvoIngreso.edadMin)  ) {               //lo k cumpla con la edad minima
            $('#btnGuardar').removeClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",false);
        }
    }catch (e) {
        mensaje.NuevoIngreso("FECHA_INVALIDA");
        $('#btnGuardar').addClass("desactivarBoton");
        $('#btnGuardar').attr("disabled",true);
        //$('#txtEdad').focus();
        document.getElementById('txtEdad').value="";
    }
}

function cbxExtj_Pre_KeyDown (e)
{
    /*if(e.which === 9) { //Si es tab
        if(document.getElementById('btnGuardar').disabled) {
            document.getElementById('btnLimpiar').focus();
            e.preventDefault();  
        }
    }*/
}

function btnGuardar_Pre_ActionPerformed()
{
    var mensaje = new Mensajes();
    
    if( validarEntradas_Pre() )
    {
        var datos = {modulo:"nuIn", metodo:"gu", numLlamada:"0", alumEstatus:jsNvoIngreso.alumEstatus, existeUno:"AquiNoAplica", 
            tblPrincipal_cveplan:jsNvoIngreso.tblPrincipal_cveplan, txtApe1:$("#txtApe1").val().trim().toUpperCase(), txtApe2:$("#txtApe2").val().trim().toUpperCase(), 
            txtNombre:$("#txtNombre").val().trim().toUpperCase(), cbxSexo:$("#cbxSexo").val(), fechaNacimiento:$("#txtEdad").val().trim(), 
            tblPrincipal_grado:jsNvoIngreso.tblPrincipal_grado,tblPrincipal_modalidad:jsNvoIngreso.tblPrincipal_modalidad, 
            tblPrincipal_idcct:jsNvoIngreso.tblPrincipal_idcct, tblPrincipal_cveturno:jsNvoIngreso.tblPrincipal_cveturno, 
            tblPrincipal_cveprograma:jsNvoIngreso.tblPrincipal_cveprograma, tblPrincipal_cicescini:jsNvoIngreso.tblPrincipal_cicescini, 
            tblPrincipal_cct:jsNvoIngreso.tblPrincipal_cct, tblPrincipal_grupo:jsNvoIngreso.tblPrincipal_grupo, txt18:$("#txt18").val().trim().toUpperCase(), 
            cbxKrta:$("#cbxKrta option:selected").text(), cbxDiscap:$('#cbxDiscap').val(), cbxEntidad:$('#cbxEntidad option:selected').text().trim(), 
            cbxLenguas:$('#cbxOtrasLenguas option:selected').val(), cbxEtnia:$('#cbxEtnia option:selected').text().trim(),fechaIngreso:$("#txtFechaIng").val().trim(),
            cbxProbem:$('#cbxProbem option:selected').text(), cbxExtJ:$('#cbxExtj option:selected').text(), idaluDeCurpRep: ""
        };
        cargarLoading();
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async: true
        })
        .done(function(result){
            switch(result.returnCase) {
                case 1:
                        $("#lblEdad").text(result.lblEdad);
                        jsNvoIngreso.alumEstatus = result.alumEstatus;
                        btnLimpiar_Pre_ActionPerformed ();
                        $('#lblAvisoGuard').text("Proceso realizado con éxito.");
                        cerrarLoading();
                    break;
                case 2:
                        cerrarLoading();
                        if (result.btnLimpiar_Click)
                            btnLimpiar_Pre_ActionPerformed ();
                        if (result.casoRequerido === "winMasDeUno")
                            frmwMasDeUno_Show ("frmfPreescolar", result.tblCurpsRep, datos, guardarCurpRep_Pre);
                    break;
                case 10:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        cerrarLoading();
                        if (result.btnLimpiar_Click)
                            btnLimpiar_Pre_ActionPerformed ();
                        if (result.casoRequerido === "winAjustar")
                            frmwAjustar_Show ( "frmfPreescolar", result.kienLlamas, "", result.curpD10 );
                    break;
                case 0: case -1:
                        $("#txtCurp").focus();
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                          //Al aventar este alert, quien sabe porqué, el evento keydown que llama a esta función se vuelve a llamar automáticamente (causado por el evento click), así que será necesario poner un preventDefault
                        cerrarLoading();
                    break;
                case -10:
                        //e.preventDefault();
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        btnCerrarSesion_ActionPerformed ();
                    break;
                default:
                        cerrarLoading();
                    break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
            cerrarLoading();
        });
        
    }
    //initEventsMain();
}

/* 
   Esta funcion es exclusivamente para envir a la ventana MasDeUno, 
   servirá para que al oprimir su botón Aceptar o Cancelar, mande a llamar a esta función.
*/
function guardarCurpRep_Pre (optionButton, posSelCurps, tblCurpsRep_SelectedRow, parametros)
{
    var mensaje = new Mensajes();
    //--------------- Establecemos los nuevos datos a enviar ---------------
    parametros.numLlamada = "1";
    parametros.existeUno = (optionButton===null || optionButton==="CANCELAR")? "rechazados" : "ok";
    parametros.idaluDeCurpRep = tblCurpsRep_SelectedRow ? tblCurpsRep_SelectedRow.idalu : null;
    
    if (posSelCurps >= 0)
        var QTabla = arrayJSON_To_ArrayString (parametros.QBuskAlum, posSelCurps);
    parametros.nomColsQBuskAlum= QTabla ? QTabla.nameCols : "";
    parametros.filaSelQBuskAlum= QTabla ? QTabla.rows : [""];
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: parametros,
        async:true
    })
    .done(function(result){
        switch(result.returnCase) {
            case 1:
                    $("#lblEdad").text(result.lblEdad);
                    jsNvoIngreso.alumEstatus = result.alumEstatus;
                    btnLimpiar_Pre_ActionPerformed ();
                    $('#lblAvisoGuard').text("Proceso realizado con éxito.");
                    cerrarLoading();
                break;
            case 0: case -1:
                    $("#txtCurp16").focus();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                         //Al aventar este alert, quien sabe porqué, el evento keydown que llama a esta función se vuelve a llamar automáticamente (causado por el evento click), así que será necesario poner un preventDefault
                    cerrarLoading();
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:
                    cerrarLoading();
                break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function btnLimpiar_Pre_KeyDown (e)
{
    if(e.which === 9)                                                                                                                                   //Si oprimió tab
    {
        if($('#txtNombre').attr("disabled"))
            document.getElementById('cbxKrta').focus();
        else
            document.getElementById('txtApe1').focus();
    } else if(e.which === 13)                                                                                                                       //Si oprimió enter
        btnLimpiar_Pre_ActionPerformed ();
}

function btnLimpiar_Pre_ActionPerformed ()
{
    var nomDatos = new Array("txtCurp","txt18","txtApe1","txtApe2","txtNombre","cbxSexo","txtEdad","cbxEntidad");
    for(i=0; i<nomDatos.length; i++ ){
        if($('#'+nomDatos[i]).attr("disabled"))
            $('#'+nomDatos[i]).removeAttr("disabled");
    }
    jsNvoIngreso.alumEstatus = "X";
    //jsNvoIngreso.existeUno = "x";         //OJO: Si ya funciona el sistema, eliminar sin piedad
    //-------------------- Limpiamos todos los componentes --------------------
    $('#frmfPreescolar :text').val("");  //Limpiamos todos los textbox    
    
    $($("#frmfPreescolar select")).prop('selectedIndex', 0);                                                                            //Seleccionamos el index 0 de todos los combobox. Tambien se puede usar:     $("#"+campos[i]+" option:first-child").attr('selected', 'selected');
    //$($("#cbxEntidad")).prop('selectedIndex', 20);                                                                                          //El combobox cbxEntidad lo dejamos en el item con entidad Oaxaca.   Tambien se puede usar:   $("#cbxEntidad").val("20");
    $('#lblEdad').text("");
    $('#txtFechaIng').val("");
    $('#lblAvisoGuard').text("");
    
    document.getElementById('txtApe1').focus();
}

function validarEntradas_Pre ()
{
    var mensaje = new Mensajes ();
    
    if ($("#txtApe1").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","el Primer apellido");    $("#txtApe1").focus();  return false;
    }if ($("#txtNombre").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","el Nombre");    $("#txtNombre").focus();  return false;
    }if ($("#cbxSexo").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","el Género");    $("#cbxSexo").focus();  return false;
    }if ($("#txtEdad").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","la Fecha de nacimiento");    $("#txtEdad").focus();  return false;
    }if ($("#cbxEntidad").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","la Entidad");    $("#cbxEntidad").focus();  return false;
    }
    return true;
}