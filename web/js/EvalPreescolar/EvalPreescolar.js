/* 
    Creado el : 12-dic-2016, 17:41:40
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsEvalPree;

function frmwEvalPreescolar_Show(buildView, tblPrincipal_idcct, tblPrincipal_modalidad, tblPrincipal_cct, tblPrincipal_nombre, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cicescini)
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
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsEvalPree = {
        tblPrincipal_idcct:tblPrincipal_idcct,
        tblPrincipal_modalidad:tblPrincipal_modalidad,
        tblPrincipal_cct:tblPrincipal_cct,
        tblPrincipal_nombre:tblPrincipal_nombre,
        tblPrincipal_cveplan:tblPrincipal_cveplan,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo:tblPrincipal_grupo,
        tblPrincipal_cicescini:tblPrincipal_cicescini,
        
        hayCambiosXEval:false,
        tipoCambCic:null,
        eval:bimMin, //valor original 1
        califCicEscIn:tblPrincipal_cicescini,
        tblAlumCapEval_idalu:"",
        evalOf:false,
        evalOf1:true,
        evalOf2:true,
        evalOf3:true,
        evalMax: bimMax,
        evalMin: bimMin,
        
        paqueteMatsDefault:null
    };
    
    if (buildView){
        object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
        $("#frmwEvalPreescolar").css("display", "block");                           // Mostramos el formulario correspondiente
        frmwEvalPreescolar_Create ();
    }
            
    frmwEvalPreescolar_FormActivate ();
}


function frmwEvalPreescolar_Close()
{
    jsEvalPree=null;
    irAVentanaPrincipal ();
}

function frmwEvalPreescolar_Create ()
{
    if($('#frmfEvalPreescolar').length)
        $('#frmfEvalPreescolar').remove();
    $('#frmwEvalPreescolar').append('<fieldset id="frmfEvalPreescolar"><legend>Evaluaciones del grado '+jsEvalPree.tblPrincipal_grado+'° para el ciclo  '+jsEvalPree.tblPrincipal_cicescini+'-'+(parseInt(jsEvalPree.tblPrincipal_cicescini)+1)+'</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfEvalPreescolar").append('<div id="pnlEvalPreescolar"></div>');
        
            $('#pnlEvalPreescolar').append('<div id="pnlAlumnos"></div>');
                //$('#pnlAlumnos').append('<div id="pnlCicloEscolar"></div>');
                    //$('#pnlCicloEscolar').append('<ul class="buttonBar"> <li><a href="#" id="btnAnteriorCiclo"><label class="iconBtnCicAnt iconBtnRedondo middleHoriz icon-arrow-left4"></label><label class="middleHoriz">Ciclo Ant.</label></a></li>  </ul>');
                    //$('#pnlCicloEscolar').append('<label id="lblCiclo"> ... </label>');
                    //$('#pnlCicloEscolar').append('<ul class="buttonBar"> <li><a href="#" id="btnSiguienteCiclo"><label class="middleHoriz">Sig. Ciclo</label><label class="iconBtnCicSig iconBtnRedondo middleHoriz icon-arrow-right4"></label></a></li>  </ul>');
                $('#pnlAlumnos').append('<div id="pnlDatosGenerales"></div>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapEval_cveprograma"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapEval_idalu"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapEval_curp"> ... </label>');
                $('#pnlAlumnos').append('<div id="scrlAlumCapEval" class="scrollTable"></div>');
            $('#pnlEvalPreescolar').append('<div id="pnlGestionEval"></div>');
                $('#pnlGestionEval').append('<div id="pnlEvaluaciones" class="panel"></div>');
                    $('#pnlEvaluaciones').append('<div id="pnlCambiarEval"></div>');
                        $('#pnlCambiarEval').append('<ul id="ulBtnEvalAnt" class="buttonBar alinearHoriz"> <li><a href="#" id="btnAnteriorNumEval"><label class="iconBtnBimEvalAnt iconBtnRedondo icon-arrow-left4"></label>Eval. Ant.</a></li> </ul>');
                        $('#pnlCambiarEval').append('<label id="lblNumEval" class="alinearHoriz"> ... </label>');
                        $('#pnlCambiarEval').append('<ul id="ulbtnEvalSig" class="buttonBar alinearHoriz"> <li><a href="#" id="btnSiguienteNumEval">Sig. Eval.<label class="iconBtnBimEvalSig iconBtnRedondo icon-arrow-right4"></label></a></li> </ul>');
                    $('#pnlEvaluaciones').append('<div id="scrlAvancesXEval"></div>');
                    $('#pnlEvaluaciones').append('<div id="pnlMensajeGuardar">  <label id="lblMensajeGuardar" title="No olvide guardar la evaluación por cada alumno.">No olvide guardar la evaluación por cada alumno.<label>  </div>');
                    $('#pnlEvaluaciones').append('<ul class="buttonBar"> <li><a href="#" id="btnGuardarEvaluaciones"><label class="iconBtnGuardar icon-disquete"></label>Guardar evaluación del alumno</a></li>  </ul>');
                $('#pnlGestionEval').append('<div id="pnlInasistencias" class="panel"><div class="tituloPanel">Inasistencias</div>');
                    $('#pnlInasistencias').append('<div id="pnlTblInasistencias" class="alinearHoriz"><div id="scrlInasistencias" class="scrollTable"></div></div>');
                    $('#pnlInasistencias').append('<div id="pnlBotonesInasis" class="alinearHoriz">'
                                                    +'<ul class="buttonBar">'
                                                        +'<li><a href="#" id="btnGuardaInasistencias"><label class="iconBtnGuardar icon-disquete"></label>Guardar inasis</a></li>'
                                                        +'<li><a href="#" id="btnGenInasis" title="Crea los espacios a todos los alumnos que no los tienen para ingresarles las inasistencias.">Genera renglón inasistencias</a></li>'
                                                    +'</ul>'
                                                +'</div>');
                                              
                $('#pnlGestionEval').append('<div id="pnlLengua" class="panel"><div class="tituloPanel">Lengua</div></div>');                    
                    $('#pnlLengua').append('<div id="pnlHablaOtraLengua"> '
                                    + '<label id="lblOtraLengua">Lengua indígena (opcional): <select id="cbxOtrasLenguas" name="cbxOtrasLenguas"></select></label> </div>');
                    $('#pnlLengua').append('<div id="btnGuardarDatosComp" class="singleButton"><label class="alinearHoriz vertMarginable"><span class="iconButton icon-disquete"></span>Guardar lengua</label></div>');
                            
                            
                                            //+'<div id="pnlHablaEspañol"> <label id="lblHablaEspañol">Habla Español: <input type="radio" id="rbnSiHablaEspañol" name="rbgHablaEspañol" value="rbnSiHablaEspañol" tabindex="115">Sí <input type="radio" id="rbnNoHablaEspañol" name="rbgHablaEspañol" value="rbnNoHablaEspañol" tabindex="116">No</label> </div>'
                                         
                                 
                /*$('#pnlGestionEval').append('<div id="pnlDatosComp" class="panel">'
                                                +'<div class="tituloPanel">DATOS COMPLEMENTARIOS DEL REPORTE DE EVALUACIÓN</div>'
                                                +'<div class="globoayuda_pointdown"><div class="btnCerrarGlobo">X</div>CAPTURE AQUÍ<BR>Complemento del Reporte de Evaluación</div>'
                                            +'</div>');
                    $('#pnlDatosComp').append('<div id="pnlCriteriosDePromocion" class="panel"><div class="tituloPanel">Criterios de promoción</div>'
                                                 +'<div id="pnlConcluyo"> <label id="lblConcluyo" class="alinearHoriz">Concluyó:</label><input type="checkbox" id="chkConcluyo"  class="alinearHoriz" name="chkConcluyo" value="Concluyo"> </div>'
                                             +'</div>');                                 
                    $('#pnlDatosComp').append('<div id="pnlLengua" class="panel"><div class="tituloPanel">Lengua</div>'
                                                //+'<div id="pnlHablaEspañol"> <label id="lblHablaEspañol">Habla Español: <input type="radio" id="rbnSiHablaEspañol" name="rbgHablaEspañol" value="rbnSiHablaEspañol" tabindex="115">Sí <input type="radio" id="rbnNoHablaEspañol" name="rbgHablaEspañol" value="rbnNoHablaEspañol" tabindex="116">No</label> </div>'
                                             +'<div id="pnlHablaOtraLengua"> <label id="lblOtraLengua">Lengua indígena (opcional): <select id="cbxOtrasLenguas" name="cbxOtrasLenguas"></select></label> </div>'
                                         +'</div>');*/
    
                    /*$('#pnlDatosComp').append('<div id="pnlComDocAlum" class="panel"><div class="tituloPanel">Comunicación Docente-Alumno</div>'
                                            +'<div><label id="lblComDA">Indicar el tipo de comunicación que tuvo el docente con cada alumno.</label></div>'
                                            +'<div id="pnlTblComDocAlum">  <div id="scrlComDocAlum" class="scrollTable"></div>  </div>'                                            
                                         +'</div>')¨*/

                    /*$('#pnlDatosComp').append('<div id="pnlRecomendaciones" class="panel"><div class="tituloPanel">Recomendaciones</div>'
                                                 +'<textarea id="txtaRecomendGrales" maxlength="800" placeholder="Escriba máximo 800 caracteres." title="Escriba máximo 800 caracteres."></textarea>'
                                             +'</div>');*/
                    
                    
                $('#pnlGestionEval').append('<div id="pnlBotonesDecontrol"></div>');
                    $('#pnlBotonesDecontrol').append('<div id="pnlBotonesDecontrolIzq"></div>');
                        $('#pnlBotonesDecontrolIzq').append('<ul class="buttonBar">'
                                                                +'<li><a href="#" id="btnLimpiarEvalPreesc"><label class="iconBtnLimpiar icon-brocha"></label>Limpiar captura</a></li>'
                                                                +'<li><a href="#" id="btnAnteriorGrupo"><label class="iconBtnGpoAnt iconBtnRedondo icon-arrow-left4"></label>Gpo. anterior</a></li>'
                                                                +'<li><a href="#" id="btnSiguienteGrupo">Siguiente gpo.<label class="iconBtnGpoSig iconBtnRedondo icon-arrow-right4"></label></a></li>'
                                                            +'</ul>');
        
        //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwEvalPreescolar_Close(); });
    
    $("#btnAnteriorNumEval").on('click',function(){ btnAnteriorNumEval_EvalPreesc_Click(); return false; });
    $("#btnSiguienteNumEval").on('click',function(){ btnSiguienteNumEval_EvalPreesc_Click(); return false; });
    $("#btnGuardarEvaluaciones").on('click',function(){ btnGuardarEvaluaciones_EvalPreesc_Click ();  return false;});
    $("#btnGuardaInasistencias").on('click',function(){ btnGuardaInasistencias_EvalPreesc_Click(); return false; });
    $("#btnGuardarDatosComp").on('click',function(){ btnGuardarDatosComp_EvalPreesc_Click(); return false; });
    $("#btnAnteriorGrupo").on('click',function(){ btnAnteriorGrupo_EvalPreesc_Click(); return false; });
    $("#btnLimpiarEvalPreesc").on('click',function(){ limpiarDatosDeCaptura_EvalPreesc(); return false; });
    $("#btnSiguienteGrupo").on('click',function(){ btnSiguienteGrupo_EvalPreesc_Click();  return false;});
    $("#btnGenInasis").on('click',function(){ btnGenInasis_EvalPreesc_Click(); return false; });
}

function frmwEvalPreescolar_FormActivate ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"EvPr", metodo:"foAc", califCicEscIn:sisVars.cicescin, cicescin:sisVars.cicescin, eval:jsEvalPree.eval, 
        tblPrincipal_idcct:jsEvalPree.tblPrincipal_idcct, tblPrincipal_modalidad:jsEvalPree.tblPrincipal_modalidad, 
        tblPrincipal_grado:jsEvalPree.tblPrincipal_grado, tblPrincipal_grupo:jsEvalPree.tblPrincipal_grupo, tblPrincipal_cveplan:jsEvalPree.tblPrincipal_cveplan
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
                //-------------------- Asignamos variables ---------------------
                jsEvalPree.evalOf=result.evalOf;
                jsEvalPree.evalOf1=result.evalOf1;
                jsEvalPree.evalOf2=result.evalOf2;
                jsEvalPree.evalOf3=result.evalOf3;
                jsEvalPree.tipoCambCic = result.tipoCambCic;
                jsEvalPree.paqueteMatsDefault = result.paqueteMatsDefault;
                
                //--------------- Asignamos datos a componentes ----------------
                $("#lblCiclo").text(result.lblCiclo);
                $('#lblNumEval').text(result.lblNumEval);
                var tblAlumCapEval={cveprograma:"", idalu:"", curp:""};
                if (result.tblAlumCapEval.length > 0){
                    tblAlumCapEval.cveprograma = result.tblAlumCapEval[0].cveprograma;
                    tblAlumCapEval.idalu = result.tblAlumCapEval[0].idalu;
                    tblAlumCapEval.curp = result.tblAlumCapEval[0].curp;
                }
                jsEvalPree.tblAlumCapEval_idalu = tblAlumCapEval.idalu;
                
                setDatosLabel_EvalPreesc ("AlumCapEval", tblAlumCapEval.cveprograma, tblAlumCapEval.idalu==="-1"?"":tblAlumCapEval.idalu, tblAlumCapEval.curp);
                switch (jsEvalPree.tblPrincipal_grado){
                    case "1": $("#lblConcluyo").text("Concluyó el primer grado:"); break;
                    case "2": $("#lblConcluyo").text("Concluyó el segundo grado:"); break;
                    case "3": $("#lblConcluyo").text("Concluyó su educación preescolar:"); break;
                }
                
                $("#cbxOtrasLenguas").append("<option value='' selected></option>");
                $.each(result.lenguas,function(clave,valor) {
                       $("#cbxOtrasLenguas").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"'>"+valor.trim()+"</option>");                        
                });
                
                //insertarTabla_ComDocAlum(result.tblComDocAlum);
                
                insertarDatosCompRepEval (result);
                //----------- Activamos o desactivamos componentes -------------
                boton_setVisible(result.btnSiguienteCiclo_Visible,'btnSiguienteCiclo');
                boton_setVisible(result.btnAnteriorCiclo_Visible,'btnAnteriorCiclo');
                if (!result.btnAnteriorGpo_Enabled)     boton_setEnabled(result.btnAnteriorGpo_Enabled,'btnAnteriorGpo');
                if (!result.btnSiguienteGpo_Enabled)    boton_setEnabled(result.btnSiguienteGpo_Enabled,'btnSiguienteGpo');
                
                boton_setVisible(false,"btnHistAcad");
                boton_setVisible(false,"btnKardex");
                boton_setVisible(false,"btnFoliosKardex");
                
                //--------------------- Rellenamos tablas ----------------------
                insertarTablas_AlumCapEval_y_Inasistencias_EvalPreesc(result.tblAlumCapEval, result.tblAvancesXEval, result.tblInasistencias);
                        
                cerrarLoading();
                aparecerGlogoAyuda ("#pnlEvalPreescolar #pnlDatosComp .globoayuda_pointdown",1500);
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
            case -11: 
                    cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
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

function limpiarDatosDeCaptura_EvalPreesc ()
{
    var tabla = new Tabla();
    $("#pnlEvalPreescolar #pnlGestionEval textarea").val("");
    //$($("#pnlEvalPreescolar #pnlDatosComp select")).prop('selectedIndex', 0);
    //$('#pnlEvalPreescolar #pnlCapturaDeEvaluaciones input[name="rbnTutoria"]').prop('checked', false);
    //$('#pnlEvalPreescolar #pnlDatosComp input[type="checkbox"]').prop('checked', true);
    
    var numFilas = tabla.getNumRows("tblInasistencias");
    for (var i=0; i<numFilas; i++)
        tabla.removeRow("tblObsYRecomXBimYAsig",0);
}

function insertarDatosCompRepEval (result)
{
    //document.getElementById('chkConcluyo').checked = result.concluyo===true;
    $("#cbxOtrasLenguas").val((result.cvelengua.trim()==="ESP")?"":result.cvelengua.trim());
    //$("#txtaRecomendGrales").val(result.recomendaciones);    
}

function insertarTabla_ComDocAlum(tblComDocAlum)
{    
    var tabla = new Tabla();
    var colsTitle = new Array ("1°TRIM","2°TRIM","3°TRIM");
    var colsToShow = new Array ("trim1","trim2","trim3");
    

    tabla.create("scrlComDocAlum","tblComDocAlum",tblComDocAlum, colsTitle, colsToShow, ["combobox","combobox","combobox"], ["center","center","center"], false, null, null, null );    
        
    // Insertamos el catálogo para los combos de Comunicacion docente alumno y establecemos el dato que trae el muchito 
    $("#pnlComDocAlum .coltblComDocAlum_col0 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col0 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlGestionEval #tblComDocAlum_cbx_f0"+"_c0").val(tblComDocAlum[0].trim1);
    
    $("#pnlComDocAlum .coltblComDocAlum_col1 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col1 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlGestionEval #tblComDocAlum_cbx_f0"+"_c1").val(tblComDocAlum[0].trim2);
    
    $("#pnlComDocAlum .coltblComDocAlum_col2 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col2 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlGestionEval #tblComDocAlum_cbx_f0"+"_c2").val(tblComDocAlum[0].trim3);     
}


function setDatosLabel_EvalPreesc (caso, cveprograma, idalu, curp)
{
    if (caso==="AlumCapEval"){
        $('#lblTblPrincipal_cct').text(jsEvalPree.tblPrincipal_cct);
        $('#lblTblPrincipal_grado').text(jsEvalPree.tblPrincipal_grado);
        $('#lblTblPrincipal_grupo').text(jsEvalPree.tblPrincipal_grupo);
        $('#lblTblAlumCapEval_cveprograma').text(cveprograma);
        $('#lblTblAlumCapEval_idalu').text(idalu);
        $('#lblTblAlumCapEval_curp').text(curp);
    }
    
    $("#frmfEvalPreescolar legend").html('Evaluaciones del grado '+jsEvalPree.tblPrincipal_grado+'° para el ciclo  '+jsEvalPree.tblPrincipal_cicescini+'-'+(parseInt(jsEvalPree.tblPrincipal_cicescini)+1));
    switch (jsEvalPree.tblPrincipal_grado){
        case "1": $("#lblConcluyo").text("Concluyó el primer grado:"); break;
        case "2": $("#lblConcluyo").text("Concluyó el segundo grado:"); break;
        case "3": $("#lblConcluyo").text("Concluyó su educación preescolar:"); break;
    }
}

function insertarTablas_AlumCapEval_y_Inasistencias_EvalPreesc(tblAlumCapEval, tblAvancesXEval, tblInasistencias )
{
    var tabla = new Tabla();
    //---------------------- Dibujamos la tabla tblAlumCapEval ----------------------\\
    tabla.create("scrlAlumCapEval","tblAlumCapEval",tblAlumCapEval, ["Nombre"], ["nom_tot"], null, null, false, null, function(index){
        tblAlumCapEval_EvalPreesc_ChangeSelectedItem (index);
    }, null);
    
    tabla.setSelectedRow ('tblAlumCapEval', 0);
    //---------------------- Dibujamos la tabla tblAvancesXEval ----------------------\\
    insertarTabla_AvancesXEval_EvalPreesc(tblAvancesXEval);
    //---------------------- Dibujamos la tabla tblInasistencias ----------------------\\
    insertarTabla_Inasistencias_EvalPreesc(tblInasistencias); //se quito 30-12-2020 se agregp para 2023-2024
}

function insertarTabla_AvancesXEval_EvalPreesc(tblAvancesXEval)
{
    var mensaje = new Mensajes();
    var tablaEval;
    var casoObjOfic = (jsEvalPree.evalOf)?"label":"textarea";
    var title = "Escriba máximo 300 caracteres.";
    //---------------------- Ponemos los datos de las evaluaciones ----------------------\\
    $("#tblAvancesXEval").remove();                                            //Limpiamos los datos
    $('#scrlAvancesXEval').append('<div id="tblAvancesXEval"></div>');
    
    tablaEval =  (tblAvancesXEval.length>0)?tblAvancesXEval:jsEvalPree.paqueteMatsDefault;
    
    
    for (var i=0; i<tablaEval.length; i++){
        if(tablaEval[i].cvetipmat==='FA' && tablaEval[i].cvemat==="ING")
            title = "Escriba máximo 300 caracteres solo si lleva la materia de inglés.";
        else 
            title = "Escriba máximo 300 caracteres.";
        $('#tblAvancesXEval').append('<div class="areaEval panel">'
                                            +'<label class="tituloPanel">'+tablaEval[i].desmat+'</label>'
                                            +'<'+casoObjOfic+' id="txa'+tablaEval[i].cvetipmat+'_'+tablaEval[i].cvemat+'" name="txa'+tablaEval[i].cvetipmat+'_'+tablaEval[i].cvemat+'" class="alinearHoriz avances" maxlength="300" placeholder="'+title+'" title="'+title+'">'
                                                + tablaEval[i].avances
                                            +'</'+casoObjOfic+'>'
                                     +'</div>');
    }
    
    if (jsEvalPree.evalOf)
        $("#pnlEvalPreescolar .avances").on('click',function(){ mensaje.EvalPreescolar("EVALUACION_OFICIALIZADA"); });
    $("#scrlAvancesXEval").scrollTop (0);
}

function insertarTabla_Inasistencias_EvalPreesc(tblInasistencias)
{
    var tabla = new Tabla();
    var colsTitle = new Array ("inst1","inst2","inst3");
    var colsToShow = new Array ("inst1","inst2","inst3");
    //var inDataCaseEvOf = new Array (jsEvalPree.evalOf1?"":"textbox", jsEvalPree.evalOf2?"":"textbox", jsEvalPree.evalOf3?"":"textbox"); //Original antes del ciclo 2023-2024
    var inDataCaseEvOf = new Array (jsEvalPree.evalOf1?"":"textbox", jsEvalPree.evalOf2?"":"textbox", jsEvalPree.evalOf3?"":"textbox");
    
    tabla.create("scrlInasistencias","tblInasistencias",tblInasistencias, colsTitle, colsToShow, inDataCaseEvOf, ["right","right","right"], false, null, null, null);
}

function tblAlumCapEval_EvalPreesc_ChangeSelectedItem (index)
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    var tblAlumCapEval_selRow = tabla.getRow ("tblAlumCapEval", index, ['cveprograma','idalu','curp'], null, "JSON");
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"EvPr", metodo:"tbMaCa_ChSeIt", numeval:jsEvalPree.eval, tblAlumCapEval_idalu:tblAlumCapEval_selRow.idalu, 
        califCicEscIn:jsEvalPree.califCicEscIn, tblPrincipal_grado:jsEvalPree.tblPrincipal_grado
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true                                                              //Sí es forsoso que no sea asíncrono, para que espere a que se carguen todas las materias
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    //------ Establecemos la selección de la fila en la tabla ------
                    jsEvalPree.tblAlumCapEval_idalu=tblAlumCapEval_selRow.idalu;
                    setDatosLabel_EvalPreesc ("AlumCapEval", tblAlumCapEval_selRow.cveprograma, tblAlumCapEval_selRow.idalu, tblAlumCapEval_selRow.curp);
                    
                    limpiarDatosDeCaptura_EvalPreesc();
                    insertarDatosCompRepEval (result);
                    tabla.setSelectedRow ('tblAlumCapEval', index);
                    
                    //--------------------- Rellenamos tablas ----------------------
                    insertarTabla_AvancesXEval_EvalPreesc(result.tblAvancesXEval);
                    insertarTabla_Inasistencias_EvalPreesc(result.tblInasistencias);
                    //insertarTabla_ComDocAlum(result.tblComDocAlum);
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

function btnAnteriorNumEval_EvalPreesc_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","desplazarce entre sus evaluaciones");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"EvPr", metodo:"btAnNuEv", tblAlumCapEval_cicescini:jsEvalPree.califCicEscIn, califCicEscIn:jsEvalPree.califCicEscIn, 
            tblPrincipal_idcct:jsEvalPree.tblPrincipal_idcct, tblPrincipal_cveplan:jsEvalPree.tblPrincipal_cveplan, tblPrincipal_grado:jsEvalPree.tblPrincipal_grado, 
            tblPrincipal_grupo:jsEvalPree.tblPrincipal_grupo, numeval:jsEvalPree.eval, tblAlumCapEval_idalu:jsEvalPree.tblAlumCapEval_idalu,
            evalMin:jsEvalPree.evalMin, evalMax:jsEvalPree.evalMax
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
                        jsEvalPree.eval = result.eval;
                        jsEvalPree.evalOf = result.evalOf;
                        //---------------------- Dibujamos la tabla tblAvancesXEval ----------------------\\
                        insertarTabla_AvancesXEval_EvalPreesc(result.tblAvancesXEval);
                        //insertarTabla_ComDocAlum(result.tblComDocAlum);
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

function btnSiguienteNumEval_EvalPreesc_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","desplazarce entre sus evaluaciones");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"EvPr", metodo:"btSiNuEv", tblPrincipal_idcct:jsEvalPree.tblPrincipal_idcct, tblPrincipal_grado:jsEvalPree.tblPrincipal_grado, 
            tblPrincipal_grupo:jsEvalPree.tblPrincipal_grupo, numeval:jsEvalPree.eval, tblAlumCapEval_idalu:jsEvalPree.tblAlumCapEval_idalu, 
            tblAlumCapEval_cicescini:jsEvalPree.califCicEscIn, evalMin:jsEvalPree.evalMin, evalMax:jsEvalPree.evalMax
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
                        jsEvalPree.eval = result.eval;
                        jsEvalPree.evalOf = result.evalOf;
                        //---------------------- Dibujamos la tabla tblAvancesXEval ----------------------\\
                        insertarTabla_AvancesXEval_EvalPreesc(result.tblAvancesXEval);
                        //insertarTabla_ComDocAlum(result.tblComDocAlum);
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

function btnGuardarEvaluaciones_EvalPreesc_Click ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblAvancesXEval = new Array(), textoEscrito="";
    
    if (jsEvalPree.evalOf)
        mensaje.EvalPreescolar("EVALUACION_OFICIALIZADA");
    else if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar sus avances");
    else{        
        for (var i=0; i<jsEvalPree.paqueteMatsDefault.length; i++){            
            tblAvancesXEval[i]=jsEvalPree.paqueteMatsDefault[i].cveprograma+"~"+jsEvalPree.paqueteMatsDefault[i].cvetipmat+"~"+jsEvalPree.paqueteMatsDefault[i].cvemat+"~"+$("#txa"+jsEvalPree.paqueteMatsDefault[i].cvetipmat+"_"+jsEvalPree.paqueteMatsDefault[i].cvemat).val().trim();
            textoEscrito+=$("#txa"+jsEvalPree.paqueteMatsDefault[i].cvetipmat+"_"+jsEvalPree.paqueteMatsDefault[i].cvemat).val().trim();
        }
        if (textoEscrito.trim() === "")
            return mensaje.General ("NADA_QUE_GUARDAR");
    
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"EvPr", metodo:"btGdEv_Cl", tblAvancesXEval:tblAvancesXEval, idalu:jsEvalPree.tblAlumCapEval_idalu, 
            califCicEscIn:jsEvalPree.califCicEscIn, tblPrincipal_idcct:jsEvalPree.tblPrincipal_idcct, tblPrincipal_grado:jsEvalPree.tblPrincipal_grado, 
            tblPrincipal_grupo:jsEvalPree.tblPrincipal_grupo, numeval:jsEvalPree.eval
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
                        mensaje.General("GUARDADO_EXITOSO");//OJO: NECESITAREMOS STOP
                        var numFilas = tabla.getNumRows ('tblAlumCapEval');
                        var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapEval');
                        if (posSelActual < (numFilas-1))
                            tblAlumCapEval_EvalPreesc_ChangeSelectedItem (posSelActual+1);  //Seleccionamos la siguiente fila de tblAlumCapCalif y obtenemos los datos correspondientes
                        //cerrarLoading();
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        //cerrarLoading();
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

function btnGuardaInasistencias_EvalPreesc_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar sus inasistencias");
    else if ( tabla.getNumRows("tblInasistencias") === 0)
        mensaje.General("NADA_QUE_GUARDAR");
    else if (jsEvalPree.evalOf1 && jsEvalPree.evalOf2 && jsEvalPree.evalOf3)
        mensaje.EvalPreescolar("TODO_OFICIALIZADO");
    else{
        var filaTblInasistencias = tabla.getRow ("tblInasistencias",0,null,"VISIBLES","array");
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"EvPr", metodo:"btGuIn", tblAlumCapEval_idalu:jsEvalPree.tblAlumCapEval_idalu, tblAlumCapEval_cicescini:jsEvalPree.califCicEscIn, 
            tblPrincipal_idcct:jsEvalPree.tblPrincipal_idcct, tblPrincipal_cveplan:jsEvalPree.tblPrincipal_cveplan, tblPrincipal_grado:jsEvalPree.tblPrincipal_grado, 
            tblPrincipal_grupo:jsEvalPree.tblPrincipal_grupo, inst1:filaTblInasistencias[0], inst2:filaTblInasistencias[1], inst3:filaTblInasistencias[2]
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
                        //cerrarLoading();
                        mensaje.General("GUARDADO_EXITOSO");
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

function btnGuardarDatosComp_EvalPreesc_Click ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapEval") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar sus recomendaciones");
    else if (jsEvalPree.evalOf1 && jsEvalPree.evalOf2 && jsEvalPree.evalOf3)
        mensaje.EvalPreescolar("TODO_OFICIALIZADO");
    else{
        var datos = {
            modulo:"EvPr", metodo:"btGuDaCo", califCicEscIn:jsEvalPree.califCicEscIn, tblPrincipal_idcct:jsEvalPree.tblPrincipal_idcct, 
            tblAlumCapEval_idalu:jsEvalPree.tblAlumCapEval_idalu, /*chkConlcuyo_checked:document.getElementById('chkConcluyo').checked, */
            cvelengua:$('#cbxOtrasLenguas').val()===""?"ESP":$('#cbxOtrasLenguas').val(), /*recomendaciones:$("#txtaRecomendGrales").val().trim(),*/
            tblPrincipal_grado:jsEvalPree.tblPrincipal_grado, tblPrincipal_grupo:jsEvalPree.tblPrincipal_grupo
            /*trim1:filaTblComDocAlum[0], trim2:filaTblComDocAlum[1], trim3:filaTblComDocAlum[2]*/
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
                        //cerrarLoading();
                        mensaje.General("GUARDADO_EXITOSO");
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

function btnAnteriorGrupo_EvalPreesc_Click()
{
    var tabla = new Tabla();
    
    if (jsEvalPree.hayCambiosXEval) 
        btnGuardarEvaluaciones_EvalPreesc_Click ();
    
    if ( tabla.getSelectedIndexRow ('tblPrincipal')-1 >= 0 ){
        var datos = { 
            modulo:"EvPr", metodo:"btAnGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","nombre","cveplan","modalidad"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsEvalPree.califCicEscIn, numeval:jsEvalPree.eval 
        };
        sigAntGrupo_EvalPreesc (datos);
    }
}

function btnSiguienteGrupo_EvalPreesc_Click()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows ('tblPrincipal');
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    
    
    if (posSelActual < (numFilas-1))
    {
        if (jsEvalPree.hayCambiosXEval)
            btnGuardarEvaluaciones_EvalPreesc_Click ();

        var datos = { 
            modulo:"EvPr", metodo:"btSiGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","nombre","cveplan","modalidad"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsEvalPree.califCicEscIn, numeval:jsEvalPree.eval 
        };

        sigAntGrupo_EvalPreesc (datos);
    }
}

function sigAntGrupo_EvalPreesc (datos)
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
                    jsEvalPree.tblPrincipal_grado = result.tblPrincipal_grado; 
                    jsEvalPree.tblPrincipal_grupo = result.tblPrincipal_grupo;
                    jsEvalPree.paqueteMatsDefault = result.paqueteMatsDefault;
                    
                    insertarTablas_AlumCapEval_y_Inasistencias_EvalPreesc(result.tblAlumCapEval, result.tblAvancesXEval, result.tblInasistencias);
                    //insertarTabla_ComDocAlum(result.tblComDocAlum);
                    var tblAlumCapEval={cveprograma:"", idalu:"", curp:""};
                    if (tabla.getSelectedIndexRow("tblAlumCapEval")>=0)
                        tblAlumCapEval = tabla.getSelectedRow("tblAlumCapEval",["cveprograma","idalu","curp"],null,"JSON");
                    setDatosLabel_EvalPreesc ("AlumCapEval", tblAlumCapEval.cveprograma, tblAlumCapEval.idalu==="-1"?"":tblAlumCapEval.idalu, tblAlumCapEval.curp);
                    insertarDatosCompRepEval (result);
                    jsEvalPree.tblAlumCapEval_idalu = tblAlumCapEval.idalu;

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

function btnGenInasis_EvalPreesc_Click()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    
    if ( tabla.getNumRows("tblAlumCapEval") === 0)
        mensaje.EvalPreescolar("SIN_ALUMNOS_PARA_INASIS");
    else if (jsEvalPree.evalOf1 && jsEvalPree.evalOf2 && jsEvalPree.evalOf3)
        mensaje.EvalPreescolar("TODO_OFICIALIZADO");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"EvPr", metodo:"btGeIn", tblAlumCapEval:tabla.getTable('tblAlumCapEval',['idalu']), tblAlumCapEval_cicescini:jsEvalPree.califCicEscIn,
            tblPrincipal_idcct:jsEvalPree.tblPrincipal_idcct, tblPrincipal_cveplan:jsEvalPree.tblPrincipal_cveplan, 
            tblPrincipal_grado:jsEvalPree.tblPrincipal_grado
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
                        tblAlumCapEval_EvalPreesc_ChangeSelectedItem (0);
                        insertarTabla_Inasistencias_EvalPreesc(result.tblInasistencias);
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

