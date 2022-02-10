/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

import java.io.Serializable;

/**
 *
 * @author Luis Humanoide
 */
public class ArrayMatrix implements Serializable{
    
    matrix [] arrayMatrix;

    public matrix[] getArrayMatrix() {
        return arrayMatrix;
    }

    public void setArrayMatrix(matrix[] arrayMatrix) {
        this.arrayMatrix = arrayMatrix;
    }

    public ArrayMatrix(matrix[] arrayMatrix) {
        this.arrayMatrix = arrayMatrix;
    }
    
}
