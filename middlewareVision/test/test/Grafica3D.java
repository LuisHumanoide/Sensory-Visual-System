/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import static java.lang.Math.pow;
import javax.swing.JFrame;
import org.math.plot.Plot3DPanel;

/**
 *
 * @author HumanoideFilms
 */
public class Grafica3D {
    
    public static void main(String [] args){
        double [] x={0.1,0.2,0.3,0.4,0.5};
        double [] y={0.1,0.2,0.3,0.4,0.5};
        double [][] z=f(x,y);
        Plot3DPanel grafica3D=new Plot3DPanel();
        grafica3D.addLegend("SOUTH");
        grafica3D.addGridPlot("ASDF",x,y,z);
        JFrame frame=new JFrame("grafica");
        frame.setSize(500,500);
        frame.setContentPane(grafica3D);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }
    
    public static double[][] f(double[]x, double[] y){
        double[][] z =new double[y.length][x.length];
        for(int i=0;i<x.length;i++){
            for(int j=0;j<y.length;j++){
                z[j][i]=pow(x[i],2)+pow(y[j],2);
            }
        }
        return z;
    }
    
}
