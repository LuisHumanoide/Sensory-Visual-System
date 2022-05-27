/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import VisualMemory.Cell;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.getStructuringElement;

/**
 *
 * @author HumanoideFilms
 */
public class MatrixUtils {

    /**
     * create a matrix with the maximum value of an array of matrixes
     *
     * @param mat
     * @return a mat of doubles
     */
    public static Mat maxSum(Mat... mat) {

        Mat result = Mat.zeros(mat[0].height(), mat[0].width(), CvType.CV_32FC1);
        ArrayList<Double> values = new ArrayList<>();

        for (int x = 0; x < mat[0].height(); x++) {
            for (int y = 0; y < mat[0].width(); y++) {
                for (int i = 0; i < mat.length; i++) {
                    values.add(mat[i].get(x, y)[0]);
                }
                result.put(x, y, maximun(values));
                values.clear();

            }
        }
        return result;
    }

    public static Mat elementSum(Mat[] mat, double[] w) {

        Mat result = Mat.zeros(mat[0].height(), mat[0].width(), CvType.CV_32FC1);

        for (int x = 0; x < mat[0].height(); x++) {
            for (int y = 0; y < mat[0].width(); y++) {
                double s = 0;
                for (int i = 0; i < mat.length; i++) {
                    s = s + mat[i].get(x, y)[0];
                }
                result.put(x, y, s);
                utils.Msg.print("sum " + s);

            }
        }
        return result;
    }

    /**
     * Create a matrix with the maximum value from an ArrayList of OpenCV Mar
     *
     * @param mat is the ArrayList of OpenCV Mats
     * @return an OpenCV Mat with the maximum values of each Mat
     */
    public static Mat maxSum(ArrayList<Mat> matL) {
        Mat[] matArray = new Mat[matL.size()];
        for (int i = 0; i < matL.size(); i++) {
            matArray[i] = matL.get(i);
        }
        return maxSum(matArray);
    }

    public static Mat maxSum(Cell... mat) {

        Mat result = Mat.zeros(mat[0].mat.height(), mat[0].mat.width(), CvType.CV_32FC1);
        ArrayList<Double> values = new ArrayList<>();

        for (int x = 0; x < mat[0].mat.height(); x++) {
            for (int y = 0; y < mat[0].mat.width(); y++) {
                for (int i = 0; i < mat.length; i++) {
                    values.add(mat[i].mat.get(x, y)[0]);
                }
                result.put(x, y, maximun(values));
                values.clear();

            }
        }
        return result;
    }

    /**
     * find the maximun double in a list
     *
     * @param d
     * @return
     */
    public static double maximun(ArrayList<Double> d) {
        double max = 0;
        for (double ds : d) {
            if (ds > max) {
                max = ds;
            }
        }
        return max;
    }

    public static Mat multiply(Mat[] mat) {
        Mat mul = Mat.zeros(mat[0].width(), mat[0].height(), CvType.CV_32FC1);
        Core.add(mul, new Scalar(1), mul);
        Core.multiply(mul, mat[0], mul);
        for (int i = 1; i < mat.length; i++) {
            Core.multiply(mul, mat[i], mul);
        }
        return mul;
    }

    public static Mat multiply(Cell[] mat) {
        Mat mul = Mat.zeros(mat[0].mat.width(), mat[0].mat.height(), CvType.CV_32FC1);
        Core.add(mul, new Scalar(1), mul);
        Core.multiply(mul, mat[0].mat, mul);
        for (int i = 1; i < mat.length; i++) {
            Core.multiply(mul, mat[i].mat, mul);
        }
        return mul;
    }

    public static Mat sum(Mat[] mat, int w, int bias) {
        Mat sum = Mat.zeros(mat[0].width(), mat[0].height(), CvType.CV_32FC1);
        for (int i = 0; i < mat.length; i++) {
            Core.patchNaNs(mat[i], 0.0);
            Core.addWeighted(sum, 1, mat[i], w, 0, sum);
        }
        Core.add(sum, Scalar.all(bias), sum);
        return sum;
    }

    public static Mat sum(Mat[] mat, double[] w) {
        Mat sum = Mat.zeros(mat[0].width(), mat[0].height(), CvType.CV_32FC1);
        for (int i = 0; i < mat.length; i++) {
            Core.patchNaNs(mat[i], 0.0);
            Core.addWeighted(sum, 1, mat[i], w[i], 0, sum);
        }
        return sum;
    }

    public static Mat sum(double w, double bias, int pow, Mat... mat) {
        Mat sum = Mat.zeros(mat[0].width(), mat[0].height(), CvType.CV_32FC1);
        for (int i = 0; i < mat.length; i++) {
            Mat powm = new Mat();
            Core.pow(mat[i], pow, powm);
            Core.addWeighted(sum, 1, powm, w, 0, sum);
        }
        Core.add(sum, Scalar.all(bias), sum);
        return sum;
    }

    public static Mat[] basicDilate(Cell[] mats, int blurSize, int kernelSize) {
        Mat blur[] = new Mat[mats.length];
        Mat element = getStructuringElement(MORPH_RECT,
                new Size(2 * blurSize + 1, 2 * blurSize + 1),
                new Point(blurSize, blurSize));
        for (int i = 0; i < mats.length; i++) {
            blur[i] = mats[i].mat.clone();
            Imgproc.dilate(blur[i], blur[i], element);
            //Imgproc.dil
            //Imgproc.GaussianBlur(blur[i], blur[i], new Size(kernelSize, kernelSize), blurSize);
        }
        return blur;
    }

}
