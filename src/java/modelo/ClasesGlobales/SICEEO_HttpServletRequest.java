/**
 *
 * Creado el : 10/03/2015, 01:40:29 PM
 * Autor     : Ing. Maai Nolasco Sáncez
 *
 */

package modelo.ClasesGlobales;

import javax.servlet.http.HttpServletRequest;

public class SICEEO_HttpServletRequest
{
    private final HttpServletRequest request;
    
    public SICEEO_HttpServletRequest ( HttpServletRequest request)
    {
        this.request = request;
    }
    
    /**
     * Trabaja con el método getParameter
     * @param parametro Nombre del parámetro
     * @return El valor que retorna el getParameter
     */
    public String gP (String parametro)
    {
        return this.request.getParameter(parametro);
    }
    
    public String[] gPV (String parametro)
    {
        return this.request.getParameterValues(parametro+"[]");
    }
}
