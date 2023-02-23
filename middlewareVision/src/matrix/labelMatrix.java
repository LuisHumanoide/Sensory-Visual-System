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
public class labelMatrix implements Serializable{
    
    private Label label[][];

    public labelMatrix(int width, int height) {
        this.label=new Label[width][height];
        this.width = width;
        this.height = height;
    }

    public Label getLabel(int x, int y) {
        return label[x][y];
    }
    
    public Label[][] getLabelMatrix(){
        return label;
    }

    public void setLabel(int x, int y, int[] values) {
        label[x][y]=new Label(values);
    }
    
    private int width;
    private int height;


   
}
