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
public class SecondaryIndex implements Comparable<SecondaryIndex> {
    
    private String value;
    private int rrn;
    
    public SecondaryIndex() {
        //invertedList = new ArrayList<>();
    }
    
    public SecondaryIndex(String value, int rrn) {
        this();
        this.value = value;
        this.rrn = rrn;
    }
    
    public void setRrn(int rrn) {
        this.rrn = rrn;
    }
    
    public int getRrn() {
        return rrn;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(SecondaryIndex o) {
        return this.value.compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object obj) {
        SecondaryIndex si = (SecondaryIndex) obj;
        if(compareTo(si) == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    
}
