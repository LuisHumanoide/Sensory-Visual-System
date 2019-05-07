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
public class DoubleArray implements Serializable{

    private double value[];

    public void setValue(double[] label) {
        this.value = value;
    }

    public DoubleArray(double... value) {
        this.value = value;
    }

    public double[] getValueArray() {
        return value;
    }

    public double getValue() {
        return value[0];
    }

    public double getValue(int index) {
        return value[index];
    }

}
