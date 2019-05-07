/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 *
 * @author HumanoideFilms
 */
public class MatrixUtils {

    /**
     * create a matrix with the maximun value of an array of matrixes
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

}
