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
import utils.filters.CurvatureFilter;

/**
 *
 * @author Laptop
 */
public class Functions {


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

    /**
     * Performs the max summation with Cells <br>
     * the result is loaded on the Mat parameter from the Cell class
     *
     * @param cells array of cells
     * @return an opencv matrix
     */
    public static Mat maxSum(Cell... cells) {
        return MatrixUtils.maxSum(cells);
    }

    /**
     * Perform the curvature filtering <br>
     * the model can be seen in the paper <br>
     * <i>10.3389/fncom.2013.00067 </i><br><br>
     *
     * It consist in Gabor Filters arranged in a curvature trajectory<br>
     * there is a convex and concave trajectory<br>
     * the activations of the filters are multiplied and <br>
     * there is a difference between the concave and convex results
     *
     * @param src the original image to filter
     * @param cFilter the curvature filter class
     * @param convex if it's necessary to subtract the convex result
     * @return the activation matrix corresponding to an specific curvature in
     * an specific orientation
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

    /**
     * Simple motion process function, the receives an array of matrixes,
     * displaces the kernel and perform a multiplication, resulting in the
     * activation of motion detection
     *
     * @param T
     * @param dx
     * @param angle
     * @return
     */
    public static Mat stage1MotionProcess(Mat[] T, int dx, double angle) {
        Mat result = new Mat();
        for (int i = 1; i < T.length; i++) {
            T[i] = SpecialKernels.displaceKernel(T[i], -angle, dx);
        }
        result = MatrixUtils.multiply(T);
        result = SpecialKernels.displaceKernel(result, -angle, (int) (-dx * (T.length / 2)));
        return result;
    }

    /**
     * Implementation of the Intersection of Constrains based on the model <br>
     * developed in Desmos app: <br>
     * https://www.desmos.com/calculator/vdh8bzudes
     *
     * @param i1 is the intensity of the first vector
     * @param a1 angle of the first vector
     * @param i2 intensity of the second vector
     * @param a2 angle of the second vector
     * @return an array of doubles [intensity, angle]
     */
    public static double[] IoCProcess(double r1, double a1, double r2, double a2) {
        double[] IoC = new double[2];
        int d1 = (int) Math.toDegrees(a1);
        int d2 = (int) Math.toDegrees(a2);

        /*
        condition to prevent the angles from being collinear, for example 0 and 180,
        if this happens, there may be indeterminacy
        */
        if (d1 != (d2 - 180) % 360) {
            
            double x = (double) ((r1 * Math.sin(a2) - r2 * Math.sin(a1)) / Math.sin(a2 - a1));
            double y = (double) ((r2 * Math.cos(a1) - r1 * Math.cos(a2)) / Math.sin(a2 - a1));
            
            double speed=Math.sqrt(x * x + y * y);
            
            /**
             * extra condition to avoid infinite speeds
             */
            if(speed>1000){
                speed=0;
            }
            
            IoC[0] = speed;
            IoC[1] = Math.atan2(y, x);
            
        } else {
            
            IoC[0] = 0;
            IoC[1] = 0;
            
        }
        return IoC;
    }

}
