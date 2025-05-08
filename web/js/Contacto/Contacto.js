/* 
    Creado el : 28/01/2016, 12:21:32 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

function frmwContacto_Show ()
{
    //-------- Guardamos las variables a ser usadas en este formulario ---------
    object_setVisible (false,"gridTable");                                      // Ocultamos el gridTable
    $("#frmwContacto").css("display", "block");                                  // Mostramos el formulario correspondiente
    
    frmwContacto_Create ();
}

function frmwContacto_Close()
{
    irAVentanaPrincipal ();
}

function frmwContacto_Create ()
{
    if($('#frmfContacto').length)
        $('#frmfContacto').remove();
    $('#frmwContacto').append('<fieldset id="frmfContacto"><legend>Contacto</legend><div id="btnRegresar" class="divBtnRegresar" title="Regresar a la ventana anterior" tabindex="90"><label id="ibtnRegresar" class="icon-regresar"></label></div> </fieldset>');
        $("#frmfContacto").append('<div id="pnlContacto"></div>');
        
        $("#pnlContacto").append('<div id="pnlPanelGris"></div>');
            $("#pnlPanelGris").append('<div id="pnlIconoOperador">  <label id="iconOperador" class="icon-operador"></label></div>');
            $("#pnlPanelGris").append('<div id="pnlPanelDatos"></div>');
                $("#pnlPanelDatos").append('<div id="pnlTitulo"> <label>CONTÁCTENOS<label></div>');
                $("#pnlPanelDatos").append('<div id="pnlLeyenda"> <label>Para informar sobre problemas, dudas, o sugerencias que se presente con el SICEEO, puede comunicarse a la Delegación de Servicios Escolares de la región a la que usted pertenezca o al Departamento de Registro y Certificación Escolar de la Dirección de Planeación Educativa del IEEPO mediante:</label></div>');
                $("#pnlPanelDatos").append('<div id="pnlInformacion"></div>');
                    $("#pnlInformacion").append('<div id="pnlCorreo"></div>');
                        $("#pnlCorreo").append('<div id="pnlTituloCorreo"><label class="formatoIcono icon-mail"></label><label>Correo electrónico:</label></div>');
                        $("#pnlCorreo").append('<div id="pnlDatosCorreo">' +
                                                    '<div><div class="lblNivel"><label>PREESCOLAR:</label></div><label>preescertioax@gmail.com</label></div>' +
                                                    '<div><div class="lblNivel"><label>PRIMARIA:</label></div><label>primacertioax@gmail.com</label></div>' +
                                                    '<div><div class="lblNivel"><label>SECUNDARIA:</label></div><label>secundcertioax@gmail.com</label></div>' +
                                                '</div>');
                    $("#pnlInformacion").append('<div id="pnlTelefono"></div>');
                        $("#pnlTelefono").append('<div id="pnlTituloTelefono"><label class="formatoIcono icon-telefono"></label><label>Teléfonos:</label></div>');
                        $("#pnlTelefono").append('<div id="pnlDatosTelefono">'+
                                                    '<label>51 35045</label>'+
                                                    '<label>51 53900 Ext.482</label>'+
                                                '</div>');
                    $("#pnlInformacion").append('<div id="pnlJefeDepto"></div>');
                        $("#pnlJefeDepto").append('<div id="pnlTituloJefeDepto"><label class="formatoIcono icon-ejecutivo"></label><label>Jefe del departamento:</label></div>');
                        $("#pnlJefeDepto").append('<div id="pnlDatosJefeDepto"><div><label>Lic. Victor Manuel García.</label></div></div>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    //$("#btnRegresar").unbind("click");
    $("#btnRegresar").on("click",function(){ frmwContacto_Close(); });
}


