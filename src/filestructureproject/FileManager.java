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
                fileCreator.createFiles(line);
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
    
    public void searchRegisterByPK(String key) {
        long tempoInicial = System.currentTimeMillis();
        File dir = new File(new File("").getAbsolutePath());

        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + fields[0] + ".txt", "r");
            System.out.println("file: " + file.length());

            long start = 0;
            long end = file.length();

            while (start <= end) {
                long mid = start + (end - start) / 2;
                file.seek(mid);
                String line = file.readLine();
                String pk = line.substring(0, 50).trim();
                System.out.println("pk: " + pk);
                if (key.compareTo(pk) < 0) {
                    end = mid - 64;
                    System.out.println("1!");
                } else if (key.equalsIgnoreCase(pk)) {
                    //return 1;
                    // 52 because of the pipe '|'
                    String address = line.substring(52).trim();
                    String returnRegister = getRegister(address);
                    System.out.println(returnRegister);
                    System.out.println("2!");
                    return;
                } else {
                    start = mid + 64;
                    System.out.println("3!");
                    return;
                }

            }

            //return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // execução do método
        System.out.println("o metodo executou em " + (System.currentTimeMillis() - tempoInicial));

    }

    public String getRegister(String address) {
        System.out.println("address: " + address);
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
        File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes");
        
        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + fields[indexPos] + ".txt", "r");
            System.out.println("file size: " + file.length());

            long start = 0;
            long end = file.length();

            while (start <= end) {
                long mid = start + (end - start) / 2;
                //testing
                mid = 64 * ((mid) / 64);
                //
                file.seek(mid);
                System.out.println("mid: " + mid);
                String line = file.readLine();
                System.out.println("line: " + line);
                String pk = line.substring(0, 50).trim();
                System.out.println("pk: " + pk);
                if (value.compareTo(pk) < 0) {
                    end = mid - 64;
                    System.out.println("1!");
                } else if (value.equalsIgnoreCase(pk)) {
                    //return 1;
                    // 52 because of the pipe '|'
                    String rrn = line.substring(52).trim();
                    int pos = searchInInvertedList(indexPos, rrn);
                    //String returnRegister = getRegister(address);
                    //System.out.println(returnRegister);
                    System.out.println("2!");
                    return;
                } else {
                    start = mid + 64;
                    System.out.println("3!");
                }
                
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public int searchInInvertedList(int indexPos, String rrn) {
        File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes");
        
        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + fields[indexPos] + "_IL.txt", "r");
            System.out.println("file: " + file.length());
            int intRrn = Integer.valueOf(rrn);
            int pos = intRrn * 64;
            file.seek(pos);
            System.out.println("file: " + file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return -1;
    }

    public String[] getKeys(File file) {

        return null;
    }

    public void insertRegister(String newRegister) {
        String registerName = "file1";
        newRegister = "1234588833030|Trabalho de estrutura de arquivos|Rodrigo Galvao|Rogerio Hirata |Luiz panicachi|2016#";
        String[] valuesOfRegister = newRegister.split("\\|");
        
        
        //Insert(Append) the new register in the register file - Ok      
        File dir = new File(new File("").getAbsolutePath());
        File fileName = new File(dir, "/" + registerName + ".txt");
        

        //get the last RRN address to write in the primary index file later
        String Address = fileName.length()+"";
        
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(newRegister);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
         

        //Get Keys Names
        BufferedReader reader = null;
        String fileContent ="";
        String[] Keys = null;
        try {
            reader = new BufferedReader(new FileReader(new File(dir, "/" + registerName + ".txt")));
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
            System.out.println("valor-->"+valuesOfRegister[0]);       
            String filledPK =      FileCreator.fillString(valuesOfRegister[0], 50);
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
}
