package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/**
 *
 * Creado el : 30/05/2015, 02:08:40 PM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

public class SICEEO_PromGralSec 
{

    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_PromGralSec (Map datosReturn)
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
        
        if (metodo.equals("btGu"))
            btnGuarda_Click (dm.toInt(r.gP("tblAlumCapCalif_matrepact")), r.gP("tblAlumCapCalif_promediogral"), r.gP("txtUsuario").toUpperCase(), r.gP("califCicEscIn"), r.gP("tblAlumCapCalif_idalu"));
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    
    public void btnGuarda_Click(int tblAlumCapCalif_MatRepAct, String tblAlumCapCalif_promediogral, String txtUsuario, String califCicEscIn, String tblAlumCapCalif_idalu)
    {
        Map tblAlumCapCalif;
        try
        {
            qryIfx.conectar();
              // if (dm.Q_Alum_cap_califpromediogral.AsFloat >=6.0) and <==quitamos esta condicion x k no importa si aun no consigue su prom ya k tiene mas oportunidades
            tblAlumCapCalif=qryIfx.actualizaPromedioGral(tblAlumCapCalif_MatRepAct, tblAlumCapCalif_promediogral, txtUsuario, califCicEscIn, tblAlumCapCalif_idalu);
            dr.put("tblAlumCapCalif", tblAlumCapCalif);
        }catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
}
