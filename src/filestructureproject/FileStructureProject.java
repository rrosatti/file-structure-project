/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author rodri
 */
public class FileStructureProject {
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        /*OldFileManager manager = new OldFileManager();
        manager.readFile("file1.txt");*/
        
        FileManager manager = new FileManager();
        manager.readFile("file1.txt");
        
        int op = -1;
        System.out.println("Options:");
        System.out.println("1 - Show Indexes"); // ?? - I don't know if it is necessary
        System.out.println("2 - Search for a Register"); // Submenu option with the avaiable indexes
        System.out.println("3 - Remove Register");
        Scanner scan = new Scanner(System.in);
        //op = scan.nextInt();
        op=1;
        
        switch (op) {
            case 1: {
                System.out.println("Show Indexes");
                break;
            }

            case 2: {
                /*List<String> indexes = manager.getIndexesName();
                System.out.println("Choose which field you want to use to search:");
                for (int i=0; i<indexes.size(); i++) {
                    System.out.println("\t2."+ i + " - " + indexes.get(i));
                }*/

                break;
            }

            case 3: {
                System.out.println("Remove a Register");
                break;
            }

            default:
                break;

        }
        
    }
    
}
