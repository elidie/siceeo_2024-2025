/* 
    Creado el : 08-ago-2015, 0:49:29
    Autor     : Ing. Maai Nolasco Sánchez
*/

$(document).on("ready",inicioDeGradoGrupoAExcel);

function inicioDeGradoGrupoAExcel ()
{
    //var datosSesion = JSON.parse( sessionStorage.getItem("tblAlumCapCalif"));
    //if (datosSesion.llamada===0)
        obtenerDatosExcel ();
}

function obtenerDatosExcel ()
{
    
    var mensaje = new Mensajes();
    var tblAlumCapCalif = JSON.parse( sessionStorage.getItem("tblAlumCapCalif"));
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"biXAl", metodo:"exDaEx", tblAlumCapCalif_idalu: tblAlumCapCalif.idalu, tblAlumCapCalif_cicescini:tblAlumCapCalif.cicescini
    };
    
    //------------------------- Hacemos la llamada -------------------------
    //cargarLoading();
    $.ajax({url:"../../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:false
    })
    .done(function(result){
        switch(result.returnCase){
            case 1: 
                dibujarTablaDeExcel (result.tblCalifsBimXAlum, result.tblInasistenciasXAlum, tblAlumCapCalif.tblPrincipal_cct, tblAlumCapCalif.tblPrincipal_nombre, tblAlumCapCalif.grado, tblAlumCapCalif.grupo);
                //tblAlumCapCalif.llamada = 1;
                //sessionStorage.tblAlumCapCalif = JSON.stringify(tblAlumCapCalif);
                //$('#contenidoExcel2').css("display","none");
                
                
                $("#divCalifsToExcel").table2excel({
                    exclude: ".noExl",
                    name: "Excel Document Name",
                    filename: "Calificaciones"
                });
                
                /*window.open('data:application/vnd.ms-excel,' + encodeURIComponent($('#divCalifsToExcel').html()));
                
                var ancho=450, alto=200, posicion_x, posicion_y, idalus, cicescini; 
    
                posicion_x=(screen.width/2)-(ancho/2); 
                posicion_y=(screen.height/2)-(alto/2);
                //window.open("jsp/Calificaciones/GradoGrupoAExcel.jsp?exportToExcel=YES","","status=0, toolbar=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
                //window.open("GradoGrupoAExcel.jsp?exportToExcel=YES","","status=0, toolbar=0, menubar=0, resizable=0, scrollbars=no, width="+ancho+", height="+alto+",left="+posicion_x+",top="+posicion_y);
                //$('#tblCalifsToExcel').remove();
                */
                mensaje.General("EXPORTADO","");
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
function dibujarTablaDeExcel (tblCalifsBimXAlum, tblInasistenciasXAlum, tblPrincipal_cct, tblPrincipal_nombre, tblAlumCapCalif_grado, tblAlumCapCalif_grupo) 
{
    var idaluActual="",idaluAnterior="", inasistencias, huboAlumnos=false;
    //$('#bodisito').append("<%response.setHeader(\"Content-Disposition\", \"attachment;filename=\"miArchivoExcel.xls\"\");%>");
    $('#divCalifsToExcel').append('<table id="tblCalifsToExcel"></table>');
        $('#tblCalifsToExcel').append('<tr>  <td colspan="3">Sistema Integral de Control Escolar del Estado de Oaxaca</td> <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        $('#tblCalifsToExcel').append('<tr>  <td>'+tblPrincipal_cct+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        $('#tblCalifsToExcel').append('<tr>  <td>'+tblPrincipal_nombre+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        $('#tblCalifsToExcel').append('<tr>  <td>Grado: '+tblAlumCapCalif_grado+'</td>  <td>Grupo:'+tblAlumCapCalif_grupo+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
        
        $.each(tblCalifsBimXAlum,function(i,fila){
            idaluActual = fila.idalu;
            
            if (idaluAnterior !== idaluActual) {
                if (i>0)
                    $('#tblCalifsToExcel').append('<tr id="idalu_'+idaluAnterior+'"></tr>');
                
                $('#tblCalifsToExcel').append('<tr>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
                $('#tblCalifsToExcel').append('<tr>  <td> idAlu:</td>  <td>'+fila.idalu+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
                $('#tblCalifsToExcel').append('<tr>  <td>  CURP:</td>  <td>'+fila.curp+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
                $('#tblCalifsToExcel').append('<tr>  <td>Nombre:</td>  <td>'+fila.apepat+'/'+fila.apemat+'*'+fila.nombre+'</td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
                $('#tblCalifsToExcel').append('<tr>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  <td></td>  </tr>');
                
                $('#tblCalifsToExcel').append('<tr>  <td></td>  <td>MATERIA</td>  <td>1erBim(1)</td>  <td>1erBim(2)</td>  <td>2doBim(1)</td>  <td>2doBim(2)</td>  <td>3erBim(1)</td>  <td>3erBim(2)</td> <td>Promedio</td>  </tr>');
                idaluAnterior = idaluActual;
                huboAlumnos = true;
            }
            
            $('#tblCalifsToExcel').append('<tr>  <td></td>  <td>'+normalizarAcentos(fila.desmat)+'</td>  <td>'+(fila.b11==="null"?"0":fila.b11)+'</td>  <td>'+(fila.b12==="null"?"0":fila.b12)+'</td>  <td>'+(fila.b21==="null"?"0":fila.b21)+'</td>  <td>'+(fila.b22==="null"?"0":fila.b22)+'</td>  <td>'+(fila.b31==="null"?"0":fila.b31)+'</td>  <td>'+(fila.b32==="null"?"0":fila.b32)+'</td> <td>'+fila.promedio+'</td>  </tr>');
        });
        
        if (huboAlumnos)                                                        //Este if es porque en el for anterior no alcanza a pintar el último renglón de inasistencias porque se sale antes de pintarlo
            $('#tblCalifsToExcel').append('<tr id="idalu_'+idaluAnterior+'"></tr>');
        
        $.each(tblInasistenciasXAlum,function(i,fila){
            inasistencias = fila;
            $('#idalu_'+i).append('<td></td>  <td>Inasistencias:</td>  <td>'+fila.inst1+'</td>  <td></td>  <td>'+fila.inst2+'</td>  <td></td>  <td>'+fila.inst3+'</td>  <td></td>  <td></td>');
        });
        
}