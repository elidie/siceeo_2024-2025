CREATE TABLE reps_tipografia
(
    idfuente SMALLINT,
    tamanio SMALLINT,
    fuente VARCHAR(15),
    PRIMARY KEY (idfuente),
    UNIQUE (tamanio, fuente)
);
INSERT INTO reps_tipografia VALUES
(1,11,'Arial');

CREATE TABLE reps_formatos
(
    idformato INTEGER NOT NULL,
    formato VARCHAR (20),
    PRIMARY KEY(idformato)
);
INSERT INTO reps_formatos VALUES
(1,'CARTILLA');

CREATE TABLE reps_descleyen
(
    idleyenda INTEGER NOT NULL,
    idformato INTEGER NOT NULL,
    ordenimpre SMALLINT NOT NULL,
    leyenda varchar(20),
    PRIMARY KEY(idleyenda),
    UNIQUE (idformato, leyenda)
);
INSERT INTO reps_descleyen VALUES (1,1,0,'primerApe');
INSERT INTO reps_descleyen VALUES (2,1,1,'segundoApe');
INSERT INTO reps_descleyen VALUES (3,1,2,'nombres');
INSERT INTO reps_descleyen VALUES (4,1,3,'curp');
INSERT INTO reps_descleyen VALUES (5,1,4,'grupo');
INSERT INTO reps_descleyen VALUES (6,1,5,'turno');
INSERT INTO reps_descleyen VALUES (7,1,6,'escuela');
INSERT INTO reps_descleyen VALUES (8,1,7,'cct');
INSERT INTO reps_descleyen VALUES (9,1,8,'mat1Bim1');
INSERT INTO reps_descleyen VALUES (10,1,9,'mat2Bim1');
INSERT INTO reps_descleyen VALUES (11,1,10,'mat3Bim1');
INSERT INTO reps_descleyen VALUES (12,1,11,'mat4Bim1');
INSERT INTO reps_descleyen VALUES (13,1,12,'mat5Bim1');
INSERT INTO reps_descleyen VALUES (14,1,13,'mat6Bim1');
INSERT INTO reps_descleyen VALUES (15,1,14,'mat7Bim1');
INSERT INTO reps_descleyen VALUES (16,1,15,'mat8Bim1');
INSERT INTO reps_descleyen VALUES (17,1,16,'mat9Bim1');
INSERT INTO reps_descleyen VALUES (18,1,17,'mat1Bim2');
INSERT INTO reps_descleyen VALUES (19,1,18,'mat1Bim3');
INSERT INTO reps_descleyen VALUES (20,1,19,'mat1Bim4');
INSERT INTO reps_descleyen VALUES (21,1,20,'mat1Bim5');
INSERT INTO reps_descleyen VALUES (22,1,21,'mat1Prom');
INSERT INTO reps_descleyen VALUES (23,1,22,'claveTec');
INSERT INTO reps_descleyen VALUES (24,1,23,'enfasisTec');
INSERT INTO reps_descleyen VALUES (25,1,24,'arte');
INSERT INTO reps_descleyen VALUES (26,1,25,'asignaturaEstatal');
INSERT INTO reps_descleyen VALUES (27,1,26,'promGralAnualEntero');
INSERT INTO reps_descleyen VALUES (28,1,27,'promGralAnualDecimal');
INSERT INTO reps_descleyen VALUES (29,1,28,'promGralEntero');
INSERT INTO reps_descleyen VALUES (30,1,29,'promGralDecimal');


CREATE TABLE reps_impresoras
(
    idimpresora INTEGER NOT NULL,
    idcct INTEGER NOT NULL,
    impresora VARCHAR(30) NOT NULL,
    PRIMARY KEY (idimpresora),
    UNIQUE (idcct, impresora)
);
INSERT INTO reps_impresoras VALUES
(1,0,'DAI');

CREATE TABLE reps_coordenadas
(
    idformato INTEGER NOT NULL,
    idconfigsimpre INTEGER NOT NULL,
    idfuente SMALLINT NOT NULL,
    idclasif SMALLINT NOT NULL,
    cveplan SMALLINT NOT NULL,
    grado SMALLINT NOT NULL,
    ordenimpre SMALLINT NOT NULL,
    idleyenda INTEGER NOT NULL,
    x SMALLINT NOT NULL,
    y SMALLINT NOT NULL,
    PRIMARY KEY (idformato, idconfigsimpre, idclasif, cveplan, grado, idleyenda)
);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,1,180,100);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,2,340,100);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,3,460,100);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,4,80,120);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,5,340,120);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,6,480,120);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,7,140,145);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,8,500,140);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,9,130,450);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,10,130,475);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,11,130,500);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,12,130,520);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,13,130,545);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,14,130,570);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,15,130,595);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,16,130,610);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,17,130,630);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,18,170,450);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,19,200,450);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,20,240,450);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,21,276,450);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,22,310,450);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,23,550,410);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,24,400,420);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,25,410,443);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,26,400,480);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,27,490,550);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,28,532,550);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,29,450,520);
INSERT INTO reps_coordenadas VALUES (2,3,1,1,30,480,520);

--
-- Relaciones
--
ALTER TABLE reps_coordenadas
    ADD CONSTRAINT FOREIGN KEY (idimpresora) REFERENCES reps_impresoras (idimpresora) CONSTRAINT idimpresora_FK_repsimpre,
    ADD CONSTRAINT FOREIGN KEY (idfuente) REFERENCES reps_tipografia (idfuente) CONSTRAINT idfuente_FK_repstipog,
    ADD CONSTRAINT FOREIGN KEY (idleyenda) REFERENCES reps_descleyen (idleyenda) CONSTRAINT idleyenda_FK_repsdesley;
    
ALTER TABLE reps_descleyen
    ADD CONSTRAINT FOREIGN KEY (idformato) REFERENCES reps_formatos (idformato) CONSTRAINT idformato_FK_repsformatos
--
-- Query para extraer las coordenadas
--
SELECT t.tamanio, t.fuente, l.ordenimpre, l.leyenda, c.x, c.y
FROM reps_coordenadas c, reps_impresoras i, reps_tipografia t, reps_descleyen l, reps_formatos f
WHERE c.idimpresora=i.idimpresora AND c.idfuente=t.idfuente AND c.idleyenda=l.idleyenda AND l.idformato=f.idformato
AND c.cveplan=2 AND c.grado=3 AND i.idcct=0 AND i.impresora='DAI' AND f.formato='CARTILLA' --8695
ORDER BY l.ordenimpre



SELECT c.idcoordenada, c.idimpresora, c.idfuente, c.idleyenda, c.x, c.y FROM reps_coordenadas c, reps_descleyen d WHERE c.idleyenda=d.idleyenda AND c.idimpresora=1 AND d.idformato=1 ORDER BY d.ordenimpres

select * from reps_coordenadas; --where idimpresora=3;
SELECT * FROM reps_impresoras;
select * from reps_descleyen order by ordenimpre;
SELECT * FROM reps_tipografia;
select * from reps_formatos;

delete from reps_coordenadas where idimpresora=1
update reps_coordenadas set grado=3

SELECT a.idalu, 
                    //"--ag.idcct, em.cveplan, em.cveprograma, em.cvetipmat, em.cvemat, em.grado,  em.cicescini, em.cicescfin, \n" +
                    m.cvetipmat, m.cvemat, m.desmat, 
                    (SELECT calnum FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.numeval=1) as bimestre1,
                    (SELECT calnum FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.numeval=2) as bimestre2,
                    (SELECT calnum FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.numeval=3) as bimestre3,
                    (SELECT calnum FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.numeval=4) as bimestre4,
                    (SELECT calnum FROM evaluaciones e WHERE e.idalu=ag.idalu AND e.cvetipmat=em.cvetipmat AND e.cvemat=em.cvemat  AND e.cicescini=ag.cicescini AND e.grado=ag.grado AND e.cveplan=ag.cveplan AND e.numeval=5) as bimestre5,
                    am.promedio,format(am.promedio,"#") as proment,
                    em.ordenimpres, ag.idcct, curp, a.apepat, a.apemat, a.nombre
                    FROM alumno a, alumnogrado ag, alumnomaterias am, esquemamaterias em, materias m 
                    WHERE em.cvetipmat=m.cvetipmat AND em.cvemat=m.cvemat
                    AND em.cveplan=am.cveplan AND em.cveprograma=am.cveprograma AND em.grado=am.grado AND em.cicescini<=am.cicescini AND am.cicescini<=em.cicescfin AND em.cvetipmat=am.cvetipmat AND em.cvemat=am.cvemat
                    AND am.cicescini=ag.cicescini AND am.grado=ag.grado AND am.cveplan=ag.cveplan AND am.idalu=ag.idalu AND ag.idalu=a.idalu
                    AND a.idalu=556773
                    AND ag.cveplan=2 AND (ag.Grupo = 'A' OR ag.Grupo='---') AND ag.idcct=8695 AND ag.grado=3 AND ag.cicescini=2012
                    ORDER BY a.apepat, a.apemat, a.nombre, a.curp, em.ordenimpres
                    
                    
SELECT a.idalu, curp, a.apepat, a.apemat, a.nombre,  g.promedio, g.promedioGral 
                    FROM alumno a, alumnogrado g 
                    WHERE a.idalu = g.idalu AND g.cicescini=2012 AND g.idcct=1826 AND g.Grado=1 AND 
                    (g.Grupo = 'A' OR g.Grupo='---') 
                    ORDER BY a.apepat, a.apemat, a.nombre, a.curp
                    
                    select idcct from escuela where cct='20DST0200U'
                    
