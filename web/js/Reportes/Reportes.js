/* 
    Creado el : 15/06/2017, 07:25:36 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

var jsReportes;

function frmwReportes_Show(tblPrincipal_selRow)
{
    jsReportes = {
        tblPrincipal_cicescini: tblPrincipal_selRow.cicescini,        
        tblPrincipal_cveplan: tblPrincipal_selRow.cveplan,
        tblPrincipal_idcct: tblPrincipal_selRow.idcct,
        tblPrincipal_cct: tblPrincipal_selRow.cct,
        tblPrincipal_modalidad: tblPrincipal_selRow.modalidad,
        tblPrincipal_grado:tblPrincipal_selRow.grado,
        tblPrincipal_grupo:tblPrincipal_selRow.grupo,
        tblPrincipal_cveunidad:tblPrincipal_selRow.cveunidad,
        
        hayCambios:false,
        cicescini: tblPrincipal_selRow.cicescini,
        cicescinilib: tblPrincipal_selRow.cicescini
    };
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwReportes").css("display", "block");                                 // Mostramos el formulario correspondiente
    
    frmwReportes_Create ();
    frmwReportes_FormActivate ();
}

function frmwReportes_Close()
{
    irAVentanaPrincipal();
}

function crearBotonReporte (contenedor, btnNombre, logo, titulo1, titulo2, titulo3, bandaInferior)
{
    //var tamLogo = [[0,0],[8,13],[8,5],[13,4],[17,2]];                         //Para un ícono con tamaño relativo
    var tamLogo = [[0,0],[17,22],[17,15],[17,9],[17,2],[18,2]];                        //Para un ícono con tamaño estático
    
    var estiloLogo = 'style="padding: '+(tamLogo[logo.length][0])+'px '+(tamLogo[logo.length][1]+(numCharRep (logo, "I")*4))+'px"';
    $("#"+contenedor).append('<div id="'+btnNombre+'" class="singleButton alinearHoriz">'
                    +'<div id="'+btnNombre+'_logo" class="logo" '+estiloLogo+'>'+logo+'</div>'
                    +((titulo1==="")?'':'<div id="'+btnNombre+'_titulo1" class="titulo1">'+titulo1+'</div>')
                    +((titulo2==="")?'':'<div id="'+btnNombre+'_titulo2" class="titulo2">'+titulo2+'</div>')
                    +((titulo3==="")?'':'<div id="'+btnNombre+'_titulo3" class="titulo3">'+titulo3+'</div>')
                    +'<label id="'+btnNombre+'_gradogrupo" class="bandaInferior">'+bandaInferior+'</label>'
                +'</div>');
    
    //Para que tengan una altura exacta
    var margin = 42;
    if (titulo2 === "" && bandaInferior !== "")
        margin -= 15;
    if (bandaInferior === "" && titulo2 !== "" )
        margin -= 19;
    if (bandaInferior !== "" && titulo2 !== "" )
        margin = 8;
    $("#"+contenedor+" #"+btnNombre+" .logo").css("margin-bottom",margin);
}

function numCharRep (texto, caracter)
{
    var contador = 0;
    var arrayText = texto.split("");
    for (var i=0; i<texto.length; i++)
        if (arrayText[i]===caracter)
            contador ++;
    return contador;
}

function frmwReportes_Create()
{
    var tabindexIni = 50, tabindexReturn=35;
    var gradoGrupo = jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo;
    
    if($('#frmfReportes').length)
        $('#frmfReportes').remove();
    $('#frmwReportes').append('<fieldset id="frmfReportes"><legend>Impresión de reportes</legend><div id="btnRegresar_Reportes" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfReportes").append('<div id="pnlReportes"></div>');
        
            $("#pnlReportes").append('<div id="pnlCambioDeCiclo">'
                                        + '<label id="lblEtiquetaCiclo">Ciclo escolar a trabajar: </label>'
                                        +'<div class="combobox"><select id="cbxCambioDeCiclo"></select></div>'
                                    +'</div>');
            
            crearBotonReporte ("pnlReportes","btnAcuseOfic", "AOP", "Acuse de oficialización", "de Preinscripción", "", "");             
            crearBotonReporte ("pnlReportes","btnRepInsc", "RI", "Reporte de", "Inscripción", "", gradoGrupo);
            crearBotonReporte ("pnlReportes","btnRepEval", "BE", "Boleta de", "Evaluación", "", gradoGrupo);              
            
            if (jsReportes.tblPrincipal_cveplan==="1" && jsReportes.tblPrincipal_grado==="6" || jsReportes.tblPrincipal_cveplan==="2" && jsReportes.tblPrincipal_grado==="3" || 
                      jsReportes.tblPrincipal_cveplan==="3" && jsReportes.tblPrincipal_grado==="3")  //para pree
            /*if (jsReportes.tblPrincipal_cveplan==="2" && jsReportes.tblPrincipal_grado==="3" || jsReportes.tblPrincipal_cveplan==="3" && jsReportes.tblPrincipal_grado==="3" ) */
                crearBotonReporte ("pnlReportes","btnCertificado", "C", "Certificado", "", "", gradoGrupo); 
            
            switch (jsReportes.tblPrincipal_cveplan)
            {
                case "3":
                        crearBotonReporte ("pnlReportes","btnIAR", "IAR", "Fin de curso", "Reporte IAR", "", gradoGrupo);
                         if (jsReportes.tblPrincipal_grado==="3"){                         
                             crearBotonReporte ("pnlReportes","btnRELP", "RELP", "Fin de curso", "Reporte REL-P", "", gradoGrupo);
                         }
                    break;
                case "1":
                        if (jsReportes.tblPrincipal_grado==="6"){
                            crearBotonReporte ("pnlReportes","btnCREL", "CREL", "Fin de curso", "Reporte CREL", "", gradoGrupo);
                            crearBotonReporte ("pnlReportes","btnCRELc", "CRELc", "Fin de curso", "Reporte CREL Complementaria", "", gradoGrupo);
                        }
                        crearBotonReporte ("pnlReportes","btnIAE", "IAE", "Fin de curso", "Reporte IAE", "", gradoGrupo);
                        crearBotonReporte ("pnlReportes","btnIAEc", "IAEc", "Fin de curso", "Reporte IAE Complementaria", "", gradoGrupo);
                    break;
                case "2":
                        if (jsReportes.tblPrincipal_grado==="3"){
                            crearBotonReporte ("pnlReportes","btnREL", "REL", "Fin de curso", "Reporte REL", "", gradoGrupo);
                            crearBotonReporte ("pnlReportes","btnRELc", "RELc", "Fin de curso", "Reporte REL Complementaria", "", gradoGrupo);
                        }
                        crearBotonReporte ("pnlReportes","btnR", "R", "Fin de curso", "Reporte R", "", gradoGrupo);
                        crearBotonReporte ("pnlReportes","btnRc", "Rc", "Fin de curso", "Reporte R complementaria", "", gradoGrupo);
                        crearBotonReporte ("pnlReportes","btnKardex", "KX", "Fin de Curso", "Kardex", "", gradoGrupo);  
                        crearBotonReporte ("pnlReportes","btnCER", "CER", "Constancia de", "Regularización", "", gradoGrupo);  
                    break;
            }
        $("#frmfReportes").append('<div id="mwfmSelMesCompl" class="mwfModal" style="display:none"></div>');  

    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnRegresar_Reportes").on("click",function(){ frmwOficializar_Close(); });
    
    $("#cbxCambioDeCiclo").on("change", function(){ cbxCambioDeCiclo_ChangeSelectedItem (); });
    $("#btnRepInsc").on("click", function(){ btnReporteDeInscripcion_ActionPerformed (); });
    $("#btnRepEval").on("click", function(){ btnRepEval_ActionPerformed (); });
    $("#btnKardex").on('click',function(){ btnKardex_Click(); });
    $("#btnAcuseOfic").on("click", function(){ btnAcuseOfic_ActionPerformed (); });
    $("#btnCertificado").on("click", function(){ btnCertificado_ActionPerformed (); });
    $("#btnIAR").on("click", function(){ finDeCurso ("IAR"); });
    $("#btnCREL").on("click", function(){ finDeCurso ("CREL"); });
    $("#btnCRELc").on("click", function(){ elegirComplementaria ("CRELc"); });
    $("#btnIAE").on("click", function(){ finDeCurso ("IAE"); });
    $("#btnIAEc").on("click", function(){ elegirComplementaria ("IAEc"); });
    $("#btnREL").on("click", function(){ finDeCurso ("REL"); });
    $("#btnRELP").on("click", function(){ finDeCurso ("RELP"); });
    $("#btnRELc").on("click", function(){ elegirMesCertComplem ("RELc"); });
    $("#btnR").on("click", function(){ finDeCurso ("R"); });
    $("#btnRc").on("click", function(){ elegirComplementaria ("Rc"); });
    $("#btnCER").on("click", function(){ generarConstancias ("CER"); });        
}

function frmwReportes_FormActivate ()
{
    /*$("#btnRepEval_gradogrupo").text(jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo);
    $("#btnCertificado_gradogrupo").text(jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo);
    $("#btnIAR_gradogrupo").text(jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo);
    $("#btnIAE_gradogrupo").text(jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo);
    $("#btnCREL_gradogrupo").text(jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo);
    $("#btnR_gradogrupo").text(jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo);
    $("#btnREL_gradogrupo").text(jsReportes.tblPrincipal_grado+jsReportes.tblPrincipal_grupo);*/
    var mensaje = new Mensajes();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Re", metodo:"foAc",usuario:sisVars.usuario 
    };
    //------------------------- Hacemos la llamada -------------------------
    cargarLoading();
    $.ajax({url:"../sis_web/siS1",
        type:"POST",
        dataType:"JSON",
        data: datos,
        async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    //-------------------- Asignamos variables ---------------------
                    if (!result.cbxCambioDeCiclo_setVisible)
                        $('#pnlReportes #pnlCambioDeCiclo').remove();
                    else {
                        $("#pnlReportes #cbxCambioDeCiclo").append(result.cbxCambioDeCiclo);                        
                    }
                    
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        //cerrarLoading();
    })
    .always(function() {
        cerrarLoading();
    });
}

function cbxCambioDeCiclo_ChangeSelectedItem ()
{
    jsReportes.cicescinilib = $("#pnlReportes #cbxCambioDeCiclo").val();
}

function btnReporteDeInscripcion_ActionPerformed ()
{
    var mensaje = new Mensajes();
    //var tabla = new Tabla();
    //var posSelActual = tabla.getSelectedIndexRow ('tblCamDeGpo');
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    window.open("Grupo/reporteDeInscripcion.jsp?cicescini="+jsReportes.cicescini+"&cicescinilib="+jsReportes.cicescinilib
            +"&cveplan="+jsReportes.tblPrincipal_cveplan+"&idcct="+jsReportes.tblPrincipal_idcct+"&grado="+jsReportes.tblPrincipal_grado
            +"&grupo="+jsReportes.tblPrincipal_grupo);
    
}

function btnKardex_Click()
{
    var mensaje = new Mensajes();
    
    mensaje.CalifSecXBim("KARDEX_EN_CREACION","","","");
    //window.open("Calificaciones/Kardex.jsp?r=Calificaciones/Kardex/Kardex&cveunidad="+jsReportes.tblPrincipal_cveunidad+"&califCicEscIn="+jsReportes.cicescini+"&grado="+jsReportes.tblPrincipal_grado+"&grupo="+jsReportes.tblPrincipal_grupo+"&idcct="+jsReportes.tblPrincipal_idcct);
    showReport ("frmfReportes", "Calificaciones/Kardex.jsp", {r:"Calificaciones/Kardex", cveunidad:jsReportes.tblPrincipal_cveunidad, 
                                                                    cicescini:jsReportes.cicescini, califCicEscIn:jsReportes.cicescinilib, idcct:jsReportes.tblPrincipal_idcct, 
                                                                    grado:jsReportes.tblPrincipal_grado, grupo:jsReportes.tblPrincipal_grupo});
    
}

                                
function btnRepEval_ActionPerformed ()
{
    var mensaje = new Mensajes();
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    showReport ("frmfReportes", "Reportes/ReporteDeEvaluacion.jsp", {cicescini:jsReportes.cicescini,cicescinilib:jsReportes.cicescinilib, cveplan:jsReportes.tblPrincipal_cveplan, 
                                                                    idcct:jsReportes.tblPrincipal_idcct, modalidad:jsReportes.tblPrincipal_modalidad, 
                                                                    grado:jsReportes.tblPrincipal_grado, grupo:jsReportes.tblPrincipal_grupo});
                
}

function btnAcuseOfic_ActionPerformed ()
{
    var mensaje = new Mensajes();
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    showReport ("frmfReportes", "Reportes/acuseOficPreinsc.jsp", {cicesciniPreinsc:parseInt(jsReportes.cicescini)+1, 
                                                                  idcct:jsReportes.tblPrincipal_idcct, r:"Preinscripcion/acuseOficPreinsc"});
}

function btnCertificado_ActionPerformed ()
{
    var mensaje = new Mensajes();
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    showReport ("frmfReportes", "Reportes/Certificado.jsp", {cicescini:jsReportes.cicescini,cicescinilib:jsReportes.cicescinilib, cveplan:jsReportes.tblPrincipal_cveplan, 
                                                             idcct:jsReportes.tblPrincipal_idcct, grado:jsReportes.tblPrincipal_grado, 
                                                             grupo:jsReportes.tblPrincipal_grupo});                
}

function finDeCurso (caso)
{
    var mensaje = new Mensajes();
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    showReport ("frmfReportes", "Reportes/FinDeCurso.jsp", {cicescini:jsReportes.cicescini,cicescinilib:jsReportes.cicescinilib, cveplan:jsReportes.tblPrincipal_cveplan, 
                                                            idcct:jsReportes.tblPrincipal_idcct, modalidad:jsReportes.tblPrincipal_modalidad, 
                                                            grado:jsReportes.tblPrincipal_grado, grupo:jsReportes.tblPrincipal_grupo, caso:caso});
}

function generarConstancias(caso){
var mensaje = new Mensajes();
    
    mensaje.General("REPORTE_EN_CREACION","","","");
    showReport ("frmfReportes", "Reportes/Constancias.jsp", {cicescini:jsReportes.cicescini,cicescinilib:jsReportes.cicescinilib, cveplan:jsReportes.tblPrincipal_cveplan, 
                                                            idcct:jsReportes.tblPrincipal_idcct, modalidad:jsReportes.tblPrincipal_modalidad, 
                                                            grado:jsReportes.tblPrincipal_grado, grupo:jsReportes.tblPrincipal_grupo, caso:caso});
}

function elegirComplementaria (casoRep)
{
    mwfComplementaria_Show ( {cicescinilib:jsReportes.cicescinilib,cicescini:jsReportes.cicescini, cveplan:jsReportes.tblPrincipal_cveplan, cct:jsReportes.tblPrincipal_cct, 
                            idcct:jsReportes.tblPrincipal_idcct, modalidad:jsReportes.tblPrincipal_modalidad, grado:jsReportes.tblPrincipal_grado, 
                            grupo:jsReportes.tblPrincipal_grupo }, casoRep);
}

function elegirMesCertComplem (casoRep)
{
    mwfSelMesCompl_Show ( {cicescini:jsReportes.cicescini, cicescinilib:jsReportes.cicescinilib, cveplan:jsReportes.tblPrincipal_cveplan, cct:jsReportes.tblPrincipal_cct, 
                            idcct:jsReportes.tblPrincipal_idcct, modalidad:jsReportes.tblPrincipal_modalidad, grado:jsReportes.tblPrincipal_grado, 
                            grupo:jsReportes.tblPrincipal_grupo });
}

