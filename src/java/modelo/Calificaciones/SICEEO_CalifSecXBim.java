package modelo.Calificaciones;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Creado el : 25/02/2015, 08:46:43 AM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 **/

public class SICEEO_CalifSecXBim {
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final SICEEO_DataModule dm;
    private final Map dr;
    private final SICEEO_HttpServletRequest r;
    private final HttpSession sesion;
    private final HttpServletRequest request;
    
    public SICEEO_CalifSecXBim (Map datosReturn, HttpServletRequest request)
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
        
        if (metodo.equals("foAc")) {
            FormCreate (r.gP("cicescin"), r.gP("tblPrincipal_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"),                     
                    r.gP("bim"), r.gP("tblPrincipal_cveplan"));  //Solo se puso directo bim=1
                    //r.gP("bim"), r.gP("tblPrincipal_cveplan")); //Comentado para ciclo 2023-2024
            formActivate (
                    ""+dr.get("califCicEscIn"), txtUsuario, r.gP("cicescin"), r.gP("calProm"), r.gP("bim"), r.gP("tblPrincipal_grado"), 
                    r.gP("tblPrincipal_cveplan")
            );            
        } else if (metodo.equals("btAnCi"))
            btnAnteriorCiclo_Click (
                    dm.reqToMap(request, new String[]{"tblPrincipal_idcct","tblPrincipal_cveplan","tblPrincipal_grado","tblPrincipal_grupo"},new String[]{"idcct","cveplan","grado","grupo"}), 
                    r.gP("tblAlumCapCalif_grado"), txtUsuario, dm.toInt(r.gP("cicescin")), dm.toInt(r.gP("califCicEscIn")), r.gP("numeval")
            );
        else if (metodo.equals("btSiCi"))
            btnSiguienteCiclo_Click (
                    dm.reqToMap(request, new String[]{"tblPrincipal_idcct","tblPrincipal_grado","tblPrincipal_grupo"},new String[]{"idcct","grado","grupo"}), 
                    r.gP("numeval"), dm.toInt(r.gP("califCicEscIn")), dm.toInt(r.gP("cicescin"))
            );
        else if (metodo.equals("btAnBi"))
            btnAnteriorBimestre_Click (dm.toInt(r.gP("bim")), r.gP("tblAlumCapCalif_idalu"),  r.gP("tblPrincipal_idcct"),  r.gP("tblAlumCapCalif_cicescini"), 
                    dm.toInt(r.gP("califCicEscIn")), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"),r.gP("bimMin"),r.gP("bimMax")
            );
        else if (metodo.equals("btSiBi"))
            btnSiguienteBimestre_Click (dm.toInt(r.gP("bim")), r.gP("tblAlumCapCalif_idalu"), r.gP("tblPrincipal_idcct"),  r.gP("tblAlumCapCalif_cicescini"), 
                    dm.toInt(r.gP("califCicEscIn")), r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"),r.gP("bimMin"),r.gP("bimMax")
            );
        /*else if (metodo.equals("btGuCom"))
            btnGuardaComDocAlum_Click (r.gP("tblAlumCapCalif_idalu"), r.gP("tblAlumCapCalif_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), dm.reqToMap(request, new String[]{"trim1", "trim2", "trim3"},null) 
            );*/ //Boton de comunicacion alumno-docente
        
        else if (metodo.equals("btGuIn"))
            btnGuardaInasistencias_Click (r.gP("tblAlumCapCalif_idalu"), r.gP("tblAlumCapCalif_cicescini"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_cveplan"), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), dm.reqToMap(request, new String[]{"inst1", "inst2", "inst3"},null) 
            ); // Para ciclo 2020-2021 no se aplicará, se reactivo para 2023-2024
        
        else if (metodo.equals("btGeBi"))
            btnGenBim_Click (r.gP("califCicEscIn"), r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("bim"), r.gP("idalu"), 
                    txtUsuario, /*r.gP("bandIngles"),*/r.gP("tblPrincipal_cveplan")
            );        
        else if (metodo.equals("btChMa"))
            btnChekMat_Click (r.gP("tblMatCalifXBim_isEmpty"), r.gP("califCicEscIn"), r.gP("bim"), r.gP("tblPrincipal_idcct"), 
                    r.gP("tblAlumCapCalif_idalu"), r.gP("tblAlumCapCalif_grado"), r.gP("tblAlumCapCalif_grupo"), r.gP("tblAlumCapCalif_cicescini"), 
                    r.gP("tblAlumCapCalif_cveplan"),txtUsuario
            );      
        else if (metodo.equals("btElBiAl"))
            btnElimBimAlum_Click (r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"), r.gP("tblAlumCapCalif_idalu"), r.gP("bim"), 
                    r.gP("califCicEscIn"), r.gP("tblPrincipal_cveplan")
            ); 
        //Comentado el 21-09-2018 por Ely
        else if (metodo.equals("btAnGp"))
            btnAnteriorGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","nombre","cveplan"}),  
                    dm.toInt(r.gP("posSelActual")) ,  r.gP("califCicEscIn") ,  r.gP("cbxBim_SelItem") );
        else if (metodo.equals("btSiGp"))
            btnSiguienteGpo_Click (dm.vstrToArrMap(r.gPV("tblPrincipal"), "~", new String[]{"cct","grado","grupo","idcct","nombre","cveplan"}),  
                    dm.toInt(r.gP("posSelActual")) ,  r.gP("califCicEscIn") ,  r.gP("cbxBim_SelItem") );
        // Comentado el 21-09-2018 por Ely
        /* else if (metodo.equals ("btElBi"))
            btnElimBim_Click (r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo"),
                    r.gP("bim"), r.gP("tblAlumCapCalif_idalu"), r.gP("califCicEscIn"), r.gP("tblPrincipal_cveplan")); */
        else if (metodo.equals("btGeIn")) // Para ciclo 2020-2021 no se aplicará, activado para 2023-2024
            btnGenInasis_Click(dm.vstrToArrMap(r.gPV("tblAlumCapCalif"), "~", new String[]{"idalu"}), r.gP("tblAlumCapCalif_cicescini"), r.gP("tblPrincipal_idcct"), 
                    r.gP("tblPrincipal_cveplan"), r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_grupo") );
        else if (metodo.equals("chToMa"))
            chkTotMat_Click (r.gP("tblAlumCapCalif_idalu"), r.gP("tblAlumCapCalif_cicescini") ) ;
        else if (metodo.equals("btGdCaRe")){
            /*btnGdaCalifReal_Click (r.gP("numLlamada"), dm.toInt(r.gP("tblMatCalifXBim_size")), 
                    dm.vstrToArrMap(r.gPV("tblMatCalifXBim_calif1_OldValue"), "~", new String[]{"OldValue"}), r.gP("existeCalif2"), 
                    dm.vstrToArrMap(r.gPV("tblMatCalifXBim_calif2_OldValue"),  "~", new String[]{"OldValue"}), txtUsuario, r.gP("califCicEscIn"), 
                    r.gP("calProm"), r.gP("cicescin"), dm.vstrToArrMap(r.gPV("tblMatCalifXBim"), "~", new String[]{"calif1","calif2","idalu","cvemat","cvetipmat","numeval","cicescini","grado","cveplan","cvemat","cvetipmat"}), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_cveplan"), dm.strToMap(r.gP("tblAlumCapCalif"), "~", new String[]{"idalu","grado","matrepensecu","c_rep","promd1rop","promd2dop","promd3rop","promd4top","promd5top"}),
                    dm.toInt(r.gP("matRepGdo")),r.gP("sumCalif"),dm.toInt(r.gP("noMat")), r.gP("todasSusMat"), r.gP("esp"),r.gP("mat"), r.gP("prom"), 
                    r.gP("respAlumProCond"), r.gP("respAlumRep"), r.gP("respAcredPorCursarlo"), r.gP("cbxBim_SelItem"), r.gP("tblAlumCapCalif_changeToidalu")
            );*/
        } else if (metodo.equals("tbMaCa_KePr")) {//Comentado para no guardar calificaciones 2018 (OJO) 
            tblMatCalif_KeyPress (r.gP("numLlamada"), dm.toInt(r.gP("tblAlumCapCalif_SelecRow")), dm.toInt(r.gP("tblMatCalifXBim_size")), 
                    dm.vstrToArrMap(r.gPV("tblMatCalifXBim_calif1_OldValue"), "~", new String[]{"OldValue"}), r.gP("existeCalif2"), 
                    dm.vstrToArrMap(r.gPV("tblMatCalifXBim_calif2_OldValue"),  "~", new String[]{"OldValue"}), txtUsuario, r.gP("califCicEscIn"), r.gP("calProm"), 
                    r.gP("cicescin"), dm.vstrToArrMap(r.gPV("tblMatCalifXBim"), "~", new String[]{"calif1","calif2","idalu","cvemat","cvetipmat","numeval","cicescini","grado","cveplan","cvemat","cvetipmat"}), 
                    r.gP("tblPrincipal_grado"), r.gP("tblPrincipal_cveplan"), dm.strToMap(r.gP("tblAlumCapCalif"), "~", new String[]{"matrepact","idalu","grado","matrepensecu","c_rep","cicescini","promdp","promd1rop","promd2dop","promd3rop","promd4top","promd5top","debe1ro","debe2do"}),
                    dm.toInt(r.gP("matRepGdo")),dm.toInt(r.gP("matAprobGdo")),dm.toInt(r.gP("porcAsist")),dm.toInt(r.gP("aluSolicitud")),r.gP("sumCalif"),dm.toInt(r.gP("noMat")), r.gP("todasSusMat"), r.gP("esp"),r.gP("mat"), r.gP("prom"), 
                    r.gP("respAlumProCond"), r.gP("respAlumRep"), r.gP("respAcredPorCursarlo"), r.gP("cbxBim_SelItem"), r.gP("tblAlumCapCalif_changeToidalu"),
                    r.gP("tblPrincipal_idcct"), r.gP("tblPrincipal_grupo"));
        }    
        else if (metodo.equals("tbMaCa_ChSeIt"))
            tblAlumCapCalif_ChangeSelectedItem (r.gP("numeval"),r.gP("tblAlumCapCalif_idalu"),r.gP("califCicEscIn"),r.gP("tblPrincipal_idcct"),
                    r.gP("tblPrincipal_grado"),r.gP("tblPrincipal_grupo"),r.gP("tblPrincipal_cveplan"));
        else if (metodo.equals("peVeRe"))
            return permisosVerReportes ();
        
        return false;
    }
/*******************************************************************************/
/*******************************************************************************/
/*******************************************************************************/
    
    
private void formActivate (String califCicEscIn, String txtUsuario, String cicescin, String calProm, String bim, String tblAlumCapCalif_grado, 
        String tblPrincipal_cveplan )
{
    String superUsuario = ""+sesion.getAttribute("superUsuario");
    String tipo_usuario = ""+sesion.getAttribute("tipo_usuario");
    boolean tipoCambCic1=false, tipoCambCic2=false;
    
    dr.put("lblCiclo",califCicEscIn+'-'+(dm.toInt(califCicEscIn)+1));
    dr.put("tblMatCalifXBim_Visible",false);
    dr.put("tipoCambCic",-1);                                                   //Si tipoCambCic es -1 no hay permisos para desplazarse entre ciclos
          //---------------- Tambien as escuelas de secundaria de 3er grado pueden ver el botón de ciclo anterior
    if ( /*(tipoCambCic1=(tblAlumCapCalif_grado.equals("3") && tblPrincipal_cveplan.equals("2") && dm.isInt(tipo_usuario))) || */
            (tipoCambCic2=(txtUsuario.toUpperCase().equals("IVALLE") || txtUsuario.toUpperCase().equals("ELYLOPEZ") 
            || txtUsuario.toUpperCase().equals("EMMANUEL") || txtUsuario.toUpperCase().equals("EDITHEGON") || superUsuario.equals("si"))) )
    {
        dr.put("btnAnteriorCiclo_Visible",true);
        dr.put("btnSiguienteCiclo_Visible",true);
        if (tipoCambCic2)
            dr.put("tipoCambCic",1);                                            //Si tipoCambCic es 1, es superusuario y tiene todos los accesos
        else if (tipoCambCic1)
            dr.put("tipoCambCic",0);                                            //Si tipoCambCic es 0, es CCT y puede moverse al ciclo anterior con restricciones
    }else{
        dr.put("btnAnteriorCiclo_Visible",false);
        dr.put("btnSiguienteCiclo_Visible",false);
    }

    if ( !cicescin.equals(califCicEscIn) ) { //handa en otro ciclo menos el actual
       dr.put("btnElimBimAlum_Enabled",false);
       dr.put("btnElimBim_Enabled",false);
       dr.put("btnAnteriorGpo_Enabled",false);
       dr.put("btnSiguienteGpo_Enabled",false);
    }else{
       dr.put("btnElimBimAlum_Enabled",true);
       dr.put("btnElimBim_Enabled",true);
       dr.put("btnAnteriorGpo_Enabled",true);
       dr.put("btnSiguienteGpo_Enabled",true);
    }

    if ( calProm.equals("si") ){
        dr.put("pnlConOSinProm_Color","green");
        dr.put("pnlConOSinProm_Text","Calculando Promedios por Materia");
        dr.put("pnlConOSinProm_FontColor","white");
    }else{
        dr.put("pnlConOSinProm_Color","yellow");
        dr.put("pnlConOSinProm_Text","Sin Calcular Promedios por Materia");
        dr.put("pnlConOSinProm_FontColor","black");
    }

    if ( txtUsuario.toUpperCase().equals("IVALLE") || txtUsuario.toUpperCase().equals("ELYLOPEZ"))
       dr.put("btnElimBim_Visible",true);
    else
       dr.put("btnElimBim_Visible",false);

    if ( bim.equals("1") ) dr.put("lblBim","1er PERIODO");
    else if ( bim.equals("2") ) dr.put("lblBim","2do PERIODO");
    else if ( bim.equals("3") ) dr.put("lblBim","3er PERIODO"); //Comentado para ciclo 2023-2024

    //dm.v_Califcicescin := dm.v_cicescin ; //solo ciclo actual
    //--Vista--> tblMatCalif.Fields[1].FocusControl;

    if ( (tblAlumCapCalif_grado.equals("3") && tblPrincipal_cveplan.equals("2")) || (tblAlumCapCalif_grado.equals("6") && tblPrincipal_cveplan.equals("1")) )
    {
        dr.put("tblAlumCapCalif_Columns2_Visible",true);   //promedio gral
        dr.put("tblAlumCapCalif_Columns8_Visible",true);   //folio del certificado
        //--Vista--> dr.put("tblCalif_Columns2_Width:=40;
        //--Vista--> dr.put("tblCalif_Columns8_Width:=75;
        /*if ( tblAlumCapCalif_grado.equals("3") && tblPrincipal_cveplan.equals("2") )//solo secundaria 2012-13
        {   
            //   g_calif.Columns[3].Visible:= True;   //promedio prim
            //   g_calif.Columns[4].Visible:= True;   //promedio edu bas
            //   g_calif.Columns[3].Width:=40;
            //   g_calif.Columns[4].Width:=40;
        }*/
    }

    if ( (!tblAlumCapCalif_grado.equals("3") && tblPrincipal_cveplan.equals("2")) && (!tblAlumCapCalif_grado.equals("6") && tblPrincipal_cveplan.equals("1")) )
    {
        dr.put("tblAlumCapCalif_Columns2_Visible",false);
        dr.put("tblAlumCapCalif_Columns8_Visible",false);
        dr.put("tblAlumCapCalif_Columns3_Visible",false);
        dr.put("tblAlumCapCalif_Columns4_Visible",false);
        // b_PromGral.Visible := False;
    }

    if ( tblPrincipal_cveplan.equals("2") ) {
        if ( dm.toInt(califCicEscIn)>=2013 ) {
            if ( bim.equals("5") )
               dr.put("tblMatCalifXBim_Columns2_Visible", false); 
            else
               dr.put("tblMatCalifXBim_Columns2_Visible",true);
        }
    }else{
       dr.put("tblMatCalifXBim_Columns2_Visible",false);
    }
    //formato
    if ( dm.toInt(califCicEscIn)>=2013 ) 
    {
        dr.put("tblMatCalifXBim_calif1_DisplayFormat","0.0");
        dr.put("tblMatCalifXBim_calif2_DisplayFormat","0.0");
    }else{
        dr.put("tblMatCalifXBim_calif1_DisplayFormat","0");
        dr.put("tblMatCalifXBim_calif2_DisplayFormat","0");
    }

    dr.put("tblMatCalifXBim_Visible",true);
}

private void btnGdaCalifReal_Click (String numLlamada, int tblMatCalifXBim_size, ArrayList<Map> tblMatCalifXBim_calif1_OldValue, String existeCalif2, 
        ArrayList<Map> tblMatCalifXBim_calif2_OldValue, String txtUsuario, String califCicEscIn, String calProm, String cicescin, 
        ArrayList<Map> tblMatCalifXBim, String tblPrincipal_grado, String tblPrincipal_cveplan, Map tblAlumCapCalif, int matRepGdo, int matAprobGdo, int porcAsist, int aluSolicitud, String sumCalif, 
        int noMat, String todasSusMat, String esp, String mat, String prom, String respAlumProCond, String respAlumRep, String respAcredPorCursarlo, 
        String cbxBim_SelItem, String tblAlumCapCalif_changeToidalu, String tblPrincipal_idcct, String tblPrincipal_grupo)
{
    //Esp, Mat      : string;
    Map v=new HashMap();
    ArrayList<Map> QChkCalifMat, QSusMaterias,QMatAlum;
    boolean hacerCommit=false;
    
    int matRepNiv;
    String promovido="",estGdo="";
    int x, numfilas,totalMat;
    float promGral=0;
    float dato1, dato2, dato3, dato4, dato5, dato8;
    String dato6, dato7;
    //DatosTmp : TRXQuery;
    v.put("porcAsist",porcAsist);
    v.put("matRepGdo", matRepGdo); //int                                        Ojo: Debe traer 0 en la primer llamada
    dr.put("matRepNiv", 0);
    v.put("sumCalif",sumCalif); //Float                                         Ojo: Debe traer 0 en la primer llamada
    v.put("noMat",noMat); //Int                                                 Ojo: Debe traer 0 en la primer llamada
    dr.put("prom", prom);                                                       //Ojo: Debe traer 0.0 en la primer llamada
    v.put("matAprobGdo",matAprobGdo); //Int                                                 Ojo: Debe traer 0 en la primer llamada.
    v.put("esp",esp);                                                           //Ojo: Debe traer vacío en la primer llamada
    v.put("mat",mat);                                                           //Ojo: Debe traer vacío en la primer llamada
    v.put("promCero","0");  // para cuando exista promedio de materia = 0  
    dr.put("todasSusMat", todasSusMat);                                         //Ojo: Debe traer vacío en la primer llamada
    v.put("aluSolicitud",aluSolicitud);
    String queryPara_alumGdo="";//,statusIngles = "";                           // ingles se quito para ciclo 2023-2024
    String cvetec1="",cveart1=""; 
    
    
    //+*+ alum_act := dm.Q_Alum_cap_calif.GetBookmark;
    try{
        
        qryIfx.conectarConTransaccion();
        if (numLlamada.equals("0"))
        {
            v.put("esp","A"); // A de aprobada
            v.put("mat","A");
            for (int f=0; f<tblMatCalifXBim_size; f++)
            {
                //actualizamos Evaluaciones------------------------------------------------------------------
                //if (!tblMatCalifXBim.get(f).get("calif1").equals("10.0") && !(""+tblMatCalifXBim.get(f).get("calif1")).matches("[0-9]\\.[0-9]"))
                if ((""+tblMatCalifXBim.get(f).get("calif1")).matches("[0-5]") && tblPrincipal_cveplan.equals("1") && 
                        tblPrincipal_grado.equals("1") )   //Comentando para ciclo 21-22, //se quito 2do de primaria
                    throw new SICEEO_Excepcion (0,"ERROR_FORMATO_CALIF_GDO",""+tblMatCalifXBim.get(f).get("calif1"),"1");
                if ((""+tblMatCalifXBim.get(f).get("calif1")).matches("[0-4]"))                        
                    throw new SICEEO_Excepcion (0,"ERROR_FORMATO_CALIF_APROB",""+tblMatCalifXBim.get(f).get("calif1"),"1");
                if (!tblMatCalifXBim.get(f).get("calif1").equals("10") && !(""+tblMatCalifXBim.get(f).get("calif1")).matches("[0-9]"))
                    throw new SICEEO_Excepcion (0,"ERROR_FORMATO_CALIF",""+tblMatCalifXBim.get(f).get("calif1"),"1");
                //else if (existeCalif2.equals("true") && !tblMatCalifXBim.get(f).get("calif2").equals("10.0") && !(""+tblMatCalifXBim.get(f).get("calif2")).matches("[0-9]\\.[0-9]"))
                else if (existeCalif2.equals("true") && !tblMatCalifXBim.get(f).get("calif2").equals("10") && !(""+tblMatCalifXBim.get(f).get("calif2")).matches("[0-9]"))
                    throw new SICEEO_Excepcion (0,"ERROR_FORMATO_CALIF",""+tblMatCalifXBim.get(f).get("calif2"),"2");
                /*else if (tblMatCalifXBim.get(f).get("cvetipmat").equals("TEC") && tblMatCalifXBim.get(f).get("cvemat").equals("9004"))
                    throw new SICEEO_Excepcion (0,"ERROR_CVE_TEC",""+tblMatCalifXBim.get(f).get("idalu"),"2");*/
                else if (tblMatCalifXBim.get(f).get("cvetipmat").equals("ART") && tblMatCalifXBim.get(f).get("cvemat").equals("555"))
                    throw new SICEEO_Excepcion (0,"ERROR_CVE_ART",""+tblMatCalifXBim.get(f).get("idalu"),"2");
                
                qryIfx.actualizarEvaluaciones ( tblMatCalifXBim.get(f), ""+tblMatCalifXBim_calif1_OldValue.get(f).get("OldValue"), existeCalif2.equals("true"), ""+tblMatCalifXBim_calif2_OldValue.get(f).get("OldValue"), v, txtUsuario, califCicEscIn, calProm, dm);
            }
            
            String moda = qryIfx.getModalidad(tblPrincipal_idcct); 
            QChkCalifMat = qryIfx.chkCalifMat(califCicEscIn, tblPrincipal_grado, tblPrincipal_cveplan, ""+tblAlumCapCalif.get("idalu") );
            QSusMaterias = qryIfx.getPaqueteDeMateriasDeCiclo (califCicEscIn, moda, tblPrincipal_cveplan, tblPrincipal_grado); //materias obligatorias del nivel
            QMatAlum = qryIfx.susMaterias (califCicEscIn, ""+tblAlumCapCalif.get("idalu"));  //Materias del alumno de alumnomaterias
                      
            // Verificamos que su cve de taller y artes sea la misma en evaluaciones y en alumnomaterias    
            if(tblPrincipal_cveplan.equals("2")){
                for(int i=0; i < QMatAlum.size(); i++){
                    if((""+QMatAlum.get(i).get("cvetipmat")).trim().equals("TEC"))
                        cvetec1 = (""+QMatAlum.get(i).get("cvemat")).trim();
                    else if((""+QMatAlum.get(i).get("cvetipmat")).trim().equals("ART"))
                        cveart1 = (""+QMatAlum.get(i).get("cvemat")).trim();
                }
                if (cvetec1.isEmpty())
                    throw new SICEEO_Excepcion (0,"ERROR_MAT_TEC","2");
                else if (cveart1.isEmpty())
                    throw new SICEEO_Excepcion (0,"ERROR_MAT_ART","2");
                
                for(int i=0; i< QChkCalifMat.size(); i++){
                    if((""+QChkCalifMat.get(i).get("cvetipmat")).trim().equals("TEC") && 
                            !(""+QChkCalifMat.get(i).get("cvemat")).trim().equals(cvetec1))
                       throw new SICEEO_Excepcion (0,"ERROR_CVEMAT_DIF","TECNOLOGÍA","2");
                    else if((""+QChkCalifMat.get(i).get("cvetipmat")).trim().equals("ART") && 
                            !(""+QChkCalifMat.get(i).get("cvemat")).trim().equals(cveart1))
                       throw new SICEEO_Excepcion (0,"ERROR_CVEMAT_DIF","ARTES","2");
                }                
            }
            //-chekamos si el alumno ya tiene todas las calif de todos los bimestres de todas sus materias--------------------------
            if ( calProm.equals("si") ) //el usuario selecciono CALCULAR PROMEDIOS
            {
                dr.put("todasSusMat", "SI");
                //statusIngles = qryIfx.getIngles(califCicEscIn, tblPrincipal_grado, tblPrincipal_idcct, tblPrincipal_grupo);
                                
                /*if(tblPrincipal_cveplan.equals("1") && (statusIngles==null || statusIngles.equals("null"))){
                    throw new SICEEO_Excepcion (0,"ERROR_FORMATO_ING","");
                }*/
                
                totalMat = QSusMaterias.size();
                /*if(tblPrincipal_cveplan.equals("1") && statusIngles.equals("f"))
                    totalMat = totalMat-1;                                */
                                
                if(QChkCalifMat.isEmpty() || QChkCalifMat.size() != totalMat) {                   
                    dr.put("todasSusMat","NO");
                    throw new SICEEO_Excepcion (0,"ERROR_MAT_INCOMPL","");
                }
                
                numfilas = QChkCalifMat.size();
                for (int i=0; i<numfilas; i++)
                {
                    if ( !QChkCalifMat.get(i).get("nobim").equals("3") )
                    {
                        dr.put("todasSusMat","NO");
                        break;
                    }
                }
                
            } else
                dr.put("todasSusMat","NO");
            
        
            //-----------------------------
            //PARA primaria
            if ( tblPrincipal_cveplan.equals("1") )
            {
                prom="0.0";
                if ( dr.get("todasSusMat").equals("SI") )
                {
                    if(v.get("promCero").equals("0"))                        
                        prom = dm.dividirFraccion (v.get("sumCalif"), v.get("noMat")); //dm.toBigDecimal(v.get("sumCalif")).divide(dm.toBigDecimal(v.get("noMat")), 4, RoundingMode.HALF_DOWN).toPlainString();
                    
                    /*if (tblAlumCapCalif.get("grado").equals("3") || tblAlumCapCalif.get("grado").equals("4") || tblAlumCapCalif.get("grado").equals("5") )  //solo 3ro, 4to o 5to promedio minimo de 6 y maximo 2 materias reprobadas
                    {
                        //if ((Integer)v.get("matRepGdo")<=2 && dm.toFloat(prom)>=6.0 ){  // si reprueba max 2 materias y alcanza un prom de 6
                        if ((Integer)v.get("matAprobGdo")>=6 && dm.toFloat(prom)>=6.0 ){  // si reprueba max 2 materias y alcanza un prom de 6
                            if ( dm.toFloat(prom)<10 )
                                prom=prom.substring(0,3);
                        } else
                            prom="0.0";
                    }*/  //Secomento para ciclo 2023-2024 ya no es necesario

                    //if ( tblAlumCapCalif.get("grado").equals("6") )     //para 6to debe pasar todas las materias, ciclo anterior al 2023
                    if ( !tblAlumCapCalif.get("grado").equals("1") )      // De 2 a 6to debe de tener promedio de materia aprobatoria para calcularle su promedio
                    {
                        if ( v.get("matRepGdo").equals(0) && dm.toFloat(prom)>=6.0 ) { // si reprueba max 2 materias y alcanza un prom de 6
                            if ( dm.toFloat(prom)<10 )
                                prom=prom.substring(0,3);
                        } else
                            prom="0.0";
                    }

                    if ( dm.toFloat(prom)>=6.0 ) {
                        if ( Float.parseFloat(prom)<10 )
                            prom=prom.substring(0,3);
                    } else
                        prom="0.0";
               }
            }

            //PARA SECUNDARIA
            if ( tblPrincipal_cveplan.equals("2") )
            {
                prom="0.0";
                if (dr.get("todasSusMat").equals("SI") )
                    if(v.get("promCero").equals("0") && (Integer)v.get("matRepGdo")==0) {
                        //prom = dm.extractFloat ( ""+dm.divideFloat(v.get("sumCalif"), v.get("noMat")),2,2 );
                        //prom = dm.toBigDecimal(v.get("sumCalif")).divide(dm.toBigDecimal(v.get("noMat")), 4, RoundingMode.HALF_DOWN).toPlainString();
                        prom = dm.dividirFraccion (v.get("sumCalif"), v.get("noMat"));
                    }
                  if ( dm.toFloat(prom)<10 ) prom=prom.substring(0,3);
            }
            
            // Obtener el porcentaje de asistencia del alumno 
            v.put("porcAsist",qryIfx.getPorcAsistencia(califCicEscIn, tblPrincipal_grado, tblPrincipal_cveplan, ""+tblAlumCapCalif.get("idalu") ));
            v.put("aluSolicitud",qryIfx.alumnoSolicitudCerti(califCicEscIn, ""+tblAlumCapCalif.get("idalu")));   //PAra saber si se encuentra en la tabla de solicitudes.            
            dr.put("tblAlumCapCalif_c_rep", tblAlumCapCalif.get("c_rep"));
            throw new SICEEO_Excepcion (1,"PASAR_A_NUMLLAMADA=1","");
        }
        
         //actualizamos AlumnoGRADO------------------------------------------------------------------
        matRepNiv = dm.toInt(v.get("matRepGdo")) + (tblAlumCapCalif.get("matrepensecu").equals("null")?0:dm.toInt(tblAlumCapCalif.get("matrepensecu")));
        dr.put("matRepNiv", matRepNiv);

          //en 3ro de secundaria pasa si reprueba 4 o menos PASA
        if ( tblPrincipal_cveplan.equals("2") &&  matRepNiv<=4 && califCicEscIn.equals(cicescin) && dr.get("todasSusMat").equals("SI") 
                && tblAlumCapCalif.get("grado").equals("3") )
        {
            //qryIfx.alumGdo (prom,"C","P");
            promovido="P";
            estGdo="C";
        }
            //para 2do o 1ro con 3 reprobadas PASA
            //Sofi: para 2do o 1ro con 4 reprobadas pasas
        else if ( tblPrincipal_cveplan.equals("2") && matRepNiv<=4 && califCicEscIn.equals(cicescin) && dr.get("todasSusMat").equals("SI") && !tblAlumCapCalif.get("grado").equals("3") )
        {
            //qryIfx.alumGdo (prom,"P","P");
            promovido="P";
            estGdo="P";
        }else if ( tblPrincipal_cveplan.equals("2") && ( ( matRepNiv>=5 && tblAlumCapCalif.get("grado").equals("3") ) || ( matRepNiv>=5 && !tblAlumCapCalif.get("grado").equals("3") ) ) && califCicEscIn.equals(cicescin) && dr.get("todasSusMat").equals("SI") )
        {
            //qryIfx.alumGdo ("0","NP","NP");
            promovido="NP";
            prom="0.0";
            estGdo="NP";
            // MessageDlg('Criterio de no Promoción:'+#13+#10+''+#13+#10+'Al concluir el ciclo escolar, presente cinco o más asignaturas '+#13+#10+'no acreditadas de las establecidas en el plan de estudios de '+#13+#10+'primero, segundo y/o tercero grado.', mtInformation, [mbOK], 0);
        } 
//FIN SECUNDARIA----------------
//para primaria
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && dm.toFloat(prom)>=6.0 && (Integer)v.get("matRepGdo")>0 && (Integer)v.get("matRepGdo")<=2 && ( tblAlumCapCalif.get("grado").equals("4") || tblAlumCapCalif.get("grado").equals("5") ) && dr.get("todasSusMat").equals("SI") ) //para 4to y 5to puede pasar  con condicion si su promed es minimo de 6 y solo reprueba 2 materias
        else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && dm.toFloat(prom)>=6.0 && (Integer)v.get("matAprobGdo")>=4 && ( tblAlumCapCalif.get("grado").equals("2") || tblAlumCapCalif.get("grado").equals("3") || tblAlumCapCalif.get("grado").equals("4") || tblAlumCapCalif.get("grado").equals("5") ) && dr.get("todasSusMat").equals("SI") ) //para 3ro, 4to y 5to puede pasar  con condicion si su promed es minimo de 6 y solo reprueba 2 materias
        {
            if ( respAlumProCond.equals("YES") )
            {
                //qryIfx.alumGdo (prom,"P","PC");
                promovido="P";
                estGdo="P";
            } else {
                //qryIfx.alumGdo ("0","NP","NP");
                promovido="NP";
                prom="0.0";
                estGdo="NP";
            }

        //}else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && (dm.toFloat(prom)<6.0 || (Integer)v.get("matRepGdo")>2) && ( tblAlumCapCalif.get("grado").equals("3") || tblAlumCapCalif.get("grado").equals("4") || tblAlumCapCalif.get("grado").equals("5") ) && dr.get("todasSusMat").equals("SI") ) //para 4to y 5to no pasa si su prom es 5 o  reprueba +2 materias
        //falta agregar sus inasistenacias    
        }else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && ((dm.toFloat(prom)<6.0 && dm.toFloat(prom)>=5.0) || ((Integer)v.get("matRepGdo")>0 && (Integer)v.get("matAprobGdo")<4) ) && ( !tblAlumCapCalif.get("grado").equals("1") ) && dr.get("todasSusMat").equals("SI") ) //para 3ro, 4to y 5to no pasa si su prom es 5 o  reprueba +2 materias    
        {
            //qryIfx.alumGdo ("0","NP","NP");
            promovido="NP";
            prom="0.0";
            estGdo="NP";
        }else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && (dm.toFloat(prom)>=6.0 ) && dr.get("todasSusMat").equals("SI") && v.get("matRepGdo").equals(0) && tblAlumCapCalif.get("grado").equals("6") )   //si es sexto grado  y aprueba Y PASA TODAS SUS MATERIAS        
        {
            //qryIfx.alumGdo (prom,"C","P");
            promovido="P";
            estGdo="C";
        }else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && dm.toFloat(prom)>=6.0 && dr.get("todasSusMat").equals("SI") && v.get("matRepGdo").equals(0) && dm.toInt(tblAlumCapCalif.get("grado")) <= 5 )   //si pasa de 1 a 5 normal, y sin reprobar nunguna materia
        {
            //qryIfx.alumGdo (prom,"P","P");
            promovido="P";
            estGdo="P";
        }/*else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && dm.toFloat(prom)==0.0 && dr.get("todasSusMat").equals("SI") && v.get("matRepGdo").equals(0) && (Integer)v.get("porcAsist")>=80 && dm.toInt(tblAlumCapCalif.get("grado")) <= 5 )   //si pasa de 1 a 5 normal, y sin reprobar nunguna materia
        {
            //qryIfx.alumGdo (prom,"P","P");
            promovido="P";
            estGdo="P";
        } //comentado por mi 28-06-2023 */  
        else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && (v.get("esp").equals("A") && v.get("mat").equals("A")) && (Integer)v.get("matRepGdo")>0 && dm.toFloat(prom)>=6.0 && ( tblAlumCapCalif.get("grado").equals("1") || tblAlumCapCalif.get("grado").equals("2")) && dr.get("todasSusMat").equals("SI") ) //logra su promedio pero reprueba una materia diferente de español o matematicas
        {
            //qryIfx.alumGdo (prom,"P","P");                                    //todos pasa de 1ro a 2do aun con 5
            promovido="P";
            estGdo="P";
        } /*else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && ( v.get("esp").equals("R") || v.get("mat").equals("R")) && ( tblAlumCapCalif.get("grado").equals("1") || tblAlumCapCalif.get("grado").equals("2") ) && dr.get("todasSusMat").equals("SI") ) // la norma dice k 1ro, 2do y 3ro pasa xk pasa Sofi dice k hay k dejar k el profesor decida si pasa o no pasa
        {
            if ( tblAlumCapCalif.get("c_rep").equals("R") )  //si se encuentra repitiendo el grado, pasa x k pasa
            {
                if ( respAlumRep.equals("YES"))
                {
                    //qryIfx.alumGdo (prom,"P","P");                            //todos pasa de 1ro a 2do aun con 5
                    promovido="P";
                    estGdo="P";
                }else{
                    //qryIfx.alumGdo ("0","NP","NP");
                    promovido="NP";
                    prom="0.0";
                    estGdo="NP";
                }
            }else {
                if ( respAcredPorCursarlo.equals("YES") )
                {
                    //qryIfx.alumGdo (prom,"P","P");                            //todos pasa de 1ro a 2do aun con 5
                    promovido="P";
                    estGdo="P";
                }else{
                    //qryIfx.alumGdo ("0","NP","NP");
                    promovido="NP";
                    prom="0.0";
                    estGdo="NP";
                }
            }
        }// Ya no se aplica para este ciclo*/ 
        else if ( tblPrincipal_cveplan.equals("1") && califCicEscIn.equals(cicescin) && (dm.toFloat(prom)<6.0 || (Integer)v.get("matRepGdo")>0 ) && dr.get("todasSusMat").equals("SI") && !tblAlumCapCalif.get("grado").equals("1") )   //si es sexto grado  y reprueba
        {
            //qryIfx.alumGdo ("0","NP","NP");
            promovido="NP";
            prom="0.0";
            estGdo="NP";
        }//hasta aki todos los grados con calif aprobatoria
        
//FIN PRIMARIA--------------
        else if ( dr.get("todasSusMat").equals("NO"))
        {
            //qryIfx.alumGdo ("0, promedioGral = 0, promedioEb=0,","I","P");    //"I, P" VALOR X DEFAUL
            promovido="P";
            prom="0.0";
            promGral=(float) 0.0;
            estGdo="I";
            queryPara_alumGdo += " promedioGral = 0, promedioEb=0, ";
        }
//TERMINA IF ANIDADO

// para 6To grado---------------------------
        if ( tblPrincipal_grado.equals("6") && tblPrincipal_cveplan.equals("1") )
        {
            if ( dr.get("todasSusMat").equals("SI") && ( (
                      ( dm.toFloat(tblAlumCapCalif.get("promd1rop")) != -11 ) &&
                      ( dm.toFloat(tblAlumCapCalif.get("promd2dop")) != -11 ) &&
                      ( dm.toFloat(tblAlumCapCalif.get("promd3rop")) != -11 ) &&
                      ( dm.toFloat(tblAlumCapCalif.get("promd4top")) != -11 ) &&
                      ( dm.toFloat(tblAlumCapCalif.get("promd5top")) != -11 ) &&
                      ( dm.toFloat(prom) != -11 )) || ( dm.toInt(v.get("aluSolicitud"))>0 && (dm.toFloat(prom) != -11 )) ) )
            {
                //    Dm.Q_AlumGdo.SQL.Add(' PromedioGral = '+Vprom+' , ');

                x=0;
                //para el promedio de primaria
                if (dm.toFloat(tblAlumCapCalif.get("promd1rop")) >= 6.0) x=x+1;
                if (dm.toFloat(tblAlumCapCalif.get("promd2dop")) >= 6.0) x=x+1;
                if (dm.toFloat(tblAlumCapCalif.get("promd3rop")) >= 6.0) x=x+1;
                if (dm.toFloat(tblAlumCapCalif.get("promd4top")) >= 6.0) x=x+1;
                if (dm.toFloat(tblAlumCapCalif.get("promd5top")) >= 6.0) x=x+1;
                if (dm.toFloat(prom)>=6.0) x=x+1;
                  //  dm.Q_Alum_cap_califentreNoMat.Value := x ;
                if ( (dm.toFloat(prom)>=6.0 && x==6) || ( dm.toFloat(prom)>=6.0 && x<6 && dm.toInt(v.get("aluSolicitud"))>0) )  //tiene historial y este 6to lo aprobo || esta registrado en solicitudes de certi y este grado lo aprobo
                {
                    dato1 = ( (dm.toFloat(tblAlumCapCalif.get("promd1rop"))>=6 ? dm.toFloat(tblAlumCapCalif.get("promd1rop"))*10 : 0) +
                            (dm.toFloat(tblAlumCapCalif.get("promd2dop"))>=6 ? dm.toFloat(tblAlumCapCalif.get("promd2dop"))*10 : 0) +
                            (dm.toFloat(tblAlumCapCalif.get("promd3rop"))>=6 ? dm.toFloat(tblAlumCapCalif.get("promd3rop"))*10 : 0) +
                            (dm.toFloat(tblAlumCapCalif.get("promd4top"))>=6 ? dm.toFloat(tblAlumCapCalif.get("promd4top"))*10 : 0) +
                            (dm.toFloat(tblAlumCapCalif.get("promd5top"))>=6 ? dm.toFloat(tblAlumCapCalif.get("promd5top"))*10 : 0) +
                            dm.toFloat(prom)*10 );

                    dato2=dato1 ;
                    dato3=dato2/x;
                    dato4=dato3;
                    dato5=dato4/10;
                    dato6=""+dato5;
                    dato7=dato6.substring(0,3);
                    promGral=dm.toFloat(dato7);
                }
                else {
                    promGral=(float)0.0;
                    
                }
                //+++++++++++++++++++++++
                queryPara_alumGdo+=" PromedioGral = "+promGral+ ", ";
            }             
            else {                                                            //no tiene todas sus materias
                queryPara_alumGdo+=" PromedioGral = 0, ";
                promGral=(float)0.0;
            }
        }
// para 3ro de secundaria
        if ( tblAlumCapCalif.get("grado").equals("3") && matRepNiv > 0 && dr.get("todasSusMat").equals("SI") && tblPrincipal_cveplan.equals("2") )
        {
            queryPara_alumGdo+=" PromedioGral = 0, PromedioEb = 0, ";
            promGral=(float)0.0;
        }
//-----------------------------------------------------
        if ( dr.get("todasSusMat").equals("SI") ) //SI YA tiene todas sus materias ya sabemos cuantas repetira
            queryPara_alumGdo+=" MatRepAct="+v.get("matRepGdo")+", ";
        else
            queryPara_alumGdo+=" MatRepAct=0, "; //mientras no sea el ultimo bimestre ponle 0

        // Dm.Q_AlumGdo.SQL.Add('                       Calif_real   = "S"  ');
        //  MessageDlg('qry:'+#13+#10+''+#13+#10+Dm.Q_AlumGdo.SQL.Text, mtWarning, [mbOK], 0);

        if ( calProm.equals("si") ) //el usuario selecciono CALCULAR PROMEDIOS
            if ( califCicEscIn.equals(cicescin) ){
                if (!prom.equals("10.0") && !prom.matches("[0-9]\\.[0-9]")){
                    if (prom.equals("10.0000"))
                        prom="10.0";
                    else
                        throw new SICEEO_Excepcion (0,"ERROR_FORMATO_PROM",prom);
                }
                qryIfx.alumGdo (prom,estGdo,promovido,queryPara_alumGdo,califCicEscIn,""+tblAlumCapCalif.get("idalu"), txtUsuario);
            }else{
                this.dr.put("returnCase",3);
                mensaje.CalifSecxBim("SICANT_NOCALC_PROM", "", "", this.dr);
            }

        hacerCommit=true;
    
        //--Vista--> g_Matcalif.EnableScroll;
        if ( califCicEscIn.equals(cicescin) ) //solo para ciclo actual
        {
            if ( dr.get("todasSusMat").equals("SI") || calProm.equals("si") )
            {
                tblAlumCapCalif.put("promedio", ""+prom);
                tblAlumCapCalif.put("promediogral", ""+promGral);
                tblAlumCapCalif.put("matrepact", ""+v.get("matRepGdo"));
                tblAlumCapCalif.put("promovido", ""+promovido);
                tblAlumCapCalif.put("estatusgrado", ""+estGdo);
                dr.put("tblAlumCapCalif", tblAlumCapCalif);
            }
        }
        
        if (!tblAlumCapCalif_changeToidalu.equals("-1"))
        {            
            dr.put("tblMatCalifXBim",qryIfx.matCalifXBim(cbxBim_SelItem, tblAlumCapCalif_changeToidalu, califCicEscIn,tblPrincipal_cveplan));
            dr.put("tblInasistencias", qryIfx.inasistencias (califCicEscIn, tblPrincipal_grado, tblAlumCapCalif_changeToidalu));
            dr.put("tblComDocAlum", qryIfx.comunicacionAlumDoc(califCicEscIn, tblAlumCapCalif_changeToidalu));
        }
    
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (SICEEO_Excepcion ex){
        this.dr.put("returnCase",ex.getNumError()); 
        if (ex.getMensaje().contains("PASAR_A_NUMLLAMADA")){
            dr.put("prom", prom);
            dr.put("matRepGdo", v.get("matRepGdo"));
            dr.put("porcAsist", v.get("porcAsist"));
            dr.put("matAprobGdo", v.get("matAprobGdo"));
            dr.put("tblAlumCapCalif_grado", tblAlumCapCalif.get("grado"));
            dr.put("esp", v.get("esp"));
            dr.put("mat", v.get("mat"));
            dr.put("tblAlumCapCalif_c_rep", tblAlumCapCalif.get("c_rep"));
            dr.put("todasSusMat", dr.get("todasSusMat"));
            dr.put("aluSolicitud",v.get("aluSolicitud"));
            hacerCommit=true;
            mensaje.General(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);
        }else{ 
            hacerCommit=true;
            mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);
        }
    } catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { } }
}

private boolean tblMatCalif_KeyPress (String numLlamada, int tblAlumCapCalif_SelecRow, int tblMatCalifXBim_size, ArrayList<Map> tblMatCalifXBim_calif1_OldValue, 
        String existeCalif2, ArrayList<Map> tblMatCalifXBim_calif2_OldValue, String txtUsuario, String califCicEscIn, String calProm, String cicescin, 
        ArrayList<Map> tblMatCalifXBim, String tblPrincipal_grado, String tblPrincipal_cveplan, Map tblAlumCapCalif, int matRepGdo, int matAprobGdo, int porcAsist, int aluSolicitu, String sumCalif, int noMat, 
        String todasSusMat, String esp, String mat, String prom, String respAlumProCond, String respAlumRep, String respAcredPorCursarlo, String cbxBim_SelItem,
        String tblAlumCapCalif_changeToidalu,String tblPrincipal_idcct, String tblPrincipal_grupo)
{
    //int noMat=0;                                                              //Ojo, noMat=0 en primera llamada
    int alum_act;
    float sumProm=0, promSec, PromPrim, promEB;
    float dato1, dato2, dato3, dato4, dato5, dato8;
    String dato6, dato7;
    
    Map QPromGral, QPromGral2012;
    
    dr.put("tblAlumCapCalif", tblAlumCapCalif);
    
    //--Vista--> if ( keyPress.equals("enter") && (tblMatCalifXBim_SelCol.equals("2") || tblMatCalifXBim_SelCol.equals("3")) ) 
    //--Vista--> {
        //--Vista--> g_Matcalif.DataSource.DataSet.Next;                                      
        //DM.q_MatCalif_xBim.Next ;
        //--Vista--> if DM.q_MatCalif_xBim.EOF then
        //--Vista--> {
        
        btnGdaCalifReal_Click(numLlamada, tblMatCalifXBim_size, tblMatCalifXBim_calif1_OldValue, existeCalif2, tblMatCalifXBim_calif2_OldValue, txtUsuario, 
                califCicEscIn, calProm, cicescin, tblMatCalifXBim, tblPrincipal_grado, tblPrincipal_cveplan, tblAlumCapCalif, matRepGdo, matAprobGdo, porcAsist, aluSolicitu, sumCalif, noMat, 
                todasSusMat, esp, mat, prom, respAlumProCond, respAlumRep, respAcredPorCursarlo, cbxBim_SelItem, tblAlumCapCalif_changeToidalu, tblPrincipal_idcct, tblPrincipal_grupo);
        if (numLlamada.equals("0"))
            return true;
        if (!dr.get("returnCase").equals(1) && !dr.get("returnCase").equals(3))
            return false;
        try 
        {
            //para secundaria------------------------------------------------------
            if ( tblPrincipal_grado.equals("3") && tblPrincipal_cveplan.equals("2") ) 
            {
                if ( (""+tblAlumCapCalif.get("matrepact")).trim().equals("0") )               //y no debe ninguna materia
                {
                    alum_act = tblAlumCapCalif_SelecRow;
                    dr.put("todasSusMat", todasSusMat);
                    if ( todasSusMat.equals("SI") )
                    {
                        //**********************************************************
                        dr.put("todasSusMat", "NO");
                        //**********************************************************
                        qryIfx.conectar();
                        if ( (dm.toInt(califCicEscIn) < 2012) )
                        {
                            QPromGral=qryIfx.promGral (""+tblAlumCapCalif.get("idalu"));
                            this.dr.put("casoRequerido", "winPromGsec");
                            if ( QPromGral.get("grados").equals("3") )
                                tblAlumCapCalif.put("promediogral",""+QPromGral.get("promgral"));
                            else
                                throw new SICEEO_Excepcion (2,"CHECAR_HISTORIAL");
                        }
                        //**********************************************************
                        else if (dm.toInt(califCicEscIn) >= 2012 )
                        {
                            QPromGral2012=qryIfx.promGral2012 (""+tblAlumCapCalif.get("idalu"),""+tblAlumCapCalif.get("cicescini"));
                            if  ( dm.toFloat(QPromGral2012.get("promd1ros"))>=6.0 ) { noMat=1;  sumProm=dm.toFloat(QPromGral2012.get("promd1ros"))*10; }
                            if  ( dm.toFloat(QPromGral2012.get("promd2dos"))>=6.0 ) { noMat=noMat+1; sumProm=sumProm+dm.toFloat(QPromGral2012.get("promd2dos"))*10; }
                            if  ( dm.toFloat(QPromGral2012.get("promd3ros"))>=6.0 ) { noMat=noMat+1; sumProm=sumProm+dm.toFloat(QPromGral2012.get("promd3ros"))*10; }
                            
                            if(noMat==3){ // Inicia IF-ELSE - Para los que no tienen promedio de un grado no se le calcula el promedio general
                                dato2=sumProm ;
                                dato3=dato2/noMat;
                                dato4=dato3;
                                dato5=dato4/10;
                                dato6=""+dato5;
                                dato7=dato6.substring(0,3);
                                promSec=dm.toFloat(dato7);
                                                        
                                PromPrim = dm.toFloat(tblAlumCapCalif.get("promdp"));
                                if ( PromPrim > 0 )
                                {
                                    dato2=(promSec*10+PromPrim*10);
                                    dato3=dato2/2 ;
                                    dato4=dato3;
                                    dato5=dato4/10;
                                    dato6=""+dato5;
                                    dato7=dato6.substring(0,3);
                                    promEB=dm.toFloat(dato7);
                                }else{
                                    promEB= promSec;
                                }
                            } else {promSec = dm.toFloat("0.0"); promEB = dm.toFloat("0.0");} // Se agrego el if-else Para los que no tienen promedio en algun grado fech: 30-06-2020
                            /*if (!(""+promSec).equals("10.0") && !(""+promSec).matches("[0-9]\\.[0-9]")){
                                if (promSec.equals("10.0000"))
                                    promSec="10.0";
                                else
                                    throw new SICEEO_Excepcion (0,"ERROR_FORMATO_PROM",prom);
                            }*/

                            if ( (dm.toInt(tblAlumCapCalif.get("debe1ro")) + dm.toInt(tblAlumCapCalif.get("debe2do")) + dm.toInt(tblAlumCapCalif.get("matrepact")) ) >0 )
                            {
                                tblAlumCapCalif.put("promediogral",0) ;
                                tblAlumCapCalif.put("promedioeb",0);
                            }else{
                                tblAlumCapCalif.put("promediogral",""+promSec);
                                tblAlumCapCalif.put("promedioeb",""+promEB);
                            }
                            dr.put("QPromGral2012", QPromGral2012);
                            dr.put("tblAlumCapCalif", tblAlumCapCalif);
                            dr.put("returnCase", 3);
                            this.dr.put("casoRequerido", "winPromGsec2012");
                        }

                        //----------------------------------------------------------
                    }

                    //     dm.Q_Alum_cap_calif.Close;
                    //     dm.Q_Alum_cap_calif.Open;
                    //     dm.Q_Alum_cap_calif.GotoBookmark(Alum_Act);
                    //     dm.Q_Alum_cap_calif.FreeBookmark(Alum_Act);
                }
            }
            //para primaria------------------------------------------------------
            if ( tblPrincipal_grado.equals("6") && tblPrincipal_cveplan.equals("1") )
            {
                //ya lo agregamos en el momento de guardar en alumnoGRado
            }

            //MessageDlg('antes', mtWarning, [mbOK], 0);
            //--Vista--> g_calif.DataSource.DataSet.Next;
            //--Vista--> Bar_guarda.Position := 0;
            // dm.Q_Alum_cap_calif.Next;
            //--Vista--> }
        //--Vista--> }
        } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    return true;
}

private void btnChekMat_Click (String tblMatCalifXBim_isEmpty, String califCicEscIn, String bim, String tblPrincipal_idcct, String tblAlumCapCalif_idalu, 
        String tblAlumCapCalif_grado, String tblAlumCapCalif_grupo, String tblAlumCapCalif_cicescini, String tblAlumCapCalif_cveplan, String txtUsuario)
{
    //boolean hacerCommit=false;
    int numFilas;
    ArrayList<Map> QSusMaterias, QSusEval, tblMatCalifXBim, QMatRepetida, QMatRepet2, QMatCalif, QMatKSobran;
            
//*************************************************************************************************************************
    try 
    {
        dm.isIdcctAutorizada(sesion, tblPrincipal_idcct);
        
        //qryIfx.conectarConTransaccion();
        qryIfx.conectar();
        
        Map oficYDesofic = qryIfx.oficYDesoficEnCalifEval (califCicEscIn, tblPrincipal_idcct, tblAlumCapCalif_grado, tblAlumCapCalif_grupo, tblAlumCapCalif_idalu, "CALIFS BIM", bim);
        //if (oficYDesofic.get("algunOf").equals(true) && oficYDesofic.get("todoDeof").equals(false))  // COmentado el 09-04-2024
        if (oficYDesofic.get("bimOf").equals(true) && oficYDesofic.get("alDeof").equals(false)) 
            throw new SICEEO_Excepcion (0,"BLOQUEADO_POR_OFICIALIZACION");
        
        if ( tblMatCalifXBim_isEmpty.equals("true") )
        {
            //hay alumnos que se kedan con materias de un plan diferente al actual
            //es x eso k disparamos el de elimina bimestre, con ese limpiamos
            //y luego dispararemos el de inserta materias
            //** b_ElimBimAlum.Click;
            //mejor solo eliminamos materias del bimestre en cuestion
            qryIfx.eliminaBimestre( califCicEscIn, tblAlumCapCalif_idalu, bim);
            //---------------------------------------------------
            qryIfx.insertaEvaluaciones (bim, txtUsuario, tblAlumCapCalif_idalu , califCicEscIn, "", "");
            //eso es todo, terminamos y regresamos
        } else {
            //empatamos materias papa e hijo ----------------------------------------------------------------------------------------------------------------------------------------------
            //MATERIAS papa
            QSusMaterias = qryIfx.susMaterias (tblAlumCapCalif_cicescini, tblAlumCapCalif_idalu); //8
            //buscamos los registros que NO coincicen con el papa alumnomaterias
            QSusEval = qryIfx.susEval (tblAlumCapCalif_cicescini,tblAlumCapCalif_idalu,bim);
            
            qryIfx.actualizaSusEvaluaciones(QSusMaterias, QSusEval, this.dm, bim, txtUsuario);
            
            //------chekamos materia repetida k está más de una vez
            QMatRepetida = qryIfx.matRepetida(tblAlumCapCalif_idalu, califCicEscIn, bim);
            if ((numFilas=QMatRepetida.size())>0 )
                for(int i=0; i<numFilas; i++)
                    qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+QMatRepetida.get(i).get("cvetipmat"), ""+QMatRepetida.get(i).get("cvemat"), "", "", ""+QMatRepetida.get(i).get("numeval"));
            //--MATERIAS REPETIDAS Y QUE NO SON CBA verficar para secundarias ----------------------------
            QMatRepet2=qryIfx.matRepet2(tblAlumCapCalif_idalu,califCicEscIn,bim);
            if ( (numFilas=QMatRepet2.size())>0 )
                for(int i=0; i<numFilas; i++)                    
                    qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+QMatRepet2.get(i).get("cvetipmat"), "", "", "", ""+QMatRepet2.get(i).get("numeval"));
        //------------------------------
        //------------------------------
            QMatCalif=qryIfx.matCalif (tblAlumCapCalif_idalu, califCicEscIn);
            tblMatCalifXBim=qryIfx.matCalifXBim (bim, tblAlumCapCalif_idalu, califCicEscIn, tblAlumCapCalif_cveplan); //sin tomar en cuenta si lleva o no ingles
            numFilas = QMatCalif.size();
            for (int i=0; i<numFilas; i++)
            {
                if ( -1 == dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cveprograma","cvetipmat","cvemat"}, new Object[]{QMatCalif.get(i).get("cveprograma"),QMatCalif.get(i).get("cvetipmat"),QMatCalif.get(i).get("cvemat")}))
                {
                    if ( (QMatCalif.get(i).get("cvetipmat").equals("EDT") || QMatCalif.get(i).get("cvetipmat").equals("TEC")) && -1!=dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cvetipmat"}, new Object[]{QMatCalif.get(i).get("cvetipmat")}) )  //LO K ESTOY BUSCANDO ES EL TALLER PERO EXISTE CON OTRO TALLER
                        qryIfx.actualizaEvaluaciones (""+QMatCalif.get(i).get("cvemat"), ""+QMatCalif.get(i).get("cveprograma"), tblAlumCapCalif_idalu, califCicEscIn,""+QMatCalif.get(i).get("cvetipmat"), txtUsuario);
                    else { 
                        if (QMatCalif.get(i).get("cvetipmat").equals("ART") && -1!=dm.indexOfArrMap(tblMatCalifXBim, new String[]{"cvetipmat"}, new Object[]{QMatCalif.get(i).get("cvetipmat")}))  //LO K ESTOY BUSCANDO ES ARTE //PERO EXISTE CON OTRO ARTE
                            qryIfx.actualizaEvaluaciones (""+QMatCalif.get(i).get("cvemat"), ""+QMatCalif.get(i).get("cveprograma"), tblAlumCapCalif_idalu, califCicEscIn,"ART",txtUsuario);
                        else
                            qryIfx.insertaEvaluaciones (bim, txtUsuario, tblAlumCapCalif_idalu, califCicEscIn, ""+QMatCalif.get(i).get("cvetipmat"), ""+QMatCalif.get(i).get("cvemat"));
                    }
                   // MessageDlg('Materia Agregada!', mtInformation, [mbOK], 0);
                }
            }
        //*************************************************************************************************************************
        //Materias que sobran
            QMatKSobran=qryIfx.matksobran (tblAlumCapCalif_idalu, califCicEscIn, bim);
            numFilas = QMatKSobran.size();
            for (int i=0; i<numFilas; i++)
                if ( -1 == dm.indexOfArrMap(QMatCalif, new String[]{"cveprograma","cvetipmat","cvemat"}, new Object[]{QMatKSobran.get(i).get("cveprograma"),QMatKSobran.get(i).get("cvetipmat"),QMatKSobran.get(i).get("cvemat")}))
                    qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+QMatKSobran.get(i).get("cvetipmat"), ""+QMatKSobran.get(i).get("cvemat"), ""+QMatKSobran.get(i).get("cveprograma"), "", "");
                    //    MessageDlg('Materia Eliminada!', mtInformation, [mbOK], 0);
        //*************************************************************************************************************************
            tblMatCalifXBim=qryIfx.matCalifXBim (bim, tblAlumCapCalif_idalu, califCicEscIn, tblAlumCapCalif_cveplan);  //false sin tomar en cuenta si lleva o no ingles
            numFilas = tblMatCalifXBim.size();
            for (int i=0; i<numFilas; i++)
                if ( !tblMatCalifXBim.get(i).get("grado").equals(tblAlumCapCalif_grado) )
                    qryIfx.eliminarEvaluaciones (tblAlumCapCalif_idalu, califCicEscIn, ""+tblMatCalifXBim.get(i).get("cvetipmat"), ""+tblMatCalifXBim.get(i).get("cvemat"), "", ""+tblMatCalifXBim.get(i).get("grado"), "");
                    //  MessageDlg('Materia Eliminada!', mtInformation, [mbOK], 0);
        }
//***************************************************************************************************************************
        tblMatCalifXBim=qryIfx.matCalifXBim (bim, tblAlumCapCalif_idalu, califCicEscIn, tblAlumCapCalif_cveplan);  //true para tomar en cuenta si lleva o no ingles
        dr.put("tblMatCalifXBim", tblMatCalifXBim);        
        //hacerCommit = true;
    } catch (SQLException ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { /*qryIfx.cerrarConexionConTransaccion(hacerCommit);*/ qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

//--vista --> private void FormClose () {} // 1/2día

private void btnGenInasis_Click(ArrayList<Map> tblAlumCapCalif, String tblAlumCapCalif_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
        String tblPrincipal_grado, String tblPrincipal_grupo)
{
    int numFilas;
    boolean hacerCommit = false;
    ArrayList<Map> tblInasistencias=null;
    
//CHEAMOS A ALUMNO X ALUMNO SI YA TIENE SUS MATERIAS DEL BIMESTRE
//SINO SE LAS AGREGAMOS
    //*--> dm.Q_Alum_cap_calif.First;
    //     dm.q_ChekBim.Close;
    //     dm.q_ChekBim.ParamByName('v_numeval').AsInteger := dm.v_bim;
    //     dm.q_ChekBim.Open;

    numFilas = tblAlumCapCalif.size();
    try{
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        
        qryIfx.conectarConTransaccion();
        
        Map oficYdesofic = qryIfx.oficYDesoficEnCalifEval(tblAlumCapCalif_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, ""+tblAlumCapCalif.get(0).get("idalu"), "CALIFS BIM", "1"); // parametro de idalu llevaba ""
        /*if (oficYdesofic.get("alDeof").equals(false))  //tenia todoDeof  //Se quito, comentado con Roberto caso de Emmanuel alumno de traslado.
            throw new SICEEO_Excepcion (0,"BLOQUEADO_POR_OFICIALIZACION");*/
        
        for (int i=0; i<numFilas; i++)
        {
            tblInasistencias = qryIfx.inasistencias (tblAlumCapCalif_cicescini, tblPrincipal_grado, ""+tblAlumCapCalif.get(i).get("idalu"));
            if ( tblInasistencias.isEmpty() )                                         //osea k aun no tiene registros en la tabla inasistencias
                qryIfx.insertarInasistencias (""+tblAlumCapCalif.get(i).get("idalu"), tblAlumCapCalif_cicescini, tblPrincipal_cveplan, tblPrincipal_grado);
        }
        tblInasistencias = qryIfx.inasistencias (tblAlumCapCalif_cicescini, tblPrincipal_grado, ""+tblAlumCapCalif.get(0).get("idalu"));
        dr.put("tblInasistencias", tblInasistencias);
        hacerCommit = true; 
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexionConTransaccion(hacerCommit); } catch (SQLException ex) { } }
    //--Vista--> dm.Q_Alum_cap_calif.First;
    //       dm.q_ChekBim.close;
}

private void btnSiguienteBimestre_Click (int bim, String idalu, String tblPrincipal_idcct, String cicescini, int califCicEscIn, String tblPrincipal_cveplan,
    String tblPrincipal_grado, String tblPrincipal_grupo, String bimMin, String bimMax) // 1/8 día
{
    ArrayList<Map> tblMatCalifXBim=null;
    //boolean ingles = false;
    
    //dm.v_bim :=strToint(combo_Bim.Items[combo_Bim.ItemIndex]);
    if ( bim>0 && bim < Integer.parseInt(bimMax) )
       bim=bim+1; //Comentado para ciclo 2023-2024
    dr.put("bim", bim);
    
    if ( bim == 1 ) dr.put("lblBim", "1er PERIODO");
    else if ( bim == 2 ) dr.put("lblBim", "2do PERIODO");
    else if ( bim == 3 ) dr.put("lblBim", "3er PERIODO");  // Comentado para ciclo 2023-2024

    try {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        qryIfx.conectar();
        Map oficYdesofic = qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idalu, "CALIFS BIM", ""+bim);
        dr.put("ofs", oficYdesofic);
        if(bim > 0)
            tblMatCalifXBim = qryIfx.matCalifXBim (""+bim, idalu, cicescini, tblPrincipal_cveplan);// en secundarias hay k abir este qry x materia x bimestre
        
        dr.put("tblMatCalifXBim", tblMatCalifXBim);
        //tblComDocAlumn = qryIfx.comunicacionAlumDoc(""+califCicEscIn,idalu);  //qry de captura de calificaciones
        //dr.put("tblComDocAlum",tblComDocAlumn);        
        /*if ((""+bim).equals("1") && tblPrincipal_cveplan.equals("1"))
            ingles = true;*/
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }

    if ( tblPrincipal_cveplan.equals("2") ){
        if ( califCicEscIn < 2013 ) //y es ciclo actual
            dr.put("tblMatCalifXBim_Columns2_Visible",false);
        else if ( bim == 3 ){ // Antes 5
            dr.put("tblMatCalifXBim_Columns2_Visible",false);
            this.dr.put("returnCase",2); 
            mensaje.CalifSecxBim("ACTIVAR_CALPROM", "", "", this.dr);
            dr.put("calProm", "si");
        }else
            dr.put("tblMatCalifXBim_Columns2_Visible",true);                
    } else
        dr.put("tblMatCalifXBim_Columns2_Visible",false);
    //dr.put("alertaIngles", ingles);
}

private void btnAnteriorBimestre_Click (int bim, String tblAlumCapCalif_idalu, String tblPrincipal_idcct, String tblAlumCapCalif_cicescini, int califCicEscIn, 
        String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo, String bimMin, String bimMax) // 1/8 día
{
    ArrayList<Map> tblMatCalifXBim=null;//,tblComDocAlumn;
   // boolean ingles = false;
    
    if ( bim!=0 && bim > Integer.parseInt(bimMin) ) //Valor original bim > 1
        bim=bim-1;
    dr.put("bim", bim);
    
    if ( bim == 1 ) dr.put("lblBim", "1er PERIODO");
    else if ( bim == 2 ) dr.put("lblBim", "2do PERIODO");
    else if ( bim == 3 ) dr.put("lblBim", "3er PERIODO");  // Comentado para el periodo 2023-2024

    
    try {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        qryIfx.conectar();
        Map oficYdesofic = qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblAlumCapCalif_idalu, "CALIFS BIM", ""+bim);
        dr.put("ofs", oficYdesofic);
        if(bim>0)
            tblMatCalifXBim = qryIfx.matCalifXBim (""+bim, tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini, tblPrincipal_cveplan);// en secundarias hay k abir este qry x materia x bimestre
        /*if ((""+bim).equals("1") && tblPrincipal_cveplan.equals("1"))
            ingles = true;*/
        dr.put("tblMatCalifXBim", tblMatCalifXBim);
        //btnSigAntBimestre(oficYdesofic, bim, "Anterior"); // Agregado para ciclo 2023-2024
        //tblComDocAlumn = qryIfx.comunicacionAlumDoc(""+califCicEscIn,tblAlumCapCalif_idalu);  //qry de captura de calificaciones
        //dr.put("tblComDocAlum",tblComDocAlumn);      
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    
    if ( tblPrincipal_cveplan.equals("2") ){
       if ( califCicEscIn < 2013 ) //y es ciclo actual
           dr.put("tblMatCalifXBim_Columns2_Visible",false);
       else if ( bim == 3 )
           dr.put("tblMatCalifXBim_Columns2_Visible",false);
       else
           dr.put("tblMatCalifXBim_Columns2_Visible",true);
    }else
        dr.put("tblMatCalifXBim_Columns2_Visible",false);
    //dr.put("alertaIngles",ingles);         
}

private void btnSigAntBimestre(Map ofs, int bim, String caso) {
    if(caso.equals("Siguiente")){
        if( bim==1 && ofs.get("btnTrim2").equals(true) || 
            bim==2 && ofs.get("btnTrim3").equals(true) )
            dr.put("btnSiguienteBimestre_Enable",true);
        else
            dr.put("btnSiguienteBimestre_Enable",false);
    }
    else {
        if( bim==2 && ofs.get("btnTrim1").equals(true) || 
            bim==3 && ofs.get("btnTrim2").equals(true) )
            dr.put("btnAnteriorBimestre_Enable",true);
        else
            dr.put("btnAnteriorBimestre_Enable",false);
    }
}
private void btnElimBim_Click (String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String bim, 
        String tblAlumCapCalif_idalu, String califCicEscIn, String tblPrincipal_cveplan)
{
    ArrayList<Map> tblMatCalifXBim;
    //boolean ingles = false;
            
    try {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        qryIfx.conectar();
        
        Map oficYDesofic = qryIfx.oficYDesoficEnCalifEval (califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblAlumCapCalif_idalu, "CALIFS BIM", bim);
        if (oficYDesofic.get("algunOf").equals(true))                           //porque afecta a todos los alumnos
            throw new SICEEO_Excepcion (0,"BLOQUEADO_POR_OFICIALIZACION");
        
        qryIfx.eliminaBimestre ("", califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
        tblMatCalifXBim=qryIfx.matCalifXBim (bim, tblAlumCapCalif_idalu, califCicEscIn, tblPrincipal_cveplan);
        /*if (tblMatCalifXBim.size() == 0 && !tblPrincipal_cveplan.equals("2"))
            ingles = true;*/
        dr.put("tblMatCalifXBim", tblMatCalifXBim);
        //dr.put("alertaIngles",ingles);
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

private void btnElimBimAlum_Click (String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String tblAlumCapCalif_idalu, 
        String bim, String califCicEscIn, String tblPrincipal_cveplan)
{
    ArrayList<Map> tblMatCalifXBim;
    boolean ingles = false;
    
    try {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        qryIfx.conectar();
        
        Map oficYDesofic = qryIfx.oficYDesoficEnCalifEval (califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblAlumCapCalif_idalu, "CALIFS BIM", bim);
        if (oficYDesofic.get("algunOf").equals(true) && oficYDesofic.get("todoDeof").equals(false))
            throw new SICEEO_Excepcion (0,"BLOQUEADO_POR_OFICIALIZACION");
        
        qryIfx.eliminaBimestre (tblAlumCapCalif_idalu, califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
        tblMatCalifXBim=qryIfx.matCalifXBim (bim, tblAlumCapCalif_idalu, califCicEscIn, tblPrincipal_cveplan);
        /*if (tblMatCalifXBim.size() == 0 && !tblPrincipal_cveplan.equals("2"))
            ingles = true;*/
        
        dr.put("tblMatCalifXBim", tblMatCalifXBim);
        //dr.put("alertaIngles",ingles);
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

private void btnAnteriorGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn, String cbxBim_SelItem) 
{ 
    ArrayList<Map> QAlumCapCalif=new ArrayList<Map>(), tblMatCalifXBim=null;
    //boolean ingles = false;
    dr.put("tblPrincipal_selectedRow", posSelActual);
    
    //--Vista--> if (dm.q_MatCalif_xBim.State =dsEdit) or (dm.q_MatCalif_xBim.UpdatesPending) then b_Gda_Calif_Real.Click;
    try
    {
        dm.isIdcctAutorizada (sesion, ""+tblPrincipal.get(posSelActual).get("idcct"));
        qryIfx.conectar();
        posSelActual--;
        while ( posSelActual>=0 && QAlumCapCalif.isEmpty() )
        {
            Map ofs = qryIfx.oficYDesoficEnCalifEval(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), "", "CALIFS BIM", cbxBim_SelItem);
            QAlumCapCalif = qryIfx.alumCapCalif("I", califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de calificaciones
            
            if (QAlumCapCalif.size()>0)
                tblMatCalifXBim = qryIfx.matCalifXBim(cbxBim_SelItem, ""+QAlumCapCalif.get(0).get("idalu"),califCicEscIn, ""+tblPrincipal.get(posSelActual).get("cveplan"));
            /*if (tblMatCalifXBim.size()==0 && !(""+tblPrincipal.get(posSelActual).get("cveplan")).equals("2"))
                ingles = true;*/
            
            dr.put("tblAlumCapCalif",QAlumCapCalif);
            dr.put("tblMatCalifXBim",tblMatCalifXBim);
            
            dr.put("tblPrincipal_selectedRow", posSelActual);
            dr.put("tblPrincipal_idcct", tblPrincipal.get(posSelActual).get("idcct"));
            dr.put("tblPrincipal_cct", tblPrincipal.get(posSelActual).get("cct"));
            dr.put("tblPrincipal_nombre", tblPrincipal.get(posSelActual).get("nombre"));
            dr.put("tblPrincipal_cveplan", tblPrincipal.get(posSelActual).get("cveplan"));
            dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
            dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
            //dr.put("alertaIngles",ingles);
            posSelActual--;
        }
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

private void btnSiguienteGpo_Click(ArrayList<Map>tblPrincipal, int posSelActual, String califCicEscIn, String cbxBim_SelItem) 
{ 
    int numFilas;
    ArrayList<Map> QAlumCapCalif=new ArrayList<Map>(), tblMatCalifXBim = null;
   // boolean ingles = false;
    
    //--Vista--> if (dm.q_MatCalif_xBim.State =dsEdit) or (dm.q_MatCalif_xBim.UpdatesPending) then b_Gda_Calif_Real.Click;
    numFilas = tblPrincipal.size();
    try
    {
        dm.isIdcctAutorizada (sesion, ""+tblPrincipal.get(posSelActual).get("idcct"));
        qryIfx.conectar();
        posSelActual++;
        while ( posSelActual<numFilas && QAlumCapCalif.isEmpty()  )
        {
            Map ofs = qryIfx.oficYDesoficEnCalifEval(califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"), "", "CALIFS BIM", cbxBim_SelItem);
            QAlumCapCalif = qryIfx.alumCapCalif("I", califCicEscIn, ""+tblPrincipal.get(posSelActual).get("idcct"), ""+tblPrincipal.get(posSelActual).get("grado"), ""+tblPrincipal.get(posSelActual).get("grupo"));  //qry de captura de calificaciones
            if (QAlumCapCalif.size()>0)
                tblMatCalifXBim = qryIfx.matCalifXBim(cbxBim_SelItem, ""+QAlumCapCalif.get(0).get("idalu"), califCicEscIn, ""+tblPrincipal.get(posSelActual).get("cveplan"));
            /*if (tblMatCalifXBim.size() == 0 && !(""+tblPrincipal.get(posSelActual).get("cveplan")).equals("2"))
            ingles = true;*/
            
            dr.put("tblAlumCapCalif",QAlumCapCalif);
            dr.put("tblMatCalifXBim",tblMatCalifXBim);
            
            dr.put("tblPrincipal_selectedRow", posSelActual);
            dr.put("tblPrincipal_idcct", tblPrincipal.get(posSelActual).get("idcct"));
            dr.put("tblPrincipal_cct", tblPrincipal.get(posSelActual).get("cct"));
            dr.put("tblPrincipal_nombre", tblPrincipal.get(posSelActual).get("nombre"));
            dr.put("tblPrincipal_cveplan", tblPrincipal.get(posSelActual).get("cveplan"));
            dr.put("tblPrincipal_grado", tblPrincipal.get(posSelActual).get("grado"));
            dr.put("tblPrincipal_grupo", tblPrincipal.get(posSelActual).get("grupo"));
            //dr.put("alertaIngles",ingles);
            posSelActual++;
        }
        
        if ( posSelActual >= numFilas && QAlumCapCalif.isEmpty() ) {
            qryIfx.cerrarConexion();
           btnAnteriorGpo_Click(tblPrincipal, posSelActual, califCicEscIn, cbxBim_SelItem);
        }
        
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}
//--vista --> private void b_AnalisMatClick () {} 

private void chkTotMat_Click (String tblAlumCapCalif_idalu, String tblAlumCapCalif_cicescini)
{
    dr.put("datosReturn", 1);
    ArrayList<Map> tblTotMat;
    try
    {
        qryIfx.conectar();
        tblTotMat = qryIfx.totMat (tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini);
        dr.put("tblTotMat", tblTotMat);
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
} 

private void btnGuardaInasistencias_Click (String tblAlumCapCalif_idalu, String tblAlumCapCalif_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
        String tblPrincipal_grado, String tblPrincipal_grupo, Map tblInasistencias)
{
    Map oficYdesofic;
    
    dr.put("returnCase", 1);
    try {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        qryIfx.conectar();

        oficYdesofic = qryIfx.oficYDesoficEnCalifEval(tblAlumCapCalif_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblAlumCapCalif_idalu, "CALIFS BIM", "");
        qryIfx.guardaInasistencias (tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_grado, ""+tblInasistencias.get("inst1"), ""+tblInasistencias.get("inst2"), ""+tblInasistencias.get("inst3"), oficYdesofic);
    } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
}

private void btnGuardaComDocAlum_Click (String tblAlumCapCalif_idalu, String tblAlumCapCalif_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
        String tblPrincipal_grado, String tblPrincipal_grupo, Map tblInasistencias)
{
    Map oficYdesofic;
    
    dr.put("returnCase", 1);
    try {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        qryIfx.conectar();

        oficYdesofic = qryIfx.oficYDesoficEnCalifEval(tblAlumCapCalif_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblAlumCapCalif_idalu, "CALIFS BIM", "");

        qryIfx.guardaComDocAlum (tblAlumCapCalif_idalu, tblAlumCapCalif_cicescini, ""+tblInasistencias.get("trim1"), ""+tblInasistencias.get("trim2"), ""+tblInasistencias.get("trim3"), oficYdesofic);
    } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
}

private void FormCreate (String cicescin, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, 
        String bim, String tblPrincipal_cveplan)
{
    String idalu = "0";
    ArrayList<Map> tblAlumCapCalif, tblMatCalifXBim = new ArrayList<Map>(), tblInasistencias = new ArrayList<Map>(), tblComDocAlumn = new ArrayList<Map>();
    dr.put("returnCase", 1);
    
    
    try
    {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        dr.put("califCicEscIn", cicescin);                                          // para iniciar con el ciclo actual, despues el usuario k se mueva pa donde kiera
        qryIfx.conectar();
        Map ofs = qryIfx.oficYDesoficEnCalifEval(""+dr.get("califCicEscIn"), tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idalu, "CALIFS BIM", bim);
        tblAlumCapCalif = qryIfx.alumCapCalif("I", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);  //qry de captura de calificaciones
        if (tblAlumCapCalif.size()>0  && !bim.equals("0")) // && && !bim.equals("0") agregado para el caso de inactivo los 3 trim
            tblMatCalifXBim = qryIfx.matCalifXBim(bim, ""+tblAlumCapCalif.get(0).get("idalu"), tblPrincipal_cicescini, tblPrincipal_cveplan);
        //if (tblMatCalifXBim.size() == 0 && !tblPrincipal_cveplan.equals("2"))
        /*if (!tblPrincipal_cveplan.equals("2") && bim.equals("1"))
            ingles = true;*/
        
        //dr.put("alertaIngles",  ingles);   
        dr.put("tblAlumCapCalif",tblAlumCapCalif);
        dr.put("tblMatCalifXBim",tblMatCalifXBim);
        //--------------
        dr.put("returnCase", 1);
        dr.put("paTras", "no");
    
        if (tblAlumCapCalif.size()>0  && !bim.equals("0")) {  // && !bim.equals("0") agregado para el caso de inactivo los 3 trim
            tblInasistencias = qryIfx.inasistencias (""+dr.get("califCicEscIn"), ""+tblAlumCapCalif.get(0).get("grado"), ""+tblAlumCapCalif.get(0).get("idalu"));  //qry de captura de calificaciones
            //tblComDocAlumn = qryIfx.comunicacionAlumDoc(""+dr.get("califCicEscIn"), ""+tblAlumCapCalif.get(0).get("idalu"));  //qry de captura de calificaciones
            idalu = "" + tblAlumCapCalif.get(0).get("idalu");
        }
        dr.put("tblInasistencias",tblInasistencias);
        //dr.put("tblComDocAlum",tblComDocAlumn);
        dr.put("ofs", ofs);
        dr.put("bim",bim);
        
    } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
    catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
}

private boolean btnAnteriorCiclo_Click (Map tblPrincipal, String tblAlumCapCalif_grado, String txtUsuario, int cicescin, int califCicEscIn, 
        String numeval) // 1/4 día
{
    String estatusAlu, idalu="";
    //boolean ingles = false;   se quito para ciclo 2023-2024
    ArrayList<Map> tblMatCalifXBim = new ArrayList<Map>(), tblAlumCapCalif, tblInasistencias = new ArrayList<Map>(), tblComDocAlum = new ArrayList<Map>();
    
    dr.put("paTras", "no");
    dr.put("califCicEscIn",califCicEscIn);
    
    if ( tblPrincipal.get("cveplan").equals("2") )   //solo secundaria
    {
        if ( tblAlumCapCalif_grado.equals("3") )
        {
            if ( txtUsuario.equals("IVALLE") || txtUsuario.equals("ELYLOPEZ") /*|| txtUsuario.equals("JULIANCRUZ") || txtUsuario.equals("EMMANUEL") ||  txtUsuario.equals("EDITHEGON") || txtUsuario.equals("MARIOCIMM")*/ )
                //CONTINUA
                dr.put("paTras", "si");
            else {
                if ( califCicEscIn > (cicescin-1) )                             //OJO: Este if es solo para SICEEO, para que las escuelas puedan navegar máximo un ciclo atrás al actual
                    dr.put("paTras", "si");
                else
                    return false;
            }
        }

        else if ( tblAlumCapCalif_grado.equals("2") ){
            if ( cicescin - (califCicEscIn-1) < 2 )
                dr.put("paTras", "si");

        }else if ( tblAlumCapCalif_grado.equals("1") )
            if ( cicescin - (califCicEscIn-1)  <3 )
               dr.put("paTras", "si");

        if (dr.get("paTras").equals("si"))
        {
            califCicEscIn=califCicEscIn -1 ;
            dr.put("califCicEscIn",califCicEscIn);
            dr.put("lblCiclo", califCicEscIn+"-"+(califCicEscIn+1));
            
            estatusAlu = ( califCicEscIn == cicescin )? "I":"BD";
            
            try {
                qryIfx.conectar();
                Map ofs = qryIfx.oficYDesoficEnCalifEval(""+dr.get("califCicEscIn"), ""+tblPrincipal.get("idcct"), tblAlumCapCalif_grado, ""+tblPrincipal.get("grupo"), idalu, "CALIFS BIM", numeval);
                tblAlumCapCalif=qryIfx.alumCapCalif (estatusAlu, ""+califCicEscIn, ""+tblPrincipal.get("idcct"), ""+tblPrincipal.get("grado"), ""+tblPrincipal.get("grupo"));
                dr.put("tblAlumCapCalif", tblAlumCapCalif);                         //qry de captura de calificaciones
                if (!tblAlumCapCalif.isEmpty())  {
                    idalu = ""+tblAlumCapCalif.get(0).get("idalu");
                    tblMatCalifXBim=qryIfx.matCalifXBim (numeval, ""+tblAlumCapCalif.get(0).get("idalu"), ""+califCicEscIn, ""+tblPrincipal.get("cveplan"));
                    tblInasistencias=qryIfx.inasistencias (""+tblAlumCapCalif.get(0).get("cicescini"), ""+tblAlumCapCalif.get(0).get("grado"), ""+tblAlumCapCalif.get(0).get("idalu"));
                    tblComDocAlum = qryIfx.comunicacionAlumDoc(""+tblAlumCapCalif.get(0).get("cicescini"),""+tblAlumCapCalif.get(0).get("idalu"));
                }
                /*if (tblMatCalifXBim.size() == 0 && !(""+tblAlumCapCalif.get(0).get("cveplan")).equals("2"))
                    ingles = true;*/
                dr.put("tblMatCalifXBim", tblMatCalifXBim);
                dr.put("tblInasistencias", tblInasistencias);
                dr.put("tblComDocAlum", tblComDocAlum);
                dr.put("ofs", ofs);
                //dr.put("alertaIngles", ingles);
            } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
            catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
            finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        }
    }
    return true;
}

private void btnSiguienteCiclo_Click (Map tblPrincipal, String numeval, int califCicEscIn, int cicescin) // 1/8 día
{
    String estatusAlu, idalu="";
    ArrayList<Map> tblMatCalifXBim = new ArrayList<Map>(), tblAlumCapCalif, tblInasistencias = new ArrayList<Map>(), tblComDocAlum = new ArrayList<Map>();
    Map ofs = new HashMap();
    //boolean ingles = false; se quito para en ciclo 2023-2024
    
    dr.put("califCicEscIn",califCicEscIn);

    if ( califCicEscIn < cicescin )
    {
        califCicEscIn= califCicEscIn +1 ;
        dr.put("califCicEscIn",califCicEscIn);
        dr.put("lblCiclo",califCicEscIn+"-"+(califCicEscIn+1));
        
        estatusAlu = ( califCicEscIn == cicescin )? "I":"BD";
        
        try {
            qryIfx.conectar();
            tblAlumCapCalif=qryIfx.alumCapCalif (estatusAlu, ""+califCicEscIn, ""+tblPrincipal.get("idcct"), ""+tblPrincipal.get("grado"), ""+tblPrincipal.get("grupo"));
            dr.put("tblAlumCapCalif", tblAlumCapCalif);                         //qry de captura de calificaciones
            ofs = qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, ""+tblPrincipal.get("idcct"), ""+tblPrincipal.get("grado"), ""+tblPrincipal.get("grupo"), idalu, "CALIFS BIM", numeval);
            if (!tblAlumCapCalif.isEmpty()) {
                idalu = ""+tblAlumCapCalif.get(0).get("idalu");
                tblMatCalifXBim = qryIfx.matCalifXBim (numeval, ""+tblAlumCapCalif.get(0).get("idalu"), ""+califCicEscIn,""+tblPrincipal.get("cveplan"));
                tblInasistencias = qryIfx.inasistencias (""+tblAlumCapCalif.get(0).get("cicescini"), ""+tblAlumCapCalif.get(0).get("grado"), ""+tblAlumCapCalif.get(0).get("idalu"));
                tblComDocAlum = qryIfx.comunicacionAlumDoc(""+tblAlumCapCalif.get(0).get("cicescini"),""+tblAlumCapCalif.get(0).get("idalu"));
            }
            /*if (tblMatCalifXBim.size() == 0 && !(""+tblPrincipal.get("cveplan")).equals("2"))
                ingles = true;*/
            dr.put("tblMatCalifXBim", tblMatCalifXBim);
            dr.put("tblInasistencias", tblInasistencias);
            dr.put("tblComDocAlum", tblComDocAlum);
            dr.put("ofs", ofs);
            //dr.put("alertaIngles", ingles);
            
        } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);  }
        catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
    }
}

private void btnGenBim_Click (String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String bim, String idalu, 
        String txtUsuario, /*String estatusIng,*/ String tblPrincipal_cveplan)
{
    String proceso="";
    //boolean ingles = false;//,autocomit=false;
    ArrayList<Map> tblMatCalifXBim=new ArrayList<Map>(), matDeGrado = new ArrayList<Map>(), QMatAlum= new ArrayList<Map>();
    
    dr.put("returnCase", 1);
//CHEAMOS A ALUMNO X ALUMNO SI YA TIENE SUS MATERIAS DEL BIMESTRE
//SINO SE LAS AGREGAMOS
    //     dm.q_ChekBim.Close;
    //     dm.q_ChekBim.ParamByName('v_numeval').AsInteger := dm.v_bim;
    //     dm.q_ChekBim.Open;
    try {
        dm.isIdcctAutorizada (sesion, tblPrincipal_idcct);
        qryIfx.conectar();
        
        Map oficYDesofic = qryIfx.oficYDesoficEnCalifEval (califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idalu, "CALIFS BIM", bim);        
        // if (oficYDesofic.get("bimOf").equals(true)) -de maai 
        // if (oficYDesofic.get("todoDeof").equals(false)) - agregado por mi en un momento
        //if (oficYDesofic.get("bimOf").equals(true))     //porque afecta a todos los alumnos   
        if(oficYDesofic.get("todoOf").equals(true) && !oficYDesofic.get("algunDeof").equals(true))   
            throw new SICEEO_Excepcion (0,"BLOQUEADO_POR_OFICIALIZACION");
        /*if(tblPrincipal_cveplan.equals("1")){            
            if((estatusIng.equals("null") || estatusIng.equals("undefined")))
                throw new SICEEO_Excepcion (0,"CHK_INGLES");
        } */ 

        // Verificar si todos tienen su esquemamaterias en tabla alumnomaterias para el 2024-2025
        // verificarMateriasDeGrupo(califCicEscIn, tblPrincipal_cveplan, tblPrincipal_idcct, tblPrincipal_grado,tblPrincipal_grupo);
            /*qryIfx.actualizarIngles(tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, califCicEscIn, tblPrincipal_cveplan, estatusIng,""+oficYDesofic.get("ingles"),txtUsuario);*/

        proceso = "GENERANDO_EVALUACIONES";        
        qryIfx.generaEvaluaciones (1, bim, txtUsuario, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, califCicEscIn);        
        proceso = "OBTENIENDO_tblMatCalifXBim";
        if (!idalu.equals("")) {
            oficYDesofic = qryIfx.oficYDesoficEnCalifEval (califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idalu, "CALIFS BIM", bim);        
            tblMatCalifXBim = qryIfx.matCalifXBim(bim, idalu, califCicEscIn,tblPrincipal_cveplan);
        }
        /*if (tblPrincipal_cveplan.equals("1") && bim.equals("1"))
            ingles = true;*/
        dr.put("tblMatCalifXBim",tblMatCalifXBim);
        //dr.put("alertaIngles",ingles);
        //autocomit = true;
    } catch (SQLException ex){ 
        if (proceso.equals("GENERANDO_EVALUACIONES") ){
            this.dr.put("returnCase",2); 
            mensaje.CalifSecxBim("ALUM_CON_MATBIM", "", "", this.dr);
            try {
                Map oficYDesofic = qryIfx.oficYDesoficEnCalifEval (califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idalu, "CALIFS BIM", bim); 
                /*if(bim.equals("1"))
                    qryIfx.actualizarIngles(tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, califCicEscIn, tblPrincipal_cveplan, estatusIng,""+oficYDesofic.get("ingles"),txtUsuario);*/
                qryIfx.generaEvaluaciones (2, bim, txtUsuario, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, califCicEscIn);                
                if (!idalu.equals("")){
                    oficYDesofic = qryIfx.oficYDesoficEnCalifEval (califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, idalu, "CALIFS BIM", bim); 
                    tblMatCalifXBim = qryIfx.matCalifXBim(bim, idalu, califCicEscIn, tblPrincipal_cveplan);
                }
                /*if (tblPrincipal_cveplan.equals("1") && bim.equals("1"))
                    ingles = true;*/
                dr.put("tblMatCalifXBim",tblMatCalifXBim);
                //dr.put("alertaIngles",ingles);
                //autocomit = true;
            }catch(Exception ex2) { this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex2.getMessage(), "", this.dr); }
        }else {
            this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr);
        }        
    }catch (SICEEO_Excepcion ex){  this.dr.put("returnCase",ex.getNumError());  mensaje.CalifSecxBim(ex.getMensaje(), ex.getMensaje2(), ex.getMensaje3(), this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion();} catch (SQLException ex) { } }
    //       dm.q_ChekBim.close;
}

private void tblAlumCapCalif_ChangeSelectedItem (String cbxBim_SelItem, String tblAlumCapCalif_idalu, String califCicEscIn, String tblPrincipal_idcct, 
        String tblPrincipal_grado, String tblPrincipal_grupo,String tblPrincipal_cveplan)
{
    ArrayList<Map> tblMatCalifXBim, tblInasistencias, tblComDocAlum;
    
    try
    {
        qryIfx.conectar();
        Map ofs = qryIfx.oficYDesoficEnCalifEval(""+califCicEscIn, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, tblAlumCapCalif_idalu, "CALIFS BIM", cbxBim_SelItem);
        tblMatCalifXBim = qryIfx.matCalifXBim(cbxBim_SelItem, tblAlumCapCalif_idalu, califCicEscIn, tblPrincipal_cveplan); // en secundarias hay k abir este qry x materia x bimestre
        /*if (tblMatCalifXBim.size() == 0 && !tblPrincipal_cveplan.equals("2"))
            ingles = true;*/
        dr.put("tblMatCalifXBim",tblMatCalifXBim);
        tblInasistencias = qryIfx.inasistencias (califCicEscIn, tblPrincipal_grado, tblAlumCapCalif_idalu);
        tblComDocAlum = qryIfx.comunicacionAlumDoc(califCicEscIn, tblAlumCapCalif_idalu);  //qry de captura de calificaciones
        dr.put("tblInasistencias", tblInasistencias);
        dr.put("tblComDocAlum",tblComDocAlum);
        dr.put("ofs", ofs);
        //dr.put("alertaIngles", ingles);
    } catch (SQLException ex){ this.dr.put("returnCase",0); mensaje.XBimOPromFin("ALUM_ERROR_HIST", "", "", this.dr);  }
    catch (Exception ex){ this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", this.dr); }
    finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
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
}
