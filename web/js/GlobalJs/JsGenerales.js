/* 
    Creado el : 6/03/2015, 01:42:45 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/
jsJSGenerales = {
    activarConteoDeBloqueo : false,                                             //Si hay subprocesos que vuelven a llamar a cargar y cerrar loading, activando esta variable con true nos ayudará a que hasta que termine el último subproceso permitirá el cerrarloading
    numBlockPantalla:0
};
function resetLoading ()
{
    jsJSGenerales.activarConteoDeBloqueo = false;
    jsJSGenerales.numBlockPantalla=0;
}
function cargarLoading(activarConteo)                                           //LLamar con activarConteo sólo en el proceso padre cuando se desee que tenga efecto el cerrarLoading en el último subproceso
{
    if (typeof activarConteo!=="undefined" && activarConteo) 
        jsJSGenerales.activarConteoDeBloqueo = true;
    
    if (jsJSGenerales.activarConteoDeBloqueo)
        jsJSGenerales.numBlockPantalla++;
    if(!$("#loading").length){
        $('body').append('<div id="loading"><label id="iconLoading" class="icon-cw"></label> </div>');
        //$("#hidloading").focus();
        //$("#hidloading").on("blur",function(){$("#hidloading").focus();});
        //$('#loading').css("cursor","wait");
    }
}

function cerrarLoading()
{
    if (jsJSGenerales.numBlockPantalla>0)
        jsJSGenerales.numBlockPantalla--;
    else 
        jsJSGenerales.activarConteoDeBloqueo=false;
    
    if(jsJSGenerales.numBlockPantalla===0 && $("#loading").length){
        //$('#loading').css("cursor","auto");
        $("#loading").remove();
    }
}

function showProcessing(formActivo) {
    $("#"+formActivo).css("display", "block"); // Mostramos el formulario correspondiente   
    if($('#cargando').length)
          $('#cargando').remove();
    $("#"+formActivo).append('<center><img src="../imagenes/generales/Dai - 2.gif" id="cargando" /></center>');
    $('body').append('<div id="blockWorkArea"></div>');
    //$('#blockWorkArea').css("cursor","wait");
}

function hidePreocessing() {
    //$('#blockWorkArea').css("cursor","pointer");
    if($('#cargando').length){
          $('#cargando').remove();
          $("#blockWorkArea").remove();
      }
}

function showUpObject (nombreObjeto, timeBeforeShow, timeFadeIn, timeDelay, timeFadeOut)
{
    setTimeout(function() {
        //$('.globoayuda_pointup').fadeIn(1500);                                  //$('.globoayuda_pointup').show();
        //setTimeout(function() { $('.globoayuda_pointup').fadeOut(1500); $('.globoayuda_pointup').remove(); },9000);//$('.globoayuda_pointup').delay(10000).hide(600);,    $(“#divActualizarContrato_Success”).hide();  
        $(nombreObjeto).fadeIn(timeFadeIn).delay(timeDelay).fadeOut(timeFadeOut); //1000, 7000, 3000
    },timeBeforeShow); //1500
}

function aparecerGlogoAyuda (nombreObjeto, timeBeforeShow)
{
    showUpObject (nombreObjeto, timeBeforeShow, 1000, 7000, 3000);
}

//Los elementeos en general que aceptan enabled o disabled son: button, input, optgroup, option, select, y textarea.
function object_setEnabled (enabled, nombreObjeto)
{
    if (enabled === true)
        $("#"+nombreObjeto).removeAttr("disabled");
    else
        $("#"+nombreObjeto).attr("disabled", "disabled");
        
}
function object_setVisible (enabled, nombreObjeto)
{
    if (enabled === true)
        $("#"+nombreObjeto).css("display","block");
    else
        $("#"+nombreObjeto).css("display","none");
}

function boton_setEnabled (enabled, nombreObjeto, evento1, funcion1, evento2, funcion2)
{
    if (enabled === true){
        if ( $( '#'+nombreObjeto ).hasClass("btnDesactivado") && typeof evento1!== "undefined" && evento1!== null && typeof funcion1!== "undefined" && funcion1!== null) {
            $( '#'+nombreObjeto ).removeClass("btnDesactivado");
            $('#'+nombreObjeto).on(evento1, funcion1);
            if (typeof evento2!=="undefined" && funcion2!=="undefined")
                $('#'+nombreObjeto).on(evento2, funcion2);
        }
    }else{
        $('#'+nombreObjeto).off();
        if (!$( '#'+nombreObjeto ).hasClass("btnDesactivado"))
            $( '#'+nombreObjeto ).addClass("btnDesactivado");
    }
}

function boton_setVisible (enabled, nombreObjeto)
{
    if (enabled === true)
        $( '#'+nombreObjeto+':parent').css("display","");
    else
        $( '#'+nombreObjeto+':parent').css("display","none");
}
/* aun falta implementar este metodo */
function singleBoton_setEnabled (enabled, nombreObjeto, evento1, funcion1, evento2, funcion2)
{
    if (enabled === true){
        if ( $( '#'+nombreObjeto ).hasClass("btnDesactivado") && typeof evento1!== "undefined" && evento1!== null && typeof funcion1!== "undefined" && funcion1!== null) {
            $( '#'+nombreObjeto ).removeClass("btnDesactivado");
            $('#'+nombreObjeto).on(evento1, funcion1);
            if (typeof evento2!=="undefined" && funcion2!=="undefined")
                $('#'+nombreObjeto).on(evento2, funcion2);
        }
    }else{
        $('#'+nombreObjeto).off();
        if (!$( '#'+nombreObjeto ).hasClass("btnDesactivado"))
            $( '#'+nombreObjeto ).addClass("btnDesactivado");
    }
}

/**
 * Busca un elemento dentro de un arreglo
 * @param {String} windowName Nombre de la ventana con el cual se creará las clases e identificadores
 * @param {String} titleWindow Texto que se mostrará en la barra de título de la ventana.
 * @param {String} closeFunction Función que se ejecutará al primir el botón cerrar.
 * @param {String} tabindexIni Número con el que se empezarán a indexar cada elemnto de la ventana
 * @param {JSON} parametros Están definidos en esta función e indicarán el comportamiento de la ventana.
 */
function modalWindow_Create (windowName, titleWindow, closeFunction, tabindexIni, parametros)
{
    var anchoAutoajustable = "", anchoFijoCentrado="";
    if (parametros!==null && typeof parametros!=="undefined"){
        if (parametros.anchoAutoajustable!==null && typeof parametros.anchoAutoajustable!=="undefined" && parametros.anchoAutoajustable)
            anchoAutoajustable = " mwfForm-AnchoAutoajustable";
        if (parametros.anchoFijoCentrado!==null && typeof parametros.anchoFijoCentrado!=="undefined" && parametros.anchoFijoCentrado)
            anchoFijoCentrado = " mwfForm-AnchoFijoCentrado ";
    }
    
    
    if($('#mwfm'+windowName+' #mwff'+windowName+'').length)
        $('#mwfm'+windowName+' #mwff'+windowName+'').remove();
    $('#mwfm'+windowName+'').append(
            '<div id="mwff'+windowName+'" class="mwfForm'+anchoFijoCentrado+anchoAutoajustable+'" tabindex="'+(tabindexIni++)+'"> '
                +'<div id="mwfb'+windowName+'" class="mwfTitBar">'
                    + ((titleWindow!=="")?'<label id="mwft'+windowName+'" class="mwfTitle">'+titleWindow+'</label>':'')
                    +'<div id="mwfc'+windowName+'" title="Cerrar" tabindex="'+(tabindexIni++)+'" class="mwfButtonClose">'
                        +'<label id="mwfi'+windowName+'" class="mwfIconClose icon-cross2">'
                    +'</div>'
                +'</div>'
                +'<div id="mwfp'+windowName+'" class="mwfPanelForm"></div>'
            +'</div>'
    );
                        
        $('#frmw'+windowName).append('<div id="pnl'+windowName+'"></div>');
    
    $('#mwfc'+windowName).on("click",function(){ closeFunction(); });
    //$('#mwff'+windowName).on("focusout", function(){ $('#mwff'+windowName).focus(); });   //Para que el foco se mantega siempre en el formulario
    //$('#mwff'+windowName).focus();
}

function modalWindow_Close (windowName, deleteSpace)
{
    $('#mwfm'+windowName+' #mwff'+windowName).remove();
    if (typeof deleteSpace === "undefined" || deleteSpace===null)
        object_setVisible (false,"mwfm"+windowName);
    else if (deleteSpace === true)
        $('#mwfm'+windowName).remove();
}

function isFloat (numero)
{
    //var forma1=new RegExp("^[0-9]+$");
    var forma2=new RegExp("^[0-9]+\.$");
    var forma3=new RegExp("^\.[0-9]+$");
    var forma4=new RegExp("^[0-9]+\.[0-9]+$");
    //if (forma1.test(numero) || forma2.test(numero) || forma3.test(numero) || forma4.test(numero))
    if (forma2.test(numero) || forma3.test(numero) || forma4.test(numero))
        return true;
    else 
        return false;                
}

function isFloat1 (numero){
    var forma = new RegExp("/^-?\d*(\.\d+)?$/");
    if(forma.test(numero))
        return true;
    else 
        return false;    
    //return parseFloat(numero.match(/^-?\d*(\.\d+)?$/))>0;
}

function esNumeroEntero(numero)
{
    var forma1=new RegExp("^[0-9]+$");
    return forma1.test(numero);
    /*var numero = parseInt(dato);
    if (isNaN(numero))
        return 1;
    return numero;*/
}
    
var jsonToArray = function(js){
	var arr = new Array();
	for (var prop in js) {
	    arr.push(js[prop]);
	};
	//cambiando los valores null del arreglo a vacio ""
	var lonArr = arr.length;
	for (var i = 0; i < lonArr; i++) {
		if(arr[i] === null)
		 arr[i] = "";
	};

	return arr;
};

/**
 * Busca un elemento dentro de un arreglo
 * @param {Array} array El arreglo de datos
 * @param {String} elementoABuscar El elemnto que se quiere buscar en el arreglo
 * @returns {Number} Retorna la posición en donde se encontró el elemnto, -1 si no lo encuentra;
 */
function indexOf (array, elementoABuscar)
{
    var index=-1;
    if( typeof Array.prototype.indexOf !== "function") {                        // si no tenemos disponible indexOf lo simulamos 
        var arrayTxt = array.join("::");                                        // unión de todo el array 
        var re = new RegExp("^(([^(::)]+::)*)"+elementoABuscar, "");            // expresión: n elementos y luego el nuestro 
        var m = arrayTxt.match(re);                                             // [0]:original; [1]:n elementos;  
        index = m[1].split("::").length-1;                                      // Contamos cuantos elementos hay en [1] 
    }else { 
        index = array.indexOf(elementoABuscar); 
    }  
    return index;
}

function formatoFecha(fechaHora){
    var array = fechaHora.split(':');
    return array[0]+":"+array[1]+" "+(array[2].split(' '))[1];
}

/**
 * Confierte un arreglo de JSON a formato arreglo de Strings separando los datos de columna para cada fila con el caracter ~.
 * Este formato es utilizado para enviar al servidor.
 * @param {type} arrayJSON La tabla en formato arreglo de JSON que se desea convertir
 * @param {type} index Especificar un índice si se quiere que sólo extraiga esa posición de fila, si no, enviar null o indefinido o menor a 0 para que convierta la tabla completa
 * @returns {JSON} Retorna el nombre de las columnas y la tabla en formato arreglo de Strings separando los datos de columna para cada fila con el caracter ~. Los nombres son retornados en la clave 'nameCols', y los datos en la clave 'rows'
 */
function arrayJSON_To_ArrayString (arrayJSON, index)
{
    var datosYColsTabla = { nameCols:null, rows:null };
    var tabla=[], nombreCols="", fila="", i=0, j=0;
    
    if (typeof arrayJSON!=="undefined" && arrayJSON!==null && arrayJSON.length >= 0 )
    {
        if (typeof index!=="undefined" && index!==null && index >= 0){
            tabla[0]="";
            $.each(arrayJSON[index],function(clave,valor) {
                nombreCols += ((i++)===0?"":"~")+clave;
                fila += ((j++)===0?"":"~")+valor;
            });
            tabla[0] = fila;
        }else {
            $.each(arrayJSON,function(f,row) {
                fila =""; j=0;
                $.each(row,function(clave,valor) {
                    if (f===0) { 
                        nombreCols += ((i++)===0?"":"~")+clave;
                    }
                    fila += ((j++)===0?"":"~")+valor;
                });
                tabla[f] = fila;
            });

        }
    }
    
    datosYColsTabla.nameCols=nombreCols;
    datosYColsTabla.rows=tabla;
    
    return datosYColsTabla;
}

function removeAccentsAndTildes(str) 
{
    var c;
    var map={
        'À':'A','Á':'A','Â':'A','Ã':'A','Ä':'A','Å':'A','Æ':'AE','Ç':'C','È':'E','É':'E','Ê':'E','Ë':'E','Ì':'I','Í':'I','Î':'I','Ï':'I','Ð':'D','Ñ':'N',
        'Ò':'O','Ó':'O','Ô':'O','Õ':'O','Ö':'O','Ø':'O','Ù':'U','Ú':'U','Û':'U','Ü':'U','Ý':'Y','ß':'s','à':'a','á':'a','â':'a','ã':'a','ä':'a','å':'a','æ':'ae',
        'ç':'c','è':'e','é':'e','ê':'e','ë':'e','ì':'i','í':'i','î':'i','ï':'i','ñ':'n','ò':'o','ó':'o','ô':'o','õ':'o','ö':'o','ø':'o',
        'ù':'u','ú':'u','û':'u','ü':'u','ý':'y','ÿ':'y','Ā':'A','ā':'a','Ă':'A','ă':'a','Ą':'A','ą':'a',
        'Ć':'C','ć':'c','Ĉ':'C','ĉ':'c','Ċ':'C','ċ':'c','Č':'C','č':'c','Ď':'D','ď':'d','Đ':'D','đ':'d',
        'Ē':'E','ē':'e','Ĕ':'E','ĕ':'e','Ė':'E','ė':'e','Ę':'E','ę':'e','Ě':'E','ě':'e','Ĝ':'G','ĝ':'g','Ğ':'G','ğ':'g','Ġ':'G','ġ':'g','Ģ':'G','ģ':'g',
        'Ĥ':'H','ĥ':'h','Ħ':'H','ħ':'h','Ĩ':'I','ĩ':'i','Ī':'I','ī':'i','Ĭ':'I','ĭ':'i','Į':'I','į':'i','İ':'I','ı':'i','Ĳ':'IJ','ĳ':'ij','Ĵ':'J','ĵ':'j',
        'Ķ':'K','ķ':'k','Ĺ':'L','ĺ':'l','Ļ':'L','ļ':'l','Ľ':'L','ľ':'l','Ŀ':'L','ŀ':'l','Ł':'L','ł':'l','Ń':'N','ń':'n','Ņ':'N','ņ':'n','Ň':'N','ň':'n','ŉ':'n',
        'Ō':'O','ō':'o','Ŏ':'O','ŏ':'o','Ő':'O','ő':'o','Œ':'OE','œ':'oe','Ŕ':'R','ŕ':'r','Ŗ':'R','ŗ':'r','Ř':'R','ř':'r',
        'Ś':'S','ś':'s','Ŝ':'S','ŝ':'s','Ş':'S','ş':'s','Š':'S','š':'s','Ţ':'T','ţ':'t','Ť':'T','ť':'t','Ŧ':'T','ŧ':'t',
        'Ũ':'U','ũ':'u','Ū':'U','ū':'u','Ŭ':'U','ŭ':'u','Ů':'U','ů':'u','Ű':'U','ű':'u','Ų':'U','ų':'u','Ŵ':'W','ŵ':'w','Ŷ':'Y','ŷ':'y','Ÿ':'Y',
        'Ź':'Z','ź':'z','Ż':'Z','ż':'z','Ž':'Z','ž':'z','ſ':'s','ƒ':'f','Ơ':'O','ơ':'o','Ư':'U','ư':'u','Ǎ':'A','ǎ':'a','Ǐ':'I','ǐ':'i','Ǒ':'O','ǒ':'o',
        'Ǔ':'U','ǔ':'u','Ǖ':'U','ǖ':'u','Ǘ':'U','ǘ':'u','Ǚ':'U','ǚ':'u','Ǜ':'U','ǜ':'u','Ǻ':'A','ǻ':'a','Ǽ':'AE','ǽ':'ae','Ǿ':'O','ǿ':'o'
    };
    var res=''; //Está variable almacenará el valor de str, pero sin acentos y tildes
    for (var i=0;i<str.length;i++){
        c = str.charAt(i);
        res += map[c] || c;
    }
    return res;
}

var normalizarAcentos = (function() 
{
    var from = "ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç", 
        to   = "AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuunncc",
        mapping = {};
 
    for(var i = 0, j = from.length; i < j; i++ )
        mapping[ from.charAt( i ) ] = to.charAt( i );
 
    return function( str ) {
        var ret = [];
        for( var i = 0, j = str.length; i < j; i++ ) {
            var c = str.charAt( i );
            if( mapping.hasOwnProperty( str.charAt( i ) ) )
                ret.push( mapping[ c ] );
            else
                ret.push( c );
        }      
        return ret.join( '' );
    };
 
})();

var accentEncode = function (tx)
{
	var rp = String(tx);
	//
	rp = rp.replace(/á/g, '&aacute;');
	rp = rp.replace(/é/g, '&eacute;');
	rp = rp.replace(/í/g, '&iacute;');
	rp = rp.replace(/ó/g, '&oacute;');
	rp = rp.replace(/ú/g, '&uacute;');
	rp = rp.replace(/ñ/g, '&ntilde;');
	rp = rp.replace(/ü/g, '&uuml;');
	//
	rp = rp.replace(/Á/g, '&Aacute;');
	rp = rp.replace(/É/g, '&Eacute;');
	rp = rp.replace(/Í/g, '&Iacute;');
	rp = rp.replace(/Ó/g, '&Oacute;');
	rp = rp.replace(/Ú/g, '&Uacute;');
	rp = rp.replace(/Ñ/g, '&Ntilde;');
	rp = rp.replace(/Ü/g, '&Uuml;');
	//
	return rp;
};

var accentDecode = function (tx)
{
	var rp = String(tx);
	//
	rp = rp.replace(/&aacute;/g, 'á');
	rp = rp.replace(/&eacute;/g, 'é');
	rp = rp.replace(/&iacute;/g, 'í');
	rp = rp.replace(/&oacute;/g, 'ó');
	rp = rp.replace(/&uacute;/g, 'ú');
	rp = rp.replace(/&ntilde;/g, 'ñ');
	rp = rp.replace(/&uuml;/g, 'ü');
	//
	rp = rp.replace(/&Aacute;/g, 'Á');
	rp = rp.replace(/&Eacute;/g, 'É');
	rp = rp.replace(/&Iacute;/g, 'Í');
	rp = rp.replace(/&Oacute;/g, 'Ó');
	rp = rp.replace(/&Uacute;/g, 'Ú');
	rp = rp.replace(/&Ñtilde;/g, 'Ñ');
	rp = rp.replace(/&Üuml;/g, 'Ü');
	//
	return rp;
};

function isNombreOApellido (texto,numCaracteres)
{
    var reg = new RegExp("^([A-Za-zñäëïöü\-_\.]{2,"+numCaracteres+"}[ ]?)*[^ ]$","i");    
    if ( reg.test(texto) ) 
        return true;
    return false;
}

//Enviar fecha en formato dd/MM/yyyy o dd/MMMM/yyyy ('MMMM' Muestra el mes en forma completa (por ejemplo, Enero o ENERO).)
function isFecha (fecha)
{    
    var mesesEnTexto = ["ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"];
    try{
        var fecha = fecha.split("/");
        var dia = fecha[0], mes = fecha[1], anio = fecha[2];
        var estado = true, dmax="";
        
        if (mes.length>2){                                                      //Si el mes viene en MMMM, extraemos el número correspondiente a dicho mes
            for (var i=0; i<12; i++)
                if ( mes.toUpperCase() === mesesEnTexto[i] ){
                    mes = ((i<9)?"0":"")+(i+1);
                    fecha[1] = mes;
                    break;
                }
        }
    
        if ((dia.length === 2 || dia.length === 1) && (mes.length === 2) && (anio.length === 4))     //Verificamos que sean la cantidad exacta de dígitos para el día, mes y año
        {
            switch (parseInt(mes)) {                                            //Obtenemos cuántos días tiene el mes
                case 4:case 6:case 9: case 11: 
                        dmax = 30; 
                    break; 
                case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                        dmax = 31;
                    break;
                case 2: 
                    dmax = (anio % 4 === 0) ? 29 : 28; 
                    break;
            }

            dmax = (dmax!=="") ? dmax : -1;

            if ((dia >= 1) && (dia <= dmax) && (mes >= 1) && (mes <= 12))       //verificamos que el rango esté entre 1 y 31 y 1 y 12 para el día y el mes respectivamente
            {
                for (var i = 0; i < fecha[0].length; i++)                       //Verificamos que el día y el mes sean números
                { 
                    var diaC = fecha[0].charAt(i).charCodeAt(0);
                    if (!((diaC > 47) && (diaC < 58))) return false;
                    var mesC = fecha[1].charAt(i).charCodeAt(0);
                    if (!((mesC > 47) && (mesC < 58))) return false;
                }
            }else 
                return false;

            for (var i = 0; i < fecha[2].length; i++)                           //Verificamos que el año sea numérico
            {
                var anoC = fecha[2].charAt(i).charCodeAt(0);
                if (!((anoC > 47) && (anoC < 58))) return false;
            }
        }else 
            return false;

        return estado;

    }catch(err){
        return false;
    }
}

function showReport (panelContenedor, jsp, jsonParameters)
{
    var parametros='';
    
    for (var prop in jsonParameters) {
        parametros += '<input type="hidden" id="'+prop+'" name="'+prop+'" value="'+jsonParameters[prop]+'">';
	};
    
    if($("#"+panelContenedor+' #pnlReportContainer').length)
        $("#pnlReportContainer").remove();
    
    $("#"+panelContenedor).append('<div id="pnlReportContainer" style="display:none;"></div>');
    $("#pnlReportContainer").append('<form id="formulario" method="POST" action="'+jsp+'"  target="_blank">' //onsubmit="window.open("", "_blank")"
                                        + parametros
                                        +'<button type="submit">Firmar</button>'
                                    +'</form>');
    var form = document.getElementById("formulario");
    form.submit();    
    //$("#pnlReportContainer").remove();
    panelContenedor="";
    jsp="";
}