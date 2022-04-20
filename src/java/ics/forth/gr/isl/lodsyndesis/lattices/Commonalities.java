
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */
package ics.forth.gr.isl.lodsyndesis.lattices;

import ics.forth.gr.isl.lodchaincode.LODChainController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 * @author Michalis Mountantonakis
 */
public class Commonalities {
    int nodes=0;
    int checks=0;
    static int maxID=0;
    HashMap<Integer,Integer> IDsMapping=new HashMap<Integer,Integer>();
    HashMap<Integer,Integer> reverseIDsMapping=new HashMap<Integer,Integer>();
    HashMap<String,Integer> results=new HashMap<String,Integer>();
    
   
    
    public void runCommonalities(int maxLevel, HashMap<String,Integer> dcounts, int minscore, boolean onlyForFirst, boolean print) throws IOException, CloneNotSupportedException{
        HashSet<String> dcountsMap= this.dcountsMapping(dcounts);
        results.clear();
        this.lattice_based_Intersection_Pruning(2, maxLevel, dcountsMap, null, minscore, "0", "", null,maxID,onlyForFirst,print);
    }
    
    public HashMap<String,Integer> getResults(){
        return results;
    }
    public HashSet<String> dcountsMapping(HashMap<String,Integer> dcounts){
         TreeSet<Integer> tr=new TreeSet<Integer>();
       IDsMapping.clear();
       reverseIDsMapping.clear();
        for(String k:dcounts.keySet()){
            if(k==null)
                continue;
            String[] dtsets=k.split(",");
            if(dtsets.length>50)
                continue;
            for(String dset:dtsets){
                tr.add(Integer.parseInt(dset));
            }
        }
        int count=1;
        for(int dset:tr.descendingSet()){
            ////System.out.println(dset);
            IDsMapping.put(dset, count);
            reverseIDsMapping.put(count,dset);
            count++;
        }
        maxID=count-1;
        HashSet<String> dcountsMapping=new HashSet<String>();
         for(String k:dcounts.keySet()){
             if(k==null)
                continue;
            String[] dtsets=k.split(",");
            if(dtsets.length>50)
                continue;
            String newDcount="";
            for(int j=dtsets.length-1;j>=0;j--){
                newDcount+=IDsMapping.get(Integer.parseInt(dtsets[j]))+",";
            }
            newDcount=newDcount.substring(0,newDcount.length()-1)+"\t"+dcounts.get(k);
            dcountsMapping.add(newDcount);
         }
           // for(String str:dcountsMapping)
            // //System.out.println(str);
            
         return dcountsMapping;
    }
    
    
    public void lattice_based_Intersection_Pruning(int level, int maxLevel,HashSet<String> dcounts, String nod, int minscore, String newNode, String oldNode, Map<ArrayList<String>, Integer> ups,  int maxB, boolean onlyForFirst, boolean print) throws IOException, CloneNotSupportedException {
        int max = maxB;
        if (level == 2) {
            HashMap<Integer, HashMap<ArrayList<String>, Integer>> ups1 = this.setIntersectionDC(dcounts, max);
            for (int j = 1; j <= max; j++) {
                if(onlyForFirst==true && j==2)
                    break;
                String node = Integer.toString(j);
                int score = 0;

                ////System.out.println(node + " " + ups1.get(j));
                for (Iterator<Map.Entry<ArrayList<String>, Integer>> it = ups1.get(j).entrySet().iterator(); it.hasNext();) {

                    Map.Entry<ArrayList<String>, Integer> entry = it.next();
                    checks++;//= entry.getKey().size();
                    ////System.out.println(entry.getKey());
                    if (!entry.getKey().contains(node)) {
                        int cScore = entry.getValue();
                        it.remove();
                    } else {
                        int cScore = entry.getValue();
                        score += cScore;

                    }
                }
                //Map<ArrayList<String>, Integer> upsN = new HashMap(ups1.get(j));

                ////System.out.println(node + " " + score);
                for (int i = j + 1; i <= max; i++) {
                   // TrieInt tr1 = triesInt.get(Integer.toString(i - 1));
                    String newN = node + "," + i;
                    lattice_based_Intersection_Pruning(3, maxLevel,null, newN, minscore, Integer.toString(i), node, new HashMap(ups1.get(j)),  maxB,onlyForFirst, print);
                }
            }
        } else {
            nodes++;

            if (nodes % 10000 == 0) {
                ////System.out.println(nodes);
            }
            int score = 0;// scoreAll;
            boolean gBreak = false;
            Set<ArrayList<String>> remove = new HashSet<>();
            Map<ArrayList<String>, Integer> upsX = new HashMap<>();

            for (Iterator<Map.Entry<ArrayList<String>, Integer>> it = ups.entrySet().iterator(); it.hasNext();) {
                Map.Entry<ArrayList<String>, Integer> entry = it.next();
                checks++;//=entry.getKey().size();//++;
                if (!entry.getKey().contains(newNode)) {
                    int cScore = entry.getValue();
                    it.remove();
                } else {
                    int cScore = entry.getValue();
                    score += cScore;
                    ArrayList<String> array = new ArrayList<>(entry.getKey());
                    Iterator<String> x = array.iterator();
                        ////System.out.println("Prin"+array.size());
                    while (x.hasNext()) {

                        if (Integer.parseInt(x.next()) <= Integer.parseInt(newNode)) {
                            x.remove();
                        }

                    }
                   // //System.out.println("Meta"+array.size());
                    if (upsX.containsKey(array)) {
                        upsX.put(array, upsX.get(array) + entry.getValue());
                    } else {
                        upsX.put(array, entry.getValue());
                    }

                }

            }
            Map<ArrayList<String>, Integer> ups1;

            ups1 = new HashMap(upsX);

            if (print == true && score>=minscore ) {
                String[] split=nod.split(",");
                String convertedNode="";
                for(String k:split){
                    convertedNode+=LODChainController.IDtoDatasetName.get(this.reverseIDsMapping.get(Integer.parseInt(k)))+",";
                    
                }
                convertedNode=convertedNode.substring(0,convertedNode.length()-1);
                results.put(convertedNode,score);
                ////System.out.println(convertedNode + " " + score);
            }
          
            level++;
            
            if(level<=maxLevel+1 && score>=minscore && ups.size()>0){
                
            for (int i = Integer.parseInt(newNode) + 1; i <= max; i++) {
                String node = nod + "," + i;
                if(minscore<5)
                    minscore=5;
                lattice_based_Intersection_Pruning(level, maxLevel, null, node, minscore, Integer.toString(i), nod, new HashMap(ups1), maxB, onlyForFirst, print);

            }
            }
        }
    }
    
    
     public HashMap<Integer, HashMap<ArrayList<String>, Integer>> setIntersectionDC(HashSet<String> dcounts, int max) {
       
            HashMap<Integer, HashMap<ArrayList<String>, Integer>> map = new HashMap<>();

            for (int t = 0; t <= max; t++) {
                map.put(t, new HashMap<ArrayList<String>, Integer>());
            }
            HashMap<ArrayList<String>, Integer> ret = new HashMap<>();
            int sc = 0;
            HashMap<Integer, Integer> sum = new HashMap<>();
            int count = 0;
            for(String s:dcounts){
                ArrayList<String> dtsets = new ArrayList<String>();
                boolean flag = false;
                String[] ids = s.split("\t")[0].split(",");
                for (int p = ids.length - 1; p >= 0; p--) {
                    if (Integer.parseInt(ids[p]) <= max) {
                        dtsets.add(ids[p]);
                        ArrayList<String> toAdd = new ArrayList<String>(dtsets);

                        if (toAdd.size() > 0) {
                            if (map.get(Integer.parseInt(ids[p])).containsKey(toAdd)) {

                                map.get(Integer.parseInt(ids[p])).put(toAdd, map.get(Integer.parseInt(ids[p])).get(toAdd) + Integer.parseInt(s.split("\t")[1]));
                            } else {
                                map.get(Integer.parseInt(ids[p])).put(toAdd, Integer.parseInt(s.split("\t")[1]));
                            }

                        }
                    }

                }
                //if(flag==false)
                //  continue;

                // dtsets.addAll(Arrays.asList(s.split("\t")[0].split(",")));
                // int last = Integer.parseInt(s.split("\t")[0].split(",")[s.split("\t")[0].split(",").length - 1]);
                ////System.out.println(last+" "+i);
                //if (last >= i) {
                //    ret.put(dtsets, Integer.parseInt(s.split("\t")[1]));
                // }
            }
            count += ret.size();
            // //System.out.println(count);
            return map;

        
    }

}
