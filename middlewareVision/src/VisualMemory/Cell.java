/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import utils.Config;

/**
 *
 * @author Laptop
 */
public class Cell {

    public Mat mat;
    public Cell[] previous;
    public Cell[] next;
    public int id;
    public Mat filter;

    public Cell(Mat mat, Cell[] previous, Cell[] next) {
        this.mat = mat;
        this.previous = previous;
        this.next = next;
    }

    public Cell() {
        mat = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    }
    
    public Cell(Mat mat){
        this.mat=mat;
    }
    
    public void setFilter(Mat filter){
        this.filter=filter;
    }

    public void setNext(Cell[] next) {
        this.next = next;
    }

    public void setPrevious(Cell... pre) {
        int i = 0;
        previous = new Cell[pre.length];
        for (Cell mat : pre) {
            previous[i] = mat;
            i++;
        }
    }

}
