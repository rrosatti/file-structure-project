/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject;

import filestructureproject.createfile.FileCreator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.text.Normalizer;


/**
 *
 * @author rodri
 */
public class FileManager {
    
    private FileCreator fileCreator;
    private String filename = "";
    private String fields[];
    
    public FileManager() {
        fileCreator = new FileCreator();
    }
    
    public void createOrRead(String name) {
        filename = name;
               
        File dir = new File(new File("").getAbsolutePath());
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(new File(dir, name)));
            String line = reader.readLine();

            if (areFilesAlreadyCreated()) {
                //searchRegisterByPK(name, "470723378");
                getIndexes(line);
            } else { 
                fileCreator.createFiles(filename, line);
                getIndexes(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

                
    }

    public boolean areFilesAlreadyCreated() {
        return false;
    }
    
    public void getIndexes(String content) {
        
        if (content != null) {
            String[] registers = content.split("#");
            // it will take only the values referring to the indexes
            fields = registers[0].split("\\|");
        }
        
    }
    
    public void printIndexes(int op) {
        if (op == 1) {
            System.out.print("| ");
            for (int i=0; i<fields.length; i++) {
                System.out.print(fields[i] + " | ");
            }
            System.out.println("");
        } else if (op == 2) {
            for (int i=0; i<fields.length; i++) {
                System.out.println(i + ": " + fields[i]);
            }
        }
        
    }
    
    public String searchRegisterByPK(String key) {
        long startTime = System.currentTimeMillis();
        File dir = new File(new File("").getAbsolutePath());

        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + fields[0] + ".txt", "r");
            //System.out.println("file size: " + file.length());

            long start = 0;
            long end = file.length();
            
            while (start <= end) {
                long mid = start + (end - start) / 2;
                
                mid = 64 * ((mid) / 64);
                if (mid == file.length()) 
                    return "";
                file.seek(mid);
                
                String line = file.readLine();
                String pk = line.substring(0, 50).trim();
                if (key.compareTo(pk) < 0) {
                    end = mid - 64;
                    //System.out.println("1!");
                } else if (key.equalsIgnoreCase(pk)) {
                    // 52 because of the pipe '|'
                    String address = line.substring(52).trim();
                    String returnRegister = getRegister(address);
                    System.out.println(returnRegister);
                    //System.out.println("2!");
                    start = end+1; // just to finish the while loop
                    return address;
                } else {
                    start = mid + 64;
                    //System.out.println("3!");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // execução do método
        System.out.println("PrimaryIndex: The method took " + (System.currentTimeMillis() - startTime) + " seconds.");
        return "";

    }

    public String getRegister(String address) {
        //System.out.println("address: " + address);
        File dir = new File(new File("").getAbsolutePath());
        String register = "";
        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + filename, "r");
            long pos = Long.valueOf(address);
            file.seek(pos);
            //register = file.readLine();
            int r;
            while ((r = file.read()) != -1 && (char)r != '#') {
                char ch = (char) r;
                register += ch; 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return register;
    }

    public void searchRegister(int indexPos, String value) {
        long startTime = System.currentTimeMillis();
        File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes");
        
        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + fields[indexPos] + ".txt", "r");
            //System.out.println("file size: " + file.length());

            long start = 0;
            long end = file.length();

            while (start <= end) {
                long mid = start + (end - start) / 2;
                //testing
                mid = 64 * ((mid) / 64);
                if (mid == file.length()) {
                    System.out.println("Testing");
                    break;
                }
                file.seek(mid);
                String line = file.readLine();
                String pk = line.substring(0, 50).trim();
                
                if (value.compareTo(pk) < 0) {
                    end = mid - 64;
                    //System.out.println("1!");
                } else if (value.equalsIgnoreCase(pk)) {
                    // 52 because of the pipe '|'
                    String rrn = line.substring(52).trim();
                    searchInInvertedList(indexPos, rrn);
                    //System.out.println("2!");
                    //return;
                    start = end+1; // just to finish the while loop
                } else {
                    start = mid + 64;
                    //System.out.println("3!");
                }
                
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("SecondaryIndex: The method took " + (System.currentTimeMillis() - startTime) + " seconds.");

    }
    
    public void searchInInvertedList(int indexPos, String rrn) {
        long startTime = System.currentTimeMillis();
        File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes");
        
        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + fields[indexPos] + "_IL.txt", "r");
            
            int intRrn = Integer.valueOf(rrn);
            int pos = intRrn * 64;
            file.seek(pos);
            String line = file.readLine();
            String key = line.substring(0, 50).trim();
            String next = line.substring(52).trim();
            
            searchRegisterByPK(key);
            if (!next.equals("-1")) {
                //System.out.println("key: " + key + " next: " + next);
                searchInInvertedList(indexPos, next);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("InvertedList: The method took " + (System.currentTimeMillis() - startTime) + " seconds.");
    }

    public String[] getFields() {
        return fields;
    }

    public void insertRegister(String filename, String[] newRegister) {
        //newRegister = "1234588833030|Trabalho de estrutura de arquivos|Rodrigo Galvao|Rogerio Hirata |Luiz panicachi|2016#";
        newRegister = new String[]{"1234588833030", "Trabalho de estrutura de arquivos", "Rodrigo Galvao", "Rogerio Hirata",
            "Luiz panicachi", "2016"};
        //String[] valuesOfRegister = newRegister.split("\\|");
        
        // add '|' and '#' to register;
        String register = "";
        for (int i=0; i<newRegister.length; i++) {
            register += newRegister[i];
            if (i == newRegister.length - 1) {
                register += "#";
            } else {
                register += "|";
            }
        }
        
        register = replaceAccentedCharacters(register);
        
        //Insert(Append) the new register in the register file - Ok      
        File dir = new File(new File("").getAbsolutePath());
        //File fileName = new File(dir, "/" + filename);
        
        //get the last RRN address to write in the primary index file later
        String Address = filename.length()+"";
        
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename, true));
            writer.append(register);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
         

        //Get Keys Names
        BufferedReader reader = null;
        String fileContent ="";
        String[] Keys = null;
        try {
            reader = new BufferedReader(new FileReader(new File(dir, "/" + filename)));
            int r;
            while ((r = reader.read()) != -1 && (char)r != '#') {
                char ch = (char) r;
                fileContent += ch; 
                Keys = fileContent.split("\\|");
            }  
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        //For each key, i gonna insert in the respective archive
        
        //insert the new register in the primary index
        RandomAccessFile file;
        try {
            //open file
            file = new RandomAccessFile(dir + "/" + Keys[0] + ".txt", "rw");
            
            //Insert in the end of the file
            
            
            //Insert in the seek position
            file.seek(file.length());
            System.out.println("valor-->"+newRegister[0]);       
            String filledPK =      FileCreator.fillString(newRegister[0], 50);
            String filledAddress = FileCreator.fillString(Address, 10);
            file.writeBytes(filledPK+"|"+filledAddress);
            System.out.println(filledPK+"|"+filledAddress);
            file.close();

            int totalRRN = 3;
            //Locate the right position in the file
            
            //bubble sort all the file
        
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        //insert the new register in the Secondary indexes... i start with 1 because i need to jump the primary key
        for(int i=1;i<Keys.length;i++){
            try {
                RandomAccessFile file2 = new RandomAccessFile(dir + "/SecondaryIndexes/" + Keys[i] + ".txt", "r");
                System.out.println(file2.length());
            
            
            
            
            
            
            
            
            
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }
        
    }
    
    public String replaceAccentedCharacters(String content) {     
        content = Normalizer.normalize(content, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return content;
    }
    
    public void removeRegister(String key) {
        System.out.println("Register to be removed: ");
        String address = searchRegisterByPK(key);
        
        if (address.isEmpty()) {
            System.out.println("Register not found!");
        } else {
            String register = getRegister(address);
            String removedRegister = "%" + register.substring(1);
            System.out.println("removedRegister: " + removedRegister);

            File dir = new File(new File("").getAbsolutePath());
            // replace the old register by its "removed version" in file1.txt
            try {
                RandomAccessFile f = new RandomAccessFile(dir + "/" + filename, "rw");
                //f.seek(Long.valueOf(address));
                //f.writeBytes(removedRegister);
                f.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Remove from PrimaryIndex
            //removeFromPrimaryIndexFile(key);
            
            // Remove from SecondaryIndex and InvertedList
            removeFromSecondaryIndexFile(register);
        }
        
        
        
    }
    
    public void removeFromPrimaryIndexFile(String key) {
        File dir = new File(new File("").getAbsolutePath() + "/" + fields[0] + ".txt");
        
        try {
            RandomAccessFile file = new RandomAccessFile(dir, "rw");
            //System.out.println("file size: " + file.length());
            
            // Save the content of the file in completeFile
            
            StringBuffer completeFile = new StringBuffer();
            completeFile = copyAllData(dir);
            
            long start = 0;
            long end = file.length();
            
            while (start <= end) {
                long mid = start + (end - start) / 2;
                
                mid = 64 * ((mid) / 64);
                if (mid == file.length()) 
                    break;
                file.seek(mid);
                
                String line = file.readLine();
                String pk = line.substring(0, 50).trim();
                if (key.compareTo(pk) < 0) {
                    end = mid - 64;
                } else if (key.equalsIgnoreCase(pk)) {
                    // remove the found register of the StringBuffer (completeFile)
                    completeFile.delete((int)mid, (int)mid+64);
                    // re-write the file without the removed register
                    rewriteData(completeFile, dir);
                    start = end+1; // just to finish the while loop
                } else {
                    start = mid + 64;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void removeFromSecondaryIndexFile(String register) {
        System.out.println(register);
        String[] values = register.split("\\|");
        for (int i=1; i<values.length; i++) {
            File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes" + "/" + fields[i] + ".txt");
            values[i] = FileCreator.fillString(values[i], 50).trim();   
            System.out.println("value: " + values[i]);
            try {
                RandomAccessFile file = new RandomAccessFile(dir, "r");
                //System.out.println("file size: " + file.length());

                long start = 0;
                long end = file.length();

                while (start <= end) {
                    long mid = start + (end - start) / 2;
                    //testing
                    mid = 64 * ((mid) / 64);
                    if (mid == file.length()) {
                        break;
                    }
                    file.seek(mid);
                    String line = file.readLine();
                    String pk = line.substring(0, 50).trim();
                    System.out.println("pk: " + pk);
                    if (values[i].compareTo(pk) < 0) {
                        end = mid - 64;
                    } else if (values[i].equalsIgnoreCase(pk)) {
                        // 52 because of the pipe '|'
                        String rrn = line.substring(52).trim();
                        System.out.println("I've been here!");
                        int pos = removeFromInvertedList(i, rrn, values[i]);
                        if (pos == -1) {
                            // Remove the register here
                            StringBuffer buffer = new StringBuffer();
                            buffer = copyAllData(dir);
                            buffer.delete((int)mid, (int)mid+64);
                            rewriteData(buffer, dir);
                            
                        }
                        start = end+1; // just to finish the while loop
                    } else {
                        start = mid + 64;
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
    public int removeFromInvertedList(int indexPos, String rrn, String key) {
        File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes" +  "/" + fields[indexPos] + "_IL.txt");
        
        try {
            RandomAccessFile file = new RandomAccessFile(dir, "r");
            
            int intRrn = Integer.valueOf(rrn);
            int pos = intRrn * 64;
            file.seek(pos);
            String line = file.readLine();
            String value = line.substring(0, 50).trim();
            String next = line.substring(52).trim();
            
            if (!next.equals("-1")) {
                //Remove here
                // need to check if the key 
                return 0;
            } else {
                // remove here and return -1
                StringBuffer buffer = new StringBuffer();
                buffer = copyAllData(dir);
                buffer.delete(pos, pos+64);
                rewriteData(buffer, dir);
                return -1;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return -1;

    }
    
    public StringBuffer copyAllData(File dir) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(dir));
            String l = br.readLine();
            while (l != null) {
                buffer.append(l+"\r\n");
                l = br.readLine();
            }
            br.close();    
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return buffer;
    }
    
    public void rewriteData(StringBuffer buffer, File dir) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(dir));
            bw.write(buffer.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
