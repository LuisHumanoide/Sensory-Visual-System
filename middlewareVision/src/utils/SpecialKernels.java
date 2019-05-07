/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/**
 *
 * @author dmadrigal
 */
public class SpecialKernels {
    
    public static Mat getDoubleOpponentKernel(Size s, double sigma1, double sigma2,double height1, double height2, double gamma1,double gamma2,double dX){
        Mat m = new Mat(s, CvType.CV_32FC1);
        
        double[] kernel= new double[(int)(s.height*s.width)];
        double div = 0;
        int p = 0;
        int cX = (int)(s.width/2);
        int cY = (int)(s.height/2);
        int r =0;
        
        for(int i = 0; i < s.height; i++){
            for(int j = 0; j < s.width; j++){
                kernel[p]  = height1 * Math.exp(-((Math.pow(j-cX+dX,2) + (Math.pow(gamma1, 2) * Math.pow(i-cY, 2)))/(2 * Math.pow(sigma1, 2))));
                kernel[p] += height2 * Math.exp(-((Math.pow(j-cX-dX,2) + (Math.pow(gamma2, 2) * Math.pow(i-cY, 2)))/(2 * Math.pow(sigma2, 2))));
                div += Math.abs(kernel[p]);
                p++;
            }
        }
        
        m.put(0, 0, kernel);
        Core.divide(m, Scalar.all(div), m);
        for(int i =0; i < kernel.length;i++)
            r += kernel[i];
        
        System.out.println(r);
        
        return m;
    }
    
    public static Mat getOtherDoubleOpponentKernel(Size s, double sigmaX1, double sigmaY1, double sigmaX2,double sigmaY2, double height1, double height2,double dX){
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel= new double[(int)(s.height*s.width)];
        double A = 0;
        double B = 0;
        int p = 0;
        int cX = (int)(s.width/2);
        int cY = (int)(s.height/2);
        
        
        
        height1 *= 2*Math.PI*sigmaX1*sigmaY1; 
        height2 *= 2*Math.PI*sigmaX2*sigmaY2;
        
        for(int i = 0; i < s.height; i++){
            for(int j = 0; j < s.width; j++){
                A = Math.pow((j-cX+dX),2)/(2*Math.pow(sigmaX1, 2));
                B = Math.pow(i-cY, 2)/(2*Math.pow(sigmaY1, 2));
                kernel[p] = Math.exp(-(A+B))/height1;
                

                
                A = Math.pow((j-cX-dX),2)/(2*Math.pow(sigmaX2, 2));
                B = Math.pow(i-cY, 2)/(2*Math.pow(sigmaY2, 2));
                kernel[p] += Math.exp(-(A+B))/height2;

                p++;
                
            }
        }
        
        
        
        m.put(0, 0, kernel);

        return m;
    }
    
    public static Mat getSineKernel(Size s, double frec){
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel= new double[(int)(s.height*s.width)];
        int p = 0;
        for(int i = 0; i < s.height; i++){
            for(int j = 0; j < s.width; j++){
                kernel[p++] = Math.sin(frec*j);
            }
        }
        m.put(0, 0, kernel);
        return m;
    }
    
    public static Mat getCosKernel(Size s, double frec){
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel= new double[(int)(s.height*s.width)];
        int p = 0;
        for(int i = 0; i < s.height; i++){
            for(int j = 0; j < s.width; j++){
                kernel[p++] = Math.cos(frec*j);
            }
        }
        m.put(0, 0, kernel);
        return m;
    }
    
    public static Mat getGauss(Size s, double sigmaX,double sigmaY, double alpha){
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel= new double[(int)(s.height*s.width)];
        double A = 0;
        double B = 0;
        int p = 0;
        int cX = (int)(s.width/2);
        int cY = (int)(s.height/2);

        
        for(int i = 0; i < s.height; i++){
            for(int j = 0; j < s.width; j++){
                A = Math.pow((j-cX),2)/(2*Math.pow(sigmaX, 2));
                B = Math.pow(i-cY, 2)/(2*Math.pow(sigmaY, 2));
                kernel[p] = Math.exp(-(A+B)) * alpha;
                p++;
                
            }
        }

        m.put(0, 0, kernel);
        return m;
    } 
}
