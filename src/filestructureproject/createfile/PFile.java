/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject.createfile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rodri
 */
public class PFile {

    private String[] fields;
    private List<Register> registers;
    private static int numFields = 0;
    
    public PFile(int n) {
        fields = new String[n];
        registers = new ArrayList<>();
    }
    
    public void addField(String field) {
        fields[numFields++] = field;
    }
    
    public String getField(int pos) {
        return fields[pos];
    }
    
    public String[] getFields() {
        return fields;
    }
    
    public void addRegister(Register res) {
        registers.add(res);
    }
    
    public Register getRegister(int pos) {
        return registers.get(pos);
    }
    
    public List<Register> getRegisters() {
        return registers;
    }
    
}
