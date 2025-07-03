/* 
    Creado el : 01/12/2014, 20:50:49 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

function Mensajes ()
{
    this.confirmDialog = function (tipo, texto1)
    {
        if (tipo==="MARCAR_PAGADO")
            return confirm("¿Confirma que desea marcar como pagado al proveedor "+texto1+"?"); //"Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        if (tipo==="GUARDAR_CAMBIOS")
            return confirm("Ha efectuado cambios en los datos ¿Desea guardarlos antes de continuar?"); //"Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        else if (tipo==="ELIMINAR")
            return confirm("¿Confirma que desea eliminar "+texto1+" seleccionado?");//, "Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        else if (tipo==="GUARDAR_SALIR")
            return confirm("Ha efectuado cambios en los datos ¿Desea guardarlos antes de que se cierre la ventana?");//, "Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    };
    
    this.General = function(tipo, texto1, texto2)
    {
        if (tipo === "GENERAL")
            alert(texto1);
        else if (tipo === "GUARDADO_EXITOSO")
            alert("INFORMACIÓN\n\nLos datos se han guardado satisfactoriamente");
        else if (tipo === "PROCESO_EXITOSO")
            alert("INFORMACIÓN\n\nEl proceso se ha realizado satisfactoriamente");
        else if (tipo === "NO_SELEC")
            alert ("PRECAUCIÓN\n\nPrimero seleccione un"+texto1+" de la lista para poder "+texto2+".");
        else if (tipo === "ESPECIFIQUE_DATO")
            alert("PRECAUCIÓN\n\nEspecifique "+texto1+".");
        else if (tipo === "ESPECIFIQUE_DATO_CORRECTO")
            alert("PRECAUCIÓN\n\nEspecifique "+texto1+" válido.");
        else if (tipo === "ERROR_AJAX")
            alert( "ERROR\n\nLa comunicación con el servidor no se ha podido establecer o se perdió.\nEs probable que la velocidad de su internet esté baja o el servidor esté ocupado.\n\nPorfavor intente de nuevo.");
        else if (tipo === "CAMPO_VACIO")
            alert("PRECAUCIÓN\n\nEl campo " + texto1 +" no puede estar vacío.");
        else if (tipo === "FORMATO_FECHA")
            alert("ERROR\n\nEl formato de fecha no es correcto.\nUse formato: dd/MM/aaaa, o si no desea especificar año use: dd/MM/xxxx");
        else if (tipo === "ERROR_GUARDAR")
            alert("ERROR\n\nNo se pudo guardar la información.");
        else if (tipo === "SOLO_TIENE_PERMITIDO")
            alert("PRECAUCIÓN\n\nPor el momento solo puede usar "+texto1);
        else if (tipo === "CALIF_INVALIDA")
            alert("ERROR\n\nCalificación no válida: "+texto1);
        else if (tipo === "CALIF_INVALIDA_CIC18")
            alert("ERROR\n\nCalificación no válida: "+texto1+", ingrese enteros de 1 solo dígito.");
        else if (tipo === "CALIF_INVALIDA_CIC21")
            alert("ERROR\n\nCalificación no válida: "+texto1+", la calificación debe ser mayor o igual a 5");
        else if (tipo === "ELIMINADO")
            alert("INFORMACIÓN\n\nEl registro se ha eliminado correctamente.");//, "INFORMACION",JOptionPane.ERROR_MESSAGE);
        else if (tipo === "NADA_QUE_ELIMINAR")
            alert("PRECAUCIÓN\n\nNo hay registros qué eliminar.");
        else if (tipo === "NADA_QUE_GUARDAR")
            alert("PRECAUCIÓN\n\nNo hay registros qué guardar.");
        else if (tipo === "NO_HAY_DATO")
            alert("PRECAUCIÓN\n\nNo hay "+texto1+". "+texto2);
        else if (tipo === "EXPORTADO")
            alert("INFORMACIÓN\n\nLos datos para que pueda exportar se han extraído satisfactoriamente.");
        else if (tipo === "OFICIALIZADO")
            alert("INFORMACIÓN\n\nLa oficialización se ha establecido con éxito.");
        else if (tipo === "DESOFICIALIZADO")
            alert("INFORMACIÓN\n\nLa desoficialización se ha establecido con éxito.");
        else if (tipo==="EXCEL_EN_CREACION")
            alert("INFORMACIÓN\n\nEl archivo empezará a crearse...\nPor favor de click en aceptar y espere a que se muestre el documento.");
        else if (tipo==="REPORTE_EN_CREACION")
            alert("INFORMACIÓN\n\nEl reporte empezarán a crearse...\nPor favor de click en aceptar y espere a que se muestre el documento.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE
        else if (tipo==="FORMATO_INCORRECTO")
            alert("ERROR\n\nEl formato ingresado para "+texto1+" no es correcto.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE
        else if (tipo==="INFO_NO_ENCONTRADA")
            alert("INFORMACIÓN\n\nNo se encontró información.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE
        else if (tipo === "FIRMAYFOLIADO")
            alert("INFORMACIÓN\n\nSu información se ha enviado para ser procesada, verifique nuevamente en un lapso de 24hrs.");//, "INFORMACION",JOptionPane.ERROR_MESSAGE);
        else if (tipo === "ELEGIR_OPCION")
            alert("ERROR\n\nDebe seleccionar la opcion "+texto1+".");
        else if (tipo === "FECHA_INVALIDA")
            alert("ERROR\n\nEl dato capurado en el campo de " + texto1+" no es válido.");
        return false;
    };
    
    this.Principal = function(tipo, texto)
    {
        if(tipo==="SELEC")
            alert("PRECAUCIÓN\n\nDebe seleccionar un " +texto);
        else if(tipo==="SN_BUSQ")
            alert("PRECAUCIÓN\n\nNecesita realizar  la búsqueda de un centro de trabajo");
        else if(tipo==="TAM_CCT")
            alert("PRECAUCIÓN\n\nLa clave debe contener mínimo 9 dígitos.\nEjemplo [ 20DJN0101 ]");
        else if(tipo==="CCT_INCORR")
            alert("INFORMACIÓN\n\nCentro de trabajo incorrecto.");
        else if(tipo==="VERSION_NO_ACTUALIZADA")                                                                                                                       
            alert("PRECAUCIÓN\n\nParece ser que su versión de sistema NO ESTÁ ACTUALIZADO.\n\nDe click en el botón aceptar de este mensaje y\nposteriormente MIENTRAS OPRIME LA TECLA 'CONTROL' OPRIMA LA TECLA 'F5' (las veces necesarias)\nhasta que su versión muestre la "+texto+" tornándose de color rojo a AZUL.\n\nNOTA: La versión se ve en la parte superior derecha\nde la barra de menú del sistema.");
        else if(tipo==="NO_ES_SECU_O_1G")
            alert("PRECAUCIÓN\n\nNo se ha seleccionado una Escuela Secundaria o no es Primer Grado.");
        else if(tipo==="NO_ES_SECU")
            alert("PRECAUCIÓN\n\nNo se ha seleccionado una Escuela Secundaria.");
        else if(tipo==="CONTACTO")
            alert("INFORMACIÓN\n\nPARA INFORMAR SOBRE PROBLEMAS, DUDAS, O SUGERENCIAS QUE SE PRESENTEN CON EL SICEEO PORFAVOR ENVIE CORREO A:\n\nNIVEL PREESCOLAR:\n    preescertioax@gmail.com\nNIVEL PRIMARIA:\n    primacertioax@gmail.com\nNIVEL SECUNDARIA:\n    secundcertioax@gmail.com\n\n\nO A LOS TELÉFONOS:\n   51 35045\n   51 53900 Ext.482\nLic. Victor Manuel García.\nJefe del Departamento de Registro y Certificación Escolar.");
        else if(tipo==="NO_PREINSCRIBIBLE")
            alert("PRECAUCIÓN\n\nLa preinscripción sólo es para primeros grados.\nDe segundo en adelante el sistema los reinscribirá automáticamente.");
        else if(tipo==="NO_APLICA") // PAra boton de revocación de grado, no aplica para prom diferente de 6 y 7
            alert("PRECAUCIÓN\n\nEsta opción solo aplica para los alumnos que tienen ya un promedio y este sea entre 6.0 y 7.0.\n\n"+texto+", no cumplen el requisito.");
        return false;
    };
    
    this.XbimOPromFin = function (tipo, texto1, texto2)
    {
        if (tipo === "TIPO_CAPTURA")
            alert("PRECAUCIÓN\n\nIndique un tipo de captura");
    };
    
    this.Tutor = function (tipo, texto1, texto2)
    {
        if (tipo === "FECHA_INVALIDA")
            alert("ERROR\n\nLa fecha de nacimiento no está correctamente escrita.");
        else if (tipo === "SIN_DATO")
            alert("PRECAUCIÓN\n\nProporcione "+texto1+".");
        else if (tipo === "INCORR_LONG")
            alert("PRECAUCIÓN\n\nLa longitud de "+texto1+" no es la correcta.");
        else if (tipo === "FORMATO_INCOR")
            alert("PRECAUCIÓN\n\nEl formato de "+texto1+" es incorrecto.");        
        
    };
    
    this.CalifSecXBim = function (tipo, texto1, texto2, modo)
    {
        if (modo==="CONFIRM_DIALOG"){
            if (tipo==="PROMOVIDO_COND")
                return confirm("PREGUNTA EMERGENTE\n\n¿El alumno será promovido con condición?\n\nOprima Aceptar para que SÍ sea promovido.\nOprima Cancelar para que NO sea promovido.");//, "Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            else if (tipo==="ALUMREP_PROMOVIDO")
                return confirm("PREGUNTA EMERGENTE\n\nEl alumno actualmente se encuentra repitiendo el grado,\ndebería ser promovido al siguiente grado.\n\n¿El alumno será promovido?\n\nOprima Aceptar para que SÍ sea promovido.\nOprima Cancelar para que NO sea promovido.");//, "Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            else if (tipo==="ACRED_X_CURSADO")
                return confirm("PREGUNTA EMERGENTE\n\nLa acreditación de este grado se obtendrá por el sólo hecho de\nhaberlo cursado.\nSi el alumno no alcanzó los aprendizajes esperados podrá\npermanecer en este grado por otro ciclo escolar.\n\n¿El alumno será promovido?\n\nOprima Aceptar para que SÍ sea promovido.\nOprima Cancelar para que NO sea promovido.");//, "Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (tipo==="INGLES")
                return confirm("PREGUNTA EMERGENTE\n\n¿El grupo cursa la materia de ingles?\n\nOprima Aceptar en caso de que Sí cursen la clase.\nOprima Cancelar en caso de que NO.");//, "Pregunta emergente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);    
        }else if (tipo==="SIN_ALUMNOS")
            alert("PRECAUCIÓN\n\nNo hay alumnos en el grupo.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE
        else if (tipo==="KARDEX_EN_CREACION")
            alert("INFORMACIÓN\n\nEl kárdex empezará a crearse...\nPor favor de click en aceptar y espere a que se muestre el documento.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE
        else if (tipo==="FOLKARDEX_EN_CREACION")
            alert("INFORMACIÓN\n\nLos folios de kárdex empezarán a crearse...\nPor favor de click en aceptar y espere a que se muestre el documento.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE    
        else if (tipo==="PERMISO_MODIF_EXAMEXTRAORD")
            alert("PRECAUCIÓN\n\nEl alumno no tiene materias reprobadas, no podrá modificar su Kárdex en este ciclo.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE    
        else if (tipo === "CALIF_NO_VALIDA")
            alert("ERROR\n\nLa calificación de la materia "+texto1+" de la columna "+texto2+" no es válida.");
        else if (tipo === "CALIF_NO_VALIDA_GDO")  
            alert("ERROR\n\nLa calificación de la materia "+texto1+" de la columna "+texto2+" no es válida,\npara 1er grado de primaria la calificación debe ser aprobatoria.");
        /*else if (tipo === "CALIF_NO_VALIDA_GDO")  // se utilizo para ciclo 21-22
            alert("ERROR\n\nLa calificación de la materia "+texto1+" de la columna "+texto2+" no es válida,\nla calificación debe ser aprobatoria, cinco o cero.");*/
        else if (tipo === "CALIFS_NO_VALIDAS")
            alert("ERROR\n\nLa calificaciones de la materia "+texto1+" no son válidas.");
        else if (tipo === "TODO_OFICIALIZADO")
            alert("PRECAUCIÓN\n\nNo podrá editar ni guardar ningún dato porque ha oficializado sus tres evaluaciones.\nSi desea desoficializar debe pedir ayuda a su UDSE.");
        else if (tipo === "BIMESTRE_OFICIALIZADO")
            alert("INFORMACIÓN\n\nNo podrá editar ni guardar datos de esta evaluación ya que se encuentra oficializado.\nSi desea desoficializar debe pedir ayuda a su UDSE.");
        else if (tipo === "BLOQUEADO_POR_OFICIALIZACION")
            alert("INFORMACIÓN\n\nNo podrá usar esta opción debido a que ya hay al menos una evaluación oficializada.\nSi desea desoficializar debe pedir ayuda a su UDSE.");
        else if (tipo === "CALIF_INVALIDA_1y2PRIM")
            alert("ERROR\n\nLas calificaciones para 1er grado de primaria deben ser aprobatorias.");
        else if (tipo === "CALIF_CERO_COL2")
            alert("ERROR\n\nLa calificación de la materia "+texto1+" de la columna "+texto2+" no puede ser cero si en la columna calif1 ya ingresó cero.");
        else if (tipo === "FECHA_INVALIDA")
            alert("ERROR\n\nLa fecha " + texto1 + " de examen de la " + texto2 + " no está correctamente escrita, formato valido dd/mm/aaaa.");
        else if (tipo === "ALU_CON_CERTIFICADO")
            alert("ERROR\n\nEl alumno ya tiene un certificado.");
        
        return false;
    };
  
    this.RepEvaluacion = function (tipo, texto, texto2, modo)
    {
        if (modo==="CONFIRM_DIALOG"){
            if (tipo==="ASIGNAR_FOLIOS")
                return confirm("PREGUNTA EMERGENTE\n\n¿Confirma que desea asignar los folios a partir de su selección?\nOprima Aceptar para que SÍ asigne folio(s).\nOprima Cancelar para que NO se asigne(n) folio(s).");
            else if (tipo==="QUITAR_FOLIOS")
                return confirm("PREGUNTA EMERGENTE\n\n¿Confirma que desea quitar los folios a partir de su selección?\nOprima Aceptar para que SÍ quite el(los) folio(s).\nOprima Cancelar para que NO quite el(los) folio(s).");
        }
        else if (tipo === "RANGO_IMPRESION")
            alert("ERROR\n\nEl rango de impresión no es válido.");
        else if (tipo === "FOLIOS_ASIGNADOS")
            alert("INFORMACIÓN\n\nLos folios se han asignado satisfactoriamente.");
        else if (tipo === "FOLIOS_ELIMINADOS")
            alert("INFORMACIÓN\n\nLos folios se han eliminado satisfactoriamente.");
    };
    
    this.Preinscripcion = function (tipo, texto, texto2, modo)
    {
        if (modo==="CONFIRM_DIALOG"){
            if (tipo==="OFICIALIZAR")
                return confirm("PREGUNTA EMERGENTE\n\nOficializar sirve para indicar al sistema que ha terminado\nde capturar la preinscripción de toda la escuela.\n\nSi oficializa no podrá posteriormente preinscribir en el ciclo "+texto+"\n\n¿Confirma que desea oficializar su preinscripción?\nOprima Aceptar para que SÍ oficialice.\nOprima Cancelar para que NO oficialice.");
            if (tipo==="DESOFICIALIZAR")
                return confirm("PREGUNTA EMERGENTE\n\nAl desoficializar dará permiso a la escuela para que continue preinscribiendo en el ciclo "+texto+"\n\n¿Confirma que desea desoficializar la preinscripción?\nOprima Aceptar para que SÍ desoficialice.\nOprima Cancelar para que NO desoficialice.");
        }else if (tipo==="ACUSEOFIC_EN_CREACION")
            alert("INFORMACIÓN\n\nEl acuse de oficialiazión empezarán a crearse...\nPor favor de click en aceptar y espere a que se muestre el documento.");//"Información", JOptionPane.OK, JOptionPane.INFORMATION_MESSAGE    
    };
    
    this.ModifiCurp = function (tipo, texto1, texto2)
    {
        if (tipo === "FECHA_INVALIDA")
            alert("ERROR\n\nLa fecha de nacimiento no está correctamente escrita.");
        else if (tipo === "SIN_DATO")
            alert("PRECAUCIÓN\n\nProporcione "+texto1+".");
    };
    
    this.EvalPreescolar = function (tipo, texto1, texto2)
    {
        if (tipo === "SIN_ALUMNOS_PARA_INASIS")
            alert("PRECAUCIÓN\n\nNo hay alumnos a quién generarles su tabla de insasistencias.");
        else if (tipo === "EVALUACION_OFICIALIZADA")
            alert("INFORMACIÓN\n\nNo podrá editar ni guardar los avances de esta evaluación ya que se encuentra oficializada.\nSi desea desoficializar debe pedir ayuda a su UDSE.");
        else if (tipo === "TODO_OFICIALIZADO")
            alert("PRECAUCIÓN\n\nNo podrá editar ni guardar ningún dato porque ha oficializado sus tres evaluaciones.\nSi desea desoficializar debe pedir ayuda a su UDSE.");
    };
    
    this.CapRepEval = function (tipo, texto1, texto2)
    {
        if (tipo === "CAP_REP_OFICIALIZADA")
            alert("INFORMACIÓN\n\nNo podrá editar ni guardar datos ya que se encuentra oficializada la captura.\nSi desea desoficializar debe pedir ayuda a su UDSE.");
    };
    
    this.Oficializaciones = function (tipo, texto, texto2, modo)
    {
        if (modo==="CONFIRM_DIALOG"){
            if (tipo==="OFICIALIZAR")
                return confirm("PREGUNTA EMERGENTE\n\n¿Confirma que desea oficializar "+texto2+"?\n\nOprima Aceptar para que SÍ oficialice.\nOprima Cancelar para que NO oficialice.");
            else if (tipo==="DESOFICIALIZAR")
                return confirm("PREGUNTA EMERGENTE\n\n¿Confirma que desea desoficializar "+texto2+"?\n\nOprima Aceptar para que SÍ desoficialice.\nOprima Cancelar para que NO desoficialice.");
            else if (tipo==="OFIC_DESOFIC")                
                return confirm("PREGUNTA EMERGENTE\n\n¿Confirma que desea "+texto+" "+texto2+"?\n\nOprima Aceptar para que SÍ "+(texto==="oficializar"?"oficialice":"desoficialice")+".\nOprima Cancelar para que NO "+(texto==="oficializar"?"oficialice":"desoficialice")+".");                            
            else if (tipo==="OFIC_DESOFIC_3RO6TO")                
                return confirm("PREGUNTA EMERGENTE\n\n"+(texto==="oficializar"?"Si los promedios generales son correctos debe oficializar para que los\nregistros sean enviados a SIGED y generar sus certificados.\n\n":"")+"¿Confirma que desea "+texto+" "+texto2+"?\n\nOprima Aceptar para que SÍ "+(texto==="oficializar"?"oficialice":"desoficialice")+".\nOprima Cancelar para que NO "+(texto==="oficializar"?"oficialice":"desoficialice")+".");                
            else if (tipo==="OFIC_DESOFIC_OTROS")                
                return confirm("PREGUNTA EMERGENTE\n\n"+(texto==="oficializar"?"Si los promedios son correctos debe oficializar.\n":"")+"¿Confirma que desea "+texto+" "+texto2+"?\n\nOprima Aceptar para que SÍ "+(texto==="oficializar"?"oficialice":"desoficialice")+".\nOprima Cancelar para que NO "+(texto==="oficializar"?"oficialice":"desoficialice")+".");                
            else if (tipo==="QUIERE_ESOFIC")
                return confirm("PRECAUCIÓN\n\nLa desoficialización implica la cancelación de folios de los\ndocumentos que ya se hayan generado con anterioridad a los\nalumnos que desoficializará.\n\n¿Desea continuar para cancelar folios y desoficializar?\n\nOprima botón Acepatar para continuar o botón Cancelar para regresar.");            
        }else {
            if (tipo==="ESCUELA_DESOFIC")
                alert("INFORMACIÓN\n\nPara desoficializar pida ayuda a su UDSE.");
            if (tipo==="MAS_DE_UN_IDCCT")
                alert("INFORMACIÓN\n\nMÁS DE UN TURNO O idcct.\n\nAsegúrese de seleccionar un grado-grupo que pertenezca al turno con el que desea oficializar/desoficializar.");
            if (tipo==="DESOFIC_DESACTIVADO")
                alert("INFORMACIÓN\n\nPor el momento las desoficializaciones de "+texto+" no son posibles.\n\nPara más información pregunte al Departamento de Información Escolar.");
            if (tipo==="SOLO_TODO_OFIC")
                alert("PRECAUCIÓN\n\nEs necesario tener oficializado todas sus evaluaciones.");
        }
    };
    
    this.CamDeGpo = function (tipo, texto1, texto2, modo)
    {
        if (modo==="CONFIRM_DIALOG"){
            if (tipo==="CANCELAR_FOL_REPEVAL")
                return confirm("¡PRECAUCIÓN!\n\nAl dar de baja, si el alumno tiene asignado un\nfolio de Reporte de Evaluación en este ciclo\nescolar ("+texto1+"), éste será cancelado.\n\n¿Desea continuar?\nOprima botón Aceptar para continuar con la baja\ny cancelación de folio.\nOprima botón Cancelar para no continuar y regresar.");
            else if(modo==="DAR_BAJA_ALUMNO")    
                return confirm("¡PRECAUCIÓN!\n\nAl dar de baja, si el alumno tiene asignado un\nfolio de Reporte de Evaluación en este ciclo\nescolar ("+texto1+"), éste será cancelado.\n\n¿Desea continuar?\nOprima botón Aceptar para continuar con la baja\ny cancelación de folio.\nOprima botón Cancelar para no continuar y regresar.");
        } else {    
            if (tipo === "INSCRIPCION_OFICIALIZADA")
                alert("PRECAUCIÓN\n\nNo podrá acceder a esta opción debido a que tiene oficializada su inscripción;\nSin embargo, una vez que termine la etapa de Inscripción\nel sistema reactivará el acceso automáticamente.\n\nPara más información pida ayuda a su UDSE.");
            else if (tipo === "SIN_OFICIALIZAR")
                alert("PRECAUCIÓN\n\nNo es posible generar la Constancia de Baja; debido a que la 1ra Evaluación no esta oficializada.");            
            else if (tipo === "ALU_INSCRITO")
                alert("PRECAUCIÓN\n\nEl alumno con idalu ["+texto1+"] se encuentra Inscrito. Esta opción aplica para los alumnos con estatus BD.");
            else if (tipo === "ALU_CALF_DESOF")
                alert("PRECAUCIÓN\n\nNo podrá imprimir la constancia debido a que el alumno se encuentra desoficializado en su ultima evaluación.");
            else if (tipo === "SIN_CALIFICACIONES")
                alert("PRECAUCIÓN\n\nEl alumno con idalu = "+texto1+" no cuenta con calificaciones de la primera evalucación.");                
        }
    };
    
    this.CamDeTaller = function (tipo, texto1, texto2)
    {
        if (tipo==="SIN_DATO"){            
            alert("PRECAUCIÓN\n\nNo hay "+texto1+" en el grupo para mostrarse.");
        }
        else if (tipo === "NO_SELEC")
            alert ("PRECAUCIÓN\n\nPrimero seleccione un"+texto1+" de la lista para poder "+texto2+".");        
    };
    
    this.Reportes = function (tipo, texto1, texto2)
    {
        if (tipo === "SIN_ALU_REPS")
            alert("PRECAUCIÓN\n\nNo hay alumnos para mostrarse en el repote.");
    };
    
    this.GestionarAvisos = function (tipo, texto1, texto2, modo)
    {
        if (modo==="CONFIRM_DIALOG"){
            if (tipo==="REEMPLAZAR_MENSAJE")
                return confirm("¡PRECAUCIÓN!\n\n¿Confirma que desea reemplazar el mensaje seleccionado?.");
        }
    };
    
    this.Exalumnos = function (tipo, texto1, texto2, modo)
    {
        if (modo==="CONFIRM_DIALOG"){
            if (tipo==="CANCELAR_FOL_CERTIF")
                return confirm("¡PRECAUCIÓN!\n\nEste alumno ya tiene un folio de secundaria.\nSi continua, el folio será cancelado y se asignará uno nuevo.\n\n¿Desea continuar?\nOprima botón Aceptar para continuar con el foliado.\nOprima botón Cancelar para no continuar y regresar.");
        }else {
            if (tipo==="SIN_HISTORIAL")
                alert ("¡PRECAUCIÓN!\n\nEste alumno no tiene historial académico.");
            else if (tipo==="HISTORIAL_INCOMPLETO")
                alert ("¡PRECAUCIÓN!\n\nEste alumno no tiene su historial completo.");
            else if (tipo==="GRADO_SEL_INCORRECTO")
                alert ("¡PRECAUCIÓN!\n\nPara este módulo sólo se permite usar grupos del 3er grado.\nPorfavor elija el grado y grupo con el que desea trabajar.");
            else if (tipo==="NO_HAY_NINGUN_FOLIO")
                alert ("¡PRECAUCIÓN!\n\nEste alumno no tiene asignado ningún folio.");
            else if (tipo==="SIN_FOLIO_SECU")
                alert ("¡PRECAUCIÓN!\n\nNo tiene asignado un folio de secundaria.");
            else if (tipo==="FOLIO_SIN_PROMEDIO")
                alert ("¡PRECAUCIÓN!\n\nEl folio no tiene registrado un promedio general.\nContacte a su UDSE para la actualización del promedio.");
            else if (tipo==="SIN_FIRMA")
                alert ("¡PRECAUCIÓN!\n\nEl folio no está firmado.");
            else if (tipo==="TIENE_FOLIO_SECU")
                alert ("¡PRECAUCIÓN!\n\nEste alumno ya tiene un folio de secundaria.");
            else if (tipo==="MESEXPED_NO_ACEPTADO")
                alert ("¡PRECAUCIÓN!\n\nLa fecha de expedición del folio no corresponde a una etapa complementaria.");
        }
        
        return 0;
    };
    
}