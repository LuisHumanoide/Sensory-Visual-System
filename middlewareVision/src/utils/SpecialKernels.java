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
    public static Mat[][] GaborKernels;
    //[scale index]-[frequency index]-[parity index]-[orientation index]
    public static Mat[][][][] GaborKernels2;
    static double valueMinus = -0.15;
    static double valueMax = 0.3;
    public static ArrayList<PairFilter> ilusoryFilters;
    public static ArrayList<PairFilter> endStoppedFilters;
    static ArrayList<RF> RFs;
    public static Mat v2Kernels[];

    /**
     * *************************************************************************
     * LOAD THE KERNELS HERE
     * *************************************************************************
     */
    public static void loadKernels() {
        initRFlist();
        loadGaborFilters();
        loadEndStoppedFilters();
        getdiag45(Config.diagonalSize);
        getdiag135(Config.diagonalSize);
        loadV2Kernels();
        V4CellStructure.loadV4Structure();
    }
    static float a1 = 5f;
    static float a2 = 0.5f;

    public static void loadGaborFilters() {
        GaborKernels = new Mat[3][Config.gaborOrientations];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            float angle = i * inc;
            GaborKernels[0][i] = getGaborKernel(new Size(20, 20), sigma, 0, a1, a2, 0, CvType.CV_32F);
            GaborKernels[0][i] = rotateKernelRadians(GaborKernels[0][i], angle);
            GaborKernels[1][i] = getGaborKernel(new Size(20, 20), sigma, 0, a1, a2, 1, CvType.CV_32F);
            GaborKernels[1][i] = rotateKernelRadians(GaborKernels[1][i], angle);
        }
    }

    public static void modifyDispGabor(int disp) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            float angle = i * inc;
            GaborKernels[2][i] = displaceKernel(getGaborKernel(new Size(50, 50), sigma, angle, a1, a2, 0, CvType.CV_32F), angle, disp);
        }
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
     * initialize RF list
     */
    public static void initRFlist() {
        RFs = new ArrayList();
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
     * V2 Ilusory filters
     */
    public static void loadIlusoryFilters() {
        ilusoryFilters = new ArrayList();
        String path = "RFV2";
        String file = "ilusory1";
        loadList(path + "/" + file + ".txt");
        for (int i = 0; i < Config.gaborOrientations; i++) {
            double angle = (180 / Config.gaborOrientations) * i;
            double rangle = Math.toRadians(angle);
            RF rf1 = RFs.get(0);
            RF rf2 = RFs.get(1);
            double amp = Math.pow(rf1.getPx(), 2) + Math.pow(rf1.getPy(), 2);
            amp = Math.sqrt(amp);
            rf1.setPx((int) (amp * Math.sin(rangle)));
            rf1.setPy((int) (amp * Math.cos(rangle)));
            rf1.setAngle(angle);
            rf2.setPx((int) (-amp * Math.sin(rangle)));
            rf2.setPy((int) (-amp * Math.cos(rangle)));
            rf2.setAngle(angle);
            PairFilter pair = new PairFilter(getFilterFromRF(rf1), getFilterFromRF(rf2));
            ilusoryFilters.add(pair);
        }

        clearList();

    }

    /**
     * Load the endStoppedFilters
     */
    public static void loadEndStoppedFilters() {
        endStoppedFilters = new ArrayList();
        String path = "RFV1";
        String file = "endStop";
        loadList(path + "/" + file + ".txt");
        for (int i = 0; i < Config.gaborOrientations; i++) {
            double angle = (180 / Config.gaborOrientations) * i;
            double rangle = Math.toRadians(angle);
            RF rf1 = RFs.get(0);
            RF rf2 = RFs.get(1);
            double amp = Math.pow(rf1.getPx(), 2) + Math.pow(rf1.getPy(), 2);
            amp = Math.sqrt(amp);
            rf1.setPx((int) (amp * Math.sin(rangle)));
            rf1.setPy((int) (amp * Math.cos(rangle)));
            rf1.setAngle(angle);
            rf2.setPx((int) (-amp * Math.sin(rangle)));
            rf2.setPy((int) (-amp * Math.cos(rangle)));
            rf2.setAngle(angle);
            PairFilter pair = new PairFilter(getFilterFromRF(rf1), getFilterFromRF(rf2));
            endStoppedFilters.add(pair);
        }
        clearList();
    }

    /**
     * Load the kernels that will be used in V2 for the angular activation
     */
    public static void loadV2Kernels() {
        v2Kernels = new Mat[Config.gaborOrientations * 2];
        String path = "RFV2";
        String file = "angular";
        loadList(path + "/" + file + ".txt");
        for (int i = 0; i < Config.gaborOrientations * 2; i++) {
            double angle = (180 / Config.gaborOrientations) * i;
            double rangle = Math.toRadians(angle);
            RF rf1 = RFs.get(0);
            double amp = Math.pow(rf1.getPx(), 2) + Math.pow(rf1.getPy(), 2);
            amp = Math.sqrt(amp);
            rf1.setPx((int) (amp * Math.sin(rangle)));
            rf1.setPy((int) (amp * Math.cos(rangle)));
            rf1.setAngle(angle);
            v2Kernels[i] = getFilterFromRF(rf1);
        }
    }

    static void clearList() {
        RFs.clear();
    }

    static void loadList(String path) {
        clearList();
        String stList = FileUtils.readFile(new File(path));
        String lines[] = stList.split("\\n");
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
            RFs.add(rf);
        }
    }

    /**
     * Generate double opponent Kernel for color processing
     *
     * @param s
     * @param sigma1
     * @param sigma2
     * @param height1
     * @param height2
     * @param gamma1
     * @param gamma2
     * @param dX
     * @return
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
     * @param s
     * @param sigmaX
     * @param sigmaY
     * @param alpha
     * @return
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
     * elevates a number to the 2 pow
     *
     * @param n
     * @return
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

    public static Mat getGabor(int kernelSize, float angle) {
        return getGaborKernel(new Size(kernelSize, kernelSize), sigma * 0.05, angle, 0.5f, 0.3f, 0, CvType.CV_32F);
    }

    public static Mat get45Gabor(int kernelSize) {
        return getGabor(kernelSize, inc * 1);
    }

    public static Mat get135Gabor(int kernelSize) {
        return getGabor(kernelSize, inc * 3);
    }

    public static Mat displaceKernel(Mat kernel, double angle, int dis) {
        //https://docs.opencv.org/3.4/d4/d61/tutorial_warp_affine.html
        Point[] srcTri = new Point[3];
        srcTri[0] = new Point(0, 0);
        srcTri[1] = new Point(kernel.cols() - 1, 0);
        srcTri[2] = new Point(0, kernel.rows() - 1);

        //angle=Math.toRadians(angle);
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

    public static Mat rotateKernel(Mat kernel, double angle) {
        Mat rotationMat = Imgproc.getRotationMatrix2D(new Point(kernel.width() / 2, kernel.height() / 2), angle, 1);
        Mat rKernel = new Mat();
        Imgproc.warpAffine(kernel, rKernel, rotationMat, kernel.size());
        return rKernel;
    }

    public static Mat rotateKernel(Mat kernel, int rx, int ry, double angle) {
        Mat rotationMat = Imgproc.getRotationMatrix2D(new Point(kernel.width() / 2 + rx, kernel.height() / 2 + ry), angle, 1);
        Mat rKernel = new Mat();
        Imgproc.warpAffine(kernel, rKernel, rotationMat, kernel.size());
        return rKernel;
    }

    public static Mat rotateKernelRadians(Mat kernel, double angle) {
        return rotateKernel(kernel, Math.toDegrees(angle));
    }

    public static Mat rotateKernelRadians(Mat kernel, int rx, int ry, double angle) {
        return rotateKernel(kernel, rx, ry, Math.toDegrees(angle));
    }

}
