package servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;
import java.util.logging.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import modelo.Calificaciones.SICEEO_BimXAlum;
import modelo.Calificaciones.SICEEO_PromGralSec;
import modelo.Calificaciones.SICEEO_PromGralSec2012;
import modelo.Calificaciones.SICEEO_SecHist;
import modelo.Calificaciones.SICEEO_CalifSecXBim;
import modelo.Calificaciones.SICEEO_CaptuRepEval;
import modelo.Tutor.SICEEO_Tutor;
import modelo.Calificaciones.SICEEO_RevisaGpo;
import modelo.Calificaciones.SICEEO_XBimOPromFin;
import modelo.CambioDeAes.SICEEO_CamDAes;
import modelo.CambioDeArte.SICEEO_CamDArte;
import modelo.CambioDeCurp.SICEEO_ModifiCurp;
import modelo.CambioDeTaller.SICEEO_CamDTaller;
import modelo.CambioDeTaller.SICEEO_CamDTaller_Clubes;
import modelo.RepEvaluacion.SICEEO_CalibrarCaida;
import modelo.RepEvaluacion.SICEEO_RepEvaluacion;
import modelo.ClasesGlobales.SICEEO_Mensajes;
import modelo.Configuraciones.SICEEO_Configuraciones;
import modelo.Configuraciones.SICEEO_GestionarAvisos;
import modelo.Configuraciones.SICEEO_Permisos;
import modelo.Discap.SICEEO_Discap;
import modelo.EvalPreescolar.SICEEO_EvalPreescolar;
import modelo.Exalumnos.SICEEO_Exalumnos;
import modelo.Exalumnos.SelComplExalum;
import modelo.Grupo.SICEEO_CambioDeGrupo;
import modelo.Login.SICEEO_CamPass;
import modelo.Login.SICEEO_Password;
import modelo.Oficializaciones.SICEEO_Desoficializar;
import modelo.Oficializaciones.SICEEO_Oficializar;
import modelo.Personal.SICEEO_Personal;
import modelo.nuevoIngreso.SICEEO_NuevoIngreso;
import modelo.principal.SICEEO_Principal;
import modelo.Preinscripcion.SICEEO_Preinscripcion;
import modelo.Reportes.SICEEO_Complementaria;
import modelo.Reportes.SICEEO_Reportes;
import modelo.Ubicar.SICEEO_Ajustar;
import modelo.nuevoIngreso.SICEEO_MasDeUno;

/**
 *
 * @author ely
 */
public class servlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException 
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion;
        Map datosReturn = new HashMap();
        datosReturn.put("returnCase", 1);
        
        Gson gson = new Gson();
        String jsonOutput;
        
        try {
            String modulo = request.getParameter("modulo");

            if (modulo.equals("pa")){                                                                   //Modulo de Logueo
                SICEEO_Password loginUser = new SICEEO_Password(datosReturn);
                loginUser.ejecutarPeticion(request, response, request.getParameter("metodo"));
            }else if (modulo.equals("CaPa")){                                                           //Modulo de Cambio de Contraseña
                SICEEO_CamPass camPass = new SICEEO_CamPass(datosReturn);
                camPass.ejecutarPeticion(request, request.getParameter("metodo"));
            }else {
                sesion = ( (HttpServletRequest)request ).getSession(false);
                if ( (sesion==null || !(""+sesion.getAttribute("usuario")).equals("true") ) ){          //Verificamos que la sesión exista y tambien que el usuario esté logueado
                    datosReturn.put("returnCase", -10);
                    SICEEO_Mensajes mensaje = new SICEEO_Mensajes();
                    mensaje.General("SESION_EXPIRADA", "", "", datosReturn);
                }else {
                    sesion.setAttribute("modulo", modulo);
                    if (modulo.equals("Prin")) {                                                         //Módulo Principal
                        SICEEO_Principal principal = new SICEEO_Principal(datosReturn);
                        principal.ejecutarPeticion(request, request.getParameter("metodo"));
                    } else if (modulo.equals("nuIn")){                                                   //Módulo Nuevos Ingresos
                        SICEEO_NuevoIngreso nuevoIngreso = new SICEEO_NuevoIngreso(request, response, datosReturn);
                        nuevoIngreso.nuevoIngreso(request);
                    }else if (modulo.equals("CaDeGp")){                                                 //Módulo de ventana Grupo    
                        SICEEO_CambioDeGrupo camDeGpo = new SICEEO_CambioDeGrupo(datosReturn, request);
                        camDeGpo.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("Di")){                                                     //Módulo de ventana Discap   
                        SICEEO_Discap discap = new SICEEO_Discap(datosReturn, request);
                        discap.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("CaDeTa")){                                                 //Módulo de ventana CamDeTaller                            
                        SICEEO_CamDTaller camDTaller = new SICEEO_CamDTaller(datosReturn);
                        camDTaller.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("CaDeCl")){                                                 //Módulo de ventana CamDeClubes    
                        SICEEO_CamDTaller_Clubes camDClub = new SICEEO_CamDTaller_Clubes(datosReturn, request);
                        camDClub.ejecutarPeticion(request, request.getParameter("metodo"));    
                    }
                    else if (modulo.equals("CaDeAe")){                                                 //Módulo de ventana CamDeAes
                        SICEEO_CamDAes camDAes = new SICEEO_CamDAes(datosReturn);
                        camDAes.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("CaDeAr")){                                                 //Módulo de ventana CamDeArte 
                        SICEEO_CamDArte camDArte = new SICEEO_CamDArte(datosReturn);
                        camDArte.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("ReEv")){                                                   //Reportes de Evaluación
                        SICEEO_RepEvaluacion reEv = new SICEEO_RepEvaluacion(datosReturn);
                        reEv.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("caCaDeReEv")){                                             //Módulo de calibracion de coordenadas
                        SICEEO_CalibrarCaida caCa = new SICEEO_CalibrarCaida(datosReturn);
                        caCa.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("xBiOPrFi")){                                               //Calificacion por bimestre de secundarioa
                        SICEEO_XBimOPromFin xBiOPrFi = new SICEEO_XBimOPromFin(datosReturn, request);
                        xBiOPrFi.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("caSeXBi")){                                                //Calificacion por bimestre de secundarioa
                        SICEEO_CalifSecXBim caBiSec = new SICEEO_CalifSecXBim(datosReturn, request);
                        caBiSec.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("EvPr")){                                                   //Evaluación de preescolar
                        SICEEO_EvalPreescolar evPr = new SICEEO_EvalPreescolar(datosReturn, request);
                        evPr.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("prGrSe2012")){
                        SICEEO_PromGralSec2012 prGrSe2012 = new SICEEO_PromGralSec2012(datosReturn, request);
                        prGrSe2012.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("prGrSe")){
                        SICEEO_PromGralSec prGrSe = new SICEEO_PromGralSec(datosReturn);
                        prGrSe.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("biXAl")){
                        SICEEO_BimXAlum biXAl = new SICEEO_BimXAlum(datosReturn);
                        biXAl.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("Pr")){
                        SICEEO_Preinscripcion pr = new SICEEO_Preinscripcion(datosReturn);
                        pr.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("Aj")){
                        SICEEO_Ajustar aj = new SICEEO_Ajustar(datosReturn);
                        aj.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("MaDeUn")){
                        SICEEO_MasDeUno maDeUn = new SICEEO_MasDeUno(datosReturn);
                        maDeUn.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("SeHi")){
                        SICEEO_SecHist seHi = new SICEEO_SecHist(datosReturn);
                        seHi.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("MoCu")){
                        SICEEO_ModifiCurp moCu = new SICEEO_ModifiCurp(datosReturn, request);
                        moCu.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("Co")){
                        SICEEO_Configuraciones co = new SICEEO_Configuraciones(datosReturn);
                        co.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("Pe")){
                        SICEEO_Permisos pe = new SICEEO_Permisos(datosReturn);
                        pe.ejecutarPeticion(request, request.getParameter("metodo"));
                    }else if (modulo.equals("Of")){
                        SICEEO_Oficializar of = new SICEEO_Oficializar(datosReturn, request);
                        of.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("CaReEv")){
                        SICEEO_CaptuRepEval caReEv = new SICEEO_CaptuRepEval(datosReturn, request);
                        caReEv.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("Pers")){
                        SICEEO_Personal pers = new SICEEO_Personal(datosReturn, request);
                        pers.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("DaTut")) {  // 2023-2024
                        SICEEO_Tutor daTut = new SICEEO_Tutor(datosReturn, request);
                        daTut.ejecutarPeticion(request.getParameter("metodo"));                        
                    }else if (modulo.equals("Re")){
                        SICEEO_Reportes re = new SICEEO_Reportes(datosReturn, request/*, response*/);
                        re.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("De")){
                        SICEEO_Desoficializar de = new SICEEO_Desoficializar(datosReturn, request);
                        de.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("Compl")){
                        SICEEO_Complementaria compl = new SICEEO_Complementaria(datosReturn, request);
                        compl.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("ReGp")){
                        SICEEO_RevisaGpo regpo = new SICEEO_RevisaGpo(datosReturn, request);
                        regpo.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("GeAv")){
                        SICEEO_GestionarAvisos geav = new SICEEO_GestionarAvisos(datosReturn, request);
                        geav.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("Exal")){
                        SICEEO_Exalumnos exal = new SICEEO_Exalumnos(datosReturn, request);
                        exal.ejecutarPeticion(request.getParameter("metodo"));
                    }else if (modulo.equals("SeMeCoEx")){
                        SelComplExalum complExal = new SelComplExalum(datosReturn, request);
                        complExal.ejecutarPeticion(request.getParameter("metodo"));
                    }
                }
            }
        }catch (Exception ex) {
            SICEEO_Mensajes mensaje = new SICEEO_Mensajes();
            datosReturn.put("returnCase", -1); 
            mensaje.General("GENERAL", ex.getMessage(), "", datosReturn);
        }finally {
            jsonOutput = gson.toJson(datosReturn);
            out.print(jsonOutput);
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>    
}
