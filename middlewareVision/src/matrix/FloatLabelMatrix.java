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
public class FloatLabelMatrix {

    private FloatLabel label[][];

    public FloatLabelMatrix(int width, int height) {
        this.label = new FloatLabel[width][height];
        this.width = width;
        this.height = height;
    }

    public FloatLabel[][] getLabel() {
        return label;
    }

    public void setLabel(FloatLabel[][] label) {
        this.label = label;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public FloatLabel getLabel(int x, int y) {
        return label[x][y];
    }

    public FloatLabel[][] getLabelMatrix() {
        return label;
    }

    public void setLabel(int x, int y, float[] values) {
        label[x][y] = new FloatLabel(values);
    }

    private int width;
    private int height;

}
