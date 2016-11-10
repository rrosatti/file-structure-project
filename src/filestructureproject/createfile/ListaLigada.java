/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filestructureproject.createfile;

/**
 *
 * @author rogis
 */
class ListaLigada {
        
    long chavePrimaria;
    int RNN;
    ListaLigada prox;

    
    //Construtor
    public ListaLigada(long chavePrimaria) {
        this.chavePrimaria = chavePrimaria;
        RNN = -1;
    }
    
    public ListaLigada(long chavePrimaria,int RNN) {
        this.chavePrimaria = chavePrimaria;
        this.RNN = RNN;
    }
    

    public long getChavePrimaria() {
        return chavePrimaria;
    }

    public void setChavePrimaria(int chavePrimaria) {
        this.chavePrimaria = chavePrimaria;
    }

    public int getRNN() {
        return RNN;
    }

    public void setRNN(int RNN) {
        this.RNN = RNN;
    }

    
    
    public ListaLigada getProx() {
        return prox;
    }


    
    
    
    
}
