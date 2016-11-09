/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject.createfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author rodri
 */
public class FileCreator {
    
    private TreeMap<Long, Integer> primaryIndex;
    private PFile file;
    private List<List<SecondaryIndex>> secondaryIndexList; // String(Field) / Integer(RRN)
    //private List<TreeMap<Long, Integer>> invertedList; // Long(PrimaryKey) / Integer("Next")
    
    public FileCreator() {
        primaryIndex = new TreeMap<>();
        secondaryIndexList = new ArrayList<>();
        //invertedList = new ArrayList<>();
    }
    
    public void createFile(String fileContent) {
        
        if (fileContent != null) {
            String[] registers = fileContent.split("#");
            
            int count = 0;
            
            file = new PFile(registers.length-1);
            for (int i=0; i<registers.length; i++) {
                //System.out.print(register[i]);
                String[] fields = registers[i].split("\\|");

                if (i == 0) {
                    for (int j=0; j<fields.length; j++) {
                        file.addField(fields[j]);
                    }
                } else {
                    Register res = new Register(fields.length);
                    res.setValues(fields);
                    res.setAddress(count);
                    file.addRegister(res);
                    primaryIndex.put(Long.valueOf(fields[0].trim()), count);
                }
                count += registers[i].length() + 1;

            }

        }
        
        createPrimaryIndexFile();
        createSecondaryIndexAndInvertedList();
        
    }
    
    private void createPrimaryIndexFile() {
        String primaryIndexFilename = file.getField(0) + ".txt";
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
    
    /**
    public void createSecondaryIndexAndInvertedList() {
        String[] fields = file.getFields();
        for (Register res: file.getRegisters()) {
            int j = 0;
            List<SecondaryIndex> secondaryIndex = new ArrayList<>();
            for (int i=1; i<fields.length; i++) {
                System.out.println("field: " + fields[i]);
                SecondaryIndex si = new SecondaryIndex(res.getValue(i), 0);
                System.out.println("i: " + i);
                if (secondaryIndex.size() > 0) {
                    if (!secondaryIndex.contains(si)) {
                        secondaryIndex.add(si);
                    } else {
                        int pos = secondaryIndex.indexOf(si);
                        secondaryIndex.get(pos).addToInvertedList(Integer.valueOf(res.getValue(0).trim()));
                    }
                } else {
                    secondaryIndex.add(si);
                }
            }
            secondaryIndexList.add(secondaryIndex);
            int aux = j;
            System.out.println("");
            for (int i=0; i<secondaryIndexList.get(aux).size(); i++) {
                System.out.println("IDK:" + secondaryIndexList.get(aux).get(i));
            }
            
            Collections.sort(secondaryIndexList.get(j++));
        }*/
    
    public void createSecondaryIndexAndInvertedList() {
        String[] fields = file.getFields();
        
        int j = 0;
        for (int i=1; i<fields.length; i++) {
            System.out.println("field: " + fields[i]);
            
            List<SecondaryIndex> secondaryIndex = new ArrayList<>();
            for (Register res: file.getRegisters()) {
                SecondaryIndex si = new SecondaryIndex(res.getValue(i), 0);
                //System.out.println("value: " + res.getValue(i));
                if (secondaryIndex.size() > 0) {
                    if (!secondaryIndex.contains(si)) {
                        secondaryIndex.add(si);
                    } else {
                        int pos = secondaryIndex.indexOf(si);
                        secondaryIndex.get(pos).addToInvertedList(Long.valueOf(res.getValue(0).trim()));
                    }
                } else {
                    secondaryIndex.add(si);
                }
            }
            secondaryIndexList.add(secondaryIndex);
            int aux = j;
            //j++;
            Collections.sort(secondaryIndexList.get(j++));
            System.out.println("");
            for (int z=0; z<secondaryIndexList.get(aux).size(); z++) {
                System.out.println("IDK: " + secondaryIndexList.get(aux).get(z).getValue());
            }
            
        }
        
    }
    
    
    
}
