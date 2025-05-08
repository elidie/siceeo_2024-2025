/* 
    Creado el : 24-ene-2017, 9:43:39
    Autor     : Ing. Maai Nolasco Sánchez
*/
var jsPermisos;

function mwfPermisos_Show()
{
    jsPermisos={
        tiposUsr:null
    };
    object_setVisible (true,"mwfmPermisos");                                      // Ocultamos el gridTable
    //$("#mwffPermisos").css("display", "block");                          // Mostramos el formulario correspondiente
    mwfPermisos_Create();
    mwffPermisos_FormActivate ();
}

function mwfPermisos_Close()
{
    $('#mwfmPermisos #mwffPermisos').remove();
    object_setVisible (false,"mwfmPermisos");
    $("#txtBusquedaCCT").focus();
}

function mwfPermisos_Create ()
{
    modalWindow_Create ("Permisos", "Configuración de permisos",mwfPermisos_Close, 90, {anchoAutoajustable:true});
    $('#mwfpPermisos').append('<div id="pnlPermisos"></div>');
        $('#pnlPermisos').append('<div id="pnlTipoUsuarios" class="panel">'
                                    + '<label class="tituloPanel">Tipos de usuario</label>'
                                    + '<div id="pnlTblTipoUsuarios">  <div id="scrlTipoUsuarios" class="scrollTable"></div>  </div>'
                                    +'<ul class="buttonBar"> '
                                        +'<li><a href="#" id="btnGuardaTipoUsuarios"><label class="icon-disquete"></label> Guardar cambios </a></li>'
                                    +'</ul>'
                                +'</div>');
        $('#pnlPermisos').append('<div id="pnlUsuariosEsp" class="panel"><label class="tituloPanel">Alterar usuario y permiso específico</label></div>');
            $('#pnlUsuariosEsp').append('<div id="pnlTablaUsuariosEsp" class="panel"><label class="tituloPanel">Excepciones configuradas</label></div>');
                $('#pnlTablaUsuariosEsp').append('<div id="pnlFiltrarUsuario">'
                                                    + '<label id="lblFiltrarUsuario" class="alinearHoriz">Usuario a filtrar:</label> '
                                                    +'<input type="text" id="txtFiltrarUsuario" class="alinearHoriz" tabindex="100" /> '
                                                    + '<ul class="alinearHoriz buttonBar">  <li><a href="#" id="btnFiltrarUsuario" tabindex="102" title="Filtrar usuario"><label id="ibtnFiltrarUsuario" class="icon-lupa"></label> Filtrar</a></li>  </ul> '
                                                +'</div>');
                $('#pnlTablaUsuariosEsp').append('<div id="pnlTblUsuariosEsp">  <div id="scrlUsuariosEsp" class="scrollTable"></div>  </div>'
                                                +'<ul class="buttonBar"> '
                                                    +'<li><a href="#" id="btnGuardaUsuariosEsp"><label class="icon-disquete"></label> Guardar cambios </a></li> '
                                                    +'<li><a href="#" id="btnEliminarUsuariosEsp"><label class="icon-delete"></label> Eliminar permiso</a></li>'
                                                +'</ul>');
            $('#pnlUsuariosEsp').append('<div id="pnlAgregarConfig" class="panel"><label class="tituloPanel">Crear excepción</label>'
                                            + '<div id="pnlBuscarUsuario">'
                                                + '<label id="lblBuscarUsuario" class="alinearHoriz">Usuario a buscar:</label> '
                                                +'<input type="text" id="txtBuscarUsuario" class="alinearHoriz" tabindex="100" /> '
                                                + '<ul class="alinearHoriz buttonBar">  <li><a href="#" id="btnBuscarUsuario" tabindex="102" title="Buscar usuario"><label id="ibtnBuscarUsuario" class="icon-lupa"></label> Buscar</a></li>  </ul> '
                                            +'</div>'
                                            + '<div id="pnlConfigPermiso">'
                                                +'<div class="alinearHoriz"><label>Usuario: <select id="cbxUsrEncontrados" name="cbxUsrEncontrados" tabindex="108"></select></label></div>'
                                                +'<div class="alinearHoriz"><label>Componente: <select id="cbxComponenteAPermitir" name="cbxComponenteAPermitir" tabindex="108"></select></label></div>'
                                                +'<div class="alinearHoriz"><label>Permiso: <select id="cbxPermisoDeComponente" name="cbxPermisoDeComponente" tabindex="108"> <option value="true">ACTIVO</option> <option value="false">INACTIVO</option> </select></label></div>'
                                                + '<ul class="alinearHoriz buttonBar">  <li><a href="#" id="btnInsertUsuariosEsp" tabindex="102" title="Insertar configuración"><label id="ibtnUsuariosEsp"></label> Insertar configuración</a></li>  </ul> '
                                            +'</div>'
                                        +'</div>');
    
    //------------------------------------------ ACTIVACIÓN DE EVENTOS -------------------------------------------------
    
    $("#btnGuardaTipoUsuarios").on('click',function(){ btnGuardaTipoUsuarios_Click(); });
    $("#btnFiltrarUsuario").on('click',function(){ btnFiltrarUsuario_Click(); });
    $("#btnGuardaUsuariosEsp").on('click',function(){ btnGuardaUsuariosEsp_Click(); });
    $("#btnEliminarUsuariosEsp").on('click',function(){ btnEliminarUsuariosEsp_Click(); });
    $("#btnBuscarUsuario").on('click',function(){ btnBuscarUsuario_Click(); });
    $("#btnInsertUsuariosEsp").on('click',function(){ btnInsertUsuariosEsp_Click(); });
}
function mwffPermisos_FormActivate ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pe", metodo:"geTiUsCoPe", txtUsuario:sisVars.usuario
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
                    jsPermisos.tiposUsr = result.tiposUsr.slice(0);             //Hacemos un clon por valor
                    result.tiposUsr.splice(0, 0, "componente", "formulario");   //Agregamos al inicio dos datos
                    for (var i=0; i<result.tiposUsr.length; i++){
                        result.tiposUsr[i]=result.tiposUsr[i].toLowerCase();
                        result.tiposUsr[i]=result.tiposUsr[i].replace(" ","_");
                    }
                            
                    tabla.create("scrlTipoUsuarios","tblTipoUsuarios",result.tblTipoUsuarios, result.tiposUsr, result.tiposUsr, ["","","checkbox","checkbox", "checkbox", "checkbox","checkbox","checkbox"], ["","","CENTER", "CENTER", "CENTER","CENTER","CENTER","CENTER"], true, null, null, null);
                    for (var f=0; f<result.tblTipoUsuarios.length; f++)
                    {
                        $.each(result.tblTipoUsuarios[f],function(nombre,dato) {
                            $("#tblTipoUsuarios .tblTipoUsuarios_"+nombre+"_f"+f+" :checkbox").prop( "checked", (dato===true?dato:(dato==='t'?true:false)) );
                        });
                    }
                    
                    $.each(result.cbxComponenteAPermitir,function(clave,item) {
                        $("#cbxComponenteAPermitir").append("<option value='"+item.idobjeto+"' title='"+item.objeto.trim()+"'>"+item.objeto.trim()+"</option>");                        
                    });
                    
                    setTblUsuariosEsp (result.tblUsuariosEsp);
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
    })
    .always(function() {
        cerrarLoading();
    });
}

function setTblUsuariosEsp (tblUsuariosEsp, caso)
{
    var tabla = new Tabla();
    var permiso;
    
    if (typeof caso==="undefined" || caso==="CREAR"){
        tabla.create("scrlUsuariosEsp","tblUsuariosEsp",tblUsuariosEsp, ["USUARIO","COMPONENTE","FORMULARIO", "PERMISO"], ["loginuser", "componente", "formulario", "permiso"], ["","","","checkbox"], ["","","", "CENTER"], true, null, null, null);
        //Establecemos checkeo del checkbox según corresponda
        for (var f=0; f<tblUsuariosEsp.length; f++){
            permiso = tblUsuariosEsp[f].permiso;
            $("#tblUsuariosEsp .tblUsuariosEsp_permiso_f"+f+" :checkbox").prop( "checked", (permiso===true?permiso:(permiso==='t'?true:false)) );
        }
    }else if (caso==="INSERTAR"){
        tabla.addRows (-1,"tblUsuariosEsp",tblUsuariosEsp, ["loginuser", "componente", "formulario", "permiso"], ["","","","checkbox"], ["","","", "CENTER"], true, null, null, null);
        //Establecemos checkeo del checkbox según corresponda
        var numRows = tabla.getNumRows("tblUsuariosEsp");
        $("#tblUsuariosEsp .tblUsuariosEsp_permiso_f"+(numRows-1)+" :checkbox").prop( "checked", tblUsuariosEsp[0].permiso );
    }
}

function btnGuardaTipoUsuarios_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    var numCols = tabla.getNumCols ("tblTipoUsuarios");

    //Hacemos un preparativo llenando un arreglo con las columnas que se van a extraer
    var colsNameForTblTipoUsuarios = new Array();
    colsNameForTblTipoUsuarios[0]="idobjeto";                                   //iniciamos con el nombre de columna idobjeto
    for (var i=2, j=1; i<numCols; i++, j++){                                    //Obtenemos los demas nombres de las columnas (sólo los checkbox nos interesan)
        colsNameForTblTipoUsuarios[j] = i;
        colsNameForTblTipoUsuarios[j+(numCols-2)] = i+"_val";
    }
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pe", metodo:"guTiUsCoPe", txtUsuario:sisVars.usuario, tiposUsr:jsPermisos.tiposUsr,
        tblTipoUsuarios:tabla.getTable ("tblTipoUsuarios", colsNameForTblTipoUsuarios)
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
                    //---------- Actualizamos el permiso de respaldo _oldValue ----------\\
                    var tblTipoUsuarios = tabla.getTable ("tblTipoUsuarios", colsNameForTblTipoUsuarios,null,"JSON");
                    var permiso, permiso_oldValue;
                    for (var f=0; f<tblTipoUsuarios.length; f++){
                        $.each(tblTipoUsuarios[f],function(col,valor) {
                            if (col.indexOf("_val")<0){                         //No tomamos en cuenta las columnas que en su nombre contengan _val
                                permiso = ""+valor;
                                permiso_oldValue = ""+$("#tblTipoUsuarios #tblTipoUsuarios_chk_f"+f+"_c"+col).val();
                                if (permiso.charAt(0)!==permiso_oldValue.charAt(0))
                                    $("#tblTipoUsuarios #tblTipoUsuarios_chk_f"+f+"_c"+col).val( permiso.charAt(0) );
                            }
                        });
                    }
                    //--------------------------------------------------------------------\\
                    mensaje.General("GUARDADO_EXITOSO");
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
    })
    .always(function() {
        cerrarLoading();
    });
}

function btnFiltrarUsuario_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pe", metodo:"btFiUs_Cl", txtUsuario:sisVars.usuario, txtFiltrarUsuario:$("#txtFiltrarUsuario").val()
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
                   setTblUsuariosEsp (result.tblUsuariosEsp); 
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
    })
    .always(function() {
        cerrarLoading();
    });
}

function btnGuardaUsuariosEsp_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pe", metodo:"btGuUsEs_Cl", txtUsuario:sisVars.usuario, tblUsuariosEsp:tabla.getTable ("tblUsuariosEsp", ["idaccesoespecifico","permiso","permiso_val"])
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
                    //---------- Actualizamos el permiso de respaldo _oldValue ----------\\
                    var tblUsuariosEsp = tabla.getTable ("tblUsuariosEsp", ["idaccesoespecifico","permiso","permiso_val"],null,"JSON");
                    var permiso, permiso_oldValue;
                    for (var f=0; f<tblUsuariosEsp.length; f++){
                        permiso = tblUsuariosEsp[f].permiso;
                        permiso_oldValue = tblUsuariosEsp[f].permiso_val;
                        if (permiso.charAt(0)!==permiso_oldValue.charAt(0))
                            $("#tblUsuariosEsp .tblUsuariosEsp_permiso_f"+f+" :checkbox").val( permiso.charAt(0) );
                    }
                    //--------------------------------------------------------------------\\
                   mensaje.General("GUARDADO_EXITOSO");
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
    })
    .always(function() {
        cerrarLoading();
    });
}

function btnEliminarUsuariosEsp_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    var tabla = new Tabla();
    
    var posFilaSel = tabla.getSelectedIndexRow("tblUsuariosEsp");
    if (posFilaSel === -1)
        mensaje.General("NO_SELEC"," componente","eliminarlo");
    else{
        var tblUsuariosEsp = tabla.getSelectedRow("tblUsuariosEsp",["idaccesoespecifico"],null,"JSON");
        //------------------ Establecemos los datos a enviar -------------------
        var datos = {
            modulo:"Pe", metodo:"btElUsEs_Cl", txtUsuario:sisVars.usuario, idaccesoespecifico:tblUsuariosEsp.idaccesoespecifico
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
                        tabla.removeRow ("tblUsuariosEsp", posFilaSel);
                        mensaje.General("PROCESO_EXITOSO");
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    break;
                case -10:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        btnCerrarSesion_ActionPerformed ();
                    break;
                default:break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
        })
        .always(function() {
            cerrarLoading();
        });
    }
}

function btnBuscarUsuario_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pe", metodo:"btBuUs_Cl", txtUsuario:sisVars.usuario, txtBuscarUsuario:$("#txtBuscarUsuario").val()
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
                    $.each(result.cbxUsrEncontrados,function(clave,valor) {
                        $("#cbxUsrEncontrados").append("<option value='"+valor.trim()+"' title='"+valor.trim()+"'>"+valor.trim()+"</option>");                        
                    });
                break;
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                break;
            case -10:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    btnCerrarSesion_ActionPerformed ();
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
    })
    .always(function() {
        cerrarLoading();
    });
}

function btnInsertUsuariosEsp_Click ()
{
    var mensaje = new Mensajes ();
    var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
    
    
    //------------------ Establecemos los datos a enviar -------------------
    var datos = {
        modulo:"Pe", metodo:"btInUsEs_Cl", txtUsuario:sisVars.usuario, loginuser:$("#cbxUsrEncontrados").val(), idobjeto:$("#cbxComponenteAPermitir").val(), 
        permiso:$("#cbxPermisoDeComponente").val()
    };
    
    if (datos.loginuser===null || datos.loginuser==="")
        mensaje.General ("ESPECIFIQUE_DATO","el usuario");
    else {
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
                        var componente = $("#cbxComponenteAPermitir option:selected").text().split("-");
                        var rowTblUsuariosEsp = { loginuser:datos.loginuser, componente:componente[0].trim(), formulario:componente[1].trim(), 
                            permiso:datos.permiso.charAt(0), idaccesoespecifico:result.idaccesoespecifico, permisooriginal:datos.permiso.charAt(0)
                        };
                         var rowsTblUsuariosEsp = new Array();
                         rowsTblUsuariosEsp[0] = rowTblUsuariosEsp;
                        setTblUsuariosEsp (rowsTblUsuariosEsp, "INSERTAR");
                        mensaje.General("PROCESO_EXITOSO");
                    break;
                case 0: case -1:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    break;
                case -10:
                        mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                        btnCerrarSesion_ActionPerformed ();
                    break;
                default:break;
            }
        })
        .fail(function() {
            mensaje.General("ERROR_AJAX", "", "");
        })
        .always(function() {
            cerrarLoading();
        });
    }
}
