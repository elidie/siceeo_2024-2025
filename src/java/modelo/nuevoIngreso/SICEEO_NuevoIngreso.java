package modelo.nuevoIngreso;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;

/**
 *
 * @author ely
 */
public class SICEEO_NuevoIngreso {
    private final SICEEO_HttpServletRequest r;
    private final SICEEO_DataModule dm;
    private final Map dr;

    
    public SICEEO_NuevoIngreso (HttpServletRequest request, HttpServletResponse response,Map datosReturn){
        this.r = new SICEEO_HttpServletRequest(request);
        this.dr = datosReturn;
        this.dm =  new SICEEO_DataModule();
    }
    
    public void nuevoIngreso(HttpServletRequest request)
    {   
        switch(dm.toInt(r.gP("tblPrincipal_cveplan"))){
            case 1: // Primaria    
                    SICEEO_NewIngresoPri nuevoIngresoPri = new SICEEO_NewIngresoPri(dr, request);
                    nuevoIngresoPri.ejecutarPeticion (r.gP("metodo"));
            break;
            case 2: //Secundaria                
                    SICEEO_NewIngresoSec nuevoIngresoSec = new SICEEO_NewIngresoSec(dr, request);
                    nuevoIngresoSec.ejecutarPeticion (r.gP("metodo"));
            break;
            case 3: //Preescolar
                    SICEEO_NewIngresoPre nuevoIngresoPre = new SICEEO_NewIngresoPre(dr, request);
                    nuevoIngresoPre.ejecutarPeticion (r.gP("metodo"));
            break;        
            default: break;
        }    
    }
}
