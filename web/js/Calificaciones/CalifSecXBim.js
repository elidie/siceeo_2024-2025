/* 
    Creado el : 6/03/2015, 08:42:02 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaSeXBi;
var tabla;
function frmwCalifSecXBim_Show (buildView, rbgXBimOPromOVal_Checked, chkCalProm_isChecked, cveunidad, bim, calProm, tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_nombre, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo, bimMin, bimMax )
{
    //-------- Creamos el objeto para manipular tablas -------- 
    tabla = new Tabla ();
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsCaSeXBi = {
        rbgXBimOPromOVal_Checked:rbgXBimOPromOVal_Checked,
        chkCalProm_isChecked:chkCalProm_isChecked,
        cveunidad:cveunidad,
        bim:bim,
        calProm:calProm,
        tblPrincipal_cicescini:tblPrincipal_cicescini,
        tblPrincipal_idcct:tblPrincipal_idcct,
        tblPrincipal_cct:tblPrincipal_cct,
        tblPrincipal_nombre:tblPrincipal_nombre,
        tblPrincipal_cveplan:tblPrincipal_cveplan,        
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo:tblPrincipal_grupo,
        tblAlumCapCalif_grado:tblPrincipal_grado,
        tblAlumCapCalif_idalu:"",
        tblAlumCapCalif_Columns2_Visible:false,
        tblAlumCapCalif_Columns8_Visible:false,
        tblAlumCapCalif_Columns3_Visible:false,
        tblAlumCapCalif_Columns4_Visible:false,
        tblMatCalifXBim_Columns2_Visible:false,
        //tblMatCalifXBim_calif1_DisplayFormat:"",
        //tblMatCalifXBim_calif2_DisplayFormat:"",
        bimMax: bimMax,
        bimMin: bimMin,
        califCicEscIn:null,
        hayCambiosXBim:false,
        tblMatCalifXBim_calif1_OldValue:null,
        tblMatCalifXBim_calif2_OldValue:null,
        btnElimBim_Visible:false,
        califConFoco: 0,
        tipoCambCic:"-1",
        ofs:null
    };
    //--------------------------------------------------------------------------
    if (buildView){
        object_setVisible (false,"gridTable");                                  // Ocultamos el gridTable
        $("#frmwCalifSecXBim").css("display", "block");                         // Mostramos el formulario correspondiente
        frmwCalifSecXBim_Create();
    } 
    //if (jsCaSeXBi.tblPrincipal_cveplan!=="2")                                   // Condicion temporal para secundarias, mientras quedan los talleres.
      frmwCalifSecXBim_FormActivate ();
}

function frmwCalifSecXBim_Close()
{
    jsCaSeXBi=null;
    tabla = null;
    irAVentanaPrincipal ();
}

function frmwCalifSecXBim_Create ()
{
    if($('#frmfCalifSecXBim').length) // Verificando si la tabla Existe
        $('#frmfCalifSecXBim').remove();
    $('#frmwCalifSecXBim').append('<fieldset id="frmfCalifSecXBim"><legend>Calificaciones trimestrales</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCalifSecXBim").append('<div id="pnlCalifSecXBim"></div>');
        
            $('#pnlCalifSecXBim').append('<div id="pnlAlumnos"></div>');
                $('#pnlAlumnos').append('<div id="pnlCicloEscolar"></div>');
                    $('#pnlCicloEscolar').append('<ul class="buttonBar"> <li><a href="#" id="btnAnteriorCiclo"><label class="iconBtnCicAnt iconBtnRedondo middleHoriz icon-arrow-left4"></label><label class="middleHoriz">Ciclo Ant.</label></a></li>  </ul>');
                    $('#pnlCicloEscolar').append('<label id="lblCiclo"> ... </label>');
                    $('#pnlCicloEscolar').append('<ul class="buttonBar"> <li><a href="#" id="btnSiguienteCiclo"><label class="middleHoriz">Sig. Ciclo</label><label class="iconBtnCicSig iconBtnRedondo middleHoriz icon-arrow-right4"></label></a></li>  </ul>');
                $('#pnlAlumnos').append('<div id="pnlDatosGenerales"></div>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapCalif_cveprograma"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapCalif_idalu"> ... </label>');
                    $('#pnlDatosGenerales').append('<label id="lblTblAlumCapCalif_curp"> ... </label>');
                $('#pnlAlumnos').append('<div id="pnlTblAlumCapCalif">  <div id="scrlAlumCapCalif" class="scrollTable"></div>  </div>');
                    $('#pnlTblAlumCapCalif').append('');
                    
            $('#pnlCalifSecXBim').append('<div id="pnlGestionBim"></div>');    
            
            if (jsCaSeXBi.bim!=="0") {                                    
                $('#pnlGestionBim').append('<div id="pnlCalifis" class="panel"></div>');                           
                    $('#pnlCalifis').append('<div id="pnlBimestre"></div>');  
                        $('#pnlBimestre').append('<ul class="buttonBar"> <li><a href="#" id="btnAnteriorBimestre"><label class="iconBtnBimEvalAnt iconBtnRedondo icon-arrow-left4"></label> Anterior</a></li>  </ul>');
                        $('#pnlBimestre').append('<label id="lblBim"> ... </label>');
                        $('#pnlBimestre').append('<ul class="buttonBar"> <li><a href="#" id="btnSiguienteBimestre">Siguiente<label class="iconBtnBimEvalSig iconBtnRedondo icon-arrow-right4"></label></a></li>  </ul>');
                        $('#pnlBimestre').append('<label id="lblTblMatCalifXBim_cvemat"> ... </label>');
                    $('#pnlCalifis').append('<div id="pnlTblMatCalifXBim">  <div id="scrlMatCalifXBim" class="scrollTable"></div>  </div>');
                    $('#pnlCalifis').append('<div id="pnlMensajeGuardar">  <label id="lblMensajeGuardar" title="Para guardar las calificaciones oprima la tecla enter en el último renglón.">Para guardar las calificaciones oprima la tecla enter en el último renglón.<label>  </div>');
                
                /*   Panel para captura de inasistencias */
                $('#pnlGestionBim').append('<div id="pnlInasistencias" ></div>');
                    $('#pnlInasistencias').append('<div id="pnlTblInasistencias">  <div id="scrlInasistencias" class="scrollTable"></div>  </div>');
                    $('#pnlInasistencias').append('<div id="pnlBtnInasistencias"></div>');
                        $('#pnlBtnInasistencias').append('<ul class="buttonBar"> <li><a href="#" id="btnGuardaInasistencias"><label class="iconBtnGuardar icon-disquete"></label>Guarda inasis</a></li>  </ul>');
                
                /*  Panel para captura de Comunicación Docente-alumno*/                
                /*$('#pnlGestionBim').append('<div id="pnlComunicacion" class="panel"></div>');                           
                    $('#pnlComunicacion').append('<div id="pnlComDocAlum" ></div>')
                        $('#pnlComDocAlum').append('<div><label id="lblComDA">Indicar el tipo de comunicación que tuvo el docente con cada alumno.</label></div>');
                        $('#pnlComDocAlum').append('<div id="pnlTblComDocAlum">  <div id="scrlComDocAlum" class="scrollTable"></div>  </div>');
                        $('#pnlComDocAlum').append('<div id="pnlBtnComDocAlum"></div>');
                            $('#pnlBtnComDocAlum').append('<ul class="buttonBar"> <li><a href="#" id="btnGuardaComDocAlum" title="Guarda la comunicacion docente-alumno."><label class="iconBtnGuardar icon-disquete"></label>Guardar</a></li>  </ul>');
                */
               
                //$('#pnlGestionBim').append('<div id="pnlConOSinProm">Sin calcular promedios por materia</div>');  //Barra verde que aparecia del lado de inasistencias.
                $('#pnlGestionBim').append('<div id="pnlMemo1"></div>'); 
                    $('#pnlMemo1').append('<label id="lblMemo1"></label>'); 
                        $("#lblMemo1").append('"70.4.5. Si el resultado obtenido en el examen o exámenes de recuperación es APROBATORIO, será éste el que deberá reportarse como calificación en la evaluación"');
            }        
                $('#pnlGestionBim').append('<div id="pnlBotonesDecontrol" class="pnlBotonesDecontrol"></div>');  // Se agrego la clase=pnlBotonesDecontrol por Ely
                    $('#pnlBotonesDecontrol').append('<div id="pnlBotonesDecontrolIzq"></div>');
                        $('#pnlBotonesDecontrolIzq').append('<ul class="buttonBar"> '
                                                                + ((jsCaSeXBi.bim!=="0") ? '<li><a href="#" id="btnGenBim" title="Le crea el paquete de materias del trimestre a todos los alumnos que les haga falta">Generar materias del periodo</a></li>' : '')
                                                                + ((jsCaSeXBi.bim!=="0") ? '<li><a href="#" id="btnChekMat" title="Corrige el paquete de materias al alumno seleccionado de acuerdo al plan de estudios vigente">Corregir materias del alumno</a></li>' : '')
                                                                //+'<li><a href="#" id="btnElimBimAlum" title="Elimina todas las materias de todos los bimestres al alumno seleccionado">Elimina todos los Bim. del Alum.</a></li>'
                                                                //+'<li><a href="#" id="btnElimBim" title="Elimina todas las materias de todos los bimestres a todos los alumnos">Elimina materias Bim. del Gpo.</a></li>'
                                                                +'<li><a href="#" id="btnKardex">Imprime Kárdex</a></li>'
                                                                +'<li><a href="#" id="btnHistAcad" title="Muestra la ventana para ingresar calificaciones de exámenes extraordinarios">Exam. Extraord.</a></li>'
                                                            +'</ul>');
                    $('#pnlBotonesDecontrol').append('<div id="pnlBotonesDecontrolDer"></div>');
                        $('#pnlBotonesDecontrolDer').append('<ul class="buttonBar"> '                                                                
                                                                + ((jsCaSeXBi.bim!=="0") ? '<li><a href="#" id="btnGenInasis" title="Genera el renglón con espacios para ingresar inasistencias a todos los alumnos que les haga falta dicho concepto">Genera renglón inasistencias</a></li>' : '')                                                              
                                                                //+'<li><a href="#" id="btnAnalisMat">Mostrar calif.</a></li>'
                                                                +'<li><a href="#" id="btnReporteAvances">Reporte de Avances.</a></li>'  // Boton de avances de captura 2023-2024.
                                                                +'<li><a href="#" id="btnAnteriorGrupo"><label class="iconBtnGpoAnt iconBtnRedondo icon-arrow-left4"></label>Gpo. anterior</a></li>'
                                                                +'<li><a href="#" id="btnSiguienteGrupo">Siguiente gpo.<label class="iconBtnGpoSig iconBtnRedondo icon-arrow-right4"></label></a></li>'
                                                                //+'<li><a href="#" id="btnFoliosKardex">Folios Kárdex</a></li>'
                                                                //+'<li><a href="#" id="btnAlumnosConPromXMat">Alum. con prom. por mat.</a></li>'
                                                            +'</ul>');
                /*$('#pnlGestionBim').append('<div id="pnlTotMatYDeudas"></div>');
                    $('#pnlTotMatYDeudas').append('<label id="lblTotMat"> <input id="chkTotMat" type="checkbox" name="chkTotMat" value="totMat">Total de materias</label>');
                    $('#pnlTotMatYDeudas').append('<label id="lblTblAlumCapCalif_debe1ro">...</label>');
                    $('#pnlTotMatYDeudas').append('<label id="lblTblAlumCapCalif_debe2do">...</label>');
                $('#pnlGestionBim').append('<div id="pnlTblTotMat">  <div id="scrlTotMat" class="scrollTable"></div>  </div>');*/
    
    //object_setVisible (false,'pnlTblTotMat');
    
    //------------------------------------- DECLARACIÓN DE VENTANAS MODALES --------------------------------------------
    $("#frmfCalifSecXBim").append('<div id="mwfmRevisaGpo" class="mwfModal" style="display:none"></div>');  

    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnRegresar").unbind("click");
    $("#btnRegresar").bind("click",function(){ frmwCalifSecXBim_Close(); });    
    $("#btnAnteriorCiclo").on('click',function(){ btnAnteriorCiclo_Click(); return false; });
    $("#btnSiguienteCiclo").on('click',function(){ btnSiguienteCiclo_Click(); return false; });    
    $("#btnAnteriorBimestre").on('click',function(){ btnAnteriorBimestre_Click(); return false; });
    $("#btnSiguienteBimestre").on('click',function(){ btnSiguienteBimestre_Click(); return false; });    
    
    //$("#btnGuardaComDocAlum").on('click',function(){ btnGuardaComDocAlum_Click(); return false; });
    
    /************  Comentado el 31-01-2024 por ely  *************/
    $("#btnGenBim").on('click',function(){ btnGenBim_Click(); return false; }); //SIN_CAPTURA
    $("#btnGuardaInasistencias").on('click',function(){ btnGuardaInasistencias_Click(); return false; }); //SIN_CAPTURA
    $("#btnChekMat").on('click',function(){ btnChekMat_Click(); return false; }); // Comentado SIN_CAPTURA
    $("#btnGenInasis").on('click',function(){ btnGenInasis_Click(); return false; });  //Comentado SIN_CAPTURA
    
    
    /* $("#btnElimBimAlum").on('click',function(){ btnElimBimAlum_Click(); return false; });
     $("#btnElimBim").on('click',function(){ btnElimBim_Click(); return false; });    */
    
    //$("#btnAnalisMat").on('click',function(){ btnAnalisMat_Click(); return false; });  //muestra calificaciones y boton de exportar a excel
    $("#btnAnteriorGrupo").on('click',function(){ btnAnteriorGrupo_Click(); return false; });
    $("#btnSiguienteGrupo").on('click',function(){ btnSiguienteGrupo_Click();  return false;});
    $("#btnReporteAvances").on('click',function(){ btnReporteAvances_Click();  return false;});  // Boton para mosntrar avances de captura 2023-2024.
    
    //$("#pnlGestionBim #chkTotMat").on('click',function() { chkTotMat_Click ();  });
    $("#btnKardex").on('click',function(){ btnKardex1_Click();  return false;});
    //$("#btnFoliosKardex").on('click',function(){ btnFoliosKardex_Click();  return false;});  // Comentado el 21-09-2018 por ely 
    $("#btnHistAcad").on('click',function(){ btnHistAcad_Click();  return false;});
    //$("#btnAlumnosConPromXMat").on('click',function(){ btnAlumnosConPromXMat_Click();  return false;});    
}

function frmwCalifSecXBim_FormActivate ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"caSeXBi", metodo:"foAc", cicescin:sisVars.cicescin, tblPrincipal_cicescini:jsCaSeXBi.tblPrincipal_cicescini, 
        tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, 
        tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, calProm:jsCaSeXBi.calProm, bim:jsCaSeXBi.bim
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
                    jsCaSeXBi.califCicEscIn=result.califCicEscIn;
                    boton_setVisible(result.btnSiguienteCiclo_Visible,'btnSiguienteCiclo');
                    boton_setVisible(result.btnAnteriorCiclo_Visible,'btnAnteriorCiclo');
                    jsCaSeXBi.tipoCambCic = result.tipoCambCic;
                    $("#lblCiclo").text(result.lblCiclo);
                    jsCaSeXBi.ofs=result.ofs;
                    jsCaSeXBi.bim=result.bim;
                    
                    if (result.tblAlumCapCalif.length)
                        setDatosLabel ("AlumCapCalif", result.tblAlumCapCalif[0]["cveprograma"].trim(), result.tblAlumCapCalif[0]["idalu"], result.tblAlumCapCalif[0]["curp"], result.tblAlumCapCalif[0].debe1ro, result.tblAlumCapCalif[0].debe2do);
                    else
                        setDatosLabel ("AlumCapCalif", "", "", "", "", "");
                    
                    $('#lblBim').text(result.lblBim);
                    
                    jsCaSeXBi.tblAlumCapCalif_Columns2_Visible = result.tblAlumCapCalif_Columns2_Visible;
                    jsCaSeXBi.tblAlumCapCalif_Columns8_Visible = result.tblAlumCapCalif_Columns8_Visible;
                    jsCaSeXBi.tblAlumCapCalif_Columns3_Visible = result.tblAlumCapCalif_Columns3_Visible;
                    jsCaSeXBi.tblAlumCapCalif_Columns4_Visible = result.tblAlumCapCalif_Columns4_Visible;
                   
                  
                    jsCaSeXBi.tblMatCalifXBim_Columns2_Visible = result.tblMatCalifXBim_Columns2_Visible;
                    insertarTablas_AlumCapCalif_y_Inasistencias(result.tblAlumCapCalif, result.tblMatCalifXBim, result.tblInasistencias); // Aqui va la tabla de inasistencias
                    setDatosLabel ("MatCalifXBim", null, null, null);
                    
                    
                    jsCaSeXBi.tblAlumCapCalif_idalu = result.tblAlumCapCalif.length?result.tblAlumCapCalif[0].idalu:"";
                    result.tblMatCalifXBim_Visible;
                    
                    /*$('#pnlConOSinProm').css( "color", result.pnlConOSinProm_FontColor );
                    $('#pnlConOSinProm').css( "background-color", result.pnlConOSinProm_Color );
                    $('#pnlConOSinProm').html(result.pnlConOSinProm_Text);*/
                    
                    // if (!result.btnElimBim_Enabled)
                        // boton_setEnabled(result.btnElimBim_Enabled,'btnElimBim');  //Comentado el 21-09-2018 por ely
                    
                    // if (!result.btnElimBimAlum_Enabled)     boton_setEnabled(result.btnElimBimAlum_Enabled,'btnElimBimAlum'); //Comentado el 21-09-2018 por ely
                    // if (!result.btnElimBim_Visible)         boton_setVisible( (jsCaSeXBi.btnElimBim_Visible=result.btnElimBim_Visible),'btnElimBim');  //Comentado el 21-09-2018 por ely
                    if (!result.btnAnteriorGpo_Enabled)     boton_setEnabled(result.btnAnteriorGpo_Enabled,'btnAnteriorGpo');
                    if (!result.btnSiguienteGpo_Enabled)    boton_setEnabled(result.btnSiguienteGpo_Enabled,'btnSiguienteGpo');
                    
                    //jsCaSeXBi.tblMatCalifXBim_calif1_DisplayFormat = result.tblMatCalifXBim_calif1_DisplayFormat;
                    //jsCaSeXBi.tblMatCalifXBim_calif2_DisplayFormat = result.tblMatCalifXBim_calif2_DisplayFormat;
                    //$('#tblMatCalifXBim .coltblMatCalifXBim_col1 input[type="text"]').mask("1"+jsCaSeXBi.tblMatCalifXBim_calif1_DisplayFormat,{translation:  {1: {pattern: /[1]/, optional: true}}});
                    //$('#tblMatCalifXBim .coltblMatCalifXBim_col2 input[type="text"]').mask("n"+jsCaSeXBi.tblMatCalifXBim_calif2_DisplayFormat);
                    if(jsCaSeXBi.tblPrincipal_cveplan === "2")
                        object_setVisible(true,"pnlMemo1");
                    if (jsCaSeXBi.tblPrincipal_cveplan !== "2"){
                        object_setVisible(false,"pnlMemo1");
                        boton_setVisible(false,"btnHistAcad");
                        boton_setVisible(false,"btnKardex");                        
                        // boton_setVisible(false,"btnFoliosKardex"); //Comentado el 21-09-2018 por ely
                    }
                    
                    //************************  Agregado para ciclo 2023-2024 *************************************//                    
                    //if (!jsCaSeXBi.ofs.btnSiguienteBimestre_Enable)     boton_setEnabled(jsCaSeXBi.ofs.btnSiguienteBimestre_Enable,'btnSiguienteBimestre');
                    //if (!jsCaSeXBi.ofs.btnAnteriorBimestre_Enable)    boton_setEnabled(jsCaSeXBi.ofs.btnAnteriorBimestre_Enable,'btnAnteriorBimestre');                    
                        
                    //cerrarLoading();
                break;
            case 0: case -1:
                    //cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -2:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    mwfRevisaGpo_Show ({cicescini:jsCaSeXBi.tblPrincipal_cicescini, cveplan:jsCaSeXBi.tblPrincipal_cveplan, cct:jsCaSeXBi.tblPrincipal_cct, 
                                idcct:jsCaSeXBi.tblPrincipal_idcct, modalidad:jsCaSeXBi.tblPrincipal_modalidad, grado:jsCaSeXBi.tblPrincipal_grado, 
                                grupo:jsCaSeXBi.tblPrincipal_grupo });
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

function setDatosLabel (caso, cveprograma, idalu, curp, debe1ro, debe2do)
{
    if (caso==="AlumCapCalif"){
        $('#lblTblPrincipal_cct').text(jsCaSeXBi.tblPrincipal_cct);
        $('#lblTblPrincipal_grado').text(jsCaSeXBi.tblPrincipal_grado);
        $('#lblTblPrincipal_grupo').text(jsCaSeXBi.tblPrincipal_grupo);
        $('#lblTblAlumCapCalif_cveprograma').text(cveprograma);
        $('#lblTblAlumCapCalif_idalu').text(idalu);
        $('#lblTblAlumCapCalif_curp').text(curp);
        //$('#lblTblAlumCapCalif_debe1ro').text(debe1ro==="null"?"0":debe1ro);
        //$('#lblTblAlumCapCalif_debe2do').text(debe2do==="null"?"0":debe2do);
    } else if (caso==="MatCalifXBim"){
        var filaConDatos = tabla.getSelectedRow ("tblMatCalifXBim", ['cvemat']);
        if(filaConDatos)
            $('#lblTblMatCalifXBim_cvemat').text(filaConDatos);
    }
}

function insertarTablas_AlumCapCalif_y_Inasistencias(tblAlumCapCalif, tblMatCalifXBim, tblInasistencias)
{
    //---------------------- Dibujamos la tabla tblAlumCapCalif ----------------------\\
    var colsTitle = new Array ("Nombre", "PdGDO", "PdNiv","Prim","PdEB","MR","P","EstGdo");//,"folio");
    var colsToShow = new Array("nom_tot","promedio","promediogral","promdp","promedioeb","matrepact","promovido","estatusgrado");//,"folio");
    if (!jsCaSeXBi.tblAlumCapCalif_Columns2_Visible){ colsTitle.splice(colsTitle.indexOf("PdNiv"),1); colsToShow.splice(colsToShow.indexOf("promediogral"),1); }
    if (!jsCaSeXBi.tblAlumCapCalif_Columns8_Visible){ colsTitle.splice(colsTitle.indexOf("folio"),1); colsToShow.splice(colsToShow.indexOf("folio"),1); }
    if (!jsCaSeXBi.tblAlumCapCalif_Columns3_Visible){ colsTitle.splice(colsTitle.indexOf("Prim"),1); colsToShow.splice(colsToShow.indexOf("promdp"),1); }
    if (!jsCaSeXBi.tblAlumCapCalif_Columns4_Visible){ colsTitle.splice(colsTitle.indexOf("PdEB"),1); colsToShow.splice(colsToShow.indexOf("promedioeb"),1); }
    tabla.create("scrlAlumCapCalif","tblAlumCapCalif",tblAlumCapCalif, colsTitle, colsToShow, null, ["","center","center","center","center","center"], false, null, function(index){
        /*var filaConDatos = tabla.getVisibleAndHiddenRow ("tblAlumCapCalif", tabla.getSelectedIndexRow ("tblAlumCapCalif"), null, ['cveprograma','idalu','curp','debe1ro','debe2do'], "array");
        jsCaSeXBi.tblAlumCapCalif_idalu=filaConDatos[1];
        setDatosLabel ("AlumCapCalif", filaConDatos[0], filaConDatos[1], filaConDatos[2], filaConDatos[3],filaConDatos[4]);*/
        tblAlumCapCalif_ChangeSelectedItem (index); //Descomentar para calificaciones 2018 (OJO)
    }, null);
    
    tblAlumCapCalif_ApplyCellAttribute();
    tabla.setSelectedRow ('tblAlumCapCalif', 0);
    if(jsCaSeXBi.bim!=="0") {
        //---------------------- Dibujamos la tabla tblMatCalifXBim ----------------------\\
        insertarTabla_MatCalifXBim(tblMatCalifXBim);
        //---------------------- Dibujamos la tabla tblInasistencias ----------------------\\
        insertarTabla_Inasistencias(tblInasistencias);
    }
    //---------------------- Dibujamos la tabla de comunicacion docente alumno ----------------------\\    
    //insertarTabla_ComDocAlum(tblComDocAlum);
    /*var tabla_com = new Tabla ();
    var colsTitle_com = new Array ("1°TRIM","2°TRIM","3°TRIM");
    var colsToShow_com = new Array ("trim1","trim2","trim3"); */
    //====  Se cambio a editable no importando si esta o no oficializado -- Comentado x ely 25-05-18         
    //tabla_com.create("scrlComDocAlum","tblComDocAlum",tblComDocAlum, colsTitle_com, colsToShow_com, ["combobox","combobox","combobox"], ["center","center","center"], false, null, null, null );    
        
    /*********** Insertamos el catálogo para los combos de Comunicacion docente alumno y establecemos el dato que trae el muchito ***********/
    /*$("#pnlComDocAlum .coltblComDocAlum_col0 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col0 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c0").val(tblComDocAlum[0].trim1);*/
    //$("#tblComDocAlum_cbx_f0_c0 option[value='1']").attr("selected",true);
    
    /*$("#pnlComDocAlum .coltblComDocAlum_col1 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col1 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c1").val(tblComDocAlum[0].trim2);*/
    
    /*$("#pnlComDocAlum .coltblComDocAlum_col2 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col2 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c2").val(tblComDocAlum[0].trim3);     */     
}

function verifBimClose (b)
{   
    /*var ceCapTrim1 = false, ceCapTrim2 = false, ceCapTrim3 = true; //Para cierre de captura 2023 */ //Lo que funciona al 15-04-2024
    var ceCapTrim1 = true, ceCapTrim2 = true, ceCapTrim3 = true; //Para cierre de captura 2023 false=captura, true=sin captura
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    if (sisVars){        
        for (var boton in sisVars.botonesDeCalif) {              
            if(sisVars.botonesDeCalif[boton]==="btnTrim1")
                ceCapTrim1 = false;
            else if (sisVars.botonesDeCalif[boton]==="btnTrim2")
                ceCapTrim2 = false;
            else if (sisVars.botonesDeCalif[boton]==="btnTrim3")
                ceCapTrim3 = false;
        }    
    }
    //alert("Trimestres: 1-"+ceCapTrim1+" 2-"+ceCapTrim2+" 3-"+ceCapTrim3);   
    var inDataCase = "textbox";
    
    if (typeof b === "undefined"){        
        switch(parseInt(jsCaSeXBi.bim)){
            case 1:
                if ((jsCaSeXBi.ofs.bimOf && !jsCaSeXBi.ofs.alDeof) || ceCapTrim1)
                    inDataCase = "";            
                break;
            case 2:    
                if ((jsCaSeXBi.ofs.bimOf && !jsCaSeXBi.ofs.alDeof) || ceCapTrim2)
                    inDataCase = "";
                break;    
            case 3:
                if ((jsCaSeXBi.ofs.bimOf && !jsCaSeXBi.ofs.alDeof) || ceCapTrim3)
                    inDataCase = "";
                break;                
        }
    } else {
        switch (parseInt(b)){
            case 1:
                if ((jsCaSeXBi.ofs.bimOf1 && !jsCaSeXBi.ofs.alDeofB1) || ceCapTrim1) 
                    inDataCase = "";
                break;
            case 2:
                if ((jsCaSeXBi.ofs.bimOf2 && !jsCaSeXBi.ofs.alDeofB2) || ceCapTrim2) 
                    inDataCase = "";
                break;
            case 3:
                if ((jsCaSeXBi.ofs.bimOf3 && !jsCaSeXBi.ofs.alDeofB3) || ceCapTrim3) 
                    inDataCase = "";
                break;
        }
    }
    return inDataCase;
}
function insertarTabla_MatCalifXBim(tblMatCalifXBim)
{
    //---------------------- Dibujamos la tabla tblMatCalifXBim ----------------------\\    
    var colsTitle = new Array ("Materia","Calif1","Calif2");
    var colsToShow = new Array ("desmat", "calif1", "calif2");
    
    if (!jsCaSeXBi.tblMatCalifXBim_Columns2_Visible){ colsTitle.splice(2,1); colsToShow.splice(2,1); }
    var inDataCase = verifBimClose ();
    tabla.create("scrlMatCalifXBim","tblMatCalifXBim",tblMatCalifXBim, colsTitle, colsToShow, ["",inDataCase,inDataCase], ["","right","right"], true, null, function(f){
        //Cuando da click sobre la fila, actualizamos datos y ponermos foco en el textbox
        //if(!$( "#tblMatCalifXBim_txt_f"+(f)+"_c"+jsCaSeXBi.califConFoco).is(":focus")){
        $( "#tblMatCalifXBim_txt_f"+(f)+"_c"+jsCaSeXBi.califConFoco ).focus();
        $( "#tblMatCalifXBim_txt_f"+(f)+"_c"+jsCaSeXBi.califConFoco ).select();
        //}
        setDatosLabel ("MatCalifXBim", "", "", "", "", "");
    }, function(f,c){
        $("#tblMatCalifXBim_txt_f"+f+"_c"+1).on("keyup",function (e){ tblMatCalifXBim_Keyup (e, f, 1); });
        $("#tblMatCalifXBim_txt_f"+f+"_c"+2).on("keyup",function (e){ tblMatCalifXBim_Keyup (e, f, 2); });
        $("#tblMatCalifXBim_txt_f"+f+"_c"+1).on("change",function () { matCalifXBim_calif1_Change(f); });
        $("#tblMatCalifXBim_txt_f"+f+"_c"+2).on("change",function () { matCalifXBim_calif2_Change(f); });
        $("#tblMatCalifXBim_txt_f"+f+"_c"+1).on("focus",function () { jsCaSeXBi.califConFoco = 1; });
        $("#tblMatCalifXBim_txt_f"+f+"_c"+2).on("focus",function () { jsCaSeXBi.califConFoco = 2; });
    });
    $("#tblMatCalifXBim tr").attr("title","Escriba la calificación y oprima la tecla enter para pasar al siguiente renglón.\n\nEl enter en el último renglón guarda las calificaciones.");
    tblMatCalifXBim_ApplyCellAttribute ();
    tabla.setSelectedRow ('tblMatCalifXBim', 0);
    $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
    $( "#tblMatCalifXBim_txt_f0_c1" ).select();
    jsCaSeXBi.tblMatCalifXBim_calif1_OldValue = tabla.getTable ('tblMatCalifXBim', ["calif1"]);
    jsCaSeXBi.tblMatCalifXBim_calif2_OldValue = tabla.getTable ('tblMatCalifXBim', [2]);                                   //Forzosamente necesitamos el visible al usuario
}

function insertarTabla_Inasistencias(tblInasistencias)
{    
    var colsTitle = new Array ("inst1","inst2","inst3");
    var colsToShow = new Array ("inst1","inst2","inst3");
    
    //====  Se cambio a editable no importando si esta o no oficializado -- Comentado x ely 25-05-18 
     var inDataCaseEvOf = new Array (verifBimClose (1), verifBimClose (2), verifBimClose (3)); //Se volvio a activar para ciclo 2023-2024
    //var inDataCaseEvOf = new Array ("textbox", "textbox", "textbox");   // Comentado activo para ciclo 2022-2023     
    tabla.create("scrlInasistencias","tblInasistencias",tblInasistencias, colsTitle, colsToShow, inDataCaseEvOf, ["right","right","right"], false, null, null, null);    
}

function insertarTabla_ComDocAlum(tblComDocAlum)
{    
    var tabla_com = new Tabla ();
    var colsTitle = new Array ("1°TRIM","2°TRIM","3°TRIM");
    var colsToShow = new Array ("trim1","trim2","trim3");
    //====  Se cambio a editable no importando si esta o no oficializado -- Comentado x ely 25-05-18         
    tabla_com.create("scrlComDocAlum","tblComDocAlum",tblComDocAlum, colsTitle, colsToShow, ["combobox","combobox","combobox"], ["center","center","center"], false, null, null, null );    
        
    /*********** Insertamos el catálogo para los combos de Comunicacion docente alumno y establecemos el dato que trae el muchito ***********/
    $("#pnlComDocAlum .coltblComDocAlum_col0 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col0 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c0").val(tblComDocAlum[0].trim1);
    //$("#tblComDocAlum_cbx_f0_c0 option[value='1']").attr("selected",true);
    
    $("#pnlComDocAlum .coltblComDocAlum_col1 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col1 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c1").val(tblComDocAlum[0].trim2);
    
    $("#pnlComDocAlum .coltblComDocAlum_col2 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#pnlComDocAlum .coltblComDocAlum_col2 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
    $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c2").val(tblComDocAlum[0].trim3);  
}

function desactivarBoton (objeto)
{
    $( "#"+objeto )
        .mouseenter(function() {
            if (!$(this).hasClass("botonSeleccionado"))
                if (!$(this).hasClass("botonHover"))
                    $( this ).addClass("botonHover");

        })
        .mouseleave(function() {
            $( this ).removeClass("botonHover");
    });
}

function btnAnteriorCiclo_Click()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    //$('input#foo').val()//
    var datos = {
        modulo:"caSeXBi", metodo:"btAnCi", tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, 
        tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, tblAlumCapCalif_grado:jsCaSeXBi.tblAlumCapCalif_grado, 
        cicescin:sisVars.cicescin, califCicEscIn:jsCaSeXBi.califCicEscIn, numeval:jsCaSeXBi.bim
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
                    //jsCaSeXBi.paTras = result.paTras;
                    if (result.paTras === "si") 
                    {
                        jsCaSeXBi.califCicEscIn = result.califCicEscIn;
                        $("#lblCiclo").text(result.lblCiclo);
                        jsCaSeXBi.ofs = result.ofs;
                        jsCaSeXBi.tblAlumCapCalif_idalu = (result.tblAlumCapCalif.length) ? result.tblAlumCapCalif[0].idalu : -1;
                        //jsCaSeXBi.ingles=result.alertaIngles;
                        insertarTablas_AlumCapCalif_y_Inasistencias(result.tblAlumCapCalif, result.tblMatCalifXBim, result.tblInasistencias); //Aqui iba tambien tblInasistencias
                        
                        var visible = ( jsCaSeXBi.tipoCambCic === 1 || parseInt(result.tblAlumCapCalif[0].cicescini)===parseInt(sisVars.cicescin) );
                        object_setVisible(visible,"pnlBimestre");
                        object_setVisible(visible,"pnlTblMatCalifXBim");
                        //object_setVisible(visible,"pnlInasistencias");
                        //object_setVisible(visible,"pnlConOSinProm");
                        object_setVisible(visible,"pnlMemo1");
                        boton_setVisible (visible,"btnGenBim");   //Comentado el 21-09-2018 por ely
                        boton_setVisible (visible,"btnChekMat");  //Comentado el 21-09-2018 por ely
                        //boton_setVisible (visible,"btnElimBimAlum");  //Comentado el 21-09-2018 por ely
                        //boton_setVisible (visible,"btnGenInasis");  //Comentado el 21-09-2018 por ely
                        //boton_setVisible (visible,"btnElimBim");  //Comentado el 21-09-2018 por ely
                    }

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

function btnSiguienteCiclo_Click()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"caSeXBi", metodo:"btSiCi", tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, 
        tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, numeval:jsCaSeXBi.bim, califCicEscIn:jsCaSeXBi.califCicEscIn, cicescin:sisVars.cicescin
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
                    //jsCaSeXBi.paTras = result.paTras;
                    if (jsCaSeXBi.califCicEscIn < sisVars.cicescin )
                    {
                        jsCaSeXBi.califCicEscIn = result.califCicEscIn;
                        $("#lblCiclo").text(result.lblCiclo);
                        jsCaSeXBi.ofs = result.ofs;
                        //jsCaSeXBi.ingles=result.alertaIngles;
                        jsCaSeXBi.tblAlumCapCalif_idalu = (result.tblAlumCapCalif.length) ? result.tblAlumCapCalif[0].idalu : -1;
                        insertarTablas_AlumCapCalif_y_Inasistencias(result.tblAlumCapCalif, result.tblMatCalifXBim, result.tblInasistencias); //Aqui tambien iba tblInasistencias
                    }
                    
                    object_setVisible(true,"pnlBimestre");
                    object_setVisible(true,"pnlTblMatCalifXBim");
                    //object_setVisible(true,"pnlInasistencias");
                    //object_setVisible(true,"pnlConOSinProm");
                    object_setVisible(true,"pnlMemo1");
                    boton_setVisible (true,"btnGenBim");  //Comentado el 21-09-2018 por ely
                    boton_setVisible (true,"btnChekMat");  //Comentado el 21-09-2018 por ely
                    // boton_setVisible (true,"btnElimBimAlum");  Comentado el 21-09-2018 por ely
                    //boton_setVisible (true,"btnGenInasis");   //Comentado el 21-09-2018 por ely
                    // boton_setVisible (jsCaSeXBi.btnElimBim_Visible,"btnElimBim");  //Comentado el 21-09-2018 por ely
                    
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

function btnAnteriorBimestre_Click()
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var mensaje = new Mensajes();
    if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","desplazarce entre sus evaluaciones");
    else {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btAnBi", bim:jsCaSeXBi.bim, tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, 
            tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini:jsCaSeXBi.califCicEscIn, califCicEscIn:jsCaSeXBi.califCicEscIn, 
            tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo,
            bimMax: jsCaSeXBi.bimMax, bimMin: jsCaSeXBi.bimMin
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
                        $('#lblBim').text(result.lblBim);
                        jsCaSeXBi.bim = result.bim;
                        jsCaSeXBi.ofs = result.ofs;
                        //jsCaSeXBi.ingles = result.alertaIngles;
                        if(jsCaSeXBi.bim!=="0") {
                            jsCaSeXBi.tblMatCalifXBim_Columns2_Visible = result.tblMatCalifXBim_Columns2_Visible;
                            //---------------------- Dibujamos la tabla tblMatCalifXBim ----------------------\\
                            insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
                        }
                        //insertarTabla_ComDocAlum(result.tblComDocAlum);
                        //---------------------- Dibujamos la tabla de comunicacion docente-alumno ----------------------------//
                        /*var tabla_com = new Tabla ();
                        var colsTitle_com = new Array ("1°TRIM","2°TRIM","3°TRIM");
                        var colsToShow_com = new Array ("trim1","trim2","trim3");
                        //====  Se cambio a editable no importando si esta o no oficializado -- Comentado x ely 25-05-18         
                        tabla_com.create("scrlComDocAlum","tblComDocAlum",result.tblComDocAlum, colsTitle_com, colsToShow_com, ["combobox","combobox","combobox"], ["center","center","center"], false, null, null, null );    
                        */
                        /*********** Insertamos el catálogo para los combos de Comunicacion docente alumno y establecemos el dato que trae el muchito ***********/
                       /* $("#pnlComDocAlum .coltblComDocAlum_col0 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col0 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c0").val(result.tblComDocAlum[0].trim1);
                        //$("#tblComDocAlum_cbx_f0_c0 option[value='1']").attr("selected",true);

                        $("#pnlComDocAlum .coltblComDocAlum_col1 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col1 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c1").val(result.tblComDocAlum[0].trim2);

                        $("#pnlComDocAlum .coltblComDocAlum_col2 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col2 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c2").val(result.tblComDocAlum[0].trim3); */
                        
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

function btnSiguienteBimestre_Click()
{
    var mensaje = new Mensajes();
    if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","desplazarce entre sus evaluaciones");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btSiBi", bim:jsCaSeXBi.bim, tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct,
            tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini:jsCaSeXBi.califCicEscIn, califCicEscIn:jsCaSeXBi.califCicEscIn, 
            tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo,
            bimMax: bimMax, bimMin: bimMin
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
                case 1: case 2:
                        $('#lblBim').text(result.lblBim);
                        jsCaSeXBi.bim = result.bim;
                        jsCaSeXBi.ofs = result.ofs;
                        //jsCaSeXBi.ingles = result.alertaIngles;
                        if(jsCaSeXBi.bim!=="0") {
                            jsCaSeXBi.tblMatCalifXBim_Columns2_Visible = result.tblMatCalifXBim_Columns2_Visible;
                            //---------------------- Dibujamos la tabla tblMatCalifXBim ----------------------\\
                            insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
                        }
                        //insertarTabla_ComDocAlum(result.tblComDocAlum);                        
                        //--------------------------- Dibujamos la tabla de comunicacion docente-alumno -----------------------------//
                        /*var tabla_com = new Tabla ();
                        var colsTitle_com = new Array ("1°TRIM","2°TRIM","3°TRIM");
                        var colsToShow_com = new Array ("trim1","trim2","trim3");
                        //====  Se cambio a editable no importando si esta o no oficializado -- Comentado x ely 25-05-18         
                        tabla_com.create("scrlComDocAlum","tblComDocAlum",result.tblComDocAlum, colsTitle_com, colsToShow_com, ["combobox","combobox","combobox"], ["center","center","center"], false, null, null, null );    
                        */
                        /*********** Insertamos el catálogo para los combos de Comunicacion docente alumno y establecemos el dato que trae el muchito ***********/
                       /* $("#pnlComDocAlum .coltblComDocAlum_col0 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col0 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c0").val(result.tblComDocAlum[0].trim1);
                        //$("#tblComDocAlum_cbx_f0_c0 option[value='1']").attr("selected",true);

                        $("#pnlComDocAlum .coltblComDocAlum_col1 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col1 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c1").val(result.tblComDocAlum[0].trim2);

                        $("#pnlComDocAlum .coltblComDocAlum_col2 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col2 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c2").val(result.tblComDocAlum[0].trim3); 
                        //-----------------------------------------------------------------------------------------------------//                                                
                        */
                        if (result.returnCase === 2)
                        {
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                            jsCaSeXBi.calProm = result.calProm;
                        }
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

function btnGuardaInasistencias_Click()
{
    var mensaje = new Mensajes();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar su inasistencias");
    else if (jsCaSeXBi.ofs.todoOf && !jsCaSeXBi.ofs.algunDeof)
        mensaje.CalifSecXBim("TODO_OFICIALIZADO");
    else{
        var filaTblInasistencias = tabla.getRow ("tblInasistencias",0,null,"VISIBLES","array");
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btGuIn", tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini:jsCaSeXBi.califCicEscIn, 
            tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, inst1:filaTblInasistencias[0], inst2:filaTblInasistencias[1], inst3:filaTblInasistencias[2]
            //, inst4:filaTblInasistencias[3], inst5:filaTblInasistencias[4]
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
                        cerrarLoading();
                        mensaje.General("GUARDADO_EXITOSO");
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

function btnGuardaComDocAlum_Click()
{
    var mensaje = new Mensajes();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","guardar Com. Docente-Alumno");
    else if (jsCaSeXBi.ofs.todoOf && !jsCaSeXBi.ofs.algunDeof)
        mensaje.CalifSecXBim("TODO_OFICIALIZADO");
    else{
        var filaTblComDocAlum = tabla.getRow ("tblComDocAlum",0,null,"VISIBLES","array");
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btGuCom", tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini:jsCaSeXBi.califCicEscIn, 
            tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, trim1:filaTblComDocAlum[0], trim2:filaTblComDocAlum[1], trim3:filaTblComDocAlum[2]
            //, inst4:filaTblInasistencias[3], inst5:filaTblInasistencias[4]
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
                        cerrarLoading();
                        mensaje.General("GUARDADO_EXITOSO");
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

function btnGenBim_Click()
{
    var mensaje = new Mensajes();
    
    // if (!jsCaSeXBi.ofs.todoDeof) -agregado por mi en algun momento
    // if (jsCaSeXBi.ofs.bimOf) - de maai      
    if (jsCaSeXBi.ofs.todoOf && !jsCaSeXBi.ofs.algunDeof)    
        return mensaje.CalifSecXBim("BLOQUEADO_POR_OFICIALIZACION");
    else if (tabla.getNumRows("tblAlumCapCalif") === 0)
        mensaje.CalifSecXBim("SIN_ALUMNOS");
    else {
        /*if(jsCaSeXBi.ingles)
            var respMatIngles = mensaje.CalifSecXBim("INGLES","","","CONFIRM_DIALOG");        
        else respMatIngles = "";*/
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btGeBi", bim:jsCaSeXBi.bim, tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, califCicEscIn:jsCaSeXBi.califCicEscIn, idalu:jsCaSeXBi.tblAlumCapCalif_idalu, 
            tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan//, bandIngles: respMatIngles
        };
        //------------------------- Hacemos la llamada -------------------------
        cargarLoading();
        boton_setEnabled(false,'btnGenBim');
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:true
        })
        .done(function(result){
            switch(result.returnCase){
                case 1:                        
                        insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
                        //jsCaSeXBi.ingles = result.alertaIngles;
                        cerrarLoading();
                    break;
                case 2:
                        insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
                        //jsCaSeXBi.ingles = result.alertaIngles;
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
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
        boton_setEnabled (true, "btnGenBim", "click", function(){ btnGenBim_Click(); return false; } );
    }
}

function btnChekMat_Click()
{
    var mensaje = new Mensajes();
    
    //if (jsCaSeXBi.ofs.algunOf && !jsCaSeXBi.ofs.todoDeof) Comentando el 09-04-2024
    if (jsCaSeXBi.ofs.bimOf && !jsCaSeXBi.ofs.alDeof)  //Bimestre oficializado y alumnoficializado/No se encuentra desoficializado
        mensaje.CalifSecXBim("BLOQUEADO_POR_OFICIALIZACION");
    else if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","corregir sus materias");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btChMa", tblMatCalifXBim_isEmpty:tabla.getNumRows('tblMatCalifXBim')===0?true:false, califCicEscIn:jsCaSeXBi.califCicEscIn, 
            tblAlumCapCalif_cicescini:jsCaSeXBi.califCicEscIn, tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblAlumCapCalif_grado:jsCaSeXBi.tblPrincipal_grado,
            tblAlumCapCalif_grupo:jsCaSeXBi.tblPrincipal_grupo, bim:jsCaSeXBi.bim, tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu, 
            tblAlumCapCalif_cveplan:jsCaSeXBi.tblPrincipal_cveplan, tblPrincipal_cct:jsCaSeXBi.tblPrincipal_cct
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
                        insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
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

function btnElimBimAlum_Click()
{
    var mensaje = new Mensajes();
    
    if (jsCaSeXBi.ofs.algunOf && !jsCaSeXBi.ofs.todoDeof)
        mensaje.CalifSecXBim("BLOQUEADO_POR_OFICIALIZACION");
    else if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","eliminar sus evaluaciones");
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btElBiAl", tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu, bim:jsCaSeXBi.bim, 
            califCicEscIn:jsCaSeXBi.califCicEscIn
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
                        insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
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

function btnElimBim_Click()
{
    var mensaje = new Mensajes();
    if (jsCaSeXBi.ofs.algunOf)                                                  //porque afecta a todos los alumnos
        return mensaje.CalifSecXBim("BLOQUEADO_POR_OFICIALIZACION");
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"caSeXBi", metodo:"btElBi", tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, 
        tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, bim:jsCaSeXBi.bim, tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu, 
        califCicEscIn:jsCaSeXBi.califCicEscIn
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
                    insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
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

function btnGenInasis_Click()
{    
    var mensaje = new Mensajes();  
    if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","generar su renglón de inasistencias");
    /*else if (jsCaSeXBi.ofs.todoOf)  // tenia algunOf todoDeof
        mensaje.CalifSecXBim("BLOQUEADO_POR_OFICIALIZACION");*/  // Se quito, comentado con roberto caso de Emmanuel alumno de traslado
    else{
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"btGeIn", tblAlumCapCalif:tabla.getTable('tblAlumCapCalif',['idalu']), tblAlumCapCalif_cicescini:jsCaSeXBi.califCicEscIn,
            tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado,
            tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo
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
                        tblAlumCapCalif_ChangeSelectedItem (0);
                        insertarTabla_Inasistencias(result.tblInasistencias);
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
}

function btnAnalisMat_Click()
{
    var mensaje = new Mensajes();
    
    if ( tabla.getSelectedIndexRow("tblAlumCapCalif") === -1)
        mensaje.General("NO_SELEC"," alumno","mostrar sus calificaciones");
    else{
        var tblAlumCapCalif_nom_tot = tabla.getSelectedRow("tblAlumCapCalif", ["nom_tot"]);
        frmfBimXAlum_Show (tblAlumCapCalif_nom_tot, jsCaSeXBi.tblPrincipal_cct, jsCaSeXBi.tblPrincipal_nombre, jsCaSeXBi.tblPrincipal_grado, jsCaSeXBi.tblPrincipal_cveplan, jsCaSeXBi.tblAlumCapCalif_idalu, jsCaSeXBi.califCicEscIn);
    }
}

function btnAnteriorGrupo_Click()
{
    if (jsCaSeXBi.hayCambiosXBim) 
        btnGdaCalifReal_Click ();
    
    if ( tabla.getSelectedIndexRow ('tblPrincipal')-1 >= 0 ){
        var datos = { 
            modulo:"caSeXBi", metodo:"btAnGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","nombre","cveplan"]), 
            posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaSeXBi.califCicEscIn, cbxBim_SelItem:jsCaSeXBi.bim 
        };
        sigAntGrupo (datos);
    }
}

function btnSiguienteGrupo_Click()
{
    if (jsCaSeXBi.hayCambiosXBim)
        btnGdaCalifReal_Click ();

    var datos = { 
        modulo:"caSeXBi", metodo:"btSiGp", tblPrincipal:tabla.getTable("tblPrincipal",["cct","grado","grupo","idcct","nombre","cveplan"]), 
        posSelActual:tabla.getSelectedIndexRow ('tblPrincipal'), califCicEscIn:jsCaSeXBi.califCicEscIn, cbxBim_SelItem:jsCaSeXBi.bim 
    };
    sigAntGrupo (datos);
}

function btnReporteAvances_Click()
{
    var mensaje = new Mensajes();
    
   /* mensaje.General("REPORTE_EN_CREACION","","","");
    alert("AQUI");
    showReport ("frmfReportes", "Reportes/FinDeCurso.jsp", {cicescini:jsCaSeXBi.tblPrincipal_cicescini,cicescinilib:jsCaSeXBi.tblPrincipal_cicescini, cveplan:jsCaSeXBi.tblPrincipal_cveplan, 
                                                            idcct:jsCaSeXBi.tblPrincipal_idcct, cct:jsCaSeXBi.tblPrincipal_cct,
                                                            grado:jsCaSeXBi.tblPrincipal_grado, grupo:jsCaSeXBi.tblPrincipal_grupo, caso:"Ra"});
    */
    mensaje.General("REPORTE_EN_CREACION","","","");
        window.open("Reportes/FinDeCurso.jsp?r=Grupo/historialAcademico&cicescini="+jsCaSeXBi.tblPrincipal_cicescini+"&cicescinilib="+jsCaSeXBi.tblPrincipal_cicescini
                        +"&cveplan="+jsCaSeXBi.tblPrincipal_cveplan+"&idcct="+jsCaSeXBi.tblPrincipal_idcct+"&cct="+jsCaSeXBi.tblPrincipal_cct
                        +"&grado="+jsCaSeXBi.tblPrincipal_grado+"&grupo="+jsCaSeXBi.tblPrincipal_grupo+"&caso=Ra");
}

function sigAntGrupo (datos)
{
    var mensaje = new Mensajes ();
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
                    jsCaSeXBi.ofs = result.ofs;
                    tabla.setSelectedRow ('tblPrincipal', result.tblPrincipal_selectedRow);
                    frmwCalifSecXBim_Show( false, jsCaSeXBi.rbgXBimOPromOVal_Checked, jsCaSeXBi.chkCalProm_isChecked, jsCaSeXBi.cveunidad, jsCaSeXBi.bim, jsCaSeXBi.calProm, jsCaSeXBi.califCicEscIn, result.tblPrincipal_idcct, result.tblPrincipal_cct, result.tblPrincipal_nombre, result.tblPrincipal_cveplan, result.tblPrincipal_grado, result.tblPrincipal_grupo );
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

function chkTotMat_Click ()
{
    if(document.getElementById('chkTotMat').checked){
        var mensaje = new Mensajes();
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"chToMa", tblAlumCapCalif_idalu:jsCaSeXBi.tblAlumCapCalif_idalu===""?0:jsCaSeXBi.tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini:jsCaSeXBi.califCicEscIn
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
                        //---------------------- Dibujamos la tabla tblInasistencias ----------------------\\
                        tabla.create("scrlTotMat","tblTotMat",result.tblTotMat, ["idalu","Bimestre","total de materias"], ["idalu","numeval","nummat"], null, ["center","center","center"], true, null, null, null);
                        object_setVisible(true,'pnlTblTotMat');
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
    }else {
        object_setVisible(false,'pnlTblTotMat');
    }
}

function btnGdaCalifReal_Click ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    if ( validaCalificaciones () )
    {
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"caSeXBi", metodo:"tbMaCa_KePr", numLlamada:0, tblAlumCapCalif_SelecRow:tabla.getSelectedIndexRow ('tblAlumCapCalif'), 
            tblMatCalifXBim_size:tabla.getNumRows('tblMatCalifXBim'), tblPrincipal_cct:jsCaSeXBi.tblPrincipal_cct,
            tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct, tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo,
            existeCalif2: (jsCaSeXBi.tblMatCalifXBim_calif2_OldValue[0].indexOf("colUndefined")===-1)?true:false,
            tblMatCalifXBim_calif1_OldValue:jsCaSeXBi.tblMatCalifXBim_calif1_OldValue, tblMatCalifXBim_calif2_OldValue:jsCaSeXBi.tblMatCalifXBim_calif2_OldValue, 
            califCicEscIn:jsCaSeXBi.califCicEscIn, calProm:jsCaSeXBi.calProm, cicescin:sisVars.cicescin, 
            tblMatCalifXBim:tabla.getTable ('tblMatCalifXBim', ["calif1",2,"idalu","cvemat","cvetipmat","numeval","cicescini","grado","cveplan","cvemat","cvetipmat"]), 
            tblPrincipal_grado:jsCaSeXBi.tblPrincipal_grado, tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, 
            tblAlumCapCalif:tabla.getSelectedRow ('tblAlumCapCalif', ["matrepact","idalu","grado","matrepensecu","c_rep","cicescini","promdp","promd1rop","promd2dop","promd3rop","promd4top","promd5top","debe1ro","debe2do"]), 
            matRepGdo:0, matAprobGdo:0, porcAsist:0, aluSolicitud:0, sumCalif:0, noMat:0, todasSusMat:"", esp:"", mat:"", prom:"0.0", respAlumProCond:"", respAlumRep:"", respAcredPorCursarlo:""
        };
        //------------------------- Hacemos la llamada --------tbMaCa_KePr-----------------
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
                        var respAlumProCond=true, respAlumRep=true, respAcredPorCursarlo=true;
                        /*if ( datos.tblPrincipal_cveplan==="1" && datos.califCicEscIn===datos.cicescin && result.prom>=6.0 && result.matRepGdo>0 && result.matRepGdo<=2 && (result.tblAlumCapCalif_grado==="4" || result.tblAlumCapCalif_grado==="5" ) && result.todasSusMat==="SI" ) //para 4to y 5to puede pasar  con condicion si su promed es minimo de 6 y solo reprueba 2 materias
                            respAlumProCond = mensaje.CalifSecXBim("PROMOVIDO_COND","","","CONFIRM_DIALOG");
                        else if ( datos.tblPrincipal_cveplan==="1" && datos.califCicEscIn===datos.cicescin && ( result.esp==="R" || result.mat==="R") && ( result.tblAlumCapCalif_grado==="1" || result.tblAlumCapCalif_grado==="2" ) && result.todasSusMat==="SI" ) // la norma dice k 1ro, 2do y 3ro pasa xk pasa Sofi dice k hay k dejar k el profesor decida si pasa o no pasa
                        {
                            if ( result.tblAlumCapCalif_c_rep==="R" )  //si se encuentra repitiendo el grado, pasa x k pasa
                                respAlumRep = mensaje.CalifSecXBim("ALUMREP_PROMOVIDO","","","CONFIRM_DIALOG");
                            else 
                                respAcredPorCursarlo = mensaje.CalifSecXBim("ACRED_X_CURSADO","","","CONFIRM_DIALOG");
                        }*/
                        
                        // Cambiamos valores de los parámetros de respuesta
                        datos.matRepGdo=result.matRepGdo; datos.matAprobGdo=result.matAprobGdo; datos.porcAsist=result.porcAsist; datos.aluSolicitud=result.aluSolicitud; 
                        datos.sumCalif=result.sumCalif; datos.noMat=result.noMat; datos.todasSusMat=result.todasSusMat; 
                        datos.numLlamada = 1;       datos.esp=result.esp;       datos.mat=result.mat;       datos.prom=result.prom;
                        datos.respAlumProCond=(respAlumProCond?"YES":"NO");     
                        datos.respAlumRep=(respAlumRep?"YES":"NO");     datos.respAcredPorCursarlo=(respAcredPorCursarlo?"YES":"NO");

                        var numFilas = tabla.getNumRows ('tblAlumCapCalif');
                        var posSelActual = datos.tblAlumCapCalif_SelecRow;
                        datos.cbxBim_SelItem = jsCaSeXBi.bim;
                        datos.tblAlumCapCalif_changeToidalu = (posSelActual < (numFilas-1)) ? tabla.getRow ("tblAlumCapCalif", posSelActual+1, ['idalu'], null, "JSON").idalu:-1;
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
                                        tabla.updateRow ('tblAlumCapCalif',datos.tblAlumCapCalif_SelecRow,result.tblAlumCapCalif);
                                        if (posSelActual < (numFilas-1)){
                                            //-------------------- Seleccionamos la siguiente fila de tblAlumCapCalif y obtenemos los datos correspondientes
                                            /*tblAlumCapCalif_ChangeSelectedItem (posSelActual+1);*/
                                            //------ Establecemos la selección de la fila en la tabla ------
                                            /*var tblAlumCapCalif_selRow = tabla.getRow ("tblAlumCapCalif", posSelActual+1, ['cveprograma','idalu','curp','debe1ro','debe2do'], null, "JSON");
                                            /*jsCaSeXBi.tblAlumCapCalif_idalu=tblAlumCapCalif_selRow.idalu;
                                            setDatosLabel ("AlumCapCalif", tblAlumCapCalif_selRow.cveprograma, tblAlumCapCalif_selRow.idalu, tblAlumCapCalif_selRow.curp, tblAlumCapCalif_selRow.debe1ro,tblAlumCapCalif_selRow.debe2do);
                                            tabla.setSelectedRow ('tblAlumCapCalif', posSelActual+1);

                                            //--------------------- Rellenamos tablas ----------------------
                                            insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
                                            insertarTabla_Inasistencias(result.tblInasistencias);*/
                                            //-------------------- -------------------- -------------------- -------------------- -------------------- 
                                            tblAlumCapCalif_ChangeSelectedItem (posSelActual+1, function(){
                                                tabla.setSelectedRow ('tblMatCalifXBim', 0);
                                                $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
                                                $( "#tblMatCalifXBim_txt_f0_c1" ).select();
                                                jsCaSeXBi.tblMatCalifXBim_calif1_OldValue = tabla.getTable ('tblMatCalifXBim', ["calif1"]);
                                                jsCaSeXBi.tblMatCalifXBim_calif2_OldValue = tabla.getTable ('tblMatCalifXBim', [2]);                //Forzosamente necesitamos el que está visible al usuario
                                                //..................................................         //Forsozo repetir esta sección de código por cuestión de llamada a subproceso
                                                jsCaSeXBi.hayCambiosXBim = false;
                                                //..................................................
                                            });
                                        }else                                   //Se tendrá que poner en un else para que no se ejecute si entra al subproceso tblAlumCapCalif_ChangeSelectedItem
                                            jsCaSeXBi.hayCambiosXBim = false;
                                        //cerrarLoading();
                                    break;
                                case 2:
                                        result.tblAlumCapCalif_promediogral;
                                        //result.todasSusMat;
                                        var tblAlumCapCalif = tabla.getSelectedRow ('tblAlumCapCalif', ["idalu","matrepact","promedio","promediogral"], null, "JSON");
                                        frmfPromGralSec_Show (datos.txtUsuario, datos.califCicEscIn, tblAlumCapCalif.idalu, tblAlumCapCalif.matrepact, tblAlumCapCalif.promedio, tblAlumCapCalif.promediogral );
                                        //cerrarLoading();
                                    break;
                                case 3:
                                        //result.todasSusMat;
                                        if (typeof result.mensaje!=="undefined")
                                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                                        frmfPromGralSec2012_Show (result.tblAlumCapCalif.promdp, result.QPromGral2012.promd1ros, result.QPromGral2012.promd2dos, result.QPromGral2012.promd3ros, result.tblAlumCapCalif.debe1ro, result.tblAlumCapCalif.debe2do, result.tblAlumCapCalif.matrepact, result.tblAlumCapCalif.promediogral, result.tblAlumCapCalif.promedioeb, result.tblAlumCapCalif.idalu, datos.txtUsuario, datos.califCicEscIn);
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

function validaCalificaciones ()
{
    var mensaje = new Mensajes();
    var formatoCalif=new RegExp("^[0-9]$");
    
    if (verifBimClose ()==="")
        return false;
    
    var tblMatCalifXBim = tabla.getTable('tblMatCalifXBim',["desmat","calif1","calif2"],null,"JSON");
    for (var i=0; i<tblMatCalifXBim.length; i++)
    {
        //if (!isFloat (tblMatCalifXBim[i].calif1))
        if (tblMatCalifXBim[i].calif1!=="10" && !formatoCalif.test(tblMatCalifXBim[i].calif1))
            return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif1");
        if ( !(parseFloat(tblMatCalifXBim[i].calif1) >= 0 && parseFloat(tblMatCalifXBim[i].calif1) <= 10) )
            return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif1");
        
        
        if (jsCaSeXBi.tblMatCalifXBim_Columns2_Visible && jsCaSeXBi.tblPrincipal_cveplan==="2"){
            //if ( !isFloat (tblMatCalifXBim[i].calif2) )
            if (tblMatCalifXBim[i].calif2!=="10" && !formatoCalif.test(tblMatCalifXBim[i].calif2))
                return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif2");
            if ( !(parseFloat(tblMatCalifXBim[i].calif2) >= 0 && parseFloat(tblMatCalifXBim[i].calif2) <= 10) )
                return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif2");
        }
        
        /*if (parseFloat(tblMatCalifXBim[i].calif1) < 5.0){
            if (parseFloat(tblMatCalifXBim[i].calif1)===0.0) {
                if (jsCaSeXBi.tblMatCalifXBim_Columns2_Visible && jsCaSeXBi.tblPrincipal_cveplan==="2"){
                    if (parseFloat(tblMatCalifXBim[i].calif2)===0.0)
                        return mensaje.CalifSecXBim ("CALIF_CERO_COL2",tblMatCalifXBim[i].desmat,"calif2");
                    else if (parseFloat(tblMatCalifXBim[i].calif2) < 5.0)
                        return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif2");
                } else
                    return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif1");
            }else
                return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif1");
        }if (jsCaSeXBi.tblMatCalifXBim_Columns2_Visible && jsCaSeXBi.tblPrincipal_cveplan==="2"){
            if (parseFloat(tblMatCalifXBim[i].calif2)!==0.0){
                if (parseFloat(tblMatCalifXBim[i].calif1) >= 5.0 && parseFloat(tblMatCalifXBim[i].calif1) < 6.0) {
                    if(parseFloat(tblMatCalifXBim[i].calif2) < 5.9)
                        return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif2");
                }else
                    return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif2");
            }
        }*/
        if(jsCaSeXBi.tblPrincipal_cveplan==="1" && (jsCaSeXBi.tblPrincipal_grado==="1")){            
            if( parseFloat(tblMatCalifXBim[i].calif1) >= 0.0 && parseFloat(tblMatCalifXBim[i].calif1) < 6.0 )
                return mensaje.CalifSecXBim ("CALIF_NO_VALIDA_GDO",tblMatCalifXBim[i].desmat,"calif1");
        } 
        if( parseFloat(tblMatCalifXBim[i].calif1) >= 0.0 && parseFloat(tblMatCalifXBim[i].calif1) < 5.0 )
                return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif1");
            
        if (parseFloat(tblMatCalifXBim[i].calif1) >= 6.0 && parseFloat(tblMatCalifXBim[i].calif1) <= 10.0){
            if (jsCaSeXBi.tblMatCalifXBim_Columns2_Visible && jsCaSeXBi.tblPrincipal_cveplan==="2")
                if (parseFloat(tblMatCalifXBim[i].calif2)!==0.0)
                    return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif2");
        }else if (parseFloat(tblMatCalifXBim[i].calif1) >= 5.0 && parseFloat(tblMatCalifXBim[i].calif1) < 6.0 || parseFloat(tblMatCalifXBim[i].calif1) === 0.0){
            if (jsCaSeXBi.tblMatCalifXBim_Columns2_Visible && jsCaSeXBi.tblPrincipal_cveplan==="2")
                if(parseFloat(tblMatCalifXBim[i].calif2)!==0.0 && (parseFloat(tblMatCalifXBim[i].calif2) < 6.0 || parseFloat(tblMatCalifXBim[i].calif2) > 10.0))
                    return mensaje.CalifSecXBim ("CALIF_NO_VALIDA",tblMatCalifXBim[i].desmat,"calif2");
        }else
            return mensaje.CalifSecXBim ("CALIFS_NO_VALIDAS",tblMatCalifXBim[i].desmat,"calif1");                
    }
    return true;
}

function selecSigRenglon_MatCalifXBim (filaActual, colActual, numFilas)
{
    if (filaActual<(numFilas-1)){
        tabla.setSelectedRow ('tblMatCalifXBim', filaActual+1);
        $( "#tblMatCalifXBim_txt_f"+(filaActual+1)+"_c"+colActual ).focus();
        $( "#tblMatCalifXBim_txt_f"+(filaActual+1)+"_c"+colActual ).select();
        jsCaSeXBi.califConFoco = colActual;
        setDatosLabel ("MatCalifXBim", "", "", "", "", "");
    }
}
function selecAntRenglon_MatCalifXBim (filaActual, colActual)
{
    if (filaActual>0){
        tabla.setSelectedRow ('tblMatCalifXBim', filaActual-1);
        $( "#tblMatCalifXBim_txt_f"+(filaActual-1)+"_c"+colActual ).focus();
        $( "#tblMatCalifXBim_txt_f"+(filaActual-1)+"_c"+colActual ).select();
        setDatosLabel ("MatCalifXBim", "", "", "", "", "");
    }
}
function selecSigCol_MatCalifXBim (filaActual, colActual)
{
    if ( $( "#tblMatCalifXBim_txt_f"+(filaActual)+"_c"+(colActual+1) ).length )
        $( "#tblMatCalifXBim_txt_f"+(filaActual)+"_c"+(colActual+1) ).focus();
}
function selecAntCol_MatCalifXBim (filaActual, colActual)
{
    if ( $( "#tblMatCalifXBim_txt_f"+(filaActual)+"_c"+(colActual-1) ).length )
        $( "#tblMatCalifXBim_txt_f"+(filaActual)+"_c"+(colActual-1) ).focus();
}

function tblAlumCapCalif_ChangeSelectedItem (index, executarFuncion)
{
    var mensaje = new Mensajes();
    
    var tblAlumCapCalif_selRow = tabla.getRow ("tblAlumCapCalif", index, ['cveprograma','idalu','curp','debe1ro','debe2do'], null, "JSON");
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"caSeXBi", metodo:"tbMaCa_ChSeIt", numeval:jsCaSeXBi.bim, tblAlumCapCalif_idalu:tblAlumCapCalif_selRow.idalu, 
        califCicEscIn:jsCaSeXBi.califCicEscIn, tblPrincipal_grado:jsCaSeXBi.tblAlumCapCalif_grado, tblPrincipal_grupo:jsCaSeXBi.tblPrincipal_grupo, 
        tblPrincipal_idcct:jsCaSeXBi.tblPrincipal_idcct,tblPrincipal_cveplan:jsCaSeXBi.tblPrincipal_cveplan, cct: jsCaSeXBi.tblPrincipal_cct
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
                    jsCaSeXBi.tblAlumCapCalif_idalu=tblAlumCapCalif_selRow.idalu;
                    jsCaSeXBi.ofs = result.ofs;
                    setDatosLabel ("AlumCapCalif", tblAlumCapCalif_selRow.cveprograma, tblAlumCapCalif_selRow.idalu, tblAlumCapCalif_selRow.curp, tblAlumCapCalif_selRow.debe1ro,tblAlumCapCalif_selRow.debe2do);
                    tabla.setSelectedRow ('tblAlumCapCalif', index);
                    
                    //--------------------- Rellenamos tablas ----------------------
                    insertarTabla_MatCalifXBim(result.tblMatCalifXBim);
                    insertarTabla_Inasistencias(result.tblInasistencias);                    
                    //insertarTabla_ComDocAlum(result.tblComDocAlum);
                    //--------------------------- Dibujamos la tabla de comunicacion docente-alumno -----------------------------//
                        /*var tabla_com = new Tabla ();
                        var colsTitle_com = new Array ("1°TRIM","2°TRIM","3°TRIM");
                        var colsToShow_com = new Array ("trim1","trim2","trim3");
                        //====  Se cambio a editable no importando si esta o no oficializado -- Comentado x ely 25-05-18         
                        tabla_com.create("scrlComDocAlum","tblComDocAlum",result.tblComDocAlum, colsTitle_com, colsToShow_com, ["combobox","combobox","combobox"], ["center","center","center"], false, null, null, null );    

                        //================= Insertamos el catálogo para los combos de Comunicacion docente alumno y establecemos el dato que trae el muchito =========/
                        $("#pnlComDocAlum .coltblComDocAlum_col0 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col0 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c0").val(result.tblComDocAlum[0].trim1);
                        //$("#tblComDocAlum_cbx_f0_c0 option[value='1']").attr("selected",true);

                        $("#pnlComDocAlum .coltblComDocAlum_col1 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col1 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c1").val(result.tblComDocAlum[0].trim2);

                        $("#pnlComDocAlum .coltblComDocAlum_col2 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
                        $("#pnlComDocAlum .coltblComDocAlum_col2 select").append("<option value='0' title=''></option><option value='1' title=''>1-Continua</option><option value='2' title=''>2-Intermitente</option><option value='3' title=''>3-Nula</option>"); // Valores definidos
                        $("#pnlComDocAlum #tblComDocAlum_cbx_f0"+"_c2").val(result.tblComDocAlum[0].trim3); 
                        *///-----------------------------------------------------------------------------------------------------//   
                    
                    if (typeof executarFuncion !== "undefined")
                        executarFuncion();
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

function matCalifXBim_calif1_Change(f)
{
    var d={calif1:$("#tblMatCalifXBim_txt_f"+f+"_c"+1).val(), calif2:$("#tblMatCalifXBim_txt_f"+f+"_c"+2).val()};         
    /* Usada en Version de ciclo 2017-2018
     * if (!isFloat(d.calif1)) {
        var mensaje = new Mensajes ();
        mensaje.General("CALIF_INVALIDA",d.calif1);
        return false;
    }*/
    var mensaje = new Mensajes ();
    
    if (!esNumeroEntero(d.calif1) || (d.calif1 > 10 && d.calif1 < 99) ){        
        d.calif1='0';
        mensaje.General("CALIF_INVALIDA_CIC18",d.calif1);        
        return false;
    }
    if (jsCaSeXBi.tblPrincipal_cveplan==="1")   // Comentado para el ciclo 21-20
    {        
        if (jsCaSeXBi.tblPrincipal_grado==="1"  && (d.calif1===0 || d.calif1 > '0.0' && d.calif1 < 6)) { //Para ciclo 2023-2024 se quito 2do de primaria
            mensaje.CalifSecXBim("CALIF_INVALIDA_1y2PRIM",d.calif1);
            d.calif1='0';
            return false;
        }
    }
    
    if (d.calif1 >= 0 && d.calif1 < 5 ){        
        mensaje.General("CALIF_INVALIDA_CIC21",d.calif1);
        d.calif1='0';
        return false;
    }
    
    if (d.calif1  <= 0  || d.calif1  > 99) {
        d.calif1='0';
    } /*else if (d.calif1 >= 50.0)
        d.calif1=d.calif1/10;
    else if ( d.calif1 > 10.0 && d.calif1 < 50.0 )
        d.calif1='5';*/
    
    else if (d.calif1  < 5.0  && d.calif1  >= 0.0)
        d.calif1='0';
    
    if (typeof d.calif2  !== "undefined"){
        if (d.calif1 > 5.0 )    //entonces calif2=0
            d.calif2='0';
        if ( d.calif1 <= 5.0 && d.calif2 <= 5.0 ) //osea calif2 no tiene valor entonces calif2=0
            d.calif2='0';
    }
    
    /*-------------------------- Formateamos la calificación según el ciclo --------------------------*/
    if ( jsCaSeXBi.califCicEscIn<2013 ) {
        if ( (new RegExp("^1?\\d\.0+[\d]*$")).test(d.calif1) )
            d.calif1=(""+d.calif1).replace( new RegExp("\.0+[\d]*$"),"");
    }else if (jsCaSeXBi.califCicEscIn === 2013){
        if ( d.calif1===10 )
            d.calif1="10";
    }else {
        if ( (new RegExp("^1?\\d[\.]*$")).test(d.calif1) ){
            d.calif1=(""+d.calif1);
            /* Usada en Version de ciclo 2017-2018
             * d.calif1=(""+d.calif1).replace(new RegExp("[\.]*$"),"")+".0"; */
        }
    }
    
    tabla.updateRow ("tblMatCalifXBim",f,d);
    tblMatCalifXBim_ApplyCellAttribute (f);
}

function matCalifXBim_calif2_Change(f)
{
    var d={calif1:$("#tblMatCalifXBim_txt_f"+f+"_c"+1).val(), calif2:$("#tblMatCalifXBim_txt_f"+f+"_c"+2).val()};
    
    /*if (!isFloat(d.calif2)) {
        var mensaje = new Mensajes ();
        mensaje.General("CALIF_INVALIDA",d.calif2);
        d.calif2='0.0';
        return false;
    }*/
    if (!esNumeroEntero(d.calif2) || (d.calif2 > 10 && d.calif2 < 99)){        
        mensaje.General("CALIF_INVALIDA_CIC18",d.calif2);
        d.calif2='0';
        return false;
    }
    
    if (d.calif2  < 0  || d.calif2  > 99)
        d.calif2= '0';
                                                             //paso la calif1
    if (d.calif1 > 5.0)//entonces calif2=0
        d.calif2='0';
    /*else if ( d.calif2 >= 60.0  && d.calif1 <  6.0 )    //reprob la calif1 y paso la calif2
        d.calif2=d.calif2/10;
    else if (d.calif2 >= 50.0 && d.calif2 <  60.0 ) //no importa si rep o paso calif1 reprob la calif2
        d.calif2=d.calif2/10;
    else if ( d.calif2   > 10.0 && d.calif2 < 50.0 )   //no importa si rep o paso calif1
        d.calif2='5.0';*/
    else if ( d.calif2   > 0 && d.calif2 < 5 )   //no importa si rep o paso calif1
        d.calif2='5';
    else if (d.calif2  <  5.0 && d.calif2 > 0.0)//no importa si rep o paso calif1
        d.calif2='0';
    
    /*-------------------------- Formateamos la calificación según el ciclo --------------------------*/
    if ( jsCaSeXBi.califCicEscIn<2013 ) {
        if ( (new RegExp("^1?\\d\.0+[\d]*$")).test(d.calif2) )
            d.calif2=(""+d.calif2).replace( new RegExp("\.0+[\d]*$"),"");
    }else{
        if ( (new RegExp("^1?\\d[\.]*$")).test(d.calif2) )
            d.calif2=(""+d.calif2);//.replace(new RegExp("[\.]*$"),"")+".0";
    }
    
    tabla.updateRow ("tblMatCalifXBim",f,d);
    tblMatCalifXBim_ApplyCellAttribute (f);
}

function tblMatCalifXBim_Keyup (e, row, col)
{
    var numFilas;
    if (e.keyCode === 13){                                                                                                                          //Enter
        numFilas=tabla.getNumRows ('tblMatCalifXBim');
        if (row === (numFilas-1) )
            btnGdaCalifReal_Click();
        else
            selecSigRenglon_MatCalifXBim (row, col, numFilas);
    }else if (e.keyCode === 40){                                                                                                                  //Flecha abajo
        numFilas=tabla.getNumRows ('tblMatCalifXBim');
        if (row === (numFilas-1) ){
            var respuesta = window.confirm("¿Actualizar datos?");
            if (respuesta)
                btnGdaCalifReal_Click();
        }else
            selecSigRenglon_MatCalifXBim (row, col, numFilas);
    }else if (e.keyCode === 38){                                                                                                                  //Flecha arriba
        numFilas=tabla.getNumRows ('tblMatCalifXBim');
        selecAntRenglon_MatCalifXBim (row, col);
    }else if (e.keyCode === 39){                                                                                                                  //Flecha derecha
        selecSigCol_MatCalifXBim (row, col);
    }else if (e.keyCode === 37){                                                                                                                  //Flecha izquierda
        selecAntCol_MatCalifXBim (row, col);
    }else if (e.keyCode === 9){                                                                                                                    //Tab                  (e.shiftKey && e.keyCode == 9)  Shift+Tab
        tabla.setSelectedRow ('tblMatCalifXBim', row);
        setDatosLabel ("MatCalifXBim", "", "", "", "", "");
    }
    if ( (e.keyCode>=48 && e.keyCode<=57) || e.keyCode===110 || e.keyCode===8 || e.keyCode===46)           //Del 0 al 9, el punto 190 190=alfabético o 110=numérico, backspace, suprimir    //40 y 38 Flecha abajo y flecha arriba
        jsCaSeXBi.hayCambiosXBim = true;
}

function tblMatCalifXBim_ApplyCellAttribute (fila)
{
    var tabla = new Tabla ();
    var ini, fin;
    var tblMatCalifXBim = tabla.getTable("tblMatCalifXBim",["calif1","calif2"],null,"JSON");
    
    //Si se especifica la posición de una fila a la que se le desea dar el formato, si no recorre todas las filas
    if (typeof(fila)!=="undefined"){
        ini = fila;
        fin = ini+1;
    }else{
        ini = 0;
        fin = tblMatCalifXBim.length;
    }
    
    for (var i=ini; i<fin; i++)
    {
        
        $(".tblMatCalifXBim_calif1_f"+i+" input").css("font-weight","bold");
        //$(".tblMatCalifXBim_calif1_f"+i+" input").css("background-color", "white");
        $(".tblMatCalifXBim_calif1_f"+i+" input").css("color", "black");
        
        
        $(".tblMatCalifXBim_calif2_f"+i+" input").css("font-weight","bold");
        //$(".tblMatCalifXBim_calif2_f"+i+" input").css("background-color", "white");
        $(".tblMatCalifXBim_calif2_f"+i+" input").css("color", "black");
        
        if ( parseFloat(tblMatCalifXBim[i].calif1)>=6.0 )
            $(".tblMatCalifXBim_calif1_f"+i+" input").css("color", "#007700");//Verde
        else if ( parseFloat(tblMatCalifXBim[i].calif1)>0.0 && parseFloat(tblMatCalifXBim[i].calif1)<6.0)
            $(".tblMatCalifXBim_calif1_f"+i+" input").css("color", "maroon");
        
        if ( parseFloat(tblMatCalifXBim[i].calif2)>=6.0 )
            $(".tblMatCalifXBim_calif2_f"+i+" input").css("color", "#007700");//Verde
        else if ( parseFloat(tblMatCalifXBim[i].calif2)>0.0 && parseFloat(tblMatCalifXBim[i].calif2)<6.0)
            $(".tblMatCalifXBim_calif2_f"+i+" input").css("color", "maroon");
    }
}

function tblAlumCapCalif_ApplyCellAttribute(fila)
{
    var tabla = new Tabla ();
    var ini, fin;
    var tblAlumCapCalif = tabla.getTable("tblAlumCapCalif",["promediogral","promedio","promovido","matrepact","estatusalu","cveplan","grado","promd1rop","promd1ros","promd2dop","promd2dos","promd3rop","promd4top","promd5top"],null,"JSON");
    
    //Si se especifica la posición de una fila a la que se le desea dar el formato, si no recorre todas las filas
    if (typeof(fila)!=="undefined"){
        ini = fila;
        fin = ini+1;
    }else{
        ini = 0;
        fin = tblAlumCapCalif.length;
    }
    
    for (var i=ini; i<fin; i++)
    {   //========== IF para grados intermedios de Primaria ============================/
        if(((tblAlumCapCalif[i].cveplan==="1") && (
                (tblAlumCapCalif[i].grado==="2" && parseFloat(tblAlumCapCalif[i].promd1rop) < 6.0 ) ||
                (tblAlumCapCalif[i].grado==="3" && 
                    (parseFloat(tblAlumCapCalif[i].promd1rop) < 6.0 || parseFloat(tblAlumCapCalif[i].promd2dop) < 6.0) ) ||
                (tblAlumCapCalif[i].grado==="4" && 
                    (parseFloat(tblAlumCapCalif[i].promd1rop) < 6.0 || parseFloat(tblAlumCapCalif[i].promd2dop) < 6.0 || parseFloat(tblAlumCapCalif[i].promd3rop) < 6.0 )) ||
                (tblAlumCapCalif[i].grado==="5" && (parseFloat(tblAlumCapCalif[i].promd1rop) < 6.0  || parseFloat(tblAlumCapCalif[i].promd2dop) < 6.0 ||
                    parseFloat(tblAlumCapCalif[i].promd3rop) < 6.0 || parseFloat(tblAlumCapCalif[i].promd4top) < 6.0 ) ) ) ) ||
            ((tblAlumCapCalif[i].cveplan==="2") && 
                (tblAlumCapCalif[i].grado==="2" && parseFloat(tblAlumCapCalif[i].promd1ros) < 6.0 ) )    
            )
        {
            $(".tblAlumCapCalif_nom_tot_f"+i).css("font-weight","bold");
            $(".tblAlumCapCalif_nom_tot_f"+i).css("color", "#FFD133");//amarillo
        }
        
        if( (tblAlumCapCalif[i].cveplan==="2" && tblAlumCapCalif[i].grado==="3" && 
                (parseFloat(tblAlumCapCalif[i].promd1ros) < 6.0 || parseFloat(tblAlumCapCalif[i].promd2dos) < 6.0)) 
            || (tblAlumCapCalif[i].cveplan==="1" && tblAlumCapCalif[i].grado==="6" && 
                (parseFloat(tblAlumCapCalif[i].promd1rop) < 6.0 || parseFloat(tblAlumCapCalif[i].promd2dop) < 6.0 ||
                 parseFloat(tblAlumCapCalif[i].promd3rop) < 6.0 || parseFloat(tblAlumCapCalif[i].promd4top) < 6.0 ||
                 parseFloat(tblAlumCapCalif[i].promd5top) < 6.0 )) ) {
            $(".tblAlumCapCalif_nom_tot_f"+i).css("font-weight","bold");
            //$(".tblAlumCapCalif_nom_tot_f"+i).css("background-color", "red");
            $(".tblAlumCapCalif_nom_tot_f"+i).css("color", "#FF4233");//naranja F2671C
        }
        
        if ( parseFloat(tblAlumCapCalif[i].promediogral)>=6.0 ) {
            $(".tblAlumCapCalif_promediogral_f"+i).css("font-weight","bold");
            //$(".tblAlumCapCalif_promediogral_f"+i).css("background-color", "white");
            $(".tblAlumCapCalif_promediogral_f"+i).css("color", "#007700");//Verde
        }

        if ( parseFloat(tblAlumCapCalif[i].promedio)>=6.0 ) {
            $(".tblAlumCapCalif_promedio_f"+i).css("font-weight","bold");
            //$(".tblAlumCapCalif_promedio_f"+i).css("background-color", "white");
            $(".tblAlumCapCalif_promedio_f"+i).css("color", "#007700");//Verde
        }

        if ( tblAlumCapCalif[i].promovido==="P" )
            $(".tblAlumCapCalif_promovido_f"+i).css("color", "#007700");//Verde
        else if ( tblAlumCapCalif[i].promovido==="NP" )
            $(".tblAlumCapCalif_promovido_f"+i).css("color", "maroon");

        if ( parseInt(tblAlumCapCalif[i].matrepact)>0 )
            $(".tblAlumCapCalif_matrepact_f"+i).css("color", "maroon");

        if ( tblAlumCapCalif[i].estatusalu==="BD" )
            $(".tblAlumCapCalif_estatusalu_f"+i).css("color", "gray");        
    }

}

function btnKardex1_Click()
{
    var mensaje = new Mensajes();
    
    mensaje.CalifSecXBim("KARDEX_EN_CREACION","","","");
    //window.open("Calificaciones/Kardex.jsp?r=Calificaciones/Kardex/Kardex&cveunidad="+jsCaSeXBi.cveunidad+"&califCicEscIn="+jsCaSeXBi.califCicEscIn+"&grado="+jsCaSeXBi.tblPrincipal_grado+"&grupo="+jsCaSeXBi.tblPrincipal_grupo+"&idcct="+jsCaSeXBi.tblPrincipal_idcct);
    window.open("Calificaciones/Kardex.jsp?r=Calificaciones/Kardex&cveunidad="+jsCaSeXBi.cveunidad+"&califCicEscIn="+jsCaSeXBi.califCicEscIn+"&cicescini="+jsCaSeXBi.tblPrincipal_cicescini+"&idcct="+jsCaSeXBi.tblPrincipal_idcct+"&grado="+jsCaSeXBi.tblPrincipal_grado+"&grupo="+jsCaSeXBi.tblPrincipal_grupo);
}

function btnFoliosKardex_Click()
{
    var mensaje = new Mensajes();
    
    mensaje.CalifSecXBim("FOLKARDEX_EN_CREACION","","","");
    window.open("Calificaciones/Kardex.jsp?r=Calificaciones/foliosKardex&cveunidad="+jsCaSeXBi.cveunidad+"&califCicEscIn="+jsCaSeXBi.califCicEscIn+"&idcct="+jsCaSeXBi.tblPrincipal_idcct+"&grado="+jsCaSeXBi.tblPrincipal_grado+"&grupo="+jsCaSeXBi.tblPrincipal_grupo);
}

function btnHistAcad_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var caso = "CalifSecXBim";
    
    var tblAlumCapCalif = tabla.getSelectedRow("tblAlumCapCalif", ["idalu","curp","nom_tot","cicescini","promediogral","frma"],null,"JSON");
    var alumSigAnt_Visible = ( jsCaSeXBi.tipoCambCic === 1 || parseInt(tblAlumCapCalif.cicescini)===parseInt(sisVars.cicescin) );
    if(tblAlumCapCalif.frma==="null" || tblAlumCapCalif.frma===null) {
        if (jsCaSeXBi.tipoCambCic===1 || ((jsCaSeXBi.tipoCambCic===0 || jsCaSeXBi.tipoCambCic === -1) && (tblAlumCapCalif.cicescini===sisVars.cicescin || (tblAlumCapCalif.promediogral === "0.0" && parseInt(tblAlumCapCalif.cicescini)===parseInt(sisVars.cicescin-1)) )) || (parseInt(jsCaSeXBi.tblPrincipal_grado)<=2 && tblAlumCapCalif.cicescini===sisVars.cicescin))
            frmwSecHist_Show (jsCaSeXBi.tblPrincipal_cicescini,tblAlumCapCalif.idalu, tblAlumCapCalif.curp, tblAlumCapCalif.nom_tot, jsCaSeXBi.tblPrincipal_cveplan, tblAlumCapCalif.frma, alumSigAnt_Visible,null,caso);
        else
            mensaje.CalifSecXBim("PERMISO_MODIF_EXAMEXTRAORD");
    }
    else {               
            mensaje.CalifSecXBim("ALU_CON_CERTIFICADO");
    }
}

function btnAlumnosConPromXMat_Click ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes();
    
    var tblPrincipal = tabla.getSelectedRow("tblPrincipal", ["modalidad"],null,"JSON");
    mensaje.General("REPORTE_EN_CREACION","","","");
    window.open("Calificaciones/AlumnosConPromXMat.jsp?r=Calificaciones/alumnosConPromXMat&cicescini="+jsCaSeXBi.califCicEscIn+"&modalidad="+tblPrincipal.modalidad
            +"&cveplan="+jsCaSeXBi.tblPrincipal_cveplan+"&idcct="+jsCaSeXBi.tblPrincipal_idcct+"&grado="+jsCaSeXBi.tblPrincipal_grado
            +"&grupo="+jsCaSeXBi.tblPrincipal_grupo);
    
}