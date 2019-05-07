/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.HashSet;

/**
 *
 * @author HumanoideFilms
 */
public class numSync {
    
    HashSet<Integer> expected=new HashSet();
    HashSet<Integer> received=new HashSet();
    
    /**
     * put the indexes needed or expected
     * @param indexes 
     */
    public numSync(int ... indexes){
        for(int i:indexes){
            expected.add(i);
        }
    }
    
    public numSync(int size){
        for(int i=0;i<size;i++){
            expected.add(i);
        }
    }
    
    /**
     * the value that we got
     * @param i 
     */
    public void addReceived(int i){
        received.add(i);
    }
    
    public boolean isComplete(){
        boolean complete=received.containsAll(expected);
        if(complete){
            received.clear();
        }
        return complete;
    }
    
}
