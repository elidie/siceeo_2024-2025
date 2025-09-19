/* 
    Creado el : 01/09/2015, 12:40:21 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCamDeGpo;

function frmwCamDeGpo_Show (tblPrincipal_idcct, tblPrincipal_modalidad, tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan, tblPrincipal_cveturno, tblPrincipal_nombre, tblPrincipal_cicescini)
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsCamDeGpo = {
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_modalidad: tblPrincipal_modalidad,
        tblPrincipal_cct: tblPrincipal_cct,
        tblPrincipal_grado: tblPrincipal_grado,
        tblPrincipal_grupo: tblPrincipal_grupo,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_cveturno: tblPrincipal_cveturno,
        tblPrincipal_nombrecct: tblPrincipal_nombre,
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        
        
        cbxGrupo_Visible:false,
        cvedefsuf_oldValue:"",
        cvelengua_oldValue:"",
        etnia_oldValue:"",
        califCicEscIn: tblPrincipal_cicescini,
        catDiscap: new Array(),
        gposL: new Array(),
        xprimeraves:"si",
        hayCambios:false,
        
        esInscripOfic:true,
        /*isTodoCalOf: false,*/
        idalusDeof: new Array(),
        ofs: null
    };

    object_setVisible (false,"gridTable");
    $("#frmwCamDeGpo").css("display", "block");                                 // Mostramos el formulario correspondiente

    frmwCamDeGpo_Create();
    frmwCamDeGpo_FormActivate ();
}

function frmwCamDeGpo_Close()
{
    jsCamDeGpo=null;
    irAVentanaPrincipal ();
}

function frmwCamDeGpo_Create() 
{
    if($('#frmfCamDeGpo').length)                                                                                                               // Verificando si la tabla Existe
        $('#frmfCamDeGpo').remove();
    $('#frmwCamDeGpo').append('<fieldset id="frmfCamDeGpo"><legend>Cambio de grupo y bajas</legend> <div id="btnRegresar_CamDeGpo" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCamDeGpo").append('<div id="pnlCamDeGpo"></div>');
            
            $('#pnlCamDeGpo').append('<div id="pnlAreaTrabajoCamDeGpo"></div>');
                $('#pnlAreaTrabajoCamDeGpo').append('<div id="pnlTablaCamDeGpo"></div>');
                    $('#pnlTablaCamDeGpo').append('<div id="pnlDatosGenerales"></div>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct"> ... </label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado"> ... </label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo"> ... </label>');
                    $('#pnlTablaCamDeGpo').append('<div id="pnlTblCamDeGpo">  <div id="scrlCamDeGpo" class="scrollTable"></div>  </div>');
                $('#pnlAreaTrabajoCamDeGpo').append('<div id="pnlBotonesDecontrol"></div>');
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                    $('#pnlBotonesDecontrol').append('<ul id="ulbtnsCambiosIndividuales" class="buttonBar">'+
                                                        '<li id="libtnBajaDefi"><a href="#" id="btnBajaDefi">Baja definitiva</a></li>'+
                                                        '<li id="libtnInscrito"><a href="#" id="btnInscrito">Camb Inscrito</a></li>'+                                                        
                                                        //'<li id="libtnCorrecciones"><a href="#" id="btnCorrecciones">Correcciones</a></li>'+
                                                        '<li id="libtnCamDGpo"><a href="#" id="btnCamDGpo">Guardar cambios</a></li>'+
                                                        //'<li id="libtnEliminarAlu" title="Elimina de la Base de Datos al alumno seleccionado"><a href="#" id="btnEliminarAlu">Eliminar</a></li>'+
                                                    '</ul>');
                    
                    $('#pnlBotonesDecontrol').append('<ul class="buttonBar">'+
                                                            //'<li><a href="#" id="btnImpRepHistorialAcademico" title="Ver Historial Académico del alumno">Hist. Académico.</a></li>'+
                                                           // '<li><a href="#" id="btnReporteInscripcion" title="Ver Reporte de Inscripción">Rep. Inscripción.</a></li>'+
                                                            '<li><a href="#" id="btnAnteriorGpo" title="Grupo Anterior"><label class="iconBtnGpoAnt iconBtnRedondo icon-arrow-left4"></label><label class="alinearHoriz">Gpo. anterior</label></a></li>'+
                                                            '<li><a href="#" id="btnSiguienteGpo" title="Siguiente grupo"><label class="alinearHoriz">Siguiente gpo.</label><label class="iconBtnGpoSig iconBtnRedondo icon-arrow-right4"></label></a></li>'+
                                                      '</ul>');
                    $('#pnlBotonesDecontrol').append('<ul class="buttonBar">  <li><a href="#" id="btnAnteriorCiclo"><label class="iconBtnCicAnt iconBtnRedondo icon-arrow-left4"></label><label class="alinearHoriz">Ciclo anterior</label></a></li>  </ul>');
                    $('#pnlBotonesDecontrol').append('<label id="lblCiclo"> ... </label>');
                    $('#pnlBotonesDecontrol').append('<ul class="buttonBar">  <li><a href="#" id="btnSiguienteCiclo"><label class="alinearHoriz">Siguiente ciclo</label><label class="iconBtnCicSig iconBtnRedondo icon-arrow-right4"></label></a></li>  </ul>');
                    $('#pnlBotonesDecontrol').append('<div id="pnlbtnTotAlum"> </div>');
                        $('#pnlbtnTotAlum').append('<ul class="buttonBar alinearHoriz">  <li title="Calcular total de alumnos"><a href="#" id="btnTotAlum"><label id="ibtnTotAlum" class="icon-usuarios"></label></a></li>  </ul>    <label id="lblTotal" class="alinearHoriz"> ... </label>');
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            $('#pnlCamDeGpo').append('<div id="pnlMensajeCamDeGpo">'
                                        +'<ul class="buttonBar alinearHoriz"> '
                                            +'<li title="Exporta el grupo a Excel"><a href="#" id="btnExportGrupoAExcel"><label id="ibtnExportGrupoAExcel" class="icon-exportexcel middleHoriz"></label><label id="lbtnExportGrupoAExcel" class="middleHoriz">Exportar a Excel</label></a></li>'
                                        +'</ul>'
                                        +'<label id="lblMensajeCamDeGpo" class="alinearHoriz">SICEEO no imprime a los alumnos que aun no tienen asignado un GRUPO(\'---\').</label>'
                                    +'</div>');
    
        // ----------------- Cargar elementos permitidos de acuerdo al usuario ---------------------------------------------
        permisosGpo();
        //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
        $("#btnRegresar_CamDeGpo").unbind("click");
        $("#btnRegresar_CamDeGpo").on("click",function(){ frmwCamDeGpo_Close(); });
        
        $('#btnBajaDefi').on('click', function(event){  btnBajaDefi_Click(); event.preventDefault();   });
        $('#btnInscrito').on('click', function(event){ btnInscrito_Click(); event.preventDefault(); });
        $('#btnRevocacionGdo').on('click', function(event){ btnRevocacionGdo_Click(); event.preventDefault(); });
        $('#btnConstancia').on('click', function(event){ btnConstanciaDeBaja_Click(); event.preventDefault(); });
        
        //$('#btnCorrecciones').on('click', function(event){ btnCorrecciones_Click(); event.preventDefault();  });
        $('#btnCamDGpo').on('click', function(event){ btnCamDGpo_Click(); event.preventDefault();  });
        $('#btnEliminarAlu').on('click', function(event){ btnEliminarAlu_Click(); event.preventDefault();  });
        
        //$('#btnCamTotGpo').on('click', function(event){ btnCamTotGpo_Click(); event.preventDefault();  }); //OJO: Este código ya no debe de existir, quedó obsoleto
        $('#btnAnteriorGpo').on('click', function(event){ btnAnteriorGpo_CamDeGpo_Click();  event.preventDefault(); });
        $('#btnSiguienteGpo').on('click', function(event){ btnSiguienteGpo_CamDeGpo_Click();  event.preventDefault();  });
        $('#btnAnteriorCiclo').on('click', function(event){ btnAnteriorCiclo_CamDeGpo_Click(); event.preventDefault();  });
        $('#btnSiguienteCiclo').on('click', function(event){ btnSiguienteCiclo_CamDeGpo_Click(); event.preventDefault();  });  
        $('#btnTotAlum').on('click', function(event){ btnTotAlum_Click(); event.preventDefault();  });
        
        $("#btnExportGrupoAExcel").on("click", function(){ btnExportGrupoAExcel_ActionPerformed (); /*e.preventDefault();*/ });
        /*$("#btnImpRepHistorialAcademico").on("click", function(){ btnImpRepHistorialAcademico_ActionPerformed (); });*/
        //$("#btnReporteInscripcion").on("click", function(){ btnReporteDeInscripcion_ActionPerformed (); });
}

function frmwCamDeGpo_FormActivate() 
{
    var mensaje = new Mensajes ();
        
    var datos = { 
        modulo:"CaDeGp", metodo:"foAc", xprimeraves:jsCamDeGpo.xprimeraves, califCicEscIn:jsCamDeGpo.califCicEscIn, cicescini:jsCamDeGpo.tblPrincipal_cicescini, 
        tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_cveplan:jsCamDeGpo.tblPrincipal_cveplan, tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, 
        tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo
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
                    /*jsCamDeGpo.isTodoCalOf = result.isTodoCalOf;*/
                    frmwCamDeGpo_InitData(result);
                    cerrarLoading();
                break;
            case 0:case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
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

function frmwCamDeGpo_InitData(result) 
{
    jsCamDeGpo.esInscripOfic = result.esInscripOfic;
    jsCamDeGpo.idalusDeof = result.idalusDeof;
    jsCamDeGpo.ofs = result.ofs;
    
    $('#lblTblPrincipal_cct').text(jsCamDeGpo.tblPrincipal_cct);
    $('#lblTblPrincipal_grado').text(jsCamDeGpo.tblPrincipal_grado);
    $('#lblTblPrincipal_grupo').text(jsCamDeGpo.tblPrincipal_grupo);

    $('#lblCiclo').text(result.lblCiclo);

    if(typeof result.btnCamTotGpo_Visible !== "undefined" && result.btnCamTotGpo_Visible === false) boton_setVisible(false,'btnCamTotGpo');
    
    /* ------ OJO: Este código ya no debe de existir, quedó obsoleto -----
     * if(typeof result.cbxGrupo_Visible !== "undefined" && result.cbxGrupo_Visible === false) object_setVisible (false,'cbxGrupo');
    if (typeof result.cbxGrupo_Visible !== "undefined" && result.cbxGrupo_Visible === true){
        //$("#frmwCamDeGpo #cbxGrupo").append("<option value=''></option>");
        for (var i=0; i<result.cbxGrupo.length; i++)
            $("#frmwCamDeGpo #cbxGrupo").append("<option value='"+result.cbxGrupo[i].grupo+"'>"+result.cbxGrupo[i].grupo+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
        //$('#frmwCamDeGpo #cbxGrupo').val('');                                                                                         //Dejamos seleccionado por default el grupo en blanco
    }*/
    
    if(typeof result.btnBajaDefi_Visible !== "undefined" && result.btnBajaDefi_Visible === false) boton_setVisible(false,'btnBajaDefi');
    if(typeof result.btnRevocacionGdo_Visible !== "undefined" && result.btnRevocacionGdo_Visible === false) boton_setVisible(false,'btnRevocacionGdo');
    if(typeof result.btnInscrito_Visible !== "undefined" && result.btnInscrito_Visible === false) boton_setVisible(false,'btnInscrito');
    if(typeof result.btnCorrecciones_Visible !== "undefined" && result.btnCorrecciones_Visible === false) boton_setVisible(false,'btnCorrecciones');
    if(typeof result.btnCamDGpo_Visible !== "undefined" && result.btnCamDGpo_Visible === false) boton_setVisible(false,'btnCamDGpo');
    if(typeof result.btnConstancia_Visible !== "undefined" && result.btnConstancia_Visible === false) boton_setVisible(false,'btnConstancia');

    $('#lblTotal').text(result.lblTotal);

    if (jsCamDeGpo.xprimeraves==="si"){
        jsCamDeGpo.catDiscap = result.catDiscap;
        jsCamDeGpo.gposL = result.gposL;
        jsCamDeGpo.catLenguas = result.catLenguas;
    }
    initTablaTblCamDeGpo (result.tblCamDeGpo);

    if ( typeof result.btnAnteriorCiclo_Enabled !== "undefined" ) boton_setEnabled (result.btnAnteriorCiclo_Enabled, 'btnAnteriorCiclo');
    if ( typeof result.btnSiguienteCiclo_Enabled !== "undefined" ) boton_setEnabled (result.btnSiguienteCiclo_Enabled, 'btnSiguienteCiclo');
    if ( typeof result.btnBajaDefi_Enabled !== "undefined" ) boton_setEnabled (result.btnBajaDefi_Enabled, 'btnBajaDefi');
    if ( typeof result.btnRevocacionGdo_Enabled !== "undefined" ) boton_setEnabled (result.btnRevocacionGdo_Enabled, 'btnRevocacionGdo');
    if ( typeof result.btnInscrito_Enabled !== "undefined" ) boton_setEnabled (result.btnInscrito_Enabled, 'btnInscrito');
    if ( typeof result.btnCorrecciones_Enabled !== "undefined" ) boton_setEnabled (result.btnCorrecciones_Enabled, 'btnCorrecciones');
    //boton_setEnabled (false, 'btnCorrecciones');  //OJO: Lo desactivamos mientras no se implemente
    if ( typeof result.btnCamDGpo_Enabled !== "undefined" ) boton_setEnabled (result.btnCamDGpo_Enabled, 'btnCamDGpo');
    
    object_setVisible(!jsCamDeGpo.ofs.bimOf,"ulbtnsCambiosIndividuales");
    
    jsCamDeGpo.xprimeraves = result.xprimeraves;
}

function initTablaTblCamDeGpo (tblCamDeGpo)
{
    var tabla = new Tabla();
    tabla.create("scrlCamDeGpo","tblCamDeGpo",tblCamDeGpo, ["idalu","curp","nom_tot","Est","Gpo","Nec. Especial","probem","Lengua","Afromexicana"], ["idalu","curp","nom_tot","estatusgrado","grupo","cvedefsuf","probem","cvelengua","etnia"], ["","","","","combobox","combobox","","combobox","combobox"], ["","","","center","center","center","center","center","center"], true, "ctrlClick", 
                function (f){
                    if (jsCamDeGpo.ofs.bimOf)
                        object_setVisible((indexOf (jsCamDeGpo.idalusDeof, $("#frmwCamDeGpo .tblCamDeGpo_idalu_f"+f).text()) !== -1),"ulbtnsCambiosIndividuales");
                }, 
                function (f){
                    $(".tblCamDeGpo_grupo_f"+f).on( "change", function(){ 
                        jsCamDeGpo.hayCambios = true;
                    });
                    $(".tblCamDeGpo_cvedefsuf_f"+f).on( "change", function(){ 
                        jsCamDeGpo.hayCambios = true;
                    });
                    $(".tblCamDeGpo_cvelengua_f"+f).on( "change", function(){ 
                        jsCamDeGpo.hayCambios = true;
                    });
                    $(".tblCamDeGpo_etnia_f"+f).on( "change", function(){ 
                        jsCamDeGpo.hayCambios = true;
                    });
                }
            );
    /*********** Insertamos el catálogo para los combos de discapacidad y establecemos el dato que trae el muchito ***********/
    $("#frmwCamDeGpo .coltblCamDeGpo_col7 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#frmwCamDeGpo .coltblCamDeGpo_col7 select").append("<option value=''></option>");                    //Agregamos una opción en blanco
    if (typeof jsCamDeGpo.catLenguas !== "undefined")
        $.each(jsCamDeGpo.catLenguas,function(clave,valor) {
           $("#frmwCamDeGpo .coltblCamDeGpo_col7 select").append("<option value='"+clave.trim()+"' title='"+clave.trim()+"-"+valor.trim()+"'>"+valor.trim()+"</option>");      //lista de lenguas                  
        });    
    for (var i=0; i<tblCamDeGpo.length; i++)                                                                //A cada muchito le seleccionamos su dato de lengua en su combo correspondienet
        $("#frmwCamDeGpo #tblCamDeGpo_cbx_f"+i+"_c7").val(tblCamDeGpo[i].cvelengua);
    
    /*********** Insertamos el catálogo para los combos de discapacidad y establecemos el dato que trae el muchito ***********/
    $("#frmwCamDeGpo .coltblCamDeGpo_col8 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#frmwCamDeGpo .coltblCamDeGpo_col8 select").append("<option value=''></option><option value='NO'>NO</option><option value='SI'>SI</option>");  //Agregamos una opción en blanco, SI, NO
    for (var i=0; i<tblCamDeGpo.length; i++)                                                                //A cada muchito le seleccionamos su dato de lengua en su combo correspondienet
        $("#frmwCamDeGpo #tblCamDeGpo_cbx_f"+i+"_c8").val(tblCamDeGpo[i].etnia);
    
    /*********** Insertamos el catálogo para los combos de discapacidad y establecemos el dato que trae el muchito ***********/
    $("#frmwCamDeGpo .coltblCamDeGpo_col5 select option:selected").remove();                                //Limpiamos todos los combos de discapacidad
    $("#frmwCamDeGpo .coltblCamDeGpo_col5 select").append("<option value=''></option>");                    //Agregamos una opción en blanco
    if (typeof jsCamDeGpo.catDiscap !== "undefined")
        for (var i=0; i<jsCamDeGpo.catDiscap.length; i++)                                                   //Insertamos el catálogo de discapacidades en todos los combos correspendientes
            $("#frmwCamDeGpo .coltblCamDeGpo_col5 select").append("<option value='"+jsCamDeGpo.catDiscap[i].cvedefsuf+"' title='"+jsCamDeGpo.catDiscap[i].cvedefsuf+' - '+jsCamDeGpo.catDiscap[i].desdefsuf+"'>"+jsCamDeGpo.catDiscap[i].cvedefsuf+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+jsCamDeGpo.catDiscap[i].desdefsuf+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
    for (var i=0; i<tblCamDeGpo.length; i++)                                                                //A cada muchito le seleccionamos su dato de discapacidad en su combo correspondienet
        $("#frmwCamDeGpo #tblCamDeGpo_cbx_f"+i+"_c5").val(tblCamDeGpo[i].cvedefsuf);
    /***********************************************************************************************/
    $("#frmwCamDeGpo .coltblCamDeGpo_col4 select option:selected").remove();                                //Limpiamos todos los combos de grupo de la tabla
    for (var i=0; i<jsCamDeGpo.gposL.length; i++)                                                           //Insertamos el catálogo de nombres de grupos en todos los combos correspendientes
        $("#frmwCamDeGpo .coltblCamDeGpo_col4 select").append("<option value='"+jsCamDeGpo.gposL[i].grupo+"'>"+jsCamDeGpo.gposL[i].grupo+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
    $("#frmwCamDeGpo .coltblCamDeGpo_col4 select").append("<option value='---' disabled>---</option>");     //Agregamos un tipo de grupo que significa aún no asignado
    for (var i=0; i<tblCamDeGpo.length; i++)                                                                //A cada muchito le seleccionamos su dato de discapacidad en su combo correspondienet
        $("#frmwCamDeGpo #tblCamDeGpo_cbx_f"+i+"_c4").val(tblCamDeGpo[i].grupo);
    //$('#frmwCamDeGpo .coltblCamDeGpo_col4 select').val(jsCamDeGpo.tblPrincipal_grupo);                    //Dejamos seleccionado por default el grupo correspondiente.
    /***********************************************************************************************/
    /*$("#frmwCamDeGpo #cbxGrupo").append("<option value=''></option>");
    for (var i=0; i<jsCamDeGpo.gposL.length; i++)
        $("#frmwCamDeGpo #cbxGrupo").append("<option value='"+jsCamDeGpo.gposL[i].grupo+"'>"+jsCamDeGpo.gposL[i].grupo+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
    $('#frmwCamDeGpo #cbxGrupo').val('');                                                                   //Dejamos seleccionado por default el grupo en blanco
    */
    //--Pend--> Agregar color rojo para los que tienen BD
    jsCamDeGpo.cvedefsuf_oldValue = new Array();
    jsCamDeGpo.cvelengua_oldValue = new Array();
    jsCamDeGpo.etnia_oldValue = new Array();
    for (var i=0; i<tblCamDeGpo.length; i++) {
        jsCamDeGpo.cvedefsuf_oldValue[i] = tblCamDeGpo[i].cvedefsuf;                                        //Respaldamos la columna porque la usaremos en el btnCamDGpo_Click
        jsCamDeGpo.cvelengua_oldValue[i] = tblCamDeGpo[i].cvelengua;
        jsCamDeGpo.etnia_oldValue[i] = tblCamDeGpo[i].etnia;
    }
    if (jsCamDeGpo.ofs.bimOf){
        $("#frmwCamDeGpo #tblCamDeGpo select").prop("disabled","true");
        if (jsCamDeGpo.idalusDeof.length>0){
            for (var i=0; i<tblCamDeGpo.length; i++)
                if (indexOf (jsCamDeGpo.idalusDeof, tblCamDeGpo[i].idalu) !== -1)
                    $("#frmwCamDeGpo #tblCamDeGpo_f"+i+" select").removeAttr("disabled");
        }
    }
            
}



function btnBajaDefi_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla(), filasSelec;
    
    if (jsCamDeGpo.esInscripOfic)
        mensaje.CamDeGpo("INSCRIPCION_OFICIALIZADA");         
    /*else if (mensaje.CamDeGpo("CANCELAR_FOL_REPEVAL",jsCamDeGpo.tblPrincipal_cicescini+"-"+(parseInt(jsCamDeGpo.tblPrincipal_cicescini)+1), "","CONFIRM_DIALOG"))
    {*/       
    filasSelec = tabla.getSelectedRows("tblCamDeGpo",["idalu","estatusgrado","grado","grupo","idcct","cicescini","num_mat","num_eval3"]);        
    
    if(filasSelec.length > 0) {                                          
        var datos = {  modulo:"CaDeGp", metodo:"btBaDe", tblCamDeGpo: filasSelec, califCicEscIn:jsCamDeGpo.califCicEscIn, 
            cicescin:sisVars.cicescin, tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_cveplan:jsCamDeGpo.tblPrincipal_cveplan };
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
                        var indexes = tabla.getSelectedIndexesRow ("tblCamDeGpo");
                        for (var i=0; i<indexes.length; i++)
                            tabla.updateRow("tblCamDeGpo", indexes[i], {estatusgrado:"BD"});
                        //--Pend--> Hay que pintar de color rojo
                        mensaje.General("GUARDADO_EXITOSO", "", ""); 
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
                        cerrarLoading();
                    break;
                default:break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
            cerrarLoading();
        });
    }else
        mensaje.Principal("SELEC","alumno");
    /*} llave del if de mensaje de confirmación */
}

function btnInscrito_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    var filasSelec;
    
    if (jsCamDeGpo.esInscripOfic)
        mensaje.CamDeGpo("INSCRIPCION_OFICIALIZADA");
    else {
        filasSelec = tabla.getSelectedRows("tblCamDeGpo",["idalu","estatusgrado"]);
        if(filasSelec.length > 0) {                  
            var datos = {  modulo:"CaDeGp", metodo:"btIn", tblCamDeGpo: filasSelec, califCicEscIn:jsCamDeGpo.califCicEscIn, 
                cicescin:sisVars.cicescin, tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, 
                tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo
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
                            initTablaTblCamDeGpo (result.tblCamDeGpo);
                            mensaje.General("GUARDADO_EXITOSO", "", ""); 
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
                            cerrarLoading();
                        break;
                    default:break;
                }
            })
            .fail(function() {
                mensaje.General("ERROR_AJAX", "", "");
                cerrarLoading();
            });
        }else
            mensaje.Principal("SELEC","alumno");
    }
}

function verificarPromedio(filasSelec)
{
    var arrayFilas = (""+filasSelec).split(',');
    var fila, alumnos="",mensaje="";
    for(var i=0; i < arrayFilas.length; i++)
    {
        fila = (""+arrayFilas[i]).split('~');
        if(fila[2]<6.0 || fila[2]>7.0)
            alumnos += fila[0] + ",";        
    }
    if(alumnos.length>1)
        mensaje = "El o los alumnos con idalu:"+alumnos.slice(0,-1);
    
    return mensaje;    
}


function btnRevocacionGdo_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    var filasSelec;
    
    if (jsCamDeGpo.esInscripOfic)
        mensaje.CamDeGpo("INSCRIPCION_OFICIALIZADA");    
    else {
        filasSelec = tabla.getSelectedRows("tblCamDeGpo",["idalu","estatusgrado","promedio"]);
        if(filasSelec.length > 0) {    
            var msg = verificarPromedio(filasSelec);            
            if(msg.length === 0)
            {                
                var datos = {  modulo:"CaDeGp", metodo:"btRg", tblCamDeGpo: filasSelec, califCicEscIn:jsCamDeGpo.califCicEscIn, 
                    cicescin:sisVars.cicescin, tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, 
                    tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo
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
                                initTablaTblCamDeGpo (result.tblCamDeGpo);
                                mensaje.General("GUARDADO_EXITOSO", "", ""); 
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
            else
                mensaje.Principal("NO_APLICA",msg);
        }else
            mensaje.Principal("SELEC","alumno");
    }
}

function btnCorrecciones_Click ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    
    if (jsCamDeGpo.esInscripOfic)
        mensaje.CamDeGpo("INSCRIPCION_OFICIALIZADA");
    else {
        /*var tblCamDeGpo = tabla.getSelectedRow("tblCamDeGpo",["idalu","cicescini"],null,"JSON");
        var posSelActual = tabla.getSelectedIndexRow ('tblCamDeGpo');

        if( posSelActual >= 0 )
            frmwModifCurp_Show ( "frmwCamDeGpo", "DESDEBAJAS", tblCamDeGpo.idalu, jsCamDeGpo.tblPrincipal_cicescini, tblCamDeGpo.cicescini, jsCamDeGpo.tblPrincipal_modalidad, jsCamDeGpo.tblPrincipal_cveplan, jsCamDeGpo.tblPrincipal_idcct, jsCamDeGpo.tblPrincipal_grado, jsCamDeGpo.tblPrincipal_grupo);// antes lo llamaba el modulo de BAJAS
        else
            mensaje.Principal("SELEC","alumno");*/
    }
}

function btnCamDGpo_Click()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var tblCamDeGpo;
    
    if (jsCamDeGpo.esInscripOfic)
        mensaje.CamDeGpo("INSCRIPCION_OFICIALIZADA");
    else {
        tblCamDeGpo = tabla.getTable("tblCamDeGpo",["idalu","grupo","grupo_oldvalue","cvedefsuf","cvelengua","etnia"]);

        if(tblCamDeGpo.length > 1) {                  
            var datos = {  modulo:"CaDeGp", metodo:"btCaDGp", califCicEscIn:jsCamDeGpo.califCicEscIn, tblCamDeGpo: tblCamDeGpo, 
                tblCamDeGpo_cvedefsuf_oldValue: jsCamDeGpo.cvedefsuf_oldValue, tblCamDeGpo_cvelengua_oldValue: jsCamDeGpo.cvelengua_oldValue,
                tblCamDeGpo_etnia_oldValue: jsCamDeGpo.etnia_oldValue,tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, 
                tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo };
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
                            initTablaTblCamDeGpo (result.tblCamDeGpo);
                            jsCamDeGpo.hayCambios = false;
                            jsCamDeGpo.idalusDeof = result.idalusDeof;
                            mensaje.General("GUARDADO_EXITOSO", "", ""); 
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
                            cerrarLoading();
                        break;
                    default:break;
                }
            })
            .fail(function() {
                mensaje.General("ERROR_AJAX", "", "");
                cerrarLoading();
            });
        }else
            mensaje.Principal("SELEC","alumno");
    }
}

function btnEliminarAlu_Click ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var tblCamDeGpo;
    
    if (jsCamDeGpo.esInscripOfic)
        mensaje.CamDeGpo("INSCRIPCION_OFICIALIZADA");
    else {
        tblCamDeGpo = tabla.getSelectedRow("tblCamDeGpo",["idalu","cveplan"],null,"JSON");

        if(typeof tblCamDeGpo.idalu === "undefined") 
            mensaje.Principal("SELEC","alumno");
        else if (mensaje.confirmDialog ("ELIMINAR","el registro") === true)
        {
            var datos = {  modulo:"CaDeGp", metodo:"btElAl", califCicEscIn:jsCamDeGpo.califCicEscIn, idalu:tblCamDeGpo.idalu, 
                tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_cveplan:jsCamDeGpo.tblPrincipal_cveplan };
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
                            var posSel = tabla.getSelectedIndexRow("tblCamDeGpo");
                            tabla.removeRow("tblCamDeGpo", posSel);
                            mensaje.General("PROCESO_EXITOSO", "", ""); 
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
            
    }
}

function btnCamTotGpo_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    var tblCamDeGpo;
    
    if (jsCamDeGpo.esInscripOfic)
        mensaje.CamDeGpo("INSCRIPCION_OFICIALIZADA");
    else {
        if ($("#cbxGrupo option:selected").val()==='')
                mensaje.Principal("SELEC","grupo");
        else {
            tblCamDeGpo = tabla.getTable("tblCamDeGpo",["idalu"]);
            if(tblCamDeGpo.length > 1) {
                var datos = {  modulo:"CaDeGp", metodo:"btCaToGp", califCicEscIn:jsCamDeGpo.califCicEscIn, tblCamDeGpo: tblCamDeGpo, 
                    cbxGrupo:$("#cbxGrupo option:selected").val(), tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, 
                    tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo };
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
                                initTablaTblCamDeGpo (result.tblCamDeGpo);
                                mensaje.General("GUARDADO_EXITOSO", "", ""); 
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
                                cerrarLoading();
                            break;
                        default:break;
                    }
                })
                .fail(function() {
                    mensaje.General("ERROR_AJAX", "", "");
                    cerrarLoading();
                });
            }else
                mensaje.Principal("SELEC","alumno");
        }
    }
}

function btnAnteriorGpo_CamDeGpo_Click()
{
    var tabla = new Tabla();
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    
    if ( posSelActual-1 >= 0 ){
        var datos = { 
            modulo:"CaDeGp", metodo:"btAnGp", hayCambios:jsCamDeGpo.hayCambios, tblPrincipal:tabla.getTable("tblPrincipal",["grado","grupo"]), 
            tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_cveplan:jsCamDeGpo.tblPrincipal_cveplan,
            tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo, 
            posSelActual:posSelActual, califCicEscIn:jsCamDeGpo.califCicEscIn, cicescini:jsCamDeGpo.tblPrincipal_cicescini, xprimeraves:jsCamDeGpo.xprimeraves
        };
        
        if (jsCamDeGpo.hayCambios){
            datos['tblCamDeGpo'] = tabla.getTable("tblCamDeGpo",["idalu","grupo","cvedefsuf","cvelengua","etnia"]);
            datos['tblCamDeGpo_cvedefsuf_oldValue'] = jsCamDeGpo.cvedefsuf_oldValue;
            datos['tblPrincipal_grado'] = jsCamDeGpo.tblPrincipal_grado;
            datos['tblPrincipal_grupo'] = jsCamDeGpo.tblPrincipal_grupo;
            datos['tblCamDeGpo_cvelengua_oldValue'] = jsCamDeGpo.cvelengua_oldValue;
            datos['tblCamDeGpo_etnia_oldValue'] = jsCamDeGpo.etnia_oldValue;
        }
        
        sigAntGrupo_CamDeGpo (datos);
    }
}

function btnSiguienteGpo_CamDeGpo_Click ()
{
    var tabla = new Tabla();
    
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    var numFilas = tabla.getNumRows('tblPrincipal');
    
    if ( posSelActual+1 <  numFilas){
        var datos = {
            modulo:"CaDeGp", metodo:"btSiGp", hayCambios:jsCamDeGpo.hayCambios, tblPrincipal:tabla.getTable("tblPrincipal",["grado","grupo"]), 
            tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_cveplan:jsCamDeGpo.tblPrincipal_cveplan, 
            tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo, posSelActual:posSelActual, 
            califCicEscIn:jsCamDeGpo.califCicEscIn, cicescini:jsCamDeGpo.tblPrincipal_cicescini, xprimeraves:jsCamDeGpo.xprimeraves
            
        };

        if (jsCamDeGpo.hayCambios){
            datos['tblCamDeGpo'] = tabla.getTable("tblCamDeGpo",["idalu","grupo","cvedefsuf","cvelengua","etnia"]);
            datos['tblCamDeGpo_cvedefsuf_oldValue'] = jsCamDeGpo.cvedefsuf_oldValue;
            datos['tblPrincipal_grado'] = jsCamDeGpo.tblPrincipal_grado;
            datos['tblPrincipal_grupo'] = jsCamDeGpo.tblPrincipal_grupo;
            datos['tblCamDeGpo_cvelengua_oldValue'] = jsCamDeGpo.cvelengua_oldValue;
            datos['tblCamDeGpo_etnia_oldValue'] = jsCamDeGpo.etnia_oldValue;
        }

        sigAntGrupo_CamDeGpo (datos);
    }
}

function sigAntGrupo_CamDeGpo (datos)
{
    var mensaje = new Mensajes ();
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
                    jsCamDeGpo.tblPrincipal_grado = result.tblPrincipal_grado;
                    jsCamDeGpo.tblPrincipal_grupo = result.tblPrincipal_grupo;
                    
                    jsCamDeGpo.cvedefsuf_oldValue="",
                    jsCamDeGpo.cvelengua_oldValue="",
                    jsCamDeGpo.etnia_oldValue="",
                    jsCamDeGpo.hayCambios = result.hayCambios;
                    jsCamDeGpo.ofs = result.ofs;
                    
                    /*jsCamDeGpo.isTodoCalOf = result.isTodoCalOf;*/
                    frmwCamDeGpo_InitData(result);
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

function btnAnteriorCiclo_CamDeGpo_Click ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();

    if (jsCamDeGpo.califCicEscIn >1991)
    {
        var datos = { 
            modulo:"CaDeGp", metodo:"btAnCi", hayCambios:jsCamDeGpo.hayCambios, califCicEscIn:jsCamDeGpo.califCicEscIn, 
            tblPrincipal_cveplan:jsCamDeGpo.tblPrincipal_cveplan, tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo
        };
        
        if (jsCamDeGpo.hayCambios){
            datos['tblCamDeGpo'] = tabla.getTable("tblCamDeGpo",["idalu","grupo","cvedefsuf","cvelengua","etnia"]);
            datos['tblCamDeGpo_cvedefsuf_oldValue'] = jsCamDeGpo.cvedefsuf_oldValue;
            datos['tblCamDeGpo_cvelengua_oldValue'] = jsCamDeGpo.cvelengua_oldValue;
            datos['tblCamDeGpo_etnia_oldValue'] = jsCamDeGpo.etnia_oldValue;
        }
        
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
                        jsCamDeGpo.califCicEscIn = result.califCicEscIn;
                        $('#lblCiclo').text(result.lblCiclo);
                        jsCamDeGpo.gposL = result.gposL;
                        jsCamDeGpo.cvedefsuf_oldValue="";
                        jsCamDeGpo.cvelengua_oldValue="";
                        jsCamDeGpo.etnia_oldValue="";
                        /*jsCamDeGpo.isTodoCalOf = true;*/
                        jsCamDeGpo.idalusDeof = result.idalusDeof;
                        
                        initTablaTblCamDeGpo (result.tblCamDeGpo);
                        
                        jsCamDeGpo.hayCambios = result.hayCambios;

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

function btnSiguienteCiclo_CamDeGpo_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    
    if (jsCamDeGpo.califCicEscIn < sisVars.cicescin)
    {
        var datos = { 
            modulo:"CaDeGp", metodo:"btSiCi", hayCambios:jsCamDeGpo.hayCambios, califCicEscIn:jsCamDeGpo.califCicEscIn, cicescin:sisVars.cicescin, 
            tblPrincipal_cveplan:jsCamDeGpo.tblPrincipal_cveplan, tblPrincipal_idcct:jsCamDeGpo.tblPrincipal_idcct, tblPrincipal_grado:jsCamDeGpo.tblPrincipal_grado, 
            tblPrincipal_grupo:jsCamDeGpo.tblPrincipal_grupo
        };
        
        if (jsCamDeGpo.hayCambios){
            datos['tblCamDeGpo'] = tabla.getTable("tblCamDeGpo",["idalu","grupo","cvedefsuf","cvelengua","etnia"]);
            datos['tblCamDeGpo_cvedefsuf_oldValue'] = jsCamDeGpo.cvedefsuf_oldValue;
            datos['tblCamDeGpo_cvelengua_oldValue'] = jsCamDeGpo.cvelengua_oldValue;
            datos['tblCamDeGpo_etnia_oldValue'] = jsCamDeGpo.etnia_oldValue;
        }
        
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
                        jsCamDeGpo.califCicEscIn = result.califCicEscIn;
                        $('#lblCiclo').text(result.lblCiclo);
                        jsCamDeGpo.gposL = result.gposL;
                        jsCamDeGpo.cvedefsuf_oldValue="";
                        jsCamDeGpo.cvelengua_oldValue="";
                        jsCamDeGpo.etnia_oldValue="";
                        /*jsCamDeGpo.isTodoCalOf = true;*/
                        jsCamDeGpo.idalusDeof = result.idalusDeof;
                        
                        initTablaTblCamDeGpo (result.tblCamDeGpo);
                        
                        jsCamDeGpo.hayCambios = result.hayCambios;

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

function btnTotAlum_Click ()
{
    var tabla = new Tabla();
    var numFilas, i=0;

    numFilas = tabla.getNumRows("tblCamDeGpo");
    
    for (var f=0; f<numFilas; f++)
    {
        if ($(".tblCamDeGpo_estatusgrado_f"+f).text()!=='BD' && $(".tblCamDeGpo_grupo_f"+f+" option:selected").text()!=='---')
            i = i +1;
    }
    $('#lblTotal').text('Total : '+i+' Alumnos');
}

function btnExportGrupoAExcel_ActionPerformed()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var ancho=450, alto=200, posicion_x, posicion_y; 
    var tblCamDeGpo, datos;
    
    posicion_x=(screen.width/2)-(ancho/2); 
    posicion_y=(screen.height/2)-(alto/2);
    
    mensaje.General("EXCEL_EN_CREACION");
    tblCamDeGpo = tabla.getTable("tblCamDeGpo",["idcct","idalu","curp","nom_tot","grado","grupo","apepat","apemat","nombre","cicescini","estatusalu","fecnac","sexo","rep","promedio","promediogral","estatusgrado","cveprograma","cvedefsuf","desdefsuf","probem"], null, "JSON");

    datos = {
        tblPrincipal_cct: jsCamDeGpo.tblPrincipal_cct, tblPrincipal_grado: jsCamDeGpo.tblPrincipal_grado, tblCamDeGpo:tblCamDeGpo
    };
    sessionStorage.datosCamDeGpo = JSON.stringify(datos);
    //inicioDeGradoGrupoAExcel ();
    //window.open("Calificaciones/GradoGrupoAExcel.jsp","","status=0, toolbar=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
    window.open("../html/Grupo/exportGrupoAExcel.html","","status=0, toolbar=0, location=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
}

function _btnImpRepHistorialAcademico_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var posSelActual = tabla.getSelectedIndexRow ('tblCamDeGpo');
    
    if ( posSelActual>=0){
        var tblCamDeGpo = tabla.getSelectedRow("tblCamDeGpo",["idalu","curp","nom_tot"], null, "JSON");
        mensaje.General("REPORTE_EN_CREACION","","","");
        window.open("Grupo/repHistorialAcademico.jsp?r=Grupo/historialAcademico&idalu="+tblCamDeGpo.idalu+"&curp="+tblCamDeGpo.curp
                        +"&nom_tot="+tblCamDeGpo.nom_tot+"&idcct="+jsCamDeGpo.tblPrincipal_idcct);
    }else
        mensaje.General("NO_SELEC"," alumno","ver su Historial Académico");
}

function btnConstanciaDeBaja_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    var filasSelec, i, alumnos="";    
    
    
    filasSelec = tabla.getSelectedRows("tblCamDeGpo",["idalu","estatusgrado","num_eval1","num_mat"]);
    if(!jsCamDeGpo.ofs.bimOf1 ) {
            mensaje.CamDeGpo("SIN_OFICIALIZAR");         
            return;
    }
    else if(filasSelec.length > 0) {      
        for (i=0; i<filasSelec.length; i++) {
            var datos_alu = filasSelec[i].split('~');
            if(datos_alu[1]!=="BD") {
                mensaje.CamDeGpo("ALU_INSCRITO",datos_alu[0]);
                return;
            } else if (jsCamDeGpo.idalusDeof.length > 0 && indexOf (jsCamDeGpo.idalusDeof, datos_alu[0]) !== -1) {
                mensaje.CamDeGpo("ALU_CALF_DESOF");            
                return;
            } else if(datos_alu[3]==='0' || datos_alu[2]!==datos_alu[3] ) {
                mensaje.CamDeGpo("SIN_CALIFICACIONES",datos_alu[0]);            
                return;
            }
            alumnos += datos_alu[0]+",";
        }             
    } else {
        mensaje.Principal("SELEC","alumno");  
        return;
    }
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    window.open("Grupo/reporteDeBaja.jsp?cicescini="+jsCamDeGpo.tblPrincipal_cicescini
            +"&cveplan="+jsCamDeGpo.tblPrincipal_cveplan+"&idcct="+jsCamDeGpo.tblPrincipal_idcct+"&grado="+jsCamDeGpo.tblPrincipal_grado
            +"&grupo="+jsCamDeGpo.tblPrincipal_grupo+"&idalus="+ alumnos);   
}

/*function btnReporteDeInscripcion_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    //var posSelActual = tabla.getSelectedIndexRow ('tblCamDeGpo');
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    window.open("Grupo/reporteDeInscripcion.jsp?cicescini="+jsCamDeGpo.tblPrincipal_cicescini
            +"&cveplan="+jsCamDeGpo.tblPrincipal_cveplan+"&idcct="+jsCamDeGpo.tblPrincipal_idcct+"&grado="+jsCamDeGpo.tblPrincipal_grado
            +"&grupo="+jsCamDeGpo.tblPrincipal_grupo);
    
}*/
                                                                                                                                                                                                        
function permisosGpo ()
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    if (sisVars){        
        for (var boton in sisVars.botonesDeGpo) {                           
            $('#ulbtnsCambiosIndividuales').append(sisVars.botonesDeGpo[boton]);
        }
    }
}                                                                                                                                                                                                        
                                                                                                                                                                                                        