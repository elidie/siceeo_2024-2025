package modelo.ClasesGlobales;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ely
 */
public class SICEEO_Filtro implements Filter 
{    
    private String urlList;// = new ArrayList();//<String>();    
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public SICEEO_Filtro() {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {    
            //Cargamos la sesion
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        
        HttpSession session= httpRequest.getSession(false);
        Object usuario = null;
        String quienLlama = httpRequest.getServletPath();
        
        //Preguntamos por un objeto en la sesion
        if(session!=null)
            usuario=session.getAttribute("usuario");
        
        if (  !(
                  quienLlama.contains("/libs/") || quienLlama.contains("/inicio/") || quienLlama.contains("/GlobalJs/") || quienLlama.contains("index.html") 
                  || quienLlama.contains("procesos.html") || quienLlama.contains("logueo.html")  || quienLlama.contains("mantenimiento.html") 
                  || quienLlama.contains("cerrarSesion.jsp")
               ) && (quienLlama.contains(".js") || quienLlama.contains(".html"))  && (session==null || usuario==null || usuario.equals(false)) )
        {//if we have no session
            try {
                if (quienLlama.contains(".html"))
                    httpRequest.getRequestDispatcher("../../html/Inicio/logueo.html").forward(request, response);
                else if (quienLlama.contains("/jsp/main.jsp")){
                    httpRequest.getRequestDispatcher("../jsp/redireccionALogueo.jsp").forward(request, response);
                    //res.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
                }else
                    httpRequest.getRequestDispatcher("html/Inicio/logueo.html").forward(request, response);
                /*httpRequest.getRequestDispatcher("//jsp/redireccionALogueo.jsp").forward(request, response);*/
                return;
            }
            catch (ServletException e) { }
            catch (IOException e) {}
        } 
        
        try {
            chain.doFilter(request, response);
        } catch (IOException e) {
            System.out.println("Error: "+e);
            throw (IOException) e;
        } catch (ServletException e) {
            System.out.println("Error: "+e);
            throw (ServletException) e;
        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {    
        filterConfig = null;
    }

    /**
     * Init method for this filter
     * @param filterConfig
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {        
        this.filterConfig = filterConfig;
        this.urlList = this.filterConfig.getInitParameter("urls_ini"); 
        if (urlList == null || urlList.trim().length() == 0) {
        //Error al cargar la url de login
           throw new ServletException("No se ha configurado URL de login");
        }   
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("filtros()");
        }
        StringBuffer sb = new StringBuffer("filtros(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
}
