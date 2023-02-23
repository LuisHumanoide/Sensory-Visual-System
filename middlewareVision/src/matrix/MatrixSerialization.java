/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

import java.util.ArrayList;
import org.opencv.core.Mat;

/**
 *
 * @author Luis Humanoide
 */
public class MatrixSerialization {

    /**
     * Convert OpenCV Mat (one channel) to a serializable matrix object
     * @param mat
     * @return 
     */
    public static matrix serializeMatrix(Mat mat) {
        matrix m = new matrix(mat.width(), mat.height(), mat.type());
        for (int i = 0; i < mat.width(); i++) {
            for (int j = 0; j < mat.height(); j++) {
                m.setValue(i, j, (float) mat.get(j, i)[0]);
            }
        }
        return m; 
    }

    /**
     * Convert an OpenCV Mat array to a serializable object for sending
     * @param mat
     * @return 
     */
    public static MultiChannelMatrix serializeMatrixArray(Mat[] mat) {
        float[] floatArray=new float[mat.length];
        MultiChannelMatrix m = new MultiChannelMatrix(mat[0].width(), mat[0].height(), mat[0].type());
        for (int i = 0; i < mat[0].width(); i++) {
            for (int j = 0; j < mat[0].height(); j++) {
                
                for(int k=0;k<mat.length;k++){
                    floatArray[k]=(float) mat[k].get(j, i)[0];
                }
                m.setValue(i, j, floatArray);
            }
        }
        return m;
    }

    /**
     * Convert received serializable matrix to a OpenCV matrix
     * @param m
     * @return an OpenCV Mat of 1 channel
     */
    public static Mat deSerializeMatrix(matrix m) {
        Mat mat = new Mat(m.getHeight(), m.getWidth(), m.getType());
        for (int i = 0; i < m.getWidth(); i++) {
            for (int j = 0; j < m.getHeight(); j++) {
                mat.put(j, i, m.getValue(i, j));
            }
        }
        return mat;
    }

}
