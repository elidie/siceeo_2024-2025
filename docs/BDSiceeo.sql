--
-- Estructura de tabla para la tabla siceeo_permisos
--
create table siceeo_permisos
(
  idpermiso INTEGER NOT NULL, 
  tipousuario varchar(10) NOT NULL,  
  cveconfigpermiso SMALLINT NOT NULL,  
  PRIMARY KEY  (idpermiso)
);
INSERT INTO siceeo_permisos (idpermiso,tipousuario,cveconfigpermiso) VALUES(1,'ADMIN',1);
INSERT INTO siceeo_permisos (idpermiso,tipousuario,cveconfigpermiso) VALUES(2,'CCT',2);
INSERT INTO siceeo_permisos (idpermiso,tipousuario,cveconfigpermiso) VALUES(3,' ',3);
INSERT INTO siceeo_permisos (idpermiso,tipousuario,cveconfigpermiso) VALUES(4,'consulta',4);
INSERT INTO siceeo_permisos (idpermiso,tipousuario,cveconfigpermiso) VALUES(5,'captura',5);
INSERT INTO siceeo_permisos (idpermiso,tipousuario,cveconfigpermiso) VALUES(6,'mesa',6);
INSERT INTO siceeo_permisos (idpermiso,tipousuario,cveconfigpermiso) VALUES(7,'CCT 59',7);


CREATE table siceeo_configpermiso
(
  idconfigpermiso INTEGER not null,
  cveconfigpermiso smallint not null,
  idobjeto integer not null,
  permiso BOOLEAN NOT NULL,
  permisodefault BOOLEAN NOT NULL,
  UNIQUE (cveconfigpermiso, idobjeto) CONSTRAINT cveconfperidobjCP_UK,
  primary key (idconfigpermiso)
);

-- SUPERUSUARIO --
INSERT INTO siceeo_configpermiso VALUES (1,1,1,'t');
INSERT INTO siceeo_configpermiso VALUES (2,1,2,'t');
INSERT INTO siceeo_configpermiso VALUES (3,1,3,'t');
INSERT INTO siceeo_configpermiso VALUES (4,1,4,'t');
INSERT INTO siceeo_configpermiso VALUES (5,1,5,'t');
INSERT INTO siceeo_configpermiso VALUES (6,1,6,'t');
INSERT INTO siceeo_configpermiso VALUES (7,1,7,'t');
INSERT INTO siceeo_configpermiso VALUES (8,1,8,'t');
INSERT INTO siceeo_configpermiso VALUES (9,1,9,'t');
INSERT INTO siceeo_configpermiso VALUES (10,1,10,'t');
INSERT INTO siceeo_configpermiso VALUES (11,1,11,'t');
INSERT INTO siceeo_configpermiso VALUES (12,1,12,'t');
INSERT INTO siceeo_configpermiso VALUES (13,1,13,'t');
INSERT INTO siceeo_configpermiso VALUES (14,1,14,'t');
INSERT INTO siceeo_configpermiso VALUES (15,1,15,'t');
INSERT INTO siceeo_configpermiso VALUES (16,1,16,'t');
INSERT INTO siceeo_configpermiso VALUES (17,1,17,'t');
INSERT INTO siceeo_configpermiso VALUES (18,1,18,'t');
INSERT INTO siceeo_configpermiso VALUES (19,1,19,'t');
INSERT INTO siceeo_configpermiso VALUES (20,1,20,'t');
-- CCT --
--"btnImprimeActual", "btnPreinscripcion", "btnNuevoIngreso", "btnCamDeGpo", "btnDiscapa", "btnCamDTaller", "btnCamDArte", "btnCalif", "btnDirector","btnAlumno","btnBuskAlum", "btnBoletas", (btnCartilla_Visible?"btnCartilla":""
INSERT INTO siceeo_configpermiso VALUES (21,2,1,'t');
INSERT INTO siceeo_configpermiso VALUES (22,2,2,'t');
INSERT INTO siceeo_configpermiso VALUES (23,2,3,'t');
INSERT INTO siceeo_configpermiso VALUES (24,2,4,'t');
INSERT INTO siceeo_configpermiso VALUES (25,2,5,'t');
INSERT INTO siceeo_configpermiso VALUES (26,2,6,'t');
INSERT INTO siceeo_configpermiso VALUES (27,2,7,'f');
INSERT INTO siceeo_configpermiso VALUES (28,2,8,'t');
INSERT INTO siceeo_configpermiso VALUES (29,2,9,'t');
INSERT INTO siceeo_configpermiso VALUES (30,2,10,'t');
INSERT INTO siceeo_configpermiso VALUES (31,2,11,'t');
INSERT INTO siceeo_configpermiso VALUES (32,2,12,'f');
INSERT INTO siceeo_configpermiso VALUES (33,2,13,'t');
INSERT INTO siceeo_configpermiso VALUES (34,2,14,'f');
INSERT INTO siceeo_configpermiso VALUES (35,2,15,'t');
INSERT INTO siceeo_configpermiso VALUES (36,2,16,'f');
INSERT INTO siceeo_configpermiso VALUES (37,2,17,'f');
INSERT INTO siceeo_configpermiso VALUES (38,2,18,'f');
INSERT INTO siceeo_configpermiso VALUES (39,2,19,'t');
INSERT INTO siceeo_configpermiso VALUES (40,2,20,'f');

-- ' ' --
--"btnImprimeActual","btnPreinscripcion", "btnNuevoIngreso", "btnCamDeGpo", "btnDiscapa", "btnCamDTaller", "btnCamDArte", "btnCalif", "btnDirector","btnAlumno","btnGrupos","btnBuskAlum", "btnCertifi", "btnBoletas","btnReAjustar","btnModifiCurp"
INSERT INTO siceeo_configpermiso VALUES (41,3,1,'t');
INSERT INTO siceeo_configpermiso VALUES (42,3,2,'t');
INSERT INTO siceeo_configpermiso VALUES (43,3,3,'t');
INSERT INTO siceeo_configpermiso VALUES (44,3,4,'t');
INSERT INTO siceeo_configpermiso VALUES (45,3,5,'t');
INSERT INTO siceeo_configpermiso VALUES (46,3,6,'t');
INSERT INTO siceeo_configpermiso VALUES (47,3,7,'f');
INSERT INTO siceeo_configpermiso VALUES (48,3,8,'t');
INSERT INTO siceeo_configpermiso VALUES (49,3,9,'t');
INSERT INTO siceeo_configpermiso VALUES (50,3,10,'t');
INSERT INTO siceeo_configpermiso VALUES (51,3,11,'t');
INSERT INTO siceeo_configpermiso VALUES (52,3,12,'t');
INSERT INTO siceeo_configpermiso VALUES (53,3,13,'t');
INSERT INTO siceeo_configpermiso VALUES (54,3,14,'t');
INSERT INTO siceeo_configpermiso VALUES (55,3,15,'t');
INSERT INTO siceeo_configpermiso VALUES (56,3,16,'t');
INSERT INTO siceeo_configpermiso VALUES (57,3,17,'t');
INSERT INTO siceeo_configpermiso VALUES (58,3,18,'f');
INSERT INTO siceeo_configpermiso VALUES (59,3,19,'f');
INSERT INTO siceeo_configpermiso VALUES (60,3,20,'f');

-- 'consulta' --
--"btnCamDeGpo", "btnDiscapa", "btnCamDTaller", "btnCamDArte", "btnAlumno","btnBuskAlum", "btnReAjustar"
INSERT INTO siceeo_configpermiso VALUES (61,4,1,'f');
INSERT INTO siceeo_configpermiso VALUES (62,4,2,'f');
INSERT INTO siceeo_configpermiso VALUES (63,4,3,'f');
INSERT INTO siceeo_configpermiso VALUES (64,4,4,'t');
INSERT INTO siceeo_configpermiso VALUES (65,4,5,'t');
INSERT INTO siceeo_configpermiso VALUES (66,4,6,'t');
INSERT INTO siceeo_configpermiso VALUES (67,4,7,'f');
INSERT INTO siceeo_configpermiso VALUES (68,4,8,'t');
INSERT INTO siceeo_configpermiso VALUES (69,4,9,'f');
INSERT INTO siceeo_configpermiso VALUES (70,4,10,'f');
INSERT INTO siceeo_configpermiso VALUES (71,4,11,'t');
INSERT INTO siceeo_configpermiso VALUES (72,4,12,'f');
INSERT INTO siceeo_configpermiso VALUES (73,4,13,'t');
INSERT INTO siceeo_configpermiso VALUES (74,4,14,'f');
INSERT INTO siceeo_configpermiso VALUES (75,4,15,'f');
INSERT INTO siceeo_configpermiso VALUES (76,4,16,'t');
INSERT INTO siceeo_configpermiso VALUES (77,4,17,'f');
INSERT INTO siceeo_configpermiso VALUES (78,4,18,'f');
INSERT INTO siceeo_configpermiso VALUES (79,4,19,'f');
INSERT INTO siceeo_configpermiso VALUES (80,4,20,'f');

-- 'captura' --
--"btnCalif", "btnAlumno","btnBuskAlum"
INSERT INTO siceeo_configpermiso VALUES (81,5,1,'f');
INSERT INTO siceeo_configpermiso VALUES (82,5,2,'f');
INSERT INTO siceeo_configpermiso VALUES (83,5,3,'f');
INSERT INTO siceeo_configpermiso VALUES (84,5,4,'f');
INSERT INTO siceeo_configpermiso VALUES (85,5,5,'f');
INSERT INTO siceeo_configpermiso VALUES (86,5,6,'f');
INSERT INTO siceeo_configpermiso VALUES (87,5,7,'f');
INSERT INTO siceeo_configpermiso VALUES (88,5,8,'f');
INSERT INTO siceeo_configpermiso VALUES (89,5,9,'t');
INSERT INTO siceeo_configpermiso VALUES (90,5,10,'f');
INSERT INTO siceeo_configpermiso VALUES (91,5,11,'t');
INSERT INTO siceeo_configpermiso VALUES (92,5,12,'f');
INSERT INTO siceeo_configpermiso VALUES (93,5,13,'t');
INSERT INTO siceeo_configpermiso VALUES (94,5,14,'f');
INSERT INTO siceeo_configpermiso VALUES (95,5,15,'f');
INSERT INTO siceeo_configpermiso VALUES (96,5,16,'f');
INSERT INTO siceeo_configpermiso VALUES (97,5,17,'f');
INSERT INTO siceeo_configpermiso VALUES (98,5,18,'f');
INSERT INTO siceeo_configpermiso VALUES (99,5,19,'f');
INSERT INTO siceeo_configpermiso VALUES (100,5,20,'f');

-- 'mesa' --
--"btnImprimeActual", "btnCamDeGpo", "btnDiscapa", "btnCamDTaller", "btnCamDArte", "btnDirector","btnAlumno","btnBuskAlum", "btnReAjustar"
INSERT INTO siceeo_configpermiso VALUES (101,6,1,'t');
INSERT INTO siceeo_configpermiso VALUES (102,6,2,'f');
INSERT INTO siceeo_configpermiso VALUES (103,6,3,'f');
INSERT INTO siceeo_configpermiso VALUES (104,6,4,'t');
INSERT INTO siceeo_configpermiso VALUES (105,6,5,'t');
INSERT INTO siceeo_configpermiso VALUES (106,6,6,'t');
INSERT INTO siceeo_configpermiso VALUES (107,6,7,'f');
INSERT INTO siceeo_configpermiso VALUES (108,6,8,'t');
INSERT INTO siceeo_configpermiso VALUES (109,6,9,'f');
INSERT INTO siceeo_configpermiso VALUES (110,6,10,'t');
INSERT INTO siceeo_configpermiso VALUES (111,6,11,'t');
INSERT INTO siceeo_configpermiso VALUES (112,6,12,'f');
INSERT INTO siceeo_configpermiso VALUES (113,6,13,'t');
INSERT INTO siceeo_configpermiso VALUES (114,6,14,'f');
INSERT INTO siceeo_configpermiso VALUES (115,6,15,'f');
INSERT INTO siceeo_configpermiso VALUES (116,6,16,'t');
INSERT INTO siceeo_configpermiso VALUES (117,6,17,'f');
INSERT INTO siceeo_configpermiso VALUES (118,6,18,'f');
INSERT INTO siceeo_configpermiso VALUES (119,6,19,'f');
INSERT INTO siceeo_configpermiso VALUES (120,6,20,'f');

-- CCT 59 --
INSERT INTO siceeo_configpermiso VALUES (121,7,1,'f');
INSERT INTO siceeo_configpermiso VALUES (122,7,2,'t');
INSERT INTO siceeo_configpermiso VALUES (123,7,3,'f');
INSERT INTO siceeo_configpermiso VALUES (124,7,4,'f');
INSERT INTO siceeo_configpermiso VALUES (125,7,5,'f');
INSERT INTO siceeo_configpermiso VALUES (126,7,6,'f');
INSERT INTO siceeo_configpermiso VALUES (127,7,7,'f');
INSERT INTO siceeo_configpermiso VALUES (128,7,8,'f');
INSERT INTO siceeo_configpermiso VALUES (129,7,9,'f');
INSERT INTO siceeo_configpermiso VALUES (130,7,10,'f');
INSERT INTO siceeo_configpermiso VALUES (131,7,11,'f');
INSERT INTO siceeo_configpermiso VALUES (132,7,12,'f');
INSERT INTO siceeo_configpermiso VALUES (133,7,13,'f');
INSERT INTO siceeo_configpermiso VALUES (134,7,14,'f');
INSERT INTO siceeo_configpermiso VALUES (135,7,15,'f');
INSERT INTO siceeo_configpermiso VALUES (136,7,16,'f');
INSERT INTO siceeo_configpermiso VALUES (137,7,17,'f');
INSERT INTO siceeo_configpermiso VALUES (138,7,18,'f');
INSERT INTO siceeo_configpermiso VALUES (139,7,19,'f');
INSERT INTO siceeo_configpermiso VALUES (140,7,20,'f');


--
-- Estructura de tabla para la tabla siceeo_objetospermiso
--
create table siceeo_objetospermiso
(
  idobjeto INTEGER NOT NULL, 
  formulario varchar(30) NOT NULL,
  componente varchar(50) NOT NULL,
  PRIMARY KEY  (idobjeto)
);
INSERT INTO siceeo_objetospermiso VALUES (0,"SIN ASIGNAR","SIN ASIGNAR");
INSERT INTO siceeo_objetospermiso VALUES (1,"PRINCIPAL","btnImprimeActual");
INSERT INTO siceeo_objetospermiso VALUES (2,"PRINCIPAL","btnPreinscripcion");
INSERT INTO siceeo_objetospermiso VALUES (3,"PRINCIPAL","btnNuevoIngreso");
INSERT INTO siceeo_objetospermiso VALUES (4,"PRINCIPAL","btnCamDeGpo");
INSERT INTO siceeo_objetospermiso VALUES (5,"PRINCIPAL","btnDiscapa");
INSERT INTO siceeo_objetospermiso VALUES (6,"PRINCIPAL","btnCamDTaller");
INSERT INTO siceeo_objetospermiso VALUES (7,"PRINCIPAL","btnCamDAes");
INSERT INTO siceeo_objetospermiso VALUES (8,"PRINCIPAL","btnCamDArte");
INSERT INTO siceeo_objetospermiso VALUES (9,"PRINCIPAL","btnCalif");
INSERT INTO siceeo_objetospermiso VALUES (10,"PRINCIPAL","btnDirector");
INSERT INTO siceeo_objetospermiso VALUES (11,"PRINCIPAL","btnAlumno");
INSERT INTO siceeo_objetospermiso VALUES (12,"PRINCIPAL","btnGrupos");
INSERT INTO siceeo_objetospermiso VALUES (13,"PRINCIPAL","btnBuskAlum");
INSERT INTO siceeo_objetospermiso VALUES (14,"PRINCIPAL","btnCertifi");
INSERT INTO siceeo_objetospermiso VALUES (15,"PRINCIPAL","btnBoletas");
INSERT INTO siceeo_objetospermiso VALUES (16,"PRINCIPAL","btnReAjustar");
INSERT INTO siceeo_objetospermiso VALUES (17,"PRINCIPAL","btnModifiCurp");
INSERT INTO siceeo_objetospermiso VALUES (18,"PRINCIPAL","btnPreinscrip");
INSERT INTO siceeo_objetospermiso VALUES (19,"PRINCIPAL","btnCartilla");
INSERT INTO siceeo_objetospermiso VALUES (20,"PRINCIPAL","btnConfiguraciones");

--
-- Estructura de tabla para la tabla siceeo_accesoEspecifico
--
CREATE table siceeo_accesoEspecifico
(
  idaccesoespecifico INTEGER NOT NULL,
  loginuser VARCHAR(15) NOT NULL,
  idobjeto INTEGER NOT NULL,
  permiso BOOLEAN NOT NULL,
  PRIMARY KEY (idaccesoespecifico),
  UNIQUE (loginuser, idobjeto) CONSTRAINT idobjetopermisoSAE_UK
);

--
-- Estructura de tabla para la tabla observsAluGdo
--
create table observsAluGdo
(
  idalu INTEGER NOT NULL,
  cicescini SMALLINT NOT NULL,
  grado SMALLINT NOT NULL,
  observaciones lvarchar(300) NOT NULL,
  usuario VARCHAR(10) NOT NULL,
  fechainsert DATETIME YEAR TO SECOND,
  PRIMARY KEY  (idalu, cicescini, grado)
);

CREATE TABLE desoficializacion
(
    idcct INTEGER NOT NULL,
    cicescini SMALLINT NOT NULL,
    idalu INTEGER NOT NULL,
    cveoficializacion SMALLINT NOT NULL,
    usuario VARCHAR(25) NOT NULL,
    fecha DATETIME YEAR TO SECOND NOT NULL,
    PRIMARY KEY (cicescini, idalu, cveoficializacion)
);


-- -------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------

ALTER TABLE siceeo_permisos
    ADD CONSTRAINT  FOREIGN KEY (cveconfigpermiso) REFERENCES siceeo_configpermiso (cveconfigpermiso) CONSTRAINT cveconfigpermisoP_FK_ConfigPermiso;

ALTER TABLE siceeo_configpermiso
    ADD CONSTRAINT  FOREIGN KEY (idobjeto) REFERENCES siceeo_objetospermiso (idobjeto) CONSTRAINT idobjetoCP_FK_ObjetosPermiso;

ALTER TABLE siceeo_accesoEspecifico
    ADD CONSTRAINT  FOREIGN KEY (idobjeto) REFERENCES siceeo_objetospermiso (idobjeto) CONSTRAINT idobjetoAE_FK_ObjetosPermiso;

ALTER TABLE desoficializacion
    ADD CONSTRAINT FOREIGN KEY (idcct, cicescini, cveoficializacion) REFERENCES oficializacion (idcct, cicescini, cveoficializacion) CONSTRAINT idcciccveofdo_fk_oficializacion,
    ADD CONSTRAINT FOREIGN KEY (cveoficializacion) REFERENCES catoficializacion (cveoficializacion) CONSTRAINT cveoficializaciondo_fk_catoficializacion,
    ADD CONSTRAINT FOREIGN KEY (idcct) REFERENCES escuela (idcct) CONSTRAINT idcctdo_fk_escuela

--ALTER TABLE siceeo_configpermiso ADD CONSTRAINT UNIQUE (cveconfigpermiso, idobjeto) CONSTRAINT cveconfperidobjCP_UK;


select * from oficializacion;
select * from catoficializacion;

ALTER TABLE oficializacion
    ADD CONSTRAINT  FOREIGN KEY (cveoficializacion) REFERENCES catoficializacion (cveoficializacion) CONSTRAINT cveoficializacionO_FK_catoficializacion;


