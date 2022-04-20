
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */
package ics.forth.gr.isl.lodchaincode;

import isl.forth.gr.isl.dataset.Entity;
import isl.forth.gr.isl.dataset.RDFClass;
import isl.forth.gr.isl.dataset.Property;
import isl.forth.gr.isl.dataset.Dataset;
import ics.forth.gr.isl.configuration.Resources;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ics.forth.gr.isl.lodsyndesis.lattices.Commonalities;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.rio.RDFFormat;

/**
 *
 * @author Michalis Mountantonakis
 */
public class LODChainController {

    public static String folder = Resources.getETIFolder() + "part-r-000";
    Connection conn;
    int currentETI = 95;
    HashMap<String, Entity> entities = new HashMap<String, Entity>();

    HashSet<String> commonTriples = new HashSet<String>();

    HashMap<String, RDFClass> classes = new HashMap<String, RDFClass>();
    HashMap<String, Property> properties = new HashMap<String, Property>();

    ArrayList<String> triplesToInsert = new ArrayList<String>();
    ArrayList<String> triplesToDelete = new ArrayList<String>();
    HashMap<String, String> linesToInsertInFile = new HashMap<String, String>();

    HashMap<String, HashMap<String, Set<String>>> triples = new HashMap<>();
    HashSet<String> uris = new HashSet<>();
    HashSet<String> props = new HashSet<>();
    HashSet<String> clses = new HashSet<>();

    public static SPARQLQuery sq = new SPARQLQuery();
    static int countIDs = 10000000;
    static int count2 = 0;
    Dataset newDataset = new Dataset();
    public static int queriesNum = 0;
    ArrayList<String> readLines = new ArrayList<String>();
    /**
     * Converts a property to property ID
     */
    public HashMap<String, String> propertyToID = new HashMap<>();
    public HashMap<String, String> entityToID = new HashMap<>();
    public HashMap<String, String> classToID = new HashMap<>();
    /**
     * Converts a dataset name to ID
     */
    public static HashMap<String, Integer> datasetNameToID = new HashMap<>();
    public static HashMap<String, String> datasetNameToDomain = new HashMap<>();
    public static HashMap<String, String> datasetURLToName = new HashMap<>();
    /**
     * Converts a dataset ID to dataset name
     */
    public static HashMap<Integer, String> IDtoDatasetName = new HashMap<>();

    public HashSet<String> getCommonTriples() {
        return commonTriples;
    }

    public void insertDatasetTriples(String graph) {
        this.insertTriples(graph, newDataset.virtuosoTriples);
    }

    public void dropGraph(String graph) {
        String query;
        query = " DROP SILENT GRAPH <" + graph + ">";
        sq.runSPARQLQuery(query, false, true, "%");

    }

    public void printStats() {
        newDataset.printDatasetStatistics();
        //System.out.println(newDataset.prevConnections);

    }

    public Dataset getDataset() {
        return newDataset;
    }

    public String[][] getStats(String[][] res) {
        return newDataset.getDatasetStatistics(res);
    }

    public String[][] getTriplesStats(String[][] res) {
        return newDataset.getTriplesStatistics(res);
    }

    public String[][] getEquivStats(String[][] res) {
        return newDataset.getEquivStatistics(res);
    }

    public String[][] getSchemaStats(String[][] res) {
        return newDataset.getSchemaStatistics(res);
    }

    public String[][] getDiscoveryStats(String[][] res) {
        return newDataset.getDiscoveryStatistics(res);
    }

    public String getDomains() {
        return newDataset.domains;
    }

    public HashMap<String, Entity> getEntities() {
        return entities;
    }

    public HashMap<String, Property> getProperties() {
        return properties;
    }

    public HashMap<String, RDFClass> getClasses() {
        return classes;
    }

    public String getEntitiesNumber() {
        return newDataset.entitiesNumberString;
    }

    public String getTriplesNumber() {
        return newDataset.triplesNumberString;
    }

    public String getPropertiesNumber() {
        return newDataset.propertiesNumberString;
    }

    public String getClassesNumber() {
        return newDataset.classesNumberString;
    }

    public String getDatasetsMetrics() {
        return newDataset.datasetsMetrics;
    }

    public void runMetrics(boolean merge) throws CloneNotSupportedException, IOException {
        HashMap<String, Integer> entitiesMetrics = findCommonEntities(4);
        HashMap<String, Integer> triplesMetrics = findCommonTriples(4);
        HashMap<String, Integer> propertiesMetrics = findCommonProperties(3);
        HashMap<String, Integer> classesMetrics = findCommonClasses(3);

        newDataset.createVirtuosoTriples(entitiesMetrics, triplesMetrics, propertiesMetrics, classesMetrics);
        if (merge == true) {
            this.insertTriples("http://lodsyndesisLive", newDataset.virtuosoTriples);
        }

    }

    public HashMap<String, Integer> findCommonEntities(int subsets) throws IOException, CloneNotSupportedException {
        Commonalities td = new Commonalities();
        td.runCommonalities(subsets, newDataset.entitiesDirectCounts, 5, true, true);
        return td.getResults();
    }

    public HashMap<String, Integer> findCommonTriples(int subsets) throws IOException, CloneNotSupportedException {
        Commonalities td = new Commonalities();
        td.runCommonalities(subsets, newDataset.triplesDirectCounts, 2, true, true);
        return td.getResults();
    }

    public HashMap<String, Integer> findCommonProperties(int subsets) throws IOException, CloneNotSupportedException {
        Commonalities td = new Commonalities();
        td.runCommonalities(subsets, newDataset.propertiesDirectCounts, 1, true, true);
        return td.getResults();
    }

    public HashMap<String, Integer> findCommonClasses(int subsets) throws IOException, CloneNotSupportedException {
        Commonalities td = new Commonalities();
        td.runCommonalities(subsets, newDataset.classesDirectCounts, 1, true, true);
        return td.getResults();
    }

    /**
     * Datasets of LODsyndesis
     *
     * @param file the datasets of Property Index of LODsyndesis
     * @throws FileNotFoundException
     * @throws IOException
     */
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
            LODChainController.datasetURLToName.put(split[2], split[1]);

            LODChainController.datasetNameToID.put(split[2], Integer.parseInt(split[0]));
            LODChainController.datasetNameToDomain.put(split[2], split[3]);
            lastID = Integer.parseInt(split[0]);
            LODChainController.IDtoDatasetName.put(Integer.parseInt(split[0]), split[2]);
        }

        br.close();
        lastID++;
        return lastID;

    }

    public void setNewDataset(String ID, String name, String URL, String domain, String file) throws IOException {
        newDataset.setDatasetID(ID);
        newDataset.setDatasetName(name);
        newDataset.setDatasetURL(URL);
        newDataset.setDatasetDomain(domain);
        LODChainController.datasetNameToID.put(newDataset.datasetURL, Integer.parseInt(newDataset.datasetID));
        LODChainController.IDtoDatasetName.put(Integer.parseInt(newDataset.datasetID), newDataset.datasetURL);

        String s = "";
        int i = 0;
        int lastID = 0;

    }

    public void refreshIndexes(String datasetsPath) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(datasetsPath, true));

        bw.append("\n" + newDataset.datasetID + "\t" + newDataset.datasetName + "\t" + newDataset.datasetURL + "\t" + newDataset.datasetDomain + "\t" + newDataset.datasetTriples + "\t" + newDataset.entitiesNumber);
        bw.close();

    }

    public String getProvenance(String URI) {
        String query;
        query = "select distinct ?SID where { <" + URI + "> <http://www.ics.forth.gr/isl/provenance> ?SID}";
        String[][] temp = sq.runSPARQLQuery(query, false, true, "%");
        if (temp.length > 1) {
            return temp[1][0] + "," + newDataset.getDatasetID();
        }
        return newDataset.getDatasetID();
    }

    public void setEntityInformation(String URI, Entity ent, boolean URIsnum) {
        String query;
        if (URI.startsWith("EIDL" + newDataset.datasetID)) {
            return;
        }
        if (URIsnum == true) {
            ent.numberOfURIs = this.getEntityURIsNumber(URI);
        }
        query = "select distinct ?g ?p ?SID where {graph ?g {<" + URI + "> ?p ?SID}}";
        String[][] temp = sq.runSPARQLQuery(query, false, true, "%");
        // System.out.println(URI + " " + temp.length);
        for (int j = 0; j < temp.length; j++) {
            if (temp[j][0].contains("provenance")) {
                String[] spl = temp[j][0].split("\t");
                if (spl.length > 2) {
                    ent.provenance = spl[2] + "," + newDataset.getDatasetID();
                    //    System.out.println(ent.provenance);

                    String delQuery = "DELETE DATA FROM  <" + spl[0] + "> { <" + URI + "> <http://www.ics.forth.gr/isl/provenance> \"" + spl[2] + "\"}";
                    ent.deleteQueries.add(delQuery);
                }
            }
            if (temp[j][0].contains("directCount")) {
                String spl = temp[j][0].split("http://www.ics.forth.gr/isl/directCount\t")[1];
                String[] spl2 = temp[j][0].split("\t");

                if (spl.length() > 0) {
                    ent.tempDC = spl;

                    String delQuery = "DELETE DATA FROM  <" + spl2[0] + "> { <" + URI + "> <http://www.ics.forth.gr/isl/directCount> \"" + ent.tempDC + "\"}";
                    ent.deleteQueries.add(delQuery);
                    //   System.out.println(ent.tempDC);
                }
            }
            if (temp[j][0].contains("identifier")) {
                String[] spl = temp[j][0].split("\t");
                if (spl.length > 2) {
                    if (ent.previousPointer.length() > 0) {
                        ent.previousPointer += ",";
                    }
                    ent.previousPointer = spl[2];
                    //String delQuery = "DELETE DATA FROM  <" + spl[0] + "> { <" + URI + "> <http://www.ics.forth.gr/isl/identifier> \"" + spl[2] + "\"}";
                    //ent.deleteQueries.add(delQuery);

                }
            }
        }
    }

    public String getDC(String URI) {
        String query;
        if (URI.contains("EIDL")) {
            return "";
        }
        query = "select distinct ?SID where { <" + URI + "> <http://www.ics.forth.gr/isl/directCount> ?SID}";
        String[][] temp = sq.runSPARQLQuery(query, false, true, "%");
        if (temp.length > 1) {
            return temp[1][0];
        }
        return "";
    }

    public void insertTriples(String graph, String triples) {
        String query;
        query = "INSERT INTO  GRAPH <" + graph + "> {\n"
                + triples
                + "}";
        sq.runSPARQLQuery(query, false, true, "%");

    }

    public void deleteTriples(Entity ent) {
        for (String query : ent.deleteQueries) {
            //System.out.println(query);
            sq.runSPARQLQuery("sparql " + query, false, true, "%");
        }

    }

    public boolean existsPrefix(String URI, int threshold) {
        String query;

        query = "select distinct ?SID where { <" + URI + "> <http://www.ics.forth.gr/isl/provenance> ?SID}";
        String[][] temp = sq.runSPARQLQuery(query, false, true, "%");
        if (temp.length > 1) {
            String[] sp = temp[1][0].split(",");
            int count = 0;
            int loops = 0;

            for (String k : sp) {

                count += Integer.parseInt(k.split(":")[1]);
                loops++;
            }
            //System.out.println(URI + " " + count);
            if (count > threshold && loops > 1) {
                return true;
            }
        }
        return false;
    }

    public String getClass(String URI) {
        String query;
        query = "select distinct ?SID where { <" + URI + "> <http://www.ics.forth.gr/isl/identifier> ?SID}";
        String[][] temp = sq.runSPARQLQuery(query, false, true, "%");
        if (temp.length > 1) {
            return temp[1][0].replace("http://www.ics.forth.gr/isl/code/", "");
        }
        return "";
    }

    /**
     * Property Index of LODsyndesis
     *
     * @param file the path of Property Index of LODsyndesis
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void setProperties(String file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = "";
        while ((s = br.readLine()) != null) {
            String[] split = s.split("\t");
            propertyToID.put(split[0], split[1]);
            //System.out.println(split[0]+" "+split[1]);
        }
        br.close();

    }

    public void transformTriples(boolean bothSides) throws FileNotFoundException, IOException {
        HashSet<String> pairs = new HashSet<String>();
        //br = new BufferedReader(new FileReader(file));
        int count = 1, zeros = 0;

        for (String sCurrentLine : readLines) {
            String[] line = sCurrentLine.split("\t");
            if (line.length < 3) {

                line = sCurrentLine.split(" ");
                if (line.length < 3) {
                    continue;
                } else {
                    if (line[2].startsWith("\"")) {
                        for (int i = 3; i < line.length - 1; i++) {
                            line[2] += line[i];
                            if (line[i].contains("\"")) {
                                line[2] += line[i];
                                break;
                            } else {
                                line[2] += line[i] + " ";
                            }
                        }
                    }
                }
            }
            // System.out.println(line[0] + " " +line[1] + " " +line[2]);

            if (sCurrentLine.contains("http://www.w3.org/2002/07/owl#sameAs") || sCurrentLine.contains("_:")) {
                continue;

            }
            String convertedSub = "", convertedPred = "", convertedObj = "";
            boolean entityObject = false;
            if (line[0].startsWith("<http") && entityToID.containsKey(line[0].replace("<", "").replace(">", ""))) {
                convertedSub = entityToID.get(line[0].replace("<", "").replace(">", ""));
            }
            if (line[1].startsWith("<http") && propertyToID.containsKey(line[1].replace("<", "").replace(">", ""))) {
                convertedPred = propertyToID.get(line[1].replace("<", "").replace(">", ""));
            }
            if (line[1].contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && line[2].startsWith("<http") && classToID.containsKey(line[2].replace("<", "").replace(">", ""))) {
                convertedObj = classToID.get(line[2].replace("<", "").replace(">", ""));
            } else if (line[2].startsWith("<http") && entityToID.containsKey(line[2].replace("<", "").replace(">", ""))) {
                convertedObj = entityToID.get(line[2].replace("<", "").replace(">", ""));
                entityObject = true;
            } else {

                convertedObj = line[2].replace("\"", "").replace("^", "").split("@")[0].split("<")[0].toLowerCase();
            }

            if (convertedSub.equals("") || convertedPred.equals("") || convertedObj.equals("")) {
                continue;
            }

            String oldTriple = line[0] + "\t" + line[1] + "\t" + line[2];
            String newTriple = convertedSub + "\t" + convertedPred + "\t" + convertedObj;

            if (triples.containsKey(convertedSub)) {
                if (triples.get(convertedSub).containsKey(convertedPred)) {
                    triples.get(convertedSub).get(convertedPred).add(convertedObj);
                } else {
                    Set<String> objs = new HashSet<>();
                    objs.add(convertedObj);
                    triples.get(convertedSub).put(convertedPred, objs);

                }
            } else {
                Set<String> objs = new HashSet<>();
                objs.add(convertedObj);
                HashMap<String, Set<String>> props = new HashMap<>();
                props.put(convertedPred, objs);
                triples.put(convertedSub, props);
            }

            if (entityObject == true && bothSides == true) {
                if (triples.containsKey(convertedObj)) {
                    if (triples.get(convertedObj).containsKey(convertedPred + "*")) {
                        triples.get(convertedObj).get(convertedPred + "*").add(convertedSub);
                    } else {
                        Set<String> objs = new HashSet<>();
                        objs.add(convertedSub);
                        triples.get(convertedObj).put(convertedPred + "*", objs);

                    }
                } else {
                    Set<String> objs = new HashSet<>();
                    objs.add(convertedSub);
                    HashMap<String, Set<String>> props = new HashMap<>();
                    props.put(convertedPred + "*", objs);
                    triples.put(convertedObj, props);
                }

            }

        }
        readLines.clear();
    }

    public void mergeWithLODsyndesis(boolean updateIndexes) throws IOException {
        //newDataset.entitiesNumber=triples.keySet().size();
        for (String entity : triples.keySet()) {
            String data = "";

            Entity ent = entities.get(entity);
            if (!ent.code.startsWith("EIDL" + newDataset.datasetID)) {
                //System.out.println(ent.previousPointer);
                ent.lodsyndesisIndex = this.getPredicateObjectsOfEntity(ent.previousPointer);
            }

            /*  if (newDataset.entitiesDirectCounts.containsKey(ent.provenance)) {
                newDataset.entitiesDirectCounts.put(ent.provenance, newDataset.entitiesDirectCounts.get(ent.provenance) + 1);
            } else {
                newDataset.entitiesDirectCounts.put(ent.provenance, 1);
            }*/
            if (ent.lodsyndesisIndex == null) {
                ent.lodsyndesisIndex = new HashMap<>();
            }
            if (updateIndexes == true) {
                data = entity + "\n";
            }
            for (String convertedProperty : triples.get(entity).keySet()) {
                if (updateIndexes == true) {
                    data += "\t" + convertedProperty + "\n";
                }

                for (String convertedObject : triples.get(entity).get(convertedProperty)) {
                    newDataset.datasetTriples++;
                    String dcount = "";
                    if (ent.getLodsyndesisIndex().containsKey(convertedProperty)) {
                        String toRemove = "", toAdd = "";
                        Set<String> values = ent.getLodsyndesisIndex().get(convertedProperty);
                        for (String str : values) {
                            String[] spl = str.split("\t");
                            if (spl[0].equals(convertedObject)) {
                                toAdd = spl[0] + "\t" + spl[1] + "," + newDataset.getDatasetID();
                                String commonFact = "";//+ "," + newDataset.getDatasetID();
                                if (spl[0].startsWith("C") && !convertedProperty.endsWith("*")) {
                                    for (String dst : spl[1].split(",")) {
                                        commonFact = "<" + ent.URI + ">\t<" + properties.get(convertedProperty).url + ">\t<" + classes.get(spl[0]).URI + ">\t<" + IDtoDatasetName.get(Integer.parseInt(dst)) + ">\t.";
                                        commonTriples.add(commonFact);
                                    }
                                } else if (spl[0].startsWith("EID") && !convertedProperty.endsWith("*")) {
                                    for (String dst : spl[1].split(",")) {
                                        commonFact = "<" + ent.URI + ">\t<" + properties.get(convertedProperty).url + ">\t<" + entities.get(spl[0]).URI + ">\t<" + IDtoDatasetName.get(Integer.parseInt(dst)) + ">\t.";
                                        commonTriples.add(commonFact);
                                    }
                                } else if (!convertedProperty.endsWith("*")) {
                                    for (String dst : spl[1].split(",")) {
                                        commonFact = "<" + ent.URI + ">\t<" + properties.get(convertedProperty).url + ">\t\"" + spl[0] + "\"\t<" + IDtoDatasetName.get(Integer.parseInt(dst)) + ">\t.";
                                        commonTriples.add(commonFact);
                                    }
                                }

                                if (ent.getDirectCounts().containsKey(spl[1])) {
                                    ent.getDirectCounts().put(spl[1], ent.getDirectCounts().get(spl[1]) - 1);
                                    if (ent.getDirectCounts().get(spl[1]) == 0) {
                                        ent.getDirectCounts().remove(spl[1]);
                                    }
                                }
                                dcount = spl[1] + "," + newDataset.getDatasetID();
                                toRemove = str;
                                break;
                            }
                        }
                        if (!toAdd.equals("")) {
                            ent.getLodsyndesisIndex().get(convertedProperty).add(toAdd);
                            ent.getLodsyndesisIndex().get(convertedProperty).remove(toRemove);
                            if (updateIndexes == true) {
                                data += "\t\t" + toAdd + "\n";
                            }
                            // System.out.println(toAdd + "\t" + toRemove);
                        } else {
                            ent.getLodsyndesisIndex().get(convertedProperty).add(convertedObject + "\t" + newDataset.getDatasetID());
                            dcount = newDataset.getDatasetID();
                            if (updateIndexes == true) {
                                data += "\t\t" + convertedObject + "\t" + newDataset.getDatasetID() + "\n";
                            }
                        }
                    } else {
                        Set<String> values = new HashSet<String>();
                        values.add(convertedObject + "\t" + newDataset.getDatasetID());
                        if (updateIndexes == true) {
                            data += "\t\t" + convertedObject + "\t" + newDataset.getDatasetID() + "\n";
                        }
                        dcount = newDataset.getDatasetID();
                        ent.getLodsyndesisIndex().put(convertedProperty, values);
                    }

                    if (ent.getDirectCounts().containsKey(dcount)) {
                        ent.getDirectCounts().put(dcount, ent.getDirectCounts().get(dcount) + 1);
                    } else {
                        ent.getDirectCounts().put(dcount, 1);
                    }

                }
            }
            if (updateIndexes == true) {
                RandomAccessFileTest raf = new RandomAccessFileTest();
                if (ent.previousPointer.equals("")) {
                    ent.pointer = currentETI + ":" + raf.appendData(folder + this.currentETI, data);
                } else {
                    ent.pointer = ent.previousPointer + "," + currentETI + ":" + raf.appendData(folder + this.currentETI, data);
                }

            }
            //printDcounts(ent);
            ent.setVirtuoso();
            //System.out.println(ent.virtuosoTriples);
            if (updateIndexes == true) {
                this.insertTriples("http://lodsyndesisLive", ent.getAllVirtuosoTriples());
                this.deleteTriples(ent);
            }

            for (String x : ent.directCounts.keySet()) {

                if (newDataset.triplesDirectCounts.containsKey(x)) {
                    newDataset.triplesDirectCounts.put(x, newDataset.triplesDirectCounts.get(x) + ent.directCounts.get(x));
                } else {
                    newDataset.triplesDirectCounts.put(x, ent.directCounts.get(x));
                }
            }
            ent.lodsyndesisIndex.clear();
            triples.get(entity).clear();

        }

    }

    public void mergePropertiesAndClassesWithLODsyndesis(boolean updateIndexes) throws IOException {
        //newDataset.entitiesNumber=triples.keySet().size();
        //System.out.println("==================================");
        int URIsBefore = 0, URIsAfter = 0, URIsNum = 0;
        for (String entity : entities.keySet()) {
            String data = "";

            Entity ent = entities.get(entity);
            if (ent.numberOfURIsOLD > 1) {
                URIsBefore += ent.numberOfURIsOLD;
                URIsAfter += ent.numberOfURIs;
                URIsNum++;
            }
            if (ent.provenance != null && ent.provenance.split(",").length > 1) {
                newDataset.commonEntitiesDatasets += ent.provenance.split(",").length;
            }
            if (newDataset.entitiesDirectCounts.containsKey(ent.provenance)) {
                newDataset.entitiesDirectCounts.put(ent.provenance, newDataset.entitiesDirectCounts.get(ent.provenance) + 1);
            } else {
                newDataset.entitiesDirectCounts.put(ent.provenance, 1);
            }
            //System.out.println(ent.virtuosoTriples);
            if (updateIndexes == true) {
                //this.insertTriples("http://lodsyndesisLive", ent.getAllVirtuosoTriples());
                //this.deleteTriples(ent);
            }
            //  System.out.println(p.virtuosoTriples);
            //   System.out.println(p.virtuosoTriplesToDelete);
            //   System.out.println(p.addToPropFile);

        }
        newDataset.inferredRelationships = URIsAfter;

        for (String property : properties.keySet()) {
            String data = "";

            Property p = properties.get(property);

            if (newDataset.propertiesDirectCounts.containsKey(p.provenance)) {
                newDataset.propertiesDirectCounts.put(p.provenance, newDataset.propertiesDirectCounts.get(p.provenance) + 1);
            } else {
                newDataset.propertiesDirectCounts.put(p.provenance, 1);
            }
            //System.out.println(ent.virtuosoTriples);
            if (updateIndexes == true) {
                //this.insertTriples("http://lodsyndesisLive", ent.getAllVirtuosoTriples());
                //this.deleteTriples(ent);
            }
            //  System.out.println(p.virtuosoTriples);
            //   System.out.println(p.virtuosoTriplesToDelete);
            //   System.out.println(p.addToPropFile);

        }

        newDataset.classesNumber = classes.size();
        for (String k : classes.keySet()) {
            RDFClass cl = classes.get(k);
            if (newDataset.classesDirectCounts.containsKey(cl.provenance)) {
                newDataset.classesDirectCounts.put(cl.provenance, newDataset.classesDirectCounts.get(cl.provenance) + 1);
            } else {
                newDataset.classesDirectCounts.put(cl.provenance, 1);
            }

            //  System.out.println(cl.virtuosoTriples);
            //  System.out.println(cl.virtuosoTriplesToDelete);
        }

        //System.out.println("==================================");
        //  System.out.println(newDataset.propertiesDirectCounts);
    }

    public HashSet<String> readFile2(String fileURL, boolean isURL, int lines, boolean triples) throws IOException {

        BufferedReader br = null;
        String sCurrentLine;
        HashSet<String> pairs = new HashSet<String>();
        if (!isURL) {
            try {
                br = new BufferedReader(new FileReader(fileURL));
                pairs = readAndReturnPairsNTriples(br, lines, triples);
            } catch (Exception ex) {
                Logger.getLogger(LODChainController.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            URL url = null;
            try {
                url = new URL(fileURL);
            } catch (MalformedURLException ex) {
                Logger.getLogger(LODChainController.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

            try {
                if (fileURL.endsWith("nt") || fileURL.endsWith("nq")) {
                    br = new BufferedReader(new InputStreamReader(url.openStream()));
                    pairs = readAndReturnPairsNTriples(br, lines, triples);
                } else {
                    pairs = readAndReturnPairsOtherFormats(url, fileURL, lines, triples);
                }
            } catch (Exception ex) {

                Logger.getLogger(LODChainController.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        int count = 0, zeros = 0;

        return pairs;

    }

    public HashSet<String> readAndReturnPairsOtherFormats(URL url, String file, int lines, boolean triples) throws IOException {
        int count = 0, zeros = 0;
        HashSet<String> pairs = new HashSet<String>();
        String sCurrentLine;

        InputStream inputStream = url.openStream();
        String baseURI = url.toString();
        RDFFormat format = RDFFormat.TURTLE;
        if (file.endsWith(".ttl")) {
            format = RDFFormat.TURTLE;
        } else if (file.endsWith(".rdf")) {
            format = RDFFormat.RDFXML;
        } else if (file.endsWith(".jsonld")) {
            format = RDFFormat.JSONLD;
        }
        //System.out.println(url + " " + file.toString());
        try (GraphQueryResult res = QueryResults.parseGraphBackground(inputStream, baseURI, format)) {
            while (res.hasNext()) {
                Statement st = res.next();
                sCurrentLine = "";
                if (st.getSubject().toString().startsWith("http")) {
                    sCurrentLine = "<" + st.getSubject().toString() + ">\t";
                } else {
                    sCurrentLine = st.getSubject().toString() + "\t";
                }
                if (st.getPredicate().toString().startsWith("http")) {
                    sCurrentLine += "<" + st.getPredicate().toString() + ">\t";
                } else {
                    sCurrentLine += st.getPredicate().toString() + "\t";
                }
                if (st.getObject().toString().startsWith("http")) {
                    sCurrentLine += "<" + st.getObject().toString();
                } else {
                    sCurrentLine += st.getObject().toString();
                }

                //System.out.println(sCurrentLine);
                String[] line = sCurrentLine.split("\t");
                count++;
                if (count > lines) {
                    break;
                }

                newDataset.allTriples++;
                if (sCurrentLine.contains("_:") || sCurrentLine.contains("http://www.w3.org/2002/07/owl#differentFrom") || sCurrentLine.startsWith("#")) {
                    continue;
                }

                if (line.length < 3) {
                    line = sCurrentLine.split(" ");
                }
                if (line.length < 3) {
                    continue;
                }
                if (line[1].contains("http://www.w3.org/2002/07/owl#sameAs") && !sCurrentLine.contains("_:")) {
                    pairs.add(line[0].replace("<", "").replace(">", "").replace("http://www.wikidata.org/wiki", "http://www.wikidata.org/entity").replace("https://www.wikidata.org/wiki", "http://www.wikidata.org/entity") + "\t" + line[2].replace("<", "").replace(">", "").replace("http://www.wikidata.org/wiki", "http://www.wikidata.org/entity"));
                    //continue;
                } else if (triples == true) {
                    this.readLines.add(sCurrentLine);
                }

                if (line[0].startsWith("<http")) {
                    uris.add(line[0].replace("<", "").replace(">", ""));
                }
                if (!line[1].contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && line[2].startsWith("<http")) {
                    uris.add(line[2].replace("<", "").replace(">", "").replace("http://www.wikidata.org/wiki", "http://www.wikidata.org/entity").replace("https://www.wikidata.org/wiki", "http://www.wikidata.org/entity"));
                } else if (line[1].contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                    clses.add(line[2].replace("<", "").replace(">", ""));
                }
                props.add(line[1].replace("<", "").replace(">", ""));

            }
        } catch (RDF4JException e) {
            System.out.println(e);
        } finally {
            inputStream.close();
        }

        newDataset.uris = uris.size();
        return pairs;

    }

    public HashSet<String> readAndReturnPairsNTriples(BufferedReader br, int lines, boolean triples) throws IOException {
        int count = 0, zeros = 0;
        HashSet<String> pairs = new HashSet<String>();
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            String[] line = sCurrentLine.split("\t");
            count++;
            if (count > lines) {
                break;
            }

            newDataset.allTriples++;
            if (sCurrentLine.contains("_:") || sCurrentLine.contains("http://www.w3.org/2002/07/owl#differentFrom") || sCurrentLine.startsWith("#")) {
                continue;
            }

            if (line.length < 3) {
                line = sCurrentLine.split(" ");
            }
            if (line.length < 3) {
                continue;
            }
            if (line[1].contains("http://www.w3.org/2002/07/owl#sameAs") && !sCurrentLine.contains("_:")) {
                pairs.add(line[0].replace("<", "").replace(">", "").replace("http://www.wikidata.org/wiki", "http://www.wikidata.org/entity").replace("https://www.wikidata.org/wiki", "http://www.wikidata.org/entity") + "\t" + line[2].replace("<", "").replace(">", "").replace("http://www.wikidata.org/wiki", "http://www.wikidata.org/entity"));
                //continue;
            } else if (triples == true) {
                this.readLines.add(sCurrentLine);
            }

            if (line[0].startsWith("<http")) {
                uris.add(line[0].replace("<", "").replace(">", ""));
            }
            if (!line[1].contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && line[2].startsWith("<http")) {
                uris.add(line[2].replace("<", "").replace(">", "").replace("http://www.wikidata.org/wiki", "http://www.wikidata.org/entity").replace("https://www.wikidata.org/wiki", "http://www.wikidata.org/entity"));
            } else if (line[1].contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                clses.add(line[2].replace("<", "").replace(">", ""));
            }
            props.add(line[1].replace("<", "").replace(">", ""));

        }
        newDataset.uris = uris.size();
        return pairs;

    }

    public Map<Integer, Set<String>> sameAsIndexFile2(HashSet<String> pairs) {
        newDataset.sameAsPairsNum = pairs.size();
        Map<Integer, Set<String>> sameAsList = new HashMap<Integer, Set<String>>(2000000);
        HashMap<String, Integer> sameAsIndex = new HashMap<String, Integer>(3000000);
        int id = 0, count = 0, times = 0;
        int uniqueIDs = 0, pairsNum = 0;
        BufferedReader br = null;

        long startTime = System.currentTimeMillis();

        int i = 0;
        for (String sCurrentLine : pairs) {
            i++;
            String[] pairMembers = sCurrentLine.split("\t");
            if (pairMembers.length != 2 || sCurrentLine.contains("nytimes")) {
                continue;
            }
            if (i >= 1000000 && i % 1000000 == 0) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                //System.out.println("Seconds for " + i + "\t" + (double) estimatedTime / (double) 1000);
            }

            pairsNum++;
            String member1 = pairMembers[0].trim();
            String member2 = pairMembers[1].trim();
            boolean member1InIndex = sameAsIndex.containsKey(member1);
            boolean member2InIndex = sameAsIndex.containsKey(member2);
            if (!member1InIndex && !member2InIndex) {
                sameAsIndex.put(member1, id);
                sameAsIndex.put(member2, id);
                Set<String> mem = new HashSet<String>();
                mem.add(member1);
                mem.add(member2);
                sameAsList.put(id, mem);
                id++;
                uniqueIDs++;
            } else if (member1InIndex && !member2InIndex) {
                sameAsIndex.put(member2, sameAsIndex.get(member1));
                sameAsList.get(sameAsIndex.get(member1)).add(member2);
            } else if (!member1InIndex && member2InIndex) {
                sameAsIndex.put(member1, sameAsIndex.get(member2));

                sameAsList.get(sameAsIndex.get(member2)).add(member1);
            } else if (sameAsIndex.get(member2).intValue() != sameAsIndex.get(member1).intValue()) {
                int member1ID = sameAsIndex.get(member1);
                int member2ID = sameAsIndex.get(member2);
                int min, max;
                if (member1ID < member2ID) {
                    min = new Integer(member1ID);
                    max = new Integer(member2ID);
                } else {
                    min = new Integer(member2ID);
                    max = new Integer(member1ID);
                }
                for (String str : sameAsList.get(max)) {

                    sameAsIndex.put(str, min);
                    //System.out.println("After:"+sameAsIndex.get(str));
                }
                uniqueIDs--;
                ArrayList<String> list = new ArrayList<String>(sameAsList.get(max));
                sameAsList.get(min).addAll(list);
                sameAsList.remove(max);

            }
        }
        uris.removeAll(sameAsIndex.keySet());

        //System.out.println(sameAsIndex.size());
        int URIs = 0;
        sameAsIndex.clear();
        //System.out.println("Pairs Num" + pairsNum);

        //System.out.println("Unique IDs: " + uniqueIDs);
        // //System.out.println("Inferred: " + (relationships - pairsNum));
        // //System.out.println("Biggest: "+ biggest);
        //System.out.println(URIs);
        //System.out.println(sameAsList.size());
        //  System.out.println(sameAsList);
        for (String uri : uris) {
            Set<String> mem = new HashSet<String>();
            mem.add(uri);
            sameAsList.put(id, mem);
            id++;
        }

        return sameAsList;
    }

    public String getPrefix(String URI) {
        String prefix = "";
        if (URI.startsWith("http://")) {
            String temp = URI.replace("http://", "").split("/")[0];
            prefix = "http://" + temp;
        } else if (URI.startsWith("https://")) {
            String temp = URI.replace("https://", "").split("/")[0];
            prefix = "https://" + temp;
        }
        return prefix;
    }

    public HashSet<String> setIDsForUris(Map<Integer, Set<String>> sameAsList, String pref) throws SQLException, IOException {
        ConnectToVirtuoso cn = new ConnectToVirtuoso();
        HashSet<String> URIs = new HashSet<String>();
        try {
            cn.startJDBC_Connection();
        } catch (ClassNotFoundException ex) {
            //  Logger.getLogger(RunQuery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            // Logger.getLogger(RunQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        String input = "";
        int k = 0;
        conn = ConnectToVirtuoso.getJDBC_Connection();
        HashMap<String, Boolean> prefixesCache = new HashMap<String, Boolean>();
        for (int i : sameAsList.keySet()) {

            String code = "";
            boolean error = false;

            HashMap<String, String> mapping = new HashMap<String, String>();
            for (String URI : sameAsList.get(i)) {
                newDataset.allURIs++;
                String URIprefix = getPrefix(URI);
                if (!prefixesCache.containsKey(URIprefix)) {
                    newDataset.uniquePrefixes++;
                    if (pref.equals("false")) {
                        prefixesCache.put(URIprefix, true);

                    } else {
                        boolean flag = existsPrefix(URIprefix, 50);
                        prefixesCache.put(URIprefix, flag);
                    }

                }
                if (prefixesCache.containsKey(URIprefix) && prefixesCache.get(URIprefix) == false) {
                    mapping.put(URI, "");

                    continue;
                }

                if (newDataset.prefixURIs % 1000 == 0) {
                    //System.out.println(newDataset.prefixURIs);
                }

                //    if (!URI.startsWith("http://www.wikidata.org/") && !URI.startsWith("http://dbpedia") && !URI.startsWith("http://viaf")) {
                //    continue;
                //  }
                newDataset.prefixURIs++;
                newDataset.indexReads++;
                String x = getEntityCode(URI, sq);

                if (!"".equals(x)) {
                    if (!code.equals(x) && !code.equals("")) {
                        error = true;
                        //System.out.println("Error " + URI);
                    } else {
                        code = x;
                        mapping.put(URI, code);
                    }

                } else {
                    mapping.put(URI, "");
                }
            }
            if (error != true && !"".equals(code)) {
                if (code.startsWith("http://www.ics.forth.gr/isl/code/C") || code.startsWith("http://www.ics.forth.gr/isl/code/P")) {
                    continue;
                }
                newDataset.datasetCommonEntities++;
                newDataset.entitiesNumber++;

                Entity ent = new Entity();
                this.setEntityInformation(code, ent, true);
                //ent.provenance = this.getProvenance(code);
                ent.numberOfURIsOLD = sameAsList.get(i).size();
                //ent.numberOfURIs=this.getEntityURIsNumber(code);
                //System.out.println(code+" "+ent.numberOfURIs);
                String directCounts = ent.tempDC;
                if (directCounts.contains("DC")) {
                    String[] split = directCounts.split("DC")[1].split("\\$");
                    for (String str : split) {

                        String[] split2 = str.split("\t");
                        ent.getDirectCounts().put(split2[0], Integer.parseInt(split2[1]));
                    }
                }

                ent.code = code;
                entities.put(code.replace("http://www.ics.forth.gr/isl/code/", ""), ent);
                for (String URI : sameAsList.get(i)) {
                    if (!newDataset.getDatasetURL().toLowerCase().contains("world_war") || !this.getPrefix(URI).contains("ldf.fi")) {
                        newDataset.prevConnections.add(this.getPrefix(URI).replace("http://www.", "")
                                .replace("https://www.", "").replace("http://", "").replace("https://", ""));
                    }
                    if (mapping.get(URI).equals("")) {
                        ent.newURIs.add(URI);
                    } else {
                        ent.existingURI = URI;
                    }

                    ent.LODsyndesisEntity = true;
                    ent.URI = URI;
                    entityToID.put(URI, code.replace("http://www.ics.forth.gr/isl/code/", ""));
                }

            } else if (error != true && "".equals(code)) {
                newDataset.entitiesNumber++;
                code = "http://www.ics.forth.gr/isl/code/EIDL" + newDataset.getDatasetID() + countIDs++;
                Entity ent = new Entity();
                ent.provenance = newDataset.getDatasetID();
                ent.code = code;
                entities.put(code.replace("http://www.ics.forth.gr/isl/code/", ""), ent);
                for (String URI : sameAsList.get(i)) {

                    if (mapping.get(URI).equals("")) {
                        ent.newURIs.add(URI);
                    }
                    ent.LODsyndesisEntity = false;
                    entityToID.put(URI, code.replace("http://www.ics.forth.gr/isl/code/", ""));
                }

            } else if (error == true) {
                String err = "";
                boolean dataBib = false;
                HashSet<String> prefixes = new HashSet<String>();
                for (String URI : sameAsList.get(i)) {
                    if (URI.startsWith("http://data.bibliotheken")) {
                        dataBib = true;
                    }
                    prefixes.add(this.getPrefix(URI));
                    err += URI + "\t";
                }
                //newDataset.errors.add(err);

                if (!(prefixes.size() == sameAsList.get(i).size() && dataBib == true)) {
                    err = err.substring(0, err.length() - 1);
                    newDataset.errors.add(err);
                }
            }
        }

        // System.out.println("Size:" + URIs.size());
        for (String property : props) {
            // System.out.println(property);
            if (this.propertyToID.containsKey(property)) {
                if (!properties.containsKey(this.propertyToID.get(property))) {
                    Property p = new Property();
                    p.url = property;
                    p.setCode(this.propertyToID.get(property));
                    p.provenance = this.getProvenance("http://www.ics.forth.gr/isl/code/" + p.getCode());
                    p.virtuosoTriples += "<http://www.ics.forth.gr/isl/code/" + p.getCode() + "> <http://www.ics.forth.gr/isl/provenance> \"" + p.provenance + "\" .\n";
                    p.setLODsyndesisEntity(true);
                    p.virtuosoTriplesToDelete += "DELETE DATA FROM   { <http://www.ics.forth.gr/isl/code/" + p.getCode() + "> <http://www.ics.forth.gr/isl/provenance> \""
                            + p.getProvenance().replace("," + newDataset.datasetID, "") + "\"} .\n";
                    p.addToPropFile = property + "\t" + p.getCode();
                    newDataset.commonProperties++;
                    properties.put(p.getCode(), p);

                }
            } else {
                String code = "PL" + newDataset.getDatasetID() + count2++;
                Property p = new Property();
                p.setCode(code);
                p.url = property;
                p.provenance = newDataset.datasetID;

                newDataset.uniqueProperties++;
                p.virtuosoTriples += "<" + property + "> <http://www.ics.forth.gr/isl/identifier> <http://www.ics.forth.gr/isl/code/" + code + "> .\n";
                p.virtuosoTriples += "<http://www.ics.forth.gr/isl/code/" + code + "> <http://www.ics.forth.gr/isl/provenance> \"" + p.provenance + "\" .\n";
                p.addToPropFile = property + "\t" + p.getCode();
                properties.put(code, p);

                this.propertyToID.put(property, code);
            }
        }

        for (String object : clses) {

            String cl = this.getClass(object);
            RDFClass newCL = new RDFClass();
            if (cl.equals("")) {
                cl = "CL" + newDataset.getDatasetID() + countIDs++;
                newCL.code = cl;
                newCL.provenance = newDataset.datasetID;
                newDataset.uniqueClasses++;
                newCL.virtuosoTriples += "<http://www.ics.forth.gr/isl/code/" + newCL.getCode() + "> <http://www.ics.forth.gr/isl/provenance> \""
                        + newCL.getProvenance() + "\"} \n";
                newCL.virtuosoTriples += "<" + object + "> <http://www.ics.forth.gr/isl/identifier> <http://www.ics.forth.gr/isl/code/" + newCL.code + "> .\n";

            } else {
                newCL.code = cl;
                newDataset.commonClasses++;
                newCL.provenance = this.getProvenance("http://www.ics.forth.gr/isl/code/" + cl);
                newCL.virtuosoTriples += "<http://www.ics.forth.gr/isl/code/" + newCL.getCode() + "> <http://www.ics.forth.gr/isl/provenance> \""
                        + newCL.getProvenance() + "\"} . \n";

                newCL.virtuosoTriplesToDelete += "DELETE DATA FROM  { <http://www.ics.forth.gr/isl/code/" + newCL.getCode() + "> <http://www.ics.forth.gr/isl/provenance> \""
                        + newCL.getProvenance().replace("," + newDataset.datasetID, "") + "\"} \n";

            }
            newCL.URI = object;
            classToID.put(object, cl);
            classes.put(cl, newCL);
        }

        return URIs;
    }

    /**
     * the URI of an entity in LODsyndesis
     *
     * @param entity the code of an entity in LODsyndesis
     * @param sparql a sparql instance
     * @return the URI of an entity in LODsyndesis
     */
    public static String getEntityCode(String entity, SPARQLQuery sparql) {
        if (entity.contains("geonames") && !entity.contains("sws.geonames")) {
            entity = entity + "/";
            // System.out.println(entity);
        }
        String query = "select distinct ?code where { <" + entity.replace("\"", "") + "> <http://www.ics.forth.gr/isl/identifier> ?code}";
        String[][] queryResults = sparql.runSPARQLQuery(query, false, true, "\t");
        if ((queryResults.length > 1 && queryResults[1][0] != null)) {
            String code = queryResults[1][0];
            return code;
        }
        return "";
    }

    /**
     * Returns all the triples containing URIs for an entity
     *
     * @param entityCode the code of an entity in LODsyndesis index
     * @param sparql a sparql instance
     * @return all the triples containing URIs for an entity
     * @throws IOException
     */
    public HashMap<String, Set<String>> getPredicateObjectsOfEntity(String identifiers) throws IOException {
        String[] splitIDs = identifiers.split(",");
        int triplesH = 0;
        HashMap<String, Set<String>> allTriples = new HashMap<>();
        for (int j = 0; j < splitIDs.length; j++) {
            if (splitIDs[j] == null || splitIDs[j].equals("")) {
                continue;
            }
            String identifier = splitIDs[j];
            int id = Integer.parseInt(identifier.split(":")[0]);
            long bytesStart = Long.parseLong(identifier.split(":")[1]);
            File myfile = new File(folder + "0" + id + "");
            if (id >= 10) {
                myfile = new File(folder + id + "");
            }

// where to seek to
            //   long seekToByte = (recordIWantToStartAt == 1 ? 0 : ((recordIWantToStartAt - 1) * sizeofrecordinbytes));
// byte the reader will jump to once we know where to go
            long startAtByte = 0;

// seek to that position using a RandomAccessFile
            // NOTE since we are using fixed length records, you could actually skip this 
            // and just use our seekToByte as the value for the BufferedReader.skip() call below
            RandomAccessFile raf = new RandomAccessFile(myfile, "r");
            ////System.out.println(raf);
            long p = bytesStart;
            //raf.seek(p);
            raf.skipBytes((int) bytesStart);
            boolean toBreak = false;
            int count = 0;
            String currentPID = "";
            while (toBreak == false) {
                byte[] bytes = new byte[3000];

                //raf.read(bytes);
                String k = raf.readLine();//  new String(bytes, "UTF-8");
                //  String k = new String(utf.getBytes("ISO-8859-1"), "UTF-8");
                // System.out.println(k);
                String[] split = k.split("\n");

                for (String pr : split) {
                    //System.out.println(pr);
                    if (pr.startsWith("EID") && count == 0) {
                        count = 1;
                        continue;
                    } else if (pr.startsWith("EID") && count == 1) {
                        toBreak = true;
                        break;
                    }
                    String[] split2 = pr.split("\t");
                    if (split2.length > 1 && split2[1].startsWith("P")) {
                        currentPID = split2[1];

                        if (!allTriples.containsKey(currentPID)) {
                            allTriples.put(currentPID, new HashSet<String>());
                        }

                    } else {
                        //  //System.out.println(currentPID);
                        if (allTriples.containsKey(currentPID)) {
                            if (j > 0 && pr.split("\t")[3].contains(",")) {
                                String[] sp = pr.split("\t")[3].split(",");
                                String toRemove = "\t\t" + pr.split("\t")[2] + "\t";
                                for (int cnt = 0; cnt < sp.length - 1; cnt++) {
                                    toRemove += sp[cnt] + ",";
                                }
                                toRemove = toRemove.substring(0, toRemove.length() - 1);
                                allTriples.get(currentPID).remove(toRemove.trim());
                                triplesH--;
                            }
                            allTriples.get(currentPID).add(pr.trim());
                            if (triplesH >= 10000) {
                                toBreak = true;
                                break;
                            }
                            triplesH++;
                        }
                        ////System.out.println(pr);
                    }

                }

            }

            raf.close();
        }
        return allTriples;
        //return result;
    }

    /**
     * the file pointer of an entity in LODsyndesis index
     *
     * @param code the code of an entity in LODsyndesis
     * @param sparql a sparql instance
     * @return the file pointer of an entity in LODsyndesis index
     */
    public static String getEntityFilePointer(String code, SPARQLQuery sparql) {
        String query = "select distinct ?SID where { <" + code + "> <http://www.ics.forth.gr/isl/identifier> ?SID}";
        String[][] queryResults = sparql.runSPARQLQuery(query, false, true, "\t");

        if ((queryResults.length > 1 && queryResults[1][0] != null)) {
            String filePointer = queryResults[1][0];
            if (filePointer.contains(":")) {
                return filePointer;
            }
        }

        return null;

    }

    public int getEntityURIsNumber(String code) {
        String query = "select count distinct ?SID where { ?SID <http://www.ics.forth.gr/isl/identifier> <" + code + ">}";
        String[][] queryResults = sq.runSPARQLQuery(query, false, true, "\t");

        if ((queryResults.length > 1 && queryResults[1][0] != null)) {
            String cnt = queryResults[1][0];
            return Integer.parseInt(cnt);

        }

        return 0;

    }

}
