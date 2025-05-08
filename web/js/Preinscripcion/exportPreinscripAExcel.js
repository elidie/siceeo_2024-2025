/* 
    Creado el : 21/12/2015, 01:01:35 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

$(document).on("ready",inicioDePreinscripcionAExcel);

function inicioDePreinscripcionAExcel ()
{
    var dataPr = JSON.parse( sessionStorage.getItem("datosPreinscripcion"));
    //var datosSesion = JSON.parse( sessionStorage.getItem("tblAlumCapCalif"));
    //if (datosSesion.llamada===0)
        getDatosPreinscripcionToExport (dataPr.tblPrincipal_idcct, dataPr.tblPrincipal_cct, dataPr.tblPrincipal_nombre, dataPr.tblPrincipal_cveplan, 
            dataPr.tblPrincipal_grado, dataPr.cicesciniPreinsc);
}

function getDatosPreinscripcionToExport (tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_nombre, tblPrincipal_cveplan, tblPrincipal_grado, cicesciniPreinsc)
{
    var mensaje = new Mensajes();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pr", metodo:"gePrPaEx", tblPrincipal_idcct:tblPrincipal_idcct, tblPrincipal_cveplan:tblPrincipal_cveplan, tblPrincipal_grado:tblPrincipal_grado, 
        cicesciniPreinsc:cicesciniPreinsc
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1: 
                    dibujarTablaDePreinscripcion (result.tblPreinscripcion, tblPrincipal_cct, tblPrincipal_nombre, tblPrincipal_grado);

                    $("#divPreinscripAExcel").table2excel({
                        exclude: ".noExl",
                        name: "Excel Document Name",
                        filename: "Preinscripcion G"+tblPrincipal_grado
                    });

                    cerrarLoading();
                    mensaje.General("EXPORTADO","");
                    //window.close();
                break;
            case 0: case -1:
                    cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    window.open('../../cerrarSesion.jsp','_parent');
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    });
}

function dibujarTablaDePreinscripcion (tblPreinscripcion, tblPrincipal_cct, tblPrincipal_nombre, tblPrincipal_grado) 
{
    $('#divPreinscripAExcel').append('<table id="tbl1PreinscripcionToExcel"></table>');
        $('#tbl1PreinscripcionToExcel').append('<tr>  <td colspan="3">Sistema Integral de Control Escolar del Estado de Oaxaca</td> <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        $('#tbl1PreinscripcionToExcel').append('<tr>  <td>'+tblPrincipal_cct+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        $('#tbl1PreinscripcionToExcel').append('<tr>  <td>'+tblPrincipal_nombre+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        $('#tbl1PreinscripcionToExcel').append('<tr>  <td>Grado: '+tblPrincipal_grado+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        $('#tbl1PreinscripcionToExcel').append('<tr>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
    
    $('#divPreinscripAExcel').append('<table id="tbl2PreinscripcionToExcel"></table>');
        $('#tbl2PreinscripcionToExcel').append('<tr>  <td>idalu</td>  <td>CURP</td>  <td>Nombre completo</td>  <td>Carta</td>  <td>Habla Esp.</td>  <td>Otra Leng.</td> <td>Afromexicana</td> </tr>');
        
        $.each(tblPreinscripcion,function(i,fila){
            $('#tbl2PreinscripcionToExcel').append('<tr>  <td>'+fila.idalu+'</td>  <td>'+fila.curp+'</td>  <td>'+fila.nom_tot+'</td>  <td>'+fila.krtacompromiso+'</td>  <td>'+normalizarAcentos(fila.hablaespaniol)+'</td>  <td>'+fila.cveotralengua+'</td> <td>'+fila.etnia+'</td> </tr>');
        });
}
