/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import MiniPrograms.RF;
import java.io.File;
import java.util.ArrayList;
import middlewareVision.nodes.Visual.V4.V4CellStructure;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;

/**
 *
 * @author dmadrigal
 */
public class SpecialKernels {

    static float sigma = 0.47f * 2.5f;
    public static float inc = (float) (Math.PI / Config.gaborOrientations);
    public static Mat diag45;
    public static Mat diag135;
    static double valueMinus = -0.15;
    static double valueMax = 0.3;
    public static Mat v2Kernels[];

    /**
     * *************************************************************************
     * LOAD THE KERNELS HERE
     * *************************************************************************
     */
    public static void loadKernels() {
        loadV2Kernels();
        V4CellStructure.loadV4Structure();
    }

    /**
     * Diagonal matrix 45 degrees
     *
     * @return
     */
    public static Mat getdiag45(int size) {
        diag45 = Mat.zeros(new Size(size, size), CvType.CV_32FC1);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                diag45.put(i, j, valueMinus);
            }
        }
        for (int i = 0; i < size; i++) {
            diag45.put(i, i, valueMax);
        }
        return diag45;
    }

    /**
     * Diagonal matrix 135 degrees
     *
     * @return
     */
    public static Mat getdiag135(int size) {
        diag135 = Mat.zeros(new Size(size, size), CvType.CV_32FC1);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                diag135.put(i, j, valueMinus);
            }
        }
        for (int i = 0; i < size; i++) {
            diag135.put(3 - i - 1, i, valueMax);
        }
        return diag135;
    }

    /**
     * Make a Gaussian filter from a Receptive Field class object
     *
     * @param rf
     * @return a MAT of the filter
     */
    public static Mat getFilterFromRF(RF rf) {
        return getAdvencedGauss(new Size(rf.getSize(), rf.getSize()), rf.getIntensity(), -rf.getPy() + rf.getSize() / 2, rf.getPx() + rf.getSize() / 2, rf.getRx(), rf.getRy(), Math.toRadians(rf.getAngle() + 90));
    }

    /**
     * Cargar los filtros de selectividad angular en V2<br>
       The filter file is <b>angular.txt</b>.
     */
    public static void loadV2Kernels() {
        v2Kernels = new Mat[Config.gaborOrientations * 2];
        String path = "RFV2";
        String file = "angular";
        Mat baseKernel = getCompositeRF(path + "/" + file + ".txt");
        for (int i = 0; i < Config.gaborOrientations * 2; i++) {
            double angle = (180 / Config.gaborOrientations) * i;
            double rangle = Math.toRadians(angle);
            v2Kernels[i] = SpecialKernels.rotateKernelRadians(baseKernel, rangle);
        }
    }

    /**
     * Obtain the composite filter, product of the sum of Gaussians created in a file.
     *
     * @param path path of the file
     * @return an OpenCV mat corresponding to the filter
     */
    static Mat getCompositeRF(String path) {
        String stList = FileUtils.readFile(new File(path));
        String lines[] = stList.split("\\n");
        ArrayList<Mat> kernelList = new ArrayList();
        for (String st : lines) {
            String values[] = st.split(" ");
            RF rf = new RF(Double.parseDouble(values[0]),
                    Double.parseDouble(values[1]),
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]),
                    Double.parseDouble(values[4]),
                    Double.parseDouble(values[5]),
                    values[6],
                    Integer.parseInt(values[7]));
            Mat kernel = new Mat();
            kernel = getAdvencedGauss(new Size(rf.size, rf.size), rf.intensity,
                    -rf.py + rf.size / 2, rf.px + rf.size / 2, rf.rx, rf.ry,
                    Math.toRadians(rf.angle + 90));
            kernelList.add(kernel);
        }
        Mat compKernel = Mat.zeros(kernelList.get(0).size(), CvType.CV_32FC1);
        for (Mat kn : kernelList) {
            Core.add(compKernel, kn, compKernel);
        }
        return compKernel;
    }

    /**
     * Generate double opponent Kernel for color processing
     *
     * @param s size of the kernel
     * @param sigma1 sigma (size) of the first Gaussian
     * @param sigma2 sigma of the second Gaussian
     * @param height1 height of the first Gaussian
     * @param height2 height of the second Gaussian
     * @param gamma1 gamma of the first Gaussian
     * @param gamma2 gamma of the second Gaussian
     * @param dX displacement of the seconD Gaussian
     * @return an OpenCV matrix with the difference of Gaussian filter
     */
    public static Mat getDoubleOpponentKernel(Size s, double sigma1, double sigma2, double height1, double height2, double gamma1, double gamma2, double dX) {
        Mat m = new Mat(s, CvType.CV_32FC1);

        double[] kernel = new double[(int) (s.height * s.width)];
        double div = 0;
        int p = 0;
        int cX = (int) (s.width / 2);
        int cY = (int) (s.height / 2);
        int r = 0;

        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                kernel[p] = height1 * Math.exp(-((Math.pow(j - cX + dX, 2) + (Math.pow(gamma1, 2) * Math.pow(i - cY, 2))) / (2 * Math.pow(sigma1, 2))));
                kernel[p] += height2 * Math.exp(-((Math.pow(j - cX - dX, 2) + (Math.pow(gamma2, 2) * Math.pow(i - cY, 2))) / (2 * Math.pow(sigma2, 2))));
                div += Math.abs(kernel[p]);
                p++;
            }
        }

        m.put(0, 0, kernel);
        Core.divide(m, Scalar.all(div), m);
        for (int i = 0; i < kernel.length; i++) {
            r += kernel[i];
        }
        return m;
    }

    /**
     * Get double opponent kernel
     *
     * @param s
     * @param sigmaX1
     * @param sigmaY1
     * @param sigmaX2
     * @param sigmaY2
     * @param height1
     * @param height2
     * @param dX
     * @return
     */
    public static Mat getOtherDoubleOpponentKernel(Size s, double sigmaX1, double sigmaY1, double sigmaX2, double sigmaY2, double height1, double height2, double dX) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        double A = 0;
        double B = 0;
        int p = 0;
        int cX = (int) (s.width / 2);
        int cY = (int) (s.height / 2);

        height1 *= 2 * Math.PI * sigmaX1 * sigmaY1;
        height2 *= 2 * Math.PI * sigmaX2 * sigmaY2;

        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                A = Math.pow((j - cX + dX), 2) / (2 * Math.pow(sigmaX1, 2));
                B = Math.pow(i - cY, 2) / (2 * Math.pow(sigmaY1, 2));
                kernel[p] = Math.exp(-(A + B)) / height1;

                A = Math.pow((j - cX - dX), 2) / (2 * Math.pow(sigmaX2, 2));
                B = Math.pow(i - cY, 2) / (2 * Math.pow(sigmaY2, 2));
                kernel[p] += Math.exp(-(A + B)) / height2;

                p++;

            }
        }
        m.put(0, 0, kernel);

        return m;
    }

    /**
     * Get Sine Kernel<br>
     * Not used in this software, but was included in Daniel program
     * @param s
     * @param frec
     * @return 
     */
    public static Mat getSineKernel(Size s, double frec) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        int p = 0;
        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                kernel[p++] = Math.sin(frec * j);
            }
        }
        m.put(0, 0, kernel);
        return m;
    }

    /**
     * Get Cosine kernel, not used in this software
     * @param s
     * @param frec
     * @return 
     */
    public static Mat getCosKernel(Size s, double frec) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        int p = 0;
        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                kernel[p++] = Math.cos(frec * j);
            }
        }
        m.put(0, 0, kernel);
        return m;
    }

    /**
     * Get a simplified Gaussian filter
     *
     * @param s is the size of the kernel
     * @param sigmaX is the length in x
     * @param sigmaY is the length in y
     * @param alpha is the intensity or amplitude
     * @return a OpenCV Mat of a Gaussian filter
     */
    public static Mat getGauss(Size s, double sigmaX, double sigmaY, double alpha) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        double A = 0;
        double B = 0;
        int p = 0;
        int cX = (int) (s.width / 2);
        int cY = (int) (s.height / 2);

        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                A = Math.pow((j - cX), 2) / (2 * Math.pow(sigmaX, 2));
                B = Math.pow(i - cY, 2) / (2 * Math.pow(sigmaY, 2));
                kernel[p] = Math.exp(-(A + B)) * alpha;
                p++;

            }
        }

        m.put(0, 0, kernel);
        return m;
    }

    /**
     * Elevates a number to the 2 pow
     *
     * @param n the number
     * @return the number elevate to square pow
     */
    public static double to2(double n) {
        return Math.pow(n, 2);
    }

    /**
     * Get a 2D Gaussian with the complete parameters
     *
     * @param s size of the kernel
     * @param A intensity or amplitude
     * @param x0 center x
     * @param y0 center y
     * @param sigmax width x
     * @param sigmay width y
     * @param theta angle of rotation
     * @return a new Gaussian kernel
     */
    public static Mat getAdvencedGauss(Size s, double A, double x0, double y0, double sigmax, double sigmay, double theta) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        double a = to2(Math.cos(theta)) / (2 * to2(sigmax)) + to2(Math.sin(theta)) / (2 * to2(sigmay));
        double b = -Math.sin(2 * theta) / (4 * to2(sigmax)) + Math.sin(2 * theta) / (4 * to2(sigmay));
        double c = to2(Math.sin(theta)) / (2 * to2(sigmax)) + to2(Math.cos(theta)) / (2 * to2(sigmay));
        double s1 = 0;
        double s2 = 0;
        double cc = 0;
        int p = 0;
        for (int x = 0; x < s.height; x++) {
            for (int y = 0; y < s.width; y++) {
                s1 = (x - x0);
                s2 = (y - y0);
                cc = a * to2(s1) + 2 * b * s1 * s2 + c * to2(s2);
                kernel[p] = A * Math.exp(-cc);
                p++;
            }
        }
        m.put(0, 0, kernel);
        return m;
    }

    /**
     * Method for displacing an openCV matrix<br>
     * further information here: <br>
     * https://docs.opencv.org/3.4/d4/d61/tutorial_warp_affine.html
     * @param kernel is the original or source Mat
     * @param angle is angle at which the displacement is to be made
     * @param dis is the length to be displaced
     * @return a displaced OpenCV matrix
     */
    public static Mat displaceKernel(Mat kernel, double angle, int dis) {
        Point[] srcTri = new Point[3];
        srcTri[0] = new Point(0, 0);
        srcTri[1] = new Point(kernel.cols() - 1, 0);
        srcTri[2] = new Point(0, kernel.rows() - 1);

        double dx = dis * Math.cos(angle);
        double dy = dis * Math.sin(angle);

        Point[] dstTri = new Point[3];
        dstTri[0] = new Point(dx, dy);
        dstTri[1] = new Point(kernel.cols() - 1 + dx, dy);
        dstTri[2] = new Point(dx, kernel.rows() - 1 + dy);

        Mat warpMat = Imgproc.getAffineTransform(new MatOfPoint2f(srcTri), new MatOfPoint2f(dstTri));

        Mat warpDst = Mat.zeros(kernel.rows(), kernel.cols(), kernel.type());
        Imgproc.warpAffine(kernel, warpDst, warpMat, warpDst.size());

        return warpDst;
    }

    /**
     * Rotate a OpenCV matrix in degrees<br>
     * the displacement is made from the center
     * @param kernel matrix to rotate
     * @param angle angle in degrees
     * @return a rotated OpenCV Mat
     */
    public static Mat rotateKernel(Mat kernel, double angle) {
        Mat rotationMat = Imgproc.getRotationMatrix2D(new Point(kernel.width() / 2, kernel.height() / 2), angle, 1);
        Mat rKernel = new Mat();
        Imgproc.warpAffine(kernel, rKernel, rotationMat, kernel.size());
        return rKernel;
    }

    /**
     * Rotate a OpenCV matrix in degrees<br>
     * the displacement is done from a user-defined point
     * @param kernel matrix to rotate or source matrix
     * @param rx point in x
     * @param ry point in y
     * @param angle angle in degrees
     * @return  a rotated OpenCV Mat
     */
    public static Mat rotateKernel(Mat kernel, int rx, int ry, double angle) {
        Mat rotationMat = Imgproc.getRotationMatrix2D(new Point(kernel.width() / 2 + rx, kernel.height() / 2 + ry), angle, 1);
        Mat rKernel = new Mat();
        Imgproc.warpAffine(kernel, rKernel, rotationMat, kernel.size());
        return rKernel;
    }

    /**
     * Rotate a OpenCV matrix in radians<br>
     * the displacement is made from the center
     * @param kernel matrix to rotate
     * @param angle angle in degrees
     * @return a rotated OpenCV Mat
     */
    public static Mat rotateKernelRadians(Mat kernel, double angle) {
        return rotateKernel(kernel, Math.toDegrees(angle));
    }

    /**
     * 
     * Rotate a OpenCV matrix in radians<br>
     * the displacement is done from a user-defined point
     * @param kernel matrix to rotate or source matrix
     * @param rx point in x
     * @param ry point in y
     * @param angle angle in degrees
     * @return  a rotated OpenCV Mat
     */
    public static Mat rotateKernelRadians(Mat kernel, int rx, int ry, double angle) {
        return rotateKernel(kernel, rx, ry, Math.toDegrees(angle));
    }

}
