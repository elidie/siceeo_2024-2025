package modelo.ClasesGlobales;

import java.util.ArrayList;

/**
 *
 * @author dai
 */
public class SICEEO_CoordenadasImpre {

    private ArrayList<Object[]> coordenadas;
    private final int numCols = 9;
    
    public SICEEO_CoordenadasImpre() 
    {
        this.coordenadas = new ArrayList<Object[]>();
    }
    
    public void addDataCoords (int idleyenda, int tamfuente, String fuente, int ordenimpre, String leyenda, String nombreLeyenda, String descLeyenda, int x, int y)
    {
        Object[] fila = new Object[numCols];
        fila[0]=idleyenda;
        fila[1]=tamfuente;
        fila[2]=fuente;
        fila[3]=ordenimpre;
        fila[4]=leyenda;
        fila[5]=nombreLeyenda;
        fila[6]=x;
        fila[7]=y;
        fila[8]=descLeyenda;
        this.coordenadas.add(fila);
    }
    
    public void addDataCoords (int ordenimpre, String leyenda, String nombreLeyenda, String descLeyenda, int x, int y)
    {
        Object[] fila = new Object[7];
        fila[0]=0;
        fila[1]=11;
        fila[2]="Arial";
        fila[3]=ordenimpre;
        fila[4]=leyenda;
        fila[5]=nombreLeyenda;
        fila[6]=x;
        fila[7]=y;
        fila[8]=descLeyenda;
        this.coordenadas.add(fila);
    }
    
    public int getLength ()             { return this.coordenadas.size(); }
    public int getIdLeyenda(int pos) { return (Integer)this.coordenadas.get(pos)[0]; }
    public int getTamfuente(int pos)    { return (Integer)this.coordenadas.get(pos)[1]; }
    public String getFuente(int pos)    { return (String)this.coordenadas.get(pos)[2]; }
    public int getOrdenImpre(int pos)   { return (Integer)this.coordenadas.get(pos)[3]; }
    public String getLeyenda(int pos)   { return (String)this.coordenadas.get(pos)[4]; }
    public String getNombreLeyenda(int pos)   { return (String)this.coordenadas.get(pos)[5]; }
    public int getX(int pos)            { return (Integer)this.coordenadas.get(pos)[6]; }
    public int getY(int pos)            { return (Integer)this.coordenadas.get(pos)[7]; }
    public int getX(String leyenda)     { return (Integer)buscarPosDato (leyenda, 6); }
    public int getY(String leyenda)     { return (Integer)buscarPosDato (leyenda, 7); }
    public String getDescLeyenda(int pos){ return (String)this.coordenadas.get(pos)[8]; } 
    public void clear()                 { this.coordenadas.clear(); }
    
    public ArrayList<Object[]> getCoordenadas(){
        ArrayList<Object[]> tabla = new ArrayList<Object[]>();        
        //Map datos = new HashMap();
        Object fila[];
        for(int i=0; i<this.coordenadas.size(); i++){
            fila = new Object[5];
            fila[0]=this.coordenadas.get(i)[0];
            fila[1]=this.coordenadas.get(i)[4];
            fila[2]=this.coordenadas.get(i)[6];
            fila[3]=this.coordenadas.get(i)[7];
            fila[4]=this.coordenadas.get(i)[8];
            tabla.add(fila);
            //datos.put(""+fila[1],fila);
        }
        return tabla;
        //return datos;
    }
    
    //Retorna el índice si lo encuentra, -1 si no es encontrado. 
    private Object buscarPosDato (Object dato, int caso)
    {
        int tam = coordenadas.size();
        
        for (int i=0; i<tam; i++){
            if (coordenadas.get(i)[5].equals(dato))                             //Modificar el número a donde esté el nombre de la coordenada
                return coordenadas.get(i)[caso];
        }
        return -1;
    }
}
