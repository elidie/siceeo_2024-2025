/* 
    Creado el : 1/06/2017, 10:00:16 AM
    Autor     : Ing. Maai Nolasco Sánchez
*/

$(document).on("ready",inicioDeMantenimiento);

function inicioDeMantenimiento ()
{
    $("#btnVolverALogueo").on('click', function(){ btnVolverALogueo_Click (); });
}

function btnVolverALogueo_Click ()
{
    window.open("logueo.html","_self");
}