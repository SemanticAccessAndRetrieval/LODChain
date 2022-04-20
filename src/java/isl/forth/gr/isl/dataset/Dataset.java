
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */
package isl.forth.gr.isl.dataset;

import ics.forth.gr.isl.lodchaincode.LODChainController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Michalis Mountantonakis
 */
public class Dataset {

    public String datasetName, datasetURL;
    public int datasetCommonEntities;
    public int indexReads = 0, uris = 0, allURIs = 0, prefixURIs = 0, uniquePrefixes = 0;
    public int inferredRelationships;
    public int allTriples;
    public double avgURIsInSameAs;
    public int sameAsPairsNum;
    public String datasetID, SameAsErrors = "";
    public String threeDLODDat = "Dataset,Category,Triples,URIs,Literals,Blank_Nodes,Degree, Height, Width\n", threeDLODConn = "#links\nsource1,source2,commonURIs\n";
    public String csvMetadata = "";
    public String topTriadDatasets = "";
    public int datasetTriples, datasetUniqueTriples, commonTriples, entitiesNumber, commonTriplesDatasets, uniqueProperties, commonProperties, allComplementaryTriples = 0;
    public double avgInferred = 0;
    public HashMap<String, Integer> entitiesDirectCounts = new HashMap<String, Integer>();
    public HashMap<String, Integer> classesDirectCounts = new HashMap<String, Integer>();
    public HashMap<String, Integer> propertiesDirectCounts = new HashMap<String, Integer>();
    public String domains = "", datasetsMetrics = "", commonFactsDatasets = "";
    public HashMap<String, Integer> entitiesPairs = new HashMap<String, Integer>();
    public HashSet<String> errors = new HashSet<String>();
    public HashSet<String> errorsURIs = new HashSet<String>();
    public HashSet<String> prevConnections = new HashSet<String>();

    public HashSet<String> getPrevConnections() {
        return prevConnections;
    }
    public String topConnectedDataset, entitiesNumberString, propertiesNumberString, classesNumberString;
    public String topConnectedDomain, mostUniqueContentDataset;

    public String rankingDomainAfterC = "", rankingDomainBeforeC = "", rankingAfterC = "", rankingBeforeC = "";

    public String getRankingDomainAfterC() {
        return rankingDomainAfterC;
    }

    public String getRankingDomainBeforeC() {
        return rankingDomainBeforeC;
    }

    public String getRankingAfterC() {
        return rankingAfterC;
    }

    public String getRankingBeforeC() {
        return rankingBeforeC;
    }

    public String bestPairEntities = "", bestPairProperties = "", bestPairClasses = "", bestPairTriples = "", bestPairComp = "";

    public int getInferredRelationships() {
        return inferredRelationships;
    }

    public int getSameAsPairsNum() {
        return sameAsPairsNum;
    }
    public String bestTriadEntities = "", bestTriadProperties = "", bestTriadClasses = "", bestTriadTriples = "", bestTriadComp = "";
    public String bestQuadEntities = "", bestQuadProperties = "", bestQuadClasses = "", bestQuadTriples = "", bestQuadComp = "";

    public HashMap<String, Integer> triplesDirectCounts = new HashMap<String, Integer>();
    public int connectionsNumber = 0, commonFactsConnectionsNumber = 0, commonPropsConnectionsNumber = 0;
    public String virtuosoTriples = "", virtuosoTriplesCode = "";
    public String connections = "", triplesNumberString = "";
    public int commonEntitiesDatasets = 0;
    public int bnodeCount = 0;
    public int classesNumber;
    public int propertiesNumber, prevConnectionsSize;
    private int commonClassesConnectionsNumber;
    public int uniqueClasses;
    public int commonClasses;
    public String VoIDMetadata = "", csvMeasurements = "#########\nSubset,Number of Common Entities\n";

    public String getVoIDMetadata() {
        return VoIDMetadata;
    }

    public void setDatasetDomain(String datasetDomain) {
        this.datasetDomain = datasetDomain;
    }
    private int numberOfConnectedDomain;
    public TreeMap<Integer, HashSet<String>> topDatasets = new TreeMap<Integer, HashSet<String>>(Collections.reverseOrder());
    public TreeMap<Integer, HashSet<String>> topDatasetsCMNFacts = new TreeMap<Integer, HashSet<String>>(Collections.reverseOrder());
    public TreeMap<Integer, HashSet<String>> topDatasetsCMNProperties = new TreeMap<Integer, HashSet<String>>(Collections.reverseOrder());
    public TreeMap<Integer, HashSet<String>> topDatasetsCMNClasses = new TreeMap<Integer, HashSet<String>>(Collections.reverseOrder());

    public TreeMap<Integer, HashSet<String>> topDatasetsCompFacts = new TreeMap<Integer, HashSet<String>>(Collections.reverseOrder());
    public String datasetDomain;

    public void createVirtuosoTriples(HashMap<String, Integer> results, HashMap<String, Integer> triplesResults, HashMap<String, Integer> propResults, HashMap<String, Integer> classResults) {
        this.createVirtuosoTriplesR(results, "Entities", 5);
        this.createVirtuosoTriplesR(triplesResults, "Triples", 1);
        this.createVirtuosoTriplesR(propResults, "Properties", 5);
        this.createVirtuosoTriplesR(classResults, "Classes", 1);
        this.virtuosoQueryForTriplesDC();

        //System.out.println(this.virtuosoTriples);
    }

    public String[][] getDatasetStatistics(String[][] res) {

        res[3][0] += "Number of Datasets <br> with Common Entities<br> (or Links)";//Most Connected Dataset to " + datasetName;
        res[3][1] = "<div id=\"connectionsBar\"></div>"; //topConnectedDataset;

        res[2][0] += "Average Number of <br> Datasets per Common Entity<br>";
        if (datasetCommonEntities != 0) {
            res[2][1] = ((double) (commonEntitiesDatasets) / (double) datasetCommonEntities) + "";
        } else {
            res[2][1] = "0";
        }
        //res[1][0] = "Previous Connections";
        // res[1][1] = prevConnections.size() + "";
        // res[2][0] = "Current Connections";
        // res[2][1] = connectionsNumber + "";
        res[4][0] += "Connections Increase<br> Percentage";
        if (prevConnections.isEmpty()) {
            prevConnectionsSize = 0;
        } else {
            prevConnectionsSize = prevConnections.size();// - 1;
            if(prevConnections.contains("ldf.fi"))
                prevConnectionsSize=prevConnectionsSize-1;
        }
        if (!prevConnections.isEmpty()) {
            res[4][1] = (double) (connectionsNumber - (prevConnectionsSize)) * 100 / (double) (prevConnectionsSize) + "%";
        } else {
            res[4][1] = "0%";
        }
        res[7][0] += "Dataset Ranking <br> in Connections Number";
        rankingBeforeC = getRankingOfDataset(prevConnectionsSize, null) + "";
        rankingAfterC = getRankingOfDataset(connectionsNumber, null) + "";
        rankingDomainBeforeC = getRankingOfDataset(prevConnectionsSize, this.datasetDomain) + "";
        rankingDomainAfterC = getRankingOfDataset(connectionsNumber, this.datasetDomain) + "";
        res[7][1] = "Ranking in All Domains Before Closure: " + getRankingOfDataset(prevConnectionsSize, null);
        res[7][1] += "<br>Ranking in All Domains After Closure: " + getRankingOfDataset(connectionsNumber, null);

        res[8][0] += "Dataset Ranking <br> in Connections Number in <br>" + this.datasetDomain.replace("http://dbpedia.org/resource/", " ") + " Domain";
        res[8][1] = "Ranking in " + this.datasetDomain.replace("http://dbpedia.org/resource/", " ") + "  Domain Before Closure: " + getRankingOfDataset(prevConnectionsSize, this.datasetDomain);
        res[8][1] += "<br>Ranking in " + this.datasetDomain.replace("http://dbpedia.org/resource/", " ") + " Domain After Closure: " + getRankingOfDataset(connectionsNumber, this.datasetDomain);

        res[9][0] += "Positions Gained in Ranking";
        res[9][1] = "<div id=\"rankingBar\"></div>";
        entitiesNumberString = "Unique Entities:" + (entitiesNumber - datasetCommonEntities) + ",Common Entities:" + datasetCommonEntities;

        propertiesNumberString = "Unique Properties:" + uniqueProperties + ",Common Properties:" + commonProperties;

        classesNumberString = "Unique Classes:" + uniqueClasses + ",Common Classes:" + commonClasses;

        triplesNumberString = "Unique Facts:" + datasetUniqueTriples + ",Common Facts:" + commonTriples;

        res[1][0] += "Dataset Entities";//Most Connected Dataset to " + datasetName;
        res[1][1] = "<div id=\"piechartEntities\"></div>";  //topConnectedDataset;

        res[6][0] += "Number of Connections <br> with each Domain";
        res[6][1] = "<div id=\"piechartDomains\"></div>";

        //res[8][0] += "Top Connected Triad";
        // res[8][1] = topTriadDatasets;
        res[5][0] += "K Most Connected <br> Datasets";
        res[5][1] = "<div id=\"chart_div\"></div>\n";
        datasetsMetrics = setTopConnectedDatasets(10);

        this.VoIDMetadata += "<" + this.datasetURL + "> <http://www.w3.org/2000/01/rdf-schema#label> \"" + this.datasetName + "\" .\n";
        this.VoIDMetadata += "<" + this.datasetURL + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://rdfs.org/ns/void#Dataset>.\n";
        this.VoIDMetadata += "<" + this.datasetURL + "> <http://purl.org/dc/terms/subject> <" + this.datasetDomain + "> .\n";
        this.VoIDMetadata += "<" + this.datasetURL + "> <http://rdfs.org/ns/void#triples> \"" + this.allTriples + "\" .\n";
        this.VoIDMetadata += "<" + this.datasetURL + "> <http://rdfs.org/ns/void#entities> \"" + entitiesNumber + "\" .\n";
        this.VoIDMetadata += this.virtuosoTriples;

        this.csvMetadata = "Category,Value\n";
        this.csvMetadata += "Dataset Name," + this.datasetName + "\n";
        this.csvMetadata += "Dataset URL," + this.datasetURL + "\n";
        this.csvMetadata += "Dataset Domain," + this.datasetDomain + "\n";
        this.csvMetadata += "Dataset Triples," + this.allTriples + "\n";
        this.csvMetadata += "Dataset Entities," + this.entitiesNumber + "\n";
        this.csvMetadata += "Dataset Unique Entities," + (entitiesNumber - datasetCommonEntities) + "\n";
        this.csvMetadata += "Dataset Common Entities," + (datasetCommonEntities) + "\n";
        this.csvMetadata += "Average Number of Datasets per Common Entity," + ((double) (commonEntitiesDatasets) / (double) datasetCommonEntities) + "\n";
        this.csvMetadata += "Previous Connections," + (prevConnectionsSize) + "\n";
        this.csvMetadata += "Current Connections," + (this.connectionsNumber) + "\n";
        if (prevConnections.size() != 0) {
            this.csvMetadata += "Connections Increase Percentage," + (double) (connectionsNumber - (prevConnectionsSize)) * 100 / (double) (prevConnectionsSize) + "%\n";
        } else {
            this.csvMetadata += "Connections Increase Percentage,0%" + "\n";
        }
        this.csvMetadata += "Number of Connected Domains," + numberOfConnectedDomain + "\n";

        String[] split = domains.split(",");
        for (String str : split) {
            if (str.length() < 2) {
                break;
            }
            this.csvMetadata += "Number of Connections with "
                    + str.split(" ")[0].replace("http://dbpedia.org/resource/", "") + " Domain," + str.split(" ")[1] + "\n";
        }
        this.csvMetadata += "Ranking in All Domains," + getRankingOfDataset(connectionsNumber, null) + "\n"
                + "Ranking in " + this.datasetDomain.replace("http://dbpedia.org/resource/", " ") + " Domain," + getRankingOfDataset(connectionsNumber, this.datasetDomain) + "\n";

        this.csvMetadata += "Number of Initial SameAs  Relationships," + this.sameAsPairsNum + "\n";
        this.csvMetadata += "Number of Inferred SameAs  Relationships," + this.inferredRelationships + "\n";
        this.csvMetadata += "Increase Percentage of SameAs  Relationships,";
        this.csvMetadata += ((this.inferredRelationships + this.sameAsPairsNum) - this.sameAsPairsNum) * 100 / (double) this.sameAsPairsNum + "%\n";
        this.csvMetadata += csvMeasurements;

        // res[19][0] += "More Measurements <br>for your Dataset";
        //  res[19][1] = "<a href=\"https://demos.isl.ics.forth.gr/lodsyndesis/Config?type=advancedServices\">Visit the Page for More Advanced Measurements</a>";
        //res[12][1] = "Common Classes with"
        //       +                 this.commonClassesConnectionsNumber+ "Datasets\n"
        //      + "Unique Classes " + uniqueClasses + " Common Classes: " + commonClasses+"\n"+this.setCommonClassesDatasets(10);
        return res;
    }

    public String[][] getEquivStatistics(String[][] res) {

        res[2][0] += "Number of  SameAs  Relationships";//Most Connected Dataset to " + datasetName;
        res[2][1] = "<div id=\"sameAsBar\"></div>"; //topConnectedDataset; 

        //res[1][0] += "Number of Initial SameAs  Relationships";
        //res[1][1] = this.sameAsPairsNum + "";
        //res[2][0] += "Number of Inferred SameAs  Relationships";
        //res[2][1] = this.inferredRelationships + "";
        res[3][0] += "Increase Percentage of SameAs  Relationships";
        res[3][1] = ((this.inferredRelationships + this.sameAsPairsNum) - this.sameAsPairsNum) * 100 / (double) this.sameAsPairsNum + "%";

        res[1][0] += "SameAs  Quality Check";
        res[1][1] = this.errors.size() + " Possible Errors in SameAs relationships" + " <img src=\"images/cross.png\" alt=\"Failure\" width=\"50\" height=\"50\"><br><br>";
        if (this.errors.size() > 0) {
            res[1][1] += "<br> <button class=\"button special\" id=\"myBtn\" onclick=\"getErrors();\">Download SameAs Possible Errors</button><br>";
            this.setSameAsErrors();

        } else {
            res[1][1] = "Quality Check Passed Successfully" + " <img src=\"images/tick.png\" alt=\"Success\" width=\"50\" height=\"50\">";
        }
        return res;
    }

    public int getErrors() {
        return errors.size();
    }

    public String[][] getSchemaStatistics(String[][] res) {

        res[1][0] += "Dataset Properties";
        res[1][1] = "<div id=\"piechartProperties\"></div>";

        res[2][0] += "Number of Datasets <br> with common Properties";
        res[2][1] = this.commonPropsConnectionsNumber + "";

        res[3][0] += "Top K datasets with <br> most Common <br> Properties";
        res[3][1] = "<div id=\"chart_commonProperties\"></div>";

        res[4][0] += "Classes";
        res[4][1] = "<div id=\"piechartClasses\"></div>";

        res[5][0] += "Number of Datasets <br> having common Classes";
        res[5][1] = this.commonClassesConnectionsNumber + "";

        res[6][0] += "Top K datasets having <br> most Common Classes";
        res[6][1] = "<div id=\"chart_commonClasses\"></div>";

        return res;
    }

    public String[][] getDiscoveryStatistics(String[][] res) {

        res[1][1] += this.bestPairEntities;
        res[1][2] += this.bestTriadEntities;
        res[1][3] += this.bestQuadEntities;

        res[2][1] += this.bestPairProperties;
        res[2][2] += this.bestTriadProperties;
        res[2][3] += this.bestQuadProperties;

        res[3][1] += this.bestPairClasses;
        res[3][2] += this.bestTriadClasses;
        res[3][3] += this.bestQuadClasses;

        res[4][1] += this.bestPairTriples;
        res[4][2] += this.bestTriadTriples;
        res[4][3] += this.bestQuadTriples;

        res[5][1] += this.bestPairComp;
        res[5][2] += this.bestTriadComp;
        res[5][3] += this.bestQuadComp;

        return res;
    }

    public String[][] getTriplesStatistics(String[][] res) {

        res[1][0] += "Dataset Facts";//Most Connected Dataset to " + datasetName;
        res[1][1] = "<div id=\"piechartTriples\"></div>"; //topConnectedDataset;

        res[2][0] += "K datasets having <br> most Common Facts";
        res[2][1] = "<div id=\"chart_commonFacts\"></div>";

        res[3][0] += "Common Facts<br> Verification";
        res[3][1] = "Common Facts with: " + this.commonFactsConnectionsNumber + " Datasets<br>"
                + "Each Common Fact is verified from: " + commonTriplesDatasets() + " other Datasets";
        res[4][0] += "Number of Complementary <br> Facts for the entities of your dataset";
        double increase = (double) allComplementaryTriples * 100 / ((double) datasetUniqueTriples + commonTriples);
        res[4][1] = "" + allComplementaryTriples + " (Increase " + increase + "%)";
        res[5][0] += "Top Datasets offering<br> Complementary Facts";
        res[5][1] = "<div id=\"chart_compFacts\"></div>";
        return res;
    }

    public String getCsvMetadata() {
        return csvMetadata;
    }

    public String setCommonFactsDatasets(int maxDatasets) {
        String result = "";
        int cnt = 0;
        for (int i : topDatasetsCMNFacts.keySet()) {
            for (String dst : topDatasetsCMNFacts.get(i)) {
                if (cnt == maxDatasets) {
                    break;
                }
                result += dst + " " + i + ",";
                cnt++;
            }
            //System.out.println(i);
        }
        return result;
    }

    public String setCommonClassesDatasets(int maxDatasets) {
        String result = "";
        int cnt = 0;
        for (int i : topDatasetsCMNClasses.keySet()) {
            for (String dst : topDatasetsCMNClasses.get(i)) {
                if (cnt == maxDatasets) {
                    break;
                }
                result += dst + " " + i + ",";
                cnt++;
            }
            //System.out.println(i);
        }
        return result;
    }

    public String setCommonPropsDatasets(int maxDatasets) {
        String result = "";
        int cnt = 0;
        for (int i : topDatasetsCMNProperties.keySet()) {
            for (String dst : topDatasetsCMNProperties.get(i)) {
                if (cnt == maxDatasets) {
                    break;
                }
                result += dst + " " + i + ",";
                cnt++;
            }
            //System.out.println(i);
        }
        return result;
    }

    public String setCompFactsDatasets(int maxDatasets) {
        String result = "";
        int cnt = 0;
        int total = 0;
        String bestK = "";
        for (int i : topDatasetsCompFacts.keySet()) {
            for (String dst : topDatasetsCompFacts.get(i)) {
                if (cnt == maxDatasets) {
                    break;
                }
                result += dst + " " + i + ",";
                if (cnt <= 2) {
                    bestK += (cnt + 1) + ". <a href=\"" + dst + "\">" + dst + "</a><br>";
                    total += i;
                }
                if (cnt == 0) {
                    this.bestPairComp = bestK + "<br><b>" + total + " Complementary Facts</b> <br> for the entities of <br>" + datasetName;
                } else if (cnt == 1) {
                    this.bestTriadComp = bestK + "<br><b>" + total + " Complementary Facts</b> <br> for the entities of <br>" + datasetName;
                } else if (cnt == 2) {
                    this.bestQuadComp = bestK + "<br><b>" + total + " Complementary Facts</b> <br> for the entities of <br>" + datasetName;
                }

                cnt++;
            }
            //System.out.println(i);
        }
        for (int i : topDatasetsCompFacts.keySet()) {
            for (String dst : topDatasetsCompFacts.get(i)) {
                allComplementaryTriples += i;
            }
        }

        return result;
    }

    public int getConnectionsNumber() {
        return connectionsNumber;
    }

    public int getPreviousConnections() {
        return prevConnectionsSize;
    }

    public String setTopConnectedDatasets(int maxDatasets) {
        String result = "";
        int cnt = 0;
        for (int i : topDatasets.keySet()) {
            for (String dst : topDatasets.get(i)) {
                if (cnt == maxDatasets) {
                    break;
                }
                result += dst + " " + i + ",";
                cnt++;
            }
            //System.out.println(i);
        }

        return result;
    }

    public TreeMap<Integer, HashSet<String>> getTopDatasets() {
        return topDatasets;
    }

    public void printDatasetStatistics() {

        //System.out.println("=========");
        //System.out.println("Previous Connections: " + (prevConnections.size() - 1));

        //System.out.println("Current Connections: " + connectionsNumber);

        //System.out.println("Connections Increase Percentage: " + (double) (connectionsNumber - prevConnections.size()) * 100 / (double) prevConnections.size() + "%");
        //System.out.println("Dataset Ranking in Connections: " + getRankingOfDataset(connectionsNumber));

        //System.out.println("Most Connected Dataset to " + datasetName + ": " + topConnectedDataset);

        //System.out.println("=========");
        //System.out.println("Connection with " + numberOfConnectedDomain + " Domains");

        //System.out.println("Most Connected Domain: " + topConnectedDomain);
        //System.out.println("=========");

        //System.out.println("=========\nPossible Connectivity Errors: " + errors.size());
        for (String x : errors) {
            //System.out.println(x);
        }
        //System.out.println("=========");

        //System.out.println("Dataset Entities: " + entitiesNumber);
        //System.out.println("Dataset Unique Entities: " + (entitiesNumber - datasetCommonEntities));
        //System.out.println("Dataset Common Entities: " + (datasetCommonEntities));
        //System.out.println("=========");
        //System.out.println("Dataset Triples: " + datasetTriples);
        //System.out.println("Dataset Unique Facts: " + datasetUniqueTriples);
        //System.out.println("Dataset Common Facts: " + commonTriples);
        //System.out.println("Each Common Fact is verified from: " + commonTriplesDatasets() + " other Datasets");

        //System.out.println("Dataset offering most complementary triples: " + mostUniqueContentDataset);

        //System.out.println("ALL URIS, " + allURIs + "\nURIs With prefix in LODsyndesis," + prefixURIs);
        // //System.out.println(virtuosoTriples);
    }

    public String returnMessage() {
        return "ALL URIs, " + allURIs + "\nURIs With prefix in LODsyndesis," + prefixURIs;

    }

    public int getRankingOfDataset(int tr) {
        String query = "select ?c count(distinct ?y)  where {?y ?p <http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#commonEntitiesPairs> . ?y voidwh:over ?c} group by(?c) having(count(distinct ?y)>=" + tr + ")";
        String[][] queryResults = LODChainController.sq.runSPARQLQuery(query, false, true, "\t");

        if ((queryResults.length > 1 && queryResults[1][0] != null)) {
            return queryResults.length;
        }
        return 0;
    }

    public int getRankingOfDataset(int tr, String domain) {
        String query = "select ?c count(distinct ?y)  where {?y ?p <http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#commonEntitiesPairs> . ?y voidwh:over ?c } group by(?c) having(count(distinct ?y)>=" + tr + ")";
        if (domain != null) {
            query = "select ?c count(distinct ?y)  where {?y ?p <http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#commonEntitiesPairs> . ?y voidwh:over ?c . ?c ?k <" + this.datasetDomain + ">} group by(?c) having(count(distinct ?y)>=" + tr + ")";
        }
        String[][] queryResults = LODChainController.sq.runSPARQLQuery(query, false, true, "\t");

        if ((queryResults.length > 1 && queryResults[1][0] != null)) {
            return queryResults.length;
        }
        return 0;
    }

    public double commonTriplesDatasets() {
        return (double) commonTriplesDatasets / (double) commonTriples;
    }

    public void virtuosoQueryForTriplesDC() {
        this.virtuosoTriplesCode += "<http://www.ics.forth.gr/isl/code/" + datasetID + "> \t <http://www.ics.forth.gr/isl/directCount>\t\"";
        this.virtuosoTriplesCode += connections + this.datasetID + "DC";
        HashMap<String, Integer> mostUniqueCont = new HashMap<>();
        for (String dc : triplesDirectCounts.keySet()) {

            this.virtuosoTriplesCode += dc + "\t" + triplesDirectCounts.get(dc) + "$";
            if (dc.equals(datasetID)) {
                datasetUniqueTriples = triplesDirectCounts.get(dc);
            } else if (dc.contains(datasetID)) {
                commonTriples += triplesDirectCounts.get(dc);
                commonTriplesDatasets += (dc.split(",").length - 1) * triplesDirectCounts.get(dc);
            } else if (!dc.contains(datasetID)) {
                String[] split = dc.split(",");
                for (String dst : split) {
                    if (mostUniqueCont.containsKey(dst)) {
                        mostUniqueCont.put(dst, mostUniqueCont.get(dst) + triplesDirectCounts.get(dc));
                    } else {
                        mostUniqueCont.put(dst, triplesDirectCounts.get(dc));
                    }
                }
            }
        }
        //System.out.println("Common Triples: " + commonTriples + " From: " + this.datasetTriples + " triples");
        int max = 0;
        for (String ds : mostUniqueCont.keySet()) {
            //System.out.println(ConnectionCheckerCode.IDtoDatasetName.get(Integer.parseInt(ds))+"\t"+mostUniqueCont.get(ds));
            if (mostUniqueCont.get(ds) > max) {
                mostUniqueContentDataset = LODChainController.IDtoDatasetName.get(Integer.parseInt(ds)) + " with " + mostUniqueCont.get(ds) + " triples";
                max = mostUniqueCont.get(ds);
            }
            if (this.topDatasetsCompFacts.containsKey(mostUniqueCont.get(ds))) {
                topDatasetsCompFacts.get(mostUniqueCont.get(ds)).add(LODChainController.IDtoDatasetName.get(Integer.parseInt(ds)));
            } else {
                HashSet<String> dtsets = new HashSet<String>();
                dtsets.add(LODChainController.IDtoDatasetName.get(Integer.parseInt(ds)));
                topDatasetsCompFacts.put(mostUniqueCont.get(ds), dtsets);
            }

        }
        this.virtuosoTriplesCode += "\" .";
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatasetURL() {
        return datasetURL;
    }

    public void setDatasetURL(String datasetURL) {
        this.datasetURL = datasetURL;
    }

    public String getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(String datasetID) {
        this.datasetID = datasetID;
    }

    public int getDatasetTriples() {
        return datasetTriples;
    }

    public void setDatasetTriples(int datasetTriples) {
        this.datasetTriples = datasetTriples;
    }

    public HashMap<String, Integer> getEntitiesDirectCounts() {
        return entitiesDirectCounts;
    }

    public void setEntitiesDirectCounts(HashMap<String, Integer> entitiesDirectCounts) {
        this.entitiesDirectCounts = entitiesDirectCounts;
    }

    public HashMap<String, Integer> getTriplesDirectCounts() {
        return triplesDirectCounts;
    }

    public void setTriplesDirectCounts(HashMap<String, Integer> triplesDirectCounts) {
        this.triplesDirectCounts = triplesDirectCounts;
    }

    public void createVirtuosoTriplesR(HashMap<String, Integer> results, String type, int thres) {
        String topConnected = "";
        HashMap<String, Integer> domain = new HashMap<>();
        int topConnectedScore = 0;
        int topTriad = 0, topPair = 0, topQuad = 0;
        TreeSet<Integer> pairs = new TreeSet<Integer>();
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(results);
        Map values = valueSort(sortedMap);
        for (Object ob : values.keySet()) {
            String k = (String) ob;
            String[] datasets = k.split(",");
            if (results.get(k) < thres) {
                continue;
            }
            if (datasets.length == 3 && type.equals("Entities")) {
//
//                if (results.get(k) > topTriad) {
//                    topTriadDatasets = "The  triad of datasets (containing " + datasetName + ") with most common entities is:<br><br> " + datasetName + "<br>" + datasets[1] + "<br>" + datasets[2] + "<br><br> with " + results.get(k) + " common Entities";
//                    topTriad = results.get(k);
//                }
            }
            if (datasets.length == 2 && type.equals("Entities")) {
                String dtsDomain = LODChainController.datasetNameToDomain.get(datasets[1]);
                if (domain.containsKey(dtsDomain)) {
                    domain.put(dtsDomain, domain.get(dtsDomain) + 1);
                } else {
                    domain.put(dtsDomain, 1);
                }
                connectionsNumber++;
                pairs.add(LODChainController.datasetNameToID.get(datasets[1]));
                if (results.get(k) > topConnectedScore) {
                    topConnected = datasets[1];
                    topConnectedScore = results.get(k);
                }
                //threeDLODDat+=datasets[1]+","+dtsDomain+",1000000,,,,,,\n";
                //threeDLODConn+=this.datasetName+","+datasets[1]+","+results.get(k)+"\n";

                if (this.topDatasets.containsKey(results.get(k))) {
                    topDatasets.get(results.get(k)).add(datasets[1]);
                } else {
                    HashSet<String> dtsets = new HashSet<String>();
                    dtsets.add(datasets[1]);
                    topDatasets.put(results.get(k), dtsets);
                }
                //datasetsMetrics+=datasets[1]+" "+results.get(k)+",";
            } else if (datasets.length == 2 && type.equals("Triples")) {
                commonFactsConnectionsNumber++;
                if (this.topDatasetsCMNFacts.containsKey(results.get(k))) {
                    topDatasetsCMNFacts.get(results.get(k)).add(datasets[1]);
                } else {
                    HashSet<String> dtsets = new HashSet<String>();
                    dtsets.add(datasets[1]);
                    topDatasetsCMNFacts.put(results.get(k), dtsets);
                }
                //datasetsMetrics+=datasets[1]+" "+results.get(k)+",";
            } else if (datasets.length == 2 && type.equals("Properties")) {
                this.commonPropsConnectionsNumber++;
                if (this.topDatasetsCMNProperties.containsKey(results.get(k))) {
                    topDatasetsCMNProperties.get(results.get(k)).add(datasets[1]);
                } else {
                    HashSet<String> dtsets = new HashSet<String>();
                    dtsets.add(datasets[1]);
                    topDatasetsCMNProperties.put(results.get(k), dtsets);
                }
                //datasetsMetrics+=datasets[1]+" "+results.get(k)+",";
            } else if (datasets.length == 2 && type.equals("Classes")) {
                this.commonClassesConnectionsNumber++;
                if (this.topDatasetsCMNClasses.containsKey(results.get(k))) {
                    topDatasetsCMNClasses.get(results.get(k)).add(datasets[1]);
                } else {
                    HashSet<String> dtsets = new HashSet<String>();
                    dtsets.add(datasets[1]);
                    topDatasetsCMNClasses.put(results.get(k), dtsets);
                }
                //datasetsMetrics+=datasets[1]+" "+results.get(k)+",";
            }

            if (datasets.length == 2 && results.get(k) > topPair) {
                topPair = results.get(k);
                if (type.equals("Entities")) {
                    this.bestPairEntities = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br><br><b>" + topPair + " Common Entities</b> <br> with " + datasetName;
                }
                if (type.equals("Properties")) {
                    this.bestPairProperties = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br><br><b>" + topPair + " Common Properties</b>  <br> with " + datasetName;
                }
                if (type.equals("Classes")) {
                    this.bestPairClasses = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br><br><b>" + topPair + " Common Classes</b>  <br> with " + datasetName;
                }
                if (type.equals("Triples")) {
                    this.bestPairTriples = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br><br><b>" + topPair + " Common Triples</b> <br> with " + datasetName;
                }
            }

            if (datasets.length == 3 && results.get(k) > topTriad) {
                topTriad = results.get(k);
                if (type.equals("Entities")) {
                    this.bestTriadEntities = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br>" + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br><br><b>" + topTriad + " Common Entities</b> <br>with " + datasetName;
                }
                if (type.equals("Properties")) {
                    this.bestTriadProperties = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br> " + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br><br><b>" + topTriad + " Common Properties</b> <br>with " + datasetName;
                }
                if (type.equals("Classes")) {
                    this.bestTriadClasses = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br> " + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br><br><b>" + topTriad + " Common Classes</b> <br>with " + datasetName;
                }
                if (type.equals("Triples")) {
                    this.bestTriadTriples = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br> " + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br><br><b>" + topTriad + " Common Triples</b> <br> with " + datasetName;
                }
            }
            if (datasets.length == 4 && results.get(k) > topQuad) {
                topQuad = results.get(k);
                if (type.equals("Entities")) {
                    this.bestQuadEntities = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br>" + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br> " + "3. <a href=\"" + datasets[3] + "\">" + datasets[3] + "</a><br><br><b>" + topQuad + " Common Entities</b><br> with " + datasetName;
                }
                if (type.equals("Properties")) {
                    this.bestQuadProperties = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br>" + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br> " + "3. <a href=\"" + datasets[3] + "\">" + datasets[3] + "</a><br><br><b>" + topQuad + " Common Properties</b><br> with " + datasetName;
                }
                if (type.equals("Classes")) {
                    this.bestQuadClasses = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br>" + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br> " + "3. <a href=\"" + datasets[3] + "\">" + datasets[3] + "</a><br><br><b>" + topQuad + " Common Classes</b><br> with " + datasetName;
                }
                if (type.equals("Triples")) {
                    this.bestQuadTriples = "1. <a href=\"" + datasets[1] + "\">" + datasets[1] + "</a><br>" + "2. <a href=\"" + datasets[2] + "\">" + datasets[2] + "</a><br> " + "3. <a href=\"" + datasets[3] + "\">" + datasets[3] + "</a><br><br><b>" + topQuad + " Common Triples</b><br> with " + datasetName;
                }
            }

            virtuosoTriples += getTriples(datasets, results.get(k), datasets.length, type);

        }

        if (type.equals("Entities")) {
            for (int id : pairs) {
                connections += id + ",";
            }
            int max = 0;
            for (String str : domain.keySet()) {
                //System.out.println(str + " " + domain.get(str));
                this.domains += str.replace("http://dbpedia.org/resource/", "") + " " + domain.get(str) + ",";
                numberOfConnectedDomain++;
                if (domain.get(str) >= max) {
                    this.topConnectedDomain = str + " with " + domain.get(str) + " Connections";
                    max = domain.get(str);
                }
            }

            topConnectedDataset = topConnected + " with " + topConnectedScore + " Common Entities";
        }
        //System.out.println(topConnectedDataset);
    }

    public String getTriples(String[] datasets, int score, int numberOfDatasets, String type) {
        String triples = "";
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        //System.out.println(date);.sql.Date(millis);

        String bnode = "<" + "http://www.ics.forth.gr/isl/measurement/" + this.datasetID + "/" + bnodeCount++ + ">";
        triples += bnode + "\t<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#Measurement> .\n";
        triples += bnode + "\t<http://purl.org/dc/terms/date>\t\"" + date.toString() + "\" .\n";
        triples += bnode + "\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#producesValue>\t\"" + score + "\"^^xsd:integer .\n";
        if (numberOfDatasets == 2) {
            triples += bnode + "\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#usesMetrics>\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#common" + type + "Pairs> .\n";

        }

        if (numberOfDatasets == 3) {
            triples += bnode + "\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#usesMetrics>\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#common" + type + "Triads> .\n";
        }
        if (numberOfDatasets == 4) {
            triples += bnode + "\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#usesMetrics>\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#common" + type + "Quads> .\n";
        }
        boolean flag = false;
        for (String dset : datasets) {
            triples += bnode + "\t<http://www.ics.forth.gr/isl/VoIDWarehouse/VoID_Extension_Schema.owl#over>\t<" + dset.trim() + "> .\n";
            if (flag == false) {
                csvMeasurements += dset.trim();
                flag = true;
            } else {
                csvMeasurements += "\t" + dset.trim();

            }
        }
        csvMeasurements += "," + score + "\n";
        return triples;
    }

    public static <K, V extends Comparable<V>> Map<K, V>
            valueSort(final Map<K, V> map) {
        // Static Method with return type Map and
        // extending comparator class which compares values
        // associated with two keys
        Comparator<K> valueComparator = new Comparator<K>() {

            // return comparison results of values of
            // two keys
            public int compare(K k1, K k2) {
                int comp = map.get(k1).compareTo(
                        map.get(k2));
                if (comp == 0) {
                    return 1;
                } else {
                    return -comp;
                }
            }

        };

        // SortedMap created using the comparator
        Map<K, V> sorted = new TreeMap<K, V>(valueComparator);

        sorted.putAll(map);

        return sorted;
    }

    public int getUniquePrefixes() {
        return uniquePrefixes;
    }

    public int getIndexReads() {
        return indexReads;
    }

    public void setIndexReads(int indexReads) {
        this.indexReads = indexReads;
    }

    public int getUris() {
        return uris;
    }

    public void setUris(int uris) {
        this.uris = uris;
    }

    public String getSameAsErrors() {
        return SameAsErrors;
    }

    public void setSameAsErrors() {
        int i = 1;
        for (String x : errors) {
            this.SameAsErrors += i + ".\t" + x + "\n";
            i++;
        }

    }
}
