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
        manager.createOrRead("file1.txt");*/
        
        FileManager manager = new FileManager();
        manager.createOrRead("file1.txt");
        
        int op = -1;
        System.out.println("Options:");
        System.out.println("1 - Show Indexes"); // ?? - I don't know if it is necessary
        System.out.println("2 - Search for a Register"); // Submenu option with the avaiable indexes
        System.out.println("3 - Remove Register");
        Scanner scan = new Scanner(System.in);
        //op = scan.nextInt();
        op=2;
        
        switch (op) {
            case 1: {
                manager.printIndexes(1);
                break;
            }

            case 2: {
                System.out.println("\nSelect an index: ");
                manager.printIndexes(2);
                Scanner in = new Scanner(System.in);
                int op2 = scan.nextInt();
                if (op2 == 0) {
                    // Ex: "3642237673";
                    System.out.print("Search: ");
                    String key = in.next();
                    manager.searchRegisterByPK(key);
                } else {
                    System.out.print("Search: ");
                    String key = in.next();
                    manager.searchRegister(op2, key);
                }
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
