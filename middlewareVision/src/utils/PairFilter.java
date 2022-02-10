/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.opencv.core.Mat;

/**
 *
 * @author HumanoideFilms
 */
public class PairFilter {
    
    Mat filter1;
    Mat filter2;

    public PairFilter(Mat filter1, Mat filter2) {
        this.filter1 = filter1;
        this.filter2 = filter2;
    }

    public Mat getFilter1() {
        return filter1;
    }

    public void setFilter1(Mat filter1) {
        this.filter1 = filter1;
    }

    public Mat getFilter2() {
        return filter2;
    }

    public void setFilter2(Mat filter2) {
        this.filter2 = filter2;
    }
    
    
    
}
