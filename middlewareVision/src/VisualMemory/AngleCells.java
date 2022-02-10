/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.Mat;
import utils.MatrixUtils;

/**
 *
 * @author Laptop
 */
public class AngleCells {
    
    public int scale;
    public Cell[][] Cells;
    public Mat[] mergedAC;

    public AngleCells(int scale, Cell[][] angleCells, Mat[] mergedAngleCells) {
        this.scale = scale;
        this.Cells = angleCells;
        this.mergedAC = mergedAngleCells;
    }
    
    /**
     * Create V2 Cells bank
     * @param scale scale of the RF
     * @param n1 number of Gabor Orientations (recommended)
     * @param n2 
     */
    public AngleCells(int scale,int n1,int n2){
        this.scale=scale;
        Cells=new Cell[n1][n2];
        mergedAC=new Mat[n1];
        for(int i=0;i<n1;i++){
            mergedAC[i]=new Mat();
            for(int j=0;j<n2;j++){
                Cells[i][j]=new Cell();
            }
        }
    }
    
    public AngleCells(int n1,int n2){
        Cells=new Cell[n1][n2];
        mergedAC=new Mat[n1];
        for(int i=0;i<n1;i++){
            mergedAC[i]=new Mat();
            for(int j=0;j<n2;j++){
                Cells[i][j]=new Cell();
            }
        }
    }
    
    
    
    public AngleCells(int scale,int n1,int n2, int i1, int i2){
        this.scale=scale;
        Cells=new Cell[n1][n2];
        mergedAC=new Mat[n1];
        for(int i=0;i<n1;i++){
            mergedAC[i]=new Mat();
            for(int j=0;j<n2;j++){
                Cells[i][j]=new Cell();
            }
        }
    }
    
    public void mergeCells(){
        for (int i = 0; i < mergedAC.length; i++) {
            mergedAC[i] = MatrixUtils.maxSum(Cells[i]);
        }
    }
}
