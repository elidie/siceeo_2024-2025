package modelo.ClasesGlobales;

import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.ServletConfig;

/**
 *
 * Creado el : 13/07/2015, 08:59:59 PM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

public class SICEEO_SessionListener implements HttpSessionListener
{
    Map activeUsers; 
    public void init(ServletConfig config)    {    }

    /**
    * Añade sesiones cada que estas son iniciadas al context con ámbito HashMap.
    */
    //@Override
    public void sessionCreated(HttpSessionEvent event)
    {
        HttpSession sesion = event.getSession();
        //System.out.println("Sesión iniciada: "+sesion.getId());
        //----------------- Vamos registrando cada sesión para poderlas manipular en otro momento ------------\\
        SICEEO_SessionManager sessionManager = (SICEEO_SessionManager)sesion.getServletContext().getAttribute("activeUsers"); 
        if (sessionManager == null){
            sessionManager = new SICEEO_SessionManager();
            sesion.getServletContext().setAttribute("activeUsers", sessionManager);
        }
        sessionManager.addSession(sesion);
        //--------------------------------------------------------------------------------------------------------------------\\
    }

    /**
     * Quita las seciones del contexto con ámgito HashMap cada que estas expiran
     * o son invalidadas.
     */
    //@Override
    public void sessionDestroyed(HttpSessionEvent event)
    {
        HttpSession session = event.getSession();
        //System.out.println("Sesión cerrada: "+session.getId());
        if (session.getAttribute("lockSessionListener")==null || session.getAttribute("lockSessionListener").equals(false)){
            SICEEO_SessionManager sessionManager = (SICEEO_SessionManager)session.getServletContext().getAttribute("activeUsers");
            if (sessionManager!=null && !sessionManager.isLockSessionListener())
                sessionManager.removeSession (session, null);
        }
    }
 }
