/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import matrix.MatrixSerialization;
import matrix.matrix;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32FC3;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Luis Humanoide
 */
public class Convertor {

    public static BufferedImage ConvertMat2Image2(Mat mat) {
        MatOfByte bytes = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, bytes);
        byte[] byteArray = bytes.toArray();
        BufferedImage image = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            image = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return image;
    }

    /**
     * convert bytes to mat
     *
     * @param data
     * @param size
     * @param type
     * @return
     */
    public static Mat bytesToMat(byte[] data, Size size, int type) {
        Mat receive = new Mat(size, type);
        receive.put(0, 0, data);
        return receive;
    }

    /**
     * convert mat to bytes
     *
     * @param src
     * @return
     */
    public static byte[] matToBytes(Mat src) {
        byte[] return_buff = new byte[(int) (src.total()
                * src.channels())];
        src.get(0, 0, return_buff);
        return return_buff;
    }

    public static matrix MatToMatrix(Mat src) {
        return MatrixSerialization.serializeMatrix(src);
    }
    
    public static Mat matrixToMat(matrix m){
        return MatrixSerialization.deSerializeMatrix(m);
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    public static Mat bufferedImageToMat2(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8U);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    /**
     * convert double mat to bytes
     *
     * @param src
     * @return
     */
    public static byte[] imageToBytes(BufferedImage img) {
        return matToBytes(bufferedImageToMat2(img));
    }

    public static Mat bytesToDoubleMat(byte[] data, Size size, int type) {
        Mat receive = new Mat(size, type);
        receive.put(0, 0, data);
        receive.convertTo(receive, CV_32FC3, 1.f / 255);
        return receive;
    }

    /**
     * Convert a matrix to image without multiplying by 255
     *
     * @param mat
     * @return
     */
    public static BufferedImage ConvertMat2Image(Mat mat1) {
        Mat mat=mat1.clone();
        MatOfByte bytes = new MatOfByte();
        Scalar alpha = new Scalar(255);
        Core.multiply(mat, alpha, mat);
        Imgcodecs.imencode(".jpg", mat, bytes);
        byte[] byteArray = bytes.toArray();
        BufferedImage image = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            image = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return image;
    }

}
