
/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2022 Semantic Access and Retrieval group, All rights reserved
 */
package ics.forth.gr.isl.lodchaincode;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RandomAccessFileTest {
    
   public static void main(String[] ar) throws IOException{
      // RandomAccessFileTest r=new RandomAccessFileTest();
     //  r.readCharsFromFile(LODChainController.folder+"95",26246, 100);
   } 
   
    public String appendData(String filePath, String data) throws IOException {
        RandomAccessFile raFile = new RandomAccessFile(filePath, "rw");
        raFile.seek(raFile.length());
        long i = raFile.getFilePointer();
        
        String s = String.valueOf(i);
       // System.out.println(s);
       // System.out.println("current pointer = " + raFile.getFilePointer());
        raFile.write(data.getBytes());
        raFile.close();
        return s;
    }

    public void writeData(String filePath, String data, int seek) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        file.seek(seek);
        file.write(data.getBytes());
        file.close();
    }

    public byte[] readCharsFromFile(String filePath, int seek, int chars) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(seek);
        byte[] bytes = new byte[chars];
        file.read(bytes);
        String s = new String(bytes);

        System.out.println(s);
        file.close();
        return bytes;
    }

}
