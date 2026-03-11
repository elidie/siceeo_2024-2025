/* 
    Creado el : 6/03/2015, 08:43:06 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/
var bimMax=0, bimMin=0;

function frmwXBimOPromFin_Show ()
{
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwXBimOPromFin").css("display", "block");                             // Mostramos el formulario correspondiente    
    frmwXBimOPromFin_Create();
    frmwXBimOPromFin_FormActivate ();    
}

function frmwXBimOPromFin_Close()
{
    /*if($("#frmfXBimOPromFin").length)
        $("#frmfXBimOPromFin").remove();
    object_setVisible (false,'frmwXBimOPromFin');
    object_setVisible (true,'gridTable');                                             // Mostramos el gridTable
    */
    irAVentanaPrincipal();
}

function frmwXBimOPromFin_Create ()
{
    var tabla = new Tabla();
    var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","nombre","grado","grupo","cveplan","cicescini"], null, "JSON");
    
    if($('#frmfXBimOPromFin').length)                                           // Verificando si la tabla Existe
        $('#frmfXBimOPromFin').remove();
    $('#frmwXBimOPromFin').append('<fieldset id="frmfXBimOPromFin"><legend>Tipo de captura</legend><div id="btnRegresar_XBimOPromFin" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfXBimOPromFin").append('<div id="pnlXBimOPromFin"></div>');
        
        $('#pnlXBimOPromFin').append('<div id="pnlCapturarCalifs" class="panel"><div class="tituloPanel">Captura de evaluaciones</div></div>');
            $('#pnlCapturarCalifs').append('<div id="pnlControlesYRecomendaciones"></div>');
                $('#pnlControlesYRecomendaciones').append('<div id="pnlControles" class="alinearHoriz"></div>');
                    $('#pnlControles').append('<label id="lblXBim"> <input type="radio" id="rbnXBim" name="rbgXBimOPromOVal" value="rbnXBim" checked>Captura del periodo <select id="cbxBim" name="cbxBim" ></select></label>');
                    $('#pnlControles').append('<label id="lblYCalcProm"> <input type="checkbox" id="chkCalProm" name="chkCalProm" value="calcularPromedios">y Calcular promedios</label>');
                    $('#pnlControles').append('<label id="lblXProm"><input type="radio" id="rbnXProm" name="rbgXBimOPromOVal" value="rbnXProm" >Captura del Promedio Final</label>');
                    $('#pnlControles').append('<label id="lblValidacion"><input type="radio" id="rbnValidacion" name="rbgXBimOPromOVal" value="rbnValidacion" >Validación</label>');
                    $('#pnlControles').append('<ul class="buttonBar"> <li><a href="#" id="btnXBim">Continuar</a></li>  </ul>');
                    $('#pnlControles').append('<ul class="buttonBar"> <li><a href="#" id="btnAvance">Avance de captura</a></li>  </ul>');
                $('#pnlControlesYRecomendaciones').append('<div id="pnlRecomendacion" class="alinearHoriz"></div>');
                    if(tblPrincipal.cveplan==="1" && tblPrincipal.grado==="6") {             
                        $('#pnlRecomendacion').append('<div id="pnlmsgTerm"><label id="lblRecomendacion" style="color:red">Recomendación:\n\nVerificar\nHISTORIAL académico\n\nantes de oficializar.</label></div>');                                    
                        $('#pnlmsgTerm').append('<div id="btnImpRepIns" class="singleButton">'
                                                +'<div class="globoayuda_pointdown"><div class="btnCerrarGlobo">X</div>VERIFIQUE AQUÍ<BR>Promedios de grado</div>'
                                                +'<div id="btnCapDatosRepEv_obj1">'
                                                    +'<label id="icnCapDatosRepEv" class="icon-documento alinearHoriz"></label>'
                                                    +'<div id="btnCapDatosRepEv_obj2" class="alinearHoriz">'
                                                        +'<label id="btnCapDatosRepEv_obj3">Verificar</label>'
                                                        +'<label id="btnCapDatosRepEv_obj4">Historial académico</label>'
                                                    +'</div>'
                                                +'</div>' 
                                                +'<div id="btnCapDatosRepEv_obj5"><label id="btnCapDatosRepEv_obj6">'+tblPrincipal.grado+" "+tblPrincipal.grupo+'</label></div>'
                                            +'</div>');
                    }  
                    else 
                        $('#pnlRecomendacion').append('<div id="pnlmsgInter"><label id="lblRecomendacion">Recomendación:\n\nActivar\n "y CALCULAR PROMEDIOS"\n\n sólo cuando esté ingresando las calificaciones del último bloque</label></div>');
                    
                    
                    
        
        
        $('#pnlXBimOPromFin').append('<div id="pnlCapturarRepsEval" class="panel"><div class="tituloPanel">Captura de boletas de evaluación.</div></div>');
            $('#pnlCapturarRepsEval').append('<label id="lblInstruccionesDatosComp">Capture las observaciones o recomendaciones generales del docente.</label>');
            $('#pnlCapturarRepsEval').append('<div id="btnCapDatosRepEv" class="singleButton">'
                                                +'<div class="globoayuda_pointdown"><div class="btnCerrarGlobo">X</div>CAPTURE AQUÍ<BR>Boletas de Evaluación</div>'
                                                +'<div id="btnCapDatosRepEv_obj1">'
                                                    +'<label id="icnCapDatosRepEv" class="icon-documento alinearHoriz"></label>'
                                                    +'<div id="btnCapDatosRepEv_obj2" class="alinearHoriz">'
                                                        +'<label id="btnCapDatosRepEv_obj3">Capturar</label>'
                                                        +'<label id="btnCapDatosRepEv_obj4">Observaciones</label>'  
                                                        +'<label id="btnCapDatosRepEv_obj4">y/o recomendaciones</label>' 
                                                    +'</div>'
                                                +'</div>'
                                                //+'<div id="btnCapDatosRepEv_obj5"><label id="btnCapDatosRepEv_obj6">generales del Docente.</label></div>'
                                            +'</div>');
        
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );   

    
    if (sisVars.botonesDeCalif) {        
        for (var boton in sisVars.botonesDeCalif) {   
            if(sisVars.botonesDeCalif[boton]==="btnTrim1"){
                $('#cbxBim').append('<option value="1">1</option>'); 
                bimMin = (bimMin===0 ? 1: bimMin);
                bimMax = (bimMax===0 ? 1: bimMax);
            }else if (sisVars.botonesDeCalif[boton]==="btnTrim2"){
                $('#cbxBim').append('<option value="2">2</option>'); 
                bimMin = (bimMin===0 || bimMin > 2 ? 2: bimMin);
                bimMax = (bimMax===0 || bimMax < 2 ? 2: bimMax);
            }else if (sisVars.botonesDeCalif[boton]==="btnTrim3"){
                $('#cbxBim').append('<option value="3">3</option>');  
                bimMin = (bimMin===0 || bimMin > 3 ? 3: bimMin);
                bimMax = (bimMax===0 || bimMax < 3 ? 3: bimMax);
            }              
        }    
    } 
    /*for (var i=1; i<=2; i++)  //Se cambio el 3 por el 1 mientras
        $('#cbxBim').append('<option value="'+i+'">'+i+'</option>');  */
    //$('#cbxBim').append('<option value="2">2</option>');  
        //Se reemplazo la i por el numero de trimestre permitido
        
    
    //---------------------- ACTIVACIÓN DE EVENTOS -----------------------------
    $("#btnRegresar_XBimOPromFin").unbind("click");
    $("#btnRegresar_XBimOPromFin").bind("click",function(){ frmwXBimOPromFin_Close(); });
    $("input[name='rbgXBimOPromOVal']",$('#pnlControles')).on('click',function() { grbnXBimOProm_Click($("input[name='rbgXBimOPromOVal']:checked").val());  }); //Tambien se puede más directo: $(this).val()
    $("#btnXBim").on('click',function(){ btnXBim_Click(); });
    $("#btnAvance").on('click',function(){ btnAvance_Click(); });
    $("#cbxBim").on('change',function(){ cbxBim_Change($(this).val()); });
    $("#btnCapDatosRepEv").on('click',function(){ btnCapDatosRepEv_Click(); });
    $("#btnImpRepIns").on("click", function(){ btnReporteDeInscripcion_ActionPerformed2(); });
}   

function grbnXBimOProm_Click(rbnSeleccionado)
{
    if (rbnSeleccionado === 'rbnXBim')
        $("#cbxBim").removeAttr('disabled');
    else if (rbnSeleccionado === 'rbnXProm')
        $("#cbxBim").attr('disabled','disabled');                               //$("#selector option[value=3]").attr('disabled','disabled');
        
}

function btnReporteDeInscripcion_ActionPerformed2()
{    
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","nombre","grado","grupo","cveplan","cicescini"], null, "JSON");
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    window.open("Grupo/reporteDeInscripcion.jsp?cicescini="+tblPrincipal.cicescini
            +"&cveplan="+tblPrincipal.cveplan+"&idcct="+tblPrincipal.idcct+"&grado="+tblPrincipal.grado
            +"&grupo="+tblPrincipal.grupo);    
}

function btnXBim_Click()
{
    
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    if (!document.getElementById('rbnXBim').checked && !document.getElementById('rbnXProm').checked)
        mensaje.XbimOPromFin ("TIPO_CAPTURA");
    else if (document.getElementById('rbnXProm').checked || document.getElementById('rbnValidacion').checked)
        mensaje.General("SOLO_TIENE_PERMITIDO","Captura de evaluaciónes");
    else{
        
        //document.getElementById('btnRegresar').setAttribute('id','btnBotonDesligado');  //Chanchuyo Para quitarle funcionamiento, como ya no usaremos este botón, le cambiamos el nombre para que no cause conflictos
        object_setVisible (false,'frmwXBimOPromFin');
        //------------ Extraemos los datos de la tabla tblPrincipal ------------
        var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","cct","nombre","grado","grupo","cveplan","cicescini"], null, "JSON");
        //------------------ Establecemos los datos a enviar -------------------
        var cbxBim = $("#cbxBim").val();
        var datos = {
            modulo:"xBiOPrFi", metodo:"btXBi", rbgXBimOPromOVal_Checked:$("input[name='rbgXBimOPromOVal']:checked").val(), 
            chkCalProm_isChecked:document.getElementById('chkCalProm').checked, cbxBim_SelItem:cbxBim,
            cicescin:sisVars.cicescin, tblPrincipal_idcct:tblPrincipal.idcct, tblPrincipal_grado:tblPrincipal.grado,
            tblPrincipal_grupo:tblPrincipal.grupo, tblPrincipal_cveplan:tblPrincipal.cveplan
        };
        //------------------------- Hacemos la llamada -------------------------
        cargarLoading(true);                                                    //Le indicamos que bloquee pantalla mientras halla subprocesos
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:true
        })
        .done(function(result){                        
            switch(result.returnCase){
                case 1:            
                        switch (result.casoRequerido)
                        {
                            case 'frmwValidPrim': break;
                            case 'frmwCalifPrim': break;
                            case 'frmwCalifSec': break;
                            case 'frmwRevisaGpo': break;
                            case 'frmwCalifSecXBim': 
                                    frmwCalifSecXBim_Show(true, $("input[name='rbgXBimOPromOVal']:checked").val(),document.getElementById('chkCalProm').checked, sisVars.unidad, result.bim, result.calProm, tblPrincipal.cicescini, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.nombre, tblPrincipal.cveplan, tblPrincipal.grado, tblPrincipal.grupo, bimMin, bimMin );
                                break;
                        }                        
                    break;
                case 2: case -2:                           
                        if(result.mensaje)
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        object_setVisible (true,'frmwXBimOPromFin');                        
                    break;
                case 0: case -1: 
                        if(result.mensaje)
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
        })
        .always(function() {
            cerrarLoading();
        });
    }
}

function btnAvance_Click()
{
    //f_AvceCaptu.ShowModal ;
    alert ("Opción desactivada");
}

function frmwXBimOPromFin_FormActivate ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    var datos = {modulo:"xBiOPrFi", metodo:"foAc", tipo_usuario:sisVars.tipo_usuario, txtUsuario:sisVars.usuario };
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
                    object_setVisible(result.btnAvance_Visible,"frmwXBimOPromFin #btnAvance");
                    object_setVisible(result.rbnValidacion_Visible,"frmwXBimOPromFin #lblValidacion");
                    
                    object_setVisible(false,"lblXProm");
                        boton_setVisible(false,"btnAvance");
                    capValid = result.capValid;
                    
                    document.getElementById('chkCalProm').checked = true;       //Cambio temporal durante el 5o bimestre, para que esté fijo calcular promedio
                    object_setEnabled(false,"chkCalProm");
                    
                    cerrarLoading();
                    aparecerGlogoAyuda ("#btnCapDatosRepEv .globoayuda_pointdown",100);
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

function cbxBim_Change (item)
{
    if ( item==="3" )
        document.getElementById('chkCalProm').checked = true;
    //else
    //  Box_CalProm.Checked := false;
    document.getElementById('rbnXBim').checked = true;
}

function btnCapDatosRepEv_Click()
{
    var tabla = new Tabla();    
    var mensaje = new Mensajes ();
    object_setVisible (false,'frmwXBimOPromFin');
    var tblPrincipal = tabla.getSelectedRow("tblPrincipal",["idcct","modalidad","cct","nombre","grado","grupo","cveplan","cicescini","cveprograma"], null, "JSON");    
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );       
    var ceCapTrim1=false, ceCapTrim2=false, ceCapTrim3=false;
        
    if (sisVars){        
        for (var boton in sisVars.botonesDeCalif) {              
            if(sisVars.botonesDeCalif[boton]==="btnTrim1")
                ceCapTrim1 = true;
            else if (sisVars.botonesDeCalif[boton]==="btnTrim2")
                ceCapTrim2 = true;
            else if (sisVars.botonesDeCalif[boton]==="btnTrim3")
                ceCapTrim3 = true;
        }    
    }
    if(ceCapTrim1===false && ceCapTrim2===false && ceCapTrim3===false) {
        mensaje.XbimOPromFin ("PERIODO_EVAL");
        frmwXBimOPromFin_Close();
    }        
    else
        frmwCaptuRepEval_Show(tblPrincipal.cicescini, tblPrincipal.modalidad, tblPrincipal.idcct, tblPrincipal.cct, tblPrincipal.cveplan, tblPrincipal.grado, tblPrincipal.grupo, tblPrincipal.cveprograma);
}