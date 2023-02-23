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
public class matrix implements Serializable{
    
    
    private float values[][];
    private int width;
    private int height;
    private int type;

    public float[][] getValues() {
        return values;
    }

    public void setValues(float[][] values) {
        this.values = values;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public matrix(int w, int h, int type){
        width=w;
        height=h;
        values=new float[width][height];
        this.type=type;
    }
    
    public void setValue(int i, int j, float value){
        values[i][j]=value;
    }
    
    public float getValue(int i, int j){
        return values[i][j];
    }
    
}
