/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject.createfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author rodri
 */
public class SecondaryIndex{
    
    private String valorDoCampo;
    private int rrn;
    private ListaLigada raiz;
    
    public SecondaryIndex(String valorDoCampo, int rrn) {
        this.valorDoCampo = valorDoCampo;
        this.rrn = rrn;
        this.raiz = null;
    }

    public SecondaryIndex(String valorDoCampo, int rrn, ListaLigada raiz) {
        this.valorDoCampo = valorDoCampo;
        this.rrn = rrn;
        this.raiz = raiz;
    }

    public String getChave() {
        return valorDoCampo;
    }

    public void setChave(String chave) {
        this.valorDoCampo = chave;
    }

    public int getRrn() {
        return rrn;
    }

    public void setRrn(int rrn) {
        this.rrn = rrn;
    }

    public ListaLigada getRaiz() {
        return raiz;
    }

    public void setRaiz(ListaLigada raiz) {
        this.raiz = raiz;
    }
    
    public ListaLigada inserirListaInicio(ListaLigada lista) {
        lista.prox = raiz;
        raiz = lista;
        
        return lista;
    }
    

}
   
