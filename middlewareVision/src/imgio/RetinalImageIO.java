/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgio;

import java.util.Arrays;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author dmadrigal
 */
public class RetinalImageIO {
    
    public static void lmmWriter(Mat img, String path){
        int rows = img.rows();
        int cols = img.cols();
        
        Mat b = Mat.zeros(rows,cols,CvType.CV_8UC1);
        Mat g = new Mat(rows,cols,CvType.CV_8UC1);
        Mat r = new Mat(rows,cols,CvType.CV_8UC1);
        Mat imgOutput = new Mat(rows,cols,CvType.CV_8UC3);
        
        float[] imgData = new float[rows*cols];
        byte[] gData = new byte[imgData.length];
        byte[] rData = new byte[imgData.length];
        
        img.get(0, 0, imgData);
        for(int i = 0; i < imgData.length; i++){
            if(imgData[i] >= 0)
                rData[i] = (byte)(imgData[i] * 255);
            else
                gData[i] = (byte)(Math.abs(imgData[i])*255);

        }

        g.put(0, 0, gData);
        r.put(0, 0, rData);
        
        Core.merge(Arrays.asList(b,g,r), imgOutput);
        Imgcodecs.imwrite(path, imgOutput);
    } 
    
    public static void smlpmWriter(Mat img, String path){
        int rows = img.rows();
        int cols = img.cols();
        
        Mat b = new Mat(rows,cols,CvType.CV_8UC1);
        Mat g = new Mat(rows,cols,CvType.CV_8UC1);
        Mat r = new Mat(rows,cols,CvType.CV_8UC1);
        Mat imgOutput = new Mat(rows,cols,CvType.CV_8UC3);
        
        float[] imgData = new float[rows*cols];
        byte[] bData = new byte[imgData.length];
        byte[] gData = new byte[imgData.length];
        byte[] rData = new byte[imgData.length];
        
        img.get(0, 0, imgData);
        for(int i = 0; i < imgData.length; i++){
            if(imgData[i] >= 0){
                bData[i] = (byte)(imgData[i]*255);
            }
            else{
                gData[i] = (byte)(Math.abs(imgData[i])*255);
                rData[i] = gData[i];
            }
        }

        b.put(0, 0, bData);
        g.put(0, 0, gData);
        r.put(0, 0, rData);
        
        Core.merge(Arrays.asList(b,g,r), imgOutput);
        Imgcodecs.imwrite(path, imgOutput);
    } 
    
    public static void lpmWriter(Mat img, String path){
        int rows = img.rows();
        int cols = img.cols();
        
        Mat b = new Mat(rows,cols,CvType.CV_8UC1);
        Mat g = new Mat(rows,cols,CvType.CV_8UC1);
        Mat r = new Mat(rows,cols,CvType.CV_8UC1);
        Mat imgOutput = new Mat(rows,cols,CvType.CV_8UC3);
        
        float[] imgData = new float[rows*cols];
        byte[] lData = new byte[imgData.length];
        
        img.get(0, 0, imgData);
        for(int i = 0; i < imgData.length; i++){
                lData[i] = (byte)(imgData[i]*255);
        }

        b.put(0, 0, lData);
        g.put(0, 0, lData);
        r.put(0, 0, lData);
        
        Core.merge(Arrays.asList(b,g,r), imgOutput);
        Imgcodecs.imwrite(path, imgOutput);
    } 
    
    public static void borderWriter(Mat img,String path){
        int rows = img.rows();
        int cols = img.cols();
        
        Mat b = new Mat(rows,cols,CvType.CV_8UC1);
        Mat g = new Mat(rows,cols,CvType.CV_8UC1);
        Mat r = new Mat(rows,cols,CvType.CV_8UC1);
        Mat imgOutput = new Mat(rows,cols,CvType.CV_8UC3);
        
        float[] imgData = new float[rows*cols];
        byte[] lData = new byte[imgData.length];
        
        img.get(0, 0, imgData);
        for(int i = 0; i < imgData.length; i++){
            if(imgData[i] > 0)
                lData[i] = (byte) (255*imgData[i]);
        }

        b.put(0, 0, lData);
        g.put(0, 0, lData);
        r.put(0, 0, lData);
        
        Core.merge(Arrays.asList(b,g,r), imgOutput);
        Imgcodecs.imwrite(path, imgOutput);
    }
}
