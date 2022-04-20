
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */

package isl.forth.gr.isl.dataset;

import java.util.HashSet;

/**
 *
 * @author Michalis Mountantonakis
 */
public class Property {
    public String code="";
    public String url="";
    public String provenance;
    boolean LODsyndesisEntity=false;
    public  String virtuosoURIs="";
    public HashSet<String> newURIs=new HashSet<String>();
     public String addToPropFile="";
    public  String virtuosoTriples="",virtuosoTriplesToDelete="";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public boolean isLODsyndesisEntity() {
        return LODsyndesisEntity;
    }

    public void setLODsyndesisEntity(boolean LODsyndesisEntity) {
        this.LODsyndesisEntity = LODsyndesisEntity;
    }

    public String getVirtuosoURIs() {
        return virtuosoURIs;
    }

    public void setVirtuosoURIs(String virtuosoURIs) {
        this.virtuosoURIs = virtuosoURIs;
    }

    public HashSet<String> getNewURIs() {
        return newURIs;
    }

    public void setNewURIs(HashSet<String> newURIs) {
        this.newURIs = newURIs;
    }


    public String getVirtuosoTriples() {
        return virtuosoTriples;
    }

    public void setVirtuosoTriples(String virtuosoTriples) {
        this.virtuosoTriples = virtuosoTriples;
    }
}
