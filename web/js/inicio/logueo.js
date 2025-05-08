/* 
    Creado el : 24/04/2015, 03:08:37 PM
    Autor     : Ing. Maai Nolasco Sánchez
*/

$(document).on("ready", inicioDeLogueo);

function inicioDeLogueo()
{
    $("#txtUser").on ("keydown",function (e){
        if (e.keyCode === 13){
            if ( $("#btnIniciar").css("display")!=="none" )
                enterOprimido ();
        }
    });
    
    $("#pwdPass").on ("keydown",function (e) {
        if (e.keyCode === 13){
            if ( $("#btnIniciar").css("display")!=="none" )
                enterOprimido ();
        }
    });
    
    $("#btnIniciar").on("click", function(){ validaUsuario(); });
    /* $("#btnCambiarPasswd").on("click", function(){ btnCambiarPasswd_Click (); }); */
}

function enterOprimido ()
{    
    var u = document.getElementById('txtUser').value;
    var p = document.getElementById('pwdPass').value;
    if(u !== '' && p !== '')        
        validaUsuario();
    else
        alert('Ingrese usuario y contraseña.');
}

function validaUsuario() 
{   
    
    var mensaje = new Mensajes();
    
    /*------------------ Establecemos los datos a enviar -------------------*/
    var datos = {
        modulo:"pa", metodo:"lo", usuario:$("#txtUser").val(), password:$("#pwdPass").val(), cambUsu:"sinusuario"
    };
    /*------------------------- Hacemos la llamada -------------------------*/
    
    object_setVisible (false,"btnIniciar");    
    cargarLoading();    
    $.ajax({url:"../../sis_web/siS1",
            type:"POST",
            dataType:"JSON",
            data: datos,
            async:true
    })
    .done(function(result){
        switch(result.returnCase){
            case 1:
                    //var sisVars = JSON.parse( sessionStorage.getItem("sistemVars") );
                    result['usuario']=$("#txtUser").val().trim().toUpperCase();
                    result['cicescin']=result.cicescini;
                    result['orden']='cveplan';
                    result['versionSis']=result.versionSis;
                    result['btnBusquedaCCT_Click']=result.btnBusquedaCCT_Click;
                    result['cctdefault']=result.cctdefault;
                    result['modulos']=result.modulos;                    
                    result['showGloboNewModulo']=true;
                    
                    sessionStorage.sistemVars = JSON.stringify(result);
                    cerrarLoading();    
                    window.open('../../jsp/main.jsp','_parent');
                break;
            case 2:                    
                    cerrarLoading();
                    window.open('../../html/Inicio/mantenimiento.html','_parent');
                break;
            case -2:               
                    cerrarLoading();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");   
                    //window.open('../../html/Inicio/mantenimiento.html','_parent');
                break;    
            case 0: case -1:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    object_setVisible (true,"btnIniciar");
                    cerrarLoading();
                break;
            case -10:
                    //e.preventDefault();
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    window.open('../../jsp/cerrarSesion.jsp','_parent');
                break;
            case -11:
                    mensaje.General("GENERAL", result.tipoMensaje +"\n\n"+result.mensaje, "");
                    window.open('../../jsp/chngServ.jsp?puerto='+result.puerto,'_parent');
                break;
            default:break;
        }
    })
    .fail(function() {
        mensaje.General("ERROR_AJAX", "", "");
        cerrarLoading();
    }); 
}

function btnCambiarPasswd_Click ()
{
    mwffCamPass_Show ();
}
