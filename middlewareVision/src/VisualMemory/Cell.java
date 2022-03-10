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
    //Extra Matrix
    public Mat eMat;
    public Cell[] previous;
    public Cell[] next;
    public int id;
    public Mat filter;
    
    public int ids[];

    /**
     * Return the OpenCV Mat
     * @return an OpenCV Mat
     */
    public Mat getMat() {
        return mat;
    }

    /**
     * Set the OpenCV Mat
     * @param mat 
     */
    public void setMat(Mat mat) {
        this.mat = mat;
    }

    /**
     * Get the id, the <b> id </b> is useful for obtaining an angle
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    
    public void setIds(int ... ids){
        ids=ids;
    }
    
    /**
     * Cell constructor with 3 parameters, the actual Matrix, previous and next Cells
     * @param mat
     * @param previous
     * @param next 
     */
    public Cell(Mat mat, Cell[] previous, Cell[] next) {
        this.mat = mat;
        this.previous = previous;
        this.next = next;
    }

    /**
     * Void constructor
     */
    public Cell() {
        mat = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    }
    
    /**
     * Cell constructor if a required size needs to be settled
     * @param size 
     */
    public Cell(int size){
        mat = Mat.zeros(new Size(size,size), CvType.CV_32FC1);
        eMat = Mat.zeros(new Size(size,size), CvType.CV_32FC1);
    }
    
    /**
     * Constructor for setting a OpenCV Mat
     * @param mat 
     */
    public Cell(Mat mat){
        this.mat=mat;
    }
    
    /**
     * Set the filter to convolve
     * @param filter 
     */
    public void setFilter(Mat filter){
        this.filter=filter;
    }

    /**
     * Set the next Cells
     * @param next 
     */
    public void setNext(Cell[] next) {
        this.next = next;
    }

    /**
     * Set the previous Cells, useful for feedback process
     * @param pre 
     */
    public void setPrevious(Cell... pre) {
        int i = 0;
        previous = new Cell[pre.length];
        for (Cell mat : pre) {
            previous[i] = mat;
            i++;
        }
    }

}
