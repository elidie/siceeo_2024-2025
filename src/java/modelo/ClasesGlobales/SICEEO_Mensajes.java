/*
 * SiCEEB_Mensajes.java
 *
 * Creado el 09/09/2011, 02:34:08 AM
 */

package modelo.ClasesGlobales;

import java.util.Map;
import javax.swing.JOptionPane;
/**
 *
 * @author Ing. Maai Nolasco Sánchez
 * 
 */
public class SICEEO_Mensajes {
    
    private final String ERROR = "ERROR";
    private final String INFO = "INFORMACIÓN";
    private final String WARNING = "PRECAUCIÓN";
    private final String QUESTION = "PREGUNTA";
    private final String PLAIN = "";
    
    public SICEEO_Mensajes (){}

    public String inputBox (java.awt.Component parent, String titulo, String mensaje, String inputInit)
    {
        String texto = "";
        if (inputInit.equals(""))
            texto = JOptionPane.showInputDialog(parent,mensaje, titulo, JOptionPane.QUESTION_MESSAGE);
        else
            texto = JOptionPane.showInputDialog(parent,mensaje, inputInit);
        
        if (texto!=null)
            return texto;
        return texto;
    }
    
    public void confirmDialog (String tipo, String texto1, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("GUARDAR_CAMBIOS"))     {  tipoMensaje[0]= this.QUESTION;   tipoMensaje[1]= "Ha efectuado cambios en los datos ¿Desea guardarlos antes de continuar?";  }
        else if (tipo.equals("GUARDAR_SALIR"))  {  tipoMensaje[0]= this.QUESTION;   tipoMensaje[1]= "Ha efectuado cambios en los datos ¿Desea guardarlos antes de que se cierre la ventana?";  }
        else if (tipo.equals("ELIMINAR"))       {  tipoMensaje[0]= this.QUESTION;   tipoMensaje[1]= "¿Confirma que desea eliminar "+texto1+" seleccionado?";  }
        else if (tipo.equals("CANCELAR"))       {  tipoMensaje[0]= this.QUESTION;   tipoMensaje[1]= "¿Confirma que desea cancelar "+texto1+" seleccionado?";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
    
    public void General(String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("CONEXION"))                 {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No se pudo acceder a la Base de Datos debido a un error de conexión,\nes probable que el servidor esté ocupado o en mantenimiento.\n\nPor favor intente de nuevo o más tarde." + (texto1.contains("ieepo_tcp4")?"":"\n\n"+texto1);  }
        else if(tipo.equals("DRIVER"))               {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No se encontró el driver de conexión a MySQL."+texto1;  }
        else if (tipo.equals("CONSULTA"))            {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No se realizó la consulta. " + texto1;  }
        else if (tipo.equals("CAMPO_VACIO"))         {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El campo " + texto1 +" no puede estar vacío.";  }
        else if (tipo.equals("NO_SELEC"))            {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Primero seleccione un"+texto1+" de la lista para poder "+texto2;  }
        else if(tipo.equals("GENERAL"))              {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "" + texto1; }
        else if(tipo.equals("REPORTE"))              {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No se pudo leer el archivo: "+texto1;  }
        else if(tipo.equals("NUM_CONTROL"))          {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No se pudo asignar un número de control para "+texto1+".";  }
        else if (tipo.equals ("ERROR_GUARDAR"))      {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No se pudo guardar la información.";  }
        else if(tipo.equals("FECHA"))                {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La fecha "+texto1+" no es válida.";  }
        else if (tipo.equals("SIN_TEXTO_APLICATIVO")){  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Introduzca el dato que desea aplicar.";  }
        else if(tipo.equals("VALOR_INVALIDO"))       {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "El valor que ha introducido para "+texto1+" no es válido.";  }
        else if(tipo.equals("LONG_INVALIDA"))       {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La longitud del dato de "+texto1+ " que ha introducido no es válido.";  }
        else if (tipo.equals("INDISPUESTO"))         {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "¡IdAlu no disponible o el alumno ya existe en la Base de Datos!\n"+texto1; }
        else if (tipo.equals("ACTUALIZACION_INCOMPLETA")){  tipoMensaje[0]=this.WARNING;tipoMensaje[1]= "¡La información no se pudo actualizar completamente!\n"+texto1; }
        else if (tipo.equals("IMPRESION"))           {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No se pudo realizar la impresión. " + texto1;  }        
        else if (tipo.equals("SESION_EXPIRADA"))     {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Su sesión ha expirado, vuelva a loguearse.";  }
        else if (tipo.equals("ACTUALIZACION_EXITOSA")){  tipoMensaje[0]= this.INFO;     tipoMensaje[1]= "Los datos se han actualizado con éxito.";  }
        else if (tipo.equals("NADA_QUE_GUARDAR"))    {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No hay registros qué guardar.";  }
        else if (tipo.equals("USUARIO_REESTRINGIDO")){  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Su usuario no tiene permiso de usar esta opción.";  }
        else if (tipo.equals("NO_PUDO_FIRMAR"))         {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se pudo asignar folio de certificado a el(los) alumno(s),\nprobablemente se perdió el enlace con el servidor.\n\nPor favor vuelva a intentarlo.";  }
        else if (tipo.equals("ERROR_WEBSERVICE"))       {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Error en el servicio de asignación de folios.\n"+texto1;  }
        else if (tipo.equals("SIN_PERMISO"))       {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No puede realizar esta actualización.\n\nConsulte a su UDR correspondiente.";  }
        else if (tipo.equals("CARACTER_INVALIDO"))  { tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Caracter no válido en el "+texto1+" ("+texto2+")."; }
        else if (tipo.equals("PROM_INVALIDO"))  { tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se realizó la actualización.\n\n"+texto1+" "; }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//***************************** MENSAJES USADOS EN EL MÓDULO LoginUser ***************************************
//************************************************************************************************************    
    public void loginUser (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        
        if (tipo.equals("INACTIVO"))                {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El usuario está inactivo.";  }
        else if (tipo.equals("SERVER-ERROR"))                {  tipoMensaje[0]= "500 - Internal System SICEEO error.";   tipoMensaje[1]= "There is a problem with the resource you are looking for,\nand it cannot be displayed.";  }
        else if (tipo.equals("USR_SIN_REFRENDO"))   {  tipoMensaje[0]= "INFORMACIÓN";      tipoMensaje[1]= "SIN ACCESO AL SISTEMA.\n\nComunicarse al Departamento de Autorización y Revalidación.\nTelefono: 51 3 40 55\nCorreo: dictaminacion.planeacion@ieepo.gob.mx";  }
        else if (tipo.equals("USR_SIN_DELEG"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El usuario no pertenece a una delegación.";  }
        else if (tipo.equals("USR_PASS_INCORRECTO")){  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Usuario incorrecto.";  }
        else if (tipo.equals("USUARIO"))            {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Ingrese un nombre de usuario.";  }
        else if (tipo.equals("CONTRASEÑA"))         {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Ingrese una contraseña.";  }
        else if (tipo.equals("USR_REESTRINGIDO"))   {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Usted tiene acceso reestringido al sistema.";  }
        else if (tipo.equals("USR_SIN_ACCESO"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Usted no tiene acceso al sistema.";  }
        //else if (tipo.equals("SIS_CERRADO_IMPRE"))  {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "Error 500 - Internal server error. ";  }
        else if (tipo.equals("SIS_CERRADO_IMPRE"))  {  tipoMensaje[0]= "500 - Internal System SICEEO error.";   tipoMensaje[1]= "Cryptographic bus error: service SIGED-SEP Failed to authenticate the string.\n"
                                                                                                                + "There is a problem with the resource you are looking for,\nand it cannot be displayed."; }
        else if (tipo.equals("EN_MANTENIMIENTO"))   {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "Disculpe las molestias. Por el momento el sistema se encuentra en mantenimiento y no será posible acceder.";  }
        else if (tipo.equals("CAMBIAR_DE_SERVIDOR")){  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Conexión válida,\nPorfavor, vuelva a loguearse.";  }
        else if (tipo.equals("SIN_ARCHIVO_CONFIG")) {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se detectaron las configuraciones de inicio de sesión. "+texto1;  }
        else if (tipo.equals("SIN_VAR_CONFIG"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se encontró una variable de configuración.";  }
        else if (tipo.equals("SERVIDOR_SATURADO"))  {  tipoMensaje[0]= "INTÉNTELO POSTERIORMENTE";      tipoMensaje[1]= "Por el momento el sistema no se encuentra disponible (saturado).\n\nPor favor intente ingresar más tarde.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//****************************** MENSAJES USADOS EN EL MÓDULO CamPass ****************************************
//************************************************************************************************************    
    public void camPass (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("USUARIO_NO_ENCONTRADO")) {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se encontró el usuario o la contraseña es incorrecta.";  }
        else if (tipo.equals("CONTRASENIA_ORIGINAL_INCORRECTA")) {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La contraseña actual que ha escrito es incorrecta.\nVuelva a escribirla.";  }
        else if (tipo.equals("TAM_CONTRASENIA")) {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La nueva contraseña debe ser de 10 caracteres.";  }
        else if (tipo.equals("CONTRASENIA_NO_COINCIDE")) {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La contraseña nueva y su confirmación que ha escrito no coinciden.\nVuelva a escribir la nueva contraseña en los dos recuadros.";  }
        else if (tipo.equals("CONTRASENIA_NO_NOMENCLATURA")) {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La contraseña debe ser una letra mayuscula al principio seguido de 4 numeros.";  }
        
        
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//***************************** MENSAJES USADOS EN EL MÓDULO NuevoIngreso ************************************
//************************************************************************************************************    
    //----------
    //---------- Contiene los mensajes en común para Nuevo Ingreso de preescolar, primaria y secundaria
    //----------
    private void NewIngreso (String tipo, String texto1, String texto2, String [] tipoMensaje)
    {
        if (tipo.equals("EDAD_INVALIDA"))           { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La edad no es válida."; }
        else if (tipo.equals("CAPGPO_INVALIDA")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Ha alcanzado el límite máximo de alumnos en el grupo:" +texto1+".\n\nPara más información pida ayuda a su UDR.";  }
        else if (tipo.equals("ESPACIO_BLANCO"))     { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Más de un espacio en el "+texto1+"."; }
        else if (tipo.equals("EXISTE_ESPACIO"))     { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Existe un espacio en blanco en "+texto1+", el cual no es permitido ."; }
        else if (tipo.equals("CARACTER_INVALIDO"))  { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Caracter no válido en el "+texto1+"."; }
        else if (tipo.equals("CARACTER_NO_VALIDO")) { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Caracter no válido"+texto1+"."; }
        else if (tipo.equals("SIN_DATO"))           { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Proporcione el "+texto1+"."; }
        else if (tipo.equals("2ULT_CURP"))          { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Caracter no válido en los 2 últimos dígitos de la CURP, \n cambia la letra 'O' por cero ó la '@' por 'A'."; }
        else if (tipo.equals("TAM_CURP"))           { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La CURP debe contener 18 o 16 caracteres."; }
        else if (tipo.equals("DIG_VERIF"))          { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "El último caracter debe ser numérico."; }
        else if (tipo.equals("CURP_INCOMPLETA"))    { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Proporcione por lo menos los 10 primeros dígitos de la CURP."; }        
        else if (tipo.equals("CURP_MAL_GENERADA"))  { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La curp se ha generado incorrectamente, verifique los datos introducidos."; }
        else if (tipo.equals("EXISTE_EN_CICLOACT")) { tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "Ya existe en en ciclo actual.";  }
        else if (tipo.equals("ALUMNO_EXISTENTE"))   { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "¡Ya existe el alumno en la Base de Datos!"; }
        else if (tipo.equals("ALUMNO_INEXISTENTE")) { tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "No existe en la Base de Datos. Puede ingresar los datos manualmente.";  }
        else if (tipo.equals("ALUMNO_NO_EXISTE"))   { tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "No existe en la Base de Datos.";  }
        else if (tipo.equals("VERIF_PLAN"))         { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Verificar plan asignado."; }
        else if (tipo.equals("DISCAPACIDAD"))       { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Error en discapacidad."; }
        else if (tipo.equals("FECHA_NAC"))          { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La fecha de nacimiento no es válida.";  }
        else if (tipo.equals("FORMATO_FECHA_NAC"))          { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La fecha de nacimiento no es válida; Ingrese el formato correcto(AAAA/MM/DD).";  }        
        else if (tipo.equals("VARIOS_REGISTROS"))   { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Se encontraron más de un registro con la misma CURP.";  }
        else if (tipo.equals("CHECAR_HISTORIAL"))   { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "¡Revisar historial del alumno!";  }
        else if (tipo.equals("TIENE_FOLIO_ASIGNADO")){ tipoMensaje[0]= this.WARNING;  tipoMensaje[1]= "¡El alumno tiene un folio asignado! Revise su historial.";  }
        else if (tipo.equals("NO_PREINSCRIBIBLE_MISMO_CCT")){ tipoMensaje[0]= this.WARNING;  tipoMensaje[1]= "El alumno ya está inscrito en esta escuela.\n\nNo es necesario preinscribirlo ya que el sistema\nlo promoverá automáticamente.\n\nA menos que esté dado de baja sí podrá\npreinscribirlo en la misma escuela.";  }
        else if (tipo.equals("ALUMNO_AUN_INSCRITO")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Est"+(texto2.equals("o")?"e":"a")+" alumn"+texto2+" se encuentra inscrit"+texto2+(texto1.length() > 0 ? " en la escuela con cct "+texto1 : " en otra escuela ")+"\ny SICEEO no permite el nuevo ingreso mientras esté dad"+texto2+" de alta en alguna escuela.\n\nSolicite apoyo a su Unidad Delegacional de Servicios Educativos (UDR)"+(texto1.length() > 0 ? "\no con la escuela "+texto1 : "")+" para que realice la baja "+(texto2.equals("o")?"del":"de la")+" alumn"+texto2+" en el sistema.";  }
        else if (tipo.equals("PREINSCRITO_MISMO_CCT")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Este alumno ya lo ha preinscrito anteriormente en este grado.";  }
        else if (tipo.equals("INSCRIPCION_OFICIALIZADA")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No podrá acceder a esta opción debido a que tiene oficializada su inscripción;\nSin embargo, una vez que termine la etapa de Inscripción\nel sistema reactivará el acceso automáticamente.\n\nPara más información pida ayuda a su UDR.";  }
        else if (tipo.equals("ULTBIM_OFICIALIZADO")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No podrá acceder a esta opción debido a que ya ha oficializado su "+texto1+".\n\nPara más información pida ayuda a su UDR.";  }
        else if (tipo.equals("SIN_FECHA"))           {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Capture la fecha de ingreso a la escuela.";  }
        else if (tipo.equals("FECHA_FOR_INCORR"))           {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La fecha que ingreso no tiene el formato correcto, ingreselo de la siguiente manera: AAAA/MM/DD";  }
        else if (tipo.equals("FECHA_NO_VALIDO"))           {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La fecha no es correcta, ingrese una fecha valida dentro del periodo escolar.";  }
    }
     
    public String TutorAlum (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        if (tipo.equals("SIN_DATO"))           { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Proporcione el "+texto1+"."; }
        else if (tipo.equals("INDISPUESTO"))  {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "¡El idTutor no se encuentra disponible, vuelva a intentar Guardar su información.!\n"+texto1; }
        else if (tipo.equals("CARACTER_INVALIDO"))  {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Caracter no válido en el "+texto1+"."; }        
        else if (tipo.equals("ESPACIO_BLANCO"))     { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Más de un espacio en el "+texto1+"."; }
        else if (tipo.equals("OCUPADO"))  {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se logro realizar la acción, intentelo más tarde.\n"+texto1; }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
        
        return tipoMensaje[1];
    }
    
    public String NewIngresoPre (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        NewIngreso (tipo, texto1, texto2, tipoMensaje);
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
        return tipoMensaje[1];
    }    
    
    public String NewIngresoPri (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("ENTIDAD"))                 {  tipoMensaje[0]= this.ERROR;    tipoMensaje[1]= "Seleccione la entidad de nacimiento.";  }
        else if (tipo.equals("PVD_SIN_PERMISO_CAPTURA")) {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "El periodo de registro de inscripción y reinscripcion (altas y bajas) a finalizado.";  }
        else if (tipo.equals("BUSCAR"))             {  tipoMensaje[0]= this.ERROR;    tipoMensaje[1]= "Es necesario buscar al alumno antes de querer guardarlo.";  }
        else if (tipo.equals("NO_EGRE_PRE"))        {  tipoMensaje[0]= this.WARNING;  tipoMensaje[1]= "No es egresado de Preescolar.";  }
        else if (tipo.equals("ALUM_CON_BAJA"))      {  tipoMensaje[0]= this.WARNING;  tipoMensaje[1]= "El alumno está dado de baja, por lo tanto no terminó su Primaria en esa escuela.";  }        
        else if (tipo.equals("HIST_INCOMPLETO"))    {  tipoMensaje[0]= this.WARNING;  tipoMensaje[1]= "El alumno tiene historial incompleto en: {" +texto1+"}, no será posible realizar el registro.\n\nSolicite ayuda a su UDR.";  }        
        else if (tipo.equals("NVO_ALUMNO"))    {  tipoMensaje[0]= this.WARNING;  tipoMensaje[1]= "No fue posible realizar el registro de la alumna o del alumno; debido a que en el SICEEO su historial académico esta incompleto.\n\nSolicite ayuda a su UDR.";  }
        else NewIngreso(tipo, texto1, texto2, tipoMensaje);
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
        return tipoMensaje[1];
    }
    
    public String NewIngresoSec (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("ENTIDAD"))                 {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Seleccione la entidad de nacimiento.";  }
        else if (tipo.equals("PVD_SIN_PERMISO_CAPTURA")) {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "El periodo de registro de inscripción y reinscripcion (altas y bajas) a finalizado.";  }
        else if (tipo.equals("BUSCAR"))             {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Es necesario buscar al alumno antes de querer guardarlo.";  }
        else if (tipo.equals("SEL_TALLER"))         {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Seleccione el taller que llevará el alumno.";  }
        else if (tipo.equals("SEL_ARTE"))           {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Seleccione el arte que llevará el alumno.";  }        
        else if (tipo.equals("SEL_ENTIDAD"))        {  tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Seleccione la entidad de nacimiento.";  }
        else if (tipo.equals("EXISTE_EN_CICLOACT")) {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Ya existe en el ciclo actual."; }
        else if (tipo.equals("NO_EGRE_PRIM"))       {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No es egresado de Primaria.";  }
        else if (tipo.equals("ESPACIO_EXTRA"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Más de un espacio en el "+texto1+".";  }
        else NewIngreso (tipo, texto1, texto2, tipoMensaje);
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
        return tipoMensaje[1];
    }
//************************************************************************************************************
//******************************* MENSAJES USADOS EN EL MÓDULO Historial *************************************
//************************************************************************************************************
    public void Ajustar (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("BUSCAR_POR"))          {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "Elija si la búsqueda es por CURP o por idAlu.";  }
        else if (tipo.equals("ALUMNO_NO_EXISTE")){ tipoMensaje[0]= this.INFO;         tipoMensaje[1]= "No existe en la Base de Datos.";  }
        else if (tipo.equals("MAS_DE_UNO"))     {  tipoMensaje[0]= this.INFO;         tipoMensaje[1]= "Existe más de un alumno con esa CURP.";  }
        else if (tipo.equals("ALUMNO_EN_OTRA_DELEG")){  tipoMensaje[0]= this.WARNING; tipoMensaje[1]= "El alumno no pertenece a su delegación.";  }
        else if (tipo.equals("CON_FOLIO"))      {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "El alumno tiene un FOLIO DE CERTIFICADO asignado.";  }
        else if (tipo.equals("ELIMINADO"))      {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "****************************** ¡¡ E L I M I N A D O !! ******************************";  }
        else if (tipo.equals("NO_CIC_ANT"))     {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "No existe el Ciclo Escolar anterior";  }
        else if (tipo.equals("CON_CERTIF"))     {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "El Alumno tiene un Certificado.";  }
        else if (tipo.equals("NO_ES_REPROBADO")){  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "El Alumno NO REPROBÓ, NO DEBES DE OCUPAR ESTA OPCIÓN.\nEl Alumno SE DIO DE BAJA, DEBES DE OCUPAR LA OPCION: CURSARÁ EL GRADO.";  }
        else if (tipo.equals("VERIF_PLAN"))     {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "Verificar plan asignado         -CHECHAR-";  }
        else if (tipo.equals("NO_REPROBABLE"))  {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "El alumno no se puede Reprobar.";  }
        else if (tipo.equals("TAM_CURP"))       {  tipoMensaje[0]= this.WARNING;      tipoMensaje[1]= "Proporcione mínimo 10 caracteres para la curp.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
    
    public void CambioDeEscuela (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("GRUPO"))               {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "Asigne el grupo.";  }
        else if (tipo.equals("EXITO"))          {  tipoMensaje[0]= this.INFO;       tipoMensaje[1]= "Movimiento realizado con éxito.";  }
        else if (tipo.equals("EDAD"))           {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "No tiene la edad permitida (de "+texto1+" años), sólo se puede transitar hacia otro DML.";  }
        else if (tipo.equals("CEBAS"))          {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Sólo se puede transitar hacia otro CEBAS.";  }
        else if (tipo.equals("CCT_NO_EXISTE"))  {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "El centro de trabajo no existe, o no pertenece a su delegación, o no corresponde al Nivel Educativo.";  }
        else if (tipo.equals("SIN_TALLER"))     {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "Seleccione el taller que llevará el alumno.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//***************************** MENSAJES USADOS EN EL MÓDULO CambioDeGrupo ***********************************
//************************************************************************************************************
    public void CambioDeGrupo (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("ALUMNO_CON_CERTIFICADO")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "El alumno con idAlu "+texto1+" tiene un FOLIO DE CERTIFICADO asignado.";  }
        else if (tipo.equals("INSCRIPCION_OFICIALIZADA")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No podrá acceder a esta opción debido a que tiene oficializada su inscripción;\nSin embargo, una vez que termine la etapa de Inscripción\nel sistema reactivará el acceso automáticamente.\n\nPara más información pida ayuda a su UDR.";  }
        else if (tipo.equals("EVAL_OFICIALIZADA")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No podrá acceder a esta opción debido a que tiene uno o mas evaluaciones oficializados.\n\nPara más información pida ayuda a su UDR.";  }
        else if (tipo.equals("USUARIO_NO_PUEDE_ELIMINAR")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Sólo el usuario que dio de alta al alumno puede eliminarlo.\nSe ha detectectado que su usuario no dio de alta a este alumno.";  }
        else if (tipo.equals("ALUMNO_CON_HISTORIAL")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se podrá eliminar a este alumno porque ya tiene historial registrado.";  }
        //agregado para validad la capacidad limite del grupo
        else if (tipo.equals("CAPGPO_INVALIDA")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Ha alcanzado el límite máximo de alumnos en el grupo: "+texto1+".\n\nPara más información pida ayuda a su UDR.";  }
        else if (tipo.equals("PROMEDIO_INVALIDO")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Verifique que el promedio de los alumnos seleccionados sea un valor entre 6.0 y 7.0, de no ser asi no podrá utilizar esta opción.";  }
        else if (tipo.equals("CBIO_INSC_INVALIDO")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El alumno no se le puede cambiar a Inscrito debido a que se encuentra con estatus "+texto1+".";  }
        else if (tipo.equals("EVAL3_CAPTURADA")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El alumno con id = ["+texto1 +"] no es posible darlo de Baja Definitiva debido a que ya cuenta con calificaciones en su 3era Evaluación.";  }
        else if (tipo.equals("ALUMNO_INSCRITO")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No es posible generarle su 'Constancia de Baja' debido a que el alumno con idalu ["+texto1+"] se encuentra inscrito.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//******************************* MENSAJES USADOS EN EL MÓDULO Principal *************************************
//************************************************************************************************************        
    public void principal (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        //if (tipo.equals("ESC_CLAUSURADA"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Escuela clausurada. " + texto1;  } // se cambio el mensaje
        if (tipo.equals("ESC_CLAUSURADA"))      {tipoMensaje[0]= "500 - CL:Internal System SICEEO error.";   tipoMensaje[1]= "There is a problem with the resource you are looking for,\nand it cannot be displayed.";  }
        if (tipo.equals("ESCUELA_DESUBICADA"))  {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La escuela pertenece a "+ texto1;  }
        if (tipo.equals("GRUPO_AGREGADO"))      {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "Grupo agregado, ejecute la búsqueda nuevamente.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//********************************* MENSAJES USADOS EN EL MÓDULO GRUPO ***************************************
//************************************************************************************************************   
    public void grupo (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("SEL_ESCUELA"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se ha seleccionado una escuela." + texto1;  }
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//***************************** MENSAJES USADOS EN EL MÓDULO CalifSexXBim ************************************
//************************************************************************************************************   
    public void CalifSecxBim (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("SICANT_NOCALC_PROM"))          {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "En ciclos anteriores no se calcula el promedio de grado\n\n* Consulte el Kárdex del alumno y \n verifique la calificación de cada materia.";  }
        else if (tipo.equals("PVD_SIN_PERMISO_CAPTURA"))          {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "El periodo de registro de la primera evaluación a concluido.";  }
        else if (tipo.equals("CHECAR_HISTORIAL"))       {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Checa el Historial del Alumno, no se puede calcular su Promedio General.";  }
        else if (tipo.equals("ALUM_CON_MATBIM"))        {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "Existe un alumno con materias trimestrales.\nSe crearon materias sólo a los que no tienen.";  }
        else if (tipo.equals("BIMESTRE_OFICIALIZADO"))  {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "No podrá editar ni guardar datos de esta evaluación ya que se encuentra oficializado.\nSi desea desoficializar debe pedir ayuda a su UDR.";  }
        else if (tipo.equals("BLOQUEADO_POR_OFICIALIZACION"))     {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "No podrá usar esta opción debido a que ya hay al menos una evaluación oficializado.\nSi desea desoficializar debe pedir ayuda a su UDR.";  }
        else if (tipo.equals("TODO_OFICIALIZADO"))      {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "No podrá editar ni guardar ningún dato porque ha oficializado sus tres evaluaciones.\nSi desea desoficializar debe pedir ayuda a su UDR.";  }
        else if (tipo.equals("ACTIVAR_CALPROM"))        {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "Recuerde activar 'CALCULAR PROMEDIOS'\ny oprimir tecla ENTER en la ultima calificación para que\nel sistema genere el promedio de cada materia\ny al final el promedio del grado.";  }
        else if (tipo.equals("CHK_INGLES"))             {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Recuerde indicar si cursa la materia de INGLES.\nSi no le muestra la opción al generar sus materias debe pedir ayuda a su UDR.";  }
        else if (tipo.equals("ERROR_FORMATO_CALIF"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Calificación "+texto1+" no válida en Calif"+texto2+".";  }
        //else if (tipo.equals("ERROR_FORMATO_CALIF_GDO"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Calificación "+texto1+" no válida en Calif"+texto2+", para 1° y 2° de primaria la calificación tiene que ser aprobatoria.";  } //Comento para ciclo 2122
        else if (tipo.equals("ERROR_FORMATO_CALIF_APROB"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Calificación "+texto1+" no válida en Calif"+texto2+", la calificación debe ser aprobatoria, cinco o cero.";  }
        else if (tipo.equals("ERROR_FORMATO_PROM"))     {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Promedio generado incorrectamente: "+texto1;  }
        else if (tipo.equals("ALU_ERROR_HISTORIAL"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Hay un alumno con error en su historial.";  }
        else if (tipo.equals("ERROR_FORMATO_ING"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "No se especificó si lleva la materia de Inglés, vuelva a presionar el botón de 'Generar materias del periodo'. Para asi calcular el promedio.";  }
        else if (tipo.equals("ERROR_CVEMAT_DIF"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "La materia de " + texto1 + " tiene inconsistencias; oprima botón de 'Corregir materias del alumno' para resolver.";  }
        else if (tipo.equals("ERROR_MAT_INCOMPL"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Verifique las materias del alumno; no se le ha asignado calificación a todas sus materias o no cuenta con todas las establecidas.";  }
        else if (tipo.equals("ERROR_CVE_TEC"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Verifique las materias del alumno (idalu="+texto1+"); tiene la clave de TECNOLOGÍA DEFAULT, debe asignarle la correspondiente.";  }
        else if (tipo.equals("ERROR_CVE_ART"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Verifique las materias del alumno(idalu="+texto1+"); tiene la clave de ARTES DEFAULT, debe asignarle la correspondiente.";  }
        else if (tipo.equals("ERROR_MAT_TEC"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Verifique las materias del alumno; No se le a asignado la materia de TECNOLOGÍA";  }
        else if (tipo.equals("ERROR_MAT_ART"))    {  tipoMensaje[0]= this.ERROR;      tipoMensaje[1]= "Verifique las materias del alumno; No se le ha asignado la materia de ARTES.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//***************************** MENSAJES USADOS EN EL MÓDULO XBimOPromFin ************************************
//************************************************************************************************************   
    public void XBimOPromFin (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("ALUM_ERROR_HIST"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Hay un alumno con error en su historial.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//***************************** MENSAJES USADOS EN EL MÓDULO SecHist ************************************
//************************************************************************************************************   
    public void SecHist (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("SIN_CALC_PROM_GRAL"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No es posible calcular su promedio general del nivel.\n\nEste se calculará al momento en que se oficialicen las calificaciones de los examenes de regularización.";  }
        else if (tipo.equals("ALUM_CON_CERTIFICADO"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El alumno ya tiene un certificado.";  }
        else if (tipo.equals("ALUM_SIN_REGISTRO_EXTRA"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El alumno no tiene registro de su(s) extraordinario(s) "+texto1+".\nPara realizar esta actualización deberá realizar primero su captura.";  }
        else if (tipo.equals("FECHA_INVALIDA"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La fecha ingresada no corresponde a las fechas programadas para examenes extraordinarios. Revise su calendario.";  }
        else if (tipo.equals("MAT_SIN_REPROB"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se guardo el registro.\nEsta materia [" +texto1+"] ya cuenta con un promedio aprobatorio.";  }
        else if (tipo.equals("EXM_EXT_SIN_APROB"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "¡No se guardo el dato!.\n\nPara una calificación REPROBATORIA no es necesario realizar la capturar.\nSolo el registro de calificaciones aprobadas.";  }
        else if (tipo.equals("CALIF_SIN_ACTUAL"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La calificación de la materia que intenta guardar es reprobatoria  ["+texto1+"], no es necesaria la actualización.";  }        
        else if (tipo.equals("MAT_CALIF_SIN_CAMBIO"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El cambio de calificación para la materia con clave[" +texto1+"], no esta permitida.\nConsultelo con su UDR.";  }
        else if (tipo.equals("SIN_MAT_X_OFIC"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El alumno no cuenta con ningún examen extraordinario del periodo por oficializar.";  }
        else if (tipo.equals("SIN_MAT_SEL"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No seleccionó ningun registro para oficializar.";  }
        else if (tipo.equals("SIN_COINC"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se encontraron coincidencias para oficializar.";  }
        else if (tipo.equals("EXM_EXT_OFIC"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se eliminó el examen de la materia de clave "+texto1+", debido a que ya se encuentra oficializada.\n\nComuniquese con su UDR."; }
        else if (tipo.equals("DATOS_INCOMP"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No se pudo realizar el proceso debido a que la captura fue realizada antes de la fechas establecidas.\n\nComuniquese con su UDR."; }
        else if (tipo.equals("REG_DUPLICADO"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Existe ya un registro con los mismos datos principales, debe eliminarlo para poder insertar nuevamente."; }
        else if (tipo.equals("CALIF_DIF"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "El promedio que esta ingresando de la materia con claves "+texto1+" no corresponde con el que reporto en el examen extraordinario.\n\nIngrese la calificación correcta."; }
        else if (tipo.equals("EXM_EXT_SIN_OF"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "La actualización del promedio de nivel no se realizó.\nExisten calif. de examenes de regularización sin oficializar.\n\nDebe realizar la oficialización para poder calcular el promedio de nivel."; }
        else if (tipo.equals("EXISTEN_REP"))     {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No es posible realizar esta actualización, el alumno tiene materias reprobadas en: "+texto1+"\n\nDeberá realizar el proceso correspondiente para sus examenes de regularización."; }
        else if (tipo.equals("MAT_OF_OK"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "¡No se guardo el dato!. El registro del examen ya se encuentra oficializado.\n\nConsulte con la UDR correspondiente.";  }
        else if (tipo.equals("EXM_EXT_CORR_REP"))      {  tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Calificaciones reprobatorias no estan permitidas. En este caso si la calificación correcta es reprobatoria deberá eliminar el registro de su examen de Regularización.";  }
     
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//********************************* MENSAJES USADOS EN EL MÓDULO CURP ****************************************
//************************************************************************************************************
    public void ModifCurp (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("CURP_INCOMPLETA"))         { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Proporcione por lo menos los 10 primeros dígitos de la CURP."; }
        else if (tipo.equals("CARACTER_NO_VALIDO")) { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Caracter no válido"+texto1+"."; }
        else if (tipo.equals("ALUMNO_NO_EXISTE"))   { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No existe en la Base de Datos.";  }
        else if (tipo.equals("ALUMNO_EN_OTRA_DELEG")){  tipoMensaje[0]= this.WARNING; tipoMensaje[1]= "El alumno no pertenece a su delegación.";  }
        else if (tipo.equals("MAS_DE_UNO"))         {  tipoMensaje[0]= this.INFO;     tipoMensaje[1]= "Existe más de un alumno con esa CURP.";  }
        else if (tipo.equals("BUSCAR"))             {  tipoMensaje[0]= this.WARNING;  tipoMensaje[1]= "Antes de Actualizar es necesario buscar al alumno.";  }
        else if (tipo.equals("EDAD_INVALIDA"))      { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La edad no es válida."; }
        else if (tipo.equals("2ULT_CURP"))          { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "Caracter no válido en los 2 últimos dígitos de la CURP, \n cambia la letra 'O' por cero."; }
        else if (tipo.equals("ESPACIO_EXTRA"))      { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Más de un espacio en el "+texto1+".";  }
        else if (tipo.equals("CARACTER_INVALIDO"))  { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Caracter no válido en el "+texto1+"."; }
        else if (tipo.equals("TAM_CURP"))           { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "La CURP debe contener 18 o 16 caracteres."; }
        else if (tipo.equals("SIN_DATO"))           { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "Proporcione el "+texto1+"."; }
        else if (tipo.equals("DIG_VERIF"))          { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "El último caracter debe ser numérico."; }
        else if (tipo.equals("CURP_CON_NO_DEF"))    { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "No puede tener CURP cuando su Entidad de Nacimiento es NO DEFINIDA."; }
        else if (tipo.equals("NIÑO_SIN_NOMBRE"))    { tipoMensaje[0]= this.ERROR;     tipoMensaje[1]= "¡UPS!, es el Niño sin Nombre."; }
        else if (tipo.equals("CURP_ACTUALIZADA"))   { tipoMensaje[0]= this.INFO;     tipoMensaje[1]= "CURP ACTUALIZADA\naun así el nombre contiene una VOCAL con diéresis o acento\nVerifique que la CURP sea la correcta."; }
        else if (tipo.equals("CURP_ACTUALIZADA_RENAPO"))   { tipoMensaje[0]= this.INFO;     tipoMensaje[1]= "CURP Actualizada para confronta con RENAPO."; }
        else if (tipo.equals("INSCRIPCION_OFICIALIZADA")){ tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No podrá acceder a esta opción debido a que tiene oficializada su inscripción;\nSin embargo, una vez que termine la etapa de Inscripción\nel sistema reactivará el acceso automáticamente.\n\nPara más información pida ayuda a su UDR.";  }
        else if (tipo.equals("CON_FOLIO"))          { tipoMensaje[0]= this.WARNING;   tipoMensaje[1]= "No puede modificar el registro del alumno por tener asignado un folio de certificado.\n\nPara más información pida ayuda a su UDR.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//*************************** MENSAJES USADOS EN EL MÓDULO Configuraciones ***********************************
//************************************************************************************************************
    public void Configuraciones (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("USUARIO_REESTRINGIDO"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Este usuario no tiene permiso de acceder al módulo de configuraciones.";  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//******************************* MENSAJES USADOS EN EL MÓDULO Permisos **************************************
//************************************************************************************************************
    public void Permisos (String tipo, String texto1, String texto2,  Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("PERMISO_REPETIDO")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "El permiso para el objeto que intenta insertar ya está dado de alta.\nPor favor intente establecer el estatus desde la tabla correspondiente.";  }
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//**************************** MENSAJES USADOS EN EL MÓDULO RepEvaluacion ************************************
//************************************************************************************************************
    public void RepEvaluacion (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("SIN_FOLIO_INI"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Proporcione el folio inicial.";  }
        else if (tipo.equals("SIN_FOLIO_LET")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Asigne la letra del folio.";  }
        else if (tipo.equals("FOLIACION_SUSPENDIDA")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "¡ASIGNACIÓN DE FOLIOS SUSPENDIDA! Es posible que el folio esté asignado a otro alumno.\n\n"+texto1;  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//***************************** MENSAJES USADOS EN EL MÓDULO Oficializar *************************************
//************************************************************************************************************
    public void Oficializar (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("SIN_PERMISO_DESOFIC"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No tiene permiso para desoficializar.";  }
        if (tipo.equals("NIVEL_SIN_PERMISO_OFIC"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Sólo el nivel de "+texto1+" puede oficializar "+texto2+".";  }
        if (tipo.equals("NIVEL_SIN_PERMISO_DESOFIC"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Sólo el nivel de "+texto1+" puede desoficializar "+texto2+".";  }
        else if (tipo.equals("INCONSISTENCIAS_PARA_OFIC")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se pudo oficializar porque hay inconsistencias en sus "+texto2+" del(los) siguiente(s) alumno(s):\n\n"+texto1;  }
        else if (tipo.equals("INCONSISTENCIAS_PARA_OFIC_SIN_REG")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se pudo oficializar porque hay inconsistencias en sus "+texto2+" \n\n"+texto1;  }
        else if (tipo.equals("VALIDACION_COMDOCALU_PARA_OFIC")) {  
            tipoMensaje[0]= this.ERROR;   
            tipoMensaje[1]= texto2.equals("evaluaciones-pree") ? "No se pudo oficializar porque hay alumnos con comunicación docente-alumno CONTINUA\n" 
                    +"y su calificación debe ser aprobatoria en todas las materias del trimestre.\n\n "
                    +"3ro de Preescolar: Si no cursó ingles anote NO APLICA\n\n"
                    +"se encontraron inconsistencias del(los) siguiente(s) alumno(s):\n\n"+texto1 :
                    "No se pudo oficializar porque hay alumnos con comunicación docente-alumno CONTINUA\n" 
                    +"y su calificación debe ser aprobatoria en todas las materias del trimestre\n "
                    +"se encontraron inconsistencias del(los) siguiente(s) alumno(s):\n\n"+texto1;  
        }        
        else if (tipo.equals("INCONSIST_COMDOCALU_PARA_OFIC")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se pudo oficializar ya que es obligatorio reportar el tipo de comunicación Docente-Alumno de cada trimestre.\nSe encontraron inconsistencias del(los) siguiente(s) alumno(s):\n\n"+texto1;  }        
        else if (tipo.equals("BIM_SIN_PREVIA_OFIC")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Necesita tener oficializado todos las evaluaciones anteriores.";  }
        else if (tipo.equals("EVAL_SIN_PREVIA_OFIC")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Necesita tener oficializado todas las evaluaciones anteriores.";  }
        else if (tipo.equals("ULTIMO_BIMESTRE_OFIC")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se podrá desoficializar, debido a que la 3a evaluación ya ha sido oficializado.";  }
        else if (tipo.equals("ULTIMA_EVAL_OFIC")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se podrá desoficializar, debido a que la 3a evaluación ya ha sido oficializada.";  }
        else if (tipo.equals("DESOFIC_ULTIMO_BIMESTRE")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se podrá desoficializar esta evaluación sin antes desoficializar la 3a.";  }
        else if (tipo.equals("DESOFIC_ULTIMA_EVAL")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se podrá desoficializar esta evaluación sin antes desoficializar la 3a.";  }
        else if (tipo.equals("SIN_PERMISO_DESOF_ULTIMO")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se podrá desoficializar, debido a que ya se ha asignado folio.\n\nSolicite apoyo con su enlace.";  }
        else if (tipo.equals("NO_PUDO_FIRMAR")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se pudo oficializar 3a evaluacion, probablemente se perdió el enlace con el servidor.\n\nPor favor vuelva a intentarlo.";  }
        else if (tipo.equals("ERROR_WEBSERVICE")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Error en el servicio de oficialización.\n"+texto1;  }
        else if (tipo.equals("MENSAJE_WEBSERVICE")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= texto1;  }
        else if (tipo.equals("DEBE_OFIC_ALUMNO")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Es necesario oficializar primero al alumno con los siguientes datos:\n"+texto1;  }
        else if (tipo.equals("SOLO_TODO_OFIC")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Es necesario tener oficializado todos sus evaluaciones.";  }
        else if (tipo.equals("COMIPEMS")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No se logró realizar la desoficialización;\nEl siguiente alumno se encuentra en la relación de COMIPEMS y tiene asignado un folio de certificado:\n"+texto1;  }
        
        
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//**************************** MENSAJES USADOS EN EL MÓDULO EvalPreescolar ***********************************
//************************************************************************************************************
    public void EvalPreescolar (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("EVAL_OFICIALIZADA"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No podrá editar ni guardar los avances de esta evaluación ya que se encuentra oficializada.\nSi desea desoficializar debe pedir ayuda a su UDR.";  }
        else if (tipo.equals("PVD_SIN_PERMISO_CAPTURA"))          {  tipoMensaje[0]= this.WARNING;    tipoMensaje[1]= "El periodo de registro de la primera evaluación a concluido.";  }
        else if (tipo.equals("TODO_OFICIALIZADO"))     {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "No podrá editar ni guardar ningún dato porque ha oficializado sus tres evaluaciones.\nSi desea desoficializar debe pedir ayuda a su UDR.";  }
        else if (tipo.equals("NADA_QUE_GUARDAR"))       {  this.General("NADA_QUE_GUARDAR", "", "", datosReturn);  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
    
    public void CaptuRepEval (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("EVAL_OFICIALIZADA"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No podrá editar ni guardar los avances de esta evaluación ya que se encuentra oficializada.\nSi desea desoficializar debe pedir ayuda a su UDR.";  }        
        else if (tipo.equals("TODO_OFICIALIZADO"))     {  tipoMensaje[0]= this.INFO;      tipoMensaje[1]= "No podrá editar ni guardar ningún dato porque ha oficializado sus tres evaluaciones.\nSi desea desoficializar debe pedir ayuda a su UDR.";  }
        else if (tipo.equals("NADA_QUE_GUARDAR"))       {  this.General("NADA_QUE_GUARDAR", "", "", datosReturn);  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//******************************* MENSAJES USADOS EN EL MÓDULO Reportes **************************************
//************************************************************************************************************
    public void Reportes (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("FALTA_OFICIALIZAR"))     {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No podrá imprimir su "+texto1+" hasta que haya oficializado todas sus "+texto2+".";  }
        else if (tipo.equals("FALTA_OFIC_GRADOGRUPO")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No podrá imprimir su "+texto1+" hasta que haya oficializado las calificaciones de este grado y grupo.";  }
        else if (tipo.equals("FALTA_FIRMA")) {  tipoMensaje[0]= ""; /*this.ERROR;*/   tipoMensaje[1]= "Certificados en proceso de firma electrónica... Consultar más tarde.";  }
        else if (tipo.equals("SIN_AUTORIZACION"))      {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "No tiene permiso para imprimir este documento "+texto1+".";  }
        else if (tipo.equals("SIN_ALU_REPS"))          {  tipoMensaje[0]= this.INFO;   tipoMensaje[1]= "No hay alumnos para mostrarse en el reporte.";  }
        else if (tipo.equals("CICLO_NO_PERMITIDO"))    {  tipoMensaje[0]= this.INFO;   tipoMensaje[1]= "No tiene permiso de usar un ciclo diferente al que está en curso.";  }
        else if (tipo.equals("USUARIO_REESTRINGIDO"))      {  this.General("USUARIO_REESTRINGIDO", "", "", datosReturn);  }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
//************************************************************************************************************
//************************** MENSAJES USADOS EN EL FORMULARIO Complementaria *********************************
//************************************************************************************************************
    public void Complementaria (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("SOLO_TODO_OFIC"))          {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Es necesario tener oficializado todos sus evaluaciones.";  }
        else if (tipo.equals("NO_SELEC"))           {  General(tipo, texto1, texto2, datosReturn);  return; }
        else if (tipo.equals("NO_PUDO_FIRMAR"))     {  General(tipo, texto1, texto2, datosReturn);  return; }
        else if (tipo.equals("ERROR_WEBSERVICE"))   {  General(tipo, texto1, texto2, datosReturn);  return; }
        else if (tipo.equals("MENSAJE_WEBSERVICE")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= texto1;  }
        else if (tipo.equals("ACT_EXITO")) {  tipoMensaje[0]= this.INFO;   tipoMensaje[1]= texto1;  }
        else if (tipo.equals("USUARIO_REESTRINGIDO")){  this.General("USUARIO_REESTRINGIDO", "", "", datosReturn);  return; }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
    
//************************************************************************************************************
//******************************** MENSAJES USADOS EN EL FORMULARIO Exalumnos ********************************
//************************************************************************************************************
    public void Exalumnos (String tipo, String texto1, String texto2, Map datosReturn)
    {
        String [] tipoMensaje = {"",""};
        
        if (tipo.equals("NIVEL_EDUC_NO_PERMITITO"))     {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "El nivel educativo que ha especificado no está permitido.";  }
        else if (tipo.equals("HISTORIAL_INCOMPLETO"))   {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Este alumno no tiene su historial de secundaria completo.";  }
        else if (tipo.equals("TAM_CURP"))               {  tipoMensaje[0]= this.WARNING; tipoMensaje[1]= "Proporcione mínimo 10 caracteres para la curp.";  }
        else if (tipo.equals("ALUMNO_NO_EXISTE"))       {  tipoMensaje[0]= this.INFO;     tipoMensaje[1]= "No existe en la Base de Datos.";  }
        else if (tipo.equals("DATO_NUMERICO"))               {  tipoMensaje[0]= this.WARNING; tipoMensaje[1]= "Proporcione el ciclo escolar en el que estudió 3er grado.";  }
        else if (tipo.equals("CAMPO_VACIO"))            {  General(tipo, texto1, texto2, datosReturn);  return; }
        else if (tipo.equals("NO_PUDO_FIRMAR"))         {  General(tipo, texto1, texto2, datosReturn);  return; }
        else if (tipo.equals("ERROR_WEBSERVICE"))       {  General(tipo, texto1, texto2, datosReturn);  return; }
        else if (tipo.equals("MENSAJE_WEBSERVICE"))     {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= texto1;  }
        else if (tipo.equals("ESCUELA_NO_PERMITIDA"))   {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "El alumno no estudió en esta escuela.";  }
        else if (tipo.equals("MAS_DE_UNO"))             {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Se encontró más de un alumno, porfavor proporciones más datos.";  }
        else if (tipo.equals("ALUMNO_EN_BAJA"))         {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Este alumno está dado de baja.";  }
        else if (tipo.equals("NO_ES_EXALUMNO"))         {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "Este alumno sigue estudiando en este ciclo vigente.";  }
        else if (tipo.equals("PROMFOL_NO_ACTUALIZADO")) {  tipoMensaje[0]= this.ERROR;   tipoMensaje[1]= "El promedio del folio no se pudo actualizar, ya que se han encontrado inconsistencias.";  }
        else if (tipo.equals("USUARIO_REESTRINGIDO"))   {  General(tipo, texto1, texto2, datosReturn);  return; }
        
        datosReturn.put("tipoMensaje",tipoMensaje[0]);      datosReturn.put("mensaje",tipoMensaje[1]);
    }
}
