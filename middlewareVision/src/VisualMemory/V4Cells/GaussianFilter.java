/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V4Cells;

import MiniPrograms.RF;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import utils.SpecialKernels;

/**
 *
 * @author luish
 */
public class GaussianFilter {

    double rx;
    double ry;
    int px;
    int py;
    double amp;
    double angle;
    String comb;
    int size;

    public GaussianFilter(double rx, double ry, int px, int py, double amp, double angle, String comb, int size) {
        this.rx = rx;
        this.ry = ry;
        this.px = px;
        this.py = py;
        this.amp = amp;
        this.angle = angle;
        this.comb = comb;
        this.size = size;
    }

    public GaussianFilter(String rx, String ry, String px, String py, String amp, String angle, String comb, String size) {
        this.rx = Double.parseDouble(rx);
        this.ry = Double.parseDouble(ry);
        this.px = Integer.parseInt(px);
        this.py = Integer.parseInt(py);
        this.amp = Double.parseDouble(amp);
        this.angle = Double.parseDouble(angle);
        this.comb = comb;
        this.size = Integer.parseInt(size);
    }
    
     public GaussianFilter(RF rf) {
        this.rx = rf.rx;
        this.ry = rf.ry;
        this.px = rf.px;
        this.py = rf.py;
        this.amp = rf.intensity;
        this.angle = rf.angle;
        this.comb = rf.combination;
        this.size = rf.size;
    }

    public GaussianFilter(String line) {
        String[] values = line.split(" ");
        this.rx = Double.parseDouble(values[0]);
        this.ry = Double.parseDouble(values[1]);
        this.px = Integer.parseInt(values[2]);
        this.py = Integer.parseInt(values[3]);
        this.amp = Double.parseDouble(values[4]);
        this.angle = Double.parseDouble(values[5]);
        this.comb = values[6];
        this.size = Integer.parseInt(values[7]);
    }
    
    public Mat makeFilter(){
        return SpecialKernels.getAdvencedGauss(new Size(size,size), amp, -py+size/2, -px+size/2, rx, ry, Math.toRadians(angle+90));
    }
    
    public Mat makeFilter2(){
        return SpecialKernels.getAdvencedGauss(new Size(size,size), amp, -py+size/2, px+size/2, rx, ry, Math.toRadians(angle+90));
    }
    
    public String toString(){
        return " "+rx+" "+ry+" "+px+" "+py+" "+amp+" "+comb;
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getPy() {
        return py;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public double getAmp() {
        return amp;
    }

    public void setAmp(double amp) {
        this.amp = amp;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public String getComb() {
        return comb;
    }

    public void setComb(String comb) {
        this.comb = comb;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
