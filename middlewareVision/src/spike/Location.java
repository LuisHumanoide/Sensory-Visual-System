/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spike;

import java.io.Serializable;

/**
 *
 * @author HumanoideFilms
 */
public class Location implements Serializable{

    public int index[];

    public Location(int ... values) {
        this.index=values;
    }
    
    public int[] getValues(){
        return index;
    }


    
}
