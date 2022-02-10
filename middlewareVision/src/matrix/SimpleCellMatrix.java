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
public class SimpleCellMatrix implements Serializable {

    matrix evenOrs;
    matrix oddOrs;

    public SimpleCellMatrix(matrix evenOrs, matrix oddOrs) {
        this.evenOrs = evenOrs;
        this.oddOrs = oddOrs;
    }

    public matrix getEvenOrs() {
        return evenOrs;
    }

    public void setEvenOrs(matrix evenOrs) {
        this.evenOrs = evenOrs;
    }

    public matrix getOddOrs() {
        return oddOrs;
    }

    public void setOddOrs(matrix oddOrs) {
        this.oddOrs = oddOrs;
    }

}
