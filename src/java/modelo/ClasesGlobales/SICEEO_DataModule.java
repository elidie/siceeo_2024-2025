package modelo.ClasesGlobales;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * @author dai
 */
public class SICEEO_DataModule 
{
    public int cicescini, tiempoEspera;
    
    //Controlador de mensajes de error, warnings y success
   //public int returnCase=1;                                                      //-1:Error de sistema, 0:El sistema no permite seguir, 1: Todo bien, 2:Todo bien con mensaje informativo, 3:Todo bien pero hay que pedir dato, 10:Mostrar mensaje y despues mostrar una ventana, 11:Mostrar una ventana  
    //public String  casoRequerido="";                                            //Es asignado dependiendo del número de returnCase; con 3: debe mencionar que dato se pide, con 10: debe llevar el nombre de la ventana
    //public String mensaje, tipoMensaje;
    
    //Para el queryPrincipal
    //public String vista="", condicion="", orden="";
    
   // int v_cicescin;                                                             // se le asigna el valor cuando inicia el sistema donde pide usuario y contraseÒa
    int v_Califcicescin;                                                        // para navegar x los ciclos escolares en calif de secundaria
    public int v_bim,TiempEspera;
    String v_CalProm,L_ip, v_Direc59__, v_uNewEdit;
    
    //public String ip,nomPc,pc;
    //public Date fecha;
    
    //public String unidad , usuario, tipo_usuario, superUsuario;                 // el v_tipo_usuario es cuando un usuario entra solo a su CCT
    public String cambUsu;                                                      //ES PAra cuando t cambias usuario
        
    String V_open;                                                              //saber si regresaron un folio cancelado A IMPRESO luego entonces hay k hacer un clos y un Open
    String v_conProm;                                                           // para saber si la complementaria de secundaria saldra con promedio o sin promedio S=si
    String v_TipoSec;                                                           //para saber las materias k se imprimen en la R1 difecencia en DSN y las demas (DES)
    String v_Tcicescini;                                                        //ES PAra cuando t cambias de un ciclo a otro para los certificados de secundaria
    int v_Califcicescini;                                                       // para cuando kieres repoprte de calificaciones de ciclos anteriores
    String v_Lcicescini;                                                        //ES PAra cuando asignas un folio del libro diferente al ciclo en el k estudio
    String v_Ucicescini;                                                        //ES PARA eliminar su ultimo ciclo cursado y hacerlo k recurse el grado
    String v_cveplan;                                                           //Valores aceptados PREESCOLAR PRIMARIA SECUNDARIA, es para mostrar en el Catalogo de Escuelas
    String rec_reg;                                                             //para saber kien llama a la forma U_insertaGdoCicAct si
                                                                                //Regresa a Estudiar o
                                                                                //Recursara en otra Escuela
    String v_cicesc;                                                            //para saber de k ciclo escolar se va a imprimir reportes en BLANCO
                                                                                // y tambien para escojer el ciclo de reporte de r1 r2 r3
    String conCalif,conSit, conCveTall, concveArte, v_fecOficio;
    String Complem_condi;                                                       //PARA saber como condicionar en QRY de complementaria
    String Rep_Prom_Folio;                                                      //variable para el reporte sencillo de calif y folio en ciclos anteriores
    String v_CveArte;                                                           //para los rep r1 r2 r3 con calif
    String v_CveProg;                                                           //lo mismo para rep r1 r2 r3
    String v_cBim_aExcel;                                                       //para saber si se kiere pasar las calif bim a excel
    String V_capValid;                                                          //para el modulo de captura y validacion de promedios
    String K_BimSeImp, v_ConDatosEsc,v_mod;                                     //v_mod sirve para k los transitos sean de especial a especial y de CEBAS a CEBAS
    String Esp, Mat;                                                            // por si reprueba espaÒol o matematicas

    String v_CambiarUsuario, v_cvetaller;
    boolean SuspenderTiempo;
        
    String kmpo0,kmpo1,kmpo2,valor, valor1, valor2, elwhere;
    
   // public ArrayList<Object[]> tabla;        
            
    public SICEEO_DataModule (){}
    
    /*public boolean isFloat_(String correo) {
        Pattern pat;
        Matcher mat;        
        pat = Pattern.compile("^\\d{5}(\\.\\d{2})?");                           //"^[0-9]{2}([.][0-9]+$)?"
        mat = pat.matcher(correo);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }        
    }*/
    
    public boolean isNombreOApellido(String texto, int numCaracteres)
    {
        boolean resultado;
        if (  texto.trim().contains("--") || texto.trim().contains("+")
            || texto.trim().contains(",") || texto.trim().contains("°")
            || texto.trim().contains("<") || texto.trim().contains(">")
            || texto.trim().contains("´") || texto.trim().contains(";")
            /*|| texto.trim().contains(".") || texto.trim().contains("-")*/
                
            || texto.trim().contains("0") || texto.trim().contains("1")
            || texto.trim().contains("2") || texto.trim().contains("3")    
            || texto.trim().contains("4") || texto.trim().contains("5")    
            || texto.trim().contains("6") || texto.trim().contains("7")    
            || texto.trim().contains("8") || texto.trim().contains("9"))            
            return false;        
        /*Salvamos las ñ*/
        texto = texto.replace('ñ', '\001');
        texto = texto.replace('Ñ', '\002');
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        /*Volvemos las ñ a la cadena*/
        texto = texto.replace('\001', 'ñ');
        texto = texto.replace('\002', 'Ñ');

        resultado = texto.matches("^([A-Za-zÑñÄËÏÖÜäëïöüÃÕãõÂÊÎÔÛâêîôû'\\-_\\.]{1,"+numCaracteres+"}[ ]?)*[^ ]$");
        
        return resultado;
    }
    

      
    public boolean isInt(String numero)
    {
        return numero.matches("[0-9]+");
    }
    public boolean isFloat (String numero)
    {
        return numero.matches("[0-9]+|[0-9]+.|.[0-9]+|[0-9]+.[0-9]+");
    }
        
    public boolean isDate_Time (String fecha, String formato)
    {
        try {
            java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat(formato);
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    //Invierte una fecha, si esta en formato dd/mm/aaaa la convierte a aaaa/mm/dd y biceversa
    public String invFecha (String fecha, String separador)
    {
        String nuevaFecha="";
        if (separador.equals(""))                                              //Si no se especifica un separador el default será '/'
            separador = "/";
        if (fecha.equals(""))
            return "";
        String []datos = fecha.split(separador);
        //Verificamos que haya un formato correcto
        if (fecha.length()==10 && datos.length==3)
            nuevaFecha = datos[2]+separador+datos[1]+separador+datos[0];
        return nuevaFecha;
    }
    
    /**
    * Calcula la diferencia entre dos fechas. Devuelve el resultado en días, meses o años según sea el valor del parámetro 'caso'
    * @param fechaIni Fecha inicial
    * @param fechaAct Fecha final
    * @param caso 0=TotalAños; 1=TotalMeses; 2=TotalDías; 3=MesesDelAnio; 4=DiasDelMes
    * @return numero de días, meses o años de diferencia
    */
    public long getDifFechas(String fechaIni, String fechaAct, int caso) throws ParseException 
    {
        int anios,mesesPorAnio,diasPorMes,diasTipoMes=0;
        long returnValue;
        
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
        df.setLenient(false);
        java.util.Date fechaInicio = df.parse(fechaIni); 
        java.util.Date fechaFin = df.parse(fechaAct);
        //----- Fecha inicio
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
        int mesInicio = calendarInicio.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
        int anioInicio = calendarInicio.get(Calendar.YEAR);
        //----- Fecha fin
        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTime(fechaFin);
        int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
        int mesFin = calendarFin.get(Calendar.MONTH) + 1; // 0 Enero, 11 Diciembre
        int anioFin = calendarFin.get(Calendar.YEAR);
        //
        // Calculo de días del mes
        //
        if(mesInicio==2)
            diasTipoMes = ((anioFin % 4 == 0) && ((anioFin % 100 != 0) || (anioFin % 400 == 0))) ? 29 : 28;
        else if(mesInicio <= 7)                                             // De Enero a Julio los meses pares tienen 30 y los impares 31
            diasTipoMes = (mesInicio % 2==0) ? 30 : 31;
        else if(mesInicio > 7)                                              // De Agosto a Diciembre los meses pares tienen 31 y los impares 30
            diasTipoMes = (mesInicio % 2 == 0) ? 31 : 30;
        //
        // Calculo de diferencia de año, mes y dia
        //
        if ((anioInicio > anioFin) || (anioInicio == anioFin && mesInicio > mesFin) || (anioInicio == anioFin && mesInicio == mesFin && diaInicio > diaFin)) { // La fecha de inicio es posterior a la fecha fin
                return -1;			
        } else {
            if (mesInicio <= mesFin) {
                anios = anioFin - anioInicio;
                if (diaInicio <= diaFin) {
                    mesesPorAnio = mesFin - mesInicio;
                    diasPorMes = diaFin - diaInicio;
                } else {
                    if (mesFin == mesInicio)
                        anios = anios - 1;
                    mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
                    diasPorMes = diasTipoMes - (diaInicio - diaFin);
                }
            } else {
                anios = anioFin - anioInicio - 1;
                if (diaInicio > diaFin) {
                    mesesPorAnio = mesFin - mesInicio - 1 + 12;
                    diasPorMes = diasTipoMes - (diaInicio - diaFin);
                } else {
                    mesesPorAnio = mesFin - mesInicio + 12;
                    diasPorMes = diaFin - diaInicio;
                }
            }
        }
        //
        // Totales
        //
        switch (caso) {
            case 0: returnValue = anios;  break;                                // Total Años
            case 1: returnValue = anios * 12 + mesesPorAnio;   break;           // Total Meses
            case 2:                                                             // Total Dias (se calcula a partir de los milisegundos por día)
                long millsecsPerDay = 86400000; // Milisegundos al día
                returnValue = (fechaFin.getTime() - fechaInicio.getTime()) / millsecsPerDay;
                break;
            case 3: returnValue = mesesPorAnio;  break;                         // Meses del año
            case 4: returnValue = diasPorMes;  break;                           // Dias del mes
            default: returnValue = -1; break;
        }

       return returnValue;
       
       /* Otra manera más fácil, pero no estoy seguro si da el día exacto
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
        long fechaInicial = df.parse(fechaIni).getTime(); //Tanto fecha inicial como fecha final son Date. 
        long fechaFinal = df.parse(fechaAct).getTime(); 
        long diferencia = fechaFinal - fechaInicial; 
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        if (fechaFinal < fechaInicial)
        int meses = (int)dias/(365+1);
        */
    }
    
    public String extractFloat (String numero, int cantEntero, int cantDecimal)
    {
        String[] extraccion;
        String nuevoNumero="";
        
        extraccion = numero.split("\\.");
        
        if (extraccion[0].length()>cantEntero)
            nuevoNumero=extraccion[0].substring(extraccion[0].length()-cantEntero,extraccion[0].length());
        else
            nuevoNumero=extraccion[0];
        
        if (extraccion[1].length()>=cantDecimal)
            nuevoNumero+="."+extraccion[1].substring(0,cantDecimal);
        else if(extraccion[1].length()==1)
            nuevoNumero+="."+extraccion[1];
        
        return nuevoNumero;
    }
    
    public int toInt (Object dato)
    {
        int entero;
        if (dato==null)
            dato = 0;
        else if ((""+dato).trim().equals("") || dato.equals("null"))
            dato = 0;
        entero = Integer.parseInt((""+dato).trim());
        return entero;
    }
    
    public float toFloat (Object dato)
    {
        float flotante;
        if (dato==null)
            dato = 0;
        else if ((""+dato).trim().equals("") || dato.equals("null"))
            dato = 0;
        flotante = Float.parseFloat(""+dato);
        return flotante;
    }
    
    public BigDecimal toBigDecimal (Object dato)
    {
        BigDecimal bigDecimal;
        if (dato==null)
            dato = 0;
        else if ((""+dato).trim().equals(""))
            dato = 0;
        bigDecimal = new java.math.BigDecimal(""+dato);
        return bigDecimal;
    }
    
    public boolean toBool (Object dato)
    {
        if (dato==null)
            dato = false;
        else if ((""+dato).trim().equals(""))
            dato = false;
        return (""+dato).trim().equals("true");
    }
    
    public float divideFloat (Object dividendo, Object divisor)
    {
        float nuevoDividendo, resultadoTemp, resultado;
        if (dividendo==null || divisor==null)
            return 0;
        if ( !this.isFloat(""+dividendo) || !this.isFloat(""+divisor))
            return 0;
        
        nuevoDividendo=this.toFloat(dividendo)*10;
        resultadoTemp=nuevoDividendo/this.toFloat(divisor);
        resultado=resultadoTemp/10;
        
        return resultado;
    }
    
    public String sumarFraccion (Object sumando1, Object sumando2)
    {
        return this.toBigDecimal(sumando1).add(this.toBigDecimal(sumando2)).toPlainString();
    }
    
    public String dividirFraccion (Object dividendo, Object divisor)
    {
        if (dividendo==null || divisor==null)
            return "0";
        if ( !this.isFloat(""+dividendo) || !this.isFloat(""+divisor))
            return "0";
        
        return this.toBigDecimal(""+dividendo).divide(this.toBigDecimal(""+divisor), 4, RoundingMode.HALF_DOWN).toPlainString();
    }
    
    /**
     * Extrae los datos de un HttpServletRequest segun las claves y los agrega a un Map
     * @param request El HttpServletRequest
     * @param nombresEnRequest Arreglo de nombres que se extraerán del request
     * @param nuevosNombres Arreglo de nombres nuevos si es que se desea cambiar los que trae el request. Si se envía null o vacío agarra por default los nombres de nombresEnRequest
     * Enviar null si es que se desea que quede igual, o dejar en blanco la posición del arreglo para el 
     * nombre que se desea ignorar.
     * @return Map
     */
    public Map reqToMap (HttpServletRequest request, String[] nombresEnRequest, String[]nuevosNombres)
    {
        Map datos = new HashMap();
        int numClaves = nombresEnRequest.length;
        String clave;
        
        if (nuevosNombres!=null && numClaves!=nuevosNombres.length)
            throw new IllegalArgumentException("\n\n*** Error en toMap: nombresEnRequest tiene diferente tamaño a nuevosNombres. ***\n");
        for (int i=0; i<numClaves; i++)
        {
            if (nombresEnRequest[i]==null || (nuevosNombres!=null && nuevosNombres[i]==null))
                throw new IllegalArgumentException("\n\n*** Error en toMap: No pueden existir nombres con valor null. ***\n");
            clave = (nuevosNombres!=null && !nuevosNombres[i].equals(""))?nuevosNombres[i]:nombresEnRequest[i];
            datos.put(clave, request.getParameter(nombresEnRequest[i]));
        }
        return datos;
    }
    
    /**
     * Busca en un ArrayList<Map> las claves donde coincide con sus respectivos valores y retorna la posición donde hizo match, si no 
     * encuentra nada retorna -1.
     * @param tabla La tabla en donde están los datos a buscar
     * @param claves La clave donde buscará.
     * @param valores El valor a buscar.
     * @return Retorna la posición donde se encontró(aron) el(los) valor(es), -1 si no lo encuntra.
     */
    public int indexOfArrMap (ArrayList<Map>tabla, String[] claves, Object[] valores )
    {
        int numFilas;
        int numClaves;
        int numIncidencias;
        
        if (claves.length != claves.length)
            throw new IllegalArgumentException("\n\n*** Error en indexOfArrMap: La posición la cantidad de claves no coincide con la cantidad de valores. ***\n");
        else{
            numFilas = tabla.size();
            numClaves = claves.length;
            for (int i=0; i<numFilas; i++) {
                numIncidencias=0;
                for (int j=0; j<numClaves; j++) {
                    if (tabla.get(i).get(claves[j])==null){
                        if (valores[j]==null)
                            numIncidencias++;
                    } else if (tabla.get(i).get(claves[j]).equals(valores[j]))
                        numIncidencias++;
                }
                if (numIncidencias==numClaves)
                    return i;
            }
        }
        return -1;
    }
    
    /**
     * Convierte un arreglo de strings a un ArrayList y para cada fila un Map
     * @param tabla El arreglo de datos a convertir
     * @param separadorCols El caracter que indicará la separación para la creación de columnas
     * @param nombreDeColumnas Los titulos que recivirá cada columna creada, que se convertirán a tipo Map
     * @return ArrayList \< Map >
     */
    public ArrayList<Map> vstrToArrMap (String[] tabla, String separadorCols, String[] nombreDeColumnas)
    {
        int j, k, numFilas, tamCols = nombreDeColumnas.length;
        boolean bandera=true;
        String filaOrig[];
        Map filaDest;
        ArrayList<Map> arrMap = new ArrayList<Map>();
        if (tabla != null) {
            numFilas=tabla.length;
            for (int i=0; i<numFilas;i++)
            {
                if (bandera && numFilas==1 && tabla[i].equals(""))                  //Si trae una sola fila y la fila viene en blanco
                    break;
                else{
                    filaDest = new HashMap();
                    filaOrig = (tabla[i]+separadorCols+"ƒ").split(separadorCols);                                                             //El caracter ƒ es con el fin de que si en la cadena trae por ejemplo a~b~~~~, el método .split devuelve un vector solo con 2 posiciones. Entonces si hacemos a~b~~~~ƒ, el método .split respetará cada espacio entre los '~' y devolverá un vector de 6 posiciones . Y así nadamás ya no le hacemos caso a la última posición pues es el caracter que metimos de más.
                    if (filaOrig.length-1 != tamCols)
                        throw new IllegalArgumentException("\n\n*** Error en toArrMap: El números de columnas obtenidos para la fila "+i+",\nno coincide con el número de títulos establecidos. ***\n");

                    for (j=0; j<tamCols; j++)
                        filaDest.put(nombreDeColumnas[j], filaOrig[j]);

                    arrMap.add(filaDest);
                    bandera = false;                                                //Esta bandera es para ganar rapidez y no entre a revisar las demás partes del if
                }
            }
        }
        return arrMap;
    }
    
    /**
     * Convierte un string a un Map delimitado cada elemento con el caracter especificado
     * @param fila El arreglo de datos a convertir
     * @param separadorCols El caracter que indicará la separación para la creación de columnas
     * @param nombreDeColumnas Los titulos que recivirá cada columna creada, que se convertirán a tipo Map
     * @return ArrayList \< Map >
     */
    public Map strToMap (String fila, String separadorCols, String[] nombreDeColumnas)
    {
        int tamCols = nombreDeColumnas.length;
        String filaOrig[];
        Map filaDest = new HashMap();
        if (!fila.equals(""))
        {
            filaOrig = fila.split(separadorCols);
            if (filaOrig.length != tamCols)
                throw new IllegalArgumentException("\n\n*** Error en toArrMap: El números de columnas obtenidos para la fila "+fila+",\nno coincide con el número de títulos establecidos. ***\n");
            for (int j=0; j<tamCols; j++)
                filaDest.put(nombreDeColumnas[j], filaOrig[j]);
        }
        return filaDest;
    }
    
    ////request.getParameterValues("idReparacionesYPagos[]")
    private ArrayList<String> toArrStr (String[] arreglo)
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(Arrays.asList(arreglo));
        return arrayList;
        
        //otra forma tambien es
        //ArrayList<String[]>tabla = new ArrayList<String[]>();
        //String fila[];
        //for (int i=0; i<arreglo.length;i++){
        //  fila = arreglo[i].split("~");
        //  tabla.add(fila);
        //}
        //
    }
    
    public String getFechaHoy (String formato)
    {
        String fechaHoy;
        
       java.util.Date fecha = new java.util.Date(); 
       java.text.SimpleDateFormat formateado=new java.text.SimpleDateFormat(formato);
       fechaHoy = formateado.format(fecha);
       return fechaHoy;
    }
    
    public int getNumMes(String txtMes){
        Integer numMes = 0, i = 0;
        String []meses = {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};
        for(i=0; i<12; i++){
            if(txtMes.equals(meses[i])){
                numMes = i+1;
                i = 12;
            }
        }                
        return numMes;
    }
    
    /*Devuelve verdadero o false dependiendo si dada una fecha (en formato dd/MM/yyyy) está dentro del intervalo de dos fechas (en formato dd/MM/yyyy) también proporcionadas*/
    public boolean esFechaIntermedia (String fechaAComparar, String fechaInicial, String fechaFinal) throws ParseException
    {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaIni = sdf.parse(fechaInicial);
            Date fechaFin = sdf.parse(fechaFinal);
            Date fechaDeComparacion = sdf.parse(fechaAComparar);
            
            return ( (fechaDeComparacion.equals(fechaIni)||fechaDeComparacion.after(fechaIni)) && (fechaDeComparacion.equals(fechaFin)||fechaDeComparacion.before(fechaFin)));
    }
    
    public String[] getParametros(String []datos, HttpServletRequest request){                                              //Borrar mientras solo hasta que quede implementado CambioDeGrupo y Cartilla
            int i,  n = datos.length;
            String p[] = new String[n];   
            for(i=0; i<n; i++)
            p[i]= request.getParameter(datos[i]);
            return p;
    }
    
    public boolean permisoVerReportes (HttpServletRequest request)
    {
        HttpSession sesion = request.getSession(false);
        String tipo_usuario = ""+sesion.getAttribute("tipo_usuario");
        String usuario =  ""+sesion.getAttribute("userName");

        if (tipo_usuario.equals(" ") || tipo_usuario.equals("consulta") || tipo_usuario.equals("mesa") || tipo_usuario.equals("captura") || usuario.equals("IVALLE") || usuario.equals("LPOBLETE") || usuario.equals("JULIANCRUZ"))
            return true;
        else                                                                                                                                                      // cuando es un usuario tipo CCT
            return tipo_usuario.equals(request.getParameter("idcct"));
    }
    
    public boolean isIdcctAutorizada (HttpSession sesion, String tblPrincipal_idcct) throws Exception
    {
        String tblPrincipal_distinctIdscct = ""+sesion.getAttribute("tblPrincipal_distinctIdscct");
        if (tblPrincipal_distinctIdscct.contains(tblPrincipal_idcct))
            return true;
        throw new Exception ("idcct no autorizada");
    }
    
    public JsonObject getJson (String jsonStr) throws JsonSyntaxException
    {
        Gson gson = new Gson();
        JsonElement element;
        JsonObject jsonObj = null;
        
        element = gson.fromJson (jsonStr, JsonElement.class);
        jsonObj = element.getAsJsonObject();
        
        return jsonObj;
    }
}
