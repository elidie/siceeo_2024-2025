package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/**
 *
 * Creado el : 30/05/2015, 02:07:36 PM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

public class SICEEO_PromGralSec2012 
{
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    
    public SICEEO_PromGralSec2012 (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.r = new SICEEO_HttpServletRequest(request);
        this.sesion = request.getSession(false);
    }
    
/*******************************************************************************/
/****************************** ÁREA PARA EL CONTROLADOR ***********************/
/*******************************************************************************/
    
    /**
     *
     * @param metodo Caso de método al que se va a llamar
     */
    public void ejecutarPeticion (String metodo)
    {
        if (metodo.equals("btGu"))
            btnGuarda_Click (dm.toInt(r.gP("tblAlumCapCalif_matrepact")), r.gP("tblAlumCapCalif_promediogral"), r.gP("califCicEscIn"), 
                    r.gP("tblAlumCapCalif_promedioeb"), false/*dm.toBool(r.gP("chkEditProm_isChecked"))*/, ""+sesion.getAttribute("userName"), r.gP("tblAlumCapCalif_idalu")
            );
        
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    public void btnGuarda_Click(int tblAlumCapCalif_MatRepAct, String tblAlumCapCalif_promediogral, String califCicEscIn, String tblAlumCapCalif_promedioeb, boolean chkEditProm_isChecked, String txtUsuario, String tblAlumCapCalif_idalu)
    {
        Map tblAlumCapCalif;
        try
        {
            qryIfx.conectar();
            // if (dm.Q_Alum_cap_califpromediogral.AsFloat >=6.0) and <==quitamos esta condicion x k no importa si aun no consigue su prom ya k tiene mas oportunidades
            tblAlumCapCalif=qryIfx.actualizaPromedioGral2012 ( tblAlumCapCalif_MatRepAct,  tblAlumCapCalif_promediogral,  califCicEscIn,  tblAlumCapCalif_promedioeb,  chkEditProm_isChecked,  txtUsuario,  tblAlumCapCalif_idalu);
            dr.put("tblAlumCapCalif", tblAlumCapCalif);
        }catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
}
