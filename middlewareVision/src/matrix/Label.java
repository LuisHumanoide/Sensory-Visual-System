/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

import java.io.Serializable;

/**
 *
 * @author HumanoideFilms
 */
public class Label implements Serializable{
    
    private int label[];

    public int[] getLabel() {
        return label;
    }

    public void setLabel(int[] label) {
        this.label = label;
    }

    public Label(int ... label) {
        this.label = label;
    }
    
    
    
}
