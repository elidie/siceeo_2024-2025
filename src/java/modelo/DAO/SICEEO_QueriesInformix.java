/*
 * WSFIRMA_ConexionInformix.java
 *
 * Creado el 09/Feb/2017 18:38
 *
 */
package modelo.DAO;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import modelo.ClasesGlobales.SICEEO_CoordenadasImpre;
import modelo.ClasesGlobales.SICEEO_DataModule;
import modelo.ClasesGlobales.SICEEO_Excepcion;

/**
 *
 * @author Ing. Maai Nolasco Sánchez
 */
public class SICEEO_QueriesInformix extends SICEEO_ConexionInformix {
    public SICEEO_QueriesInformix ()
    {
        super();
    }
    public SICEEO_QueriesInformix (String tipoConexion) throws Exception
    {
        super(tipoConexion);
    }
    
    public void setTipoConexionAsLOCAL(){ setConnectionTypeAsLOCAL(); }
    public void setTipoConexionAsINTERNET(){ setConnectionTypeAsINTERNET (); }
    public void setTipoConexionAsODBC(){ setConnectionTypeAsODBC (); }
    public void setTipoConexion ( String tipoConexion ) throws Exception { setConnectionType(tipoConexion); }
    public Connection getConexion () { return getConnection(); }
    public String getDefaultTipoConexion(){ return getDefaultConectionType(); }
    public void conectar()throws SQLException { connect (); }
    public void cerrarConexion()throws SQLException{ closeConnection(); }
    public void conectarConTransaccion()throws SQLException, ClassNotFoundException { connectWithTransaction(); }
    public void cerrarConexionConTransaccion(boolean commit)throws SQLException { closeConnectionWithTransaction(commit); }
    public String codificarABase64(String cadena) throws UnsupportedEncodingException { return encodeBase64(cadena); }
    public String decodificarBase64(String cadena) { return decodeBase64(cadena); }
 
//************************************************************************************************************
//******************************** MÉTODOS USADOS EN EL MÓDULO DE LOGIN USER *********************************
//************************************************************************************************************ 
    public Map Ciclo () throws SQLException
    {
        rs = stm.executeQuery("SELECT Cicescini, Cicescfin, descicesc,fecini,version,versiontext, fecha, hora, link, link2, linkdai, actualiz, boton, version_siceeo, maxsesserv_siceeo " 
                                        +"FROM CicloEscolar " 
                                        +"WHERE estatus='A'");
        return qryToMap (rs, null, true, 0);
    }
    public Map Ciclo_anterior () throws SQLException
    {
        rs = stm.executeQuery("SELECT Cicescini, Cicescfin, descicesc,version,versiontext, fecha, hora, link, link2, linkdai, actualiz, boton, version_siceeo, maxsesserv_siceeo " 
                                        +"FROM CicloEscolar " 
                                        +"WHERE Cicescini=2020");
        return qryToMap (rs, null, true, 0);
    }
    
    public Map usuario (String usuario) throws SQLException
    {
        rs = stm.executeQuery("SELECT u.idusuario, u.loginuser, u.Pasword,  u.cveunidad, u.idcct, u.curp, u.nombreu, u.apepat, u.apemat, u.puesto, u.telefono, "
                                + "u.email, u.usertipo, estatus, u.modulos,e.cveunidad AS CveUniEsc, "
                                + "(SELECT t.tiempo FROM  siceebtime t WHERE u.cveunidad = t.tipousuario) AS tiempo "
                                + ", e.cct AS cctdefault, e.modalidad, seccion "         //OJO: El campo cctdefault y sección se agregó solo para SICEEO, sirve para ejecutar automáticamente la búsqueda de escuela para usuarios tipo CCT y detectar si la escuela es sección 22 o 59
                            + "FROM usuarios u, escuela e "
                            + "WHERE u.idcct= e.idcct AND LoginUser = '"+usuario.trim().toUpperCase()+"'");
        
        return qryToMap (rs, null, true, 2);
    }
    
    public void log (String usuario, String unidad, String ip, String nomPC) throws SQLException
    {
        stm.execute("INSERT INTO repSince_Log(usuario,cveunidad,ip,pc,fecha_e,hora_e) "
                    + "values ('"+usuario.toUpperCase()+"', '"+unidad+"', '"+ip+"', '"+nomPC+"', date(current), extend(current, hour to minute))");
    }
    
    public ArrayList<String> getPermisos (String tipoUsuario, String usuario,String formulario, String cveplan) throws SQLException
    {
        String filtro="";
        int numExcepciones = 0;
        ArrayList<Map> excepciones = new ArrayList<Map>();
        ArrayList<String> componente = new ArrayList<String>();
       /* String 
                cadena = "";*/
        if (!formulario.equals("")){            
            /*int i=0;
            for (i=0; i< formulario.length; i++)
                cadena += "'"+formulario[i]+"',";
            
            cadena = cadena.substring(0, cadena.length()-1);*/
            
            filtro = " AND op.formulario='"+formulario+"' ";
            //filtro = " AND op.formulario IN ("+cadena+")";
        }
        // Buscamos primero si tiene una configuración específica de acceso 
        rs = stm.executeQuery("SELECT ae.idaccesoespecifico, op.componente, ae.permiso "
                        + "FROM siceeo_accesoEspecifico ae, siceeo_objetospermiso op "
                        + "WHERE ae.idObjeto=op.idobjeto AND formulario='"+formulario+"' AND loginUser='"+usuario+"'");  //se reemplazo formulario por cadena.
        excepciones=qryToArrlmap(rs, null, true, 2);
        
        
        //Extraemos los permisos de configuración general
        rs = stm.executeQuery("SELECT op.componente "                           
                            + "FROM siceeo_permisos p, siceeo_configpermiso cp, siceeo_objetospermiso op "
                            + "WHERE p.cveconfigpermiso=cp.cveconfigpermiso AND op.idobjeto=cp.idobjeto "
                            + "AND tipousuario='"+tipoUsuario+"' AND cp.permiso='t' " + filtro + ""
                            + "ORDER BY orden");
        while (rs.next()) 
            componente.add(rs.getString("componente"));
      
        
        //Le aplicamos las excepciones
        numExcepciones = excepciones.size();
        for (int i=0; i<numExcepciones; i++){
            if ( excepciones.get(i).get("permiso").equals("t") ){
                if (!componente.contains(""+excepciones.get(i).get("componente")))
                    componente.add(""+excepciones.get(i).get("componente"));
            }else
                componente.remove(""+excepciones.get(i).get("componente"));
        }
        
        return componente;
    }
     
//************************************************************************************************************
//******************************** MÉTODOS USADOS EN EL MÓDULO DE LOGIN USER *********************************
//************************************************************************************************************
    public void actualizaUsuario (String B64encode_pwdNewPassword, String txtNombre, String txtApepat, String txtApemat, String txtCurp, String txtPuesto, String txtTelefono, 
            String txtCorreo, String QUsuario_idusuario) throws SQLException
    {
        stm.execute("UPDATE USUARIOS SET PASWORD = \""+B64encode_pwdNewPassword+"\", nombreu = \""+txtNombre.toUpperCase()+"\", apepat=\""+txtApepat.toUpperCase()+"\", "
                        + "apemat=\""+txtApemat.toUpperCase()+"\", curp='"+txtCurp.toUpperCase()+"', puesto='"+txtPuesto.toUpperCase()+"', telefono='"+txtTelefono.toUpperCase()+"', email='"+txtCorreo.toLowerCase()+"' "
                    + "WHERE idusuario="+QUsuario_idusuario);
    }
            
//************************************************************************************************************
//****************************************** MÉTODOS USADOS EN EL MÓDULO PRINCIPAL ****************************
//************************************************************************************************************
    public ArrayList<Map>  queryPrincipal (String vista, String Condicion, String Orden, String cicescini, String[] distinctIdscct) throws SQLException
    {           
        if (Orden.equals(""))
            Orden = "cveplan";
        
        rs = stm.executeQuery("SELECT DISTINCT(idcct) AS idcct FROM V_"+vista+" WHERE ("+Condicion+") AND cicescini="+cicescini);
        while (rs.next())
            distinctIdscct[0]+=rs.getString("idcct")+"~";
                    
        rs = stm.executeQuery("SELECT idcct, cveprograma, cveplan, cvenivel, cvesistema, cvesubsis, cvezona, sector, cct, cveturno, nombre, "
                + "nombreAnt, domicilio, entrecalle, ycalle, cp, telefono, fax, cveunidad, cveunidad59, deslocalidad59, desmunicipio59, "
                + "cvemunicipio , cvelocalidad , director, cveservicio, modalidad, EstatusEsc, desmunicipio, deslocalidad, grado, grupo, alum, cicescini, "
                + "cicescfin, (TRIM(cct) || '-' || grado || '-' || TRIM(grupo))||cveturno AS salta "
                + "FROM V_"+vista+" "
                + "WHERE ("+Condicion+") AND cicescini="+cicescini+" "
                + "ORDER BY "+Orden + ",cvezona,idcct,cct,cveturno,grado,grupo"); 
        /** **/
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public Map escuela (String condicion) throws SQLException
    {
        rs = stm.executeQuery("SELECT idcct, cct, cveturno, cveunidad, cvezona, cveplan, cveprograma, estatusEsc FROM escuela WHERE situacion!='I' AND "+condicion + " ORDER BY estatusesc ASC"); 
        return qryToMap (rs, null, true, 1);
    }
    
    public void crearGrupoDefault (String QEscuela_idcct, String QEscuela_cveturno, int cicescini, int cicescfin, String grado, String grupo, String cvetipmat, String cvemat, String grupocap, String gruposal, String estatus, boolean insertarTaller, String grupoTall, String cvematTall) throws SQLException
    {
        String taller="EDT";
        stm.execute("INSERT INTO EscuelaGrupos(IDCCT, CVETURNO, CICESCINI, CICESCFIN, GRADO, GRUPO, CVETIPMAT,CVEMAT, GRUPOCAP, GRUPOSAL, ESTATUS) VALUES ("
                + QEscuela_idcct+",'"+QEscuela_cveturno+"',"+cicescini+","+cicescfin+","+grado+",'"+grupo+"','"+cvetipmat+"','"+cvemat+"',"+grupocap+","+gruposal+",'"+estatus+"')");

        if (insertarTaller) {
            if(cicescini==2019)
                taller = "TEC";
            stm.execute("INSERT INTO EscuelaGrupos(IDCCT, CVETURNO, CICESCINI, CICESCFIN, GRADO, GRUPO, CVETIPMAT,CVEMAT, GRUPOCAP, GRUPOSAL, ESTATUS) VALUES ("
                    + QEscuela_idcct+",'"+QEscuela_cveturno+"',"+cicescini+","+cicescfin+",1,'"+grupo+"','"+taller+"','"+cvematTall+"',50,0,'A')");
        }
    }
    
    public Map getAvisosSisWeb () throws SQLException
    {
        rs = stm.executeQuery("SELECT mensaje, icono FROM AvisosSisWeb WHERE activo='t' AND sistema='SICEEO'");
        return qryToMap(rs, null, true, 1);
    }
    
//************************************************************************************************************
//********************************* MÉTODOS USADOS EN EL MÓDULO DE NuevoIngreso ******************************
//************************************************************************************************************     
    public Map getPlanMod (String cveplan, String grado, String modalidad, String cicescini, String cveentidad) throws SQLException
    {
        rs = stm.executeQuery("SELECT cveprograma, desprograma, cicescfin " 
                + "FROM Planmodalidad "
                + "WHERE plan="+cveplan+" AND grado="+grado+" AND modalidad='"+modalidad+"' AND cicescini<="+cicescini+" AND cicescfin>="+cicescini+" AND cveentidad="+cveentidad);
        return qryToMap(rs, null, true, 1);
    }
    
    public Map getNormatividad (String cveplan, String cveprograma, String grado) throws SQLException
    {
        rs = stm.executeQuery("SELECT cveplan, edadadmmin, edadadmmax "
                            + "FROM normatividad "
                            + "WHERE cveplan="+cveplan+" AND cveprograma = '"+cveprograma+"' AND grado="+grado);
        return qryToMap(rs, null, true, 1);
    }
    
    public ArrayList<String> getEstados () throws SQLException
    {
        ArrayList<String> estados = new ArrayList<String>();
        rs = stm.executeQuery("SELECT CASE WHEN cveentlet='ND' THEN ' ' ELSE desEntidad[1,23] || ',  ' || '[ ' || CVEeNTIDAD|| '] ' || cveentlet END AS entidad, cveentidad "
                + "FROM entidad "
                + "WHERE pais='MEX' "
                + "ORDER BY cveentidad");
        while (rs.next()) {
            estados.add(rs.getString("entidad"));
            //datos.put("cveENTIDAD", rs.getString("cveENTIDAD"));
        }
        return estados;
    }
    
    public ArrayList<Map> getCatDiscap (String cicescini, String cveplan) throws SQLException
    {
        rs = stm.executeQuery("SELECT  cvedefsuf[1,4] || ' | ' || desdefsuf AS cveydes, cvetipdefsuf, cvedefsuf, desdefsuf "
                + "FROM deficsuficalu "
                + "WHERE  cicescini <= "+cicescini+" AND cicescfin >="+cicescini+" AND cveplan = "+cveplan+" ORDER BY desdefsuf");
        return qryToArrlmap(rs, null, true, 0);
    }
    
    public ArrayList<Map> BuskAlum (int caso, String curp, String nombre, String ape1, String ape2) throws SQLException
    {
        String condicion="";
        if (caso==1)                                                            //Buscamos por curp, nombre y apellidos
            condicion = "(a.curp like '"+curp.trim().toUpperCase().substring(0,10) +"%' AND A.NOMBRE = \""+nombre.trim().toUpperCase()+"\" AND A.APEPAT= \""+ape1.trim().toUpperCase()+"\" AND A.APEMAT= \""+ape2.trim().toUpperCase()+"\")";
        else if (caso==2)                                                       //Buscamos únicamente por curp
            condicion = "a.curp like '"+curp.trim().toUpperCase()+"%'";
        else if (caso==3)                                                       //Buscamos únicamente por curp
            condicion = "a.curp = '"+curp.trim().toUpperCase()+"%'";
        else if (caso==4)
            condicion = curp.trim().toUpperCase();
        else if (caso==5)                                                       //Buscamos únicamente por curp
            condicion = "a.curp = '"+curp.trim().toUpperCase()+"'";
        
        return buscarAlumno (condicion);
    }
    
    public ArrayList<Map> buscarAlumno (String condicion) throws SQLException
    {
        ArrayList<Map> QBuskAlum;
        
        rs = stm.executeQuery(
                "SELECT "
                    + "a.idalu, " //0
                    + "(trim(TRAILING ' ' FROM nvl(a.apepat,'')) || '/' || trim(TRAILING ' ' FROM nvl(a.apemat,'')) || \"*\" || trim(TRAILING ' ' FROM nvl(a.nombre,''))) AS nom_tot, " //1
                    + "a.cveentidad,  a.cvemunicipio,  a.cvedocprob,  a.idcct,  a.estatusalu,  a.cvenacion, " //2-7
                    + "(SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu AND estatusgrado<>'BD') AS Maxcicescini, " //8
                    + "(SELECT cveprograma    FROM alumnogrado WHERE idalu = a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu AND estatusgrado<>'BD') ) AS CvePrograma, " //9
                    + "(SELECT cveplan        FROM alumnogrado WHERE idalu = a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu AND estatusgrado<>'BD') ) AS CvePlan, " //10
                    + "(SELECT Grado          FROM alumnogrado WHERE idalu = a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu AND estatusgrado<>'BD') ) AS Grado, " //11
                    + "(SELECT Grupo          FROM alumnogrado WHERE idalu = a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu AND estatusgrado<>'BD') ) AS Grupo, " //12
                    + "(SELECT cveDefSuf      FROM alumnogrado WHERE idalu = a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu AND estatusgrado<>'BD') ) AS cvedefsuf, " //13
                    + "(SELECT estatusgrado   FROM alumnogrado WHERE idalu = a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu AND estatusgrado<>'BD') ) AS estatusGrado, " //13    
                    + "a.cveocup,    a.cvelengua,   a.cvedoctrans,   a.idtutor,       a.curp, a.nombre, a.apepat,    a.apemat,  a.fecnac,a.sexo,  a.tiposangre,a.cveentfednac,  a.anoregistro, " //14-26
                    + "a.libro,      a.noacta,      a.foliodp,       a.errordp,       a.estatusdp, a.foliodoctrans, a.fecingsis, a.curp16,  a.cve_estat_curp, a.crip,  a.krtaCompromiso, a.probem, " //27-38
                    + "a.extranjero, a.reg_nac_ext, a.folio_cta_nat, a.estatus_cambio,(case when a.afromexicana is null then '' else a.afromexicana end) as afromexicana, " //39-42
                    //+ "(SELECT cct FROM escuela WHERE idcct = (SELECT idcct FROM alumnogrado WHERE idalu=a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu))) AS cctMax, "  //43 
                    //+ "(SELECT cveunidad FROM escuela WHERE idcct = (SELECT idcct FROM alumnogrado WHERE idalu=a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu))) AS cveunidadMax, "  //44
                    //------------ OJO: Este es para SICEEO --------------------
                    + "(SELECT estatusgrado FROM alumnogrado WHERE idalu = a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu) ) AS estatusGradoMax, " //45
                    + "(SELECT cct FROM escuela WHERE idcct = (SELECT idcct FROM alumnogrado WHERE idalu=a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu))) AS cctMax "    
                    //----------------------------------------------------------
                    + ",(SELECT idcct FROM alumnogrado WHERE idalu=a.idalu AND cicescini = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = a.idalu)) AS idcctMax "    
                +" FROM alumno a "
                +" WHERE "+condicion
                +" ORDER BY  a.apepat, a.apemat, a.nombre ");
        
        QBuskAlum = qryToArrlmap(rs, null, true, 1);
        QBuskAlum_CalcField_escalon(QBuskAlum);
        
        return QBuskAlum;
    }
    
    private void QBuskAlum_CalcField_escalon(ArrayList<Map> QBuskAlum)
    {
        int QBuskAlum_cveplan, QBuskAlum_grado;
        int numFilas = QBuskAlum.size();
        
        for (int i=0; i<numFilas; i++){
            if (!QBuskAlum.get(i).get("cveplan").equals("null"))
                QBuskAlum_cveplan = Integer.parseInt(""+QBuskAlum.get(i).get("cveplan"));
            else
                QBuskAlum_cveplan = -1;
                    
            if (!QBuskAlum.get(i).get("grado").equals("null"))
                QBuskAlum_grado = Integer.parseInt(""+QBuskAlum.get(i).get("grado"));
            else
                QBuskAlum_grado = -1;
            
            if ( QBuskAlum_cveplan == 3 ) { 
                if ( QBuskAlum_grado == 1 )
                    QBuskAlum.get(i).put("escalon", "1");
                else if ( QBuskAlum_grado == 2 )
                    QBuskAlum.get(i).put("escalon", "2");
                else if ( QBuskAlum_grado == 3 )
                    QBuskAlum.get(i).put("escalon", "3");
            }else if ( QBuskAlum_cveplan == 1 ) {
                if ( QBuskAlum_grado == 1 )
                    QBuskAlum.get(i).put("escalon", "4");
                else if ( QBuskAlum_grado == 2 )
                    QBuskAlum.get(i).put("escalon", "5");
                else if ( QBuskAlum_grado == 3 )
                    QBuskAlum.get(i).put("escalon", "6");
                else if ( QBuskAlum_grado == 4 )
                    QBuskAlum.get(i).put("escalon", "7");
                else if ( QBuskAlum_grado == 5 )
                    QBuskAlum.get(i).put("escalon", "8") ;
                else if ( QBuskAlum_grado == 6 )
                    QBuskAlum.get(i).put("escalon", "9") ;
            }else if ( QBuskAlum_cveplan == 2 ) {
                if ( QBuskAlum_grado == 1 )
                    QBuskAlum.get(i).put("escalon", "10");
                else if ( QBuskAlum_grado == 2 )
                    QBuskAlum.get(i).put("escalon", "11");
                else if ( QBuskAlum_grado == 3 )
                    QBuskAlum.get(i).put("escalon", "12");
            }
        }
    }
    
    public int maxidxesc (String idcct) throws SQLException
    {
        rs = stm.executeQuery("SELECT Min(idalu) AS libreidalu FROM idalus WHERE estatusalu='D' AND idcct="+idcct);
        if (rs.next())
            return rs.getInt("libreidalu");
        return -1;
    }
      
    public void actualizaAlumno (String alumEstatus, String QBuskAlum_estatusalu, String QBuskAlum_curp, String idalu, String cveentidad, String idcct, String tblPrincipal_cct, 
            String discapacidad, String newIdTutor, String curpRaiz, String nombre, String ape1, String ape2, String fechaNacimiento, String sexo, 
            String cartaCompromiso, String entNac, String probem, String Extj, int cicescini, int grado, String cveturno, String cveplan, String cveprograma, 
            String grupo, int edad, String usuario, String taller, String arte, String existeUno,String cvelengua, String etnia, String fechaIng) throws SQLException, SICEEO_Excepcion
    {
        String qurySec="", calif;
        String lengua = cvelengua.isEmpty()?"ESP":cvelengua;
        if (alumEstatus.equals("I")) {
            stm.execute("INSERT INTO Alumno VALUES ("+idalu+", "+cveentidad+", null, 1,"+idcct+", 'I','MEX','"+(discapacidad.equals("")?"":discapacidad.substring(2,5))+"','01','"+lengua+"','A', "+newIdTutor+", "
                    + "'"+curpRaiz+"', \""+nombre.trim()+"\", \""+ape1.trim()+"\", \""+ape2.trim()+"\","
                    +" TO_DATE('"+fechaNacimiento+"','%Y/%m/%d'),  "
                    + "'"+sexo+"','"+cartaCompromiso.toUpperCase()+"','---',"+entNac+","+ fechaNacimiento.substring(0, 4)+", null, null, null, "
                    + "0,'FA', null,date(current), '"+curpRaiz.substring(0,16)+"', '"+probem+"' , '"+Extj+"', 'I', null, null, null, 0, '"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute),'"+etnia+"','000' )");
        }

        ArrayList<Map> datosDiscap;
        String cveDis = "";
        if (!discapacidad.equals(""))
        {
            datosDiscap = getCatDiscap(""+cicescini,cveplan);

            for (Map dato : datosDiscap)
                if ((""+dato.get("cveydes")).equals(discapacidad)){
                    cveDis = ""+dato.get("cvedefsuf");
                    break;
                }
            if (cveDis.equals(""))
                throw new SICEEO_Excepcion (0,"DISCAPACIDAD");
        }

        // chekar si esta dado de baja en en ciclo actual
        // o si es de la 59
        ArrayList<Map> QSusEstudR;        
        QSusEstudR = susEstudR(1,idalu,""+cicescini);        
        if ( QSusEstudR.size() == 1 && (QSusEstudR.get(0).get("estatusgrado").equals("BD") ) ) //11/03/2016
        {
            stm.execute("INSERT INTO movimientosAlumno(idalu, cicescini, cicescfin, fecmov, estatusag, cctant, camcct, usuario, fecha, hora) "
                    + "values ("+idalu+","+cicescini+","+(cicescini+1)+",date(current),'TN', '"+QSusEstudR.get(0).get("cct")+"', '"+tblPrincipal_cct+"', "
                    + "'"+usuario.toUpperCase()+"', date(current), extend(current, hour to minute) )");
                   
            stm.execute("DELETE AlumnoMATERIAS WHERE idalu="+idalu+" AND cicescini="+cicescini);
            stm.execute("DELETE AlumnoGRADO WHERE idalu="+idalu+" AND cicescini="+cicescini);
        }
        
        //25 de mayo de 2016----------------------------------------------------------------------
        if ( !(""+QBuskAlum_estatusalu).trim().equals("I") || (cveplan.equals("3") && existeUno.equals("ok") && (""+QBuskAlum_curp).trim().length()==16 && curpRaiz.length()==18) )
            stm.execute("UPDATE alumno SET estatusalu='I', curp='"+curpRaiz+"', usuario='"+usuario.toUpperCase()+"', fecha=date(current), cvelengua='"+lengua+"',afromexicana='"+etnia+"',"
                        + "hora=extend(current, hour to minute)"
                    + "WHERE idalu = "+idalu);
        

        calif = (cveplan.equals("3"))? "10.0":"0.0";
        stm.execute("INSERT INTO AlumnoGrado values ("+ idalu+", "+cicescini+" ,"+(cicescini+1)+" ,"+ grado+", 0, 0, 0, "                   //   /---El 1 en peso, será un distintivo para indicar que el ingreso fue por SiCEEO
                    + "'"+cveturno+"', "+idcct+", 'INS','I',"+cveplan+",'"+cveprograma+"','"+grupo+"', "+ "'SIN REGISTRAR', '00000', null, null, 1, 0.00, "+edad+", "
                    + "0.0, "+calif+",null, null,'P','NA','N', 1,'E', 0, 0, 0, null, null, null, '"+cveDis+"', '"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute),TO_DATE('"+fechaIng+"','%Y/%m/%d'))" );
        updateGposAdd(""+cicescini, idcct, ""+grado, grupo);
        if (!cveplan.equals("3"))
        {
            if (cveplan.equals("2"))
                //qurySec = " AND (CVETIPMAT='CBA' OR CVETIPMAT='OPC' OR (CVETIPMAT='AES' AND CVEMAT='040') OR (CVETIPMAT='ART' AND CVEMAT='"+arte.substring(0,3)+"') OR CVETIPMAT='LEX' OR (CVETIPMAT='EDT' AND CVEMAT='"+taller.substring(0,4)+"'))";
                //qurySec = " AND (CVETIPMAT='FA' OR CVETIPMAT='DPS' OR (CVETIPMAT='ART' AND CVEMAT= '555') OR (CVETIPMAT IN ('EDT','TEC') AND CVEMAT='"+taller.substring(0,4)+"')) ";
                qurySec = " AND (SUBSTR(CVETIPMAT,1,2)='CF' OR (CVETIPMAT='ART' AND CVEMAT='"+arte.substring(0,3)+"') OR (CVETIPMAT IN ('EDT','TEC') AND CVEMAT='"+taller.substring(0,4)+"')) ";  //Para ciclo 2023
            stm.execute("INSERT INTO AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu, cicescini, cicescfin, grado,  grupotaller, promedio, estatmat, califant, repetidor, repecont, usuario, fecha, hora) "
                + "SELECT cvetipmat, cvemat, cveplan, cveprograma, "+idalu+" AS IDALU, "+cicescini+" AS cicescini, "+ (cicescini+1)+" AS cicescfin, grado,'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, 0 AS califant, 'E' AS repetidor, "
                    + "0 AS repecont, '"+usuario.toUpperCase()+"' AS USUARIO, date(current) AS fecha, extend(current, hour to minute) AS hora "
                + "FROM ESQUEMAMATERIAS "
                + "WHERE CVEPLAN= "+ cveplan+" AND GRADO = "+ grado+" AND CVEPROGRAMA = '"+cveprograma+"' AND estatus= 'A' "
                + "AND CICESCINI <= " + cicescini + " AND CICESCFIN >= " + cicescini
                + qurySec);
        }                
    }
    
    
    public void insertarAlumnoPreescolar_ (String alum_Estatus, String idalu, String cveentidad, String idcct, String discapacidad, String newIdTutor, String curpRaiz, String nombre, String ape1, String ape2, 
            String fechaNacimiento, String sexo, String cartaCompromiso, String entNac, String probem, String Extj, int cicescini, int grado, String cveturno, String cveplan, String cveprograma, String grupo, 
            int edad, String cveDis, String usuario, String taller, String arte) throws SQLException
    {
        //try{
            if ( alum_Estatus.equals("I") )
                stm.execute("INSERT INTO Alumno VALUES ("+idalu+", "+cveentidad+", null, 1,"+idcct+", 'I','MEX','"+(discapacidad.equals("")?"":discapacidad.substring(2,5))+"','01','ESP','A', "+newIdTutor+", "
                        + "'"+curpRaiz+"', \""+nombre.trim()+"\", \""+ape1.trim()+"\", \""+ape2.trim()+"\","
                        +" TO_DATE('"+fechaNacimiento+"','%Y/%m/%d'),  "
                        + "'"+sexo+"','"+cartaCompromiso.toUpperCase()+"','---',"+entNac+","+ fechaNacimiento.substring(0, 4)+", null, null, null, "
                        + "0,'FA', null,date(current), '"+curpRaiz.substring(0,16)+"', '"+probem+"' , '"+Extj+"', 'I',null,null,null, 0, '"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))");

            //              Dm.Q_Alum.SQL.Add('null,'+#39+#39+','+#39+#39+', 0)' );
              
            cveDis = "";
            /*if ( !discapacidad.equals("") )
                  cveDis = dm2.q_CatDiscapcvedefsuf.AsString
			  
              //chekar si esta dado de baja en en ciclo actual
              //o si es de la 59
              dm.Q_susEstudR.Close;
              dm.Q_susEstudR.MacroByName('condicion').AsString :='a.idcct = e.idcct AND a.idalu ='+v_idalu+' AND a.cicescini='+intToStr(v_cicescini) ;
              dm.Q_susEstudR.Open;

              if (dm.Q_susEstudR.RecordCount= 1)  AND
                 (
                 (dm.Q_susEstudRestatusgrado.AsString='BD') or
                 (copy(dm.Q_susEstudRgrupo.AsString,2,1)='_')
                 )
                 THEN

                  begin
                   dm.Q_Mat_cicAnt_U.SQL.Clear;
                   dm.Q_Mat_cicAnt_U.SQL.Add('delete AlumnoMATERIAS ');
                   dm.Q_Mat_cicAnt_U.SQL.Add('WHERE   idalu = '+V_Idalu );
                   dm.Q_Mat_cicAnt_U.SQL.Add('AND cicescini = '+ inttostr(v_cicescini));
                   dm.Q_Mat_cicAnt_U.ExecSQL;

                   dm.Q_Tutor.SQL.Clear;
                   dm.Q_tutor.SQL.Add('delete AlumnoGRADO ');
                   dm.Q_tutor.SQL.Add('WHERE   idalu = '+v_idalu );
                   dm.Q_tutor.SQL.Add('AND cicescini = '+ inttostr(v_cicescini));
                   dm.Q_tutor.ExecSQL;


                  end;

            Dm.Q_AlumGdo.SQL.Add('INSERT INTO AlumnoGrado values ('+ v_idalu+', '+inttostr(V_cicescini)+' ,'+inttostr(V_cicescini+1)+' ,');
            Dm.Q_AlumGdo.SQL.Add(Dm.Q_Principalgrado.AsString+', 0, 0, 0,');
            Dm.Q_AlumGdo.SQL.Add(#39+Dm.Q_Principalcveturno.AsString+#39+','+Dm.Q_Principalidcct.AsString+','+#39+'INS'+#39+','+#39+'I'+#39+',');
            Dm.Q_AlumGdo.SQL.Add(Dm.Q_Principalcveplan.AsString+','+#39+Dm.Q_Principalcveprograma.AsString+#39+' ,'+#39+Dm.Q_Principalgrupo.AsString+#39+',');
            Dm.Q_AlumGdo.SQL.Add(#39+'SIN REGISTRAR'+#39+','+#39+'00000'+#39+', NULL, NULL,');
            Dm.Q_AlumGdo.SQL.Add(' 0 , 0.00,'+intToStr(Edad(E_Edad.text))+', 0.0,10.0, null, null, '+#39+'P'+#39+',');
            Dm.Q_AlumGdo.SQL.Add(#39+'NA'+#39+','+#39+'N'+#39+','+' 1,'+#39+'E'+#39+', 0, 0, 0, NULL, NULL, '); // NULL, NULL )');
            Dm.Q_AlumGdo.SQL.Add('null, "'+v_cveDis+'" ,' );
            Dm.Q_AlumGdo.SQL.Add('"'+UPPERCASE(f_password.E_Usuario.Text)+'",' );
            dm.Q_AlumGdo.SQL.Add('date(current) , extend(current, hour to minute) )');*/
        //    conn.commit();
        //}catch (SQLException ex){
        //    conn.rollback();
        //    throw new SQLException (""+ex.getMessage());
        //}finally { conn.setAutoCommit(true);}
    }
    
    public void insertarTutor(String idtutor,String curp, String txtPrimerApe, String txtSegundoApe, String txtNombre, String txtTelefono) throws SQLException
    {            
        stm.execute("INSERT INTO tutor(idtutor,cvemunicipio,cvelocalidad,cveentidad,nombre,apepat,apemat,telefono,curp, cveparent)"
                + " VALUES ("+idtutor+",'67','1','20','"+txtNombre+"','"+txtPrimerApe+"','"+txtSegundoApe+"','"+txtTelefono+"','"+curp+"','000')");                    
    }
    public void actualizarAlumnoTutor(String idtutor, String idalu, String cicescini, String cveparent, String Usuario) throws SQLException
    {                
        rs = stm.executeQuery("SELECT * FROM alumnotutor WHERE idalu="+idalu+" AND cicescini="+cicescini);
        if(rs.next())
            stm.execute("UPDATE alumnotutor SET "
                    + "idtutor = " + idtutor+", "
                    + "cveparent = '"+cveparent+"', "
                    + "fecha = date(current), "
                    + "hora = extend(current, hour to minute) "        
                + "WHERE idalu = " + idalu +" AND cicescini="+cicescini);
        else
            stm.execute("INSERT INTO alumnotutor (idalu,idtutor,cveparent,cicescini,usuario,fecha,hora)"
                + "VALUES ("+idalu +","+idtutor+",'"+cveparent+"',"+cicescini+",'"+Usuario+"',date(current), extend(current, hour to minute))");
    }
    public void actualizarTutor(String cicescini, String idtutor, String cveparent, String txtPrimerApe, String txtSegundoApe, String txtNombre, String txtTelefono) throws SQLException{
        stm.execute("UPDATE tutor SET "
                + "cveparent='"+cveparent+"', "
                + "apepat='"+txtPrimerApe+"', "
                + "apemat='"+txtSegundoApe+"', "
                + "nombre='"+txtNombre+"', "
                + "telefono='"+txtTelefono+"' "
                + "WHERE idtutor="+idtutor);
    }
        
    private ArrayList<Map> susEstudR (int caso, String idalu, String cicescini) throws SQLException
    {
        String condicion="";
        if (caso==1)
            condicion = "a.idcct = e.idcct AND a.idalu ="+idalu+" AND a.cicescini="+cicescini;
                               
        rs = stm.executeQuery("SELECT a.idalu, a.idcct, e.cct, e.cveplan, e.cvezona, e.cveunidad, a.cicescini, grado, grupo, estatusgrado, a.cveturno,promedioant, Promedio, PromedioGral " +
                                   "FROM alumnoGrado a, Escuela e " +
                                   "WHERE "+ condicion + " " +
                                   "ORDER BY a.cicescini");
        return qryToArrlmap(rs, null, true, 1);
    }
    
    public String getDatoEscuela(String nombreCampo,String idcct) throws SQLException{
        return getData("SELECT "+nombreCampo+ " FROM escuela WHERE idcct="+idcct);
    }
    
    public String getModalidad(String idcct) throws SQLException{
        return getData("SELECT modalidad FROM escuela WHERE idcct="+idcct);
    }
        
    public int QFolioIdalu (String idalu, String cveplan) throws SQLException
    {
        if(cveplan.equals("GPO") || cveplan.equals("null"))  //agregue segunda condicion 23 de sep 2020
            rs = stm.executeQuery("SELECT count(*) AS cantidad FROM folios_impre WHERE idalu="+idalu+" AND estatus='AC' ");
        else
            rs = stm.executeQuery("SELECT count(*) AS cantidad FROM folios_impre WHERE idalu="+idalu+" AND cveplan="+cveplan+" AND estatus='AC' ");
        
        if (rs.next())
            return rs.getInt("cantidad");
        return 0;
    }
    
//************************************************************************************************************
//****************************** MÉTODOS USADOS EN EL MÓDULO NuevoIngreso Primaria ****************************
//************************************************************************************************************
    /*public void insertarAlumnoPrimaria (String alum_Estatus, String idalu, String idcct, String discapacidad, String curpRaiz, String nombre, String primerApe, String segundoApe, String fechaNacimiento, String sexo, String cartaCompromiso, String entNac, String probem, String Extj, 
            int cicescini, int grado, String cveturno, String cveplan, String cveprograma, String grupo, int edad, String cveDis, String usuario) throws SQLException
    {
        try
        {
            if (alum_Estatus.equals("O"))
                rs = stm.executeQuery("UPDATE alumno SET idcct = "+idcct+",estatusAlu = 'I' WHERE idalu = "+idalu);
            else if (alum_Estatus.equals("I")){
                stm.execute("INSERT INTO Alumno values ("+idalu+", 20 , null, 1,"+idcct+", 'I','MEX','"+discapacidad.substring(2,5)+"','01','ESP','A', 1, "
                        + "'"+curpRaiz+"', \""+nombre.trim()+"\",\""+primerApe.trim()+"\",\""+segundoApe.trim()+"\", "
                        + "'"+fechaNacimiento.substring(5, 7)+"-"+fechaNacimiento.substring(8, 10)+"-"+fechaNacimiento.substring(0, 4)+"', "
                        + "'"+sexo+"','"+cartaCompromiso.toUpperCase()+"','---',"+entNac+","+ fechaNacimiento.substring(0, 4)+", null, null, null, "
                        + "0,'FA', null,date(current), '"+curpRaiz.substring(0,16)+"', '"+probem+"' , '"+Extj+"', 'I',null,'','', 0)");
            }

            stm.execute("INSERT INTO AlumnoGrado values ("+ idalu+", "+cicescini+" ,"+(cicescini+1)+" ,"+ grado+", 0, 0, 0, "
                    + "'"+cveturno+"', "+idcct+", 'INS','I',"+cveplan+",'"+cveprograma+"','"+grupo+"', "+ "'SIN REGISTRAR', '00000', NULL, NULL, 0, 0.00, "+edad+", "
                        + "0.0,0.0, null,'P','NA','N', 1,'E', 0, 0, 0, NULL, NULL, NULL, '"+cveDis+"')" );
                    //    -----/

            stm.execute("INSERT INTO AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu, cicescini, cicescfin, grado,  grupotaller, promedio, estatmat, califant, repetidor, repecont, usuario, fecha, hora) "
                + "SELECT cvetipmat, cvemat, cveplan, cveprograma, "+idalu+" AS IDALU, "+cicescini+" AS cicescini, "+ (cicescini+1)+" AS cicescfin, grado,'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, 0 AS califant, 'E' AS repetidor, "
                    + "0 AS repecont, '"+usuario.toUpperCase()+"' AS USUARIO, date(current) AS fecha, extend(current, hour to minute) AS hora' "
                + "FROM ESQUEMAMATERIAS "
                + "WHERE CVEPLAN= "+ cveplan+" AND GRADO = "+ grado+" AND CVEPROGRAMA = '"+cveprograma+"' "
                + " AND CICESCINI <= "+ cicescini+" AND CICESCFIN >= "+ cicescini);
            
            conn.commit();
        }catch (SQLException ex){
            conn.rollback();
            throw new SQLException (""+ex.getMessage());
        }finally { conn.setAutoCommit(true);}
    }*/
    
    public Map getAlumIn3Pre (String idalu, String cicescini) throws SQLException
    {
        Map datos = new HashMap();
        rs = stm.executeQuery("SELECT g.idalu,g.cicescini,g.cveplan,g.grado "
                + "FROM alumnogrado g "
                + "WHERE g.idalu = "+idalu+" AND g.cicescini = "+cicescini+" AND g.cveplan=3 AND grado=3");
        if (rs.next()){
            datos.put("cveplan", rs.getString("cveplan"));
        }
        return datos;
    }
    
    
    
//************************************************************************************************************
//***************************** MÉTODOS USADOS EN EL MÓDULO NuevoIngreso Secundaria ***************************
//************************************************************************************************************    
    //Este método es igual al insertarAlumnoPri
    /*public void insertarAlumnoSecundaria (String alum_Estatus, String idalu, String idcct, String discapacidad, String curpRaiz, String nombre, String primerApe, String segundoApe, String fechaNacimiento, String sexo, String cartaCompromiso, String entNac, String probem, String Extj, 
            int cicescini, int grado, String cveturno, String cveplan, String cveprograma, String grupo, int edad, String cveDis, String usuario) throws SQLException
    {
        try
        {
            if (alum_Estatus.equals("O"))
                rs = stm.executeQuery("UPDATE alumno SET idcct = "+idcct+",estatusAlu = 'I' WHERE idalu = "+idalu);
            else if (alum_Estatus.equals("I")){
                stm.execute("INSERT INTO Alumno values ("+idalu+", "+entNac+" , null, 1,"+idcct+", 'I','MEX','"+discapacidad.substring(2,5)+"','01','ESP','A', 1, "
                        + "'"+curpRaiz+"', \""+nombre.trim()+"\",\""+primerApe.trim()+"\",\""+segundoApe.trim()+"\", "
                        + "'"+fechaNacimiento.substring(5, 7)+"-"+fechaNacimiento.substring(8, 10)+"-"+fechaNacimiento.substring(0, 4)+"', "
                        + "'"+sexo+"','"+cartaCompromiso.toUpperCase()+"','---',"+entNac+","+ fechaNacimiento.substring(0, 4)+", null, null, null, "
                        + "0,'FA', null,date(current), '"+curpRaiz.substring(0,16)+"', '"+probem+"' , '"+Extj+"', 'I',null,'','', 0)");
            }

            stm.execute("INSERT INTO AlumnoGrado values ("+ idalu+", "+cicescini+" ,"+(cicescini+1)+" ,"+ grado+", 0, 0, 0, "
                    + "'"+cveturno+"', "+idcct+", 'INS','I',"+cveplan+",'"+cveprograma+"','"+grupo+"', "+ "'SIN REGISTRAR', '00000', NULL, NULL, 0, 0.00, "+edad+", "
                        + "0.0,0.0, null,'P','NA','N', 1,'E', 0, 0, 0, NULL, NULL, NULL, '"+cveDis+"')" );

            stm.execute("INSERT INTO AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu, cicescini, cicescfin, grado,  grupotaller, promedio, estatmat, califant, repetidor, repecont, usuario, fecha, hora) "
                + "SELECT cvetipmat, cvemat, cveplan, cveprograma, "+idalu+" AS IDALU, "+cicescini+" AS cicescini, "+ (cicescini+1)+" AS cicescfin, grado,'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, 0 AS califant, 'E' AS repetidor, "
                    + "0 AS repecont, '"+usuario.toUpperCase()+"' AS USUARIO, date(current) AS fecha, extend(current, hour to minute) AS hora' "
                + "FROM ESQUEMAMATERIAS "
                + "WHERE CVEPLAN= "+ cveplan+" AND GRADO = "+ grado+" AND CVEPROGRAMA = '"+cveprograma+"' "
                + " AND CICESCINI <= "+ cicescini+" AND CICESCFIN >= "+ cicescini);
            
            conn.commit();
        }catch (SQLException ex){
            conn.rollback();
            throw new SQLException (""+ex.getMessage());
        }finally { conn.setAutoCommit(true);}
    }*/
    
    public ArrayList<Map> getCatArtes (String cveprograma, int grado, String cicescini) throws SQLException
    {
        ArrayList<Map> datos = new ArrayList<Map>();
        Map fila;
        String tmp;
        rs = stm.executeQuery("SELECT cveplan, cvetipmat, cvemat, ordenimpres , (SELECT desmat FROM materias WHERE cvemat = esquemamaterias.cvemat AND cvetipmat = esquemamaterias.cvetipmat) AS desmat "
                + "FROM ESQUEMAMATERIAS "
                + "WHERE CVETIPMAT='ART' AND CVEPROGRAMA = '"+cveprograma+"' AND GRADO="+grado+" AND cicescini <="+cicescini+" AND cicescfin >= "+cicescini);
        while (rs.next()){
            fila = new HashMap();
            fila.put("cvemat",((tmp=rs.getString("cvemat"))!=null)?tmp.trim():"");
            fila.put("desmat",((tmp=rs.getString("desmat"))!=null)?tmp.trim():"");
            datos.add(fila);
        }
        return datos;
    }
    
    public ArrayList<Map> getTalleres (String cicescini, String cveprograma, int grado) throws SQLException
    {
        /*rs = stm.executeQuery("SELECT DISTINCT idcct, cveturno, cicescini, cicescfin, grado, 'x' AS grupo, cvetipmat, cvemat, 0 AS grupocap, 0 AS gruposal, 'A' estatus , "
                + "(SELECT desmat FROM materias WHERE cvemat = escuelagrupos.cvemat AND cvetipmat = escuelagrupos.cvetipmat) AS desmat "
                + "FROM escuelaGrupos "
                + "WHERE cicescini = "+cicescini+" AND idcct = "+idcct+" AND grado = "+grado+" AND cvetipmat ='EDT'  "
                + "ORDER BY cvemat");*/
        rs = stm.executeQuery("SELECT distinct m.cvemat, m.desmat " 
                + " FROM esquemamaterias AS em, materias m " 
                + " WHERE em.cicescini <= " + cicescini 
                + " AND em.cicescfin >= " + cicescini
                + " AND em.cvetipmat IN ('EDT','TEC') " 
                + " AND em.cvemat = m.cvemat " 
                + " AND em.cvetipmat = m.cvetipmat " 
                + " AND cveprograma = '" + cveprograma + "' " 
                + " AND estatus = 'A' " 
                + " AND grado = " + grado
                + " AND cveplan = 2");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public Map getAlumEnCicloActual (String idalu, String cicesciniAct) throws SQLException
    {
        rs = stm.executeQuery("SELECT g.idalu,g.cveplan,g.cveprograma,g.grado,g.grupo,g.cvedefsuf,g.cvetipdefsuf,e.cct,e.cveturno "
                + "FROM alumnogrado g, escuela e WHERE g.idcct = e.idcct AND g.idalu = "+idalu+" AND g.cicescini = "+cicesciniAct +" "
                + "ORDER BY g.idalu");
        return qryToMap(rs, null, false, 0);
    }
    
    public Map getAlumIn6Prim (String idalu, String cicescini) throws SQLException
    {
        Map datos = new HashMap();
        rs = stm.executeQuery("SELECT g.idalu,g.cicescini,g.cveplan,g.grado "
                + "FROM alumnogrado g "
                + "WHERE g.idalu = "+idalu+" AND g.cicescini = "+cicescini+" AND g.cveplan=1 AND grado=6");
        if (rs.next()){
            datos.put("cveplan", rs.getString("cveplan"));
        }
        return datos;
    }
    
    public Map verificaHistorialPrim(String idalu,String cicescini) throws SQLException
    {
        Map historial = new HashMap();
        historial.put("band", 0);
        String strQuery = "SELECT NVL((CASE " 
                + " WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu = g.idalu AND gr.estatusgrado ='RE' AND gr.cveplan=1 AND gr.grado=1) > 1 " 
                    + " THEN -11.0 " 
                + " ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado ='RE' AND gp.cveplan=1 AND gp.grado=1) "
                    + " END ),-11.0) AS Promd1roP, " 
                + " NVL((CASE " 
                + " WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu=g.idalu AND gr.estatusgrado='RE' AND gr.cveplan=1 AND gr.grado=2) > 1 " 
                    + " THEN  -11.0 " 
                + " ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado='RE' AND gp.cveplan=1 AND gp.grado=2) " 
                    + " END  ),-11.0) AS Promd2doP, " 
                + " NVL((CASE " 
                + " WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu = g.idalu AND   gr.estatusgrado ='RE' AND   gr.cveplan=1 AND   gr.grado  =3) > 1 "
                    + " THEN  -11.0 " 
                + " ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND   gp.estatusgrado ='RE' AND   gp.cveplan=1 AND   gp.grado  =3) " 
                    + " END ),-11.0) AS Promd3roP," 
                + " NVL((CASE " 
                + " WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu = g.idalu AND gr.estatusgrado ='RE' AND gr.cveplan=1 AND gr.grado=4) > 1 " 
                    + " THEN  -11.0 " 
                + " ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado='RE' AND gp.cveplan=1 AND gp.grado=4) " 
                    + " END ),-11.0) AS Promd4toP, " 
                + " NVL((CASE " 
                + " WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu=g.idalu AND gr.estatusgrado='RE' AND gr.cveplan=1 AND gr.grado=5) > 1 "
                    + " THEN  -11.0 " 
                + " ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado ='RE' AND gp.cveplan=1 AND gp.grado  =5) "
                    + " END ),-11.0) AS Promd5toP "     
            + " FROM alumnogrado g, alumno a " 
            + " WHERE g.idalu=a.idalu "
            + " AND g.cveplan = 1 " 
            + " AND g.estatusGrado <> 'I' ";
        
        try {
            rs = stm.executeQuery ( strQuery
            + " AND g.cicescini  = (SELECT max(cicescini) FROM alumnogrado WHERE idalu = "+idalu+" AND cveplan = 1) "
            + " AND g.idalu = " + idalu );

            if (rs.next()) {
                historial.put("Promd1roP",rs.getString("Promd1roP"));
                historial.put("Promd2doP",rs.getString("Promd2doP"));
                historial.put("Promd3roP",rs.getString("Promd3roP"));
                historial.put("Promd4toP",rs.getString("Promd4toP"));
                historial.put("Promd5toP",rs.getString("Promd5toP"));
                historial.put("band",1);
            }            
        } catch (SQLException ex){
            throw new SQLException(ex);
        }
        return historial;
    }
    
    public void actualiza2ultDigCurp (String txtCurp17y18, String idcct, String usuario, String idalu) throws SQLException
    {
        stm.execute("UPDATE alumno SET curp = trim(curp)||'"+txtCurp17y18.trim().toUpperCase()+"', idcct="+idcct+", usuario='"+usuario.toUpperCase()+"', fecha = date(current) , hora = extend(current, hour to minute) WHERE idalu="+idalu);
    }
    
    public void setIdcctYEstatusalu_Alumno (String tblPrincipal_idcct, String txtUsuario, String idalu) throws SQLException
    {
        stm.execute("UPDATE alumno SET idcct = "+tblPrincipal_idcct+", estatusAlu = 'I', usuario = '"+txtUsuario.trim().toUpperCase()+"', fecha = date(current) , hora = extend(current, hour to minute) "
                + "WHERE idalu = "+idalu);
        
    }
    
    public void actualizaTablaidalus_con_O (String idalu, String idcct) throws SQLException
    {
        stm.execute("UPDATE idalus SET estatusalu='O' WHERE idalu="+idalu+" AND idcct="+idcct);
    }
    
    public void actualizaTablaParametros (String idalu) throws SQLException
    {
        stm.execute("UPDATE parametros SET IdAluAct="+idalu);
    }
    
    // Actualizar el idtutor de tabla parametros
    public void actualizaTablaParametrosT (String idtutor) throws SQLException
    {
        stm.execute("UPDATE parametros SET IdTutorAct="+idtutor);
    }
    
    //Obtiene el maximo en la tabla Alumno
    public String getMaximosA () throws SQLException
    {
        String max = getData ("SELECT Max(idalu)+1 AS maxlibrea FROM Alumno");
        return ""+(Integer.parseInt(max)+1);
    }
    
    //Obtiene el maximo idTutor en la tabla Alumno
    public String getMaximosT () throws SQLException
    {
        String max = getData ("SELECT Max(idtutor)+1 AS maxlibrep FROM tutor");
        return ""+max;
    }
    
    //Obtiene el maximo en la tabla Parametros
    public Map getMaximosP () throws SQLException
    {
        Map maximosP = new HashMap();
        rs = stm.executeQuery("SELECT IDTUTORACT+1 AS maxlibret,  IDaluACT+1 AS maxlibrea FROM PARAMETROS");
        if (rs.next()){
            maximosP.put("maxlibret",rs.getInt("maxlibret"));
            maximosP.put("maxlibrea",rs.getInt("maxlibrea"));
        }
        return maximosP;
    }
    
    public Map yNoEnCicloAct (String idalu, String cicesciniAct) throws SQLException
    {
        rs = stm.executeQuery("SELECT g.idalu, g.cicescini, g.cveplan, g.cveprograma, g.grado, g.grupo, g.cvedefsuf, g.cvetipdefsuf, e.cct, e.cveturno "
                + "FROM alumnogrado g, escuela e "
                + "WHERE g.idcct=e.idcct AND g.idalu="+idalu+" AND g.cicescini="+cicesciniAct+" "
                + "ORDER BY g.idalu");
        return qryToMap(rs, null, true, 1);
    }
    
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO Historial **********************************
//************************************************************************************************************
    //Tabla para mostrar los certificados que se le han impreso
    public ArrayList<Object[]> getSuFolio (String idalu)throws SQLException
    {
        ArrayList<Object[]> tabla = new ArrayList<Object[]>();
        Object []fila; //usuario, fecha, hora
        rs = stm.executeQuery("SELECT cveunidad, cicescinilib, SUBSTR(foliolet,2,1) AS folioletra, folionum, cct, grupo, curp, (TRIM(NVL(apepat,'')) || '/' || TRIM(NVL(apemat,'')) || '*' || TRIM(NVL(nombre,''))) AS nom_tot, usuario, fecha, hora "
                + " FROM folios_impre "
                + " WHERE idalu = "+idalu
                + " AND estatus='AC' ");
        while (rs.next()){
            fila = new Object[11];
            fila[0]=rs.getString("cveunidad");
            fila[1]=rs.getString("cicescinilib");
            fila[2]=rs.getString("folioletra");
            fila[3]=rs.getString("folionum");
            fila[4]=rs.getString("cct");
            fila[5]=rs.getString("grupo");
            fila[6]=rs.getString("curp");
            fila[7]=rs.getString("nom_tot");
            fila[8]=rs.getString("usuario");
            fila[9]=rs.getString("fecha");
            fila[10]=rs.getString("hora");
            tabla.add(fila);
        }
        return tabla;
    }
    
    //Tabla para mostrar sus materias
    public ArrayList<Object[]> getSusMat (String idalu, String cicescini) throws SQLException
    {
        ArrayList<Object[]> tabla = new ArrayList<Object[]>();
        Object []fila;
        rs = stm.executeQuery("SELECT  m.cveprograma, m.cvetipmat, m.cvemat, e.desmat, m.promedio "
                            + "FROM alumnomaterias m,  materias e "
                            + "WHERE  m.cvemat = e.cvemat AND m.cvetipmat = e.cvetipmat AND cicescini= '"+cicescini+"' AND idalu = "+idalu);
        while (rs.next()){
            fila = new Object[5];
            fila[0]=rs.getString("cveprograma");
            fila[1]=rs.getString("cvetipmat");
            fila[2]=rs.getString("cvemat");
            fila[3]=rs.getString("desmat");
            fila[4]=rs.getString("promedio");
            tabla.add(fila);
        }
        return tabla;
    }
    
    public void setMatCicAnt (String grupo, String idalu, String cicescini) throws SQLException
    {
        stm.execute("UPDATE AlumnoGRADO SET grupo= '"+grupo+"' WHERE idalu ="+idalu+" AND cicescini = "+cicescini);
    }
    
    public ArrayList<Map> getEscu (String cct, String cveturno, String cveunidad, String cveplan) throws SQLException
    {
        ArrayList<Map> datos = new ArrayList<Map>();
        Map fila;
        rs = stm.executeQuery("SELECT idcct, cvezona, cveprograma "
                            + "FROM escuela "
                            + "WHERE cct = '"+cct+"' AND cveturno = '"+cveturno+"' AND cveunidad = '"+cveunidad+"' AND cveplan= "+cveplan);
        if (rs.next()){
            fila = new HashMap();
            fila.put("idcct",rs.getString("idcct"));
            fila.put("cvezona",rs.getString("cvezona"));
            fila.put("cveprograma",rs.getString("cveprograma"));
            datos.add(fila);
        }
        return datos;
    }
    
    public ArrayList<Map> getAlumOrig (String cicescini, String idcct, String grado) throws SQLException
    {
        ArrayList<Map> datos = new ArrayList<Map>();
        Map fila;
        rs = stm.executeQuery("SELECT cveplan, cveprograma "
                            + "FROM planescuela "
                            + "WHERE cicescini="+cicescini+" AND idcct = "+idcct+" AND Grado= "+grado);
        while (rs.next()){
            fila = new HashMap();
            fila.put("cveplan",rs.getString("cveplan"));
            fila.put("cveprograma",rs.getString("cveprograma"));
            datos.add(fila);
        }
        
        return datos;
    }
    
    public void cambiarDeEscuela (String buskAlumIdalu, String califCicescin, String buskAlumCurp, String buskAlumFecnac, String susEstudCct, String txtCct, String buskAlumNombre, String buskAlumApepat,String buskAlumApemat, String txtCveturno, String cbxGrupo, String susEstudGrado, String usuario, String cicescin, String escuIdcct, String cveprograma, String susEstudCveplan, String cveopc, String cvetaller) throws SQLException
    {
        stm.execute("INSERT INTO movimientosAlumno values ("+buskAlumIdalu+","+califCicescin+","+(Integer.parseInt(califCicescin)+1)+",date(current),'TN', '"+buskAlumCurp+"', "
                + " TO_DATE('"+buskAlumFecnac+"','%Y-%m-%d'),  '"+susEstudCct+"', '"+txtCct+"', \""+buskAlumNombre+"\",\""+buskAlumApepat+"\",\""+buskAlumApemat+"\", '"+txtCveturno+"', '"+cbxGrupo+"', "+susEstudGrado+", '"
                + usuario.toUpperCase()+"',date(current), extend(current, hour to minute), 'G')");
        stm.execute("DELETE AlumnoMATERIAS WHERE idalu = "+buskAlumIdalu+" AND cicescini = "+ califCicescin);

        stm.execute("UPDATE alumnoGrado SET estatusgrado = '"+(califCicescin.equals(cicescin)?"I":"RE")+"', idcct= "+escuIdcct+", cveturno='"+txtCveturno+"', cveprograma='"+cveprograma+"', grupo='"+cbxGrupo+"', cveINSCRIP = 'TRN' WHERE cicescini = "+califCicescin+" AND idalu="+buskAlumIdalu);

        stm.execute("INSERT into AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu, cicescini, cicescfin, grado,  grupotaller, promedio, estatmat, califant, repetidor, repecont, usuario, fecha, hora ) "
                + " SELECT cvetipmat, cvemat, cveplan, cveprograma, "+buskAlumIdalu+" AS IDALU, "+califCicescin+" AS cicescini, "+(Integer.parseInt(califCicescin)+1)+" AS cicescfin, grado, "
                + "'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, 0 AS califant, 'E' AS repetidor, 0 AS repecont, '"+usuario.toUpperCase()+"' AS USUARIO, date(current) AS fecha, extend(current, hour to minute) AS hora "
                + "FROM ESQUEMAMATERIAS "
                + "WHERE CVEPLAN= "+susEstudCveplan+" AND  GRADO = " + susEstudGrado+" AND CVEPROGRAMA = '"+cveprograma+"' AND  CICESCINI  <= "+ califCicescin+" AND  CICESCFIN  >= "+ califCicescin+" "
                + "AND (CVETIPMAT='CBA' OR (CVETIPMAT='AES' AND CVEMAT='040') OR CVETIPMAT='LEX' OR (CVETIPMAT='ART' AND CVEMAT='555') OR (CVETIPMAT= 'OPC' AND CVEMAT= '"+cveopc+"') OR (CVETIPMAT IN ('EDT','TEC') AND CVEMAT='"+cvetaller+"'))");

        if (califCicescin.equals(cicescin))
            stm.execute("UPDATE alumno SET estatusalu = 'I',idcct='"+escuIdcct+"' WHERE idalu ="+buskAlumIdalu);

    }
    
    public void setEstatusAluAInscrito (String idcct, String idalu, String cicescini, String estatusGrado) throws SQLException
    {
        stm.execute("UPDATE alumno SET estatusalu='I', idcct="+idcct+" WHERE idalu = "+idalu);
        if ((estatusGrado.equals("BD") || estatusGrado.equals("BD")))
            stm.execute("UPDATE alumnoGrado SET estatusgrado = 'I' WHERE cicescini = "+cicescini+" AND idalu = "+idalu);
    }
    
    public void setEstatusAluABaja (String idcct, String idalu, String cicescini, String estatusGrado, String grado, String grupo, String usuario) throws SQLException
    {
        stm.execute("UPDATE alumno SET estatusalu='BD', idcct="+idcct+" WHERE idalu = "+idalu);
        if (!estatusGrado.equals("C"))
            stm.execute("UPDATE alumnoGrado SET estatusgrado = 'BD' WHERE cicescini = "+cicescini+" AND idalu = "+idalu);
        stm.execute("INSERT INTO bajasvit values ("+idalu+", "+idcct+", "+grado+", '"+grupo+"', '"+usuario.toUpperCase()+"', date(current), extend(current, hour to minute),"+cicescini+")");
    }
    
    public void eliminarAlumno (String idalu) throws SQLException
    {
        stm.execute("DELETE ALUMNOMATERIAS WHERE idalu ="+idalu);
        stm.execute("DELETE AlumnoGrado WHERE idalu ="+idalu);
        stm.execute("DELETE AlumnoGrado2 WHERE IDALU ="+idalu);
        stm.execute("DELETE Alumno2 WHERE IDALU ="+idalu);
        stm.execute("DELETE Alumno WHERE IDALU ="+idalu);
    }
    
    public void cancelarFolio (String usuario, String idalu, String cicescini, String folionum) throws SQLException
    {
        stm.execute("INSERT INTO folios_cance(idfolio, foliolet, folionum, idalu, idcct, cvezona, cveplan, cct, cveturno, grado, grupo, curp,nombre, apepat, apemat, cicescini, cicescinilib, usukgenero, feckgenero, horkgenero, usuario, fecha, hora, cveunidad, crip)"
                + "SELECT idfolio, foliolet, folionum, idalu, idcct, cvezona, cveplan, cct, cveturno, grado, grupo, curp,nombre, apepat, apemat, cicescini, cicescinilib, usuario AS usukgenero, fecha AS feckgenero, hora AS horkgenero ,"
                + "'"+usuario.toUpperCase()+"' AS USUARIO, date(current) AS fecha, extend(current, hour to minute) AS hora, cveunidad, crip "
                + "FROM folios_impre "
                + "WHERE idalu = "+idalu+" AND CICESCINI = "+cicescini+" AND folionum = "+folionum + " AND estatus = 'AC' ");

        stm.execute("DELETE folios_impre WHERE idalu="+idalu+" AND CICESCINI = "+cicescini+" AND folionum = "+folionum + " AND estatus='AC' ");
    }
    
    public ArrayList<Object[]> AGcicAnt (String idalu, int cicAnt) throws SQLException
    {
        ArrayList<Object[]> tabla = new ArrayList<Object[]>();
        Object []fila;
        String tmp;
                                // 0        1          2        3      4           5       6        7         8         9        10        11         12         13        14          15           16
        rs = stm.executeQuery("SELECT idalu, cicescini, cicescfin, grado, idcct, estatusgrado, grupo, cveplan, cveprograma, edad, cveturno, promedioant, promedio, promovido, repetidor, repecont, promediogral "
                        + "FROM alumnoGRADO"
                        + "WHERE idalu = "+idalu+" AND cicescini = "+cicAnt);
        
        while (rs.next()){
            fila = new Object[17];
            for (int i=0; i<17; i++)
                fila[i]=((tmp=rs.getString(i+1))!=null)?tmp.trim():"";
            tabla.add(fila);
        }
        return tabla;
    }
    
    public ArrayList<Object[]> AGcicAct (String idalu, int cicAct) throws SQLException
    {
        ArrayList<Object[]> tabla = new ArrayList<Object[]>();
        Object []fila;
        String tmp;
                               // 0         1         2      3        4          5       6          7             8         9        10           11        12
        rs = stm.executeQuery("SELECT idalu, cicescini, grado, idcct, estatusgrado, grupo, cveplan, cveprograma, promedioant, promedio, promovido, repetidor, repecont "
                + "FROM alumnoGRADO WHERE idalu = "+idalu+" AND cicescini="+cicAct);
        while (rs.next()){
            fila = new Object[13];
            for (int i=0; i<13; i++)
                fila[i]=((tmp=rs.getString(i+1))!=null)?tmp.trim():"";
            tabla.add(fila);
        }
        return tabla;
    }
    
    public void setReprobarCiclo (String idalu, int cicescin, String grado, String cveturno, String idcct, String cveplan, String cveprograma, String grupo, String edad) throws SQLException
    {
        stm.execute("INSERT INTO AlumnoGrado values ("+idalu+", "+cicescin+", "+(cicescin+1)+", "+grado+", 0, 0, 0, '"+cveturno+"', "+idcct+", 'INS', 'I', "+cveplan+", '"+cveprograma+"', '"+grupo+"', "
                +"'SIN REGISTRAR', '00000', null, null,  1 , 0.00, "+edad+", 5.0, 0.0, null, 'P', 'NA', 'N',  1, 'R',  0, 0, 0, null, null, null, null )");
                                                    //   \---El 1 en peso, será un distintivo para indicar que el ingreso fue por SiCEEO
    }
    
//************************************************************************************************************
//********************************* MÉTODOS USADOS EN EL MÓDULO CambioDeGrupo ********************************
//************************************************************************************************************
    public ArrayList<Map> getGpos_Val (String idcct, String grado, String cicescini, boolean filter, String seccion) throws SQLException
    {
        String filtro="";
        /*if (filter) {   // Se comento para que las escuelas vean grupos con guion bajo, sea 22 o 59
            if (seccion.equals("22"))
                filtro = "AND SUBSTR(grupo,2,1)<>'_'";
            // else if (seccion.equals("59"))
            //    filtro = "AND ( SUBSTR(grupo,2,1)='_' OR SUBSTR(grupo,2,1)='__' ) AND LENGTH(TRIM(grupo))>1"; 
        }*/
        
        rs = stm.executeQuery("SELECT distinct idcct, grado, grupo, SUBSTR(grupo,2,1) AS guionbajo "
                    + "FROM escuelagrupos "
                    + "WHERE idcct = "+idcct+" AND grado = "+grado+" AND cicescini="+cicescini+ " "
                    + filtro + " "
                    + "ORDER BY grupo");
        return qryToArrlmap(rs, null, true, 0);
    }
    
    public boolean suFolio (String idalu, String cicesc) throws SQLException
    {
        rs = stm.executeQuery("SELECT cicescini FROM folios_impre WHERE idalu = "+idalu+" AND cicescini="+cicesc + " AND estatus='AC' ");
        return rs.next();
    }
      
    public ArrayList<Map> getCamDgpo (String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        rs = stm.executeQuery("SELECT g.cicescini, g.idcct, a.idalu, curp, "
                    + "(TRIM(NVL(a.apepat,'')) || '/' || TRIM(NVL(a.apemat,'')) || '*' || TRIM(NVL(a.nombre,''))) AS nom_tot, "
                    + "g.grado, g.grupo, g.cveprograma, g.cvedefsuf, (case when (a.cvelengua='ESP' or a.cvelengua is null)then '' else a.cvelengua end) as cvelengua," //Se agrega a opcion de lengua indigena 
                    + "g.grupo AS grupo_oldvalue, " //<--- OJO Este campo fue agregado sólo para SICEEO
                    // + "--(SELECT desdefsuf FROM deficsuficalu WHERE cvedefsuf = a.cvedefsuf ) AS desdefsuf, "
                    + "  (SELECT desdefsuf FROM deficsuficalu WHERE cvedefsuf = g.cvedefsuf AND cveplan= g.cveplan  AND g.cicescini >= cicescini AND g.cicescini <=cicescfin ) AS desdefsuf, "
                    //+ "--AND cvetipdefsuf = g.cvetipdefsuf ya no lo ocupamos "
                    + "a.apepat, a.apemat, a.nombre, a.estatusalu, g.estatusgrado, fecnac, sexo, g.promedio, g.promedioGral, a.probem, "
                    + "(CASE WHEN repetidor ='R' THEN 'R'  ELSE '' END) AS rep, a.afromexicana AS etnia, "
                    + "(SELECT COUNT(*) FROM evaluaciones WHERE cicescini=g.cicescini AND grado = g.grado AND idalu = g.idalu AND numeval=1 AND calif1>=5.0) AS num_eval1,"                     
                    + "(SELECT COUNT(*) FROM evaluaciones WHERE cicescini=g.cicescini AND grado = g.grado AND idalu = g.idalu AND numeval=3 AND calif1>=5.0) AS num_eval3," 
                    + "(SELECT COUNT(*) FROM alumnomaterias WHERE cicescini=g.cicescini AND cveplan = g.cveplan AND grado=g.grado AND idalu = g.idalu) AS num_mat "
                    + "FROM alumno a, alumnogrado g "
                    + "WHERE a.idalu = g.idalu AND g.cicescini="+cicescini+" AND g.idcct="+idcct+" AND g.Grado="+grado+" AND "
                    //+ "--a.estatusalu='I' AND "                
                    + "(g.Grupo = '"+grupo+"' OR g.Grupo='---') "
                    + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        return qryToArrlmap(rs, null, true, 2);
    }
        
    public void setBajaAAlumno (String califCicEscIn, String cicescin, String idalu, String tblPrincipal_cveplan, String idcct, String grado, String grupo, String cicescini, String usuario) throws SQLException
    {
        if (califCicEscIn.equals(cicescin))
            stm.execute("UPDATE alumno SET estatusalu = 'BD', usuario='"+usuario.toUpperCase()+"', fecha = date(current), hora = extend(current, hour to minute) WHERE idalu = "+idalu);
        stm.execute("UPDATE alumnoGrado SET estatusgrado = 'BD', usuario='"+usuario.toUpperCase()+"', fecha = date(current), hora = extend(current, hour to minute) WHERE cicescini ="+califCicEscIn+" AND idalu = "+idalu);
        stm.execute("INSERT INTO bajasvit VALUES ("+idalu+", "+idcct+", "+grado+", '"+grupo+"', '"+usuario.toUpperCase()+"', date(current), extend(current, hour to minute), "+cicescini+" )");
        //Disminuimos el dato de total en el grupo
        updateGposSubtract(cicescini,idcct,grado, grupo); 
        // Cancelamos el folio de Reporte de Evaluación
        cancelarFolioRepEval (califCicEscIn, idalu, usuario);
        if (tblPrincipal_cveplan.equals("1") && grado.equals("6") || tblPrincipal_cveplan.equals("2") && grado.equals("3"))
            cancelarFolioCertif (califCicEscIn, idcct, grado, grupo, idalu, usuario);
    }
    
    private void cancelarFolioRepEval (String cicescini, String idalu, String usuario) throws SQLException
    {
        stm.execute (
                  "INSERT INTO fol_re_elec_cance (idfolio, idalu, cveplan, foliolet, folionum, idcct, grado, cicescini, usuario, fecha, hora, usuario_cance, "
                          + "fecha_cance,hora_cance,estatus,nivelmod,fecha_expedicion) "
                + "SELECT idfolio, idalu, cveplan, foliolet, folionum, idcct, grado, cicescini, usuario, fecha, hora, '"+usuario+"', date(current), "
                          + "extend(current, hour to minute),estatus,nivelmod,fecha_expedicion "
                + "FROM fol_re_elec "
                + "WHERE cicescini="+cicescini+" AND idalu="+idalu);
        stm.execute("DELETE FROM fol_re_elec WHERE cicescini="+cicescini+" AND idalu="+idalu);
    }
    
    public boolean isValidaCapacidadGpo(String califCicEscIn, String cicescin, String idcct, String grado, String grupo) throws SQLException
    {
        boolean grupovalido = false;
        
        rs = stm.executeQuery("SELECT grupocap > gruposal "
                      + " FROM escuelagpos WHERE cicescini="+califCicEscIn+" AND idcct="+idcct+" AND grado="+grado+" AND grupo='"+grupo+"' AND estatus='A' ");
        rs.next();
        grupovalido = rs.getBoolean(1);
        
        return grupovalido;
    }
    public void setInscritoAAlumno (String califCicEscIn, String cicescin, String idalu, String usuario, String idcct,String grado, String grupo) throws SQLException
    {
        if (califCicEscIn.equals(cicescin)){
            stm.execute("UPDATE alumno SET estatusalu = 'I', usuario='"+usuario.toUpperCase()+"', fecha = date(current), hora = extend(current, hour to minute)  WHERE idalu = "+idalu);
            stm.execute("UPDATE alumnoGrado SET estatusgrado = 'I', usuario='"+usuario.toUpperCase()+"', fecha = date(current), hora = extend(current, hour to minute)  WHERE cicescini ="+califCicEscIn+" AND idalu = "+idalu);
        }else
            stm.execute("UPDATE alumnoGrado SET estatusgrado = 'RE', usuario='"+usuario.toUpperCase()+"', fecha = date(current), hora = extend(current, hour to minute) WHERE cicescini ="+califCicEscIn+" AND idalu = "+idalu);
        //agregado para validad la capacidad limite del grupo
        updateGposAdd(califCicEscIn,idcct,grado, grupo);                
    }
    public void setRevocacionGdoAAlumno (String califCicEscIn, String cicescin, String idalu, String usuario, String idcct,String grado, String grupo) throws SQLException
    {
        stm.execute("UPDATE alumnoGrado SET estatusgrado = 'RG', usuario='"+usuario.toUpperCase()+"', fecha = date(current), hora = extend(current, hour to minute) WHERE cicescini ="+califCicEscIn+" AND idalu = "+idalu);        
    }
    
    public void setCamDGpo (String cicescini, String idcct, String grado,  String grupo, String idalu, String cvedefsuf, String usuario, String cvelengua, String etnia, String grupo_oldValue) throws SQLException
    {
        String afromexicana = "";
        if(cvelengua.isEmpty())
            cvelengua="ESP";        
        afromexicana = " afromexicana = "+(etnia.isEmpty() ? "null, ": "'"+etnia+"', ");        
        stm.execute("UPDATE alumno SET "+afromexicana+"cvelengua='"+cvelengua+"', usuario='"+usuario.toUpperCase().trim()
                +"', fecha = date(current) , hora = extend(current, hour to minute) WHERE idalu="+idalu);
        stm.execute("UPDATE alumnoGrado SET grupo = '"+grupo+"', cvedefsuf='"+cvedefsuf+"', usuario='"+usuario.toUpperCase().trim()
                +"', fecha = date(current) , hora = extend(current, hour to minute) WHERE idalu="+idalu+" AND cicescini = "+cicescini);
        if ("t".equals(getData("SELECT ofic_caleval_b5 FROM escuelagpos WHERE cicescini="+cicescini+" AND idcct="+idcct+" AND grado="+grado+" AND grupo='"+grupo+"'")))
            stm.execute("UPDATE desoficializacion SET grupo='"+grupo+"', usuario='"+usuario.toUpperCase().trim()+"', fecha = extend(current, YEAR TO SECOND) WHERE cicescini="+cicescini+" AND idcct="+idcct+" AND grado="+grado+" AND idalu="+idalu);
        updateGposAdd(cicescini,idcct,grado, grupo);
        updateGposSubtract(cicescini,idcct,grado, grupo_oldValue);
        
    }
    
    public void updateGposAdd(String cicescini, String idcct, String grado,  String grupo) throws SQLException
    {
        stm.execute("UPDATE escuelagpos SET gruposal = gruposal+1 "
                + " WHERE cicescini = "+cicescini+" AND idcct = " + idcct + " AND grado = " + grado 
                + " AND grupo = '" +grupo+"' AND estatus='A' ");
    }
    
    public void updateGposSubtract(String cicescini, String idcct, String grado,  String grupo) throws SQLException 
    {
        stm.execute("UPDATE escuelagpos SET gruposal = (CASE WHEN gruposal > 0 THEN gruposal-1 ELSE 0 END)"
                + " WHERE cicescini = "+cicescini+" AND idcct = " + idcct + " AND grado = " + grado 
                + " AND grupo = '" +grupo+"' AND estatus='A' ");
    }
    
    public void setCamTotGpo (String idalu, String grupo, String califCicEscIn, String usuario) throws SQLException
    {
        stm.execute("UPDATE alumnoGrado SET grupo = '"+grupo+"', usuario='"+usuario.toUpperCase().trim()+"', fecha = date(current) , hora = extend(current, hour to minute) WHERE idalu="+idalu+" AND cicescini = "+califCicEscIn);
    }
    
    public void eliminarAlumno (String idalu, String califCicEscIn, String tblPrincipal_cveplan, String usuario) throws SQLException, SICEEO_Excepcion
    {
        if (!usuario.equals(getData("SELECT TRIM(usuario) FROM alumnogrado WHERE idalu="+idalu+" AND cicescini="+califCicEscIn)))  //Verificamos que sea el mismo usuario que dio de alta esté eliminando.
            throw new SICEEO_Excepcion (0,"USUARIO_NO_PUEDE_ELIMINAR");
        else if ( 0 != Integer.parseInt(getData ("SELECT count(*) FROM alumnogrado WHERE idalu="+idalu+" AND cicescini<>"+califCicEscIn))) //Verificamos que sólo tenga un registro que es el del ciclo a eliminar en la tabla alumnogrado.
            throw new SICEEO_Excepcion (0,"ALUMNO_CON_HISTORIAL");
        else if ( 0 != Integer.parseInt(getData ("SELECT count(*) FROM folios_impre WHERE idalu="+idalu + " AND estatus='AC' "))) //Verificamos que no tenga ningún folio impreso
            throw new SICEEO_Excepcion (0,"ALUMNO_CON_HISTORIAL");
        
        rs = stm.executeQuery("SELECT sum(calif1) AS sumcalifs FROM evaluaciones WHERE idalu="+idalu);  //revisamos que no haya registros en evaluaciones o que tenga 0 en calificaciones
        if(rs.next() && rs.getInt("sumcalifs")>0)
            throw new SICEEO_Excepcion (0,"ALUMNO_CON_HISTORIAL");
        
        rs = stm.executeQuery("SELECT sum(promedio) AS sumpromedio FROM alumnomaterias WHERE idalu="+idalu); //revisamos que no haya registros en alumnomaterias o que tenga 0 en promedio
        if(rs.next() && rs.getInt("sumpromedio")>0)
            throw new SICEEO_Excepcion (0,"ALUMNO_CON_HISTORIAL");
        
        
        stm.execute("DELETE FROM folios_impre WHERE idalu="+idalu+" AND cveplan="+tblPrincipal_cveplan + " AND estatus='AC' ");
        stm.execute("DELETE FROM evaluaciones WHERE idalu="+idalu+" AND cicescini="+califCicEscIn);
        stm.execute("DELETE FROM alumnomaterias WHERE idalu="+idalu+" AND cicescini="+califCicEscIn);
        stm.execute("DELETE FROM alumnogrado WHERE idalu="+idalu+" AND cicescini="+califCicEscIn);
        stm.execute("DELETE FROM alumno WHERE idalu="+idalu);
    }
    
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO Discapa ***********************************
//************************************************************************************************************
   
    public ArrayList<Map> catDiscap (String tblPrincipal_cicescini, String tblPrincipal_cveplan) throws SQLException
    {
        rs = stm.executeQuery ("SELECT cvedefsuf[1,4] || ' | ' || desdefsuf AS cveydes, cvetipdefsuf, cvedefsuf, desdefsuf "
                + "FROM deficsuficalu "
                + "WHERE cicescini<="+tblPrincipal_cicescini+" AND cicescfin>="+tblPrincipal_cicescini+" AND cveplan="+tblPrincipal_cveplan+" "
                + "ORDER BY desdefsuf");
        
        return qryToArrlmap(rs, null, true, 0);
    }
    
    public ArrayList<Map> discap(String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        rs = stm.executeQuery(
                  "SELECT Alumnogrado.cicescini, alumnogrado.idcct, alumno.idalu, curp, "
                    + "(trim(nvl(alumno.apepat,'')) || '/' || trim(nvl(alumno.apemat,'')) || \"*\" || trim(nvl(alumno.nombre,''))) AS nom_tot, "
                    + "alumnogrado.grado, alumnogrado.grupo, alumnogrado.cvedefsuf, "
                    + "(SELECT desdefsuf FROM deficsuficalu WHERE cvedefsuf = alumnogrado.cvedefsuf AND cicescini <= alumnogrado.cicescini AND cicescfin >= alumnogrado.cicescini AND cveplan= alumnogrado.cveplan)AS desdefsuf, "
                    + "alumno.apepat, alumno.apemat, alumno.nombre "
                + "FROM alumno , alumnogrado "
                + "WHERE alumno.idalu = alumnogrado.idalu AND "
                    + "alumnogrado.cicescini  = "+cicescini+" AND "
                    + "alumnogrado.idcct      = "+idcct+"     AND "
                    + "alumnogrado.Grado      = "+grado+"     AND "
                    + "alumno.estatusalu='I' AND "
                    + "(alumnogrado.Grupo     = '"+grupo+"'   OR "
                    + "alumnogrado.Grupo     ='---') "
                + "ORDER BY alumno.apepat, alumno.apemat, alumno.nombre, alumno.curp"
        );
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public void guardarDiscap (ArrayList<Map> tblDiscap, String tblPrincipal_cicescini) throws SQLException
    {
        int numFilas = tblDiscap.size();
        for (int i=0; i<numFilas; i++)
            stm.execute("UPDATE alumnogrado SET cvedefsuf='"+tblDiscap.get(i).get("cvedefsuf_newvalue")+"' WHERE idalu="+tblDiscap.get(i).get("idalu")+" AND cicescini="+tblPrincipal_cicescini);
    }
    
//************************************************************************************************************
//************************ MÉTODOS USADOS EN EL MÓDULO camDTaller, camDAes y camDArte ************************
//************************************************************************************************************
    private ArrayList<Map> getTablaTaller (String cvetipmat, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, 
             String tblPrincipal_grado, String tblPrincipal_grupo) throws SQLException
    {
        rs = stm.executeQuery("SELECT Alumnogrado.cicescini, alumnogrado.idcct, alumno.idalu, curp, "
                            + "(trim(nvl(alumno.apepat,'')) || '/' || trim(nvl(alumno.apemat,'')) || \"*\" || trim(nvl(alumno.nombre,''))) AS nom_tot, "
                            + "alumnogrado.grado, alumnogrado.grupo, alumnogrado.cveprograma, alumno.apepat, alumno.apemat, alumno.nombre "                            
                        + "FROM alumno , alumnogrado "
                        + "WHERE alumno.idalu = alumnogrado.idalu AND "                            
                            + "alumnogrado.cveplan= " + tblPrincipal_cveplan +" AND "
                            + "alumnogrado.cicescini  = "+tblPrincipal_cicescini+" AND "
                            + "alumnogrado.idcct      = "+tblPrincipal_idcct+" AND "
                            + "alumnogrado.Grado      = "+tblPrincipal_grado+" AND "
                            + "alumno.estatusalu='I' AND "
                            + "(alumnogrado.Grupo     = '"+tblPrincipal_grupo+"' OR "
                            + "alumnogrado.Grupo     ='---') "
                        + "ORDER BY alumno.apepat, alumno.apemat, alumno.nombre, alumno.curp");                
                        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    private ArrayList<Map> getTablaDiscapTallerAesArte (String cvetipmat, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo) throws SQLException
    {
        rs = stm.executeQuery("SELECT Alumnogrado.cicescini, alumnogrado.idcct, alumno.idalu, curp, "
                            + "(trim(nvl(alumno.apepat,'')) || '/' || trim(nvl(alumno.apemat,'')) || \"*\" || trim(nvl(alumno.nombre,''))) AS nom_tot, "
                            + "alumnogrado.grado, alumnogrado.grupo, alumnogrado.cveprograma, alumnomaterias.cvetipmat, alumnomaterias.cvemat, "
                            + "(SELECT desmat FROM materias WHERE cvemat = alumnomaterias.cvemat AND cvetipmat = alumnomaterias.cvetipmat)AS desMat, "
                            + "alumno.apepat, alumno.apemat, alumno.nombre "
                        + "FROM alumno , alumnogrado, alumnomaterias "
                        + "WHERE alumno.idalu = alumnogrado.idalu AND alumnogrado.idalu = alumnomaterias.idalu AND "
                            + "alumnomaterias.cicescini="+tblPrincipal_cicescini+" AND "
                            + "alumnomaterias.grado = "+tblPrincipal_grado+" AND "
                            //+ "alumnomaterias.cvetipmat ='"+cvetipmat+"' AND "
                            + "alumnomaterias.cvetipmat " + (cvetipmat.equals("EDT") ? "IN ('EDT','TEC') " : " = '"+cvetipmat+"' ") + " AND "
                            + "alumnomaterias.cveplan= 2 AND "
                            + "alumnogrado.cicescini  = "+tblPrincipal_cicescini+" AND "
                            + "alumnogrado.idcct      = "+tblPrincipal_idcct+" AND "
                            + "alumnogrado.Grado      = "+tblPrincipal_grado+" AND "
                            + "alumno.estatusalu='I' AND "
                            + "(alumnogrado.Grupo     = '"+tblPrincipal_grupo+"' OR "
                            + "alumnogrado.Grupo     ='---') "
                        + "ORDER BY alumno.apepat, alumno.apemat, alumno.nombre, alumno.curp");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    private ArrayList<Map> getTablaClubEscuela (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo) throws SQLException
    {
        rs = stm.executeQuery("SELECT eg.*,cvetipmat,cvemat,(select desmat from materias m where  " 
                                    + " eg.cvetipmat = m.cvetipmat " 
                                    + " AND eg.cvemat = m.cvemat) AS desmat "                                                     
                        + "FROM escuelagrupos eg "
                        + "WHERE eg.idcct = " + tblPrincipal_idcct + " AND "
                        + " eg.cicescini = " + tblPrincipal_cicescini + " AND "
                        + " eg.grado = " + tblPrincipal_grado + " AND "        
                        + " eg.grupo = '" + tblPrincipal_grupo + "' AND "                
                        + " eg.estatus = 'A' AND" 
                        + " eg.cvetipmat like 'AC%'");
                         
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<Map> tblClubAlumno (String idalu, String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_cveplan) throws SQLException
    {
        rs = stm.executeQuery("SELECT cvetipmat,cvemat,(select desmat from materias m where  " 
                                    + " am.cvetipmat = m.cvetipmat " 
                                    + " AND am.cvemat = m.cvemat) AS desmat "                                                     
                        + " FROM alumnomaterias am "
                        + " WHERE am.idalu = " + idalu + " AND "                        
                        + " am.cicescini = " + tblPrincipal_cicescini + " AND "
                        + " am.grado = " + tblPrincipal_grado + " AND "        
                        + " am.cveplan = " + tblPrincipal_cveplan + " AND "                                     
                        + " am.cvetipmat like 'AC%'");
                         
        return qryToArrlmap(rs, null, true, 2);
    }
    
    
    public void insertarClubAlumno (String idalu,String cvetipmat, String cvemat,String modalidad,String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String txtUsuario) throws SQLException
    {        
        String query = "INSERT INTO AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu, cicescini, cicescfin, grado,  grupotaller, promedio, estatmat, "
                            + "califant, repetidor, repecont, usuario, fecha, hora ) "
                        + "SELECT cvetipmat, cvemat, cveplan, e.cveprograma, "+idalu+" AS IDALU, "+tblPrincipal_cicescini+" AS cicescini,"
                            + (Integer.parseInt(""+tblPrincipal_cicescini)+1)+" AS cicescfin, e.grado,'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, "
                            + "0 AS califant, 'E' AS repetidor, 0 AS repecont, '"+txtUsuario.trim().toUpperCase()+"' AS USUARIO, date(current) AS fecha, "
                            + "extend(current, hour to minute) AS hora "
                        + " FROM ESQUEMAMATERIAS e, planmodalidad p "
                        + " WHERE e.cveplan = p.plan AND "
                        + " e.grado = p.grado AND " 
                        + " e.cveprograma = p.cveprograma AND " 
                        + " p.cveentidad = 20 AND " 
                        + " p.modalidad = '" + modalidad + "' AND " 
                        + " p.cicescini <= " + tblPrincipal_cicescini +" AND "
                        + " p.cicescfin > " + tblPrincipal_cicescini + " AND "                         
                        + " e.CVEPLAN= "+tblPrincipal_cveplan+" AND "
                        + " e.GRADO = "+ tblPrincipal_grado + " AND  "                        
                        + " e.cvetipmat= '"+cvetipmat+"' AND "
                        + " e.cvemat= '"+cvemat+"' AND "   
                        + " e.estatus= 'A' ";
        
        stm.execute(query);
    }        
            
    private Map QKienTieneTallerAesArtes (String cvetipmat, String cicescini, String idcct, String grado, String grupo, String idalu) throws SQLException
    {
        Map QKienTiene;
        Statement stm2=null;
        ResultSet rs2=null;
        
        try
        {
            stm2 = getNewStatement ();
            rs2 = stm2.executeQuery("SELECT Alumnogrado.cicescini, alumnogrado.idcct, alumnogrado.idalu, alumnogrado.grado, alumnogrado.grupo, alumnomaterias.cvetipmat, "
                                    + "alumnomaterias.cvemat "
                                + "FROM alumnogrado, alumnomaterias "
                                + "WHERE alumnogrado.idalu = alumnomaterias.idalu AND alumnomaterias.cicescini= alumnogrado.cicescini AND "
                                    + "alumnomaterias.grado = alumnogrado.Grado AND "
                                    //+ "alumnomaterias.cvetipmat = '"+cvetipmat+"' AND"     
                                    + "alumnomaterias.cvetipmat " + (cvetipmat.equals("EDT") ? "IN ('EDT','TEC') " : " = '"+cvetipmat+"' ") + " AND "
                                    + "alumnomaterias.cveplan= 2 AND "
                                    + "alumnogrado.cicescini="+cicescini+" AND "
                                    + "alumnogrado.idcct="+idcct+" AND "
                                    + "alumnogrado.Grado="+grado+" AND "
                                    //+ "--alumnogrado.estatusGrado='I' AND "
                                    + "alumnogrado.Grupo='"+grupo+"' AND "
                                    + "alumnogrado.idalu="+idalu);
            QKienTiene = qryToMap(rs2, null, false, 0);

        }finally {
            closeStatement(stm2, rs2);
        }
        
        return QKienTiene;
    }
    
    private ArrayList<Map> camDGpo (String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        
        rs = stm.executeQuery("SELECT a.apepat, a.apemat, a.nombre, a.curp, g.cicescini, a.idalu, g.grado, g.cveprograma "
                    + "FROM alumno a, alumnogrado g "
                    + "WHERE a.idalu = g.idalu AND g.cicescini="+cicescini+" AND g.idcct="+idcct+" AND g.Grado="+grado+" AND "
                    + "(g.Grupo = '"+grupo+"' OR g.Grupo='---') AND estatusgrado<>'BD' "
                    + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        return qryToArrlmap(rs, null, false, 0);
    }
    
  //************************************************************************************************************
//********************************** MÉTODOS USADOS EN EL MÓDULO camDTaller **********************************
//************************************************************************************************************
    public ArrayList<Map> camDTaller (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo) throws SQLException
    {
        return getTablaDiscapTallerAesArte ("EDT", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
    }
    
    public ArrayList<Map> tallValX (String cicescini, String cveprograma, String grado, String[]friltroCols) throws SQLException
    {
        /*rs = stm.executeQuery("SELECT distinct idcct, cveturno, cicescini, cicescfin, grado, 'x' AS grupo, cvetipmat, cvemat, 0 AS grupocap, 0 AS gruposal, "
                + "'A' estatus , (SELECT desmat FROM materias WHERE cvemat = escuelagrupos.cvemat AND cvetipmat = escuelagrupos.cvetipmat) AS desmat "
                + "FROM escuelaGrupos "
                + "WHERE cicescini = "+cicescini+" AND idcct = "+idcct+" AND grado="+grado+" "
                    //+ "AND grupo = :grupo "                                   //OJO: Esta llínea la comenta robert al entrar al módulo CamDTaller, pero la activa al salir
                    + "AND cvetipmat ='EDT' "
                + "ORDER BY cvemat");
        */
        rs = stm.executeQuery("SELECT distinct m.cvemat, m.desmat " 
                + " FROM esquemamaterias AS em, materias m " 
                + " WHERE em.cicescini <= " + cicescini 
                + " AND em.cicescfin >= " + cicescini 
                + " AND em.cvetipmat IN ('EDT','TEC') " 
                + " AND em.cvemat = m.cvemat " 
                + " AND em.cvetipmat = m.cvetipmat " 
                + " AND cveprograma = '" + cveprograma + "' " 
                + " AND estatus = 'A' " 
                + " AND grado = " + grado
                + " AND cveplan = 2");
        
        return qryToArrlmap(rs, friltroCols, true, 0);
    }
    
    public void insertarTalleres (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_modalidad, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String txtUsuario) throws SQLException            
    {
        String query="";
        int numFilas, QCamDGpo_grado;
        Map QKienTieneTaller;
        
        ArrayList<Map> QCamDGpo = camDGpo(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
        
        numFilas = QCamDGpo.size();
        for (int i=0; i<numFilas; i++)
        {
            QKienTieneTaller = QKienTieneTallerAesArtes ("TEC", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, ""+QCamDGpo.get(i).get("idalu"));
            if (QKienTieneTaller.isEmpty())
            {
                QCamDGpo_grado = Integer.parseInt(""+QCamDGpo.get(i).get("grado"));
                query = "INSERT INTO AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu, cicescini, cicescfin, grado,  grupotaller, promedio, estatmat, "
                            + "califant, repetidor, repecont, usuario, fecha, hora ) "
                        + "SELECT cvetipmat, cvemat, cveplan, cveprograma, "+QCamDGpo.get(i).get("idalu")+" AS IDALU, "+QCamDGpo.get(i).get("cicescini")+" AS cicescini,"
                            + (Integer.parseInt(""+QCamDGpo.get(i).get("cicescini"))+1)+" AS cicescfin, grado,'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, "
                            + "0 AS califant, 'E' AS repetidor, 0 AS repecont, '"+txtUsuario.trim().toUpperCase()+"' AS USUARIO, date(current) AS fecha, "
                            + "extend(current, hour to minute) AS hora "
                        + "FROM ESQUEMAMATERIAS "
                        + "WHERE CVEPLAN= 2 AND  GRADO = "+ QCamDGpo_grado + " AND  CVEPROGRAMA = '"+QCamDGpo.get(i).get("cveprograma")+"' "  //todos los 1ros llevan el nuevo plan con diferencia en DSN
                        + "AND estatus= 'A' ";
                        //    dm.Q_Mat_cicAct_i.SQL.Add('      AND CICESCINI <= '+ dm.Q_camDgpocicescini.AsString);
                        //    dm.Q_Mat_cicAct_i.SQL.Add('      AND CICESCFIN >= '+ dm.Q_camDgpocicescini.AsString);

                if (tblPrincipal_modalidad.equals("DSN") )
                   query += " AND CVETIPMAT = 'TEC' AND CVEMAT LIKE 'NAP%' ";
                else
                {
                    if ( tblPrincipal_modalidad.equals("DTV") )
                    {
                        /* if (QCamDGpo_grado == 1)
                            query += "AND  CVETIPMAT= 'EDT' AND CVEMAT=965 ";
                         else if (QCamDGpo_grado == 2)
                            query += "AND  CVETIPMAT= 'EDT' AND CVEMAT=952 ";
                         else if (QCamDGpo_grado == 3)
                            query += "AND  CVETIPMAT= 'EDT' AND CVEMAT=952 ";*/
                        query += " AND  CVETIPMAT = 'TEC' AND CVEMAT='1013' ";  //Ciclo: 2019-2020
                    }else{
                        query += " AND  CVETIPMAT = 'TEC' AND CVEMAT='9004' "; //Ciclo: 2019-2020
                         /*if (QCamDGpo_grado == 1)
                            query += "AND  CVETIPMAT= 'EDT' AND CVEMAT=9001 ";
                         else if (QCamDGpo_grado == 2)
                            query += "AND  CVETIPMAT= 'EDT' AND CVEMAT=9002 ";
                         else if (QCamDGpo_grado == 3)
                            query += "AND  CVETIPMAT= 'EDT' AND CVEMAT=9003 ";*/
                    }
                }
                stm.execute(query);
            }
        }
    }
       
    public void guardarCamDTaller (ArrayList<Map> tblCamDTaller, String tblPrincipal_cicescini) throws SQLException
    {
        int numFilas = tblCamDTaller.size();
        for (int i=0; i<numFilas; i++) {
            stm.execute("UPDATE alumnomaterias SET cvemat='"+tblCamDTaller.get(i).get("cvemat_newvalue")+"' "
                      + "WHERE idalu="+tblCamDTaller.get(i).get("idalu")+" AND cicescini="+tblPrincipal_cicescini+" "
                          + "AND cvemat='"+tblCamDTaller.get(i).get("cvemat_oldvalue")+"' AND cvetipmat='TEC' ");  //Antes EDT
            
            stm.execute("UPDATE evaluaciones SET CveMat='"+tblCamDTaller.get(i).get("cvemat_newvalue")+"' "
                    + "WHERE idalu="+tblCamDTaller.get(i).get("idalu")+" AND cicescini="+tblPrincipal_cicescini+" AND CVETIPMAT='TEC'");  //Antes EDT
        }
    }
    
//************************************************************************************************************
//********************************** MÉTODOS USADOS EN EL MÓDULO camDClub **********************************
//************************************************************************************************************
    public ArrayList<Map> camDTaller (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String tblPrincipal_grupo) throws SQLException
    {
        return getTablaTaller ("FA", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_cveplan, tblPrincipal_grado, tblPrincipal_grupo);
    }
    
    public ArrayList<Map> tblClubEscuela_ (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo) throws SQLException
    {
        return getTablaClubEscuela (tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
    }
    
    public ArrayList<Map> tblClubEscuela (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo,String[]friltroCols) throws SQLException
    {
        rs = stm.executeQuery("SELECT cvetipmat,cvemat,(select desmat from materias m where  " 
                                    + " eg.cvetipmat = m.cvetipmat " 
                                    + " AND eg.cvemat = m.cvemat) AS desmat "                                                     
                        + "FROM escuelagrupos eg "
                        + "WHERE eg.idcct = " + tblPrincipal_idcct + " AND "
                        + " eg.cicescini = " + tblPrincipal_cicescini + " AND "
                        //+ " eg.grado = " + tblPrincipal_grado + " AND "        
                        //+ " eg.grupo = '" + tblPrincipal_grupo + "' AND "                
                        + " eg.estatus = 'A' AND" 
                        + " eg.cvetipmat like 'AC%'"
                        + " GROUP BY 1,2,3 ");                
        
        return qryToArrlmap(rs, friltroCols, true, 0);        
    }
    
    public ArrayList<Map> tblClubOaxaca (String cvetipmat) throws SQLException
    {
        rs = stm.executeQuery("SELECT m.cvetipmat,m.cvemat,m.desmat "
                + " FROM materias m "
                + " WHERE m.cvetipmat = '"+cvetipmat+"' "
                + " ORDER BY 1,2,3");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<Map> tblClubCct () throws SQLException
    {
        rs = stm.executeQuery("SELECT tm.cvetipmat,tm.destipmat,m.cvemat,m.desmat "
                + " FROM materias m, tipomaterias tm "
                + " WHERE m.cvetipmat = tm.cvetipmat "
                + " AND tm.cvetipmat like 'AC%' "
                + " ORDER BY 1,3");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<Map> tblAmbito (String cicescini, String idcct, String grado, String[]friltroCols) throws SQLException
    {
        rs = stm.executeQuery("SELECT cvetipmat,destipmat "
                + " FROM tipomaterias"
                + " WHERE cvetipmat like 'AC%'");
        
        return qryToArrlmap(rs, friltroCols, true, 0);
    }
    
    public ArrayList<Map> tblAmbitos () throws SQLException
    {
        rs = stm.executeQuery("SELECT cveambito,nom_ambito"
                + "FROM catambito ");
        
        return qryToArrlmap(rs, null, true, 0);
    }
    
    public ArrayList<Map> tblAluAmbClub (String idalu, String cicescini, String idcct, String cveplan, String grado, String grupo) throws SQLException
    {
        rs = stm.executeQuery("SELECT * "
                + " FROM alumnomaterias"
                + " WHERE ");
        
        return qryToArrlmap(rs, null, true, 0);
    }
        
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO camDAes ***********************************
//************************************************************************************************************
    public ArrayList<Map> camDAes (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo) throws SQLException
    {
        return getTablaDiscapTallerAesArte ("AES", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
    }
    
    public ArrayList<Map> catAes (String cicescini, String grado, String cveprograma, String[]friltroCols) throws SQLException
    {
        rs = stm.executeQuery(
                  "SELECT * , (SELECT desmat FROM materias WHERE cvemat = esquemamaterias.cvemat AND cvetipmat = esquemamaterias.cvetipmat)AS desmat "
                + "FROM ESQUEMAMATERIAS "
                + "WHERE CVETIPMAT='AES' AND CVEPROGRAMA='"+cveprograma+"' AND GRADO="+grado+" AND cicescini<="+cicescini+" AND cicescfin>="+cicescini);
        
        return qryToArrlmap(rs, friltroCols, true, 0);
    }
    
    public void guardarCamDAes (ArrayList<Map> tblCamDAes, String tblPrincipal_cicescini) throws SQLException
    {
        int numFilas = tblCamDAes.size();
        for (int i=0; i<numFilas; i++){
            stm.execute("UPDATE alumnomaterias SET cvemat='"+tblCamDAes.get(i).get("cvemat_newvalue")+"' "
                      + "WHERE idalu="+tblCamDAes.get(i).get("idalu")+" AND cicescini="+tblPrincipal_cicescini+" "
                          + "AND cvemat='"+tblCamDAes.get(i).get("cvemat_oldvalue")+"' AND cvetipmat='AES' ");
        
            stm.execute("UPDATE evaluaciones SET CveMat='"+tblCamDAes.get(i).get("cvemat_newvalue")+"' "
                    + "WHERE idalu="+tblCamDAes.get(i).get("idalu")+" AND cicescini="+tblPrincipal_cicescini+" AND CVETIPMAT='AES'");
        }
    }
    
    public void insertarCamDAes (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario) throws SQLException
    {
        int numFilas;
        Map QKienTieneAes;
        
        ArrayList<Map> QCamDGpo = camDGpo(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
        
        numFilas = QCamDGpo.size();
        for (int i=0; i<numFilas; i++)
        {
            QKienTieneAes = QKienTieneTallerAesArtes ("AES", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, ""+QCamDGpo.get(i).get("idalu"));
            if (QKienTieneAes.isEmpty())
            {
                stm.execute("INSERT INTO AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu,cicescini, cicescfin, grado,  grupotaller, promedio,"
                                + "estatmat, califant, repetidor, repecont, usuario, fecha, hora ) "
                            + "SELECT cvetipmat, cvemat, cveplan, cveprograma, "+QCamDGpo.get(i).get("idalu")+" AS IDALU, "
                                + QCamDGpo.get(i).get("cicescini")+" AS cicescini, "+(Integer.parseInt(""+QCamDGpo.get(i).get("cicescini"))+1)+" AS cicescfin, grado, "
                                + "'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, 0 AS califant, 'E' AS repetidor, 0 AS repecont, "
                                + "'"+txtUsuario.trim().toUpperCase()+"' AS USUARIO, date(current) AS fecha, extend(current, hour to minute) AS hora "
                            + "FROM ESQUEMAMATERIAS "
                            + "WHERE CVEPLAN=2 "
                                + "AND GRADO="+QCamDGpo.get(i).get("grado")+" "
                                + "AND CVEPROGRAMA='"+QCamDGpo.get(i).get("cveprograma")+"' " //todos los 1ros llevan el nuevo plan con diferencia en DSN
                                + "AND estatus='A' "
                                + "AND  CVETIPMAT='AES' AND CVEMAT='040'");

            }
        }
    }
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO camDArte **********************************
//************************************************************************************************************
    public ArrayList<Map> camDArte (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo) throws SQLException
    {
        return getTablaDiscapTallerAesArte ("ART", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
    }
    
    public ArrayList<Map> catArte (String cicescini, String grado, String cveprograma, String[]friltroCols) throws SQLException
    {
        rs = stm.executeQuery(
                  "SELECT * , (SELECT desmat FROM materias WHERE cvemat = esquemamaterias.cvemat AND cvetipmat = esquemamaterias.cvetipmat) AS desmat "
                + "FROM ESQUEMAMATERIAS "
                + "WHERE CVETIPMAT='ART' AND CVEPROGRAMA='"+cveprograma+"' AND GRADO="+grado+" AND cicescini<="+cicescini+" AND cicescfin>="+cicescini);
        
        return qryToArrlmap(rs, friltroCols, true, 0);
    }
    
    public void guardarCamDArte (ArrayList<Map> tblCamDArte, String tblPrincipal_cicescini, String usuario) throws SQLException
    {
        int numFilas = tblCamDArte.size();
        for (int i=0; i<numFilas; i++){
            stm.execute("UPDATE alumnomaterias SET cvemat='"+tblCamDArte.get(i).get("cvemat_newvalue")+"', usuario='"+usuario+"', fecha=date(current), hora= extend(current, hour to minute) "
                      + "WHERE idalu="+tblCamDArte.get(i).get("idalu")+" AND cicescini="+tblPrincipal_cicescini+" "
                          + "AND cvemat='"+tblCamDArte.get(i).get("cvemat_oldvalue")+"' AND cvetipmat='ART' ");
        
            stm.execute("UPDATE evaluaciones SET CveMat='"+tblCamDArte.get(i).get("cvemat_newvalue")+"', usuario='"+usuario+"', fecha=date(current), hora= extend(current, hour to minute) "
                    + "WHERE idalu="+tblCamDArte.get(i).get("idalu")+" AND cicescini="+tblPrincipal_cicescini+" AND CVETIPMAT='ART'");
        }
    }
    public void insertNvoClubOax(String desmat, String cvetipmat, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_modalidad, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String tblPrincipal_cicescini) throws SQLException {
        int cvemat=0;
        
        rs = stm.executeQuery("SELECT *  "
                + "FROM materias "
                + "WHERE CVETIPMAT like 'AC%' AND desmat = '"+desmat+"'");
        
        if(!rs.next()){
            rs = stm.executeQuery("SELECT cvemat FROM materias WHERE cvetipmat like 'AC%' "
                    + " ORDER BY cvemat desc LIMIT 1");
            if(!rs.next())
                cvemat = 1001;
            else cvemat = Integer.parseInt(""+rs.getString("cvemat"))+1;
                                          
            stm.execute("INSERT INTO materias (cvetipmat, cvemat, desmat) "
                    + "VALUES ('"+cvetipmat+"','"+cvemat+"','"+desmat.toUpperCase()+"')");
            stm.execute("INSERT INTO esquemamaterias (cveplan,cveprograma,cvetipmat,cvemat,grado,ordenimpres,estatus,cicescini,cicescfin) "
                    + "SELECT "+tblPrincipal_cveplan+",cveprograma,'"+cvetipmat+"','"+cvemat+"',grado,11,'A',cicescini,cicescfin "
                            + " FROM planmodalidad " 
                            + " WHERE modalidad='" + tblPrincipal_modalidad + "' "
                            + " AND plan = " + tblPrincipal_cveplan + ""
                            + " AND cicescini <= " + tblPrincipal_cicescini +" "
                            + " AND cicescfin >= " + tblPrincipal_cicescini +" "
                            + " AND cveentidad = 20 " 
                            + " GROUP BY 2,grado,cicescini,cicescfin");
            stm.execute("INSERT INTO escuelagrupos (idcct,cveturno,cicescini,cicescfin,grado,grupo,cvetipmat,cvemat,grupocap,gruposal,estatus)" 
            + "SELECT idcct,cveturno,cicescini,cicescfin,grado,grupo,'"+cvetipmat+"','"+cvemat+"',70,0,'A' "
                + " FROM escuelagrupos where idcct = " + tblPrincipal_idcct
                + " AND cvetipmat = 'FA'" 
                + " AND cicescini = " + tblPrincipal_cicescini 
                + " AND estatus='A' " 
                + " GROUP BY 1,2,3,4,5,6 " 
                + " ORDER BY grado,grupo ");                                            
        }                
    }
    
    public void insertNvoClubCct(String cvemat, String cvetipmat, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_modalidad, String tblPrincipal_grado, String tblPrincipal_grupo, String tblPrincipal_cicescini) throws SQLException
    {        
        String query;
        rs = stm.executeQuery("SELECT * FROM esquemamaterias "
                + " WHERE cveplan = '"+tblPrincipal_cveplan+"' AND cvetipmat='"+cvetipmat+"' AND cvemat='"+cvemat+"' AND "
                + " grado="+tblPrincipal_grado+" AND cicescini="+tblPrincipal_cicescini);
        
        if(!rs.next()){
            stm.execute("INSERT INTO esquemamaterias (cveplan,cveprograma,cvetipmat,cvemat,grado,ordenimpres,estatus,cicescini,cicescfin) "
                + "SELECT "+tblPrincipal_cveplan+",cveprograma,'"+cvetipmat+"','"+cvemat+"',grado,11,'A',cicescini,cicescfin "
                + " FROM planmodalidad " 
                + " WHERE modalidad='" + tblPrincipal_modalidad + "' "
                + " AND plan = " + tblPrincipal_cveplan + ""
                + " AND cicescini <= " + tblPrincipal_cicescini +" "
                + " AND cicescfin >= " + tblPrincipal_cicescini +" "
                + " AND cveentidad = 20 " 
                + " GROUP BY 2,grado,cicescini,cicescfin");
        }
        
        query = "INSERT INTO escuelagrupos (idcct,cveturno,cicescini,cicescfin,grado,grupo,cvetipmat,cvemat,grupocap,gruposal,estatus)" 
            + "SELECT "+tblPrincipal_idcct+",cveturno,"+tblPrincipal_cicescini+","+(Integer.parseInt(""+tblPrincipal_cicescini)+1)+","+tblPrincipal_grado+",'"+tblPrincipal_grupo+"','"+cvetipmat+"','"+cvemat+"',70,0,'A' "
                + "FROM escuela where idcct="+tblPrincipal_idcct;
        stm.execute(query);        
    }
    
    public boolean clubEscuela(String cvemat, String cvetipmat, String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_modalidad, String tblPrincipal_grado, String tblPrincipal_grupo, String tblPrincipal_cicescini)throws SQLException
    {
        boolean respuesta=false;
        String query;
        
        query = "SELECT * FROM escuelagrupos WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+tblPrincipal_cicescini
                +" AND cvetipmat='"+cvetipmat+"' AND cvemat='"+cvemat+"' AND grado="+tblPrincipal_grado+ " AND grupo='"+tblPrincipal_grupo+"' ";
        
        rs = stm.executeQuery(query); 
        
        if(rs.next())
            respuesta= true;
        
        return respuesta;
    }
    
    public void insertarCamDArte (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario) throws SQLException
    {
        int numFilas, QCamDGpo_grado;
        String query;
        Map QKienTieneArte;
        
        ArrayList<Map> QCamDGpo = camDGpo(tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo);
        
        numFilas = QCamDGpo.size();
        for (int i=0; i<numFilas; i++)
        {
            QKienTieneArte = QKienTieneTallerAesArtes ("ART", tblPrincipal_cicescini, tblPrincipal_idcct, tblPrincipal_grado, tblPrincipal_grupo, ""+QCamDGpo.get(i).get("idalu"));
            if (QKienTieneArte.isEmpty())
            {
                QCamDGpo_grado = Integer.parseInt(tblPrincipal_grado);
                query = "INSERT INTO AlumnoMATERIAS(cvetipmat, cvemat, cveplan, cveprograma, idalu,cicescini, cicescfin, grado,  grupotaller, promedio,"
                            + "estatmat, califant, repetidor, repecont, usuario, fecha, hora ) "
                            + "SELECT cvetipmat, cvemat, cveplan, cveprograma, "+QCamDGpo.get(i).get("idalu")+" AS IDALU, "
                            + QCamDGpo.get(i).get("cicescini")+" AS cicescini, "+(Integer.parseInt(""+QCamDGpo.get(i).get("cicescini"))+1)+" AS cicescfin, grado, "
                            + "'---' AS grupotaller, 0 AS promedio, 'A' AS estatmat, 0 AS califant, 'E' AS repetidor, 0 AS repecont, "
                            + "'"+txtUsuario.trim().toUpperCase()+"' AS USUARIO, date(current) AS fecha, extend(current, hour to minute) AS hora "
                        + "FROM ESQUEMAMATERIAS "
                        + "WHERE CVEPLAN=2 "
                            + "AND GRADO="+QCamDGpo.get(i).get("grado")+" "
                            + "AND CVEPROGRAMA='"+QCamDGpo.get(i).get("cveprograma")+"' " //todos los 1ros llevan el nuevo plan con diferencia en DSN
                            + "AND estatus='A' "
                            + "AND CVETIPMAT='ART' AND CVEMAT='555' ";
                /*
                if (QCamDGpo_grado == 1 )
                    query += "AND CVETIPMAT='ART' AND CVEMAT='556' ";
                if (QCamDGpo_grado == 2 )
                    query += "AND CVETIPMAT='ART' AND CVEMAT='557' ";
                if (QCamDGpo_grado == 3 )
                    query += "AND CVETIPMAT='ART' AND CVEMAT='558' ";
                */
                stm.execute(query);
            }
        }
    }
    
    
    
//*************************************************************************************************************************************
//***************************************** MÉTODOS USADOS EN EL MÓDULO RepEvaluacion *************************************************
//*************************************************************************************************************************************
    public ArrayList<Map> getGrupoRepEvaluacion (String cicescini, String cveplan, String idcct, String grado, String grupo, String idalus) throws SQLException
    {
        String condicion="";
        if (!idalus.equals(""))
            condicion = "AND a.idalu in ("+idalus+") ";
        rs = stm.executeQuery("SELECT 'false' AS selec, a.idalu, curp, a.apepat, a.apemat, a.nombre,  '' AS folio, "
                    + "(TRIM(TRAILING ' ' FROM NVL(a.apepat,'')) || '/' || TRIM(TRAILING ' ' FROM NVL(a.apemat,'')) || '*' || TRIM(TRAILING ' ' FROM NVL(a.nombre,''))) AS nom_tot, "
                    + "CASE g.promedio WHEN '0.0' THEN ' ' ELSE NVL(g.promedio,' ') END AS promedio, "
                    + "CASE g.promedioGral WHEN '0.0' THEN ' ' ELSE NVL(g.promedioGral,' ') END AS promedioGral, g.promovido, g.grado, g.grupo "
                    + "FROM alumno a, alumnogrado g "
                    + "WHERE a.idalu = g.idalu AND g.cicescini="+cicescini+" AND g.cveplan="+cveplan+" AND g.idcct="+idcct+" AND g.Grado="+grado+" AND "
                    + "g.estatusgrado!='BD' AND g.Grupo = '"+grupo+"' " + condicion
                    + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<Map> boleta (String cicescini, String cveplan, String idcct, String grado, String grupo, 
            String folioIniFiltro, String idalus) throws SQLException
    {
        String tfolio, condicion="";
        
        if (folioIniFiltro.equals(""))
            tfolio ="outer folios_boleta "; //cuando hay k presentar a los alumnos aunk no tengan folio
        else
            tfolio = "folios_boleta";     //cuando en la consulata solicitas un folio en particular
        
        if (!idalus.equals(""))
            condicion = "AND a.idalu in ("+idalus+") ";
        condicion += "AND g.cicescini="+cicescini+" AND g.cveplan="+cveplan+" AND g.idcct= "+idcct+" AND g.Grado="+grado+" AND g.Grupo = '"+grupo+"'";
        
        rs = stm.executeQuery("SELECT f.idfolio, f.foliolet, f.folionum, e.cveplan, g.cicescini, e.cvezona, e.cct, substr(e.cct,3,8)AS cct7, e.cveturno, e.modalidad, "
                            + "E.NOMBRE AS ESCUELA, e.cveunidad,"
                            //+ "substr(e.cvezona,1,1) AS z1,substr(e.cvezona,2,1) AS z2,substr(e.cvezona,3,1) AS z3, "
                            //+ "(SELECT (trim(nvl(nombre,'')) || ' ' || trim(nvl(apepat,'')) || \" \" || trim(nvl(apemat,''))) FROM DIRECTORES WHERE DIRECTORES.IDCCT = E.IDCCT)  AS director, "
                            + "g.idcct, g.idalu, a.apepat, a.apemat, a.nombre, "
                            //+ "trim(E.desmunicipio)||', OAXACA' AS mpio, "
                            //+ "(SELECT TRIM(desMunicipio)||', OAXACA'  FROM mod59 WHERE mod59.idcct = G.idcct AND mod59.grado = G.grado AND mod59.grupo = G.grupo AND mod59.cicescini=g.cicescini) AS mpio59, "
                            //+ "(SELECT adg FROM delegado d, mod59 eg WHERE eg.cveunidad = d.cveunidad AND eg.idcct = g.idcct AND eg.grado = g.grado AND eg.grupo = g.grupo AND eg.cicescini = g.cicescini) AS adg, "
                            //+ "(SELECT CveUnidad  FROM mod59 WHERE mod59.idcct = G.idcct AND mod59.grado = G.grado AND mod59.grupo = G.grupo AND mod59.cicescini=g.cicescini) AS cveunidad59, "
                            + "(trim(nvl(a.apepat,'')) || '/' || trim(nvl(a.apemat,'')) || \"*\" || trim(nvl(a.nombre,''))) AS nom_tot, fecnac, sexo, G.grado, "
                            + "G.grupo,  a.CURP, g.promediogral , f.Estatus, "
                            
                            + "'false' AS selec "                              //Lìneas agregadas solo en SICEEO
                        + "FROM ESCUELA e, alumnogrado g, alumno a, "+tfolio+" f "
                        //+ "--outer folios_boleta f\n" +
                        +"WHERE g.idalu=a.idalu AND g.idcct=e.idcct AND a.estatusalu='I' AND e.estatusesc <>'CL' "
                            + "AND a.idalu=f.idalu AND g.grado = f.grado AND g.cicescini = f.cicescini AND f.estatus ='I' "
                        //"-- e.cct = '20DJN0006O'  AND\n" +
                        //"-- g.grado=3             AND\n" +
                        //"-- g.cicescini= 2008\n" +
                        //"--se agrego en la programacion g.cicescini  =     AND\n" +
                            + "AND g.estatusgrado!='BD' AND f.cveplan=g.cveplan AND f.cveplan=e.cveplan AND f.idcct=g.idcct "     //Lineas agregadas solo en SICEEO
                            +condicion
                        + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public Map getCalifsRepEvaluacion (String cicescini, String idcct, String cveplan, String grado, String grupo, String idalus) throws SQLException
    {
        String condicion="", idaluAct="",idaluAnt="";
        Map materia, alumnos = new HashMap();
        ArrayList<Map> calificaciones = new ArrayList<Map>();
        
        boolean primeraVez=true;
        
        
        String dato, columna;
        if (!idalus.equals(""))
            condicion = "AND a.idalu in ("+idalus+") ";
        
        rs = stm.executeQuery("SELECT a.idalu, " + 
                    //"--ag.idcct, em.cveplan, em.cveprograma, em.cvetipmat, em.cvemat, em.grado,  em.cicescini, em.cicescfin, \n" +
                    "m.cvetipmat, m.cvemat, m.desmat, m.matdefault, " +
                    //"(SELECT CASE WHEN e.calif1<6 THEN (CASE WHEN e.calif2<5 THEN '' ELSE CAST(e.calif2 AS VARCHAR(3)) END) ELSE CAST(e.calif1 AS VARCHAR(3)) END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=1) AS bimestre1, " +
                    "(SELECT CASE WHEN e.calif1>=5 THEN CAST(e.calif1 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=1) AS bimestre1, " +
                    "(SELECT CASE WHEN e.calif1>=5 THEN CAST(e.calif1 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=2) AS bimestre2, " +
                    "(SELECT CASE WHEN e.calif1>=5 THEN CAST(e.calif1 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=3) AS bimestre3, " +
                    "(SELECT CASE WHEN e.calif1>=5 THEN CAST(e.calif1 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=4) AS bimestre4, " +
                    "CASE am.exm WHEN 'ER' THEN '---' ELSE (SELECT CASE WHEN e.calif1>=5 THEN CAST(e.calif1 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=5) END AS bimestre5, " +
                    "CASE WHEN am.promedio>=5 THEN CAST(am.promedio AS VARCHAR(3)) ELSE '' END AS promedio, " +

                    "(SELECT CASE WHEN e.calif2>=6 THEN CAST(e.calif2 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=1) AS exmExtBim1, " +
                    "(SELECT CASE WHEN e.calif2>=6 THEN CAST(e.calif2 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=2) AS exmExtBim2, " +
                    "(SELECT CASE WHEN e.calif2>=6 THEN CAST(e.calif2 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=3) AS exmExtBim3, " +
                    "(SELECT CASE WHEN e.calif2>=6 THEN CAST(e.calif2 AS VARCHAR(3)) ELSE '' END FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cveprograma = em.cveprograma AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.cvemat not like 'NAP%' AND e.numeval=4) AS exmExtBim4, " +

                    "em.ordenimpres, ag.idcct, curp, a.apepat, a.apemat, a.nombre " +
                "FROM alumno a, alumnogrado ag, alumnomaterias am, esquemamaterias em, materias m " +
                "WHERE em.cvetipmat=m.cvetipmat AND em.cvemat=m.cvemat " +
                    "AND em.cveplan=am.cveplan AND em.cveprograma=am.cveprograma AND em.grado=am.grado AND em.cicescini<=am.cicescini AND am.cicescini<=em.cicescfin AND em.cvetipmat=am.cvetipmat AND em.cvemat=am.cvemat " +
                    "AND am.cicescini=ag.cicescini AND am.grado=ag.grado AND am.cveplan=ag.cveplan AND am.idalu=ag.idalu AND ag.idalu=a.idalu " +
                    //"--AND a.idalu=502033 " +
                    "AND ag.estatusgrado!='BD' AND ag.cveplan="+cveplan+" AND (ag.Grupo = '"+grupo+"' OR ag.Grupo='---') AND ag.idcct="+idcct+" AND ag.grado="+grado+" AND ag.cicescini="+cicescini+" " + condicion +
                "ORDER BY a.apepat, a.apemat, a.nombre, a.curp, em.ordenimpres");
        
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCols=rsmd.getColumnCount();
        while (rs.next()){
            materia = new HashMap();
            for (int posCol=1; posCol<=numCols; posCol++){                      //Para cada columna
                columna = rsmd.getColumnName(posCol).toLowerCase();
                dato = rs.getString(posCol);
                if (dato==null)
                    dato="";
                dato=dato.trim();
                materia.put(columna, dato);
            }
            
            idaluAct = ""+materia.get("idalu");
            if (primeraVez){ idaluAnt = idaluAct; primeraVez = false; }
            
            if ( !idaluAct.equals(idaluAnt) ){                                  //Por cada cambio de idalu, guardamos las calificaciones recopiladas asignándolo a su idalu correspondiente
                alumnos.put(idaluAnt, calificaciones);
                calificaciones = new ArrayList<Map>();
                calificaciones.add(materia);
                idaluAnt = idaluAct;
            }else
                calificaciones.add(materia);
        }
        alumnos.put(idaluAct, calificaciones);                                  //Como en la última vuelta no asigna, lo forzamos
        return alumnos;
    }
    
    public ArrayList <String[]> getConfigsImpreAsignadasAlFormato(String cveplan, String grado, String idcct, String modalidad, String formato) throws SQLException
    {
        ArrayList <String[]> configsImpreDelFormato=new ArrayList<String[]>();
        String[] fila;
        String idclasif="1";
        
        if (cveplan.equals("1") && modalidad.equals("DPB") )                    //Para diferenciar las primarias de las primarias indígenas
            idclasif="2";
        
        //rs = stm.executeQuery("SELECT idconfigsimpre, nombreconfig FROM reps_configsimpre WHERE idcct="+idcct);  //Para una única configuración de impresora
        rs = stm.executeQuery("SELECT i.idconfigsimpre, i.nombreconfig " +
                            "FROM reps_coordenadas c, reps_configsimpre i, reps_tipografia t, reps_descleyen l, reps_formatos f " +
                            "WHERE c.idconfigsimpre=i.idconfigsimpre AND c.idfuente=t.idfuente AND c.idleyenda=l.idleyenda AND c.idformato=f.idformato "
                                + "AND c.cveplan="+cveplan+" AND c.grado="+grado+" AND i.idcct="+idcct+" AND c.idclasif="+idclasif+" AND f.formato='"+formato+"' "
                                + "AND c.ordenimpre=1");
        while (rs.next()){
            fila = new String[2];
            fila[0]=rs.getString("idconfigsimpre");
            fila[1]=rs.getString("nombreconfig");
            configsImpreDelFormato.add(fila);
        }
        return configsImpreDelFormato;
    }
    
    public Map getInasistenciasRepEval (String cicescini, String cveplan, String grado, String idalus) throws SQLException
    {
        Map inasistenciasDeAlumnos = new HashMap();
        Map inasistencias;
        rs = stm.executeQuery("SELECT idalu, inst1, inst2, inst3, inst4, inst5 "
                            + "FROM inasistencias "
                            + "WHERE cicescini="+cicescini+" AND cveplan="+cveplan+" AND grado="+grado+" AND idalu in ("+idalus+")");
        while (rs.next()){
            inasistencias = new HashMap();
            inasistencias.put("inst1", rs.getString("inst1"));
            inasistencias.put("inst2", rs.getString("inst2"));
            inasistencias.put("inst3", rs.getString("inst3"));
            inasistencias.put("inst4", rs.getString("inst4"));
            inasistencias.put("inst5", rs.getString("inst5"));
            inasistenciasDeAlumnos.put(rs.getString("idalu"), inasistencias);
        }
        
        return inasistenciasDeAlumnos;
    }
    
    public void generaFolioReporteEvaluacion (String nivel, String txtFolioLet, int folioNum, int posSelActual, ArrayList<Map>tblRepEvaluacion, 
            String tblRepEvaluacion_cvezona, String tblRepEvaluacion_idcct, String tblRepEvaluacion_cct, String tblPrincipal_modalidad, String tblRepEvaluacion_cveturno, 
            String tblRepEvaluacion_cveplan, String tblRepEvaluacion_grado, String tblRepEvaluacion_grupo, String tblRepEvaluacion_cicescini, 
            String txtUsuario, String tblRepEvaluacion_cveunidad, Map dr) throws SQLException, SICEEO_Excepcion
    {
        int numFilas = tblRepEvaluacion.size();
        String tblRepEvaluacion_fott;
        String letNivel[] = {"","PRI","SEC","PRE"};
        String tipoNivel = letNivel[Integer.parseInt(tblRepEvaluacion_cveplan)];
        
        if (tblPrincipal_modalidad.equals("DPB"))
            tipoNivel = "DPB";
        
        for (int i=posSelActual; i<numFilas; i++)
        {
            tblRepEvaluacion_fott = ""+tblRepEvaluacion.get(i).get("foliolet")+tblRepEvaluacion.get(i).get("folionum");
            if ( tblRepEvaluacion_fott.length() == 0 )
            {
                try {
                    // dm2.Q_Boleta_canc.Close;
                    // dm2.Q_Boleta_canc.ParamByName('V_folioLet').AsString:= V_nivel+F_let.Text;
                    // dm2.Q_Boleta_canc.ParamByName('V_folioNum').AsInteger:= V_folio ;
                    // dm2.Q_Boleta_canc.Open;
                    // If dm2.Q_Boleta_canc.RecordCount>0 then begin
                    // MessageDlg('el Folio ya esta CANCELADO!!!', mtWarning, [mbOK], 0);
                    // dm.a_informix.Rollback;
                    // eXIT;
                    // END;

                    stm.execute("INSERT INTO folios_boleta(foliolet,folionum,idalu,idcct,cvezona,cveplan, tiponivel, cct,cveturno,grado,grupo,nombre,apepat,apemat,curp, "
                                    + "crip, cicescini, usuario, fecha, hora,cveunidad, estatus) "
                                    + "values ('"+nivel+txtFolioLet+"', "+folioNum+", "+tblRepEvaluacion.get(i).get("idalu")+","+tblRepEvaluacion_idcct+", "
                                    + "'"+tblRepEvaluacion_cvezona+"',"+tblRepEvaluacion_cveplan+", '"+tipoNivel+"','"+tblRepEvaluacion_cct+"', "
                                    + "'"+tblRepEvaluacion_cveturno+"', "+tblRepEvaluacion_grado+",'"+tblRepEvaluacion_grupo+"', "
                                    + "\""+tblRepEvaluacion.get(i).get("nombre")+"\",\""+tblRepEvaluacion.get(i).get("apepat")+"\", "
                                    + "\""+tblRepEvaluacion.get(i).get("apemat")+"\", '"+tblRepEvaluacion.get(i).get("curp")+"', "
                                    + "'"+(""+tblRepEvaluacion.get(i).get("curp")).substring(4,10)+"', "+tblRepEvaluacion_cicescini+", "
                                    + "'"+txtUsuario.toUpperCase()+"', date(current) , extend(current, hour to minute), '"+tblRepEvaluacion_cveunidad+"', 'I')");

                    folioNum ++;
                }catch (SQLException ex){
                    String mensaje;
                    if (ex != null && ex.getMessage()==null)
                        throw new SQLException (ex);
                    else if ( ex.getMessage().toUpperCase().contains("UNIQUE CONSTRAINT") ){
                        dr.put("txtFolioNum_Text", folioNum);
                        dr.put("tblRepEvaluacion", boleta (tblRepEvaluacion_cicescini, tblRepEvaluacion_cveplan, tblRepEvaluacion_idcct, tblRepEvaluacion_grado, 
                                tblRepEvaluacion_grupo, "", "") );
                        //podemos hacer una buskeda del folio
                        mensaje = buskFolBoleRepetido (txtFolioLet,folioNum, tblRepEvaluacion_cicescini, tblRepEvaluacion_cveplan, tblRepEvaluacion_grado, tipoNivel);
                        throw new SICEEO_Excepcion (0,"FOLIACION_SUSPENDIDA", mensaje);
                    }else
                        throw new SQLException (ex);

                }catch (Exception ex){
                    throw new SQLException (""+ex.getMessage());
                }
            }else{   
                //cuando en el camino se encuentra con un alumno k ya tiene folio de otro ciclo escolar
                // if dm2.Q_boletacicesciniLib.AsInteger <> dm.v_cicescin then
                // begin
                // MessageDlg('`ASIGNACION DE FOLIOS SUSPENDIDA !'+#13+#10+'Es posible que el folio este asignado a otro alumno', mtWarning, [mbOK], 0);
                // exit;
                // end;
            }
        }
    }
    
    private String buskFolBoleRepetido (String folioLet, int folioNum, String tblRepEvaluacion_cicescini, String tblRepEvaluacion_cveplan, String grado, String tiponivel) throws SQLException
    {
        rs = stm.executeQuery("SELECT fb.cct, fb.grado, fb.grupo, fb.idalu, fb.apepat, fb.apemat, fb.nombre, ag.estatusgrado, 'el folio lo tiene asignado la escuela: ' AS letrero " +
                              "FROM folios_boleta fb, alumnogrado ag " +
                              "WHERE fb.cicescini=ag.cicescini AND fb.grado=ag.grado AND fb.idalu=ag.idalu AND "
                                    + "fb.folionum="+folioNum+" AND fb.cicescini="+tblRepEvaluacion_cicescini+" AND fb.cveplan="+tblRepEvaluacion_cveplan+" "
                                    + "AND fb.grado="+grado+"  AND fb.tiponivel='"+tiponivel+"'");

        if (rs.next())
            return "Revise: ESCUELA:\""+rs.getString("cct")+"\", GRADO:\""+rs.getString("grado")+"\", GRUPO:\""+rs.getString("grupo").trim()+"\", "
                    + "ESTATUSGRADO:\""+rs.getString("estatusgrado").trim()+"\", IDALU:\""+rs.getString("idalu")+"\", "
                    + "ALUMNO:\""+rs.getString("apepat").trim()+"/"+rs.getString("apemat").trim()+"*"+rs.getString("nombre").trim()+"\", FOLIO:\""+folioLet+folioNum+"\".";
        return "";
    }
    
    public void eliminaFolioReporteEvaluacion ( int posSelActual, ArrayList<Map>tblRepEvaluacion ) throws SQLException
    {
        int numFilas = tblRepEvaluacion.size();
        String tblRepEvaluacion_fott;
        //--Vista--> Actual := dm2.Q_boleta.GetBookmark;
        //dm2.Q_boleta.First;
        for (int i=posSelActual; i<numFilas; i++)
        {
            tblRepEvaluacion_fott = ""+tblRepEvaluacion.get(i).get("foliolet")+tblRepEvaluacion.get(i).get("folionum");
            if ( tblRepEvaluacion_fott.length() > 0 )
                stm.execute("DELETE folios_boleta WHERE idalu="+ tblRepEvaluacion.get(i).get("idalu")+" AND CICESCINI="+tblRepEvaluacion.get(i).get("cicescini")
                        +" AND folioLet='"+tblRepEvaluacion.get(i).get("foliolet")+"' AND folionum="+tblRepEvaluacion.get(i).get("folionum") );
                //  MessageDlg('QRY:'+#13+#10+Dm.Q_Can_CerT.SQL.Text, mtWarning, [mbOK], 0);
        }
    }
    
//************************************************************************************************************
//******************************** MÉTODOS USADOS EN EL MÓDULO calibraReporteEvaluacion **********************
//************************************************************************************************************

    public int getNumMats (String modalidad, String cveplan, String grado, String cicescini) throws SQLException
    {
        rs = stm.executeQuery("SELECT count(em.cvemat) AS nummats "
                            + "FROM esquemamaterias em, materias m "
                            + "WHERE em.cvetipmat=m.cvetipmat AND em.cvemat=m.cvemat AND m.matdefault='t' "
                                + "AND em.cveprograma=(SELECT cveprograma "
                                                     + "FROM planmodalidad "
                                                     + "WHERE plan="+cveplan+" AND grado="+grado+" AND "+cicescini+">=cicescini AND "+cicescini+"<=cicescfin "
                                                         + "AND modalidad='"+modalidad+"' AND cveentidad=20) "
                                + "AND em.cveplan="+cveplan+" AND em.grado="+grado+" AND "+cicescini+">=em.cicescini AND "+cicescini+"<=em.cicescfin");
        if (rs.next())
            return rs.getInt("nummats");
        return 0;
    }
    
    public SICEEO_CoordenadasImpre obtenerCoordenadas (String idcct, String modalidad, String cveplan, String grado, String nombreconfig, String formato, String tipoImpresion) throws SQLException
    {
        boolean hayConfigsPropias=false;
        String idconfigsimpre, idclasif="1";
        SICEEO_CoordenadasImpre datosDeCoordenadas=new SICEEO_CoordenadasImpre();
        
        if (cveplan.equals("1") && (modalidad.equals("DPB") || modalidad.equals("DCC")) )  //Para diferenciar las primarias de las primarias indígenas
            idclasif="2";

        rs = stm.executeQuery("SELECT l.idleyenda, t.tamanio, t.fuente, c.ordenimpre, l.leyenda, l.nombreleyenda, l.descleyenda, c.x, c.y " +
                        "FROM reps_coordenadas c, reps_configsimpre i, reps_tipografia t, reps_descleyen l, reps_formatos f " +
                        "WHERE c.idconfigsimpre=i.idconfigsimpre AND c.idfuente=t.idfuente AND c.idleyenda=l.idleyenda AND c.idformato=f.idformato " +
                        "AND i.idcct="+idcct+" AND c.cveplan="+cveplan+" AND c.grado="+grado+" AND c.idclasif="+idclasif+" AND i.nombreconfig='"+nombreconfig+"' AND f.formato='"+formato+"' " +
                        "ORDER BY c.ordenimpre");
        while (rs.next()) {
            datosDeCoordenadas.addDataCoords(rs.getInt("idleyenda"),rs.getInt("tamanio"),rs.getString("fuente"),rs.getInt("ordenimpre"),rs.getString("leyenda"), rs.getString("nombreleyenda"), rs.getString("descleyenda"), rs.getInt("x"),rs.getInt("y"));
            hayConfigsPropias=true;
        }

        if (!hayConfigsPropias)                                             //Si no encontró configuraciones de impresión para su CCT:
        {
            if (tipoImpresion.equals("caidaDeTextosPrueba"))                     //tomamos las de DAI
            {
                rs = stm.executeQuery("SELECT l.idleyenda, t.tamanio, t.fuente, c.ordenimpre, l.leyenda, l.nombreleyenda, l.descleyenda, c.x, c.y " +
                                "FROM reps_coordenadas c, reps_configsimpre i, reps_tipografia t, reps_descleyen l, reps_formatos f " +
                                "WHERE c.idconfigsimpre=i.idconfigsimpre AND c.idfuente=t.idfuente AND c.idleyenda=l.idleyenda AND c.idformato=f.idformato " +
                                "AND i.idcct=0 AND c.cveplan="+cveplan+" AND c.grado="+grado+" AND c.idclasif="+idclasif+" AND i.nombreconfig='DAI' AND f.formato='"+formato+"' " +
                                "ORDER BY c.ordenimpre");
            }else if (tipoImpresion.equals("real"))                         //Creamos una configuración basada en DAI
            {
                nombreconfig = nombreconfig.equals("")?"Config Impre 1":nombreconfig;
                rs = stm.executeQuery("SELECT idconfigsimpre FROM reps_configsimpre WHERE idcct="+idcct+" AND nombreconfig='"+nombreconfig+"'");
                if (!rs.next()){
                    stm.execute("INSERT INTO reps_configsimpre VALUES ((SELECT max(idconfigsimpre)+1 FROM reps_configsimpre),"+idcct+", '"+nombreconfig+"')");
                    rs = stm.executeQuery("SELECT idconfigsimpre FROM reps_configsimpre WHERE idcct="+idcct+" AND nombreconfig='"+nombreconfig+"'");
                    rs.next();
                }
                idconfigsimpre = rs.getString("idconfigsimpre");
                rs = stm.executeQuery("SELECT idleyenda FROM reps_coordenadas WHERE cveplan="+cveplan+" AND grado="+grado+" AND idconfigsimpre="+idconfigsimpre+" AND idleyenda=1"); //Verificamos si hay coordenadas para ese formato
                if (!rs.next())
                    stm.execute("INSERT INTO reps_coordenadas (idclasif,cveplan, grado, idconfigsimpre, idfuente,idformato,ordenimpre, idleyenda, x, y) SELECT idclasif, "+cveplan+", "+grado+", "+idconfigsimpre+", idfuente, idformato,ordenimpre,idleyenda, x, y FROM reps_coordenadas WHERE  cveplan="+cveplan+" AND grado="+grado+" AND idclasif="+idclasif+" AND idconfigsimpre=1 ");

                rs = stm.executeQuery("SELECT l.idleyenda, t.tamanio, t.fuente, c.ordenimpre, l.leyenda, l.nombreleyenda, l.descleyenda, c.x, c.y " +
                        "FROM reps_coordenadas c, reps_configsimpre i, reps_tipografia t, reps_descleyen l, reps_formatos f " +
                        "WHERE c.idconfigsimpre=i.idconfigsimpre AND c.idfuente=t.idfuente AND c.idleyenda=l.idleyenda AND c.idformato=f.idformato " +
                        "AND i.idcct="+idcct+" AND c.cveplan="+cveplan+" AND c.grado="+grado+" AND i.nombreconfig='"+nombreconfig+"' AND f.formato='"+formato+"' " +
                        "ORDER BY c.ordenimpre");
            }

            while (rs.next()) {
                datosDeCoordenadas.addDataCoords(rs.getInt("idleyenda"),rs.getInt("tamanio"),rs.getString("fuente"),rs.getInt("ordenimpre"),rs.getString("leyenda"), rs.getString("nombreleyenda"), rs.getString("descleyenda"), rs.getInt("x"),rs.getInt("y"));
            }
        }
            
        return datosDeCoordenadas;
    }
    
    /*public ArrayList<String[]> getConfigsImpreDelCCT (String idcct) throws SQLException
    {
        ArrayList<String[]> nombresConfigs = new ArrayList<String[]>();
        String[] fila;
        rs = stm.executeQuery("SELECT idconfigsimpre, nombreconfig FROM reps_configsimpre WHERE idcct="+idcct);
        while (rs.next()){
            fila = new String[2];
            fila[0]=rs.getString("idconfigsimpre");
            fila[1]=rs.getString("nombreconfig");
            nombresConfigs.add(fila);
        }
        return nombresConfigs;
    }*/
    
    public void guardarDatosDeCalibracion (String[] coordenadas, String idcct, String modalidad, String cveplan, String grado, String nombreconfig, String formato) throws SQLException
    {
        String[] datos;
        int tamfilas;
        String idconfigsimpre, idclasif = "1";
        tamfilas = coordenadas.length;
        
        if (cveplan.equals("1") && (modalidad.equals("DPB") || modalidad.equals("DCC")) )  //Para diferenciar las primarias de las primarias indígenas
            idclasif="2";
        
        //--------------- Obtenemos el id de nombreconfig, si no existe lo creamos
        rs = stm.executeQuery("SELECT idconfigsimpre FROM reps_configsimpre WHERE idcct="+idcct+" AND nombreconfig='"+nombreconfig+"'");
        if (!rs.next()){
            stm.execute("INSERT INTO reps_configsimpre VALUES ((SELECT max(idconfigsimpre)+1 FROM reps_configsimpre),"+idcct+", '"+nombreconfig+"')");
            rs = stm.executeQuery("SELECT idconfigsimpre FROM reps_configsimpre WHERE idcct="+idcct+" AND nombreconfig='"+nombreconfig+"'");
            rs.next();
        }
        idconfigsimpre = rs.getString("idconfigsimpre");
        //--------------- Le creamos sus coordenadas default para el documento
        rs = stm.executeQuery("SELECT idleyenda FROM reps_coordenadas WHERE cveplan="+cveplan+" AND grado="+grado+" AND idclasif="+idclasif+" AND idconfigsimpre="+idconfigsimpre+" AND ordenimpre=1"); //Verificamos si hay coordenadas para ese formato
        if (!rs.next())
            stm.execute("INSERT INTO reps_coordenadas (idclasif, cveplan, grado, idconfigsimpre, idfuente,idformato,ordenimpre, idleyenda, x, y) "
                    + "SELECT idclasif, "+cveplan+", "+grado+", "+idconfigsimpre+", idfuente, idformato,ordenimpre,idleyenda, x, y "
                    + "FROM reps_coordenadas "
                    + "WHERE  cveplan="+cveplan+" AND grado="+grado+" AND idclasif="+idclasif+" AND idconfigsimpre=1 "); //Escogemos las coordenadas default (DAI)
        //--------------- Hacemos la actualización de las coordenadas            
        for (int i=0; i<tamfilas; i++){
            datos = coordenadas[i].split("-");
            stm.execute("UPDATE reps_coordenadas SET x="+datos[1]+", y="+datos[2]+" WHERE cveplan="+cveplan+" AND grado="+grado+"  AND idconfigsimpre="+idconfigsimpre+" AND idconfigsimpre="+idconfigsimpre+" AND idleyenda="+datos[0]);
        }
    }

//************************************************************************************************************
//*********************************** MÉTODOS USADOS EN EL MÓDULO XBimOPromFin ***********************************
//************************************************************************************************************
    public ArrayList <Map> matCalif (String idalu, String cicescini) throws SQLException
    {
        rs = stm.executeQuery( "SELECT q.ordenimpres, m.idalu, m.cvemat, m.exm,  e.desmat, "
                            + "m.promedio, m.EstatMat,m.cicescini,m.cveprograma,m.cvetipmat,m.grupotaller "
                        + "FROM alumnomaterias m,  materias e, esquemamaterias q "
                        + "WHERE m.cvemat=e.cvemat AND m.cvetipmat=e.cvetipmat AND m.cvemat=q.cvemat AND m.cvetipmat=q.cvetipmat "
                            + "AND m.grado=q.grado AND m.cveprograma=q.cveprograma "
                            + "AND m.idalu="+idalu+" "
                            + "AND m.cicescini="+cicescini+" "
                            + "AND q.CICESCINI<="+cicescini+" "
                            + "AND q.CICESCFIN>="+cicescini+" "
                            + "AND m.cvemat NOT LIKE 'NAP%' "
                            + "ORDER BY q.ordenimpres");
        
        return qryToArrlmap (rs, null, true, 0);
    }
    
//************************************************************************************************************
//********************************* MÉTODOS USADOS EN EL MÓDULO CalifSecXBim *********************************
//************************************************************************************************************
    public void actualizarEvaluaciones (Map tblMatCalifXBim, String tblMatCalifXBim_calif1_OldValue, boolean existeCalif2, String tblMatCalifXBim_calif2_OldValue,
            Map v, String txtUsuario, String califCicEscIn, String calProm, SICEEO_DataModule dm) throws SQLException
    {
        String calif2;
        String qryMatCicAntU;
        Map QPromXMatBim;
        
        if ( existeCalif2 && dm.toFloat(tblMatCalifXBim.get("calif2"))>=5 )
            calif2=" Calif2= "+ tblMatCalifXBim.get("calif2")+", ";
        else
            calif2=" Calif2= 0.0, "; //esto pork hay bimestres generados antes y calif2 es null
     //----------------------------------------------------------------------------------
              //dm.toFloat(tblMatCalifXBim.get("calif1"))!=tblMatCalifXBim_calif1_OldValue
        if ( !tblMatCalifXBim.get("calif1").equals(tblMatCalifXBim_calif1_OldValue) || (existeCalif2 && !tblMatCalifXBim.get("calif2").equals(tblMatCalifXBim_calif2_OldValue)) )
            stm.execute("UPDATE Evaluaciones SET Calif1="+tblMatCalifXBim.get("calif1")+","+calif2+" Usuario='"+txtUsuario.toUpperCase()+"', "
                            +"Fecha=date(current), Hora=extend(current, hour to minute) "
                       +"WHERE idalu = "+tblMatCalifXBim.get("idalu")+" AND cveMat='"+tblMatCalifXBim.get("cvemat")+"' "
                            +"AND cveTipMat='"+tblMatCalifXBim.get("cvetipmat")+"' AND cicescini="+ califCicEscIn+" "
                            +"AND numeval="+ tblMatCalifXBim.get("numeval"));
     //----------------------------------------------------------------------------------
        if ( calProm.equals("si") ) //el usuario selecciono CALCULAR PROMEDIOS
        {
            //CHEKAR SI ESA MATERIA TIENE YA SUS 3 TRIMESTRES ------------------------------------------------------------------------------
            //despues de guardar la calif del bimestre de una materia
            //chekamos esa materia si ya tiene sus 3 trim capturados
            //-------------- trae solo un registro de una materia y su promedio de esa materia ----------------\\
            QPromXMatBim = promXMatBim (""+tblMatCalifXBim.get("cicescini"), ""+tblMatCalifXBim.get("grado"), ""+tblMatCalifXBim.get("cveplan"), ""+tblMatCalifXBim.get("idalu"), ""+tblMatCalifXBim.get("cvemat"), ""+tblMatCalifXBim.get("cvetipmat"),califCicEscIn );
            qryMatCicAntU = "UPDATE AlumnoMATERIAS ";
            
            if ( QPromXMatBim.get("nobim").equals("3"))
            {
                if(dm.toFloat(QPromXMatBim.get("promxmat"))>= 5 ) {
                    qryMatCicAntU+="SET promedio= "+QPromXMatBim.get("promxmat")+ ", ";
                    if ( dm.toFloat(QPromXMatBim.get("promxmat"))<6.0 )
                    {
                        qryMatCicAntU+="estatmat='R', ";
                        v.put("matRepGdo", (Integer)v.get("matRepGdo")+1);
                        //agregado para primaria
                        if ( QPromXMatBim.get("cvemat").equals("001") ) v.put("esp","R");
                        if ( QPromXMatBim.get("cvemat").equals("002") ) v.put("mat","R");
                    }  else {
                        qryMatCicAntU+="estatmat='A', ";
                        v.put("matAprobGdo", (Integer)v.get("matAprobGdo")+1);
                    }

                    v.put("sumCalif", dm.toBigDecimal(v.get("sumCalif")).add(dm.toBigDecimal(QPromXMatBim.get("promxmat"))).toPlainString() );
                    v.put("noMat", (Integer)v.get("noMat") + 1);
                }
                else if (dm.toFloat(QPromXMatBim.get("promxmat"))==0.0) { //Caso para cuando todos sus trimestres tienen 0.0
                    qryMatCicAntU+="SET promedio= 0.0, ";
                    qryMatCicAntU+="estatmat='A', ";
                    //v.put("matRepGdo", (Integer)v.get("matRepGdo")+1);  // cuando promedio de materia es igual a =0 no se contabiliza como reprobadas.
                    //agregado para primaria
                    if ( QPromXMatBim.get("cvemat").equals("001") ) v.put("esp","R");
                    if ( QPromXMatBim.get("cvemat").equals("002") ) v.put("mat","R");
                    //v.put("sumCalif", "" );
                    v.put("promCero","1");
                }

            } else //aun no tiene sus 3 trimestres
                qryMatCicAntU+=" SET promedio=0.0, estatmat='A', ";

            qryMatCicAntU+=" Usuario='"+txtUsuario.toUpperCase()+"', Fecha=TO_CHAR(date(current), \"%Y-%m-%d\"), Hora=extend(current, hour to minute) "  //Fecha=date(current)
                +" WHERE idalu="+tblMatCalifXBim.get("idalu")+" AND cveMat='"+tblMatCalifXBim.get("cvemat")+"' "
                +" AND cveTipMat='"+tblMatCalifXBim.get("cvetipmat")+"' AND cicescini="+ califCicEscIn;
            stm.execute(qryMatCicAntU);
        }   //el usuario selecciono calcular promedio
    }
    
    private Map promXMatBim (String cicescini, String grado, String cveplan, String idalu, String cvemat, String cvetipmat, String cicescini_act) throws SQLException
    {
        Map QPromXMatBim;
                
        rs = stm.executeQuery("SELECT idalu, cveplan, grado, cicescini, cvetipmat, cvemat, " +
                        "( SELECT "
                                + "CASE WHEN calif1 >=6.0 THEN calif1 "
                                + "ELSE "
                                    + "CASE WHEN calif2>=6.0 THEN calif2 "
                                    + "ELSE calif1 "
                                    + "END "
                                +"END " 
                        + ((!cicescini_act.isEmpty() && Integer.parseInt(cicescini) < Integer.parseInt(cicescini_act)) ? //agregado para caso de modulo de extraordinarios
                        " FROM evaluaciones_historial e " : " FROM evaluaciones e " )
                        + "WHERE e.idalu = m.idalu "
                                + "AND   e.cicescini = m.cicescini "
                                + "AND   e.cvetipmat = m.cvetipmat "
                                + "AND   e.cvemat    = m.cvemat "
                                + "AND   e.numeval   = 1 "
                        + ") AS califbim1,"

                        + "(SELECT "
                                + "CASE WHEN CALIF1 >=6.0 THEN calif1 "
                                + "ELSE "
                                    + "CASE WHEN calif2>=6.0 THEN calif2 "
                                    + "ELSE calif1 "
                                    + "END "
                                + "END "
                        + ((!cicescini_act.isEmpty() && Integer.parseInt(cicescini) < Integer.parseInt(cicescini_act)) ?  //agregado para caso de modulo de extraordinarios
                        " FROM evaluaciones_historial e " : " FROM evaluaciones e " )
                                + "WHERE e.idalu     = m.idalu "
                                + "AND   e.cicescini = m.cicescini "
                                + "AND   e.cvetipmat = m.cvetipmat "
                                + "AND   e.cvemat    = m.cvemat "
                                + "AND   e.numeval   = 2 "
                        + ") AS califbim2, "

                        + "(SELECT "
                                + "CASE WHEN CALIF1 >=6.0 THEN calif1 "
                                + "ELSE "
                                    + "CASE WHEN calif2>=6.0 THEN calif2 "
                                    + "ELSE calif1 "
                                    + "END "
                                + "END "
                        + ((!cicescini_act.isEmpty() && Integer.parseInt(cicescini) < Integer.parseInt(cicescini_act)) ?  //agregado para caso de modulo de extraordinarios
                        " FROM evaluaciones_historial e " : " FROM evaluaciones e " )
                                + "WHERE e.idalu     = m.idalu "
                                + "AND   e.cicescini = m.cicescini "
                                + "AND   e.cvetipmat = m.cvetipmat "
                                + "AND   e.cvemat    = m.cvemat "
                                + "AND   e.numeval   = 3 "
                        + ") AS califbim3 "    //, "

                        /*+ "(SELECT "
                                + "CASE WHEN CALIF1 >=6.0 THEN calif1 "
                                + "ELSE "
                                    + "CASE WHEN calif2>=6.0 THEN calif2 "
                                    + "ELSE calif1 "
                                    + "END "
                                + "END "
                        + "FROM evaluaciones e WHERE e.idalu     = m.idalu "
                                + "AND   e.cicescini = m.cicescini "
                                + "AND   e.cvetipmat = m.cvetipmat "
                                + "AND   e.cvemat    = m.cvemat "
                                + "AND   e.numeval   = 4 "
                        + ") AS califbim4, "

                        + "(SELECT CALIF1 "
                        + "FROM evaluaciones e WHERE e.idalu     = m.idalu "
                                + "AND   e.cicescini = m.cicescini "
                                + "AND   e.cvetipmat = m.cvetipmat "
                                + "AND   e.cvemat    = m.cvemat "
                                + "AND   e.numeval   = 5 "
                        + ") AS califbim5 " */

                        + "FROM ALUMNOMATERIAS m "
                        + "WHERE cicescini = "+cicescini
                            + " AND grado     = "+grado
                            + " AND cveplan   = "+cveplan
                            + " AND idalu     = "+idalu
                            + " AND cvemat    = '"+cvemat+"' "
                            + " AND cvetipmat = '"+cvetipmat+"' ");
        
        QPromXMatBim = qryToMap (rs, null, true, 0);
        promXMatBim_CalcField_promXmat_noBim (QPromXMatBim);
        return QPromXMatBim;
    }
    
    private void promXMatBim_CalcField_promXmat_noBim (Map QPromXMatBim)
    {
        float dato1;
        String dato3, dato4;
        SICEEO_DataModule dm = new SICEEO_DataModule();
        
        if(QPromXMatBim.get("califbim1")==null || QPromXMatBim.get("califbim2")==null || QPromXMatBim.get("califbim3")==null) {
            QPromXMatBim.put("nobim","0");
            QPromXMatBim.put("promxmat","0.0"); 
        }
        else {    
            if ( dm.toFloat(QPromXMatBim.get("califbim1"))>=5.0 && dm.toFloat(QPromXMatBim.get("califbim2"))>=5.0 && dm.toFloat(QPromXMatBim.get("califbim3"))>=5.0 )//&& dm.toFloat(QPromXMatBim.get("califbim4"))>=5.0 && dm.toFloat(QPromXMatBim.get("califbim5"))>=5.0 )
            {
                dato1= (   dm.toFloat(QPromXMatBim.get("califbim1")) * 10+
                           dm.toFloat(QPromXMatBim.get("califbim2")) * 10+
                           dm.toFloat(QPromXMatBim.get("califbim3")) * 10 //+
                           //dm.toFloat(QPromXMatBim.get("califbim4")) * 10+
                           //dm.toFloat(QPromXMatBim.get("califbim5")) * 10
                        )/3;  //5;

                dato3 = ""+(dato1/10);
                dato4 = dato3.substring(0,3);
                QPromXMatBim.put("promxmat", dato4);
                QPromXMatBim.put("nobim","3");
            } else 
                QPromXMatBim.put("promxmat","0.0");            

            if((""+QPromXMatBim.get("califbim1")).trim().equals("0.0") || (""+QPromXMatBim.get("califbim2")).trim().equals("0.0") || (""+QPromXMatBim.get("califbim3")).trim().equals("0.0"))
                QPromXMatBim.put("nobim","3");
        }
    }
     
    public ArrayList<Map> matCalifXBim (String numeval, String idalu, String cicescini, String cveplan) throws SQLException
    {
        ArrayList<Map> QMatCalifXBim = new ArrayList<Map>();
        Map fila;
        String columna, dato;//, sinIngles="";        
        /*if(estatusIng.toUpperCase().equals("FALSE") && !cveplan.equals("2") && condicionIng)
            sinIngles = " AND e.desmat NOT LIKE '%INGLÉS%' ";*/
        
        rs = stm.executeQuery("SELECT q.ordenimpres, v.idalu, v.cvemat, e.desmat, "
            + "CAST(v.calif1 AS INTEGER) as calif1, CAST(v.calif2 AS INTEGER) as calif2, v.cicescini, v.cveprograma, v.cvetipmat, v.numeval, v.cveplan, v.grado "
            + "FROM evaluaciones v,  materias e, esquemamaterias q "
            + "WHERE q.estatus='A' AND v.cvemat=e.cvemat AND v.cvetipmat = e.cvetipmat AND v.cvemat=q.cvemat AND v.cvetipmat = q.cvetipmat AND v.grado=q.grado AND v.cveprograma=q.cveprograma "
                + " AND v.numeval="+numeval
                + " AND v.idalu="+idalu
                + " AND v.cicescini="+cicescini
                + " AND v.cvemat NOT LIKE 'NAP%' " // + sinIngles
            + "ORDER BY q.ordenimpres");
        
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCols=rsmd.getColumnCount();
        
        while (rs.next())                                                //Recorremos todas las filas
        {
            fila = new HashMap();
            for (int posCol=1; posCol<=numCols; posCol++){                      //Para cada columna
                columna = rsmd.getColumnName(posCol).toLowerCase();
                dato = rs.getString(posCol);
                if (Integer.parseInt(cicescini)<2013 && dato!=null && (columna.equals("calif1") || columna.equals("calif2")) )
                    dato = dato.replace(".0", "");
                fila.put(columna, ""+((dato==null)?dato:dato.trim()));
            }            
            QMatCalifXBim.add(fila);
        }
        return QMatCalifXBim;
    }
    
    public ArrayList<Map> chkCalifMat (String cicescini, String grado, String cveplan, String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT idalu, cveplan, grado, cicescini, cvetipmat, cvemat, count(numeval) AS noBim "
            + " FROM evaluaciones "
            + " WHERE cicescini="+cicescini+" AND grado="+grado+" AND cveplan="+cveplan+" AND idalu=" + idalu + " AND calif1>0 " // AND calif1>0 Se comento para ciclo 2020 que acepta 0 ceros
            + " GROUP BY idalu, cveplan, grado, cicescini, cvetipmat, cvemat");                                                  // *Volvi a descomentar para 21-22 porque con materias generadas en 
                                                                                                                                 // 3er Periodo en cero pone C.   
        
        return qryToArrlmap (rs, null, false, 0);
    }
    
    public int getPorcAsistencia (String cicescini, String grado, String cveplan, String idalu) throws SQLException
    {
        //int porcentaje = 0;
        SICEEO_DataModule dm = new SICEEO_DataModule();
        float porcentaje;
        
        String total_asist = ""+getData("SELECT sum(inst1+inst2+inst3) as inasistencias"
            + " FROM inasistencias "
            + " WHERE cicescini="+cicescini+" AND grado="+grado+" AND cveplan="+cveplan+" AND idalu="+idalu+" ");
        total_asist = (total_asist.equals("") || total_asist=="null" || total_asist.equals("null"))? "0": total_asist;
        
        int inasistencias = Integer.parseInt(total_asist);
        
        porcentaje = dm.toFloat((195-Float.parseFloat(""+inasistencias))*100/195);
                        
        return Math.round(porcentaje);
    }
            
    public ArrayList<Map> alumCapCalif (String estatusAlu, String cicescini, String idcct, String grado, String grupo) throws SQLException, SICEEO_Excepcion
    {
        /*Map fila;
        ArrayList<Map> QAlumCapCalif = new ArrayList<Map>();
        ResultSetMetaData rsmd;
        int numCols;*/
        try {
            rs = stm.executeQuery("SELECT "
                /*+ "(SELECT ((CAST(SUBSTR(CAST(cicescinilib+1 AS  char(4)),4,1) AS int)*1000000000)+200000000 + folionum) "
                + "FROM FOLIOS_IMPRE WHERE folios_impre.idalu = a.idalu AND folios_impre.cveplan= g.cveplan AND folios_impre.cicescini=g.cicescini "
                + " AND folios_impre.estatus='AC' ) AS folio, "*/

                + "(SELECT Foliolet "
                + "FROM FOLIOS_IMPRE "
                + "WHERE folios_impre.idalu=a.idalu AND folios_impre.cveplan=g.cveplan AND folios_impre.cicescini=g.cicescini "
                + " AND folios_impre.estatus='AC' ) AS FolioLet, "

                + "(SELECT FolioNum "
                + "FROM FOLIOS_IMPRE "
                + "WHERE folios_impre.idalu=a.idalu AND folios_impre.cveplan=g.cveplan AND folios_impre.cicescini=g.cicescini "
                + " AND folios_impre.estatus='AC' ) AS FolioReal, "

                + "(SELECT "
                    + "CASE "
                    + "WHEN length(TRIM(CAST( folionum AS char(7)))) = 1 THEN SUBSTR(Foliolet,2,1) || '000000' || TRIM(CAST( folionum AS char(7))) "
                    + "WHEN length(TRIM(CAST( folionum AS char(7)))) = 2 THEN SUBSTR(Foliolet,2,1) || '00000' || TRIM(CAST( folionum AS char(7))) "
                    + "WHEN length(TRIM(CAST( folionum AS char(7)))) = 3 THEN SUBSTR(Foliolet,2,1) || '0000' || TRIM(CAST( folionum AS char(7))) "
                    + "WHEN length(TRIM(CAST( folionum AS char(7)))) = 4 THEN SUBSTR(Foliolet,2,1) || '000' || TRIM(CAST( folionum AS char(7))) "
                    + "WHEN length(TRIM(CAST( folionum AS char(7)))) = 5 THEN SUBSTR(Foliolet,2,1) || '00' || TRIM(CAST( folionum AS char(7))) "
                    + "WHEN length(TRIM(CAST( folionum AS char(7)))) = 6 THEN SUBSTR(Foliolet,2,1) || '0' || TRIM(CAST( folionum AS char(7))) "
                    + "WHEN length(TRIM(CAST( folionum AS char(7)))) = 7 THEN SUBSTR(Foliolet,2,1) || TRIM(CAST( folionum AS char(7))) "
                    + "END "
                + "FROM folios_boleta "
                + "WHERE folios_boleta.idalu = a.idalu AND folios_boleta.estatus='I' AND folios_boleta.cveplan= g.cveplan AND folios_boleta.cicescini=g.cicescini "
                + ") AS Fol_Bole, "

                + "(SELECT foliodigital "
                    + "FROM firma_elec f,folios_impre fi "
                    + "WHERE f.idalu=fi.idalu AND f.cicescinilib = fi.cicescinilib "
                    + "AND f.folionum_cer = fi.folionum AND fi.idalu=a.idalu "
                    + "AND fi.cveplan=g.cveplan AND fi.cicescini=g.cicescini  "
                    + "AND fi.estatus='AC') AS frma, "    
                    
                + "g.idcct, g.idalu, grado, grupo, a.curp, a.apepat, a.apemat, a.nombre, a.estatusalu, a.fecnac, a.sexo, "
                + "g.cicescini, g.cicescfin, Repetidor, EstatusGrado, promovido, repecont, "

                + "(SELECT cicescinilib "
                + "FROM FOLIOS_IMPRE "
                + "WHERE folios_impre.idalu=a.idalu AND folios_impre.cveplan=g.cveplan AND folios_impre.cicescini=g.cicescini "
                + " AND folios_impre.estatus='AC' ) AS cicescinilib, "

                + " 0 AS cicescfinLib, "
                + "(SELECT CAST(matRepAct AS int) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 1 AND cveplan=2 AND (estatusgrado='RE' OR estatusgrado='R'  OR estatusgrado='P')) AS Debe1ro, "
                + "(SELECT CAST(matRepAct AS int) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 2 AND cveplan=2 AND (estatusgrado='RE' OR estatusgrado='R'  OR estatusgrado='P')) AS Debe2do, "
                + "Matrepact, "
                + "SUBSTR(fecnac,9,2)||SUBSTR(fecnac,4,2)||SUBSTR(fecnac,1,2) AS FechaCadena, "
                + "(TRIM(TRAILING ' ' FROM NVL(a.apepat,'')) || '/' || TRIM(TRAILING ' ' FROM NVL(a.apemat,'')) || '*' || TRIM(TRAILING ' ' FROM NVL(a.nombre,''))) AS nom_tot, "
                + "(CASE WHEN repetidor ='R' THEN 'R'  ELSE '' END) AS C_rep, "

                + "0.0 AS Mat1, "
                + "0.0 AS Mat2, "
                + "0.0 AS Mat3, "
                + "0.0 AS Mat4, "
                + "0.0 AS Mat5, "
                + "0.0 AS Mat6, "
                + "0.0 AS Mat7, "
                + "0.0 AS Mat8, "
                + "0.0 AS Mat9, "
                + "0.0 AS Mat10, "
                + "0.0 AS Mat11, "
                + "0.0 AS Mat12, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu = g.idalu AND gr.estatusgrado ='RE' AND gr.cveplan=1 AND gr.grado=1) > 1 "
                    + "THEN -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado ='RE' AND gp.cveplan=1 AND gp.grado=1)"
                    + "END "
                + "),-11.0) AS Promd1roP, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu=g.idalu AND gr.estatusgrado='RE' AND gr.cveplan=2 AND gr.grado=1) > 1 "
                    + "THEN  -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado='RE' AND gp.cveplan=2 AND gp.grado=1) "
                    + "END "
                + "),-11.0) AS Promd1roS, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu=g.idalu AND gr.estatusgrado='RE' AND gr.cveplan=1 AND gr.grado=2) > 1 "
                    + "THEN  -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado='RE' AND gp.cveplan=1 AND gp.grado=2) "
                    + "END "
                + "),-11.0) AS Promd2doP, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu=g.idalu AND gr.estatusgrado='RE' AND gr.cveplan=2 AND gr.grado=2) > 1 "
                    + "THEN  -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado ='RE' AND gp.cveplan=2 AND gp.grado=2) "
                    + "END "
                + "),-11.0) AS Promd2doS, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu = g.idalu AND   gr.estatusgrado ='RE' AND   gr.cveplan=1 AND   gr.grado  =3) > 1 "
                    + "THEN  -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND   gp.estatusgrado ='RE' AND   gp.cveplan=1 AND   gp.grado  =3) "
                    + "END "
                + "),-11.0) AS Promd3roP, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu=g.idalu AND gr.estatusgrado='C' AND gr.cveplan=2 AND gr.grado=3) > 1 "
                    + "THEN  -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado='C' AND gp.cveplan=2 AND gp.grado=3) "
                    + "END "
                + "),-11.0) AS Promd3roS, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu = g.idalu AND gr.estatusgrado ='RE' AND gr.cveplan=1 AND gr.grado=4) > 1 "
                    + "THEN  -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado='RE' AND gp.cveplan=1 AND gp.grado=4) "
                    + "END "
                + "),-11.0) AS Promd4toP, "

                + "NVL((CASE "
                    + "WHEN (SELECT COUNT(IDALU) FROM ALUMNOGRADO gr WHERE gr.idalu=g.idalu AND gr.estatusgrado='RE' AND gr.cveplan=1 AND gr.grado=5) > 1 "
                    + "THEN  -11.0 "
                    + "ELSE (SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado ='RE' AND gp.cveplan=1 AND gp.grado  =5) "
                    + "END "
                + "),-11.0) AS Promd5toP, "

                + "(SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado ='C' AND gp.cveplan=1 AND gp.grado=6) AS Promd6toP, "
                + "(SELECT promedioGral FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado='C' AND gp.grado=6) AS PromdP, "
                + "(SELECT sum(MatRepAct) FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado<>'RC' AND gp.estatusgrado<>'BD' AND gp.cicescini<g.cicescini AND gp.cveplan=2) AS MatRepEnSecu, "
                + "promedioEb, Promedio, PromedioGral, PromedioAnt, "

                + "(CASE WHEN g.cvedefsuf ='N' THEN '' ELSE g.cvedefsuf END) AS Discap, "
                + "cveplan,cveprograma, probem, extranjero, g.cp AS cp_, SUBSTR(g.cp,2,1) AS guion_ "

                + "FROM alumnogrado g, alumno a "
                + "WHERE g.idalu=a.idalu AND (g.estatusGrado <>'BD' AND g.estatusGrado <>'RG') AND "
                    + "(a.estatusalu ='I' OR a.estatusalu = '"+estatusAlu+"') "
                    + " AND g.cicescini  = "+cicescini
                    + " AND g.idcct = "+idcct
                    + " AND g.Grado = "+grado
                    + " AND g.Grupo = '"+grupo+"'"
                + " ORDER BY a.apepat, a.apemat, a.nombre, a.curp");

            /*rsmd = rs.getMetaData();
            numCols = rsmd.getColumnCount();
            while (rs.next())
            {
                fila = new HashMap();
                for (int posCol=1; posCol<=numCols; posCol++)
                    fila.put(rsmd.getColumnName(posCol).toLowerCase(), (rs.getString(posCol)==null)?"null":rs.getString(posCol));
                QAlumCapCalif.add(fila);
            }*/
            
            //return QAlumCapCalif;
        }catch (SQLException ex){
            throw new SICEEO_Excepcion (-2,"ALU_ERROR_HISTORIAL");
        }
        return qryToArrlmap(rs, null, true, 1);
    }
    
    public ArrayList<Map> susMaterias (String cicescini, String idalu) throws SQLException
    {
        rs = stm.executeQuery(" SELECT m.idalu, m.cvetipmat, m.cvemat,m.cveplan,m.grado, "
                                + " (select e.desmat FROM materias e WHERE m.cvemat=e.cvemat AND m.cvetipmat=e.cvetipmat) AS DesMat, "
                                + " m.promedio, m.cicescini, cvetipmat||cvemat||cveprograma as Tip_Mat_Prg, "
                                + " m.cveprograma "
                            + " FROM alumnomaterias m "
                            + " WHERE cicescini="+cicescini+" AND idalu="+idalu);
        return qryToArrlmap(rs, null, true, 0);
    }
    
    public ArrayList<Map> susEval (String cicescini, String idalu, String bim) throws SQLException
    {
        rs = stm.executeQuery("SELECT distinct m.idalu, m.cvetipmat, m.cvemat, "
                                + "(SELECT e.desmat FROM materias e WHERE m.cvemat=e.cvemat AND m.cvetipmat=e.cvetipmat) AS DesMat, "
                                + "m.cicescini, cveprograma, cvetipmat||cvemat||cveprograma as Tip_Mat_Prg "
                            + "FROM evaluaciones m "
                            + "WHERE cicescini="+cicescini+" AND idalu="+idalu+" "
                                + "AND trim(cvetipmat)||trim(cvemat)||trim(cveprograma) NOT IN (SELECT trim(cvetipmat)||trim(cvemat)||trim(cveprograma) "
                                                                            + "FROM alumnomaterias "
                                                                            + "WHERE cicescini="+cicescini+" AND idalu="+idalu+") AND numeval="+bim);
        return qryToArrlmap(rs, null, true, 0);
    }
    
    public void actualizaSusEvaluaciones (ArrayList<Map> QSusMaterias, ArrayList<Map> QSusEval, SICEEO_DataModule dm, String bim, String txtUsuario) throws SQLException
    {
        int QSusMats_numFilas = QSusMaterias.size(), posQSusEval;
        
        if(!QSusEval.isEmpty()) {
            for (int i=0; i<QSusMats_numFilas; i++)
            {
                if ( -1 != (posQSusEval=dm.indexOfArrMap(QSusEval, new String[]{"desmat"}, new Object[]{QSusMaterias.get(i).get("desmat")})))
                {
                    try {
                        stm.execute("UPDATE evaluaciones "
                                  + "SET cvetipmat='"+QSusMaterias.get(i).get("cvetipmat")+"', cveMat='"+QSusMaterias.get(i).get("cvemat")+"', "
                                        + "cveprograma='"+QSusMaterias.get(i).get("cveprograma")+"', "
                                        + "cveplan = "+ QSusMaterias.get(i).get("cveplan") + ", grado = " + QSusMaterias.get(i).get("grado") + ", "
                                        + "usuario = '"+ txtUsuario +"', fecha= TO_CHAR(date(current), \'%Y-%m-%d\'), hora=extend(current, hour to minute)"+" "
                                  + "WHERE cvetipmat='"+QSusEval.get(posQSusEval).get("cvetipmat")+"' AND cveMat='"+QSusEval.get(posQSusEval).get("cvemat")+"' "
                                        + "AND cveprograma='"+QSusEval.get(posQSusEval).get("cveprograma")+"' AND cicescini="+QSusEval.get(posQSusEval).get("cicescini")+" "
                                        + "AND idalu="+QSusEval.get(posQSusEval).get("idalu"));
                    } catch(SQLException ex) {
                        //sino se puede actualizar se elimina
                        stm.execute("DELETE evaluaciones "
                                + "WHERE cvetipmat='"+QSusEval.get(posQSusEval).get("cvetipmat")+"' AND cveMat='"+QSusEval.get(posQSusEval).get("cvemat")+"' "
                                        + "AND cveprograma='"+QSusEval.get(posQSusEval).get("cveprograma")+"' AND cicescini="+QSusEval.get(posQSusEval).get("cicescini")+" "
                                        + "AND idalu="+QSusEval.get(posQSusEval).get("idalu"));
                    }
                }              
            }
        }
    }
    
    public ArrayList<Map> matRepetida(String idalu, String cicescini, String numeval) throws SQLException
    {
        rs = stm.executeQuery("SELECT idalu, numeval, CVETIPMAT, CVEMAT, count(*) AS SeRepite "
                                    + "FROM EVALUACIONES " 
                                    + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" AND numeval="+numeval +" "
                                    + "group by 1,2,3,4 "
                                    + "HAVING COUNT(*) >1 "
                                    + "ORDER BY 1,2");
        
        return qryToArrlmap (rs, null, true, 0);
    }
    
    public ArrayList<Map> matRepet2(String idalu, String cicescini, String numeval) throws SQLException
    {
        rs = stm.executeQuery("SELECT idalu, numeval, CVETIPMAT,  count(*) AS SeRepite "
                + "FROM EVALUACIONES "
                + "WHERE idalu = "+idalu+" AND cicescini= "+cicescini+" AND  numeval = "+numeval+" AND cvetipmat <>'CF1' AND cvetipmat <> 'CF2' AND cvetipmat <> 'CF3' "
                + "GROUP BY 1,2,3 "
                + "HAVING COUNT(*) >1 "
                + "ORDER BY 1,2");
        
        return qryToArrlmap (rs, null, true, 0);
    }
    
    public ArrayList<Map> matksobran (String idalu, String cicescini, String numeval) throws SQLException
    {
        rs = stm.executeQuery("SELECT idalu, numeval, CVETIPMAT, CVEMAT ,cveprograma "
                            + "FROM EVALUACIONES "
                            + "WHERE idalu = "+idalu
                            + " AND cicescini="+cicescini
                            + " AND numeval = "+numeval
                            + " ORDER BY 1,2,3,4");
        return qryToArrlmap (rs, null, true, 0);
    }
   
    public void alumGdo (String prom, String estatusgrado, String promovido, String query, String CalifCicEscIn, String idalu, String usuario) throws SQLException
    {
        stm.execute("UPDATE alumnoGrado SET Promedio="+prom+", estatusgrado='"+estatusgrado+"', Promovido = '"+promovido+"', "+query+" usuario = '"+usuario.toUpperCase()+"', fecha = date(current) , hora = extend(current, hour to minute) "
                + "WHERE cicescini ="+CalifCicEscIn+" AND idalu = "+idalu );
    }
    
    public void eliminaBimestre (String califCicEscIn, String idalu, String bim) throws SQLException
    {
        stm.execute("DELETE evaluaciones WHERE cicescini="+ califCicEscIn+" AND idalu="+idalu+" AND numeval="+bim );
    }
    
    public void eliminaBimestre (String idalu, String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        String qryIdalu="";
        if (!idalu.equals(""))
            qryIdalu = " AND idalu="+idalu;
        
        stm.execute("DELETE evaluaciones "
                  + "WHERE cicescini="+ cicescini+" "
                  + "AND idalu in "
                    + "(SELECT idalu "
                    + "FROM alumnogrado "
                    + "WHERE cicescini="+cicescini+" AND idcct="+idcct+" AND grado="+grado+" AND grupo= '"+grupo+"' " +qryIdalu +") " );
    }
    
    public void insertaEvaluaciones (String bim, String usuario, String idalu, String califCicEscIn, String cvetipmat, String cvemat) throws SQLException
    {
        String qryCvetipmat="", qryCvemat="";
        if (!cvetipmat.equals(""))
            qryCvetipmat = " AND cvetipmat='"+cvetipmat+"' ";
        if (!cvemat.equals(""))
            qryCvemat = " AND cvemat='"+cvemat+"' ";
        
        stm.execute("INSERT INTO evaluaciones (cvetipmat, cvemat, idalu , cicescini, cicescfin, cveplan,cveprograma, grado , numeval, calnum, calif1, calif2,asistencia, usuario, fecha, hora)"
                    + "SELECT cvetipmat, cvemat, idalu , cicescini, cicescfin, cveplan, cveprograma, grado ,"+bim+" AS numeval, 0 AS calnum, "
                        + "0 AS calif1, 0 AS calif2, 0 AS asistencia, '"+usuario.toUpperCase()+"'  AS usuario, "
                        + "TO_CHAR(date(current), \"%Y-%m-%d\") AS fecha, extend(current, hour to minute) AS hora " //date(current)AS fecha
                    + "FROM alumnomaterias "
                    + "WHERE idalu = "+idalu+" AND cicescini= "+califCicEscIn+qryCvetipmat+qryCvemat);
    }
    
    public void generaEvaluaciones (int numLlamada, String bim, String usuario, String idcct, String grado, String grupo, String califCicEscIn) throws SQLException
    {                
        if(numLlamada==1)
            stm.execute(  "INSERT INTO evaluaciones (cvetipmat, cvemat, idalu , cicescini, cicescfin, cveplan, cveprograma, grado , numeval, calnum, calif1, calif2,asistencia, usuario, fecha,  hora) "
                        + "SELECT m.cvetipmat, m.cvemat, m.idalu , m.cicescini, m.cicescfin, m.cveplan, m.cveprograma, m.grado, "+bim+" AS numeval, "
                            + "0 AS calnum, 0 AS calif1, 0 AS calif2, 0 AS asistencia, '"+usuario+"' AS usuario, "
                        + "TO_CHAR(date(current), \"%Y-%m-%d\") AS fecha, extend(current, hour to minute) AS hora "
                        + "FROM alumnomaterias m, alumnogrado g "
                        + "WHERE m.idalu=g.idalu AND m.cicescini=g.cicescini AND m.grado=g.grado AND g.estatusgrado<>'BD' "
                            + "AND g.idcct="+idcct+" AND g.grado="+grado+" AND g.grupo='"+grupo+"' AND g.cicescini="+califCicEscIn);
        else if (numLlamada==2)
            stm.execute("INSERT INTO evaluaciones (cvetipmat, cvemat, idalu , cicescini, cicescfin, cveplan,cveprograma, grado , numeval, calnum, calif1, calif2,asistencia, usuario, fecha,  hora) "
                    + "SELECT m.cvetipmat, m.cvemat, m.idalu , m.cicescini, m.cicescfin, m.cveplan, m.cveprograma, m.grado , "+bim+" AS numeval, "
                        + "0 AS calnum, 0 AS calif1, 0 AS calif2, 0 AS asistencia, '"+usuario+"' AS usuario, "
                    + "TO_CHAR(date(current), \"%Y-%m-%d\") AS fecha, extend(current, hour to minute) AS hora "
                    + "FROM alumnomaterias m, alumnogrado g "
                    + "WHERE m.idalu=g.idalu AND m.cicescini=g.cicescini AND m.grado=g.grado AND g.idcct="+idcct+" "
                    + "AND g.estatusgrado<>'BD' AND g.grado="+grado+" AND g.grupo='"+grupo+"' "
                    + "AND g.idalu NOT IN ("
                                        + "SELECT DISTINCT idalu "
                                        + "FROM evaluaciones e "
                                        + "WHERE  e.grado="+grado+" AND e.cicescini="+califCicEscIn+" AND e.numeval="+bim+" "
                                            + "AND e.idalu in (SELECT idalu FROM alumnogrado g WHERE idcct="+idcct+" AND g.cicescini="+califCicEscIn+" AND g.grado="+grado+" )) "
                    + "AND g.cicescini="+califCicEscIn);
    }
    
    public void actualizarIngles(String idcct, String grado, String grupo, String califCicEscIn, String cveplan,String estatusIng, String inglesActual,String txtUsuario) throws SQLException {
        String strQuery="";
        if(estatusIng.toUpperCase().equals("TRUE") || estatusIng.toUpperCase().equals("FALSE")) {
            stm.execute("UPDATE escuelagpos SET "
                + " ing1eval = "+(estatusIng.toUpperCase().equals("TRUE") ? "'t' " : "'f' ")
                + " WHERE cicescini="+califCicEscIn+" AND idcct="+idcct+" AND grado="+grado+" AND grupo='"+grupo+"'");
        
            if(estatusIng.equals("false") && !cveplan.equals("2")) {
                if(cveplan.equals("1")) {
                    strQuery = "UPDATE evaluaciones SET "
                            + "calif1 = 0.0"                              
                        + " WHERE CVEMAT IN ('084','ING') AND CVETIPMAT='FA' AND CVEPLAN = " + cveplan+"";
                    
                    stm.execute(" UPDATE alumnomaterias SET "
                            + " promedio = 0.0 ,"
                            + " Usuario='"+txtUsuario.toUpperCase()+"', "
                            + " Fecha=date(current), "
                            + " Hora=extend(current, hour to minute) "
                            + " WHERE CVEMAT IN ('084','ING') AND CVETIPMAT='FA' AND CVEPLAN = " + cveplan+""
                            + " AND cicescini = " + califCicEscIn +" AND grado="+grado
                            + " AND idalu IN (SELECT idalu FROM alumnogrado WHERE cicescini= " + califCicEscIn 
                            + " AND grado = " + grado 
                            + " AND grupo = '" + grupo + "' "
                            + " AND idcct = " + idcct 
                            + " AND estatusgrado!='BD' )");    
                                        
                } else if(cveplan.equals("3")) {
                    strQuery = "UPDATE evalpreesc SET "
                            + "avances = '' "                              
                        + " WHERE CVEMAT = 'ING' AND CVETIPMAT='FA' ";
                }
                                                
                strQuery += " AND cicescini = " + califCicEscIn +" AND grado="+grado
                + " AND idalu IN (SELECT idalu FROM alumnogrado WHERE cicescini= " + califCicEscIn 
                    + " AND grado = " + grado 
                    + " AND grupo = '" + grupo + "' "
                    + " AND idcct = " + idcct 
                    + " AND estatusgrado!='BD' )";                                  
                
                stm.execute(strQuery);
                
            }                                                            
        }
    }
    public String getIngles(String cicescini, String grado, String idcct, String grupo) throws SQLException{
        String statusIngles= "";
        statusIngles = this.getData("SELECT ing1eval FROM escuelagpos WHERE idcct = " + idcct
                    + " AND cicescini = "+cicescini +" AND grado = "+grado+" AND grupo='"+grupo+"' ");        
        return statusIngles;
    }
    
    // Se eliminan sus calificaciones en caso de que tengan en alguna evaluacion de todo el grado/grupo.
    public void actualizarCalifIngles(String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String bim, String idalu, 
        String txtUsuario, String estatusIng, String tblPrincipal_cveplan) throws SQLException {        
        
        stm.execute("UPDATE evaluaciones SET"
                + " calif1 = 0.0 "
            + " WHERE cicescini = " + califCicEscIn + " AND CVETIPMAT='FA' "
            + " AND CVEMAT IN ('ING','084') AND CVEPLAN = "+ tblPrincipal_cveplan
            + " AND idalu IN (SELECT idalu FROM alumnogrado WHERE cicescini= " + califCicEscIn 
                + " AND grado = " + tblPrincipal_grado 
                + " AND grupo='" + tblPrincipal_grupo 
                + "' AND idcct= " + tblPrincipal_idcct + ")");                  
                    
    }
    
    public void eliminarEvaluaciones (String idalu, String califCicEscIn, String cvetipmat, String cvemat, String cveprograma, 
            String grado, String numeval) throws SQLException
    {
        String qryCvemat="", qryCveprograma="", qryNumeval="", qryGrado="";
        if (!cvemat.equals(""))
            qryCvemat = " AND cvemat='"+cvemat+"'";
        if (!cveprograma.equals(""))
            qryCveprograma = " AND cveprograma='"+cveprograma+"'";
        if (!grado.equals(""))
            qryGrado = " AND grado="+grado;
        if (!numeval.equals(""))
            qryNumeval = " AND numEval="+numeval;
        
        stm.execute("DELETE evaluaciones WHERE idalu="+idalu+" AND cicescini="+califCicEscIn+" AND cvetipmat='"+cvetipmat+"'" + qryCvemat + qryCveprograma + qryGrado + qryNumeval);
    }
    
    public void actualizaEvaluaciones (String cvemat, String cveprograma, String idalu, String califCicEscIn, String cvetipmat, String Usuario) throws SQLException
    {
        stm.execute("UPDATE evaluaciones SET "
                + "CveMat='"+cvemat+"', CvePrograma='"+cveprograma+"', "
                + "usuario = '"+Usuario+"', fecha= TO_CHAR(date(current), \'%Y-%m-%d\'), hora=extend(current, hour to minute) "       
                + "WHERE idalu="+idalu+" AND cicescini="+califCicEscIn+" AND CVETIPMAT='"+cvetipmat+"'");
    }
    
    public void insertarInasistencias (String idalu, String cicescini, String cveplan, String grado) throws SQLException
    {
        stm.execute("INSERT INTO inasistencias VALUES( "+idalu+", "+cicescini+", "+cveplan+", "+grado+", 0, 0, 0, 0, 0)");
    }
    
    public void guardaInasistencias (String idalu, String cicescini, String tblPrincipal_idcct, String cveplan, String grado, String inst1, String inst2, String inst3, Map oficYdesofic) throws SQLException
    {
        String sqrySETInst;
        
        //Verificamos que los bimestres o evaluaciones que estén oficializados no se actualicen
        // Agregue oficYdesofic.get("bimOf1").equals(true) a cada bimestre para guarde actualizacion de inasistencias.(ely-25-05-18)
        sqrySETInst = oficYdesofic.get("bimOf1").equals(true) || oficYdesofic.get("bimOf1").equals(false) || oficYdesofic.get("alDeofB1").equals(true)?"inst1="+inst1:"";
        sqrySETInst += (oficYdesofic.get("bimOf2").equals(true) || oficYdesofic.get("bimOf2").equals(false) || oficYdesofic.get("alDeofB2").equals(true)? (sqrySETInst.equals("")?"":", ")+"inst2="+inst2:"");
        sqrySETInst += (oficYdesofic.get("bimOf3").equals(true) || oficYdesofic.get("bimOf3").equals(false) || oficYdesofic.get("alDeofB3").equals(true)? (sqrySETInst.equals("")?"":", ")+"inst3="+inst3:"");
        //try { sqrySETInst += (oficYdesofic.get("bimOf4").equals(true) || oficYdesofic.get("bimOf4").equals(false) || oficYdesofic.get("alDeofB4").equals(true)?(sqrySETInst.equals("")?"":", ")+"inst4="+inst4:""); } catch (NullPointerException ex){}
        //try { sqrySETInst += (oficYdesofic.get("bimOf5").equals(true) || oficYdesofic.get("bimOf5").equals(false) || oficYdesofic.get("alDeofB5").equals(true)?(sqrySETInst.equals("")?"":", ")+"inst5="+inst5:""); } catch (NullPointerException ex){}
        
        if (!sqrySETInst.trim().equals("")) 
            stm.execute("UPDATE inasistencias SET "+sqrySETInst+" "
                    + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" AND cveplan="+cveplan+" AND grado="+grado);
    }
    public void guardaComDocAlum (String idalu, String cicescini, String trim1, String trim2, String trim3, Map oficYdesofic) throws SQLException
    {
        String sqrySETInst;
        
        //Verificamos que los bimestres o evaluaciones que estén oficializados no se actualicen
        // Agregue oficYdesofic.get("bimOf1").equals(true) a cada bimestre para guarde actualizacion de inasistencias.(ely-25-05-18)
        sqrySETInst = oficYdesofic.get("bimOf1").equals(true) || oficYdesofic.get("bimOf1").equals(false) || oficYdesofic.get("alDeofB1").equals(true)?"trim1="+trim1:"";
        sqrySETInst += (oficYdesofic.get("bimOf2").equals(true) || oficYdesofic.get("bimOf2").equals(false) || oficYdesofic.get("alDeofB2").equals(true)? (sqrySETInst.equals("")?"":", ")+"trim2="+trim2:"");
        sqrySETInst += (oficYdesofic.get("bimOf3").equals(true) || oficYdesofic.get("bimOf3").equals(false) || oficYdesofic.get("alDeofB3").equals(true)? (sqrySETInst.equals("")?"":", ")+"trim3="+trim3:"");
        //try { sqrySETInst += (oficYdesofic.get("bimOf4").equals(true) || oficYdesofic.get("bimOf4").equals(false) || oficYdesofic.get("alDeofB4").equals(true)?(sqrySETInst.equals("")?"":", ")+"inst4="+inst4:""); } catch (NullPointerException ex){}
        //try { sqrySETInst += (oficYdesofic.get("bimOf5").equals(true) || oficYdesofic.get("bimOf5").equals(false) || oficYdesofic.get("alDeofB5").equals(true)?(sqrySETInst.equals("")?"":", ")+"inst5="+inst5:""); } catch (NullPointerException ex){}
        
        if (!sqrySETInst.trim().equals(""))
            stm.execute("UPDATE comunicacion_doc_alum SET "+sqrySETInst+" "
                    + "WHERE idalu="+idalu+" AND cicescini="+cicescini);
    }
    
    
    public ArrayList<Map> inasistencias (String cicescini, String grado, String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT * FROM inasistencias i WHERE i.cicescini="+cicescini+" AND i.grado="+grado+" AND i.idalu="+idalu);
        
        return qryToArrlmap (rs, null, false, 0);
    }
    
    public ArrayList<Map> comunicacionAlumDoc (String cicescini, String idalu) throws SQLException
    {    
        if( !cicescini.isEmpty() && Integer.parseInt(cicescini)>=2020 ) {
            rs = stm.executeQuery("SELECT * FROM comunicacion_doc_alum WHERE cicescini="+cicescini+" AND idalu="+idalu);
            if (!rs.next()) {
                stm.execute("INSERT INTO comunicacion_doc_alum VALUES( "+idalu+", "+cicescini+", 0, 0, 0)");
            }
            rs = stm.executeQuery("SELECT * FROM comunicacion_doc_alum WHERE cicescini="+cicescini+" AND idalu="+idalu);
        }
        return qryToArrlmap (rs, null, false, 0);
    }
    
    public ArrayList<Map> totMat (String idalu, String cicescini) throws SQLException
    {
        rs = stm.executeQuery("SELECT idalu, numeval, count(*) AS NumMat "
                            + "FROM EVALUACIONES "
                            + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" "
                            + "GROUP BY 1,2 "
                            + "ORDER BY 1,2");
        
        return qryToArrlmap (rs, null, false, 0);
    }
    
    public Map promGral2012  (String idalu, String cicescini) throws SQLException  //padre : QAlumCapCalif
    {
        rs = stm.executeQuery("SELECT idalu, "
                + "(SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado ='RE' AND gp.cveplan=2 AND gp.grado=1) AS Promd1roS, "
                + "(SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado ='RE' AND gp.cveplan=2 AND gp.grado=2) AS Promd2doS, "
                + "(SELECT promedio FROM alumnogrado gp WHERE gp.idalu = g.idalu AND gp.estatusgrado ='C' AND gp.cveplan=2 AND gp.grado=3) AS Promd3roS "
                + "FROM alumnogrado g "
                + "WHERE g.idalu="+idalu+" AND g.cicescini="+cicescini);
        return qryToMap (rs, null, false, 0);
    }
    
    public Map promGral (String idalu) throws SQLException //padre: QAlumCapCalif
    {
        rs = stm.executeQuery( "SELECT idalu, count(distinct grado) AS Grados,(trunc(sum(promedio)*10/count(*),0))/10 AS promgral "
                        + "FROM alumnomaterias "
                        + "WHERE idalu="+idalu+" AND cveplan=2 "
                            + "AND cicescini in (SELECT cicescini "
                                                + "FROM alumnogrado "
                                                + "WHERE idalu = "+idalu+" AND promedio>=6.0 AND cveplan=2 AND estatusgrado<>'BD' AND estatusgrado<>'RC' ) "
                            + "AND promedio >=6.0 "
                        + "GROUP BY idalu ");
        return qryToMap (rs, null, false, 0);
    }

//************************************************************************************************************
//********************************* MÉTODOS USADOS EN EL MÓDULO EvalPreescolar *******************************
//************************************************************************************************************
    public ArrayList<Map> getPaqueteMats_EvalPree (String CalifCicEscIn, String modalidad, String grado) throws SQLException
    {
        rs = stm.executeQuery("SELECT em.cveprograma, m.cvetipmat, m.cvemat, m.desmat, '' AS avances, ordenimpres "
                            + "FROM esquemamaterias em, materias m "
                            + "WHERE em.cvetipmat=m.cvetipmat AND em.cvemat=m.cvemat "
                                + "AND "+CalifCicEscIn+">=em.cicescini AND "+CalifCicEscIn+"<=em.cicescfin "
                                + "AND em.cveprograma=(SELECT cveprograma "
                                                    + "FROM planmodalidad "
                                                    + "WHERE modalidad='"+modalidad+"' AND "+CalifCicEscIn+">=cicescini AND "+CalifCicEscIn+"<=cicescfin "
                                                        + "AND plan=em.cveplan AND grado=em.grado AND cveentidad=20) "
                                + "AND em.cveplan=3 AND em.grado="+grado+" "
                            + "ORDER BY ordenimpres");
        return qryToArrlmap (rs, null, true, 0);
    }
    
    public ArrayList<Map> alumCapEvalPree (String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        rs = stm.executeQuery("SELECT a.idalu, TRIM(a.curp) AS curp, "
                                + "(TRIM(NVL(a.apepat,'')) || ' / ' || TRIM(NVL(a.apemat,'')) || ' * ' || TRIM(NVL(a.nombre,''))) AS nom_tot, a.apepat, "
                                + "a.apemat, a.nombre, g.cveprograma "
                            + "FROM alumnogrado g, alumno a "
                            + "WHERE g.idalu=a.idalu AND g.estatusGrado <>'BD' AND a.estatusalu ='I' "
                                + " AND g.cicescini  = "+cicescini
                                + " AND g.idcct = "+idcct
                                + " AND g.Grado = "+grado
                                + " AND g.Grupo = '"+grupo+"'"
                            + " ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        return qryToArrlmap (rs, null, true, 0);
    }    
    
    public ArrayList<Map> MatAvenceXEval (String numeval, String idalu, String cicescini, String grado) throws SQLException
    {
        rs = stm.executeQuery("SELECT q.ordenimpres, v.idalu, v.cvemat, e.desmat, v.avances, v.cicescini, v.cveprograma, v.cvetipmat, v.numeval, v.grado " 
                            + "FROM evalpreesc v,  materias e, esquemamaterias q "
                            + "WHERE q.estatus='A' AND v.cvemat=e.cvemat AND v.cvetipmat = e.cvetipmat AND v.cvemat=q.cvemat AND v.cvetipmat = q.cvetipmat "
                                + " AND v.grado=q.grado AND v.cveprograma=q.cveprograma "
                                + " AND v.numeval="+numeval
                                + " AND v.idalu="+idalu
                                + " AND v.cicescini="+cicescini
                                + " AND v.grado="+grado
                                + " AND v.cvemat NOT LIKE 'NAP%' "
                            + "ORDER BY q.ordenimpres");
        return qryToArrlmap (rs, null, true, 0);
    }
    
    public Map getDatoscomplementariosEvalPreesc (String califCicEscIn, String idalu) throws SQLException
    {
        Map datosComplementarios = new HashMap();
        /*datosComplementarios.put("concluyo","P".equals(getData("SELECT TRIM(promovido) "   //cancelado para 2023-2024
                                                             + "FROM alumnogrado "
                                                             + "WHERE cicescini="+ califCicEscIn+" AND idalu = "+idalu)));*/
        
        datosComplementarios.put("cvelengua",getData("SELECT cvelengua FROM alumno WHERE idalu="+idalu));
        
        /*datosComplementarios.put("recomendaciones",getData("SELECT recomendaciones "   //cancelado para 2023-2024
                                                         + "FROM ev_recompreesc "
                                                         + "WHERE cicescini="+ califCicEscIn+" AND idalu = "+idalu));*/
        
        return datosComplementarios;
    }
    
    /* Convierte las comillas según convenga para hacer un query que haga insert o update*/
    private String gestionarComillas (String avance)
    {
        String comilla="'";
        boolean tieneDobleComilla, tieneComillaSimple;
        
        tieneDobleComilla = avance.contains("\"");
        tieneComillaSimple = avance.contains("'");
        
        if (!tieneDobleComilla && !tieneComillaSimple)
            comilla = "'";
        else if (tieneDobleComilla && tieneComillaSimple)
            avance = avance.replace("'", "´");
        else if (tieneDobleComilla)
            comilla = "'";
        else if (tieneComillaSimple)
            comilla = "\"";
        
        return comilla+avance+comilla;
    }
    
    public void actualizarRecomXEvalYMat (Map tblAvancesXEvalYMat, String idalu, String califCicEscIn, String grado, String numeval, 
            String txtUsuario) throws SQLException, Exception
    {
        String txtaAvances = (""+tblAvancesXEvalYMat.get("avances")).trim().toUpperCase().replace("\n", " ");
        
        txtaAvances=gestionarComillas(txtaAvances);
        
        try {
            stm.execute("INSERT INTO ev_obsyrecgralxmat (idalu, cicescini, numeval, cvetipmat, cvemat, grado, obs_rec_gral, "
                            + "usuario, fecha, hora) "
                        + "VALUES ("+idalu+", "+califCicEscIn+", " + numeval+", '"
                            + tblAvancesXEvalYMat.get("cvetipmat")+"', '"+tblAvancesXEvalYMat.get("cvemat")+"'," + grado +", "
                            + txtaAvances +", "       
                            + "'"+txtUsuario.toUpperCase()+"', date(current), extend(current, hour to minute) )");
        }catch (SQLException ex){
            if (ex == null || ex.getMessage()==null)
                throw new SQLException(ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("EV_OBSYRECGRALXMAT_PK"))
                stm.execute("UPDATE ev_obsyrecgralxmat SET obs_rec_gral="+txtaAvances+", Usuario='"+txtUsuario.toUpperCase()+"', "
                                +"Fecha=date(current), Hora=extend(current, hour to minute) "
                           +"WHERE idalu = "+idalu+" AND cveMat='"+tblAvancesXEvalYMat.get("cvemat")+"' "
                                +"AND cveTipMat='"+tblAvancesXEvalYMat.get("cvetipmat")+"' AND cicescini="+ califCicEscIn+" "
                                +"AND grado="+grado+" AND numeval="+ numeval);
            else                
                throw new SQLException(ex+"\n\n"+ txtaAvances);
        }catch (Exception ex){
            throw new SQLException(ex);
        }
    }
    
    public void actualizarEvalPreescolar (Map tblMatCalifXBim, String idalu, String califCicEscIn, String grado, String numeval, 
            String txtUsuario) throws SQLException, Exception
    {
        String txtaAvances = (""+tblMatCalifXBim.get("avances")).trim().toUpperCase().replace("\n", " ");
        
        txtaAvances=gestionarComillas(txtaAvances);
        
        try {
            stm.execute("INSERT INTO evalpreesc (cvetipmat, cvemat, idalu, cicescini, cicescfin, cveprograma, grado, numeval, avances, recomendaciones, usuario, "
                            + "fecha, hora) "
                        + "VALUES ('"+tblMatCalifXBim.get("cvetipmat")+"', '"+tblMatCalifXBim.get("cvemat")+"', "+idalu+", "+califCicEscIn+", "
                            +(Integer.parseInt(califCicEscIn)+1)+", '"+tblMatCalifXBim.get("cveprograma")+"', "+grado+", "+numeval+", "
                            + txtaAvances+", '', "
                            + "'"+txtUsuario.toUpperCase()+"', date(current), extend(current, hour to minute) )");
        }catch (SQLException ex){
            if (ex == null || ex.getMessage()==null)
                throw new SQLException(ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("EVALPREESC_PK"))
                stm.execute("UPDATE evalpreesc SET avances="+txtaAvances+", Usuario='"+txtUsuario.toUpperCase()+"', "
                                +"Fecha=date(current), Hora=extend(current, hour to minute) "
                           +"WHERE idalu = "+idalu+" AND cveMat='"+tblMatCalifXBim.get("cvemat")+"' "
                                +"AND cveTipMat='"+tblMatCalifXBim.get("cvetipmat")+"' AND cicescini="+ califCicEscIn+" "
                                +"AND grado="+grado+" AND numeval="+ numeval);
            else
                /*throw new SQLException(ex+"\n\n"+tblMatCalifXBim.get("cvetipmat")+", "+tblMatCalifXBim.get("cvemat")+", "+idalu+", "+califCicEscIn+", "
                            +(Integer.parseInt(califCicEscIn)+1)+", "+tblMatCalifXBim.get("cveprograma")+", "+grado+", "+numeval+", "
                            + txtaAvances);*/
                throw new SQLException(ex+"\n\n"+ txtaAvances);
        }catch (Exception ex){
            throw new SQLException(ex);
        }
    }
    // cambio de nombre guardarRecomendacionesEvalPrescolar
    public void guardarLengua (String califCicEscIn, String idalu, String cvelengua, String txtUsuario) throws SQLException, SICEEO_Excepcion /*boolean chkConlcuyo_checked, String txtaRecomendaciones,*/
    {
        try{
            //stm.execute("UPDATE alumnogrado SET promovido='"+(chkConlcuyo_checked?"P":"NP")+"' WHERE idalu="+idalu+" AND cicescini="+califCicEscIn);
            stm.execute("UPDATE alumno SET cvelengua='"+cvelengua+"' WHERE idalu="+idalu);
            //stm.execute("INSERT INTO ev_recompreesc (idalu, cicescini, recomendaciones) VALUES ("+idalu+", "+califCicEscIn+", "+gestionarComillas (txtaRecomendaciones.trim().toUpperCase())+")");
        }catch (SQLException ex){
            /*if (ex==null || ex.getMessage()==null)
                throw new SQLException (""+ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("EVRECOMPREESC_PK"))
                if (!txtaRecomendaciones.equals(""))
                    stm.execute("UPDATE ev_recompreesc SET recomendaciones="+gestionarComillas (txtaRecomendaciones.trim().toUpperCase())+" "
                           +"WHERE cicescini="+califCicEscIn+" AND idalu="+idalu);
                else
                    stm.execute("DELETE FROM ev_recompreesc WHERE cicescini="+califCicEscIn+" AND idalu="+idalu);
            else*/   // Se quito para el ciclo 2023-2024
                throw new SQLException (ex);
        }
    }
    
    public Map oficYDesoficEnCalifEval (String cicescini, String idcct, String grado, String grupo, String idalu, String tipoOfic, String bim) throws SQLException
    {
        Map oficializaciones = new HashMap();
        int numOfics = 0, numDesofics = 0;
        boolean estadoOfic, estadoDesofic, permiso;
        String str_ofic = "ofic_caleval_b1, ofic_caleval_b2, ofic_caleval_b3 ";
        
        //str_ofic += !cicescini.equals("2018") ? ", ofic_caleval_b4, ofic_caleval_b5 " : ""; //PAra ciclos anteriores al 2018
        //Obtenemos los estatus de la oficializacion
        rs = stm.executeQuery("SELECT " + str_ofic + ", ing1eval "
                            + " FROM escuelagpos "
                            + " WHERE cicescini="+cicescini+" AND idcct="+idcct+" AND grado="+grado+" AND grupo='"+grupo+"'");
        if (rs.next()) {
            oficializaciones.put("bimOf1", estadoOfic=rs.getBoolean("ofic_caleval_b1"));
            if (estadoOfic) numOfics++;
            oficializaciones.put("bimOf2", estadoOfic=rs.getBoolean("ofic_caleval_b2"));
            if (estadoOfic) numOfics++;
            oficializaciones.put("bimOf3", estadoOfic=rs.getBoolean("ofic_caleval_b3"));
            if (estadoOfic) numOfics++;
            
            /*if (tipoOfic.contains("CALIFS BIM") && !cicescini.equals("2018")){
                oficializaciones.put("bimOf4", estadoOfic=rs.getBoolean("ofic_caleval_b4"));
                if (estadoOfic) numOfics++;
                oficializaciones.put("bimOf5", estadoOfic=rs.getBoolean("ofic_caleval_b5"));
                if (estadoOfic) numOfics++;
            } */           
            //oficializaciones.put("ingles", ""+rs.getBoolean("ing1eval"));
        }
        oficializaciones.put("todoOf", numOfics == (tipoOfic.contains("CALIFS BIM")?3:3));
        oficializaciones.put("algunOf", numOfics > 0);
        oficializaciones.put("bimOf", (Boolean)oficializaciones.get("bimOf"+bim));
        
        //Obtenemos los estatus de la desoficializacion
        oficializaciones.put("alDeofB1",false);
        oficializaciones.put("alDeofB2",false);
        oficializaciones.put("alDeofB3",false);
        //oficializaciones.put("alDeofB4",false);
        //oficializaciones.put("alDeofB5",false);
        if (!idalu.equals("")){
            rs = stm.executeQuery("SELECT co.cveoficializacion, 'alDeofB'||REPLACE(co.oficializacion,'"+tipoOfic+" ') AS bimDeof, CASE WHEN d.idcct IS NULL THEN 'f' ELSE 't' END AS estadoDesofic "
                                + "FROM catoficializacion co left join desoficializacion d on (co.cveoficializacion=d.cveoficializacion AND d.cicescini="+cicescini+" AND d.idcct="+idcct+" AND d.idalu="+idalu+" AND d.grado="+grado+" AND d.grupo='"+grupo+"') "
                                + "WHERE co.oficializacion like '"+tipoOfic+"%' "
                                + "ORDER BY co.oficializacion");
            while (rs.next()){
                estadoDesofic = rs.getBoolean("estadoDesofic");
                oficializaciones.put(rs.getString("bimDeof"), estadoDesofic);
                if (estadoDesofic) numDesofics++;
            }
        }
        oficializaciones.put("todoDeof", numDesofics == numOfics);
        oficializaciones.put("algunDeof", numDesofics > 0);
        oficializaciones.put("alDeof", (Boolean)oficializaciones.get("alDeofB"+bim));
        
        /******************** Apartado para checar si activa o no captura de cada trimestre 2023-2024 **************************/
        rs = stm.executeQuery("SELECT formulario,componente, permiso, permisodefault " 
            + " FROM siceeo_configpermiso cp, siceeo_objetospermiso op, siceeo_permisos p "
            + " WHERE cp.cveconfigpermiso = p.cveconfigpermiso "
            + " AND cp.idobjeto = op.idobjeto " 
            + " AND tipousuario='CCT' " 
            + " AND formulario = 'CALIFICACIONES' ");
            while (rs.next()) {
                permiso = rs.getBoolean("permiso");
                oficializaciones.put(rs.getString("componente"), permiso);                
            }        
        /******************************** Cierre de validacion de captura trimestral *************************************/
        return oficializaciones;
    }
    
    public Map ofYDeofEnCalEvalYGpos (String cicescini, String idcct, String grado, String grupo, String tipoOfic) throws SQLException
    {
        Map oficializaciones = new HashMap();
        int numDesofics = 0;
        boolean estadoOfic, estadoDesofic;
        
        //Obtenemos los estatus de la oficializacion
        oficializaciones = oficYDesoficEnCalifEval (cicescini, idcct, grado, grupo, "", tipoOfic, "");
        
        rs = stm.executeQuery("SELECT "
                                + "'alDeofB'||REPLACE(oficializacion,'"+tipoOfic+" ') AS bimDeof, "
                                + "(SELECT count(grupo) "
                                 + "FROM desoficializacion "
                                + "WHERE cicescini="+cicescini+" AND idcct="+idcct+" AND grado="+grado+" "
                                    + "AND grupo='"+grupo+"' AND cveoficializacion=co.cveoficializacion) AS numAlusDeof "
                            + "FROM catoficializacion co "
                            + "WHERE co.oficializacion like '"+tipoOfic+"%'");
        while (rs.next()){
            estadoDesofic = rs.getInt("numAlusDeof")>0;
            oficializaciones.put(rs.getString("bimDeof"), estadoDesofic);
            if (estadoDesofic) numDesofics++;
        }
        oficializaciones.put("todoDeof", numDesofics == (tipoOfic.contains("CALIFS BIM")?3:3));
        oficializaciones.put("algunDeof", numDesofics > 0);
        
        return oficializaciones;
    }
//************************************************************************************************************
//********************************** MÉTODOS USADOS EN EL MÓDULO CaptuRepEval ********************************
//************************************************************************************************************
    public ArrayList<Map> alumCaptuRepEval (String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        rs = stm.executeQuery("SELECT a.idalu, TRIM(a.curp) AS curp, "
                                + "(TRIM(NVL(a.apepat,'')) || ' / ' || TRIM(NVL(a.apemat,'')) || ' * ' || TRIM(NVL(a.nombre,''))) AS nom_tot, a.apepat, "
                                + "a.apemat, a.nombre, g.cveprograma "
                            + "FROM alumnogrado g, alumno a "
                            + "WHERE g.idalu=a.idalu AND g.estatusGrado <>'BD' AND a.estatusalu ='I' "
                                + " AND g.cicescini  = "+cicescini
                                + " AND g.idcct = "+idcct
                                + " AND g.Grado = "+grado
                                + " AND g.Grupo = '"+grupo+"'"
                            + " ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        return qryToArrlmap (rs, null, true, 0);
    }
    
    public Map getCaptuRepEval (String cicescini, String idalu) throws SQLException
    {
        Map QCaptuRepEval = new HashMap();
        
        /*rs = stm.executeQuery("SELECT or_escritura, or_lectura, or_mate, tutoria, alerta1, alerta2, alerta3, obsrec_gral "
                            + "FROM ev_caprepeval "
                            + "WHERE idalu="+idalu+" AND cicescini="+cicescini);
        QCaptuRepEval = qryToMap(rs, null, true, 0);*/
        
        rs = stm.executeQuery("SELECT cvelengua FROM alumno WHERE idalu="+idalu);
        if (rs.next())
            QCaptuRepEval.put("cvelengua", rs.getString("cvelengua"));
        
        /*rs = stm.executeQuery("SELECT numeval, obs_rec_gral "
                        + "FROM ev_obsyrecgralxeval "
                        + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" "
                        + "ORDER BY numeval");
        while(rs.next()){            
                QCaptuRepEval.put("obsrec_gral_eval"+rs.getInt("numeval"), rs.getString("obs_rec_gral"));
        }*/
        
        //QCaptuRepEval.put("obsRecGral", qryToArrlmap(rs, null, true, 0));
        
        /*rs = stm.executeQuery("SELECT numeval, cvehabilidad "
                            + "FROM ev_apoyohabilidad "
                            + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" "
                            + "ORDER BY cvehabilidad, numeval");
        QCaptuRepEval.put("apoyoHabXBim", qryToArrlmap(rs, null, true, 0));
        
        rs = stm.executeQuery("SELECT idpregunta, cvefrecuencia, cvemeseval "
                            + "FROM ev_complectora "
                            + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" "
                            + "ORDER BY idpregunta, cvefrecuencia, cvemeseval");
        QCaptuRepEval.put("tblEvalLec", qryToArrlmap(rs, null, true, 0));*/
        
        return QCaptuRepEval;
    }
    
    public ArrayList<Map> obsvRecomXMat (String numeval, String idalu, String cicescini, String grado, String cveprograma) throws SQLException
    {
        rs = stm.executeQuery("SELECT o.cicescini, o.numeval, o.grado, o.idalu, o.cvetipmat, o.cvemat, m.desmat, o.obs_rec_gral AS avances " 
            + "FROM ev_obsyrecgralxmat o, esquemamaterias e, materias m " 
            + "WHERE " 
            + " idalu = " + idalu 
            + " AND o.grado = " + grado 
            + " AND o.numeval = " + numeval 
            + " AND o.cicescini= " + cicescini
            + " AND o.cvetipmat = e.cvetipmat "
            + " AND o.cvemat = e.cvemat " 
            + " AND o.grado = e.grado " 
            + " AND "+ cicescini + " >= e.cicescini AND " + cicescini +" <= e.cicescfin "
            + " AND cveplan = 1 " 
            + " AND cveprograma = '" + cveprograma + "' "
            + " AND e.cvetipmat = m.cvetipmat "
            + " AND e.cvemat = m.cvemat "
            + " AND m.matdefault='t' "
            + " ORDER BY ordenimpres ");
        return qryToArrlmap (rs, null, true, 0);
        
    }
    
    public ArrayList<Map>  getMateriasAlumno (String cicescini, String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT TRIM(m.desmat) AS desmat, TRIM(m.cvetipmat)||'~'||TRIM(m.cvemat) AS cvemats " +
                            "FROM alumnomaterias am, materias m, esquemamaterias em " +
                            "WHERE am.cvetipmat=m.cvetipmat AND am.cvemat=m.cvemat AND am.cicescini>=em.cicescini  " +
                                "AND am.cicescini<=em.cicescfin AND am.cveprograma=em.cveprograma AND am.cveplan=em.cveplan AND am.grado=em.grado " +
                                "AND m.cvemat=em.cvemat AND m.cvetipmat=em.cvetipmat " +
                                "AND am.idalu="+idalu+" AND am.cicescini="+cicescini+" " +
                            "ORDER BY em.ordenimpres");
        return qryToArrlmap (rs, null, true, 0);
    }
    
    public ArrayList<Map> getTablaObsYRecomXBimYAsig (String cicescini, String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT ba.numeval, TRIM(m.desmat) AS desmat, ba.obsv_esp, ba.apoyo_esp, TRIM(ba.cvetipmat)||'~'||TRIM(ba.cvemat) AS clavesmat "
                        + "FROM ev_bimasignatura ba, materias m "
                        + "WHERE ba.cvetipmat=m.cvetipmat AND ba.cvemat=m.cvemat AND ba.idalu="+idalu+" AND ba.cicescini="+cicescini+" "
                        + "ORDER BY ba.ordenimpre");
        return qryToArrlmap (rs, null, true, 0);
    }
    
    
    public ArrayList<Map> getTablaPreguntasCompLectora (String cveplan, String grado) throws SQLException
    {
        rs = stm.executeQuery("SELECT idpregunta, descrpreg "
                            + "FROM ev_catpreguntas "
                            + "WHERE cveplan="+cveplan+" AND grado="+grado+" "
                            + "ORDER BY numpreg");
        return qryToArrlmap (rs, null, true, 0);
    }
    
    public void guardarCaptuRepEval (String cicescini, String cveplan, String idalu, int numeval, String cvelengua, String txtaRecomedGrales, String usuario) throws SQLException
    {  
        if(cveplan.equals("1")) {
            try {
                stm.execute("INSERT INTO ev_obsyrecgralxeval (idalu, cicescini, numeval, obs_rec_gral, usuario, fecha, hora) "
                    + "VALUES (" + idalu +", " + cicescini + ", " + numeval + ", "+gestionarComillas(txtaRecomedGrales.trim().toUpperCase())+", "
                        + "'"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))");                    

            }catch (SQLException ex){
                if (ex == null || ex.getMessage()==null)
                    throw new SQLException(""+ex);
                else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("EV_OBSYRECGRALXEVAL_PK")) {
                    stm.execute("UPDATE ev_obsyrecgralxeval SET "
                            + "obs_rec_gral="+gestionarComillas(txtaRecomedGrales.trim().toUpperCase())+", "
                            + "usuario='"+usuario.trim().toUpperCase()+"', fecha=date(current) "
                            + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" AND numeval="+numeval);                                
                }else
                    throw new SQLException(ex);
            }catch (Exception ex){
                throw new SQLException(ex);
            }                                                        
        }
        stm.execute("UPDATE alumno SET cvelengua='"+cvelengua+"' WHERE idalu="+idalu); 
        
    }
    
    public void guardarCaptuRepEval (String cicescini, String cveplan, String idalu, String cvelengua, String chkRiesgosAlerta1, String chkRiesgosAlerta2, String chkRiesgosAlerta3, 
            String tutoria, String txtaHabEscritura, String txtaHabLectura, String txtaHabMatematica, ArrayList<Map>chkHabEscritura, 
            ArrayList<Map> chkHabLectura, ArrayList<Map> chkHabMatematica, ArrayList<Map> tblObsYRecomXBimYAsig, 
            String txtaRecomedGrales_eval1, String txtaRecomedGrales_eval2, String txtaRecomedGrales_eval3, 
            ArrayList<Map> tblEvalLec, String usuario) throws SQLException
    {
        String cvecalif[]={"S","CS","EO","RA"},strquery="";
        int catmeseval[]={1,2,3,4};        
        
            /*stm.execute("INSERT INTO ev_caprepeval (idalu, cicescini, or_escritura, or_lectura, or_mate, tutoria, alerta1, alerta2, alerta3, obsrec_gral, usuario, fecha, hora) "
                    + "VALUES ("+idalu+", "+cicescini+", "+gestionarComillas(txtaHabEscritura.trim().toUpperCase())+", "+gestionarComillas(txtaHabLectura.trim().toUpperCase())+", "
                        + ""+gestionarComillas(txtaHabMatematica.trim().toUpperCase())+", '"+tutoria+"', '"+chkRiesgosAlerta1.charAt(0)+"', "
                        + "'"+chkRiesgosAlerta2.charAt(0)+"', '"+chkRiesgosAlerta3.charAt(0)+"', "+gestionarComillas(txtaRecomedGrales.trim().toUpperCase())+", "
                        + "'"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))"); */
        for(int i=0; i<3; i++) {
            strquery = " ("+idalu+", "+cicescini+", "+(i+1)+", ";
            if (i==0)
                strquery += gestionarComillas(txtaRecomedGrales_eval1.trim().toUpperCase()) + " ), ";            
            else if (i==1)
                strquery += gestionarComillas(txtaRecomedGrales_eval2.trim().toUpperCase()) + " ), ";            
            else if (i==2)
                strquery += gestionarComillas(txtaRecomedGrales_eval3.trim().toUpperCase()) + " ) ";                        
        }
        
        try {
            stm.execute("INSERT INTO ev_obsyrecgralxeval (idalu, cicescini,numeval,obs_rec_gral) "
                + "VALUES " + strquery );

        }catch (SQLException ex){
            if (ex == null || ex.getMessage()==null)
                throw new SQLException(""+ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("EVCAPREPEVAL_PK")) {
                stm.execute("UPDATE ev_obsyrecgralxeval SET "
                        + "obs_rec_gral="+gestionarComillas(txtaRecomedGrales_eval1.trim().toUpperCase())+", "
                        + "usuario='"+usuario.trim().toUpperCase()+"', fecha=date(current), "
                        + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" AND numeval=1");
                
                stm.execute("UPDATE ev_obsyrecgralxeval SET "
                        + "obs_rec_gral="+gestionarComillas(txtaRecomedGrales_eval2.trim().toUpperCase())+", "
                        + "usuario='"+usuario.trim().toUpperCase()+"', fecha=date(current), "
                        + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" AND numeval=2");
                
                stm.execute("UPDATE ev_obsyrecgralxeval SET "
                        + "obs_rec_gral="+gestionarComillas(txtaRecomedGrales_eval3.trim().toUpperCase())+", "
                        + "usuario='"+usuario.trim().toUpperCase()+"', fecha=date(current), "
                        + "WHERE idalu="+idalu+" AND cicescini="+cicescini+" AND numeval=3");
                                
                /*
                stm.execute("UPDATE ev_caprepeval SET "
                            /*+ "or_escritura="+gestionarComillas(txtaHabEscritura.trim().toUpperCase())+", or_lectura="+gestionarComillas(txtaHabLectura.trim().toUpperCase())+", "
                            + "or_mate="+gestionarComillas(txtaHabMatematica.trim().toUpperCase())+", tutoria='"+tutoria+"', "
                            + "alerta1='"+chkRiesgosAlerta1.charAt(0)+"',  alerta2='"+chkRiesgosAlerta2.charAt(0)+"', alerta3='"+chkRiesgosAlerta3.charAt(0)+"', "                        
                            + "obsrec_gral="+gestionarComillas(txtaRecomedGrales.trim().toUpperCase())+", usuario='"+usuario.trim().toUpperCase()+"', fecha=date(current), "
                            + "hora=extend(current, hour to minute) "
                        + "WHERE idalu="+idalu+" AND cicescini="+cicescini);                
                stm.execute("DELETE FROM ev_apoyohabilidad WHERE idalu="+idalu+" AND cicescini="+cicescini);
                stm.execute("DELETE FROM ev_bimasignatura WHERE idalu="+idalu+" AND cicescini="+cicescini);
                stm.execute("DELETE FROM ev_complectora WHERE idalu="+idalu+" AND cicescini="+cicescini);*/
            }else
                throw new SQLException(ex);
        }catch (Exception ex){
            throw new SQLException(ex);
        }    
        
                                    
        
        
        stm.execute("UPDATE alumno SET cvelengua='"+cvelengua+"' WHERE idalu="+idalu);
        
        /*for (int i=1; i<=5; i++){
            if ((""+chkHabEscritura.get(0).get(""+i)).equals("true"))
                stm.execute("INSERT INTO ev_apoyohabilidad (idalu, cicescini, numeval, cvehabilidad) "
                          + "VALUES ("+idalu+", "+cicescini+", "+i+", 'ESC')");
            if ((""+chkHabLectura.get(0).get(""+i)).equals("true"))
                stm.execute("INSERT INTO ev_apoyohabilidad (idalu, cicescini, numeval, cvehabilidad) "
                          + "VALUES ("+idalu+", "+cicescini+", "+i+", 'LEC')");
            if ((""+chkHabMatematica.get(0).get(""+i)).equals("true"))
                stm.execute("INSERT INTO ev_apoyohabilidad (idalu, cicescini, numeval, cvehabilidad) "
                          + "VALUES ("+idalu+", "+cicescini+", "+i+", 'MAT')");
        }

        for (int i=0; i<tblObsYRecomXBimYAsig.size(); i++)
            stm.execute("INSERT INTO ev_bimasignatura (idalu, cicescini, numeval, cvetipmat, cvemat, obsv_esp, apoyo_esp, ordenimpre) "
                    + "VALUES ("+idalu+", "+cicescini+", "+tblObsYRecomXBimYAsig.get(i).get("bimestre")+", '"+tblObsYRecomXBimYAsig.get(i).get("cvetipmat")+"', "
                        + "'"+tblObsYRecomXBimYAsig.get(i).get("cvemat")+"', '"+tblObsYRecomXBimYAsig.get(i).get("obserEsp")+"', "
                        + "'"+tblObsYRecomXBimYAsig.get(i).get("apoyo")+"',"+(i+1)+")");
        
        for (int i=0; i<tblEvalLec.size(); i++){
            for (int j=1; j<17; j++){
                if(tblEvalLec.get(i).get(""+j).equals("true"))
                    stm.execute("INSERT INTO ev_complectora (idalu, cicescini, cvemeseval, idpregunta, cvefrecuencia) "
                            + "VALUES ("+idalu+", "+cicescini+", "+(catmeseval[(j-1)%4])+", "+tblEvalLec.get(i).get("idpregunta")+", '"+cvecalif[(j-1)/4]+"')");
            }
        }
        */
    }
    
    
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO PromGralSec **********************************
//************************************************************************************************************

    public Map actualizaPromedioGral (int tblAlumCapCalif_MatRepAct, String tblAlumCapCalif_promediogral, String txtUsuario, String califCicEscIn, String tblAlumCapCalif_idalu) throws SQLException
    {
        Map tblAlumCapCalif=new HashMap();
        String promediogral, estatusgrado, promovido;
        
        if ( tblAlumCapCalif_MatRepAct  <= 5  ){
            promediogral=tblAlumCapCalif_promediogral;            estatusgrado="C";            promovido="P";
        }else{
            promediogral="0.0";     estatusgrado="NP";      promovido="NP";
        }
       //      Dm.Q_AlumGdo.SQL.Add(' Calif_real   = "S"  ');
        stm.execute( "UPDATE alumnoGrado SET PromedioGral = "+promediogral+ ", estatusgrado = '"+estatusgrado+"', Promovido='"+promovido+"' , usuario = '"+txtUsuario.trim().toUpperCase()+"', "
                + "fecha = date(current) , hora = extend(current, hour to minute) WHERE cicescini ="+califCicEscIn+" AND idalu = "+tblAlumCapCalif_idalu );
        
        tblAlumCapCalif.put("promediogral",promediogral);
        tblAlumCapCalif.put("estatusgrado",estatusgrado);
        tblAlumCapCalif.put("promovido",promovido);
        return tblAlumCapCalif;
    }
    
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO PromGralSec2012 **********************************
//************************************************************************************************************
    public Map actualizaPromedioGral2012 (int tblAlumCapCalif_MatRepAct, String tblAlumCapCalif_promediogral, String califCicEscIn, String tblAlumCapCalif_promedioeb, boolean chkEditProm_isChecked, String txtUsuario, String tblAlumCapCalif_idalu ) throws SQLException
    {
        Map tblAlumCapCalif=new HashMap();
        String promediogral, promedioeb, cveconcepto="", estatusgrado, promovido;
        
        String query;
        if ( tblAlumCapCalif_MatRepAct<= 4  )
        {
              promediogral= tblAlumCapCalif_promediogral;

              if ( califCicEscIn.equals("2012") )  //solo para 2012
                 promedioeb = tblAlumCapCalif_promedioeb;
              else
                 promedioeb = "0.0";

              if ( chkEditProm_isChecked )
                 cveconcepto = "cveconcepto = 'ed', ";  //promedio editado
              else
                 cveconcepto = "cveconcepto = '', ";

              estatusgrado = "C";                 //debe menos de 4 mat, asi k no repetira el grado
              promovido = "P";
        } else {
              promediogral="0.0"; promedioeb = "0.0"; estatusgrado = "NP"; promovido = "NP";
        }
        
        query = "UPDATE alumnoGrado SET PromedioGral="+promediogral+", PromedioEB = "+promedioeb+", "+cveconcepto+ "estatusgrado = '"+estatusgrado+"', Promovido = '"+promovido+"', ";
        //      Dm.Q_AlumGdo.SQL.Add(" Calif_real   = 'S'  ");
        query += "usuario = '"+txtUsuario.trim().toUpperCase()+"', fecha = date(current) , hora = extend(current, hour to minute) ";
        query += "WHERE cicescini ="+califCicEscIn+ " AND idalu = "+tblAlumCapCalif_idalu;

        stm.execute(query);
        
        tblAlumCapCalif.put("promediogral",promediogral);
        tblAlumCapCalif.put("promedioeb",promedioeb);
        tblAlumCapCalif.put("estatusgrado",estatusgrado);
        tblAlumCapCalif.put("promovido",promovido);
        return tblAlumCapCalif;
    }
    
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO BimXAlum **********************************
//************************************************************************************************************
    public ArrayList<Map> califBimXAlum(String idalu, String cicescini) throws SQLException
     {
         rs = stm.executeQuery("SELECT "
                                            + "q.ordenimpres, m.idalu, m.cicescini, m.cveprograma, q.cvetipmat, m.cveplan, m.grado, m.cvemat, "
                                            +"(CASE WHEN (m.cvetipmat ='EDT' OR m.cvetipmat ='TEC')  THEN '('||m.cvemat||') '||e.desmat  ELSE e.desmat END) AS desmat, "
                                            +"("
                                                +"SELECT calif1 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 1 "
                                                    + "AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B11, "
                                            + "("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                + "WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 1 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B12, "
                                            +"("
                                                +"SELECT calif1 "
                                                    +"FROM evaluaciones vb "
                                                    +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 2 "
                                                        +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B21, "
                                            +"("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 2 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B22, "
                                            +"("
                                                + "SELECT calif1 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 3 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B31, "
                                            +"("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 3 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B32, "
                                            +"("
                                                +"SELECT calif1 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 4 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B41, "
                                            +"("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 4 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B42, "
                                            +"("
                                                +"SELECT calif1 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 5 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B51, "
                                            +"("
                                                +"SELECT calif2 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 5 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B52, "
                                            +"m.promedio "
                                        +"FROM alumnomaterias m,  materias e, esquemamaterias q "
                                        +"WHERE  q.estatus = 'A' AND m.cvemat = e.cvemat AND m.cvetipmat = e.cvetipmat AND m.cvemat = q.cvemat AND m.cvetipmat = q.cvetipmat AND m.grado = q.grado "
                                            + "AND m.cveprograma = q.cveprograma "
                                            +"AND m.IDALU =  "+idalu+" "
                                            +"AND m.cicescini= "+cicescini+" AND m.cvemat not like 'NAP%' "
                                        +"ORDER BY q.ordenimpres ");
         
         return qryToArrlmap (rs, null, true, 1);
     }
    
    public ArrayList<Map> calif1ro (String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT "
                                + "(SELECT cveunidad FROM escuela e WHERE e.idcct = g.idcct) AS Deleg, "
                                +" (SELECT acreditado FROM alumnodelextrj j WHERE j.idalu = g.idalu AND g.cicescini = j.cicescini) AS AlmExtrj, "
                                +"(SELECT TRIM(cct)||SUBSTR(cveturno,1,1) FROM escuela e WHERE e.idcct = g.idcct) AS cct, g.grupo, "
                                +"m.cicescini, q.ordenimpres, m.idalu, m.cvemat, g.idcct, g.promedio AS promGdo,  "
                                +"g.matrepACT, g.matrepANT, "
                                +"(CASE "
                                    + "WHEN g.cveplan =2 "
                                    +" THEN ("
                                        + "SELECT count(*) "
                                        + "FROM exm_ext_ordi x "
                                        + "WHERE x.cvetipmat= m.cvetipmat AND x.cvemat= m.cvemat AND x.grado= m.grado AND x.idalu = m.idalu AND x.cicescini = m.cicescini AND x.promedio>=6.0)  "
                                    +" ELSE 0  "
                                +" END) AS eer, "
                                + "m.exm, "
                                +"(CASE "
                                    + "WHEN (m.cvetipmat ='EDT' OR m.cvetipmat ='TEC')"
                                    +"THEN '('||m.cvemat||') '||e.desmat  ELSE e.desmat "
                                + "END) AS desmat, "
                                +"m.promedio, m.califant, m.EstatMat, m.cveprograma, m.cvetipmat "
                            +" FROM alumnogrado g, alumnomaterias m,  materias e, esquemamaterias q "
                            +"WHERE  m.cvemat = e.cvemat AND m.cvetipmat = e.cvetipmat AND m.cvemat = q.cvemat AND m.cvetipmat = q.cvetipmat AND m.grado = q.grado AND m.grado= 1 AND g.cicescini= m.cicescini "
                                +"AND g.estatusgrado<>'BD' AND g.estatusgrado<>'RC' AND g.idalu = m.idalu AND g.cveplan=2 AND m.cveprograma = q.cveprograma AND m.cvemat NOT LIKE 'NAP%' "
                                +"AND m.idalu = "+idalu+" "
                                +"AND    q.cicescini<=g.cicescini AND q.cicescfin>=g.cicescini "
                                //+"AND g.cicescini != 2018 "        
                            +"ORDER BY q.ordenimpres");
        
        return qryToArrlmap (rs, null, true, 2);
        
    }
    
     public ArrayList<Map> calif2do (String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT "
                                        + "(SELECT cveunidad FROM escuela e WHERE e.idcct = g.idcct) AS Deleg, "
                                        +"(SELECT acreditado FROM alumnodelextrj j WHERE j.idalu = g.idalu AND g.cicescini = j.cicescini) AS AlmExtrj, "
                                        +"(SELECT TRIM(cct)||SUBSTR(cveturno,1,1) FROM escuela e WHERE e.idcct = g.idcct)AS cct, g.grupo, "
                                        +"m.cicescini, q.ordenimpres, m.idalu, m.cvemat, g.idcct, g.promedio AS promGdo, g.matrepACT, g.matrepANT, "
                                        +"(CASE "
                                            + "WHEN g.cveplan =2 "
                                            +" THEN ("
                                                + "SELECT count(*) "
                                                + "FROM exm_ext_ordi x "
                                                +"WHERE x.cvetipmat= m.cvetipmat AND x.cvemat= m.cvemat AND x.grado= m.grado AND x.idalu = m.idalu AND x.cicescini = m.cicescini AND x.promedio>=6.0)  "
                                            +" ELSE 0  "
                                        +" END) AS eer, "
                                        + "m.exm, "
                                        +"(CASE "
                                            + "WHEN (m.cvetipmat ='EDT' OR m.cvetipmat ='TEC') "
                                            +"THEN '('||m.cvemat||') '||e.desmat  ELSE e.desmat "
                                        + "END) AS desmat, "
                                        +"m.promedio, m.califant, m.EstatMat, m.cveprograma, m.cvetipmat "
                                    +"FROM alumnogrado g, alumnomaterias m,  materias e, esquemamaterias q "
                                    +"WHERE  m.cvemat = e.cvemat AND m.cvetipmat = e.cvetipmat AND m.cvemat = q.cvemat AND m.cvetipmat = q.cvetipmat AND m.grado = q.grado AND m.grado= 2 AND g.cicescini= m.cicescini "
                                        +"AND g.estatusgrado<>'BD' AND g.estatusgrado<>'RC' AND g.idalu = m.idalu AND g.cveplan=2 AND m.cveprograma = q.cveprograma AND m.cvemat NOT LIKE 'NAP%' "
                                        +"AND m.idalu = "+idalu+" "
                                        +"AND q.cicescini<=g.cicescini AND    q.cicescfin>=g.cicescini "
                                        //+"AND g.cicescini != 2018 "          
                                    +"ORDER BY q.ordenimpres");
        
        return qryToArrlmap (rs, null, true, 2);
    }
    
    public ArrayList<Map> getMesExt (String cicescini) throws SQLException
    {        
        rs = stm.executeQuery("SELECT mes, dia_ini, dia_fin FROM exm_ext_ordi_periodos " +
                              "WHERE estatus='A'");        
        return qryToArrlmap (rs, null, true, 2);
    }
        
    public ArrayList<Map> califBimXAlumToExcel(String idalus, String cicescini) throws SQLException
    {
        rs = stm.executeQuery("SELECT "
                                            + "q.ordenimpres, m.idalu, m.cicescini, m.cveprograma, q.cvetipmat, m.cveplan, m.grado, m.cvemat, "
                                            + "a.apepat, a.apemat, a.nombre, a.curp, "
                                            +"(CASE WHEN (m.cvetipmat ='EDT' OR m.cvetipmat ='TEC') THEN '('||m.cvemat||') '||e.desmat  ELSE e.desmat END) AS desmat, "
                                            +"("
                                                +"SELECT calif1 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 1 "
                                                    + "AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B11, "
                                            + "("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                + "WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 1 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B12, "
                                            +"("
                                                +"SELECT calif1 "
                                                    +"FROM evaluaciones vb "
                                                    +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 2 "
                                                        +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B21, "
                                            +"("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 2 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B22, "
                                            +"("
                                                + "SELECT calif1 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 3 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B31, "
                                            +"("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 3 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B32, "
                                            +"("
                                                +"SELECT calif1 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 4 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B41, "
                                            +"("
                                                + "SELECT calif2 "
                                                + "FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 4 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B42, "
                                            +"("
                                                +"SELECT calif1 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 5 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B51, "
                                            +"("
                                                +"SELECT calif2 "
                                                +"FROM evaluaciones vb "
                                                +"WHERE vb.cvemat = q.cvemat AND vb.cvetipmat = q.cvetipmat AND vb.grado = q.grado AND vb.cveprograma = q.cveprograma AND vb.cicescini = m.cicescini AND vb.numeval = 5 "
                                                    +"AND Vb.IDALU =  m.idalu AND vb.cvemat not like 'NAP%'"
                                            + ") AS B52, "
                                            +"m.promedio "
                                        +"FROM alumno a, alumnomaterias m,  materias e, esquemamaterias q "
                                        +"WHERE  q.estatus = 'A' AND m.cvemat = e.cvemat AND m.cvetipmat = e.cvetipmat AND m.cvemat = q.cvemat AND m.cvetipmat = q.cvetipmat AND m.grado = q.grado "
                                            + "AND m.cveprograma = q.cveprograma "
                                            +"AND m.IDALU = a.idalu "
                                            +"AND m.IDALU IN  ("+idalus+") "
                                            +"AND m.cicescini= "+cicescini+" AND m.cvemat not like 'NAP%' "
                                        +"ORDER BY a.apepat, a.apemat, a.nombre, a.curp, q.ordenimpres ");
         
        return qryToArrlmap (rs, null, true, 1);
     }
     
     public Map inasistenciasToExcel (String cicescini, String grado, String idalus) throws SQLException
    {
        ResultSetMetaData rsmd;
        int numCols;
        String dato, columna;
        Map inasistencias = new HashMap();
        Map fila;
        rs = stm.executeQuery("SELECT i.*, a.apepat, a.apemat, a.nombre, a.curp "
                + "FROM inasistencias i, alumno a "
                + "WHERE i.idalu = a.idalu AND i.cicescini="+cicescini+" AND i.grado="+grado+" AND i.idalu IN ("+idalus+") "
                + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        rsmd = rs.getMetaData();
        numCols=rsmd.getColumnCount();
        while (rs.next())
        {
            fila = new HashMap();
            for (int i=1; i<=numCols; i++){
                columna = rsmd.getColumnName(i).toLowerCase();
                dato = rs.getString(i);
                if (columna.equals("idalu"))
                    inasistencias.put(dato, fila);
                else if (!columna.equals("apepat") && !columna.equals("apemat") && !columna.equals("nombre") && !columna.equals("curp"))
                    fila.put(columna, dato==null?"":dato.trim());
            }
        }
        return inasistencias;
    }
     
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO SecHist ***********************************
//************************************************************************************************************
    public ArrayList<Map>  pagoSusMatEn2012 (String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT * FROM exm_ext_ordi WHERE PROMEDIO>=6.0 AND ANIO>=2012 AND idalu="+idalu);
        return qryToArrlmap (rs, null, true, 1);
    }
    public void actualizaAlumnoMaterias (String cicescini, String cveplan, String grado, String idalu, String cvemat, String cvetipmat, String exm, int eer, 
            String promedio, String txtUsuario, String cicescini_act) throws SQLException
    {
        String query="";
        Map QPromXMatBim;
        QPromXMatBim = promXMatBim (cicescini, grado, cveplan, idalu, cvemat, cvetipmat, cicescini_act);
        query = "UPDATE AlumnoMATERIAS ";
        
        if ( QPromXMatBim.get("nobim").equals("3") )
        {
            if (Float.parseFloat(""+QPromXMatBim.get("promxmat")) < 6.0 )
                query += "SET califant="+QPromXMatBim.get("promxmat")+ ", ";
            else
                query += "SET califant=0.0, ";
        }
        else //no tiene sus 5 bim
        {
            if ( exm.equals("ER") )                                             //presento examen ER
            {
                if ( eer > 0)                                                   //y presento examen y la aprobo
                    query += "SET califant=5.0, ";
                else
                    query += "SET califant=0.0, ";
            }
            else
            {
                if ( Float.parseFloat(promedio)>=5.0 && Float.parseFloat(promedio)< 6.0 )
                    query += "SET califant="+promedio+", ";
                else
                    query += "SET califant=0.0, ";
            }
        }
        
        query += "Usuario='"+txtUsuario.toUpperCase()+"', Fecha=date(current), Hora=extend(current, hour to minute) "
                +"WHERE idalu="+idalu+" AND cveMat='"+cvemat+"' AND cveTipMat='"+cvetipmat+"' AND cicescini = "+ cicescini;
        
        stm.execute(query);
    }
    
    public ArrayList<Map> calif3ro (String idalu) throws SQLException
    {
           rs = stm.executeQuery("SELECT "
                                   + "(SELECT cveunidad FROM escuela e WHERE e.idcct = g.idcct)AS Deleg, "
                                   + "(SELECT acreditado FROM alumnodelextrj j WHERE j.idalu = g.idalu AND g.cicescini = j.cicescini) AS AlmExtrj, " 
                                   + "(SELECT TRIM(cct)||SUBSTR(cveturno,1,1) FROM escuela e WHERE e.idcct = g.idcct) AS cct, g.grupo, m.cicescini, "
                                   + "q.ordenimpres, m.idalu, m.cvemat, g.idcct, g.promedio AS promGdo, g.promedioGral, g.promedioEb, g.matrepACT, g.matrepANT, "
                                   + "(CASE "
                                       + "WHEN g.cveplan =2 "
                                       + "THEN (SELECT count(*) FROM exm_ext_ordi x WHERE x.cvetipmat= m.cvetipmat AND x.cvemat= m.cvemat AND x.grado= m.grado AND x.idalu = m.idalu AND x.cicescini = m.cicescini AND x.promedio>=6.0 ) "
                                       + "ELSE 0 END "
                                   + ")AS eer, "
                                   + "m.exm, "
                                   + "(CASE WHEN (m.cvetipmat ='EDT' OR m.cvetipmat ='TEC') THEN '('||m.cvemat||') '||e.desmat  ELSE e.desmat END) AS desmat, "
                                   + "m.promedio, m.califant, m.EstatMat, m.cveprograma, m.cvetipmat "
                               + " FROM alumnogrado g, alumnomaterias m,  materias e, esquemamaterias q "
                               + "WHERE  m.cvemat = e.cvemat AND m.cvetipmat = e.cvetipmat AND m.cvemat = q.cvemat AND m.cvetipmat = q.cvetipmat "
                                   + "AND m.grado = q.grado AND m.grado= 3 AND g.cicescini= m.cicescini AND g.estatusgrado<>'BD' AND g.estatusgrado<>'RC' "
                                   + "AND g.idalu = m.idalu AND g.cveplan=2 AND m.cveprograma = q.cveprograma AND m.cvemat NOT LIKE 'NAP%' "
                                   + "AND m.idalu = "+idalu+" AND q.cicescini<=g.cicescini AND q.cicescfin>=g.cicescini "
                                   //+ "AND g.cicescini != 2018 "          
                               + "ORDER BY q.ordenimpres");
           return qryToArrlmap (rs, null, true, 2);
    }
     
    public ArrayList<Map> exmExt1ro_onSelect (String idalu, String cvetipmat, String cvemat) throws SQLException
    {
        return exmExt_onSelect (idalu, "1", cvetipmat, cvemat);
    }
     
    public void exmExt1ro_onDelete (String idalu_oldValue, String cicescini_oldValue, String idcct_apl_oldValue, String grado_oldValue, String cvemat_oldValue, 
            String cvetipmat_oldValue, String dia_oldValue, String mes_oldValue, String anio_oldValue, String promedio_oldValue) throws SQLException
    {
       exmExt_onDelete (idalu_oldValue, idcct_apl_oldValue, grado_oldValue, cvemat_oldValue, cvetipmat_oldValue, dia_oldValue, mes_oldValue, anio_oldValue, 
               promedio_oldValue);
    }

    public void exmExt1ro_onInsert (String idalu, String cicescini, String idcct_apl, String grado, String cvemat, String cvetipmat, String dia, String mes, 
            String anio, String promedio, String usuario) throws SQLException
    {
        exmExt_onInsert (idalu, cicescini, idcct_apl, grado, cvemat, cvetipmat, dia, mes, anio, promedio, usuario);
    }

    public void exmExt1ro_onUpdate (String idcct_apl, String dia, String mes, String anio, String promedio, String usuario, String idalu_oldValue, 
            String idcct_apl_oldValue, String grado_oldValue, String cvetipmat_oldValue, String cvemat_oldValue, String dia_oldValue, String mes_oldValue, 
            String anio_oldValue, String promedio_oldValue) throws SQLException
    {
        exmExt_onUpdate (idcct_apl, dia, mes, anio, promedio, usuario, idalu_oldValue, idcct_apl_oldValue, grado_oldValue, cvetipmat_oldValue, cvemat_oldValue, 
                dia_oldValue, mes_oldValue, anio_oldValue, promedio_oldValue);
    }
     
    
    public ArrayList<Map> exmExt2do_onSelect (String idalu, String cvetipmat, String cvemat) throws SQLException
    {
        return exmExt_onSelect (idalu, "2", cvetipmat, cvemat);
    }
     
    public void exmExt2do_onDelete (String idalu_oldValue, String cicescini_oldValue, String idcct_apl_oldValue, String grado_oldValue, String cvemat_oldValue, 
            String cvetipmat_oldValue, String dia_oldValue, String mes_oldValue, String anio_oldValue, String promedio_oldValue) throws SQLException
    {
       exmExt_onDelete (idalu_oldValue, idcct_apl_oldValue, grado_oldValue, cvemat_oldValue, cvetipmat_oldValue, dia_oldValue, mes_oldValue, anio_oldValue, 
               promedio_oldValue);
    }

    public void exmExt2do_onInsert (String idalu, String cicescini, String idcct_apl, String grado, String cvemat, String cvetipmat, String dia, String mes, 
            String anio, String promedio, String usuario) throws SQLException
    {
        exmExt_onInsert (idalu, cicescini, idcct_apl, grado, cvemat, cvetipmat, dia, mes, anio, promedio, usuario);
    }

    public void exmExt2do_onUpdate (String idcct_apl, String dia, String mes, String anio, String promedio, String usuario, String idalu_oldValue, 
            String idcct_apl_oldValue, String grado_oldValue, String cvetipmat_oldValue, String cvemat_oldValue, String dia_oldValue, String mes_oldValue, 
            String anio_oldValue, String promedio_oldValue) throws SQLException
    {
        exmExt_onUpdate (idcct_apl, dia, mes, anio, promedio, usuario, idalu_oldValue, idcct_apl_oldValue, grado_oldValue, cvetipmat_oldValue, cvemat_oldValue, 
                dia_oldValue, mes_oldValue, anio_oldValue, promedio_oldValue);
    }
     
    
    public ArrayList<Map> exmExt3ro_onSelect (String idalu, String cvetipmat, String cvemat) throws SQLException
    {
        return exmExt_onSelect (idalu, "3", cvetipmat, cvemat);
    }
     
    public void exmExt3ro_onDelete (String idalu_oldValue, String cicescini_oldValue, String idcct_apl_oldValue, String grado_oldValue, String cvemat_oldValue, 
            String cvetipmat_oldValue, String dia_oldValue, String mes_oldValue, String anio_oldValue, String promedio_oldValue) throws SQLException
    {
       exmExt_onDelete (idalu_oldValue, idcct_apl_oldValue, grado_oldValue, cvemat_oldValue, cvetipmat_oldValue, dia_oldValue, mes_oldValue, anio_oldValue, 
               promedio_oldValue);
    }    
    
    public void exmExt3ro_onInsert (String idalu, String cicescini, String idcct_apl, String grado, String cvemat, String cvetipmat, String dia, String mes, 
            String anio, String promedio, String usuario) throws SQLException
    {      
        exmExt_onInsert (idalu, cicescini, idcct_apl, grado, cvemat, cvetipmat, dia, mes, anio, promedio, usuario);
    }
    
    public void exmExt3ro_onUpdate (String idcct_apl, String dia, String mes, String anio, String promedio, String usuario, String idalu_oldValue, 
            String idcct_apl_oldValue, String grado_oldValue, String cvetipmat_oldValue, String cvemat_oldValue, String dia_oldValue, String mes_oldValue, 
            String anio_oldValue, String promedio_oldValue) throws SQLException
    {
        exmExt_onUpdate (idcct_apl, dia, mes, anio, promedio, usuario, idalu_oldValue, idcct_apl_oldValue, grado_oldValue, cvetipmat_oldValue, cvemat_oldValue, 
                dia_oldValue, mes_oldValue, anio_oldValue, promedio_oldValue);
    }
    
    
    private ArrayList<Map> exmExt_onSelect (String idalu, String grado, String cvetipmat, String cvemat) throws SQLException
    {
        rs = stm.executeQuery("SELECT "
                               + "*, (SELECT TRIM(cct)||SUBSTR(cveturno,1,1) FROM escuela WHERE idcct = exm_ext_ordi.idcct_apl ) AS cct "
                           + "FROM exm_ext_ordi "
                           + "WHERE grado = "+grado+" AND idalu="+idalu+" AND cvetipmat = '"+cvetipmat+"' AND cvemat= '"+cvemat+"'");
        return qryToArrlmap (rs, null, true, 1); 
    } 
    
    private void exmExt_onDelete (String idalu_oldValue, String idcct_apl_oldValue, String grado_oldValue, String cvemat_oldValue, 
            String cvetipmat_oldValue, String dia_oldValue, String mes_oldValue, String anio_oldValue, String promedio_oldValue) throws SQLException
    {
        stm.execute("DELETE FROM exm_ext_ordi "
                       + "WHERE idalu="+idalu_oldValue+" AND grado="+grado_oldValue+" AND idcct_apl="+idcct_apl_oldValue+" AND cvetipmat='"+cvetipmat_oldValue+"' "
                           + "AND cvemat='"+cvemat_oldValue+"' AND dia="+dia_oldValue+" AND mes='"+mes_oldValue+"' AND anio="+anio_oldValue+" "
                           + "AND promedio="+promedio_oldValue);
    }
     
    private void exmExt_onInsert (String idalu, String cicescini, String idcct_apl, String grado, String cvemat, String cvetipmat, String dia, String mes, 
            String anio, String promedio, String usuario) throws SQLException
    {
        /* Se actualiza la tabla de examenes extraordinarios */
        stm.execute("INSERT INTO exm_ext_ordi (idalu, grado, idcct_apl, cicescini, cvetipmat, cvemat, dia, mes, anio, promedio, usuario, fecha, hora) "
                       + "VALUES ("+idalu+", "+grado+", "+idcct_apl+", "+cicescini+", '"+cvetipmat+"', '"+cvemat+"', "+dia+",  '"+mes+"', "+anio+", "+promedio+", "
                           + "'"+usuario+"', date(current), extend(current, hour to minute) )");
        
        /* Respectivamente actualizamos el promedio en la tabla de alumnomaterias agragado el 06-03-2025 */ 
        /* stm.execute("UPDATE alumnomaterias SET "
                    + "califant = promedio, "
                    + "promedio = " + promedio + ", "
                    + "usuario = " + usuario + ", "       
                    + "fecha = date(current), "
                    + "hora = extend(current, hour to minute) "         
                    + "WHERE idalu = " + idalu + " AND cicescini=" + cicescini + " AND grado=" + grado + " AND "
                    + "cvemat="+cvemat + " AND cvetipmat="+cvetipmat); */
    }
    
    private void exmExt_onUpdate(String idalu, String cicescini, String idcct_apl, String grado, String cvemat, String cvetipmat,
            String promedio, String usuario)  throws SQLException
    {
        stm.execute("UPDATE alumnomaterias SET "
                    + "promedio = " + promedio + " "
                    + "WHERE idalu = " + idalu + " AND cicescini=" + cicescini + " AND grado=" + grado + " AND "
                    + "cvemat="+cvemat + " AND cvetipmat="+cvetipmat);
    }
    
    private String exmExt_validateDate (String cicescini, String dia, String mes, String anio) throws SQLException
    {
        String fecha = "";
        rs = stm.executeQuery("SELECT fecha_certi FROM exm_calendario WHERE exm_anio = "+anio+" AND exm_mes= '"+mes+"' AND exm_dia= "+dia+"");
        if(rs.next()){
            fecha = ""+rs.getString("fecha_certi");
        }
        return fecha;
    }
        
    private void exmExt_onUpdate (String idcct_apl, String dia, String mes, String anio, String promedio, String usuario, String idalu_oldValue, 
           String idcct_apl_oldValue, String grado_oldValue, String cvetipmat_oldValue, String cvemat_oldValue, String dia_oldValue, String mes_oldValue, 
           String anio_oldValue, String promedio_oldValue) throws SQLException
    {
        if (!idcct_apl.equals(idcct_apl_oldValue) || !dia.equals(dia_oldValue) || !mes.equals(mes_oldValue) || !anio.equals(anio_oldValue) || !promedio.equals(promedio_oldValue))
            stm.execute("UPDATE exm_ext_ordi SET idcct_apl="+idcct_apl+", dia="+dia+", mes='"+mes+"', anio = "+anio+", promedio="+promedio+", usuario='"+usuario+"', "
                               + "fecha=date(current), hora = extend(current, hour to minute) "
                           + "WHERE idalu="+idalu_oldValue+" AND grado = "+grado_oldValue+" AND idcct_apl="+idcct_apl_oldValue+" AND cvetipmat='"+cvetipmat_oldValue+"' "
                               + "AND cvemat='"+cvemat_oldValue+"' AND dia="+dia_oldValue+" AND mes = '"+mes_oldValue+"' AND anio = "+anio_oldValue+" "
                               + "AND promedio = "+promedio_oldValue);
    }
    
    public void guardaTablaCalif1ro_ (ArrayList<Map>tblCalif1ro, int cicescin, String tblCalif1ro_cicescini, int tblPrincipal_cveplan, 
            String tblCalif1ro_idalu, String tblCalif1ro_almextrj, String txtUsuario, SICEEO_DataModule dm) throws SQLException
    {
        int matRep, noMat, elGrado, tblCalif1ro_numRows = tblCalif1ro.size();
        String sumCalif;
        String prom, camCalif, elCiclo, elIdalu, ylapaso;
        String qryMatCicAntU, qryAlumGdo;
        
        matRep = 0;
        sumCalif = "0";
        noMat = 0;
        prom = "0.0";                                                           //para calcular el promedio
        camCalif = "x";                                                         //variable para saber si modificaron una calificacion
        ylapaso = "x";                                                          //variable para saber que ya paso la materia

        elCiclo = tblCalif1ro_cicescini;
        elIdalu = tblCalif1ro_idalu;
        elGrado = 1;

        int f=0;
        while (f<tblCalif1ro_numRows)
        {
            qryMatCicAntU = "UPDATE AlumnoMATERIAS ";
            qryMatCicAntU += "SET promedio="+tblCalif1ro.get(f).get("promedio") + ", ";

            if ( dm.toFloat(tblCalif1ro.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalif1ro.get(f).get("promedio_oldValue"))<6.0 && dm.toFloat(tblCalif1ro.get(f).get("califant"))==0 ) //si antes tenia entre 5.0 y 5.9
                qryMatCicAntU += "califant="+tblCalif1ro.get(f).get("promedio_oldValue")+ ", ";      //    pasa el valor a califant
            else                                                            //si el usuario captura califant hay k guardarla
                if ( dm.toFloat(tblCalif1ro.get(f).get("califant"))>=5.0 && dm.toFloat(tblCalif1ro.get(f).get("califant"))< 6.0 )//y solo puede guardar califant reprobatoria entre 5.0 y 5.9
                    qryMatCicAntU += "califant="+tblCalif1ro.get(f).get("califant")+ ", ";
            
                sumCalif = dm.sumarFraccion (sumCalif, tblCalif1ro.get(f).get("promedio")); //sumCalif = sumCalif + dm.toFloat(tblCalif1ro.get(f).get("promedio"));
                noMat = noMat + 1;
                
                //si tiene examen global ------------------------------------
                if ( dm.toFloat(tblCalif1ro.get(f).get("promedio")) == 0.0 && ( tblCalif1ro_almextrj.equals("EG") || tblCalif1ro_almextrj.equals("TR") || tblCalif1ro_almextrj.equals("RV")) )
                {
                    qryMatCicAntU += "estatmat='E', ";
                    noMat = noMat - 1;
                }

                //si la reprueba---------------------------------------------
                if ( dm.toFloat(tblCalif1ro.get(f).get("promedio"))>= 5.0 && dm.toFloat(tblCalif1ro.get(f).get("promedio"))<6.0 ) 
                {
                    qryMatCicAntU += "estatmat='R' , ";
                    matRep = matRep + 1;
                }
                
                //si la aprueba----------------------------------------------
                if ( dm.toFloat(tblCalif1ro.get(f).get("promedio"))>=6.0 )
                    qryMatCicAntU += "estatmat='A', ";

                qryMatCicAntU+="Usuario='"+txtUsuario.trim().toUpperCase()+"', Fecha=date(current), Hora=extend(current, hour to minute) "
                        + "WHERE idalu="+tblCalif1ro_idalu+" AND cveMat='"+tblCalif1ro.get(f).get("cvemat")+"' AND cveTipMat='"+tblCalif1ro.get(f).get("cvetipmat")+"' "
                            + "AND cicescini="+ tblCalif1ro_cicescini+" "; // inttostr(dm.v_Califcicescin));
                //solo se guardara si la calificacion cambia de valor respecto al anterior
                //      if (dm2.q_Calif1ropromedio.Value <> dm2.q_Calif1ropromedio.OldValue) or
                //         (dm2.q_Calif1rocalifant.Value <> dm2.q_Calif1rocalifant.OldValue) then
                //        {
                camCalif="si";
                if ( dm.toFloat(tblCalif1ro.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalif1ro.get(f).get("promedio_oldValue"))< 6.0 && dm.toFloat(tblCalif1ro.get(f).get("promedio"))>=6 )//antes tenia entre 5.0 y 5.9  //su nueva calificacion es >=6.0
                    ylapaso="si";
                //       MessageDlg('qry:'+#13+#10+dm.Q_Mat_cicAnt_U.SQL.Text, mtWarning, [mbOK], 0);
                stm.execute(qryMatCicAntU);
                //       }
                // }
                f++;
        }
        //********************************************************************************************
        //PARA SECUNDARIA
        if ( tblPrincipal_cveplan == 2 )
        {
            if ( matRep >= 1 )  // con una k repruebe no se puede guardar su promedio
                prom = "0.0";
            else{
                String promTemp = dm.dividirFraccion(sumCalif, noMat);
                prom = promTemp.length()>3 ? promTemp.substring(0,4) : promTemp;    //prom = (""+(sumCalif/noMat)).length()>3 ? (""+(sumCalif/noMat)).substring(0,4) : (""+(sumCalif/noMat));
            }
            if ( dm.toFloat(prom)<10 ) prom=prom.substring(0,3);
        }
        //************************************************************************************************
        qryAlumGdo = "UPDATE alumnoGrado SET Promedio="+prom+", ";
        //SECUNDARIA
        if ( elGrado == 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='NP', ";
        else if ( elGrado!= 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='P', ";
        else                                                                    //   valores cu&&o capturan algun ciclo anterior
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && tblPrincipal_cveplan==2 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RC', ";
        else if ( elGrado!=3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RE', ";

        //---------------------------------------------------
        //Para secundaria
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep>0 )
            qryAlumGdo += "PromedioGral=0, ";

        if ( matRep <=5 && tblPrincipal_cveplan==2 )
            qryAlumGdo += "Promovido='P', ";

        qryAlumGdo += "MatRepAct="+matRep+", usuario='"+txtUsuario.trim().toUpperCase()+"', fecha=date(current), hora=extend(current, hour to minute) "
                + "WHERE cicescini="+elCiclo+" AND idalu="+elIdalu;
     // MessageDlg('qry:'+#13+#10+dm.Q_AlumGdo.SQL.Text, mtWarning, [mbOK], 0);
        stm.execute(qryAlumGdo);
    }
    
    public void guardaTablaCalif2do_ (ArrayList<Map>tblCalif2do, int cicescin, String tblCalif2do_cicescini, int tblPrincipal_cveplan, 
            String tblCalif2do_idalu, String tblCalif2do_almextrj, String txtUsuario, SICEEO_DataModule dm) throws SQLException
    {
        int matRep, noMat, elGrado, tblCalif2do_numRows = tblCalif2do.size();
        String sumCalif;
        String prom, camCalif, elCiclo, elIdalu, ylapaso;
        String qryMatCicAntU, qryAlumGdo;
        
        matRep = 0;
        sumCalif = "0";
        noMat = 0;
        prom = "0.0";                                                           //para calcular el promedio
        camCalif = "x";                                                         //variable para saber si modificaron una calificacion
        ylapaso = "x";                                                          //variable para saber que ya paso la materia

        elCiclo = tblCalif2do_cicescini;
        elIdalu = tblCalif2do_idalu;
        elGrado = 1;

        int f=0;
        while (f<tblCalif2do_numRows)
        {
            qryMatCicAntU = "UPDATE AlumnoMATERIAS ";
            qryMatCicAntU += "SET promedio="+tblCalif2do.get(f).get("promedio") + ", ";

            if ( dm.toFloat(tblCalif2do.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalif2do.get(f).get("promedio_oldValue"))<6.0 && dm.toFloat(tblCalif2do.get(f).get("califant"))==0 ) //si antes tenia entre 5.0 y 5.9
                qryMatCicAntU += "califant="+tblCalif2do.get(f).get("promedio_oldValue")+ ", ";      //    pasa el valor a califant
            else                                                            //si el usuario captura califant hay k guardarla
                if ( dm.toFloat(tblCalif2do.get(f).get("califant"))>=5.0 && dm.toFloat(tblCalif2do.get(f).get("califant"))< 6.0 )//y solo puede guardar califant reprobatoria entre 5.0 y 5.9
                    qryMatCicAntU += "califant="+tblCalif2do.get(f).get("califant")+ ", ";
            
                sumCalif = dm.sumarFraccion (sumCalif, tblCalif2do.get(f).get("promedio"));   //sumCalif = sumCalif + dm.toFloat(tblCalif2do.get(f).get("promedio"));
                noMat = noMat + 1;
                
                //si tiene examen global ------------------------------------
                if ( dm.toFloat(tblCalif2do.get(f).get("promedio")) == 0.0 && ( tblCalif2do_almextrj.equals("EG") || tblCalif2do_almextrj.equals("TR") || tblCalif2do_almextrj.equals("RV")) )
                {
                    qryMatCicAntU += "estatmat='E', ";
                    noMat = noMat - 1;
                }

                //si la reprueba---------------------------------------------
                if ( dm.toFloat(tblCalif2do.get(f).get("promedio"))>= 5.0 && dm.toFloat(tblCalif2do.get(f).get("promedio"))<6.0 ) 
                {
                    qryMatCicAntU += "estatmat='R' , ";
                    matRep = matRep + 1;
                }
                
                //si la aprueba----------------------------------------------
                if ( dm.toFloat(tblCalif2do.get(f).get("promedio"))>=6.0 )
                    qryMatCicAntU += "estatmat='A', ";

                qryMatCicAntU+="Usuario='"+txtUsuario.trim().toUpperCase()+"', Fecha=date(current), Hora=extend(current, hour to minute) "
                        + "WHERE idalu="+tblCalif2do_idalu+" AND cveMat='"+tblCalif2do.get(f).get("cvemat")+"' AND cveTipMat='"+tblCalif2do.get(f).get("cvetipmat")+"' "
                            + "AND cicescini="+ tblCalif2do_cicescini+" "; // inttostr(dm.v_Califcicescin));
                //solo se guardara si la calificacion cambia de valor respecto al anterior
                //      if (dm2.q_Calif2dopromedio.Value <> dm2.q_Calif2dopromedio.OldValue) or
                //         (dm2.q_Calif2docalifant.Value <> dm2.q_Calif2docalifant.OldValue) then
                //        {
                camCalif="si";
                if ( dm.toFloat(tblCalif2do.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalif2do.get(f).get("promedio_oldValue"))< 6.0 && dm.toFloat(tblCalif2do.get(f).get("promedio"))>=6 )//antes tenia entre 5.0 y 5.9  //su nueva calificacion es >=6.0
                    ylapaso="si";
                //       MessageDlg('qry:'+#13+#10+dm.Q_Mat_cicAnt_U.SQL.Text, mtWarning, [mbOK], 0);
                stm.execute(qryMatCicAntU);
                //       }
                // }
                f++;
        }
        //********************************************************************************************
        //PARA SECUNDARIA
        if ( tblPrincipal_cveplan == 2 )
        {
            if ( matRep >= 1 )  // con una k repruebe no se puede guardar su promedio
                prom = "0.0";
            else{
                String promTemp = dm.dividirFraccion(sumCalif, noMat);
                prom = promTemp.length()>3 ? promTemp.substring(0,4) : promTemp;    //prom = (""+(sumCalif/noMat)).length()>3 ? (""+(sumCalif/noMat)).substring(0,4) : (""+(sumCalif/noMat));
            }
            if ( dm.toFloat(prom)<10 ) prom=prom.substring(0,3);
        }
        //************************************************************************************************
        qryAlumGdo = "UPDATE alumnoGrado SET Promedio="+prom+", ";
        //SECUNDARIA
        if ( elGrado == 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='NP', ";
        else if ( elGrado!= 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='P', ";
        else                                                                    //   valores cu&&o capturan algun ciclo anterior
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && tblPrincipal_cveplan==2 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RC', ";
        else if ( elGrado!=3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RE', ";

        //---------------------------------------------------
        //Para secundaria
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep>0 )
            qryAlumGdo += "PromedioGral=0, ";

        if ( matRep <=5 && tblPrincipal_cveplan==2 )
            qryAlumGdo += "Promovido='P', ";

        qryAlumGdo += "MatRepAct="+matRep+", usuario='"+txtUsuario.trim().toUpperCase()+"', fecha=date(current), hora=extend(current, hour to minute) "
                + "WHERE cicescini="+elCiclo+" AND idalu="+elIdalu;
     // MessageDlg('qry:'+#13+#10+dm.Q_AlumGdo.SQL.Text, mtWarning, [mbOK], 0);
        stm.execute(qryAlumGdo);
    }
    
    public void guardaTablaCalif3ro_ (ArrayList<Map>tblCalif3ro, int cicescin, String tblCalif3ro_cicescini, int tblPrincipal_cveplan, 
            String tblCalif3ro_idalu, String tblCalif3ro_almextrj, String txtUsuario, SICEEO_DataModule dm) throws SQLException
    {
        int matRep, noMat, elGrado, tblCalif3ro_numRows = tblCalif3ro.size();
        String sumCalif;
        String prom, camCalif, elCiclo, elIdalu, ylapaso;
        String qryMatCicAntU, qryAlumGdo;
        
        matRep = 0;
        sumCalif = "0";
        noMat = 0;
        prom = "0.0";                                                           //para calcular el promedio
        camCalif = "x";                                                         //variable para saber si modificaron una calificacion
        ylapaso = "x";                                                          //variable para saber que ya paso la materia

        elCiclo = tblCalif3ro_cicescini;
        elIdalu = tblCalif3ro_idalu;
        elGrado = 1;

        int f=0;
        while (f<tblCalif3ro_numRows)
        {
            qryMatCicAntU = "UPDATE AlumnoMATERIAS ";
            qryMatCicAntU += "SET promedio="+tblCalif3ro.get(f).get("promedio") + ", ";

            if ( dm.toFloat(tblCalif3ro.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalif3ro.get(f).get("promedio_oldValue"))<6.0 && dm.toFloat(tblCalif3ro.get(f).get("califant"))==0 ) //si antes tenia entre 5.0 y 5.9
                qryMatCicAntU += "califant="+tblCalif3ro.get(f).get("promedio_oldValue")+ ", ";      //    pasa el valor a califant
            else                                                            //si el usuario captura califant hay k guardarla
                if ( dm.toFloat(tblCalif3ro.get(f).get("califant"))>=5.0 && dm.toFloat(tblCalif3ro.get(f).get("califant"))< 6.0 )//y solo puede guardar califant reprobatoria entre 5.0 y 5.9
                    qryMatCicAntU += "califant="+tblCalif3ro.get(f).get("califant")+ ", ";
            
                sumCalif = dm.sumarFraccion (sumCalif, tblCalif3ro.get(f).get("promedio"));   //sumCalif = sumCalif + dm.toFloat(tblCalif3ro.get(f).get("promedio"));
                noMat = noMat + 1;
                
                //si tiene examen global ------------------------------------
                if ( dm.toFloat(tblCalif3ro.get(f).get("promedio")) == 0.0 && ( tblCalif3ro_almextrj.equals("EG") || tblCalif3ro_almextrj.equals("TR") || tblCalif3ro_almextrj.equals("RV")) )
                {
                    qryMatCicAntU += "estatmat='E', ";
                    noMat = noMat - 1;
                }

                //si la reprueba---------------------------------------------
                if ( dm.toFloat(tblCalif3ro.get(f).get("promedio"))>= 5.0 && dm.toFloat(tblCalif3ro.get(f).get("promedio"))<6.0 ) 
                {
                    qryMatCicAntU += "estatmat='R' , ";
                    matRep = matRep + 1;
                }
                
                //si la aprueba----------------------------------------------
                if ( dm.toFloat(tblCalif3ro.get(f).get("promedio"))>=6.0 )
                    qryMatCicAntU += "estatmat='A', ";

                qryMatCicAntU+="Usuario='"+txtUsuario.trim().toUpperCase()+"', Fecha=date(current), Hora=extend(current, hour to minute) "
                        + "WHERE idalu="+tblCalif3ro_idalu+" AND cveMat='"+tblCalif3ro.get(f).get("cvemat")+"' AND cveTipMat='"+tblCalif3ro.get(f).get("cvetipmat")+"' "
                            + "AND cicescini="+ tblCalif3ro_cicescini+" "; // inttostr(dm.v_Califcicescin));
                //solo se guardara si la calificacion cambia de valor respecto al anterior
                //      if (dm2.q_Calif3ropromedio.Value <> dm2.q_Calif3ropromedio.OldValue) or
                //         (dm2.q_Calif3rocalifant.Value <> dm2.q_Calif3rocalifant.OldValue) then
                //        {
                camCalif="si";
                if ( dm.toFloat(tblCalif3ro.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalif3ro.get(f).get("promedio_oldValue"))< 6.0 && dm.toFloat(tblCalif3ro.get(f).get("promedio"))>=6 )//antes tenia entre 5.0 y 5.9  //su nueva calificacion es >=6.0
                    ylapaso="si";
                //       MessageDlg('qry:'+#13+#10+dm.Q_Mat_cicAnt_U.SQL.Text, mtWarning, [mbOK], 0);
                stm.execute(qryMatCicAntU);
                //       }
                // }
                f++;
        }
        //********************************************************************************************
        //PARA SECUNDARIA
        if ( tblPrincipal_cveplan == 2 )
        {
            if ( matRep >= 1 )  // con una k repruebe no se puede guardar su promedio
                prom = "0.0";
            else{
                String promTemp = dm.dividirFraccion(sumCalif, noMat);
                prom = promTemp.length()>3 ? promTemp.substring(0,4) : promTemp;  //prom = (""+(sumCalif/noMat)).length()>3 ? (""+(sumCalif/noMat)).substring(0,4) : (""+(sumCalif/noMat));
            }
            if ( dm.toFloat(prom)<10 ) prom=prom.substring(0,3);
        }
        //************************************************************************************************
        qryAlumGdo = "UPDATE alumnoGrado SET Promedio="+prom+", ";
        //SECUNDARIA
        if ( elGrado == 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='NP', ";
        else if ( elGrado!= 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='P', ";
        else                                                                    //   valores cu&&o capturan algun ciclo anterior
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && tblPrincipal_cveplan==2 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RC', ";
        else if ( elGrado!=3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RE', ";

        //---------------------------------------------------
        //Para secundaria
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep>0 )
            qryAlumGdo += "PromedioGral=0, ";

        if ( matRep <=5 && tblPrincipal_cveplan==2 )
            qryAlumGdo += "Promovido='P', ";

        qryAlumGdo += "MatRepAct="+matRep+", usuario='"+txtUsuario.trim().toUpperCase()+"', fecha=date(current), hora=extend(current, hour to minute) "
                + "WHERE cicescini="+elCiclo+" AND idalu="+elIdalu;
     // MessageDlg('qry:'+#13+#10+dm.Q_AlumGdo.SQL.Text, mtWarning, [mbOK], 0);
        stm.execute(qryAlumGdo);
    }
    
    public boolean verificaExExtra(String idalu, String cvemat, String cvetipmat, String grado, String cicescini) throws SQLException{
        boolean tiene_promedio=false;
        rs = stm.executeQuery("SELECT promedio FROM exm_ext_ordi WHERE idalu = "+idalu +" AND cvetipmat='"+cvetipmat
            + "' AND cvemat='"+cvemat+"' AND cicescini="+cicescini);
        if(rs.next())
            tiene_promedio = true;
        return tiene_promedio;    
    }
    
    public String guardaTablaCalifNGdo (ArrayList<Map>tblCalifNGdo, int cicescin, String tblCalifNGdo_cicescini, int tblPrincipal_cveplan, 
            String tblCalifNGdo_idalu, String tblCalifNGdo_almextrj, String txtUsuario, SICEEO_DataModule dm) throws SQLException
    {
        int matRep, noMat, elGrado, tblCalifNGdo_numRows = tblCalifNGdo.size();
        String sumCalif;
        String prom, camCalif, elCiclo, elIdalu, ylapaso;
        String qryMatCicAntU, qryAlumGdo;
        
        matRep = 0;
        sumCalif = "0";
        noMat = 0;
        prom = "0.0";                                                           //para calcular el promedio
        camCalif = "x";                                                         //variable para saber si modificaron una calificacion
        ylapaso = "x";                                                          //variable para saber que ya paso la materia

        elCiclo = tblCalifNGdo_cicescini;
        elIdalu = tblCalifNGdo_idalu;
        elGrado = 1;

        int f=0;
        while (f<tblCalifNGdo_numRows)
        {
            qryMatCicAntU = "UPDATE AlumnoMATERIAS ";
            qryMatCicAntU += "SET promedio="+tblCalifNGdo.get(f).get("promedio") + ", ";

            if ( dm.toFloat(tblCalifNGdo.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalifNGdo.get(f).get("promedio_oldValue"))<6.0 && dm.toFloat(tblCalifNGdo.get(f).get("califant"))==0 ) //si antes tenia entre 5.0 y 5.9
                qryMatCicAntU += "califant="+tblCalifNGdo.get(f).get("promedio_oldValue")+ ", ";      //    pasa el valor a califant
            else                                                            //si el usuario captura califant hay k guardarla
                if ( dm.toFloat(tblCalifNGdo.get(f).get("califant"))>=5.0 && dm.toFloat(tblCalifNGdo.get(f).get("califant"))< 6.0 )//y solo puede guardar califant reprobatoria entre 5.0 y 5.9
                    qryMatCicAntU += "califant="+tblCalifNGdo.get(f).get("califant")+ ", ";

                sumCalif = dm.sumarFraccion (sumCalif, tblCalifNGdo.get(f).get("promedio"));         //sumCalif = sumCalif + dm.toFloat(tblCalifNGdo.get(f).get("promedio"));
                noMat = noMat + 1;
                
                //si tiene examen global ------------------------------------
                if ( dm.toFloat(tblCalifNGdo.get(f).get("promedio")) == 0.0 && ( tblCalifNGdo_almextrj.equals("EG") || tblCalifNGdo_almextrj.equals("TR") || tblCalifNGdo_almextrj.equals("RV")) )
                {
                    qryMatCicAntU += "estatmat='E', ";
                    noMat = noMat - 1;
                }

                //si la reprueba---------------------------------------------
                if ( dm.toFloat(tblCalifNGdo.get(f).get("promedio"))>= 5.0 && dm.toFloat(tblCalifNGdo.get(f).get("promedio"))<6.0 ) 
                {
                    qryMatCicAntU += "estatmat='R' , ";
                    matRep = matRep + 1;
                }
                
                //si la aprueba----------------------------------------------
                if ( dm.toFloat(tblCalifNGdo.get(f).get("promedio"))>=6.0 )
                    qryMatCicAntU += "estatmat='A', ";

                qryMatCicAntU+="Usuario='"+txtUsuario.trim().toUpperCase()+"', Fecha=date(current), Hora=extend(current, hour to minute) "
                        + "WHERE idalu="+tblCalifNGdo_idalu+" AND cveMat='"+tblCalifNGdo.get(f).get("cvemat")+"' AND cveTipMat='"+tblCalifNGdo.get(f).get("cvetipmat")+"' "
                            + "AND cicescini="+ tblCalifNGdo_cicescini+" "; // inttostr(dm.v_Califcicescin));
                //solo se guardara si la calificacion cambia de valor respecto al anterior
                //      if (dm2.q_CalifNGdopromedio.Value <> dm2.q_CalifNGdopromedio.OldValue) or
                //         (dm2.q_CalifNGdocalifant.Value <> dm2.q_CalifNGdocalifant.OldValue) then
                //        {
                camCalif="si";
                if ( dm.toFloat(tblCalifNGdo.get(f).get("promedio_oldValue"))>=5.0 && dm.toFloat(tblCalifNGdo.get(f).get("promedio_oldValue"))< 6.0 && dm.toFloat(tblCalifNGdo.get(f).get("promedio"))>=6 )//antes tenia entre 5.0 y 5.9  //su nueva calificacion es >=6.0
                    ylapaso="si";
                //       MessageDlg('qry:'+#13+#10+dm.Q_Mat_cicAnt_U.SQL.Text, mtWarning, [mbOK], 0);
                stm.execute(qryMatCicAntU);
                //       }
                // }
                f++;
        }
        //********************************************************************************************
        //PARA SECUNDARIA
        if ( tblPrincipal_cveplan == 2 )
        {
            if ( matRep >= 1 )  // con una k repruebe no se puede guardar su promedio
                prom = "0.0";
            else{
                String promTemp = dm.dividirFraccion(sumCalif, noMat);
                prom = promTemp.length()>3 ? promTemp.substring(0,4) : promTemp;   //prom = (""+(sumCalif/noMat)).length()>3 ? (""+(sumCalif/noMat)).substring(0,4) : (""+(sumCalif/noMat));
            }
            if ( dm.toFloat(prom)<10 ) prom=prom.substring(0,3);
        }
        //************************************************************************************************
        qryAlumGdo = "UPDATE alumnoGrado SET Promedio="+prom+", ";
        //SECUNDARIA
        if ( elGrado == 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='NP', ";
        else if ( elGrado!= 3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)==cicescin )
            qryAlumGdo += "estatusgrado='P', ";
        else                                                                    //   valores cu&&o capturan algun ciclo anterior
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='C', ";
        else if ( matRep>=6 && tblPrincipal_cveplan==2 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RC', ";
        else if ( elGrado!=3 && tblPrincipal_cveplan==2 && matRep<=5 && dm.toInt(elCiclo)<cicescin )
            qryAlumGdo += "estatusgrado='RE', ";

        //---------------------------------------------------
        //Para secundaria
        if ( elGrado==3 && tblPrincipal_cveplan==2 && matRep>0 )
            qryAlumGdo += "PromedioGral=0, ";

        if ( matRep <=5 && tblPrincipal_cveplan==2 )
            qryAlumGdo += "Promovido='P', ";

        qryAlumGdo += "MatRepAct="+matRep+", usuario='"+txtUsuario.trim().toUpperCase()+"', fecha=date(current), hora=extend(current, hour to minute) "
                + "WHERE cicescini="+elCiclo+" AND idalu="+elIdalu;
     // MessageDlg('qry:'+#13+#10+dm.Q_AlumGdo.SQL.Text, mtWarning, [mbOK], 0);
        stm.execute(qryAlumGdo);
        return prom;
    }
    
    public Map actualizaReprobadas (String tblAlumCapCalif_idalu, String tblCalif2do_cicescini, int tblCalif3ro_cicescini, int tblCalif1ro_matRepAct,
            int tblCalif2do_matRepAct, int tblCalif1ro_size,int tblCalif2do_size, int tblCalif3ro_size) throws SQLException
    {
        Map matRepants = new HashMap();
        int matRep1ro, matRep2do;
        if ( tblCalif1ro_size >= 1 )
            matRep1ro = tblCalif1ro_matRepAct;
        else
            matRep1ro = 0;

        if ( tblCalif2do_size >= 1 )
            matRep2do = tblCalif2do_matRepAct;
        else
            matRep2do = 0;
        
        if ( tblCalif2do_size >= 1 ){
            stm.execute("UPDATE AlumnoGRADO SET matrepAnt="+matRep1ro+" "
                    + "WHERE grado=2 AND idalu="+tblAlumCapCalif_idalu+" AND cveplan=2 AND cicescini="+tblCalif2do_cicescini );
            matRepants.put("tblCalif2do_matrepant", matRep1ro);

        }if ( tblCalif3ro_size >= 1 ){
            stm.execute("UPDATE AlumnoGRADO SET matrepAnt="+(matRep1ro + matRep2do)+" "
                    + "WHERE grado=3 AND idalu="+tblAlumCapCalif_idalu+" AND cveplan=2 AND cicescini = "+tblCalif3ro_cicescini );
            matRepants.put("tblCalif3ro_matrepant", (matRep1ro + matRep2do));
        }
        return matRepants;
    }
    
    public ArrayList<Map> actualizaPromedioGeneral (int tblCalif3ro_size, int tblCalif3ro_cicescini, String idalu, String txtUsuario, boolean []errorCalcPromGral, SICEEO_DataModule dm) throws SQLException
    {
        ArrayList<Map>QPagoSusMatEn2012;
        Map QPromGral, QPromGral2012, QAlumProm;
        String qryMatCicAntU="";
        ArrayList<Map> QCalif3ro=null;
        
        int noMat=0;
        float sumProm=0, promSec,PromPrim, promEB;
        float dato1, dato2, dato3, dato4, dato5, dato8;
        String dato6, dato7;
        //**********************************************************
        if ( tblCalif3ro_size >0 )
        {
            //chekar si pago materias en el ciclo 2012-13 para aplicar
            // la formula de etre de 3 y su promedio de educacion basica

            //que formula le aplicamos?
            QPagoSusMatEn2012 = pagoSusMatEn2012(idalu);
            if ( QPagoSusMatEn2012.size()>0 || (tblCalif3ro_cicescini>= 2012) ) //(dm.v_Califcicescin >= 2012) then //Pago materias en el 2012 o 2013 o etc
            {
                //           formula:='nueva>=2012'
                rs = stm.executeQuery("SELECT idalu, count(distinct grado) AS Grados, (trunc(sum(promedio)*10/count(*),0))/10 AS promgral "
                        + "FROM alumnoGRADO "
                        + "WHERE idalu="+idalu+" AND cveplan=2 "
                            + "AND cicescini in (  SELECT cicescini "
                                                + "FROM alumnogrado "
                                                + "WHERE idalu="+idalu+" AND promedio>=6.0 AND cveplan=2 AND estatusgrado<>'BD' AND estatusgrado<>'RC' ) "
                            + "AND promedio>=6.0 "
                        + "GROUP BY idalu ");
            } else {
                //           formula:='anterior<=2011';
                //promedio de secundaria entre de 27 materias
                rs = stm.executeQuery("SELECT idalu, count(distinct grado) AS Grados, (trunc(sum(promedio)*10/count(*),0))/10 AS promgral "
                        + "FROM alumnoMATERIAS "
                        + "WHERE idalu="+idalu+" AND cveplan=2 "
                            + "AND cicescini IN (SELECT cicescini "
                                                + "FROM alumnogrado "
                                                + "WHERE idalu="+idalu+" AND promedio>=6.0 AND cveplan=2 AND estatusgrado<>'BD' AND estatusgrado<>'RC' ) "
                            + "AND promedio>=6.0 "
                        + "GROUP BY idalu ");
            }
            QPromGral = qryToMap (rs, null, true, 1);
            //**********************************************************

            //----------------------------------------------------------
            if ( (""+QPromGral.get("grados")).equals("3"))
            {
                QAlumProm  = this.getAlumProm2012 (idalu);                          //OJO: Agregado para uso sólo en SICEEO
                if ( Float.parseFloat(""+QPromGral.get("promgral"))>=6.0 && QPromGral.get("grados").equals("3") )
                {
                    if (tblCalif3ro_cicescini == 2012 && dm.toFloat(QAlumProm.get("promdp"))>0)                          //OJO: Este if se agrega sólo para SICEEO
                    {
                        /*QPromGral2012=this.promGral2012 (idalu,""+tblCalif3ro_cicescini);
                        if  ( dm.toFloat(QPromGral2012.get("promd1ros"))>=6.0 ) { noMat=1;  sumProm=dm.toFloat(QPromGral2012.get("promd1ros"))*10; }
                        if  ( dm.toFloat(QPromGral2012.get("promd2dos"))>=6.0 ) { noMat=noMat+1; sumProm=sumProm+dm.toFloat(QPromGral2012.get("promd2dos"))*10; }
                        if  ( dm.toFloat(QPromGral2012.get("promd3ros"))>=6.0 ) { noMat=noMat+1; sumProm=sumProm+dm.toFloat(QPromGral2012.get("promd3ros"))*10; }

                        dato2=sumProm ;
                        dato3=dato2/noMat;
                        dato4=dato3;
                        dato5=dato4/10;
                        dato6=""+dato5;
                        dato7=dato6.substring(0,3);
                        promSec=dm.toFloat(dato7);*/
                        
                        dato7 = (QPromGral.get("promgral")+"0").substring(0,3);
                        promSec=dm.toFloat(dato7);

                        PromPrim = dm.toFloat(QAlumProm.get("promdp"));
                        //if ( PromPrim > 0 )
                        //{
                            dato2=(promSec*10+PromPrim*10);
                            dato3=dato2/2 ;
                            dato4=dato3;
                            dato5=dato4/10;
                            dato6=""+dato5;
                            dato7=dato6.substring(0,3);
                            promEB=dm.toFloat(dato7);
                        //}else{
                        //    promEB= promSec;
                        //}


                        if ( (dm.toInt(QAlumProm.get("debe1ro")) + dm.toInt(QAlumProm.get("debe2do")) + dm.toInt(QAlumProm.get("matrepact")) ) >0 )
                            qryMatCicAntU = "UPDATE alumnoGrado SET PromedioGral=0.0, promedioeb=0.0, estatusgrado='NP', Promovido='NP', ";
                        else
                            qryMatCicAntU = "UPDATE alumnoGrado SET PromedioGral="+promSec+", promedioeb="+promEB+", estatusgrado='C', promovido='P', "; //OJO: Modificado para uso en SICEEO
                        
                    }else
                        qryMatCicAntU = "UPDATE alumnoGrado SET PromedioGral="+QPromGral.get("promgral")+", promedioeb=0.0, estatusgrado='C', promovido='P', "; //OJO: Modificado para uso en SICEEO
                }else
                    qryMatCicAntU = "UPDATE alumnoGrado SET PromedioGral=0.0, promedioeb=0.0, estatusgrado='NP', Promovido='NP', "; //OJO: Modificado para uso en SICEEO

                qryMatCicAntU += "usuario='"+txtUsuario.toUpperCase()+"', fecha = date(current), hora=extend(current, hour to minute) WHERE cicescini="+tblCalif3ro_cicescini+" AND idalu="+idalu;
                stm.execute(qryMatCicAntU);
                QCalif3ro = calif3ro (idalu);
            }else{
                stm.execute("UPDATE alumnoGrado "
                        + "SET PromedioGral=0.0, estatusgrado='NP', Promovido='NP', usuario='"+txtUsuario.toUpperCase()+"', fecha = date(current) , hora = extend(current, hour to minute) "
                        + "WHERE cicescini="+tblCalif3ro_cicescini+" AND idalu="+idalu);
                QCalif3ro = calif3ro (idalu);
                errorCalcPromGral[0]=true;
            }
        }
        // F_PromGsec.ShowModal;
        //----------------------------------------------------------
        return QCalif3ro;
    }
    
    private Map getAlumProm2012 (String idalu) throws SQLException
    {
        rs = stm.executeQuery (
                "SELECT matrepact, promediogral, "
                    + "(SELECT promedioGral FROM alumnogrado gp WHERE gp.idalu=g.idalu AND gp.estatusgrado='C' AND gp.grado=6) AS PromdP, "
                    + "(SELECT CAST(matRepAct AS int) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 1 AND cveplan=2 AND estatusgrado='RE' ) AS Debe1ro, "
                    + "(SELECT CAST(matRepAct AS int) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 2 AND cveplan=2 AND estatusgrado='RE' ) AS Debe2do "
                + "FROM alumnogrado g "
                + "WHERE g.estatusGrado <>'BD' AND g.cicescini=2012 AND g.idalu="+idalu+" ");
        return qryToMap (rs, null, true, 1);
    }
    
//************************************************************************************************************
//**************************** MÉTODO USADOS EN LA CLASE SubqueryPromPorMat **********************************
//************************************************************************************************************    
    public ArrayList<Map> getPaqueteDeMateriasDeCiclo (String cicescini, String modalidad, String cveplan, String grado) throws SQLException
    {
        rs = stm.executeQuery ("SELECT em.cveprograma, m.cvetipmat, m.cvemat, TRIM(m.desmat) AS desmat, em.ordenimpres,'' AS avances " +
                                "FROM esquemamaterias em, materias m " +
                                "WHERE em.cvetipmat=m.cvetipmat AND em.cvemat=m.cvemat AND " +
                                "  "+cicescini+">=em.cicescini AND "+cicescini+"<=em.cicescfin AND em.cveplan="+cveplan+" AND em.grado="+grado+" " +
                                "  AND em.cveprograma = (SELECT cveprograma "
                                                      + "FROM planmodalidad "
                                                      + "WHERE "+cicescini+">=cicescini AND "+cicescini+"<=cicescfin "
                                                           + "AND modalidad='"+modalidad+"' AND plan=em.cveplan AND grado=em.grado AND cveentidad=20) " +
                                "  AND m.matdefault='t' " +
                                "ORDER BY em.ordenimpres");
        return qryToArrlmap(rs, null, true, 2);
        
    }        

//************************************************************************************************************
//****************************** MÉTODOS USADOS EN EL MÓDULO DE Configuraciones ******************************
//************************************************************************************************************
     public String getEstatusMantenimiento () throws SQLException
     {
         return getData("SELECT descicesc FROM CicloEscolar WHERE estatus='A'").trim();
     }
     
     public void setEstatusMantenimiento (boolean mantenimiento) throws SQLException
     {
         if (mantenimiento)
             stm.execute("UPDATE CicloEscolar SET descicesc='MANTENIMIENTOWEB' WHERE estatus='A' AND descicesc <> 'MANTENIMIENTO'");
         else
             stm.execute("UPDATE CicloEscolar SET descicesc='MANTENIMIENTOx' WHERE estatus='A' AND descicesc <> 'MANTENIMIENTO'");
     }
     
//************************************************************************************************************
//********************************* MÉTODOS USADOS EN EL MÓDULO DE Permisos **********************************
//************************************************************************************************************
    public ArrayList<Map>  getPermisosTipoUsuario () throws SQLException
    {
        ArrayList<String> tipoUsuarios;
        String sqryTiposUsuario="";
        int i=0;
        
        tipoUsuarios=getTiposUsuario ();
        for (String tipoUsuario : tipoUsuarios)
            sqryTiposUsuario += ((i++==0)?" ":", ")+"(SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='"+tipoUsuario+"')) AS "+tipoUsuario.replace(" ", "_")+" ";
        
        rs = stm.executeQuery("SELECT o.idobjeto, o.componente, o.formulario, "
                               + sqryTiposUsuario
                           + "FROM siceeo_objetospermiso o "
                           + "WHERE idobjeto<>0 "
                           + "ORDER BY o.formulario, o.orden");
       return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<String> getTiposUsuario () throws SQLException
    {
        ArrayList<String> tipoUsuarios = new ArrayList<String>();
        rs = stm.executeQuery("SELECT tipousuario FROM siceeo_permisos WHERE tipousuario<>'ADMIN' ORDER BY tipousuario");
        while (rs.next())
            tipoUsuarios.add(rs.getString("tipousuario"));
        
        return tipoUsuarios;
    }
    
    public void guardarTipoUsuariosConPermiso(ArrayList<Map> tblTipoUsuarios) throws SQLException
    {
        int numFilas = tblTipoUsuarios.size();
        String permiso, permiso_oldValue;
        ArrayList<String> tiposUsuario;
        
        tiposUsuario = this.getTiposUsuario ();
        
        for (int f=0; f<numFilas; f++)
            for (String tipoUsuario : tiposUsuario) {
                permiso = "" + tblTipoUsuarios.get(f).get(tipoUsuario);
                permiso_oldValue = "" + tblTipoUsuarios.get(f).get(tipoUsuario + "_oldValue");
                if (permiso.charAt(0) != permiso_oldValue.charAt(0)) {
                    stm.execute("UPDATE siceeo_configpermiso "
                            + "SET permiso='"+permiso.charAt(0)+"' "
                            + "WHERE idobjeto="+tblTipoUsuarios.get(f).get("idobjeto")+" "
                            + "AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='" + tipoUsuario + "')");
                }
            }
    }
    
    public ArrayList<Map> getUsuarioConPermisosEspecificos (String filtroUsuario) throws SQLException
    {
        String filtro="";
        
        if (!filtroUsuario.equals(""))
            filtro = "AND ae.loginuser like '"+filtroUsuario.trim().toUpperCase()+"%' ";
        
        rs = stm.executeQuery("SELECT ae.idaccesoespecifico, ae.loginuser, op.componente, op.formulario, ae.permiso, ae.permiso AS permisoOriginal "
                        + "FROM siceeo_accesoEspecifico ae, siceeo_objetospermiso op "
                        + "WHERE ae.idObjeto=op.idobjeto " + filtro
                        + "ORDER BY formulario, loginUser");
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList geUsuariosFiltrados (String filtroUsuario) throws SQLException
    {
        ArrayList<String> QUsrEncontrados = new ArrayList<String>();
        
        rs = stm.executeQuery("SELECT loginuser FROM usuarios WHERE estatus='A' AND loginuser LIKE '"+filtroUsuario.trim().toUpperCase()+"%' ORDER BY loginUser");
        while (rs.next())
            QUsrEncontrados.add(rs.getString("loginuser"));
        
        return QUsrEncontrados;
    }
    
    public ArrayList<Map> getNombreComponentesParaPermiso () throws SQLException
    {
        rs = stm.executeQuery("SELECT idobjeto, componente||' - '||formulario AS objeto FROM siceeo_objetospermiso WHERE idobjeto>0 ORDER BY formulario, componente");
        return qryToArrlmap(rs, null, true, 2);
    }

    public void setPermisosATipoUsuario (boolean mantenimiento) throws SQLException
    {
        if (mantenimiento)
            stm.execute("UPDATE CicloEscolar SET descicesc='MANTENIMIENTOWEB' WHERE estatus='A' AND descicesc <> 'MANTENIMIENTO'");
        else
            stm.execute("UPDATE CicloEscolar SET descicesc='MANTENIMIENTOx' WHERE estatus='A' AND descicesc <> 'MANTENIMIENTO'");
    }

    public String getPermisosUsuarioEspecifico () throws SQLException
    {
        return getData("SELECT descicesc FROM CicloEscolar WHERE estatus='A'").trim();
    }

    public void setPermisosUsuarioEspecifico (boolean mantenimiento) throws SQLException
    {
        if (mantenimiento)
            stm.execute("UPDATE CicloEscolar SET descicesc='MANTENIMIENTOWEB' WHERE estatus='A' AND descicesc <> 'MANTENIMIENTO'");
        else
            stm.execute("UPDATE CicloEscolar SET descicesc='MANTENIMIENTOx' WHERE estatus='A' AND descicesc <> 'MANTENIMIENTO'");
    }
    
    public void guardarExcepcionDePermiso (ArrayList<Map> tblUsuariosEsp) throws SQLException
    {
        int numFilas = tblUsuariosEsp.size();
        String permiso, permiso_oldValue;
        for (int i=0; i<numFilas; i++){
            permiso = "" + tblUsuariosEsp.get(i).get("permiso");
            permiso_oldValue = "" + tblUsuariosEsp.get(i).get("permiso_oldValue");
            if (permiso.charAt(0) != permiso_oldValue.charAt(0))
                stm.execute("UPDATE siceeo_accesoEspecifico SET permiso='"+permiso.charAt(0)+"' WHERE idaccesoespecifico="+tblUsuariosEsp.get(i).get("idaccesoespecifico"));
        }
    }
    
    public void eliminarExcepcionDePermiso (String idaccesoespecifico) throws SQLException
    {
        stm.execute("DELETE FROM siceeo_accesoEspecifico WHERE idaccesoespecifico="+idaccesoespecifico);
    }
    
    public String insertExcepcionDePermiso (String loginuser, String idobjeto, String permiso) throws SQLException, SICEEO_Excepcion
    {
        String idaccesoespecifico=null;
        
        idaccesoespecifico = getData("SELECT MAX(idaccesoespecifico) FROM siceeo_accesoEspecifico");
        if (idaccesoespecifico == null || idaccesoespecifico.equals(""))
            idaccesoespecifico = "1";
        else
            idaccesoespecifico = "" + (Integer.parseInt(idaccesoespecifico)+1);
        
        try {
            stm.execute("INSERT INTO siceeo_accesoEspecifico (idaccesoespecifico, loginuser, idobjeto, permiso) VALUES ("+idaccesoespecifico+",'"+loginuser+"',"+idobjeto+",'"+permiso.charAt(0)+"')");
        }catch (SQLException ex)
        {
            if (ex==null || ex.getMessage()==null)
                throw new SQLException(ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("IDOBJETOPERMISOSAE_UK"))
                throw new SICEEO_Excepcion("PERMISO_REPETIDO");
            else
                throw new SQLException(ex);
        }
        
        return idaccesoespecifico;
    }
    
    public void updateExcepcionDePermiso (String loginuser, String idobjeto, String permiso) throws SQLException
    {
        rs = stm.executeQuery("SELECT idaccesoespecifico FROM siceeo_accesoEspecifico WHERE loginuser='"+loginuser+"' AND idobjeto="+idobjeto);
        if (rs.next()){
            stm.execute("UPDATE siceeo_accesoEspecifico SET permiso='"+permiso.charAt(0)+"' WHERE idaccesoespecifico="+rs.getString("idaccesoespecifico"));
        }
    }
     
//************************************************************************************************************
//********************************* MÉTODOS USADOS EN EL MÓDULO Preinscripcion *******************************
//************************************************************************************************************
    public ArrayList<Map> getPreinscripcion (String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String cicesciniPreinsc) throws SQLException
    {
        Map fila;
        ArrayList<Map> QPreinscripcion = new ArrayList<Map>();
        rs = stm.executeQuery("SELECT a.idalu, TRIM(a.curp) AS curp, "
                                + "(TRIM(NVL(a.apepat,'')) || ' / ' || TRIM(NVL(a.apemat,'')) || ' * ' || TRIM(NVL(a.nombre,''))) AS nom_tot, a.apepat, "
                                + "a.apemat, a.nombre, p.krtacompromiso, "
                                + "CASE WHEN p.hablaespaniol='t' THEN 'SÍ' ELSE 'NO' END AS hablaespaniol, "
                                + "CASE WHEN p.cveotralengua='ESP' THEN '' ELSE p.cveotralengua END AS cveotralengua,(case when a.afromexicana is null then '' else a.afromexicana end) as afromexicana "
                           + "FROM alumno a, preinscripcion p "
                           + "WHERE a.idalu=p.idalu AND p.idcct="+tblPrincipal_idcct+" AND p.cveplan="+tblPrincipal_cveplan+" "
                                + "AND p.grado="+tblPrincipal_grado+" AND p.cicescini="+cicesciniPreinsc+" "
                           + "ORDER BY a.apepat, a.apemat, a.nombre");
        
        while (rs.next())
        {
            fila = new HashMap();
            fila.put("idalu", rs.getString("idalu"));
            fila.put("curp", rs.getString("curp"));
            fila.put("nom_tot", rs.getString("nom_tot"));
            fila.put("apepat", rs.getString("apepat"));
            fila.put("apemat", rs.getString("apemat"));
            fila.put("nombre", rs.getString("nombre"));
            
            fila.put("krtacompromiso", rs.getString("krtacompromiso"));
            //fila.put("cvedefsufx", rs.getString("cvedefsufx"));
            //fila.put("probem", rs.getString("probem"));
            //fila.put("extranjero", rs.getString("extranjero"));
            fila.put("hablaespaniol", rs.getString("hablaespaniol"));
            fila.put("cveotralengua", rs.getString("cveotralengua"));
            fila.put("etnia", rs.getString("afromexicana"));
            QPreinscripcion.add(fila);
        }
       return QPreinscripcion;
    }
    
    public ArrayList<Map> getPreinscripcionParaExcel (String tblPrincipal_idcct, String tblPrincipal_cveplan, String tblPrincipal_grado, String cicesciniPreinsc) throws SQLException
    {
        Map fila;
        ArrayList<Map> QPreinscripcion = new ArrayList<Map>();
        rs = stm.executeQuery("SELECT a.idalu, TRIM(a.curp) AS curp, "
                                + "(TRIM(NVL(a.apepat,'')) || '/' || TRIM(NVL(a.apemat,'')) || '*' || TRIM(NVL(a.nombre,''))) AS nom_tot, a.apepat, "
                                + "a.apemat, a.nombre, p.krtacompromiso, "
                                /*+ "TRIM(NVL((SELECT desdefsuf FROM deficsuficalu WHERE cicescini <= "+cicesciniPreinsc+" AND cicescfin >="+cicesciniPreinsc+" AND cveplan = "+tblPrincipal_cveplan+" AND cvedefsuf=p.cvedefsufx),'')) AS desdefsuf, "
                                + "CASE p.probem "
                                    + "WHEN 'T' THEN 'NAC PROV DE EUA CON DOC DE TRANSF' "
                                    + "WHEN 'N' THEN 'NAC PROV DE EUA SIN DOC DE TRNASF' "
                                    + "WHEN 'E' THEN 'NAC PROV DE OTROS PAISES' "
                                    + "WHEN 'D' THEN 'ALUMNOS QUE SE DIRIGEN A LOS EUA CON D' "
                                    + "ELSE '' "
                                + "END AS probem, "
                                + "CASE p.extranjero "
                                    + "WHEN '1' THEN 'AFRICA' "
                                    + "WHEN '2' THEN 'ASIA' "
                                    + "WHEN '3' THEN 'CANADA' "
                                    + "WHEN '4' THEN 'CENTROAMERICA Y EL CARIBE' "
                                    + "WHEN '5' THEN 'EUA' "
                                    + "WHEN '6' THEN 'EUROPA' "
                                    + "WHEN '7' THEN 'OCEANIA' "
                                    + "WHEN '8' THEN 'SUDAMERICA' "
                                    + "ELSE '' "
                                + "END AS extranjero, "*/
                                + "CASE WHEN p.hablaespaniol='t' THEN 'SÍ' ELSE 'NO' END AS hablaespaniol, "
                                + "TRIM(NVL((SELECT CASE WHEN p.cveotralengua='ESP' THEN '' ELSE deslengua END FROM lenguas WHERE cvelengua=p.cveotralengua),'')) AS otralengua,NVL(a.afromexicana,'') as afromexicana "
                           + "FROM alumno a, preinscripcion p "
                           + "WHERE a.idalu=p.idalu "
                                + "AND p.idcct="+tblPrincipal_idcct+" "
                                + "AND p.cveplan="+tblPrincipal_cveplan+" "
                                + "AND p.grado="+tblPrincipal_grado+" AND p.cicescini="+cicesciniPreinsc+" "
                           + "ORDER BY a.apepat, a.apemat, a.nombre");
        
        while (rs.next())
        {
            fila = new HashMap();
            fila.put("idalu", rs.getString("idalu"));
            fila.put("curp", rs.getString("curp"));
            fila.put("nom_tot", rs.getString("nom_tot"));
            fila.put("apepat", rs.getString("apepat"));
            fila.put("apemat", rs.getString("apemat"));
            fila.put("nombre", rs.getString("nombre"));
            
            fila.put("krtacompromiso", rs.getString("krtacompromiso"));
            //fila.put("cvedefsufx", rs.getString("desdefsuf"));
            //fila.put("probem", rs.getString("probem"));
            //fila.put("extranjero", rs.getString("extranjero"));
            fila.put("hablaespaniol", rs.getString("hablaespaniol"));
            fila.put("cveotralengua", rs.getString("otralengua"));
            fila.put("etnia", rs.getString("afromexicana"));
            QPreinscripcion.add(fila);
        }
       return QPreinscripcion;
    }
   
   public Map getLenguas () throws SQLException
   {
       Map QcveLenguas = new LinkedHashMap();
       rs = stm.executeQuery("SELECT cvelengua, TRIM(deslengua) AS deslengua FROM lenguas WHERE cvelengua <>'ESP' ORDER BY deslengua");
       while (rs.next())
           QcveLenguas.put(rs.getString("cvelengua"), rs.getString("deslengua"));
       return QcveLenguas;
   }
   
   public Map getParentesco () throws SQLException
   {
       Map QcveParent = new LinkedHashMap();
       rs = stm.executeQuery("SELECT cveparent,desparent FROM parentesco WHERE estatus='A' ORDER BY cveparent");
       while (rs.next())
           QcveParent.put(rs.getString("cveparent"), rs.getString("desparent"));
       return QcveParent;
   }
   
   public void insertarAlumnoPreinscrito (String alumEstatus, String idalu, String cveentidad, String idcct, String newIdTutor, 
           String curpRaiz, String nombre, String ape1, String ape2, String fechaNacimiento, String sexo, /*String cartaCompromiso,*/ String entNac, 
           int cicescini, int grado, String cveplan, boolean hablaEspaniol, String cveLengua, String etnia, String usuario) throws SQLException, SICEEO_Excepcion, Exception
    {
        try {
        String afromexicana = etnia.isEmpty() ? "null ": "'"+etnia+"'";
        if (alumEstatus.equals("I")){            
            stm.execute("INSERT INTO Alumno VALUES ("+idalu+", "+cveentidad+", null, 1,"+idcct+", 'I','MEX','','01','ESP','A', "+newIdTutor+", "
                    + "'"+curpRaiz+"', \""+nombre.trim()+"\", \""+ape1.trim()+"\", \""+ape2.trim()+"\","
                    +" TO_DATE('"+fechaNacimiento+"','%Y/%m/%d'),  "
                    + "'"+sexo+"','NO','---',"+entNac+","+ fechaNacimiento.substring(0, 4)+", null, null, null, "
                    + "0,'FA', null,date(current), '"+curpRaiz.substring(0,16)+"', '' , '', 'I', null, null, null, 0, '"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute),"+afromexicana+",'000')");
        }
        else if(alumEstatus.equals("O")){            
            stm.execute("UPDATE Alumno SET "
                    + "usuario = '"+usuario.trim().toUpperCase()+"', "
                    + "fecha = date(current), "
                    + "hora = extend(current, hour to minute),"
                    + "afromexicana = "+afromexicana+","
                    + "cvelengua = '"+cveLengua+"' "        
                    + " WHERE idalu = "+idalu);
        }

        stm.execute("INSERT INTO Preinscripcion VALUES ("+idalu+", "+cicescini+", "+cveplan+", '"+idcct+"', "+grado+", "
                + "'NO', "+(hablaEspaniol?"'t'":"'f'")+", '"+cveLengua+"', "
                + "'"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))");
        }catch(SQLException ex){
            if (ex==null || ex.getMessage()==null)
                throw new SQLException (ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("UK_IDALUCICIDCCT"))
                throw new SICEEO_Excepcion("PREINSCRITO_MISMO_CCT");
            else
                throw new SQLException (ex);
        }
    }
   
   public void eliminarAlumnoPreinscrito (String idalu, String tblPrincipal_idcct, String cicesciniPreinsc, String tblPrincipal_grado) throws SQLException
   {
       stm.execute("DELETE FROM Preinscripcion WHERE idalu="+idalu+" AND idcct="+tblPrincipal_idcct+" AND cicescini="+cicesciniPreinsc+" AND grado="+tblPrincipal_grado);
       rs = stm.executeQuery("SELECT idalu FROM Alumnogrado WHERE idalu="+idalu);
       if (!rs.next()){
           rs = stm.executeQuery("SELECT idalu FROM Preinscripcion WHERE idalu="+idalu+" AND cicescini="+cicesciniPreinsc+" AND idcct<>"+tblPrincipal_idcct); //Revisamos primero si otra escuela no lo tiene preinscrito
           if (!rs.next())
               stm.execute("DELETE FROM Alumno WHERE idalu="+idalu);
       }
   }
   
      
//************************************************************************************************************
//************************************ MÉTODOS USADOS EN EL MÓDULO Ajustar ***********************************
//************************************************************************************************************
   public ArrayList<Map> BuskAlum1 (String condicion) throws SQLException
   {
       rs = stm.executeQuery("SELECT a.idalu,(TRIM(NVL(a.apepat,'')) || '/' || TRIM(NVL(a.apemat,'')) || \"*\" || TRIM(NVL(a.nombre,''))) AS nom_tot, "
                                + "a.cveentidad, a.cvemunicipio, a.cvedocprob, a.idcct, a.estatusalu, a.cvenacion, a.cveocup, a.cvelengua, a.cvedoctrans, a.idtutor, a.curp, "
                                + "a.nombre, a.apepat, a.apemat, a.fecnac, a.sexo, a.tiposangre, a.cveentfednac, a.anoregistro, a.libro, a.noacta, a.foliodp, a.errordp, a.estatusdp, "
                                + "a.foliodoctrans, a.fecingsis, a.curp16, a.cve_estat_curp, a.crip, a.reg_nac_ext, a.folio_cta_nat, a.estatus_cambio "
                            + "FROM alumno a "
                            + "WHERE "+condicion);
       return qryToArrlmap(rs, null, true, 2);
   }
   
   public ArrayList<Map> susEstud (String idalu) throws SQLException
   {
       rs = stm.executeQuery("SELECT a.idalu, e.idcct, e.cct, a.cveturno, (select desturno from turno where turno.cveturno = a.cveturno)as desturno, e.estatusesc, "
                            + "CASE WHEN e.estatusesc='AC' THEN 'ACTIVA' WHEN e.estatusesc='CL' THEN 'CLAUSURADA' ELSE 'NO DEFINIDO' END AS DESESTATUSESC, "
                            + "e.nombre, e.region AS Reg_geo, trim(e.region) ||' (' || trim(e.cveunidad) || ')' AS regT, e.cvemunicipio, e.desmunicipio, "
                            + "e.cvelocalidad, e.deslocalidad, e.domicilio, e.telefono, e.cveplan, "
                            + "CASE "
                                + "WHEN e.cveplan= 3 THEN 'PREESCOLAR' "
                                + "WHEN e.cveplan= 1 THEN 'PRIMARIA' "
                                + "WHEN e.cveplan= 2 THEN 'SECUNDARIA' "
                                + "ELSE 'NO DEFINIDO' "
                            + "END AS NIVEL, "
                            + "e.cvezona, e.cveunidad, a.cicescini, substr(a.cicescini,3,2) ||'-'|| substr(a.cicescfin,3,2) AS inifin, grado, grupo, "
                            + "substr(grupo,2,1) AS guionbajo, estatusgrado, "
                            + "CASE "
                                + "WHEN estatusgrado= 'RE' AND PROMEDIO>=6.0 then 'APROBADO' "
                                + "WHEN estatusgrado= 'P'  AND PROMEDIO>=6.0 then 'APROBADO' "
                                + "WHEN (SELECT COUNT (acreditado) FROM alumnodelextrj j WHERE j.idalu=a.idalu AND j.cveplan=a.cveplan AND j.acreditado='RV' AND j.cicescini=a.cicescini)=1 "
                                    + "THEN 'APROBADO POR REVALIDACION' "
                                + "WHEN estatusgrado= 'RC' THEN 'NO APROBADO' "
                                + "WHEN estatusgrado= 'C' THEN 'CERTIFICADO' "
                                + "WHEN estatusgrado= 'BD' THEN 'BAJA DEFINITIVA' "
                                + "WHEN estatusgrado= 'I'  THEN 'INSCRITO' "
                                + "WHEN estatusgrado= 'NP' THEN 'NO APROBADO'"
                                + "ELSE 'NO DEFINIDO' "
                            + "END AS DESESTATUSgrado, "
                            + "Promovido, promedioant, Promedio, PromedioGral, "
                            + "(SELECT acreditado FROM alumnodelextrj j WHERE j.idalu=a.idalu AND j.cveplan=a.cveplan AND j.cicescini=a.cicescini) AS TipoAcred, "
                            + "PromedioEb, a.cveprograma, matRepAnt, matRepAct "
                        + "FROM alumnoGrado a, Escuela e "
                        + "WHERE a.idcct = e.idcct AND idalu = " + idalu
                        + "ORDER BY a.cicescini");
       return qryToArrlmap(rs, null, true, 2);
   }
   
   public ArrayList<Map> susMat (String cicescini, String idalu) throws SQLException
   {
       rs = stm.executeQuery("SELECT  m.idalu, m.cvemat,  e.desmat,  m.promedio, m.cicescini, m.usuario, TO_CHAR(m.fecha,'%d/%m/%Y') AS fecha, m.hora, m.cveprograma, m.cvetipmat, "
                            + "(CASE "
                                + "WHEN cveplan =2 THEN ("
                                    + "SELECT COUNT(*) "
                                    + "FROM exm_ext_ordi x "
                                    + "WHERE x.cvetipmat= m.cvetipmat AND x.cvemat= m.cvemat AND x.grado= m.grado AND x.idalu = m.idalu "
                                        + "AND x.cicescini = m.cicescini"
                                + ") "
                                + "ELSE 0 "
                            + "END) AS eer "
                            + "FROM alumnomaterias m,  materias e "
                            + "WHERE  m.cvemat = e.cvemat AND m.cvetipmat = e.cvetipmat AND cicescini="+cicescini+" AND idalu="+idalu);
       return qryToArrlmap(rs, null, true, 2);
   }
   
   public ArrayList<Map> suFolio (String idalu) throws SQLException
   {
        rs = stm.executeQuery("SELECT *, SUBSTR(grupo,2,1) AS guionbajo, "
                                + "(TRIM(NVL(apepat,'')) || '/' || TRIM(NVL(apemat,'')) || '*' || TRIM(NVL(nombre,''))) AS nom_tot, " 
                                + "SUBSTR(foliolet,2,1) AS folioletra, SUBSTR(cicescinilib,3,2) ||'-'|| SUBSTR(cicescinilib+1,3,2) AS inifin, "
                                + "(CASE WHEN estatus='IA' THEN 'I' ELSE estatus END) as estatus,"
                                + "(SELECT FOLIOrodac "
                                    + "FROM rodac "
                                    + "WHERE idalu = folios_impre.idalu AND cveplan= folios_impre.cveplan AND folios_impre.cicescinilib = rodac.cicescinilib "
                                        + "AND tipodoc='CER'"
                                + ")AS folioRodac, TO_CHAR(fecha,'%d/%m/%Y') AS fechaf, "
                                + "CASE (SELECT idalu FROM firma_elec WHERE idalu=folios_impre.idalu AND cicescini=folios_impre.cicescini) "
                                    + "WHEN NULL "
                                    + "THEN 'NO' ELSE 'SI' END AS tieneFirma, " /*OJO: Línea agregada para uso de SICEEO*/
                                + "foliolet||'20' AS follet, idcct "
                            + "FROM folios_impre "
                            + "WHERE idalu = "+idalu+" "  /* AND estatus='AC' se quito 18-06-2024 */
                            + "ORDER BY cicescinilib");  //OJO:Línea agregada para uso de SICEEO 
        return qryToArrlmap(rs, null, true, 2);
   }
   
   public ArrayList<Map> suFolioC (String idalu) throws SQLException
   {
       rs = stm.executeQuery("SELECT *, (TRIM(NVL(apepat,'')) || '/' || TRIM(NVL(apemat,'')) || \"*\" || TRIM(NVL(nombre,''))) AS nom_tot, "
                                + "SUBSTR(foliolet,2,1) AS folioletra "
                            + "FROM folios_cance "
                            + "WHERE idalu = "+idalu);
       return qryToArrlmap(rs, null, true, 2);
   }
   
   public ArrayList<Map> Comentario (String idalu) throws SQLException
   {
       rs = stm.executeQuery("SELECT comentario, usuario, TO_CHAR(fecha,'%d/%m/%Y') AS fecha "
                            + "FROM AlumnoComen "
                            + "WHERE idalu = "+idalu+" "
                            + "ORDER BY fecha");
       return qryToArrlmap(rs, null, true, 2);
   }
   
//************************************************************************************************************
//********************************** MÉTODOS USADOS EN EL MÓDULO ModifiCurp **********************************
//************************************************************************************************************
   
    public ArrayList<Map> yEsDLaReg (String condicion) throws SQLException
    {
        rs = stm.executeQuery("SELECT a.idalu, a.curp, e.cveunidad, e.cct, g.idcct "
                + "FROM alumno a, escuela e, alumnogrado g "
                + "WHERE a.idalu = g.idalu AND g.idcct = e.idcct AND ("+condicion+")");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<Map> curpRenapo (String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT col003 AS curp, col004 AS apepat, col005 AS apemat, col006 AS nombre, col008 AS fecnac, col026 AS Estatus, "
                + "col017 AS crip, "
                + "(CASE "
                    + "WHEN COL026 ='AN' THEN 'ALTA NORMAL' "
                    + "WHEN COL026 ='AH' THEN 'ALTA CON HOMONIMIA' "
                    + "WHEN COL026 ='RCN' THEN 'REGISTRO DE CAMBIO NO AFECTANDO A LA CURP' "
                    + "WHEN COL026 ='RCC' THEN 'REGISTRO DE CAMBIO AFECTANDO A LA CURP' "
                    + "END "
                + ") AS DESeSTATUS, "
                + "(CASE WHEN col027='000' THEN 'Si'  ELSE 'No' END) AS regcivcert, "
                + "(trim(nvl(col004,'')) || '/' || trim(nvl(col005,'')) || \"*\" || trim(nvl(col006,''))) AS nom_tot "
                + "FROM renapoENCONTRADOS " //curpRenapo
                + "WHERE col002="+idalu);
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public void modificarCurp (String gdoTerminal, String modifCurp, String curp_ant, String nombre_ant, String apepat_ant, String apemat_ant, 
           String regCivCert_ant, String valida_ant, String txtPermiso, String idalu, String txtCurp16, String txtCurp17y18, String txtCrip, String txtNombre, 
           String txtApe1, String txtApe2, String txtSexo, String txtFechaNacimiento, String cbxKrta, String entNac, String probem, String extj, String usuario, 
           Map dr) throws SQLException
    {
        ArrayList<Map> QCurpRenapo, QCurpVit;
        String query="";
        
        QCurpRenapo = curpRenapo (idalu);
        // si es grado terminal y  es una curp valida se kitan los 2 ultimos digitos
        if ( gdoTerminal.equals("SI") && QCurpRenapo.size()==1 && !usuario.trim().toUpperCase().equals("IVALLE") ) // AND
                //  (UPPERCASE(f_password.E_Usuario.Text)<>'FLALUISSA') AND
                //  (UPPERCASE(f_password.E_Usuario.Text)<>'MARIOCIMM') AND
                //  (UPPERCASE(f_password.E_Usuario.Text)<>'LPOBLETE')  then
        {
            if ( (""+QCurpRenapo.get(0).get("curp")).length()==18 && txtCurp17y18.toUpperCase().equals((""+QCurpRenapo.get(0).get("curp")).substring(16,18)) )
                query = "UPDATE alumno SET curp = '"+txtCurp16.toUpperCase()+txtCurp17y18.toUpperCase()+"', ";
            else
            {
                query = "UPDATE alumno SET curp = '"+txtCurp16.toUpperCase()+"', ";
                dr.put("txtCurp17y18_Text", "");
            }
        }
        else
            query = "UPDATE alumno SET curp = '"+txtCurp16.toUpperCase()+txtCurp17y18.toUpperCase()+"', ";

        query += "curp16 = '"+txtCurp16.toUpperCase()+"', nombre = \""+txtNombre.toUpperCase()+"\", apepat=\""+txtApe1.toUpperCase()+"\", "
                + "apemat=\""+txtApe2.toUpperCase()+"\", probem = '"+probem+"', " //                  Dm.Q_Alum.SQL.Add('cvedefsuf = '+'"'+dm2.q_CatDiscapcvedefsuf.AsString+'"'+',');
                + "extranjero = '"+extj+"', fecnac = TO_DATE('"+txtFechaNacimiento+"','%d/%m/%Y'), sexo='"+txtSexo+"', "
                + "krtacompromiso='"+cbxKrta.substring(0,2).toUpperCase()+"', cveentfednac="+entNac+", ";
        
        query += "crip = "+ ( ((""+txtCrip).trim().toUpperCase().equals("NULL") || (""+txtCrip).trim().equals("")) ? "null, " : "'"+txtCrip.toUpperCase()+"', " ); 
        
        query += "anoregistro="+txtFechaNacimiento.substring(6,10)+", usuario='"+usuario.trim().toUpperCase()+"', fecha=date(current), "
                + "hora = extend(current, hour to minute) WHERE idalu="+idalu;


        //--------------------------------------------------------------------------------------------------------------
        if ( modifCurp.equals("SI") || txtPermiso.equals("CAMPEON") )
        {
            stm.execute(query);
            //Dm.Q_Newgrupo.ExecSQL ;
        }
        else {
        //---------------------------------------------------------------------------------------------------------------
            //guardamos mas campos fuera de los 6 basicos de la curp
            stm.execute("UPDATE alumno SET Probem = '"+probem+"', Extranjero = '"+extj+"', krtacompromiso = '"+cbxKrta.substring(0,2).toUpperCase()+"', "
                        + "usuario = '"+usuario.trim().toUpperCase()+"', fecha = date(current) , hora = extend(current, hour to minute) "
                    + "WHERE idalu = "+idalu);
            //Este Qry sirve para empatar los demas campos
            stm.execute("UPDATE alumno SET curp16 = '"+txtCurp16.toUpperCase()+"', fecnac = TO_DATE('"+txtFechaNacimiento+"','%d/%m/%Y'), sexo='"+txtSexo+"', "
                        + "cveentfednac="+entNac+", usuario = '"+usuario.trim().toUpperCase()+"', fecha = date(current), "
                        + "hora = extend(current, hour to minute) "
                    + "WHERE idalu = "+idalu);
        }
        //-------------------------------------------------------------------------------------------------------
        if ( !(""+txtCrip).trim().toUpperCase().equals("NULL") && !txtCrip.equals("") )            //actualizamos en Folios_impre
            stm.execute("UPDATE Folios_impre SET crip = '"+txtCrip.toUpperCase()+"' WHERE idalu = "+idalu + " AND estatus='AC'");

        if ( !curp_ant.equals(txtCurp16+txtCurp17y18) || !nombre_ant.equals(txtNombre) || !apepat_ant.equals(txtApe1) || !apemat_ant.equals(txtApe2) )
        {
            QCurpVit = curpVit(idalu);
            
            if ( QCurpVit.size()>=1 )
            {
                query = "UPDATE CURPvit SET curp_ant=\""+curp_ant+"\", apepat_ant=\""+apepat_ant+"\", apemat_ant=\""+apemat_ant+"\", "
                        + "nombre_ant=\""+nombre_ant+"\", curp_des='"+txtCurp16+txtCurp17y18+"', apepat_des=\""+txtApe1+"\", "
                        + "apemat_des=\""+txtApe2+"\", nombre_des=\""+txtNombre+"\", usuario='"+usuario.trim().toUpperCase()+"', "
                        + "fecha=date(current), hora=extend(current, hour to minute), ";
                
                if ( QCurpRenapo.size()==1 && gdoTerminal.equals("SI") )
                    query += "err_renapo='SI' ";
                else
                    query += "err_renapo= NULL ";
                
                query += "WHERE idalu="+idalu;
            }
            else
            {
                query = "INSERT INTO CURPvit values("+idalu+", '"+curp_ant+"', \""+apepat_ant+"\", \""+apemat_ant+"\", \""+nombre_ant+"\", "
                        + "'"+regCivCert_ant+"', '"+valida_ant+"', '"+txtCurp16+txtCurp17y18+"', \""+txtApe1+"\", \""+txtApe2+"\", "
                        + "\""+txtNombre+"\", '"+usuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute), ";
                
                if ( QCurpRenapo.size()==1 && gdoTerminal.equals("SI") )
                    query += "'SI')";                                           //err_curp
                else
                    query += "NULL)";                                           //err_curp
            }

            if ( modifCurp.equals("NO") || (QCurpRenapo.size()==1 && gdoTerminal.equals("SI") ) )
               stm.execute(query);                                              //PARA SABER k curp aun estando CORRECTA la kisieron cambiar
        }
   }
    
    private ArrayList<Map> curpVit (String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT * FROM curpVit WHERE idalu="+idalu);
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public String getObservacionesAluGdo (String idalu, String cicescini) throws SQLException
    {
        rs = stm.executeQuery("SELECT observaciones FROM observsAluGdo WHERE idalu="+idalu+" AND cicescini="+cicescini);
        if (rs.next())
            return rs.getString("observaciones");
        return "";
            
    }
    
    public void setObservacionesAluGdo (String idalu, String cicescini, String grado, String observaciones, String usuario) throws SQLException
    {
        rs = stm.executeQuery("SELECT observaciones FROM observsAluGdo WHERE idalu="+idalu+" AND cicescini="+cicescini);
        if (rs.next())
            stm.execute("UPDATE observsAluGdo SET observaciones=\""+(""+observaciones).replace("\n", " ")+"\", usuario='"+usuario+"', fecha=EXTEND(CURRENT, HOUR TO SECOND) WHERE idalu="+idalu+" AND cicescini="+cicescini);
        else
            stm.execute("INSERT INTO observsAluGdo (idalu,cicescini,grado,observaciones,usuario,fecha) VALUES ("+idalu+","+cicescini+","+grado+",\""+(""+observaciones).replace("\n", " ")+"\",'"+usuario+"',EXTEND(CURRENT, HOUR TO SECOND))");
    }
   
    
//************************************************************************************************************
//******************************* MÉTODOS USADOS EN EL MÓDULO Oficializaciones *******************************
//************************************************************************************************************
    
    public void oficializar (String cicescini, String tblPrincipal_modalidad, String tblPrincipal_cveplan, String tblPrincipal_idcct, String grado, 
            String grupo, String oficializacion, String bimestre, String txtUsuario) throws SQLException, SICEEO_Excepcion
    {
        String cveoficializacion;
        
        if (oficializacion.contains("CALIFS BIM"))
            canOficCalifBim (cicescini, tblPrincipal_cveplan, tblPrincipal_idcct, grado, grupo, bimestre);
        else if (oficializacion.contains("EVALUACION"))
            canOficPreescEvals (cicescini, tblPrincipal_cveplan, tblPrincipal_idcct, grado, grupo, bimestre);
       
        cveoficializacion = this.getData("SELECT cveoficializacion FROM catoficializacion WHERE oficializacion='"+oficializacion+"'");
        try {
            if (oficializacion.contains("CALIFS BIM") || oficializacion.contains("EVALUACION")){
                if (oficializacion.contains("CALIFS BIM 3") || oficializacion.contains("EVALUACION 3"))
                    asignarFolioRepEval (cicescini, tblPrincipal_modalidad, tblPrincipal_cveplan, tblPrincipal_idcct, grado, grupo, txtUsuario);
                stm.execute("UPDATE escuelagpos SET ofic_caleval_b"+bimestre+"='t' WHERE cicescini="+cicescini+" AND idcct="+tblPrincipal_idcct+" AND grado="+grado+" AND grupo='"+grupo+"'");
            }
            stm.execute("INSERT INTO Oficializacion (idcct,cicescini,cveoficializacion,usuario) VALUES ("+tblPrincipal_idcct+","+cicescini+","+cveoficializacion+",'"+txtUsuario+"')");
        }catch (SQLException ex){
            if (ex==null || ex.getMessage()==null)
                throw new SQLException (ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("OFICIALIZACION_PK")){
                if (oficializacion.contains("CALIFS BIM") || oficializacion.contains("EVALUACION"))
                    stm.execute ("DELETE FROM desoficializacion "
                               + "WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+cicescini+" AND grado="+grado+" "
                                    + "AND grupo='"+grupo+"' AND cveoficializacion="+cveoficializacion);
            } else
                throw new SQLException (ex);
        }                
    }
        
    public void canOficPreescEvals (String cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, String grado, String grupo, String numeval) throws SQLException, SICEEO_Excepcion
    {
        String msgInconsistencias="", alumnos_sineval="";
        int numMax=1, alum = 0;
        boolean hay_registros = false;
        
        //Verificamos que ya tenga el bimestre anterior oficializado
        verifPreviaOfic (tblPrincipal_idcct, cicescini, tblPrincipal_cveplan, grado, grupo, numeval, "EVALUACION "+numeval, "EVAL_SIN_PREVIA_OFIC");
        
        //verficamos si los alumnos ya cuentan con todas sus materias capturadas        
        rs = stm.executeQuery("SELECT ag.grado, ag.grupo, a.idalu, a.curp,TRIM(a.nombre)||' '||TRIM(a.apepat)||' '||TRIM(a.apemat) AS nom_tot,"
                + "NVL((SELECT count(*) FROM evalpreesc WHERE idalu = ag.idalu AND cicescini=ag.cicescini AND "
                    + "numeval="+numeval+" AND avances!=''),0) as num_eval," 
                + "NVL((SELECT count(*) FROM esquemamaterias WHERE grado = ag.grado AND cicescini <= ag.cicescini AND " 
                    + "cicescfin >= ag.cicescfin AND cveplan = ag.cveplan AND cveprograma=ag.cveprograma),0) AS num_mat"
                + " FROM alumno a, alumnogrado ag, escuela e " 
                + " WHERE a.idalu=ag.idalu AND ag.idcct=e.idcct AND ag.estatusGrado <>'BD' AND ag.estatusGrado <>'RG' AND " 
                + " ag.cicescini="+cicescini+" AND ag.idcct="+tblPrincipal_idcct+" AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' ");

        
        while (rs.next()) {
            alum += 1;
            if(rs.getInt("num_eval")!=rs.getInt("num_mat")){
                alumnos_sineval +=  alum + ".- grado:"+rs.getString("grado")+", grupo:" 
                    + rs.getString("grupo")+", idalu:"+rs.getString("idalu")+", curp:"+rs.getString("curp")
                    + ", nombre:"+rs.getString("nom_tot")+".\n";
            }                                
        }        
        if(!alumnos_sineval.equals(""))                         
                throw new SICEEO_Excepcion (0,"INCONSISTENCIAS_PARA_OFIC_SIN_REG",alumnos_sineval,"evaluaciones");        
                
        //Verificamos inconsistencias en las calificaciones de los alumnos
        rs = stm.executeQuery("SELECT ag.grado, ag.grupo, a.idalu, a.curp, "
                + " (TRIM(NVL(a.apepat,'')) || '/' || TRIM(NVL(a.apemat,'')) || '*' || TRIM(NVL(a.nombre,''))) AS nom_tot, " 
                + " count(*) AS nummatsvacias, "
                + " (SELECT count(*) FROM esquemamaterias em " 
                + "     WHERE ag.cicescini>=em.cicescini AND ag.cicescini<=em.cicescfin AND " 
                + "     em.cveprograma=(SELECT cveprograma "
                                        + " FROM planmodalidad "
                                        + " WHERE modalidad=e.modalidad AND ag.cicescini>=cicescini AND ag.cicescini<=cicescfin "
                                        + " AND plan=em.cveplan AND grado=em.grado AND cveentidad=20) "
                    + "AND em.cveplan=ag.cveplan AND em.grado=ag.grado) as nummatspaq "
                + " FROM alumno a, alumnogrado ag, evalpreesc ep, escuela e "
                + " WHERE a.idalu=ag.idalu AND ep.cicescini=ag.cicescini AND ep.idalu=ag.idalu AND "
                + " ag.idcct=e.idcct AND ag.estatusGrado <>'BD' AND ag.estatusGrado <>'RG' "
                + " AND ag.cicescini="+cicescini+" AND ag.idcct="+tblPrincipal_idcct+" AND ag.grado="+grado
                + " AND ag.grupo='"+grupo+"' AND ep.numeval="+numeval+" AND TRIM(ep.avances)='' "
                + " GROUP BY 1,2,3,4,5,7 "
                + " ORDER BY 6, 1, 2, 4");
            
        while (rs.next()) {
            hay_registros = true;
            if (numMax>10)
                break;
            if (!rs.getString("nummatsvacias").equals(rs.getString("nummatspaq"))) {
                msgInconsistencias += numMax+".- grado:"+rs.getString("grado")+", grupo:"+rs.getString("grupo")+", idalu:"+rs.getString("idalu")+", curp:"+rs.getString("curp")+", nombre:"+rs.getString("nom_tot")+".\n";
                numMax++;
            }
        }
        if(hay_registros) {
            msgInconsistencias="Deberá registrar las evaluaciones de su grupo.";
            throw new SICEEO_Excepcion (0,"INCONSISTENCIAS_PARA_OFIC_SIN_REG",msgInconsistencias,"evaluaciones");            
        }    
        if (!msgInconsistencias.equals("")) {
            throw new SICEEO_Excepcion (0,"INCONSISTENCIAS_PARA_OFIC",msgInconsistencias,"evaluaciones");
        }
    }
    
    public void canOficCalifBim (String cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, String grado, String grupo, String bimestre) throws SQLException, SICEEO_Excepcion
    {
        String idalu, idalus="";
        String msgInconsistencias="";
        int numMax =1, bim;
        //String statusIngles = "", strIng="";
        //Verificamos que ya tenga el bimestre anterior oficializado
        verifPreviaOfic (tblPrincipal_idcct, cicescini, tblPrincipal_cveplan, grado, grupo, bimestre, "CALIFS BIM "+bimestre, "BIM_SIN_PREVIA_OFIC");
        /*if(tblPrincipal_cveplan.equals("1")) 
        {
            statusIngles = this.getData("SELECT ing1eval FROM escuelagpos WHERE idcct = " + tblPrincipal_idcct
                    + " AND cicescini = "+cicescini +" AND grado = "+grado+" AND grupo='"+grupo+"' ");
            //Verificamos inconsistencias en las calificaciones de los alumnos
            if((""+statusIngles).equals("f") || (""+statusIngles).equals("null"))
                strIng = "-1";
        }*/
        /*rs = stm.executeQuery("SELECT ag.grado, ag.grupo, am.idalu, a.curp, "
                                 + "(TRIM(NVL(apepat,'')) || '/' || TRIM(NVL(apemat,'')) || '*' || TRIM(NVL(nombre,''))) AS nom_tot, "
                                 + "count(*)"+strIng+" AS NumMat, "
                                 + "(SELECT count(*) FROM evaluaciones ev "
                                 + "WHERE ev.idalu = am.idalu AND ev.cicescini = am.cicescini AND numeval="+bimestre + (strIng.equals("-1")?" AND cvemat NOT IN ('084','ING','087','088') ":" ") + "  ) AS NumMatEv, "
                                 //+ "NVL((SELECT " + (bimestre.equals("1")?"trim1":(bimestre.equals("2")?"trim2":"trim3")) + " FROM comunicacion_doc_alum WHERE idalu=a.idalu AND cicescini=ag.cicescini),0) AS ComDocAlu, "
                                 + "(SELECT count(*) FROM evaluaciones WHERE cicescini=ag.cicescini AND idalu=a.idalu and numeval=" + bimestre + " AND " + (strIng.equals("-1")?"cvemat NOT IN ('084','ING','087','088') AND ":"") + "calif1 < 6.0) AS CalfRep "
                             + "FROM alumno a, alumnomaterias am, alumnogrado ag "
                             + "WHERE a.idalu=am.idalu AND am.idalu=ag.idalu AND am.cicescini=ag.cicescini "//+"AND ag.estatusGrado <>'BD' AND a.estatusalu ='I'  "
                                 + "AND  ag.cicescini="+cicescini+" AND ag.idcct = "+tblPrincipal_idcct+" AND ag.estatusgrado<>'BD' AND ag.estatusgrado<>'RG' AND substr(am.cvemat,1,3)<>'NAP' "
                                 + "AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' "
                                 + "AND ag.idalu NOT IN (SELECT idalu FROM comipems WHERE cicescini="+cicescini+") "
                             + "GROUP BY 1,2,3,4,5,7,8,9 "
                             + "ORDER BY 7, 1, 2, 4");

        while (rs.next()) {
            if(rs.getInt("comdocalu")==0 )
                msgComDocAlum += "idalu:"+rs.getString("idalu")+", curp:"+rs.getString("curp")+", nombre:"+rs.getString("nom_tot")+".\n"; 
            if( (""+rs.getInt("comdocalu")).equals("1") && rs.getInt("calfrep")>0 )
                msgValComDocAlum += "idalu:"+rs.getString("idalu")+", curp:"+rs.getString("curp")+", nombre:"+rs.getString("nom_tot")+".\n";
        }
        */
        rs = stm.executeQuery("SELECT ag.grado, ag.grupo, am.idalu, a.curp, "
                                 + "(TRIM(NVL(apepat,'')) || '/' || TRIM(NVL(apemat,'')) || '*' || TRIM(NVL(nombre,''))) AS nom_tot, "
                                 + "count(*) AS NumMat, "
                                 + "(SELECT count(*) FROM evaluaciones ev "
                                 + "WHERE ev.idalu = am.idalu AND ev.cicescini = am.cicescini AND numeval="+bimestre + " AND calif1>=5 AND calif1<=10 ) AS NumMatEv, "
                                 + "NVL((SELECT " + (bimestre.equals("1")?"trim1":(bimestre.equals("2")?"trim2":"trim3")) + " FROM comunicacion_doc_alum WHERE idalu=a.idalu AND cicescini=ag.cicescini),0) AS ComDocAlu, "
                                 + "(SELECT count(*) FROM evaluaciones WHERE cicescini=ag.cicescini AND idalu=a.idalu and numeval=" + bimestre + " AND "  + "calif1 < 6.0) AS CalfRep "
                             + "FROM alumno a, alumnomaterias am, alumnogrado ag "
                             + "WHERE a.idalu=am.idalu AND am.idalu=ag.idalu AND am.cicescini=ag.cicescini "//+"AND ag.estatusGrado <>'BD' AND a.estatusalu ='I'  "
                                 + "AND  ag.cicescini="+cicescini+" AND ag.idcct = "+tblPrincipal_idcct+" AND ag.estatusgrado<>'BD' AND ag.estatusgrado<>'RG' AND substr(am.cvemat,1,3)<>'NAP' "
                                 + "AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' "
                                 + "AND ag.idalu NOT IN (SELECT idalu FROM comipems WHERE cicescini="+cicescini+") "
                             + "GROUP BY 1,2,3,4,5,7,8,9 "
                             + "ORDER BY 7, 1, 2, 4");
        
        while (rs.next()) {
            if ((""+rs.getInt("nummat")).equals(rs.getString("nummatev")) || numMax>10)
                break;
            idalu = rs.getString("idalu");
            msgInconsistencias += numMax+".- grado:"+rs.getString("grado")+", grupo:"+rs.getString("grupo")+", idalu:"+idalu+", curp:"+rs.getString("curp")+", nombre:"+rs.getString("nom_tot")+".\n";
            idalus += (numMax==1?"":",")+idalu;
            numMax++;
        }      
        
        if (numMax<10) {
            rs = stm.executeQuery("SELECT ag.grado, ag.grupo, a.idalu, a.curp, "
                                    + "(TRIM(NVL(a.apepat,'')) || '/' || TRIM(NVL(a.apemat,'')) || '*' || TRIM(NVL(a.nombre,''))) AS nom_tot " +
                                "FROM alumno a, alumnogrado ag, evaluaciones ev " +
                                "WHERE a.idalu=ag.idalu AND a.idalu=ev.idalu AND ag.cicescini=ev.cicescini AND ag.estatusGrado <>'BD' AND ag.estatusGrado <>'RG' " +
                                    "AND (substr(CAST(ev.calif1 AS CHAR(4)),4,1)!='0' OR substr(CAST(ev.calif2 AS CHAR(4)),4,1)!='0') AND " +
                                    "ag.cicescini="+cicescini+" AND ag.idcct="+tblPrincipal_idcct+" AND ev.numeval="+bimestre+" "+
                                    "AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' " +
                                    "AND ag.idalu NOT IN (SELECT idalu FROM comipems WHERE cicescini="+cicescini+") " +
                                    (idalus.equals("")?"":"AND ag.idalu NOT IN ("+idalus+") ") +
                                "GROUP BY ag.grado, ag.grupo, a.idalu, a.curp, nom_tot");
            while (rs.next()){
                if (numMax>10)
                    break;
                msgInconsistencias+=numMax+".- grado:"+rs.getString("grado")+", grupo:"+rs.getString("grupo")+", idalu:"+rs.getString("idalu")+", curp:"+rs.getString("curp")+", nombre:"+rs.getString("nom_tot")+".\n";
                numMax++;
            }
        }
        
        if (!msgInconsistencias.equals(""))
            throw new SICEEO_Excepcion (0,"INCONSISTENCIAS_PARA_OFIC",msgInconsistencias,"calificaciones");
        /*else if(!msgComDocAlum.equals(""))
            throw new SICEEO_Excepcion (0,"INCONSIST_COMDOCALU_PARA_OFIC",msgComDocAlum,"Comunicación");
        else if(!msgValComDocAlum.equals(""))
            throw new SICEEO_Excepcion (0,"VALIDACION_COMDOCALU_PARA_OFIC",msgValComDocAlum,"calificaciones");*/
    }
    
    public void desoficializar (boolean puedeDesoficializar, String oficializacion, String tblPrincipal_cicescini, String tblPrincipal_idcct, String idalus, String txtUsuario) throws SQLException, SICEEO_Excepcion
    {
        String cveoficializacion, sqryIdalus;
       
        if (oficializacion.contains("CALIFS BIM") || oficializacion.contains("EVALUACION"))  //Verificamos si ya tiene oficializado el 5to bimestre o la 3ra evaluación, pues si lo está entonces ya no será posible desoficializar
        {
            cveoficializacion = (oficializacion.contains("CALIFS BIM"))?"7":"10";
            if ( !"".equals(this.getData("SELECT idcct FROM Oficializacion WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+tblPrincipal_cicescini+" AND cveoficializacion="+cveoficializacion)) )
                if (!puedeDesoficializar)
                    throw new SICEEO_Excepcion (0, ((oficializacion.contains("CALIFS BIM"))?"ULTIMO_BIMESTRE_OFIC":"ULTIMA_EVAL_OFIC"));
        }
        
        cveoficializacion = this.getData("SELECT cveoficializacion FROM catoficializacion WHERE oficializacion='"+oficializacion+"'");
        
        // OJO: TENER CUIDADO CON EL CONCEPTO PREINSCRIPCION, YA QUE TRABAJA CON EL CICESCINI+1
        if (oficializacion.contains("CALIFS BIM") || oficializacion.contains("EVALUACION"))
        {
            sqryIdalus = idalus.equals("")?"":" AND idalu in ("+idalus+")";
            stm.execute("INSERT INTO desoficializacion (idcct, cicescini, grado, grupo, idalu, cveoficializacion, usuario, fecha) "
                      + "SELECT idcct, cicescini, grado, grupo, idalu, "+cveoficializacion+", '"+txtUsuario+"', extend(current, YEAR TO SECOND) "
                      + "FROM alumnogrado "
                      + "WHERE cicescini="+tblPrincipal_cicescini+" AND idcct="+tblPrincipal_idcct + " AND estatusgrado<>'BD' " + sqryIdalus);
        }else
            stm.execute("DELETE FROM Oficializacion WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+tblPrincipal_cicescini+" AND cveoficializacion="+cveoficializacion);
    }
       
    private void verifPreviaOfic (String tblPrincipal_idcct, String cicescini, String tblPrincipal_cveplan, String grado, String grupo, String bimeval, String oficializacion, String nombreExcepcion) throws SQLException, SICEEO_Excepcion
    {
        String cveoficializacion;
        String numYaOfic, sqryCalEval="";
        //Verificamos que ya tenga el bimestre anterior oficializado,   OJO: falta checar que no haya alumnos desoficializados, y falta checar que no haya algun grupo sin oficializar
        if (!bimeval.equals("1")){
            cveoficializacion = this.getData("SELECT cveoficializacion FROM catoficializacion WHERE oficializacion='"+oficializacion+"'");
            /*numYaOfic = this.getData("SELECT count(o.idcct) "
                                    + "FROM Oficializacion o, catoficializacion co "
                                    + "WHERE o.cveoficializacion=co.cveoficializacion AND o.idcct="+tblPrincipal_idcct+" AND o.cicescini="+cicescini+" "
                                            + "AND o.cveoficializacion<"+cveoficializacion+" "
                                            + "AND co.oficializacion like '"+oficializacion.replace(bimeval, "")+"%'");
            
            if (Integer.parseInt(bimeval)-1 != Integer.parseInt(numYaOfic))*/
            //    throw new SICEEO_Excepcion (0,nombreExcepcion);
            
            //Verificamos que los bimestres anteriores estén en true, es decir oficializado
            for (int i=1; i<Integer.parseInt(bimeval); i++)
                sqryCalEval += ((i==1)?"":" AND ")+("ofic_caleval_b"+i);
            rs = stm.executeQuery("SELECT "+sqryCalEval+" "
                                + "FROM escuelagpos "
                                + "WHERE cicescini="+cicescini+" AND idcct="+tblPrincipal_idcct+" AND grado="+grado+" AND grupo='"+grupo+"'");
            if (rs.next())
                if (!rs.getBoolean(1))
                    throw new SICEEO_Excepcion (0,nombreExcepcion);
            //Verificamos que no haya alumnos desoficializados
            stm.executeQuery("SELECT count(idalu) FROM desoficializacion "
                          + "WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+cicescini+" AND grado="+grado+" "
                                + "AND grupo='"+grupo+"' AND "
                                + (tblPrincipal_cveplan.equals("3")? " (cveoficializacion >= 8 AND ": " (cveoficializacion >= 3 AND ") + " cveoficializacion < "+cveoficializacion + ") "
                                + "AND idalu IN (SELECT idalu FROM alumnogrado WHERE cicescini="+cicescini+" AND idcct="+tblPrincipal_idcct+" AND "
                                + "grado="+grado+" AND grupo='"+grupo+"' AND estatusgrado!='BD') ");
            if (rs.next())
                if (rs.getInt(1)>0)
                    throw new SICEEO_Excepcion (0,nombreExcepcion);
        }
    }
    private String verificaGrupo (String tblPrincipal_idcct, String cicescini, String grado, String grupo, String numeval,String nombreExcepcion) throws SQLException, SICEEO_Excepcion
    {
        String dato="";
        rs = stm.executeQuery("SELECT a.idalu "                 
                + " FROM alumno a, alumnogrado ag "
                + " WHERE a.idalu=ag.idalu "
                + " AND ag.estatusGrado <>'BD' AND ag.estatusGrado <>'RG' "
                + " AND ag.cicescini="+cicescini+" AND ag.idcct="+tblPrincipal_idcct+" AND ag.grado="+grado
                + " AND ag.grupo='"+grupo+"'");
        if (rs.next())                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
            dato = rs.getString(1);
                    
        return dato;            
    }
   
    public boolean isOficializado (String tblPrincipal_idcct, String cicescini, String grado, String grupo, String oficializacion) throws SQLException
    {
        boolean respuesta=false;
        String cveoficializacion = this.getData("SELECT cveoficializacion FROM catoficializacion WHERE oficializacion='"+oficializacion+"'");
        if (oficializacion.contains("CALIFS") || oficializacion.contains("EVALUACION")){
            rs = stm.executeQuery("SELECT ofic_caleval_b"+oficializacion.replace("CALIFS BIM ", "").replace("EVALUACION ", "")+" AS bimOfic, "
                                    + "(select count(idalu) from desoficializacion where cicescini=eg.cicescini AND idcct=eg.idcct AND grado=eg.grado AND grupo=eg.grupo AND cveoficializacion="+cveoficializacion+") AS alusDesofic "
                                + "FROM escuelagpos eg WHERE eg.cicescini="+cicescini+" AND eg.idcct="+tblPrincipal_idcct+" "
                                    + "AND eg.grado="+grado+" AND eg.grupo='"+grupo+"'");
            if (rs.next()){
                respuesta = rs.getBoolean("bimOfic") && rs.getInt("alusDesofic")==0;
            }
        }else{
            rs = stm.executeQuery("SELECT idcct "
                                + "FROM Oficializacion WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+cicescini+" "
                                    + "AND cveoficializacion="+cveoficializacion);
            respuesta = rs.next();
        }
        return respuesta;
    }
    
    public boolean isInEscuelaGrado (String tblPrincipal_idcct, String cicescini, String grado, String grupo, String oficializacion) throws SQLException
    {
        boolean respuesta=false;
        rs = stm.executeQuery("SELECT idcct "
                                + "FROM Oficializacion WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+cicescini+" "
                                    + "AND cveoficializacion=");
            respuesta = rs.next();
            
        return respuesta;
    }
    
    
    public boolean isCalEvalGradoGrupoOficializado(String tblPrincipal_idcct, String cicescini, String grado, String grupo, String oficializacion) throws SQLException
    {
        String sqryBimSel="";
        if (oficializacion.contains("CALIFS"))
            sqryBimSel = "ofic_caleval_b1 AND ofic_caleval_b2 AND ofic_caleval_b3 AND ofic_caleval_b4 AND ofic_caleval_b5";
        else if (oficializacion.contains("EVALUACION"))
            sqryBimSel = "ofic_caleval_b1 AND ofic_caleval_b2 AND ofic_caleval_b3";
        
        rs = stm.executeQuery("SELECT " + sqryBimSel + " "
                        + "FROM escuelagpos WHERE cicescini="+cicescini+" AND idcct="+tblPrincipal_idcct+" AND grado="+grado+" AND grupo='"+grupo+"'");
        rs.next();
        return rs.getBoolean(1);
    }
    
    public boolean isCalEvalGradoGrupoOficSinAlusDesofic(String tblPrincipal_idcct, String cicescini, String grado, String grupo, String oficializacion) throws SQLException
    {
        boolean gradoOficializado = false, hayAlumnosDesoficializados;
        String sqryBimSel="";
        if (oficializacion.contains("CALIFS"))
            sqryBimSel = "ofic_caleval_b1 AND ofic_caleval_b2 AND ofic_caleval_b3"; // AND ofic_caleval_b4 AND ofic_caleval_b5";
        else if (oficializacion.contains("EVALUACION"))
            sqryBimSel = "ofic_caleval_b1 AND ofic_caleval_b2 AND ofic_caleval_b3";
        
        rs = stm.executeQuery("SELECT " + sqryBimSel + " "
                        + "FROM escuelagpos WHERE cicescini="+cicescini+" AND idcct="+tblPrincipal_idcct+" AND grado="+grado+" AND grupo='"+grupo+"'");
        if(rs.next())            
            gradoOficializado = rs.getBoolean(1);       
        hayAlumnosDesoficializados = hasAlumnosDesoficializados (cicescini, tblPrincipal_idcct, grado, grupo, "", oficializacion);
        
        return gradoOficializado && !hayAlumnosDesoficializados;
    }
   
    public boolean isFirmaElecAlu (String tblPrincipal_idcct, String cicescini, String cicescinilib, String grado, String grupo) throws SQLException
    {
        boolean firmados = false;
        String strQuery="";
        
            strQuery = "SELECT " + (cicescinilib.equals("2016") ? "count(fe.sellodigitalieepo)" : "count(fe.foliodigital)" ) 
                    + " FROM alumnogrado ag, folios_impre fi, escuela e, firma_elec fe " 
                + " WHERE ag.idalu=fi.idalu AND ag.cicescini=fi.cicescini AND fi.idcct=e.idcct " 
                + "  AND fi.idalu=fe.idalu AND fi.cicescinilib=fe.cicescinilib " 
                + "  AND ag.estatusgrado<>'BD' AND fi.promediogral is not null  " 
                + "  AND fi.promediogral>=6 AND fi.cicescinilib= " + cicescinilib 
                + "  AND fi.idcct="+tblPrincipal_idcct+" AND fi.grado="+grado+" AND fi.grupo='"+grupo+"' AND fi.estatus = 'AC' " 
                + (cicescinilib.equals("2016") ? "" : " AND fe.foliodigital is not null AND fe.sellodigitalsep is not null" );
            
        rs = stm.executeQuery(strQuery);
        rs.next();        
        if(rs.getInt(1)>0)
            firmados = true;
        return firmados;    
    }
    
    public boolean isConceptoTodoOficializado (String tblPrincipal_idcct, String cicescini, String oficializacion) throws SQLException
    {
        int totalGpos;
        if (oficializacion.contains("CALIFS") || oficializacion.contains("EVALUACION")){
            rs = stm.executeQuery(
                "SELECT "
                    + "COUNT(ofic_caleval_b1) AS totalGpos, "
                    + "(SELECT COUNT(ofic_caleval_b1) FROM escuelagpos WHERE cicescini=eg.cicescini AND idcct=eg.idcct AND ofic_caleval_b1 = 't') AS oficB1, "
                    + "(SELECT COUNT(ofic_caleval_b2) FROM escuelagpos WHERE cicescini=eg.cicescini AND idcct=eg.idcct AND ofic_caleval_b2 = 't') AS oficB2, "
                    + "(SELECT COUNT(ofic_caleval_b3) FROM escuelagpos WHERE cicescini=eg.cicescini AND idcct=eg.idcct AND ofic_caleval_b3 = 't') AS oficB3, "
                    + "(SELECT COUNT(ofic_caleval_b4) FROM escuelagpos WHERE cicescini=eg.cicescini AND idcct=eg.idcct AND ofic_caleval_b4 = 't') AS oficB4, "
                    + "(SELECT COUNT(ofic_caleval_b5) FROM escuelagpos WHERE cicescini=eg.cicescini AND idcct=eg.idcct AND ofic_caleval_b5 = 't') AS oficB5 "
                + "FROM escuelagpos eg "
                + "WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+cicescini+" "
                + "GROUP BY 2,3,4,5,6");
            if (rs.next()){
                totalGpos = rs.getInt("totalGpos");
                return (totalGpos==rs.getInt("oficB1") && totalGpos==rs.getInt("oficB2") && totalGpos==rs.getInt("oficB3") && totalGpos==rs.getInt("oficB4") 
                        && totalGpos==rs.getInt("oficB5") ) && !hasAlumnosDesoficializados (cicescini, tblPrincipal_idcct, "", "", "", oficializacion.substring(0,6));
            }
        }else{
            rs = stm.executeQuery("SELECT COUNT(*) = (SELECT COUNT(cveoficializacion) FROM catoficializacion WHERE oficializacion like '"+oficializacion+"%') " +
                                 "FROM Oficializacion " +
                                 "WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+cicescini+" "
                                     + "AND cveoficializacion IN (SELECT cveoficializacion FROM catoficializacion WHERE oficializacion like '"+oficializacion+"%')");
            if (rs.next())
                return rs.getBoolean(1);
        }
        return false;
    }
    
    public boolean hasSomeConceptoOficializado (String tblPrincipal_idcct, String cicescini, String oficializacion) throws SQLException
    {
        
        rs = stm.executeQuery("SELECT COUNT(*)>0 " +
                             "FROM Oficializacion " +
                             "WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+cicescini+" "
                                 + "AND cveoficializacion IN (SELECT cveoficializacion FROM catoficializacion WHERE oficializacion like '"+oficializacion+"%')");
        if (rs.next())
            return rs.getBoolean(1);
        return false;
    }
    
    public boolean isAlumnoDesoficialziado (String cicescini, String oficializacion, String idalu) throws SQLException
    {
        rs = stm.executeQuery("SELECT idalu "
                            + "FROM desoficializacion "
                            + "WHERE cicescini="+cicescini+" AND idalu="+idalu+" "
                                + "AND cveoficializacion IN (SELECT cveoficializacion FROM catoficializacion WHERE oficializacion like '"+oficializacion+"%') ");
        return rs.next();
    }
    
    public boolean hasAlumnosDesoficializados (String cicescini, String idcct, String grado, String grupo, String idalus, String oficializacion) throws SQLException
    {
        String sqryGrado, sqryGrupo, sqryIdalus;
        sqryGrado = grado.equals("") ? "" : " AND grado="+grado + " ";
        sqryGrupo = grupo.equals("") ? "" : " AND grupo='"+grupo + "' ";
        sqryIdalus = idalus.equals("") ? "" : " AND idalu IN (" + idalus + ") ";
        
        rs = stm.executeQuery("SELECT idalu "
                            + "FROM desoficializacion "
                            + "WHERE cicescini="+cicescini+" AND idcct="+idcct+" " + sqryGrado + sqryGrupo +sqryIdalus
                                + "AND cveoficializacion IN (SELECT cveoficializacion FROM catoficializacion WHERE oficializacion like '"+oficializacion+"%') ");
        return rs.next();
    }
    
    public ArrayList<String> getAlumnosDesoficEnGrupoEnBimeval (String cicescini, String idcct, String grado, String grupo, String oficializacion) throws SQLException
    {
        ArrayList<String> idalus = new ArrayList<String>();
        rs = stm.executeQuery("SELECT idalu "
                            + "FROM desoficializacion "
                            + "WHERE cicescini="+cicescini+" AND idcct="+idcct+" AND grado="+grado+" AND grupo='"+grupo+"' "
                                + "AND cveoficializacion IN (SELECT cveoficializacion FROM catoficializacion WHERE oficializacion = '"+oficializacion+"') ");
        while (rs.next())
            idalus.add(rs.getString("idalu"));
        return idalus;
    }
    
    public void asignarFolioRepEval (String cicescini, String modalidad, String cveplan, String idcct, String grado, String grupo, String usuario) throws SQLException
    {
        String nivelmod="";
        int folionum;
        Statement stm2=null;
        ResultSet rs2=null;
        
        //Obtenemos el nuevo número de folio
        if (cveplan.equals("1"))
            nivelmod =  "PRIM";
        else if (cveplan.equals("2"))
            nivelmod = "SECU";
        else if (cveplan.equals("3"))
            nivelmod = "PREE";
        
        //rs = stm.executeQuery("SELECT NVL(MAX(folionum),0)+1 FROM fol_re_elec WHERE cicescini="+cicescini+" AND nivelmod='"+nivelmod+"'");
        rs = stm.executeQuery("SELECT NVL(MAX(folionum),0)+1 as folionum,"
                + " (SELECT NVL(MAX(folionum),0)+1 FROM fol_re_elec_cance WHERE cicescini="+cicescini+" AND nivelmod='"+nivelmod+"' ) AS foliocance "
                + " FROM fol_re_elec WHERE cicescini="+cicescini+" AND nivelmod='"+nivelmod+"'");
        rs.next();
        folionum = rs.getInt(1)>rs.getInt(2) ? rs.getInt(1) : rs.getInt(2);
        
        //Obtenemos los alumnos que no tienen folio
        try
        {
            stm2 = getNewStatement ();
            /*rs2 = stm2.executeQuery(
                      "SELECT ag.idalu "
                    + "FROM alumnogrado ag, alumno a "
                    + "WHERE ag.idalu=a.idalu  AND ag.estatusgrado<>'BD' AND ag.cicescini="+cicescini+" AND ag.idcct="+idcct+" AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' " 
                    + "AND ag.idalu NOT IN (SELECT idalu FROM fol_re_elec WHERE cicescini="+cicescini+" AND cveplan="+cveplan+") "
                    + "AND ag.idalu NOT IN (SELECT idalu FROM comipems WHERE cicescini="+cicescini+") "          
                    + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp ");*/
            rs2 = stm2.executeQuery("SELECT ag.idalu, (SELECT folionum FROM fol_re_elec f WHERE f.idalu = ag.idalu AND f.cicescini="+cicescini+") AS folio "
                    + " FROM alumnogrado ag, alumno a " 
                    + " WHERE ag.idalu=a.idalu  AND ag.estatusgrado<>'BD' AND ag.estatusgrado<>'RG' "
                    + " AND ag.cicescini="+cicescini+" AND ag.idcct="+idcct+" AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' " 
                    //+ " AND ag.idalu NOT IN (SELECT idalu FROM comipems WHERE cicescini="+cicescini+") "   //Se van a mexico y se requiere rapidamente su boleta, comenta en fin de ciclo 2021.
                    + " ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
            
            //Creamos los nuevos registros con folio de Reportes de Evaluación
            while (rs2.next()){
                if((""+rs2.getString("folio")).equals("null") || rs2.getString("folio")==null) {
                    stm.execute(
                              "INSERT INTO fol_re_elec (idalu, cveplan, foliolet, folionum, idcct, grado, cicescini, usuario, fecha, hora, estatus, nivelmod, fecha_expedicion) "
                            + "SELECT idalu, cveplan, 'BE', "+folionum+", idcct, grado, cicescini, '"+usuario+"', date(current), extend(current, hour to minute), 'A', '"+nivelmod+"','2025-07-16' "
                            + "FROM alumnogrado "
                            + "WHERE cicescini="+cicescini+" AND idalu="+rs2.getString("idalu")+" ");
                    folionum ++;
                }
            }
        }finally {
            closeStatement(stm2, rs2);
        }
    }
    
    public boolean hayAlumnosParaComplementaria (String cicescini, String cicescinilib, String idcct, String grado, String grupo) throws SQLException
    {
        rs = stm.executeQuery(
                  "SELECT ag.idalu "
                + "FROM alumnogrado ag "
                + "WHERE "
                    + "ag.idalu  = (SELECT distinct(idalu)  "
                                    + "FROM exm_ext_ordi  "
                                    + "WHERE idalu=ag.idalu AND promedio>=6 AND (anio=(ag.cicescini+1) AND mes IN ('AGOSTO','SEPTIEMBRE','OCTUBRE') "
                                        + "OR anio=(ag.cicescini+2) AND mes IN ('ENERO')) ) "
                    + "AND (ag.idalu = (SELECT idalu "
                                        + "FROM folios_impre "
                                        + "WHERE idalu=ag.idalu AND cicescinilib="+cicescinilib+" AND grado=ag.grado AND estatus='AC' AND alos LIKE '%julio%') " //AND idcct=ag.idcct AND grupo=ag.grupo 
                            + "OR ag.idalu NOT IN (SELECT idalu FROM folios_impre WHERE idalu=ag.idalu AND cicescinilib="+cicescinilib+"  AND estatus='AC' ) ) "
                    + "AND ag.cicescini="+cicescini+" AND ag.idcct="+idcct+" AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' AND ag.estatusgrado<>'BD' AND ag.estatusgrado<>'RG' "
                    + "AND ag.promediogral>=6 ");
        
        return rs.next();
    }
       
//************************************************************************************************************
//*********************************** MÉTODOS USADOS EN EL MÓDULO Personal ***********************************
//************************************************************************************************************
    public ArrayList<Map> getGradosGrupos (String tblPrincipal_idcct, String califCicEscIni) throws SQLException
    {
        rs = stm.executeQuery("SELECT idcct, cveturno, grado, grupo "
                            + "FROM escuelagpos "
                            + "WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+califCicEscIni+" AND estatus='A'"
                            + "ORDER BY idcct, cveturno, grado, grupo");
        return qryToArrlmap(rs, null, true, 0);
    }
    
    public Map getDatosEscuelaDePersonal (String tblPrincipal_idcct, String califCicEscIni) throws SQLException
    {
        Map escuela;
        rs = stm.executeQuery("SELECT cct, (SELECT  TRIM(nombre) || ' ' || TRIM(apepat) || ' ' || TRIM(apemat) FROM directores WHERE idcct=e.idcct  AND cicescini = "+ califCicEscIni + ") AS director, "
                + "telefono, email, domicilio, colonia FROM escuela e WHERE idcct="+tblPrincipal_idcct);
        return qryToMap(rs, null, true, 2);
    }
    
    public void getPersonal (String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, Map dr) throws SQLException
    {
        Map director, profesor, temp = new HashMap();
        temp.put("numemp", "");        
        temp.put("nombre", "");
        temp.put("apepat", "");
        temp.put("apemat", "");
        temp.put("sexo", "");
        
        rs = stm.executeQuery("SELECT  TRIM(nombre) AS nombre, TRIM(apepat) AS apepat, TRIM(apemat) AS apemat, TRIM(sexo) AS sexo " +
                                "FROM directores " +
                                "WHERE idcct="+tblPrincipal_idcct + " "
                            + " AND cicescini = "+ califCicEscIn );
        director = qryToMap(rs, null, true, 2);
        if (director.isEmpty())
            director.putAll(temp);
        
        /*rs = stm.executeQuery("SELECT p.numemp, TRIM(p.rfc) AS rfc, TRIM(p.nombre) AS nombre, TRIM(p.apepat) AS apepat, TRIM(p.apemat) AS apemat " +
                            "FROM profcurricula pc, profesores p " +
                            "WHERE pc.numemp=p.numemp AND pc.cicescini="+califCicEscIn+" AND pc.idcct="+tblPrincipal_idcct+" AND pc.grado="+tblPrincipal_grado+" "
                                + "AND pc.grupo='"+tblPrincipal_grupo+"'");*/
        rs = stm.executeQuery("SELECT nombre, apepat, apemat FROM profgdogpo WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+califCicEscIn+" "
                                + "AND grado="+tblPrincipal_grado+" AND grupo='"+tblPrincipal_grupo+"'");
        
        profesor = qryToMap(rs, null, true, 2);
        if (profesor.isEmpty())
            profesor.putAll(temp);
        
        dr.put("director", director);
        dr.put("profesor", profesor);
    }
    
    public String buscarTutor (String curp) throws SQLException
    {
        String idtutor="";
        rs = stm.executeQuery("SELECT idtutor FROM tutor WHERE curp='"+curp+"'");
        if(rs.next())
            idtutor = rs.getString("idtutor");
        return idtutor;
    }
    
    public void getTutorAlumno (String tblPrincipal_idalu, String cicescini, Map dr) throws SQLException
    {
        Map tutor, temp = new HashMap();
        temp.put("numemp", "");        
        temp.put("nombre", "");
        temp.put("apepat", "");
        temp.put("apemat", "");
        temp.put("telefono", "");
        temp.put("curp", "");
        temp.put("cveparent", "");
        
        /*rs = stm.executeQuery("SELECT idalu, a.idtutor,t.curp, t.nombre,t.apepat, t.apemat, t.cveparent, t.telefono " 
                + " FROM alumno a, tutor t " 
                + " WHERE a.idalu = " + tblPrincipal_idalu
                + " AND a.idtutor = t.idtutor ");*/
        
        rs = stm.executeQuery("SELECT t.nombre, t.apepat, t.apemat, t.curp, t.telefono, alt.cveparent " 
                + " FROM alumnotutor alt, tutor t "
                + " WHERE alt.idtutor = t.idtutor " 
                + " AND alt.idalu = " + tblPrincipal_idalu
                + " AND alt.cicescini = " + cicescini );
        tutor = qryToMap(rs, null, true, 2);
        if (tutor.isEmpty())
            tutor.putAll(temp);

        dr.put("tutor", tutor);
    }
    
    public void insertarAluTutor (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, String tblPrincipal_grupo, String txtUsuario) throws SQLException
    {        
        String query;
        int cicescini_ant = Integer.parseInt(tblPrincipal_cicescini)-1;

        query = " INSERT INTO alumnotutor (idalu, idtutor, cveparent, cicescini,usuario,fecha,hora) "
            + " SELECT atu.idalu, atu.idtutor, atu.cveparent,"+tblPrincipal_cicescini+",'" + txtUsuario + "',  date(current), extend(current, hour to minute) "
            + " FROM alumnotutor atu, alumnogrado ag "
                + " WHERE atu.idalu = ag.idalu AND " 
                + " ag.cicescini = " + tblPrincipal_cicescini + " AND " 
                + " atu.cicescini = " + cicescini_ant + " AND " 
                + " ag.estatusgrado <> 'BD' AND " 
                + " ag.grado = " + tblPrincipal_grado + " AND " 
                + " ag.grupo = '" + tblPrincipal_grupo + "' AND " 
                + " ag.idcct = " + tblPrincipal_idcct + " AND " 
                + " NOT EXISTS (SELECT * FROM alumnotutor atu WHERE atu.idalu = ag.idalu AND cicescini = " + tblPrincipal_cicescini + " ) ";
                       
        stm.execute(query);        
    }
    
    public Map buscarRFCPersonal (String caso, String RFC) throws SQLException
    {
        int numDirectores;
        Map personal = new HashMap();
        personal.put("nombre", "");
        personal.put("apepat", "");
        personal.put("apemat", "");
        personal.put("sexo", "");
        personal.put("encontrado", false);
        
        if (caso.equals("PROFESOR")){
            rs = stm.executeQuery ("SELECT TRIM(nombre) AS nombre, TRIM(apepat) AS apepat, TRIM(apemat) AS apemat FROM profesores WHERE curp='"+RFC.trim().toUpperCase()+"'");
            if (rs.next()){
                personal.put("nombre", rs.getString("nombre"));
                personal.put("apepat", rs.getString("apepat"));
                personal.put("apemat", rs.getString("apemat"));
                personal.put("encontrado", true);
            }
        }else{
            numDirectores = Integer.parseInt(getData ("SELECT count(*) FROM directores WHERE rfc='"+RFC.trim().toUpperCase()+"'"));
            if (numDirectores == 1){
                rs = stm.executeQuery ("SELECT TRIM(nombre) AS nombre, TRIM(apepat) AS apepat, TRIM(apemat) AS apemat, sexo FROM directores WHERE rfc='"+RFC.trim().toUpperCase()+"'");
                if (rs.next()){
                    personal.put("nombre", rs.getString("nombre"));
                    personal.put("apepat", rs.getString("apepat"));
                    personal.put("apemat", rs.getString("apemat"));
                    personal.put("sexo", rs.getString("sexo"));
                    personal.put("encontrado", true);
                }
            }
        }
        
        return personal;
    }
    
    public void guardarDatosDirector (String califCicEscIn, String tblPrincipal_idcct, String tblPrincipal_cveturno, String tblPrincipal_cveplan, String txtNombreDir, 
            String txtPrimerApeDir, String txtSegundoApeDir, String cbxGeneroDir, String txtUsuario) throws SQLException
    {
        String cvecargo = tblPrincipal_cveplan.equals("2")?"003":(tblPrincipal_cveplan.equals("1")?"002":"001");
        
        stm.execute("UPDATE directores SET cvecargo='"+cvecargo+"', sexo='', "
                        + "nombre='"+txtNombreDir.trim().toUpperCase()+"', apepat='"+txtPrimerApeDir.trim().toUpperCase()+"', "
                        + "apemat='"+txtSegundoApeDir.trim().toUpperCase()+"', curp='', "
                        + "tratamiento='DIRECTOR(A) TEMPORAL', estatus='A', usuario='"+txtUsuario.trim().toUpperCase()+"', fecha=date(current), hora=extend(current, hour to minute) "
                    + "WHERE idcct="+tblPrincipal_idcct + " AND cicescini = " + califCicEscIn);
    }
    
    public void guardarDatosProfesor (String califCicEscIn, Map tblPersonal_selRow, String txtNombreProf, String txtPrimerApeProf, String txtSegundoApeProf, 
             String txtUsuario) throws SQLException
    {
        try {
            stm.execute ("INSERT INTO profgdogpo (rfc, nombre, apepat, apemat, idcct, cveturno, cicescini, cicescfin, grado, grupo, curp,usuario, fecha, hora) "
                            + "VALUES ('', '"+txtNombreProf.trim().toUpperCase()+"', '"+txtPrimerApeProf.trim().toUpperCase()+"', "
                            + "'"+txtSegundoApeProf.trim().toUpperCase()+"', "+tblPersonal_selRow.get("idcct")+", "+tblPersonal_selRow.get("cveturno")+", "
                            + ""+califCicEscIn+", "+(Integer.parseInt(califCicEscIn)+1)+", "+tblPersonal_selRow.get("grado")+", "
                            + "'"+tblPersonal_selRow.get("grupo")+"', '', '"+txtUsuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))");
        }catch (SQLException ex){
            if (ex==null || ex.getMessage()==null)
                throw new SQLException (ex);
            else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("PROFGDOGPO_PK")){
                stm.execute ("UPDATE profgdogpo SET "
                                + "rfc='', nombre='"+txtNombreProf.trim().toUpperCase()+"', apepat='"+txtPrimerApeProf.trim().toUpperCase()+"', "
                                + "apemat='"+txtSegundoApeProf.trim().toUpperCase()+"', idcct="+tblPersonal_selRow.get("idcct")+", "
                                + "cveturno="+tblPersonal_selRow.get("cveturno")+", cicescini="+califCicEscIn+", cicescfin="+(Integer.parseInt(califCicEscIn)+1)+", "
                                + "grado="+tblPersonal_selRow.get("grado")+", grupo='"+tblPersonal_selRow.get("grupo")+"', curp='', "
                                + "usuario='"+txtUsuario.trim().toUpperCase()+"', fecha=date(current), hora=extend(current, hour to minute) "
                            + "WHERE idcct="+tblPersonal_selRow.get("idcct")+" AND cicescini="+califCicEscIn+" AND grado="+tblPersonal_selRow.get("grado")+" "
                                + "AND grupo='"+tblPersonal_selRow.get("grupo")+"'");
            }else
                throw new SQLException (ex);
                
        }
    }
    public void guardarDatosProfesor_ (String califCicEscIn, Map tblPersonal_selRow, String txtNombreProf, String txtPrimerApeProf, String txtSegundoApeProf, 
            String txtRfcProf, String txtUsuario) throws SQLException
    {
        String numemp, rfcProfeAct="";
        numemp = getData("SELECT numemp FROM profcurricula WHERE idcct="+tblPersonal_selRow.get("idcct")+" AND cicescini="+califCicEscIn+" AND grado="+tblPersonal_selRow.get("grado")+" AND grupo='"+tblPersonal_selRow.get("grupo")+"'");
        if (!numemp.equals(""))
            rfcProfeAct = getData("SELECT TRIM(rfc) FROM profesores WHERE numemp="+numemp);
        
        if (!numemp.equals("") && rfcProfeAct.equals(txtRfcProf.trim().toUpperCase())){ //Si ya hay asignado un profesor en ese grado-grupo, actualizamos al profesor, pero hacemos la actualización siempre y cuando sea el único en el universo y el rfc coincida
            try {
                stm.execute("UPDATE profesores SET rfc='"+txtRfcProf.trim().toUpperCase()+"', nombre='"+txtNombreProf.trim().toUpperCase()+"', "
                        + "apepat='"+txtPrimerApeProf.trim().toUpperCase()+"', apemat='"+txtSegundoApeProf.trim().toUpperCase()+"', estatus='A', "
                        + "usuario='"+txtUsuario.trim().toUpperCase()+"', fecha=date(current), hora=extend(current, hour to minute) "
                        + "WHERE numemp="+numemp);
            } catch (SQLException ex){                                          //Si el nuevo profesor ya existe, entonces le asignamos su numemp a la tabla profcurricula
                if (ex==null || ex.getMessage()==null)
                    throw new SQLException (""+ex);
                else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("PROFESORES_UK")){
                    //Extraemos el numemp del profesor que ya existe
                    numemp = getData("SELECT numemp FROM profesores WHERE rfc='"+txtRfcProf.trim().toUpperCase()+"' AND nombre='"+txtNombreProf.trim().toUpperCase()+"' AND apepat='"+txtPrimerApeProf.trim().toUpperCase()+"' AND apemat='"+txtSegundoApeProf.trim().toUpperCase()+"'");
                    //Si no está en escuelaprofesor, en ese idcct lo metemos
                    rs = stm.executeQuery("SELECT numemp FROM escuelaprofesor WHERE numemp="+numemp+" AND idcct="+tblPersonal_selRow.get("idcct"));
                    if(!rs.next())
                        stm.execute("INSERT INTO escuelaprofesor (numemp, idcct, estatusep, usuario, fecha, hora) "
                                    + "VALUES ("+numemp+", "+tblPersonal_selRow.get("idcct")+",'A', "
                                        + "'"+txtUsuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))");
                    //Actualizamos el grado grupo con el profesor que el usuario indicó
                    stm.execute("UPDATE profcurricula SET "
                                    + "numemp="+numemp+", cveturno='"+tblPersonal_selRow.get("cveturno")+"', cvetipmat='CBA', cvemat='001', "
                                    + "cveplan="+ tblPersonal_selRow.get("cveplan")+", cveprograma=null, usuario='"+txtUsuario.trim().toUpperCase()+"', "
                                    + "fecha=date(current), hora=extend(current, hour to minute) "
                                + "WHERE idcct="+tblPersonal_selRow.get("idcct")+" AND cicescini="+califCicEscIn+" AND grado="+tblPersonal_selRow.get("grado")+" AND grupo='"+tblPersonal_selRow.get("grupo")+"'");
                }else
                    throw new SQLException (ex);
            }
        }else{
            numemp = getData("SELECT numemp FROM profesores WHERE rfc='"+txtRfcProf.trim().toUpperCase()+"' AND nombre='"+txtNombreProf.trim().toUpperCase()+"' AND apepat='"+txtPrimerApeProf.trim().toUpperCase()+"' AND apemat='"+txtSegundoApeProf.trim().toUpperCase()+"'");
            
            if (numemp.equals("")){                                         //Si no hay asignado un grado-grupo y tampoco existe el profesor dado de alta, insertamos todo
                numemp = getData("SELECT MAX(numemp)+1 FROM profesores");
                stm.execute("INSERT INTO profesores (numemp, rfc, nombre, apepat, apemat, estatus, usuario, fecha, hora) "
                        + "VALUES ("+numemp+", '"+txtRfcProf.trim().toUpperCase()+"', '"+txtNombreProf.trim().toUpperCase()+"', "
                            + "'"+txtPrimerApeProf.trim().toUpperCase()+"', '"+txtSegundoApeProf.trim().toUpperCase()+"', 'A', "
                            + "'"+txtUsuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))");
            }//Si no hay grado-grupo pero ya existe un profesor, le hacemos el amarre
            
            rs = stm.executeQuery("SELECT numemp FROM escuelaprofesor WHERE numemp="+numemp+" AND idcct="+tblPersonal_selRow.get("idcct"));
            if(!rs.next())
                stm.execute("INSERT INTO escuelaprofesor (numemp, idcct, estatusep, usuario, fecha, hora) "
                            + "VALUES ("+numemp+", "+tblPersonal_selRow.get("idcct")+",'A', '"+txtUsuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute))");
            
            try {
                stm.execute("INSERT INTO profcurricula (numemp, idcct, cveturno, cicescini, cicescfin, grado, grupo, cvetipmat, cvemat, cveplan, cveprograma, usuario, fecha, hora) "
                        + "VALUES ("+numemp+", "+tblPersonal_selRow.get("idcct")+", '"+tblPersonal_selRow.get("cveturno")+"', "+califCicEscIn+", "
                            + (Integer.parseInt(califCicEscIn)+1)+", "+tblPersonal_selRow.get("grado")+", '"+tblPersonal_selRow.get("grupo")+"', 'CBA', '001', "
                            + tblPersonal_selRow.get("cveplan")+", null, '"+txtUsuario.trim().toUpperCase()+"', "
                            + "date(current), extend(current, hour to minute))");
            }catch (SQLException ex){
                if (ex==null || ex.getMessage()==null)
                    throw new SQLException (""+ex);
                else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("PROFCURRICULA_PK")){
                    stm.execute("UPDATE profcurricula SET "
                                    + "numemp="+numemp+", cveturno='"+tblPersonal_selRow.get("cveturno")+"', cvetipmat='CBA', cvemat='001', "
                                    + "cveplan="+ tblPersonal_selRow.get("cveplan")+", cveprograma=null, usuario='"+txtUsuario.trim().toUpperCase()+"', "
                                    + "fecha=date(current), hora=extend(current, hour to minute) "
                                + "WHERE idcct="+tblPersonal_selRow.get("idcct")+" AND cicescini="+califCicEscIn+" AND grado="+tblPersonal_selRow.get("grado")+" AND grupo='"+tblPersonal_selRow.get("grupo")+"'");
                }else
                    throw new SQLException (ex);
            }
        }
    }
    
//************************************************************************************************************
//****************************** MÉTODOS USADOS EN EL FORMULARIO Desoficializar ******************************
//************************************************************************************************************
    public ArrayList<Map> getAlumnosParaDesoficializar (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo, String oficializacion, String bimeval) throws SQLException
    {
        String cveoficializacion = this.getData("SELECT cveoficializacion FROM catoficializacion WHERE oficializacion='"+oficializacion+"'");
        rs = stm.executeQuery("SELECT NVL((SELECT 't' FROM desoficializacion WHERE idalu=g.idalu AND cicescini=g.cicescini AND idcct=g.idcct AND grado=g.grado AND grupo=g.grupo AND cveoficializacion="+cveoficializacion+"),'f') AS selec, "
                                + "a.idalu, a.curp, a.apepat, a.apemat, a.nombre, "
                                + "(trim(nvl(a.apepat,'')) || '/' || trim(nvl(a.apemat,'')) || \"*\" || trim(nvl(a.nombre,''))) AS nom_tot, "
                                + "g.grado, g.grupo "
                            + "FROM alumnogrado g, alumno a "
                            + "WHERE g.idalu=a.idalu AND g.estatusgrado<>'BD' AND g.estatusgrado<>'RG' "
                                + "AND g.cicescini="+tblPrincipal_cicescini+" AND g.idcct= "+tblPrincipal_idcct+" AND g.grado="+tblPrincipal_grado+" "
                                + "AND g.grupo = '"+tblPrincipal_grupo+"'"
                            + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public void desoficXAlumno (boolean puedeDesoficializar, boolean puedeQuitarFolio, String tblPrincipal_cicescini, String tblPrincipal_cveplan, String tblPrincipal_idcct, 
            String grado, String grupo, ArrayList<Map> tblAlumnos, String txtUsuario, String oficializacion, String bimeval, boolean chkTodos) throws SQLException, SICEEO_Excepcion
    {
        String cveoficializacion, cveoficializacionUtlBimEv, ultimoBimEval, comipems;
        int tamIdalus = tblAlumnos.size(), total_desof=0, ya_desofidos=0;
        
        if (bimeval.equals("3") && !puedeQuitarFolio) // Para usuarios que tienen modulo = 28, no pueden desoficializar 3ra evaluacion
            throw new SICEEO_Excepcion (0, "SIN_PERMISO_DESOF_ULTIMO");
        
        if ((oficializacion.contains("CALIFS BIM") && !bimeval.equals("3")) || (oficializacion.contains("EVALUACION") && !bimeval.equals("3")))  //Verificamos si ya tiene desoficializado el 3er bimestre o la 3ra evaluación, pues si no lo está entonces obligamos a que lo hagan
        {
            cveoficializacionUtlBimEv = (oficializacion.contains("CALIFS BIM"))?"5":"10";
            ultimoBimEval = (oficializacion.contains("CALIFS BIM"))?"3":"3";
         //   if ( !"".equals(this.getData("SELECT idcct FROM Oficializacion WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+tblPrincipal_cicescini+" AND cveoficializacion="+cveoficializacionUtlBimEv)) )
                if ( "t".equals(this.getData("SELECT ofic_caleval_b"+ultimoBimEval+" FROM escuelagpos WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+tblPrincipal_cicescini+" AND grado="+grado+" AND grupo='"+grupo+"'")) )
                    if ( "0".equals(this.getData("SELECT count(idalu) FROM desoficializacion "
                              + "WHERE idcct="+tblPrincipal_idcct+" AND cicescini="+tblPrincipal_cicescini+" AND idcct="+tblPrincipal_idcct+" AND grado="+grado+" "
                                    + "AND grupo='"+grupo+"' AND cveoficializacion="+cveoficializacionUtlBimEv)))
                    //if (!puedeDesoficializar)
                        throw new SICEEO_Excepcion (0, ((oficializacion.contains("CALIFS BIM"))?"DESOFIC_ULTIMO_BIMESTRE":"DESOFIC_ULTIMA_EVAL"));
        }
        
        cveoficializacion = this.getData("SELECT cveoficializacion FROM catoficializacion WHERE oficializacion='"+oficializacion+"'");
        
        for (int i=0; i<tamIdalus; i++)
            if (tblAlumnos.get(i).get("selec").equals("true")){
                //comipems = this.getData("SELECT idalu FROM comipems WHERE cicescini="+tblPrincipal_cicescini+" AND idalu="+tblAlumnos.get(i).get("idalu")+"");
                comipems = this.getData("SELECT (CASE WHEN fi.folionum>0 THEN 't' ELSE 'f' END) FROM comipems c LEFT JOIN folios_impre fi" 
                                        + " ON c.cicescini = fi.cicescini " 
                                        + " AND c.idalu = fi.idalu " 
                                        + " WHERE c.cicescini= "+tblPrincipal_cicescini+" AND c.idalu="+tblAlumnos.get(i).get("idalu")+""); 
                
                if((""+comipems).equals("f") || (""+comipems).equals("null") || (""+comipems).equals("") || (""+comipems).equals("NULL"))
                    try{                    

                        stm.execute("INSERT INTO desoficializacion (idcct, cicescini, grado, grupo, idalu, cveoficializacion, usuario, fecha)"
                                    + "VALUES ("+tblPrincipal_idcct+", "+tblPrincipal_cicescini+", "+grado+", '"+grupo+"',"+tblAlumnos.get(i).get("idalu")+", "+cveoficializacion+",'"+txtUsuario+"', extend(current, YEAR TO SECOND) )");

                        //----------------- Cancelamos folios de certificado -----------------
                        if (tblPrincipal_cveplan.equals("1") && grado.equals("6") || tblPrincipal_cveplan.equals("2") && grado.equals("3"))
                            cancelarFolioCertif (tblPrincipal_cicescini, tblPrincipal_idcct, grado, grupo, ""+tblAlumnos.get(i).get("idalu"), txtUsuario);

                        //----------------- Cancelamos folios de boleta -----------------
                        /*stm.execute(
                              "INSTERT INTO fol_re_elec_cance (idfolio,idalu,cveplan,foliolet,folionum,idcct,grado,cicescini,usuario,fecha,hora,usuario_cance,fecha_cance,hora_cance,estatus,nivelmod)"
                            + "SELECT idfolio,idalu,cveplan,foliolet,folionum,idcct,grado,cicescini,usuario,fecha,hora,'"+txtUsuario+"', date(current), extend(current, hour to minute), estatus,nivelmod "
                            + "FROM fol_re_elec "
                            + "WHERE cicescini="+tblPrincipal_cicescini+" AND idalu="+tblAlumnos.get(i).get("idalu"));

                            stm.execute(
                                  "DELETE FROM fol_re_elec "
                                + "WHERE cicescinilib="+tblPrincipal_cicescini+" AND idalu="+tblAlumnos.get(i).get("idalu"));*/
                        total_desof +=1;
                    } catch(SQLException ex){
                        if (ex==null || ex.getMessage()==null)
                            throw new SQLException (""+ex);
                        else if (ex.getMessage().toUpperCase().contains("UNIQUE") && ex.getMessage().toUpperCase().contains("DESOFICIALIZACION_PK")){
                            rs = stm.executeQuery("SELECT do.idalu, a.curp, (TRIM(NVL(a.apepat,'')) || '/' || TRIM(NVL(a.apemat,'')) || '*' || TRIM(NVL(a.nombre,''))) AS nom_tot, "
                                                + "e.cct, do.grado, do.grupo, do.cveoficializacion "
                                                + "FROM desoficializacion do, alumno a, escuela e "
                                                + "WHERE do.idalu=a.idalu AND do.idcct=e.idcct "
                                                    + "AND do.idcct="+tblPrincipal_idcct+" AND do.cicescini="+tblPrincipal_cicescini+" "
                                                    + "AND do.idalu="+tblAlumnos.get(i).get("idalu")+" AND do.cveoficializacion="+cveoficializacion);
                            rs.next();
                            if (!rs.getString("grado").equals(grado) || !rs.getString("grupo").equals(grupo))
                                throw new SICEEO_Excepcion (0,"DEBE_OFIC_ALUMNO","idalu: "+rs.getString("idalu")+", CURP: "+rs.getString("curp")+",\nnombre: "+rs.getString("nom_tot")+", CCT: "+rs.getString("cct")+",\ngrado: "+rs.getString("grado")+", grupo: "+rs.getString("grupo"));
                            else {
                                ya_desofidos +=1;
                            }
                        }else
                            throw new SQLException (ex);
                    }
                else throw new SICEEO_Excepcion (0, "COMIPEMS","idalu: "+tblAlumnos.get(i).get("idalu"));
            }
        if(total_desof==tamIdalus || ya_desofidos==tamIdalus)
           stm.execute("UPDATE escuelagpos SET ofic_caleval_b"+bimeval+"='f' WHERE cicescini="+tblPrincipal_cicescini+" AND idcct="+tblPrincipal_idcct+" AND grado="+grado+" AND grupo='"+grupo+"'");
    }
    
    private void cancelarFolioCertif (String cicescini, String idcct, String grado, String grupo, String idalu, String txtUsuario) throws SQLException
    {
        stm.execute(
                "INSERT INTO firma_elec_cance (idalu, cicescini, cicescinilib, idfirmante, cadenaoriginal, fechatimbradoieepo, sellodigitalieepo, "
                  + "fechatimbradosep, sellodigitalsep, folionum_cer, estatus, foliodigital, usuario_cance, fecha_cance, hora_cance, "
                  + "sellodec, textoenxml, estatus_firma, carpeta, folionum_cersep) "
              + "SELECT idalu, cicescini, cicescinilib, idfirmante, cadenaoriginal, fechatimbradoieepo, sellodigitalieepo, replace(fechatimbradosep,'T',' '), "
                  + "sellodigitalsep, folionum_cer, estatus, foliodigital, '"+txtUsuario+"', date(current), extend(current, hour to minute), "
                  + "sellodec, textoenxml, estatus_firma, carpeta, folionum_cersep "
                // + "sellodigitalsep, folionum_cer, estatus_x, foliodigital, '"+txtUsuario+"', date(current), extend(current, hour to minute) "
              + "FROM firma_elec "
              + "WHERE cicescinilib="+cicescini+" AND idalu="+idalu);
          stm.execute(
                "DELETE FROM firma_elec "
              + "WHERE cicescinilib="+cicescini+" AND idalu="+idalu);

          stm.execute(
                "INSERT INTO folios_cance (idfolio, foliolet, folionum, idalu, idcct, cvezona, cveplan, cct, cveturno, grado, grupo, curp, nombre, "
                    + "apepat, apemat, cicescini, cicescinilib, usukgenero, horkgenero, feckgenero, alos, delmes, promediogral, quienfirma, tratamiento, "
                    + "usuario, fecha, hora, cveunidad, crip, cveentidad )"
              + "SELECT fi.idfolio, fi.foliolet, fi.folionum, fi.idalu, fi.idcct, fi.cvezona, fi.cveplan, fi.cct, fi.cveturno, fi.grado, fi.grupo, fi.curp, fi.nombre, "
                    + " fi.apepat, fi.apemat, fi.cicescini, fi.cicescinilib, fi.usuario, fi.hora, fi.fecha, fi.alos, fi.delmes, fi.promediogral, fi.quienfirma, fi.tratamiento, "
                    + "'"+txtUsuario.trim().toUpperCase()+"', date(current), extend(current, hour to minute), fi.cveunidad, fi.crip, cveentidad "
              + "FROM folios_impre fi "
              + "WHERE fi.cicescinilib="+cicescini+" AND fi.idcct="+idcct+" AND fi.grado="+grado+" AND fi.grupo='"+grupo+"' AND fi.idalu="+idalu + " AND fi.estatus='AC'");
          stm.execute(
                "DELETE FROM folios_impre "
              + "WHERE cicescinilib="+cicescini+" AND idcct="+idcct+" AND grado="+grado+" AND grupo='"+grupo+"' AND idalu="+idalu + " AND estatus='AC' ");
    }
    
//************************************************************************************************************
//****************************** MÉTODOS USADOS EN EL FORMULARIO Complementaria ******************************
//************************************************************************************************************
    public ArrayList<Map> getAlumnosParaComplementaria (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo) throws SQLException
    {
        rs = stm.executeQuery(
                  "SELECT 'f' selec, "
                    + "a.idalu, a.curp, a.apepat, a.apemat, a.nombre, "
                    + "(trim(nvl(a.apepat,'')) || '/' || trim(nvl(a.apemat,'')) || \"*\" || trim(nvl(a.nombre,''))) AS nom_tot, "
                    + "g.grado, g.grupo "
                + "FROM alumnogrado g, alumno a "
                + "WHERE g.idalu=a.idalu AND g.estatusgrado<>'BD' "
                    + "AND g.cicescini="+tblPrincipal_cicescini+" AND g.idcct= "+tblPrincipal_idcct+" AND g.grado="+tblPrincipal_grado+" "
                    + "AND g.grupo = '"+tblPrincipal_grupo+"'"
                + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<Map> getAlumnosParaMesCompl (String tblPrincipal_cicescini, String tblPrincipal_idcct, String tblPrincipal_grado, 
            String tblPrincipal_grupo) throws SQLException
    {
        rs = stm.executeQuery(
                  "SELECT 'f' selec, "
                    + "a.idalu, a.curp, a.apepat, a.apemat, a.nombre, "
                    + "(trim(nvl(a.apepat,'')) || '/' || trim(nvl(a.apemat,'')) || \"*\" || trim(nvl(a.nombre,''))) AS nom_tot, "
                    + "g.grado, g.grupo "
                + "FROM alumnogrado g, alumno a, folios_impre fi "
                + "WHERE g.idalu=a.idalu AND g.idalu=fi.idalu AND g.cicescini=fi.cicescinilib "
                    + "AND fi.promediogral>6 AND fi.estatus='AC' AND g.estatusgrado<>'BD' "
                    + "AND g.cicescini="+tblPrincipal_cicescini+" AND g.idcct= "+tblPrincipal_idcct+" AND g.grado="+tblPrincipal_grado+" "
                    + "AND g.grupo = '"+tblPrincipal_grupo+"'"
                + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public ArrayList<Map> getAlumnosConExtraordinarioAprobado (String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        rs = stm.executeQuery(
                "SELECT 'f' AS selec, ag.idalu, "
                    + "(SELECT mes  "
                    + "FROM ( "
                            + "SELECT FIRST 1 mes "
                            + "FROM exm_ext_ordi  "
                            + "WHERE idalu=ag.idalu AND promedio>=6 AND (anio=(ag.cicescini+1) AND mes IN ('AGOSTO', 'SEPTIEMBRE', 'OCTUBRE') " //Quite septiembre
                                        + "OR anio=(ag.cicescini+2) AND mes IN ('ENERO')) "
                            + "ORDER BY anio desc, substr(mes,3,1) desc "
                        + ") "
                    + ")as mes, "
                    + "ag.promediogral, a.curp, "
                    + "(trim(nvl(a.apepat,'')) || '/' || trim(nvl(a.apemat,'')) || '*' || trim(nvl(a.nombre,''))) AS nom_tot "
                    //+ ",a.nombre, a.apepat, a.apemat "
                    //+ ",ag.grado, ag.idcct, ag.grupo, ag.promedio "
                + "FROM alumnogrado ag, alumno a "
                + "WHERE ag.idalu=a.idalu "
                    + "AND ag.idalu IN (SELECT idalu  "
                                    + "FROM exm_ext_ordi  "
                                    + "WHERE idcct_apl=ag.idcct AND promedio>=6 AND (anio=(ag.cicescini+1) AND mes IN ('AGOSTO','SEPTIEMBRE','OCTUBRE') "  //Quite septiembre
                                        + "OR anio=(ag.cicescini+2) AND mes IN ('ENERO')) ) "  //AND (anio=(ag.cicescini+1) AND mes IN ('JULIO', 'AGOSTO','SEPTIEMBRE') OR anio=(ag.cicescini+2) AND mes IN ('ENERO'))
                    + "AND (ag.idalu IN (SELECT idalu "
                                      + "FROM folios_impre "
                                      + "WHERE cicescinilib=ag.cicescini AND grado=ag.grado AND estatus='AC' AND alos LIKE '%ocho días del mes de julio%') " + //and idcct=ag.idcct and grupo=ag.grupo 
                            "OR ag.idalu NOT IN (SELECT idalu FROM folios_impre WHERE cicescinilib=ag.cicescini AND estatus='AC' )) "
                    + "AND ag.cicescini="+cicescini+" AND ag.idcct="+idcct+" AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' AND ag.estatusgrado<>'BD' "
                    + "AND ag.promediogral>=6"
                + "ORDER BY a.apepat, a.apemat, a.nombre, a.curp");
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public String getIdalusConExtraordinarioAprobado (String cicescini, String idcct, String grado, String grupo, String mes, String idalusPorFiltrar) throws SQLException
    {
        String idalus="", sqryAnio="";
        sqryAnio = mes.equals("ENERO")?"fi.cicescinilib+2":"fi.cicescinilib+1";
        rs = stm.executeQuery(
                "SELECT ag.idalu "
                + "FROM alumnogrado ag, folios_impre fi "
                + "WHERE  ag.idalu=fi.idalu AND ag.cicescini=fi.cicescini "
                        + "AND ag.idalu IN (SELECT idalu "
                                        + "FROM exm_ext_ordi "
                                        + "WHERE idalu=ag.idalu AND promedio>=6 AND (anio=("+sqryAnio+") "
                                             + "AND '"+mes+"'= (SELECT mes "
                                                           + "FROM( "
                                                                 + "SELECT FIRST 1 mes "
                                                                 + "FROM exm_ext_ordi  "
                                                                 + "WHERE idalu=ag.idalu AND promedio>=6 AND (anio=(fi.cicescinilib+1) AND mes IN ('AGOSTO','SEPTIEMBRE','OCTUBRE') "  //añadi OCTUBRE 
                                                                     + "OR anio=(fi.cicescinilib+2) AND mes IN ('ENERO')) "
                                                                 + "ORDER BY anio desc, substr(mes,3,1) desc "
                                                             + ") "
                                                         + ") "
                                         + ")) "
                    //+ "AND ag.idalu IN (SELECT idalu FROM folios_impre WHERE idalu=ag.idalu AND cicescinilib=ag.cicescini AND grado=ag.grado AND alos NOT like '%julio%' ) "
                    +(idalusPorFiltrar.equals("null") || idalusPorFiltrar.equals("")?"":"AND fi.idalu IN ("+idalusPorFiltrar+") ")
                    + "AND fi.grado=3 AND fi.estatus='AC' AND fi.alos NOT LIKE '% ocho días del mes de julio%' "
                    + "AND ag.idcct="+idcct+" AND ag.grado="+grado+" AND ag.grupo='"+grupo+"' " //ag.cicescini="+cicescini+" 
                    + "AND ag.estatusgrado<>'BD' AND ag.promediogral>=6 "
                    + "AND fi.cicescinilib="+cicescini);
        while (rs.next()){
            idalus += ((idalus.equals("")?"":", ")+rs.getString("idalu"));
        }
        return idalus;
    }

//************************************************************************************************************
//******************************** MÉTODOS USADOS EN EL FORMULARIO RevisaGpo *********************************
//************************************************************************************************************
    public ArrayList<Map> revisaGpo (String estatusAlu, String cicescini, String idcct, String grado, String grupo) throws SQLException
    {
        ArrayList<Map> QRevisaGpo = new ArrayList<Map>();
        Map fila;
        rs = stm.executeQuery(
                  "SELECT a.IDALU, a.curp, "
                    + "(trim(TRAILING ' ' FROM NVL(a.apepat,'')) || '/' || TRIM(TRAILING ' ' FROM NVL(a.apemat,'')) || '*' || TRIM(TRAILING ' ' FROM NVL(a.nombre,''))) AS nom_tot, "
                    + "g.estatusgrado, "
                    + "(SELECT count(*) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 6 AND cveplan=1 AND (estatusgrado='RE' OR estatusgrado='C'  )) AS en6to, "
                    + "(SELECT count(*) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 1 AND cveplan=2 AND (estatusgrado='RE' )) AS en1ro, "
                    + "(SELECT count(*) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 2 AND cveplan=2 AND (estatusgrado='RE' )) AS en2do "
                + "FROM alumnogrado g, alumno a "
                + "WHERE g.idalu=a.idalu "
                    + "AND g.estatusGrado<>'BD' AND (a.estatusalu ='I' OR a.estatusalu='"+estatusAlu+"') AND "
                    + "g.cicescini="+cicescini+" AND g.idcct="+idcct+" AND g.Grado="+grado+" AND g.Grupo='"+grupo+"' "
                + "ORDER BY a.curp");
        
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCols=rsmd.getColumnCount();
        String dato, columna;
        while (rs.next()){
            if (rs.getInt("en6to")>1 || rs.getInt("en1ro")>1 || rs.getInt("en2do")>1)
            {
                fila = new HashMap();
                for (int posCol=1; posCol<=numCols; posCol++){                      //Para cada columna
                    columna = rsmd.getColumnName(posCol).toLowerCase();
                    dato = rs.getString(posCol);
                    if (dato==null)                    //Convierte el null a cadena vacía
                        dato="";
                    fila.put(columna, dato);
                }
                QRevisaGpo.add(fila);
            }
            
        }
        
        return QRevisaGpo;
    }
    
//************************************************************************************************************
//********************************** MÉTODOS USADOS EN EL FORMULARIO Avisos **********************************
//************************************************************************************************************
    public ArrayList<Map> getAvisos () throws SQLException
    {
        rs = stm.executeQuery("SELECT idaviso, activo, mensaje, icono, fechainsert FROM avisossisweb WHERE sistema='SICEEO'");
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public void eliminarAviso (String idaviso) throws SQLException
    {
        stm.execute("DELETE FROM avisossisweb WHERE idaviso="+idaviso);
    }
    
    public void setMensajeActivoInactivo (String idaviso, String caso) throws SQLException
    {
        stm.execute("UPDATE avisossisweb SET activo='"+caso+"' WHERE idaviso="+idaviso);
    }
    
    public void reemplazarMensaje (String idaviso, String aviso) throws SQLException
    {
        stm.execute("UPDATE avisossisweb SET mensaje='"+aviso+"' WHERE idaviso="+idaviso);
    }
    
    public void insertarMensaje (String aviso) throws SQLException
    {
        int idaviso = getNewNumTableId("idaviso", "avisossisweb");
        stm.execute("INSERT INTO avisossisweb (idaviso, sistema, activo, mensaje, icono) VALUES ("+idaviso+",'SICEEO','f','"+aviso+"','')");
    }
    
//************************************************************************************************************
//********************************** MÉTODOS USADOS EN EL MÓDULO Exalumnos ***********************************
//************************************************************************************************************
    public ArrayList<Map> buscarExalumno (String cicescini, String condicion) throws SQLException
    {
        rs = stm.executeQuery("SELECT a.idalu,(TRIM(NVL(a.apepat,'')) || '/' || TRIM(NVL(a.apemat,'')) || \"*\" || TRIM(NVL(a.nombre,''))) AS nom_tot, "
                                + "a.curp, a.nombre, a.apepat, a.apemat "
                            + "FROM alumno a, alumnogrado ag "
                            + "WHERE a.idalu=ag.idalu AND cveplan=2 AND grado=3 "
                                + "AND ag.cicescini="+cicescini+" AND "+condicion);
       return qryToArrlmap(rs, null, true, 2);
    }
    
    public boolean canEditExmExtExalumno (String cicesciniEstud, String idalu, String txtUsuario, SICEEO_DataModule dm) throws SQLException, SICEEO_Excepcion, ParseException
    {
        if ( !cicesciniEstud.equals(getData ("SELECT  MAX(cicescini) FROM alumnogrado WHERE idalu="+idalu+" AND estatusgrado<>'BD'")) )
            return false;
        rs = stm.executeQuery(
                  "SELECT promediogral, promedioeb, usuario, TO_CHAR (fecha, '%d/%m/%Y') AS fecha, TO_CHAR (date(current), '%d/%m/%Y') AS fechaActual "
                + "FROM alumnogrado "
                + "WHERE idalu="+idalu+" AND cicescini="+cicesciniEstud+" AND cveplan=2 AND grado=3 AND estatusgrado<>'BD'");
        if (rs.next()){
            if (cicesciniEstud.equals("2012")){
                if (rs.getString("promedioeb") == null || rs.getFloat("promedioeb")<6)
                    return true;
            }else if ( rs.getString("promediogral") == null || rs.getFloat("promediogral")<6 )
                return true;
            if (rs.getString("usuario")!=null && rs.getString("fecha")!=null && txtUsuario.equals(rs.getString("usuario").trim().toUpperCase()) && dm.getDifFechas(rs.getString("fecha"), rs.getString("fechaActual"), 2) < 5)
                return true;
        }
        
        return false;
    }
    public int alumnoSolicitudCerti (String cicescini, String idalu) throws SQLException, SICEEO_Excepcion, ParseException
    {
        rs = stm.executeQuery(
            "SELECT idalu from alumnoSolicitud_certi WHERE cveplan=1 AND cicescini = " + cicescini + " AND idalu = " + idalu);
        while (rs.next())
            return 1;                        
        return 0;
    }
    
    public Map fechaRegularizacion(String idalu,String cicescini, SICEEO_DataModule dm) throws SQLException, SICEEO_Excepcion, ParseException {
        Map fecha = new HashMap();
        Integer anioRegul = 0, mesRegul = 0;
        rs = stm.executeQuery( "SELECT dia,trim(mes) as mes,anio "
                + " FROM exm_ext_ordi "
                + " WHERE idalu = "+idalu+" AND anio = (SELECT max(anio) FROM exm_ext_ordi WHERE idalu = "+idalu+")"
                + " AND (SELECT promediogral FROM alumnogrado WHERE idalu = "+idalu+" AND cicescini="+cicescini+ ") >=6 "
                + " GROUP BY 1,2,3" );
        
        while (rs.next()){
            anioRegul = rs.getInt("anio");
            if(dm.getNumMes(""+rs.getString("mes")) > mesRegul){
                mesRegul = dm.getNumMes(""+rs.getString("mes"));
            }
        }
        fecha.put("anio", anioRegul);
        fecha.put("mes", mesRegul);        
        return fecha;
    }
       
    public boolean canFoliarYFirmarExalumno (String cicesciniEstud, String idalu, String txtUsuario, SICEEO_DataModule dm) throws SQLException, SICEEO_Excepcion, ParseException
    {
        String sqryPromedio = (cicesciniEstud.equals("2012"))?"promedioeb":"promediogral";
        
        rs = stm.executeQuery(
                  "SELECT usuario, TO_CHAR (fecha, '%d/%m/%Y') AS fecha, TO_CHAR (date(current), '%d/%m/%Y') AS fechaActual "
                + "FROM alumnogrado "
                + "WHERE idalu="+idalu+" AND cicescini="+cicesciniEstud+" AND cveplan=2 AND grado=3 AND "+sqryPromedio+">=6 AND estatusgrado<>'BD'");
        
        if (rs.next()){
            if (rs.getString("usuario")!=null && rs.getString("fecha")!=null && txtUsuario.equals(rs.getString("usuario").trim().toUpperCase()) && dm.getDifFechas(rs.getString("fecha"), rs.getString("fechaActual"), 2) < 5)
                return true;
        }
        
        return false;
    }
        
    public int tiempoTranscurridoDeFirmado (String cicescinilib, String idalu, String txtUsuario, SICEEO_DataModule dm) throws SQLException, SICEEO_Excepcion, ParseException
    {
        rs = stm.executeQuery(
                  "SELECT fi.usuario, TO_CHAR (fie.fechatimbradoieepo, '%d/%m/%Y') AS fecha, TO_CHAR (date(current), '%d/%m/%Y') AS fechaActual "
                + "FROM folios_impre fi, firma_elec fie "
                + "WHERE fi.idalu=fie.idalu AND fi.cicescinilib=fie.cicescinilib AND fi.folionum=fie.folionum_cer "
                          + "AND fi.idalu="+idalu+" AND fi.cicescinilib="+cicescinilib+" AND fi.estatus='AC' ");
        
        if (rs.next()){
            if (rs.getString("usuario")!=null && rs.getString("fecha")!=null && txtUsuario.equals(rs.getString("usuario").trim().toUpperCase()) )
                return dm.toInt(dm.getDifFechas(rs.getString("fecha"), rs.getString("fechaActual"), 2));
        }
        
        return -1;
    }
    
    public Map estadisticaDePermisosExmExt (String idalu, String cicescini) throws SQLException
    {
        rs = stm.executeQuery(
                "SELECT "
                    + "MAX(cicescini) AS maxcicescini, "
                    + "(SELECT COUNT(distinct(grado)) FROM alumnogrado WHERE idalu=ag.idalu AND cveplan=2 AND estatusgrado <>'BD') AS numgradosestud, "                    
                    + "(SELECT estatusgrado  FROM alumnogrado WHERE idalu=ag.idalu AND cicescini=(SELECT MAX(cicescini) FROM alumnogrado WHERE idalu=ag.idalu AND cveplan=2 AND grado=3)) AS maxestatusgrado, "
                    + "(SELECT idcct FROM alumnogrado WHERE idalu=ag.idalu AND cicescini=(SELECT MAX(cicescini) FROM alumnogrado WHERE idalu=ag.idalu AND cveplan=2 AND grado=3)) AS maxidcct, "
                    + "NVL((SELECT idalu FROM alumnosolicitud_certi WHERE cicescini="+cicescini+" AND idalu = "+idalu+"),0) AS alusolicitud "
                + "FROM alumnogrado ag "
                + "WHERE idalu="+idalu+" "
                + "GROUP BY 2, 3, 4");
        
        return qryToMap(rs, null, true, 2);
    }
    
    public ArrayList<Map> getEstosExalumnosCompl (String idalus) throws SQLException
    {
        rs = stm.executeQuery(
                "SELECT 't' selec, "
                    + "fi.idalu, fi.curp, fi.apepat, fi.apemat, fi.nombre, "
                    + "(trim(nvl(fi.apepat,'')) || '/' || trim(nvl(fi.apemat,'')) || \"*\" || trim(nvl(fi.nombre,''))) AS nom_tot, "
                    + "fi.grado, fi.grupo, fi.cicescini "
                + "FROM folios_impre fi "
                + "WHERE fi.promediogral>6 AND fi.estatus='AC' AND idalu IN ("+idalus+") AND cveplan=2 "
                + "ORDER BY cicescini, grado, grupo, apepat, apemat, nombre, curp");
        
        return qryToArrlmap(rs, null, true, 2);
    }
    
    public void actualizarPromediogralDeFolio (String cicescinilib, String idalu) throws SQLException, SICEEO_Excepcion
    {
        String promediogral="null";
        rs = stm.executeQuery(
              "SELECT ag.promediogral "
            + "FROM alumnogrado ag, alumno a, folios_impre fi "
            + "WHERE ag.idalu=a.idalu AND ag.idalu=fi.idalu AND ag.cicescini=fi.cicescini AND ag.cveplan=2 AND ag.grado=3 "
            + "AND fi.nombre=a.nombre AND fi.apepat=a.apepat AND fi.apemat=a.apemat AND fi.curp=a.curp AND ag.idcct=fi.idcct "
                + "AND ag.promediogral>=6 AND fi.promediogral IS NULL AND ag.estatusgrado<>'BD' AND fi.alos NOT LIKE '%mes de julio%'"
            + "AND fi.cicescinilib="+cicescinilib+" AND fi.idalu="+idalu + " AND fi.estatus='AC' ");
        if (rs.next()){
            promediogral = rs.getString("promediogral");
            stm.execute ("UPDATE folios_impre SET promediogral="+promediogral+" WHERE cicescinilib="+cicescinilib+" AND idalu="+idalu + " AND estatus='AC'");
        }else
            throw new SICEEO_Excepcion (0,"PROMFOL_NO_ACTUALIZADO");
    }
    
    
}
