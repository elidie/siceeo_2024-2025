/* 
    Creado el : 20/01/2016, 08:59:15 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaDeAr;

function frmwCamDArte_Show (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo)
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsCaDeAr = {
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_cct:tblPrincipal_cct,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo: tblPrincipal_grupo,
        
        hayCambios: false,
        catArte: new Array(),
        tblCamDArte_cvemat_oldvalue: new Array()
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwCamDArte").css("display", "block");                                 // Mostramos el formulario correspondiente
    
    frmwCamDArte_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo);
    frmwCamDArte_FormActivate ();
}

function frmwCamDArte_Close()
{
    jsCaDeAr=null;
    irAVentanaPrincipal ();
}

function frmwCamDArte_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo)
{
    if($('#frmfCamDArte').length)
        $('#frmfCamDArte').remove();
    $('#frmwCamDArte').append('<fieldset id="frmfCamDArte"><legend>Cambio de Arte</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCamDArte").append('<div id="pnlCamDArte"></div>');
        
        $("#pnlCamDArte").append('<div id="pnlDatosGenerales"></div>');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+tblPrincipal_cct+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+tblPrincipal_grado+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+tblPrincipal_grupo+'</label>');
            
        $('#pnlCamDArte').append('<div id="pnlTblCamDArte">  <div id="scrlCamDArte" class="scrollTable"></div>  </div>');
        
        $("#pnlCamDArte").append('<div id="pnlBotonesDeControl"></div>');
            $("#pnlBotonesDeControl").append('<ul id="ulbtnInsertArte", class="buttonBar"> <li><a href="#" id="btnInsertArte"><label class="icon-insertar"></label> Insertar Arte por default</a></li> </ul>');
            $("#pnlBotonesDeControl").append('<div id="pnlCambiarTodoElGrupo"></div>');
                $('#pnlCambiarTodoElGrupo').append('<select id="cbxArte" name="cbxArte"></select>');
                $("#pnlCambiarTodoElGrupo").append('<ul class="buttonBar"> <li><a href="#" id="btnCamArte">Cambia el Arte a todo el grupo</a></li> </ul>');
            $("#pnlBotonesDeControl").append('<ul id="ulbtnGuardar" class="buttonBar"> <li><a href="#" id="btnGuardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar Cambios</a></li> </ul>');
                        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwCamDArte_Close(); });
    $("#btnGuardar").on("keydown", function(e){ if(e.which === 13){ btnGuardarCamDArte_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnGuardar").on("click", function(){ btnGuardarCamDArte_ActionPerformed (); /*e.preventDefault();*/ } );
    $("#btnInsertArte").on("keydown", function(e){ if(e.which === 13){ btnInsertArteCamDArte_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnInsertArte").on("click", function(){ btnInsertArteCamDArte_ActionPerformed (); /*e.preventDefault();*/ } );
    $("#btnCamArte").on("keydown", function(e){ if(e.which === 13){ btnCamArte_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnCamArte").on("click", function(){ btnCamArte_ActionPerformed (); /*e.preventDefault();*/ } );
}

function frmwCamDArte_FormActivate ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeAr", metodo:"foAc", tipoUsuario: sisVars.tipo_usuario, tblPrincipal_cicescini:jsCaDeAr.tblPrincipal_cicescini, 
        tblPrincipal_idcct:jsCaDeAr.tblPrincipal_idcct, tblPrincipal_grado:jsCaDeAr.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeAr.tblPrincipal_grupo
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
                    tabla.create("scrlCamDArte","tblCamDArte",result.tblCamDArte, ["idalu","CURP","Nombre completo","cvemat", "desmat"], ["idalu","curp","nom_tot","cvemat", "desmat"], ["","","","combobox", ""], ["","","","center", ""], true, null, null, 
                            function (f,c){
                                $(".tblCamDArte_cvemat_f"+f).on( "change", function(){ 
                                    jsCaDeAr.hayCambios = true;
                                    var desfase = $("#frmwCamDArte #tblCamDArte_cbx_f"+f+"_c3 option:disabled").length;  //Si hay un item en el combo que esté deshabilitado
                                    var catArte_selIndex = $("#frmwCamDArte #tblCamDArte_cbx_f"+f+"_c3 option:selected").index();
                                    $("#frmwCamDArte #tblCamDArte_td_f"+f+"_c4").html(jsCaDeAr.catArte[catArte_selIndex-desfase].desmat);
                                });
                            }
                        );
                    jsCaDeAr.catArte = result.catArte;
                    //jsCaDeAr.catArte.unshift({cvemat:"", desmat:""});           //Agregamos al principio un renglón en blanco
                    /*********** Insertamos el catálogo para los combos de taller y establecemos el dato que trae el muchito ***********/
                    $("#frmwCamDArte .coltblCamDArte_col3 select option:selected").remove();                                            //Limpiamos todos los combos de discapacidad
                    //$("#frmwCamDArte .coltblCamDArte_col3 select").append("<option value=''></option>");                        //Agregamos una opción en blanco
                    for (var i=0; i<result.catArte.length; i++){                                                                                   //Insertamos el catálogo de discapacidades en todos los combos correspendientes
                        $("#frmwCamDArte .coltblCamDArte_col3 select").append("<option value='"+result.catArte[i].cvemat+"' title='"+result.catArte[i].cvemat+' - '+result.catArte[i].desmat+"'>"+result.catArte[i].cvemat+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                        $("#frmwCamDArte #cbxArte").append("<option value='"+result.catArte[i].cvemat+"' title='"+result.catArte[i].cvemat+' - '+result.catArte[i].desmat+"'>"+result.catArte[i].cvemat+"</option>");
                    }
                    
                    //$("#frmwCamDArte #cbxArte").find("option[value='']").remove();//Quitamos el elemento en blanco que se agregó
                    for (var i=0; i<result.tblCamDArte.length; i++)             //A cada muchito le seleccionamos su dato de discapacidad en su combo correspondienet
                    {
                        if ($("#frmwCamDArte #tblCamDArte_cbx_f"+i+"_c3 option[value='"+result.tblCamDArte[i].cvemat+"']").length>0){  //Checamos si la clave está en el combo
                            //$("#frmwCamDArte #tblCamDArte_cbx_f"+i+"_c3 option:contains('"+result.tblCamDArte[i].cvemat+"')").attr('selected', true); //Para seleccionar según texto que ve el usuario
                            $("#frmwCamDArte #tblCamDArte_cbx_f"+i+"_c3").val(result.tblCamDArte[i].cvemat); //Para seleccionar según el valor
                        }else{                                                  //Si no está, la agregamos al combo, pero deshabilitamos que pueda seleccionarla
                            $("#frmwCamDArte #tblCamDArte_cbx_f"+i+"_c3").prepend("<option disabled value='"+result.tblCamDArte[i].cvemat+"' >"+result.tblCamDArte[i].cvemat+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                            $("#frmwCamDArte #tblCamDArte_cbx_f"+i+"_c3").val(result.tblCamDArte[i].cvemat); //Para seleccionar según el valor
                        }
                        
                        jsCaDeAr.tblCamDArte_cvemat_oldvalue[result.tblCamDArte[i].idalu]={cvemat:result.tblCamDArte[i].cvemat};  //Resgurdamos las cvemat originales
                    }
                    
                    boton_setEnabled (result.btnInsertArte_Enabled, "btnInsertArte");
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

function btnGuardarCamDArte_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblCamDArte, nuevosCvemat=new Array();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    if (jsCaDeAr.hayCambios)
    {
        //--------- Extraemos únicamente las claves que cambiaron ------------\\
        tblCamDArte = tabla.getTable("tblCamDArte",["idalu","cvemat"],null,"JSON");
        for (var i=0, j=0; i<tblCamDArte.length; i++)
            if (tblCamDArte[i].cvemat!==jsCaDeAr.tblCamDArte_cvemat_oldvalue[tblCamDArte[i].idalu].cvemat)
                nuevosCvemat[j++]=tblCamDArte[i].idalu+"~"+jsCaDeAr.tblCamDArte_cvemat_oldvalue[tblCamDArte[i].idalu].cvemat+"~"+tblCamDArte[i].cvemat;
        //\\------------------------------------------------------------------//
        if (nuevosCvemat.length > 0)
        {
            //------------------ Establecemos los datos a enviar -------------------
            var datos = {
                modulo:"CaDeAr", metodo:"btGuCaDAr_AcPe", tblCamDArte:nuevosCvemat, tblPrincipal_cicescini:jsCaDeAr.tblPrincipal_cicescini,txtUsuario:sisVars.usuario
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

function btnInsertArteCamDArte_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeAr", metodo:"btInAr_AcPe", tblPrincipal_cicescini:jsCaDeAr.tblPrincipal_cicescini, tblPrincipal_idcct:jsCaDeAr.tblPrincipal_idcct, 
        tblPrincipal_grado:jsCaDeAr.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeAr.tblPrincipal_grupo, txtUsuario:sisVars.usuario
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
                    frmwCamDArte_FormActivate ();
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


function btnCamArte_ActionPerformed ()
{
    $("#frmwCamDArte .coltblCamDArte_col3 select").val($("#frmwCamDArte #cbxArte option:selected").val());
    var catArte_selIndex = $("#frmwCamDArte #tblCamDArte_cbx_f"+0+"_c3 option:selected").index();
    if (catArte_selIndex !==-1 ) {
        $("#frmwCamDArte .coltblCamDArte_col4").html(jsCaDeAr.catArte[catArte_selIndex].desmat);
        jsCaDeAr.hayCambios = true;
    }
}