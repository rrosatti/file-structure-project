/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject.createfile;

/**
 *
 * @author rodri
 */
public class Register {
    
    private String[] values;
    private int address;
    private int count = 0;
    
    public Register(int n) {
        values = new String[n];
    }
    
    public void setValue(String value) {
        values[count++] = value;
    }
    
    public void setValues(String[] values) {
        for (int i=0; i<values.length; i++) {
            setValue(values[i]);
        }
    }
    
    public String getValue(int pos) {
        return values[pos];
    }
    
    public void setAddress(int address) {
        this.address = address;
    }
    
    public int getAddress() {
        return address;
    }
    
}
