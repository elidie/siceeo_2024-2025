/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author maai
 */
public class servletLogin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            URL url = new URL("https://sandbox.oaxaca.gob.mx/efirma/public/api/post");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //method to set the HTTP POST or GET command:
            conn.setRequestMethod("POST");
            // Set the necessary header fields
            conn.setRequestProperty("Accept", "application/json");
            //conn.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            //conn.setRequestProperty("SOAPAction", "http://herongyang.com/MyService#MyMethod");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            
            /******************************************************************/
            // Reading the request file
            File objFile = new File("");
            int reqLen = (int) objFile.length();
            byte[] reqBytes = new byte[reqLen];
            FileInputStream inStream = new FileInputStream(objFile);
            inStream.read(reqBytes);
            inStream.close();
            // Sending request
            OutputStream reqStream = conn.getOutputStream();
            reqStream.write(reqBytes);
            reqStream.flush();
            /******************************************************************/
            
            // Send the request
            String soapXml = "";  // jEdit: = buffer.getText(0,buffer.getLength())
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(soapXml);
            wr.flush();
            // Otra forma sería:
            //reqXML = "<?xml ...<soap:Envelope ...";                           //Prepare the entire SOAP request XML message as a string by using a DOM object, reading from a file, or simply as:
            //OutputStream reqStream = con.getOutputStream();
            //reqStream.write(reqXML.getBytes());
            
            
            
            
            // Read the response
            String output;
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            //Otra forma sería:
            //InputStream resStream = con.getInputStream();
            //byte[] byteBuf = new byte[10240];
            //int len = resStream.read(byteBuf);

            conn.disconnect();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
