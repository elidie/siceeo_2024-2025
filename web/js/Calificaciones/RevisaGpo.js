/* 
    Creado el : 17/08/2017, 12:11:51 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsRevisaGpo;

function mwfRevisaGpo_Show(tblPrincipal_selRow)
{
    jsRevisaGpo={
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo
    };
    object_setVisible (true,"mwfmRevisaGpo");                                      // Ocultamos el gridTable
    //$("#mwffRevisaGpo").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfRevisaGpo_Create();
    mwffRevisaGpo_FormActivate ();
    //event.stopPropagation();
}

function mwfRevisaGpo_Close()
{
    jsRevisaGpo=null;
    modalWindow_Close ("RevisaGpo");
    frmwCalifSecXBim_Close();
}

function mwfRevisaGpo_Create ()
{
    modalWindow_Create ("RevisaGpo", "Alumnos que aprobaron más de una vez un grado según estatus \"RE\"",mwfRevisaGpo_Close, 200, {anchoAutoajustable:true});
    $('#mwfpRevisaGpo').append('<div id="pnlRevisaGpo"></div>');
        
        $('#pnlRevisaGpo').append('<label id="lblInstrucciones">Revisar situación del alumno:</label>');
        $('#pnlRevisaGpo').append('<div id="pnlListadoDeAlumnos" class="panel">'
                                    //+ '<label class="tituloPanel">Seleccione los alumnos a desoficializar</label>'
                                    + '<div id="pnlTblAlumnos">  <div id="scrlAlumnos" class="scrollTable"></div>  </div>'
                                +'</div>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
}

function mwffRevisaGpo_FormActivate ()
{
    var tabla = new Tabla();
    var mensaje = new Mensajes ();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"ReGp", metodo:"foAc", califCicEscIn:jsRevisaGpo.tblPrincipal_cicescini, cicescin:jsRevisaGpo.tblPrincipal_cicescini, 
        tblPrincipal_idcct:jsRevisaGpo.tblPrincipal_idcct, tblPrincipal_grado:jsRevisaGpo.tblPrincipal_grado, 
        tblPrincipal_grupo:jsRevisaGpo.tblPrincipal_grupo
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
                    tabla.create("scrlAlumnos","tblAlumnos",result.tblAlumnos, ["Idalu","CURP","Nombre completo","Num. de veces que aprobó 6o","Num. de veces que aprobó 1o","Num. de veces que aprobó 2o"], ["idalu","curp","nom_tot","en6to","en1ro","en2do"], null, null, true, null, null,null);
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
