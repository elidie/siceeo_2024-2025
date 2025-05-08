package modelo.ClasesGlobales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/* 
    Creado el : 13/07/2017, 01:15:03 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_FileReader 
{
    String filePath;
    
    public SICEEO_FileReader (String filePath)
    {
        this.filePath = filePath;
    }
    
    public void setUrl (String filePath)
    {
        this.filePath = filePath;
    }
    
    public String leerFichero (boolean concatenarLineas) throws FileNotFoundException, Exception
    {
      File archivo;
      FileReader fr = null;
      BufferedReader br;
      
      String linea;
      String texto="";

      try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File (this.filePath);
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);

         // Lectura del fichero
         while((linea=br.readLine())!=null){
            if (concatenarLineas)
                texto += linea;
            else
                texto += (linea.equals("")?"":"\n")+linea;
         }
      }/*catch (FileNotFoundException ex){
          throw new FileNotFoundException();
      }catch(Exception e){
         e.printStackTrace();
      }*/
      finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         //try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         //}catch (Exception e2){ 
         //   e2.printStackTrace();
         //}
      }
      
      return texto;
    }
    
    public void escribirFichero (String filePath) throws FileNotFoundException, Exception
    {
       
        FileWriter fichero = null;
        PrintWriter pw;
        try
        {
            fichero = new FileWriter(this.filePath);
            pw = new PrintWriter(fichero);

            for (int i = 0; i < 10; i++)
                pw.println("Linea " + i);

        } /*catch (Exception e) {
            e.printStackTrace();
        } */
        finally {
           //try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           //} catch (Exception e2) {
           //   e2.printStackTrace();
           //}
        }
    
    }
}
