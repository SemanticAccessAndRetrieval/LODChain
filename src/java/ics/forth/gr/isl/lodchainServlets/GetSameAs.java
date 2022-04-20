
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
import ics.forth.gr.isl.lodchaincode.SPARQLQuery;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Michalis Mountantonakis
 */
public class GetSameAs extends HttpServlet {

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

        response.setContentType("text/nq");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
         String datasetName=session.getAttribute("datasetName").toString();
        response.setHeader("Content-Disposition", "attachment; filename=\"sameAsRelationships_"+datasetName+".nt\"");
        try {
            OutputStream outputStream = response.getOutputStream();
            ArrayList<String> results = new ArrayList<String>();
            LODChainController lov = new LODChainController();

            HashMap<String, String> uris = (HashMap<String, String>) session.getAttribute("URIs");
            String outputResult = "";
            for (String ent : uris.keySet()) {
                if (uris.get(ent).split("\t").length > 1) {
                    outputResult = getEntityAllURIs(ent,uris.get(ent).split("\t")[0]);//<"+request.getParameter("URI")+">\t<"++">\t\""+results[i][1]+"\"\t"+results[i][2]+"\n";
                    outputStream.write(outputResult.getBytes());
                }
            }
          
            outputStream.flush();
            outputStream.close();
            return;
        } catch (Exception e) {
            ////System.out.println(e.toString());
        }

    }

    public String getEntityAllURIs(String ent, String code) {
        String query = "select ?SID where { ?SID <http://www.ics.forth.gr/isl/identifier> <" + code + ">}";
        SPARQLQuery sq = new SPARQLQuery();

        String result = "", firstURI = "";

        firstURI=ent;
        String[][] queryResults = sq.runSPARQLQuery(query, false, true, "\t");
        for (int j = 0; j < queryResults.length; j++) {
            if (queryResults[j][0].equals("SID")) {
                continue;
            }
            result += "<" + firstURI + "> <http://www.w3.org/2002/07/owl#sameAs> <" + queryResults[j][0] + "> .\n";
        }
        return result;

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
