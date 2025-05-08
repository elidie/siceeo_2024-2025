/* 
    Creado el : 20/02/2016, 02:59:50 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

/*
 * 
 * @param {JSON} executeFunction Enviar un JSON con clave: onCreate, onFormActivate, onClose y como valor la clase o función a ejecutar. NOTA: Hasta ahoirita sólo el onClose está implementado.
 * @returns {undefined}
 */

var jsAvisos;
function mwffAvisos_Show (executeFunction)
{
    jsAvisos = {
        executeFunction: executeFunction
    };
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    if (typeof sisVars.avisos !== "undefined" && typeof sisVars.avisos.mensaje !== "undefined" && sisVars.avisos.mensaje !== ""){
        object_setVisible (true,"mwfmAvisos");
        mwffAvisos_Create (executeFunction);
        mwffAvisos_FormActivate (sisVars.avisos.mensaje, sisVars.avisos.icono); //comentado hoy 31-01-2024        
    }
}

function mwffAvisos_Close ()
{
    $('#mwfmAvisos #mwffAvisos').remove();
    object_setVisible (false,"mwfmAvisos");
    $("#txtBusquedaCCT").focus();
    
    //Limpiamos la variable para que no vuelva a mostrar el mensaje
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    sisVars.avisos.mensaje = "";
    sisVars.avisos.icono = "";
    delete sisVars.avisos["mensaje"];
    delete sisVars.avisos["icono"];
    delete sisVars["avisos"];
    sessionStorage.sistemVars = JSON.stringify(sisVars);
    
    if (jsAvisos.executeFunction !== null && typeof jsAvisos.executeFunction !== "undefined" && typeof jsAvisos.executeFunction.onClose !== "undefined")
        jsAvisos.executeFunction.onClose();
    
    jsAvisos = null;
}

function mwffAvisos_Create ()
{
    modalWindow_Create ("Avisos", "", mwffAvisos_Close, 90, {anchoAutoajustable:true});
    $("#mwfpAvisos").append('<div id="pnlAvisos"></div>');
    $("#mwffAvisos").on("focusout", function(){ $('#mwffAvisos').focus(); });   //Para que el foco se mantega siempre en el formulario
    $("#mwffAvisos").focus();
}

function mwffAvisos_FormActivate (mensaje, icono)
{
    $("#pnlAvisos").html(mensaje);
}