/* 
    Creado el : 18/11/2015, 12:36:33 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsPr;

function frmwPreinscripcion_Show(tblPrincipal_idcct, tblPrincipal_modalidad, tblPrincipal_cct, tblPrincipal_nombre, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_cicescini)
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsPr = {
        tblPrincipal_idcct:tblPrincipal_idcct,
        tblPrincipal_modalidad:tblPrincipal_modalidad,
        tblPrincipal_cct:tblPrincipal_cct,
        tblPrincipal_nombre:tblPrincipal_nombre,
        tblPrincipal_cveplan:tblPrincipal_cveplan,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_cicescini:tblPrincipal_cicescini,
        cicesciniPreinsc:parseInt(tblPrincipal_cicescini)+1,
        
        existeUno:null,
        alumEstatus:null,
        idalu:null,
        QBuskAlum_grado: null,
        QBuskAlum_maxCicEscIni: null,
        edadMin: null,
        edadMax:null
    };
    //alert("Aqui va");
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwPreinscripcion").css("display", "block");                           // Mostramos el formulario correspondiente
    
    frmwPreinscripcion_FormActivate ();
}

function frmwPreinscripcion_Close()
{
    jsPr=null;
    irAVentanaPrincipal ();
}

function frmwPreinscripcion_Create (preinscOficializada)
{
    var gradoNumLetra = jsPr.tblPrincipal_grado+((jsPr.tblPrincipal_grado==="1" || jsPr.tblPrincipal_grado==="3")?"er":"do");
    
    if($('#frmfPreinscripcion').length)
        $('#frmfPreinscripcion').remove();
    $('#frmwPreinscripcion').append('<fieldset id="frmfPreinscripcion"><legend>Preinscripción a '+gradoNumLetra+' Grado para el ciclo  '+jsPr.cicesciniPreinsc+'-'+(parseInt(jsPr.cicesciniPreinsc)+1)+'</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfPreinscripcion").append('<div id="pnlPreinscripcion"></div>');
        
            if (!preinscOficializada){
                $("#pnlPreinscripcion").append('<div id="pnlDatosAlumno" class="panel"> <label class="tituloPanel">Datos del alumno</label></div>');
                    $("#pnlDatosAlumno").append('<div id="pnlDatosCurpPriSec" > '
                                                    + '<label id="lblCURP">CURP:</label> '
                                                    +'<input type="text" id="txtCurp16" maxlength="16" tabindex="100" placeholder="Mínimo 10 dígitos"/> '
                                                    +'<input type="text" id="txtCurp17y18" maxlength="2" tabindex="101"/>  '
                                                    + '<ul id="ubtnBuscar" class="buttonBar">  <li><a href="#" id="btnBuskAlum" tabindex="102" title="Buscar curp"><label id="ibtnBuskAlum" class="icon-lupa"></label> Buscar</a></li>  </ul> '
                                                    + '<label id="lblIdalu">idalu: </label>'
                                                +'</div>');
                    $("#pnlDatosAlumno").append('<div id="nombreYNacimiento" class="panel"></div>');
                        $("#nombreYNacimiento").append('<div id="pnlNombreCompleto"> '
                                                        +'<div><label>Primer Ape.: <input type="text" id="txtPrimerApe" maxlength="30" required tabindex="103"/></label></div>'
                                                        +'<div><label>Segundo Ape.: <input type="text" id="txtSegundoApe" maxlength="30" tabindex="104"/></label></div>'
                                                        +'<div><label>Nombre: <input type="text" id="txtNombre" maxlength="40" required tabindex="105" /></label></div>'
                                                    +'</div>');
                        $("#nombreYNacimiento").append('<div id="pnlGeneroFechaEnt"> '
                                                        +'<div id="pnlGenero" ><label>Género: <select id="cbxGenero" name="cbxGenero"  tabindex="106">   <option value=""></option> <option value="M">MUJER</option> <option value="H">HOMBRE</option>  </select></label></div>'
                                                        +'<div id="pnlFechaNac" >'
                                                            +'<label>Fecha Nac.: <input type="text" id="txtFechaNac" maxlength="10" required tabindex="107" placeholder="aaaa/mm/dd" pattern="[0-9]{4}/(0[1-9]|1[012])/(0[1-9]|1[0-9]|2[0-9]|3[01])" /></label>'
                                                            +'<label id="lblEdad"></label>'
                                                        + '</div>'
                                                        +'<div id="pnlEntNac" ><label>Entidad de nac.: <select id="cbxEntidad" name="cbxEntidad" tabindex="108"></select></label></div>'
                                                        +'<div id="pnlDatosCurpPre" > '
                                                            + '<label id="lblCURPPre">CURP:</label><input type="text" id="txtCurp16Pre" maxlength="16" readonly tabindex="109"/>   <input type="text" id="txtCurp17y18Pre" maxlength="2" tabindex="110"/>  '
                                                        +'</div>'
                                                    +'</div>');
                    $("#pnlDatosAlumno").append('<div id="pnlOtrosDatosPreinsc" class="panel">'
                                                    //+'<div><label>Carta Compromiso: </label><select id="cbxKrta" tabindex="111"> <option value="NO">NO</option> <option value="SI">SI</option> </select></div>'
                                                    //+'<div><label>Nec. Educ. Especiales: </label><select id="cbxDiscap" tabindex="112"></select></div>'
                                                    //+'<div><label>Nacionales (PROBEM): </label><select id="cbxProbem" tabindex="113"> <option></option> <option>T) NAC PROV DE EUA CON DOC DE TRANSF</option> <option>N) NAC PROV DE EUA SIN DOC DE TRNASF</option> <option>E) NAC PROV DE OTROS PAISES</option> <option>D) ALUMNOS QUE SE DIRIGEN A LOS EUA CON D</option></select></div>'
                                                    //+'<div><label>Alumnos Extranjeros: </label><select id="cbxExtj" tabindex="114"> <option></option> <option>1) AFRICA</option> <option>2) ASIA</option> <option>3) CANADA</option> <option>4) CENTROAMERICA Y EL CARIBE</option> <option>5) EUA</option> <option>6) EUROPA</option> <option>7) OCEANIA</option> <option>8) SUDAMERICA</option></select></div>'
                                                    +'<div id="pnlHablaEspañol"> <label id="lblHablaEspañol">Habla Español: <input type="radio" id="rbnSiHablaEspañol" name="rbgHablaEspañol" value="rbnSiHablaEspañol" tabindex="115">Sí <input type="radio" id="rbnNoHablaEspañol" name="rbgHablaEspañol" value="rbnNoHablaEspañol" tabindex="116">No</label> </div>'
                                                    +'<div id="pnlHablaOtraLengua"> <label id="lblOtraLengua"> <input type="checkbox" id="chkOtraLengua" name="chkOtraLengua" value="hablaOtraLengua" tabindex="117">Habla otra lengua <select id="cbxOtrasLenguas" name="cbxOtrasLenguas" tabindex="118"></select></label> </div>'
                                                    +'<div id="pnlEtnia"> <label id="lblEtnia"> Pertenece a la etnia afromexicana </label> <select id="cbxEtnia" tabindex="118"></option><option value="" selected></option></option><option value="NO">NO</option><option value="SI">SI</option></select></div>'
                                                +'</div>');
                    $("#pnlDatosAlumno").append('<ul id="ubtnGuardarLimpiar" class="buttonBar"> '
                                                    +'<li><a href="#" id="btnGuardar" tabindex="119" title="Guardar datos"><label class="icon-disquete"></label> Guardar</a></li>   '
                                                    +'<li><a href="#" id="btnLimpiar" tabindex="120" title="Limpiar"><label class="icon-brocha"></label> Limpiar</a></li>'
                                                +'</ul>'
                                                +'<div><label id="lblRangoEdad"></label></div>'
                                                +'<div id="pnlAvisoGuardar"><label id="lblAvisoGuardar"></label></div>'
                                                );
            }
            $("#pnlPreinscripcion").append('<div id="pnlAlumnosPreinscritos" class="panel"><label class="tituloPanel">Alumnos preinscritos</label></div>');
                $('#pnlAlumnosPreinscritos').append('<div>'
                                                        +'<ul id="ubtnEdicion" class="buttonBar"> '
                                                            + ((preinscOficializada)?"":'<li><a href="#" id="btnEliminar" tabindex="121" title="Elimina el alumno seleccionado"><label class="icon-basurero"></label> Eliminar</a></li>')
                                                            +'<li><a href="#" id="btnRefrescar" tabindex="122" title="Actualiza la lista ordenándola alfabéticamente por nombre"><label class="icon-sincronizar"></label> Refrescar</a></li>'
                                                            +'<li><a href="#" id="btnExportPreinscripAExcel" tabindex="123" title="Exporta el grado preinscrito a Excel">Exportar a Excel</a></li>'
                                                            //+'<li><a href="#" id="btnOficializarPreinsc" tabindex="124" title="Si ya terminó de preinscribir todos sus grados de la escuela, indíquelo con este botón."><label class="icon-sello"></label> Oficializar</a></li>'
                                                            //+'<li><a href="#" id="btnDesoficializarPreinsc" tabindex="126" title="Al desoficializar permitirá que se pueda continuar preinscribiendo.">Desoficializar</a></li>'
                                                        +'</ul>'
                                                    +'</div>');
                $('#pnlAlumnosPreinscritos').append('<div id="pnlTblPreinscripcion">  <div id="scrlPreinscripcion" class="scrollTable"></div>  </div>');
            $("#pnlPreinscripcion").append('<div id="divPreinscripToExcel"></div>');
        
        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwPreinscripcion_Close(); });
    
    $("#txtCurp16").on("keydown", function(e){ txtCurpP_KeyDown (e); });
    $("#txtCurp17y18").on("keydown", function(e){ txtCurpP_KeyDown (e); });
    $("#btnBuskAlum").on("keydown", function(e){ if(e.which === 13){ btnBuskAlumP_ActionPerformed (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnBuskAlum').on("click",function(e){ btnBuskAlumP_ActionPerformed (); });
    $("#txtPrimerApe").on("blur",function(){ verCurpP(); return false; });
    $("#txtSegundoApe").on("blur",function(){ verCurpP(); return false; });
    $("#txtNombre").on("blur",function(){ verCurpP(); return false; });
    $("#cbxGenero").on("blur",function(){ verCurpP(); return false; });
    $("#txtFechaNac").on("blur", function(){ txtFechaNac_Exit(); });
    $("#cbxEntidad").on("blur",function(){ verCurpP(); return false; });
    $('#chkOtraLengua').change(function() { chkOtraLengua_Change (); });
    $("#btnGuardar").on("keydown", function(e){ if(e.which === 13){ btnGuardarP_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnGuardar").on("click", function(){ btnGuardarP_ActionPerformed (); /*e.preventDefault();*/ } );
    $("#btnLimpiar").on("keydown", function(e){ btnLimpiarP_KeyDown (e); });
    $("#btnLimpiar").on("click", function(e){ btnLimpiarP_ActionPerformed (e); });
    $('#frmfPreinscripcion').change(function(){ $('#lblAvisoGuardar').text(""); });
    $("#btnEliminar").on("click", function(){ btnEliminarP_ActionPerformed (); /*e.preventDefault();*/ });
    $("#btnEliminar").on("keydown", function(e){  if(e.which === 13){ btnEliminarP_ActionPerformed (); e.preventDefault(); } });      //El preventDefault es porque en el método btnEliminarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnRefrescar").on("click", function(){ btnRefrescarP_ActionPerformed (); /*e.preventDefault();*/ });
    $("#btnRefrescar").on("keydown", function(e){  if(e.which === 13){ btnRefrescarP_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnRefrescarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnExportPreinscripAExcel").on("click", function(){ btnExportPreinscripAExcel_ActionPerformed (); /*e.preventDefault();*/ });
    $("#btnExportPreinscripAExcel").on("keydown", function(e){  if(e.which === 13){ btnExportPreinscripAExcel_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnRefrescarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    //$("#btnOficializarPreinsc").on("click", function(){ btnOficializarPreinsc_ActionPerformed (); /*e.preventDefault();*/ });
    //$("#btnOficializarPreinsc").on("keydown", function(e){  if(e.which === 13){ btnOficializarPreinsc_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnRefrescarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    //$("#btnDesoficializarPreinsc").on("click", function(){ btnDesoficializarPreinsc_ActionPerformed (); /*e.preventDefault();*/ });
    //$("#btnDesoficializarPreinsc").on("keydown", function(e){  if(e.which === 13){ btnDesoficializarPreinsc_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnRefrescarP_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    
    
    if (jsPr.tblPrincipal_cveplan==="3" && !preinscOficializada)
        document.getElementById('txtPrimerApe').focus();
    else if (!preinscOficializada)
        document.getElementById('txtCurp16').focus();
    
    $('#cbxOtrasLenguas').attr("disabled",true);
}

function frmwPreinscripcion_FormActivate ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pr", metodo:"foAc", tblPrincipal_idcct:jsPr.tblPrincipal_idcct, tblPrincipal_modalidad:jsPr.tblPrincipal_modalidad,
        tblPrincipal_cveplan:jsPr.tblPrincipal_cveplan, tblPrincipal_grado:jsPr.tblPrincipal_grado, cicesciniPreinsc: jsPr.cicesciniPreinsc,
        txtUsuario: sisVars.usuario
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true                                                              //Necesario el true, vara que se vea el loading
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                jsPr.edadMin = result.edadadmmin;
                jsPr.edadMax = result.edadadmmax;
                jsPr.alumEstatus=result.alumEstatus;
                
                frmwPreinscripcion_Create (result.preinscOficializada);
                
                if (jsPr.tblPrincipal_cveplan==="3")
                    object_setVisible (false,"pnlDatosCurpPriSec");
                else
                    object_setVisible (false,"pnlDatosCurpPre");
                
                tabla.create("scrlPreinscripcion","tblPreinscripcion",result.tblPreinscripcion, ["idalu","CURP","Nombre completo","Carta", "Habla Esp.", "Otra Leng.","Afromexicana"], ["idalu","curp","nom_tot","krtacompromiso", "hablaespaniol", "cveotralengua","etnia"], null, ["","","","center","center","center","center"], true, null, null, null);
                // Cargamos las entidades
                //$("#cbxEntidad").append("<option value=''  selected></option>");
                $.each(result.estados,function(i,valor) {
                       $("#cbxEntidad").append("<option value='"+i+"'>"+valor+"</option>");                        
                });
                
                //Cargamos las discapacidades
                //$("#cbxDiscap").append("<option value=''  selected></option>");
                //$.each(result.discap,function(i,valor) {
                //    $("#cbxDiscap").append("<option value='"+valor["cvedefsuf"]+"'>"+valor["cveydes"]+"</option>");
                //});
                
                //Cargamos las lenguas 
                $("#cbxOtrasLenguas").append("<option value=''  selected></option>");
                $.each(result.lenguas,function(clave,valor) {
                       $("#cbxOtrasLenguas").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"'>"+valor.trim()+"</option>");                        
                });
                
                //Verificamos permisos según la oficialización
                permisosDeOficializacion (result.preinscOficializada);
                
                $('#lblRangoEdad').text(result.lblRangoEdad);                 
                cerrarLoading();
                break;
            case 0: case -1:
                    cerrarLoading();
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
        cerrarLoading();
    });
}

function buscarAlumnoP(txtPrimerApe, txtSegundoApe, txtNombre, txtCurp16, txtCurp17y18)
{
    var mensaje = new Mensajes ();
    //var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    // Nota: La petición de Busqueda del Alumno fue realizada de forma sincrona, asi que esperamos el resultado en una variable Global
    // En el supuesto de haber encontrado al alumno se debera quitar los atributos required a los input con id:[txtNombre,txtApe1,txtEdad] para el caso de Primarias
    // y despues se asignaran los resultados encontrados en los campos del formulario (en este caso el de Primaria).
    jsPr.existeUno = 'x';
    jsPr.alumEstatus = "X";
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pr", metodo:(jsPr.tblPrincipal_cveplan==="1"?"buAlPr":"buAlSe"), numLlamada:"0", existeUno:"AquiNoAplica", txtNombre:txtNombre.trim().toUpperCase(), 
        txtPrimerApe:txtPrimerApe.trim().toUpperCase(), txtSegundoApe:txtSegundoApe.trim().toUpperCase(), txtCurp16:txtCurp16.trim(), 
        txtCurp17y18:txtCurp17y18.trim(), cicesciniPreinsc:jsPr.cicesciniPreinsc, tblPrincipal_cveplan:jsPr.tblPrincipal_cveplan, 
        tblPrincipal_idcct:jsPr.tblPrincipal_idcct, tblPrincipal_cct:jsPr.tblPrincipal_cct, tblPrincipal_grado:jsPr.tblPrincipal_grado
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
                        jsPr.alumEstatus = result.alumEstatus;
                        jsPr.idalu = typeof(result.idalu)==="undefined"?"":result.idalu;//Solo para prmaria y secundaria
                        jsPr.QBuskAlum_grado=result.QBuskAlum_grado;                    //Sólo para secundaria
                        jsPr.QBuskAlum_maxCicEscIni=result.QBuskAlum_maxcicescini;      //Sólo para secundaria
                        
                        if (typeof result.mensaje!=="undefined")
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        $("#lblIdalu").text("idalu: "+jsPr.idalu);
                        if(result.alumEstatus==="O")
                            setDatosPreinscripcion(result.curp,result.txtPrimerApe,result.txtSegundoApe,result.txtNombre,result.cbxGenero,result.txtFechaNac,result.cbxEntidad_SelectedIndex,result.idalu, jsPr.tblPrincipal_cveplan,result.cbxOtrasLenguas,result.cbxEtnia);
                        else if(result.alumEstatus==="X")
                            $("#txtCurp16").focus();
                        else if(result.alumEstatus==="I")
                            $("#btnGuardar").focus();
                        cerrarLoading();
                    break;
                case 2: //Mostrar ventana MasDeUno (Sólo lo usa primaria)
                        cerrarLoading();
                        if (result.btnLimpiar_Click)
                            btnLimpiar_Pre_ActionPerformed ();
                        if (result.casoRequerido === "winMasDeUno"){
                            datos.QBuskAlum=result.QBuskAlum;
                            datos.existeUno=result.existeUno;
                            frmwMasDeUno_Show ("frmfPreinscripcion", result.tblCurpsRep, datos, actionOnCurpRepSel_Preinscripcion);
                        }
                    break;
                case 10:// Mostrar ventana Ajustar. Lo usa primaria, secundaria
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        if (result.casoRequerido === "winAjustar"){
                            if (result.btnLimpiar_Click)
                                btnLimpiarP_ActionPerformed ();
                            frmwAjustar_Show ( "frmfPreinscripcion", result.kienLlamas, result.idaluX, result.curpD10 );
                        }
                        cerrarLoading();
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        $("#txtCurp16").focus();
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

function btnGuardarP_ActionPerformed()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    if ( validarEntradas_Preinsc() )
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {modulo:"Pr", metodo:(jsPr.tblPrincipal_cveplan==="1"?"guAlPri":(jsPr.tblPrincipal_cveplan==="2"?"guAlSec":"guAlPre")), numLlamada:"0", 
            existeUno:"AquiNoAplica", alumEstatus:jsPr.alumEstatus, edadMin:jsPr.edadMin, edadMax:jsPr.edadMax, idalu:jsPr.idalu, 
            txtNombre:$("#txtNombre").val().trim().toUpperCase(), txtPrimerApe:$("#txtPrimerApe").val().trim().toUpperCase(), 
            txtSegundoApe:$("#txtSegundoApe").val().trim().toUpperCase(), txtCurp17y18:$("#txtCurp17y18").val().trim().toUpperCase(), 
            fechaNacimiento:$("#txtFechaNac").val().trim(), cbxGenero:$("#cbxGenero").val(), cbxEntidad:$('#cbxEntidad option:selected').text().trim(), 
            txtCurp17y18Pre:$("#txtCurp17y18Pre").val().trim().toUpperCase(), tblPrincipal_idcct:jsPr.tblPrincipal_idcct, tblPrincipal_cct:jsPr.tblPrincipal_cct, 
            tblPrincipal_modalidad:jsPr.tblPrincipal_modalidad, tblPrincipal_cveplan:jsPr.tblPrincipal_cveplan, tblPrincipal_grado:jsPr.tblPrincipal_grado, 
            cicesciniPreinsc:jsPr.cicesciniPreinsc, QBuskAlum_grado:jsPr.QBuskAlum_grado, QBuskAlum_maxcicescini:jsPr.QBuskAlum_maxCicEscIni, 
            /*cbxDiscap:$('#cbxDiscap').val(), cbxKrta:$("#cbxKrta option:selected").text(), cbxProbem:$('#cbxProbem option:selected').text(), 
            cbxExtj:$('#cbxExtj option:selected').text(),*/ hablaEspaniol:document.getElementById('rbnSiHablaEspañol').checked, 
            cbxEtnia:$('#cbxEtnia option:selected').text().trim(),
            cveLengua:!document.getElementById('chkOtraLengua').checked?"ESP":$('#cbxOtrasLenguas').val(), txtUsuario:sisVars.usuario
        };
        //------------------------- Hacemos la llamada -------------------------
        cargarLoading();
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:true
        }).done(
            function(result){
            switch(result.returnCase) {
                case 1:
                        jsPr.alumEstatus = result.alumEstatus;
                        jsPr.idalu = result.idalu;                              //Solo para prmaria y secundaria
                        
                        tabla.addRows (-1,"tblPreinscripcion",result.rowPreinscripcion, ["idalu","curp","nom_tot","krtacompromiso", /*"cvedefsufx", "probem", "extranjero",*/ "hablaespaniol", "cveotralengua","etnia"], null, ["","","","center",/*"center","center","center",*/"center","center","center"], true, null, null, null);
                        btnLimpiarP_ActionPerformed ();
                        $('#lblAvisoGuardar').text("PROCESO REALIZADO CON ÉXITO.");
                        cerrarLoading();
                    break;
                case 2: //Mostrar ventana MasDeUno (Sólo lo usa Preescolar)
                        cerrarLoading();
                        if (typeof result.btnLimpiar_Click!=="undefined" && result.btnLimpiar_Click)
                            btnLimpiar_Pre_ActionPerformed ();
                        if (result.casoRequerido === "winMasDeUno"){
                            datos.QBuskAlum=result.QBuskAlum;
                            frmwMasDeUno_Show ("frmfPreinscripcion", result.tblCurpsRep, datos, actionOnCurpRepSel_Preinscripcion);
                        }
                    break;
                case 10: //Mostrar ventana Ajustar. Sólo lo usa Preescolar
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        cerrarLoading();
                        if (result.btnLimpiar_Click)
                            btnLimpiarP_ActionPerformed ();
                        if (result.casoRequerido === "winAjustar")
                            frmwAjustar_Show ( "frmfPreinscripcion", result.kienLlamas, result.idaluX, result.curpD10 );
                    break;
                case 0: case -1:
                        if (jsPr.tblPrincipal_cveplan === "3")
                            $("#txtCurp16Pre").focus();
                        else
                            $("#txtCurp16").focus();
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                         //Al aventar este alert, quien sabe porqué, el evento keydown que llama a esta función se vuelve a llamar automáticamente (causado por el evento click), así que será necesario poner un preventDefault
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
    //initEventsMain();
}

/* 
   Esta funcion es exclusivamente para envir a la ventana MasDeUno, 
   servirá para que al oprimir su botón Aceptar o Cancelar, mande a llamar a esta función.
*/
function actionOnCurpRepSel_Preinscripcion (optionButton, posSelCurps, tblCurpsRep_SelectedRow, parametros)
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    //--------------- Establecemos los nuevos datos a enviar ---------------
    parametros.numLlamada = "1";
    
    if (jsPr.tblPrincipal_cveplan === "1")
        parametros.existeUno = (optionButton===null || optionButton==="CANCELAR")? "rechazados" : parametros.existeUno;
    else
        parametros.existeUno = (optionButton===null || optionButton==="CANCELAR")? "rechazados" : "ok";
    
    parametros.idaluDeCurpRep = tblCurpsRep_SelectedRow ? tblCurpsRep_SelectedRow.idalu : null;
    if (jsPr.tblPrincipal_cveplan !== "3" ){
        parametros.txtNombre="";
        parametros.txtPrimerApe="";
        parametros.txtSegundoApe="";
    }
    
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
                    jsPr.alumEstatus = result.alumEstatus;
                    jsPr.idalu = result.idalu;                                      //Solo para prmaria y secundaria
                    jsPr.QBuskAlum_grado=result.QBuskAlum_grado;                    //Sólo para secundaria
                    jsPr.QBuskAlum_maxCicEscIni=result.QBuskAlum_maxcicescini;      //Sólo para secundaria
                    
                    if (jsPr.tblPrincipal_cveplan==="1" || jsPr.tblPrincipal_cveplan==="2"){
                        if (typeof result.mensaje!=="undefined")
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        $("#lblIdalu").text("idalu: "+result.idalu);
                        if(result.alumEstatus==="O")
                            setDatosPreinscripcion(result.curp,result.txtPrimerApe,result.txtSegundoApe,result.txtNombre,result.cbxGenero,result.txtFechaNac,result.cbxEntidad_SelectedIndex,result.idalu, jsPr.tblPrincipal_cveplan,result.cveotralengua,result.etnia);
                        else if(result.alumEstatus==="X")
                            $("#txtCurp16").focus();
                        else if(result.alumEstatus==="I")
                            $("#btnGuardar").focus();
                    }else{
                        tabla.addRows (-1,"tblPreinscripcion",result.rowPreinscripcion, ["idalu","curp","nom_tot","krtacompromiso", /*"cvedefsufx", "probem", "extranjero",*/ "hablaespaniol", "cveotralengua","etnia"], null, ["","","","center",/*"center","center","center",*/"center","center","center"], true, null, null, null);
                        $('#lblAvisoGuardar').text("PROCESO REALIZADO CON ÉXITO.");
                    }   
                    cerrarLoading();
                break;
            case 0: case -1:
                    if (jsPr.tblPrincipal_cveplan === "3")
                        $("#txtCurp16Pre").focus();
                    else
                        $("#txtCurp16").focus();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                         //Al aventar este alert, quien sabe porqué, el evento keydown que llama a esta función se vuelve a llamar automáticamente (causado por el evento click), así que será necesario poner un preventDefault
                    cerrarLoading();
                break;
            case 10:// Mostrar ventana Ajustar. Lo usa primaria, secundaria
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    if (result.casoRequerido === "winAjustar"){
                        if (result.btnLimpiar_Click)
                            btnLimpiarP_ActionPerformed ();
                        frmwAjustar_Show ( "frmfPreinscripcion", result.kienLlamas, result.idaluX, result.curpD10 );
                    }
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

function verCurpP()
{
    
    var fecha="",anioNac="",diaNac="",mesNac="";
    
    document.getElementById('txtCurp16').value = "";
    document.getElementById('txtCurp16Pre').value = "";
    if( $("#txtFechaNac").val() !== "")
    {
        fecha = (""+$("#txtFechaNac").val()).split('/');
        anioNac = fecha[0].substring(2,4);
        mesNac = fecha[1];
        diaNac = fecha[2];
    }
    
    //entidad = document.getElementById('cbxEntidad').value;
    var entidad = $("#cbxEntidad option:selected").text().trim();
    entidad = entidad.substring( parseInt(entidad.length) - 2, entidad.length );
    
    var validarCurp = new ValidarCurp($("#txtNombre").val(), $("#txtPrimerApe").val(), $("#txtSegundoApe").val(),anioNac,mesNac,diaNac,$("#cbxGenero").val(),entidad);
    var curp = validarCurp.curp();
    document.getElementById('txtCurp16').value = curp;
    document.getElementById('txtCurp16Pre').value = curp;
}

function btnEliminarP_ActionPerformed ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var posSel;
    
    posSel=tabla.getSelectedIndexRow ('tblPreinscripcion');
    if (posSel===-1)
        mensaje.General("NO_SELEC", " alumno","eliminarlo");
    else 
    {
        if (tabla.getNumRows("tblPreinscripcion")===0)
            mensaje.General("NADA_QUE_ELIMINAR");
        else if( mensaje.confirmDialog ("ELIMINAR","el alumno") === true )
        {
            //------------------ Establecemos los datos a enviar -------------------
            var datos = {
                modulo:"Pr", metodo:"el", idalu:tabla.getSelectedRow("tblPreinscripcion",["idalu"]), tblPrincipal_idcct:jsPr.tblPrincipal_idcct, 
                cicesciniPreinsc:jsPr.cicesciniPreinsc, tblPrincipal_grado:jsPr.tblPrincipal_grado
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
                            tabla.removeRow("tblPreinscripcion",posSel);
                            cerrarLoading();
                            mensaje.General("ELIMINADO", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        break;
                    case 10:// Mostrar mensaje y ventana ajustar
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                            cerrarLoading();
                        break;
                    case 0: case -1:
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                            if (jsPr.tblPrincipal_cveplan === "3")
                                $("#txtPrimerApe").focus();
                            else
                                $("#txtCurp16").focus();
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
    }
}

function btnRefrescarP_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pr", metodo:"acLiPr", tblPrincipal_idcct:jsPr.tblPrincipal_idcct, tblPrincipal_cveplan:jsPr.tblPrincipal_cveplan, 
        tblPrincipal_grado:jsPr.tblPrincipal_grado, cicesciniPreinsc:jsPr.cicesciniPreinsc
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
                    tabla.create("scrlPreinscripcion","tblPreinscripcion",result.tblPreinscripcion, ["idalu","CURP","Nombre completo","Carta", "Habla Esp.", "Otra Leng.","Afromexicana"], ["idalu","curp","nom_tot","krtacompromiso", "hablaespaniol", "cveotralengua","etnia"], null, ["","","","center","center","center","center","center","center","center"], true, null, null, null);
                    cerrarLoading();
                break;
            case 10:// Mostrar mensaje y ventana ajustar
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    if (jsPr.tblPrincipal_cveplan === "3")
                        $("#txtPrimerApe").focus();
                    else
                        $("#txtCurp16").focus();
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

function txtFechaNac_Exit ()
{
    var mensaje = new Mensajes();
    try{
        verCurpP();
    //    FechaNac:=StrToDate(E_Edad.text);
        var fechaNac = new Date( $("#txtFechaNac").val().substring(0,4), parseInt($("#txtFechaNac").val().substring(5,7))-1, $("#txtFechaNac").val().substring(8,10) );
        if (fechaNac.toString() === "Invalid Date")
            throw "Fecha inválida";
        
        var anioNac = fechaNac.getFullYear();                                   //OJO: Enero empieza en 0, Diciembre es 11
        var anioAct = jsPr.cicesciniPreinsc;
        
        var iTemp = anioAct;
        var iTemp2 = anioNac;

        if ( anioAct < anioNac )
            $('#lblEdad').text(" Edad: "+(iTemp-iTemp2-1)+" años en el "+jsPr.cicesciniPreinsc);
        else 
            $('#lblEdad').text(" Edad: "+(iTemp-iTemp2)+" años en el "+jsPr.cicesciniPreinsc);

        if ( ((iTemp-iTemp2)< jsPr.edadMin) || ((iTemp-iTemp2)> jsPr.edadMax) )
            boton_setEnabled (false, "btnGuardar");
        else
            boton_setEnabled (true, "btnGuardar", "click", btnGuardarP_ActionPerformed, "keydown", function(e){ if(e.which === 13){ btnGuardarP_ActionPerformed (); e.preventDefault(); }} );

        if (jsPr.tblPrincipal_cveplan==="2")
        {
            //26 de noviembre 2016 si en el 2016 cumple 15 o mas ya no entra,
            //                     pero si antes de agosto tiene 14 debe entrar
            
            //31 de enero 2017 si en el 2016 cumple 14 entra,
//                     pero si despues del 1ro agosto ya cumple 15 no debe entrar
            if ( (jsPr.tblPrincipal_modalidad==="DES" || jsPr.tblPrincipal_modalidad==="PES" || jsPr.tblPrincipal_modalidad==="DST" || jsPr.tblPrincipal_modalidad==="PST" ) && jsPr.tblPrincipal_grado==="1" && (iTemp-iTemp2)===parseInt(jsPr.edadMax) && parseInt($("#txtFechaNac").val().substring(5,7))<8 )
                boton_setEnabled (false, "btnGuardar");

            if ( jsPr.tblPrincipal_modalidad==="DTV" && jsPr.tblPrincipal_grado==="1" && (iTemp-iTemp2)===parseInt(jsPr.edadMax) && parseInt($("#txtFechaNac").val().substring(5,7))<8 )
                boton_setEnabled (false, "btnGuardar");
        }
//--------------------------------------------------------------------
//
        //solicitud para los DBA y HMC 27 de julio del 2012
        
        if ( (jsPr.tblPrincipal_modalidad==="DBA" ||  jsPr.tblPrincipal_modalidad==="HMC") && ((iTemp-iTemp2)>=13  && (iTemp-iTemp2)<100) )               //lo k cumpla con la edad minima
            boton_setEnabled (true, "btnGuardar", "click", btnGuardarP_ActionPerformed, "keydown", function(e){ if(e.which === 13){ btnGuardarP_ActionPerformed (); e.preventDefault(); }} );
        
        
        if ( jsPr.tblPrincipal_modalidad==="DML"  && (iTemp-iTemp2)>= jsPr.edadMin)      //lo k cumpla con la edad minima
            boton_setEnabled (true, "btnGuardar", "click", btnGuardarP_ActionPerformed, "keydown", function(e){ if(e.which === 13){ btnGuardarP_ActionPerformed (); e.preventDefault(); }} );
            
    }catch (e) {
        mensaje.NuevoIngreso("FECHA_INVALIDA");
        boton_setEnabled (false, "btnGuardar");
        document.getElementById('txtFechaNac').value="";
        //$('#txtFechaNac').focus();
    }
}

function txtCurpP_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
        buscarAlumnoP($('#txtPrimerApe').val(), $('#txtSegundoApe').val(), $('#txtNombre').val(), $('#txtCurp16').val(), $('#txtCurp17y18').val());
}

function btnBuskAlumP_ActionPerformed ()
{
    buscarAlumnoP($('#txtPrimerApe').val(), $('#txtSegundoApe').val(), $('#txtNombre').val(), $('#txtCurp16').val(),$('#txtCurp17y18').val());
}

function chkOtraLengua_Change ()
{
    if ($("#chkOtraLengua").is(':checked'))
        $('#cbxOtrasLenguas').attr("disabled",false);
    else {
        $('#cbxOtrasLenguas').attr("disabled",true);
        $($("#pnlPreinscripcion #cbxOtrasLenguas")).prop('selectedIndex', 0);                                                                               //Seleccionamos el index 0 de todos los combobox.   Tambien se puede usar:     $("#"+campos[i]+" option:first-child").attr('selected', 'selected');
    }
}

function setDatosPreinscripcion(curp, txtPrimerApe, txtSegundoApe, txtNombre, txtGenero, txtFechaNac, cbxEntidad_SelectedIndex, idalu, cveplan,cveOtrasLenguas, cbxEtnia) 
{
    var camposADeshabilitar = new Array("txtCurp16","txtPrimerApe","txtSegundoApe","txtNombre","cbxGenero","txtFechaNac","cbxEntidad");
    //cambioDeForm();
        
    $("#lblIdalu").text(idalu);
    $("#txtPrimerApe").val(txtPrimerApe);
    $("#txtSegundoApe").val(txtSegundoApe);
    $("#txtNombre").val(txtNombre);
    $("#cbxGenero").val(txtGenero);
    $("#txtFechaNac").val(txtFechaNac);    
    $("#cbxOtrasLenguas").val(cveOtrasLenguas);
    $("#cbxEtnia").val(cbxEtnia);
    var indice =  parseInt(cbxEntidad_SelectedIndex);
    if(indice===34)
        $("#cbxEntidad").val(indice-1);
    else  $("#cbxEntidad").val(indice);

    switch (cveplan){
        case "1": txtFechaNac_Exit (); break;
        case "2": txtFechaNac_Exit (); break;
        case "3": txtEdad_Pre_Exit (); break;
    }
    $("#txtCurp16").val(curp.substring(0,16));
    $("#txtCurp17y18").val(curp.substring(16,18));
    
    for(var i=0; i<8;i++)
        $("#"+camposADeshabilitar[i]).attr("disabled","disabled");
    
    if($('#frmfSecundaria').length && $('#cbxArtes').attr("value"))
        $("#btnGuardar").focus();
    else if($('#frmfPrimaria').length)
        $("#btnGuardar").focus();
    else $("#cbxKrta").focus();
}

function btnLimpiarP_KeyDown (e)
{
    if(e.which === 9)                                                           //Tab
    {
        if (jsPr.tblPrincipal_cveplan==="3")
            document.getElementById('txtPrimerApe').focus();
        else
            document.getElementById('txtCurp16').focus();
        e.preventDefault();                                                     //OJO: Sí necesitamos el preventDefault, porque como trae el Tab, si le hacemos focus en un elemento, al salir de la función aplica el tab que ya trae.
    }else if(e.which === 13)
        btnLimpiarP_ActionPerformed ();
}

function btnLimpiarP_ActionPerformed ()
{
    var camposAHabilitar = new Array("txtCurp16","txtCurp17y18","txtCurp17y18Pre","txtPrimerApe","txtSegundoApe","txtNombre","cbxGenero","txtFechaNac","cbxEntidad");
    for(i=0; i<camposAHabilitar.length; i++ ){
        if($('#'+camposAHabilitar[i]).attr("disabled"))
            $('#'+camposAHabilitar[i]).removeAttr("disabled");
    }
    jsPr.alumEstatus = 'X';
    //jsNvoIngreso.existeUno = 'x';  //OJO: Si ya funciona el sistema, eliminar sin piedad
    //-------------------- Limpiamos todos los componentes --------------------
    $('#pnlPreinscripcion :text').val("");                                      //Limpiamos todos los textbox
    $($("#pnlPreinscripcion select")).prop('selectedIndex', 0);                 //Seleccionamos el index 0 de todos los combobox.   Tambien se puede usar:     $("#"+campos[i]+" option:first-child").attr('selected', 'selected');
    //$($("#cbxEntidad")).prop('selectedIndex', 20);                            //El combobox cbxEntidad lo dejamos en el item con entidad Oaxaca.  Tambien se puede usar:   $("#cbxEntidad").val("20");
    
    //PARA SELECCIONAR UN ITEM DE UN COMBOBOX PODEMOS USAR:
    //Para indicar según el valor:          $('#id option[value="SEL0"]').prop('selected', true);  o tambien $("#id").val("SEL0");
    //Para indicar según el índice:         $('#id option:eq(0)').prop('selected', true);
    //Para indicar según un texto conocido: $('#id option:contains("Selection 0")').prop('selected', true);
    
    $('#lblEdad').text("");
    $('#lblAvisoGuardar').text("");
    $('#lblIdalu').text("idalu: ");
    document.getElementById('rbnSiHablaEspañol').checked = false;
    document.getElementById('rbnNoHablaEspañol').checked = false;
    document.getElementById('chkOtraLengua').checked = false;
    
    if (jsPr.tblPrincipal_cveplan==="3")
        document.getElementById('txtPrimerApe').focus();
    else
        document.getElementById('txtCurp16').focus();
}

function validarEntradas_Preinsc ()
{
    var mensaje = new Mensajes ();
    
    if ($("#txtPrimerApe").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","el Primer apellido");    $("#txtPrimerApe").focus();  return false;
    }if ($("#txtNombre").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","el Nombre");    $("#txtNombre").focus();  return false;
    }if ($("#cbxGenero").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","el Género");    $("#cbxGenero").focus();  return false;
    }if ($("#txtFechaNac").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","la Fecha de nacimiento");    $("#txtFechaNac").focus();  return false;
    }if ($("#cbxEntidad").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","la Entidad");    $("#cbxEntidad").focus();  return false;
    }if (!document.getElementById('rbnSiHablaEspañol').checked && !document.getElementById('rbnNoHablaEspañol').checked){
        mensaje.General ("ESPECIFIQUE_DATO","si habla español");    $("#rbnSiHablaEspañol").focus();  return false;
    }if (document.getElementById('chkOtraLengua').checked && document.getElementById('cbxOtrasLenguas').selectedIndex<1){  //Porque la pos 0 tiene blanco
        mensaje.General ("ESPECIFIQUE_DATO","la otra lengua");    $("#cbxOtrasLenguas").focus();  return false;
    }
    return true;
}

function btnExportPreinscripAExcel_ActionPerformed()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var ancho=450, alto=200, posicion_x, posicion_y; 
    var tblAlumCapCalif, datos;
    
    posicion_x=(screen.width/2)-(ancho/2); 
    posicion_y=(screen.height/2)-(alto/2);
    
    mensaje.General("EXCEL_EN_CREACION");
    tblAlumCapCalif = tabla.getTable("tblAlumCapCalif",["idalu","cicescini","grado","grupo"], null, "JSON");
    
    datos = {
        llamada:0, tblPrincipal_idcct:jsPr.tblPrincipal_idcct, tblPrincipal_cct: jsPr.tblPrincipal_cct, tblPrincipal_nombre: jsPr.tblPrincipal_nombre, 
        tblPrincipal_cveplan: jsPr.tblPrincipal_cveplan, tblPrincipal_grado: jsPr.tblPrincipal_grado, cicesciniPreinsc:jsPr.cicesciniPreinsc
    };
    sessionStorage.datosPreinscripcion = JSON.stringify(datos);
    //inicioDeGradoGrupoAExcel ();
    //window.open("Calificaciones/GradoGrupoAExcel.jsp","","status=0, toolbar=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
    window.open("../html/Preinscripcion/exportPreinscripAExcel.html","","status=0, toolbar=0, location=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
}

/*function btnOficializarPreinsc_ActionPerformed()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var respOficializar;
    
    respOficializar = mensaje.Preinscripcion("OFICIALIZAR",jsPr.cicesciniPreinsc+'-'+(parseInt(jsPr.cicesciniPreinsc)+1),"","CONFIRM_DIALOG");
    if (respOficializar === true)
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Pr", metodo:"ofPr", tblPrincipal_idcct:jsPr.tblPrincipal_idcct, cicesciniPreinsc:jsPr.cicesciniPreinsc, txtUsuario: sisVars.usuario
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
                        permisosDeOficializacion (true);
                        
                        mensaje.General("OFICIALIZADO");
                        cerrarLoading();
                    break;
                case 10:// Mostrar mensaje y ventana ajustar
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        cerrarLoading();
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        if (jsPr.tblPrincipal_cveplan === "3")
                            $("#txtPrimerApe").focus();
                        else
                            $("#txtCurp16").focus();
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
}*/

/*function btnDesoficializarPreinsc_ActionPerformed()
{
    var mensaje = new Mensajes();
    var respDesoficializar;
        
    respDesoficializar = mensaje.Preinscripcion("DESOFICIALIZAR",jsPr.cicesciniPreinsc+'-'+(parseInt(jsPr.cicesciniPreinsc)+1),"","CONFIRM_DIALOG");
    if (respDesoficializar === true)
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Pr", metodo:"dePr", tblPrincipal_idcct:jsPr.tblPrincipal_idcct, cicesciniPreinsc:jsPr.cicesciniPreinsc
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
                        permisosDeOficializacion (false);
                        
                        mensaje.General("DESOFICIALIZADO");
                        cerrarLoading();
                    break;
                case 10:// Mostrar mensaje y ventana ajustar
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        cerrarLoading();
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        if (jsPr.tblPrincipal_cveplan === "3")
                            $("#txtPrimerApe").focus();
                        else
                            $("#txtCurp16").focus();
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
}*/

function permisosDeOficializacion (permiso)
{
    if (permiso === true)
        $("#pnlAlumnosPreinscritos").css("margin-left","0px");
    else
        $("#pnlAlumnosPreinscritos").css("margin-left","410px");
}