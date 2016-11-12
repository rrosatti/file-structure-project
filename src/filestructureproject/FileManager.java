/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject;

import filestructureproject.createfile.FileCreator;
import filestructureproject.createfile.PFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodri
 */
public class FileManager {

    FileCreator fileCreator;
    String filename = "";

    public FileManager() {
        fileCreator = new FileCreator();
    }

    public void readFile(String name) {
        filename = name;
        if (areFilesAlreadyCreated()) {
            searchRegisterByPK(name, "470723378");
        } else {
            //searchRegisterByPK(name, "470723378");

            File dir = new File(new File("").getAbsolutePath());
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(new File(dir, name)));
                String line = reader.readLine();
                fileCreator.createFile(line);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public boolean areFilesAlreadyCreated() {
        return true;
    }

    public void searchRegisterByPK(String indexName, String key) {
        indexName = "﻿ISBN";
        key = "3642237673";
        long tempoInicial = System.currentTimeMillis();
        File dir = new File(new File("").getAbsolutePath());

        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + indexName + ".txt", "r");
            System.out.println("file: " + file.length());

            long start = 0;
            long end = file.length();

            while (start <= end) {
                long mid = start + (end - start) / 2;
                file.seek(mid);
                String pk = file.readLine().substring(0, 50).trim();
                System.out.println("pk: " + pk);
                if (key.compareTo(pk) < 0) {
                    end = mid - 64;
                    System.out.println("1!");
                } else if (key.equalsIgnoreCase(pk)) {
                    //return 1;
                    // 52 because of the pipe '|'
                    String address = file.readLine().substring(52).trim();
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
            register = file.readLine();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return register;
    }

    public void searchRegister(String indexName, String registerKey) {
        indexName = "Ano";
        File dir = new File(new File("").getAbsolutePath());
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(new File(dir, "/SecondaryIndex/" + indexName + ".txt")));
            String line = reader.readLine();
            fileCreator.createFile(line);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String[] getKeys(File file) {

        return null;
    }

    public void insertRegister(String newRegister) {
        String registerName = "file1";
        newRegister = "9788588833030|Trabalho de estrutura de arquivos|Rodrigo Galvao|Rogerio Hirata |Luiz panicachi|2016#";

        //Insert(Append) the new register in the register file - Ok
        File dir = new File(new File("").getAbsolutePath());
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(dir, "/" + registerName + ".txt"), true));
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
            file = new RandomAccessFile(dir + "/" + Keys[0] + ".txt", "r");
            System.out.println(file.length());
       
        
        
        
        
        
        
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
