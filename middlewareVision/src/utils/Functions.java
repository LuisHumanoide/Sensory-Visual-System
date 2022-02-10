/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import VisualMemory.Cell;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32FC1;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;
import utils.filters.CurvatureFilter;

/**
 *
 * @author Laptop
 */
public class Functions {

    /**
     * Gabor filter
     *
     * @param img source image
     * @param phi phase
     * @param part part of the filter
     * @param sigma sigma
     * @param inc increment
     * @return
     */
    public static Mat gaborFilter(Mat img, int part, int parity) {
        Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        img.convertTo(img, CV_32F);
        Imgproc.filter2D(img, gab, CV_32F, SpecialKernels.GaborKernels[parity][part]);
        Imgproc.threshold(gab, gab, 0, 1, Imgproc.THRESH_TOZERO);
        return gab;
    }

    public static Mat filter(Mat img, Mat filter) {
        Mat filt = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        img.convertTo(img.clone(), CV_32FC1);
        Imgproc.filter2D(img, filt, CV_32FC1, filter);
        Imgproc.threshold(filt, filt, 0, 1, Imgproc.THRESH_TOZERO);
        return filt;
    }

    /**
     * Energy process
     *
     * @param mat1
     * @param mat2
     * @return
     */
    public static Mat energyProcess(Mat mat1, Mat mat2) {
        Mat r1, r2;

        Mat energy = Mat.zeros(mat1.rows(), mat1.cols(), CvType.CV_32FC1);
        r1 = mat1.clone();
        r2 = mat2.clone();

        Core.pow(r1, 2, r1);
        Core.pow(r2, 2, r2);

        Core.add(r1, r2, r1);

        Core.sqrt(r1, energy);

        Imgproc.threshold(energy, energy, 0, 1, Imgproc.THRESH_TOZERO);

        return energy;
    }

    /**
     *
     * @param src1
     * @param src2
     * @param l3
     * @return
     */
    public static Mat V2Activation(Mat src1, Mat src2, double l3) {
        Mat dst = new Mat();
        Mat vlvr = new Mat();
        Mat vlpvr = new Mat();
        Mat num = new Mat();
        Mat den = new Mat();
        Mat h = new Mat();
        
        Scalar dl3 = new Scalar((double) 1 / l3);
        Scalar d2l3 = new Scalar((double) 2 / l3);
        Scalar dl3_2 = new Scalar((double) 1 / (l3 * l3));
        
        Core.multiply(src1, src2, vlvr);
        
        Core.add(src1, src2, vlpvr);
        Core.add(vlpvr, d2l3, num);
        
        Core.multiply(vlpvr, dl3, den);
        
        Core.add(den, vlpvr, den);
        Core.add(den, dl3_2, den);
        
        Core.divide(num, den, h);
        
        Core.multiply(vlvr, h, dst);
        
        Imgproc.threshold(dst, dst, 0, 1, Imgproc.THRESH_TOZERO);
        
        return dst;
    }

    public static Mat maxSum(Cell... cells) {
        return MatrixUtils.maxSum(cells);
    }

    
    /**
     * Perform the curvature filtering
     * @param src the original image to filter
     * @param cFilter the curvature filter class
     * @param convex if it's neccesary to substract the convex result
     * @return the activation matrix corresponding to an specific curvature in an specific orientation
     */
    public static Mat curvatureFiltering(Mat src, CurvatureFilter cFilter, boolean convex) {
        
        Mat concaveFiltered[];
        Mat convexFiltered[];
        
        concaveFiltered = new Mat[cFilter.n];
        convexFiltered = new Mat[cFilter.n];
        
        Mat concaveResult = Mat.zeros(src.rows(), src.cols(), src.type());
        Mat convexResult = Mat.zeros(src.rows(), src.cols(), src.type());
        
        Core.add(concaveResult, Scalar.all(1), concaveResult);
        Core.add(convexResult, Scalar.all(1), convexResult);
        
        for (int i = 0; i < cFilter.n; i++) {
            concaveFiltered[i] = Functions.filter(src, SpecialKernels.rotateKernelRadians(cFilter.concaveFilters[i], cFilter.angle));
            Core.multiply(concaveFiltered[i], Scalar.all(cFilter.mul), concaveFiltered[i]);
            concaveResult = concaveResult.mul(concaveFiltered[i]);
            
            if (convex) {
                convexFiltered[i] = Functions.filter(src, SpecialKernels.rotateKernelRadians(cFilter.convexFilters[i], cFilter.angle));
                Core.multiply(convexFiltered[i], Scalar.all(cFilter.mul), convexFiltered[i]);
                convexResult = convexResult.mul(convexFiltered[i]);
            }
        }
        
        if (convex) {
            Core.subtract(concaveResult, convexResult, concaveResult);
        }
        return concaveResult;
    }

}
