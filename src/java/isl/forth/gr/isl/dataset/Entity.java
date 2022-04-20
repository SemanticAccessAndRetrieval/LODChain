
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */

package isl.forth.gr.isl.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Michalis Mountantonakis
 */
public class Entity {
     public HashMap<String, Set<String>> lodsyndesisIndex=new HashMap<String, Set<String>>();
    public  HashMap<String, Set<String>> datasetIndex=new HashMap<String, Set<String>>();
     public int entityID,numberOfURIs;
    public String code="",URI,existingURI;
     public String provenance;
   public   boolean LODsyndesisEntity=false;
    public  HashMap<String,Integer> directCounts=new HashMap<String,Integer>();
    public  String pointer;
    public  String virtuosoURIs="";
    public HashSet<String> newURIs=new HashSet<String>();
    public  String virtuosoProvenance="";
    public  String virtuosoTriples="";
     public String previousPointer="",tempDC="";
     public ArrayList<String> deleteQueries=new ArrayList<String>();
    public  int numberOfURIsOLD;

    public void setVirtuoso(){
        this.setVirtuosoProvenance();
        this.setVirtuosoTriples();
        this.setVirtuosoURIs();
        this.setPointer();
        //this.printVirtuosoTriples();
    }
    
    public void printVirtuosoTriples(){
        //System.out.println(this.virtuosoURIs);
        //System.out.println(this.virtuosoProvenance);
        //System.out.println(this.virtuosoTriples);
        //System.out.println(this.pointer);
    }
    
    
    public String getAllVirtuosoTriples(){
        return this.virtuosoURIs+this.virtuosoProvenance+this.virtuosoTriples+this.pointer;
        
    }
    
    public void setVirtuosoURIs(){
       for(String uri:newURIs){
           virtuosoURIs+="<"+uri+">\t<http://www.ics.forth.gr/isl/identifier>\t<"+code+"> .\n";
       }
    }
    
    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getPointer() {
        return pointer;
    }

    public void setPointer() {
        this.pointer = "<"+code+"> \t <http://www.ics.forth.gr/isl/identifierLive>\t\""+pointer+"\" .\n";
    }

    public HashSet<String> getNewURIs() {
        return newURIs;
    }

    public void setNewURIs(HashSet<String> newURIs) {
        this.newURIs = newURIs;
    }

    public String getVirtuosoProvenance() {
        return virtuosoProvenance;
    }

    public void setVirtuosoProvenance() {
        this.virtuosoProvenance = "<"+code+"> \t <http://www.ics.forth.gr/isl/provenance>\t\""+this.provenance+"\" .\n";
    }

    public String getVirtuosoTriples() {
        return virtuosoTriples;
    }

    public void setVirtuosoTriples() {
        this.virtuosoTriples = "<"+code+"> \t <http://www.ics.forth.gr/isl/directCount>\t\""+provenance+"DC";
        for (String dc:directCounts.keySet()){
            this.virtuosoTriples+=dc+"\t"+directCounts.get(dc)+"$";
        }
        this.virtuosoTriples=this.virtuosoTriples.substring(0,this.virtuosoTriples.length()-1)+"\" .\n";
    }
    
    
    
    public HashMap<String, Set<String>> getLodsyndesisIndex() {
        return lodsyndesisIndex;
    }

    public void setLodsyndesisIndex(HashMap<String, Set<String>> lodsyndesisIndex) {
        this.lodsyndesisIndex = lodsyndesisIndex;
    }

    public HashMap<String, Set<String>> getDatasetIndex() {
        return datasetIndex;
    }

    public void setDatasetIndex(HashMap<String, Set<String>> datasetIndex) {
        this.datasetIndex = datasetIndex;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public HashMap<String, Integer> getDirectCounts() {
        return directCounts;
    }

    public void setDirectCounts(HashMap<String, Integer> directCounts) {
        this.directCounts = directCounts;
    }
    
    
    
}
