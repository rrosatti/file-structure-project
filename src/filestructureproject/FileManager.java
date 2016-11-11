/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject;

import filestructureproject.createfile.FileCreator;
import filestructureproject.createfile.PFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author rodri
 */
public class FileManager {
    
    FileCreator fileCreator;
    
    public FileManager() {
        fileCreator = new FileCreator();
    }
    
    public void readFile(String name) {
        if (areFilesAlreadyCreated()) {
            return;
        } else {
            searchRegisterByPK(name, "470723378");
        }
        
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
    
    public boolean areFilesAlreadyCreated() {
        return false;
    }
    
    public void searchRegisterByPK(String indexName, String key) {
        indexName = "ISBN";
        File dir = new File(new File("").getAbsolutePath());
        BufferedReader reader = null;
        System.out.println("dir: " + dir);
        
        try {
            reader = new BufferedReader(new FileReader(new File(dir, indexName + ".txt")));
            String line = reader.readLine();
            System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
    
}
