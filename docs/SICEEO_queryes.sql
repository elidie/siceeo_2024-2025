-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
select substr(a.curp,11,1), count(*)
from fol_re_elec re, alumno a, escuela e
where re.idalu=a.idalu and re.idcct=e.idcct
and re.cicescini=2016 and re.cveplan=3 and re.grado=1
and e.modalidad='DCC' 
group by 1 

select substr(a.curp,11,1), count(*)
from fol_re_elec re, alumno a, escuela e
where re.idalu=a.idalu and re.idcct=e.idcct
and re.cicescini=2016 and re.cveplan=3 and re.grado=1
--and e.modalidad='DCC' 
and e.fecalta>=TO_DATE('2016/08/22','%Y/%m/%d')
group by 1

select substr(a.curp,11,1), count(*)
from alumnogrado re, alumno a, escuela e
where re.idalu=a.idalu and re.idcct=e.idcct
and re.cicescini=2016 and re.cveplan=3 and re.grado=1
and re.estatusgrado<>'BD'
and e.modalidad='DCC' 
and e.fecalta>=TO_DATE('2016/08/22','%Y/%m/%d')
group by 1


-- ========== VERSIÓN ==========
SELECT cicescini, descicesc, estatus, version_siceeo, maxsesserv_siceeo
FROM cicloescolar WHERE estatus='A' and cicescini=2016

_UPDATE  CicloEscolar SET version_siceeo='13.1.2' WHERE estatus='A'

-- ========== AVISOS ==========
select * from avisossisweb
_insert into avisossisweb (idaviso, sistema, activo, mensaje, icono) values (2,"SICEEO",'f',
'<br><h1>¡ATENTO AVISO!</h1><br><h3>DIRECTOR(A) DE TELESECUNDARIA</h3><br><h5>Es necesario que actualices la situación de tu Infraestructura Tecnológica (red EDUSAT) mediante el Sistema de Infraestructura Educativa (SIE).<h5><br>','icono')

-- ========== PERMISOS ==========
select * from siceeo_permisos;
select * from siceeo_objetospermiso;
select * from siceeo_configpermiso;

-- Para ver una configuración
SELECT p.idpermiso, p.tipousuario, p.cveconfigpermiso, op.idobjeto, cp.permiso, cp.permisodefault, op.formulario, op.componente
FROM siceeo_permisos p, siceeo_configpermiso cp, siceeo_objetospermiso op 
WHERE p.cveconfigpermiso=cp.cveconfigpermiso AND op.idobjeto=cp.idobjeto 
   AND tipousuario='ADMIN'
   --AND tipousuario='CCT 59'
   --AND op.formulario='PRINCIPAL' 
   --AND componente='btnReportes'
   --AND cp.permiso='t'
ORDER BY tipousuario,orden

-- Para insertar un nuevo componente
SELECT MAX(idObjeto)+1 FROM siceeo_objetospermiso;  --Obtenemos el nuevo id para objetospermiso
_INSERT INTO siceeo_objetospermiso (idobjeto, formulario, componente, orden) VALUES (25,'PRINCIPAL','btnExalumnos',25);
SELECT MAX(idconfigpermiso)+1 FROM siceeo_configpermiso --Obtenemos el nuevo id para configpermiso
_INSERT INTO siceeo_configpermiso (idconfigpermiso, cveconfigpermiso, idobjeto, permiso, permisodefault) VALUES (170,1,25,'t','t');
_INSERT INTO siceeo_configpermiso (idconfigpermiso, cveconfigpermiso, idobjeto, permiso, permisodefault) VALUES (171,2,25,'f','f');
_INSERT INTO siceeo_configpermiso (idconfigpermiso, cveconfigpermiso, idobjeto, permiso, permisodefault) VALUES (172,3,25,'f','f');
_INSERT INTO siceeo_configpermiso (idconfigpermiso, cveconfigpermiso, idobjeto, permiso, permisodefault) VALUES (173,4,25,'f','f');
_INSERT INTO siceeo_configpermiso (idconfigpermiso, cveconfigpermiso, idobjeto, permiso, permisodefault) VALUES (174,5,25,'f','f');
_INSERT INTO siceeo_configpermiso (idconfigpermiso, cveconfigpermiso, idobjeto, permiso, permisodefault) VALUES (175,6,25,'f','f');
_INSERT INTO siceeo_configpermiso (idconfigpermiso, cveconfigpermiso, idobjeto, permiso, permisodefault) VALUES (176,7,25,'f','f');


-- Regresar a configuración default un objeto
_UPDATE siceeo_configpermiso 
SET permiso=permisodefault 
WHERE idobjeto=(select idobjeto FROM siceeo_objetospermiso WHERE componente='btnPreinscripcion' AND formulario='PRINCIPAL') 

-- Para poderle activar o desactivar un permiso a un componente
--SELECT * FROM siceeo_configpermiso 
_UPDATE siceeo_configpermiso 
SET permiso='t'
where 
   cveconfigpermiso=(select cveconfigpermiso from siceeo_permisos WHERE tipoUsuario='CCT 59') 
   and idobjeto=(select idobjeto FROM siceeo_objetospermiso WHERE componente='btnPreinscripcion' AND formulario='PRINCIPAL') 

-- ========== 59 ==========
select * from escuela where seccion<>'22'

-- ========== PARA BUSCAR UN MUCHITO DE PRUEBA ==========
select a.curp, ag.*
from alumnogrado ag, alumno a 
where a.idalu=ag.idalu and ag.cicescini = 2014 and (ag.grupo='A') and ag.cveplan=1 and ag.grado=6 and estatusgrado='BD'


-- ======================================================================================================================================================
-- ======================================================================================================================================================
-- ======================================================================================================================================================


SELECT * FROM inasistencias i WHERE i.cicescini=2016 AND i.grado=2 AND i.idalu=4340429
select * from inasistencias where cicescini=2016 and cveplan=3 and grado=2

select * from evalpreesc

_INSERT INTO evalpreesc (cvetipmat, cvemat, idalu, cicescini, cicescfin, cveprograma, grado, numeval, avances, recomendaciones, usuario, fecha, hora) 
VALUES ('CBA', '101', 4340429, 2016, 2017, 'PREESCOLAR', 2, 1, "texto1 texto2", "", 'MAAINOLASCO', date(current), extend(current, hour to minute) )

_UPDATEblob evalpreesc SET avances="texto1 texto4", Usuario='MAAINOLASCO', Fecha=date(current), Hora=extend(current, hour to minute) 
WHERE idalu = 4340429 AND cveMat='101' AND cveTipMat='CBA' AND cicescini=2016 AND numeval=1



SELECT m.cvetipmat, m.cvemat, m.desmat, '' AS avances, ordenimpres 
FROM esquemamaterias em, materias m 
WHERE em.cvetipmat=m.cvetipmat AND em.cvemat=m.cvemat AND 
    2016>=em.cicescini AND 2016<=em.cicescfin 
    AND em.cveprograma=(SELECT cveprograma FROM planmodalidad WHERE modalidad='DDI' AND 2016>=cicescini AND 2016<=cicescfin AND plan=em.cveplan AND grado=em.grado) 
    AND em.cveplan=3 AND em.grado=1
ORDER BY em.ordenimpres

select * from esquemamaterias em where 2016>=em.cicescini AND 2016<=em.cicescfin and cveplan=3 and grado=1
select * from planmodalidad where cveprograma='INICIAL'
select * from evalpreesc

select distinct modalidad from escuela where cveplan=3;
select distinct modalidad, cveprograma from planmodalidad where modalidad in ('ADG','DCC','DDI','DJN','DML','EDI','NDI','PDI','PJN')
select * from planmodalidad where cveprograma<>'PREESCOLAR' and modalidad in ('ADG','DCC','DDI','DJN','DML','EDI','NDI','PDI','PJN') and plan=3

select * from escuela where cct like '20DDI0009%'
select * from evalpreesc where substr(usuario,3,3)='DDI' and usuario='20DDI0010E1' and grado=1 and numeval=1



-- ----------QUERYS PARA TRABAJAR CON EL NUEVO MÓDULO DE PERMISOS
select * from siceeo_permisos;
select * from siceeo_configpermiso;
select * from siceeo_objetospermiso;
select * from siceeo_accesoEspecifico;
select * from usuarios

SELECT p.idpermiso, p.tipousuario, p.cveconfigpermiso, op.idobjeto, cp.permiso, cp.permisodefault, op.formulario, op.componente
FROM siceeo_permisos p, siceeo_configpermiso cp, siceeo_objetospermiso op 
WHERE p.cveconfigpermiso=cp.cveconfigpermiso AND op.idobjeto=cp.idobjeto 
   --AND tipousuario='CCT 59'
   AND tipousuario='CCT'
   --AND op.formulario='PRINCIPAL' 
   --AND componente='btnPreinscripcion'
   --AND cp.permiso='t'
ORDER BY tipousuario,orden

SELECT o.idobjeto, o.componente, o.formulario, 
    (SELECT CASE permiso WHEN 't' THEN true ELSE false END FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='CCT')) AS CCT, 
    (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario=' ')) AS _, 
    (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='consulta')) AS consulta, 
    (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='captura')) AS captura, 
    (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='mesa')) AS mesa, 
    (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='CCT 59')) AS CCT59 
FROM siceeo_objetospermiso o
WHERE idobjeto<>0



SELECT o.idobjeto, o.componente, o.formulario, (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='CCT')) AS CCT, (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario=' ')) AS _, (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='consulta')) AS consulta, (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='captura')) AS captura, (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='mesa')) AS mesa, (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='CCT 59')) AS CCT59 FROM siceeo_objetospermiso o WHERE idobjeto<>0

select * from siceeo_configpermiso WHERE idobjeto=2 AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='mesa')

_UPDATE siceeo_configpermiso SET permiso='t' WHERE idobjeto=1 AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='consulta')


SELECT ae.idaccesoespecifico, ae.loginuser, op.componente, op.formulario, ae.permiso, ae.permiso AS permisoOriginal 
FROM siceeo_accesoEspecifico ae, siceeo_objetospermiso op, usuarios u
WHERE ae.idObjeto=op.idobjeto and ae.loginuser=u.loginuser
ORDER BY formulario, loginUser

SELECT o.idobjeto, o.componente, o.formulario,  
    (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='CCT')) AS CCT , 
(SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario=' ')) AS _ , (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='consulta')) AS consulta , (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='captura')) AS captura , (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='mesa')) AS mesa , (SELECT permiso FROM siceeo_configpermiso WHERE idobjeto=o.idobjeto AND cveconfigpermiso=(SELECT cveconfigpermiso FROM siceeo_permisos WHERE tipousuario='CCT 59')) AS CCT_59 FROM siceeo_objetospermiso o WHERE idobjeto<>0 ORDER BY o.formulario, o.orden

SELECT * FROM usuarios WHERE loginuser LIKE 'MAAI%' ORDER BY loginUser

-------- PARA REVISAR EL MODULO PREINSCRIPCION
select idcct, count(*) 
from preinscripcion 
where cicescini=2017  --Alumnos:2761, Escuelas:163
group by idcct

SELECT a.idalu, TRIM(a.curp) AS curp, (TRIM(NVL(a.apepat,'')) || ' / ' || TRIM(NVL(a.apemat,'')) || ' * ' || TRIM(NVL(a.nombre,''))) AS nom_tot, a.apepat, a.apemat, a.nombre, g.cveprograma FROM alumnogrado g, alumno a WHERE g.idalu=a.idalu AND g.estatusGrado <>'BD' AND a.estatusalu ='I'  AND g.cicescini  = 2016 AND g.idcct = 1826 AND g.Grado = 1 AND g.Grupo = 'A' ORDER BY a.apepat, a.apemat, a.nombre, a.curp

-------- PARA REVISAR EL MODULO CAPTURA DE REPORTES DE EVALUACIÓN
select * from evalpreesc where idalu=5599684
select * from alumnogrado where 

select * from ev_caprepeval;

select * from ev_apoyohabilidad;
select * from ev_catapoyohabilidad;

select * from ev_bimasignatura;

select * from ev_complectora;
select * from ev_catfrecuencia;
select * from ev_catmeseval;
select * from ev_catpreguntas;

-------- PARA REVISAR EL MÓDULO DE PERSONAL
select * from profesores; --contiene los datos del profesor
select * from profcurricula; --indica qué profesor está en el grado-grupo-ciclo
select * from escuelaprofesor; --indica que profesor está en tal idcct
select * from profgdogpo; --Es la nueva tabla que rige a los reportes de evaluación
select * from directores;
select * from cargo;

SELECT p.numemp, p.rfc, p.nombre, p.apepat, p.apemat, p.tratamiento 
FROM profcurricula pc, profesores p 
WHERE pc.numemp=p.numemp AND cicescini=2016 AND idcct=1826 AND grado=1 AND grupo=1

SELECT idcct, cveturno, cvecargo, rfc, sexo, nombre, apepat, apemat, tratamiento, estatus, usuario, fecha, hora
FROM directores
WHERE idcct=1826

SELECT MAX(numemp) FROM profesores
select * from profesores where numemp=48546
select * from profcurricula where numemp=48546

_CREATE TABLE ev_recompreesc
(
cicescini SMALLINT NOT NULL,
idalu INTEGER NOT NULL,
recomendaciones lvarchar(800),
primary key (cicescini, idalu) constraint evrecompreesc_pk
);
_ALTER TABLE 
         ev_recompreesc ADD CONSTRAINT FOREIGN KEY (cicescini, idalu) REFERENCES alumnogrado (cicescini, idalu) CONSTRAINT idalucicERP_fk_alumnogrado
_ALTER TABLE
    ev_recompreesc DROP CONSTRAINT idalucicerp_fk_alumnogrado
        
_CREATE INDEX cicidalu_index_evalpreesc ON evalpreesc(cicescini, idalu)
_ALTER TABLE
    ev_recompreesc ADD CONSTRAINT FOREIGN KEY (cicescini, idalu) REFERENCES evalpreesc (cicescini, idalu) CONSTRAINT idalucicERP_fk_evalpreesc
_DROP INDEX informix.cicidalu_index_evalpreesc

select * from ev_recompreesc


-------- PARA REVISAR LA CAPTURA DE DATOS COMPLEMENTARIOS DE EVALUACIÓN PREESCOLAR
select ep.idalu, e.idcct, e.cct, ep.grado, ag.promovido, ep.numeval, a.apepat, a.apemat, a.nombre, ep.cvetipmat, ep.cvemat, ep.avances
from evalpreesc ep, alumnogrado ag, alumno a, escuela e
where ep.idalu=ag.idalu and ep.cicescini=ag.cicescini and ep.idalu=a.idalu and ag.idcct=e.idcct
order by e.cct, ep.grado, ep.numeval;


--------- PARA REVISAR OFICIALIZACIONES DE CALIFICACIONES DE UNA ESCUELA
SELECT co.cveoficializacion, 'bimOf'||REPLACE(co.oficializacion,'CALIFS BIM ') AS bimof, 
    CASE WHEN o.idcct IS NULL THEN 'f' ELSE 't' END AS estadoOfic 
FROM catoficializacion co left join oficializacion o on (co.cveoficializacion=o.cveoficializacion AND o.cicescini=2016 AND o.idcct=1826) 
WHERE co.oficializacion like 'CALIFS BIM%' 
ORDER BY co.oficializacion;

--------- PARA REVISAR DESOFICIALIZACIONES DE CALIFICACIONES DE UN ALUMNO
SELECT co.cveoficializacion, 'alDeofB'||REPLACE(co.oficializacion,'CALIFS BIM ') AS bimDeof, 
    CASE WHEN d.idcct IS NULL THEN 'f' ELSE 't' END AS estadoDesofic 
FROM catoficializacion co left join desoficializacion d on (co.cveoficializacion=d.cveoficializacion AND d.cicescini=2016 AND d.idcct=1826 AND d.idalu=1366302) 
WHERE co.oficializacion like 'CALIFS BIM%' 
ORDER BY co.oficializacion;


SELECT idfolio, foliolet, folionum, idalu, idcct, e.cvezona, cveplan, e.cct, cveturno, grado, grupo, a.curp, a.nombre,
                        a.apepat, a.apemat, cicescini, cicescini AS cicescinilib, 'usukgenero', 'horkgenero', 'feckgenero', alos, delmes, 
                        promediogral, quienfirma, tratamiento, usuario, fecha, hora, cveunidad
FROM folios_impre fi, escuela e, alumno a
where idcct=e.idcct AND aidalu=a.idalu AND idalu = 2824553 and cicescini=2016


"CERO,UNO,DOS,TRES,CUATRO,CINCO,SEIS,SIETE,OCHO,NUEVE,DIEZ".split(",")[0]
"CERO,UNO,DOS,TRES,CUATRO,CINCO,SEIS,SIETE,OCHO,NUEVE,DIEZ".split(",")[Integer.parseInt("9.6".split(".")[0])] + " PUNTO " + "CERO,UNO,DOS,TRES,CUATRO,CINCO,SEIS,SIETE,OCHO,NUEVE,DIEZ".split(",")[Integer.parseInt("9.6".split(".")[1])]

--Convertir decimal en letra
$F{promediogral}.equals("")
?""
:$F{promediogral}.contains("10")
 ?"DIEZ"
 :"CERO,UNO,DOS,TRES,CUATRO,CINCO,SEIS,SIETE,OCHO,NUEVE,DIEZ".split(",")[Integer.parseInt($F{promediogral}.split("\\.")[0])]
  + " PUNTO "
  + "CERO,UNO,DOS,TRES,CUATRO,CINCO,SEIS,SIETE,OCHO,NUEVE,DIEZ".split(",")[Integer.parseInt($F{promediogral}.split("\\.")[1])]



SELECT idalu 
FROM desoficializacion WHERE cicescini=2016 AND idcct=1826 AND grado=1 AND grupo='A' AND cveoficializacion 
    IN (SELECT cveoficializacion FROM catoficializacion WHERE oficializacion = 'CALIFS BIM 5') 

/************************ PARA VER UN ALUMNO QUE NO APARECE SU CERTIFICADO ************************/  
select 
  idcct, 
  (select idcct from folios_impre where cicescinilib=ag.cicescini and idalu=ag.idalu) as idcct_foliosimpre,
  grado, 
  (select grado from folios_impre where cicescinilib=ag.cicescini and idalu=ag.idalu) as grado_foliosimpre,
  grupo, 
  (select grupo from folios_impre where cicescinilib=ag.cicescini and idalu=ag.idalu) as grupo_foliosimpre,
  promediogral, 
  (select promediogral from folios_impre where cicescinilib=ag.cicescini and idalu=ag.idalu) as promediogral_foliosimpre,
  estatusgrado,
  (select estatusgrado from folios_impre where cicescinilib=ag.cicescini and idalu=ag.idalu) as estatusgrado_foliosimpre,
  (select folionum from folios_impre where cicescinilib=ag.cicescini and idalu=ag.idalu) as folionum_certif_folimpre,
  (select folionum from fol_re_elec where cicescini=ag.cicescini and idalu=ag.idalu) as folionum_certif_folcanc,
  (select sellodigitalieepo from firma_elec where cicescinilib=ag.cicescini and idalu=ag.idalu) as estatusgrado_foliosimpre
from alumnogrado ag
where ag.cicescini=2016 and ag.idalu=2754407;

select idcct, cct, cveturno, cveunidad, seccion from escuela where idcct=10071; 
select idalu, cicescini, grado, grupo, idcct, estatusgrado, cveplan, promedio, promediogral, promovido, usuario, fecha, hora
from alumnogrado where cicescini=2016 and idalu=1085465; --1077683
SELECT * FROM exm_ext_ordi WHERE idalu=1085465;
select * from folios_impre where cicescinilib=2016 and idalu=1085465;
select * from firma_elec where cicescinilib=2016 and idalu=1085465;
select * from folios_cance where cicescinilib=2016 and idalu=1085465;
select * from firma_elec_cance where cicescinilib=2016 and idalu=1085465;
select * from fol_re_elec where cicescini=2016 and idalu=1085465;
select * from fol_re_elec_cance where cicescini=2016 and idalu=1085465;


/*Para asignar folio de reporte de evaluación*/
if (cveplan.equals("1"))
    nivelmod =  (modalidad.equals("DPB") || modalidad.equals("DCI")) ? "PRII" : "PRIF";
else if (cveplan.equals("2"))
    nivelmod = "SECU";
else if (cveplan.equals("3"))
    nivelmod = "PREE";
SELECT NVL(MAX(folionum),0)+1 FROM fol_re_elec WHERE cicescini="+cicescini+" AND nivelmod='"+nivelmod+"'

"INSERT INTO fol_re_elec (idalu, cveplan, foliolet, folionum, idcct, grado, cicescini, usuario, fecha, hora, estatus, nivelmod) "
+ "SELECT idalu, cveplan, 'RE', "+folionum+", idcct, grado, cicescini, '"+usuario+"', date(current), extend(current, hour to minute), 'A', '"+nivelmod+"' "
+ "FROM alumnogrado "
+ "WHERE cicescini="+cicescini+" AND idalu="+rs2.getString("idalu")+" "


SELECT ag.idalu
 ,(SELECT mes 
  FROM (
        SELECT FIRST 1 mes
        FROM exm_ext_ordi 
        WHERE idalu=ag.idalu AND cicescini=ag.cicescini
        order by anio desc, substr(mes,3,1) desc
      )
 )as mes,
 ag.promediogral, a.curp, a.nombre, a.apepat, a.apemat
 ,ag.grado, ag.idcct, ag.grupo, ag.promedio,
  (SELECT alos FROM folios_impre WHERE idcct=ag.idcct and cicescinilib=ag.cicescini and idalu=ag.idalu)
FROM alumnogrado ag, alumno a
WHERE   ag.idalu=a.idalu
        AND ag.cicescini=2016  
        AND ag.idcct=8541 
        AND ag.grado=3 
        AND ag.grupo='B'
        AND ag.promediogral>=0
        AND ag.estatusgrado<>'BD'
        AND ag.idalu IN (SELECT idalu 
                        FROM exm_ext_ordi 
                        WHERE idcct_apl=ag.idcct AND cicescini=ag.cicescini )--951194
        AND (ag.idalu IN (SELECT idalu FROM folios_impre WHERE cicescinilib=ag.cicescini and idcct=ag.idcct and grado=ag.grado and grupo=ag.grupo and alos like '%julio%')
            or ag.idalu NOT IN (SELECT idalu FROM folios_impre WHERE cicescinilib=ag.cicescini))
ORDER BY a.apepat, a.apemat, a.nombre, a.curp


SELECT distinct(idalu), (SELECT alos FROM folios_impre WHERE idalu=ex.idalu and cicescinilib=ex.cicescini )
FROM exm_ext_ordi ex
WHERE idcct_apl=8541 AND cicescini=2016
   AND idalu IN (SELECT idalu FROM folios_impre WHERE cicescinilib=ex.cicescini and idcct=ex.idcct_apl and alos like '%julio%')


SELECT * FROM folios_impre WHERE idcct=8541 and cicescini=2016 and idalu in (1150763,918515,1230616) alos like '%julio%' --1378662, 918515

SELECT * 
FROM exm_ext_ordi
WHERE cicescini=2016 and idalu not in (SELECT idalu FROM folios_impre WHERE cicescinilib=2016)

select * from folios_impre where idalu=920663 and cicescini=2016

/******** PARA CANCELAR UN FOLIO DE CERTIFICADO ********/
_INSERT INTO folios_cance (idfolio, foliolet, folionum, idalu, idcct, cvezona, cveplan, cct, cveturno, grado, grupo, curp, nombre, 
    apepat, apemat, cicescini, cicescinilib, usukgenero, horkgenero, feckgenero, alos, delmes, promediogral, quienfirma, tratamiento, 
    usuario, fecha, hora, cveunidad )
SELECT fi.idfolio, fi.foliolet, fi.folionum, fi.idalu, fi.idcct, fi.cvezona, fi.cveplan, fi.cct, fi.cveturno, fi.grado, fi.grupo, fi.curp, fi.nombre, 
    fi.apepat, fi.apemat, fi.cicescini, fi.cicescinilib, fi.usuario, fi.hora, fi.fecha, fi.alos, fi.delmes, fi.promediogral, fi.quienfirma, fi.tratamiento, 
    'ADMIN', date(current), extend(current, hour to minute), fi.cveunidad 
FROM folios_impre fi 
WHERE fi.cicescinilib=2016 AND fi.idcct=1826 AND fi.grado=3 AND fi.grupo='a' AND fi.idalu=0

_DELETE FROM folios_impre 
WHERE cicescinilib=2016 AND idcct=1826 AND grado=3 AND grupo='A' AND idalu=0

/******** PARA CANCELAR UNA FIRMA ********/
_INSERT INTO firma_elec_cance 
       (idalu, cicescini, cicescinilib, idfirmante, cadenaoriginal, fechatimbradoieepo, sellodigitalieepo, fechatimbradosep, sellodigitalsep, 
        folionum_cer, estatus, usuario_cance, fecha_cance, hora_cance) 
SELECT idalu, cicescini, cicescinilib, idfirmante, cadenaoriginal, fechatimbradoieepo, sellodigitalieepo, fechatimbradosep, sellodigitalsep, 
        folionum_cer, estatus, 'MAAINOLASCO', date(current), extend(current, hour to minute) 
FROM firma_elec
WHERE cicescinilib=2016 AND idalu=2754407

_DELETE FROM firma_elec WHERE cicescinilib=2016 AND idalu=2754407

/******** PARA REVERSA A CANCELACIÓN DE FOLIO DE CERTIFICADO ********/
select idfolio, foliolet, folionum, idalu, idcct, grado, grupo, usuario, fecha, hora from folios_impre where cicescinilib=2016 and idalu=890606;
select idfolio, foliolet, folionum, idalu, idcct, grado, grupo, alos, usuario, fecha, hora from folios_cance where cicescinilib=2016 and idalu=890606;

_INSERT INTO folios_impre (idfolio, foliolet, folionum, idalu, idcct, cvezona, cveplan, cct, cveturno, grado, crip, curp, grupo, nombre, apepat, 
        apemat, cicescini, cicescinilib, alos, delmes, promediogral, quienfirma, tratamiento, usuario, fecha, hora, cveunidad, cveentidad)
SELECT idfolio, foliolet, folionum, idalu, idcct, cvezona, cveplan, cct, cveturno, grado, NVL(crip,substr(curp,5,6)), curp, grupo, nombre, apepat, apemat, cicescini, 
        cicescinilib, alos, delmes, promediogral, quienfirma, tratamiento, usukgenero, feckgenero, horkgenero, cveunidad, 20
FROM folios_cance
WHERE idfolio=7375488
  --folionum=170100038 AND cveplan=2 AND cicescinilib=2016 AND idalu=1085465

_DELETE FROM folios_cance WHERE idfolio=7375488 --folionum=170100034 AND cveplan=2 AND cicescinilib=2016 AND idalu=1399796

/******** PARA REVERSA A CANCELACIÓN DE FIRMA ********/
select idalu, cicescini, fechatimbradoieepo, folionum_cer from firma_elec where cicescinilib=2016 and idalu=890606;
select idcance, idalu, cicescini, fechatimbradoieepo, folionum_cer, usuario_cance, fecha_cance, hora_cance from firma_elec_cance where cicescinilib=2016 and idalu=890606;

_INSERT INTO firma_elec
       (idalu, cicescini, cicescinilib, idfirmante, cadenaoriginal, fechatimbradoieepo, sellodigitalieepo, fechatimbradosep, sellodigitalsep, 
        folionum_cer, estatus)
SELECT idalu, cicescini, cicescinilib, idfirmante, cadenaoriginal, fechatimbradoieepo, sellodigitalieepo, fechatimbradosep, sellodigitalsep, 
        folionum_cer, estatus
FROM firma_elec_cance
WHERE idcance=57081 --cicescinilib=2016 AND idalu=1399796 AND fechatimbradoieepo='2017-07-25 00:46:14'

_DELETE FROM firma_elec_cance WHERE idcance=56378 --cicescinilib=2016 AND idalu=1399796 AND fechatimbradoieepo='2017-07-25 00:46:14'

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

SELECT a.IDALU, a.curp, 
    (trim(TRAILING ' ' FROM NVL(a.apepat,'')) || '/' || TRIM(TRAILING ' ' FROM NVL(a.apemat,'')) || '*' || TRIM(TRAILING ' ' FROM NVL(a.nombre,''))) AS nom_tot, 
    g.estatusgrado, 
    (SELECT count(*) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 6 AND cveplan=1 AND (estatusgrado='RE' OR estatusgrado='C'  )) AS en6to, 
    (SELECT count(*) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 1 AND cveplan=2 AND (estatusgrado='RE' )) AS en1ro, 
    (SELECT count(*) FROM alumnoGrado WHERE AlumnoGrado.idalu = g.idalu AND Grado= 2 AND cveplan=2 AND (estatusgrado='RE' )) AS en2do 
FROM alumnogrado g, alumno a 
WHERE g.idalu=a.idalu AND g.estatusGrado<>'BD' AND (a.estatusalu ='I' OR a.estatusalu=I) 
    AND g.cicescini=:2016 AND g.idcct=1826 AND g.Grado=1 AND g.Grupo=A 
ORDER BY a.curp


20DES0008W	3	H
1145019	AASE020312HOCYNNA0	AYALA/SANCHEZ*ENRIQUE GABRIEL
4129420	BEPD020928MDFRZNA6	BERNABE/PAZ*DIANA



  -- ¡¡¡¡¡ OJO MUY MUY IMPORTANTE !!!!!!, FIRMAS DE FOLIOS QUE SE DUPLICARON
  select * from firma_elec where idalu in (1060848,
1163667,
172014,
1174979,
1055819,
988179,
294151229,
1055795)
  
  select idalu, cicescinilib, count(*)
  from firma_elec 
  group by idalu, cicescinilib
  having count(*)>1
)  


select * FROM CicloEscolar WHERE estatus='A'