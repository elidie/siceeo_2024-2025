package modelo.ClasesGlobales;

import java.io.File;
import java.net.URL;

/* 
    Creado el : 13-jul-2017, 17:22:54
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_SourcePath 
{
    private File WORKING_DIRECTORY;
    private final String separador;
    
    public SICEEO_SourcePath ()
    {
        setPath();
        
        if (File.separator.equals("\\"))
            this.separador = "\\\\";
        else
            this.separador = File.separator;
    }
    
    public String getPath()
    {
        return WORKING_DIRECTORY.getAbsolutePath ();
    }
    
    public File getFileInPath ()
    {
        return WORKING_DIRECTORY;
    }
    
    public String getSeparator ()
    {
        return this.separador;
    }
    
    public String getLevelUpPath (int levelUp)
    {
        String [] rutade;
        //if (this.separador.equals("/"))
            rutade = this.getPath().split(this.separador);                      //descompongo la ruta    File.separatorChar
        //else
        //    rutade = this.getPath().split("\\\\");                              //descompongo la ruta    File.separatorChar
        
        int level = rutade.length-levelUp;
        String rutaFinal="";
        
        for (int i=0; i<level; i++)
        {
            if (i==0){
                if (!rutade[0].equals(""))
                    rutaFinal += rutade[i];
                else if (level == 1){
                    if (rutade[0].equals(""))
                        rutaFinal += this.separador;
                }
            }else
                rutaFinal += this.separador+rutade[i];
        }
        
        return rutaFinal;
    }
    
    public String getPathThisClass (Class clase)
    {
        URL rutaca = clase.getProtectionDomain().getCodeSource().getLocation();
        return rutaca.toString().replace("file:", "").replace("%", " ");
    }
    
    private void setPath() 
    {
        String Recurso = SICEEO_SourcePath.class.getSimpleName() + ".class";
        if (WORKING_DIRECTORY == null) {
            try {
                URL url = SICEEO_SourcePath.class.getResource(Recurso);
                if (url.getProtocol().equals("file")) {
                    File f = new File(url.toURI());
                    do {
                        f = f.getParentFile();
                    } while (!f.isDirectory());
                    WORKING_DIRECTORY = f;
                } else if (url.getProtocol().equals("jar")) {
                    String expected = "!/" + Recurso;
                    String s = url.toString();
                    s = s.substring(4);
                    s = s.substring(0, s.length() - expected.length());
                    File f = new File(new URL(s).toURI());
                    do {
                        f = f.getParentFile();
                    } while (!f.isDirectory());
                    WORKING_DIRECTORY = f;
                }
            } catch (Exception e) {
                WORKING_DIRECTORY = new File(".");
            }
        }   
    }
}
