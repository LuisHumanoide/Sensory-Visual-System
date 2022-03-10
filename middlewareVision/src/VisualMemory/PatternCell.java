/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.Mat;

/**
 *
 * @author HumanoideFilms
 */
public class PatternCell extends Cell {

    double speed;
    double angle;
    int indexes[];
    
    public void setIds(int ... ids){
        indexes=ids;
    }

    public PatternCell() {
        super();
    }
    
    public PatternCell(int size){
        super(size);
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

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public Mat geteMat() {
        return eMat;
    }

    public void seteMat(Mat eMat) {
        this.eMat = eMat;
    }

    public Cell[] getPrevious() {
        return previous;
    }

    public void setPrevious(Cell[] previous) {
        this.previous = previous;
    }

    public Cell[] getNext() {
        return next;
    }

    public void setNext(Cell[] next) {
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Mat getFilter() {
        return filter;
    }

    public void setFilter(Mat filter) {
        this.filter = filter;
    }

}
