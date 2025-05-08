/* 
    Creado el : 20/01/2016, 08:38:30 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsCaDeTa;

function frmwCamDTaller_Show_ (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_modalidad, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo,tblPrincipal_cveprograma)
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
        tblPrincipal_cveprograma: tblPrincipal_cveprograma,
        hayCambios: false,
        catEdt: new Array(),
        catAmbito: new Array(),
        tblCamDTaller_cvemat_oldvalue: new Array(),
        tblClubOax: new Array(),
        tblClubCct: new Array()
    };
    
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwCamDTaller").css("display", "block");                               // Mostramos el formulario correspondiente
    
    frmwCamDTaller_Create (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo);
    frmwCamDTaller_FormActivate ();
}

function frmwCamDTaller_Close_()
{
    jsCaDeTa=null;
    irAVentanaPrincipal ();
}

function frmwCamDTaller_Create_ (tblPrincipal_cct, tblPrincipal_grado, tblPrincipal_grupo)
{
    if($('#frmfCamDTaller').length)
        $('#frmfCamDTaller').remove();
    $('#frmwCamDTaller').append('<fieldset id="frmfCamDTaller"><legend>Asignación de Autonomia Curricular</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfCamDTaller").append('<div id="pnlAreaTrabCamDTaller"></div>');
            
            $("#pnlAreaTrabCamDTaller").append('<div id="pnlCamDTaller"></div>');
            
                $("#pnlCamDTaller").append('<div id="pnlDatosGenerales"></div>');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_cct">'+tblPrincipal_cct+'</label> ');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grado">'+tblPrincipal_grado+'</label> ');
                    $('#pnlDatosGenerales').append('<label id="lblTblPrincipal_grupo">'+tblPrincipal_grupo+'</label>');
                $('#pnlCamDTaller').append('<ul class="buttonBar">'+                                                            
                                                            '<li><a href="#" id="btnAnteriorGpo" title="Grupo Anterior"><label class="iconBtnGpoAnt iconBtnRedondo icon-arrow-left4"></label><label class="alinearHoriz">Gpo. anterior</label></a></li>'+
                                                            '<li><a href="#" id="btnSiguienteGpo" title="Siguiente grupo"><label class="alinearHoriz">Siguiente gpo.</label><label class="iconBtnGpoSig iconBtnRedondo icon-arrow-right4"></label></a></li>'+
                                                      '</ul>');    
                $('#pnlCamDTaller').append('<div id="pnlTblCamDTaller"> <div id="scrlCamDTaller" class="scrollTable"></div> </div>');                

                $("#pnlCamDTaller").append('<ul class="buttonBar"> '
                                            +'<li><a href="#" id="btnInsertNvoClub"><label class="icon-insertar"></label> Insertar Nuevo CLUB</a></li>'                                            
                                        +'</ul>');    
                                        
            $("#pnlAreaTrabCamDTaller").append('<div id="frmAreaTrabClubAgregar"></div>');                
                    $("#frmAreaTrabClubAgregar").append('<div id="pnlInsertNvoClub"></div>');
                
                    
            $("#pnlAreaTrabCamDTaller").append('<div id="pnlAsignarClubAlumno"></div>');                  
     
                
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwCamDTaller_Close(); });
    //$("#btnGuardar").on("keydown", function(e){ if(e.which === 13){ btnGuardarCamDTaller_ActionPerformed (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    //$("#btnGuardar").on("click", function(){ btnGuardarCamDTaller_ActionPerformed (); /*e.preventDefault();*/ } );
    $("#btnInsertNvoClub").on("keydown", function(e){ if(e.which === 13){ frmwAgregarNvoClub_Create (); e.preventDefault(); } });  //El preventDefault es porque en el método btnGuardar_... cuando toca mostrar un alert, se manda a llamar nuevamente el método a travéz del evento click
    $("#btnInsertNvoClub").on("click", function(){ frmwAgregarNvoClub_Create (); /*e.preventDefault();*/ } );    
    //$("#btnAsignarClubAlumno").on("click", function(){ btnInsertClubAlumno_ActionPerformed (); /*e.preventDefault();*/ } );
    $('#btnAnteriorGpo').on('click', function(event){ btnAnteriorGpo_camDTaller_Click();  event.preventDefault(); });
    $('#btnSiguienteGpo').on('click', function(event){ btnSiguienteGpo_camDTaller_Click();  event.preventDefault();  });
}


function frmwAgregarNvoClub_Create (){    

    var tabla = new Tabla();    
    var tblcct = new Tabla();    
    
    if($('#pnlTrabNvoClub').length)
        $('#pnlTrabNvoClub').remove();    
    
    $("#pnlInsertNvoClub").append('<div id="pnlTrabNvoClub"></div>');
        $("#pnlTrabNvoClub").append('<div id="frmXClubOax-Ct"></div>');        
            $("#frmXClubOax-Ct").append('<div id="pnlTblClubOaxaca"></div>');
                $("#pnlTblClubOaxaca").append('<div class="txtClTitulo"><label>Catálogo de clubes estatales</label></div>');
                $("#pnlTblClubOaxaca").append('<div class="frmSelAmbOax"><label>Elija el ambito</label><select id="selAmbOax"></select></div>');
                    for (var i=0; i<jsCaDeTa.catAmbito.length; i++) {
                        if(jsCaDeTa.catAmbito[i].cvetipmat==='AC1')
                            $("#selAmbOax").append('<option value='+jsCaDeTa.catAmbito[i].cvetipmat+' selected>( '+jsCaDeTa.catAmbito[i].cvetipmat+' ) '+jsCaDeTa.catAmbito[i].destipmat+'</option>');
                        else
                            $("#selAmbOax").append('<option value='+jsCaDeTa.catAmbito[i].cvetipmat+'>( '+jsCaDeTa.catAmbito[i].cvetipmat+' ) '+jsCaDeTa.catAmbito[i].destipmat+'</option>');
                    }
                $("#pnlTblClubOaxaca").append('<div id="scrllTblClubOax" class="scrollTable"></div>');
                    tabla.create("scrllTblClubOax","tblClubOax",jsCaDeTa.tblClubOax, ["Clave","Nombre Club"], ["cvemat","desmat"], ["",""], ["",""], true, null, null, null);

            $("#frmXClubOax-Ct").append('<div id="pnlBtnParaAgregar"></div>');    
                $("#pnlBtnParaAgregar").append('<ul class="buttonBar"> '
                                            +'<li><a href="#" id="btnAddClubCt">Asignar club a escuela <label class="iconBtnAddClubCt iconBtnRedondo icon-arrow-right4"></label></a></li>   '                                            
                                        +'</ul>');            
            $("#frmXClubOax-Ct").append('<div id="pnlTblCctAgregadas"></div>');
                $("#pnlTblCctAgregadas").append('<div class="txtClTitulo"><label>Clubes que imparte la escuela</label></div>');
                $("#pnlTblCctAgregadas").append('<div id="scrllTblCctAgregadas" class="scrollTable"></div>');    
                    tblcct.create("scrllTblCctAgregadas","tblClubCct",jsCaDeTa.tblClubCct, ["cveTipoMat","cveMateria","desMateria"], ["cvetipmat","cvemat","desmat"], ["","",""], ["","",""], true, null, null, null);
                
                $("#pnlTblCctAgregadas").append('<div class="txtClTitulo"><label>Crear nuevo club y asignarlo a la escuela</label></div>');    
                $("#pnlTblCctAgregadas").append('<div class="frmInsClub"><label>Seleccionar ambito</label><select id="selAmbitos"></select></div>');
                    for (var i=0; i<jsCaDeTa.catAmbito.length; i++)                 
                        $("#selAmbitos").append('<option value='+jsCaDeTa.catAmbito[i].cvetipmat+'>'+jsCaDeTa.catAmbito[i].destipmat+'</option>');                  
                $("#pnlTblCctAgregadas").append('<div class="frmInsClub"><label>Nuevo nombre del club</label><input type="text" size="45" id="txtClubNvo" placeholder="Nombre del club" style="text-transform:uppercase;"></input></div>');    
                $("#pnlTblCctAgregadas").append('<div class="frmInsClub"><ul class="buttonBar btnAlinear btnInsert"> '
                                                +'<li><a href="#" id="btnInsertClubOax"><label class="icon-insertar"></label> Crear y asignar a la escuela</a></li>   '                                            
                                            +'</ul></div>');                      
                                  
    $("#btnInsertClubOax").on("click",function(){ btnInsertClubOax_ActionPerformed(); });  
    $("#btnAddClubCt").on("click",function(){ btnAddClubCt_ActionPerformed(); });  
    $("#selAmbOax").on( "change", function(){ 
        selAmbOax_ActionPerformed();
    });
    
}
function btnAddClubCt_ActionPerformed(){
    var tabla = new Tabla(), filasSelec;
    var mensaje = new Mensajes ();
        
    filasSelec = tabla.getSelectedRows("tblClubOax",["cvetipmat","cvemat"]);   
    
    if(filasSelec.length > 0) {         
        var datos = {modulo:"CaDeTa", metodo:"btnAddClCt", tblClubOax: filasSelec, tblPrincipal_cct: jsCaDeTa.tblPrincipal_cct,
        tblPrincipal_cicescini: jsCaDeTa.tblPrincipal_cicescini, tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, 
        tblPrincipal_cveplan:jsCaDeTa.tblPrincipal_cveplan, tblPrincipal_modalidad:jsCaDeTa.tblPrincipal_modalidad, tblPrincipal_grupo:jsCaDeTa.tblPrincipal_grupo};
        
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
                        mensaje.General("GUARDADO_EXITOSO", "", ""); 
                        
                        jsCaDeTa.tblClubOax = result.tblClubOax;
                        jsCaDeTa.tblClubCct = result.tblClubCct;
                        btnInsertClubAlumno_ActionPerformed(); 
                        frmwAgregarNvoClub_Create ();
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

    } else
        mensaje.Principal("SELEC","club");
}

function selAmbOax_ActionPerformed(){
    var mensaje = new Mensajes ();
    var cvetipmat = $("#selAmbOax").val();
    var datos = {modulo:"CaDeTa", metodo:"selAmbOax", cvetipmat:cvetipmat};
    var tabla = new Tabla ();
    //------------------------- Hacemos la llamada -------------------------//
    cargarLoading();
    $.ajax({
       url:"../sis_web/siS1",
       type:"POST",
       dataType:"JSON",
       data: datos,
       async: true
    })
    .done(function(result){
        switch(result.returnCase) {
            case 1:              
                    tabla.create("scrllTblClubOax","tblClubOax",result.tblClubOax, ["Clave","Nombre Club"], ["cvemat","desmat"], ["",""], ["",""], true, null, null, null);
                    cerrarLoading();
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    cerrarLoading();
                break;
            case -10:                    
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

function btnInsertClubOax_ActionPerformed() {
    var mensaje = new Mensajes ();
    
    if ($("#txtClubNvo").val().trim() === ""){       
        mensaje.General("GENERAL", "¡PRECAUCIÓN! " +"\n\nDebe de ingresar un nombre de club.");
        $("#txtClubNvo").focus();  return false;
    }
    else {
        var txtClubOax = $("#txtClubNvo").val().trim();
        var cvetipmat = $("#selAmbitos option:selected").val();
        
        var datos = {modulo:"CaDeTa", metodo:"btnInClOax", tblPrincipal_cct: jsCaDeTa.tblPrincipal_cct,tblPrincipal_cicescini: jsCaDeTa.tblPrincipal_cicescini, 
            tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeTa.tblPrincipal_grupo, 
            tblPrincipal_cveplan:jsCaDeTa.tblPrincipal_cveplan, tblPrincipal_modalidad:jsCaDeTa.tblPrincipal_modalidad, txtClubOax: txtClubOax, txtcvetipmat: cvetipmat };
  
        //------------------------- Hacemos la llamada -------------------------//
        cargarLoading();
        $.ajax({
           url:"../sis_web/siS1",
           type:"POST",
           dataType:"JSON",
           data: datos,
           async: true
        })
        .done(function(result){
            switch(result.returnCase) {
                case 1:
                        $('#lblAvisoGuard').text("Proceso realizado con éxito.");
                        jsCaDeTa.tblClubOax = result.tblClubOax;
                        jsCaDeTa.tblClubCct = result.tblClubCct;
                        btnInsertClubAlumno_ActionPerformed(); 
                        frmwAgregarNvoClub_Create ();
                        cerrarLoading();
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        cerrarLoading();
                    break;
                case -10:                    
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

function btnAgregarClub_ActionPerformed()
{
    var mensaje = new Mensajes();
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();

    
    var tblCamDTaller = tabla.getSelectedRow("tblCamDTaller",["idalu"],null,"JSON");    
    var posSelActual = tabla.getSelectedIndexRow ('tblCamDTaller');
    
    //------------------ Establecemos los datos a enviar -------------------
    if( posSelActual >= 0 ) {
        var datos = {
            modulo:"CaDeTa", metodo:"foAc", tipoUsuario: sisVars.tipo_usuario, tblPrincipal_cct: jsCaDeTa.tblPrincipal_cct,tblPrincipal_cicescini: jsCaDeTa.tblPrincipal_cicescini,
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
        .done(function(result) {
            switch(result.returnCase) {
                case 1:
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
    

function frmwCamDTaller_FormActivate_ ()
{
    var mensaje = new Mensajes();    
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeTa", metodo:"foAc", tipoUsuario: sisVars.tipo_usuario, tblPrincipal_cct: jsCaDeTa.tblPrincipal_cct,
        tblPrincipal_cicescini: jsCaDeTa.tblPrincipal_cicescini,tblPrincipal_cveplan: jsCaDeTa.tblPrincipal_cveplan,
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
                    jsCaDeTa.catEdt = result.catEdt;
                    jsCaDeTa.catAmbito = result.catAmbito;
                    jsCaDeTa.tblClubOax = result.tblClubOax;
                    jsCaDeTa.tblClubCct = result.tblClubCct;
                    frmwCamDTaller_InitData(result);                    
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

function frmwCamDTaller_InitData_(result){
    var tabla = new Tabla();
    
    $('#lblTblPrincipal_cct').text(jsCaDeTa.tblPrincipal_cct);
    $('#lblTblPrincipal_grado').text(jsCaDeTa.tblPrincipal_grado);
    $('#lblTblPrincipal_grupo').text(jsCaDeTa.tblPrincipal_grupo);
    
    tabla.create("scrlCamDTaller","tblCamDTaller",result.tblCamDTaller, ["idalu","CURP","Nombre completo"],
        ["idalu","curp","nom_tot"], ["","",""], ["","",""], true, null, 
        function(index){        
            btnInsertClubAlumno_ActionPerformed();
        }, null);
    
    tabla.setSelectedRow ('tblCamDTaller', 0);
    btnInsertClubAlumno_ActionPerformed();    
}

function btnInsertClubAlumno_ActionPerformed_(){
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();

    
    var tblCamDTaller = tabla.getSelectedRow("tblCamDTaller",["idalu"],null,"JSON");    //
    var posSelActual = tabla.getSelectedIndexRow ('tblCamDTaller');

    if( posSelActual >= 0 ) {

        var datos = {  modulo:"CaDeTa", metodo:"btInCl_Ac", tipoUsuario: sisVars.tipo_usuario, tblCamDTaller_idalu: tblCamDTaller.idalu, 
                tblPrincipal_cicescin:jsCaDeTa.tblPrincipal_cicescini, tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaDeTa.tblPrincipal_cveplan, 
                tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeTa.tblPrincipal_grupo
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
                        if($("#pnlAsigCbAlumno").length)
                            $("#pnlAsigCbAlumno").remove();
                        
                        $("#pnlAsignarClubAlumno").append('<div id="pnlAsigCbAlumno" ></div>');               
                            $("#pnlAsigCbAlumno").append('<div class="txtClTitulo"><label>Clubes asignados al alumno</label></div> ');
                            $("#pnlAsigCbAlumno").append('<div id="pnlTblAsigCbAlumno" ><div id="scrollAsigCbAlumno" class="scrollTable"></div></div>');
                                tabla.create("scrollAsigCbAlumno","tblClubAlumno",result.tblClubAlumno, ["cveTipoMat","cveMateria","desMateria"], ["cvetipmat","cvemat","desmat"], ["","",""], ["","",""], true, null, null, null);                        

                            $("#pnlAsigCbAlumno").append('<div><label>Seleccionar club</label><select id="selAmbitosAlu" ></select></div>');
                                for (var i=0; i<result.tblClubCct.length; i++) {                                    
                                        $("#selAmbitosAlu").append('<option value="'+result.tblClubCct[i].cvetipmat+','+result.tblClubCct[i].cvemat+'">'+result.tblClubCct[i].desmat+'</option>');
                                }
                            $("#pnlAsigCbAlumno").append('<ul class="buttonBar"> '
                                                +'<li><a href="#" id="btnGuardarClubAlu"><label class="icon-insertar"></label> Asignar y Guardar </a></li> '
                                            +'</ul>'); 
                                                            
                        $("#btnGuardarClubAlu").on("click",function(){ btnGuardarClubAlu_ActionPerformed(); });  
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

function btnGuardarClubAlu_ActionPerformed(){
    var mensaje = new Mensajes ();    
    var tabla = new Tabla();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tblCamDTaller = tabla.getSelectedRow("tblCamDTaller",["idalu"],null,"JSON");    
    var posSelActual = tabla.getSelectedIndexRow ('tblCamDTaller');
    
    var club = $("#selAmbitosAlu").val();
    var numFilas = tabla.getNumRows('tblCamDTaller');
    
    if( posSelActual >= 0 ) {

        var datos = {  modulo:"CaDeTa", metodo:"btGuClAl", tipoUsuario: sisVars.usuario, tblCamDTaller_idalu: tblCamDTaller.idalu, cveclub:club,
                tblPrincipal_cicescini:jsCaDeTa.tblPrincipal_cicescini, tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaDeTa.tblPrincipal_cveplan, 
                tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, tblPrincipal_modalidad:jsCaDeTa.tblPrincipal_modalidad
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
                        mensaje.General("GUARDADO_EXITOSO", "", ""); 
                        tabla.create("scrollAsigCbAlumno","tblClubAlumno",result.tblClubAlumno, ["cveTipoMat","cveMateria","desMateria"], ["cvetipmat","cvemat","desmat"], ["","",""], ["","",""], true, null, null, null);
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
    } else if(numFilas > 0)
            mensaje.CamDeTaller("NO_SELEC","alumno","mostrar los clubes.");
      else   
            mensaje.CamDeTaller("SIN_DATO","grupos");
}


function btnGuardarCamDTaller_ActionPerformed_ ()
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

function btnInsertTallCamDTaller_ActionPerformed_ ()
{
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"CaDeTa", metodo:"btInNvoClub_AcPe", tblPrincipal_cicescini:jsCaDeTa.tblPrincipal_cicescini, tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, 
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
                    
                    //frmwCamDTaller_FormActivate ();
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

function btnAnteriorGpo_camDTaller_Click_()
{
    var tabla = new Tabla();
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    
    if ( posSelActual-1 >= 0 ){
        var datos = { 
            modulo:"CaDeTa", metodo:"btAnGp", tblPrincipal:tabla.getTable("tblPrincipal",["grado","grupo"]), 
            tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaDeTa.tblPrincipal_cveplan,
            tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeTa.tblPrincipal_grupo, 
            posSelActual:posSelActual, cicescini:jsCaDeTa.tblPrincipal_cicescini
        };
        
        sigAntGrupo_camDTaller (datos);
    }
}

function btnSiguienteGpo_camDTaller_Click_ ()
{
    var tabla = new Tabla();
    
    var posSelActual = tabla.getSelectedIndexRow ('tblPrincipal');
    var numFilas = tabla.getNumRows('tblPrincipal');
    
    if ( posSelActual+1 <  numFilas){
        var datos = {
            modulo:"CaDeTa", metodo:"btSiGp", tblPrincipal:tabla.getTable("tblPrincipal",["grado","grupo"]), 
            tblPrincipal_idcct:jsCaDeTa.tblPrincipal_idcct, tblPrincipal_cveplan:jsCaDeTa.tblPrincipal_cveplan, 
            tblPrincipal_grado:jsCaDeTa.tblPrincipal_grado, tblPrincipal_grupo:jsCaDeTa.tblPrincipal_grupo, 
            posSelActual:posSelActual, cicescini:jsCaDeTa.tblPrincipal_cicescini            
        };

        sigAntGrupo_camDTaller (datos);
    }
}

function sigAntGrupo_camDTaller_ (datos)
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
                    jsCaDeTa.tblPrincipal_grado = result.tblPrincipal_grado;
                    jsCaDeTa.tblPrincipal_grupo = result.tblPrincipal_grupo;                    
                    frmwCamDTaller_InitData(result);
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

