/* 
    Creado el : 29/04/2015, 08:47:10 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsNvoIngreso;

function NuevoIngreso_Show(tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan, tblPrincipal_cveprograma, tblPrincipal_cveturno, tblPrincipal_modalidad, tblPrincipal_cicescini)
{
    jsNvoIngreso = {
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_cct: tblPrincipal_cct,
        tblPrincipal_grado: tblPrincipal_grado,
        tblPrincipal_grupo: tblPrincipal_grupo,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_cveprograma: tblPrincipal_cveprograma,
        tblPrincipal_cveturno: tblPrincipal_cveturno,
        tblPrincipal_modalidad: tblPrincipal_modalidad,
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        
        edadMin: null,
        edadMax:null,
        entidades: null,
        necesidadesEspeciales: null,
        lblRangoEdad: null,
        
        datosAlu: null,
        alumEstatus: null,
        talleres: null, 
        artes: null,
        idalu: null,
        QBuskAlum_grado:"0",
        QBuskAlum_maxCicEscIni:"0"
    };
        
    object_setVisible (false,"gridTable");
    $("#frmwNuevoIngreso").css("display", "block"); // Mostramos el formulario correspondiente
    
    NuevoIngreso_FormActivate ();
}

function NuevoIngreso_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    var datos = {
        modulo:"nuIn", metodo:"foAc", tblPrincipal_grado:jsNvoIngreso.tblPrincipal_grado, tblPrincipal_grupo:jsNvoIngreso.tblPrincipal_grupo,
        tblPrincipal_modalidad:jsNvoIngreso.tblPrincipal_modalidad, tblPrincipal_idcct:jsNvoIngreso.tblPrincipal_idcct, 
        tblPrincipal_cveplan:jsNvoIngreso.tblPrincipal_cveplan, tblPrincipal_cicescini:jsNvoIngreso.tblPrincipal_cicescini, 
        tblPrincipal_cicescfin:parseInt(jsNvoIngreso.tblPrincipal_cicescini)+1
    };
    cargarLoading();
    $.ajax({
        url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    jsNvoIngreso.tblPrincipal_cveprograma = result.cveprograma;                                                            //Preescolar y secundaria
                    jsNvoIngreso.alumEstatus=result.alumEstatus;
                    jsNvoIngreso.lblRangoEdad = result.lblRangoEdad;
                    jsNvoIngreso.necesidadesEspeciales = result.discap;
                    jsNvoIngreso.edadMin = result.edadadmmin;
                    jsNvoIngreso.edadMax = result.edadadmmax;
                    jsNvoIngreso.entidades = result.estados;
                    jsNvoIngreso.lenguas = result.lenguas;
                    jsNvoIngreso.artes = result.artes;                                                                                    //Solo secundaria
                    jsNvoIngreso.talleres = result.talleres;                                                                             //Solo secundaria

                    switch(jsNvoIngreso.tblPrincipal_cveplan) {
                        case '1': NuevoIngresoPri_Create(jsNvoIngreso.tblPrincipal_cct, jsNvoIngreso.tblPrincipal_grado, jsNvoIngreso.tblPrincipal_grupo);
                    break;
                        case '2': NuevoIngresoSec_Create(jsNvoIngreso.tblPrincipal_cct, jsNvoIngreso.tblPrincipal_grado, jsNvoIngreso.tblPrincipal_grupo);
                    break;
                        case '3': NuevoIngresoPre_Create(jsNvoIngreso.tblPrincipal_cct, jsNvoIngreso.tblPrincipal_grado, jsNvoIngreso.tblPrincipal_grupo);
                    break;
                    default:break;
                }
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
            case -11: //Mostrar mensaje y salirse del formulario
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
                    jsNvoIngreso=null; 
                    irAVentanaPrincipal();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function buscarAlumno(txtApe1, txtApe2, txtNombre, grado, cicescini, cveplan, txtCurp, txt18, datosAlumno)
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    jsNvoIngreso.alumEstatus = "X";
    var datos = {
            modulo:"nuIn", metodo:"buAl", numLlamada:"0", existeUno:"AquiNoAplica", primerApe:txtApe1.trim().toUpperCase(), segundoApe:txtApe2.trim().toUpperCase(), 
            nombre:txtNombre.trim().toUpperCase(), tblPrincipal_grado:grado, cicescini:cicescini, tblPrincipal_cveplan:cveplan, txtCurp: txtCurp.trim(), 
            txt18:txt18.trim(), datosAlumno: datosAlumno, usuario:sisVars.usuario, cveunidad:sisVars.unidad, cct:jsNvoIngreso.tblPrincipal_cct
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
                        if (typeof result.mensaje!=="undefined")
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        jsNvoIngreso.idalu=result.idalu;
                        jsNvoIngreso.alumEstatus = result.alumEstatus;
                        if(result.alumEstatus==="O")
                            setDatosNuevoIngreso(result.curp,result.txtApe1,result.txtApe2,result.txtNombre,result.cbxSexo,result.txtEdad,result.cbxEntidad_SelectedIndex,result.idalu,jsNvoIngreso.tblPrincipal_cveplan,result.cbxOtrasLenguas,result.cbxEtnia);
                        else if(result.alumEstatus==="X")
                            $("#txtCurp").focus();
                        else if(result.alumEstatus==="I")
                            $("#btnGuardar").focus();
                        jsNvoIngreso.QBuskAlum_grado=result.QBuskAlum_grado;                                              //Sólo para secundaria
                        jsNvoIngreso.QBuskAlum_maxCicEscIni=result.QBuskAlum_maxcicescini;                                //Sólo para secundaria
                        cerrarLoading();
                    break;
                case 2: //Ventana MasDeUno (Sólo lo usa primaria)
                        cerrarLoading();
                        jsNvoIngreso.alumEstatus = result.alumEstatus;
                        jsNvoIngreso.idalu = result.idalu;
                        if (result.btnLimpiar_Click)
                            btnLimpiar_Pre_ActionPerformed ();
                        if (result.casoRequerido === "winMasDeUno"){
                            datos.QBuskAlum=result.QBuskAlum;
                            frmwMasDeUno_Show ("frmfPrimaria", result.tblCurpsRep, datos, elegirCurpRep_Pri);
                        }
                        cerrarLoading();
                    break; 
                case 10: //Ventana Ajustar
                        jsNvoIngreso.alumEstatus = result.alumEstatus;
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        cerrarLoading();
                        if (result.btnLimpiar_Click){
                            if (jsNvoIngreso.tblPrincipal_cveplan==="1") btnLimpiar_Pri_ActionPerformed();
                            else if (jsNvoIngreso.tblPrincipal_cveplan==="2") btnLimpiar_Sec_ActionPerformed();
                        }
                        
                        var formularioQueLlama;
                        if (jsNvoIngreso.tblPrincipal_cveplan==="2") formularioQueLlama = "frmfSecundaria";
                        else if (jsNvoIngreso.tblPrincipal_cveplan==="1") formularioQueLlama = "frmfPrimaria";
                        
                        if (result.casoRequerido === "winAjustar")
                            frmwAjustar_Show ( formularioQueLlama, result.kienLlamas, result.idaluX, result.curpD10 );
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        $("#txtCurp").focus();
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

function setDatosNuevoIngreso(curp, txtApe1, txtApe2, txtNombre, cbxSexo, txtFechaNac, cbxEntidad_SelectedIndex, idalu, cveplan, cbxOtrasLenguas, cbxEtnia) 
{
    var camposADeshabilitar = new Array("txtCurp","txtApe1","txtApe2","txtNombre","cbxSexo","txtEdad","cbxEntidad");
    //cambioDeForm();
    $("#txtCurp").val(curp.substring(0,16));
    $("#txt18").val(curp.substring(16,18));    
    $("#txtApe1").val(txtApe1);
    $("#txtApe2").val(txtApe2);
    $("#txtNombre").val(txtNombre);
    $("#cbxSexo").val(cbxSexo);
    $("#txtEdad").val(txtFechaNac);
    $("#lblIdalu").text(idalu);
    $("#cbxOtrasLenguas").val(cbxOtrasLenguas);
    $("#cbxEtnia").val(cbxEtnia);
    var indice =  parseInt(cbxEntidad_SelectedIndex);
    if(indice===34)
        $("#cbxEntidad").val(indice-1);
    else  $("#cbxEntidad").val(indice);

    switch (cveplan){
        case "1": txtEdad_Pri_Exit (); break;
        case "2": txtEdad_Sec_Exit (); break;
        case "3": txtEdad_Pre_Exit (); break;
    }
    
   for(var i=0; i<8;i++)
        $("#"+camposADeshabilitar[i]).attr("disabled","disabled");
    
    if($('#frmfSecundaria').length && $('#cbxArtes').attr("value"))
        $("#btnGuardar").focus();
    else if($('#frmfPrimaria').length)
        $("#btnGuardar").focus();
    else $("#cbxKrta").focus();
}

function verCurp()
{    
    var fecha="",anioNac="",diaNac="",mesNac="";
    
    document.getElementById('txtCurp').value = "";
    if( $("#txtEdad").val() !== "")
    {
        fecha = (""+$("#txtEdad").val()).split('/');
        anioNac = fecha[0].substring(2,4);
        mesNac = fecha[1];
        diaNac = fecha[2];
    }
    
    //entidad = document.getElementById('cbxEntidad').value;
    var entidad = $("#cbxEntidad option:selected").text().trim();
    entidad = entidad.substring( parseInt(entidad.length) - 2, entidad.length );
    
    var validarCurp = new ValidarCurp($("#txtNombre").val(), $("#txtApe1").val(), $("#txtApe2").val(),anioNac,mesNac,diaNac,$("#cbxSexo").val(),entidad);
    document.getElementById('txtCurp').value = validarCurp.curp();
}
