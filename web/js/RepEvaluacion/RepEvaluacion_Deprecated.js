/* 
    Creado el : 3/09/2015, 08:34:32 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/
var jsRepEvaluacion;
function frmfRepEvaluacion_Show(tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_modalidad, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cicescini, tblPrincipal_nombre, tblPrincipal_cveturno, tblPrincipal_cveplan, tblPrincipal_cveprograma) 
{
    var tabla = new Tabla ();
    jsRepEvaluacion = {
        idcct: tblPrincipal_idcct,
        cct: tblPrincipal_cct,
        modalidad: tblPrincipal_modalidad,
        grado: tblPrincipal_grado,
        grupo: tblPrincipal_grupo,
        cicescini: tblPrincipal_cicescini,
        nombrecct: tblPrincipal_nombre,
        turno: tblPrincipal_cveturno,
        nombreEscuela:"",
        desTurno: "",
        cveplan: tblPrincipal_cveplan,
        cveprograma: tblPrincipal_cveprograma,

        Califcicescin: tblPrincipal_cicescini,
        idTabla: tabla.getSelectedIndexRow("tblPrincipal"),
        tblGruposImp: null,
        configsImpre: null,
        configImpre:"",
        elementosReporte: null,
        arrayIdAlu: null
    };
    
    /****** Se ocultan los contenedores correspondiente *******/
    object_setVisible (false,"gridTable");
    $("#frmwRepEvaluacion").css("display", "block");
    
    frmfRepEvaluacion_FormActivate();
    
}

function frmwRepEvaluacion_Close()
{
    jsRepEvaluacion=null;
    irAVentanaPrincipal ();
}

function frmfRepEvaluacion_Create(cct,grado,grupo)
{    var totalAlumnosGrupo = 0;
    if($('#frmfRepEvaluacion').length) // Verificando si la tabla Existe
        $('#frmfRepEvaluacion').remove();
    $('#frmwRepEvaluacion').append('<fieldset id="frmfRepEvaluacion"><legend>Grupos para Impresión del Reporte de Evaluación</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfRepEvaluacion").append('<div id="pnlRepEvaluacion"></div>');
        
        $('#pnlRepEvaluacion').append('<table id="tableGrupo"></table>');            
            $('#tableGrupo').append("<tr><th class='estiloEncabezados' colspan='5' style='background: rgba(133,178,0,1); font-size: 1.3em; color:white;'>"+cct+" - "+grado+" - "+grupo+"</th></tr>");                
            var listar ="<tr><th colspan='5'><label>Configuraciones: </label></div><select id='selectConfig'>";                
            if(jsRepEvaluacion.configsImpre.length > 0) {
                $.each(jsRepEvaluacion.configsImpre,function(indicg,valorg) {                                                                
                    listar+="<option value='"+valorg[0]+"'>"+valorg[1]+"</option>";
                });  
            }
            else
                listar+="<option value='0'>Config Impre 1</option>";                    
            listar+="</select><input id='btnCalibCaidaRepEvaluacion' tabindex='12' type='button' value='Editar caídas de texto...' title='Permite establecer la coordenadas de dónde va a imprimirse cada dato(texto) en la hoja de Reporte de Evaluación'/>"
                            +"<label>De: </label><input id='txtSelDesde' tabindex='' type='number' value='' size='1px'/>"
                            +"<label>Hasta: </label><input id='txtSelHasta' tabindex='' type='text' size='1px' value=''/>"
                            +"<input id='btnImprimirRepEvaluacion' tabindex='12' type='button' value='Vista previa' title='Muestra vista previa con los datos de los alumnos seleccionados.'/><img style='margin-left: 100px;' title='Total de Alumnos' src='../imagenes/main/cambio-usuario_32.png'/><label id='lblTotalAlus'>Total:</label><input type='hidden' id='pree' value=''/><label id='pre'></label><div class='menuNavGrupo'><a href='#' id='btnGrupoAnterior' tabindex='16'><img src='../imagenes/main/GrupoAnt.png' title='Grupo Anterior' ></a><div class='lblTextog'><label>Grupo</label></div><a href='#' id='btnSiguienteGrupo' tabindex='17'><img src='../imagenes/main/GrupoSig.png' title='Siguiente grupo' ></a></div></th></tr>";
            $('#tableGrupo').append(listar);
            $('#tableGrupo').append("<tr><th><input type='hidden' id='modulo' value='2'><input name='chkSelTodos' id='chkSelTodos'  type='checkbox'/></th><th>IDALU</th><th>CURP</th><th>NOMBRE COMPLETO</th></tr>");

            if(jsRepEvaluacion.tblGruposImp.length > 0) {
                var i = 0;
                $.each(jsRepEvaluacion.tblGruposImp,function(indice,fila) {
                    var np=i+1;                              
                    $("#tableGrupo").append("<tr class='zoom1' id='x"+i+"' name='"+i+"' value='"+i+"'>"
                                +"<td><center>"+np+"&nbsp;<input class='zoom1' type='checkbox' name='seleccionIdaluGrupo'  id='seleccionIdaluGrupo' value='"+i+"'/></center></td>"
                                +"<td><input type='hidden' id='idalu"+i+"' value='"+fila.idalu+"'/>"+fila.idalu+"</td>"
                                +"<td>"+fila.curp+"</td>"
                                +"<td>"+fila.apepat+" "+fila.apemat+" "+fila.nombre+"</td>"
                            +"</tr>");
                    i++;
                    totalAlumnosGrupo = i;
                });
            }
            else
                $("#tableGrupo").append("<tr class='zoom'><td  colspan='5'> <center>No se Encontraron Coincidencias</center></td></tr>");          
            $('#tableGrupo').append("<tr><td colspan='5'><center><label id='mensaje'>SiCEEB no imprime alumnos que no tienen asignado un grupo.</label></center></td></tr>");

    $('#pre').text(totalAlumnosGrupo);    
    $('#pree').val(totalAlumnosGrupo);    
    /*************** cambios eli ******************/
    $('.zoom1').click(function() {                
        if(lock===false) {
            var oID = parseInt($(this).attr("value"));
            var elementos = document.getElementsByName("seleccionIdaluGrupo");

            for(var i=0; i<elementos.length; i++) {
                if(oID === i) { 
                    if(elementos[i].checked) {
                        elementos[i].checked = false;
                        // Despintamos la Linea
                        $("#x"+i).css("background","rgba(255,255,255,1)");
                        $("#x"+i).css("color","rgba(0,0,0,1)");
                    }
                    else {
                        elementos[i].checked = true;
                        // Pintamos la Linea
                        $("#x"+i).css("background", "rgba(97,148,80,0.5)");
                        $("#x"+i).css("color", "rgba(0,0,0,0.4)");
                    }
                }
            }
        } else lock=false;
     });

    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwRepEvaluacion_Close(); });
    
    $('#btnSiguienteGrupo').click(function(event){ btnSiguienteGpo_RepEvaluacion_Click (); event.preventDefault();  });
    $('#btnGrupoAnterior').click(function(event){ btnAnteriorGpo_RepEvaluacion_Click (); event.preventDefault();  });
    $('#chkSelTodos').change(function(event){ chkSelTodos_Click(); event.preventDefault(); });
    $("#txtSelDesde").blur(function(){ return ValNumero(this); });
    $("#txtSelHasta").blur(function(){ return ValNumero(this); });
    $("#txtSelHasta").blur(function(e){ txtSelHasta_Exit(  document.getElementById("pree").value); e.preventDefault();  });
    $("#btnImprimirRepEvaluacion").click(function(e){ btnImprimirRepEvaluacion_Click(); e.preventDefault(); });
    $("#btnCalibCaidaRepEvaluacion").click(function(e){ btnCalibCaidaRepEvaluacion_Click(); e.preventDefault(); });
}

function frmfRepEvaluacion_FormActivate() 
{
    var mensaje = new Mensajes ();
    
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
        alumEstatus = result.alumEstatus;
        switch(result.returnCase){
            case 1:
                    jsRepEvaluacion.tblGruposImp=result.tblGrupoRepEvaluacion;            //lista de grupos en ese grado de ese centro de trabajo
                    jsRepEvaluacion.configsImpre=result.configsImpre;
                    jsRepEvaluacion.desTurno = getDescripcionTurno();

                    //Validamos el nombre del CCT
                    if( jsRepEvaluacion.nombrecct.split(",").length > 0 )
                        jsRepEvaluacion.nombreEscuela = jsRepEvaluacion.nombrecct.replace(",","~");
                    else
                        jsRepEvaluacion.nombreEscuela = jsRepEvaluacion.nombrecct;

                    frmfRepEvaluacion_Create(jsRepEvaluacion.cct,jsRepEvaluacion.grado,jsRepEvaluacion.grupo);
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

function chkSelTodos_Click()
{
    var elementos = document.getElementsByName("seleccionIdaluGrupo");           
    var longi = elementos.length;
    if($("#chkSelTodos").is(':checked'))
    {
        for(var i=0; i<longi; i++)//----------- despintamos toda la fila  -----------/
        {    
            elementos[i].checked = true;
            $("#x"+i).css("background", "rgba(70,130,180,0.5)");
            $("#x"+i).css("color", "rgba(0,0,0,0.4)");
        }
        $("#txtSelDesde").val(1);
        $("#txtSelHasta").val(longi);
    }
    else {
        for(var i=0; i<longi; i++) {    
            elementos[i].checked = false;
            $("#x"+i).css("background","rgba(255,255,255,1)");
            $("#x"+i).css("color","rgba(0,0,0,1)");                
        }
        $("#txtSelDesde").val("");
        $("#txtSelHasta").val("");
    }           
}

function ValNumero(Control)
{
    Control.value=esNumeroEntero(Control.value);
}

function txtSelHasta_Exit(total)
{
    var mensaje = new Mensajes();
    var inicio= document.getElementById("txtSelDesde").value;
    var fin = document.getElementById("txtSelHasta").value;   
    var numero=parseInt(total);        
    
    
    if(inicio==="" || fin==="" || parseInt(inicio)===0 || parseInt(fin)===0 || parseInt(inicio)>parseInt(fin) || fin<parseInt(inicio) || parseInt(fin)>numero){
        mensaje.RepEvaluacion("RANGO_IMPRESION");
        document.getElementById("txtSelDesde").value="";
        document.getElementById("txtSelHasta").value="";
    }else{
        var elementos = document.getElementsByName("seleccionIdaluGrupo");   
        if($("#chkSelTodos").is(':checked'))
            $("#chkSelTodos").attr('checked', false); 
        
        for(var i=0; i<total; i++) {       
            elementos[i].checked = false;
            //----------- despintamos cada fila  -----------/
            $("#x"+i).css("background","rgba(255,255,255,1)");
            $("#x"+i).css("color","rgba(0,0,0,1)");
            if(i>=inicio-1 && i<fin){
                elementos[i].checked = true;
                $("#x"+i).css("background", "rgba(70,130,180,0.5)");
                $("#x"+i).css("color", "rgba(0,0,0,0.4)");                                        
            }
        }  
    }
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
                            jsRepEvaluacion.configImpre = $('#selectConfig option:selected').text().trim();
                            
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
    jsRepEvaluacion.arrayIdAlu = validarDatosParaImprimir();
    if(jsRepEvaluacion.arrayIdAlu.length)
    {           
        var datos = "idcct="+jsRepEvaluacion.idcct+"&modalidad="+jsRepEvaluacion.modalidad+"&cveplan="+jsRepEvaluacion.cveplan+"&grado="+jsRepEvaluacion.grado
                +"&grupo="+jsRepEvaluacion.grupo+"&turno="+jsRepEvaluacion.desTurno+"&nombrecct="+jsRepEvaluacion.nombreEscuela+"&cct="+jsRepEvaluacion.cct
                +"&cveprograma="+jsRepEvaluacion.cveprograma+"&configImpre="+$("#selectConfig option:selected").text()+"&tipoImpresion=0"
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
                    jsRepEvaluacion.tblGruposImp=result.tblGrupoRepEvaluacion;            //lista de grupos en ese grado de ese centro de trabajo
                    frmfRepEvaluacion_Create(jsRepEvaluacion.cct,jsRepEvaluacion.grado,jsRepEvaluacion.grupo);
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

function validarDatosParaImprimir()
{
    var elementos = document.getElementsByName("seleccionIdaluGrupo");
    var longi = elementos.length;
    var idAlumno = new Array();    
    for(var i=0; i<longi;i++)
    {
        if(elementos[i].checked===true)
            idAlumno.push(document.getElementById("idalu"+i).value);        
    }
    return idAlumno;
}

function getDescripcionTurno()
{
    var desturno="";
    switch(jsRepEvaluacion.turno){
        case '100':desturno="MATUTINO";
            break;
        case '200':desturno="VESPERTINO";
            break;
        case '300':desturno="NOCTURNO";
            break;
        case '400':desturno="DISCONTINUO";                           
            break;
        case '500':desturno="MIXTO";
           break
       default:break;
    }
    return desturno;
}