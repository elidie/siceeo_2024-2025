package modelo.Reportes;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.DAO.SICEEO_QueriesInformix;
//import webservices.WsfirmaDatosReturn;

/* 
    Creado el : 3/08/2017, 04:23:02 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

public class SICEEO_Complementaria {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    private final String superUsuario, txtUsuario;
    private final boolean tienePrivilegios, esUsrAdmin;
    
    public SICEEO_Complementaria (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.r = new SICEEO_HttpServletRequest(request);
        this.sesion = request.getSession(false);
        this.request = request;
        
        superUsuario = ""+sesion.getAttribute("superUsuario");
        txtUsuario = ""+sesion.getAttribute("userName");
        tienePrivilegios = ( (esUsrAdmin=(txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ"))) || superUsuario.equals("si"));
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
        if (metodo.equals("foAc"))
            formActivate ( r.gP("tblPrincipal_cicescinilib"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("casoRep"));
        else if (metodo.equals("btOfCeCo_cl"))
            btnOficCerCompl_Click ( r.gP("tblPrincipal_cicescinilib"), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_grupo"), r.gP("idalus"), txtUsuario);
        else if (metodo.equals("geAlSeMeCo"))
            getAlumnosSelMesCompl ( r.gP("tblPrincipal_cicescinilib"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"));
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    private void formActivate (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String casoRep)
    {
        try
        {
            qryIfx.conectar();
            
            if (casoRep.equals("oficCertCompl"))
                dr.put("tblAlumnos",qryIfx.getAlumnosConExtraordinarioAprobado(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            else if (casoRep.equals("selMesCompl"))
                dr.put("canSelEspAlus",tienePrivilegios);
            else
                dr.put("tblAlumnos",qryIfx.getAlumnosParaComplementaria(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnOficCerCompl_Click (String tblPrincipal_cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String idalus, String txtUsuario)
    {
        Map ofsCal;
        //WsfirmaDatosReturn datosReturn;
        boolean hacerCommit=false;
        
        try
        {
            qryIfx.conectarConTransaccion();
            ofsCal = qryIfx.ofYDeofEnCalEvalYGpos(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "CALIFS BIM");
            if (ofsCal.get("todoOf").equals(false))
                throw new SICEEO_Excepcion(0, "SOLO_TODO_OFIC");
            
            if (idalus.equals(""))
                throw new SICEEO_Excepcion(0,"NO_SELEC");
            
            //Llevamos a cabo el firmado, haciendo mediante el webservice  ---- Codigo comentado 27-07-2018 ely
            /*datosReturn = escuelaFirmandoCertCompl(tblPrincipal_cicescini, tblPrincipal_cicescini, tblPrincipal_cveplan, tblPrincipal_idcct, tblPrincipal_grupo, idalus, txtUsuario);
            if (datosReturn==null)
                throw new SICEEO_Excepcion (-1,"NO_PUDO_FIRMAR");
            else if (datosReturn.getReturnCase() == 0)
                throw new SICEEO_Excepcion (datosReturn.getReturnCase(), "MENSAJE_WEBSERVICE", datosReturn.getMensaje());
            else if (datosReturn.getReturnCase() == -1)
                throw new SICEEO_Excepcion (datosReturn.getReturnCase(), "ERROR_WEBSERVICE", datosReturn.getMensaje()); fin*/
            dr.put("noHasAluCompl", !qryIfx.hayAlumnosParaComplementaria(tblPrincipal_cicescini, tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            
            mensaje.Complementaria("ACT_EXITO", "Proceso realizado con exito, información enviada al SIGED.", "", this.dr); // agregado ely
            //dr.put("tblAlumnos",qryIfx.getAlumnosParaComplementaria(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            hacerCommit=true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Complementaria(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    public void getAlumnosSelMesCompl (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo)
    {
        try
        {
            qryIfx.conectar();
            
            if (!tienePrivilegios)
                throw new SICEEO_Excepcion(0, "USUARIO_REESTRINGIDO");
                
            dr.put("tblAlumnos",qryIfx.getAlumnosParaMesCompl(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.Complementaria(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }

    /*private static WsfirmaDatosReturn escuelaFirmandoCertCompl(String cicescini, String cicescinilib, String cveplan, String idcct, String grupo, String idalus, String usuario) {
        webservices.WSGestionFirma_Service service = new webservices.WSGestionFirma_Service();
        webservices.WSGestionFirma port = service.getWSGestionFirmaPort();
        return port.escuelaFirmandoCertCompl(cicescini, cicescinilib, cveplan, idcct, grupo, idalus, usuario);
    }*/


}
