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
    
    private FileManager manager = new FileManager();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        FileStructureProject main = new FileStructureProject();
        main.manager = new FileManager();
        main.manager.createOrRead("file1.txt");
        
        int op = -1;
        System.out.println("Options:");
        System.out.println("1 - Show Indexes"); 
        System.out.println("2 - Search for a Register"); // Submenu option with the avaiable indexes
        System.out.println("3 - Insert Register");
        System.out.println("4 - Remove Register");
        System.out.println("0 - Exit");
        Scanner scan = new Scanner(System.in);
        op = scan.nextInt();
        //op=4;
        
        switch (op) {
            case 0: 
                break;
            case 1: {
                main.manager.printIndexes(1);
                break;
            }

            case 2: {
                System.out.println("\nSelect an index: ");
                main.manager.printIndexes(2);
                Scanner in = new Scanner(System.in);
                int op2 = scan.nextInt();
                if (op2 == 0) {
                    // Ex: "3642237673";
                    System.out.print("Search: ");
                    String key = in.next();
                    System.out.println("Result: ");
                    long startTime = System.currentTimeMillis();
                    main.manager.searchRegisterByPK(key);
                    System.out.println("Searching for Primary Index took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                } else {
                    System.out.print("Search: ");
                    String key = in.next();
                    System.out.println("Result: ");
                    long startTime = System.currentTimeMillis();
                    main.manager.searchRegister(op2, key);
                    System.out.println("Searching for Secondary Index took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                }
                break;
            }

            case 3: {
                System.out.println("Insert a Register");
                String[] fields = main.manager.getFields();
                String[] values = new String[fields.length];
                Scanner in = new Scanner(System.in);
                for (int i=0; i<fields.length; i++) {
                    System.out.print(fields[i] + ": ");
                    values[i] = in.nextLine();
                }
                main.manager.insertRegister("file1.txt", values);
                break;
            }
            case 4: {
                System.out.print("Register primary key: ");
                Scanner in = new Scanner(System.in);
                String key = in.next();
                main.manager.removeRegister(key);
                break;
            }

            default:
                break;

        }
        
    }
    
}
