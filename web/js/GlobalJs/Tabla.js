/* 
    Creado el     : 15/04/2015, 08:40:17 AM
    Autor         : Ing. Maai Nolasco Sánchez
    Versión       : 7.2.0
    Última edición: 04/Sep/2017 19:36
*/
//******************************************************************************
//*********************** FUNCIONES PARA MANIPULAR TABLAS **********************
//******************************************************************************

function Tabla ()
{
    /**
    * Dibuja una tabla en base a los parámetros dados
    * @param {String} nombreContenedor Un contenedor que servirá como el escroll de la tabla
    * @param {String} nombreTabla El nombre de la tabla a dibujar
    * @param {Array} tabla Los datos que contendrá la tabla de tipo JSON
    * @param {Array} tituloColumnas Un arreglo con los nombres que irán en el encabezado de cada columna
    * @param {Array} colsNameToShow Un arreglo con los nombres de las columnas que serán mostrados, en el orden que estén en el arreglo así serán mostradas. Enviar null si se desea que se muestre todo el contenido
    * @param {Array} claseColumnas Un arreglo con el tipo de entrada de datos: default o vacío o null, checkbox, radiobutton, textbox, combox. Enviar null si no se desea especificar el arreglo
    * @param {Array} textAlign Un arreglo con el tipo de alineacion del contenido de las columnas: right, center, left. Si se envía null la alinación default es right. El arreglo debe coincidir con la posición de columnas visibles de la tabla.
    * @param {Boolean} selOnClick Booleano que indicara si se quiere que dé el efecto de fila seleccionada. 
    * @param {String} modoSeleccion Nombre del caso que indica el modo de selección: Si selOnClick es true entonces modoSelección se activa pudiendo usar los siguientes valores: default o vacío o null: Indica que el modo de selección sólo es de una fila a la vez, ctrlClick:Selección con tecla Control + Click,  shiftClick: Selección de rango con tecla Shift + Click, ctrlOShiftClick: El poder usar las dos opciones anteriores
    * @param {function} actionsOnClick Función a llamar cuando se le dé click en la fila. Enviar null si no se desea especificar una función.
    * @param {function} eventsForDrawTable Una funcion con los eventos a los componentes de la tabla. Enviar null si no se desea especificar una función.
    * @returns {void}
    */                                                                          //<<--OK-->>
    this.create = function (nombreContenedor, nombreTabla, tabla, tituloColumnas, colsNameToShow, claseColumnas, textAlign, selOnClick, modoSeleccion, actionsOnClick, eventsForDrawTable)
    {
        var i, c, talig, k, colName;
        var thisClass = this;
        /*
            Verificamos si la tabla donde se muestran los resultados de búsqueda Existe, si el caso es SI, entonces la eliminamos, para poder imprimir la nueva tabla
            con los resultados encontrados
        */
       if($('#'+nombreTabla).length) // Verificando si la tabla Existe
            $('#'+nombreTabla).remove();
        /*Empezamos a crear la tabla utilizando la Libreria Jquery dentro del contenedor indicado en nombreContenedor*/
        $("#"+nombreContenedor).append('<table id="'+nombreTabla+'">');
            var cabecera = "<thead><tr>";
            for (i=0; i< tituloColumnas.length; i++)
                cabecera+="<th class='"+nombreTabla+"_col"+i+"'><div id='"+nombreTabla+"_divth"+i+"'>"+ tituloColumnas[i]+"</div></th>";
            cabecera+="</tr></thead>";
            $("#"+nombreTabla).append(cabecera);

        $("#"+nombreTabla).append("<tbody id ='tbody'>");
        if(tabla!==null && typeof tabla!=="undefined" && tabla.length > 0)
        {
            var fila, dataFila, columnas, columnasOcultas, celda, dato, p;
            //----------------------------------------------------------------------
            var posColsOcultas = new Array();                                   //Un arreglo donde nos quedará las posiciones de las columnas que quederán como ocultas
            i=0; 
            for (var t in tabla[0]) i++;             //Contamos cuántas columnas trae la tabla
            for (var t in tabla[0])                    //Parece ser que saca los nombres del arreglo empezando por el último elemento
                posColsOcultas[--i]=t;              //Inicializamos por default que todas las posiciones no serán visibles

            //----------------------------------------------------------------------
            $.each(tabla,function(f,valor) {
                fila = valor; dataFila="", columnas="", columnasOcultas="";
                for (c=0; c< tituloColumnas.length; c++){                        //Obtenenmos las columnas que serán visibles al usuario
                    if (colsNameToShow!==null && typeof fila[colsNameToShow[c]]==="undefined") 
                        celda=colsNameToShow[c]+"/Indef";
                    else {
                        //---------- Extraemos el dato que vamos a poner en la tabla ----------\\
                        if (colsNameToShow===null){
                            k=0; 
                            for (var name in fila) { if (k++===c) { dato = fila[name]; colName = name; break;  } };
                        }else {
                            colName=colsNameToShow[c];
                            dato = (fila[colName]===null || fila[colName]==="null")?"":(""+fila[colName]).trim();
                        }
                        if (f===0){                                              //Con analizar la primer fila nos será suficiente para detectar qué columnas serán las ocultas
                            p = thisClass._posOf(posColsOcultas, colName);
                            posColsOcultas.splice(p,1); 
                        }
                        //----------------------------------------------------------------------\\
                        if (claseColumnas===null || claseColumnas[c]==="default" || claseColumnas[c]==="")
                            celda = dato;
                        else if (claseColumnas[c]==="checkbox")
                            celda = "<input type='checkbox' id='"+nombreTabla+"_chk_f"+f+"_c"+c+"' name='"+nombreTabla+"_chk_f"+f+"_c"+c+"' value='"+dato+"'>";
                        else if (claseColumnas[c]==="radiobutton")
                            celda = "<input type='radio'    id='"+nombreTabla+"_rbn_f"+f+"_c"+c+"' name='"+nombreTabla+"_rbg_c"+c+"'        value='"+dato+"'/>";
                        else if (claseColumnas[c]==="textbox" || claseColumnas[c]==="textarea"|| claseColumnas[c]==="label"){
                            talig="";
                            if (textAlign!==null && (typeof textAlign!=="undefined") && (typeof textAlign[c]!=="undefined") && textAlign[c]!=="")
                                talig = "style='text-align:"+textAlign[c]+";'";
                            
                            if (claseColumnas[c]==="textbox")
                                celda = "<input type='text' id='"+nombreTabla+"_txt_f"+f+"_c"+c+"' name='"+nombreTabla+"_txt_f"+f+"_c"+c+"' value='"+dato+"' "+talig+">";
                            else if (claseColumnas[c]==="textarea")
                                celda = "<textarea id='"+nombreTabla+"_txta_f"+f+"_c"+c+"' name='"+nombreTabla+"_txta_f"+f+"_c"+c+"' "+talig+">"+dato+"</textarea>";
                            else if (claseColumnas[c]==="label")
                                celda = "<label id='"+nombreTabla+"_lbl_f"+f+"_c"+c+"' name='"+nombreTabla+"_lbl_f"+f+"_c"+c+"' "+talig+">"+dato+"</label>";
                            
                        }else if (claseColumnas[c]==="combobox")
                            celda = "<select id='"+nombreTabla+"_cbx_f"+f+"_c"+c+"' name='"+nombreTabla+"_cbx_f"+f+"_c"+c+"' > <option value='"+dato+"'>"+dato+"</option> </select>";
                    }
                    columnas+="<td class='col"+nombreTabla+"_col"+c+" "+nombreTabla+"_"+colName+"_f"+f+" "+nombreTabla+"_col"+c+"' id='"+nombreTabla+"_td_f"+f+"_c"+c+"'>"+celda+"</td>";
                }
                for (c=0; c<posColsOcultas.length; c++)                         //Obtenemos las columnas que serán ocultas al usuario
                    columnasOcultas+="<input type='hidden' class='"+nombreTabla+"_"+posColsOcultas[c]+"_f"+f+"' id='"+nombreTabla+"_"+posColsOcultas[c]+"_f"+f+"' value='"+fila[posColsOcultas[c]]+"'/> ";

                $("#"+nombreTabla).append("<tr class='fila"+nombreTabla+"' id='"+nombreTabla+"_f"+f+"'>  "+columnas+" "+columnasOcultas+"</tr>");
                //====================== Alineación de columnas ======================\\
                if (textAlign!==null && typeof textAlign!=="undefined")
                {
                    for (c=0; c<textAlign.length; c++)
                        if ((typeof textAlign[c]!=="undefined") && textAlign[c]!=="")
                            $(".col"+nombreTabla+"_col"+c).css("text-align",textAlign[c]);
                }
                //====================== Eventos en cada celda ======================\\
                if (eventsForDrawTable!==null)
                {
                    eventsForDrawTable (f,c);
                }
                //===================================================================\\
            });
            
            if (selOnClick || actionsOnClick!==null)
            {
                $('.fila'+nombreTabla).on("click",function(event)
                {
                    if (selOnClick)
                    {
                        if (modoSeleccion===null || typeof modoSeleccion==="undefined" || modoSeleccion==="default" || modoSeleccion===""){
                            $('#'+nombreTabla+' .filaSel').removeClass('filaSel');      //Quitamos el elemento sombreado anteriormente
                            $(this).addClass("filaSel");                                //Sombreamos la fila
                        }else if (modoSeleccion==="ctrlClick"){
                            if (!event.ctrlKey)
                                $('#'+nombreTabla+' .filaSel').removeClass('filaSel');  //Quitamos el elemento sombreado anteriormente
                            $(this).addClass("filaSel");                                //Sombreamos la fila
                        }else if (modoSeleccion==="shiftClick"){
                            //Falta implementar
                        } else if (modoSeleccion==="ctrlOShiftClick"){
                            //Falta implementar
                        }
                    }

                    if (actionsOnClick!==null)
                        actionsOnClick ($(this).index(),nombreTabla,$(this).attr("id"));
                });

                /*$('.fila'+nombreTabla).dblclick(function(event){
                    alert("Dio doble click");
                });*/
            }
        }else if(nombreTabla==="tblMatCalifXBim") {
            $("#"+nombreTabla).append("<tr class='fila"+nombreTabla+"'><td  colspan='"+ tituloColumnas.length+"'> No hay datos que mostrar.</td></tr>"); //Para poder capturar las calificaciones favor de dar clic en el boton de abajo General materias del periodo
            $("#"+nombreTabla).append("<tr class='fila"+nombreTabla+"'><td  colspan='"+ tituloColumnas.length+"'> Para poder capturar las calificaciones favor de dar clic en el boton de abajo <h4>'Generar materias del periodo'</h4>.</td></tr>");
        }
        else
            $("#"+nombreTabla).append("<tr class='fila"+nombreTabla+"'><td  colspan='"+ tituloColumnas.length+"'> No hay datos que mostrar.</td></tr>");
        $("#"+nombreTabla).append("</tbody>");
        $("#"+nombreContenedor).append("</table>");

        
    };

    /**
     * Extrae todos los datos de la tabla a menos que se especifique en colsName las columnas específicas a extraer. Si "colsName" es null o no definido se activa el parámetro "caso" donde se podrá especificar si extrae todas las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * Por default devuleve un string donde el dato de cada columna estará separada por el caracter ~. Si se especifica en formato la palabra array, devuelve un arreglo en lugar de string
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea extraer los datos.
     * @param {Array} colsName Un arreglo de numeros enteros y/o nombre de columna que indican la posición o nombre columna(s) respectivamente a extraer. 
     *                         Enviar null si no se desea no especificar columnas en específico y que retorne todas las columnas tanto visibles como no visibles.
     *                         Cuando la columna contiene combobox:
     *                                  Por default obtiene el dato de value.
     *                                  Si se desea obtener el dato del option concatenarle al nombre de columa '_opt',
     *                                  Si se desea obtener el índice que corresponde al elemnto seleccionado concatenarle al nombre '_index'
     *                         Cuando la columna contiene un checkbox:
     *                                  Por default obtiene el ischecked.
     *                                  Si se desea obtener el dato del value concatenarle al nombre de columna '_val'
     * @param {String} caso Puede tomar 3 valores: null (o undefined), VISIBLES u OCULTAS. Cuando colsName es null la función por default extrae todas las columnas tanto visibles como ocultas. El parámetro "caso" sirve para especificar el filtro si se desea la extracción de las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * @param {String} formato El formato en el que se desa que devuelva cada fila. Si no se especifica devuelve cada fila como String separado por ~, si se especifica "array" devuelve cada fila como arreglo, o si se especifica "json" devuelve cada fila como objeto JSON.
     * @returns {Array} Retorna un arreglo de filas. Cada fila contiene las columnas extraídas (en formato de arreglo, JSON, o string con las columnas separadas por el caracter ~).
     */
    this.getTable = function(nombreTabla, colsName, caso, formato)              //<<--OK-->>
    {
        var tabla = new Array();
        var numFilas, fila;
        
        numFilas = this.getNumRows (nombreTabla);
        for (var index=0; index<numFilas; index++){
            fila = this.getRow (nombreTabla, index, colsName, caso, formato);
            tabla[index]=fila;
        }
        return tabla;
    };
    
    /**
     * Devuelve la fila en la posición indicada con todas las columnas (a menos que se indiquen específicamente en el parámetro "colsName"). Si "colsName" es null o no definido se activa el parámetro "caso" donde se podrá especificar si se extraen todas las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea extraer los datos.
     * @param {Number|index} rowIndex Posición de la fila que se desa extraer.
     * @param {Array} colsName Un arreglo de numeros enteros y/o nombre de columna que indican la posición o nombre columna(s) respectivamente a extraer. 
     *                         Enviar null si no se desea no especificar columnas en específico y que retorne todas las columnas tanto visibles como no visibles.
     *                         Cuando la columna contiene combobox:
     *                                  Por default obtiene el dato de value.
     *                                  Si se desea obtener el dato del option concatenarle al nombre de columa '_opt',
     *                                  Si se desea obtener el índice que corresponde al elemnto seleccionado concatenarle al nombre '_index'
     *                         Cuando la columna contiene un checkbox:
     *                                  Por default obtiene el ischecked.
     *                                  Si se desea obtener el dato del value concatenarle al nombre de columna '_val'
     *                         Cuando la columna contiene textarea:
     *                                  Por default obtiene el value.
     *                                  Si se desea obtener el dato del text concatenarle al nombre de columna '_text'
     *                         Cuando la columna contiene label:
     *                                  Por default obtiene el html.
     *                                  Si se desea obtener el dato del text concatenarle al nombre de columna '_text'
     * @param {String} caso Puede tomar 3 valores: null (o undefined), VISIBLES u OCULTAS. Cuando colsName es null la función por default extrae todas las columnas tanto visibles como ocultas. El parámetro "caso" sirve para especificar el filtro si se desea la extracción de las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * @param {String} formato El formato en el que se desa que devuelva la fila. Si no se especifica devuelve un String separado por ~, si se especifica "array" devuelve la fila como arreglo, o si se especifica "json" devuelve la fila como objeto JSON.
     * @returns {String|Array|JSON} Retorna los datos de la fila especificada en un string separando las columnas por el caracter ~, o las columnas en array o JSON según se haya especificado en la variable formato.
     */
    this.getRow = function(nombreTabla, rowIndex, colsName, caso, formato)      //<<--ok-->>
    {
        var c=0, f;
        var tr,td, fila="", colNameTmp;

        if (this.getNumRows (nombreTabla) > 0)
        {
            tr = $("#"+nombreTabla+" >tbody").children("tr")[rowIndex];
            //numIdRow = $(tr).attr("id").replace(nombreTabla+"_f","");    //OJO: Se Extrae el número de nombre de fila y se lo damos a numIdRow, la posición de la fila podría no coincidir precisamente con el número que tiene el nombre, esto depende si despues se inserta o elimina una fila posteriormente
            fila = "";
            if (colsName!==null && typeof colsName!=="undefined"){
                for (c=0; c<colsName.length; c++){                              //Obtenemos cada columna específicada en el arreglo namesColsToGet
                    fila+= (c===0)?"":"~";
                    colNameTmp = ""+colsName[c];                                //Convertimos a String porque cuando atrapa números se invalida el .indexOf
                    if (colNameTmp.indexOf("_opt")>=0 || colNameTmp.indexOf("_index")>=0 || colNameTmp.indexOf("_val")>=0)  //_opt, _index y _val serán palabras reservadas en nombres de columnas usadas para el combobox y checkbox
                        colNameTmp = colsName[c].replace("_opt","").replace("_index","").replace("_val","");
                    if (isNaN(colNameTmp))
                        td = $(tr).children("[class*='_"+colNameTmp+"_']");
                    else
                        td = $(tr.cells[colNameTmp]);                          //Si es numérico, se sobreentiende que es únicamente para las columnas visibles y el número indica la posición de columna a elegir
                    
                    if (typeof td !== "undefined" && td.length){
                        if ( td[0].outerHTML.indexOf('type="') >= 0 || td[0].outerHTML.indexOf('<select') >= 0 || td[0].outerHTML.indexOf('<textarea') >= 0 || td[0].outerHTML.indexOf('<label') >= 0 )
                        {
                            if ( td[0].outerHTML.indexOf('type="text"') >= 0 )
                                fila+=$(td).children("input[type='text']").val();
                            else if ( td[0].outerHTML.indexOf('type="hidden"') >= 0 )
                                fila+=td.val();
                            else if ( td[0].outerHTML.indexOf('type="checkbox"') >= 0 ){
                                if ( (""+colsName[c]).indexOf("_val") >= 0 )
                                    fila+=$(td[0]).children("input[type='checkbox']").val();
                                else
                                    fila+=$(td[0]).children("input[type='checkbox']").is(':checked');             //Falta probarlo y corregir si está mal
                            }else if ( td[0].outerHTML.indexOf('type="radio"') >= 0 )
                                fila+=$(td[0]).children("input[type='radio']").is(':checked');                    //Falta probarlo y corregir si está mal
                            else if ( td[0].outerHTML.indexOf('<select ') >= 0 ){
                                if ( (""+colsName[c]).indexOf("_index") >= 0 )
                                    fila+=$('#'+$(td[0]).attr('id')+' option:selected').index();                  //Devuélve el índice del item seleccionada
                                else if ( (""+colsName[c]).indexOf("_opt") >= 0 )
                                    fila+=$('#'+$(td[0]).attr('id')+' option:selected').text();                   //Devuelve lo que se ve en el combo
                                else
                                    fila+=$('#'+$(td[0]).attr('id')+' option:selected').val();                    //Devuelve lo que hay en el atributo value.
                            }else if ( td[0].outerHTML.indexOf('<textarea ') >= 0 ){
                                if ( (""+colsName[c]).indexOf("_text") >= 0 )
                                    fila+=$('#'+$(td[0]).attr('id')+' textarea').text();
                                else
                                    fila+=$('#'+$(td[0]).attr('id')+' textarea').val();                            //con .val se obtiene lo que el usuario ha escrito recientemente en el textarea.
                            }else if ( td[0].outerHTML.indexOf('<label ') >= 0 ){
                                if ( (""+colsName[c]).indexOf("_text") >= 0 )
                                    fila+=$('#'+$(td[0]).attr('id')+' label').text();
                                else
                                    fila+=$('#'+$(td[0]).attr('id')+' label').html();
                            }else    
                                fila+=colsName[c]+"_objUnknown";
                        }else
                            fila+=$(td).text();                                 //objeto.cells[0].childNodes[0].nodeValue
                    }else
                        fila+=colsName[c]+"_colUndefined";
                }
            }else {                                                                                               //Si no se especifican columnas, extrae todas
                if (caso===null || typeof caso==="undefined" || caso.trim().toUpperCase()==="VISIBLES"){
                    f=0;
                    $(tr).children("td").each(function (){                                                        //Obtenemos primeramente las columnas visibles
                        fila+= (f===0)?"":"~";
                        if ( this.outerHTML.indexOf('type="') >= 0 || this.outerHTML.indexOf('<select') >= 0 || this.outerHTML.indexOf('<textarea') >= 0 || this.outerHTML.indexOf('<label') >= 0 )
                        {
                            if ( this.innerHTML.indexOf('type="text"') >= 0 )
                                fila+=$('#'+$(this).attr('id')+' input:text').val();
                            else if ( this.innerHTML.indexOf('type="checkbox"') >= 0 )
                                fila+=$('#'+$(this).attr('id')+' input:checkbox').is(':checked');       //Falta probarlo y corregir si está mal
                            else if ( this.innerHTML.indexOf('type="radio"') >= 0 )
                                fila+=$('#'+$(this).attr('id')+' input:radio').is(':checked');          //Falta probarlo y corregir si está mal
                            else if ( this.innerHTML.indexOf('<select ') >= 0 )
                                fila+=$('#'+$(this).attr('id')+' option:selected').val();               //con .text se obtiene lo que se ve en el combo, con .val se obtiene lo que hay en el atributo value.
                            else if ( this.innerHTML.indexOf('<textarea ') >= 0 )
                                fila+=$('#'+$(this).attr('id')+' textarea').val();                      //con .val se obtiene lo que el usuario ha escrito recientemente en el textarea.
                            else if ( this.innerHTML.indexOf('<label ') >= 0 )
                                fila+=$('#'+$(this).attr('id')+' label').html();
                            else
                                fila+=$(this).attr('id')+"_objUnknown";
                        }else
                            fila+=$(this).text().trim();
                        f++;
                    });
                }
                if (caso===null || typeof caso==="undefined" || caso.trim().toUpperCase()==="OCULTAS"){
                    f=0;
                    $(tr).children("input:hidden").each(function (){                    //Obtenemos las columnas no visibles
                        fila+= (f===0 && fila.length===0)?"":"~";
                        fila+=$(this).val();         //Falta probarlo y corregir si está mal
                        f++;
                    });
                }
            }
        }

        if (typeof formato!=="undefined" && formato!==null ){
            if (formato.trim().toUpperCase()==="ARRAY")
                return (fila==="") ? new Array() : fila.split("~");
            else if (formato.trim().toUpperCase()==="JSON"){
                var yeison = {};
                var arreglo = (fila==="") ? new Array() : fila.split("~");
                for (var i=0; i<arreglo.length; i++) {
                    if (colsName)
                        yeison[colsName[i]]=arreglo[i];
                    else
                        yeison["dato"+i]=arreglo[i];
                }
                return yeison;
            }
        }
        
        return fila;
    };

    /**
     * Devuelve la fila en la posición con efecto de selección con todas las columnas (a menos que se indiquen específicamente en el parámetro "colsName"). Si "colsName" es null o no definido se activa el parámetro "caso" donde se podrá especificar si se extraen todas las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea extraer la fila.
     * @param {Array} colsName Un arreglo de numeros enteros y/o nombre de columna que indican la posición o nombre columna(s) respectivamente a extraer. 
     *                         Enviar null si no se desea especificar columnas y que retorne todas las columnas tanto visibles como no visibles.
     *                         Cuando la columna contiene combobox:
     *                                  Por default obtiene el dato de value.
     *                                  Si se desea obtener el dato del option concatenarle al nombre de columa '_opt',
     *                                  Si se desea obtener el índice que corresponde al elemnto seleccionado concatenarle al nombre '_index'
     *                         Cuando la columna contiene un checkbox:
     *                                  Por default obtiene el ischecked.
     *                                  Si se desea obtener el dato del value concatenarle al nombre de columna '_val'
     * @param {String} caso Puede tomar 3 valores: null (o undefined), VISIBLES u OCULTAS. Cuando colsName es null la función por default extrae todas las columnas tanto visibles como ocultas. El parámetro "caso" sirve para especificar el filtro si se desea la extracción de las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * @param {String} formato El formato en el que se desa que devuelva la fila. Si no se especifica devuelve un String separado por ~, si se especifica "array" devuelve la fila como arreglo, o si se especifica "json" devuelve la fila como objeto JSON.
     * @returns {String|Array|JSON} Retorna los datos de la fila seleccionada en un string separando las columnas por el caracter ~, o las columnas en array o JSON según se haya especificado en la variable formato.
     */
    this.getSelectedRow = function(nombreTabla, colsName, caso, formato)        //<<--OK-->>
    {
        var celdas = new Array();
        var index;
        
        index = this.getSelectedIndexRow (nombreTabla);
        if ( index >= 0 )
            celdas = this.getRow (nombreTabla, index, colsName, caso, formato);
         return celdas;
    };
    
    /**
     * Devuelve las filas en la posición con efecto de selección con todas las columnas (a menos que se indiquen específicamente en el parámetro "colsName"). Si "colsName" es null o no definido se activa el parámetro "caso" donde se podrá especificar si se extraen todas las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea extraer la fila.
     * @param {Array} colsName Un arreglo de numeros enteros y/o nombre de columna que indican la posición o nombre columna(s) respectivamente a extraer. 
     *                         Enviar null si no se desea especificar columnas y que retorne todas las columnas tanto visibles como no visibles.
     *                         Cuando la columna contiene combobox:
     *                                  Por default obtiene el dato de value.
     *                                  Si se desea obtener el dato del option concatenarle al nombre de columa '_opt',
     *                                  Si se desea obtener el índice que corresponde al elemnto seleccionado concatenarle al nombre '_index'
     *                         Cuando la columna contiene un checkbox:
     *                                  Por default obtiene el ischecked.
     *                                  Si se desea obtener el dato del value concatenarle al nombre de columna '_val'
     * @param {String} caso Puede tomar 3 valores: null (o undefined), VISIBLES u OCULTAS. Cuando colsName es null la función por default extrae todas las columnas tanto visibles como ocultas. El parámetro "caso" sirve para especificar el filtro si se desea la extracción de las columnas visibles (VISIBLES) u ocultas (OCULTAS) o ambas (null o undefined).
     * @param {String} formato El formato en el que se desa que devuelva la fila. Si no se especifica devuelve un String separado por ~, si se especifica "array" devuelve un arreglo.
     * @returns {Array of Array|String} Un arreglo con el valor de cada columna de la fila seleccionada.
     */
    this.getSelectedRows = function(nombreTabla, colsName, caso, formato)       //<<--OK-->>
    {
        var fila = new Array();
        var tabla = [];
        var fs=0;
        var thisClass = this;
        
        if ($('#'+nombreTabla+" >tbody >tr").hasClass('filaSel')){
            $("#"+nombreTabla+" >tbody >tr").each(function (index) {
                if ($(this).hasClass("filaSel")){
                    fila = thisClass.getRow (nombreTabla, index, colsName, caso, formato);
                    tabla[fs++] = fila;
                }
            });
        }
        return tabla;
    };
    
    
    /**
     * Devuelve si la tabla especificada tiene un fila con efecto de selección.
     * @param {type} nombreTabla El nombre de la tabla de la que se desea saber si tiene fila seleccionada.
     * @returns {Boolean} Valor booleano el cual retorna true si tiene una fila seleccionada, false si no.
     */
    this.hasSelectedRow = function(nombreTabla)                                 //<<--OK-->>
    {
        if ((document.getElementById(nombreTabla)).getElementsByClassName("filaSel").length>0)    // tambien: $('#'+nombreTabla+' .filaSel td').size()>0
            return true;
        else
            return false;
    };

    /**
     * Devuelve la cantidad de filas que contiene la tabla especificada
     * @param {String} nombreTabla El nombre de la tabla de la que se desea saber el número de filas contenidas.
     * @returns {Number} El número de filas que contiene la tabla
     */
    this.getNumRows = function(nombreTabla)                                     //<<--OK-->>
    {
        var tam = $('#'+nombreTabla+' >tbody >tr').length;
        if (tam===1  &&  $('#'+nombreTabla+' >tbody >tr >td')[0].innerHTML.trim()==="No hay datos que mostrar." )
            return 0;
        return tam;
    };
    
    /**
     * Devuelve la cantidad de columnas que contiene la tabla especificada
     * @param {String} nombreTabla El nombre de la tabla de la que se desea saber el número de columnas contenidas.
     * @returns {Number} El número de columnas que contiene la tabla
     */
    this.getNumCols = function(nombreTabla)
    {
        var tam = $('#'+nombreTabla+' >thead th').length;
        return tam;
    };

    /**
     * Devuelve la posición en la que la tabla especificada tiene un fila con efecto de selección.
     * @param {String} nombreTabla El nombre de la tabla de la que se desea saber la posición de la fila seleccionada.
     * @returns {Number|index} La posición de la fila seleccinada, -1 si no existe la selección o null si no está definida la tabla
     */
    this.getSelectedIndexRow = function(nombreTabla)                            //<<--OK-->>
    {
        var element, filaSel;
        if (document.getElementById(nombreTabla) === null)
            return null;
        
        element = document.getElementById(nombreTabla).getElementsByClassName("filaSel");
        filaSel = -1;
        
        if (element.length > 0)
            if (element[0].cells[0].innerText !== "No hay datos que mostrar.")
                filaSel = $(element).index();
        //----------------------- CÓDIGO OBSOLETO -----------------------\\
        /*if ($('#'+nombreTabla+" >tbody >tr").hasClass('filaSel')){
            $("#"+nombreTabla+" >tbody >tr").each(function (index) {
                //$(this).children("td").each(function (index2) {});
                if ($(this).hasClass("filaSel")){
                    filaSel = index;
                    return false;
                }
            });
        }else
            return filaSel;*/
        //----------------------------------------------------------------//
        return filaSel;
    };
    
        /**
     * Devuelve las posiciones en la que la tabla especificada tiene fila con efecto de selección.
     * @param {String} nombreTabla El nombre de la tabla de la que se desea saber la posición de la fila seleccionada.
     * @returns {Array} Un vector de posiciones de filas seleccinadas.
     */
    this.getSelectedIndexesRow = function(nombreTabla)                          //<<--OK-->>
    {
        var i=0;
        var vectorIndex = [];
        if ($('#'+nombreTabla+" >tbody >tr").hasClass('filaSel')){
            $("#"+nombreTabla+" >tbody >tr").each(function (index) {
                if ($(this).hasClass("filaSel")){
                    vectorIndex[i++] = index;
                }
            });
        }
        return vectorIndex;
    };

    /**
     * Establece una fila con efecto de selección a la tabla especificada.
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea seleccionar la fila
     * @param {Number} pos La posición de la fila que se desea establecer la selecció
     * @param {boolean} multiselec Valor booleano que indica si se quire seleccionar varias filas.
     * @returns {void}
     */
    this.setSelectedRow = function(nombreTabla, pos, multiselec)                //<<--OK-->>
    {
        //var numIdRow;
        if (multiselec !== true){                                               //Si multiselec trae true, no borra las selecciones ya hechas, de tal manera que permite el efecto de seleccionar varias filas
            if ($('#'+nombreTabla+" >tbody >tr").hasClass('filaSel')){          //Si hay una fila seleccionada.  Otra forma: document.getElementsByClassName("filaSel").length 
                $('#'+nombreTabla+" >tbody .filaSel").each(function (){
                    $(this).removeClass('filaSel');                             //Deseleccionamos todo lo que esté con selección
                });
            }
        }
        
        if (pos>=0 && pos<this.getNumRows(nombreTabla))
            $( $("#"+nombreTabla+" >tbody").children("tr")[pos] ).addClass("filaSel");
            //$("#"+nombreTabla+"_f"+numIdRow).addClass("filaSel");
    };

    /**
     * Actualiza los datos de un fila en las celdas especificadas
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea seleccionar la fila
     * @param {Number} index La posición de la fila que se desea establecer la selecció
     * @param {JSON} jsonrow Los datos a actualizar en las celdas en formato JSON, las claves del JSON pueden ser de dos tipos: numérico o string, Si es tipo númerico representa a la posición de la columna visible, si es tipo String representa al nombre de la columna ya sea visible u oculta
     */
    this.updateRow = function(nombreTabla,index,jsonrow)                        //<<--OK-->>
    {
        var celda;
        for (var prop in jsonrow) {                                             //Obtenemos cada columna específicada            
            //----------- SI TODO FUNCIONA BIEN, QUITAR ESTE CODIGO -----------------\\
            //numIdRow = this._getNumIdRow (nombreTabla, index);                //OJO: Se Extrae el número de nombre de fila y se lo damos a numIdRow, la posición de la fila podría no coincidir precisamente con el número que tiene el nombre, esto depende si despues se inserta o elimina una fila posteriormente
            //celda = document.getElementsByClassName(nombreTabla+"_"+prop+"_f"+numIdRow);
            //\\-----------------------------------------------------------------------//
            tr = $("#"+nombreTabla+" >tbody >tr")[index];
            if (isNaN(prop))
                celda = $(tr).children("[class*='_"+prop+"_']");
            else
                celda = $(tr.cells[prop]);
            
            if (celda.length>0){
                if ( celda[0].innerHTML.indexOf('type="checkbox"') >= 0 )
                    $(celda).children("input[type='checkbox']").on(':checked'); //Falta probarlo y corregir porque está mal
                else if ( celda[0].innerHTML.indexOf('type="radio"') >= 0 )
                    $(celda).children("input[type='radio']").is(':checked');    //Falta probarlo y corregir porque está mal
                else if ( celda[0].innerHTML.indexOf('type="text"') >= 0 )
                    $(celda).children("input[type='text']").val(jsonrow[prop]);
                else if ( celda[0].innerHTML.indexOf('<select ') >= 0 ){
                    $('#'+$(celda).attr('id')+' option:selected').val();        //Implementar el _index y el _text
                    //$($("#pnlPreinscripcion select")).prop('selectedIndex', 0);
                    //$('#frmwCamDeGpo .coltblCamDeGpo_col4 select').val(jsCamDeGpo.tblPrincipal_grupo);
                }else if ( celda[0].innerHTML.indexOf('<textarea ') >= 0 ){
                    $('#'+$(celda).attr('id')+' textarea').val();               //Implementar el _text  //Hay que probarlo a ver si está bien
                }else if ( celda[0].innerHTML.indexOf('<label ') >= 0 ){
                    $('#'+$(celda).attr('id')+' label').html();                 //Implementar el _text
                }else if ( celda[0].outerHTML.indexOf('type="hidden"') >= 0 )
                    $(celda).val(jsonrow[prop]);
                else
                    celda[0].innerHTML = jsonrow[prop];
            }
        }
    };
    
    /**
     * Remueve una fila de la tabla
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea eliminar la fila
     * @param {Number} index La posición de la fila que se desea eliminar. Enviar -1 si se desea eliminar la última.
     */
    this.removeRow = function (nombreTabla, index)                              //<<--RC-->>
    {
        var numFilas=this.getNumRows (nombreTabla);
        if(numFilas>0)
        {
            if (numFilas===1){                                                   //Si sólo quedaba una, como al eliminar ya no queda más, agregamos nuestro texto de que ya no hay datos
                tr = $("#"+nombreTabla+" >tbody >tr >td");
                $("#"+nombreTabla).append("<tr class='fila"+nombreTabla+"'><td  colspan='"+tr.length+"'> No hay datos que mostrar.</td></tr>");
            }
            if (index <= -1)
                $("#"+nombreTabla+" tr:last").remove();                         // Eliminamos la ultima columna
            else
                $( $("#"+nombreTabla+" >tbody").children("tr")[index] ).remove();
        }
    };
    
    /**
     * Agrega fila(s) a una tabla
     */                                                                         //<<--RC-->>
    this.addRows = function (index, nombreTabla, tabla, colsNameToShow, claseColumnas, textAlign, selOnClick, modoSeleccion, actionsOnClick, eventsForDrawTable)
    {
        var i, c, numIdRow, posInsert, newRow, n, numFilas;
        var thisClass = this;
        //tr = $("#"+nombreTabla+" >tbody").children("tr")[rowIndex];
        if(typeof tabla!=="undefined" && tabla.length > 0)
        {
            var fila, dataFila, columnas, columnasOcultas;
            var trs = $('#'+nombreTabla+' >tbody >tr');
            numFilas = trs.length
            numIdRow = numFilas;
            if (numIdRow===1  &&  $('#'+nombreTabla+' >tbody >tr >td')[0].innerHTML.trim()==="No hay datos que mostrar." ){
                $("#"+nombreTabla+" tr:last").remove();
                numIdRow = 0;
                index = -1;
            }else{                                                              //Creamos el numIdRow más grande, pero antes buscamos en cuál se quedó
                for (i=0; i<numFilas; i++){
                    n = parseInt( trs[i].id.replace(nombreTabla+"_f","") );
                    if (n>numIdRow)
                        numIdRow = n;
                }
            }
            posInsert = index+1;
            //----------------------------------------------------------------------
            var posColsOcultas = new Array();                                   //Un arreglo donde nos quedará las posiciones de las columnas que quederán como ocultas
            i=0; 
            for (var t in tabla[0]) i++;                                     //Contamos cuántas columnas trae la tabla
            for (var t in tabla[0])                                          //Parece ser que saca los nombres del arreglo empezando por el último elemento
                posColsOcultas[--i]=t;                                       //Inicializamos por default que todas las posiciones no serán visibles
            //----------------------------------------------------------------------
            $.each(tabla,function(f,valor) {
                htmlRow = thisClass._makeHtmlRow(thisClass, nombreTabla, f, numIdRow, valor, colsNameToShow, claseColumnas, textAlign, posColsOcultas);
                if (index===-1)
                    $("#"+nombreTabla).append(htmlRow);
                else{
                    newRow = document.getElementById(nombreTabla).insertRow(posInsert++);
                    newRow.innerHTML = htmlRow;
                    $(newRow).attr("class","fila"+nombreTabla);
                    $(newRow).attr("id",nombreTabla+"_f"+numIdRow);
                }
                numIdRow++;
                //====================== Alineación de columnas ======================\\
                if (textAlign!==null && typeof textAlign!=="undefined")
                {
                    for (c=0; c<textAlign.length; c++)
                        if ((typeof textAlign[c]!=="undefined") && textAlign[c]!=="")
                            $(".col"+nombreTabla+"_col"+c).css("text-align",textAlign[c]);
                }
                //====================== Eventos en cada celda ======================\\
                if (eventsForDrawTable!==null)
                {
                    eventsForDrawTable (numIdRow,c);
                }
                //===================================================================\\
            });
            
            if (selOnClick)
            {
                $('.fila'+nombreTabla).on("click",function(event){
                    if (modoSeleccion===null || typeof modoSeleccion==="undefined" || modoSeleccion==="default" || modoSeleccion===""){
                        $('#'+nombreTabla+' .filaSel').removeClass('filaSel');      //Quitamos el elemento sombreado anteriormente
                        $(this).addClass("filaSel");                                //Sombreamos la fila
                    }else if (modoSeleccion==="ctrlClick"){
                        if (!event.ctrlKey)
                            $('#'+nombreTabla+' .filaSel').removeClass('filaSel');  //Quitamos el elemento sombreado anteriormente
                        $(this).addClass("filaSel");                                //Sombreamos la fila
                    }else if (modoSeleccion==="shiftClick"){
                        //Falta implementar
                    } else if (modoSeleccion==="ctrlOShiftClick"){
                        //Falta implementar
                    }

                    if (actionsOnClick!==null)
                        actionsOnClick ($(this).index(),nombreTabla,$(this).attr("id"));
                });

                /*$('.fila'+nombreTabla).dblclick(function(event){
                    alert("Dio doble click");
                });*/

                //Escucha y reacciona el evento cuando se pulsa una tecla
                /*if (window.document.addEventListener){
                   // document.activeElement //<--- Para saber quién tiene el foco
                   window.document.addEventListener("keydown", thisClass._myFunction2, false);
                }else
                   window.document.attachEvent("onkeydown", thisClass._myFunction2);*/
            }
        }else
            if (this.getNumRows (nombreTabla)<0)
                $("#"+nombreTabla).append("<tr class='fila"+nombreTabla+"'><td  colspan='"+ colsNameToShow.length+"'> No hay datos que mostrar.</td></tr>");
    };
    
    /**
    * Devuelve el nombre (id) de la celda (td) en la posición de fila y posición o nombre de columna específico, ya sea visible u oculta
    * @param {String} nombreTabla El nombre de la tabla con la que se desea trabajar
    * @param {Number} rowIndex La posición de la fila.
    * @param {Number/String} indexColName La columna. Toma dos valores: Numérico o String. Cuando es numérico hará referencia a la posición de la columna visible, cuando es String hará referencia al nombre de la columna visible u oculta.
    */
   this.getIdNameCell = function (nombreTabla, rowIndex, indexColName)          //<<--OK-->>
   {
        var id="";
        tr = $("#"+nombreTabla+" >tbody").children("tr")[rowIndex];
       
        if (isNaN(indexColName)){
            // ---------- Forma 1 ---------- \\
            id = $(tr).children("[class*='_"+indexColName+"_']").attr("id");                                          //En base al nombre de columna
            // ---------- Forma 2 ---------- \\
            //id = $($($("#"+nombreTabla+" >tbody >tr >td[class*='_"+indexColName+"_']"))[rowIndex]).attr("id");      //En base a nombre de columna
            // ---------- Forma 3 ---------- \\
            //id = $("#"+($(tr).attr("id"))+ " td[class*='_"+indexColName+"_']").attr("id");                          //En base a nombre de columna
        }else{
            // ---------- Forma 1 ---------- \\
            id = $(tr.cells[indexColName]).attr("id");                          //Si es numérico, se sobreentiende que es únicamente para las columnas visibles y el número indica la posición de columna a elegir
            // ---------- Forma 2 ---------- \\
            //id = $($(tr).children("td")[indexColName]).attr("id");                                                  //En base a posición de columna
        }
       return id;
    };
    
    /**
     * Limpia e inserta un arreglo de datos a los combobox de la columna especeficada.
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea establecer los datos.
     * @param {Number} colIndex La posición de la columna donde están los combobox a los que se les establecerán los datos.
     * @param {Array} dataArray Arreglo de datos que se le establecerá a cada combobox.
     * @param {Array} valuesArray Arreglo de datos que que se le establecerá en el atributo value del combobox. 
     *                Si valueArray no está definido o es null, se establecerá por default en el atributo value el número de índice correspondiente.
     *                Si valueArray está definido y dataArray no, entonces el mismo dato para el atributo value se establecerá para el option.
     * @param {Boolean} insertDataBlank Booleano para indicar si se require que se inserte un item en blanco al principio de los datos del combobox.
     * @param {Boolean} isDataBlankEnabled Booleano para indicar si el item en blanco insertado es elegible.
     * @param {Array} titlesArray Arreglo de datos que que se establecera en el atributo title del combobox.
     * @param {Number} rowIniIndex Si se especifica este campo, el combobox será agregado a partir de la posición especificada.
     * @param {Number} rowEndIndex Si se especifica este campo, el combobox terminará de agregarse en esta posición. Si no se especifica terminará de agregarse en las filas restantes. El valor es tomado en cuenta siempre y cuando rowIniIndex esté especificado
     * @param {Number/JSON} setSelectedIndex Establece como seleccionado el índice del o los combobox.
     *                Si es un número, selecciona el índice en dicho número de cada combobox de toda la columna
     *                Si es un JSON, especificar como clave:valor de la siguiente manera: 
     *                  - Para seleccionar por índice usar JSON: {index:[[f1,i1],[f2,i2],...,[fn,im]]} donde index es la palabra recervada para indicar que la selección será por índice que tiene como valor un ARRAY de pares el cual un número que indica el número de fila y y otro el índice a seleccionar en dicha fila. 
     *                  - Para seleccionar por value usar JSON: {value:[[f1],[],...,[]]} OJO: FALTA IMPLEMENTAR
     */
    this.setDataToComboboxCol = function(nombreTabla, colIndex, dataArray, valuesArray, insertDataBlank, isDataBlankEnabled, titlesArray, rowIniIndex, rowEndIndex, setSelectedIndex)
    {
        var title, value, option, numDatos, rowIni=0, rowFin;
        
        if ((typeof dataArray==="undefined" || dataArray===null) && (typeof valuesArray==="undefined" || valuesArray===null) )
            alert("ERROR\n\nError en setDataToComboboxCol.\n\nNo ha indicado datos en dataArray o valuesArray para la tabla '"+nombreTabla+"'");
        else if (typeof dataArray!=="undefined" && dataArray!==null && typeof valuesArray!=="undefined" && valuesArray!==null && dataArray.length!=valuesArray.length)
            alert("ERROR\n\nError en setDataToComboboxCol.\n\nNo se ha podido cargar el combo a la tabla '"+nombreTabla+"'\n\nporque valuesArray tiene longitud diferente a dataArray.");
        else if (typeof titlesArray!=="undefined" && titlesArray!==null && ( (typeof dataArray!=="undefined" && dataArray!==null && dataArray.length!=titlesArray.length) || (typeof valuesArray!=="undefined" && valuesArray!==null && valuesArray.length!=titlesArray.length)))
            alert("ERROR\n\nError en setDataToComboboxCol.\n\nNo se ha podido cargar el combo a la tabla '"+nombreTabla+"'\n\nporque titlesArray tiene longitud diferente a dataArray o valuesArray.");
        
        else{
            //-------------- Para limpiar sólo la columna de las filas especificadas ------------------
            if (typeof(rowIniIndex)!=="undefined" && rowIniIndex!==null){
                rowIni = rowIniIndex;
                if (typeof(rowEndIndex)!=="undefined" && rowEndIndex!==null)
                    rowFin = rowEndIndex;
                else
                    rowFin = this.getNumRows(nombreTabla)-1;
                for (var i=rowIni; i<=rowFin; i++){
                    $("#"+nombreTabla+" #"+nombreTabla+"_td_f"+i+"_c"+colIndex+" select option:selected").remove();  //Limpiamos los combos de la columna en las filas especificadas
                    if (typeof(insertDataBlank)!== "undefined" && insertDataBlank!==null && insertDataBlank === true)
                        $("#"+nombreTabla+" #"+nombreTabla+"_td_f"+i+"_c"+colIndex+" select").append("<option "+(isDataBlankEnabled===true?"":"disabled")+" value=''></option>"); //Agregamos una opción en blanco
                }
            //--------------------------- Para limpiar toda la columna --------------------------------
            }else{
                $("#"+nombreTabla+" .col"+nombreTabla+"_col"+colIndex+" select option:selected").remove();  //Limpiamos todos los combos de la columna
                if (typeof insertDataBlank!== "undefined" && insertDataBlank!==null && insertDataBlank === true)
                    $("#"+nombreTabla+" .col"+nombreTabla+"_col"+colIndex+" select").append("<option "+(isDataBlankEnabled===true?"":"disabled")+" value=''></option>"); //Agregamos una opción en blanco
            }
            
            numDatos = ((typeof dataArray!=="undefined" && dataArray!==null )?dataArray.length:valuesArray.length);
            for (var i=0; i<numDatos; i++){                                           //Insertamos sus datos a los combos correspendientes
                title="";
                value="value="+i;

                if (typeof titlesArray!== "undefined" && titlesArray!==null)
                    title="title='"+titlesArray[i]+"'";
                if (typeof valuesArray!== "undefined" && valuesArray!==null)
                    value="value='"+valuesArray[i]+"'";
                option = (typeof dataArray!=="undefined" && dataArray!==null )?dataArray[i]:valuesArray[i];

                if (typeof(rowIniIndex)==="undefined" || rowIniIndex===null)
                    $("#"+nombreTabla+" .col"+nombreTabla+"_col"+colIndex+" select").append("<option "+value+" "+title+">"+option+"</option>");     //Arreglo de datos a toda la columna
                else
                    for (var j=rowIni; j<=rowFin; j++)
                        $("#"+nombreTabla+" #"+nombreTabla+"_td_f"+j+"_c"+colIndex+" select").append("<option "+value+" "+title+">"+option+"</option>");     //Arreglo de datos por fila
                    
            }
            
            if (typeof setSelectedIndex!=="undefined" && setSelectedIndex!==null)
            {
                if (!isNaN(setSelectedIndex))
                    $($("#"+nombreTabla+" .col"+nombreTabla+"_col"+" > select")).prop('selectedIndex', setSelectedIndex);
                else{
                    if (typeof setSelectedIndex.index != "undefined")
                        for (var i=0; i<setSelectedIndex.index.length; i++)
                            $($("#"+nombreTabla+" #"+nombreTabla+"_td_f"+setSelectedIndex.index[i][0]+"_c"+colIndex+" > select")).prop('selectedIndex', setSelectedIndex.index[i][1]);
                }
            }
        }
    };
    
    /**
     * Crea el html para la fila de una tabla. NOTA: Este es un método privado exclusivo para esta clase
     */                                                                         //<<--OK-->>
    this._makeHtmlRow = function (thisClass, nombreTabla, f, numIdRow, fila, colsNameToShow, claseColumnas, textAlign, posColsOcultas)
    {
        var c, k, p, talig, htmlRow, dato, celda, colName, dataFila="", columnas="", columnasOcultas="";
        for (c=0; c< colsNameToShow.length; c++){                               //Obtenenmos las columnas que serán visibles al usuario
            if (colsNameToShow!==null && typeof fila[colsNameToShow[c]]==="undefined") 
                celda=colsNameToShow[c]+"/Indef";
            else {
                //---------- Extraemos el dato que vamos a poner en la tabla ----------\\
                if (colsNameToShow===null){
                    k=0; 
                    for (var name in fila) { if (k++===c) { dato = fila[name]; colName = name; break;  } };
                }else {
                    colName=colsNameToShow[c];
                    dato = (fila[colName]===null || fila[colName]==="null")?"":(""+fila[colName]).trim();
                }
                if (f===0)                                                      //Con analizar la primer fila nos será suficiente para detectar qué columnas serán las ocultas
                {  
                    p = thisClass._posOf(posColsOcultas, colName);
                    posColsOcultas.splice(p,1); 
                }
                //----------------------------------------------------------------------\\
                if (claseColumnas===null || claseColumnas[c]==="default" || claseColumnas[c]==="")
                    celda = dato;
                else if (claseColumnas[c]==="checkbox")
                    celda = "<input type='checkbox' id='"+nombreTabla+"_chk_f"+numIdRow+"_c"+c+"' name='"+nombreTabla+"_chk_f"+numIdRow+"_c"+c+"' value='"+dato+"'>";
                else if (claseColumnas[c]==="radiobutton")
                    celda = "<input type='radio'    id='"+nombreTabla+"_rbn_f"+numIdRow+"_c"+c+"' name='"+nombreTabla+"_rbg_c"+c+"'        value='"+dato+"'/>";
                else if (claseColumnas[c]==="textbox" || claseColumnas[c]==="textarea"){
                    talig="";
                    if (textAlign!==null && (typeof textAlign!=="undefined") && (typeof textAlign[c]!=="undefined") && textAlign[c]!=="")
                        talig = "style='text-align:"+textAlign[c]+";'";
                    
                    if (claseColumnas[c]==="textbox")
                        celda = "<input type='text' id='"+nombreTabla+"_txt_f"+numIdRow+"_c"+c+"' name='"+nombreTabla+"_txt_f"+numIdRow+"_c"+c+"' value='"+dato+"' "+talig+">";
                    else if (claseColumnas[c]==="textarea")
                        celda = "<textarea id='"+nombreTabla+"_txta_f"+numIdRow+"_c"+c+"' name='"+nombreTabla+"_txta_f"+numIdRow+"_c"+c+"' "+talig+">"+dato+"</textarea>";

                }else if (claseColumnas[c]==="combobox")
                    celda = "<select                id='"+nombreTabla+"_cbx_f"+numIdRow+"_c"+c+"' name='"+nombreTabla+"_cbx_f"+numIdRow+"_c"+c+"' > <option value='"+dato+"'>"+dato+"</option> </select>";
            }
            columnas+="<td class='col"+nombreTabla+"_col"+c+" "+nombreTabla+"_"+colName+"_f"+numIdRow+"' id='"+nombreTabla+"_td_f"+numIdRow+"_c"+c+"'>"+celda+"</td>";
        }
        for (c=0; c<posColsOcultas.length; c++)                                 //Obtenemos las columnas que serán ocultas al usuario
            columnasOcultas+="<input type='hidden' class='"+nombreTabla+"_"+posColsOcultas[c]+"_f"+numIdRow+"' id='"+nombreTabla+"_"+posColsOcultas[c]+"_f"+numIdRow+"' value='"+fila[posColsOcultas[c]]+"'/> ";
        
        htmlRow = "<tr class='fila"+nombreTabla+"' id='"+nombreTabla+"_f"+numIdRow+"'>  "+columnas+" "+columnasOcultas+"</tr>";
        
        return htmlRow;
    };
    
    /**
    * Busca un elemento dentro de un arreglo. NOTA: Este es un método privado exclusivo para esta clase
    * @param {Array} array El arreglo de datos
    * @param {String} elementoABuscar El elemnto que se quiere buscar en el arreglo
    * @returns {Number} Retorna la posición en donde se encontró el elemnto, -1 si no lo encuentra;
    */
   this._posOf = function(array, elementoABuscar)                               //<<--OK-->>
   {
       var index=-1;
       if( typeof Array.prototype.indexOf !== "function") {                     // si no tenemos disponible indexOf lo simulamos 
           var arrayTxt = array.join("::");                                     // unión de todo el array 
           var re = new RegExp("^(([^(::)]+::)*)"+elementoABuscar, "");         // expresión: n elementos y luego el nuestro 
           var m = arrayTxt.match(re);                                          // [0]:original; [1]:n elementos;  
           index = m[1].split("::").length-1;                                   // Contamos cuantos elementos hay en [1] 
       } 
       else { 
           index = array.indexOf(elementoABuscar); 
       }  
       return index;
   };
   
   /**
    * Devuelve el número que tiene en el atributo id, es decir, cuando se agrega un tr a la tabla se le asigna un nombre de id, compuesto por el nombre de la tabla, luego '_f' y despues un número progresivo, a ese número progresivo le llamamos numIdRow
    */
   this._numIdRow = function(nombreTabla, index)                                //<<--ok-->>
   {
       tr = $("#"+nombreTabla+" >tbody").children("tr")[index];
       return $(tr).attr("id").replace("tblPreinscripcion_f","");
   };
   

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Devuelve los datos visibles al usuario de la fila especificada mediante su nombre.
     * @param {type} nombreFila El nombre de la fila a la que se le quiere extaer los datos.
     * @returns {Array} Un arreglo con el valor de cada columna de la fila especificada.
     */
    this.getRowByRowName = function(nombreFila)
    {
        var celdas = new Array();

        $("#"+nombreFila).children("td").each(function (index){
            if ( this.innerHTML.indexOf('type="checkbox"') >= 0 )
                celdas[index]=$('#'+$(this).attr('id')+' input:checkbox').is(':checked');   //Falta probarlo y corregir si está mal
            else if ( this.innerHTML.indexOf('type="radio"') >= 0 )
                celdas[index]=$('#'+$(this).attr('id')+' input:radio').is(':checked');      //Falta probarlo y corregir si está mal
            else if ( this.innerHTML.indexOf('type="text"') >= 0 )
                celdas[index]=$('#'+$(this).attr('id')+' input:text').val();
            else if ( this.innerHTML.indexOf('<select ') >= 0 )
                celdas[index]=$('#'+$(this).attr('id')+' option:selected').text();          //Falta probarlo y corregir si está mal
            else if ( this.innerHTML.indexOf('<textarea ') >= 0 )
                celdas[index]=$('#'+$(this).attr('id')+' textarea').val();                  //Falta probarlo y corregir si está mal
            else if ( this.innerHTML.indexOf('<label ') >= 0 )
                celdas[index]=$('#'+$(this).attr('id')+' label').html();                    //Falta probarlo y corregir si está mal
            else
                celdas[index]=$(this).text().trim();
        });
        return celdas;
    };


    /**
     * Devuelve los datos visibles al usuario de la fila con efecto de selección.
     * @param {type} nombreTabla El nombre de la tabla a la que se le desea extraer la fila.
     * @returns {Array} Un arreglo con el valor de cada columna de la fila seleccionada.
     */
    this.getSelectedRow_deprecated = function(nombreTabla)
    {
        var celdas = new Array();

        $('#'+nombreTabla+' .filaSel td').each(function (index) {
            if ( this.innerHTML.indexOf('type="checkbox"') > 0 )
               celdas[index]=$('#'+$(this).attr('id')+' input:checkbox').is(':checked');   //Falta probarlo y corregir si está mal
            else if ( this.innerHTML.indexOf('type="radio"') > 0 )
                celdas[index]=$('#'+$(this).attr('id')+' input:radio').is(':checked');      //Falta probarlo y corregir si está mal
            else if ( this.innerHTML.indexOf('type="text"') > 0 )
                celdas[index]=$('#'+$(this).attr('id')+' input:text').val();
            else if ( this.innerHTML.indexOf('<select ') > 0 )
                celdas[index]=$('#'+$(this).attr('id')+' option:selected').text();          //Falta probarlo y corregir si está mal
            else
                celdas[index]=$(this).text().trim();
        });
         return celdas;
    };
    
    /**
     * Devuleve la tabla con los datos que son ocultos al usuario
     * @param {type} nombreTabla El nombre de la tabla a la que se le desea extraer los datos ocultos.
     * @param {type} nameHiddenColsToGet Un arreglo con los nombres de cada columna oculta a extraer.
     * @returns {Array} Retorna un arreglo de string separando las columnas por el caracter ~
     */
    this.getHiddenCols_deprecated = function(nombreTabla, nameHiddenColsToGet)
    {
        var c=0;
        var tabla = new Array();
        var fila;
        //var numFilas=$('#tblTabla >tbody >tr').length;

        $("#"+nombreTabla+" >tbody").children("tr").each(function (index){
            fila = "";
            if (nameHiddenColsToGet!==null && typeof nameHiddenColsToGet!=="undefined"){
                for (c=0; c<nameHiddenColsToGet.length; c++){                   //Obtenemos cada columna específicada en el arreglo namesColsToGet
                    fila+= (c===0)?"":"~";
                    fila+= $("#"+$(this).attr("id")+" #"+nombreTabla+"_"+nameHiddenColsToGet[c]+"_f"+index).val();
                }
            }else {                                                                 
                c=0;
                $("#"+$(this).attr("id")+" input[type='hidden']").each(function (){    //Obtenemos todas las columnas ocultas
                    fila+= (c===0)?"":"~";
                    fila = $(this).val();
                });
            }
            tabla[index]=fila;
        });
        return tabla;
    };

    /**
     * Devuleve la tabla con los datos que son visibles y ocultos al usuario
     * @param {type} nombreTabla El nombre de la tabla a la que se le desea extraer los datos.
     * @param {type} colPosToGet Un arreglo de numeros enteros que indican la posición de columna(s) a extraer. Enviar null si no se desea especificar un arreglo.
     * @param {type} nameHiddenColsToGet Un arreglo con los nombres de cada columna oculta a extraer. Enviar null si no se desea especificar un arreglo.
     * @returns {Array} Retorna un arreglo de string separando las columnas por el caracter ~
     */
    this.getVisibleAndHiddenCols_deprecated = function(nombreTabla, colPosToGet, nameHiddenColsToGet)
    {
        var tabla = new Array();
        var thisClass = this;
        var fila;

        $("#"+nombreTabla+" >tbody").children("tr").each(function (index){
            fila = thisClass.getVisibleAndHiddenRow (nombreTabla, index, colPosToGet, nameHiddenColsToGet);
            tabla[index]=fila;
        });
        return tabla;
    };
    
    /**
     * Por default devuleve un string con los datos de cada columna especificada que son visibles y ocultos al usuario separando cada columna con el caracter: ~. 
     * Si se especifica en formato la palabra array, devuelve un arreglo en lugar de string
     * @param {type} nombreTabla El nombre de la tabla a la que se le desea extraer los datos.
     * @param {type} index Posición de la fila que se desa extraer.
     * @param {type} colPosToGet Un arreglo de numeros enteros que indican la posición de columna(s) a extraer. Enviar null si no se desea especificar un arreglo.
     * @param {type} nameHiddenColsToGet Un arreglo con los nombres de cada columna oculta a extraer. Enviar null si no se desea especificar un arreglo.
     * @param {type} formato El formato en el que se desa que devuelva la fila. Si no se especifica devuelve un String separado por ~, si se especifica "array" devuelve un arreglo
     * @returns {Array} Retorna un arreglo de string separando las columnas por el caracter ~
     */
    this.getVisibleAndHiddenRow_deprecated = function(nombreTabla, index, colPosToGet, nameHiddenColsToGet, formato)
    {
        var c=0;
        var tr,tds, fila;

        tr = $("#"+nombreTabla+" >tbody").children("tr")[index];
        fila = "";
        if (colPosToGet!==null && typeof colPosToGet!=="undefined") {
            tds = $(tr).children("td");
            for (c=0; c<colPosToGet.length; c++){                                   //Obtenemos cada columna específicada en el arreglo namesColsToGet
                fila+= (c===0)?"":"~";
                if (typeof tds[colPosToGet[c]] !== "undefined"){
                    if ( tds[colPosToGet[c]].innerHTML.indexOf('type="checkbox"') > 0 )
                        fila+=$(tds[colPosToGet[c]]).children("input[type='checkbox']").is(':checked');   //Falta probarlo y corregir si está mal
                    else if ( tds[colPosToGet[c]].innerHTML.indexOf('type="radio"') > 0 )
                        fila+=$(tds[colPosToGet[c]]).children("input[type='radio']").is(':checked');      //Falta probarlo y corregir si está mal
                    else if ( tds[colPosToGet[c]].innerHTML.indexOf('type="text"') > 0 )
                        fila+=$(tds[colPosToGet[c]]).children("input[type='text']").val();
                    else if ( tds[colPosToGet[c]].innerHTML.indexOf('<select ') > 0 )
                        fila+=$('#'+$(tds[colPosToGet[c]]).attr('id')+' option:selected').text();         //Falta probarlo y corregir si está mal
                    else
                        fila+=$(tds[colPosToGet[c]]).text().trim();
                }else
                    fila+="Col"+colPosToGet[c]+"_undefined";
            }
        }
        if (nameHiddenColsToGet!==null && typeof nameHiddenColsToGet!=="undefined"){
            for (c=0; c<nameHiddenColsToGet.length; c++){                            //Obtenemos cada columna específicada en el arreglo namesColsToGet
                fila+= (c===0)?"":"~";
                fila+= $("#"+$(tr).attr("id")+" #"+nombreTabla+"_"+nameHiddenColsToGet[c]+"_f"+index).val();
            }
        }

        if (typeof formato!=="undefined" && formato!==null && formato==="array")
            return fila.split("~");
        return fila;
    };
    
    
   /**
     * Devuleve la tabla con los datos que son visibles al usuario
     * @param {type} nombreTabla El nombre de la tabla a la que se le desea extraer los datos.
     * @param {type} colPosToGet Un arreglo de numeros enteros que indican la posición de columna(s) a extraer.
     * @returns {Array} Retorna un arreglo de string separando las columnas por el caracter ~
     */
    this.getTable_deprecated = function(nombreTabla, colPosToGet)
    {
        var tds, c=0, numcols, j=0;
        var tabla = new Array();
        var fila, existeCol=true;
        //var numFilas=$('#tblTabla >tbody >tr').length;

        $("#"+nombreTabla+" >tbody").children("tr").each(function (index){
            tds = $(this).children("td");

            numcols = (colPosToGet!==null && typeof colPosToGet!=="undefined") ? colPosToGet.length: tds.length;

            fila = "";
            for (j=0; j<numcols; j++){
                if (colPosToGet!==null && typeof colPosToGet!=="undefined") {       //Si se envió un arreglo con las posiciones a obtener
                    if (typeof tds[colPosToGet[j]]==="undefined") {
                        existeCol = false;
                    }else{
                        c = colPosToGet[j]; existeCol = true;
                    }
                }else
                    c=j;

                if (existeCol){                                                   //Por default obtenerFila es true, a menos que se envíe un vector de posiciones
                    fila+= (fila==="")?"":"~";
                    if ( tds[c].innerHTML.indexOf('type="checkbox"') > 0 )
                        fila+=$(tds[c]).children("input[type='checkbox']").is(':checked');   //Falta probarlo y corregir si está mal
                    else if ( tds[c].innerHTML.indexOf('type="radio"') > 0 )
                        fila+=$(tds[c]).children("input[type='radio']").is(':checked');      //Falta probarlo y corregir si está mal
                    else if ( tds[c].innerHTML.indexOf('type="text"') > 0 )
                        fila+=$(tds[c]).children("input[type='text']").val();
                    else if ( tds[c].innerHTML.indexOf('<select ') > 0 )
                        fila+=$('#'+$(tds[c]).attr('id')+' option:selected').text();         //Falta probarlo y corregir si está mal
                    else
                        fila+=$(tds[c]).text().trim();
                }else
                    fila+="Col"+colPosToGet[j]+"_undefined";
            }
            tabla[index]=fila;
            //}
        });
        return tabla;
    };
    
    this.getRowByColName_deprecated = function(nombreTabla, rowIndex, colsName, formato)
    {
        var c=0;
        var tr,td, fila;

        tr = $("#"+nombreTabla+" >tbody").children("tr")[rowIndex];
        fila = "";
        if (colsName!==null && typeof colsName!=="undefined"){
            for (c=0; c<colsName.length; c++){                            //Obtenemos cada columna específicada en el arreglo namesColsToGet
                fila+= (fila==="")?"":"~";
                td = $("#"+$(tr).attr("id")+" ."+nombreTabla+"_"+colsName[c]+"_f"+rowIndex);
                if (typeof td !== "undefined" && td.length){
                    if ( td[0].outerHTML.indexOf('type="') > 0 || td[0].outerHTML.indexOf('<select') > 0 )
                    {
                        if ( td[0].outerHTML.indexOf('type="text"') > 0 )
                            fila+=$(td).children("input[type='text']").val();
                        else if ( td[0].outerHTML.indexOf('type="hidden"') > 0 )
                            fila+=td.val();         //Falta probarlo y corregir si está mal
                        else if ( td[0].outerHTML.indexOf('type="checkbox"') > 0 )
                            fila+=$(td[0]).children("input[type='checkbox']").is(':checked');   //Falta probarlo y corregir si está mal
                        else if ( td[0].outerHTML.indexOf('type="radio"') > 0 )
                            fila+=$(td[0]).children("input[type='radio']").is(':checked');      //Falta probarlo y corregir si está mal
                        else if ( td[0].outerHTML.indexOf('<select ') > 0 )
                            fila+=$('#'+$(td[0]).attr('id')+' option:selected').text();         //Falta probarlo y corregir si está mal
                        else
                            fila+=colsName[c]+"_objUnknown";
                    }else
                        fila+=$(td).text();
                }else
                    fila+=colsName[c]+"_colUndefined";
            }
        }

        if (typeof formato!=="undefined" && formato!==null && formato==="array")
            return fila.split("~");
        return fila;
    };
    
    
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------- FUNCIONES EN DESARROLLO -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Agrega fila(s) a una tabla
     * @param {String} nombreTabla El nombre de la tabla a la que se le desea agregar la fila
     * @param {Number} index La posición de la fila en donde se desea insertar. Enviar -1 si se desea que se agregue al final.
     */
    this._addRow_badFunc = function (nombreTabla, index, tabla, colsNameToShow )
    {
        var numFilas=$("#"+nombreTabla+" tr").length;
        document.getElementById(nombreTabla).insertRow(index).innerHTML = '<td>'+numFilas+'</td><td>'+numFilas+'</td><td>'+numFilas+'</td>';
        $("#"+nombreTabla).append("<tr class='fila"+nombreTabla+"' id='"+nombreTabla+"_f"+f+"'>  "+columnas+" "+columnasOcultas+"</tr>");
        //====================== Alineación de columnas ======================\\
        if (textAlign!==null && typeof textAlign!=="undefined")
        {
            for (c=0; c<textAlign.length; c++)
                if ((typeof textAlign[c]!=="undefined") && textAlign[c]!=="")
                    $(".col"+nombreTabla+"_col"+c).css("text-align",textAlign[c]);
        }
    };
    /*Ejecuta una acción diferente según la tecla del teclado que presiones.
    el 40 es la flecha abajo, el 38 la flecha arriba y el 13 el enter. Mueve el estilo
    de la fila, arriba o abajo, según el botón pulsado. Con el botón enter se muestra
    la información de la fila seleccionada*/
    this._myFunction2 = function (evnt, nombreTabla)                                         //<<--RC-->>
    {
        var ev = (evnt) ? evnt : event;
        var code=(ev.which) ? ev.which : event.keyCode;
        if (code == 40) {                                                       //Flecha abajo
            if ((document.getElementById(nombreTabla)).getElementsByClassName("filaSel").length > 0) {
                var nextElement, element = (document.getElementById(nombreTabla)).getElementsByClassName("filaSel");
                var numFilas = document.getElementById("tbody").rows.length;
                //var num = (parseInt(element[0].id) + 1).toString();
                var num = $(element).index() + 1 ;
                if (num < numFilas)
                    nextElement = document.getElementById("tbody").rows[num];
                else
                    nextElement = document.getElementById("tbody").rows[0];
                this._myFunction(nextElement);//document.getElementById(num)
            }
        }
        else if (code == 38) {                                                  //Flecha arriba
            if ((document.getElementById(nombreTabla)).getElementsByClassName("filaSel").length > 0) {
                    var nextElement, element = (document.getElementById(nombreTabla)).getElementsByClassName("filaSel");
                    var index = $(element).index() - 1;
                    //var num = (parseInt(element[0].id) - 1).toString();
                    if (index>=0){
                        nextElement = document.getElementById("tbody").rows[index];
                    }else{
                        var numFilas = document.getElementById("tbody").rows.length;
                        nextElement = document.getElementById("tbody").rows[numFilas-1];
                    }
                    this._myFunction(nextElement);    //document.getElementById(element[0].id)
            }
        }
        else if (code == 13 ) {
            if ((document.getElementById(nombreTabla)).getElementsByClassName("filaSel").length > 0) {
                var element = (document.getElementById(nombreTabla)).getElementsByClassName("filaSel");
                var info = element[0].cells[0].innerText;
                info += " "+element[0].cells[1].innerText;
                info += " "+element[0].cells[2].innerText;
                //info += " "+element[0].cells[3].innerText;
                //info += " "+element[0].cells[4].innerText;
                alert(info);
            }

        }
    };
    
    /*Selecciona la fila y se cambia el estilo cuando le das click con el ratón*/
    this._myFunction = function (x)                                             //<<--RC-->>
    {
        if ((document.getElementById(nombreTabla)).getElementsByClassName("filaSel").length > 0) {
            var element = (document.getElementById(nombreTabla)).getElementsByClassName("filaSel");
            element[0].className = "";
        }
        x.className="filaSel";
    };
}