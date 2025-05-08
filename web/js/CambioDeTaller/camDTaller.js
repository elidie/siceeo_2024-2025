/* 
    Creado el : 20/01/2016, 08:38:30 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaDeTa;

function frmwCamDTaller_Show (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_modalidad, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo)
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    jsCaDeTa = {
        tblPrincipal_cicescini: tblPrincipal_cicescini,
        tblPrincipal_idcct: tblPrincipal_idcct,
        tblPrincipal_cct: tblPrincipal_cct,
        tblPrincipal_modalidad: tblPrincipal_modalidad,
        tblPrincipal_cveplan: tblPrincipal_cveplan,
        tblPrincipal_grado:tblPrincipal_grado,
        tblPrincipal_grupo: tblPrincipal_grupo,
        
        hayCambios: false,
        catEdt: new Array(),
        tblCamDTaller_cvemat_oldvalue: new Array()
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwCamDTaller").css("display", "block");                               // Mostramos el formulario correspondiente
    
    frmwCamDTaller_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo);
    frmwCamDTaller_FormActivate ();
}

function frmwCamDTaller_Close()
{
    jsCaDeTa=null;
    irAVentanaPrincipal ();
}

function frmwCamDTaller_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo)
{
    if($('#frmfCamDTaller').length)
        $('#frmfCamDTaller').remove();
    $('#frmwCamDTaller').append('<fieldset id="frmfCamDTaller"><legend>Cambio de Tecnología</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCamDTaller").append('<div id="pnlCamDTaller"></div>');
        
        $("#pnlCamDTaller").append('<div id="pnlDatosGenerales"></div>');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+tblPrincipal_cct+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+tblPrincipal_grado+'</label> ');
            $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+tblPrincipal_grupo+'</label>');
            
        $('#pnlCamDTaller').append('<div id="pnlTblCamDTaller">  <div id="scrlCamDTaller" class="scrollTable"></div>  </div>');
        
        $("#pnlCamDTaller").append('<ul class="buttonBar"> '
                                    +'<li><a href="#" id="btnInsertTall"><label class="icon-insertar"></label> Insertar Tecnología default</a></li>   '
                                    +'<li><a href="#" id="btnGuardar"><label class="iconBtnGuardar icon-disquete"></label>Guardar Cambios</a></li>'
                                +'</ul>'
                                );
                        
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwCamDTaller_Close(); });
    $("#btnGuardar").on("keydown", function(e){ if(e.which === 13){ btnGuardarCamDTaller_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnGuardar").on("click", function(){ btnGuardarCamDTaller_ActionPerformed (); /*e.preventDefault();*/ } );
    $("#btnInsertTall").on("keydown", function(e){ if(e.which === 13){ btnInsertTallCamDTaller_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnInsertTall").on("click", function(){ btnInsertTallCamDTaller_ActionPerformed (); /*e.preventDefault();*/ } );
}

function frmwCamDTaller_FormActivate ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeTa", metodo:"foAc", tipoUsuario: sisVars.tipo_usuario, tblPrincipal_cicescini:jsCaDeTa.tblPrincipal_cicescini, 
        tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeTa.tblPrincipal_grupo
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
                    tabla.create("scrlCamDTaller","tblCamDTaller",result.tblCamDTaller, ["idalu","CURP","Nombre completo","cvemat", "desmat"], ["idalu","curp","nom_tot","cvemat", "desmat"], ["","","","combobox", ""], ["","","","center", ""], true, null, null, 
                            function (f,c){
                                $(".tblCamDTaller_cvemat_f"+f).on( "change", function(){ 
                                    jsCaDeTa.hayCambios = true;
                                    var desfase = $("#frmwCamDTaller #tblCamDTaller_cbx_f"+f+"_c3 option:disabled").length;  //Si hay un item en el combo que esté deshabilitado
                                    var catEdt_selIndex = $("#frmwCamDTaller #tblCamDTaller_cbx_f"+f+"_c3 option:selected").index();
                                    $("#frmwCamDTaller #tblCamDTaller_td_f"+f+"_c4").html(jsCaDeTa.catEdt[catEdt_selIndex-desfase].desmat);
                                });
                            }
                        );
                    jsCaDeTa.catEdt = result.catEdt;
                    //jsCaDeTa.catEdt.unshift({cvemat:"", desmat:""});       //Agregamos al principio un renglón en blanco
                    /*********** Insertamos el catálogo para los combos de taller y establecemos el dato que trae el muchito ***********/
                    $("#frmwCamDTaller .coltblCamDTaller_col3 select option:selected").remove();                                            //Limpiamos todos los combos de discapacidad
                    //$("#frmwCamDTaller .coltblCamDTaller_col3 select").append("<option value=''></option>");                        //Agregamos una opción en blanco
                    for (var i=0; i<result.catEdt.length; i++)                                                                                   //Insertamos el catálogo de discapacidades en todos los combos correspendientes
                        $("#frmwCamDTaller .coltblCamDTaller_col3 select").append("<option value='"+result.catEdt[i].cvemat+"' title='"+result.catEdt[i].cvemat+' - '+result.catEdt[i].desmat+"'>"+result.catEdt[i].cvemat+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                    for (var i=0; i<result.tblCamDTaller.length; i++)//A cada muchito le seleccionamos su dato de discapacidad en su combo correspondienet
                    {
                        if ($("#frmwCamDTaller #tblCamDTaller_cbx_f"+i+"_c3 option[value='"+result.tblCamDTaller[i].cvemat+"']").length>0){  //Checamos si la clave está en el combo
                            //$("#frmwCamDTaller #tblCamDTaller_cbx_f"+i+"_c3 option:contains('"+result.tblCamDTaller[i].cvemat+"')").attr('selected', true); //Para seleccionar según texto que ve el usuario
                            $("#frmwCamDTaller #tblCamDTaller_cbx_f"+i+"_c3").val(result.tblCamDTaller[i].cvemat); //Para seleccionar según el valor
                        }else{                                                  //Si no está, la agregamos al combo, pero deshabilitamos que pueda seleccionarla
                            $("#frmwCamDTaller #tblCamDTaller_cbx_f"+i+"_c3").prepend("<option disabled value='"+result.tblCamDTaller[i].cvemat+"' >"+result.tblCamDTaller[i].cvemat+"</option>");     //lista de grupos en ese grado de ese centro de trabajo
                            $("#frmwCamDTaller #tblCamDTaller_cbx_f"+i+"_c3").val(result.tblCamDTaller[i].cvemat); //Para seleccionar según el valor
                        }
                        jsCaDeTa.tblCamDTaller_cvemat_oldvalue[result.tblCamDTaller[i].idalu]={cvemat:result.tblCamDTaller[i].cvemat};  //Resgurdamos las cvemat originales
                    }
                    
                    boton_setEnabled (result.btnInsertTall_Enabled, "btnInsertTall");
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

function btnGuardarCamDTaller_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var tblCamDTaller, nuevosCvemat=new Array();
    
    if (jsCaDeTa.hayCambios)
    {
        //--------- Extraemos únicamente las claves que cambiaron ------------\\
        tblCamDTaller = tabla.getTable("tblCamDTaller",["idalu","cvemat"],null,"JSON");
        for (var i=0, j=0; i<tblCamDTaller.length; i++)
            if (tblCamDTaller[i].cvemat!==jsCaDeTa.tblCamDTaller_cvemat_oldvalue[tblCamDTaller[i].idalu].cvemat)
                nuevosCvemat[j++]=tblCamDTaller[i].idalu+"~"+jsCaDeTa.tblCamDTaller_cvemat_oldvalue[tblCamDTaller[i].idalu].cvemat+"~"+tblCamDTaller[i].cvemat;
        //\\------------------------------------------------------------------//
        if (nuevosCvemat.length > 0)
        {
            //------------------ Establecemos los datos a enviar -------------------
            var datos = {
                modulo:"CaDeTa", metodo:"btGuCaDTa_AcPe", tblCamDTaller:nuevosCvemat, tblPrincipal_cicescini:jsCaDeTa.tblPrincipal_cicescini
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

function btnInsertTallCamDTaller_ActionPerformed ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeTa", metodo:"btInTa_AcPe", tblPrincipal_cicescini:jsCaDeTa.tblPrincipal_cicescini, tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, 
        tblPrincipal_modalidad:jsCaDeTa.tblPrincipal_modalidad, tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeTa.tblPrincipal_grupo, 
        txtUsuario:sisVars.usuario
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
                    frmwCamDTaller_FormActivate ();
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

