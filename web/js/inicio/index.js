/* 
    Creado el : 24/04/2015, 02:09:59 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

$(document).on("ready",inicioDeIndex);

function inicioDeIndex (){
    detectarBrowser();
    document.getElementById("btnIniciar").focus();
}

function detectarBrowser ()
{
    var isChrome = window.chrome; 
    if(isChrome)
        $("#navegadorRecomendado").css("display", "none");  // Ocultamos
    else
        $("#navegadorRecomendado").css("display", "");  // Mostramos    
}