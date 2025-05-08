/* 
    Creado el : 19-may-2017, 18:12:40
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaptuRepEval;

function frmwCaptuRepEval_Show(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo)
{
    jsCaptuRepEval = {
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_cct: tblPrincipal_cct,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo:tblPrincipal_grupo,
        
        cicescin:null,
        capRepOf: null,
        tblAlumCapRepEval_idalu:null,
        califCicEscIn:null,
        cbxBimestres:["1","2","3"],
        cbxMateriasAlumno:new Array(),
        cveMats:new Array(),
        hayCambios:false
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
                    $('#pnlTblAlumCapRepEval').append('');
    //..................................................................................................................................................................
            $('#pnlCaptuRepEval').append('<div id="pnlCapturaDeEvaluaciones" class="panel">'+
                                            '<div class="panel"><div id="pnlScrlCapturaDeEvaluaciones"></div></div>' +
                                            '<div id="pnlBotonesDeGestion" ></div>'+
                                         '</div>');
                $('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlLengua" class="panel"><div class="tituloPanel">LENGUA.</div>'
                                                            //+'<div id="pnlHablaEspañol"> <label id="lblHablaEspañol">Habla Español: <input type="radio" id="rbnSiHablaEspañol" name="rbgHablaEspañol" value="rbnSiHablaEspañol" tabindex="115">Sí <input type="radio" id="rbnNoHablaEspañol" name="rbgHablaEspañol" value="rbnNoHablaEspañol" tabindex="116">No</label> </div>'
                                                            +'<div id="pnlHablaOtraLengua"> <label id="lblOtraLengua">Lengua indígena (opcional): <select id="cbxOtrasLenguas" name="cbxOtrasLenguas"></select></label> </div>'
                                                        +'</div>');
                /*$('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlRiesgos" class="panel"><div class="tituloPanel">MARQUE SI EL APRENDIZAJE Y/O LA PROMOCIÓN DE GRADO DEL(DE LA) ALUMNO(A) SE ENCUENTRA(N) EN RIESGO.</div></div>');
                    $('#pnlRiesgos').append('<div id="pnlRiesgos_captura"></div>');
                        $('#pnlRiesgos_captura').append('<div id="pnlRiesgos_capturaAlerta1" class="riesgosAlerta marco alinearHoriz"></div>');
                            $('#pnlRiesgos_capturaAlerta1').append('<label>ALERTA</label><label class="lblRiesgosAlertaBim">(BIMESTRE II)</label>');
                            $('#pnlRiesgos_capturaAlerta1').append('<input type="checkbox" id="chkRiesgosAlerta1" value="chkAlerta1">');
                        $('#pnlRiesgos_captura').append('<div id="pnlRiesgos_capturaAlerta2"  class="riesgosAlerta marco alinearHoriz"></div>');
                            $('#pnlRiesgos_capturaAlerta2').append('<label>ALERTA</label><label class="lblRiesgosAlertaBim">(BIMESTRE III)</label>');
                            $('#pnlRiesgos_capturaAlerta2').append('<input type="checkbox" id="chkRiesgosAlerta2" value="chkAlerta2">');
                        $('#pnlRiesgos_captura').append('<div id="pnlRiesgos_capturaAlerta3"  class="riesgosAlerta marco alinearHoriz"></div>');
                            $('#pnlRiesgos_capturaAlerta3').append('<label>ALERTA</label><label class="lblRiesgosAlertaBim">(BIMESTRE IV)</label>');
                            $('#pnlRiesgos_capturaAlerta3').append('<input type="checkbox" id="chkRiesgosAlerta3" value="chkAlerta3">');
               $('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlTutoria" class="panel"><div class="tituloPanel">ASISTIÓ A TUTORÍA.</div></div>');
                    $('#pnlTutoria').append('<label><input type="radio" name="rbnTutoria" id="rbnSiTutoria" value="SI"> SÍ</label>\n\
                                             <label><input type="radio" name="rbnTutoria" id="rbnNoTutoria" value="NO"> NO</label>'
                                            );
                $('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlHabilidades" class="panel"><div class="tituloPanel">EVALUACIÓN DE HABILIDADES FUNDAMENTALES PARA EL APRENDIZAJE.</div></div>');
                    $('#pnlHabilidades').append('<div id="pnlHabilidades_instrucciones">¿Requiere apoyo fuera del horario escolar?</div>');
                    $('#pnlHabilidades').append('<div id="pnlTblHabilidades"><div id="scrlHabilidades" class="scrollTable" ></div></div>');
                        $('#scrlHabilidades').append('<table>'+
                                                            '<tr><th colspan="2">OBSERVACIONES Y/O RECOMENDACIONES</th><th>BIMESTRE</th><th>SI</th></tr>'+
                                                            '<tr><td rowspan="5" class="tblHabilidades_habilid_f0 tblHabilidades_col0"><label>ESCRITURA</label></td><td rowspan="5"><textarea id="txtaHabEscritura" maxlength="450" placeholder="Escriba máximo 450 caracteres." title="Escriba máximo 450 caracteres."></textarea></td><td class="tblHabilidades_col2">I</td><td><input type="checkbox" id="chkHabEscritura1" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">II</td><td><input type="checkbox" id="chkHabEscritura2" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">III</td><td><input type="checkbox" id="chkHabEscritura3" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">IV</td><td><input type="checkbox" id="chkHabEscritura4" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">V</td><td><input type="checkbox" id="chkHabEscritura5" ></td></tr>'+
                                                            
                                                            '<tr><td rowspan="5" class="tblHabilidades_habilid_f1 tblHabilidades_col0"><label>LECTURA</label></td><td rowspan="5"><textarea id="txtaHabLectura" maxlength="450" placeholder="Escriba máximo 450 caracteres." title="Escriba máximo 450 caracteres."></textarea></td><td class="tblHabilidades_col2">I</td><td><input type="checkbox" id="chkHabLectura1" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">II</td><td><input type="checkbox" id="chkHabLectura2" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">III</td><td><input type="checkbox" id="chkHabLectura3" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">IV</td><td><input type="checkbox" id="chkHabLectura4" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">V</td><td><input type="checkbox" id="chkHabLectura5" ></td></tr>'+
                                                            
                                                            '<tr><td rowspan="5" class="tblHabilidades_habilid_f2 tblHabilidades_col0"><label>MATEMÁTICA</label></td><td rowspan="5"><textarea id="txtaHabMatematica" maxlength="450" placeholder="Escriba máximo 450 caracteres." title="Escriba máximo 450 caracteres."></textarea></td><td class="tblHabilidades_col2">I</td><td><input type="checkbox" id="chkHabMatematica1" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">II</td><td><input type="checkbox" id="chkHabMatematica2" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">III</td><td><input type="checkbox" id="chkHabMatematica3" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">IV</td><td><input type="checkbox" id="chkHabMatematica4" ></td></tr>'+
                                                            '<tr><td class="tblHabilidades_col2">V</td><td><input type="checkbox" id="chkHabMatematica5" ></td></tr>'+
                                                          '</table>');*/
                    
                /*$('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlRecomendXBimYAsig" class="panel"><div class="tituloPanel">OBSERVACIONES Y/O RECOMENDACIONES POR EVALUACIÓN Y ASIGNATURA.</div></div>');
                    $('#pnlRecomendXBimYAsig').append('<div id="pnlRecomendaciones_instrucciones"><label>El(la) maestro(a) registrará, al concluir el segundo bimestre o en el momento del ciclo escolar en el que observe dificultades en el desempeño del(dela) alumno(a), información acerca de las necesidades de apoyo que éste(a) requiere y, las acciones que la escuela y la familia deben realizar conjuntamente con el educando para favorecer que avance en los aprendizajes esperados, establecidos en los Programas de Estudio. En caso de requerir más espacio, utilice hojas adicionales.</label></div>');
                    $("#pnlRecomendXBimYAsig").append('<div id="pnlTablaObsYRecomXBimYAsig">  <div id="scrlObsYRecomXBimYAsig" class="scrollTable"></div>  </div>');
                    $("#pnlRecomendXBimYAsig").append('<div id="pnlBotoneraGestionObsYRecomXBimYAsig">'
                                                    + '<ul id="ubtnObsYRecomXBimYAsig" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnInserFila"  class="tam2Button" title="Inserta un renglón en blanco al final de la lista para ingresar datos."><label class="iconBtnMas iconBtnRedondo  middleHoriz icon-plus"></label>Insertar renglón</a></li>'
                                                        + '<li><a href="#" id="btnQuitarFila"  class="tam2Button" title="Elimina el renglón seleccionado en la lista."><label class="iconBtnMenos iconBtnRedondo  middleHoriz icon-minus"></label>Quitar renglón</a></li>'
                                                    +'</ul> '
                                                +'</div>'); */
                /*$('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlRecomendXBimYAsig" class="panel"><div class="tituloPanel">OBSERVACIONES Y/O RECOMENDACIONES GENERALES POR EVALUACIÓN</div></div>');
                    $('#pnlRecomendXBimYAsig').append('<div id="pnlRecomendaciones_instrucciones"><label>El(la) maestro(a) registrará, al concluir el segundo bimestre o en el momento del ciclo escolar en el que observe dificultades en el desempeño del(dela) alumno(a), información acerca de las necesidades de apoyo que éste(a) requiere y, las acciones que la escuela y la familia deben realizar conjuntamente con el educando para favorecer que avance en los aprendizajes esperados, establecidos en los Programas de Estudio. En caso de requerir más espacio, utilice hojas adicionales.</label></div>');
                    $("#pnlRecomendXBimYAsig").append('<div id="pnlTablaObsYRecomXBimYAsig"><div id="scrlObsYRecomXBimYAsig" class="scrollTable"></div>  </div>');
                    $("#pnlRecomendXBimYAsig").append('<div id="pnlBotoneraGestionObsYRecomXBimYAsig">'
                                                    + '<ul id="ubtnObsYRecomXBimYAsig" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnInserFila"  class="tam2Button" title="Inserta un renglón en blanco al final de la lista para ingresar datos."><label class="iconBtnMas iconBtnRedondo  middleHoriz icon-plus"></label>Insertar renglón</a></li>'
                                                        + '<li><a href="#" id="btnQuitarFila"  class="tam2Button" title="Elimina el renglón seleccionado en la lista."><label class="iconBtnMenos iconBtnRedondo  middleHoriz icon-minus"></label>Quitar renglón</a></li>'
                                                    +'</ul> '
                                                +'</div>'); */
                if(jsCaptuRepEval.tblPrincipal_cveplan === "1")
                    $('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlRecomendGrales" class="panel"><div class="tituloPanel">OBSERVACIONES Y SUGERENCIAS SOBRE LOS APRENDIZAJES.</div></div>');
                        $('#pnlRecomendGrales').append('<div id="pnlRecomendGrales_instruc"><label>Si es necesario, el(la) maestro(a) registrará las situaciones que interfieren o pueden favorecer el desempeño del(de la) alumno(a) (acoso escolar, comportamiento, valores, interacciones, higiene personal, acompañamiento de la familia en el proceso educativo, etc.).</label></div>');
                        //$('#pnlRecomendGrales').append('<div id="pnlRecomendGrales_captudatos" clas="panel"><textarea id="txtaRecomendGrales" maxlength="400" placeholder="Escriba máximo 400 caracteres." title="Escriba máximo 400 caracteres."></textarea></div>');
                        $('#pnlRecomendGrales').append('<div id="pnlRecomendGrales_captudatos" clas="panel"><label>PRIMER PERIODO</label><textarea id="txtaRecomendGrales_eval1" maxlength="400" placeholder="Escriba máximo 400 caracteres." title="Escriba máximo 400 caracteres."></textarea></div>'); 
                        $('#pnlRecomendGrales').append('<div id="pnlRecomendGrales_captudatos" clas="panel"><label>SEGUNDO PERIODO</label><textarea id="txtaRecomendGrales_eval2" maxlength="400" placeholder="Escriba máximo 400 caracteres." title="Escriba máximo 400 caracteres."></textarea></div>'); 
                        $('#pnlRecomendGrales').append('<div id="pnlRecomendGrales_captudatos" clas="panel"><label>TERCER PERIODO</label><textarea id="txtaRecomendGrales_eval3" maxlength="400" placeholder="Escriba máximo 400 caracteres." title="Escriba máximo 400 caracteres."></textarea></div>'); 

    
                /*$('#pnlScrlCapturaDeEvaluaciones').append('<div id="pnlComprensionLectora" class="panel"><div class="tituloPanel">EVALUACIÓN DE LA COMPRENSIÓN LECTORA.</div></div>');
                    $('#pnlComprensionLectora').append('<div id="pnlComprensionLectora_instruc"><label>El(la) maestro(a) registrará en el momento correspondiente los avances de la Comprensión Lectora, rellenando el circulo quedescriba la situación del(de la) alumno(a). El único objeto de estos aspectos es brindar mayor información sobre este elemento de aprendizaje indispensable para el desempeño académico de los propios educandos. Estos aspectos no deberán condicionar por si mismos la promoción de grado.</label></div>');
                    $('#pnlComprensionLectora').append('<div id="pnlTblEvalLec"><div id="scrlEvalLec" class="scrollTable"></div></div>');
                        $('#scrlEvalLec').append('<table id="tblEvalLec">' +
                                                        '<thead>' +
                                                            '<tr><th rowspan="2" id="tblEvalLec_titcol0">Los siguientes aspectos se relacionan con el desarrollo de la comprensión al leer y escribir, permitiendo informar si el (la) alumno(a):</th><th colspan="4">SIEMPRE</th><th colspan="4">CASI SIEMPRE</th><th colspan="4">EN OCASIONES</th><th colspan="4">REQUIERE APOYO ADICIONAL</th></tr>' +
                                                            '<tr><th>Ago</th><th>Nov</th><th>Mar</th><th>Jun</th> <th>Ago</th><th>Nov</th><th>Mar</th><th>Jun</th> <th>Ago</th><th>Nov</th><th>Mar</th><th>Jun</th> <th>Ago</th><th>Nov</th><th>Mar</th><th>Jun</th></tr>' +
                                                        '</thead>' +
                                                        '<tbody></tbody>' +
                                                    '</table>');*/
                
                $('#pnlBotonesDeGestion').append('<ul class="buttonBar">'+
                                                    '<li><a href="#" id="btnGuardarCapRepEval"><label class="middleHoriz icon-disquete"></label>Guardar captura</a></li>'+
                                                    '<li><a href="#" id="btnLimpiarCapRepEval"><label class="middleHoriz icon-brocha"></label>Limpiar captura</a></li>'+
                                                    '<li><a href="#" id="btnAnteriorGrupo"><label class="iconBtnGpoAnt middleHoriz iconBtnRedondo icon-arrow-left4"></label>Gpo. anterior</a></li>' +
                                                    '<li><a href="#" id="btnSiguienteGrupo">Siguiente gpo.<label class="iconBtnGpoSig middleHoriz iconBtnRedondo icon-arrow-right4"></label></a></li>' +
                                                '</ul>');
                
        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnRegresar_CaptuRepEval").on("click",function(){ frmwCaptuRepEval_Close(); });
    $("#btnInserFila").on("click",function(){ btnInserFila_Click(); return false; });
    $("#btnQuitarFila").on("click",function(){ btnQuitarFila_Click(); return false; });
    $("#btnGuardarCapRepEval").on('click',function(){ btnGuardarCapRepEval_Click ();  return false;});
    $("#btnLimpiarCapRepEval").on('click',function(){ limpiarDatosDeCaptura (); });
    $("#btnAnteriorGrupo").on('click',function(){ btnAnteriorGrupo_CapRepEval_Click(); return false; });
    $("#btnSiguienteGrupo").on('click',function(){ btnSiguienteGrupo_CapRepEval_Click();  return false;});
}

function frmwCaptuRepEval_FormActivate ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaReEv", metodo:"foAc", califCicEscIn:sisVars.cicescin, cicescin:sisVars.cicescin, tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct,
        tblPrincipal_cveplan:jsCaptuRepEval.tblPrincipal_cveplan, tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, 
        tblPrincipal_grupo:jsCaptuRepEval.tblPrincipal_grupo
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
                    
                    //for (var i=0; i<result.matsAlumno.length; i++){
                      //  jsCaptuRepEval.cbxMateriasAlumno[i]=result.matsAlumno[i].desmat;
                       // jsCaptuRepEval.cveMats[i]=result.matsAlumno[i].cvemats;
                    //}
                    
                    //----------- Activamos o desactivamos componentes -------------
                    //if (jsCaptuRepEval.tblPrincipal_cveplan!=="2")
                        //object_setVisible(false,"pnlTutoria");
                    boton_setVisible(result.btnSiguienteCiclo_Visible,'btnSiguienteCiclo');
                    boton_setVisible(result.btnAnteriorCiclo_Visible,'btnAnteriorCiclo');
                    if (!result.btnAnteriorGpo_Enabled)     boton_setEnabled(result.btnAnteriorGpo_Enabled,'btnAnteriorGpo');
                    if (!result.btnSiguienteGpo_Enabled)    boton_setEnabled(result.btnSiguienteGpo_Enabled,'btnSiguienteGpo');
                    
                    //--------------- Asignamos datos a componentes ----------------
                    $("#lblCiclo").text(result.lblCiclo);
                    //Cargamos las lenguas
                    $("#cbxOtrasLenguas").append("<option value='' selected></option>");
                    $.each(result.lenguas,function(clave,valor) {
                           $("#cbxOtrasLenguas").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"'>"+valor.trim()+"</option>");                        
                    });
                    
                    insertarTablaTblAlumCapRepEval (result.tblAlumCapRepEval);
                    insertarDatosDeCaptura_RepEval (result);
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
    //$("#txtaHabEscritura").val(result.or_escritura);
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
        tblAlumCapRepEval_idalu:tblAlumCapRepEval_selRow.idalu
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
                    
                    //-------------------- Asignamos variables ---------------------
                    /*for (var i=0; i<result.matsAlumno.length; i++){
                        jsCaptuRepEval.cbxMateriasAlumno[i]=result.matsAlumno[i].desmat;
                        jsCaptuRepEval.cveMats[i]=result.matsAlumno[i].cvemats;
                    }*/
                    
                    //--------------- Asignamos datos a componentes ----------------
                    
                    limpiarDatosDeCaptura ();
                    
                    result.tblAlumCapRepEval = new Array();
                    result.tblAlumCapRepEval[0] = tblAlumCapRepEval_selRow;
                    insertarDatosDeCaptura_RepEval (result);
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

function btnGuardarCapRepEval_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
  
    if (jsCaptuRepEval.capRepOf)
        mensaje.CapRepEval("CAP_REP_OFICIALIZADA");
    else if ( tabla.getSelectedIndexRow("tblAlumCapRepEval") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar sus avances");
    else if (validarEntradas_CapRepEval ()){
        
        /*var chkHabEscritura=[document.getElementById('chkHabEscritura1').checked+"~"+document.getElementById('chkHabEscritura2').checked+"~"+document.getElementById('chkHabEscritura3').checked+"~"+document.getElementById('chkHabEscritura4').checked+"~"+document.getElementById('chkHabEscritura5').checked];
        var chkHabLectura=[document.getElementById('chkHabLectura1').checked+"~"+document.getElementById('chkHabLectura2').checked+"~"+document.getElementById('chkHabLectura3').checked+"~"+document.getElementById('chkHabLectura4').checked+"~"+document.getElementById('chkHabLectura5').checked];
        var chkHabMatematica=[document.getElementById('chkHabMatematica1').checked+"~"+document.getElementById('chkHabMatematica2').checked+"~"+document.getElementById('chkHabMatematica3').checked+"~"+document.getElementById('chkHabMatematica4').checked+"~"+document.getElementById('chkHabMatematica5').checked]; */
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"CaReEv", metodo:"btGdCaReEv_Cl", califCicEscIn:jsCaptuRepEval.califCicEscIn, tblPrincipal_idcct:jsCaptuRepEval.tblPrincipal_idcct,
            tblPrincipal_grado:jsCaptuRepEval.tblPrincipal_grado, tblPrincipal_grupo:jsCaptuRepEval.tblPrincipal_grupo, 
            idalu:jsCaptuRepEval.tblAlumCapRepEval_idalu, cveplan: jsCaptuRepEval.tblPrincipal_cveplan,//hablaEspaniol:document.getElementById('rbnSiHablaEspañol').checked, 
            cvelengua:$('#cbxOtrasLenguas').val()===""?"ESP":$('#cbxOtrasLenguas').val(), 
            /*
            chkRiesgosAlerta1:document.getElementById('chkRiesgosAlerta1').checked, chkRiesgosAlerta2:document.getElementById('chkRiesgosAlerta2').checked, 
            chkRiesgosAlerta3:document.getElementById('chkRiesgosAlerta3').checked, 
            tutoria:document.getElementById('rbnSiTutoria').checked?'S':(document.getElementById('rbnNoTutoria').checked?'N':'-'), 
            txtaHabEscritura:$('#txtaHabEscritura').val(), txtaHabLectura:$('#txtaHabLectura').val(), txtaHabMatematica:$('#txtaHabMatematica').val(), 
            chkHabEscritura:chkHabEscritura,chkHabLectura:chkHabLectura,chkHabMatematica:chkHabMatematica, 
            tblEvalLec:tabla.getTable("tblEvalLec"), tblObsYRecomXBimYAsig:tabla.getTable("tblObsYRecomXBimYAsig"),*/
            txtaRecomendGrales_eval1:$('#txtaRecomendGrales_eval1').val(),txtaRecomendGrales_eval2:$('#txtaRecomendGrales_eval2').val(),
            txtaRecomendGrales_eval3:$('#txtaRecomendGrales_eval3').val()
            
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
    
    if (jsCaptuRepEval.hayCambios) 
        btnGuardarCapRepEval_Click ();
    
    if ( tabla.getSelectedIndexRow ('tblPrincipal')-1 >= 0 ){
        var datos = { 
            modulo:"CaReEv", metodo:"btAnGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","cveplan"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaptuRepEval.califCicEscIn
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
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaptuRepEval.califCicEscIn
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
                    //-------------------- Asignamos variables ---------------------
                    tabla.setSelectedRow ('tblPrincipal', result.tblPrincipal_selectedRow);
                    jsCaptuRepEval.tblPrincipal_grado = result.tblPrincipal_grado; 
                    jsCaptuRepEval.tblPrincipal_grupo = result.tblPrincipal_grupo;
                    /*for (var i=0; i<result.matsAlumno.length; i++){
                        jsCaptuRepEval.cbxMateriasAlumno[i]=result.matsAlumno[i].desmat;
                        jsCaptuRepEval.cveMats[i]=result.matsAlumno[i].cvemats;
                    }*/
                    
                    insertarTablaTblAlumCapRepEval (result.tblAlumCapRepEval);
                    insertarDatosDeCaptura_RepEval (result);
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