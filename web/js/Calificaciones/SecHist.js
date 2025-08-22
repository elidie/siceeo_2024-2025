/* 
    Creado el : 16/02/2016, 08:48:31 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsSeHi;
function frmwSecHist_Show (tblPrincipal_cicescini, tblAlumCapCalif_idalu, tblAlumCapCalif_curp, tblAlumCapCalif_nom_tot, tblPrincipal_cveplan, tblAlumCapCalif_frma, alumSigAnt_Visible, executeFunction,caso)
{
    jsSeHi={
        executeFunction: executeFunction,
        caso: caso,
        tblAlumCapCalif_idalu: tblAlumCapCalif_idalu,
        tblAlumCapCalif_curp: tblAlumCapCalif_curp,
        tblAlumCapCalif_nom_tot: tblAlumCapCalif_nom_tot,
        tblAlumCapCalif_frma: tblAlumCapCalif_frma,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_cicescini: tblPrincipal_cicescini,        
        
        tblCalif1ro_promedio_oldValue: new Array(),
        tblCalif2do_promedio_oldValue: new Array(),
        tblCalif3ro_promedio_oldValue: new Array(),
        
        cbxTExm1ro_Column1:null,
        cbxTExm2do_Column1:null,
        cbxTExm3ro_Column1:null,
        cbxTExm1ro_Column3:null,
        cbxTExm2do_Column3:null,
        cbxTExm3ro_Column3:null,
        
        cbxTExm1ro_idccts:null,
        cbxTExm2do_idccts:null,
        cbxTExm3ro_idccts:null,
        
        tblCalif1ro_matrepact:null,
        tblCalif2do_matrepact:null,
        tblCalif3ro_matrepact:null,
        
        msgExm1ro: null,
        msgExm2do: null,
        msgExm3ro: null,
        
        tblExmExt1ro_OldValues: null,
        tblExmExt2do_OldValues: null,
        tblExmExt3ro_OldValues: null,
        
        cbxTExm_mesesExmExt: null,
        cbxTExm_meses: null,
        
        tblCalif1ro_editable: "",
        tblCalif2do_editable: "",
        tblCalif3ro_editable: "",
                
        alumSigAnt_Visible: alumSigAnt_Visible
    };
    
    if (jsSeHi.caso === "Exalumnos")
        object_setVisible (false,'frmwExalumnos');
    else
        object_setVisible (false,'frmwCalifSecXBim');

    $("#frmwSecHist").css("display", "block");                                 // Mostramos el formulario correspondiente

    frmwSecHist_Create();
    frmwSecHist_FormActivate ();
}

function frmwSecHist_Close()
{
    if (jsSeHi.executeFunction!==null && typeof jsSeHi.executeFunction !== "undefined" && typeof jsSeHi.executeFunction.onClose !== "undefined")
        jsSeHi.executeFunction.onClose();
    
    if($("#frmfSecHist").length)
        $("#frmfSecHist").remove();
    
    object_setVisible (false,'frmwSecHist');
    
    if (jsSeHi.executeFunction!==null && typeof jsSeHi.executeFunction !== "undefined" && typeof jsSeHi.executeFunction.nameParentForm !== "undefined"){
        if (jsSeHi.executeFunction.nameParentForm === "Exalumnos")
            object_setVisible (true,'frmwExalumnos');
    }else
        object_setVisible (true,'frmwCalifSecXBim');
    
    $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
    $( "#tblMatCalifXBim_txt_f0_c1" ).select();
    
    jsSeHi=null;
}

function frmwSecHist_Create() 
{
    if($('#frmfSecHist').length)                                                // Verificando si la tabla Existe
        $('#frmfSecHist').remove();
    $('#frmwSecHist').append('<fieldset id="frmfSecHist"><legend>Exámenes extraordinarios</legend><div id="btnRegresar_SH" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfSecHist").append('<div id="pnlSecHist"></div>');
//.........................................................................................................................................................................................................................................................................
        $("#pnlSecHist").append('<div id="pnlInfoDataYButtons"></div>');
            $("#pnlInfoDataYButtons").append('<div id="pnlIdaluCurpNom" class="alinearHoriz">'
                                                +'<label id="lblTAlumCapCalif_idalu"></label>'
                                                +'<label id="lblTAlumCapCalif_curp"></label>'
                                                +'<label id="lblTAlumCapCalif_nomTot"></label>'
                                            +'</div>');
            $("#pnlInfoDataYButtons").append('<div class="alinearHoriz">'
                                                + '<ul id="ubtnAntSigAlum" class="buttonBar"> '
                                                    + '<li><a href="#" id="btnAnteriorAlum" tabindex="201" title="Alumno anterior"><label class="iconBtnAlumAnt iconBtnRedondo icon-arrow-left4"></label>Alum. anterior</a></li>'
                                                    + '<li><a href="#" id="btnSiguienteAlum" tabindex="202" title="Siguiente alumno">Siguiente alum.<label class="iconBtnAlumSig iconBtnRedondo icon-arrow-right4"></label></a></li>'
                                                +'</ul> '
                                            +'</div>');
        $("#pnlSecHist").append('<div id="pnlExamExtraord"></div>');
            $("#pnlExamExtraord").append('<div id="pnlExamExtraord1ro" class="alinearHoriz"></div>');
                $("#pnlExamExtraord1ro").append('<div id="pnlInfoCalif1ro"></div>');
                    $("#pnlInfoCalif1ro").append('<div>'
                                                    +'<label class="colorDeResalte">Primer Grado, Grupo:</label><label id="lblTCalif1ro_grupo" class="colorDeResalte"></label>'
                                                    +'<label id="lblTCalif1ro_cicescini"></label>'
                                                +'</div>');
                    $("#pnlInfoCalif1ro").append('<div>'
                                                    +'<label id="lblTCalif1ro_deleg"></label>'
                                                    +'<label id="lblTCalif1ro_cct"></label>'
                                                +'</div>');
                    $("#pnlInfoCalif1ro").append('<div>'
                                                    +'<label id="lblTCalif1ro_alumextrj"></label>'
                                                    +'<label id="lblTCalif1ro_idcct"></label>'
                                                    +'<label id="lblTCalif1ro_promgdo"></label>'
                                                +'</div>');
                $('#pnlExamExtraord1ro').append('<div id="pnlCalifsFinales1ro" class="panel"><label class="tituloPanel">Calificaciones finales</label></div>');
                    $("#pnlCalifsFinales1ro").append('<div id="pnlTablaCalif1ro">  <div id="scrlCalif1ro" class="scrollTable"></div>  </div>');    
                    $("#pnlCalifsFinales1ro").append('<div id="pnlBotones1roGuardaYCalif">'
                                                    + '<label class="colorDeResalte">Debe en 1ro: </label><label id="lblTCalif1ro_matrepact"></label>'
                                                    + '<ul id="ubtnGuardaYCalif1ro" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnGdaCalif1ro" class="tam1Button" tabindex="201" title="Guardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar</a></li>'
                                                        + '<li><a href="#" id="btnCalifDBim1ro" tabindex="202" title="Calificación obtenida de bimestres">Calif. obtenida de bimestres</a></li>'
                                                        + '<li><a href="#" id="btnOfExmExt1ro" tabindex="203" title="Oficialización de examenes extraordinarios" class="icon-sello">Oficializar Exm.Ext. 1ro</a></li>'
                                                    + '</ul> '
                                                +'</div>');
                $('#pnlExamExtraord1ro').append('<div id="pnlExamenes1ro" class="panel"><label class="tituloPanel">Exámenes por materia</label></div>');
                    $("#pnlExamenes1ro").append('<div>'                                                    
                                                    +'<label id="lblTCalif1ro_desmat" class="colorDeResalte"></label>'
                                                +'</div>');
                    $("#pnlExamenes1ro").append('<div id="pnlTablaExmExt1ro">  <div id="scrlExmExt1ro" class="scrollTable"></div>  </div>');
                    $("#pnlExamenes1ro").append('<div id="pnlBotonGuardaExm1ro">'
                                                    + '<ul id="ubtnGuardaOport1ro" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnInserFila1ro"  class="tam2Button" tabindex="202" title="Inserta un renglón en blanco al final de la lista para que ingrese sus datos de examen."><label class="iconBtnMas iconBtnRedondo  middleHoriz icon-plus"></label>Insertar renglón</a></li>'
                                                        + '<li><a href="#" id="btnQuitarFila1ro"  class="tam2Button" tabindex="202" title="Elimina el renglón seleccionado en la lista."><label class="iconBtnMenos iconBtnRedondo  middleHoriz icon-minus"></label>Quitar renglón</a></li>'
                                                      //+ '<li><a href="#" id="btnGuardaOpotunidad1ro"  class="tam1Button" tabindex="202" title="Guardar"><label class="iconBtnGuardar  middleHoriz icon-disquete"></label>Guardar</a></li>'
                                                        + '<li><a href="#" id="btnGuardaOpotunidad1ro"  class="tam1Button" tabindex="202" title="Guardar"><label class="iconBtnGuardar middleHoriz icon-disquete"></label>Guardar</a></li>'
                                                    +'</ul> '
                                                +'</div>');
//.........................................................................................................................................................................................................................................................................
            $("#pnlExamExtraord").append('<div id="pnlExamExtraord2do" class="alinearHoriz"></div>');
                $("#pnlExamExtraord2do").append('<div id="pnlInfoCalif2do"></div>');
                    $("#pnlInfoCalif2do").append('<div>'
                                                    +'<label class="colorDeResalte">Segundo Grado, Grupo:</label><label id="lblTCalif2do_grupo" class="colorDeResalte"></label>'
                                                    +'<label id="lblTCalif2do_cicescini"></label>'
                                                +'</div>');
                    $("#pnlInfoCalif2do").append('<div>'
                                                    +'<label id="lblTCalif2do_deleg"></label>'
                                                    +'<label id="lblTCalif2do_cct"></label>'
                                                +'</div>');
                    $("#pnlInfoCalif2do").append('<div>'
                                                    +'<label id="lblTCalif2do_alumextrj"></label>'
                                                    +'<label id="lblTCalif2do_idcct"></label>'
                                                    +'<label id="lblTCalif2do_promgdo"></label>'
                                                +'</div>');
                $('#pnlExamExtraord2do').append('<div id="pnlCalifsFinales2do" class="panel"><label class="tituloPanel">Calificaciones finales</label></div>');
                    $("#pnlCalifsFinales2do").append('<div id="pnlTablaCalif2do">  <div id="scrlCalif2do" class="scrollTable"></div>  </div>');    
                    $("#pnlCalifsFinales2do").append('<div id="pnlBotones2doGuardaYCalif">'
                                                    +'<div><label class="colorDeResalte">Debe en 2do: </label><label id="lblTCalif2do_matrepact"></label></div>'
                                                    +'<div><label class="colorDeResalte">Debe en grado anterior: </label><label id="lblTCalif2do_matrepant"></label></div>'
                                                    + '<ul id="ubtnGuardaYCalif2do" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnGdaCalif2do"  class="tam1Button" tabindex="201" title="Guardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar</a></li>'
                                                        + '<li><a href="#" id="btnCalifDBim2do" tabindex="202" title="Calificación obtenida de bimestres">Calif. obtenida de bimestres</a></li>'
                                                        + '<li><a href="#" id="btnOfExmExt2do" tabindex="203" title="Oficialización de examenes extraordinarios" class="icon-sello">Oficializar Exm.Ext. 2do</a></li>'
                                                    +'</ul> '
                                                +'</div>');
                $('#pnlExamExtraord2do').append('<div id="pnlExamenes2do" class="panel"><label class="tituloPanel">Exámenes por materia</label></div>');
                    $("#pnlExamenes2do").append('<div>'
                                                    +'<label id="lblTCalif2do_desmat" class="colorDeResalte"></label>'
                                                +'</div>');
                    $("#pnlExamenes2do").append('<div id="pnlTablaExmExt2do">  <div id="scrlExmExt2do" class="scrollTable"></div>  </div>');
                    $("#pnlExamenes2do").append('<div id="pnlBotonGuardaExm2do">'
                                                    + '<ul id="ubtnGuardaOport2do" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnInserFila2do"  class="tam2Button" tabindex="202" title="Inserta un renglón en blanco al final de la lista para que ingrese sus datos  de examen."><label class="iconBtnMas iconBtnRedondo  middleHoriz icon-plus"></label>Insertar renglón</a></li>'
                                                        + '<li><a href="#" id="btnQuitarFila2do"  class="tam2Button" tabindex="202" title="Elimina el renglón seleccionado en la lista."><label class="iconBtnMenos iconBtnRedondo  middleHoriz icon-minus"></label>Quitar renglón</a></li>'
                                                        + '<li><a href="#" id="btnGuardaOpotunidad2do"  class="tam1Button" tabindex="202" title="Guardar"><label class="iconBtnGuardar middleHoriz icon-disquete"></label>Guardar</a></li>'
                                                    +'</ul> '
                                                +'</div>');
//.........................................................................................................................................................................................................................................................................
            $("#pnlExamExtraord").append('<div id="pnlExamExtraord3ro" class="alinearHoriz"></div>');
                $("#pnlExamExtraord3ro").append('<div id="pnlInfoCalif3ro"></div>');
                    $("#pnlInfoCalif3ro").append('<div>'
                                                    +'<label class="colorDeResalte">Tercer Grado, Grupo:</label><label id="lblTCalif3ro_grupo" class="colorDeResalte"></label>'
                                                    +'<label id="lblTCalif3ro_cicescini"></label>'
                                                +'</div>');
                    $("#pnlInfoCalif3ro").append('<div>'
                                                    +'<label id="lblTCalif3ro_deleg"></label>'
                                                    +'<label id="lblTCalif3ro_cct"></label>'
                                                    +'<label id="lblTCalif3ro_alumextrj"></label>'
                                                +'</div>');
                    $("#pnlInfoCalif3ro").append('<div>'
                                                    +'<label>Promedio General: </label><label id="lblTCalif3ro_promediogral"></label>'
                                                    +'<label id="lblTCalif3ro_idcct"></label>'
                                                    +'<label id="lblTCalif3ro_promgdo"></label>'
                                                +'</div>');
                $('#pnlExamExtraord3ro').append('<div id="pnlCalifsFinales3ro" class="panel"><label class="tituloPanel">Calificaciones finales</label></div>');
                    $("#pnlCalifsFinales3ro").append('<div id="pnlTablaCalif3ro">  <div id="scrlCalif3ro" class="scrollTable"></div>  </div>');    
                    $("#pnlCalifsFinales3ro").append('<div id="pnlBotones3roGuardaYCalif">'
                                                    +'<div class="alinearHoriz">'
                                                        +'<div><label class="colorDeResalte">Debe en 3ro: </label><label id="lblTCalif3ro_matrepact"></label></div>'
                                                        +'<div><label class="colorDeResalte">Debe en grados anteriores: </label><label id="lblTCalif3ro_matrepant"></label></div>'
                                                    +'</div>'
                                                    + '<ul class="buttonBar alinearHoriz"> '
                                                        + '<li><a href="#" id="btnActualizaReprobadas"  tabindex="201" title="Actualizar"><label class="iconBtnSincronizar iconBtnRedondo  middleHoriz icon-plus"></label>Actualizar</a></li>'
                                                    +'</ul> '
                                                    + '<ul id="ubtnGuardaYCalif3ro" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnGdaCalif3ro"  class="tam1Button" tabindex="201" title="Guardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar</a></li>'
                                                        + '<li><a href="#" id="btnCalifDBim3ro" tabindex="202" title="Calificación obtenida de bimestres">Calif. obtenida de bimestres</a></li>'
                                                        + '<li><a href="#" id="btnOfExmExt3ro" tabindex="203" title="Oficialización de examenes extraordinarios" class="icon-sello">Oficializar Exm.Ext. 3ro</a></li>'
                                                    +'</ul> '
                                                +'</div>');
                $('#pnlExamExtraord3ro').append('<div id="pnlExamenes3ro" class="panel"><label class="tituloPanel">Exámenes por materia</label></div>');
                    $("#pnlExamenes3ro").append('<div>'
                                                    +'<label id="lblTCalif3ro_desmat" class="colorDeResalte"></label>'
                                                +'</div>');
                    $("#pnlExamenes3ro").append('<div id="pnlTablaExmExt3ro">  <div id="scrlExmExt3ro" class="scrollTable"></div>  </div>');
                    $("#pnlExamenes3ro").append('<div id="pnlBotonGuardaExm3ro">'
                                                    + '<ul id="ubtnGuardaOport3ro" class="buttonBar"> '
                                                        + '<li><a href="#" id="btnInserFila3ro"  class="tam2Button" tabindex="202" title="Inserta un renglón en blanco al final de la lista para que ingrese sus datos de examen."><label class="iconBtnMas iconBtnRedondo  middleHoriz icon-plus"></label>Insertar renglón</a></li>'
                                                        + '<li><a href="#" id="btnQuitarFila3ro"  class="tam2Button" tabindex="202" title="Elimina el renglón seleccionado en la lista."><label class="iconBtnMenos iconBtnRedondo  middleHoriz icon-minus"></label>Quitar renglón</a></li>'
                                                        + '<li><a href="#" id="btnGuardaOpotunidad3ro"  class="tam1Button" tabindex="202" title="Guardar"><label class="iconBtnGuardar middleHoriz icon-disquete"></label>Guardar</a></li>'
                                                    +'</ul> '
                                                +'</div>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    $("#btnRegresar_SH").on("click",function(){ frmwSecHist_Close(); return false; });
    $("#btnAnteriorAlum").on("click",function(){ btnAnteriorAlumSH_Click(); return false; });
    $("#btnSiguienteAlum").on("click",function(){ btnSiguienteAlumSH_Click(); return false; });
    
    $("#btnGdaCalif1ro").on("click",function(){ btnGdaCalif1ro_Click(); return false; });
    $("#btnCalifDBim1ro").on("click",function(){ btnCalifDBim1ro_Click(); return false; });
    $("#btnInserFila1ro").on("click",function(){ btnInserFila1ro_Click(); return false; });
    $("#btnQuitarFila1ro").on("click",function(){ btnQuitarFila1ro_Click(); return false; });
    $("#btnGuardaOpotunidad1ro").on("click",function(){ btnGuardaExamExt1ro_Click(); return false; });
    $("#btnOfExmExt1ro").on("click",function(){ mwfExmExtraordinarios_Show(1); return false; });    
    
    $("#btnGdaCalif2do").on("click",function(){ btnGdaCalif2do_Click(); return false; });
    $("#btnCalifDBim2do").on("click",function(){ btnCalifDBim2do_Click(); return false; });
    $("#btnInserFila2do").on("click",function(){ btnInserFila2do_Click(); return false; });
    $("#btnQuitarFila2do").on("click",function(){ btnQuitarFila2do_Click(); return false; });
    $("#btnGuardaOpotunidad2do").on("click",function(){ btnGuardaExamExt2do_Click(); return false; });
    $("#btnOfExmExt2do").on("click",function(){ mwfExmExtraordinarios_Show(2); return false; });    
    
    $("#btnActualizaReprobadas").on("click",function(){ btnActualizaReprobadas_Click(); return false; });
    $("#btnGdaCalif3ro").on("click",function(){ btnGdaCalif3ro_Click(); return false; });
    $("#btnCalifDBim3ro").on("click",function(){ btnCalifDBim3ro_Click(); return false; });
    $("#btnInserFila3ro").on("click",function(){ btnInserFila3ro_Click(); return false; });
    $("#btnQuitarFila3ro").on("click",function(){ btnQuitarFila3ro_Click(); return false; });
    $("#btnGuardaOpotunidad3ro").on("click",function(){ btnGuardaExamExt3ro_Click(); return false; });
    $("#btnOfExmExt3ro").on("click",function(){ mwfExmExtraordinarios_Show(3); return false; });    
}

function frmwSecHist_FormActivate() 
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    var datos = { 
        modulo:"SeHi", metodo:"foAc", tblAlumCapCalif_idalu:jsSeHi.tblAlumCapCalif_idalu, tblAlumCapCalif_frma:jsSeHi.tblAlumCapCalif_frma, 
        usuarioNo22:sisVars.usuarioNo22, txtUsuario:sisVars.usuario, cicescini: jsSeHi.tblPrincipal_cicescini
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
                    $("#lblTAlumCapCalif_idalu").text(jsSeHi.tblAlumCapCalif_idalu);
                    $("#lblTAlumCapCalif_curp").text(jsSeHi.tblAlumCapCalif_curp);
                    $("#lblTAlumCapCalif_nomTot").text(jsSeHi.tblAlumCapCalif_nom_tot);
                    if(result.pnlExamExtraord1ro_Visible===false)
                        $("#pnlExamExtraord1ro").css("display","none");
                    if(result.pnlExamExtraord2do_Visible===false)
                        $("#pnlExamExtraord2do").css("display","none");
                    if(result.pnlExamExtraord3ro_Visible===false)
                        $("#pnlExamExtraord3ro").css("display","none");
                    /* object_setVisible(result.pnlExamExtraord1ro_Visible,"pnlExamExtraord1ro");
                    object_setVisible(result.pnlExamExtraord2do_Visible,"pnlExamExtraord2do");
                    object_setVisible(result.pnlExamExtraord3ro_Visible,"pnlExamExtraord3ro"); */
                    
                    boton_setVisible(jsSeHi.alumSigAnt_Visible,"btnAnteriorAlum");
                    boton_setVisible(jsSeHi.alumSigAnt_Visible,"btnSiguienteAlum");
                    
                    jsSeHi.cbxTExm_mesesExmExt = result.mesesExmExt;
                    jsSeHi.cbxTExm_meses = result.meses;                    
                    
                    
                    if (result.tblCalif1ro.length > 0){
                        $("#lblTCalif1ro_grupo").text(result.tblCalif1ro[0].grupo);
                        $("#lblTCalif1ro_cicescini").text(result.tblCalif1ro[0].cicescini);
                        $("#lblTCalif1ro_deleg").text(result.tblCalif1ro[0].deleg);
                        $("#lblTCalif1ro_cct").text(result.tblCalif1ro[0].cct);
                        $("#lblTCalif1ro_alumextrj").text(result.tblCalif1ro[0].almextrj);
                        $("#lblTCalif1ro_idcct").text(result.tblCalif1ro[0].idcct);
                        $("#lblTCalif1ro_promgdo").text(result.tblCalif1ro[0].promgdo);
                        
                        $("#lblTCalif1ro_matrepact").text(result.tblCalif1ro[0].matrepact);
                        jsSeHi.tblCalif1ro_matrepact = result.tblCalif1ro[0].matrepact;
                        jsSeHi.cbxTExm1ro_Column1=result.cbxTExm1ro_Column1;
                        jsSeHi.cbxTExm1ro_Column3=result.cbxTExm1ro_Column3;
                        jsSeHi.msgExm1ro= result.msgExm1ro;
                        jsSeHi.cbxTExm1ro_idccts=result.cbxTExm1ro_idccts;
                        jsSeHi.tblCalif1ro_editable = result.tblCalif1ro_editable;
                        //object_setVisible(result.pnlExamenes1ro_visible,"pnlExamenes1ro");                                                
                        initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue, 1, result.tblCalif1ro_editable, result.msgExm1ro);
                        initTablaExmExt ("tblExmExt1ro", result.tblExmExt1ro, result.cbxTExm1ro_Column1, result.cbxTExm1ro_Column3, result.cbxTExm1ro_idccts, result.desmat1ro, result.msgExm1ro);
                    }

                    if (result.tblCalif2do.length > 0){
                        $("#lblTCalif2do_grupo").text(result.tblCalif2do[0].grupo);
                        $("#lblTCalif2do_cicescini").text(result.tblCalif2do[0].cicescini);
                        $("#lblTCalif2do_deleg").text(result.tblCalif2do[0].deleg);
                        $("#lblTCalif2do_cct").text(result.tblCalif2do[0].cct);
                        $("#lblTCalif2do_alumextrj").text(result.tblCalif2do[0].almextrj);
                        $("#lblTCalif2do_idcct").text(result.tblCalif2do[0].idcct);
                        $("#lblTCalif2do_promgdo").text(result.tblCalif2do[0].promgdo);
                        
                        $("#lblTCalif2do_matrepact").text(result.tblCalif2do[0].matrepact);
                        $("#lblTCalif2do_matrepant").text(result.tblCalif2do[0].matrepant);
                        
                        jsSeHi.cbxTExm2do_Column1=result.cbxTExm2do_Column1;
                        jsSeHi.cbxTExm2do_Column3=result.cbxTExm2do_Column3;
                        jsSeHi.msgExm2do= result.msgExm3ro;
                        jsSeHi.cbxTExm2do_idccts=result.cbxTExm2do_idccts;                        
                        jsSeHi.tblCalif2do_matrepact = result.tblCalif2do[0].matrepact;
                        jsSeHi.tblCalif2do_editable = result.tblCalif2do_editable;
                        initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, result.tblCalif2do_editable,result.msgExm2do);
                        initTablaExmExt ("tblExmExt2do", result.tblExmExt2do, result.cbxTExm2do_Column1, result.cbxTExm2do_Column3, result.cbxTExm2do_idccts, result.desmat2do, result.msgExm2do);
                    }
                
                    if (result.tblCalif3ro.length > 0){
                        $("#lblTCalif3ro_grupo").text(result.tblCalif3ro[0].grupo);
                        $("#lblTCalif3ro_cicescini").text(result.tblCalif3ro[0].cicescini);
                        $("#lblTCalif3ro_deleg").text(result.tblCalif3ro[0].deleg);
                        $("#lblTCalif3ro_cct").text(result.tblCalif3ro[0].cct);
                        $("#lblTCalif3ro_alumextrj").text(result.tblCalif3ro[0].almextrj);
                        $("#lblTCalif3ro_promediogral").text(result.tblCalif3ro[0].promediogral);
                        $("#lblTCalif3ro_idcct").text(result.tblCalif3ro[0].idcct);
                        $("#lblTCalif3ro_promgdo").text(result.tblCalif3ro[0].promgdo);
                        
                        $("#lblTCalif3ro_matrepact").text(result.tblCalif3ro[0].matrepact);
                        $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro[0].matrepant);
                        
                        jsSeHi.cbxTExm3ro_Column1=result.cbxTExm3ro_Column1;
                        jsSeHi.cbxTExm3ro_Column3=result.cbxTExm3ro_Column3;
                        jsSeHi.msgExm2do= result.msgExm2do;
                        jsSeHi.cbxTExm3ro_idccts=result.cbxTExm3ro_idccts;                        
                        jsSeHi.tblCalif3ro_matrepact = result.tblCalif3ro[0].matrepact;
                        jsSeHi.tblCalif3ro_editable = result.tblCalif3ro_editable;
                        initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, result.tblCalif3ro_editable, result.msgExm3ro);
                        initTablaExmExt ("tblExmExt3ro", result.tblExmExt3ro, result.cbxTExm3ro_Column1, result.cbxTExm3ro_Column3, result.cbxTExm3ro_idccts, result.desmat3ro, result.msgExm3ro);
                    }                                                                                                                                                                                 
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
function initTablaCalif (nombreTablaCalif, tblCalif, nombreTablaExmExt, jsSeHi_TblCalif_promedio_oldValue, grado, tblCalif_edit, msgExm)
{
    var tabla = new Tabla();
    var posSel;
    
    jsSeHi_TblCalif_promedio_oldValue.length = 0;
    
    if (typeof(tblCalif) !== "undefined") {
        if (tblCalif!==null)
            for (var i=0; i<tblCalif.length; i++) 
                jsSeHi_TblCalif_promedio_oldValue[i]=tblCalif[i].promedio;
        posSel = tabla.getSelectedIndexRow(nombreTablaCalif);                                                                                          //, ["", "textbox","textbox"]  se le quito la forma editable de columna de calificaciones    
        tabla.create(nombreTablaCalif.replace("tbl","scrl"),nombreTablaCalif,tblCalif, ["Materia","Prom","Calif. Ant."], ["desmat","promedio","califant"], ["",tblCalif_edit,tblCalif_edit], ["","center","center"], true, null, function(f){
                tblCalif_ChangeSelectedItem (nombreTablaCalif,nombreTablaExmExt,grado,msgExm, f);
        }, function(f,c){
            $("#"+nombreTablaCalif+"_txt_f"+f+"_c1").on("change",function () { tblMatCalif_ApplyCellAttribute (nombreTablaCalif); });
        });
        tabla.setSelectedRow(nombreTablaCalif,(posSel===null || posSel===-1)?0:posSel);
        tblMatCalif_ApplyCellAttribute (nombreTablaCalif);
        
        if (nombreTablaCalif==="tblCalif3ro" && tblCalif.length>0)
            $("#lblTCalif3ro_promediogral").text(tblCalif[0].promediogral);
    }
}
function initTablaExmExt (nombreTablaExmExt, tblExmExt, cbxTExm_Column1, cbxTExm_Column3, cbxTExm_idccts, desmat, msgExm)
{    
    
    var tabla = new Tabla ();
    if(typeof(tblExmExt) !== "undefined" && tblExmExt!==null) {
        
        if($("#"+nombreTablaExmExt).length>0)
            $("#"+nombreTablaExmExt).remove();
        
        
        if(typeof(msgExm) !== "undefined" && msgExm!==null && msgExm.length>0){
            $("#lbl"+nombreTablaExmExt.replace("tbl","")).remove();
            $("#"+nombreTablaExmExt.replace("tbl","scrl")).append("<label id='lbl"+nombreTablaExmExt.replace("tbl","")+"'>"+msgExm+"</label>");
        }
        else {
            if($("#lbl"+nombreTablaExmExt.replace("tbl","")).length>0)
                $("#lbl"+nombreTablaExmExt.replace("tbl","")).remove();
            
            //******** Como es probable que las tablas de exámenes extraordinarios cambien, les asignamos un id a cada renglón y las respaldamos **********\\            
            //-------- Les asignamos un id --------
            for (var i=0; i<tblExmExt.length; i++)
                tblExmExt[i].idarray = i;
            //-------- Las respaldamos -----------
            if (nombreTablaExmExt === "tblExmExt1ro") {        
                jsSeHi.tblExmExt1ro_OldValues=tblExmExt;                
                $("#lblTCalif1ro_desmat").text(desmat);
            }
            else if (nombreTablaExmExt === "tblExmExt2do") {
                jsSeHi.tblExmExt2do_OldValues=tblExmExt;        
                $("#lblTCalif2do_desmat").text(desmat);
            }
            else if (nombreTablaExmExt === "tblExmExt3ro"){
                jsSeHi.tblExmExt3ro_OldValues=tblExmExt;        
                $("#lblTCalif3ro_desmat").text(desmat);
            }
            /*********** Creamos la tabla ***********/            
            tabla.create(nombreTablaExmExt.replace("tbl","scrl"),nombreTablaExmExt,tblExmExt, ["dia","mes","año","cct","prom."], ["dia","mes","anio","cct","promedio"], ["textbox","combobox","textbox","combobox", "textbox"], null, true, null, null, null);
            /*********** Insertamos el catálogo para los combos mes ***********/
            tabla.setDataToComboboxCol (nombreTablaExmExt, 1, null, cbxTExm_Column1);  //Comentado 06-03-2025
            /*********** Insertamos el catálogo para los combos cct ***********/
            tabla.setDataToComboboxCol (nombreTablaExmExt, 3, cbxTExm_Column3, cbxTExm_idccts); //Comentado 06-03-2025
            //A cada materia le seleccionamos su dato de mes y cct en su combo correspondiente
            for (var i=0; i<tblExmExt.length; i++){              
                $("#pnlSecHist #"+nombreTablaExmExt+"_cbx_f"+i+"_c1").val(tblExmExt[i].mes);
                $("#pnlSecHist #"+nombreTablaExmExt+"_cbx_f"+i+"_c3").val(tblExmExt[i].idcct_apl);
            }
        }
    }    
}
function tblCalif_ChangeSelectedItem (nombreTablaCalif, nombreTablaExmExt, grado, msg, index)
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var Column1 = jsSeHi.cbxTExm1ro_Column1, Column3 = jsSeHi.cbxTExm1ro_Column3, idccts = jsSeHi.cbxTExm1ro_idccts;
    
    //tabla.setSelectedRow (nombreTabla, index);        
/*    if(msgExm.length===0) {*/
        var filaConDatos = tabla.getRow (nombreTablaCalif, index, ['cvetipmat','cvemat','desmat'], null, "JSON");
        //------------------ Establecemos los datos a enviar -------------------    
        var datos = {
            modulo:"SeHi", metodo:"tbCa_ChSeIt", tblAlumCapCalif_idalu:jsSeHi.tblAlumCapCalif_idalu, grado: grado, cvetipmat:filaConDatos.cvetipmat, 
            cvemat:filaConDatos.cvemat, msgExmExt: msg
        };
        //------------------------- Hacemos la llamada -------------------------    
        cargarLoading();
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:false                                                                                                                                        //Sí es forsoso que no sea asíncrono, para que espere a que se carguen todas las materias
        })
        .done(function(result){
            switch(result.returnCase){
                case 1:                    
                        if(jsSeHi.cbxTExm1ro_Column1==="null" || jsSeHi.cbxTExm1ro_Column3 === null){                        
                            Column1 = jsSeHi.cbxTExm2do_Column1;
                            Column3 = jsSeHi.cbxTExm2do_Column3;
                            idccts = jsSeHi.cbxTExm2do_idccts;
                        }
                        
                        msg = (grado===1 ? jsSeHi.msgExm1ro : (grado===2 ? jsSeHi.msgExm2do : (grado===3 ? jsSeHi.msgExm3ro : "") ) );
                        
                        initTablaExmExt (nombreTablaExmExt, result.tblExmExt, Column1, Column3, idccts, filaConDatos.desmat, msg);
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
    /*}*/
}

function btnAnteriorAlumSH_Click()
{
    var tabla = new Tabla ();
    var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapCalif');
    if (posSelActual > 0){
        //-------------------- Seleccionamos la siguiente fila de tblAlumCapCalif y obtenemos los datos correspondientes
        tblAlumCapCalif_ChangeSelectedItem (posSelActual-1, function (){
            tabla.setSelectedRow ('tblMatCalifXBim', 0);
            $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
            $( "#tblMatCalifXBim_txt_f0_c1" ).select();
            jsCaSeXBi.tblMatCalifXBim_calif1_OldValue = tabla.getTable ('tblMatCalifXBim', ["calif1"]);
            jsCaSeXBi.tblMatCalifXBim_calif2_OldValue = tabla.getTable ('tblMatCalifXBim', [2]);                //Forzosamente necesitamos el que está visible al usuario
            jsCaSeXBi.hayCambiosXBim = false;

            var tblAlumCapCalif = tabla.getSelectedRow("tblAlumCapCalif", ["idalu","curp","nom_tot"],null,"JSON");
            jsSeHi.tblAlumCapCalif_idalu = tblAlumCapCalif.idalu;
            jsSeHi.tblAlumCapCalif_curp = tblAlumCapCalif.curp;
            jsSeHi.tblAlumCapCalif_nom_tot = tblAlumCapCalif.nom_tot;
            frmwSecHist_FormActivate ();
        });
    }
}
function btnSiguienteAlumSH_Click()
{
    var tabla = new Tabla ();
    var numFilas = tabla.getNumRows ('tblAlumCapCalif');
    var posSelActual = tabla.getSelectedIndexRow ('tblAlumCapCalif');
    
    if (posSelActual < (numFilas-1)){
        //-------------------- Seleccionamos la siguiente fila de tblAlumCapCalif y obtenemos los datos correspondientes
        tblAlumCapCalif_ChangeSelectedItem (posSelActual+1, function (){
            tabla.setSelectedRow ('tblMatCalifXBim', 0);
            $( "#tblMatCalifXBim_txt_f0_c1" ).focus();
            $( "#tblMatCalifXBim_txt_f0_c1" ).select();
            jsCaSeXBi.tblMatCalifXBim_calif1_OldValue = tabla.getTable ('tblMatCalifXBim', ["calif1"]);
            jsCaSeXBi.tblMatCalifXBim_calif2_OldValue = tabla.getTable ('tblMatCalifXBim', [2]);                //Forzosamente necesitamos el que está visible al usuario
            jsCaSeXBi.hayCambiosXBim = false;

            var tblAlumCapCalif = tabla.getSelectedRow("tblAlumCapCalif", ["idalu","curp","nom_tot"],null,"JSON");
            jsSeHi.tblAlumCapCalif_idalu = tblAlumCapCalif.idalu;
            jsSeHi.tblAlumCapCalif_curp = tblAlumCapCalif.curp;
            jsSeHi.tblAlumCapCalif_nom_tot = tblAlumCapCalif.nom_tot;
            frmwSecHist_FormActivate ();
        });
    }
}

function btnGdaCalif1ro_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    var tablaCalif1ro=tabla.getRow("tblCalif1ro",0,["idalu","cicescini","almextrj","matrepact"],null,"JSON");
    var tblCalif2do=tabla.getRow("tblCalif2do",0,["cicescini","matrepact"],null,"JSON");
    var tblCalif3ro=tabla.getRow("tblCalif3ro",0,["cicescini"],null,"JSON");
    
    var tblCalif1ro=tabla.getTable("tblCalif1ro",["cvemat","cvetipmat","califant","promedio"]);
    for (var i=0; i<tblCalif1ro.length; i++)                                    //Agregamos a tblCalif1ro la columna de promedio respaldado inicialmente
        tblCalif1ro[i] += "~"+jsSeHi.tblCalif1ro_promedio_oldValue[i];
    
    var datos = { 
            modulo:"SeHi", metodo:"btGdCa1", tblCalif1ro:tblCalif1ro, cicescin:sisVars.cicescin, tblCalif1ro_cicescini:tablaCalif1ro.cicescini, 
            tblPrincipal_cveplan:jsSeHi.tblPrincipal_cveplan, tblCalif1ro_idalu:tablaCalif1ro.idalu, tblCalif1ro_almextrj:tablaCalif1ro.almextrj,
            tblCalif1ro_matrepact:(typeof tablaCalif1ro.matrepact==="undefined" || tablaCalif1ro.matrepact.indexOf("Undefined")>-1)?-1:tablaCalif1ro.matrepact,
            tblCalif2do_matrepact: (typeof tblCalif2do.matrepact==="undefined" || tblCalif2do.matrepact.indexOf("Undefined")>-1)?-1:tblCalif2do.matrepact,
            tblCalif2do_cicescini:tblCalif2do.cicescini,
            tblCalif3ro_cicescini:tblCalif3ro.cicescini, tblCalif2do_size: tabla.getNumRows("tblCalif2do"), tblCalif3ro_size:tabla.getNumRows("tblCalif3ro"),
            txtUsuario:sisVars.usuario
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
                    jsSeHi.msgExm1ro = result.msgExm1ro;
                    jsSeHi.tblCalif1ro_editable = result.tblCalif1ro_editable; 
                    $("#lblTCalif1ro_promgdo").text(result.tblCalif1ro[0].promgdo);
                    initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue,1, jsSeHi.tblCalif1ro_editable, jsSeHi.msgExm1ro);
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue,2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue,3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);
                    
                    if (typeof(result.tblCalif2do_matrepant)!=="undefined")
                        $("#lblTCalif2do_matrepant").text(result.tblCalif2do_matrepant);
                    if (typeof(result.tblCalif3ro_matrepant)!=="udefined")
                        $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro_matrepant);
                    
                    mensaje.General("GUARDADO_EXITOSO");
                    cerrarLoading();
                break;
            case 0:case -1:
                    $("#lblTCalif1ro_promgdo").text(result.tblCalif1ro[0].promgdo);
                    initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue,1, jsSeHi.tblCalif1ro_editable, jsSeHi.msgExm1ro);
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue,2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue,3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);
                    
                    if (typeof(result.tblCalif2do_matrepant)!=="undefined")
                        $("#lblTCalif2do_matrepant").text(result.tblCalif2do_matrepant);
                    if (typeof(result.tblCalif3ro_matrepant)!=="udefined")
                        $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro_matrepant);
                    
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                    cerrarLoading();
                break;
            case -11:   
                    initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue, 1, jsSeHi.tblCalif1ro_editable, jsSeHi.msgExm1ro);
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                    
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

function btnOfExmsExt1ro_Click()
{
    
}

function btnCalifDBim1ro_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    var tblCalif1ro=tabla.getRow("tblCalif1ro",0,["idalu","cicescini"],null,"JSON");
    
    var datos = { 
        modulo:"SeHi", metodo:"btCaDBi1", tblCalif1ro:tabla.getTable("tblCalif1ro",["cvemat","cvetipmat","exm","eer","promedio"]), 
        tblCalif1ro_cicescini:tblCalif1ro.cicescini, tblCalif1ro_idalu:tblCalif1ro.idalu, txtUsuario:sisVars.usuario
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
                    initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue, 1, jsSeHi.tblCalif1ro_editable,);
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
function btnInserFila1ro_Click()
{
    if(jsSeHi.tblCalif1ro_matrepact>0)
        inserNewRowTableExmExt ("tblExmExt1ro", jsSeHi.cbxTExm1ro_Column1, jsSeHi.cbxTExm1ro_Column3, jsSeHi.cbxTExm1ro_idccts);
}

function btnQuitarFila1ro_Click()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla ();
    
    if (!tabla.hasSelectedRow("tblExmExt1ro"))
        mensaje.General("NO_SELEC"," examen","eliminarlo");
    else{
        if ( mensaje.confirmDialog("ELIMINAR","el examen") )
            if ( validarEntradasDeExamenesExtraord ("tblCalif1ro", "tblExmExt1ro", jsSeHi.tblExmExt1ro_OldValues) ){
                tabla.removeRow("tblExmExt1ro",tabla.getSelectedIndexRow("tblExmExt1ro"));
                btnGuardaExamExt1ro_Click();
            }
    }
}
function btnGuardaExamExt1ro_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    if ( validarEntradasDeExamenesExtraord ("tblCalif1ro", "tblExmExt1ro", jsSeHi.tblExmExt1ro_OldValues) )
    {
        var tblExmExt1ro_OldValues = new Array();
        for (var i=0; i<jsSeHi.tblExmExt1ro_OldValues.length; i++)
            tblExmExt1ro_OldValues[i]=jsSeHi.tblExmExt1ro_OldValues[i].idarray+"~"+jsSeHi.tblExmExt1ro_OldValues[i].dia+"~"+jsSeHi.tblExmExt1ro_OldValues[i].mes+"~"
                +jsSeHi.tblExmExt1ro_OldValues[i].anio+"~"+jsSeHi.tblExmExt1ro_OldValues[i].promedio+"~"+jsSeHi.tblExmExt1ro_OldValues[i].cct+"~"
                +jsSeHi.tblExmExt1ro_OldValues[i].idcct_apl;

        var tblCalif1ro=tabla.getSelectedRow("tblCalif1ro",["idalu","cicescini","cvetipmat","cvemat"],null,"JSON");
        var tblExmExt1ro=tabla.getTable("tblExmExt1ro",["idarray","dia","mes","anio","promedio","cct_opt","cct"]);

        var datos = { 
            modulo:"SeHi", metodo:"btGuOp1", tblExmExt1ro:tblExmExt1ro, tblExmExt1ro_OldValues:tblExmExt1ro_OldValues, idalu_oldValue:tblCalif1ro.idalu, 
            tblCalif1ro_cicescini:tblCalif1ro.cicescini, grado_oldValue:"1", cvetipmat_oldValue:tblCalif1ro.cvetipmat, cvemat_oldValue:tblCalif1ro.cvemat, 
            txtUsuario:sisVars.usuario
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
                        //Respaldamos los cambios
                        tblExmExt1ro = tabla.getTable("tblExmExt1ro",["idarray","dia","mes","anio","promedio","cct_opt","cct"],null,"JSON");
                        jsSeHi.tblExmExt1ro_OldValues = new Array();
                        for (var i=0; i<tblExmExt1ro.length; i++)
                            jsSeHi.tblExmExt1ro_OldValues[i]={ idarray:i, dia:tblExmExt1ro[i].dia, mes:tblExmExt1ro[i].mes, anio:tblExmExt1ro[i].anio, 
                                promedio: tblExmExt1ro[i].promedio, cct:tblExmExt1ro[i].cct_opt, idcct_apl:tblExmExt1ro[i].cct };
                        
                        mensaje.General("GUARDADO_EXITOSO");
                        cerrarLoading();
                    break;
                case 0:case -1:
                        initTablaExmExt ("tblExmExt1ro", result.tblExmExt1ro, jsSeHi.cbxTExm1ro_Column1, jsSeHi.cbxTExm1ro_Column3, jsSeHi.cbxTExm1ro_idccts,tblCalif1ro.desmat,"");
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

function btnGdaCalif2do_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    var tblCalif1ro=tabla.getRow("tblCalif1ro",0,["matrepact"],null,"JSON");
    var tablaCalif2do=tabla.getRow("tblCalif2do",0,["idalu","cicescini","almextrj","matrepact"],null,"JSON");
    var tblCalif3ro=tabla.getRow("tblCalif3ro",0,["cicescini"],null,"JSON");
    
    var tblCalif2do=tabla.getTable("tblCalif2do",["cvemat","cvetipmat","califant","promedio"]);
    for (var i=0; i<tblCalif2do.length; i++)                                    //Agregamos a tblCalif2do la columna de promedio respaldado inicialmente
        tblCalif2do[i] += "~"+jsSeHi.tblCalif2do_promedio_oldValue[i];
    
    var datos = { 
            modulo:"SeHi", metodo:"btGdCa2", tblCalif2do:tblCalif2do, cicescin:sisVars.cicescin, tblCalif2do_cicescini:tablaCalif2do.cicescini, 
            tblPrincipal_cveplan:jsSeHi.tblPrincipal_cveplan, tblCalif2do_idalu:tablaCalif2do.idalu, tblCalif2do_almextrj:tablaCalif2do.almextrj,
            tblCalif2do_matrepact:(typeof tablaCalif2do.matrepact==="undefined" || tablaCalif2do.matrepact.indexOf("Undefined")>-1)?-1:tablaCalif2do.matrepact,
            tblCalif1ro_matrepact: (typeof tblCalif1ro.matrepact==="undefined" || tblCalif1ro.matrepact.indexOf("Undefined")>-1)?-1:tblCalif1ro.matrepact,
            tblCalif3ro_cicescini:tblCalif3ro.cicescini, tblCalif1ro_size: tabla.getNumRows("tblCalif1ro"), tblCalif3ro_size:tabla.getNumRows("tblCalif3ro"),
            txtUsuario:sisVars.usuario
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
                    jsSeHi.msgExm2do = result.msgExm2do;
                    jsSeHi.tblCalif2do_editable = result.tblCalif2do_editable;
                    $("#lblTCalif2do_promgdo").text(result.tblCalif2do[0].promgdo);
                    initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue, 1, jsSeHi.tblCalif1ro_editable, jsSeHi.msgExm1ro);
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);
                    
                    if (typeof(result.tblCalif2do_matrepant)!=="undefined")
                        $("#lblTCalif2do_matrepant").text(result.tblCalif2do_matrepant);
                    if (typeof(result.tblCalif3ro_matrepant)!=="udefined")
                        $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro_matrepant);
                    mensaje.General("GUARDADO_EXITOSO");
                    cerrarLoading();
                break;
            case 0:case -1:
                    $("#lblTCalif2do_promgdo").text(result.tblCalif2do[0].promgdo);
                    initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue, 1, jsSeHi.tblCalif1ro_editable, jsSeHi.msgExm1ro);
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);
                    
                    if (typeof(result.tblCalif2do_matrepant)!=="undefined")
                        $("#lblTCalif2do_matrepant").text(result.tblCalif2do_matrepant);
                    if (typeof(result.tblCalif3ro_matrepant)!=="udefined")
                        $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro_matrepant);
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
                break;
            case -10:                    
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                    cerrarLoading();
                break;
            case -11:   
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                    
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
function btnCalifDBim2do_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    var tblCalif2do=tabla.getRow("tblCalif2do",0,["idalu","cicescini"],null,"JSON");
    
    var datos = { 
        modulo:"SeHi", metodo:"btCaDBi2", tblCalif2do:tabla.getTable("tblCalif2do",["cvemat","cvetipmat","exm","eer","promedio"]), 
        tblCalif2do_cicescini:tblCalif2do.cicescini, tblCalif2do_idalu:tblCalif2do.idalu, txtUsuario:sisVars.usuario
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
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
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
function btnInserFila2do_Click()
{
    if(jsSeHi.tblCalif2do_matrepact>0)
        inserNewRowTableExmExt ("tblExmExt2do", jsSeHi.cbxTExm2do_Column1, jsSeHi.cbxTExm2do_Column3, jsSeHi.cbxTExm2do_idccts);
}
function btnQuitarFila2do_Click()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla ();
    
    if (!tabla.hasSelectedRow("tblExmExt2do"))
        mensaje.General("NO_SELEC"," examen","eliminarlo");
    else{
        if ( mensaje.confirmDialog("ELIMINAR","el examen") )
            if ( validarEntradasDeExamenesExtraord ("tblCalif2do", "tblExmExt2do", jsSeHi.tblExmExt2do_OldValues) ){
                tabla.removeRow("tblExmExt2do",tabla.getSelectedIndexRow("tblExmExt2do"));
                btnGuardaExamExt2do_Click();
            }
    }
}
function btnGuardaExamExt2do_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    if ( validarEntradasDeExamenesExtraord ("tblCalif2do", "tblExmExt2do", jsSeHi.tblExmExt2do_OldValues) )
    {
        var tblExmExt2do_OldValues = new Array();
        for (var i=0; i<jsSeHi.tblExmExt2do_OldValues.length; i++)
            tblExmExt2do_OldValues[i]=jsSeHi.tblExmExt2do_OldValues[i].idarray+"~"+jsSeHi.tblExmExt2do_OldValues[i].dia+"~"+jsSeHi.tblExmExt2do_OldValues[i].mes+"~"
                +jsSeHi.tblExmExt2do_OldValues[i].anio+"~"+jsSeHi.tblExmExt2do_OldValues[i].promedio+"~"+jsSeHi.tblExmExt2do_OldValues[i].cct+"~"
                +jsSeHi.tblExmExt2do_OldValues[i].idcct_apl;

        var tblCalif2do=tabla.getSelectedRow("tblCalif2do",["idalu","cicescini","cvetipmat","cvemat"],null,"JSON");
        var tblExmExt2do=tabla.getTable("tblExmExt2do",["idarray","dia","mes","anio","promedio","cct_opt","cct"]);

        var datos = { 
            modulo:"SeHi", metodo:"btGuOp2", tblExmExt2do:tblExmExt2do, tblExmExt2do_OldValues:tblExmExt2do_OldValues, idalu_oldValue:tblCalif2do.idalu, 
            tblCalif2do_cicescini:tblCalif2do.cicescini, grado_oldValue:"2", cvetipmat_oldValue:tblCalif2do.cvetipmat, cvemat_oldValue:tblCalif2do.cvemat, 
            txtUsuario:sisVars.usuario
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
                        initTablaExmExt ("tblExmExt2do", result.tblExmExt2do, jsSeHi.cbxTExm2do_Column1, jsSeHi.cbxTExm2do_Column3, jsSeHi.cbxTExm2do_idccts,tblCalif2do.desmat,"");
                        mensaje.General("GUARDADO_EXITOSO");
                        cerrarLoading();
                    break;
                case 0:case -1:
                        mensaje.General ("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        initTablaExmExt ("tblExmExt2do", result.tblExmExt2do, jsSeHi.cbxTExm2do_Column1, jsSeHi.cbxTExm2do_Column3, jsSeHi.cbxTExm2do_idccts,tblCalif2do.desmat,"");                        
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


function btnGdaCalif3ro_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    var tblCalif1ro=tabla.getRow("tblCalif1ro",0,["cicescini","matrepact"],null,"JSON");
    var tblCalif2do=tabla.getRow("tblCalif2do",0,["cicescini","matrepact"],null,"JSON");
    var tablaCalif3ro=tabla.getRow("tblCalif3ro",0,["idalu","cicescini","almextrj"],null,"JSON");
    
    var tblCalif3ro=tabla.getTable("tblCalif3ro",["cvemat","cvetipmat","califant","promedio"]);
    for (var i=0; i<tblCalif3ro.length; i++)                                    //Agregamos a tblCalif3ro la columna de promedio respaldado inicialmente
        tblCalif3ro[i] += "~"+jsSeHi.tblCalif3ro_promedio_oldValue[i];
    
    var datos = { 
            modulo:"SeHi", metodo:"btGdCa3", tblCalif3ro:tblCalif3ro, cicescin:sisVars.cicescin, tblCalif3ro_cicescini:tablaCalif3ro.cicescini, 
            tblPrincipal_cveplan:jsSeHi.tblPrincipal_cveplan, tblCalif3ro_idalu:tablaCalif3ro.idalu, tblCalif3ro_almextrj:tablaCalif3ro.almextrj,
            tblCalif3ro_matrepact:(typeof tablaCalif3ro.matrepact==="undefined" || tablaCalif3ro.matrepact.indexOf("Undefined")>-1)?-1:tablaCalif3ro.matrepact,
            tblCalif1ro_matrepact: (typeof tblCalif1ro.matrepact==="undefined" || tblCalif1ro.matrepact.indexOf("Undefined")>-1)?-1:tblCalif1ro.matrepact,
            tblCalif1ro_cicescini:tblCalif1ro.cicescini,
            tblCalif2do_cicescini:tblCalif2do.cicescini, tblCalif1ro_size: tabla.getNumRows("tblCalif1ro"), tblCalif2do_size:tabla.getNumRows("tblCalif2do"),
            txtUsuario:sisVars.usuario
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
                    jsSeHi.msgExm3ro = result.msgExm3ro;
                    jsSeHi.tblCalif3ro_editable = result.tblCalif3ro_editable; 
                    
                    $("#lblTCalif3ro_promgdo").text(result.tblCalif3ro[0].promgdo);
                    initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue, 1, jsSeHi.msgExm1ro);
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, jsSeHi.msgExm2do);
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, jsSeHi.msgExm3ro);
                    
                    if (typeof(result.tblCalif2do_matrepant)!=="undefined")
                        $("#lblTCalif2do_matrepant").text(result.tblCalif2do_matrepant);
                    if (typeof(result.tblCalif3ro_matrepant)!=="udefined")
                        $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro_matrepant);
                    mensaje.General("GUARDADO_EXITOSO");
                    cerrarLoading();
                break;
            case 0:case -1:                    
                    if(result.tipoMensaje==="ERROR")
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    else {
                        $("#lblTCalif3ro_promgdo").text(result.tblCalif3ro[0].promgdo);
                        initTablaCalif ("tblCalif1ro", result.tblCalif1ro, "tblExmExt1ro", jsSeHi.tblCalif1ro_promedio_oldValue, 1, jsSeHi.tblCalif1ro_editable, jsSeHi.msgExm1ro);
                        initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
                        initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);

                        if (typeof(result.tblCalif2do_matrepant)!=="undefined")
                            $("#lblTCalif2do_matrepant").text(result.tblCalif2do_matrepant);
                        if (typeof(result.tblCalif3ro_matrepant)!=="udefined")
                            $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro_matrepant);
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    }
                    cerrarLoading();
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                    cerrarLoading();
                break;
            case -11:   
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");                    
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
function btnCalifDBim3ro_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    var tblCalif3ro=tabla.getRow("tblCalif3ro",0,["idalu","cicescini"],null,"JSON");
    
    var datos = { 
        modulo:"SeHi", metodo:"btCaDBi3", tblCalif3ro:tabla.getTable("tblCalif3ro",["cvemat","cvetipmat","exm","eer","promedio"]), 
        tblCalif3ro_cicescini:tblCalif3ro.cicescini, tblCalif3ro_idalu:tblCalif3ro.idalu, txtUsuario:sisVars.usuario
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
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);
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
function btnInserFila3ro_Click ()
{
    if(jsSeHi.tblCalif3ro_matrepact>0)
        inserNewRowTableExmExt ("tblExmExt3ro", jsSeHi.cbxTExm3ro_Column1, jsSeHi.cbxTExm3ro_Column3, jsSeHi.cbxTExm3ro_idccts);
}
function btnQuitarFila3ro_Click()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla ();
    
    if (!tabla.hasSelectedRow("tblExmExt3ro"))
        mensaje.General("NO_SELEC"," examen","eliminarlo");
    else{
        if ( mensaje.confirmDialog("ELIMINAR","el examen") )            
            if ( validarEntradasDeExamenesExtraord ("tblCalif3ro", "tblExmExt3ro", jsSeHi.tblExmExt3ro_OldValues) ){            
                tabla.removeRow("tblExmExt3ro",tabla.getSelectedIndexRow("tblExmExt3ro"));
                btnGuardaExamExt3ro_Click();
            }
    }
}
function btnGuardaExamExt3ro_Click()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla ();
    
    if ( validarEntradasDeExamenesExtraord ("tblCalif3ro", "tblExmExt3ro", jsSeHi.tblExmExt3ro_OldValues) )
    {
        var tblExmExt3ro_OldValues = new Array();
        for (var i=0; i<jsSeHi.tblExmExt3ro_OldValues.length; i++)
            tblExmExt3ro_OldValues[i]=jsSeHi.tblExmExt3ro_OldValues[i].idarray+"~"+jsSeHi.tblExmExt3ro_OldValues[i].dia+"~"+jsSeHi.tblExmExt3ro_OldValues[i].mes+"~"
                +jsSeHi.tblExmExt3ro_OldValues[i].anio+"~"+jsSeHi.tblExmExt3ro_OldValues[i].promedio+"~"+jsSeHi.tblExmExt3ro_OldValues[i].cct+"~"
                +jsSeHi.tblExmExt3ro_OldValues[i].idcct_apl;

        var tblCalif3ro=tabla.getSelectedRow("tblCalif3ro",["idalu","cicescini","cvetipmat","cvemat"],null,"JSON");
        var tblExmExt3ro=tabla.getTable("tblExmExt3ro",["idarray","dia","mes","anio","promedio","cct_opt","cct"]);

        var datos = { 
            modulo:"SeHi", metodo:"btGuOp3", tblExmExt3ro:tblExmExt3ro, tblExmExt3ro_OldValues:tblExmExt3ro_OldValues, idalu_oldValue:tblCalif3ro.idalu, 
            tblCalif3ro_cicescini:tblCalif3ro.cicescini, grado_oldValue:"3", cvetipmat_oldValue:tblCalif3ro.cvetipmat, cvemat_oldValue:tblCalif3ro.cvemat, 
            txtUsuario:sisVars.usuario
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
                        initTablaExmExt ("tblExmExt3ro", result.tblExmExt3ro, jsSeHi.cbxTExm3ro_Column1, jsSeHi.cbxTExm3ro_Column3, jsSeHi.cbxTExm3ro_idccts,tblCalif3ro.desmat,"");
                        mensaje.General("GUARDADO_EXITOSO");
                        cerrarLoading();
                    break;
                case 0:case -1:
                        initTablaExmExt ("tblExmExt3ro", result.tblExmExt3ro, jsSeHi.cbxTExm3ro_Column1, jsSeHi.cbxTExm3ro_Column3, jsSeHi.cbxTExm3ro_idccts,tblCalif3ro.desmat,"");
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
function btnActualizaReprobadas_Click ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla ();
    
    var tblCalif1ro=tabla.getRow("tblCalif1ro",0,["cicescini","matrepact"],null,"JSON");
    var tblCalif2do=tabla.getRow("tblCalif2do",0,["cicescini","matrepact"],null,"JSON");
    var tblCalif3ro=tabla.getRow("tblCalif3ro",0,["cicescini"],null,"JSON");
    
    var datos = { 
        modulo:"SeHi", metodo:"btAcRe_Cl", tblAlumCapCalif_idalu:jsSeHi.tblAlumCapCalif_idalu, tblCalif2do_cicescini:tblCalif2do.cicescini, 
        tblCalif3ro_cicescini:tblCalif3ro.cicescini, tblCalif1ro_matrepact:tblCalif1ro.matrepact, tblCalif2do_matrepact:tblCalif2do.matrepact, 
        tblCalif1ro_size: tabla.getNumRows("tblCalif1ro"), tblCalif2do_size: tabla.getNumRows("tblCalif2do"), tblCalif3ro_size:tabla.getNumRows("tblCalif3ro")
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
                    initTablaCalif ("tblCalif2do", result.tblCalif2do, "tblExmExt2do", jsSeHi.tblCalif2do_promedio_oldValue, 2, jsSeHi.tblCalif2do_editable, jsSeHi.msgExm2do);
                    initTablaCalif ("tblCalif3ro", result.tblCalif3ro, "tblExmExt3ro", jsSeHi.tblCalif3ro_promedio_oldValue, 3, jsSeHi.tblCalif3ro_editable, jsSeHi.msgExm3ro);
                    
                    if (typeof(result.tblCalif2do_matrepant)!=="undefined")
                        $("#lblTCalif2do_matrepant").text(result.tblCalif2do_matrepant);
                    if (typeof(result.tblCalif3ro_matrepant)!=="udefined")
                        $("#lblTCalif3ro_matrepant").text(result.tblCalif3ro_matrepant);
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

function validarEntradasDeExamenesExtraord (nombreTablaCalif, nombreTablaExamen, jsSeHi_tblExmExt_OldValues)
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var numFilasTablaExamen = tabla.getNumRows(nombreTablaExamen);
    var fila;
    
    if (tabla.getSelectedIndexRow(nombreTablaCalif)<0)
        return mensaje.General ("NO_SELEC","a materia","modificar y guardar exámenes extraordinarios.");
    else if (numFilasTablaExamen === 0 && (jsSeHi_tblExmExt_OldValues===null || jsSeHi_tblExmExt_OldValues.length === 0) )
        return mensaje.General("ESPECIFIQUE_DATO","datos de exámen extraordinario para la materia seleccionada");
    else{        
        for (var i=0; i<numFilasTablaExamen; i++){            
            fila = tabla.getRow(nombreTablaExamen,i,["dia","mes","anio","cct","promedio"],null,"JSON"); 
            if (fila.mes === "")
                return mensaje.General("CAMPO_VACIO","'MES' de la fila "+(i+1));
            if (fila.dia === "")
                return mensaje.General("CAMPO_VACIO","'DIA' de la fila "+(i+1));
            /*else if (!verificarFechaExmExt(fila.dia, fila.mes))
                return mensaje.General("FECHA_INVALIDA","'DIA' de la fila "+(i+1));*/ //Comentado para no validar el día de examen
            
            if (fila.anio === "")
                return mensaje.General("CAMPO_VACIO","'AÑO' de la fila "+(i+1));
            if (fila.cct === "")
                return mensaje.General("CAMPO_VACIO","'CCT' de la fila "+(i+1));
            if (fila.promedio === "")
                return mensaje.General("CAMPO_VACIO","'PROMEDIO' de la fila "+(i+1));
            if (!isFloat(fila.promedio))
                return mensaje.General("CALIF_INVALIDA",fila.promedio+" en fila "+(i+1));
            if (!isFecha(fila.dia+"/"+fila.mes+"/"+fila.anio))
                return mensaje.CalifSecXBim("FECHA_INVALIDA",fila.dia+"/"+fila.mes+"/"+fila.anio,"en la fila "+(i+1));            
        }
    }
    return true;
}

function verificarFechaExmExt(dia, mes)
{    
    /*var valido = false;
    var fechas = jsSeHi.cbxTExm_mesesExmExt;
    for(var i=0; i< fechas.length; i++)
        if(fechas[i].mes===mes)
            return (parseInt(fechas[i].dia_ini) <= parseInt(dia)) && (parseInt(dia) <= parseInt(fechas[i].dia_fin));        
    return valido;*/
}

function inserNewRowTableExmExt (nombreTablaExmExt, jsSeHi_cbxTExm_Column1, jsSeHi_cbxTExm_Column3, jsSeHi_cbxTExm_idccts)
{
    var numRows;
    var tabla = new Tabla ();
    var fila = new Array();
    var columnas={dia:"",mes:"",anio:"",cct:"",promedio:"",idarray:"", idcct:""};
    fila[0]=columnas;
    tabla.addRows (-1,nombreTablaExmExt,fila, ["dia","mes","anio","cct","promedio"], ["textbox","combobox","textbox","combobox", "textbox"], null, true, null, null, null);
    numRows = tabla.getNumRows(nombreTablaExmExt);
    /*********** Insertamos el catálogo para los combos mes ***********/
    //tabla.setDataToComboboxCol (nombreTablaExmExt, 1, null, jsSeHi_cbxTExm_Column1,true, false, null,numRows-1);
    tabla.setDataToComboboxCol (nombreTablaExmExt, 1, null, jsSeHi.cbxTExm_meses,true, false, null,numRows-1);
    $($("#pnlSecHist #"+nombreTablaExmExt+"_td_f"+(numRows-1)+"_c1 > select")).prop('selectedIndex', 0);
    /*********** Insertamos el catálogo para los combos cct ***********/
    tabla.setDataToComboboxCol (nombreTablaExmExt, 3, jsSeHi_cbxTExm_Column3, jsSeHi_cbxTExm_idccts,true, false, null,numRows-1);
    $($("#pnlSecHist #"+nombreTablaExmExt+"_td_f"+(numRows-1)+"_c3 > select")).prop('selectedIndex', 0);
}

function tblMatCalif_ApplyCellAttribute (nombreTablaCalif)
{    
    var tabla = new Tabla ();
    var tblCalif = tabla.getTable(nombreTablaCalif,["promedio","eer","exm"],null,"JSON");
    var nombre_col = "";
    for (var i=0; i<tblCalif.length; i++) {
        nombre_col = nombreTablaCalif+( $("#"+nombreTablaCalif+"_txt_f"+i+"_c1").length>0 ? "_txt_f"+i+"_c1" : "_td_f"+i+"_c1" );
        if (parseFloat(tblCalif[i].promedio) >= 6.0 ){                        
            $("#"+nombre_col).css("font-weight","bold");
            $("#"+nombre_col).css("color", "#007700");//Verde            
        }
        else if (parseFloat(tblCalif[i].promedio) > 0.0 &&  parseFloat(tblCalif[i].promedio) < 6.0 ) {
            $("#"+nombre_col).css("font-weight","bold");
            $("#"+nombre_col).css("color", "#ba2424");////Rojo cafesoso
        }
        if ( parseInt(tblCalif[i].eer) > 0 ) {            
            if($("#"+nombreTablaCalif+"_txt_f"+i+"_c1").length){
                $("#"+nombre_col).css("font-weight","bold");
                $("#"+nombre_col).css("color", "white");
                $("#"+nombre_col).css("background-color", "#ff8040");//naranja;
            } else {
                $("#"+nombre_col).css("font-weight","bold");
                $("#"+nombre_col).css("color", "white");
                $("#"+nombre_col).css("background-color", "#ff8040");//naranja;
            }                
        }
        if ( parseInt(tblCalif[i].exm) === "ER" ) {            
            $("#"+nombre_col).css("font-weight","bold");            
        }
    }
}
 
//****************   Mostrar modal de materias extraordinarios *************************************//
//**************************************************************************************************//
function mwfExmExtraordinarios_Show(grado)
{
    var nombreTabla;
    if(grado===1)
        nombreTabla = "tblCalif1ro";
    else if (grado===2)
        nombreTabla = "tblCalif2do";
    else
        nombreTabla = "tblCalif3ro";
    
    object_setVisible (true,"mwfmExmExtraordinarios");                                      // Ocultamos el gridTable
    //$("#mwffExtraordinarios").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfExmExtraordinarios_Create(nombreTabla);
    mwffExmExtraordinarios_FormActivate (nombreTabla, grado);
    //event.stopPropagation();
}

function mwfExmExtraordinarios_Close()
{
    $('#mwfmExmExtraordinarios #mwffExmExtraordinarios').remove();
    object_setVisible (false,"mwfmExmExtraordinarios");
    $("#txtBusquedaCCT").focus();
}

function mwfExmExtraordinarios_Create (nombreTabla)
{
    var tabla = new Tabla();  
    var tblCalif=tabla.getRow(nombreTabla,0,["idalu","cicescini"],null,"JSON");
    
    modalWindow_Create ("ExmExtraordinarios", "Examenes extraordinarios del alumno para oficializar",mwfExmExtraordinarios_Close, 90, {anchoAutoajustable:true});
    $('#mwfpExmExtraordinarios').append('<div id="pnlExmExtraordinarios"></div>');
    
        $('#pnlExmExtraordinarios').append('<div id="pnlDatosGeneralesExmExt"></div>');
                            $('#pnlDatosGeneralesExmExt').append('<label id="lblTblExmExt_idalu">'+tblCalif.idalu+'</label>');
                            $('#pnlDatosGeneralesExmExt').append('<label id="lblTblExmExt_curp">'+jsSeHi.tblAlumCapCalif_curp+'</label>');
                            $('#pnlDatosGeneralesExmExt').append('<label id="lblTblExmExt_cicescini">'+tblCalif.cicescini+'</label>');
        $('#pnlExmExtraordinarios').append('<div id="pnlListadoDeExmXMat" class="panel">'
                                            + '<label class="tituloPanel">Seleccione las materias para oficializar</label>'
                                            + '<div id="pnlTblExmExtXMat">  <div id="scrlExmExtXMat" class="scrollTable"></div>  </div>'
                                        +'</div>');
        $('#pnlExmExtraordinarios').append('<ul class="buttonBar"> '                                            
                                            + '<li><a href="#" id="btnOficExmExt"><label class="icon-sello"></label> Oficializar</a></li>'                                            
                                            + '<li><a href="#" id="btnOficCancelar"><label ></label>Cancelar</a></li>'                                           
                                        +'</ul>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------    
    $("#btnOficCancelar").on('click',function(){ mwfExmExtraordinarios_Close(); });
    $("#btnOficExmExt").on('click',function(){ btnOficExmExt_Click(); });
}
function mwffExmExtraordinarios_FormActivate (nombreTabla, grado)
{    
    var mensaje = new Mensajes ();     
    var tabla = new Tabla();    
    
    var tblCalif=tabla.getRow(nombreTabla,0,["idalu","cicescini"],null,"JSON");
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"SeHi", metodo:"btnExmOf",tblAlumCapCalif_idalu:jsSeHi.tblAlumCapCalif_idalu, grado:grado, 
        tblCalif_cicescini: tblCalif.cicescini, tblCalif:tblCalif
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
                    initTablaMateriasParaOficializar (result.tblExmExtXMat);
                break;
            case 0: case -1:
                    mwfExmExtraordinarios_Close();
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
    })
    .always(function() {
        cerrarLoading();
    });
}

function initTablaMateriasParaOficializar (tblExmExtXMat)
{    
    var tabla = new Tabla();
    var tituloColumnas, colsNameToShow, claseColumnas, textAlign;
    
    tituloColumnas = ["Seleccionar","idalu","GRADO","CVEMAT","MATERIA","DIA","MES","AÑO","PROMEDIO"];
    colsNameToShow = ["selec","idalu","grado","cvemat","desmat","dia","mes","anio","promedio","idperexmext"];
    claseColumnas = ["checkbox","","","","","","","","","hidden"];
    textAlign = ["center","","","",,"","","","","",""];
    
    tabla.create("scrlExmExtXMat","tblExmExtXMat",tblExmExtXMat, tituloColumnas, colsNameToShow, claseColumnas, textAlign, true, null, null ,
    function (f,c) {
        $("#pnlExmExtraordinarios #tblExmExtXMat_chk_f"+f+"_c0").on('click', function(e){ 
            //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
            document.getElementById("tblExmExtXMat_chk_f"+f+"_c0").checked = !document.getElementById("tblExmExtXMat_chk_f"+f+"_c0").checked;
        });

        $("#pnlExmExtraordinarios #tblExmExtXMat_td_f"+f+"_c0").on('click', function(e){
            if (!$("#pnlExmExtraordinarios #tblExmExtXMat_chk_f"+f+"_c0").prop("disabled"))
                document.getElementById("tblExmExtXMat_chk_f"+f+"_c0").checked = !document.getElementById("tblExmExtXMat_chk_f"+f+"_c0").checked;
        });
    });    
    for (var i=0; i<tblExmExtXMat.length; i++)
        if (tblExmExtXMat[i].selec === 't'){
            document.getElementById("tblExmExtXMat_chk_f"+i+"_c0").checked = true;
            $("#pnlExmExtraordinarios #tblExmExtXMat_chk_f"+i+"_c0").prop('disabled', true);
        }    
}

function btnOficExmExt_Click()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var reglon="";
    
    var tblExmExtXMat = tabla.getTable("tblExmExtXMat",["selec","idalu","grado","cvemat","cvetipmat","desmat","cicescini","promedio","idperexmext"], null,"JSON");
    for (var i=0; i<tblExmExtXMat.length; i++)
        if (tblExmExtXMat[i].selec === "true") {
            reglon += (reglon===""?"":", ") + tblExmExtXMat[i].idalu+"~"+tblExmExtXMat[i].grado+"~"+tblExmExtXMat[i].cvetipmat;
            reglon += "~"+tblExmExtXMat[i].cvemat+"~"+tblExmExtXMat[i].desmat+"~"+tblExmExtXMat[i].cicescini+"~"+tblExmExtXMat[i].promedio+"~"+tblExmExtXMat[i].idperexmext;  
        }
    if (reglon === "")
        mensaje.General("ESPECIFIQUE_DATO","por lo menos un registro para la oficialización");
    else if ( mensaje.confirmDialog("OFIC_EXTRAOR","examenes extraordinarios") ){
        var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"SeHi", metodo:"btOfExmExt_cl", 
            matsel:reglon, tblExmExtXMat: tblExmExtXMat, txtUsuario:sisVars.usuario
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
                        mensaje.General("PROCESO_EXITOSO");                       
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
        })
        .always(function() {
            cerrarLoading();
        });
    }    
}