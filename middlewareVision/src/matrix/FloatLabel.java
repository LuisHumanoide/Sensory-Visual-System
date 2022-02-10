/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

/**
 *
 * @author HumanoideFilms
 */
public class FloatLabel {

    private float label[];

    public float[] getLabel() {
        return label;
    }

    public void setLabel(float[] label) {
        this.label = label;
    }

    public FloatLabel(float... label) {
        this.label = label;
    }

}
