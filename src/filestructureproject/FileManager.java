/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject;

import filestructureproject.createfile.FileCreator;
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
        if (areFilesAlreadyCreated()) return;
        
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
    
}
