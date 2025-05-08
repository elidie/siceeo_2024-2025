/* 
    Creado el : 13/05/2015, 11:08:06 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/
var jsNvoIngresoSec;

function NuevoIngresoSec_Create(cct, grado, grupo)
{
    jsNvoIngresoSec = {
        esDSN: (cct.substring(2,5)==="DSN")
    };
    var asteriscoDSN = jsNvoIngresoSec.esDSN?"":"*";
    /*Empezamos a crear la tabla utilizando la Libreria Jquery dentro del fieldset con identificador frmwNuevoIngreso*/
    $('#frmwNuevoIngreso').append('<fieldset id="frmfSecundaria"><legend>Captura de alumnos a Secundaria</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $('#frmfSecundaria').append('<table id="ftblSecundaria"></table>');
            $('#ftblSecundaria').append("<tr><th class='estiloEncabezados' colspan='2'>"+cct+" - "+grado+" - "+grupo+"</th></tr>");
            
            $('#ftblSecundaria').append("<tr><th>CURP</th><td>"
                                                +"<div id='pnlCurp' class='alinearHoriz'><input type='text' id='txtCurp' maxlength='16' tabindex='100' placeholder='Mínimo 10 dígitos' value=''/>"
                                                +"<input type='text' id='txt18' maxlength='2' tabindex='101' value=''/></div>"
                                                + '<ul id="ubtnBuscar" class="buttonBar alinearHoriz">  <li><a href="#" id="btnBuskAlum" tabindex="102" title="Buscar curp"><label class="icon-lupa"></label> Buscar</a></li>  </ul> '
                                        +"</td></tr>");
            $('#ftblSecundaria').append("<tr><th>* Primer Apellido</th>         <td><input type='text' id='txtApe1' maxlength='30' onBlur='verCurp();' required tabindex='103' value=''/></td></tr>");
            $('#ftblSecundaria').append("<tr><th>Segundo Apellido</th>          <td><input type='text' id='txtApe2' maxlength='30' onBlur='verCurp();' tabindex='104' value=''/></td></tr>");
            $('#ftblSecundaria').append("<tr><th>* Nombre(s)</th>               <td><input type='text' id='txtNombre' maxlength='40' onBlur='verCurp();' tabindex='105' value='' required/></td></tr>");
            $('#ftblSecundaria').append("<tr><th>* Género</th>                  <td><select id='cbxSexo' onBlur='verCurp();' tabindex='106'> <option value=''></option> <option value='M'>MUJER</option> <option value='H'>HOMBRE</option> </select></td></tr>");
            $('#ftblSecundaria').append("<tr><th>* Fecha de Nacimiento</th>     <td><input id='txtEdad' maxlength='10' pattern='[0-9]{4}/(0[1-9]|1[012])/(0[1-9]|1[0-9]|2[0-9]|3[01])' placeholder='aaaa/mm/dd' required tabindex='107' type='text' value=''/>  <label id='lblEdad'></label>  </td></tr>");
            $('#ftblSecundaria').append("<tr><th>* Entidad de Nacimiento</th>   <td><select id='cbxEntidad' onBlur='verCurp();' tabindex='108'></select></td></tr>");
            $('#ftblSecundaria').append("<tr><th>Carta Compromiso</th>          <td><select id='cbxKrta' tabindex='109'> <option value='NO'>NO</option> <option value='SI'>SI</option> </select></td></tr>");
            $('#ftblSecundaria').append("<tr id='trTaller'><th>"+asteriscoDSN+" Taller</th>   <td><select id='cbxTalleres' tabindex='110'></select></td></tr>");
            $('#ftblSecundaria').append("<tr><th>* Arte</th>                    <td><select id='cbxArtes' tabindex='111'></select></td></tr>");
            $('#ftblSecundaria').append("<tr><th>Nec. Educ. Especiales</th> <td><select id='cbxDiscap' tabindex='112'></select></td></tr>");
            $('#ftblSecundaria').append("<tr><th>Nacionales (PROBEM)</th> <td><select id='cbxProbem' tabindex='113'> <option></option> <option>T) NAC PROV DE EUA CON DOC DE TRANSF</option> <option>N) NAC PROV DE EUA SIN DOC DE TRNASF</option> <option>E) NAC PROV DE OTROS PAISES</option> <option>D) ALUMNOS QUE SE DIRIGEN A LOS EUA CON D</option></select></td></tr>");
            $('#ftblSecundaria').append("<tr><th>Alumnos Extranjeros</th>  <td><select id='cbxExtj' tabindex='114'> <option></option> <option>1) AFRICA</option> <option>2) ASIA</option> <option>3) CANADA</option> <option>4) CENTROAMERICA Y EL CARIBE</option> <option>5) EUA</option> <option>6) EUROPA</option> <option>7) OCEANIA</option> <option>8) SUDAMERICA</option></select></td></tr>");
            $('#ftblSecundaria').append("<tr><th>Lengua indígena</th> <td><select id='cbxOtrasLenguas' name='cbxOtrasLenguas' tabindex='115'></select></td></tr>");           
            $('#ftblSecundaria').append("<tr><th>Pertenece a la etnia afromexicana</th>     <td><select id='cbxEtnia' tabindex='116'></option><option value='NO' selected>NO</option> <option value='SI'>SI</option> </select></td></tr>");
            $('#ftblSecundaria').append("<tr><th id='resaltado'><label>* Fecha de Incripción o de traslado a la escuela (AAAA/MM/DD):</label></th><td><input type='text' id='txtFechaIng' maxlength='10' pattern='[0-9]{4}/(0[1-9]|1[012])/(0[1-9]|1[0-9]|2[0-9]|3[01])' placeholder='aaaa/mm/dd' required tabindex='113' value=''/><label id='lblFechaIng'></label>  </td></tr>");
            
            $('#ftblSecundaria').append("<tr><td colspan='2'><center><input type='button' id='btnGuardar' class='buttonGuardar' tabindex='117' value='Guardar'/><input type='button' id='btnLimpiar' class='buttonLimpiar' tabindex='116' value='Limpiar'/></center></td></tr>");
            $('#ftblSecundaria').append("<tr><td colspan='2'><label class='lblRangoEdad'>"+jsNvoIngreso.lblRangoEdad+"</label></td></tr>");                
            $('#ftblSecundaria').append("<tr><td colspan='2'><center><label id='lblAvisoGuard'></label></center></td></tr>");
            
    //------------------------------------------ INICIALIZACIÓN DE DATOS -------------------------------------------------

    // Creamos el SELECT que contiene a los Estados    
    $.each(jsNvoIngreso.entidades,function(indice,valor) {
        $("#cbxEntidad").append("<option value='"+indice+"'>"+valor+"</option>");                        
    });
    // Creamos el SELECT que contiene los Talleres
    $("#cbxTalleres").append("<option value=''  selected></option>");
    $.each(jsNvoIngreso.talleres,function(indice,valor) {                                               
        $("#cbxTalleres").append("<option value='"+indice+"'>"+valor+"</option>");
    });
    if(jsNvoIngresoSec.esDSN){                                                  //A los que son DSN les seleccionamos por el taller por default como "NAP", pues no llevan taller
        $('#cbxTalleres').prop('selectedIndex', 1);                             //$('#cbxTalleres > option[value="0"]').attr('selected', 'selected');     //$('#cbxTalleres option[value=0]').attr("selected",true);
        $("#cbxTalleres").attr("disabled", "disabled");
        object_setVisible(false,"trTaller");
    }
    
    // Creamos el SELECT que contiene las Artes
    $("#cbxArtes").append("<option value=''  selected></option>");
    $.each(jsNvoIngreso.artes,function(indice,valor) {             
        $("#cbxArtes").append("<option value='"+indice+"'>"+valor+"</option>");                        
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
    
    $("#txtCurp").on("keydown", function(e){ txtCurp_Sec_KeyDown (e); });
    $("#btnBuskAlum").on("keydown", function(e){ if(e.which === 13) { btnBuskAlum_Sec_ActionPerformed (e);  e.preventDefault(); } });     //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnBuskAlum').on("click",function(e){ btnBuskAlum_Sec_ActionPerformed (e); });
    $("#txtEdad").on("blur", function(e){ txtEdad_Sec_Exit(); });
    $("#cbxArtes").on("keydown", function(e){ cbxArte_KeyDown (e); });
    $("#cbxExtj").on("keydown", function(e){ cbxExtj_Sec_KeyDown (e); });
    $("#btnGuardar").on("keydown", function(e){ if(e.which === 13)  { btnGuardar_Sec_ActionPerformed ();  e.preventDefault(); } });   //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnGuardar").on("click", function(e){ btnGuardar_Sec_ActionPerformed (); /*e.preventDefault();*/ });
    $("#btnLimpiar").on("keydown", function(e){ btnLimpiar_Sec_KeyDown (e);  /*e.preventDefault();*/ } );
    $("#btnLimpiar").on("click", function(){ btnLimpiar_Sec_ActionPerformed ();  });
    $('#btnRegresar').on("click",function(){ jsNvoIngreso=null; irAVentanaPrincipal(); });
    $('#frmfSecundaria').change(function(){ $('#lblAvisoGuard').text(""); });
    //jQuery.fn.reset = function () { $(this).each (function() {this.reset();}); };
    $("#txtCurp").focus();
    //initEventsMain();
}

function txtEdad_Sec_Exit ()
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
        
        var anioNac = fechaNac.getFullYear();                                   //osea 2006      //OJO: Enero empieza en 0, Diciembre es 11
        var anioAct = jsNvoIngreso.tblPrincipal_cicescini;
        
        var iTemp = anioAct;
        var iTemp2 = anioNac;

        if ( anioAct < anioNac )
            $('#lblEdad').text("Edad: "+(iTemp-iTemp2-1)+" años en el "+jsNvoIngreso.tblPrincipal_cicescini);
        else 
            $('#lblEdad').text("Edad: "+(iTemp-iTemp2)+" años en el "+jsNvoIngreso.tblPrincipal_cicescini);

        if ( ((iTemp-iTemp2)< jsNvoIngreso.edadMin) || ((iTemp-iTemp2)> jsNvoIngreso.edadMax) ) {
            $('#btnGuardar').addClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",true);
        }else {
            $('#btnGuardar').removeClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",false);
        }

        //26 de noviembre 2016 si en el 2016 cumple 15 o mas ya no entra,
        //                     pero si antes de agosto tiene 14 debe entrar
        
        //31 de enero 2017 si en el 2016 cumple 14 entra,
//                     pero si despues del 1ro agosto ya cumple 15 no debe entrar
        if ( (jsNvoIngreso.tblPrincipal_modalidad==="DES" || jsNvoIngreso.tblPrincipal_modalidad==="PES" || 
                jsNvoIngreso.tblPrincipal_modalidad==="DST" || jsNvoIngreso.tblPrincipal_modalidad==="PST" || jsNvoIngreso.tblPrincipal_modalidad==="DTV" ) && 
                jsNvoIngreso.tblPrincipal_grado==="1" && 
                (iTemp-iTemp2)===parseInt(jsNvoIngreso.edadMax) && parseInt($("#txtEdad").val().substring(5,7))<8 )
        {
            $('#btnGuardar').addClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",true);
        }

        /*if ( jsNvoIngreso.tblPrincipal_modalidad==="DTV" && 
                jsNvoIngreso.tblPrincipal_grado=="1" && 
                (iTemp-iTemp2)===parseInt(jsNvoIngreso.edadMax) && parseInt($("#txtEdad").val().substring(5,7))<8 )
        {
            $('#btnGuardar').addClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",true);
        }*/
//--------------------------------------------------------------------

        if ( (jsNvoIngreso.tblPrincipal_modalidad==="DBA" ||  jsNvoIngreso.tblPrincipal_modalidad==="HMC") && ((iTemp-iTemp2)>=15  && (iTemp-iTemp2)<100) ) {               //lo k cumpla con la edad minima
            $('#btnGuardar').removeClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",false);
        }
        
        if ( jsNvoIngreso.tblPrincipal_modalidad==="DML"  && (iTemp-iTemp2)>= jsNvoIngreso.edadMin){  //los k cumpla con la edad minima
            $('#btnGuardar').removeClass("desactivarBoton");
            $('#btnGuardar').attr("disabled",false);
        }
        
        if (jsNvoIngreso.tblPrincipal_grado == 1 &&                             //lo keremos en 1ro de secundaria  NOTA: Debe ser doble igual no triple
            jsNvoIngreso.QBuskAlum_grado  === "6" &&                            //y viene de 6to de primaria
            jsNvoIngreso.QBuskAlum_maxCicEscIni < jsNvoIngreso.tblPrincipal_cicescini){  //del un ciclo anterior
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

function txtCurp_Sec_KeyDown (e)
{
    if(e.which === 13) {
        cargaDatosBuscarAlumno_Sec();
        /*e.preventDefault();*/  
    }
}

function btnBuskAlum_Sec_ActionPerformed (e)
{
    cargaDatosBuscarAlumno_Sec();
    /*e.preventDefault();*/
}

function cargaDatosBuscarAlumno_Sec()
{
    //var curpAlumno = document.getElementById('txtCurp').value + document.getElementById('txt18').value;
    //jsNvoIngreso.existeUno = 'x';             //OJO: Si ya funciona el sistema, eliminar sin piedad
    buscarAlumno($('#txtApe1').val(), $('#txtApe2').val(), $('#txtNombre').val(), jsNvoIngreso.tblPrincipal_grado, jsNvoIngreso.tblPrincipal_cicescini, jsNvoIngreso.tblPrincipal_cveplan, $('#txtCurp').val(),$('#txt18').val(),"");
    // Nota: La petición de Busqueda del Alumno fue realizada de forma sincrona, asi que esperamos el resultado en una variable Global
    // En el supuesto de haber encontrado al alumno se debera quitar los atributos required a los input con id:[txtNombre,txtApe1,txtEdad] para el caso de Primarias
    // y despues se asignaran los resultados encontrados en los campos del formulario (en este caso el de Primaria).
}

function cbxArte_KeyDown (e)
{
    if(e.which === 13) {
        validaDatosInsertarNuevoAlumnoSec();
        /*e.preventDefault();*/
    }
}

function cbxExtj_Sec_KeyDown (e)
{
    /*if(e.which === 9) { //Si es tab
        if(document.getElementById('btnGuardar').disabled) {
            document.getElementById('btnLimpiar').focus();
            e.preventDefault();  
        }
    }*/
}

function btnGuardar_Sec_ActionPerformed ()
{
    var mensaje = new Mensajes();
    
    if ( validarEntradas_Sec () )
    {
        var datos = {modulo:"nuIn", metodo:"gu", idalu:jsNvoIngreso.idalu, txtApe1:$("#txtApe1").val().trim().toUpperCase(), 
            txtApe2:$("#txtApe2").val().trim().toUpperCase(), txtNombre:$("#txtNombre").val().trim().toUpperCase(), cbxSexo:$("#cbxSexo").val(), 
            fechaNacimiento:$("#txtEdad").val().trim(), tblPrincipal_grado:jsNvoIngreso.tblPrincipal_grado, 
            tblPrincipal_modalidad:jsNvoIngreso.tblPrincipal_modalidad, tblPrincipal_idcct:jsNvoIngreso.tblPrincipal_idcct, 
            tblPrincipal_cveturno:jsNvoIngreso.tblPrincipal_cveturno, cicescini:jsNvoIngreso.tblPrincipal_cicescini, 
            tblPrincipal_grupo:jsNvoIngreso.tblPrincipal_grupo, txt18:$("#txt18").val().trim().toUpperCase(), cbxKrta:$("#cbxKrta option:selected").text(), 
            cbxDiscap:$('#cbxDiscap').val(), cbxEntidad:$('#cbxEntidad option:selected').text().trim(), cbxProbem:$('#cbxProbem option:selected').text(), 
            cbxLenguas:$('#cbxOtrasLenguas option:selected').val(), cbxEtnia:$('#cbxEtnia option:selected').text().trim(),
            cbxExtJ:$('#cbxExtj option:selected').text(), alumEstatus:jsNvoIngreso.alumEstatus, edadMin:jsNvoIngreso.edadMin, 
            edadMax:jsNvoIngreso.edadMax, tblPrincipal_cct:jsNvoIngreso.tblPrincipal_cct, fechaIngreso:$("#txtFechaIng").val().trim(),
            taller:$('#cbxTalleres option:selected').text(), arte:$('#cbxArtes option:selected').text(), cbxArtes_length:jsNvoIngreso.artes.length, 
            QBuskAlum_grado:jsNvoIngreso.QBuskAlum_grado, QBuskAlum_maxCicEscIni:jsNvoIngreso.QBuskAlum_maxCicEscIni, 
            tblPrincipal_cveplan: jsNvoIngreso.tblPrincipal_cveplan, QPlanMod_cveprograma:jsNvoIngreso.tblPrincipal_cveprograma
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
                        btnLimpiar_Sec_ActionPerformed ();
                        $('#lblAvisoGuard').text("Proceso realizado con éxito.");
                        jsNvoIngreso.alumEstatus = result.alumEstatus;
                        cerrarLoading();
                    break;
                case 0: case -1:
                        $("#txtCurp").focus();
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                          //Al aventar este alert, quien sabe porqué, el evento keydown que llama a esta función se vuelve a llamar automáticamente, así que será necesario poner un preventDefault
                        cerrarLoading();
                    break;
                case -10:
                        //e.preventDefault();
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        btnCerrarSesion_ActionPerformed ();
                        cerrarLoading();
                    break;
                default:break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
            cerrarLoading();
        });
    }
    //initEventsMain();
}

function btnLimpiar_Sec_KeyDown (e)
{
    if(e.which === 9) 
    {
        if($('#txtNombre').attr("disabled"))
            document.getElementById('cbxKrta').focus();
        else
            document.getElementById('txtCurp').focus();
        /*e.preventDefault();*/
    }

    if(e.which === 13)
            btnLimpiar_Sec_ActionPerformed ();
}

function btnLimpiar_Sec_ActionPerformed ()
{
    var nomDatos = new Array("txtCurp","txt18","txtApe1","txtApe2","txtNombre","cbxSexo","txtEdad","cbxEntidad");
    for(i=0; i<nomDatos.length; i++ ){
        if($('#'+nomDatos[i]).attr("disabled"))
            $('#'+nomDatos[i]).removeAttr("disabled");
    }
    
    jsNvoIngreso.alumEstatus = 'X';
    //jsNvoIngreso.existeUno = 'x';         //OJO: Si ya funciona el sistema, eliminar sin piedad
    
    //-------------------- Limpiamos todos los componentes --------------------
    $('#frmfSecundaria :text').val("");                                                                                                           //Limpiamos todos los textbox
    $($("#frmfSecundaria select")).prop('selectedIndex', 0);                                                                           //Seleccionamos el index 0 de todos los combobox. Tambien se puede usar:     $("#"+campos[i]+" option:first-child").attr('selected', 'selected');
    /*if(jsNvoIngresoSec.esDSN)                                                   //A los que son DSN les seleccionamos por el taller por default como "NAP", pues no llevan taller
        $('#cbxTalleres').prop('selectedIndex', 1); */
    //$($("#cbxEntidad")).prop('selectedIndex', 20);                                                                                          //El combobox cbxEntidad lo dejamos en el item con entidad Oaxaca.   Tambien se puede usar:   $("#cbxEntidad").val("20");
    $('#lblEdad').text("");
    $('#txtFechaIng').val("");  
    $('#lblAvisoGuard').text('');
    
    document.getElementById('txtCurp').focus();
}

function validarEntradas_Sec ()
{
    var mensaje = new Mensajes ();
    
    if ($("#txtApe1").val().trim() === "") {
        mensaje.NuevoIngreso ("SIN_DATO","el Primer apellido");    $("#txtApe1").focus();  return false;
    }if ($("#txtNombre").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","el Nombre");    $("#txtNombre").focus();  return false;
    }if ($("#cbxSexo").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","el Género");    $("#cbxSexo").focus();  return false;
    }if ($("#txtEdad").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","la Fecha de nacimiento");    $("#txtEdad").focus();  return false;
    }if ($("#cbxEntidad").val().trim() === ""){
        mensaje.NuevoIngreso ("SIN_DATO","la Entidad");    $("#cbxEntidad").focus();  return false;
    } /*if ($("#cbxTalleres").val().trim() === "" && !jsNvoIngresoSec.esDSN){
        mensaje.NuevoIngreso ("SIN_DATO","el Taller");    $("#cbxTalleres").focus();  return false;
    }if ($("#cbxArtes").val()=== ""){
        mensaje.NuevoIngreso ("SIN_DATO","Eel Arte");    $("#cbxArtes").focus();  return false;
    }*/
    return true;
}