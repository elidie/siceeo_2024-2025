/* 
    Creado el : 20/01/2016, 09:00:25 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsDi;

function frmwDiscapa_Show (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo)
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsDi = {
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_cct: tblPrincipal_cct,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo: tblPrincipal_grupo,
        
        hayCambios: false,
        catDiscap: new Array(),
        tblDiscap_cvedefsuf_oldvalue: new Array()
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwDiscapa").css("display", "block");                                  // Mostramos el formulario correspondiente
    
    frmwDiscapa_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo);
    frmwDiscapa_FormActivate ();
}

function frmwDiscapa_Close()
{
    jsDi=null;
    irAVentanaPrincipal ();
}

function frmwDiscapa_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo)
{
    if($('#frmfDiscapa').length)
        $('#frmfDiscapa').remove();
    $('#frmwDiscapa').append('<fieldset id="frmfDiscapa"><legend>Necesidades especiales</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfDiscapa").append('<div id="pnlDiscapa"></div>');
        
        $("#pnlDiscapa").append('<div id="pnlDatosGenerales"></div>');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+tblPrincipal_cct+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+tblPrincipal_grado+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+tblPrincipal_grupo+'</label>');
            
        $('#pnlDiscapa').append('<div id="pnlTblDiscap">  <div id="scrlDiscap" class="scrollTable"></div>  </div>');
        
        $("#pnlDiscapa").append('<ul id="ubtnGuardar" class="buttonBar"> '
                                    +'<li title="Guardar datos"><a href="#" id="btnGuardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar Cambios</a></li>   '
                                +'</ul>'
                                );
                        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwDiscapa_Close(); });
    $("#btnGuardar").on("keydown", function(e){ if(e.which === 13){ btnGuardarDiscap_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnGuardar").on("click", function(){ btnGuardarDiscap_ActionPerformed (); /*e.preventDefault();*/ } );
}

function frmwDiscapa_FormActivate ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Di", metodo:"foAc", tipoUsuario: sisVars.tipo_usuario, tblPrincipal_cicescini:jsDi.tblPrincipal_cicescini, tblPrincipal_idcct:jsDi.tblPrincipal_idcct,
        tblPrincipal_cveplan:jsDi.tblPrincipal_cveplan, tblPrincipal_grado:jsDi.tblPrincipal_grado, tblPrincipal_grupo:jsDi.tblPrincipal_grupo
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
                    /*jsDi.isTodoCalOf = result.isTodoCalOf;*/
                    
                    tabla.create("scrlDiscap","tblDiscap",result.tblDiscap, ["idalu","CURP","Nombre completo","Cve Nec. Esp.", "Nec. Esp."], ["idalu","curp","nom_tot","cvedefsuf", "desdefsuf"], ["","","","combobox", ""], ["","","","center", ""], true, null, null,
                                    function (f,c){
                                        $(".tblDiscap_cvedefsuf_f"+f).on( "change", function(){ 
                                            jsDi.hayCambios = true;
                                            var desfase = $("#frmwDiscapa #tblDiscap_cbx_f"+f+"_c3 option:disabled").length;  //Si hay un item en el combo que esté deshabilitado
                                            var catDiscap_selIndex = $("#frmwDiscapa #tblDiscap_cbx_f"+f+"_c3 option:selected").index();
                                            //$("#frmwDiscapa #tblDiscap_td_f"+f+"_c4").html($("#frmwDiscapa #tblDiscap_cbx_f"+f+"_c3 option:selected").val());
                                            $("#frmwDiscapa #tblDiscap_td_f"+f+"_c4").html(jsDi.catDiscap[catDiscap_selIndex-desfase].desdefsuf);
                                            
                                        });
                                    }
                                );
                    jsDi.catDiscap = result.catDiscap;
                    //jsDi.catDiscap.unshift({cvedefsuf:"", desdefsuf:""});       //Agregamos al principio un renglón en blanco
                    /*********** Insertamos el catálogo para los combos de discapacidad y establecemos el dato que trae el muchito ***********/
                    $("#frmwDiscapa .coltblDiscap_col3 select option:selected").remove();                                            //Limpiamos todos los combos de discapacidad
                    //$("#frmwDiscapa .coltblDiscap_col3 select").append("<option value=''></option>");                        //Agregamos una opción en blanco
                    for (var i=0; i<result.catDiscap.length; i++)                                                                                   //Insertamos el catálogo de discapacidades en todos los combos correspendientes
                        $("#frmwDiscapa .coltblDiscap_col3 select").append("<option value='"+result.catDiscap[i].cvedefsuf+"' title='"+result.catDiscap[i].cvedefsuf+' - '+result.catDiscap[i].desdefsuf+"'>"+result.catDiscap[i].cvedefsuf+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                    for (var i=0; i<result.tblDiscap.length; i++)//A cada muchito le seleccionamos su dato de discapacidad en su combo correspondienet
                    {
                        if ($("#frmwDiscapa #tblDiscap_cbx_f"+i+"_c3 option[value='"+result.tblDiscap[i].cvedefsuf+"']").length>0){  //Checamos si la clave está en el combo
                            //$("#frmwDiscapa #tblDiscap_cbx_f"+i+"_c3 option:contains('"+result.tblDiscap[i].cvedefsuf+"')").attr("selected",true);
                            $("#frmwDiscapa #tblDiscap_cbx_f"+i+"_c3").val(result.tblDiscap[i].cvedefsuf); //Para seleccionar según el valor
                        }else{                                                  //Si no está, la agregamos al combo, pero deshabilitamos que pueda seleccionarla
                            $("#tblDiscap #tblDiscap_cbx_f"+i+"_c3").prepend("<option disabled value='"+result.tblDiscap[i].cvedefsuf+"' >"+result.tblDiscap[i].cvedefsuf+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                            $("#tblDiscap #tblDiscap_cbx_f"+i+"_c3").val(result.tblDiscap[i].cvedefsuf); //Para seleccionar según el valor
                        }
                        jsDi.tblDiscap_cvedefsuf_oldvalue[result.tblDiscap[i].idalu]={cvedefsuf:result.tblDiscap[i].cvedefsuf};  //Resgurdamos las cvedefsuf originales
                    }
                    
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
            case -11: //Mostrar mensaje y salirse del formulario
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
                    frmwDiscapa_Close();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function btnGuardarDiscap_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblDiscap, nuevosCvedefsuf=new Array();
    if (jsDi.hayCambios)
    {
        //--------- Extraemos únicamente las claves que cambiaron ------------\\
        tblDiscap = tabla.getTable("tblDiscap",["idalu","cvedefsuf"],null,"JSON");
        for (var i=0, j=0; i<tblDiscap.length; i++)
            if (tblDiscap[i].cvedefsuf!==jsDi.tblDiscap_cvedefsuf_oldvalue[tblDiscap[i].idalu].cvedefsuf)
                nuevosCvedefsuf[j++]=tblDiscap[i].idalu+"~"+tblDiscap[i].cvedefsuf;
        //\\------------------------------------------------------------------//
        if (nuevosCvedefsuf.length > 0)
        {
            //------------------ Establecemos los datos a enviar -------------------
            var datos = {
                modulo:"Di", metodo:"btGu_AcPe", tblDiscap:nuevosCvedefsuf, tblPrincipal_cicescini:jsDi.tblPrincipal_cicescini
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

