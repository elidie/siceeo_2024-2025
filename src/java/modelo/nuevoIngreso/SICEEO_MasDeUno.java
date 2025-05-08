package modelo.nuevoIngreso;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/* 
    Creado el : 4/12/2015, 08:41:27 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_MasDeUno {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_MasDeUno (Map datosReturn)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
    }
    
/*******************************************************************************/
/****************************** ÁREA PARA EL CONTROLADOR ***********************/
/*******************************************************************************/
    /**
     *
     * @param request Solicitud del servidor
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (HttpServletRequest request, String metodo)
    {
        this.r = new SICEEO_HttpServletRequest(request);
        
        if (metodo.equals("tbCuRe_MoCl")){
            tblCurpsRep_MouseClicked (r.gP("idalu"));
        }
    }
    
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    private void tblCurpsRep_MouseClicked (String idalu)
    {
        try
        {
            qryIfx.conectar();
            dr.put("tblHistorialAcad",qryIfx.susEstud(idalu));
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
