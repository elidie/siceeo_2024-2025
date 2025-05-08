package modelo.principal;

import modelo.DAO.SICEEO_QueriesInformix;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelo.ClasesGlobales.*;

/**
 *
 * @author dai
 */
public class SICEEO_Principal {
    private final SICEEO_DataModule dm;
    private final SICEEO_QueriesInformix qryIfx;
    private final SICEEO_Mensajes mensaje;
    private final Map dr;
    private SICEEO_HttpServletRequest r;
    
    private boolean VarPass;
    private String var_condici, v_escuela, v_cveturno, kgrupo;
    private String impres; //defino la impresora como fichero texto
    private int TiempoContador; // sirve para chekar cuanto tiempo tiene el usuario sin ocupar el sistema
                           // si revasa el tiempo el sistema se cerrara
    
    public SICEEO_Principal (Map datosReturn){
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
        String tipo_usuario, txtUsuario;
        
        if (metodo.equals("haBu"))
        {
            HttpSession sesion = request.getSession(false);
            
            boolean rbnFaltante59=false, rbnFaltante59mod=false, rbn22=true, rbnN22=false;
            boolean chkMig=false, chkAc=true, chkCl=false;
            
            dr.put("vista", r.gP("vista"));
            dr.put("orden", r.gP("orden"));
            dr.put("unidad", r.gP("unidad"));
            tipo_usuario = ""+sesion.getAttribute("tipo_usuario");
            txtUsuario = ""+sesion.getAttribute("userName");
            
            
            String seccion = ""+sesion.getAttribute("seccion");
            
            if (seccion.equals("59")){
                rbnFaltante59 = true;
                rbn22 = false;
            }
            hacerBusqueda(r.gP("campo"),r.gP("operador"),r.gP("texto"),r.gP("btnPor1"),r.gP("campo1"),r.gP("operador1"),r.gP("texto1"),r.gP("btnPor2"),
                    r.gP("campo2"),r.gP("operador2"),r.gP("texto2"), rbnFaltante59, rbnFaltante59mod, rbn22, rbnN22, "", chkMig, chkAc, chkCl, 
                    r.gP("cicescini"), txtUsuario, tipo_usuario, sesion);
        }
    }
    
/* ***************************************************************************** */
/* ***************************************************************************** */
/* ***************************************************************************** */
    
    private boolean hacerBusqueda (String campo, String operador, String texto, String btnPor1, String campo1, String operador1, String texto1, String btnPor2, 
            String campo2, String operador2, String texto2, boolean rbnFaltante59, boolean rbnFaltante59mod, boolean rbn22, boolean rbnN22, String txtReg59, 
            boolean chkMig, boolean chkAc, boolean chkCl, String cicescini, String txtUsuario, String tipo_usuario, HttpSession sesion )
    {
        String miCampo, miConsulta="", miCondicion;
        String elwhere="", grupo="", cvemat="", grupocap="", grupoTall="", cvematTall="",modulos="";
        boolean errorConn=false, insertarTaller = false;
        Map QEscuela=null;
        String[] distinctIdscct={""};
        ArrayList<Map> QPrincipal = null;
        Map QUsuario;
        
        try {
            miCampo = verCampo( campo, operador, texto );
            if (!miCampo.equals("")){
                miConsulta = miCampo;
                if (!texto1.equals("")){
                    miCampo = verCampo(campo1, operador1, texto1);
                    if (!miCampo.equals("")){
                       if (btnPor1.equals("Y")) miCondicion = " And ";
                       else miCondicion = " Or ";
                       miConsulta = miConsulta + miCondicion + miCampo;
                    }
                }
                if (!texto2.equals("")){
                    miCampo = verCampo(campo2, operador2, texto2);
                    if (!miCampo.equals("")){
                       if (btnPor2.equals("Y")) miCondicion = " And ";
                       else miCondicion = " Or ";
                       miConsulta = miConsulta + miCondicion + miCampo;
                    }
                }
                
                /*if (rbnFaltante59)
                    miConsulta += " AND ( (substr(GRupo,2,1) = '_' AND length(trim(GRupo)) = 2)" + " OR substr(GRupo,2,2) = '__' )";

                if (rbnFaltante59mod)
                {
                    miConsulta += " and substr(GRupo,2,2) = '__'";
                    if (txtReg59.trim().length()>0)
                        miConsulta += "and cveunidad59 = \""+txtReg59.trim()+'"';
                }*/
                try {
                        qryIfx.conectar();
                        QUsuario = qryIfx.usuario (txtUsuario);
                        if(!QUsuario.isEmpty())
                            modulos = ""+QUsuario.get("modulos");
                        
                } catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("CONEXION",ex.getMessage(),"", dr);  }
                catch (Exception ex){    this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
                finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
                
                
                //if (chkAc)
                  //  miConsulta += " AND EstatusEsc = 'AC'";

                if (chkCl) {
                    miConsulta += " AND EstatusEsc = 'CL'";                
                } else {
                    if(modulos.contains(",23,") || modulos.contains(",25,")){                        
                          miConsulta += " AND (EstatusEsc = 'AC' OR EstatusEsc = 'IA' ";  
                          /*if(modulos.contains(",23,"))
                              miConsulta += " OR EstatusEsc = 'CL' ";*/
                          miConsulta += " )";
                    }
                    else 
                        miConsulta += " AND EstatusEsc = 'AC'";
                    // seccion y situacion
                    if(modulos.contains(",25,")){
                        miConsulta += " AND situacion IN ('U','C') AND seccion='22' ";
                    }
                }
                                                
                if (chkMig)  //Migrantes
                    miConsulta += " AND substr(GRupo,2,1) = '-'";
                
                miConsulta += " AND Estatus_GPO = 'A'";   //11/03/2016
                
                /*if (rbn22){
                    if (chkMig)  //Migrantes
                        miConsulta += " and substr(GRupo,2,1) = '-'";
                    else{
                        miConsulta += " and length(trim(GRupo)) = 1";
                        if ( rbnN22 )
                            miConsulta += " and idcct||'-'||GRADO in (select distinct idcct||'-'||GRADO from alumnogrado where cicescini=2013 and substr(cp,2,1)='_') ";
                    }
                }*/        

                if (!tipo_usuario.equals(" ") && !tipo_usuario.equals("consulta") &&
                   !tipo_usuario.equals("mesa") && !tipo_usuario.equals("captura")) // cuando es un usuario tipo CCT
                    miConsulta += " and idcct= "+tipo_usuario;

                //  MessageDlg('QRY:'+#13+#10+Miconsulta, mtWarning, [mbOK], 0);
                QPrincipal = asignarMacro( "CONDICION", miConsulta, true, cicescini, distinctIdscct);

                this.var_condici = miConsulta;
                elwhere = miConsulta;
            }
            //-----------------------------
            if (!tipo_usuario.equals(" ") && !tipo_usuario.equals("consulta") && !tipo_usuario.equals("mesa") && !tipo_usuario.equals("captura") && miConsulta.equals("")) // cuando es un usuario tipo CCT
            {
                //miConsulta += " idcct= "+tipo_usuario + " and length(trim(GRupo)) = 1"; //los usuarios de tipo CCT x el momento son solo
                                                                                                         //usuarios k trabajan con la 22
                miConsulta += " idcct= "+tipo_usuario;           //ya entran todos los usuarios 22 y 59
                miConsulta += " AND Estatus_GPO = 'A'";                         //11/03/2016 
                QPrincipal = asignarMacro( "CONDICION", miConsulta,true, cicescini, distinctIdscct);                                                   
                this.var_condici = miConsulta;
                elwhere = miConsulta;
            }
            
            if ((QPrincipal==null || QPrincipal.isEmpty()) && !this.v_escuela.equals(""))
            {
               //MessageDlg('Sin Grupos en Ciclo Actual', mtWarning, [mbOK], 0);
               //buscamos la escuela en la BD
                try{
                    errorConn = true;
                    qryIfx.conectar();
                    if (!v_cveturno.equals("") )
                        QEscuela = qryIfx.escuela(this.v_escuela+" AND "+v_cveturno);
                    else
                        QEscuela = qryIfx.escuela(this.v_escuela);
                    
                    if (!QEscuela.isEmpty() && QEscuela.get("estatusesc").equals("CL") && chkAc)
                    {
                        this.dr.put("returnCase",0); mensaje.principal("ESC_CLAUSURADA",""+QEscuela.get("cct"),"", dr);
                        try { qryIfx.cerrarConexion(); } catch (SQLException ex) { }
                        return false;
                    }
                    errorConn = false;
                }catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("CONEXION",ex.getMessage(),"", dr);  }
                 catch (Exception ex){    this.dr.put("returnCase", -1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
                 try { qryIfx.cerrarConexion(); } catch (SQLException ex) { }
                 if (errorConn) return false;
                 
                if (!QEscuela.isEmpty())
                {
                    //la escuela existe pero es posible k no tenga director
                    //chekemos
                    if (!QEscuela.get("cveunidad").equals(this.dr.get("unidad")))
                    {
                        mensaje.principal("ESCUELA_DESUBICADA",""+QEscuela.get("cveunidad"),"",dr);
                        this.dr.put("returnCase",2);
                        //si trabajamos con modulos el cambio de region k sea automatico
                        try{
                            qryIfx.conectar();
                            QUsuario = qryIfx.usuario (txtUsuario);
                            if (rbnFaltante59mod || tipo_usuario.equals("consulta") || tipo_usuario.equals("captura") ||
                                tipo_usuario.equals("mesa") || (""+QUsuario.get("modulos")).contains(",11,"))
                            {
                                this.dr.put("vista",""+QEscuela.get("cveunidad"));
                                QPrincipal = qryIfx.queryPrincipal(""+this.dr.get("vista"), ""+this.dr.get("condicion"), ""+this.dr.get("orden"), cicescini, distinctIdscct);
                                this.dr.put("tabla",QPrincipal);
                                this.dr.put("unidad",""+QEscuela.get("cveunidad"));
                                //*F_PRINCIPAL.L_DSR.Caption:= DM.V_UNIDAD;
                                return true;
                            }
                        }catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("CONEXION",ex.getMessage(),"", dr);  }
                        catch (Exception ex){    this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
                        finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
                    }else
                    {
                        //----LE CREAMOS UN GRUPO-----------------------------------------------------------
                        /*
                        try{
                            qryIfx.conectarConTransaccion();
                            grupo = (rbnFaltante59)?"A_":(rbnFaltante59mod)?"A__":(chkMig)?"A-":(rbn22)?"A":"";
                            
                            if (QEscuela.get("cveplan").equals("1")) 
                                { cvemat="001";  grupocap="45";   }
                            if (QEscuela.get("cveplan").equals("2"))
                                { cvemat="001";  grupocap="50";   }
                            if (QEscuela.get("cveplan").equals("3"))
                                { cvemat="100";   grupocap="35";   }
                            
                            if dm.Q_ESCUELAcveplan.AsInteger <4 then //solo ejecutar si es pree pri o sec
                            //CANCELAMOS EL CREAR GRUPO 11/03/2016
                            // dm.Q_NewGrupo.ExecSQL;
                            
                            //IF Pos(DM.Q_Principalmod.AsString, 'DES DST PES PST DTV PTV')>0 THEN
                            if ( "DES DST PES PST DTV PTV ESC".contains((""+QEscuela.get("cct")).substring(2,5)) || ( ((""+QEscuela.get("cct")).substring(2,5).equals("DML") && QEscuela.get("cveturno").equals("300"))) )
                            {
                                grupoTall = (rbnFaltante59)?"A_":(rbnFaltante59mod)?"A__":(rbn22)?"A":"";
                                cvematTall = ("DTV PTV".contains((""+QEscuela.get("cct")).substring(2,5)))?"965":"999";
                                insertarTaller = true;
                                
                                if dm.Q_ESCUELAcveplan.AsInteger <4 then //solo ejecutar si es pree pri o sec
                                //CANCELAMOS EL CREAR GRUPO 11/03/2016
                                // dm.Q_AsigTall.ExecSQL;
                            }
                            
                            qryIfx.crearGrupoDefault(""+QEscuela.get("idcct"), ""+QEscuela.get("cveturno"), dm.cicescini, dm.cicescini+1, "1", 
                                      grupo, "CBA", cvemat, grupocap, "0", "A",insertarTaller,grupoTall,cvematTall);
                             
                            this.dr.put("returnCase",2);
                            mensaje.principal("GRUPO_AGREGADO","","", dr);

                            // dm.Q_Grupos.Close;
                            // dm.Q_Grupos.ParamByName('v_idcct').AsInteger := dm.Q_Principalidcct.AsInteger ;
                            // dm.Q_Grupos.ParamByName('v_cicescini').AsInteger := dm.v_Califcicescin ;
                            // dm.Q_Grupos.Open;
                            hacerCommit = true;
                        }catch (SQLException ex){ this.dr.put("returnCase",-1); mensaje.General("CONEXION",ex.getMessage(),"", dr);  }
                        catch (Exception ex){    this.dr.put("returnCase",-1); mensaje.General("GENERAL", ex.getMessage(), "", dr); }
                        try { qryIfx.cerrarConexionConTransaccion(hacerCommit);} catch (SQLException ex) { }
                        */
                        //----FIN DE LE CREAMOS UN GRUPO----------------------------------------------------
                    }
                }
                //k la escuela pertenesca a la region
                //chekamos sus grupos 
            }
        } catch (Exception ex) {
            this.dr.put("returnCase",-1);  this.dr.put("mensaje",ex.getMessage());  this.dr.put("tipoMensaje","ERROR");
        }finally {
            /* **************** ESTE CÓDIGO ES ÚNICAMENTE PARA USO DE SICEEO, AQUÍ OBTENEMOS EL IDCCT EN EL QUE ESTÁ PARADO Y TRABAJANDO EL USUARIO **************** */
            sesion.setAttribute("tblPrincipal_idcct", (QPrincipal!=null && !QPrincipal.isEmpty())?QPrincipal.get(0).get("idcct"):"-1" );
            sesion.setAttribute("tblPrincipal_distinctIdscct", distinctIdscct[0] );
        }
        return true;
    }
    
    private ArrayList<Map> asignarMacro( String Macro, String valor, boolean realizar, String cicescini, String[] distinctIdscct) throws Exception
    {
        ArrayList<Map> QPrincipal=null;
        if (Macro.equals("CONDICION"))
            this.dr.put("condicion",valor);
        else if (Macro.equals("ORDEN"))
            this.dr.put("orden",valor);
        
        if (realizar){
            try{
                qryIfx.conectar();
                QPrincipal = qryIfx.queryPrincipal (""+this.dr.get("vista"), ""+this.dr.get("condicion"), ""+this.dr.get("orden"), cicescini, distinctIdscct);
                this.dr.put("tabla",QPrincipal);
                this.dr.put("returnCase",1);                
            }catch (SQLException ex){ mensaje.General("CONEXION",ex.getMessage(),"", dr);  throw new Exception(""+this.dr.get("mensaje")); }
             catch (Exception ex){  mensaje.General("GENERAL", ex.getMessage(), "", dr); throw new Exception(""+this.dr.get("mensaje")); }
             finally { try { qryIfx.cerrarConexion(); } catch (SQLException ex) { } }
        }
        return QPrincipal;
    }
    
    private String verCampo( String CualCampo, String Operador, String Contenido) throws Exception
    {
        String Resultado="", Operady="";
        
        if( Operador.equals("=")) Operady = " like ";
        if( Operador.equals("<>")) Operady = " not like ";

        if (!Contenido.equals("")) 
        {
            if (isCampoTipo (CualCampo, "String"))
               Resultado = CualCampo + Operady + "'%"+ Contenido +"%'";
            else if (isCampoTipo (CualCampo, "Int"))
               try { Resultado = ""+Integer.parseInt(Contenido); } catch (Exception ex){ throw new Exception ("El valor "+Contenido+" no es válido"); }
            else if (isCampoTipo (CualCampo, "Date")){
               if (!dm.isDate_Time(Contenido, "yyyy/MM/dd"))
                   throw new Exception ("La fecha "+Contenido+" no es válida");
               Resultado = Contenido;
            }else if (isCampoTipo (CualCampo, "Time")){
               if (!dm.isDate_Time(Contenido, "HH:mm:ss"))
                   throw new Exception ("La hora "+Contenido+" no es válida");
               Resultado = Contenido;
            }else if (isCampoTipo (CualCampo, "Float")){
               if (!dm.isFloat(Contenido))
                   throw new Exception ("El valor "+Contenido+" no es válido");
               Resultado = Contenido;
            }

            if (CualCampo.equals("cct") && Contenido.length()>=8)
                this.v_escuela =CualCampo + Operady + "'%"+ Contenido +"%'";
            //else
            //    this.v_escuela ="";
            
            this.v_cveturno = "";
            if ( CualCampo.equals("CVEturno") && Contenido.length()==3 )
                this.v_cveturno = CualCampo + Operador + "'" + Contenido + "'" ;

            if (Resultado.equals(""))
                Resultado = CualCampo + Operador + Contenido;
            if (CualCampo.equals("mod") && Contenido.length()==3)
                Resultado = CualCampo + Operador + "'" + Contenido + "'";
            if (CualCampo.equals("cct") && Contenido.length()==10)
                Resultado = CualCampo + " = " + "'" + Contenido + "'";
            if (CualCampo.equals("Grupo") && Contenido.length()==1)
                Resultado = CualCampo + " = " + "'" + Contenido + "'";
        }
        return Resultado;
    }
    
    private boolean isCampoTipo (String campo, String tipo)
    {
        String atributosTipoString= "cveprograma, cvenivel, cvesistema, cvesubsis, cvezona, sector, cct, cveturno, nombre, nombreAnt, domicilio, entrecalle, ycalle, cp, telefono, fax, cveunidad, cveunidad59, deslocalidad59, desmunicipio59, director, cveservicio, mod,EstatusEsc, desmunicipio, deslocalidad, grupo, salta";
        String atributosTipoInt = "idcct, cveplan, cvemunicipio, cvelocalidad, grado, cicescini, cicescfin";
        String atributosTipoDate = "";
        String atributosTipoTime = "";
        String atributosTipoFloat = "";
        
        if (tipo.equals("String"))
            return atributosTipoString.contains(campo);
        else if(tipo.equals("Int"))
            return atributosTipoInt.contains(campo);
        else if (tipo.equals("Date"))
            return atributosTipoDate.contains(campo);
        else if (tipo.equals("Time"))
            return atributosTipoTime.contains(campo);
        else if (tipo.equals("Float"))
        return atributosTipoFloat.contains(campo);
        return false;
    }
}
