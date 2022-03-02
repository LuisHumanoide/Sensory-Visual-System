/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import VisualMemory.Cell;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.Config;
import utils.Functions;

/**
 * Extension of the <b> Cell class </b><br>
 * <b> MotionCell </b> are useful for the motion process<br>
 * it has a buffer and a method for performing a simple motion detection
 * @author HumanoideFilms
 */
public class MotionCell extends Cell {

    Mat matT[];
    int stages = 3;
    public Mat mat1st;

    //time displacement
    int dt;
    //distance displacement
    int dx;
    double angle;

    int timeCount = 0;

    double speed;

    /**
     * Void constructor
     */
    public MotionCell() {
        super();
        matT = new Mat[stages];
        for (int i = 0; i < stages; i++) {
            matT[i] = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
        }
        mat1st = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    }

    /**
     * Method for adding a Matrix to the buffer
     * @param mat 
     */
    public void addDelay(Mat mat) {
        for (int i = stages - 1; i > 0; i--) {
            matT[i] = matT[i - 1].clone();
        }
        matT[0] = mat.clone();
    }

    /**
     * Set the spatial <b> dx </b> and temporal <b> dt </b> difference<br>
     * speed can be calculated by the divition <b> dx/dt </b>
     * @param dx
     * @param dt 
     */
    public void setDxDt(int dx, int dt) {
        this.dx = dx;
        this.dt = dt;
        speed = (double) dx / dt;
    }

    /**
     * Method for performing the basic stage of MotionProcessing <br>
     * without a speed opponent process
     * @param mat 
     */
    public void cellMotionProcess(Mat mat) {
        if (timeCount == dt) {
            timeCount = 0;
            addDelay(mat.clone());
            mat1st = Functions.stage1MotionProcess(matT, this.dx, (double) ((Math.PI / Config.gaborOrientations) * id));
        }
        timeCount++;
    }

}
