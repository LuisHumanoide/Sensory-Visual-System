/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.MTCells;

import VisualMemory.Cell;
import VisualMemory.V1Cells.V1Bank;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import utils.Functions;
import utils.MatrixUtils;

/**
 *
 * @author HumanoideFilms
 */
public class MTBank {

    //[eye]
    public static ComponentCells[] MTCC;
    public static PatternCells[] MTPC;
    static int CCd1;
    static int CCd2;

    /**
     * Initialize <b>MT Component Cells</b><br>
     *
     * @param d1 number of velocities/speeds
     * @param d2 number of orientations
     * @param size size of the component cells
     */
    public static void initializeComponentCells(int d1, int d2, int size) {
        MTCC = new ComponentCells[2];
        CCd1=d1;
        CCd2=d2;
        for (int i = 0; i < 2; i++) {
            MTCC[i] = new ComponentCells(d1, d2, size);
        }
        initializePatternCells(d1 * d2, size);
    }

    /**
     * Initialize all Pattern Cells of MT<br>
     * The size of the bank will be <b>n*(n-1)/2</b><br>
     * because it is only take into account the different angles and speeds
     * @param n is the size of the Pattern Cell Bank, should be the dimensions <b> d1 </b> and <b> d2 </b> from
     * the Component Cells
     * @param size is the Size of the matrix
     */
    public static void initializePatternCells(int n, int size) {
        MTPC = new PatternCells[2];
        for (int i = 0; i < 2; i++) {
            MTPC[i] = new PatternCells(n*(n-1)/2, size);
        }
        for (int i = 0; i < 2; i++) {
            setPreviousMTPC(i);
        }
    }
    
    /**
     * Set the previous matrix into MT PatternCells <br>
     * the PatternCell performs the process with the previous matrix
     * @param eye 
     */
    public static void setPreviousMTPC(int eye){
        int c=0;
        int n=CCd1*CCd2;
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                //utils.Msg.print(c+"     "+i/CCd2+"  "+i%CCd2+"-------"+j/CCd2+"  "+j%CCd2);
                MTPC[eye].Cells[c].setPrevious(MTCC[eye].CCells[i/CCd2][i%CCd2],MTCC[eye].CCells[j/CCd2][j%CCd2]);
                double velocity[]=Functions.IoCProcess(MTCC[eye].CCells[i/CCd2][i%CCd2].getSpeed(), MTCC[eye].CCells[i/CCd2][i%CCd2].getAngle(), 
                        MTCC[eye].CCells[j/CCd2][j%CCd2].getSpeed(), MTCC[eye].CCells[j/CCd2][j%CCd2].getAngle());
                MTPC[eye].Cells[c].setSpeed(velocity[0]);
                MTPC[eye].Cells[c].setAngle(velocity[1]);
                utils.Msg.print("speed  MT "+MTPC[eye].Cells[c].getSpeed()+"  angle "+MTPC[eye].Cells[c].getAngle());
                c++;
            }
        }
    }

}
