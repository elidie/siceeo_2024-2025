/* 
    Creado el : 18/06/2016, 12:59:15 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsModifCurp;

function frmwModifCurp_Show ( nameParentForm, kienLlamas, idaluX, cicescin, cicescini, tblPrincipal_modalidad, tblPrincipal_cveplan, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo)
{
    jsModifCurp = {
        nameParentForm: nameParentForm,
        cicescin:cicescin,
        cicescini:cicescini,
        kienLlamas:kienLlamas,
        tblPrincipal_modalidad:tblPrincipal_modalidad,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_idcct:tblPrincipal_idcct, 
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo:tblPrincipal_grupo,        
        
        edadMin:"",
        edadMax:"",
        maxCicEscIni:"",
        alumEstatus:"",
        
        laU:"",
        enCicloAnterior:"",
        idalu:"",
        modifCurp:"",
        gdoTerminal:"",
        regCivCertAnt:"",
        validaAnt:"",
        curp_ant:"",
        apepat_ant:"",
        apemat_ant:"",
        nombre_ant:""
    };
    
    object_setVisible (false, nameParentForm);
    $("#frmwModifCurp").css("display", "block");                                  // Mostramos el formulario correspondiente
    
    frmwModifCurp_Create ();
    frmwModifCurp_FormActivate (kienLlamas, idaluX);
}

function frmwModifCurp_Close()
{
    var nombreFormularioQueLlama = jsModifCurp.nameParentForm;
    jsModifCurp=null;
    if (typeof nombreFormularioQueLlama!=="undefined" && nombreFormularioQueLlama!==null && nombreFormularioQueLlama!==""){
        if($("#frmfModifCurp").length)
            $("#frmfModifCurp").remove();
        object_setVisible(true, nombreFormularioQueLlama);
    }else
        irAVentanaPrincipal ();
}

function frmwModifCurp_Create ()
{
    if($('#frmfModifCurp').length) // Verificando si la tabla Existe
        $('#frmfModifCurp').remove();
    $('#frmwModifCurp').append('<fieldset id="frmfModifCurp"><legend>Modifica CURP</legend><div id="btnRegresar_ModifCurp" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfModifCurp").append('<div id="pnlModifCurp"></div>');
        
            $("#pnlModifCurp").append('<div id="pnlDatosAlumno" class="panel"> <label class="tituloPanel">Datos modificables del alumno</label></div>');
                $("#pnlDatosAlumno").append('<div id="pnlLabelCurp">'
                                                +'<div class="anchoFijo alinearHoriz"></div>'
                                                +'<label id="lblCurp" class="alinearHoriz">...</label>'
                                                +'<input type="hidden" id="txtPermiso" value=""/>'
                                                +'<input type="hidden" id="txtCrip" value=""/>'
                                                +'<input type="hidden" id="lblCurp" value=""/>'
                                                +'<input type="hidden" id="lblGrado" value=""/>'
                                                +'<input type="hidden" id="lblGrupo" value=""/>'
                                                +'<input type="hidden" id="lblCct" value=""/>'
                                                +'<input type="hidden" id="lblCiclo" value=""/>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlIdAlu">'
                                                +'<div class="anchoFijo alinearHoriz">'
                                                    +'<input type="radio" id="rbnIdalu" name="rbgIdaluCurp" class="alinearHoriz" value="rbnIdalu" tabindex="301">'
                                                    +'<label id="lblRadioIdalu" class="alinearHoriz">idALU:</label>'
                                                +'</div>'
                                                +'<input type="text" id="txtIdalu" class="alinearHoriz" maxlength="18" tabindex="302"/>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlCurp">'
                                                +'<div class="anchoFijo alinearHoriz">'
                                                    +'<input type="radio" id="rbnCurp" name="rbgIdaluCurp" class="alinearHoriz" value="rbnCurp" tabindex="303">'
                                                    +'<label id="lblRadioCurp" class="alinearHoriz">CURP:</label>'
                                                +'</div>'
                                                +'<input type="text" id="txtCurp16" class="alinearHoriz" maxlength="16" tabindex="304"  placeholder="Mínimo 10 dígitos"/>'
                                                +'<input type="text" id="txtCurp17y18" class="alinearHoriz" maxlength="2" tabindex="305"/>'
                                                + '<ul id="ubtnBuscar" class="buttonBar alinearHoriz">  <li><a href="#" id="btnBuskAlum" tabindex="102" title="Buscar curp"><label class="icon-lupa"></label> Buscar</a></li>  </ul> '
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlApe1">'
                                                +'<div class="anchoFijo alinearHoriz"><label>*Primer Apellido:</label></div>'
                                                +'<input type="text" id="txtApe1" class="alinearHoriz" maxlength="30" tabindex="306"  required />'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlApe2">'
                                                +'<div class="anchoFijo alinearHoriz"><label>Segundo Apellido:</label></div>'
                                                +'<input type="text" id="txtApe2" class="alinearHoriz" maxlength="30" tabindex="307" value=""/>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlNombre">'
                                                +'<div class="anchoFijo alinearHoriz"><label>*Nombre(s):</label></div>'
                                                +'<input type="text" id="txtNombre" class="alinearHoriz" maxlength="40" tabindex="308" value="" required/>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlSexo">'
                                                +'<div class="anchoFijo alinearHoriz"><label>*Género:</label></div>'
                                                +'<select id="cbxSexo" class="alinearHoriz" tabindex="309" required> <option value=""></option> <option value="M">MUJER</option> <option value="H">HOMBRE</option> </select>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlFechaNac">'
                                                +'<div class="anchoFijo alinearHoriz"><label>*Fecha de nacimiento:</label></div>'
                                                +'<input type="text" id="txtFechaNac" class="alinearHoriz" maxlength="10" pattern="(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}" placeholder="dd/mm/aaaa" tabindex="310" required value=""/>'
                                                +'<label id="lblEdad" class="alinearHoriz"></label>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlKrta">'
                                                +'<div class="anchoFijo alinearHoriz"><label>Carta Compromiso:</label></div>'
                                                +'<select id="cbxKrta" class="alinearHoriz" tabindex="311"> <option value="No">NO</option> <option value="Si">SI</option> <option value="SUSTITUIDA">SUSTITUIDA</option> </select>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlEntidad">'
                                                +'<div class="anchoFijo alinearHoriz"><label>*Entidad de Nacimiento:</label></div>'
                                                +'<select id="cbxEntidad" class="alinearHoriz" tabindex="312"  required></select>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlProbem">'
                                                +'<div class="anchoFijo alinearHoriz"><label>Nacionales (PROBEM):</label></div>'
                                                +'<select id="cbxProbem" class="alinearHoriz" tabindex="313"> <option></option> <option>T) NAC PROV DE EUA CON DOC DE TRANSF</option> <option>N) NAC PROV DE EUA SIN DOC DE TRNASF</option> <option>E) NAC PROV DE OTROS PAISES</option> <option>D) ALUMNOS QUE SE DIRIGEN A LOS EUA CON D</option></select>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlExtj">'
                                                +'<div class="anchoFijo alinearHoriz"><label>Alumnos Extranjeros:</label></div>'
                                                +'<select id="cbxExtj" class="alinearHoriz" tabindex="314"> <option></option> <option>1) AFRICA</option> <option>2) ASIA</option> <option>3) CANADA</option> <option>4) CENTROAMERICA Y EL CARIBE</option> <option>5) EUA</option> <option>6) EUROPA</option> <option>7) OCEANIA</option> <option>8) SUDAMERICA</option></select>'
                                            +'</div>');
                $("#pnlDatosAlumno").append('<div id="pnlObservaciones">'
                                                +'<div class="anchoFijo alinearHoriz"><label>Observaciones:</label></div>'
                                                +'<textarea id="txaObservaciones" name="txaObservaciones" class="alinearHoriz" cols=60 rows=5 maxlength="300" tabindex="315" placeholder="Escriba máximo 300 caracteres."></textarea>'
                                            +'</div>');
            
            $("#pnlModifCurp").append('<div id="pnlNormatividad"> <label id="lblRangoEdad">Normatividad...</label> </div>');
            $("#pnlModifCurp").append('<ul id="ubtnActualizarCerrarLimpiar" class="buttonBar">'
                                            +'<li><a href="#" id="btnActualizar" class="alinearHoriz" tabindex="316" ><label class="iconBtnGuardar middleHoriz icon-disquete"></label>Actualizar</a></li>'
                                            +'<li><a href="#" id="btnCerrar" class="alinearHoriz" tabindex="317" >Cerrar</a></li>'
                                            +'<li><a href="#" id="btnLimpiar" class="alinearHoriz" tabindex="318" >Limpiar</a></li>'
                                       +'</ul>');
            //Est área está en sicceb, pero en SICEEO se omitirá, pues la información ya es obsoleta
            /*$("#pnlModifCurp").append('<div id="pnlRenapo"></div>');
                $("#pnlRenapo").append('<div id="pnlRenapoIzq" class="alinearHoriz">'
                                            +'<div class="ext"> <div class="anchoFijo2 alinearHoriz"><label>Información de renapo:</label></div> <label id="lblInfoRenapo"></label> </div>'
                                            +'<div class="ext"> <div class="anchoFijo2 alinearHoriz"><label>Nombre diferente:</label></div> <label id="lblNomDif"></label> </div>'
                                            +'<div class="ext"> <div class="anchoFijo2 alinearHoriz"><label>Estatus:</label></div> <label id="lblEstatus"></label> </div>'
                                        +'</div>');
                $("#pnlRenapo").append('<div id="pnlRenapoDer" class="alinearHoriz">'
                                            +'<div class="ext"> <div class="anchoFijo3 alinearHoriz"><label>Certificado por el Registro Civil:</label></div> <label id="lblCertRegCiv"></label> </div>'
                                            +'<div class="ext"> <div class="anchoFijo3 alinearHoriz"><label>Fecha de nacimiento:</label></div> <label id="lblFechaNacRenapo"></label> </div>'
                                            +'<div class="ext"> <div class="anchoFijo3 alinearHoriz"><label>CRIP:</label></div> <label id="lblCrip"></label> </div>'
                                        +'</div>');*/
                
        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar_ModifCurp").on("click",function(){ frmwModifCurp_Close(); });
    
    $("#rbnCurp").on("click", function(){ rbnCurp_ModifCurp_Click (); });
    $("#rbnIdalu").on("click", function(){ rbnIdalu_ModifCurp_Click (); });
    
    $("#txtIdalu").on("focus", function(){ txtIdalu_ModifCurp_Focus (); });
    $("#txtCurp16").on("focus", function(){ txtCurp16_ModifCurp_Focus (); });
    $("#txtCurp17y18").on("focus", function(){ txtCurp17y18_ModifCurp_Focus (); });
    $("#lblRadioIdalu").on("click", function(){ lblRadioIdalu_ModifCurp_Click (); });
    $("#lblRadioCurp").on("click", function(){ lblRadioCurp_ModifCurp_Click (); });
    $("#txtIdalu").on("keydown", function(e){ txtIdalu_ModifCurp_KeyDown (e); });
    $("#txtCurp16").on("keydown", function(e){ txtCurp_ModifCurp_KeyDown (e); });
    $("#txtCurp17y18").on("keydown", function(e){ txtCurp17y18_KeyDown (e); });
    
    $("#btnBuskAlum").on("keydown", function(e){ if(e.which === 13){ btnBuskAlum_ModifCurp_ActionPerformed (); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnBuskAlum').on("click",function(e){ btnBuskAlum_ModifCurp_ActionPerformed (); });
    $("#txtApe1").on("blur",function(){ verCurp_ModifCurp(); });
    $("#txtApe2").on("blur",function(){ verCurp_ModifCurp(); });
    $("#txtNombre").on("blur",function(){ verCurp_ModifCurp(); });
    $("#cbxSexo").on("blur",function(){ verCurp_ModifCurp(); });
    $("#txtFechaNac").on("blur", function(e){ txtFechaNac_ModifCurp_Exit(); });
    $("#cbxEntidad").on("blur",function(){ verCurp_ModifCurp(); });
    
    $("#btnActualizar").on("keydown", function(e){ if(e.which === 13)  { btnActualizar_ModifCurp_ActionPerformed ();  e.preventDefault(); } });   //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnActualizar").on("click", function(e){ btnActualizar_ModifCurp_ActionPerformed (); /*e.preventDefault();*/ });
    $("#btnCerrar").on("keydown", function(e){ if(e.which === 13){  frmwModifCurp_Close(); e.preventDefault();} });  //El preventDefault es porque en el método que manda a llmar, cuando toca mostrar un alert, se manda a llamar nuevamente dicho método a travéz del evento click
    $('#btnCerrar').on("click",function(){ frmwModifCurp_Close(); });
    $("#btnLimpiar").on("keydown", function(e){ btnLimpiar_ModifCurp_ActionPerformed (e);  /*e.preventDefault();*/ } );
    $("#btnLimpiar").on("click", function(){ btnLimpiar_ModifCurp_ActionPerformed ();  });
    $('#btnRegresar').on("click",function(){ jsModifCurp=null; irAVentanaPrincipal(); });
    
    
}

function frmwModifCurp_FormActivate (kienLlamas, idaluX)
{
    var mensaje = new Mensajes();
    
    boton_setVisible(false,"btnLimpiar");
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"MoCu", metodo:"foAc", kienLlamas:kienLlamas, idaluX:idaluX, cicescini:jsModifCurp.cicescini, tblPrincipal_idcct: jsModifCurp.tblPrincipal_idcct
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
                // Cargamos las entidades
                //$("#cbxEntidad").append("<option value=''  selected></option>");
                $.each(result.estados,function(i,valor) {
                       $("#cbxEntidad").append("<option value='"+i+"'>"+valor+"</option>");                        
                });
                
                jsModifCurp.alumEstatus=result.alumEstatus;
                btnLimpiar_ModifCurp_ActionPerformed ();
                
                if ( kienLlamas === "DESDEBAJAS" || kienLlamas==="DESDECALIF" )
                {
                    $("#txtIdalu").val(result.txtIdalu_Text);
                    document.getElementById('rbnIdalu').checked = true;

                    cerrarLoading();
                    btnBuskAlum_ModifCurp_ActionPerformed();

                    boton_setVisible(result.btnBuskAlum_Visible,'btnBuskAlum');
                    object_setEnabled(!result.txtCurp16_ReadOnly,"txtCurp16");
                    object_setEnabled(!result.txtIdalu_ReadOnly,"txtIdalu");
                    
                    document.getElementById('txtIdalu').focus();
                    
                }else
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
            case -11: //Mostrar mensaje y salirse del formulario
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
                    frmwModifCurp_Close();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function rbnCurp_ModifCurp_Click ()
{
    document.getElementById('txtCurp16').focus();
}

function rbnIdalu_ModifCurp_Click ()
{
    document.getElementById('txtIdalu').focus();
}

function lblRadioCurp_ModifCurp_Click ()
{
    document.getElementById('txtCurp16').focus();
    document.getElementById('rbnCurp').checked = true;
}

function lblRadioIdalu_ModifCurp_Click ()
{
    document.getElementById('txtIdalu').focus();
    document.getElementById('rbnIdalu').checked = true;
}

function txtCurp16_ModifCurp_Focus ()
{
    document.getElementById('rbnCurp').checked = true;
}

function txtCurp17y18_ModifCurp_Focus ()
{
    document.getElementById('rbnCurp').checked = true;
}

function txtIdalu_ModifCurp_Focus ()
{
    document.getElementById('rbnIdalu').checked = true;
}

function txtIdalu_ModifCurp_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
        btnBuskAlum_ModifCurp_ActionPerformed ();
}
function txtCurp_ModifCurp_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
        btnBuskAlum_ModifCurp_ActionPerformed ();
}

function txtCurp17y18_KeyDown (e)
{
    if(e.which === 13)                                                          //Si oprimió enter
        btnBuskAlum_ModifCurp_ActionPerformed ();
}

function btnBuskAlum_ModifCurp_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"MoCu", metodo:"btBuAl_Cl", cicescini:jsModifCurp.cicescini, txtPermiso:$("#txtPermiso").val(), unidad: sisVars.unidad, 
        rbnCurp_isChecked:document.getElementById('rbnCurp').checked, rbnIdalu_isChecked:document.getElementById('rbnIdalu').checked,
        txtIdalu:$("#txtIdalu").val(), txtCurp16:$("#txtCurp16").val(), txtCurp17y18:$("#txtCurp17y18").val(), 
        tblPrincipal_modalidad:jsModifCurp.tblPrincipal_modalidad, tblPrincipal_grado:jsModifCurp.tblPrincipal_grado
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
                    jsModifCurp.laU = result.laU;
                    jsModifCurp.enCicloAnterior  = result.enCicloAnterior;
                    jsModifCurp.alumEstatus = result.alumEstatus;
                    jsModifCurp.maxCicEscIni = result.maxCicEscIni;
                    jsModifCurp.edadMin = result.edadMin;
                    jsModifCurp.edadMax = result.edadMax;
                    jsModifCurp.idalu = result.idalu;
                    jsModifCurp.modifCurp = result.modifCurp;
                    jsModifCurp.gdoTerminal = result.gdoTerminal;
                    jsModifCurp.regCivCertAnt = result.regCivCertAnt;
                    jsModifCurp.validaAnt = result.validaAnt;
                    jsModifCurp.curp_ant = result.curp_ant;
                    jsModifCurp.apepat_ant = result.apepat_ant;
                    jsModifCurp.apemat_ant = result.apemat_ant;
                    jsModifCurp.nombre_ant = result.nombre_ant;
                    
                    $("#lblCurp").text(result.lblCurp_Text);
                    $("#lblGrado").text(result.lblGrado_Text);
                    $("#lblGrupo").text(result.lblGrupo_Text);
                    $("#lblCct").text(result.lblCct_Text);
                    $("#lblCiclo").text(result.lblCiclo_Text);
                    object_setVisible(result.lblRangoEdad_Visible,"lblRangoEdad");
                    $("#lblRangoEdad").text(result.lblRangoEdad);
                    
                    if (typeof (result.txtCurp17y18_Text) !== "undefined")
                        $("#txtCurp17y18").val(result.txtCurp17y18_Text);
                    if (typeof (result.lblDifeNom_Visible) !== "undefined")
                        object_setVisible(result.lblDifeNom_Visible,"lblDifeNom");
                    if (typeof (result.lblRenapo_Visible) !== "undefined")
                        object_setVisible(result.lblRenapo_Visible,"lblRenapo");
                    if (typeof (result.txtNombre_ReadOnly) !== "undefined")
                    {
                        object_setEnabled(!result.txtCurp17y18_ReadOnly,"txtCurp17y18");
                        object_setEnabled(!result.txtApe1_ReadOnly,"txtApe1");
                        object_setEnabled(!result.txtApe2_ReadOnly,"txtApe2");
                        object_setEnabled(!result.txtNombre_ReadOnly,"txtNombre");
                        object_setEnabled(!result.cbxSexo_ReadOnly,"cbxSexo");
                        object_setEnabled(!result.txtFechaNac_ReadOnly,"txtFechaNac");
                        object_setEnabled(result.cbxEntidad_Enabled,"cbxEntidad");
                        boton_setEnabled(result.btnGuardar_Enabled,"btnGuardar");
                    }
                    
                    $("#txtIdalu").val(result.txtIdalu_Text);
                    $("#txtApe1").val(result.txtApe1_Text);
                    $("#txtApe2").val(result.txtApe2_Text);
                    $("#txtNombre").val(result.txtNombre_Text);
                    $("#cbxSexo").val(result.cbxSexo_SelectedItem);
                    $("#txtFechaNac").val(result.txtFechaNac_Text);
                    $("#txtCrip").val(result.txtCrip_Text);
                    $("#cbxEntidad").val(result.cbxEntidad_SelectedItem);
                    if (typeof (result.cbxEntidad_SelectedIndex) !== "undefined")
                        document.getElementById('cbxEntidad').selectedIndex = result.cbxEntidad_SelectedIndex;
                    $("#cbxProbem").val(result.cbxProbem_SelectedItem);
                    $("#cbxExtj").val(result.cbxExtj_SelectedItem);
                    $("#cbxKrta").val(result.cbxKrta_SelectedItem);
                    $("#txaObservaciones").val(result.txaObservaciones);
                    
                    txtFechaNac_ModifCurp_Exit();
                    if ( $("#txtCurp17y18").val().length !== 2 )
                        document.getElementById('txtCurp17y18').focus();
                    else
                        document.getElementById('txtApe1').focus();
                    
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

function txtFechaNac_ModifCurp_Exit ()
{
    var mensaje = new Mensajes();
    try{
        verCurp_ModifCurp();
    //    FechaNac:=StrToDate(E_Edad.text);
        var fechaNac = new Date( $("#txtFechaNac").val().substring(6,10), parseInt($("#txtFechaNac").val().substring(3,5))-1, $("#txtFechaNac").val().substring(0,2) );
        if (fechaNac.toString() === "Invalid Date")
            throw "Fecha inválida";
        
        var anioNac = fechaNac.getFullYear();                                                                                                  //OJO: Enero empieza en 0, Diciembre es 11
        var anioAct = jsModifCurp.cicescini;
        
        var iTemp = anioAct;
        var iTemp2 = anioNac;

        if ( anioAct < anioNac )
            $('#lblEdad').text("Edad: "+(iTemp-iTemp2-1)+" años en el "+jsModifCurp.cicescini);
        else 
            $('#lblEdad').text("Edad: "+(iTemp-iTemp2)+" años en el "+jsModifCurp.cicescini);

        if ( ((iTemp-iTemp2)< jsModifCurp.edadMin) || ((iTemp-iTemp2)> jsModifCurp.edadMax) )
            boton_setEnabled(false,"btnActualizar");
        else
            boton_setEnabled(true,"btnActualizar", "click", btnActualizar_ModifCurp_ActionPerformed, "keydown",function(e){ if(e.which === 13)  { btnActualizar_ModifCurp_ActionPerformed ();  e.preventDefault(); } } );

        if (jsModifCurp.tblPrincipal_cveplan==="2")
        {
            //26 de noviembre 2016 si en el 2016 cumple 15 o mas ya no entra,
            //                     pero si antes de agosto tiene 14 debe entrar
            
            //31 de enero 2017 si en el 2016 cumple 14 entra,
//                     pero si despues del 1ro agosto ya cumple 15 no debe entrar
            if ( (jsModifCurp.tblPrincipal_modalidad==="DES" || jsModifCurp.tblPrincipal_modalidad==="PES" || jsModifCurp.tblPrincipal_modalidad==="DST" || jsModifCurp.tblPrincipal_modalidad==="PST" ) && jsModifCurp.tblPrincipal_grado==="1" && (iTemp-iTemp2)===parseInt(jsModifCurp.edadMax) && parseInt($("#txtFechaNac").val().substring(3,5))<8 )
                boton_setEnabled(false,"btnActualizar");

            if ( jsModifCurp.tblPrincipal_modalidad==="DTV" && jsModifCurp.tblPrincipal_grado==="1" && (iTemp-iTemp2)===parseInt(jsModifCurp.edadMax) && parseInt($("#txtFechaNac").val().substring(3,5))<8 )
                boton_setEnabled(false,"btnActualizar");
        }
//--------------------------------------------------------------------


        if ( (jsModifCurp.tblPrincipal_modalidad==="DBA" ||  jsModifCurp.tblPrincipal_modalidad==="HMC") && ((iTemp-iTemp2)>=13  && (iTemp-iTemp2)<100) )               //lo k cumpla con la edad minima
            boton_setEnabled(true,"btnActualizar", "click", btnActualizar_ModifCurp_ActionPerformed, "keydown",function(e){ if(e.which === 13)  { btnActualizar_ModifCurp_ActionPerformed ();  e.preventDefault(); } } );
        
        if ( jsModifCurp.tblPrincipal_modalidad==="DML"  && (iTemp-iTemp2)>= jsModifCurp.edadMin)                            //lo k cumpla con la edad minima
            boton_setEnabled(true,"btnActualizar", "click", btnActualizar_ModifCurp_ActionPerformed, "keydown",function(e){ if(e.which === 13)  { btnActualizar_ModifCurp_ActionPerformed ();  e.preventDefault(); } } );
        
        if ( $("#txtCurp16").val().substring(4,10) === $("#lblCurp").text().substring(4,10))
            boton_setEnabled(true,"btnActualizar", "click", btnActualizar_ModifCurp_ActionPerformed, "keydown",function(e){ if(e.which === 13)  { btnActualizar_ModifCurp_ActionPerformed ();  e.preventDefault(); } } );
            
    }catch (e) {
        boton_setEnabled(false,"btnActualizar");
        mensaje.ModifiCurp("FECHA_INVALIDA");
        document.getElementById('txtFechaNac').value="";
        //$('#txtFechaNac').focus();
    }
}

function btnActualizar_ModifCurp_ActionPerformed ()
{
    var mensaje = new Mensajes();
    
    if ( validarEntradas_ModifCurp() )
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"MoCu", metodo:"btGu_AcPe", laU:jsModifCurp.laU, cicescin:jsModifCurp.cicescin, cicescini:jsModifCurp.cicescini, edadMin:jsModifCurp.edadMin, 
            edadMax:jsModifCurp.edadMax, enCicloAnterior:jsModifCurp.enCicloAnterior, gdoTerminal:jsModifCurp.gdoTerminal, 
            tblPrincipal_idcct:jsModifCurp.tblPrincipal_idcct, tblPrincipal_grado:jsModifCurp.tblPrincipal_grado, tblPrincipal_grupo:jsModifCurp.tblPrincipal_grupo, 
            modifCurp:jsModifCurp.modifCurp, txtPermiso:"", kienLlamas:jsModifCurp.kienLlamas, alumEstatus:jsModifCurp.alumEstatus, curp_ant:jsModifCurp.curp_ant,
            nombre_ant:jsModifCurp.nombre_ant, apepat_ant:jsModifCurp.apepat_ant, apemat_ant:jsModifCurp.apemat_ant, regCivCert_ant:jsModifCurp.regCivCertAnt, 
            valida_ant:jsModifCurp.validaAnt, idalu:jsModifCurp.idalu, lblCurp:$('#lblCurp').text(), txtCurp16:$('#txtCurp16').val().trim().toUpperCase(), 
            txtCurp17y18:$('#txtCurp17y18').val().trim().toUpperCase(), txtCrip:$('#txtCrip').val().trim().toUpperCase(), lblCct:$('#lblCct').text(), 
            txtNombre:$('#txtNombre').val().trim().toUpperCase(), txtApe1:$('#txtApe1').val().trim().toUpperCase(), txtApe2:$('#txtApe2').val().trim().toUpperCase(), 
            cbxSexo:$("#cbxSexo").val(), txtFechaNac:$("#txtFechaNac").val().trim(), cbxKrta:$("#cbxKrta").val(), cbxEntidad:$('#cbxEntidad option:selected').text().trim(), 
            cbxProbem:$('#cbxProbem option:selected').text(), cbxExtj:$('#cbxExtj option:selected').text(),txaObservaciones:$("#txaObservaciones").val().trim()
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
                    jsModifCurp.idalu = result.idalu;
                    jsModifCurp.alumEstatus = result.alumEstatus;
                    jsModifCurp.laU = result.laU;

                    $("#txtCrip").val(result.txtCrip_Text);
                    $("#lblCurp").text(result.lblCurp_Text);
                    $("#lblEdad").text(result.lblEdad_Text);
                    if ( typeof(result.txtCurp16_Text) !== "undefined" ) {
                        $("#txtCurp16").val(result.txtCurp16_Text);
                        $("#txtCurp17y18").val(result.txtCurp17y18_Text);
                    }
                    $("#txtNombre").val(result.txtNombre_Text);
                    $("#txtApe1").val(result.txtApe1_Text);
                    $("#txtApe2").val(result.txtApe2_Text);
                    $("#txtFechaNac").val(result.txtFechaNac_Text);
                    $("#cbxSexo").val(result.cbxSexo_SelectedItem);
                    
                    if (jsModifCurp.nameParentForm === "frmwCamDeGpo"){
                        var tabla = new Tabla();
                        var posSelActual = tabla.getSelectedIndexRow ('tblCamDeGpo');
                        tabla.updateRow("tblCamDeGpo", posSelActual, {nom_tot:result.nombreActualizado, curp:result.curpActualizada} );
                    }
                    
                    if ( typeof(result.mensaje) !== "undefined" )
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");

                    cerrarLoading();

                    if ( typeof(result.WINDOW_CLOSE) !== "undefined" )
                        frmwModifCurp_Close();

                    if ( typeof(result.btnBuskAlum_Click) !== "undefined" )
                        btnBuskAlum_ModifCurp_ActionPerformed ();

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
}

function btnLimpiar_ModifCurp_ActionPerformed ()
{
    //-------------------- Limpiamos todos los componentes --------------------
    $('#pnlModifCurp :text').val("");
    $('#txtCrip').val("");
    object_setEnabled(true,"txtCurp17y18");                                     // PARA K PUEDAS OCUPARLO
    $('#lblEdad').text("Edad: ");
    $('#lblCurp').text("");
    document.getElementById('txtCurp16').focus();
}

function verCurp_ModifCurp ()
{
    var fecha="",anioNac="",diaNac="",mesNac="";
    
    document.getElementById('txtCurp16').value = "";
    if( $("#txtFechaNac").val() !== "")
    {
        fecha = (""+$("#txtFechaNac").val()).split('/');
        anioNac = fecha[2].substring(2,4);
        mesNac = fecha[1];
        diaNac = fecha[0];
    }
    
    //entidad = document.getElementById('cbxEntidad').value;
    var entidad = $("#cbxEntidad option:selected").text().trim();
    entidad = entidad.substring( parseInt(entidad.length) - 2, entidad.length );
    
    var validarCurp = new ValidarCurp($("#txtNombre").val(), $("#txtApe1").val(), $("#txtApe2").val(),anioNac,mesNac,diaNac,$("#cbxSexo").val(),entidad);
    var curp = validarCurp.curp();
    document.getElementById('txtCurp16').value = curp;
}

function validarEntradas_ModifCurp ()
{
    var mensaje = new Mensajes ();
    
    if ($("#txtApe1").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","el Primer apellido");    $("#txtApe1").focus();  return false;
    }if ($("#txtNombre").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","el Nombre");    $("#txtNombre").focus();  return false;
    }if ($("#cbxSexo").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","el Género");    $("#cbxSexo").focus();  return false;
    }if ($("#txtFechaNac").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","la Fecha de nacimiento");    $("#txtFechaNac").focus();  return false;
    }if ($("#cbxEntidad").val().trim() === ""){
        mensaje.General ("CAMPO_VACIO","la Entidad");    $("#cbxEntidad").focus();  return false;
    }
    return true;
}


