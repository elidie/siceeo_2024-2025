/* 
    Creado el : 09-mar-2017, 9:30:46
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsOficializar;

function frmwOficializar_Show(tblPrincipal_selRow)
{        
    jsOficializar = {
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        
        verDesofic: null,
        ofsCal: null,
        ofsEval:null,
        
        ofTrim1: false,
        ofTrim2: false,
        ofTrim3: false,
        
        lockClickInBimEv:[false, false, false, false, false, false]
    };
    
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );    
    
    for (var boton in sisVars.botonesDeCalif) {              
            if(sisVars.botonesDeCalif[boton]==="btnTrim1")                
                jsOficializar.ofTrim1 = true;
            else if (sisVars.botonesDeCalif[boton]==="btnTrim2")                
                jsOficializar.ofTrim2 = true;
            else if (sisVars.botonesDeCalif[boton]==="btnTrim3")                
                jsOficializar.ofTrim3 = true;
    }
    
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwOficializar").css("display", "block");                              // Mostramos el formulario correspondiente
    
    frmwOficializar_FormActivate ();
}

function frmwOficializar_Close()
{
    irAVentanaPrincipal();
}

function frmwOficializar_Create()
{
    var mensaje = new Mensajes ();
    var tabindexIni = 50, tabindexReturn=35;
    var gradoGrupo = jsOficializar.tblPrincipal_grado +"º "+jsOficializar.tblPrincipal_grupo;
        
    if($('#frmfOficializar').length)
        $('#frmfOficializar').remove();
    $('#frmwOficializar').append('<fieldset id="frmfOficializar"><legend>Oficializaciones</legend><div id="btnRegresar_Oficializar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfOficializar").append('<div id="pnlOficializar"></div>');
        
            $("#pnlOficializar").append('<label id="lblIndicacionGralOfic">La oficialización sirve para indicar al sistema que ha terminado de realizar para todos los grupos de la escuela alguno de los procesos abajo mencionados y que todos sus datos están correctamente ingresados. Por favor antes de oficializar revise que su información esté ingresada correctamente. Si desea desoficializar debe pedir ayuda a su UDSE.</label>'
                                        + '<div id="pnlScrollOficializar"></div>');
               /* $('#pnlScrollOficializar').append('<div id="pnlOficializarInscripcion" class="panel"><label class="tituloPanel">Inscripción</label></div>');
                    $('#pnlOficializarInscripcion').append('<label class="indicacionesOfic">Al oficializar esta etapa, el sistema bloqueará por unas semanas los nuevos ingresos, bajas, correcciones, cambios de grupo y necesidades especiales de alumnos.</label>'
                                                            + getCodeBtnOfic ("btnOficInscripcion", (tabindexIni++), "Oficialice con este botón para indicar que sus datos son correctos y pueden ser tomados para estadísticas de Inicio de curso.", "I", "Inscripcion", "oficializar", "Naranja",gradoGrupo)
                                                            + getCodeBtnOfic ("btnDesoficInscripcion", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar ingresando datos de Inscripción.", "I", "Inscripción", "desoficializar", "Gris",gradoGrupo)
                                                        );*/
                
                if (jsOficializar.tblPrincipal_cveplan==="1" || jsOficializar.tblPrincipal_cveplan==="2"
                        && (jsOficializar.ofTrim1===true || jsOficializar.ofTrim2===true || jsOficializar.ofTrim3===true) )
                {
                    $('#pnlScrollOficializar').append('<div id="pnlOficializarCalificaciones" class="panel"><label class="tituloPanel">Calificaciones</label></div>');
                        //$('#pnlOficializarCalificaciones').append('<label class="indicacionesOfic">Conforme vaya terminando la etapa de captura bimestral de calificaciones, así oficialice el bimestre corresponediente. NOTA IMPORTANTE: Sólo al oficializar el 5to bimestre ya no se podrá desoficializar posteriormente ningún bimestre.</label>'
                        $('#pnlOficializarCalificaciones').append('<label class="indicacionesOfic">Oficialice la calificación de la evaluación que hasta el momento haya terminado de capturar. NOTA IMPORTANTE: Sólo al oficializar la 3ra evaluación ya no se podrá desoficializar posteriormente ningúna evaluación.</label>'
                                                            + ( jsOficializar.ofTrim1===true ? (
                                                                 getCodeBtnOfic ("btnOficCalifBim1", (tabindexIni++), "Oficializa calificaciones de la 1ra evaluación.", "1", "1ra evaluación", "oficializar ", "Azul", gradoGrupo)
                                                                + getCodeBtnOfic ("btnDesoficCalifBim1", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar ingresando calificaciones de la 1ra evaluación.", "1", "1ra evaluación", "desoficializar ", "Gris", gradoGrupo)
                                                                ) :"") 
                                                            + ( jsOficializar.ofTrim2===true ? (   
                                                                 getCodeBtnOfic ("btnOficCalifBim2", (tabindexIni++), "Oficializa calificaciones de la 2da evaluación.", "2", "2da evaluación", "oficializar ", "Azul", gradoGrupo)
                                                                + getCodeBtnOfic ("btnDesoficCalifBim2", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar ingresando calificaciones de la 2da evaluación.", "2", "2da evaluación", "desoficializar ", "Gris", gradoGrupo)
                                                                ) :"") 
                                                            + ( jsOficializar.ofTrim3===true ? (       
                                                                 getCodeBtnOfic ("btnOficCalifBim3", (tabindexIni++), "Oficializa calificaciones de la 3ra evaluación.", "3", "3ra evaluación", "oficializar ", "Azul", gradoGrupo)
                                                                + getCodeBtnOfic ("btnDesoficCalifBim3", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar ingresando calificaciones de la 3ra evaluación.", "3", "3ra evaluación", "desoficializar ", "Gris", gradoGrupo)                                                                                                                    
                                                            ) : "")
                                                        );
                    if (jsOficializar.tblPrincipal_cveplan==="2"){
                        $('#pnlScrollOficializar').append('<div id="pnlOficializarCertComplementaria" class="panel"><label class="tituloPanel">Complementaria</label></div>');
                            $('#pnlOficializarCertComplementaria').append('<label class="indicacionesOfic">Oficialice la etapa complementaria para alumnos aprobados por examen extraordinario de regularización. Al oficializar le permitirá foliar y firmar los certificados con la fecha de acuerdo al período de presentación del examen.</label>'
                                                                    + getCodeBtnOfic ("btnOficCertCompl", (tabindexIni++), "Oficializa complementaria de certificados.", "C", "Complementaria", "foliar ", "Tamarindo", gradoGrupo)
                                                                    + getCodeBtnOfic ("btnSinCertCompl", (tabindexIni++), "No hay alumnos para complementaria en este grupo.", "C", "Complementaria", "vacío ", "Gris", gradoGrupo)
                                                                );
                    }
                } else if (jsOficializar.tblPrincipal_cveplan==="3" && (jsOficializar.ofTrim1 || jsOficializar.ofTrim2 || jsOficializar.ofTrim3)) {
                    $('#pnlScrollOficializar').append('<div id="pnlOficializarEvaluaciones" class="panel"><label class="tituloPanel">Evaluaciones</label></div>');
                        //$('#pnlOficializarEvaluaciones').append('<label class="indicacionesOfic">Conforme vaya terminando la etapa de captura de cada evaluación, así oficialice la evaluación correspondiente. NOTA IMPORTANTE: Sólo al oficializar la 3ra evaluación ya no se podrá desoficializar posteriormente ninguna evaluación.</label>'
                        $('#pnlOficializarEvaluaciones').append('<label class="indicacionesOfic">Oficialice la evaluación que hasta el momento haya terminado de capturar. NOTA IMPORTANTE: Sólo al oficializar la 3ra evaluación ya no se podrá desoficializar posteriormente ninguna evaluación.</label>'
                                                            + ( jsOficializar.ofTrim1===true ? (
                                                                 getCodeBtnOfic ("btnOficEval1", (tabindexIni++), "Oficializa 1ra. evaluación.", "1", "1ra evaluación", "oficializar ", "Azul", gradoGrupo)
                                                                + getCodeBtnOfic ("btnDesoficEval1", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar ingresando avances para la 1ra. evaluación.", "1", "1ra evaluación", "desoficializar ", "Gris", gradoGrupo)
                                                            ) :"")     
                                                            + ( jsOficializar.ofTrim2===true ? (   
                                                                 getCodeBtnOfic ("btnOficEval2", (tabindexIni++), "Oficializa 2da. evaluación.", "2", "2da evaluación", "oficializar ", "Azul", gradoGrupo)
                                                                + getCodeBtnOfic ("btnDesoficEval2", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar ingresando avances para la 2da. evaluación.", "2", "2da evaluación", "desoficializar ", "Gris", gradoGrupo)
                                                            ) :"")     
                                                            + ( jsOficializar.ofTrim3===true ? (       
                                                                 getCodeBtnOfic ("btnOficEval3", (tabindexIni++), "Oficializa 3ra. evaluación.", "3", "3ra evaluación", "oficializar ", "Azul", gradoGrupo)
                                                                + getCodeBtnOfic ("btnDesoficEval3", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar ingresando avances para la 3ra. evaluación.", "3", "3ra evaluación", "desoficializar ", "Gris", gradoGrupo)
                                                            ) :"")         
                                                        );
                }
                
                $('#pnlScrollOficializar').append('<div id="pnlOficializarPreinscripcion" class="panel"><label class="tituloPanel">Preinscripciones '+((parseInt(jsOficializar.tblPrincipal_cicescini)+1) + '-'+ (parseInt(jsOficializar.tblPrincipal_cicescini)+2) )+'</label></div>');
                    $('#pnlOficializarPreinscripcion').append('<label class="indicacionesOfic">Si ya terminó de preinscribir todos sus grados de la escuela, indíquelo con este botón.</label>'
                                                            + getCodeBtnOfic ("btnOficPreinscripcion", (tabindexIni++), "Oficializa su preinscripción.", "P", "Preinscripción", "oficializar", "Pistache")
                                                            + getCodeBtnOfic ("btnDesoficPreinscripcion", (tabindexIni++), "Al desoficializar permitirá que se pueda continuar preinscribiendo.", "P", "Preinscripción", "desoficializar","Gris")
                                                            );
                
            
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnRegresar_Oficializar").on("click",function(){ frmwOficializar_Close(); });
    
    //$("#btnOficInscripcion").on('click', function(){ oficializaInscripcion_Click ("oficializar"); });
    $("#btnOficCalifBim1").on('click', function(event){ oficializaCalificaciones (event, "oficializar",1); event.preventDefault(); });
    $("#btnOficCalifBim2").on('click', function(event){ oficializaCalificaciones (event, "oficializar",2); event.preventDefault(); });
    $("#btnOficCalifBim3").on('click', function(event){ oficializaCalificaciones (event, "oficializar",3); event.preventDefault(); });
    
    $("#btnOficEval1").on('click', function(event){ oficializaEvaluacionesPreesc (event, "oficializar",1); event.preventDefault(); });
    $("#btnOficEval2").on('click', function(event){ oficializaEvaluacionesPreesc (event, "oficializar",2); event.preventDefault(); });
    $("#btnOficEval3").on('click', function(event){ oficializaEvaluacionesPreesc (event, "oficializar",3); event.preventDefault(); });

    $("#btnOficCertCompl").on('click', function(){ oficializaCertCompl_Click ("oficCertCompl"); });
    $("#btnOficPreinscripcion").on('click', function(){ oficializaPreinscripcion_Click ("oficializar"); });
    
    //$("#btnDesoficInscripcion").on('click', function(){ oficializaInscripcion_Click ("desoficializar"); });
    $("#btnDesoficCalifBim1").on('click', function(event){ if (!jsOficializar.lockClickInBimEv[1]) desoficializarPorAlumno (event, 1, "1a Evaluación"); event.preventDefault(); /*desoficializarPorAlumno (event, ); /*oficializaCalificaciones (event, "desoficializar","1");*/ }); //mensaje.Oficializaciones("DESOFIC_DESACTIVADO","calificaciones");
    $("#btnDesoficCalifBim2").on('click', function(event){ if (!jsOficializar.lockClickInBimEv[2]) desoficializarPorAlumno (event, 2, "2a Evaluación");  event.preventDefault();/*oficializaCalificaciones (event, "desoficializar","2");*/});
    $("#btnDesoficCalifBim3").on('click', function(event){ if (!jsOficializar.lockClickInBimEv[3]) desoficializarPorAlumno (event, 3, "3a Evaluación"); event.preventDefault(); /*oficializaCalificaciones (event, "desoficializar","3");*/ });
    
    $("#btnDesoficEval1").on('click', function(event){ if (!jsOficializar.lockClickInBimEv[1]) desoficializarPorAlumno (event, 1, "1a Evaluación");  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","1");*/ });
    $("#btnDesoficEval2").on('click', function(event){ if (!jsOficializar.lockClickInBimEv[2]) desoficializarPorAlumno (event, 2, "2a Evaluación");  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","2");*/ });
    $("#btnDesoficEval3").on('click', function(event){ if (!jsOficializar.lockClickInBimEv[3]) desoficializarPorAlumno (event, 3, "3a Evaluación");  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","3");*/ });
    $("#btnDesoficPreinscripcion").on('click', function(){ oficializaPreinscripcion_Click ("desoficializar"); });
    
    
    $("#btnDesoficCalifBim1 .btnOf").on('click', function(event){ oficializaCalificaciones (event, "oficializar",1); event.preventDefault(); /*desoficializarPorAlumno (event, ); /*oficializaCalificaciones (event, "desoficializar","1");*/ }); //mensaje.Oficializaciones("DESOFIC_DESACTIVADO","calificaciones");
    $("#btnDesoficCalifBim2 .btnOf").on('click', function(event){ oficializaCalificaciones (event, "oficializar",2);  event.preventDefault();/*oficializaCalificaciones (event, "desoficializar","2");*/});
    $("#btnDesoficCalifBim3 .btnOf").on('click', function(event){ oficializaCalificaciones (event, "oficializar",3); event.preventDefault(); /*oficializaCalificaciones (event, "desoficializar","3");*/ });
    
    $("#btnDesoficEval1 .btnOf").on('click', function(event){ oficializaEvaluacionesPreesc (event, "oficializar",1);  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","1");*/ });
    $("#btnDesoficEval2 .btnOf").on('click', function(event){ oficializaEvaluacionesPreesc (event, "oficializar",2);  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","2");*/ });
    $("#btnDesoficEval3 .btnOf").on('click', function(event){ oficializaEvaluacionesPreesc (event, "oficializar",3);  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","3");*/ });
    
    $("#btnDesoficCalifBim1 .btnDeof").on('click', function(event){ desoficializarPorAlumno (event, 1, "1a Evaluación"); event.preventDefault(); /*desoficializarPorAlumno (event, ); /*oficializaCalificaciones (event, "desoficializar","1");*/ }); //mensaje.Oficializaciones("DESOFIC_DESACTIVADO","calificaciones");
    $("#btnDesoficCalifBim2 .btnDeof").on('click', function(event){ desoficializarPorAlumno (event, 2, "2a Evaluación");  event.preventDefault();/*oficializaCalificaciones (event, "desoficializar","2");*/});
    $("#btnDesoficCalifBim3 .btnDeof").on('click', function(event){ desoficializarPorAlumno (event, 3, "3a Evaluación"); event.preventDefault(); /*oficializaCalificaciones (event, "desoficializar","3");*/ });
    
    $("#btnDesoficEval1 .btnDeof").on('click', function(event){ desoficializarPorAlumno (event, 1, "1a Evaluación");  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","1");*/ });
    $("#btnDesoficEval2 .btnDeof").on('click', function(event){ desoficializarPorAlumno (event, 2, "2a Evaluación");  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","2");*/ });
    $("#btnDesoficEval3 .btnDeof").on('click', function(event){ desoficializarPorAlumno (event, 3, "3a Evaluación");  event.preventDefault();/*mensaje.Oficializaciones("DESOFIC_DESACTIVADO","evaluaciones"); /*oficializaEvaluacionesPreesc (event, "desoficializar","3");*/ });
}

function mwfOficializar_Create ()
{
    modalWindow_Create ("Oficializar", "Oficialización por alumno",frmwOficializar_Close, 90, {anchoAutoajustable:true});
    $('#mwfpOficializar').append('<div id="pnlOficializarXAlumno"></div>');
    
        $('#pnlOficializarXAlumno').append('<div id="pnlXADatosGenerales"></div>');
                            $('#pnlXADatosGenerales').append('<label id="lblTblPrincipal_cct">'+jsDesoficializar.tblPrincipal_cct+'</label>');
                            $('#pnlXADatosGenerales').append('<label id="lblTblPrincipal_grado">'+jsDesoficializar.tblPrincipal_grado+'</label>');
                            $('#pnlXADatosGenerales').append('<label id="lblTblPrincipal_grupo">'+jsDesoficializar.tblPrincipal_grupo+'</label>');
                            $('#pnlXADatosGenerales').append('<label id="lblTblPrincipal_tituloBimEval">'+jsDesoficializar.tituloBimEval+'</label>');
        $('#pnlOficializarXAlumno').append('<div id="pnlXAListadoDeAlumnos" class="panel">'
                                            + '<label class="tituloPanel">Seleccione los alumnos a desoficializar</label>'
                                            + '<div id="pnlXATblAlumnos">  <div id="scrlAlumnos" class="scrollTable"></div>  </div>'
                                        +'</div>');
        $('#pnlOficializarXAlumno').append('<ul class="buttonBar"> '
                                            +'<li><a href="#" id="btnOficAlumnos"><label class="icon-disquete"></label> Oficializar</a></li>'
                                        +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnOficAlumnos").on('click',function(){ btnDesoficAlumnos_Click(); });
}

function getCodeBtnOfic (id, tabindex, title, icono, titulo, estado, color, gradogrupo)
{
    var obj3Visible="", btnDesactivado, oficDesofVisible='style="display:none"';
    if (!jsOficializar.verDesofic && estado.indexOf('desoficializar')>=0 )      //Para la escuela si se va a dibujar el desoficializar
    {
        estado = 'OFICIALIZADO';
        obj3Visible = 'style="display:none"';
        btnDesactivado = "btnOfic-desactivado";
        title = "Para desoficializar pida ayuda a su UDR.";
    }else if (estado.indexOf('vacío')>=0){
        estado = 'VACÍO';
        obj3Visible = 'style="display:none"';
        btnDesactivado = "btnOfic-desactivado";
        title = "";
    }
    
    var buttonCode = '<div id="'+id+'" class="btnOfic btnOfic-FondoColor'+color+' '+btnDesactivado+'" tabindex="'+tabindex+'" '+(title===''?'':'title="'+title+'"')+'>'
                        + '<label class="btnOfic-obj1 btnOfic-TextColor'+color+'">'+icono+'</label>'
                        + '<label class="btnOfic-obj2">'+titulo+'</label>'
                        + (typeof gradogrupo==="undefined"?"":'<label class="btnOfic-obj3">'+gradogrupo+'</label>')
                        + '<label class="btnOfic-obj4" '+obj3Visible+'>Click aquí para:</label>'
                        + '<label class="btnOfic-obj5 btnOfic-TextColor'+color+'">'+estado+'</label>'
                        + '<div class="btnOfic-obj6 oficdesofic" '+oficDesofVisible+'>'
                            +'<div id="'+id+'1" class="btnOf alinearHoriz zoom" title="Clic para oficializar">OFIC</div>'
                            +'<div id="'+id+'2" class="btnDeof alinearHoriz zoom" title="Clic para Desoficializar">DESOF</div>'
                        +'</div>'
                    +'</div>';
    return buttonCode;
}

function frmwOficializar_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos= {
        modulo:"Of", metodo:"foAc", tblPrincipal_idcct:jsOficializar.tblPrincipal_idcct, tblPrincipal_cicescini:jsOficializar.tblPrincipal_cicescini,
        tblPrincipal_grado:jsOficializar.tblPrincipal_grado, tblPrincipal_grupo:jsOficializar.tblPrincipal_grupo
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    /************ Se realiza la petición al servlet ***************/    
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true                                                              //Necesario el true, para que se vea el loading
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    jsOficializar.verDesofic = result.verDesofic;
                    jsOficializar.ofsCal=result.ofsCal;
                    jsOficializar.ofsEval=result.ofsEval;
                    frmwOficializar_Create();
                    
                    //Verificamos permisos según la oficialización
                    permisosParaDesoficializar (result.esPreinscOficializada, "btnOficPreinscripcion", "btnDesoficPreinscripcion");    
                    permisosParaDesoficializar (null, "btnOficCalifBim1", "btnDesoficCalifBim1",1);    
                    permisosParaDesoficializar (null, "btnOficCalifBim2", "btnDesoficCalifBim2",2);    
                    permisosParaDesoficializar (null, "btnOficCalifBim3", "btnDesoficCalifBim3",3);    
    
                    permisosParaDesoficializar (null, "btnOficEval1", "btnDesoficEval1",1);    
                    permisosParaDesoficializar (null, "btnOficEval2", "btnDesoficEval2",2);    
                    permisosParaDesoficializar (null, "btnOficEval3", "btnDesoficEval3",3);    
                    permisosParaDesoficializar (result.esInscripOficializada, "btnOficInscripcion", "btnDesoficInscripcion");
                    permisosParaDesoficializar (result.noHasAluCompl, "btnOficCertCompl", "btnSinCertCompl");
                    
                    
                    //permisosParaDesoficializar (false, "btnOficCalifBim5", "btnDesoficCalifBim5");   
                    
                    
                    if (result.masDeUnIdcct)
                        mensaje.Oficializaciones("MAS_DE_UN_IDCCT");
                break;
            case 0:case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    frmwOficializar_Close();
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

function oficializaInscripcion_Click (caso)
{
    var mensaje = new Mensajes();
    var respOficializar=false;
    
    if ( caso==="desoficializar" && !jsOficializar.verDesofic && $("#btnDesoficInscripcion .btnOfic-obj5").text()==="OFICIALIZADO")
        mensaje.Oficializaciones("ESCUELA_DESOFIC");
    else
        respOficializar = mensaje.Oficializaciones("OFIC_DESOFIC",caso, "la captura de Inscripción","CONFIRM_DIALOG");
    
    if (respOficializar === true)
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Of", metodo:"ofIn", caso:caso, tblPrincipal_idcct:jsOficializar.tblPrincipal_idcct, 
            tblPrincipal_cicescini:jsOficializar.tblPrincipal_cicescini
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
                        permisosParaDesoficializar ((caso==="oficializar"), "btnOficInscripcion", "btnDesoficInscripcion");
                        
                        if (caso==="oficializar")
                            mensaje.General("OFICIALIZADO");
                        else 
                            mensaje.General("DESOFICIALIZADO");
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

function oficializaCalificaciones (event, caso, bimestre)
{
    var mensaje = new Mensajes();
    var respOficializar=false;
    
    if ( caso==="desoficializar" && !jsOficializar.verDesofic && $("#btnDesoficCalifBim"+bimestre+" .btnOfic-obj5").text()==="OFICIALIZADO")
        mensaje.Oficializaciones("ESCUELA_DESOFIC");
    else {
        if(jsOficializar.tblPrincipal_grado==="6" && jsOficializar.tblPrincipal_cveplan==="1" || 
                jsOficializar.tblPrincipal_grado==="3" && jsOficializar.tblPrincipal_cveplan==="2")            
            respOficializar = mensaje.Oficializaciones("OFIC_DESOFIC_3RO6TO",caso, "la captura de calificaciones de la evaluación "+bimestre,"CONFIRM_DIALOG");
        
        else if(jsOficializar.tblPrincipal_grado!=="6" && jsOficializar.tblPrincipal_cveplan==="1" || 
                jsOficializar.tblPrincipal_grado!=="3" && jsOficializar.tblPrincipal_cveplan==="2")
            respOficializar = mensaje.Oficializaciones("OFIC_DESOFIC_OTROS",caso, "la captura de calificaciones de la evaluación "+bimestre,"CONFIRM_DIALOG");
        else
            respOficializar = mensaje.Oficializaciones("OFIC_DESOFIC",caso, "la captura de calificaciones de la evaluación "+bimestre,"CONFIRM_DIALOG");
    }  
    if (respOficializar === true)
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Of", metodo:"ofCa", caso:caso, tblPrincipal_modalidad:jsOficializar.tblPrincipal_modalidad, tblPrincipal_idcct:jsOficializar.tblPrincipal_idcct, 
            tblPrincipal_cicescini:jsOficializar.tblPrincipal_cicescini, tblPrincipal_cveplan:jsOficializar.tblPrincipal_cveplan, 
            tblPrincipal_grado:jsOficializar.tblPrincipal_grado, tblPrincipal_grupo:jsOficializar.tblPrincipal_grupo, bimestre:bimestre, idalus:""
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
                        jsOficializar.ofsCal=result.ofsCal;
                        jsOficializar.ofsEval=result.ofsEval;
                        permisosParaDesoficializar ((caso==="oficializar"), "btnOficCalifBim"+bimestre, "btnDesoficCalifBim"+bimestre, bimestre);
                        
                        if (caso==="oficializar")
                            mensaje.General("OFICIALIZADO");
                        else 
                            mensaje.General("DESOFICIALIZADO");
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
    //event.stopPropagation();
}

function oficializaEvaluacionesPreesc (event, caso, evaluacion)
{
    var mensaje = new Mensajes();
    var respOficializar=false;
    
    if ( caso==="desoficializar" && !jsOficializar.verDesofic && $("#btnDesoficEval"+evaluacion+" .btnOfic-obj5").text()==="OFICIALIZADO")
        mensaje.Oficializaciones("ESCUELA_DESOFIC");
    else
        respOficializar = mensaje.Oficializaciones("OFIC_DESOFIC",caso, "la captura de avances de la evaluación "+evaluacion,"CONFIRM_DIALOG");
    
    if (respOficializar === true)
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Of", metodo:"ofPrPr", caso:caso, tblPrincipal_modalidad:jsOficializar.tblPrincipal_modalidad, tblPrincipal_idcct:jsOficializar.tblPrincipal_idcct, 
            tblPrincipal_cicescini:jsOficializar.tblPrincipal_cicescini, tblPrincipal_cveplan:jsOficializar.tblPrincipal_cveplan, 
            tblPrincipal_grado:jsOficializar.tblPrincipal_grado, tblPrincipal_grupo:jsOficializar.tblPrincipal_grupo, evaluacion:evaluacion, idalus:""
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
                        jsOficializar.ofsCal=result.ofsCal;
                        jsOficializar.ofsEval=result.ofsEval;
                        permisosParaDesoficializar ((caso==="oficializar"), "btnOficEval"+evaluacion, "btnDesoficEval"+evaluacion, evaluacion);
                        
                        if (caso==="oficializar")
                            mensaje.General("OFICIALIZADO");
                        else 
                            mensaje.General("DESOFICIALIZADO");
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
    //event.stopPropagation();
}

function oficializaCertCompl_Click (caso)
{
    var mensaje = new Mensajes();
    
    if ( !jsOficializar.ofsCal.todoOf )
        mensaje.Oficializaciones("SOLO_TODO_OFIC");
    else
        mwfComplementaria_Show ( {cicescini:jsOficializar.tblPrincipal_cicescini, cveplan:jsOficializar.tblPrincipal_cveplan, cct:jsOficializar.tblPrincipal_cct, 
                            idcct:jsOficializar.tblPrincipal_idcct, modalidad:jsOficializar.tblPrincipal_modalidad, grado:jsOficializar.tblPrincipal_grado, 
                            grupo:jsOficializar.tblPrincipal_grupo }, caso);
}

function oficializaPreinscripcion_Click (caso)
{
    var mensaje = new Mensajes();
    var respOficializar = false;
    
    if ( caso==="desoficializar" && !jsOficializar.verDesofic && $("#btnDesoficPreinscripcion .btnOfic-obj5").text()==="OFICIALIZADO")
        mensaje.Oficializaciones("ESCUELA_DESOFIC");
    else
        respOficializar = mensaje.Oficializaciones("OFIC_DESOFIC",caso, "la preinscripción del ciclo "+(parseInt(jsOficializar.tblPrincipal_cicescini)+1)+'-'+(parseInt(jsOficializar.tblPrincipal_cicescini)+2),"CONFIRM_DIALOG");
    
    if (respOficializar === true)
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Of", metodo:"ofPr", caso:caso, tblPrincipal_idcct:jsOficializar.tblPrincipal_idcct, 
            cicesciniPreinsc:(parseInt(jsOficializar.tblPrincipal_cicescini)+1)
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
                        permisosParaDesoficializar ((caso==="oficializar"), "btnOficPreinscripcion", "btnDesoficPreinscripcion");
                        
                        if (caso==="oficializar")
                            mensaje.General("OFICIALIZADO");
                        else 
                            mensaje.General("DESOFICIALIZADO");
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


function permisosParaDesoficializar (permiso, botonOficializar, botonDesoficializar, bimeval)
{
    var oficDesofVisible='style="display:none"';
    if (botonDesoficializar.indexOf('Calif')>=0 || botonDesoficializar.indexOf('Eval')>=0){
            var ofsCalEval, bimOf, alDeof;
            ofsCalEval = (jsOficializar.tblPrincipal_cveplan==="3")?jsOficializar.ofsEval:jsOficializar.ofsCal;
            
            if (bimeval === 1 ){
                bimOf=ofsCalEval.bimOf1; alDeof=ofsCalEval.alDeofB1;
            }else if (bimeval === 2 ){
                bimOf=ofsCalEval.bimOf2; alDeof=ofsCalEval.alDeofB2;
            }else if (bimeval === 3 ){
                bimOf=ofsCalEval.bimOf3; alDeof=ofsCalEval.alDeofB3;
            }
            
            if (jsOficializar.verDesofic) {
                botonOfic_setVisible(!bimOf, botonOficializar);
                botonOfic_setVisible(bimOf, botonDesoficializar);
                if (bimOf && alDeof){
                    $("#"+botonDesoficializar+" .oficdesofic").css("display","block");
                    //$('#'+botonDesoficializar).off();
                    jsOficializar.lockClickInBimEv[bimeval]=true;
                } else {
                    $("#"+botonDesoficializar+" .oficdesofic").css("display","none");
                    jsOficializar.lockClickInBimEv[bimeval]=false;
                    //$('#'+botonDesoficializar).on("click", funcion1);
                }
            }else{
                if (bimOf===false || (bimOf===true && alDeof===true)){
                    botonOfic_setVisible(true, botonOficializar);
                    botonOfic_setVisible(false, botonDesoficializar);
                    jsOficializar.lockClickInBimEv[bimeval]=false;
                }else {
                    botonOfic_setVisible(false, botonOficializar);
                    botonOfic_setVisible(true, botonDesoficializar);
                    /*$('#'+botonDesoficializar).off();*/
                    jsOficializar.lockClickInBimEv[bimeval]=true;
                }
            }
    }else {
        botonOfic_setVisible(!permiso,botonOficializar);
        botonOfic_setVisible(permiso,botonDesoficializar);
    }
}

function botonOfic_setVisible (enabled, nombreObjeto)
{
    if (enabled === true)
        $( '#'+nombreObjeto).css("display","");
    else
        $( '#'+nombreObjeto).css("display","none");
}

function desoficializarPorAlumno (event, bim, tituloBimEval)
{
    var mensaje = new Mensajes();
    var nombreBotonOficializar, nombreBotonDesoficializar;
    
    if (tituloBimEval.indexOf ("Bimestre")>=0){
        nombreBotonOficializar="btnOficCalifBim"+bim;
        nombreBotonDesoficializar="btnDesoficCalifBim"+bim;
    }else if (tituloBimEval.indexOf("Evaluaci")>=0){
        nombreBotonOficializar="btnOficEval"+bim;
        nombreBotonDesoficializar="btnDesoficEval"+bim;
    }
    
    if (mensaje.Oficializaciones("QUIERE_ESOFIC","","","CONFIRM_DIALOG"))
        mwfDesoficializar_Show (event, {cicescini:jsOficializar.tblPrincipal_cicescini, cveplan:jsOficializar.tblPrincipal_cveplan, cct:jsOficializar.tblPrincipal_cct, 
                                idcct:jsOficializar.tblPrincipal_idcct, modalidad:jsOficializar.tblPrincipal_modalidad, grado:jsOficializar.tblPrincipal_grado, 
                                grupo:jsOficializar.tblPrincipal_grupo }, bim, tituloBimEval, nombreBotonOficializar, nombreBotonDesoficializar);
}