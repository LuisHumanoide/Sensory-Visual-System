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
 *
 * @author HumanoideFilms
 */
public class MotionCell extends Cell {

    Mat matT[];
    int stages=3;
    public Mat mat1st;
    
    //time displacement
    int dt;
    //distance displacement
    int dx;

    int timeCount = 0;

    double speed;

    public MotionCell() {
        super();
        matT=new Mat[stages];
        for(int i=0;i<stages;i++){
            matT[i]=Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
        }
        mat1st = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    }

    public void addDelay(Mat mat) {
        for(int i=stages-1;i>0;i--){
            matT[i]=matT[i-1].clone();
        }
        matT[0]=mat.clone();
    }

    public void setDxDt(int dx, int dt) {
        this.dx = dx;
        this.dt = dt;
        speed = (double) dx / dt;
    }

    public void motionProcess(Mat mat) {
        
        //Imgproc.blur(m2, m2, new Size(5,5));
        if (timeCount == dt) {
            timeCount = 0;
            addDelay(mat.clone());
            mat1st=Functions.motionProcess(matT, this.dx, (double)((Math.PI/Config.gaborOrientations)*id));
            //this.mat=Functions.motionProcess(matT, this.dx, (double)((Math.PI/Config.gaborOrientations)*id));
        }
        timeCount++;
    }

}
