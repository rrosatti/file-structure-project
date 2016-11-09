/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author rodri
 */
public class OldFileManager {
    
    private int count = 0;
    private List<String> indexesName = new ArrayList<>();
    private String primaryIndexFilename;
    private String[] secondaryIndexFilenames;
    private String[] invertedListFilenames;
    private TreeMap<Long, Integer> primaryIndex = new TreeMap<>();
    private String[] registers;
    
    public void readFile(String name) {
        if (isIndexFilesAlreadyCreated()) return;
        
        File dir = new File(new File("").getAbsolutePath());
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(new File(dir, name)));
            String line = reader.readLine();
            //System.out.println(line);
            
            if (line != null) {
                registers = line.split("#");
                
                int count = 0;
                for (int i=0; i<registers.length; i++) {
                    //System.out.print(register[i]);
                    String[] fields = registers[i].split("\\|");
                    
                    if (i == 0) {
                        for (int j=0; j<fields.length; j++) {
                            indexesName.add(fields[j]);
                        }
                    } else {
                        primaryIndex.put(Long.valueOf(fields[0].trim()), count);
                    }
                    count += registers[i].length() + 1;
                    
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        createPrimaryIndexFile();
        //createInvertedListFiles();
        //createSecondaryIndexFiles();
        createInvertedListAndSecondaryIndexFiles();
    }
    
    
    /**
     * 
     * Think about a way to use those methods to "REALLY" create those files while reading the file (readFile())
     * 
     */
    private void createPrimaryIndexFile() {
        primaryIndexFilename = indexesName.get(0) + ".txt";
        File file = new File(new File("").getAbsolutePath() + "/" + primaryIndexFilename);
        
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (Map.Entry<Long, Integer> entry: primaryIndex.entrySet()) {
                String data = String.valueOf(entry.getKey()) + "|" + String.valueOf(entry.getValue());
                bw.write(data);
                bw.newLine();
                System.out.println(data);
            }
            bw.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void createInvertedListAndSecondaryIndexFiles() {
        // Get filenames
        secondaryIndexFilenames = new String[indexesName.size() - 1];
        invertedListFilenames = new String[indexesName.size() - 1];
        for (int i=1; i<indexesName.size(); i++) {
            secondaryIndexFilenames[i-1] = indexesName.get(i) + ".txt";
            invertedListFilenames[i-1] = indexesName.get(i) + "_IL.txt";
            //System.out.println("Secondary Index File: " + secondaryIndexFilesName[i-1]);
            TreeMap<String, Integer> secondaryIndexFile = new TreeMap<>();
            TreeMap<Long, ArrayList<Integer>> invertedListFile = new TreeMap<>();
            for (int j=0; j<registers.length - 1; j++) {
                String[] fields = registers[j+1].split("\\|");
                if (!secondaryIndexFile.containsKey(fields[i])) {
                    long key = Long.valueOf(fields[0]);
                    secondaryIndexFile.put(fields[i], primaryIndex.get(key));
                    ArrayList<Integer> aux = new ArrayList<>();
                    aux.add(invertedListFile.size()+1);
                    invertedListFile.put(key, aux);
                } else {
                    
                }
            }
        }
        
        List<TreeMap<String, Integer>> list = new ArrayList<>();
        for (int i=0; i<registers.length; i++) {
            
        }
    }
        
    
    private void createSecondaryIndexFiles() {
        secondaryIndexFilenames = new String[indexesName.size() - 1];
        for (int i=1; i<indexesName.size(); i++) {
            secondaryIndexFilenames[i-1] = indexesName.get(i) + ".txt";
            //System.out.println("Secondary Index File: " + secondaryIndexFilesName[i-1]);
        }
    }
    
    private void createInvertedListFiles() {
        invertedListFilenames = new String[indexesName.size() - 1];
        for (int i=1; i<indexesName.size(); i++) {
            invertedListFilenames[i-1] = indexesName.get(i) + "_IL.txt";
            System.out.println("Inverted List File: " + invertedListFilenames[i-1]);
        }
    }
    
    private boolean isIndexFilesAlreadyCreated() {
        return false;
    }
    
    public void showIndexes() {
        System.out.print("|| ");
        for (String field:indexesName) {
            System.out.print(field + " || ");
        }
        System.out.println();
    }
    
    public List<String> getIndexesName() {
        return indexesName;
    }
}
