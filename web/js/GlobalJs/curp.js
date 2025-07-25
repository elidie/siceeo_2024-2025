//******************************************
//          VALIDACION DE CURP
//Ver. 1.1
//Fecha modif: 19/08/2013 12:51
//******************************************

/*VARIABLES GLOBALES PARA CALCULAR LA CURP*/
    var ATAB1 = new Array("DA ","DAS ","DE ","DEL ","DER ","DI ","DIE ","DD ","EL " ,"LA ","LOS ","LAS ","LE ","LES ","MAC ","MC ","VAN ","VON ","Y ");
    var ATAB2 = new Array("BACA","BAKA","BUEI","BUEY","CACA","CACO","CAGA","CAGO","CAKA","CAKO","COGE","COGI","COJA","COJE","COJI","COJO","COLA","CULO","FALO","FETO","GETA","GUEI","GUEY","JETA",
                            "JOTO","KACA","KACO","KAGA","KAGO","KAKA","KAKO","KOGE","KOGI","KOJA","KOJE","KOJI","KOJO","KOLA","KULO","LILO","LOCA","LOCO","LOKA","LOKO","MAME","MAMO","MEAR","MEAS",
                            "MEON","MIAR","MION","MOCO","MOKO","MULA","MULO","NACA","NACO","PEDA","PEDO","PENE","PIPI","PITO","POPO","PUTA","PUTO","QULO","RATA","ROBA","ROBE","ROBO","RUIN","SENO",
                            "TETA","VAGA","VAGO","VAKA","VUEI","VUEY","WUEI","WUEY");
    var VAPL1="",VAPL11="",VAPL2="",VAPL21="",VNOM="",VNOM1="";
    var VFEC_ANIO="",VFEC_MES="",VFEC_DIA="",VSEXO="",VENT="",laU="";
    var VLEN=""; // Se utilizará como entero 

function ValidarCurp(nombre,ape1,ape2,anioNac,mesNac,diaNac,sexo,entidad){    
    VNOM = nombre.toUpperCase();;
    VAPL1 = ape1.toUpperCase();
    VAPL2 = ape2.toUpperCase();
    VFEC_ANIO = anioNac.toUpperCase();
    VFEC_MES = mesNac.toUpperCase();
    VFEC_DIA = diaNac.toUpperCase();
    VSEXO = sexo.toUpperCase();
    VENT = entidad.toUpperCase();

    /* -------------------Métodos de la PseudoClase ---------------------------*/
    
    ValidarCurp.prototype.curp = function(){
        var VI=""; // int
        var VLET="",VRAIZ=""; //string
        var vocaLes = "AEIOU";
        //   *** QUITA / ' . 
        VAPL11 = ValidarCurp.prototype.P8($.trim(VAPL1));
        VAPL21 = ValidarCurp.prototype.P8($.trim(VAPL2));
        VNOM1 = ValidarCurp.prototype.P8($.trim(VNOM));
        //** QUITA CARACTERES ESPECIALES
        VAPL11 = ValidarCurp.prototype.P7(VAPL11);
        VAPL21=ValidarCurp.prototype.P7(VAPL21);
        VNOM1=ValidarCurp.prototype.P7(VNOM1);
        //** QUITA MARIA Y JOSE
        VNOM1=ValidarCurp.prototype.P9(VNOM1);
        //** QUITA PROPOSICIONES
        VAPL11=ValidarCurp.prototype.P10(VAPL11);
        VAPL21=ValidarCurp.prototype.P10(VAPL21);
        VNOM1=ValidarCurp.prototype.P10(VNOM1);
        //*** QUITO PALABRAS COMPUESTAS
        VAPL11=ValidarCurp.prototype.P11(VAPL11);
        VAPL21=ValidarCurp.prototype.P11(VAPL21);
        VNOM1=ValidarCurp.prototype.P11(VNOM1);
        //*** CREA LAS PRIMERAS 4 LETRAS DE LA RAIZ
        //*** APELLIDO PATERNO
        if (VAPL11.length===0)
            VRAIZ = "XX";
        else 
        {
            VRAIZ = ""+VAPL11.charAt(0);
            VLET  = "X";
            for (VI=1; VI<VAPL11.length; VI++)
            {
              //if ("AEIOU".contains(""+VAPL11.charAt(VI)))
              if( vocaLes.indexOf(""+VAPL11.charAt(VI)) >= 0 )
              {
                 VLET = ""+VAPL11.charAt(VI);
                 break ; //cancela el ciclo
              }
            }
            VRAIZ = VRAIZ+VLET;
        }

        //   *** APELLIDO MATERNO
        if (VAPL21.length===0) VRAIZ += "X";
        else VRAIZ += VAPL21.charAt(0);
        
        //   *** NOMBRE
        if (VNOM1.length===0) VRAIZ += "X";
        else VRAIZ += VNOM1.charAt(0);

        for(VI=0; VI<=80; VI++)
        {
            //if (VRAIZ.equals(ATAB2[VI]))
            if (VRAIZ === ATAB2[VI])                
            {
                VRAIZ = VRAIZ.charAt(0)+"X"+VRAIZ.substring(VRAIZ.length-2,VRAIZ.length); //RIGHT(VRAIZ,2)
                break; //EXIT
            }
        }
        
        //   *** FECHA NACIMIENTO, SEXO Y E.F.
        VRAIZ += VFEC_ANIO+VFEC_MES+VFEC_DIA+VSEXO+VENT;
       
        //   *** CONSONANTES INTERNAS
        VRAIZ = ValidarCurp.prototype.P12(VAPL11,VRAIZ);
        VRAIZ = ValidarCurp.prototype.P12(VAPL21,VRAIZ);
        VRAIZ = ValidarCurp.prototype.P12(VNOM1,VRAIZ);
        //   *** FIN DE RUTINAS
        return VRAIZ;
    };
    // SUSTITUYE CARACTERES ESPECIALES POR X
    ValidarCurp.prototype.P7 = function(VPASO){
        // Retorna variable Tipo String
        
        var VI=""; //int
        var VLETRA=''; //char
        
        for (VI=0; VI<VPASO.length; VI++)
        {
            VLETRA = VPASO.charAt(VI);
            if (VLETRA ==='Ä' || VLETRA ==='Ë' || VLETRA ==='Ï' || VLETRA==='Ö' || VLETRA==='Ü' || VLETRA ==='Á' || VLETRA ==='É' || VLETRA ==='Í' || VLETRA==='Ó' || VLETRA==='Ú') 
            {
               if (VLETRA==='Ä' || VLETRA==='Á') VPASO = VPASO.substring(0,VI)+'A'+VPASO.substring(VI+1);
               if (VLETRA==='Ë' || VLETRA==='É') VPASO = VPASO.substring(0,VI)+'E'+VPASO.substring(VI+1);
               if (VLETRA==='Ï' || VLETRA==='Í') VPASO = VPASO.substring(0,VI)+'I'+VPASO.substring(VI+1);
               if (VLETRA==='Ö' || VLETRA==='Ó') VPASO = VPASO.substring(0,VI)+'O'+VPASO.substring(VI+1);
               if (VLETRA==='Ü' || VLETRA==='Ú') VPASO = VPASO.substring(0,VI)+'U'+VPASO.substring(VI+1);
               laU="si";
            }else
                if ((VLETRA.charCodeAt(0)< 65 || VLETRA.charCodeAt(0)> 90) && VPASO.charAt(VI)!== ' ')
                    VPASO  = VPASO.substring(0,VI)+'X'+VPASO.substring(VI+1);
        }
        return VPASO;
    };
    // QUITA LAS / Y '
    ValidarCurp.prototype.P8 = function(VPASO){
        var VI=""; //int
        
        //VLEN = LEN(VPASO)
        for(VI=0; VI<VPASO.length;VI++)
        {
            if (VPASO.charAt(VI)==='/' || VPASO.charAt(VI)==='\'' || VPASO.charAt(VI)==='.')
            {
                //VLEFT  = LEFT(VPASO,VI-1)
                //VRIGHT = RIGHT(VPASO,VLEN-VI)
                VPASO = VPASO.substring(0,VI)+' '+VPASO.substring(VI+1);
            }
        }
        return $.trim(VPASO);
    };
    // QUITA JOSE Y MARIA
    ValidarCurp.prototype.P9 = function(VPASO){
        
        if( VPASO.length===4 && (VPASO.indexOf("JOSE") >= 0 || VPASO.indexOf("JOSÉ") >= 0) ) return VPASO;
        if( VPASO.length===1 && VPASO.indexOf("J") >= 0 ) return VPASO;
        if( VPASO.length===2 && VPASO.indexOf("J ") >= 0 ) return VPASO;
        if( VPASO.length===5 && (VPASO.indexOf("MARIA") >= 0 || VPASO.indexOf("MARÍA") >= 0)) return VPASO;
        if( VPASO.length===1 && VPASO.indexOf("M") >= 0 ) return VPASO;
        if( VPASO.length===2 && VPASO.indexOf("M ") >= 0 ) return VPASO;
        if( VPASO.length===2 && VPASO.indexOf("MA") >= 0 ) return VPASO;
        if( VPASO.length===3 && VPASO.indexOf("MA ") >= 0 ) return VPASO;
        
        if (VPASO.length>=5 && (VPASO.substring(0,5) === "JOSE " || VPASO.substring(0,5) === "JOSÉ "))
            VPASO =  VPASO.substring(5);
        else if (VPASO.length>=3 && VPASO.substring(0,3) === "J  ") VPASO =  VPASO.substring(3);
        else if (VPASO.length>=2 && VPASO.substring(0,2) === "J ") VPASO =  VPASO.substring(2);
        else if (VPASO.length>=6 && (VPASO.substring(0,6) === "MARIA " || VPASO.substring(0,6) === "MARÍA ")) 
            VPASO =  VPASO.substring(6);
        else if (VPASO.length>=3 && VPASO.substring(0,3) === "M  ") VPASO =  VPASO.substring(3);
        else if (VPASO.length>=2 && VPASO.substring(0,2) === "M ") VPASO =  VPASO.substring(2);
        else if (VPASO.length>=4 && VPASO.substring(0,4) === "MA  ") VPASO =  VPASO.substring(4);
        else if (VPASO.length>=3 && VPASO.substring(0,3) === "MA ") VPASO =  VPASO.substring(3);
        
        return VPASO;
    };
    // QUITA PREPOSICIONES
    ValidarCurp.prototype.P10 = function(VPASO){
        var VI="", tamATAB1=""; //int
        VI = 0;
        while (VI < 19)
        {
            tamATAB1 = ATAB1[VI].length;
            if ( VPASO.length>=tamATAB1 && VPASO.substring(0,tamATAB1) === ATAB1[VI] )
            {
              VPASO = VPASO.substring(ATAB1[VI].length);
              VI = 0;
            }
            else
              VI++;
        }
        return VPASO;
    };
    // QUITA PALABRAS COMPUESTAS
    ValidarCurp.prototype.P11 = function(VPASO){
        var VI=""; //int
        
        for(VI=0; VI<VPASO.length; VI++)
            {
           if (VPASO.charAt(VI)===' ')
               {
                  VPASO = VPASO.substring(0,VI);
                  break;
               }
            }
            return VPASO;
    };
    // CONSONANTES INTERNAS
    ValidarCurp.prototype.P12 = function(VPASO,VRAIZ){
        var VLET="";//string
        var VI=""; //int
        var cadena = "BCDFGHJKLMNPQRSTVWXYZ";

        if(VPASO.length===0)
           VRAIZ = VRAIZ+'X';
        else
        {
           VLET = "X";
           for (VI=1; VI<VPASO.length; VI++)
           {
              if( cadena.indexOf(VPASO.charAt(VI)) >= 0 )
              {
                 VLET = VPASO.charAt(VI);
                 return VRAIZ = VRAIZ+VLET;
              }
           }
           VRAIZ = VRAIZ+VLET ; //POR SI NO ENCUENTRA UNA CONSONANTE LE AGREGA UNA X
        }
        return VRAIZ;
    };
}