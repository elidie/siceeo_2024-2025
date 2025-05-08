/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.RepEvaluacion;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_CoordenadasImpre;
import modelo.ClasesGlobales.SICEEO_DataModule;

/**
 *
 * @author dai
 */
    
public class SICEEO_ImprimeRepoteEvaluacion
{
    private SICEEO_QueriesInformix qryIfx;
    private final SICEEO_DataModule dm;
    
    private int posAluActual, alturaFuente, numPaginas, numMaterias;
    private float tamFuente;
    private String cicescini, idcct, modalidad, cveplan, grado, configImpre, formato, fuente, tipoImpresion;
               //-------- para las coordenadas de los objetos --------\\
    private SICEEO_CoordenadasImpre p;
               //-------- Datos que lleva la cartilla ------------\\
    private ArrayList<Map>QGrupoRepEvaluacion;
    private Map QInasistencias, QCalifsDeAlumnos;
    private String cveturno, nombreCCT, cct, grupo, cicesc;
                //-------- Datos para configurar el documento PDF (área dibujable) ------------\\
    int w = 590, h = 760, x = 0, y = 27;   //Area de impresora:w=612, Area en hoja de pantalla:w=650
    Graphics2D g2;
    PdfContentByte cb;
    PdfTemplate tp;
    
    public SICEEO_ImprimeRepoteEvaluacion()
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.dm = new SICEEO_DataModule ();
        this.tipoImpresion = "real";
        this.tamFuente = 8;
        this.numMaterias = 0;
    }
    
    public void initData(String cicescini, String idcct, String modalidad, String cveplan, String grado, String configImpre, String formato)
    {
        this.cicescini = cicescini;
        this.idcct=idcct;
        this.modalidad = modalidad;
        this.cveplan=cveplan;
        this.grado=grado;
        this.configImpre = configImpre;
        this.formato = formato;
    }
   
    public void crearDocumentoPDF (Document document, PdfWriter writer, DefaultFontMapper mapper) throws FileNotFoundException, FontFormatException, IOException
    {
        if (this.tipoImpresion.equals("real")){
            for (int i=0; i<numPaginas; i++){
                configurarDocumento (writer, mapper);
                this.posAluActual = i;
                imprimirReporteDeEvaluacion();
                g2.dispose();
                cb.addTemplate(tp,x,y);
                if (i+1!=numPaginas)
                    document.newPage();
            }
        }else if (this.tipoImpresion.equals("caidaDeTextosPrueba")){
            configurarDocumento (writer, mapper);
            imprimirCaidaDeTextos (g2);
            g2.dispose();
            cb.addTemplate(tp,x,y);
        }else if (this.tipoImpresion.equals("lineasGuia")){
            configurarDocumento (writer, mapper);
            imprimirLineasGuia (g2);
            g2.dispose();
            cb.addTemplate(tp,x,y);
        }
    }

    public void setTipoImpresion (int tipoImpre) throws SQLException, ClassNotFoundException
    {
        switch (tipoImpre)
        {
            case 0: this.tipoImpresion = "real"; cargarCoordenadas (); break;
            case 1: this.tipoImpresion = "caidaDeTextosPrueba"; cargarCoordenadas (); break;
            case 2: this.tipoImpresion = "lineasGuia"; break;
            default : this.tipoImpresion = "lineasGuia";
        }
    }
        
    private void imprimirCaidaDeTextos (Graphics2D g2)
    {
        int tam = p.getLength();
        
        imprimirLineasGuia(g2);
        
        g2.setFont(new Font("Arial", Font.PLAIN, (int) 8));  //Draft 12pci
        
        for (int i=0; i<tam;i++)
            g2.drawString(p.getLeyenda(i), p.getX(i),p.getY(i));
    }

    private void imprimirLineasGuia (Graphics2D g2)
    {        
        int margenSup=30, largo=760, margenIzq=20, ancho=620, incX=20, incY=10;

        g2.setFont(new Font("Arial", Font.PLAIN, (int) 9));  //Draft 12pci
        g2.setColor(Color.RED);
        //g2.setFont(dynamicFont.deriveFont (9f));

        //--------------------------- Lineas Horizontales ---------------------------
        //String vertical = "_||| VERTICAL |||_";
        //for (i=300, j=0; j<vertical.length(); i+=10, j++)                      
        //    g2.drawString(""+vertical.charAt(j), izq-20, i);
        g2.drawString("0", margenIzq+2, margenSup);                                      //Escribe los números del lado izquierdo
        for (int y=margenSup, numero=0; y<=largo; y+=incY, numero++){
            if (numero>0){
                g2.drawString(""+numero, margenIzq, y+4);                             //Escribe los números del lado izquierdo
                g2.drawLine(margenIzq+15, y, ancho, y);                                 //Líneas horizontales (de izq a der)
            }
        }
        
        //--------------------------- Lineas Verticales ----------------------------
        g2.drawString("<--- HORIZONTAL --->", 250, margenSup-10);
        for (int x=margenIzq*2, numero=1; x<=ancho; x+=incX, numero++){
           g2.drawString(""+numero, x-5, margenSup-2);                             //Escribe el texto en la parte superior
           g2.drawLine(x, margenSup+5, x, largo);                                  //Líneas verticales (arriba a abajo)
        }

        //g2.setFont(dynamicFont.deriveFont (tamFuente));
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, (int) tamFuente));  //Draft 12pci
    }
    
    private void imprimirReporteDeEvaluacion ()
    {
        int g = Integer.parseInt(grado), totalInasis=0;
        int posFilaExmext=1, numCalifs;
        String idalu, inasistencia, inasisAcumuladas="", exmext, exmextAcumulados="", cvetipmat;
        boolean esMatDefault;
        ArrayList<Map> califsRepEvaluacion;
        
        idalu = ""+QGrupoRepEvaluacion.get(this.posAluActual).get("idalu");
        
        //------------ Datos parte superior de la cartilla --------------\\
        g2.drawString(this.cicesc, p.getX("cicescini"), p.getY("cicescini"));
        g2.drawString(""+QGrupoRepEvaluacion.get(this.posAluActual).get("apepat"), p.getX("ape1"), p.getY("ape1"));
        g2.drawString(""+QGrupoRepEvaluacion.get(this.posAluActual).get("apemat"), p.getX("ape2"), p.getY("ape2"));
        g2.drawString(""+QGrupoRepEvaluacion.get(this.posAluActual).get("nombre"), p.getX("nombre"), p.getY("nombre"));
        g2.drawString(""+QGrupoRepEvaluacion.get(this.posAluActual).get("curp"), p.getX("curp"), p.getY("curp"));
        g2.drawString(this.nombreCCT, p.getX("nombreEscuela"), p.getY("nombreEscuela"));
        g2.drawString(this.grupo.replace("_", ""), p.getX("grupo"), p.getY("grupo"));
        g2.drawString(this.cveturno, p.getX("turno"), p.getY("turno"));
        g2.drawString(this.cct, p.getX("cct"), p.getY("cct"));
        
        //--------------------- Calificaciones ---------------------------\\
        califsRepEvaluacion = (ArrayList)QCalifsDeAlumnos.get(idalu);
        numCalifs = califsRepEvaluacion.size();
        for (int fila=0; fila<this.numMaterias && numCalifs>0; fila++){
            for (int columna=0; columna<5; columna++){
                if (columna==0)
                    g2.drawString(""+califsRepEvaluacion.get(fila).get("bimestre"+(columna+1)), p.getX("mat"+(fila+1)+"Bim"+(columna+1)), p.getY("mat"+(fila+1)+"Bim"+(columna+1)));
                else
                    g2.drawString(""+califsRepEvaluacion.get(fila).get("bimestre"+(columna+1)), p.getX("mat1Bim"+(columna+1)), p.getY("mat"+(fila+1)+"Bim1"));
                
                if (fila==0){
                    try { inasistencia = ""+((Map)this.QInasistencias.get(idalu)).get("inst"+(columna+1)); } catch (NullPointerException ex){ inasistencia=""; }
                    inasisAcumuladas += inasistencia;
                    totalInasis += dm.toInt(inasistencia);
                    g2.drawString(inasistencia, p.getX("mat1Bim"+(columna+1)), p.getY("inasisBim1"));
                }
                
                //---------------- Exámenes extraordinarios ----------------------\\
                if (this.cveplan.equals("2") && columna<4)
                {
                    exmext = ""+califsRepEvaluacion.get(fila).get("exmextbim"+(columna+1)); //Obtenemos la calificacion
                    exmextAcumulados += exmext;                                          //Acumulamos texto para ver si hubo calificaiones
                    g2.drawString(exmext, p.getX("califExamExtraBim"+(columna+1)), p.getY("examExtraord"+(posFilaExmext))); //Imprimimos la calificacion, no importa si viene en blanco
                    if (columna==3 && !exmextAcumulados.equals("")){                     //Si ya estamos revisando el 4to bimestre y sí hubo calificaciones
                        g2.drawString(""+califsRepEvaluacion.get(fila).get("desmat"), p.getX("examExtraord"+posFilaExmext), p.getY("examExtraord"+posFilaExmext)); //Mandamos a imprimir el nombre de la materia
                        posFilaExmext++;                                                 //Avanzamos la fila de exámenes extraordinarios
                    }
                    
                }
            }
            g2.drawString(""+califsRepEvaluacion.get(fila).get("promedio"), p.getX("mat1Prom"), p.getY("mat"+(fila+1)+"Bim1"));
            exmextAcumulados = "";
            
            //----------- Tecnologías, artes y asignatura estatal ----------------\\
            if (this.cveplan.equals("2"))
            { 
                cvetipmat = ""+califsRepEvaluacion.get(fila).get("cvetipmat");
                if (cvetipmat.equals("EDT") || cvetipmat.equals("TEC") || cvetipmat.equals("ART") || cvetipmat.equals("AES"))
                {
                    esMatDefault = califsRepEvaluacion.get(fila).get("matdefault").equals("t");
                    if (cvetipmat.equals("EDT") || cvetipmat.equals("TEC")){
                        g2.drawString(""+califsRepEvaluacion.get(fila).get("cvemat"), p.getX("claveTec"), p.getY("claveTec"));
                        if ( (""+califsRepEvaluacion.get(fila).get("desmat")).length()>=40 )
                            g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)this.tamFuente-3));
                        else if ( (""+califsRepEvaluacion.get(fila).get("desmat")).length()>=35 )
                            g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)this.tamFuente-2));
                        g2.drawString(getMatDefault(""+califsRepEvaluacion.get(fila).get("desmat"),esMatDefault), p.getX("enfasisTec"), p.getY("enfasisTec"));
                        g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)this.tamFuente));
                    }else if (!esMatDefault && cvetipmat.equals("ART"))
                        g2.drawString(getMatDefault(""+califsRepEvaluacion.get(fila).get("desmat"),esMatDefault), p.getX("arte"), p.getY("arte"));
                    else if (!esMatDefault && cvetipmat.equals("AES")){
                        if ( (""+califsRepEvaluacion.get(fila).get("desmat")).length()>=40 )
                            g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)this.tamFuente-3));
                        else if ( (""+califsRepEvaluacion.get(fila).get("desmat")).length()>=35 )
                            g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)this.tamFuente-2));
                        g2.drawString(getMatDefault(""+califsRepEvaluacion.get(fila).get("desmat"),esMatDefault), p.getX("asignaturaEstatal"), p.getY("asignaturaEstatal"));
                        g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)this.tamFuente));
                    }
                }
            }
        }
        if (!inasisAcumuladas.equals(""))
            g2.drawString(""+totalInasis, p.getX("mat1Prom"), p.getY("inasisBim1"));
        
        g2.drawString(""+QGrupoRepEvaluacion.get(this.posAluActual).get("promedio"), p.getX("promGrado"), p.getY("promGrado"));
        if ( this.grado.equals("6") || (this.cveplan.equals("2") && this.grado.equals("3")))
            g2.drawString(""+QGrupoRepEvaluacion.get(this.posAluActual).get("promediogral"), p.getX("promNivel"), p.getY("promNivel"));
        
        if ( this.cveplan.equals("1"))
        {
            if ( dm.toInt(this.grado) >= 2 ){
                if (QGrupoRepEvaluacion.get(this.posAluActual).get("promovido").equals("P"))
                    g2.fillOval(p.getX("promovido"), p.getY("promovido"), 10, 10);
                else if (QGrupoRepEvaluacion.get(this.posAluActual).get("promovido").equals("NP"))
                    g2.fillOval(p.getX("noPromovido"), p.getY("noPromovido"), 10, 10);
                else if ( dm.toInt(this.grado) <= 5 && QGrupoRepEvaluacion.get(this.posAluActual).get("promovido").equals("PC"))
                    g2.fillOval(p.getX("promovidoConCondicion"), p.getY("promovidoConCondicion"), 10, 10);
                
            }
            
            if (this.modalidad.equals("DPB")){                                  //Para primaria indígena
                if (QGrupoRepEvaluacion.get(this.posAluActual).get("cvelengua").equals("ESP"))
                    g2.drawString(""+QGrupoRepEvaluacion.get(this.posAluActual).get("deslengua"), p.getX("lenguaIndigena"), p.getY("lenguaIndigena"));
            }
        }
    }
    
    private String corregirPromedio(String calificacion)
    {
        if (calificacion.equals("10."))
            return "10";
        return calificacion;
    }
    
    private String getMatDefault (String materia, boolean isdefault)
    {
        int posParentesis;
        String matDefault="";
        if (!isdefault)
        {
            if ((posParentesis=materia.indexOf('('))!=-1){
                matDefault = materia.substring(posParentesis+1);
                matDefault = matDefault.substring(0,matDefault.length()-1);
                return matDefault;
            }else
                matDefault = materia;
        }
        return matDefault;
    }
    
    public void getDatosImpresion (String cveturno, String nombreCCT, String cct, String grupo, String idalus) throws SQLException, ClassNotFoundException, Exception
    {
        try 
        {
            qryIfx.conectar();
            this.QGrupoRepEvaluacion = qryIfx.getGrupoRepEvaluacion (this.cicescini, this.cveplan, this.idcct, this.grado, grupo, idalus);                 //Hacemos consulta para extraer las coordenadas
            this.QCalifsDeAlumnos = qryIfx.getCalifsRepEvaluacion (this.cicescini, this.idcct, this.cveplan, this.grado, grupo, idalus);
            this.QInasistencias = qryIfx.getInasistenciasRepEval (this.cicescini, this.cveplan, this.grado, idalus);

            this.numPaginas = QGrupoRepEvaluacion.size();
            this.cicesc = this.cicescini+" - "+(Integer.parseInt(this.cicescini)+1);
            this.cveturno = cveturno;
            this.nombreCCT = nombreCCT;
            this.cct = cct;
            this.grupo = grupo;
        }catch (SQLException ex){ throw new SQLException(ex); }
        catch (Exception ex){ throw new SQLException(ex); }
        try { qryIfx.cerrarConexion(); } catch (SQLException ex) { }
    }

     private void configurarDocumento (PdfWriter writer, DefaultFontMapper mapper) throws FileNotFoundException, FontFormatException, IOException
    {
        cb = writer.getDirectContent();
        tp = cb.createTemplate(w, h);
        g2 = tp.createGraphics(w, h, mapper);

        g2.setColor(new Color(0,0,0));
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)this.tamFuente));
        
        //g2.setFont(dynamicFont.deriveFont (tamFuente));
        FontMetrics metrica = g2.getFontMetrics();
        alturaFuente = metrica.getAscent()-(metrica.getDescent()+metrica.getLeading());
    }
   
    private void cargarCoordenadas () throws SQLException, ClassNotFoundException
    {
        p = new SICEEO_CoordenadasImpre();

        try{
            qryIfx.conectar();
            numMaterias = qryIfx.getNumMats (this.modalidad, this.cveplan, this.grado, this.cicescini);
            p = qryIfx.obtenerCoordenadas (this.idcct, this.modalidad, this.cveplan, this.grado, this.configImpre, this.formato, this.tipoImpresion);                 //Hacemos consulta para extraer las coordenadas
            tamFuente = p.getTamfuente(0);
            fuente = p.getFuente(0);
        }catch (SQLException ex){ throw new SQLException(ex); }
        catch (Exception ex){ throw new SQLException(ex); }
        try { qryIfx.cerrarConexion(); } catch (SQLException ex) { }
    }
    
    public boolean permisosVerReporteRepEvaluacion (HttpServletRequest request)
    {
        HttpSession sesion = request.getSession(false);
        String tipo_usuario = ""+sesion.getAttribute("tipo_usuario");
        String usuario =  ""+sesion.getAttribute("userName");

        if (tipo_usuario.equals(" ") || tipo_usuario.equals("consulta") || tipo_usuario.equals("mesa") || tipo_usuario.equals("captura") || usuario.equals("IVALLE") || usuario.equals("LPOBLETE") || usuario.equals("JULIANCRUZ"))
            return true;
        else                                                                                                                                                      // cuando es un usuario tipo CCT
        {
            if (tipo_usuario.equals(request.getParameter("idcct")))
                return true;
            else
                return false;
        }
    }
}