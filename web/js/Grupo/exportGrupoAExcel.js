/* 
    Creado el : 8/04/2016, 01:27:23 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

$(document).on("ready",inicioDeGrupoAExcel);

function inicioDeGrupoAExcel ()
{
    var datosCamDeGpo = JSON.parse( sessionStorage.getItem("datosCamDeGpo"));
    setDatosCamDeGpoToExport (datosCamDeGpo.tblCamDeGpo, datosCamDeGpo.tblPrincipal_cct, datosCamDeGpo.tblPrincipal_grado);
}

function setDatosCamDeGpoToExport (tblCamDeGpo, tblPrincipal_cct, tblPrincipal_grado)
{
    dibujarTablaDeCamDeGpo(tblCamDeGpo);

    cargarLoading();
    $("#divGrupoAExcel").table2excel({
        exclude: ".noExl",
        name: "Export. del Grupo",
        filename: tblPrincipal_cct+" - G"+tblPrincipal_grado
    });
    cerrarLoading();
}

function dibujarTablaDeCamDeGpo (tblCamDeGpo) 
{
    
    $('#tblGrupoToExcel').append('<tr>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
    
    $('#divGrupoAExcel').append('<table id="tblGrupoToExcel"></table>');
        $('#tblGrupoToExcel').append('<tr>  <td>idcct</td>  <td>idalu</td>  <td>curp</td>  <td>nom_tot</td>  <td>grado</td>  <td>grupo</td>  <td>apepat</td>  <td>apemat</td>  <td>nombre</td>  <td>cicescini</td>  <td>estatusalu</td>  <td>fecnac</td>  <td>sexo</td>  <td>rep</td>  <td>promedio</td>  <td>promediogral</td>  <td>estatusgrado</td>  <td>cveprograma</td>  <td>cvedefsuf</td>  <td>desdefsuf</td>  <td>probem</td>  </tr>');
        
        $.each(tblCamDeGpo,function(i,fila){
            $('#tblGrupoToExcel').append('<tr>  <td>'+fila.idcct+'</td>  <td>'+fila.idalu+'</td>  <td>'+fila.curp+'</td>  <td>'+fila.nom_tot+'</td>  <td>'+fila.grado+'</td>  <td>'+fila.grupo+'</td>  <td>'+fila.apepat+'</td>  <td>'+fila.apemat+'</td>  <td>'+fila.nombre+'</td>  <td>'+fila.cicescini+'</td>  <td>'+fila.estatusalu+'</td>  <td>'+fila.fecnac+'</td>  <td>'+fila.sexo+'</td>  <td>'+fila.rep+'</td>  <td>'+fila.promedio+'</td>  <td>'+fila.promediogral+'</td>  <td>'+fila.estatusgrado+'</td>  <td>'+fila.cveprograma+'</td>  <td>'+fila.cvedefsuf+'</td>  <td>'+fila.desdefsuf+'</td>  <td>'+fila.probem+'</td>  </tr>');
        });
}


