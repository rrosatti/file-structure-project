/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject.createfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author rodri
 */
public class FileCreator {
    
    private String filename = "";
    private TreeMap<String, Integer> primaryIndex;
    private PFile file;
    private List<List<SecondaryIndex>> secondaryIndexList; 
    private List<List<SecondaryIndex>> invertedList;
    
    public FileCreator() {
        primaryIndex = new TreeMap<>();
        secondaryIndexList = new ArrayList<>();
        invertedList = new ArrayList<>();
    }
    
    public void createFiles(String filename, String fileContent) {
        this.filename = filename;
        
        // remove all the accented characters
        fileContent = replaceAccentedCharacters(fileContent);
        
        
        if (fileContent != null) {
            String[] registers = fileContent.split("#");
            
            int count = 0;            
            
            //file = new PFile(registers.length-1);
            for (int i=0; i<registers.length; i++) {
                //System.out.print(registers[i] + "\n");
                String[] fields = registers[i].split("\\|");

                if (i == 0) {
                    file = new PFile(fields.length);
                    for (int j=0; j<fields.length; j++) {
                        file.addField(fields[j]);
                    }
                } else {
                    Register res = new Register(fields.length);
                    res.setValues(fields);
                    res.setAddress(count);
                    file.addRegister(res);
                    primaryIndex.put(fields[0].trim(), count);
                }
                count += registers[i].length() + 1;

            }

        }
        
        createPrimaryIndexFile();
        createSecondaryIndexAndInvertedList();
        
    }
    
    /**
     * Replace accented characters and replace original file with the changed content
     * 
     * @param content
     * @return 
     */
    public String replaceAccentedCharacters(String content) {
        content = Normalizer.normalize(content, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        File f = new File(new File("").getAbsolutePath() + "/" + filename);
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(content);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return content;
        
    }
    
    private void createPrimaryIndexFile() {
        
        //Creating the primaryIndex file in the folder PrimaryIndex
        String primaryIndexFilename = file.getField(0) + ".txt";
        File file = new File(new File("").getAbsolutePath()+ "/" +primaryIndexFilename);
        
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (Map.Entry<String, Integer> entry: primaryIndex.entrySet()) {
                //String data = String.valueOf(entry.getKey()) + "|" + String.valueOf(entry.getValue());
                String pk = entry.getKey();
                String address = "|" + String.valueOf(entry.getValue());
                String data = fillString(pk, 50);
                String data2 = fillString(address, 10);
                data += data2;    
                
                bw.write(data);
                bw.newLine();
                //System.out.println(data);
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
    
    /**
     * 
     * 1 STEP: Loop through the fields
     * 2 STEP: 
     *          - Create a List of Secondary Index
     *          - Loop through the Registers
     * 3 STEP: 
     *          - Check if the list already has the register
     *              - If not, then add it to the list
     *              - If so, then add it to the inverted list
     * 
     * 
     */
    public void createSecondaryIndexAndInvertedList() {
    String[] fields = file.getFields();
        
        int j = 0;
        // STEP 1
        for (int i=1; i<fields.length; i++) {
            //System.out.println("field: " + fields[i]);
            
            //Creating the secondary Indexes files in the folder SecondaryIndex
            
            
            int rrnCounter = 0;
            List<SecondaryIndex> secondaryIndex = new ArrayList<>();
            List<SecondaryIndex> invertedListValues = new ArrayList<>();
            for (Register res: file.getRegisters()) {
                SecondaryIndex si = new SecondaryIndex(res.getValue(i), rrnCounter++);
                //System.out.println("value: " + res.getValue(i));
                
                // STEP 3
                if (secondaryIndex.size() > 0) {
                    if (!secondaryIndex.contains(si)) {
                        secondaryIndex.add(si);
                        // SecondaryIndex(Primary Key, Next RRN)
                        invertedListValues.add(new SecondaryIndex(res.getValue(0), -1));
                    } else {
                        // Insert the repeated element in the inverted list
                        int pos = secondaryIndex.indexOf(si); // get position of the element in secondary index
                        SecondaryIndex aux = secondaryIndex.get(pos); // get its object
                        int rrn = aux.getRrn(); // get its rrn
                        // SecondaryIndex(Primary Key, RRN of )
                        invertedListValues.add(new SecondaryIndex(res.getValue(0), rrn));
                        aux.setRrn(rrnCounter-1); // counter - 1;
                        //secondaryIndex.get(pos).addToInvertedList(Long.valueOf(res.getValue(0).trim()));
                    }
                } else {
                    // It will enter here only in the first time
                    secondaryIndex.add(si);
                    invertedListValues.add(new SecondaryIndex(res.getValue(0), -1));
                }
            }
            secondaryIndexList.add(secondaryIndex);
            invertedList.add(invertedListValues);
            int aux = j;
            //j++;
            Collections.sort(secondaryIndexList.get(j++));
            /**
            System.out.println("Secondary Index");
            for (int z=0; z<secondaryIndexList.get(aux).size(); z++) {
                System.out.println(fields[i] + " - " + secondaryIndexList.get(aux).get(z).getValue()
                + " rrn: " + secondaryIndexList.get(aux).get(z).getRrn());
            }
            System.out.println("");
            System.out.println("Inverted List");
            for (int w=0; w<invertedList.get(aux).size(); w++) {
                System.out.println(fields[i] + " - " + invertedList.get(aux).get(w).getValue()
                + " rrn: " + invertedList.get(aux).get(w).getRrn());
            }
            System.out.println("");*/
            
        }
        createFiles();
        
    }
    
    public void createFiles() {
        String[] fields = file.getFields();
        
         for (int i=1; i<fields.length; i++) {
            String secondaryIndexFilename = fields[i]+ ".txt";
            String invertedListFilename = fields[i]+ "_IL.txt";
            File f = new File(new File("").getAbsolutePath()+"\\SecondaryIndexes\\" + secondaryIndexFilename);
            File f2 = new File(new File("").getAbsolutePath()+"\\SecondaryIndexes\\" + invertedListFilename);
            f.getParentFile().mkdir();
            
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    f2.createNewFile();
                    
                    FileWriter fw = new FileWriter(f.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    
                    // get(i-1) because the secondary list does not include the primary field
                    for (int j=0; j<secondaryIndexList.get(i-1).size(); j++) {
                        
                        //build the data with fixed number
                        String data = secondaryIndexList.get(i-1).get(j).getValue();
                               data = fillString(data,50);
                        String data2="|" + secondaryIndexList.get(i-1).get(j).getRrn();
                               data2 = fillString(data2,10);
                        
                               
                        data+=data2;
                        bw.write(data);
                        bw.newLine();
                    }
                    bw.close();
                    
                    FileWriter fw2 = new FileWriter(f2.getAbsoluteFile());
                    BufferedWriter bw2 = new BufferedWriter(fw2);
                    
                    for (int j=0; j<invertedList.get(i-1).size(); j++) {
                        String data = invertedList.get(i-1).get(j).getValue();
                               data = fillString(data,50);
                        String data2="|" + invertedList.get(i-1).get(j).getRrn();
                               data2 = fillString(data2,10);
                        
                        
                        data+=data2;       
                        //System.out.println("data: " + data);
                        bw2.write(data);
                        bw2.newLine();
                    }
                    bw2.close();
                    
                } catch (IOException ex) {}
            }
            
         }
    }
    
    public static String fillString(String str, int leng) {
        if(str.length() <= leng)
            for (int i = str.length(); i <= leng; i++)
                str += " ";
        else
           str = str.substring(0,leng+1);
        
        return str;
    }
    
}
