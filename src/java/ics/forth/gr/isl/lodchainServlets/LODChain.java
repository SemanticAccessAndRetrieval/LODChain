
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
import isl.forth.gr.isl.dataset.Entity;
import isl.forth.gr.isl.dataset.Property;
import isl.forth.gr.isl.dataset.RDFClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Michalis Mountantonakis
 */
public class LODChain extends HttpServlet {
    private int previousConnections=0,inferredConnections=0;
    
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
            throws ServletException, IOException, CloneNotSupportedException, SQLException {

        String inputURI = request.getParameter("URI");
        String name = request.getParameter("Name");
        String url = request.getParameter("URL");
        String dom = request.getParameter("domain");
        String pref = request.getParameter("prefix");
        if (pref == null) {
            pref = "true";
        }
        if (name == null || name.equals("")) {
            String[] uriParts = inputURI.split("/");
            name = uriParts[uriParts.length - 1];

        }
        if (dom == null || dom.equals("")) {
            dom = "Cross-Domain";
        }
        if (url == null || url.equals("")) {
            url = inputURI;
        }

        int lines = 10000000;
        if (request.getParameter("lines") != null && !request.getParameter("lines").equals("") && !request.getParameter("lines").toLowerCase().equals("all")) {
            if(isStringInteger(request.getParameter("lines")))
                lines = Integer.parseInt(request.getParameter("lines"));
        }

        String domain = "http://dbpedia.org/resource/" + dom;
        String query = "";
        ////System.out.println(inputURI + " " + name + " " + url + " " + domain+" "+lines);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String msg;
        String time = "";
        LODChainController lov = new LODChainController();
        //setProperties
        lov.setProperties(Resources.getETIFolder() + "propertyEquivalenceCatalog.txt");
        //   //set LODsyndesisDatasets
        int lastID = lov.setLODsyndesisDatasetsIDs(Resources.getETIFolder() + "datasetIDs.txt");
        //set new Dataset
        //String domain = "http://dbpedia.org/resource/Publications";
        lov.setNewDataset(Integer.toString(lastID), name, url, domain, Resources.getETIFolder() + "datasetIDs.txt");
        //Closure and IDs
        String file = Resources.getETIFolder();//C:\\Users\\micha\\Desktop\\";     // inputURI;
        boolean URLFile = true;
        if (inputURI.startsWith("http")) {
            file = inputURI;
        } else {
            file += inputURI;
            URLFile = false;
        }

        boolean triples = true, entities = true, schema = true;

        long start = System.currentTimeMillis();

        HashSet<String> sameAs = lov.readFile2(file, URLFile, lines, true);
        if (sameAs == null) {
            request.setAttribute("msg", "File Not Found <a href='input.jsp'>Try Again</a>");
            //request.setAttribute("page2", "queryResultLive.jsp");
            request.setAttribute("page", "error.jsp");
            getServletConfig().getServletContext().getRequestDispatcher(
                    "/index.jsp").forward(request, response);
            return;
        }
        long end = System.currentTimeMillis();

        float sec = (end - start) / 1000F;
        float totalTime = sec;
        time += "Type,Number\n";
        time += "Read the Input File," + sec + " sec\n";

        start = System.currentTimeMillis();
        Map<Integer, Set<String>> pairs = lov.sameAsIndexFile2(sameAs);
        end = System.currentTimeMillis();
        sec = (end - start) / 1000F;
        totalTime += sec;
        time += "Local Closure," + sec + " sec\n";

        //IDS
        start = System.currentTimeMillis();
        HashSet<String> input = lov.setIDsForUris(pairs, pref);
        end = System.currentTimeMillis();
        sec = (end - start) / 1000F;
        totalTime += sec;
        time += "Global Closure," + sec + " sec\n";

        //Transformation
        start = System.currentTimeMillis();

        lov.transformTriples(true);

        lov.mergePropertiesAndClassesWithLODsyndesis(false);
        lov.mergeWithLODsyndesis(false);
        end = System.currentTimeMillis();
        sec = (end - start) / 1000F;
        totalTime += sec;
        time += "Merge triples with LODsyndesis," + sec + " sec\n";

        //lov.printPropertiesAndCreateDCList();
        //Dataset Based Statistics
        //Common Entities
        start = System.currentTimeMillis();

        lov.runMetrics(false);
        lov.printStats();
        end = System.currentTimeMillis();
        sec = (end - start) / 1000F;
        time += "Metrics," + sec + " sec\n";
        totalTime += sec;
        time += "Total Time," + totalTime + " sec\n";
        time += lov.getDataset().returnMessage() + "\n";

        time += "Unique Prefixes," + lov.getDataset().getUniquePrefixes() + "\n";

        LODChainController.queriesNum = 0;
        // lov.insertDatasetTriples("http://lodsyndesisLive");
        //lov.insertTriples("http://updates");
        //lov.deleteTriples();

        String[][] results = this.createEntitiesArray(time, lov, name);
        msg = "Metadata/Visualizations for Dataset";// of Prefix <i>" + input + "</i>";
        
        request.setAttribute("page2", "entitiesTab.jsp");

        response.setContentType("charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        request.setAttribute("page", "input.jsp");
        request.setAttribute("URI", inputURI);
        request.setAttribute("name", name);
        HttpSession session = request.getSession();
        session.setAttribute("URIs", this.getURIs(lov.getEntities()));
        session.setAttribute("metadata", lov.getDataset().getVoIDMetadata());
        session.setAttribute("metadataCSV", lov.getDataset().getCsvMetadata());
        session.setAttribute("efficiencyCSV", time);
        session.setAttribute("datasetName", lov.getDataset().getDatasetName());
        request.setAttribute("domainVisual", lov.getDomains());

        request.setAttribute("datasetsConnections", lov.getDatasetsMetrics());

        request.setAttribute("entitiesNumber", lov.getEntitiesNumber());
        request.setAttribute("triplesNumber", lov.getTriplesNumber());

        request.setAttribute("propertiesNumber", lov.getPropertiesNumber());
        request.setAttribute("classesNumber", lov.getClassesNumber());

        

        request.setAttribute("rankingBeforeClosureDomain", lov.getDataset().getRankingDomainBeforeC());
        request.setAttribute("rankingAfterClosureDomain", lov.getDataset().getRankingDomainAfterC());

        request.setAttribute("rankingBeforeClosure", lov.getDataset().getRankingBeforeC());
        request.setAttribute("rankingAfterClosure", lov.getDataset().getRankingAfterC());

        request.setAttribute("sameAsErrors", lov.getDataset().getErrors());
        request.setAttribute("sameAsBeforeClosure", lov.getDataset().getSameAsPairsNum());
        request.setAttribute("sameAsAfterClosure", lov.getDataset().getSameAsPairsNum() + lov.getDataset().getInferredRelationships());

        request.setAttribute("sameAsInferred", lov.getDataset().getInferredRelationships());

        request.setAttribute("commonFacts", lov.getDataset().setCommonFactsDatasets(10));
        request.setAttribute("commonProperties", lov.getDataset().setCommonPropsDatasets(10));
        request.setAttribute("commonClasses", lov.getDataset().setCommonClassesDatasets(10));

        request.setAttribute("compFacts", lov.getDataset().setCompFactsDatasets(10));

        if (entities == true) {
            String[][] equivRes = this.createEquivArray(time, lov);
            request.setAttribute("sameAs", equivRes);
            request.setAttribute("page5", "equivTab.jsp");
            session.setAttribute("complementaryTriples", this.getLinksForComplementaryTriples(lov.getEntities()));
            request.setAttribute("page8", "graph.jsp");
        }
        if (triples == true) {
            String[][] triplesRes = this.createTriplesArray(time, lov);
            request.setAttribute("triples", triplesRes);
            request.setAttribute("page3", "triplesTab.jsp");
            session.setAttribute("commonTriples", lov.getCommonTriples());
        }
        if (schema == true) {
            String[][] schemaRes = this.createSchemaArray(time, lov);
            request.setAttribute("schema", schemaRes);
            session.setAttribute("properties", this.getProperties(lov.getProperties()));
            session.setAttribute("classes", this.getClasses(lov.getClasses()));
            request.setAttribute("page4", "schemaTab.jsp");
        }

        // if (metadata == true) {
        String[][] metadataRes = this.createMetadataArray(time, lov);
        request.setAttribute("metadata", metadataRes);
        request.setAttribute("page6", "metadataTab.jsp");

        String[][] compRes = this.createCompArray(time, lov);
        request.setAttribute("compl", compRes);
        request.setAttribute("page9", "compTab.jsp");

        String[][] discoveryRes = this.createDiscoveryArray(lov);
        request.setAttribute("discovery", discoveryRes);
        request.setAttribute("page7", "discoveryTab.jsp");
        // }
        String jsonConn = this.jsonConnections(lov.getDataset().getTopDatasets(), lov.getDataset().getDatasetName(), domain, url, lov.getDataset().getPrevConnections());
        request.setAttribute("jsonConn", jsonConn);

        session.setAttribute("errors", lov.getDataset().getSameAsErrors());
        ////System.out.println(lov.getEntitiesNumber());
        request.setAttribute("msg", msg);
        
        request.setAttribute("beforeClosure", this.previousConnections);

        request.setAttribute("afterClosure",  this.previousConnections+this.inferredConnections);
        
        if(!results[4][1].equals("0%")){
            results[4][1]=(double) (this.inferredConnections * 100) / (double) (previousConnections) + "%";
        }
        request.setAttribute("results", results);
        
        getServletConfig().getServletContext().getRequestDispatcher(
                "/index.jsp").forward(request, response);
        
        
    }

    public String jsonConnections(TreeMap<Integer, HashSet<String>> topDatasets, String datasetName, String domain, String url, HashSet<String> prevConnections) {
        previousConnections=0;
        inferredConnections=0;
        String json = "{\"Datasets\": [";
        json += "{\"Name\": \"" + datasetName + "\", \"links\":0,\"new\":\"no\",\"domain\":\""
                + domain.replace("http://dbpedia.org/resource/", "") + "\"},";
        HashSet<String> addedConns = new HashSet<String>();
        for (int i : topDatasets.keySet()) {
            for (String dst : topDatasets.get(i)) {
                String dataset = dst.replace("http://", "").replace("/", "").replace("www.geonames", "sws.geonames").replace("www.", "");
                String dstName = LODChainController.datasetURLToName.get(dst);
                if (prevConnections.contains(dataset)) {
                    json += "{\"Name\": \"" + dstName + "\", \"links\":" + i + ",\"new\":\"yes\",\"domain\":\"" + LODChainController.datasetNameToDomain.get(dst).replace("http://dbpedia.org/resource/", "") + "\"},";
                     previousConnections++;
                    addedConns.add(dataset);
                } else {
                    json += "{\"Name\": \"" + LODChainController.datasetURLToName.get(dst) + "\", \"links\":" + i + ",\"new\":\"no\",\"domain\":\""
                            + LODChainController.datasetNameToDomain.get(dst).replace("http://dbpedia.org/resource/", "") + "\"},";             
                      
                       inferredConnections++;
                }
            }

        }
        String urlDataset = url.replace("http://", "").replace("https://", "");

        for (String dst : prevConnections) {
            if (!addedConns.contains(dst) && !urlDataset.startsWith(dst)) {
                previousConnections++;
                if (LODChainController.datasetNameToDomain.containsKey(dst)) {
                    json += "{\"Name\": \"" + dst + "\", \"links\":" + 5 + ",\"new\":\"yes\",\"domain\":\"" + LODChainController.datasetNameToDomain.get(dst).replace("http://dbpedia.org/resource/", "") + "\"},";
                } else {
                    json += "{\"Name\": \"" + dst + "\", \"links\":" + 5 + ",\"new\":\"yes\",\"domain\":\"" + "Unknown" + "\"},";

                }
            }
        }

        json = json.substring(0, json.length() - 1);
        json += "]}";
        ////System.out.println(json);
        return json;
    }

    public String[][] createEntitiesArray(String time, LODChainController lov, String name) {
        String[][] results = new String[10][2];
        for (int i = 1; i < results.length; i++) {
            results[i][0] = i + ". ";
        }

        results[0][0] = "Measurement Type";
        results[0][1] = "Measurement Result/Visualization";
        results = lov.getStats(results);
        return results;
    }

    public String[][] createCompArray(String time, LODChainController lov) {
        String[][] results = new String[7][2];
        for (int i = 1; i < results.length; i++) {
            results[i][0] = i + ". ";
        }
        results[0][0] = "Category";
        results[0][1] = "Download";
        results[1][0] += "Download Inferred SameAs Relationships";
        results[1][1] = "<button class=\"button special\" id=\"myBtn\" onclick=\"downloadSameAs();\">Inferred SameAs Relationships (NT format)</button>";
        results[2][0] += "Download  Common Entities (and their Provenance)";
        results[2][1] = "<br> <button class=\"button special\" id=\"myBtn\" onclick=\"downloadProvenance('entities');\">Provenance of Common Entities (NT format)</button><br>";
        results[3][0] += "Download   Common Properties (and their Provenance)";
        results[3][1] = "<br> <button class=\"button special\" id=\"myBtnPrps\" onclick=\"downloadProvenance('properties');\">Provenance of Common Properties (NT format)</button><br>";
        results[4][0] += "Download   Common Classes (and their Provenance)";
        results[4][1] = "<br> <button class=\"button special\" id=\"myBtnCl\" onclick=\"downloadProvenance('classes');\">Provenance of Common Classes (NT format)</button><br>";
        results[5][0] += "Download   Common Facts (and their Provenance)";
        results[5][1] = "<br> <button class=\"button special\" id=\"myBtnFacts\" onclick=\"downloadFacts('commonTriples');\">Provenance of Common Facts (NQ format)</button><br>";
        results[6][0] += "Links to LODsyndesis for Downloading Complementary Triples for your entities";
        results[6][1] = "<br> <button class=\"button special\" id=\"myBtnComp\" onclick=\"downloadFacts('complementaryTriples');\">Links for Complementary Triples  (NT format)</button><br>";

        return results;
    }

    public String[][] createMetadataArray(String time, LODChainController lov) {
        String[][] results = new String[4][2];
        for (int i = 1; i < results.length; i++) {
            results[i][0] = i + ". ";
        }
        results[0][0] = "Category";
        results[0][1] = "Download";
        results[1][0] += "Download Connectivity Measurements described through VoID ontology";
        results[1][1] = "<button class=\"button special\" id=\"myBtn\" onclick=\"downloadVoID();\">Metadata and Measurements (NT format)</button>";

        results[2][0] += "Download Connectivity Measurements in CSV format";
        results[2][1] = "<br><button class=\"button special\" id=\"myBtn\" onclick=\"downloadCSV();\">Metadata and Measurements (CSV format)</button><br>";

        results[3][0] += "Download Efficiency Statistics";
        results[3][1] = "<br><button class=\"button special\" id=\"myBtn\" onclick=\"downloadEfficiencyStatistics();\">Efficiency Statistics (CSV format)</button><br>";

        return results;
    }

    public String[][] createTriplesArray(String time, LODChainController lov) {
        String[][] results = new String[6][2];
        for (int i = 1; i < results.length; i++) {
            results[i][0] = i + ". ";
        }
        results[0][0] = "Measurement Type";
        results[0][1] = "Measurement Result/Visualization";
        results = lov.getTriplesStats(results);
        return results;
    }

    public String[][] createEquivArray(String time, LODChainController lov) {
        String[][] results = new String[4][2];
        for (int i = 1; i < results.length; i++) {
            results[i][0] = i + ". ";
        }
        results[0][0] = "Measurement Type";
        results[0][1] = "Measurement Result/Visualization";
        results = lov.getEquivStats(results);
        return results;
    }

    public String[][] createSchemaArray(String time, LODChainController lov) {
        String[][] results = new String[7][2];
        for (int i = 1; i < results.length; i++) {
            results[i][0] = i + ". ";
        }
        results[0][0] = "Measurement Type";
        results[0][1] = "Measurement Result/Visualization";
        results = lov.getSchemaStats(results);
        return results;
    }

    public String[][] createDiscoveryArray(LODChainController lov) {
        String[][] results = new String[6][4];
        for (int i = 1; i < results.length; i++) {
            results[i][0] = i + ". ";
            results[i][1] = "";
            results[i][2] = "";
            results[i][3] = "";
        }
        results[0][0] = "Measurement Type";
        results[0][1] = "The Most Relevant Dataset";
        results[0][2] = "2 Most Relevant Datasets";
        results[0][3] = "3 Most Relevant Datasets";
        results[1][0] += "Common Entities";
        results[2][0] += "Common Properties";
        results[3][0] += "Common Classes";
        results[4][0] += "Common Triples";
        results[5][0] += "Complementary Triples";

        results = lov.getDiscoveryStats(results);
        return results;
    }

    public HashSet<String> getLinksForComplementaryTriples(HashMap<String, Entity> entities) {
        HashSet<String> URIs = new HashSet<String>();

        for (String str : entities.keySet()) {
            if (entities.get(str).existingURI != null) {
                URIs.add("<" + entities.get(str).existingURI + ">\t<http://www.ics.forth.gr/isl/linkToLODsyndesis>\t<https://demos.isl.ics.forth.gr/lodsyndesis/rest-api/allFacts?uri=" + entities.get(str).existingURI + ">\t.");
            }
        }
        return URIs;
    }

    public HashMap<String, String> getURIs(HashMap<String, Entity> entities) {
        HashMap<String, String> URIs = new HashMap<String, String>();

        for (String str : entities.keySet()) {
            String result = "", firstURI = "";
            Entity ent = entities.get(str);
            if (ent.getProvenance() == null || ent.getProvenance().split(",").length == 1) {
                continue;
            }
            for (String x : ent.newURIs) {
                if (firstURI.equals("")) {
                    firstURI = x;
                    result = ent.code + "\t" + ent.getProvenance();
                    break;
                }
            }

            if (!firstURI.equals("")) {
                URIs.put(firstURI, result);
            } else {
                URIs.put(entities.get(str).existingURI, ent.code + "\t" + ent.getProvenance());
            }
        }
        return URIs;
    }

    public HashMap<String, String> getProperties(HashMap<String, Property> properties) {
        HashMap<String, String> URIs = new HashMap<String, String>();

        for (String str : properties.keySet()) {
            String result = "", firstURI = "";
            Property ent = properties.get(str);

            if (ent.getProvenance() == null || ent.getProvenance().split(",").length == 1) {
                continue;
            }

            result = ent.code + "\t" + ent.getProvenance();

            //if (!firstURI.equals("")) {
            URIs.put(ent.url, result);
            //}
        }
        return URIs;
    }

    public HashMap<String, String> getClasses(HashMap<String, RDFClass> classes) {
        HashMap<String, String> URIs = new HashMap<String, String>();

        for (String str : classes.keySet()) {
            String result = "", firstURI = "";
            RDFClass ent = classes.get(str);
            if (ent.getProvenance() == null || ent.getProvenance().split(",").length == 1) {
                continue;
            }
            result = ent.code + "\t" + ent.getProvenance();

            //if (!firstURI.equals("")) {
            URIs.put(ent.URI, result);
        }
        return URIs;
    }

    public static boolean isStringInteger(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        if (Integer.parseInt(number) > 0) {
            return true;
        } else {
            return false;
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
        try {
            processRequest(request, response);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(LODChain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LODChain.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(LODChain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LODChain.class.getName()).log(Level.SEVERE, null, ex);
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
