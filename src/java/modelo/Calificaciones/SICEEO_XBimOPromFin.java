package modelo.Calificaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.DAO.SICEEO_QueriesInformix;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;
import modelo.ClasesGlobales.SICEEO_HttpServletRequest;
import modelo.ClasesGlobales.SICEEO_Mensajes;

/**
 *
 * Creado el : 10/03/2015, 09:12:32 AM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

public class SICEEO_XBimOPromFin
{
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final HttpSession sesion;
    private SICEEO_HttpServletRequest r;
    
    public SICEEO_XBimOPromFin (Map datosReturn, HttpServletRequest request)
    {
        this.qryIfx = new SICEEO_QueriesInformix();
        this.mensaje = new SICEEO_Mensajes();
        this.dm = new SICEEO_DataModule ();
        this.dr = datosReturn;
        this.sesion = request.getSession(false);
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
            FormActivate (r.gP("tipo_usuario"), r.gP("txtUsuario").toUpperCase());
        else if (metodo.equals("btXBi"))
            btnXBim_Click (r.gP("rbgXBimOPromOVal_Checked"), r.gP("chkCalProm_isChecked"), r.gP("cbxBim_SelItem"), r.gP("cicescin"), 
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"), r.gP("tblPrincipal_cveplan")
            );
    }
    
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
//--Vista--> private void RB_xBimClick(){}
//--Vista--> private void RB_xPromClick(){}
    
private void btnXBim_Click(String rbgXBimOPromOVal_Checked, String chkCalProm_isChecked, String cbxBim_SelItem, String cicescin, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String tblPrincipal_cveplan)
{
    boolean llegoQMatCalif=false;
    ArrayList<Map> tblAlumCapCalif, QMatCalif, tblMatCalifXBim=null;
    boolean ingles  = false;
    String txtSuperUsuario = ""+sesion.getAttribute("superUsuario");
    
    
    if ( rbgXBimOPromOVal_Checked.equals("rbnValidacion") )                     // CAPTURA de validacion de promedios
    { //----------------------------------------------------------------------------
        this.dr.put("casoRequerido","frmwValidPrim");
    }
    else if ( rbgXBimOPromOVal_Checked.equals("rbnXProm") )                          // CAPTURA POR PROMEDIO FINAL
    { //----------------------------------------------------------------------------
        //++++++++++++++ORIGINAL
        this.dr.put("califCicEscIn",cicescin);                                  // para iniciar con el ciclo actual, despues el susario k se mueva pa donde kiera
        try {
            qryIfx.conectar();
            tblAlumCapCalif = qryIfx.alumCapCalif("I", ""+dr.get("califCicEscIn"), tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de calificaciones
            dr.put("tblAlumCapCalif", tblAlumCapCalif);
            if (tblPrincipal_cveplan.equals("1")) {                        // solo Primaria
                this.dr.put("casoRequerido","frmwCalifPrim");
            } else if (tblPrincipal_cveplan.equals("2")) //or (Dm.Q_Principalcveplan.AsInteger = 1)
            { // solo sECUNDARIA
                llegoQMatCalif = true;
                QMatCalif = qryIfx.matCalif(""+tblAlumCapCalif.get(0).get("idalu"), ""+dr.get("califCicEscIn"));                    // en secundarias hay k abir este qry x materia
                dr.put("QMatCalif", QMatCalif);
                this.dr.put("casoRequerido","frmwCalifSec");
            }
        } catch (SQLException ex){ 
            this.dr.put("returnCase", -1);
            mensaje.General("GENERAL", ex.getMessage(), "", this.dr);
            if (!llegoQMatCalif) { this.dr.put("returnCase", 0); this.dr.put("casoRequerido","frmwRevisaGpo"); } 
        }catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    } //------------------------------------------------------------------------------
    // CAPTURA POR BIMESTRE
    else if ( rbgXBimOPromOVal_Checked.equals("rbnXBim") )
    {//------------------------------------------------------------------------------
        try {
            //*********************************  Validacion para escuelas particulares **********************//
            /*if(txtSuperUsuario.trim().equals("") && (""+sesion.getAttribute("seccion")).equals("PVD")){
                throw new SICEEO_Excepcion (2,"PVD_SIN_PERMISO_CAPTURA");
            }*/
            if ( !tblPrincipal_cveplan.equals("2") && !tblPrincipal_cveplan.equals("1") ) 
            //if ( tblPrincipal_cveplan.equals("3") && !tblPrincipal_grado.equals("3") ) 
                throw new SICEEO_Excepcion (2,""); //x
            // fin de BIMETRES AGREGADO--------------------------------------------------------------------------------------
            if ( tblPrincipal_cveplan.equals("2") || tblPrincipal_cveplan.equals("1") )
            {
                if ( chkCalProm_isChecked.equals("true") )
                    dr.put("calProm","si");
                else
                    dr.put("calProm","no");
                dr.put("califCicEscIn",cicescin);                                   // para iniciar con el ciclo actual, despues el usuario k se mueva pa donde kiera
                
                qryIfx.conectar();                
                tblAlumCapCalif = qryIfx.alumCapCalif("I", ""+dr.get("califCicEscIn"), tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de calificaciones
                Map ofs = qryIfx.oficYDesoficEnCalifEval(""+dr.get("califCicEscIn"), tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, ""+tblAlumCapCalif.get(0).get("idalu"), "CALIFS BIM", ""+dr.get("bim"));
                
                if(cbxBim_SelItem.equals(""))     //agregado para el caso de inactivo los 3 trim
                    cbxBim_SelItem="0";           //agregado para el caso de inactivo los 3 trim
                
                dr.put("bim", cbxBim_SelItem);  
                
                if (tblAlumCapCalif.size()>0 && !cbxBim_SelItem.equals("0") ) // && !cbxBim_SelItem.equals("0") agregado para el caso de inactivo los 3 trim
                    tblMatCalifXBim = qryIfx.matCalifXBim(""+dr.get("bim"), ""+tblAlumCapCalif.get(0).get("idalu"), ""+dr.get("califCicEscIn"),""+tblPrincipal_cveplan);
                if(/*tblMatCalifXBim.size()==0 &&*/ !tblPrincipal_cveplan.equals("2") && (""+dr.get("bim")).equals("1"))
                    ingles = true;
                dr.put("alertaIngles", ingles);                
                dr.put("tblAlumCapCalif",tblAlumCapCalif);
                dr.put("tblMatCalifXBim",tblMatCalifXBim);
                dr.put("tblAlumCapCalif_rowCount",tblAlumCapCalif.size());

                this.dr.put("casoRequerido","frmwCalifSecXBim");
            }//------------------------------------------------------------------------------
        }catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.XBimOPromFin("ALUM_ERROR_HIST", "", "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}

//--Vista--> private void btnAvance_Click (){ }

private void FormActivate (String tipoUsuario, String txtUsuario)
{
    if (!tipoUsuario.equals(" ") ) // cuando es un usuario tipo CCT
        dr.put("btnAvance_Visible",false);
    else
        dr.put("btnAvance_Visible",true);

    if ( txtUsuario.toUpperCase().equals("IVALLE") || txtUsuario.toUpperCase().equals("LPOBLETE") || txtUsuario.toUpperCase().equals("ELYLOPEZ") )
    {
        dr.put("capValid", "experto");
        dr.put("rbnValidacion_Visible",true);
    }else{
        dr.put("capValid", "noexperto");
        dr.put("rbnValidacion_Visible",false);
    }
}

//--Vista--> private void Combo_BimChange () {}

}
