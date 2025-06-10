/* 
    Creado el : 19-may-2017, 18:12:40
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaptuRepEval;

function frmwCaptuRepEval_Show(tblPrincipal_cicescini, tblPrincipal_modalidad, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveprograma)
{

    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );   
    var bimMax=0, bimMin=0;
    if (sisVars){        
        for (var boton in sisVars.botonesDeCalif) {              
            if(sisVars.botonesDeCalif[boton]==="btnTrim1"){               
                bimMin = (bimMin===0 ? 1: bimMin);
                bimMax = (bimMax===0 ? 1: bimMax);
            } else if (sisVars.botonesDeCalif[boton]==="btnTrim2") {                
                bimMin = (bimMin===0 || bimMin > 2 ? 2: bimMin);
                bimMax = (bimMax===0 || bimMax < 2 ? 2: bimMax);
            } else if (sisVars.botonesDeCalif[boton]==="btnTrim3") {                
                bimMin = (bimMin===0 || bimMin > 3 ? 3: bimMin);
                bimMax = (bimMax===0 || bimMax < 3 ? 3: bimMax);
            }
        }    
    }
    
    jsCaptuRepEval = {
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_modalidad: tblPrincipal_modalidad,
        tblPrincipal_cct: tblPrincipal_cct,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo:tblPrincipal_grupo,
        tblPrincipal_cveprograma:tblPrincipal_cveprograma,
        
        cicescin:null,
        capRepOf: null,
        tblAlumCapRepEval_idalu:null,
        califCicEscIn:null,
        cbxBimestres:["1","2","3"],
        cbxMateriasAlumno:new Array(),
        cveMats:new Array(),
        hayCambios:false,
        
        eval:bimMin, //valor original 1                
        evalOf:false,
        evalOf1:true,
        evalOf2:true,
        evalOf3:true,
        evalMax: bimMax,
        evalMin: bimMin,
        
        paqueteMatsDefault:null
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwCaptuRepEval").css("display", "block");                              // Mostramos el formulario correspondiente    
    frmwCaptuRepEval_Create ();
    frmwCaptuRepEval_FormActivate ();
}

function frmwCaptuRepEval_Close()
{
    jsCaptuRepEval = null;
    irAVentanaPrincipal();
}

function frmwCaptuRepEval_Create()
{
    var tabindexIni = 50, tabindexReturn=35;
    
    if($('#frmfCaptuRepEval').length)
        $('#frmfCaptuRepEval').remove();
    $('#frmwCaptuRepEval').append('<fieldset id="frmfCaptuRepEval"><legend>Boleta de Evaluación</legend><div id="btnRegresar_CaptuRepEval" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCaptuRepEval").append('<div id="pnlCaptuRepEval"></div>');
        
            $('#pnlCaptuRepEval').append('<div id="pnlListadoAlumnos"></div>');
                $('#pnlListadoAlumnos').append('<div id="pnlDatosGenerales"></div>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapRepEval_cveprograma"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapRepEval_idalu"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapRepEval_curp"> ... </label>');
                $('#pnlListadoAlumnos').append('<div id="pnlTblAlumCapRepEval">  <div id="scrlAlumCapRepEval" class="scrollTable"></div>  </div>');
            
    /************************* Fecha: 23-05-2025 ***************************************************************************/        
        
    /************************* Agregado para la captura de observaciones por materia para Primaria *************************/            
            $('#pnlCaptuRepEval').append('<div id="pnlCapturaDeRecomedaciones"></div>'); //pnlCapturaDeEvaluaciones                
                $('#pnlCapturaDeRecomedaciones').append('<div id="pnlLengua" class="panel"><div class="tituloPanel">Lengua</div></div>');                    
                    $('#pnlLengua').append('<div id="pnlHablaOtraLengua"> '
                                    + '<label id="lblOtraLengua">Lengua indígena (opcional): <select id="cbxOtrasLenguas" name="cbxOtrasLenguas"></select></label> </div>');
                    $('#pnlLengua').append('<div id="btnGuardarDatosComp" class="singleButton"><label class="alinearHoriz vertMarginable"><span class="iconButton icon-disquete"></span>Guardar lengua</label></div>');
                
            if(jsCaptuRepEval.tblPrincipal_cveplan === "1" ) {                    
                $('#pnlCapturaDeRecomedaciones').append('<div id="pnlRecomendaciones" class="panel"></div>');  //pnlEvaluaciones
                    $('#pnlRecomendaciones').append('<div id="pnlCambiarEval"></div>');
                        $('#pnlCambiarEval').append('<ul id="ulBtnEvalAnt" class="buttonBar alinearHoriz"> <li><a href="#" id="btnAnteriorNumEval"><label class="iconBtnBimEvalAnt iconBtnRedondo icon-arrow-left4"></label>Eval. Ant.</a></li> </ul>');
                        $('#pnlCambiarEval').append('<label id="lblNumEval" class="alinearHoriz"> ... </label>');
                        $('#pnlCambiarEval').append('<ul id="ulbtnEvalSig" class="buttonBar alinearHoriz"> <li><a href="#" id="btnSiguienteNumEval">Sig. Eval.<label class="iconBtnBimEvalSig iconBtnRedondo icon-arrow-right4"></label></a></li> </ul>');
                    $('#pnlRecomendaciones').append('<div id="scrlRecomXMat"></div>'); // antes scrlAvancesXEval
                    $('#pnlRecomendaciones').append('<div id="pnlMensajeGuardar">  <label id="lblMensajeGuardar" title="No olvide guardar la evaluación por cada alumno.">No olvide guardar las sugerencias y recomendaciones por cada alumno.<label>  </div>');
                    $('#pnlRecomendaciones').append('<ul class="buttonBar"> <li><a href="#" id="btnGuardarRecom"><label class="iconBtnGuardar icon-disquete"></label>Guardar captura del alumno</a></li>  </ul>');
            }   /*era pnlScrlCapturaDeEvaluaciones*/                     
                
                
                $('#pnlCapturaDeRecomedaciones').append('<ul class="buttonBar">'+                                                    
                                                    /*'<li><a href="#" id="btnLimpiarCapRepEval"><label class="middleHoriz icon-brocha"></label>Limpiar captura</a></li>'+*/
                                                    '<li><a href="#" id="btnAnteriorGrupo"><label class="iconBtnGpoAnt middleHoriz iconBtnRedondo icon-arrow-left4"></label>Gpo. anterior</a></li>' +
                                                    '<li><a href="#" id="btnSiguienteGrupo">Siguiente gpo.<label class="iconBtnGpoSig middleHoriz iconBtnRedondo icon-arrow-right4"></label></a></li>' +
                                                '</ul>');
                
        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnRegresar_CaptuRepEval").on("click",function(){ frmwCaptuRepEval_Close(); });
    $("#btnAnteriorNumEval").on('click',function(){ btnAnteriorNumEval_RecomPrim_Click(); return false; });
    $("#btnSiguienteNumEval").on('click',function(){ btnSiguienteNumEval_RecomPrim_Click(); return false; });
    /*$("#btnInserFila").on("click",function(){ btnInserFila_Click(); return false; });
    $("#btnQuitarFila").on("click",function(){ btnQuitarFila_Click(); return false; });*/
    $("#btnGuardarRecom").on('click',function(){ btnGuardarCapRepEval_Click ();  return false;});
    $("#btnGuardarDatosComp").on('click',function(){ btnGuardarDatosComp_Click(); return false; });
    /*$("#btnLimpiarCapRepEval").on('click',function(){ limpiarDatosDeCaptura (); });*/
    $("#btnAnteriorGrupo").on('click',function(){ btnAnteriorGrupo_CapRepEval_Click(); return false; });
    $("#btnSiguienteGrupo").on('click',function(){ btnSiguienteGrupo_CapRepEval_Click();  return false;});
}

function frmwCaptuRepEval_FormActivate ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaReEv", metodo:"foAc", califCicEscIn:sisVars.cicescin, cicescin:sisVars.cicescin, 
        tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct, tblPrincipal_modalidad:jsCaptuRepEval.tblPrincipal_modalidad,
        tblPrincipal_cveplan:jsCaptuRepEval.tblPrincipal_cveplan, tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, 
        tblPrincipal_grupo:jsCaptuRepEval.tblPrincipal_grupo, eval:jsCaptuRepEval.eval
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
                    jsCaptuRepEval.cicescin = sisVars.cicescin;
                    jsCaptuRepEval.capRepOf=result.capRepOf;
                    jsCaptuRepEval.califCicEscIn=datos.califCicEscIn;
                    jsCaptuRepEval.tipoCambCic = result.tipoCambCic;
                    
                    jsCaptuRepEval.paqueteMatsDefault = result.paqueteMatsDefault;
                    
                    //----------- Activamos o desactivamos componentes -------------
                    boton_setVisible(result.btnSiguienteCiclo_Visible,'btnSiguienteCiclo');
                    boton_setVisible(result.btnAnteriorCiclo_Visible,'btnAnteriorCiclo');
                    if (!result.btnAnteriorGpo_Enabled)     boton_setEnabled(result.btnAnteriorGpo_Enabled,'btnAnteriorGpo');
                    if (!result.btnSiguienteGpo_Enabled)    boton_setEnabled(result.btnSiguienteGpo_Enabled,'btnSiguienteGpo');
                    
                    //--------------- Asignamos datos a componentes ----------------
                    $("#lblCiclo").text(result.lblCiclo);
                    $('#lblNumEval').text(result.lblNumEval);                    

                    //Cargamos las lenguas
                    $("#cbxOtrasLenguas").append("<option value='' selected></option>");
                    $.each(result.lenguas,function(clave,valor) {
                           $("#cbxOtrasLenguas").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"'>"+valor.trim()+"</option>");                        
                    });
                    
                    
                    insertarTablaTblAlumCapRepEval (result.tblAlumCapRepEval);
                    insertarTabla_Recom_EvalPrim(result);
                    //insertarDatosDeCaptura_RepEval (result);
                    cerrarLoading();
                    break;
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

function insertarDatosDeCaptura_RepEval (result)
{
    //--------------- Asignamos datos a componentes ----------------
    var tblAlumCapRepEval={cveprograma:"", idalu:"", curp:""};
        
    if (result.tblAlumCapRepEval.length>0){
    
        tblAlumCapRepEval.cveprograma = result.tblAlumCapRepEval[0].cveprograma;
        tblAlumCapRepEval.idalu = result.tblAlumCapRepEval[0].idalu;
        tblAlumCapRepEval.curp= result.tblAlumCapRepEval[0].curp;
    }
    jsCaptuRepEval.tblAlumCapRepEval_idalu = tblAlumCapRepEval.idalu;

    setDatosLabel_CapRepEval ("AlumCapRepEval", tblAlumCapRepEval.cveprograma, tblAlumCapRepEval.idalu==="-1"?"":tblAlumCapRepEval.idalu, tblAlumCapRepEval.curp);
    //document.getElementById('chkRiesgosAlerta1').checked = result.alerta1==="t";
    //document.getElementById('chkRiesgosAlerta2').checked = result.alerta2==="t";
    //document.getElementById('chkRiesgosAlerta3').checked = result.alerta3==="t";
    //if (result.tutoria === "S")
    //    document.getElementById('rbnSiTutoria').checked = true;
    //else if (result.tutoria === "N")
    //    document.getElementById('rbnNoTutoria').checked = true;
    //$("#txtaHabEscritura").val(result.or_escritura);|
    //$("#txtaHabLectura").val(result.or_lectura);
    //$("#txtaHabMatematica").val(result.or_mate);
    //$("#txtaRecomendGrales").val(result.obsrec_gral);
    $("#txtaRecomendGrales_eval1").val(result.obsrec_gral_eval1);
    $("#txtaRecomendGrales_eval2").val(result.obsrec_gral_eval2);
    $("#txtaRecomendGrales_eval3").val(result.obsrec_gral_eval3);

    $("#cbxOtrasLenguas").val((result.cvelengua.trim()==="ESP")?"":result.cvelengua.trim());

    //--------------------- Rellenamos tablas ----------------------
    //insertarTablasConDatosDeCaptura(result.apoyoHabXBim, result.tblObsYRecomXBimYAsig, result.preguntasCompLectora, result.tblEvalLec);
    $("#pnlScrlCapturaDeEvaluaciones").scrollTop (0);    
}

function insertarTablasConDatosDeCaptura(apoyoHabXBim, tblObsYRecomXBimYAsig, preguntasCompLectora, tblEvalLec )
{
    //---------------------- Insertamos datos de captura ----------------------\\
    //insertarTablaApoyoHab(apoyoHabXBim);   //26-06-2019
    insertarTablaObsYRecomXBimYAsig(tblObsYRecomXBimYAsig);
    //---------------- Insertamos tabla de Comprensión Lectora ----------------\\
    //insertTablaEvalComprenLect (preguntasCompLectora, tblEvalLec); //26-06-2019
}

function insertarTablaTblAlumCapRepEval (tblAlumCapRepEval)
{
    var tabla = new Tabla();
    //---------------------- Dibujamos la tabla tblAlumCapRepEval ----------------------\\
    tabla.create("scrlAlumCapRepEval","tblAlumCapRepEval",tblAlumCapRepEval, ["Nombre"], ["nom_tot"], null, null, false, null, function(index){
        tblAlumCapRepEval_ChangeSelectedItem (index);
    }, null);
    
    tabla.setSelectedRow ('tblAlumCapRepEval', 0);
}

function insertarTabla_Recom_EvalPrim(result) //tblRecomXMat
{
    var mensaje = new Mensajes();    
    
        //--------------- Asignamos datos a componentes ----------------
    var tblAlumCapRepEval={cveprograma:"", idalu:"", curp:""};
        
    if (result.tblAlumCapRepEval.length>0){
    
        tblAlumCapRepEval.cveprograma = result.tblAlumCapRepEval[0].cveprograma;
        tblAlumCapRepEval.idalu = result.tblAlumCapRepEval[0].idalu;
        tblAlumCapRepEval.curp= result.tblAlumCapRepEval[0].curp;
    }
    jsCaptuRepEval.tblAlumCapRepEval_idalu = tblAlumCapRepEval.idalu;
    setDatosLabel_CapRepEval ("AlumCapRepEval", tblAlumCapRepEval.cveprograma, tblAlumCapRepEval.idalu==="-1"?"":tblAlumCapRepEval.idalu, tblAlumCapRepEval.curp);                        
    //---------------------- Ponemos los datos de las evaluaciones ----------------------\\
    insertarTabla_RecomXMat_EvalPrim(result.tblRecomXMat);
    /******************  Insertamos la lengua del alumno ***********************************/
    $("#cbxOtrasLenguas").val((result.cvelengua.trim()==="ESP")?"":result.cvelengua.trim());
    
    if (jsCaptuRepEval.evalOf)
        $("#pnlEvalPreescolar .avances").on('click',function(){ mensaje.EvalPreescolar("EVALUACION_OFICIALIZADA"); });
    $("#scrlRecomXMat").scrollTop (0);
}


function insertarTabla_RecomXMat_EvalPrim(tblRecomXMat)
{
    var tablaRecom;
    var casoObjOfic = "textarea"; //(jsEvalPree.evalOf)?"label":"textarea";
    var title = "Escriba máximo 300 caracteres.";
    
    $("#tblRecomXMat").remove();                                            //Limpiamos los datos
    $('#scrlRecomXMat').append('<div id="tblRecomXMat"></div>');  //antes tblAvancesXEval
    
    tablaRecom =  (tblRecomXMat.length>0)?tblRecomXMat:jsCaptuRepEval.paqueteMatsDefault;
    
    
    for (var i=0; i<tablaRecom.length; i++){
        title = "Escriba máximo 300 caracteres.";
        $('#tblRecomXMat').append('<div class="areaEval panel">'
                                            +'<label class="tituloPanel">'+tablaRecom[i].desmat+'</label>'
                                            +'<'+casoObjOfic+' id="txa'+tablaRecom[i].cvetipmat+'_'+tablaRecom[i].cvemat+'" name="txa'+tablaRecom[i].cvetipmat+'_'+tablaRecom[i].cvemat+'" class="alinearHoriz avances" maxlength="300" placeholder="'+title+'" title="'+title+'">'
                                                + tablaRecom[i].avances
                                            +'</'+casoObjOfic+'>'
                                     +'</div>');
    }    
}

function insertarTablaApoyoHab (apoyoHabXBim)
{
    for (var i=0; i<apoyoHabXBim.length; i++)
    {
        switch (apoyoHabXBim[i].cvehabilidad)
        {
            case 'ESC':
                    document.getElementById("chkHabEscritura"+apoyoHabXBim[i].numeval).checked = true;
                break;
            case 'LEC':
                    document.getElementById("chkHabLectura"+apoyoHabXBim[i].numeval).checked = true;
                break;
            case 'MAT':
                    document.getElementById("chkHabMatematica"+apoyoHabXBim[i].numeval).checked = true;
                break;
        }
    }
}

function insertarTablaObsYRecomXBimYAsig (tblObsYRecomXBimYAsig)
{
    var tabla = new Tabla();
    $("#tblObsYRecomXBimYAsig").remove();
    tabla.create("scrlObsYRecomXBimYAsig","tblObsYRecomXBimYAsig",tblObsYRecomXBimYAsig, ["EVALUACIÓN","OBSERVACIONES Y RECOMENDACIONES"], ["numeval","obs_rec_gral"], ["text","textarea"], ["","","",""], true, null, null, null);
    
    /*********** Insertamos el catálogo para los combos de bimestre y asignatura y establecemos el dato que trae cada fila ***********/
    //tabla.setDataToComboboxCol ("tblObsYRecomXBimYAsig", 0, null, jsCaptuRepEval.cbxBimestres,true, false);
    //tabla.setDataToComboboxCol ("tblObsYRecomXBimYAsig", 1, jsCaptuRepEval.cbxMateriasAlumno, jsCaptuRepEval.cveMats,true, false);
    //A cada columna le seleccionamos su dato de bimestre y asignatura en su combo correspondiente
    /*for (var i=0; i<tblObsYRecomXBimYAsig.length; i++){              
        $("#pnlCaptuRepEval #tblObsYRecomXBimYAsig_cbx_f"+i+"_c0").val(tblObsYRecomXBimYAsig[i].numeval);
        $("#pnlCaptuRepEval #tblObsYRecomXBimYAsig_cbx_f"+i+"_c1").val(tblObsYRecomXBimYAsig[i].clavesmat);
    }*/
    /***********************************************************************************************/
    //$("#tblObsYRecomXBimYAsig .coltblObsYRecomXBimYAsig_col2 textarea").attr({maxlength:"245", placeholder:"Escriba máximo 245 caracteres.", title:"Escriba máximo 245 caracteres."});
    $("#tblObsYRecomXBimYAsig .coltblObsYRecomXBimYAsig_col2 textarea").attr({maxlength:"315", placeholder:"Escriba máximo 315 caracteres.", title:"Escriba máximo 315 caracteres."});
}

function insertTablaEvalComprenLect (preguntasCompLectora, tblEvalLec)
{
    var f, c;
    var nombreTabla="tblEvalLec", aspectos, frecuencia="", columnasOcultas="";
    var colName=["preguntas","S1","S2","S3","S4","CS1","CS2","CS3","CS4","EO1","EO2","EO3","EO4","RA1","RA2","RA3","RA4"];
    var posColsOcultas = ["idpregunta"];
    var filaIdPregunta = new Array();
    
    $("#tblEvalLec tbody tr").remove();
    for (f=0; f<preguntasCompLectora.length; f++)
    {
        filaIdPregunta[preguntasCompLectora[f].idpregunta]=f;
        aspectos="<td class='preguntaEvalLec col"+nombreTabla+"_col0 "+nombreTabla+"_"+colName[0]+"_f"+f+" "+nombreTabla+"_col0' id='"+nombreTabla+"_td_f"+f+"_c0'>"+preguntasCompLectora[f].descrpreg+"</td>";
        for (c=1; c<=16; c++)
            frecuencia += "<td class='checksEvalLec col"+nombreTabla+"_col"+c+" "+nombreTabla+"_"+colName[c]+"_f"+f+" "+nombreTabla+"_col"+c+"' id='"+nombreTabla+"_td_f"+f+"_c"+c+"'>"+
                            "<input type='checkbox' id='"+nombreTabla+"_chk_f"+f+"_c"+c+"' name='"+nombreTabla+"_chk_f"+f+"_c"+c+"' value='f'>"+
                          "</td>";
        for (c=0; c<posColsOcultas.length; c++)                         //Obtenemos las columnas que serán ocultas al usuario
            columnasOcultas+="<input type='hidden' class='"+nombreTabla+"_"+posColsOcultas[c]+"_f"+f+"' id='"+nombreTabla+"_"+posColsOcultas[c]+"_f"+f+"' value='"+preguntasCompLectora[f].idpregunta+"'/> ";//posColsOcultas[c]
        
        $('#tblEvalLec tbody').append("<tr class='fila"+nombreTabla+"' id='"+nombreTabla+"_f"+f+"'>"+aspectos+frecuencia+columnasOcultas+"</tr>");
        frecuencia = "";
        columnasOcultas="";
    }
    
    for (var i=0; i<tblEvalLec.length; i++){
        f = filaIdPregunta[tblEvalLec[i].idpregunta];
        $("#pnlCaptuRepEval #tblEvalLec ."+nombreTabla+"_"+(tblEvalLec[i].cvefrecuencia+tblEvalLec[i].cvemeseval)+"_f"+f+" input[type='checkbox']").attr('checked', true);
    }
}

function tblAlumCapRepEval_ChangeSelectedItem (index)
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    var tblAlumCapRepEval_selRow = tabla.getRow ("tblAlumCapRepEval", index, ['cveprograma','idalu','curp'], null, "JSON");
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaReEv", metodo:"tbAlCaReEv_ChSeIt", califCicEscIn:jsCaptuRepEval.califCicEscIn, tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct, 
        tblPrincipal_cveplan:jsCaptuRepEval.tblPrincipal_cveplan, tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, 
        tblAlumCapRepEval_idalu:tblAlumCapRepEval_selRow.idalu, eval: jsCaptuRepEval.eval, cveprograma: jsCaptuRepEval.tblPrincipal_cveprograma
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
                    tabla.setSelectedRow ('tblAlumCapRepEval', index);
                    jsCaptuRepEval.tblAlumCapRepEval_idalu=tblAlumCapRepEval_selRow.idalu;
                    setDatosLabel_CapRepEval ("AlumCapRepEval", tblAlumCapRepEval_selRow.cveprograma, tblAlumCapRepEval_selRow.idalu==="-1"?"":tblAlumCapRepEval_selRow.idalu, tblAlumCapRepEval_selRow.curp);
                    insertarTabla_RecomXMat_EvalPrim(result.tblRecomXMat);
                    /******************  Insertamos la lengua del alumno ***********************************/
                    $("#cbxOtrasLenguas").val((result.cvelengua.trim()==="ESP")?"":result.cvelengua.trim());
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

function setDatosLabel_CapRepEval (caso, cveprograma, idalu, curp)
{
    if (caso==="AlumCapRepEval"){
        $('#lblTblPrincipal_cct').text(jsCaptuRepEval.tblPrincipal_cct);
        $('#lblTblPrincipal_grado').text(jsCaptuRepEval.tblPrincipal_grado);
        $('#lblTblPrincipal_grupo').text(jsCaptuRepEval.tblPrincipal_grupo);
        $('#lblTblAlumCapRepEval_cveprograma').text(cveprograma);
        $('#lblTblAlumCapRepEval_idalu').text(idalu);
        $('#lblTblAlumCapRepEval_curp').text(curp);
    }
}

function limpiarDatosDeCaptura ()
{
    
    var tabla = new Tabla();
    $($("#pnlCaptuRepEval #pnlCapturaDeEvaluaciones select")).prop('selectedIndex', 0);
    $("#pnlCaptuRepEval #pnlCapturaDeEvaluaciones textarea").val("");
    /*$('#pnlCaptuRepEval #pnlCapturaDeEvaluaciones input[name="rbnTutoria"]').prop('checked', false);
    $('#pnlCaptuRepEval #pnlCapturaDeEvaluaciones input[type="checkbox"]').prop('checked', false);*/
    
    var numFilas = tabla.getNumRows("tblObsYRecomXBimYAsig");
    for (var i=0; i<numFilas; i++)
        tabla.removeRow("tblObsYRecomXBimYAsig",0);
}

function btnAnteriorNumEval_RecomPrim_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","desplazarce entre sus evaluaciones");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"CaReEv", metodo:"btAnNuEv", tblAlumCapEval_cicescini:jsCaptuRepEval.califCicEscIn, califCicEscIn:jsCaptuRepEval.califCicEscIn, 
            tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaptuRepEval.tblPrincipal_cveplan, tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCaptuRepEval.tblPrincipal_grupo, numeval:jsCaptuRepEval.eval, tblAlumCapRepEval_idalu:jsCaptuRepEval.tblAlumCapRepEval_idalu,
            evalMin:jsCaptuRepEval.evalMin, evalMax:jsCaptuRepEval.evalMax, cveprograma: jsCaptuRepEval.tblPrincipal_cveprograma
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
                        $('#lblNumEval').text(result.lblNumEval);
                        jsCaptuRepEval.eval = result.eval;
                        jsCaptuRepEval.evalOf = result.evalOf;                                                                                                                               
                        insertarTabla_RecomXMat_EvalPrim(result.tblRecomXMat);
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
}

function btnSiguienteNumEval_RecomPrim_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","desplazarce entre sus evaluaciones");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"CaReEv", metodo:"btSiNuEv", tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct, tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCaptuRepEval.tblPrincipal_grupo, numeval:jsCaptuRepEval.eval, tblAlumCapRepEval_idalu:jsCaptuRepEval.tblAlumCapRepEval_idalu,
            tblAlumCapEval_cicescini:jsCaptuRepEval.califCicEscIn, evalMin:jsCaptuRepEval.evalMin, evalMax:jsCaptuRepEval.evalMax, cveprograma: jsCaptuRepEval.tblPrincipal_cveprograma
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
                        $('#lblNumEval').text(result.lblNumEval);
                        jsCaptuRepEval.eval = result.eval;
                        jsCaptuRepEval.evalOf = result.evalOf;
                        insertarTabla_RecomXMat_EvalPrim(result.tblRecomXMat);
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
}

function btnGuardarCapRepEval_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblAvancesXEvalMat = new Array(), textoEscrito="";
  
    /*if (jsCaptuRepEval.capRepOf)
        mensaje.CapRepEval("CAP_REP_OFICIALIZADA");
         else*/ 
    if ( tabla.getSelectedIndexRow("tblAlumCapRepEval") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar sus avances");    
    else{        
        for (var i=0; i<jsCaptuRepEval.paqueteMatsDefault.length; i++){            
            tblAvancesXEvalMat[i]=jsCaptuRepEval.paqueteMatsDefault[i].cveprograma+"~"+jsCaptuRepEval.paqueteMatsDefault[i].cvetipmat+"~"+jsCaptuRepEval.paqueteMatsDefault[i].cvemat+"~"+$("#txa"+jsCaptuRepEval.paqueteMatsDefault[i].cvetipmat+"_"+jsCaptuRepEval.paqueteMatsDefault[i].cvemat).val().trim();
            textoEscrito+=$("#txa"+jsCaptuRepEval.paqueteMatsDefault[i].cvetipmat+"_"+jsCaptuRepEval.paqueteMatsDefault[i].cvemat).val().trim();
        }
        if (textoEscrito.trim() === "")
            return mensaje.General ("NADA_QUE_GUARDAR");
                
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"CaReEv", metodo:"btGdCaReEv_Cl", tblAvancesXEvalYMat:tblAvancesXEvalMat,califCicEscIn:jsCaptuRepEval.califCicEscIn, 
            tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct, eval:jsCaptuRepEval.eval,
            tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, tblPrincipal_grupo:jsCaptuRepEval.tblPrincipal_grupo, 
            tblAlumCapRepEval_idalu:jsCaptuRepEval.tblAlumCapRepEval_idalu, cveplan: jsCaptuRepEval.tblPrincipal_cveplan,
            cvelengua:$('#cbxOtrasLenguas').val()===""?"ESP":$('#cbxOtrasLenguas').val()            
            /*txtaRecomendGrales_eval1:$('#txtaRecomendGrales_eval1').val(),txtaRecomendGrales_eval2:$('#txtaRecomendGrales_eval2').val(),
            txtaRecomendGrales_eval3:$('#txtaRecomendGrales_eval3').val()*/            
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
                        var numFilas = tabla.getNumRows ('tblAlumCapRepEval');
                        var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapRepEval');
                        if (posSelActual < (numFilas-1))
                            tblAlumCapRepEval_ChangeSelectedItem (posSelActual+1);  //Seleccionamos la siguiente fila de tblAlumCapRepEval y obtenemos los datos correspondientes
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

function btnInserFila_Click()
{
    var numRows;
    var tabla = new Tabla ();
    var fila = new Array();
    var columnas={numeval:"",desmat:"",obsv_esp:"",apoyo_esp:"", cvetipmat:"", cvemat:""};
    fila[0]=columnas;
    tabla.addRows (-1,"tblObsYRecomXBimYAsig",fila, ["numeval","desmat","obsv_esp","apoyo_esp"], ["combobox","combobox","textarea","textarea"], null, true, null, null, null);
    numRows = tabla.getNumRows("tblObsYRecomXBimYAsig");
    /*********** Insertamos el catálogo para los combos bimestre ***********/
    tabla.setDataToComboboxCol ("tblObsYRecomXBimYAsig", 0, null, jsCaptuRepEval.cbxBimestres,true, false, null, numRows-1, null, {index:[[numRows-1,0]]});
    /*********** Insertamos el catálogo para los combos materias ***********/
    tabla.setDataToComboboxCol ("tblObsYRecomXBimYAsig", 1, jsCaptuRepEval.cbxMateriasAlumno, jsCaptuRepEval.cveMats,true, false, null,numRows-1, null, {index:[[numRows-1,0]]});
    /*********** Bajamos el scroll a la fila agregada ***********/
    $('#pnlCaptuRepEval #scrlObsYRecomXBimYAsig').animate({ scrollTop: $('#pnlCaptuRepEval #tblObsYRecomXBimYAsig').height() }, 1500);
    
    $("#tblObsYRecomXBimYAsig .coltblObsYRecomXBimYAsig_col2 textarea").attr({maxlength:"245", placeholder:"Escriba máximo 245 caracteres.", title:"Escriba máximo 245 caracteres."});
    $("#tblObsYRecomXBimYAsig .coltblObsYRecomXBimYAsig_col3 textarea").attr({maxlength:"315", placeholder:"Escriba máximo 315 caracteres.", title:"Escriba máximo 315 caracteres."});
}

function btnQuitarFila_Click()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla ();
    
    if (!tabla.hasSelectedRow("tblObsYRecomXBimYAsig"))
        mensaje.General("NO_SELEC","a observación","eliminarla");
    else{
        if ( mensaje.confirmDialog("ELIMINAR","el registro") )
            //if ( validarEntradasDeExamenesExtraord ("tblCalif1ro", "tblExmExt1ro", jsSeHi.tblExmExt1ro_OldValues) ){
                tabla.removeRow("tblObsYRecomXBimYAsig",tabla.getSelectedIndexRow("tblObsYRecomXBimYAsig"));
            //    btnGuardaExamExt1ro_Click();
            //}
    }
}

function validarEntradas_CapRepEval ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var tblObsYRecomXBimYAsig = tabla.getTable("tblObsYRecomXBimYAsig",["numeval","desmat","obsv_esp","apoyo_esp"], null,"JSON");
    //var mensaje = new Mensajes ();
    
    /*if (document.getElementById('cbxOtrasLenguas').selectedIndex<1){  //Porque la pos 0 tiene blanco
        mensaje.General ("ESPECIFIQUE_DATO","lengua o quite la palomita");    $("#cbxOtrasLenguas").focus();  return false;
    }*/
    var enterEn, id, huboEnters=false;
    $('#pnlCaptuRepEval textarea').each(function(){
        if ($(this).val().indexOf('\n')>=0) {
            id = $(this).attr("id").indexOf('tblObsYRecomXBimYAsig_txta')===-1?$(this).attr("id"):"tblObsYRecomXBimYAsig_txta";
            switch (id) {
                case "txtaHabEscritura":
                    enterEn = "Evaluación de habilidades (Escritura)";
                    break;
                case "txtaHabLectura":
                    enterEn = "Evaluación de habilidades (Lectura)";
                    break;
                case "txtaHabMatematica":
                    enterEn = "Evaluación de habilidades (Matemática)";
                    break;
                case "tblObsYRecomXBimYAsig_txta":
                    var concepto = $(this).attr("id").replace("tblObsYRecomXBimYAsig_txta","");
                    var filacol = concepto.split("_");
                    var filaColName = ["","","Observaciones específicas","Recomendaciones"];
                    var fila = filacol[1].replace("f","");
                    var columna = filacol[2].replace("c","");
                    concepto =  concepto.replace("_f","Fila: ").replace("_c",", Columna: ");
                    enterEn = "Observaciones y/o recomendaciones por evaluación y asignatura en Fila: "+fila+", Columna:"+filaColName[parseInt(columna)];
                    break;
                case "txtaRecomendGrales":
                    enterEn = "Observaciones y/o recomendaciones generales";
                    break;
            }
            mensaje.General("GENERAL","No se aceptan saltos de línea (enters). Se detectó al menos uno en "+enterEn);
            huboEnters = true;
            return huboEnters;
        }
        
    });
    if (huboEnters)
        return false;
    
    /*Verificamos la tabla Observaciones y/o Recomendaiones por Bimestre y Asignatura que si agregaron un renglón, le hayan asignado todo los datos necesarios*/
    for (var i=0; i<tblObsYRecomXBimYAsig.length; i++){
        if ( tblObsYRecomXBimYAsig[i].numeval==="" ){
            return mensaje.General("ESPECIFIQUE_DATO","evaluación en la "+(i+1)+"a fila de la tabla Observaciones y/o Recomendaciones por Evaluación y Asignatura");
        }else if ( tblObsYRecomXBimYAsig[i].desmat==="" ){
            return mensaje.General("ESPECIFIQUE_DATO","asignatura en la "+(i+1)+"a fila de la tabla Observaciones y/o Recomendaciones por Evaluación y Asignatura");
        }else if (tblObsYRecomXBimYAsig[i].numeval==="" || tblObsYRecomXBimYAsig[i].desmat==="" || (tblObsYRecomXBimYAsig[i].obsv_esp.trim()==="" && tblObsYRecomXBimYAsig[i].apoyo_esp.trim()==="") ){
            return mensaje.General("ESPECIFIQUE_DATO","observaciones y/o recomendaciones en la fila"+(i+1)+" de la tabla Observaciones y/o Recomendaciones por evaluación y Asignatura");
        }
    }
    
    /*Verificamos que no esté en blanco la evaluación de comprensión lectora (al menos tenga una casilla)*/
    //if ($("#pnlCaptuRepEval #tblEvalLec input:checkbox:checked").length===0 )   /*$('#pnlCaptuRepEval #tblEvalLec input[type=checkbox]').each(function(){ if (this.checked) { alert($(this).val()); } }); */
        //return mensaje.General("ESPECIFIQUE_DATO","evaluación de la comprensión lectora");
        
    return true;
}

function btnAnteriorGrupo_CapRepEval_Click()
{
    var tabla = new Tabla();
    
    /*if (jsCaptuRepEval.hayCambios) 
        btnGuardarCapRepEval_Click ();*/
    
    if ( tabla.getSelectedIndexRow ('tblPrincipal')-1 >= 0 ){
        var datos = { 
            modulo:"CaReEv", metodo:"btAnGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","cveplan"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaptuRepEval.califCicEscIn,
            eval: jsCaptuRepEval.eval, tblPrincipal_cveprograma:jsCaptuRepEval.tblPrincipal_cveprograma
        };
        sigAntGrupo_CapRepEval (datos);
    }
}

function btnSiguienteGrupo_CapRepEval_Click()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows ('tblPrincipal');
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    
    
    if (posSelActual < (numFilas-1))
    {
        if (jsCaptuRepEval.hayCambios)
            btnGuardarCapRepEval_Click ();

        var datos = { 
            modulo:"CaReEv", metodo:"btSiGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","cveplan"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaptuRepEval.califCicEscIn, 
            eval: jsCaptuRepEval.eval, tblPrincipal_cveprograma:jsCaptuRepEval.tblPrincipal_cveprograma
        };

        sigAntGrupo_CapRepEval (datos);
    }
}

function sigAntGrupo_CapRepEval (datos)
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
                    tabla.setSelectedRow ('tblPrincipal', result.tblPrincipal_selectedRow);
                    jsCaptuRepEval.tblPrincipal_grado = result.tblPrincipal_grado; 
                    jsCaptuRepEval.tblPrincipal_grupo = result.tblPrincipal_grupo;
                    //jsCaptuRepEval.paqueteMatsDefault = result.paqueteMatsDefault;       
                    insertarTablaTblAlumCapRepEval (result.tblAlumCapRepEval);
                    insertarTabla_Recom_EvalPrim(result);                                        
                    cerrarLoading();
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

function btnGuardarDatosComp_Click ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar sus recomendaciones");
    /*else if (jsCaptuRepEval.evalOf1 && jsCaptuRepEval.evalOf2 && jsCaptuRepEval.evalOf3)
        mensaje.EvalPreescolar("TODO_OFICIALIZADO");*/
    else{
        var datos = {
            modulo:"CaReEv", metodo:"btGuDaCo", califCicEscIn:jsCaptuRepEval.califCicEscIn, tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct, 
            tblAlumCapRepEval_idalu:jsCaptuRepEval.tblAlumCapRepEval_idalu, 
            cvelengua:$('#cbxOtrasLenguas').val()===""?"ESP":$('#cbxOtrasLenguas').val(), 
            tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, tblPrincipal_grupo:jsCaptuRepEval.tblPrincipal_grupo
            
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
                        if(jsCaptuRepEval.tblPrincipal_cveplan==="2") {
                            var numFilas = tabla.getNumRows ('tblAlumCapRepEval');
                            var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapRepEval');
                            if (posSelActual < (numFilas-1))
                                tblAlumCapRepEval_ChangeSelectedItem (posSelActual+1);  //Seleccionamos la siguiente fila de tblAlumCapRepEval y obtenemos los datos correspondientes
                        }
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
}
