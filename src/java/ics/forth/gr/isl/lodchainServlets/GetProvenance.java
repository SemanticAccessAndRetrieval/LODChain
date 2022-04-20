
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */
package ics.forth.gr.isl.lodchainServlets;

import ics.forth.gr.isl.configuration.Resources;

import ics.forth.gr.isl.lodchaincode.LODChainController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class GetProvenance extends HttpServlet {

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
        response.setContentType("text/nt");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String type = request.getParameter("type");
        String datasetName = session.getAttribute("datasetName").toString();
        response.setHeader("Content-Disposition", "attachment; filename=\"provenance_" + type + "_" + datasetName + ".nt\"");
        this.setLODsyndesisDatasetsIDs(Resources.getETIFolder() + "datasetIDs.txt");
        try {
            OutputStream outputStream = response.getOutputStream();
            ArrayList<String> results = new ArrayList<String>();
            LODChainController lov = new LODChainController();
            HashMap<String, String> uris = new HashMap<String, String>();
            if (type.equals("entities")) {
                uris = (HashMap<String, String>) session.getAttribute("URIs");
            } else if (type.equals("properties")) {
                uris = (HashMap<String, String>) session.getAttribute("properties");
            } else if (type.equals("classes")) {
                uris = (HashMap<String, String>) session.getAttribute("classes");
            }
            String outputResult = "";
            for (String ent : uris.keySet()) {
                if (uris.get(ent).split("\t").length > 1) {
                    outputResult = getProvenance(ent, uris.get(ent).split("\t")[1]);//<"+request.getParameter("URI")+">\t<"++">\t\""+results[i][1]+"\"\t"+results[i][2]+"\n";
                    outputStream.write(outputResult.getBytes());
                }
            }
            outputStream.write(outputResult.getBytes());
            outputStream.flush();
            outputStream.close();
            return;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public String getProvenance(String firstURI, String prov) {

        String result = "";

        String[] split = prov.split(",");
        for (String dst : split) {
            result += "<" + firstURI + "> <http://www.ics.forth.gr/isl/provenance> <" + LODChainController.IDtoDatasetName.get(Integer.parseInt(dst)) + "> .\n";
        }
        return result;
    }

    public int setLODsyndesisDatasetsIDs(String file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = "";
        int i = 0;
        int lastID = 0;
        while ((s = br.readLine()) != null) {
            if (i == 0) {
                i = 1;
                continue;
            }
            String[] split = s.split("\t");
            LODChainController.datasetNameToID.put(split[2], Integer.parseInt(split[0]));
            LODChainController.datasetNameToDomain.put(split[2], split[3]);
            lastID = Integer.parseInt(split[0]);
            LODChainController.IDtoDatasetName.put(Integer.parseInt(split[0]), split[2]);
        }

        br.close();
        lastID++;
        return lastID;

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
