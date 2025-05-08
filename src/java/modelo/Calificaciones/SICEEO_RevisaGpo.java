
package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.DAO.SICEEO_QueriesInformix;

/* 
    Creado el : 17/08/2017, 12:13:04 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_RevisaGpo {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_RevisaGpo (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.r = new SICEEO_HttpServletRequest(request);
        this.sesion = request.getSession(false);
        this.request = request;
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
        String superUsuario = ""+sesion.getAttribute("superUsuario");
        String txtUsuario = ""+sesion.getAttribute("userName");
        
        if (metodo.equals("foAc"))
            formActivate ( r.gP("califCicEscIn"), r.gP("cicescin"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate (String califCicEscIn, String cicescin, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        try
        {
            qryIfx.conectar();
            dr.put("tblAlumnos",qryIfx.revisaGpo(califCicEscIn.equals(cicescin)?"I":"BD", califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
