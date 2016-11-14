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
        System.out.println("3 - Insert Register");
        System.out.println("4 - Remove Register");
        Scanner scan = new Scanner(System.in);
        //op = scan.nextInt();
        op=4;
        
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
                    System.out.println("Result: ");
                    manager.searchRegisterByPK(key);
                } else {
                    System.out.print("Search: ");
                    String key = in.next();
                    System.out.println("Result: ");
                    manager.searchRegister(op2, key);
                }
                break;
            }

            case 3: {
                System.out.println("Insert a Register");
                String[] fields = manager.getFields();
                String[] values = new String[fields.length];
                Scanner in = new Scanner(System.in);
                for (int i=0; i<fields.length; i++) {
                    System.out.print(fields[i] + ": ");
                    values[i] = in.nextLine();
                }
                //manager.insertRegister("file1.txt", values);
                break;
            }
            case 4: {
                System.out.print("Register primary key: ");
                Scanner in = new Scanner(System.in);
                String key = in.next();
                manager.removeRegister(key);
            }

            default:
                break;

        }
        
    }
    
}
