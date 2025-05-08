package modelo.Grupo;

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
 * @author Ing. Maai Nolasco Sánchez
 * Creado el 16-nov-2012, 20:05:34
 *
 */
public class SICEEO_CambioDeGrupo {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_CambioDeGrupo (Map datosReturn, HttpServletRequest request)
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
    public boolean ejecutarPeticion (String metodo)
    {
        String txtUsuario = ""+sesion.getAttribute("userName");
        
        if (metodo.equals("foAc")){                                              // Muestra la tabla de grupos
            FormActivate( true, ""+sesion.getAttribute("seccion"), r.gP("califCicEscIn"), r.gP("cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), ""+sesion.getAttribute("tipo_usuario"), txtUsuario, 
                    ""+sesion.getAttribute("usuarioNo22"), r.gP("xprimeraves") );
        } else if (metodo.equals("btBaDe"))
            btnBajaDefi_Click (dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","estatusgrado","grado","grupo","idcct","cicescini","num_mat","num_eval3"}),
                    r.gP("califCicEscIn"), r.gP("cicescin"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), txtUsuario);
        else if (metodo.equals("btImpConst"))
            btnImpConstancia_Click (dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","estatusgrado","grado","grupo","idcct","cicescini"}),
                    r.gP("califCicEscIn"), r.gP("cicescin"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), txtUsuario);            
        else if (metodo.equals("btIn"))
            btnInscrito_Click (dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","estatusgrado"}), r.gP("califCicEscIn"), 
                    r.gP("cicescin"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), txtUsuario);
        else if (metodo.equals("btRg"))  // cambio para 2023-2024 solo para UDR y DRyCE
            btnRevocacionGdo_Click (dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","estatusgrado","promedio"}), r.gP("califCicEscIn"), 
                    r.gP("cicescin"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), txtUsuario);
        else if (metodo.equals("btCaDGp")){
            btnCamDGpo_Click(dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","grupo","grupo_oldvalue","cvedefsuf","cvelengua","etnia"}), dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvedefsuf_oldValue"), "~", new String[]{"cvedefsuf_oldValue"}), 
                    r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), txtUsuario,
                    dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvelengua_oldValue"), "~", new String[]{"cvelengua_oldValue"}),dm.vstrToArrMap(r.gPV("tblCamDeGpo_etnia_oldValue"), "~", new String[]{"etnia_oldValue"}));
        }else if (metodo.equals("btElAl")){
            btnEliminarAlu_Click (r.gP("idalu"), r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), txtUsuario);
        }/* --------- Este código ya no debe existir, quedó obsoleto ---------
        else if (metodo.equals("btCaToGp")){
            btnCamTotGpo_Click(dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu"}), r.gP("cbxGrupo"), r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), 
                    r.gP("txtUsuario"));
        }*/else if (metodo.equals("btAnGp")){
            ArrayList<Map> tblCamDeGpo=null, tblCamDeGpo_cvedefsuf=null, tblCamDeGpo_cvelengua=null,tblCamDeGpo_etnia=null;
            String tblPrincipal_grado = null, tblPrincipal_grupo=null;
            
            if ( dm.toBool(r.gP("hayCambios")) ){
                tblCamDeGpo = dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","grupo","cvedefsuf","cvelengua","etnia"});
                tblCamDeGpo_cvedefsuf = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvedefsuf_oldValue"), "~", new String[]{"cvedefsuf_oldValue"});
                tblCamDeGpo_cvelengua = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvelengua_oldValue"), "~", new String[]{"cvelengua_oldValue"});
                tblCamDeGpo_etnia = dm.vstrToArrMap(r.gPV("tblCamDeGpo_etnia_oldValue"), "~", new String[]{"etnia_oldValue"});
                tblPrincipal_grado = r.gP("tblPrincipal_grado");
                tblPrincipal_grupo = r.gP("tblPrincipal_grupo");
            }
            
            btnAnteriorGpo_Click (dm.toBool(r.gP("hayCambios")), ""+sesion.getAttribute("seccion"), dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"grado","grupo"}),  
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cct"), r.gP("tblPrincipal_cveplan"), dm.toInt(r.gP("posSelActual")), r.gP("califCicEscIn"),
                    r.gP("cicescini"), ""+sesion.getAttribute("tipo_usuario"), ""+sesion.getAttribute("usuarioNo22"), 
                    r.gP("xprimeraves"), tblCamDeGpo, tblCamDeGpo_cvedefsuf, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario,tblCamDeGpo_cvelengua,tblCamDeGpo_etnia);
            
        }else if (metodo.equals("btSiGp")){
            ArrayList<Map> tblCamDeGpo=null, tblCamDeGpo_cvedefsuf=null, tblCamDeGpo_cvelengua=null,tblCamDeGpo_etnia=null;
            String tblPrincipal_grado = null, tblPrincipal_grupo=null;
            
            if ( dm.toBool(r.gP("hayCambios")) ){
                tblCamDeGpo = dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","grupo","cvedefsuf","cvelengua","etnia"});
                tblCamDeGpo_cvedefsuf = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvedefsuf_oldValue"), "~", new String[]{"cvedefsuf_oldValue"});
                tblCamDeGpo_cvelengua = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvelengua_oldValue"), "~", new String[]{"cvelengua_oldValue"});
                tblCamDeGpo_etnia = dm.vstrToArrMap(r.gPV("tblCamDeGpo_etnia_oldValue"), "~", new String[]{"etnia_oldValue"});
                tblPrincipal_grado = r.gP("tblPrincipal_grado");
                tblPrincipal_grupo = r.gP("tblPrincipal_grupo");
            }
            
            btnSiguienteGpo_Click (dm.toBool(r.gP("hayCambios")), ""+sesion.getAttribute("seccion"), dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"grado","grupo"}),  
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cct"), r.gP("tblPrincipal_cveplan"),  dm.toInt(r.gP("posSelActual")), r.gP("califCicEscIn"), 
                    r.gP("cicescini"), ""+sesion.getAttribute("tipo_usuario"), txtUsuario, ""+sesion.getAttribute("usuarioNo22"), r.gP("xprimeraves"), 
                    tblCamDeGpo, tblCamDeGpo_cvedefsuf, tblPrincipal_grado, tblPrincipal_grupo, tblCamDeGpo_cvelengua,tblCamDeGpo_etnia);
        }else if (metodo.equals("btAnCi")){
            ArrayList<Map> tblCamDeGpo=null, tblCamDeGpo_cvedefsuf=null,tblCamDeGpo_cvelengua=null,tblCamDeGpo_etnia=null;
            
            if ( dm.toBool(r.gP("hayCambios")) ){
                tblCamDeGpo = dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","grupo","cvedefsuf","cvelengua","etnia"});
                tblCamDeGpo_cvedefsuf = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvedefsuf_oldValue"), "~", new String[]{"cvedefsuf_oldValue"});
                tblCamDeGpo_cvelengua = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvelengua_oldValue"), "~", new String[]{"cvelengua_oldValue"});
                tblCamDeGpo_etnia = dm.vstrToArrMap(r.gPV("tblCamDeGpo_etnia_oldValue"), "~", new String[]{"etnia_oldValue"});
            }
            btnAnteriorCiclo_Click (dm.toBool(r.gP("hayCambios")), ""+sesion.getAttribute("seccion"), dm.toInt(r.gP("califCicEscIn")), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), txtUsuario, ""+sesion.getAttribute("usuarioNo22"), 
                    tblCamDeGpo, tblCamDeGpo_cvedefsuf, tblCamDeGpo_cvelengua, tblCamDeGpo_etnia);
        }else if (metodo.equals("btSiCi")){
            ArrayList<Map> tblCamDeGpo=null, tblCamDeGpo_cvedefsuf=null,tblCamDeGpo_cvelengua=null,tblCamDeGpo_etnia=null;
            
            if ( dm.toBool(r.gP("hayCambios")) ){
                tblCamDeGpo = dm.vstrToArrMap(r.gPV("tblCamDeGpo"), "~", new String[]{"idalu","grupo","cvedefsuf","cvelengua","etnia"});
                tblCamDeGpo_cvedefsuf = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvedefsuf_oldValue"), "~", new String[]{"cvedefsuf_oldValue"});
                tblCamDeGpo_cvelengua = dm.vstrToArrMap(r.gPV("tblCamDeGpo_cvelengua_oldValue"), "~", new String[]{"cvelengua_oldValue"});
                tblCamDeGpo_etnia = dm.vstrToArrMap(r.gPV("tblCamDeGpo_etnia_oldValue"), "~", new String[]{"etnia_oldValue"});
            }
            btnSiguienteCiclo_Click (dm.toBool(r.gP("hayCambios")), ""+sesion.getAttribute("seccion"), dm.toInt(r.gP("califCicEscIn")), dm.toInt(r.gP("cicescin")), 
                    r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), txtUsuario, 
                    ""+sesion.getAttribute("usuarioNo22"), tblCamDeGpo, tblCamDeGpo_cvedefsuf, tblCamDeGpo_cvelengua, tblCamDeGpo_etnia);
        }else if (metodo.equals("peVeRe"))
            return permisosVerReportes ();
        
        return false;
    }
/* **************************************************************************** */
/* **************************************************************************** */
/* **************************************************************************** */
    private void FormActivate(boolean llamadaDesdeVista, String seccion, String califCicEscIn, String cicescini, String tblPrincipal_idcct, 
            String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo, String tipo_usuario, String usuario, String usuarioNo22, 
            String xprimeraves)
    {
        boolean filtro;
        
        dr.put("xprimeraves", xprimeraves);
        
        try{
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            this.dr.put("lblCiclo",califCicEscIn+"-"+(Integer.parseInt(califCicEscIn)+1));
        
            if (llamadaDesdeVista){                                                                                                                     //Esta variable solo es usada en SICEEO, no es implementada por Robert
                qryIfx.conectar();
                dr.put("esInscripOfic", (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, "", "", "INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(califCicEscIn)+1), "31/10/"+(dm.toInt(califCicEscIn)+1))));
                //dr.put("isTodoCalOf", qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM"));
                dr.put("idalusDeof", qryIfx.getAlumnosDesoficEnGrupoEnBimeval(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan.equals("3")?"EVALUACION 3":"CALIFS BIM 3"));
                dr.put("ofs", qryIfx.oficYDesoficEnCalifEval(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "", tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM", tblPrincipal_cveplan.equals("3")?"3":"3"));
            }
            
            if ( tblPrincipal_grupo.length() != 1 ) //es 59  y necesitan cambiar a todos los alumnos de grupo
            {                      //se habilita el botón
                dr.put("btnCamTotGpo_Visible", true);
                dr.put("cbxGrupo_Visible", true);
                //--Vista--> cbxGrupo.Items.Clear;
                dr.put("cbxGrupo",qryIfx.getGpos_Val (tblPrincipal_idcct, tblPrincipal_grado, califCicEscIn, false, ""));
            }else{  //entonces es 22
                dr.put("btnCamTotGpo_Visible", false);
                dr.put("cbxGrupo_Visible",false);
                if (!tipo_usuario.equals(" ") && !tipo_usuario.equals("consulta") && !tipo_usuario.equals("captura") && !tipo_usuario.equals("mesa"))
                {
                    /*Map QEscuela = qryIfx.escuela("idcct = "+QUsuario_idcct);

                    if ( !QEscuela.get("cveplan").equals("2") && QEscuela.get("cveunidad").equals("DSRVAL") ){               //ES PREESCOLAR O PRIMARIA Y ES DE VALLES
                       dr.put("btnBajaDefi_Visible", false);
                       dr.put("btnInscrito_Visible", false);
                       dr.put("btnCorrecciones_Visible", false);
                       dr.put("btnCamDGpo_Visible ", false);
                    }else {*/
                       dr.put("btnBajaDefi_Visible", true);
                       dr.put("btnInscrito_Visible", true);
                       dr.put("btnCorrecciones_Visible", true);
                       dr.put("btnCamDGpo_Visible", true);
                    //}
                }
                
                //solicitaron via oral agregar 11dic2012
                /*if (dr.get("xprimeraves").equals("si")){
                    dr.put("catDiscap", qryIfx.getCatDiscap(cicescini, tblPrincipal_cveplan));
                    dr.put("catLenguas", qryIfx.getLenguas());
                }*/
            }
            
            if (llamadaDesdeVista){
                dr.put("tblCamDeGpo",qryIfx.getCamDgpo(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
                if(tblPrincipal_cveplan.equals("3"))
                    dr.put("btnConstancia_Visible", false);
            }
            dr.put("lblTotal", "Total:");
            if (dr.get("xprimeraves").equals("si")){
                //solo mostrar los grupos necesarios
                //si es 22 que no muestre los guion bajo
                if (usuario.equals("IVALLE")  || usuario.equals("ELYLOPEZ")  || usuario.equals("MARIOCIMM") || usuarioNo22.equals("si"))
                    filtro = false; //lo apagamos para mostrar todos
                else
                    filtro = true; //solo mostramos grupos de la 22
                
                dr.put("gposL",qryIfx.getGpos_Val (tblPrincipal_idcct, tblPrincipal_grado, cicescini, filtro, seccion));
                //--Vista--> g_camDgpo.Columns[4].PickList:=gposL;
                dr.put("catDiscap", qryIfx.getCatDiscap(cicescini, tblPrincipal_cveplan));
                dr.put("catLenguas", qryIfx.getLenguas());
            }
            
            if (!tipo_usuario.equals(" ") && !tipo_usuario.equals("consulta") && !tipo_usuario.equals("mesa")){// cuando es un usuario tipo CCT
                dr.put("btnAnteriorCiclo_Enabled", false);
                dr.put("btnSiguienteCiclo_Enabled", false);
                dr.put("btnRevocacionGdo_Visible", false);                
            }else{
                if (tipo_usuario.equals("consulta") || tipo_usuario.equals("mesa")){
                    dr.put("btnBajaDefi_Enabled", false);
                    dr.put("btnInscrito_Enabled", false);
                    dr.put("btnCorrecciones_Enabled", false);
                    dr.put("btnCamDGpo_Enabled ", false);
                }else{
                    dr.put("btnBajaDefi_Enabled", true);
                    dr.put("btnInscrito_Enabled", true);
                    dr.put("btnCorrecciones_Enabled", true);
                    dr.put("btnCamDGpo_Enabled ", true);
                    dr.put("btnAnteriorCiclo_Enabled", true);
                    dr.put("btnSiguienteCiclo_Enabled", true);
                }
            }
            dr.put("xprimeraves","no");                                         //ya no se ejecutan las cosas la segunda ves k sea llamado el evento ACTIVATE
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { if (llamadaDesdeVista) qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnBajaDefi_Click (ArrayList<Map> tblCamDeGpo, String califCicEscIn, String cicescin, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
            String txtUsuario)
    {
        boolean hacerCommit=false;
        int eval3=0, mattot=0;
        int numFilas = tblCamDeGpo.size(); 
        if (numFilas> 0)
        {
            try 
            {
                dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
                qryIfx.conectarConTransaccion();                
                if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, "", "", "INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(califCicEscIn)+1), "31/10/"+(dm.toInt(califCicEscIn)+1)))
                    throw new SICEEO_Excepcion (0,"INSCRIPCION_OFICIALIZADA");                                
                for (int i=0; i<numFilas; i++)
                {
                    eval3 = Integer.parseInt(""+tblCamDeGpo.get(i).get("num_eval3"));
                    mattot = Integer.parseInt(""+tblCamDeGpo.get(i).get("num_mat"));
                    if( mattot != 0 && eval3 == mattot )
                        throw new SICEEO_Excepcion (0,"EVAL3_CAPTURADA",""+tblCamDeGpo.get(i).get("idalu"));
                    if (!tblCamDeGpo.get(i).get("estatusgrado").equals("BD"))
                    {   //preguntar solo para grados terminales
                        if ( tblCamDeGpo.get(i).get("grado").equals("6") || (tblCamDeGpo.get(i).get("grado").equals("3") && !tblPrincipal_cveplan.equals("1")) )
                            if  (qryIfx.suFolio(""+tblCamDeGpo.get(i).get("idalu"),califCicEscIn))
                                throw new SICEEO_Excepcion (0,"ALUMNO_CON_CERTIFICADO",""+tblCamDeGpo.get(i).get("idalu"));
                        qryIfx.setBajaAAlumno (califCicEscIn,cicescin,""+tblCamDeGpo.get(i).get("idalu"), tblPrincipal_cveplan, ""+tblCamDeGpo.get(i).get("idcct"), ""+tblCamDeGpo.get(i).get("grado"), ""+tblCamDeGpo.get(i).get("grupo"), ""+tblCamDeGpo.get(i).get("cicescini"), txtUsuario);
                    }
                }
                hacerCommit = true; //Comentado hoy 14-01-2025
            } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("ACTUALIZACION_INCOMPLETA", "Verifique e intente de nuevo la operación.\n" + ex.getMessage(), "", this.dr);  }
            catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CambioDeGrupo(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
            catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
            finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
        }
    }
    
    private void btnImpConstancia_Click (ArrayList<Map> tblCamDeGpo, String califCicEscIn, String cicescin, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
            String txtUsuario)
    {        
        int numFilas = tblCamDeGpo.size();
        if (numFilas> 0)
        {
            try 
            {
                dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
                qryIfx.conectar();
                
                if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn, "", "", "INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(califCicEscIn)+1), "31/10/"+(dm.toInt(califCicEscIn)+1)))
                    throw new SICEEO_Excepcion (0,"INSCRIPCION_OFICIALIZADA");
                
                for (int i=0; i<numFilas; i++)
                {
                    if (tblCamDeGpo.get(i).get("estatusgrado").equals("BD"))
                    {   //preguntar solo para grados terminales
                        if ( tblCamDeGpo.get(i).get("grado").equals("6") || (tblCamDeGpo.get(i).get("grado").equals("3") && !tblPrincipal_cveplan.equals("1")) )
                            if  (qryIfx.suFolio(""+tblCamDeGpo.get(i).get("idalu"),califCicEscIn))
                                throw new SICEEO_Excepcion (0,"ALUMNO_CON_CERTIFICADO",""+tblCamDeGpo.get(i).get("idalu"));
                        qryIfx.setBajaAAlumno (califCicEscIn,cicescin,""+tblCamDeGpo.get(i).get("idalu"), tblPrincipal_cveplan, ""+tblCamDeGpo.get(i).get("idcct"), ""+tblCamDeGpo.get(i).get("grado"), ""+tblCamDeGpo.get(i).get("grupo"), ""+tblCamDeGpo.get(i).get("cicescini"), txtUsuario);
                    }
                    else
                        throw new SICEEO_Excepcion (0,"ALUMNO_INSCRITO",""+tblCamDeGpo.get(i).get("idalu"));
                }
                
            } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("ACTUALIZACION_INCOMPLETA", "Verifique e intente de nuevo la operación.\n" + ex.getMessage(), "", this.dr);  }
            catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CambioDeGrupo(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
            catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
            finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
        }
    }
    
    private void btnInscrito_Click (ArrayList<Map> tblCamDeGpo, String califCicEscIn, String cicescin, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String txtUsuario)
    {
        boolean hacerCommit=false;
        int numFilas = tblCamDeGpo.size();
        if (numFilas> 0) {
            try {
                dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
                qryIfx.conectarConTransaccion();
                
                if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(califCicEscIn)+1), "31/10/"+(dm.toInt(califCicEscIn)+1)))
                    throw new SICEEO_Excepcion (0,"INSCRIPCION_OFICIALIZADA");
                
                for (int i=0; i<numFilas; i++) {
                    
                    if(!qryIfx.isValidaCapacidadGpo(califCicEscIn, cicescin, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo))     //agregado para validad la capacidad limite del grupo                   
                        throw new SICEEO_Excepcion (0,"CAPGPO_INVALIDA");    //agregado para validad la capacidad limite del grupo
                    //*GotoBookmark(pointer(g_camDgpo.SelectedRows.items[i]));
                    if (!tblCamDeGpo.get(i).get("estatusgrado").equals("C") && !tblCamDeGpo.get(i).get("estatusgrado").equals("I") && !tblCamDeGpo.get(i).get("estatusgrado").equals("P"))
                        qryIfx.setInscritoAAlumno (califCicEscIn, cicescin, ""+tblCamDeGpo.get(i).get("idalu"), txtUsuario, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo); //cambio para validar la capacidad limite del grupo                        
                    else
                         throw new SICEEO_Excepcion (0,"CBIO_INSC_INVALIDO",""+tblCamDeGpo.get(i).get("estatusgrado")); 
                }
                dr.put("tblCamDeGpo",qryIfx.getCamDgpo(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
                hacerCommit = true;
            } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("ACTUALIZACION_INCOMPLETA", "Verifique e intente de nuevo la operación.\n" + ex.getMessage(), "", this.dr);  }
            catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CambioDeGrupo(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
            catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
            finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
        }
    }
    private void btnRevocacionGdo_Click (ArrayList<Map> tblCamDeGpo, String califCicEscIn, String cicescin, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String txtUsuario)
    {
        boolean hacerCommit=false;
        int numFilas = tblCamDeGpo.size();
        if (numFilas> 0) {
            try {
                dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
                qryIfx.conectarConTransaccion();
                
                if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn,tblPrincipal_grado,tblPrincipal_grupo,"CALIFS BIM 1"))
                    throw new SICEEO_Excepcion (0,"EVAL_OFICIALIZADA");
                
                for (int i=0; i<numFilas; i++) {                                        
                    if(Float.parseFloat(""+tblCamDeGpo.get(i).get("promedio")) < 6.0 || Float.parseFloat(""+tblCamDeGpo.get(i).get("promedio")) > 7.0)
                        throw new SICEEO_Excepcion (0,"PROMEDIO_INVALIDO");
                    else if (tblCamDeGpo.get(i).get("estatusgrado").equals("I") || tblCamDeGpo.get(i).get("estatusgrado").equals("P") || tblCamDeGpo.get(i).get("estatusgrado").equals("C") || tblCamDeGpo.get(i).get("estatusgrado").equals("CE"))
                        qryIfx.setRevocacionGdoAAlumno (califCicEscIn, cicescin, ""+tblCamDeGpo.get(i).get("idalu"), txtUsuario, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo); //cambio para validar la capacidad limite del grupo                        
                }
                dr.put("tblCamDeGpo",qryIfx.getCamDgpo(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
                hacerCommit = true; //Descomentado para ciclo 2023-2024 UDR y DRyCE  anterior: 20-07-2023 Se comento hasta que se valide el cambio
            } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("ACTUALIZACION_INCOMPLETA", "Verifique e intente de nuevo la operación.\n" + ex.getMessage(), "", this.dr);  }
            catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CambioDeGrupo(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
            catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
            finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
        }
    }
    
    private void btnCamDGpo_Click (ArrayList<Map> tblCamDeGpo, ArrayList<Map> tblCamDeGpo_cvedefsuf, String califCicEscIn, String tblPrincipal_idcct,
            String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario, ArrayList<Map> tblCamDeGpo_cvelengua, ArrayList<Map> tblCamDeGpo_etnia)
    {
        boolean hacerCommit = false;
        String cvedefsuf,cvelengua,etnia;
        int numFilas = tblCamDeGpo.size();
        
        try{
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectarConTransaccion();

            if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(califCicEscIn)+1), "31/10/"+(dm.toInt(califCicEscIn)+1)))
                throw new SICEEO_Excepcion (0,"INSCRIPCION_OFICIALIZADA");
            
            for (int i=0; i<numFilas; i++){
                cvedefsuf=""+tblCamDeGpo.get(i).get("cvedefsuf");
                cvelengua=""+tblCamDeGpo.get(i).get("cvelengua");
                etnia=""+tblCamDeGpo.get(i).get("etnia");
                if (/*!tblCamDeGpo.get(i).get("grupo").equals(tblPrincipal_grupo) || */
                    !tblCamDeGpo.get(i).get("grupo").equals(tblCamDeGpo.get(i).get("grupo_oldvalue")) || !cvedefsuf.equals(tblCamDeGpo_cvedefsuf.get(i).get("cvedefsuf_oldValue"))
                        || !cvelengua.equals(tblCamDeGpo_cvelengua.get(i).get("cvelengua_oldValue")) || !etnia.equals(tblCamDeGpo_etnia.get(i).get("etnia_oldValue")))
                    qryIfx.setCamDGpo (califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, ""+tblCamDeGpo.get(i).get("grupo"), ""+tblCamDeGpo.get(i).get("idalu"), cvedefsuf, txtUsuario,cvelengua,etnia);
            }
            dr.put("tblCamDeGpo",qryIfx.getCamDgpo(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            
            //dr.put("ofs", qryIfx.oficYDesoficEnCalif(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"),  QAlumCapCalif.isEmpty()?"":""+QAlumCapCalif.get(0).get("idalu"), cbxBim_SelItem));
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("ACTUALIZACION_INCOMPLETA", "Verifique e intente de nuevo la operación.\n" + ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CambioDeGrupo(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnEliminarAlu_Click (String idalu, String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_cveplan, String txtUsuario)
    {
        try{
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            qryIfx.conectar();

            if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(califCicEscIn)+1), "31/10/"+(dm.toInt(califCicEscIn)+1)))
                throw new SICEEO_Excepcion (0,"INSCRIPCION_OFICIALIZADA");
            
            qryIfx.eliminarAlumno (idalu, califCicEscIn, tblPrincipal_cveplan, txtUsuario);
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("ACTUALIZACION_INCOMPLETA", "Verifique e intente de nuevo la operación.\n" + ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CambioDeGrupo(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    }
    
    private void btnCamTotGpo_Click (ArrayList<Map> tblCamDeGpo, String cbxGrupo, String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String txtUsuario, HttpServletRequest request)
    {
        HttpSession sesion = request.getSession(false);
        
        boolean hacerCommit = false;
        int numFilas = tblCamDeGpo.size();
        try {
              dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
              qryIfx.conectarConTransaccion();
              
              if (qryIfx.isOficializado(tblPrincipal_idcct, califCicEscIn,"","","INSCRIPCION") && dm.esFechaIntermedia (dm.getFechaHoy ("dd/MM/yyyy"), "22/08/"+(dm.toInt(califCicEscIn)+1), "31/10/"+(dm.toInt(califCicEscIn)+1)))
                throw new SICEEO_Excepcion (0,"INSCRIPCION_OFICIALIZADA");
              
              for (int i=0; i<numFilas; i++)
                   qryIfx.setCamTotGpo  (""+tblCamDeGpo.get(i).get("idalu"), cbxGrupo, califCicEscIn, txtUsuario);
              dr.put("tblCamDeGpo",qryIfx.getCamDgpo(califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
            hacerCommit = true;
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("ACTUALIZACION_INCOMPLETA", "Verifique e intente de nuevo la operación.\n" + ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CambioDeGrupo(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
    }
    
    private void btnAnteriorGpo_Click (boolean hayCambios, String seccion, ArrayList<Map>tblPrincipal, String tblPrincipal_idcct, String tblPrincipal_cct, 
            String tblPrincipal_cveplan, int posSelActual, String califCicEscIn, String cicescini, String tipo_usuario, String usuarioNo22, 
            String xprimeraves, ArrayList<Map> tblCamDeGpo, ArrayList<Map> tblCamDeGpo_cvedefsuf, String tblPrincipal_grado, String tblPrincipal_grupo, 
            String txtUsuario, ArrayList<Map> tblCamDeGpo_cvelengua,ArrayList<Map> tblCamDeGpo_etnia) 
    { 
        ArrayList<Map> QCamDeGpo=new ArrayList<Map>();
        dr.put("tblPrincipal_selectedRow", posSelActual);
        

        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            
            if (hayCambios)
                btnCamDGpo_Click (tblCamDeGpo, tblCamDeGpo_cvedefsuf, califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario, tblCamDeGpo_cvelengua, tblCamDeGpo_etnia);
            
            if (dr.get("returnCase").equals(1))                                                                                                  //Por si se llamó a guardar cambios y no hubo errores
            {
                qryIfx.conectar();
                this.dr.put("hayCambios", false);
                posSelActual--;
                while ( posSelActual>=0 && QCamDeGpo.isEmpty() )
                {
                    QCamDeGpo = qryIfx.getCamDgpo(califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));
                    if (QCamDeGpo.size()>0)
                        FormActivate( false, seccion, califCicEscIn, cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, ""+tblPrincipal.get(posSelActual).get("grado"), 
                                ""+tblPrincipal.get(posSelActual).get("grupo"), tipo_usuario, txtUsuario, usuarioNo22, xprimeraves);

                    if (!dr.get("returnCase").equals(1))                                                                                              //Si hubo un error en el formActive
                        break;                                                                                                                                   //Nos salimos

                    dr.put("tblCamDeGpo",QCamDeGpo);

                    dr.put("tblPrincipal_selectedRow", posSelActual);
                    dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                    dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
                    //dr.put("isTodoCalOf", qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM"));
                    dr.put("idalusDeof", qryIfx.getAlumnosDesoficEnGrupoEnBimeval(califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tblPrincipal_cveplan.equals("3")?"EVALUACION 3":"CALIFS BIM 5"));
                    dr.put("ofs", qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), "", tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM", tblPrincipal_cveplan.equals("3")?"3":"5"));
                    posSelActual--;
                }
            }
        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnSiguienteGpo_Click (boolean hayCambios, String seccion, ArrayList<Map>tblPrincipal, String tblPrincipal_idcct, String tblPrincipal_cct, 
            String tblPrincipal_cveplan, int posSelActual, String califCicEscIn, String cicescini, String tipo_usuario, String txtUsuario, 
            String usuarioNo22, String xprimeraves, ArrayList<Map> tblCamDeGpo, ArrayList<Map> tblCamDeGpo_cvedefsuf, String tblPrincipal_grado, 
            String tblPrincipal_grupo,ArrayList<Map> tblCamDeGpo_cvelengua, ArrayList<Map> tblCamDeGpo_etnia) 
    { 
        int numFilas;
        ArrayList<Map> QCamDeGpo=new ArrayList<Map>();

        //--Vista--> if (dm.q_MatCalif_xBim.State =dsEdit) or (dm.q_MatCalif_xBim.UpdatesPending) then b_Gda_Calif_Real.Click;
        numFilas = tblPrincipal.size();
        try
        {
            dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
            
            if (hayCambios)
                btnCamDGpo_Click ( tblCamDeGpo, tblCamDeGpo_cvedefsuf, califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario,tblCamDeGpo_cvelengua,tblCamDeGpo_etnia);
            
            if (dr.get("returnCase").equals(1))                                                                                                  //Por si se llamó a guardar cambios y no hubo errores
            {
                qryIfx.conectar();
                this.dr.put("hayCambios", false);
                posSelActual++;
                while ( posSelActual<numFilas && QCamDeGpo.isEmpty()  )
                {
                    QCamDeGpo = qryIfx.getCamDgpo(califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));
                    if (QCamDeGpo.size()>0){
                        FormActivate( false, seccion, califCicEscIn, cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, 
                                ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tipo_usuario, txtUsuario, 
                                usuarioNo22, xprimeraves);
                    }

                    if (!dr.get("returnCase").equals(1))                                                                                              //Si hubo un error en el formActive
                        break;                                                                                                                                   //Nos salimos

                    dr.put("tblCamDeGpo",QCamDeGpo);

                    dr.put("tblPrincipal_selectedRow", posSelActual);
                    dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
                    dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
                    //dr.put("isTodoCalOf", qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, califCicEscIn, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM"));
                    dr.put("idalusDeof", qryIfx.getAlumnosDesoficEnGrupoEnBimeval(califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), tblPrincipal_cveplan.equals("3")?"EVALUACION 3":"CALIFS BIM 5"));
                    dr.put("ofs", qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, tblPrincipal_idcct, ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), "", tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM", tblPrincipal_cveplan.equals("3")?"3":"5"));
                    posSelActual++;
                }

                if ( posSelActual >= numFilas && QCamDeGpo.isEmpty() ) {
                    btnAnteriorGpo_Click(false, seccion, tblPrincipal, tblPrincipal_idcct, tblPrincipal_cct, tblPrincipal_cveplan, posSelActual, califCicEscIn, 
                            cicescini, tipo_usuario, usuarioNo22, xprimeraves, tblCamDeGpo, tblCamDeGpo_cvedefsuf, tblPrincipal_grado, 
                            tblPrincipal_grupo, txtUsuario,tblCamDeGpo_cvelengua,tblCamDeGpo_etnia);
                }
            }

        } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
    
    private void btnAnteriorCiclo_Click (boolean hayCambios, String seccion, int califCicEscIn, String tblPrincipal_cveplan, String tblPrincipal_idcct, 
            String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario, String usuarioNo22, ArrayList<Map> tblCamDeGpo, 
            ArrayList<Map> tblCamDeGpo_cvedefsuf, ArrayList<Map> tblCamDeGpo_cvelengua,ArrayList<Map> tblCamDeGpo_etnia)
    {
        boolean filtro;
        boolean hizoConexion = false;
        
        if ( califCicEscIn >1991 )
        {
            try {
                dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);

                if (hayCambios)
                    btnCamDGpo_Click ( tblCamDeGpo, tblCamDeGpo_cvedefsuf, ""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario, tblCamDeGpo_cvelengua, tblCamDeGpo_etnia);

                if (dr.get("returnCase").equals(1))                                                                                                  //Por si se llamó a guardar cambios y no hubo errores
                {
                    califCicEscIn= califCicEscIn - 1 ;
                    dr.put("califCicEscIn",califCicEscIn);
                    dr.put("lblCiclo",califCicEscIn+"-"+(califCicEscIn+1));

                    if (txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ") || txtUsuario.equals("MARIOCIMM") || usuarioNo22.equals("si"))
                            filtro = false; //lo apagamos para mostrar todos
                        else
                            filtro = true; //solo mostramos grupos de la 22

                        qryIfx.conectar();
                        hizoConexion = true; 
                        
                        this.dr.put("hayCambios", false);
                        dr.put("tblCamDeGpo",qryIfx.getCamDgpo(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
                        dr.put("gposL",qryIfx.getGpos_Val (tblPrincipal_idcct, tblPrincipal_grado, ""+califCicEscIn, filtro, seccion ));
                        dr.put("idalusDeof", qryIfx.getAlumnosDesoficEnGrupoEnBimeval(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM 5"));
                        dr.put("ofs", qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, "", tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM", tblPrincipal_cveplan.equals("3")?"3":"5"));
                        //dr.put("isTodoCalOf", qryIfx.isCalEvalGradoGrupoOficializado(tblPrincipal_idcct, ""+califCicEscIn, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM"));
                        //--Vista--> gposL := TStringList.Create;
                        //--Vista--> while not dm.Q_Gpos_Val.Eof do
                        //--Vista--> begin
                        //--Vista-->   gposL.Add(dm.Q_Gpos_Valgrupo.AsString);
                        //--Vista-->   dm.Q_Gpos_Val.Next;
                        //--Vista--> end;
                        //--Vista--> g_camDgpo.Columns[4].PickList:=gposL;
                        //--Vista--> gposL := nil;
                }
            } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
            catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
            finally { try { if(hizoConexion) qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        }else
            this.dr.put("returnCase",-2);                                                                                                            //Simplemente que ya no haga nada
    }
    
    private void btnSiguienteCiclo_Click (boolean hayCambios, String seccion, int califCicEscIn, int cicescin, String tblPrincipal_cveplan, 
            String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario, String usuarioNo22, ArrayList<Map> tblCamDeGpo, 
            ArrayList<Map> tblCamDeGpo_cvedefsuf, ArrayList<Map> tblCamDeGpo_cvelengua,ArrayList<Map> tblCamDeGpo_etnia )
    {
        boolean filtro;
        boolean hizoConexion = false;
        dr.put("califCicEscIn",califCicEscIn);

        if ( califCicEscIn < cicescin )
        {
            try {
                dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);

                if (hayCambios)
                    btnCamDGpo_Click ( tblCamDeGpo, tblCamDeGpo_cvedefsuf, ""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, txtUsuario, tblCamDeGpo_cvelengua, tblCamDeGpo_etnia);

                if (dr.get("returnCase").equals(1))                                                                                                  //Por si se llamó a guardar cambios y no hubo errores
                {
                    califCicEscIn= califCicEscIn +1 ;
                    dr.put("califCicEscIn",califCicEscIn);
                    dr.put("lblCiclo",califCicEscIn+"-"+(califCicEscIn+1));

                    if (txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ") || txtUsuario.equals("MARIOCIMM") || usuarioNo22.equals("si"))
                            filtro = false; //lo apagamos para mostrar todos
                        else
                            filtro = true; //solo mostramos grupos de la 22

                        qryIfx.conectar();
                        hizoConexion = true;
                        
                        this.dr.put("hayCambios", false);
                        dr.put("tblCamDeGpo",qryIfx.getCamDgpo(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo));
                        dr.put("gposL",qryIfx.getGpos_Val (tblPrincipal_idcct, tblPrincipal_grado, ""+califCicEscIn, filtro, seccion));
                        dr.put("idalusDeof", qryIfx.getAlumnosDesoficEnGrupoEnBimeval(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblPrincipal_cveplan.equals("3")?"EVALUACION":"CALIFS BIM 5"));
                        //--Vista--> gposL := TStringList.Create;
                        //--Vista--> while not dm.Q_Gpos_Val.Eof do
                        //--Vista--> begin
                        //--Vista--> gposL.Add(dm.Q_Gpos_Valgrupo.AsString);
                        //--Vista-->   dm.Q_Gpos_Val.Next;
                        //--Vista--> end;
                        //--Vista--> g_camDgpo.Columns[4].PickList:=gposL;
                        //--Vista--> gposL := nil;
                }
            } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
                catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
                finally { try { if(hizoConexion) qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        }else
            this.dr.put("returnCase",-2);                                                                                                            //Simplemente que ya no haga nada
    }
    
    private boolean permisosVerReportes ()
    {
        String tipo_usuario = ""+sesion.getAttribute("tipo_usuario");
        String usuario =  ""+sesion.getAttribute("userName");

        if (tipo_usuario.equals(" ") || tipo_usuario.equals("consulta") || tipo_usuario.equals("mesa") || tipo_usuario.equals("captura") || usuario.equals("IVALLE") || usuario.equals("LPOBLETE") || usuario.equals("JULIANCRUZ"))
            return true;
        else                                                                                                                                                      // cuando es un usuario tipo CCT
        {
            if (tipo_usuario.equals(this.request.getParameter("idcct")))
                return true;
            else
                return false;
        }
    }
    
    //--Vista--> public void btnTotAlum_Click () { }
}