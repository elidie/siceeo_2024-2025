package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
/*import javax.servlet.http.HttpSession;*/
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;


/**
 *
 * Creado el : 30/05/2015, 02:06:29 PM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

public class SICEEO_BimXAlum {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_BimXAlum (Map datosReturn)
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
        
        if (metodo.equals("foAc"))
            FormActivate ( dm.toInt(r.gP("tblPrincipal_grado")), dm.toInt(r.gP("tblPrincipal_cveplan")), r.gP("tblAlumCapCalif_idalu"), r.gP("tblAlumCapCalif_cicescini"));
        else if (metodo.equals("exDaEx"))
            extraerDatosDeExcel (r.gPV("tblAlumCapCalif_idalu"), r.gP("tblAlumCapCalif_cicescini"));
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    public void FormActivate (int tblPrincipal_grado, int tblPrincipal_cveplan, String tblAlumCapCalif_idalu, String tblAlumCapCalif_cicescini)
    {
        try
        {
            qryIfx.conectar();
            
            if (tblPrincipal_grado==3 && tblPrincipal_cveplan==2) 
            {
                dr.put("pnl1ro_Visible",true);
                dr.put("pnl2do_Visible",true);
                dr.put("tblCalifBimXAlum",qryIfx.califBimXAlum(tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini));
                dr.put("tblCalif1ro",qryIfx.calif1ro (tblAlumCapCalif_idalu));
                dr.put("tblCalif2do",qryIfx.calif2do (tblAlumCapCalif_idalu));
            }else
            {
                dr.put("pnl1ro_Visible",false);                
                dr.put("pnl2do_Visible",false);
                dr.put("tblCalifBimXAlum",qryIfx.califBimXAlum(tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini));
            }
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    public void extraerDatosDeExcel (String[] tblAlumCapCalif_idalu, String tblAlumCapCalif_cicescini)
    {
        String idalus="";
        
        for (int i=0; i<tblAlumCapCalif_idalu.length; i++)
            idalus += i==0?tblAlumCapCalif_idalu[i]:","+tblAlumCapCalif_idalu[i];
        try
        {
            qryIfx.conectar();
            dr.put("tblCalifsBimXAlum",qryIfx.califBimXAlumToExcel(idalus, tblAlumCapCalif_cicescini));
            dr.put("tblInasistenciasXAlum",qryIfx.inasistenciasToExcel(tblAlumCapCalif_cicescini,""+((ArrayList<Map>)dr.get("tblCalifsBimXAlum")).get(0).get("grado"), idalus));
        }catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}
