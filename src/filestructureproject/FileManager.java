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

    private boolean areFilesAlreadyCreated() {
        File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes");
        if (dir.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void getIndexes(String content) {

        if (content != null) {
            String[] registers = content.split("#");
            // it will take only the values referring to the indexes
            fields = registers[0].split("\\|");
        }

    }

    public void printIndexes(int op) {
        if (op == 1) {
            System.out.print("| ");
            for (int i = 0; i < fields.length; i++) {
                System.out.print(fields[i] + " | ");
            }
            System.out.println("");
        } else if (op == 2) {
            for (int i = 0; i < fields.length; i++) {
                System.out.println(i + ": " + fields[i]);
            }
        }

    }

    public String searchRegisterByPK(String key) {
        File dir = new File(new File("").getAbsolutePath());

        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + fields[0] + ".txt", "r");

            long start = 0;
            long end = file.length();

            while (start <= end) {
                long mid = start + (end - start) / 2;

                mid = 64 * ((mid) / 64);
                if (mid == file.length()) {
                    return "";
                }
                file.seek(mid);

                String line = file.readLine();
                String pk = line.substring(0, 50).trim();
                if (key.compareTo(pk) < 0) {
                    end = mid - 64;
                } else if (key.equalsIgnoreCase(pk)) {
                    // 52 because of the pipe '|'
                    String address = line.substring(52).trim();
                    String returnRegister = getRegister(address);
                    System.out.println(returnRegister);
                    start = end+1; // just to finish the while loop
                    return address;
                } else {
                    start = mid + 64;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    private String getRegister(String address) {
        File dir = new File(new File("").getAbsolutePath());
        String register = "";
        try {
            RandomAccessFile file = new RandomAccessFile(dir + "/" + filename, "r");
            long pos = Long.valueOf(address);
            file.seek(pos);
            int r;
            while ((r = file.read()) != -1 && (char) r != '#') {
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

            long start = 0;
            long end = file.length();

            while (start <= end) {
                long mid = start + (end - start) / 2;

                mid = 64 * ((mid) / 64);
                if (mid == file.length()) {
                    break;
                }

                file.seek(mid);
                String line = file.readLine();
                String pk = line.substring(0, 50).trim();

                if (value.compareTo(pk) < 0) {
                    end = mid - 64;
                } else if (value.equalsIgnoreCase(pk)) {
                    // 52 because of the pipe '|'
                    String rrn = line.substring(52).trim();
                    searchInInvertedList(indexPos, rrn);
                    start = end + 1; // just to finish the while loop
                } else {
                    start = mid + 64;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void searchInInvertedList(int indexPos, String rrn) {
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
                searchInInvertedList(indexPos, next);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public String[] getFields() {
        return fields;
    }

    public void insertRegister(String filename, String[] newRegister) {

        // add '|' and '#' to register;
        String register = "";
        for (int i = 0; i < newRegister.length; i++) {
            register += newRegister[i];
            if (i == newRegister.length - 1) {
                register += "#";
            } else {
                register += "|";
            }
        }

        register = replaceAccentedCharacters(register);

        //--Insert(Append) the new register in the register file - Ok      
        File dir = new File(new File("").getAbsolutePath());

        //get the last RRN address to write in the primary index file later
        String Address="";
        try {
            RandomAccessFile file = new RandomAccessFile(new File(dir, "/" + filename), "r");
            Address = file.length() + "";
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(filename, true));
            writer.append(register);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //--Get Keys Names
        BufferedReader reader = null;
        String fileContent = "";
        String[] Keys = null;
        try {
            reader = new BufferedReader(new FileReader(new File(dir, "/" + filename)));
            int r;
            while ((r = reader.read()) != -1 && (char) r != '#') {
                char ch = (char) r;
                fileContent += ch;
                Keys = fileContent.split("\\|");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        insertRegisterInPrimaryIndex(Keys, newRegister, Address);
        insertRegisterInSecondaryIndexes(Keys, newRegister);

    }

    public void insertRegisterInPrimaryIndex(String[] Keys, String[] newRegister, String Address) {
        //--Locate the right position in the file primary file
        BufferedReader reader2 = null;
        int rnnCounter = -1;
        boolean isRightPosition = false;
        File dir = new File(new File("").getAbsolutePath());

        try {
            reader2 = new BufferedReader(new FileReader(new File(dir, "/" + Keys[0] + ".txt")));
            String line;

            //Buscar posição de inserção comparando as chaves
            while (isRightPosition == false && (line = reader2.readLine()) != null) {
                String[] valuesOfActualRegister = line.split("\\|");
                int result = newRegister[0].compareTo(valuesOfActualRegister[0]);
                rnnCounter++;

                //Achei a posição de inserção
                if (result < 0) {
                    isRightPosition = true;
                    reader2.close();
                }

            }
            reader2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //--prepare the file before insert the new register and insert
        try {
            RandomAccessFile file = new RandomAccessFile(new File(dir, "/" + Keys[0] + ".txt"), "rw");
            String previousRegister = "";
            file.seek(file.length());
            file.writeBytes("\r\n");

            if (isRightPosition) {
                //Insertion Sort
                for (int rrn = (int) file.length() / 64; rrn > rnnCounter + 1 / 64; rrn--) {
                    //get previous register and
                    file.seek((rrn - 1) * 64);
                    previousRegister = file.readLine();

                    //move to forward position
                    file.seek(rrn * 64);
                    file.writeBytes(previousRegister);

                }

                file.seek(rnnCounter * 64);

            } else {
                //if the location of insert is the last position, I just set the cursor to last RRN position
                file.seek((rnnCounter + 1) * 64);
            }

            //Register Insertion
            String filledPK = FileCreator.fillString(newRegister[0], 50);
            String filledAddress = FileCreator.fillString(Address, 9);
            file.writeBytes(filledPK + "|" + filledAddress);
            file.writeBytes("\r\n");

            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertRegisterInSecondaryIndexes(String[] Keys, String[] newRegister) {
        //--Secondary Indexes Register insertion
        for (int i = 1; i < Keys.length; i++) {
            String line = "";
            int rnnCounter = -1;
            boolean isRightPosition = false;
            boolean isEqual = false;
            String auxRRN = "";
            File dir = new File(new File("").getAbsolutePath());

            try {

                BufferedReader reader = new BufferedReader(new FileReader(new File(dir, "/SecondaryIndexes/" + Keys[i] + ".txt")));

                //find the right position to insert the register
                while (!isRightPosition && !isEqual && (line = reader.readLine()) != null) {
                    String[] valuesOfActualRegister = line.split("\\|");
                    String valueOfRegisterFilled = FileCreator.fillString(newRegister[i], 50);
                    int result = valueOfRegisterFilled.compareToIgnoreCase(valuesOfActualRegister[0]);
                    rnnCounter++;

                    //Got the right position
                    if (result < 0) {
                        isRightPosition = true;
                        reader.close();
                    }

                    //Got the value that alredy exists in the archive
                    if (result == 0) {
                        isEqual = true;

                        //For future use, I store the RRN
                        auxRRN = valuesOfActualRegister[1];
                        reader.close();
                    }
                }
                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            //--prepare the file before insert the new register and insert
            try {
                RandomAccessFile file = new RandomAccessFile(new File(dir, "/SecondaryIndexes/" + Keys[i] + ".txt"), "rw");
                RandomAccessFile fInverted = new RandomAccessFile(new File(dir, "/SecondaryIndexes/" + Keys[i] + "_IL.txt"), "rw");
                String invertedRRNCounter = fInverted.length() / 64 + "";
                String previousRegister = "";

                if (isRightPosition && !isEqual) {
                    for (int rrn = (int) file.length() / 64; rrn > rnnCounter + 1 / 64; rrn--) {
                        //get previous register and
                        file.seek((rrn - 1) * 64);
                        previousRegister = file.readLine();

                        //move to forward position
                        file.seek(rrn * 64);
                        file.writeBytes(previousRegister);

                    }

                    file.seek(rnnCounter * 64);
                } else {
                    ////if the location of insert is the last position, I just set the cursor to last RRN position
                    file.seek((rnnCounter + 1) * 64);
                }
                String filledPK = "";

                //In the case of the value already exist in the secondary index
                if (isEqual) {
                    file.seek((rnnCounter) * 64);//just overwrite the previous value
                }

                filledPK = FileCreator.fillString(newRegister[i], 50);
                String filledRNN = FileCreator.fillString(invertedRRNCounter, 9);
                file.writeBytes(filledPK + "|" + filledRNN);
                file.writeBytes("\r\n");
                file.close();

                BufferedWriter writer = null;

                writer = new BufferedWriter(new FileWriter(dir + "/SecondaryIndexes/" + Keys[i] + "_IL.txt", true));
                filledPK = FileCreator.fillString(newRegister[0], 50);
                String filledRRN = "";
                if (!isEqual) {
                    filledRRN = FileCreator.fillString("-1", 9);
                } else {
                    filledRRN = FileCreator.fillString(auxRRN, 9);//auxRRN is the previous RRN
                }

                writer.append(filledPK + "|" + filledRRN);
                writer.newLine();
                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private String replaceAccentedCharacters(String content) {
        content = Normalizer.normalize(content, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return content;
    }

    public void removeRegister(String key) {
        System.out.print("Register to be removed: ");
        String address = searchRegisterByPK(key);

        if (address.isEmpty()) {
            System.out.println("Register not found!");
        } else {
            String register = getRegister(address);
            //System.out.println(register);
            String removedRegister = "%" + register.substring(1);
            
            File dir = new File(new File("").getAbsolutePath());
            // replace the old register by its "removed version" in file1.txt
            try {
                RandomAccessFile f = new RandomAccessFile(dir + "/" + filename, "rw");
                f.seek(Long.valueOf(address));
                f.writeBytes(removedRegister);
                f.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Remove from PrimaryIndex
            removeFromPrimaryIndexFile(key);

            // Remove from SecondaryIndex and InvertedList
            removeFromSecondaryIndexFile(register);
        }

    }

    private void removeFromPrimaryIndexFile(String key) {
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
                if (mid == file.length()) {
                    break;
                }
                file.seek(mid);

                String line = file.readLine();
                String pk = line.substring(0, 50).trim();
                if (key.compareTo(pk) < 0) {
                    end = mid - 64;
                } else if (key.equalsIgnoreCase(pk)) {
                    // remove the found register of the StringBuffer (completeFile)
                    completeFile.delete((int) mid, (int) mid + 64);
                    // re-write the file without the removed register
                    rewriteData(completeFile, dir);
                    start = end + 1; // just to finish the while loop
                } else {
                    start = mid + 64;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFromSecondaryIndexFile(String register) {
        String[] values = register.split("\\|");
        for (int i = 1; i < values.length; i++) {
            File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes" + "/" + fields[i] + ".txt");
            values[i] = FileCreator.fillString(values[i], 50).trim(); 
            try {
                RandomAccessFile file = new RandomAccessFile(dir, "rw");
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
                    if (values[i].compareTo(pk) < 0) {
                        end = mid - 64;
                    } else if (values[i].equalsIgnoreCase(pk)) {
                        // 52 because of the pipe '|'
                        String rrn = line.substring(52).trim();
                        int pos = removeFromInvertedList(i, rrn, values[0]);
                        if (pos == -1) {
                            // Remove the register here
                            StringBuffer buffer = new StringBuffer();
                            buffer = copyAllData(dir);
                            buffer.delete((int) mid, (int) mid + 64);
                            rewriteData(buffer, dir);

                        } else if (pos != 0) {
                            // change rrn in the secondary index file
                            file.seek(mid + 52);
                            file.writeBytes(String.valueOf(pos));
                        }
                        start = end + 1; // just to finish the while loop
                    } else {
                        start = mid + 64;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 3 possible situations
     *
     * Ex: Ano - 2011
     *
     * 2011 -> 5
     *
     * key next rrn ..039 -> 3 5 ..074 -> 2 3 ..673 -> -1 2
     *
     * 1 - Remove 039 2011 starts pointing to the next element. So: 2011 -> 3
     *
     * 2 - Remove 074 039(previous element) starts pointing to 673(next
     * element). So: 039 -> 2
     *
     * 3 - Remove 673 074(previous element) starts pointing to -1. So: 074 -> -1
     *
     *
     * @param indexPos
     * @param rrn
     * @param key
     * @return
     */
    private int removeFromInvertedList(int indexPos, String rrn, String key) {
        File dir = new File(new File("").getAbsolutePath() + "/SecondaryIndexes" + "/" + fields[indexPos] + "_IL.txt");

        try {
            RandomAccessFile file = new RandomAccessFile(dir, "rw");

            int intRrn = Integer.valueOf(rrn);
            int pos = intRrn * 64;
            file.seek(pos);
            String line = file.readLine();
            String value = line.substring(0, 50).trim();
            String next = line.substring(52).trim();
            
            if (!next.equals("-1")) {
                //Remove here
                // need to find the given key
                StringBuffer buffer = new StringBuffer();
                if (key.equals(value)) {
                    // 1st situation
                    // replace the removed line with blank spaces
                    buffer = copyAllData(dir);
                    buffer.replace(intRrn * 64, intRrn * 64 + 64, FileCreator.fillString("", 62) + "\r\n");
                    rewriteData(buffer, dir);
                    return Integer.valueOf(next);
                } else {
                    int previous = intRrn;
                    while (!next.equals("-1")) {
                        int intNext = Integer.valueOf(next);
                        file.seek(intNext * 64);
                        line = file.readLine();
                        value = line.substring(0, 50).trim();
                        next = line.substring(52).trim();

                        // 3rd situation
                        if (next.equals("-1")) {
                            // seek to the rrn position in the file
                            file.seek(previous * 64 + 52);
                            // the previous element must point to -1
                            file.writeBytes("-1");
                            // replace the removed line with blank spaces
                            buffer = copyAllData(dir);
                            buffer.replace(intNext * 64, intNext * 64 + 64, FileCreator.fillString("", 62) + "\r\n");
                            rewriteData(buffer, dir);
                            return 0;
                        } else {
                            // 2nd situation
                            if (key.equals(value)) {
                                // the previous must point to the next
                                file.seek(intNext * 64);
                                line = file.readLine();
                                next = line.substring(52).trim();
                                // seek to the rrn position in the file
                                file.seek(previous * 64 + 52);
                                file.writeBytes(next);
                                // replace the removed line with blank spaces
                                buffer = copyAllData(dir);
                                buffer.replace(intNext * 64, intNext * 64 + 64, FileCreator.fillString("", 62) + "\r\n");
                                rewriteData(buffer, dir);
                                return 0;
                            }
                        }
                        previous = intNext;

                    }
                }
                return 0;
            } else {
                // remove here and return -1
                StringBuffer buffer = new StringBuffer();
                buffer = copyAllData(dir);
                buffer.delete(pos, pos + 64);
                rewriteData(buffer, dir);
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;

    }

    private StringBuffer copyAllData(File dir) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(dir));
            String l = br.readLine();
            while (l != null) {
                buffer.append(l + "\r\n");
                l = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer;
    }

    private void rewriteData(StringBuffer buffer, File dir) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(dir));
            bw.write(buffer.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
