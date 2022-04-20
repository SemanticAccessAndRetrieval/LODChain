
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */
package ics.forth.gr.isl.lodchainServlets;

import ics.forth.gr.isl.lodchaincode.LODChainController;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Michalis Mountantonakis
 */
public class GetMetadata extends HttpServlet {

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
        response.setContentType("text/nq");
        response.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        String datasetName = session.getAttribute("datasetName").toString();
        if (type.equals("CSV")) {
            response.setHeader("Content-Disposition", "attachment; filename=\"csvMetadata_" + datasetName + ".csv\"");
        } else if (type.equals("EFF")) {
            response.setHeader("Content-Disposition", "attachment; filename=\"efficiencyMetadata_" + datasetName + ".csv\"");
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=\"voidMetadata_" + datasetName + ".nt\"");
        }
        try {
            OutputStream outputStream = response.getOutputStream();
            ArrayList<String> results = new ArrayList<String>();
            LODChainController lov = new LODChainController();

            String outputResult = "";
            if (type.equals("CSV")) {
                outputResult = session.getAttribute("metadataCSV").toString();

            } else if (type.equals("EFF")) {
                outputResult = session.getAttribute("efficiencyCSV").toString();
            } else {
                outputResult = session.getAttribute("metadata").toString();
            }

            outputStream.write(outputResult.getBytes());
            outputStream.flush();
            outputStream.close();
            return;
        } catch (Exception e) {
            ////System.out.println(e.toString());
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
