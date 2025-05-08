/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.RepEvaluacion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_CoordenadasImpre;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/**
 *
 * @author ely
 */
public class SICEEO_CalibrarCaida {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_CalibrarCaida (Map datosReturn)
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
        
        if ( metodo.equals("geDaPaCa") )
            getDatosParaCalibrar(r.gP("idcct"), r.gP("modalidad"), r.gP("cveplan"), r.gP("grado"), "REPORTE_EVALUACION");                               
        else if ( metodo.equals("guDaDeCa") )
            guardarDatosDeCalibracion(r.gPV("coordenadas"),r.gP("idcct"), r.gP("modalidad"), r.gP("cveplan"), r.gP("grado"), r.gP("configImpre"), "REPORTE_EVALUACION");
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
  
    
    public void getDatosParaCalibrar(String idcct, String modalidad, String cveplan, String grado, String formato) 
    {
        ArrayList<String[]> ConfigsImpreDelFormato, configsImpreDelCCT;
        SICEEO_CoordenadasImpre datosDeCoordenadas;
        boolean hacerCommit = false;
        
        try{
            qryIfx.conectarConTransaccion();
            
            ConfigsImpreDelFormato = qryIfx.getConfigsImpreAsignadasAlFormato (cveplan, grado, idcct, modalidad, formato);
            if (ConfigsImpreDelFormato.isEmpty())                                           //Si no tiene configuraciones asignado al documento
                datosDeCoordenadas = qryIfx.obtenerCoordenadas ("0", modalidad, cveplan, grado, "DAI", formato, "caidaDeTextosPrueba");   //escogemos las coordenadas DAI
            else
                datosDeCoordenadas = qryIfx.obtenerCoordenadas (idcct, modalidad, cveplan, grado, ConfigsImpreDelFormato.get(0)[1], formato,"caidaDeTextosPrueba"); //si sí, escogemos las coordenadas que corresponden a su escuela
            dr.put("coord",datosDeCoordenadas.getCoordenadas());
            //configsImpreDelCCT = qryIfx.getConfigsImpreDelCCT(idcct);
            //dr.put("configsImpre", formatearDatosImpresoras(configsImpreDelCCT, ConfigsImpreDelFormato));
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    /*private String formatearDatosImpresoras (ArrayList<String[]> configsImpreDelCCT, ArrayList<String[]> configsImpreDelFormato)
    {
        String configuraciones="", pertenece="0";
        for (int i=0; i<configsImpreDelCCT.size(); i++)
        {
            for (int j=0; j<configsImpreDelFormato.size(); j++)
                if (configsImpreDelCCT.get(i)[1].equals(configsImpreDelFormato.get(j)[1]))
                    {   pertenece="1"; break;  }
            if (i==0)
                configuraciones += configsImpreDelCCT.get(i)[0]+"-"+configsImpreDelCCT.get(i)[1]+"-"+pertenece;
            else
                configuraciones += ","+configsImpreDelCCT.get(i)[0]+"-"+configsImpreDelCCT.get(i)[1]+"-"+pertenece;
            pertenece="0";
        }
        return configuraciones;
    }*/
    
    public void guardarDatosDeCalibracion (String[] coordenadas, String idcct, String modalidad, String cveplan, String grado, String configImpre, String formato)
    {
        boolean hacerCommit=false;
        try{
            qryIfx.conectarConTransaccion();
            qryIfx.guardarDatosDeCalibracion (coordenadas, idcct, modalidad, cveplan, grado, configImpre, formato);
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
}
