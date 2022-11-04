/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import java.util.List;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import utils.Config;

/**
    Class representing a cell group
 */
public class Cell{

    public Mat mat;
    //Extra Matrix
    public Mat eMat;
    //Previous and next cells
    public Cell[] previous;
    public Cell[] next;
    //id
    public int id;
    //filter to make a convolution
    public Mat filter;
    //Label of the cell
    String label;

    public Mat geteMat() {
        return eMat;
    }

    public String getLabel() {
        return label;
    }

    /**
     * If the cell is labeled it is added to the LabeledCells map
     * in order to be used in the simple shape selectivity model
     * @param label
     * @param eye 
     */
    public void setLabel(String label, int eye) {
        this.label = label;
        if(eye==0){
            LabeledCells.addCellL(label, this);
        }
        if(eye==1){
            LabeledCells.addCellR(label, this);
        }       
    }

    public void seteMat(Mat eMat) {
        this.eMat = eMat;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    double speed;
    double angle;
    
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
     * Set the previous Cells, useful for feedback process or make the activation process with the previous cells
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
    
    /**
     * Set the previous cells from a list
     * @param list 
     */
    public void setPrevious(List<Cell> list){
        int i=0;
        previous = new Cell[list.size()];
        for(Cell cell:list){
            previous[i] = cell;
            i++;
        }
    }

}
