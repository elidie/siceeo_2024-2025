/* 
    Creado el : 26/07/2016, 15:57:21 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsRepEvaluacion;

function frmwRepEvaluacion_Show(tblPrincipal_cveunidad, tblPrincipal_cvezona, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_modalidad, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cicescini, tblPrincipal_nombre, tblPrincipal_cveturno, tblPrincipal_cveplan, tblPrincipal_cveprograma) 
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsRepEvaluacion = {
        cveunidad: tblPrincipal_cveunidad,
        cvezona: tblPrincipal_cvezona,
        idcct: tblPrincipal_idcct,
        cct: tblPrincipal_cct,
        modalidad: tblPrincipal_modalidad,
        grado: tblPrincipal_grado,
        grupo: tblPrincipal_grupo,
        cicescini: tblPrincipal_cicescini,
        nombrecct: tblPrincipal_nombre,
        cveturno: tblPrincipal_cveturno,
        cveplan: tblPrincipal_cveplan,
        cveprograma: tblPrincipal_cveprograma,

        Califcicescin: tblPrincipal_cicescini,
        elementosReporte: null,
        configImpre:"",
        arrayIdAlu: null
    };

    object_setVisible (false,"gridTable");
    $("#frmwRepEvaluacion").css("display", "block");                                 // Mostramos el formulario correspondiente

    frmwRepEvaluacion_Create();
    frmwRepEvaluacion_FormActivate ();
}

function frmwRepEvaluacion_Close()
{
    jsRepEvaluacion=null;
    irAVentanaPrincipal ();
}

function frmwRepEvaluacion_Create()
{
    if($('#frmfRepEvaluacion').length)                                                                                                               // Verificando si la tabla Existe
        $('#frmfRepEvaluacion').remove();
    $('#frmwRepEvaluacion').append('<fieldset id="frmfRepEvaluacion"><legend>Reporte de Evaluación</legend> <div id="btnRegresar_RepEvaluacion" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfRepEvaluacion").append('<div id="pnlRepEvaluacion"></div>');
            
            $('#pnlRepEvaluacion').append('<div id="pnlAreaTrabajoRepEvaluacion"></div>');
                $('#pnlAreaTrabajoRepEvaluacion').append('<div id="pnlTablaRepEvaluacion"></div>');
                    $('#pnlTablaRepEvaluacion').append('<div id="pnlDatosGenerales"></div>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+jsRepEvaluacion.cct+'</label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+jsRepEvaluacion.grado+'</label>');
                            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+jsRepEvaluacion.grupo+'</label>');
                    $('#pnlTablaRepEvaluacion').append('<div id="pnlImpreRepEval"></div>');
                        $('#pnlImpreRepEval').append('<ul class="buttonBar alinearHoriz">'+
                                                        '<li><a href="#" id="btnCalibCaidaRepEvaluacion" title="Permite establecer la coordenadas de dónde va a imprimirse cada dato(texto) en la hoja de Reporte de Evaluación">Editar caídas de texto...</a></li>'+
                                                    '</ul>');
                        $('#pnlImpreRepEval').append('<div id="pnlVerRepEval" class="alinearHoriz">'
                                                        +'<div id="pnlTxtsDesdeHasta" class="alinearHoriz">'
                                                            +'<label class="alinearHoriz">De: </label><input type="text" id="txtSelDesde" class="alinearHoriz" value=""/> '
                                                            +'<label class="alinearHoriz">Hasta: </label><input type="text" id="txtSelHasta" class="alinearHoriz" value=""/> '
                                                        +'</div>'
                                                        +'<ul class="buttonBar alinearHoriz">'
                                                            +'<li><a href="#" id="btnImprimirRepEvaluacion" title="Muestra vista previa del Reporte de Evaluación con los datos de los alumnos seleccionados.">Ver Rep. Eval.</a></li>'
                                                        +'</ul>'
                                                    +'</div>');
                        $('#pnlImpreRepEval').append('<ul class="buttonBar alinearHoriz">'
                                                        +'<li><a href="#" id="btnGrupoAnterior" title="Grupo anterior."><label class="iconBtnGpoAnt iconBtnRedondo middleHoriz icon-arrow-left4"></label><label class="middleHoriz">Gpo. anterior</label></a></li>'
                                                        +'<li><a href="#" id="btnSiguienteGrupo" title="Siguiente grupo."><label class="middleHoriz">Siguiente gpo.</label><label class="iconBtnGpoSig iconBtnRedondo middleHoriz icon-arrow-right4"></label></a></li>'
                                                    +'</ul>');
                    $('#pnlTablaRepEvaluacion').append('<div id="pnlTblRepEvaluacion">  <div id="scrlRepEvaluacion" class="scrollTable"></div>  </div>');
                $('#pnlAreaTrabajoRepEvaluacion').append('<div id="pnlBotonesFolio" class="panel"><label class="tituloPanel">Asignación de folios</label></div>');
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                    $('#pnlBotonesFolio').append('<div id="pnlAsignarFolio" class="panel">'
                                                    +'<div id="pnlTxtsFolioIniFin" class="alinearHoriz">'
                                                        +'<label class="alinearHoriz">Folio inicial: </label>'
                                                        +'<input type="text" id="txtFolioLet" class="alinearHoriz" maxlength="1" value="" placeholder="_" />'
                                                        +'<input type="text" id="txtFolioNum" class="alinearHoriz" maxlength="7" value="" placeholder="numero" />'
                                                    +'</div>'
                                                    +'<ul class="buttonBar">'
                                                        +'<li><a href="#" id="btnGeneraFolio" title="Asigna folio sólo a los que están en blanco y a partir del alumno seleccionado."><label class="icon-generar"></label> Genera folios</a></li>'
                                                    +'</ul>'
                                                +'</div>');
                    $('#pnlBotonesFolio').append('<ul class="buttonBar">'+
                                                    '<li><a href="#" id="btnQuitaFolios" title="Le quita el Folio a los alumnos a partir del alumno seleccionado y cuyos certificados aun no se imprimen."><label class="iconBtnMenos iconBtnRedondo  middleHoriz icon-minus"></label>Quita folios</a></li>'+
                                                '</ul>');
                    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
        $("#btnRegresar_RepEvaluacion").unbind("click");
        $("#btnRegresar_RepEvaluacion").on("click",function(){ frmwRepEvaluacion_Close(); });
        
        $("#btnCalibCaidaRepEvaluacion").on("click",function(e){ btnCalibCaidaRepEvaluacion_Click(); e.preventDefault(); });
        $("#btnImprimirRepEvaluacion").on("click",function(e){ btnImprimirRepEvaluacion_Click(); e.preventDefault(); });
        $('#btnSiguienteGrupo').on("click",function(event){ btnSiguienteGpo_RepEvaluacion_Click (); event.preventDefault();  });
        $('#btnGrupoAnterior').on("click",function(event){ btnAnteriorGpo_RepEvaluacion_Click (); event.preventDefault();  });
        
        $('#btnGeneraFolio').on("click",function(event){ btnGeneraFolio_RepEvaluacion_Click (); event.preventDefault();  });
        $('#btnQuitaFolios').on("click",function(event){ btnQuitaFolios_RepEvaluacion_Click (); event.preventDefault();  });
        
        $("#txtSelDesde").on("blur",function(){ return ValNumero(this); });
        $("#txtSelHasta").on("blur",function(){ return ValNumero(this); });
        $("#txtSelHasta").on("blur",function(e){ txtSelHasta_Exit();  });
}

function frmwRepEvaluacion_FormActivate ()
{
    var mensaje = new Mensajes ();
    
    if (jsRepEvaluacion.cveplan === "3"){                                       //Ocultamos elementos si es preescolar
        object_setVisible(false,"pnlVerRepEval");
        object_setVisible(false,"pnlVerRepEval");
        boton_setVisible(false,"btnCalibCaidaRepEvaluacion");
    }
    //$("#pnlRepEvaluacion #pnlTablaRepEvaluacion").css("margin-right","0px");
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos= {
        modulo:"ReEv", metodo:"foAc", Califcicescin:jsRepEvaluacion.Califcicescin, cicescini:jsRepEvaluacion.cicescini, idcct:jsRepEvaluacion.idcct, 
        modalidad:jsRepEvaluacion.modalidad, cveplan:jsRepEvaluacion.cveplan, grado:jsRepEvaluacion.grado, grupo:jsRepEvaluacion.grupo, formato: "REPORTE_EVALUACION"
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
        cerrarLoading();
        switch(result.returnCase){
            case 1:
                    jsRepEvaluacion.configImpre = result.configImpre;
                    initTablaTblRepEvaluacion (result.tblRepEvaluacion, jsRepEvaluacion.grado, jsRepEvaluacion.grupo);
                break;
            case 0:case -1:
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

function initTablaTblRepEvaluacion (tblRepEvaluacion, tblPrincipal_grado, tblPrincipal_grupo)
{
    var tabla = new Tabla();
    $('#lblTblPrincipal_grado').text(tblPrincipal_grado);
    $('#lblTblPrincipal_grupo').text(tblPrincipal_grupo);
    
    tabla.create("scrlRepEvaluacion","tblRepEvaluacion",tblRepEvaluacion, ["sel","idalu","CURP","NOMBRE COMPLETO","foliolet","folionum"], ["selec","idalu","curp","nom_tot","foliolet","folionum"], ["checkbox","","","","",""], ["center","","","","",""], true, null, null ,
        function (f,c){
            $("#tblRepEvaluacion_chk_f"+f+"_c0").on('click', function(e){ 
                //Si dió click en el checbox, quitamos lo que haya puesto, porque ya lo está controlando el click en la fila, (es como hacer un event.dispose)
                document.getElementById("tblRepEvaluacion_chk_f"+f+"_c0").checked = !document.getElementById("tblRepEvaluacion_chk_f"+f+"_c0").checked;
            });

            $("#tblRepEvaluacion_td_f"+f+"_c0").on('click', function(e){ 
                document.getElementById("tblRepEvaluacion_chk_f"+f+"_c0").checked = !document.getElementById("tblRepEvaluacion_chk_f"+f+"_c0").checked;
            });
        });
    $('#tblRepEvaluacion_divth0').append(" <input type='checkbox' id='chkSelTodos' name='chkSelTodos' value='false'>");
    $('#chkSelTodos').on("change",function(){ chkSelTodos_Click(); });
    if (jsRepEvaluacion.cveplan === "3")
        $("#pnlRepEvaluacion .tblRepEvaluacion_col0").css("display","none");
}

function btnCalibCaidaRepEvaluacion_Click()
{
    var mensaje = new Mensajes ();
    var datos = {
        modulo:"caCaDeReEv", metodo:"geDaPaCa", idcct:jsRepEvaluacion.idcct, modalidad:jsRepEvaluacion.modalidad, cveplan:jsRepEvaluacion.cveplan, 
        grado:jsRepEvaluacion.grado
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"json",
            data: datos,
            async:true,
            success:function(result){                      
                switch(result.returnCase){
                    case 1:
                        if(result.coord.length > 0) 
                        {
                            var renglon=new Array();
                            var fila,cadena="";
                            $.each(result.coord,function(i,valor) {
                                fila = valor;                              
                                cadena=fila[0]+"-"+fila[1]+"-"+fila[2]+"-"+fila[3]+"-"+fila[4];
                                renglon.push(cadena);
                            });        

                            jsRepEvaluacion.elementosReporte = renglon;
                            sessionStorage.jsRepEvaluacion = JSON.stringify(jsRepEvaluacion);

                            window.open('../html/RepEvaluacion/calibraReporte.html'); 
                        } else
                            alert("Información:\n\nNo se encontraron coordenadas \npara este centro de trabajo.");
                        
                        cerrarLoading();
                        break;
                    case 0: case -1:alert(result.tipoMensaje +"\n\n"+result.mensaje);
                        break;
                    case -10:
                            //e.preventDefault();
                            mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                            btnCerrarSesion_ActionPerformed ();
                        break;
                    default:break;
                }
        }});
}

function btnImprimirRepEvaluacion_Click()
{
    jsRepEvaluacion.arrayIdAlu = getIdalusAImprimir();
    if(jsRepEvaluacion.arrayIdAlu.length)
    {           
        var datos = "idcct="+jsRepEvaluacion.idcct+"&modalidad="+jsRepEvaluacion.modalidad+"&cveplan="+jsRepEvaluacion.cveplan+"&grado="+jsRepEvaluacion.grado
                +"&grupo="+jsRepEvaluacion.grupo+"&cveturno="+jsRepEvaluacion.cveturno+"&nombrecct="+jsRepEvaluacion.nombrecct+"&cct="+jsRepEvaluacion.cct
                +"&cveprograma="+jsRepEvaluacion.cveprograma+"&configImpre="+jsRepEvaluacion.configImpre+"&tipoImpresion=0"
                +"&cicescini="+jsRepEvaluacion.cicescini+'&arrayIdAlu='+jsRepEvaluacion.arrayIdAlu;
        window.open('RepEvaluacion/imprimirRepEvaluacion.jsp?'+datos,'_blank'); 
    }
    else
        alert("Información: \n\nDebe seleccionar al menos a un alumno");
}

function btnAnteriorGpo_RepEvaluacion_Click()
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    
    if ( posSelActual-1 >= 0 ){
        var datos = {
            modulo:"ReEv", metodo:"btAnGp_Cl", posSelActual: posSelActual, tblPrincipal:tabla.getTable("tblPrincipal",["grado","grupo"]), 
            Califcicescin:jsRepEvaluacion.Califcicescin, cicescin:sisVars.cicescin, idcct:jsRepEvaluacion.idcct, modalidad:jsRepEvaluacion.modalidad, 
            cveplan:jsRepEvaluacion.cveplan, formato:"REPORTE_EVALUACION"
        };
        sigAntGrupo_RepEvaluacion (datos);
    }
}

function btnSiguienteGpo_RepEvaluacion_Click ()
{
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    var numFilas = tabla.getNumRows('tblPrincipal');
    
    if ( posSelActual+1 <  numFilas){
        var datos = {
            modulo:"ReEv", metodo:"btSiGp_Cl", posSelActual: posSelActual, tblPrincipal:tabla.getTable("tblPrincipal",["grado","grupo"]), 
            Califcicescin:jsRepEvaluacion.Califcicescin, cicescin:sisVars.cicescin, idcct:jsRepEvaluacion.idcct, modalidad:jsRepEvaluacion.modalidad, 
            cveplan:jsRepEvaluacion.cveplan, formato:"REPORTE_EVALUACION"
        };
        sigAntGrupo_RepEvaluacion (datos);
    }
}

function btnGeneraFolio_RepEvaluacion_Click ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    var posSelActual = tabla.getSelectedIndexRow ('tblRepEvaluacion');
    if (posSelActual < 0)
        mensaje.General("NO_SELEC", " alumno","asignar el(los) folio(s) a partir de ahí");
    else if ( mensaje.RepEvaluacion("ASIGNAR_FOLIOS","","","CONFIRM_DIALOG") )
    {
        var datos = {
                modulo:"ReEv", metodo:"btGeFo_Cl", txtFolioLet:$("#txtFolioLet").val().trim(), txtFolioNum:$("#txtFolioNum").val().trim(), posSelActual: posSelActual, 
                tblRepEvaluacion:tabla.getTable("tblRepEvaluacion",["idalu","foliolet","folionum","nombre","apepat","apemat","curp"]), 
                tblRepEvaluacion_cvezona:jsRepEvaluacion.cvezona, tblRepEvaluacion_idcct:jsRepEvaluacion.idcct, tblRepEvaluacion_cct:jsRepEvaluacion.cct,
                tblPrincipal_modalidad:jsRepEvaluacion.modalidad, tblRepEvaluacion_cveturno:jsRepEvaluacion.cveturno, 
                tblRepEvaluacion_cveplan:jsRepEvaluacion.cveplan, tblRepEvaluacion_grado:jsRepEvaluacion.grado, tblRepEvaluacion_grupo:jsRepEvaluacion.grupo, 
                tblRepEvaluacion_cicescini:jsRepEvaluacion.Califcicescin, txtUsuario:sisVars.usuario, tblRepEvaluacion_cveunidad:jsRepEvaluacion.cveunidad
            };
        //------------------------- Hacemos la llamada -------------------------
        cargarLoading();                                                        //$('html, pnlPrincipalWorkArea').css("cursor", "wait"); 
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:true
        })
        .done(function(result){
            switch(result.returnCase){
                case 1:
                        initTablaTblRepEvaluacion (result.tblRepEvaluacion, jsRepEvaluacion.grado, jsRepEvaluacion.grupo);
                        tabla.setSelectedRow ('tblRepEvaluacion', posSelActual);
                        mensaje.RepEvaluacion("FOLIOS_ASIGNADOS");
                        cerrarLoading();
                    break;
                case 0: 
                        initTablaTblRepEvaluacion (result.tblRepEvaluacion, jsRepEvaluacion.grado, jsRepEvaluacion.grupo);
                        tabla.setSelectedRow ('tblRepEvaluacion', posSelActual);
                        $("#txtFolioNum").val(result.txtFolioNum_Text);
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        cerrarLoading();
                    break;
                case -1:
                        cerrarLoading();                                            //$('html, pnlPrincipalWorkArea').css("cursor", "default"); 
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

function btnQuitaFolios_RepEvaluacion_Click ()
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    
    var posSelActual = tabla.getSelectedIndexRow ('tblRepEvaluacion');
    
    if (posSelActual < 0)
        mensaje.General("NO_SELEC", " alumno","quitar el(los) folio(s) a partir de ahí");
    else if ( mensaje.RepEvaluacion("QUITAR_FOLIOS","","","CONFIRM_DIALOG") )
    {
        var datos = {
                modulo:"ReEv", metodo:"btQuFo_Cl",  posSelActual: posSelActual, tblRepEvaluacion:tabla.getTable("tblRepEvaluacion",["idalu","foliolet","folionum","cicescini"]), 
                tblRepEvaluacion_cicescini:jsRepEvaluacion.Califcicescin, tblRepEvaluacion_cveplan:jsRepEvaluacion.cveplan, 
                tblRepEvaluacion_idcct:jsRepEvaluacion.idcct, tblRepEvaluacion_grado:jsRepEvaluacion.grado, tblRepEvaluacion_grupo:jsRepEvaluacion.grupo
            };
        //------------------------- Hacemos la llamada -------------------------
        cargarLoading();                                                            //$('html, pnlPrincipalWorkArea').css("cursor", "wait"); 
        $.ajax({url:"../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:true
        })
        .done(function(result){
            switch(result.returnCase){
                case 1:
                        initTablaTblRepEvaluacion (result.tblRepEvaluacion, jsRepEvaluacion.grado, jsRepEvaluacion.grupo);
                        tabla.setSelectedRow ('tblRepEvaluacion', posSelActual);
                        mensaje.RepEvaluacion("FOLIOS_ELIMINADOS");
                        cerrarLoading();
                    break;
                case 0: case -1:
                        cerrarLoading();                                            //$('html, pnlPrincipalWorkArea').css("cursor", "default"); 
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

function sigAntGrupo_RepEvaluacion (datos)
{
    var mensaje = new Mensajes ();
    var tabla = new Tabla();
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();                                                            //$('html, pnlPrincipalWorkArea').css("cursor", "wait"); 
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
                    jsRepEvaluacion.grado = result.tblPrincipal_grado;
                    jsRepEvaluacion.grupo = result.tblPrincipal_grupo;
                    initTablaTblRepEvaluacion (result.tblRepEvaluacion, result.tblPrincipal_grado, result.tblPrincipal_grupo);
                    cerrarLoading();
                break;
            case 0: case -1:
                    cerrarLoading();                                            //$('html, pnlPrincipalWorkArea').css("cursor", "default"); 
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

function txtSelHasta_Exit()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var numfilas = tabla.getNumRows("tblRepEvaluacion");
    var inicio= document.getElementById("txtSelDesde").value;
    var fin = document.getElementById("txtSelHasta").value;   
    
    if(inicio==="" || fin==="" || parseInt(inicio)<=0 || parseInt(fin)===0 || parseInt(inicio)>parseInt(fin) || fin<parseInt(inicio) || parseInt(fin)>numfilas){
        mensaje.RepEvaluacion("RANGO_IMPRESION");
        $("txtSelDesde").val("");
        $("txtSelHasta").val("");
    }else{
        document.getElementById("chkSelTodos").checked = false;
        if (parseInt(inicio)===1 && parseInt(fin)===numfilas)
            document.getElementById("chkSelTodos").checked = true;
        
        $(".coltblRepEvaluacion_col0 input:checkbox").attr('checked', false);
        for(var i=inicio; i<=fin; i++)
            document.getElementById('tblRepEvaluacion_chk_f'+(i-1)+'_c0').checked = true;
    }
}

function chkSelTodos_Click()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows("tblRepEvaluacion");
    var chkSelTodos_isSelected = document.getElementById("chkSelTodos").checked;
    
    if(chkSelTodos_isSelected){
        for (var i=0; i<numFilas; i++)
            document.getElementById('tblRepEvaluacion_chk_f'+(i)+'_c0').checked = chkSelTodos_isSelected;
        $("#txtSelDesde").val(1);
        $("#txtSelHasta").val(numFilas);
    }else {
        $(".coltblRepEvaluacion_col0 input:checkbox").attr('checked', chkSelTodos_isSelected);
        $("#txtSelDesde").val("");
        $("#txtSelHasta").val("");
    }
}

function ValNumero(Control)
{
    Control.value=esNumeroEntero(Control.value);
}

function getIdalusAImprimir()
{
    var tabla = new Tabla();
    var numFilas = tabla.getNumRows("tblRepEvaluacion");
    var checkboxes = $(".coltblRepEvaluacion_col0 input:checkbox");
    
    var idAlumnos = new Array();    
    for(var i=0; i<numFilas;i++)
    {
        if(checkboxes[i].checked===true)
            idAlumnos.push($("#tblRepEvaluacion_td_f"+i+"_c1").text());        
    }
    return idAlumnos;
}


