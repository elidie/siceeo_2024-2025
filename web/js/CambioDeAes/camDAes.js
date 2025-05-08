/* 
    Creado el : 20/01/2016, 08:57:51 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaDeAe;

function frmwCamDAes_Show (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo)
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsCaDeAe = {
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_cct:tblPrincipal_cct,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo: tblPrincipal_grupo,
        
        hayCambios: false,
        catAes: new Array(),
        tblCamDAes_cvemat_oldvalue: new Array()
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwCamDAes").css("display", "block");                                  // Mostramos el formulario correspondiente
    
    frmwCamDAes_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo);
    frmwCamDAes_FormActivate ();
}

function frmwCamDAes_Close()
{
    jsCaDeAe=null;
    irAVentanaPrincipal ();
}

function frmwCamDAes_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo)
{
    if($('#frmfCamDAes').length)
        $('#frmfCamDAes').remove();
    $('#frmwCamDAes').append('<fieldset id="frmfCamDAes"><legend>Cambio de Asignatura Estatal</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCamDAes").append('<div id="pnlCamDAes"></div>');
        
        $("#pnlCamDAes").append('<div id="pnlDatosGenerales"></div>');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+tblPrincipal_cct+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+tblPrincipal_grado+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+tblPrincipal_grupo+'</label>');
            
        $('#pnlCamDAes').append('<div id="pnlTblCamDAes">  <div id="scrlCamDAes" class="scrollTable"></div>  </div>');
        
        $("#pnlCamDAes").append('<div id="pnlBotonesDeControl"></div>');
            $("#pnlBotonesDeControl").append('<ul id="ulbtnInsertAes", class="buttonBar"> <li><a href="#" id="btnInsertAes"><label class="icon-insertar"></label> Insertar Asig. Est. por default</a></li> </ul>');
            $("#pnlBotonesDeControl").append('<div id="pnlCambiarTodoElGrupo"></div>');
                $('#pnlCambiarTodoElGrupo').append('<select id="cbxAes" name="cbxAes"></select>');
                $("#pnlCambiarTodoElGrupo").append('<ul class="buttonBar"> <li><a href="#" id="btnCamAes">Cambia la Asig. Est. a todo el grupo</a></li> </ul>');
            $("#pnlBotonesDeControl").append('<ul id="ulbtnGuardar" class="buttonBar"> <li><a href="#" id="btnGuardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar Cambios</a></li> </ul>');
                        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwCamDAes_Close(); });
    $("#btnGuardar").on("keydown", function(e){ if(e.which === 13){ btnGuardarCamDAes_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnGuardar").on("click", function(){ btnGuardarCamDAes_ActionPerformed (); /*e.preventDefault();*/ } );
    $("#btnInsertAes").on("keydown", function(e){ if(e.which === 13){ btnInsertAesCamDAes_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnInsertAes").on("click", function(){ btnInsertAesCamDAes_ActionPerformed (); /*e.preventDefault();*/ } );
    $("#btnCamAes").on("keydown", function(e){ if(e.which === 13){ btnCamAes_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnCamAes").on("click", function(){ btnCamAes_ActionPerformed (); /*e.preventDefault();*/ } );
}

function frmwCamDAes_FormActivate ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeAe", metodo:"foAc", tipoUsuario: sisVars.tipo_usuario, tblPrincipal_cicescini:jsCaDeAe.tblPrincipal_cicescini, 
        tblPrincipal_idcct:jsCaDeAe.tblPrincipal_idcct, tblPrincipal_grado:jsCaDeAe.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeAe.tblPrincipal_grupo
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
                    tabla.create("scrlCamDAes","tblCamDAes",result.tblCamDAes, ["idalu","CURP","Nombre completo","cvemat", "desmat"], ["idalu","curp","nom_tot","cvemat", "desmat"], ["","","","combobox", ""], ["","","","center", ""], true, null, null, 
                            function (f,c){
                                $(".tblCamDAes_cvemat_f"+f).on( "change", function(){ 
                                    jsCaDeAe.hayCambios = true;
                                    var desfase = $("#frmwCamDAes #tblCamDAes_cbx_f"+f+"_c3 option:disabled").length;  //Si hay un item en el combo que esté deshabilitado
                                    var catAes_selIndex = $("#frmwCamDAes #tblCamDAes_cbx_f"+f+"_c3 option:selected").index();
                                    $("#frmwCamDAes #tblCamDAes_td_f"+f+"_c4").html(jsCaDeAe.catAes[catAes_selIndex-desfase].desmat);
                                });
                            }
                        );
                    jsCaDeAe.catAes = result.catAes;
                    //jsCaDeAe.catAes.unshift({cvemat:"", desmat:""});       //Agregamos al principio un renglón en blanco
                    /*********** Insertamos el catálogo para los combos de taller y establecemos el dato que trae el muchito ***********/
                    $("#frmwCamDAes .coltblCamDAes_col3 select option:selected").remove();                                            //Limpiamos todos los combos de discapacidad
                    //$("#frmwCamDAes .coltblCamDAes_col3 select").append("<option value=''></option>");                        //Agregamos una opción en blanco
                    for (var i=0; i<result.catAes.length; i++){                                                                                   //Insertamos el catálogo de discapacidades en todos los combos correspendientes
                        $("#frmwCamDAes .coltblCamDAes_col3 select").append("<option value='"+result.catAes[i].cvemat+"' title='"+result.catAes[i].cvemat+' - '+result.catAes[i].desmat+"'>"+result.catAes[i].cvemat+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                        $("#frmwCamDAes #cbxAes").append("<option value='"+result.catAes[i].cvemat+"' title='"+result.catAes[i].cvemat+' - '+result.catAes[i].desmat+"'>"+result.catAes[i].cvemat+"</option>");
                    }
                    //$("#frmwCamDAes #cbxAes").find("option[value='']").remove();//Quitamos el elemento en blanco que se agregó
                    for (var i=0; i<result.tblCamDAes.length; i++) //A cada muchito le seleccionamos su dato de discapacidad en su combo correspondienet
                    {
                        if ($("#frmwCamDAes #tblCamDAes_cbx_f"+i+"_c3 option[value='"+result.tblCamDAes[i].cvemat+"']").length>0){  //Checamos si la clave está en el combo
                            //$("#frmwCamDAes #tblCamDAes_cbx_f"+i+"_c3 option:contains('"+result.tblCamDAes[i].cvemat+"')").attr('selected', true); //Para seleccionar según texto que ve el usuario
                            $("#frmwCamDAes #tblCamDAes_cbx_f"+i+"_c3").val(result.tblCamDAes[i].cvemat); //Para seleccionar según el valor
                        }else{                                                  //Si no está, la agregamos al combo, pero deshabilitamos que pueda seleccionarla
                            $("#frmwCamDAes #tblCamDAes_cbx_f"+i+"_c3").prepend("<option disabled value='"+result.tblCamDAes[i].cvemat+"' >"+result.tblCamDAes[i].cvemat+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                            $("#frmwCamDAes #tblCamDAes_cbx_f"+i+"_c3").val(result.tblCamDAes[i].cvemat); //Para seleccionar según el valor
                        }
                        jsCaDeAe.tblCamDAes_cvemat_oldvalue[result.tblCamDAes[i].idalu]={cvemat:result.tblCamDAes[i].cvemat};  //Resgurdamos las cvemat originales
                    }
                    
                    boton_setEnabled (result.btnInsertAes_Enabled, "btnInsertAes");
                    boton_setEnabled (result.btnGuardar_Enabled, "btnGuardar");
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
            break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function btnGuardarCamDAes_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblCamDAes, nuevosCvemat=new Array();
    
    if (jsCaDeAe.hayCambios)
    {
        //--------- Extraemos únicamente las claves que cambiaron ------------\\
        tblCamDAes = tabla.getTable("tblCamDAes",["idalu","cvemat"],null,"JSON");
        for (var i=0, j=0; i<tblCamDAes.length; i++)
            if (tblCamDAes[i].cvemat!==jsCaDeAe.tblCamDAes_cvemat_oldvalue[tblCamDAes[i].idalu].cvemat)
                nuevosCvemat[j++]=tblCamDAes[i].idalu+"~"+jsCaDeAe.tblCamDAes_cvemat_oldvalue[tblCamDAes[i].idalu].cvemat+"~"+tblCamDAes[i].cvemat;
        //\\------------------------------------------------------------------//
        if (nuevosCvemat.length > 0)
        {
            //------------------ Establecemos los datos a enviar -------------------
            var datos = {
                modulo:"CaDeAe", metodo:"btGuCaDTa_AcPe", tblCamDAes:nuevosCvemat, tblPrincipal_cicescini:jsCaDeAe.tblPrincipal_cicescini
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
                            mensaje.General("GUARDADO_EXITOSO");
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

function btnInsertAesCamDAes_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeAe", metodo:"btInAe_AcPe", tblPrincipal_cicescini:jsCaDeAe.tblPrincipal_cicescini, tblPrincipal_idcct:jsCaDeAe.tblPrincipal_idcct, 
        tblPrincipal_grado:jsCaDeAe.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeAe.tblPrincipal_grupo, txtUsuario:sisVars.usuario
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
                    cerrarLoading();
                    frmwCamDAes_FormActivate ();
                    //mensaje.General("PROCESO_EXITOSO");
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
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


function btnCamAes_ActionPerformed ()
{
    $("#frmwCamDAes .coltblCamDAes_col3 select").val($("#frmwCamDAes #cbxAes option:selected").val());
    var catAes_selIndex = $("#frmwCamDAes #tblCamDAes_cbx_f"+0+"_c3 option:selected").index();
    if (catAes_selIndex !==-1 ) {
        $("#frmwCamDAes .coltblCamDAes_col4").html(jsCaDeAe.catAes[catAes_selIndex].desmat);
        jsCaDeAe.hayCambios = true;
    }
}