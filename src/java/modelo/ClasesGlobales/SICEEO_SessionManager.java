package modelo.ClasesGlobales;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 *
 * Creado el : 14/07/2015, 10:54:34 AM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

public class SICEEO_SessionManager {
    Map sessions;
    private boolean lockSessionListener;
    
    public SICEEO_SessionManager ()
    {
        sessions = new HashMap();
        lockSessionListener = false;
    }
    
    public void addSession (HttpSession session)
    {
        sessions.put(session.getId(), session);
    }
    
    public boolean isLockSessionListener ()
    {
        return lockSessionListener;
    }
    
    //Enviar null para la variable iterator si no se está llamando esta función desde un objeto iterador
    public void removeSession (HttpSession session, Iterator<HttpSession> iterator)
    {
        if (session!=null)
        {
            try {
                if (session.getAttribute("userName")!=null && !session.getAttribute("userName").equals("IVALLE") && !session.getAttribute("userName").equals("ELYLOPEZ"))   //Invalidamos todas las sesiones exepto el usuario IVALLE
                {
                    if (iterator == null)
                        sessions.remove(session.getId());
                    else
                        iterator.remove();
                    
                    Enumeration enumeration = session.getAttributeNames();
                    while (enumeration.hasMoreElements()) 
                    {
                        String element = (String) enumeration.nextElement();
                        if (element.equals("usuario"))
                            session.setAttribute("usuario", false);
                        else
                            session.removeAttribute("usuario");
                    }
                    
                    session.invalidate();
                }
            }catch (IllegalStateException ex) {
                System.out.println("Sesion anteriormente invalidada: " + ex);
            }
        }
    }
    
    public HttpSession getSession (String sessionId)
    {
        return (HttpSession)sessions.get(sessionId);
    }
    
    public Map getAllSessions ()
    {
        return sessions;
    }
    
    public ArrayList<Map> getActiveUsers ()
    {
        ArrayList<Map> activeUsers = new ArrayList<Map>();
        Map sessionParameters;
        
        Collection collection = sessions.values();
        Iterator<HttpSession> iterator = collection.iterator();
        while (iterator.hasNext())
        {
            sessionParameters = new HashMap();
            HttpSession session = iterator.next();
            
            try {
                Enumeration enumeration = session.getAttributeNames();

                sessionParameters.put("sessionId", session.getId());
                while (enumeration.hasMoreElements()) 
                {
                    String element = (String) enumeration.nextElement();
                    sessionParameters.put(element,session.getAttribute(element));
                }
                sessionParameters.put("lastAccessedTime",new Date(session.getLastAccessedTime()));
                sessionParameters.put("creationTime",new Date(session.getCreationTime()));
                activeUsers.add(sessionParameters);
            }catch (IllegalStateException ex) {                                                                                                   //Esta excepción se provoca por querer accesar a un atributo (variable) de sesión cuando la sesión ya está invalidada
                if (ex!=null && ex.getMessage()!=null && ex.getMessage().contains("invalid"))
                    iterator.remove();                                                                                                                           //Por eso la quitamos de nuestra lista de sesiones activas
            }
        }
        
        return activeUsers;
    }
    
    public int getNumUsuariosLogueados ()
    {
        int contador = 0;
        
        Collection collection = sessions.values();
        Iterator<HttpSession> iterator = collection.iterator();
        while (iterator.hasNext())
        {
            HttpSession session = iterator.next();
            
            try {
                if (session.getAttribute("usuario")!=null && session.getAttribute("usuario").equals(true))
                    contador ++;
            }catch (IllegalStateException ex) {                                                                                                   //Esta excepción se provoca por querer accesar a un atributo (variable) de sesión cuando la sesión ya está invalidada
            }
        }
        
        return contador;
    }
    
    public void invalidateAllSessions ()
    {
        Collection collection = sessions.values();
        Iterator<HttpSession> iterator = collection.iterator();
        lockSessionListener = true;
        while (iterator.hasNext())
        {
            HttpSession session = iterator.next();
            try {
                removeSession (session, iterator);
            }catch (IllegalStateException ex) {
                System.out.println("Sesion anteriormente invalidada: " + ex);
                //if (ex!=null && ex.getMessage()!=null && ex.getMessage().contains("invalid") && s!=null)
                //    s.remove();
            }
        }
        lockSessionListener = false;
    }
    
    public void invalidateSession (String sessionId)
    {
        HttpSession session = (HttpSession)sessions.get(sessionId);
        if (session!=null){
            try 
            {
                sessions.remove(sessionId);
                Enumeration enumeration = session.getAttributeNames();
                while (enumeration.hasMoreElements()) 
                {
                    String element = (String) enumeration.nextElement();
                    if (element.equals("usuario"))
                        session.setAttribute("usuario", false);
                    session.removeAttribute(element);
                }
                session.invalidate();
            }catch (Exception ex) {
                //System.out.println("Sesion anteriormente invalidada: " + ex);
            }
        }
    }

}
