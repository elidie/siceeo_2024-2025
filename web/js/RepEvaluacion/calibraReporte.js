var jsCalibRep;

$(document).on("ready",inicio);

function inicio()
{
    var jsRepEvaluacion = JSON.parse( sessionStorage.getItem("jsRepEvaluacion") );
    
    jsCalibRep = {
        flAyuda:1,
        canvas:null,
        ctx:null,
        
        margenIzq:20,
        margenSup:30,
        incX:20,
        incY:10,
        heightFont:5,
        
        itemAnterior:-1,
        idAnterior:0,
        txtAnterior:"",
        elementosReporte:jsRepEvaluacion.elementosReporte,
        
        configImpre: jsRepEvaluacion.configImpre,
        cicescini:jsRepEvaluacion.cicescini,
        cveplan:jsRepEvaluacion.cveplan,
        cct:jsRepEvaluacion.cct,
        nombrecct:jsRepEvaluacion.nombrecct,
        modalidad: jsRepEvaluacion.modalidad,
        idcct:jsRepEvaluacion.idcct,
        grado: jsRepEvaluacion.grado,
        grupo:jsRepEvaluacion.grupo,
        desTurno:jsRepEvaluacion.desTurno
    };
    
	compruebaSession();
	dibujaHojaYPanelDeEtiquetas();
    
    $("#pnlListaDeCoordenadas").append("<div class='pnlCambiarCoordenadas'>"
                                            +"<input id='txtNewX' class='coordCambios' type='text' size='2' value='' placeholder='H'>"
                                            +"<input id='txtNewY' class='coordCambios' type='text' size='2' value='' placeholder='V'>"
                                            +"<input id='btnCambiarCoordenada' class='estiloBoton' type='button' value='Cambiar'>"
                                        +"</div>");
                        
	$("#btnCambiarCoordenada").on("click",btnCambiarCoordenada_Click);
	$("#btnGuardarCambios").on("click", function(e) { btnGuardarCambios_Click(e); e.preventDefault();});
    $("#btnMostrarAyuda").on("click",btnMostrarAyuda_Click);

	$("#pnlListaDeCoordenadas").css('border','1px solid lightgray');
	$("#pnlListaDeCoordenadas").css('box-shadow','7px 7px 3px 3px rgba(0,0,0,0.1)');
    
	$("#pnlAyuda #textoControles").on("click", function(e) { efectoTitilarAPanel('pnlBotonesRegillaYGuardar') });
	$("#pnlAyuda #textoCaidas").on("click", function(e) { efectoTitilarAPanel('pnlHoja') });
	$("#pnlAyuda #textoCoordenadas").on("click", function(e) { efectoTitilarAPanel('pnlListaDeCoordenadas') });
	$("#btnCerrar_calibraRepEval").on("click",cierraModulo);
    
    $("#lblTblPrincipal_grado").text('Grado: '+jsCalibRep.grado);
	$("#pnlAyuda").hide();
    $("#pnlMouseCoords").hide();
}

function btnImprimirRejilla_Click()
{
    var datos="idcct="+jsCalibRep.idcct+"&tipoImpresion=2";
    window.open('../../jsp/RepEvaluacion/imprimirRepEvaluacion.jsp?'+datos,'_blank'); 
}

function btnImprimirCaidaDeTextosPrueba_Click()
{
    var datos = "idcct="+jsCalibRep.idcct+"&modalidad="+jsCalibRep.modalidad+"&cveplan="+jsCalibRep.cveplan+"&grado="+jsCalibRep.grado
            +"&grupo="+jsCalibRep.grupo+"&cveturno="+jsCalibRep.cveturno+"&nombrecct="+jsCalibRep.nombrecct
            +"&cct="+jsCalibRep.cct+"&configImpre="+jsCalibRep.configImpre+"&tipoImpresion=1&cicescini="+jsCalibRep.cicescini;
    window.open('../../jsp/RepEvaluacion/imprimirRepEvaluacion.jsp?'+datos,'_blank');
}

function btnMostrarAyuda_Click()
{
	if(jsCalibRep.flAyuda){
		$('#pnlAyuda').fadeIn(300);      // desvanecimiento en 300ms
		jsCalibRep.flAyuda = 0;
	}
	else{
		$('#pnlAyuda').fadeOut('slow');  // utilizar una definición de velocidad interna	
		jsCalibRep.flAyuda = 1;
	}
}

function btnCambiarCoordenada_Click()
{
    var x = $("#txtNewX").val();
	var y = $("#txtNewY").val();
	console.log('[x] ='+x+', [y] ='+y);
	//var xy = new Array();
	jsCalibRep.ctx.font = "10px arial";
    
	//**************************** nuevasCoordenadas ***************************
    var xyPix = new Array();
	$("#xp"+jsCalibRep.itemAnterior).val( x );
	$("#yp"+jsCalibRep.itemAnterior).val( y );

	xyPix = coordenadaAPixel(x,y);

	$("#txtCoordX"+jsCalibRep.itemAnterior).val( xyPix[0] );
	$("#txtCoordY"+jsCalibRep.itemAnterior).val( xyPix[1] );
	jsCalibRep.txtAnterior = getTextEtiquetaCoordSel();

    /*$("#txtNewX").val('');
	$("#txtNewY").val('');*/

	//****************************** rePaintLienzo ******************************
    var jsRepEvaluacion = JSON.parse( sessionStorage.getItem("jsRepEvaluacion") );
	var arrDatos = new Array();
	var cadena = "";
	//----- Extraemos las nuevas coordenadas generadas
	$('#listaDeCoordenadas li').each(function(indice, elemento) {
	  cadena = $(elemento).attr('id')+'-'+$(elemento).text().trim()+'-'+$("#txtCoordX"+indice).val()+'-'+$("#txtCoordY"+indice).val();
	  arrDatos[indice] = cadena;
	});
	// remplaza las nuevas coordenadas de elementos a la variable sessionStorage
    jsCalibRep.elementosReporte = arrDatos;
	jsRepEvaluacion.elementosReporte = arrDatos;
    sessionStorage.jsRepEvaluacion = JSON.stringify(jsRepEvaluacion);
	$("#btnImprimirCaidaDeTextosPrueba").addClass("botonDeshabilitado");
	$("#btnImprimirCaidaDeTextosPrueba").attr("disabled",true);
	$("#btnImprimirCaidaDeTextosPrueba").css("background",'lightgray');
    $("#btnImprimirCaidaDeTextosPrueba").css("color",'gray');
	dibujaHojaYPanelDeEtiquetas();
    //************** como se eliminó el panel de coordenadas, volvemos a resaltar la etiqueta que estamos trabajando **************
    $("#xp"+jsCalibRep.itemAnterior).css("background",'rgba(12,200,89,0.6)');
    $("#yp"+jsCalibRep.itemAnterior).css("background",'rgba(12,200,89,0.6)');
    //*****************************************************************************************************************************
}

function btnGuardarCambios_Click(event)
{
    var mensaje = new Mensajes ();
	var arrDatos = new Array();
	var cadena = "";
	//var xypp = new Array();
	$('#listaDeCoordenadas li').each(function(indice, elemento) {
	  cadena = $(elemento).attr('id')+'-'+$("#txtCoordX"+indice).val()+'-'+$("#txtCoordY"+indice).val();                  
	  arrDatos[indice] = cadena;
	});
	$("#btnImprimirCaidaDeTextosPrueba").removeClass("botonDeshabilitado");
	$("#btnImprimirCaidaDeTextosPrueba").attr("disabled",false);
	$("#btnImprimirCaidaDeTextosPrueba").css("background",'#FF8E00');
    $("#btnImprimirCaidaDeTextosPrueba").css("color",'white');

    var datos = {
        modulo:"caCaDeReEv", metodo:"guDaDeCa", coordenadas:arrDatos, idcct:jsCalibRep.idcct, modalidad:jsCalibRep.modalidad, 
        cveplan:jsCalibRep.cveplan, grado:jsCalibRep.grado, configImpre:jsCalibRep.configImpre
    };
    $("body").css("cursor","wait");
    $.ajax({url:"../../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        $("body").css("cursor","default");
        switch(result.returnCase){
            case 1:
                    mensaje.General("GUARDADO_EXITOSO");
                    event.preventDefault();         //Si es necesario, mientras se muestre un alert y la llamada venga sólo del evento click
                break;
            case 0: case -1:
                    alert(result.tipoMensaje +"\n\n"+result.mensaje);
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

function cierraModulo()
{
    jsCalibRep = null;
	window.close();
}

function dibujaHojaYPanelDeEtiquetas()
{
    var w = mmToPixel(167);
    var h = mmToPixel(210);
    $("#hoja").css('width',w+'px');
    $("#hoja").css('height',h+'px');
    $("#hoja").css('border','1px solid lightgray');
    $("#hoja").css('box-shadow','7px 7px 3px 3px rgba(0,0,0,0.1)');
    dibujaHojaDeCaidas(w,h);
    dibujaPanelDeEtiquetasSeleccionables();
}

function dibujaHojaDeCaidas(w,h)
{
    var largo=760, acho=620;
    
	// pintando cuadricula
	if($('#micanvas').length)
		$('#micanvas').remove();

	$("#hoja").append('<canvas height="'+h+'px" width="'+w+'px" id="micanvas"></canvas>');
	jsCalibRep.canvas = document.getElementById("micanvas");
	jsCalibRep.ctx = jsCalibRep.canvas.getContext("2d");
	//pix = 30.5;
	//pix = 14.7899;
	//--------------------------- Lineas Horizontales ---------------------------
    var textV = new Array('[ V ]','V','E','R','T','I','C','A','L');
	var ii = 370;
	for(var i = 0; i<textV.length; i++) {
		if(i === 0)
			jsCalibRep.ctx.fillText(textV[i],0,ii-10);
		else
			jsCalibRep.ctx.fillText(textV[i],5,ii);
		ii=ii+15;
	}
    jsCalibRep.ctx.fillText("0", jsCalibRep.margenIzq+2, jsCalibRep.margenSup);             //Escribe los números del lado izquierdo
	for (var y=jsCalibRep.margenSup, numero = 0; y<=largo; y+=jsCalibRep.incY, numero++){
        if (numero>0){
            jsCalibRep.ctx.fillText(numero, jsCalibRep.margenIzq, y+4);                     //Escribe los números del lado izquierdo
            jsCalibRep.ctx.moveTo(jsCalibRep.margenIzq+15, y);
            jsCalibRep.ctx.lineTo(acho, y);                                                 //Líneas horizontales (de izq a der)
        }
	}
    
    //--------------------------- Lineas Verticales ----------------------------
    jsCalibRep.ctx.fillText('[ H ]   H O R I Z O N T A L',250, jsCalibRep.margenSup-10);
    var numero = 0;
	for (var x=jsCalibRep.margenIzq*2, numero=1; x<=acho; x+=jsCalibRep.incX, numero++){     //Antes 60
        jsCalibRep.ctx.fillText(numero,x-5, jsCalibRep.margenSup-2);                         //Escribe el texto en la parte superior
		jsCalibRep.ctx.moveTo(x, jsCalibRep.margenSup+5);
		jsCalibRep.ctx.lineTo(x, largo);                                                    //Líneas verticales (arriba a abajo)
	}

	jsCalibRep.ctx.strokeStyle = "#c0c0c0";
	jsCalibRep.ctx.stroke();
	jsCalibRep.ctx.font = "10px arial";
	jsCalibRep.canvas.onmousemove = myCoord;
}

function dibujaPanelDeEtiquetasSeleccionables ()
{
	var datos = jsCalibRep.elementosReporte;
	var dat = "";
	var numDatos = datos.length;
	var xypp =  new Array();

	$('#listaDeCoordenadas').empty();
    //----------------------- Asigna los nuevos valores de coordenadas en el panel de selección de etiquetas de coordenadas ------------------
	for (var i = 0; i < numDatos; i++){
		dat = datos[i].split("-");
        
		xypp = pixelACoordenada(dat[2],dat[3]);
		$("#listaDeCoordenadas").append("<li id="+dat[0]+" onClick='lblEtiquetaCoord_Click("+dat[0]+","+i+");' title='"+dat[4]+"'>"
                                +"<input class='inputCoordenadas' type='text' id='xp"+i+"' value='"+xypp[0]+"' readonly/>"
                                +"<input class='inputCoordenadas' type='text' id='yp"+i+"' value='"+xypp[1]+"' readonly/>"
                                +"<input class= 'inputOculto' type='text' id='txtCoordX"+i+"' value='"+dat[2]+"' />"
                                +"<input class= 'inputOculto' type='text' id='txtCoordY"+i+"' value='"+dat[3]+"' />"+dat[1]
                        +"</li>");
	}

    pintaEtiquetasEnCoordenadas(numDatos,datos);
}

function pintaEtiquetasEnCoordenadas(num,datos)
{
	var dat = "";
	for (var i = 0; i < num; i++){
		dat = datos[i].split("-");
        
        if(jsCalibRep.txtAnterior === dat[1])
            jsCalibRep.ctx.fillStyle="#ff0000";
        else
            jsCalibRep.ctx.fillStyle="#5e5e5e";

        jsCalibRep.ctx.fillText(dat[1],dat[2],dat[3]);
	}
}

function compruebaSession()
{
	var storage;
	try {
	    if(localStorage.getItem){
	        storage = localStorage; // Este Navegador Soporta Storage
	    }

	} catch(e) {
    	storage = {}; // Este navegador no soporta storage usar Amplify.js (Investigar)
    	alert('Este navegador no tiene soporte para poder calibrar las caidas de los reportes');
	}
}

function myCoord(e)
{
	$("#xPosition").text("X:["+e.pageX+"]");
	$("#yPosition").text(":["+e.pageY+"]");
}

function pixelACoordenada(pixelX,pixelY)
{
	//console.log('[X] ='+x+', [Y] ='+y);
	var coordenadasXY = new Array();
	var coordenadaX = (pixelX/jsCalibRep.incX);
	var coordenadaY = (pixelY/jsCalibRep.incY);
	//console.log('[1] ='+uno+', [2] ='+dos);
	if(Math.floor(coordenadaX) === coordenadaX)                                 //Es entero?
		coordenadasXY[0] =  coordenadaX - 1;
	else
		coordenadasXY[0] = restarAFloatanteUnEntero(coordenadaX,1);


	if(Math.floor(coordenadaY) === coordenadaY)                                 //Es entero?
		coordenadasXY[1] =  coordenadaY - 3;
	else
		coordenadasXY[1] = restarAFloatanteUnEntero(coordenadaY,3);

	return coordenadasXY;
}

function restarAFloatanteUnEntero(minuendo,sustraendo)
{
	// a es el decimal, b es el entero
	var minuendoPartido = new Array();
	var resta = "";
	minuendoPartido = minuendo.toString().split(".");
	minuendoPartido[0] = minuendoPartido[0]-sustraendo;
	resta = minuendoPartido.toString().replace(/,/g, '.');
	return resta;
}

function mmToPixel(mm)
{
	return mm * 3.779527559;
}

// Funciones llamadas por eventos --------------------------------------------------
function lblEtiquetaCoord_Click(id,i)
{
	
	if(jsCalibRep.itemAnterior === -1){
		jsCalibRep.itemAnterior = i;
		jsCalibRep.idAnterior = id;
		$("#xp"+i).css("background",'rgba(12,200,89,0.6)');
		$("#yp"+i).css("background",'rgba(12,200,89,0.6)');
	}
	else{
		$("#xp"+jsCalibRep.itemAnterior).css("background",'white');
		$("#yp"+jsCalibRep.itemAnterior).css("background",'white');

		$("#xp"+i).css("background",'rgba(12,200,89,0.6)');
		$("#yp"+i).css("background",'rgba(12,200,89,0.6)');
		jsCalibRep.itemAnterior = i;
		jsCalibRep.idAnterior = id;
	}
    
    /*********** RESALTAMOS LA ETIQUETA EN LA HOJA DE COORDENADAS **************/
    var w = mmToPixel(170);
    var h = mmToPixel(215);
    dibujaHojaDeCaidas(w,h);
    
    var datos = jsCalibRep.elementosReporte;
    var dat = datos[i].split("-");
    jsCalibRep.txtAnterior = dat[1];
    pintaEtiquetasEnCoordenadas(datos.length,datos);
    /***************************************************************************/

	//tomarValoresXYpp
    $("#txtNewX").val( $("#xp"+i).val() );
	$("#txtNewY").val( $("#yp"+i).val() );
	$("#txtNewX").focus();
}

function getTextEtiquetaCoordSel()
{
	var dat = "";
	var numDatos = jsCalibRep.elementosReporte.length;
	var etiqueta = "";
	for (var i = 0; i < numDatos; i++){
		dat = jsCalibRep.elementosReporte[i].split("-");
		if(dat[0] == jsCalibRep.idAnterior){
			etiqueta = dat[1];
		 	i = numDatos;
		}
	}
	return etiqueta;
}

function coordenadaAPixel(coordenadaX,coordenadaY)
{
	var coordenadas = new Array();
	coordenadas[0] = (coordenadaX * jsCalibRep.incX)+jsCalibRep.margenIzq;
	coordenadas[1] = (coordenadaY * jsCalibRep.incY)+jsCalibRep.margenSup;
	//console.log('[coordenada[0]] ='+coordenadas[0]+', [coordenada[1]] ='+coordenadas[1]);
	return coordenadas;
}

function efectoTitilarAPanel(nombrePanel){
    $('#'+nombrePanel).fadeOut('slow');                                      // utilizar una definición de velocidad interna
	$('#'+nombrePanel).fadeIn(300);                                          // desvanecimiento en 300ms
}

//********************************************************************************************************************************
//**************************************************** Funciones fuera de uso ****************************************************
//********************************************************************************************************************************

function pixelTomm(pix){
	return redondeo2decimales(pix / 3.779527559);
}
function redondeo2decimales(numero) {
	var original = parseFloat(numero);
	var result = Math.round(original * 100) / 100;
	return result;
}

function rect(x,y,w,h){
	jsCalibRep.ctx.beginPath();
	jsCalibRep.ctx.rect(x,y,w,h);
	jsCalibRep.ctx.closePath();
	jsCalibRep.ctx.fill();
}



