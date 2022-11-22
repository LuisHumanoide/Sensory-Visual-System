package middlewareVision.nodes.Visual.MST;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.MSTCells.MSTPolar;
import VisualMemory.MotionCell;
import spike.Location;
import generator.ProcessList;
import gui.Visualizer;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Functions;
import utils.LongSpike;
import utils.SpecialKernels;

/**
 *
 *
 */
public class MSTPolarCells extends Activity {

    Mat logM;
    //variable used in the visualizer
    static String vrow = "zero";
    boolean started = false;
    MotionCell MotionR, MotionL, MotionUP, MotionDOWN;
    Mat gaborV;
    Mat gaborH;

    public MSTPolarCells() {
        this.ID = AreaNames.MSTPolarCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);

        MotionR = new MotionCell(4);
        MotionR.setDxDt(Config.dxExpCont, Config.dtExpCont);

        MotionL = new MotionCell(4);
        MotionL.setDxDt(Config.dxExpCont, Config.dtExpCont);

        MotionUP = new MotionCell(4);
        MotionUP.setDxDt(Config.dxRotation, Config.dtRotation);

        MotionDOWN = new MotionCell(4);
        MotionDOWN.setDxDt(Config.dxRotation, Config.dtRotation);

        //size, sigma, theta, lambda, gamma
        gaborV = Imgproc.getGaborKernel(new Size(17, 17), 1.6f, 0, 0.433f, 0.8f);
        gaborH = SpecialKernels.rotateKernelRadians(gaborV, 0.5*Math.PI);

        getVrow();

    }
    
    public static void getVrow(){
        vrow="zero"; 
        
        if ((boolean) ProcessList.ProcessMap.get("V1MotionCellsNew")) {
            vrow = "v1motion";
            if ((boolean) ProcessList.ProcessMap.get("MTComponentCells")) {
                vrow = "mtComp";
                if ((boolean) ProcessList.ProcessMap.get("MTPatternCells")) {
                    vrow = "MTP";
                }
            }
        }
    }

    /**
     * This thread is using for update the visualizer in the startup
     */
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                visualize();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    );

    @Override
    public void init() {

    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                if (spike.getModality() == Modalities.VISUAL) {

                    convertToLogPolar(LGNBank.SOC[0][0].Cells[2].mat, 0);

                    expansionContractionProcess(0);

                    rotationProcess(0);

                    visualize();

                    Visualizer.lockLimit("MSTPolar");

                }

            } catch (Exception ex) {
                Logger.getLogger(MSTPolarCells.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void visualize() {
        if (Visualizer.getRow(vrow) != -1) {
            Visualizer.setImage(MSTPolar.polarSrc[0], "logPolarMST", Visualizer.getRow(vrow) + 1, Config.gaborOrientations + 2, "MSTPolar");
            Visualizer.setImage(MSTPolar.expC[0].mat, "expansion", Visualizer.getRow(vrow) + 1, Config.gaborOrientations + 3, "MSTPolar");
            Visualizer.setImage(MSTPolar.contC[0].mat, "contraction", Visualizer.getRow(vrow) + 1, Config.gaborOrientations + 4, "MSTPolar");
            Visualizer.setImage(MSTPolar.rotC[0].mat, "rotation 1", Visualizer.getRow(vrow) + 1, Config.gaborOrientations + 5, "MSTPolar");
            Visualizer.setImage(MSTPolar.invRC[0].mat, "rotation 2", Visualizer.getRow(vrow) + 1, Config.gaborOrientations + 6, "MSTPolar");
        } else {
            if (!started) {
                thread.start();
                started = true;
            }
        }
    }

    /**
     * converts a matrix in Cartesian space to non-polar space, <br>
     * since this function is experimental, the reference point is the center of
     * the image.
     *
     * @param src
     */
    void convertToLogPolar(Mat src, int eye) {
        logM = Mat.zeros(src.size(), src.type());
        Imgproc.warpPolar(src, logM, src.size(), new Point(Config.heigth / 2, Config.heigth / 2), Config.heigth / 2, Imgproc.WARP_POLAR_LINEAR);
        MSTPolar.polarSrc[eye] = logM.clone();
    }

    /*
    Some time variables that are used in the expansion and contraction process
     */
    Mat motR = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    Mat motL = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);

    Mat tempR = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    Mat tempL = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);

    Mat mask = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);

    /**
     * Expansion and Contraction Process:<br>
     * The Polar image is filtered by vertical orientation filters<br>
     * then, the detection of right and left motion is performed<br>
     * R-L and L-R subtractions are performed<br>
     * finally, the resulting matrices are then converted to Cartesian
     * coordinates
     *
     * @param eye right or left eye
     */
    void expansionContractionProcess(int eye) {
        //filter the polar matrix with a gabor filter
        Mat filtered = Functions.filter(MSTPolar.polarSrc[0], gaborV);

        //first stages of the Reitchard motion
        MotionR.cellMotionProcess(filtered, 0);
        MotionL.cellMotionProcess(filtered, Math.PI);

        //substraction stage
        Core.subtract(MotionR.mat1st, MotionL.mat1st, tempR);
        Core.subtract(MotionL.mat1st, MotionR.mat1st, tempL);

        //circle mask
        Imgproc.circle(mask, new Point(Config.heigth / 2, Config.heigth / 2), Config.heigth / 2 - 1, Scalar.all(1), -1);

        //convertion from LogPolar to Cartesian
        Imgproc.warpPolar(tempR, MSTPolar.expC[eye].mat, tempR.size(), new Point(Config.heigth / 2, Config.heigth / 2), Config.heigth / 2, Imgproc.WARP_INVERSE_MAP);
        Imgproc.warpPolar(tempL, MSTPolar.contC[eye].mat, tempL.size(), new Point(Config.heigth / 2, Config.heigth / 2), Config.heigth / 2, Imgproc.WARP_INVERSE_MAP);

        //the cartesian matrixes have some noise, multiplying by the mask helps to reduce peripheral noise
        MSTPolar.expC[eye].mat = MSTPolar.expC[eye].mat.mul(mask);
        MSTPolar.contC[eye].mat = MSTPolar.contC[eye].mat.mul(mask);

        //Gaussian blur smoothing
        Imgproc.GaussianBlur(MSTPolar.expC[eye].mat, MSTPolar.expC[eye].mat, new Size(11, 11), 1);
        Imgproc.GaussianBlur(MSTPolar.contC[eye].mat, MSTPolar.contC[eye].mat, new Size(11, 11), 1);
    }

    /*
    Some time variables that are used in the rotation process
     */
    Mat motU = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    Mat motD = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);

    Mat tempU = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);
    Mat tempD = Mat.zeros(new Size(Config.width, Config.heigth), CvType.CV_32FC1);

    /**
     * Rotational selectivity is performed, it is similar to expansion, <br>
     * only that the detected movement is from top to bottom and vice versa.
     *
     * @param eye right or left eye
     */
    void rotationProcess(int eye) {
        //filter the polar matrix with a gabor filter
        Mat filtered = Functions.filter(MSTPolar.polarSrc[0], gaborH);
        
        //first stages of the Reitchard motion
        MotionUP.cellMotionProcess(filtered, (double)(0.5 * Math.PI));
        MotionDOWN.cellMotionProcess(filtered, 1.5 * Math.PI);
        
        //substraction stage
        Core.subtract(MotionUP.mat1st, MotionDOWN.mat1st, tempU);
        Core.subtract(MotionDOWN.mat1st, MotionUP.mat1st, tempD);

        //circle mask
        Imgproc.circle(mask, new Point(Config.heigth / 2, Config.heigth / 2), Config.heigth / 2 - 1, Scalar.all(1), -1);

        //convertion from LogPolar to Cartesian
        Imgproc.warpPolar(tempU, MSTPolar.rotC[eye].mat, tempU.size(), new Point(Config.heigth / 2, Config.heigth / 2), Config.heigth / 2, Imgproc.WARP_INVERSE_MAP);
        Imgproc.warpPolar(tempD, MSTPolar.invRC[eye].mat, tempD.size(), new Point(Config.heigth / 2, Config.heigth / 2), Config.heigth / 2, Imgproc.WARP_INVERSE_MAP);

        //the cartesian matrixes have some noise, multiplying by the mask helps to reduce peripheral noise
        MSTPolar.rotC[eye].mat = MSTPolar.rotC[eye].mat.mul(mask);
        MSTPolar.invRC[eye].mat = MSTPolar.invRC[eye].mat.mul(mask);

        //Gaussian blur smoothing
        Imgproc.GaussianBlur(MSTPolar.rotC[eye].mat, MSTPolar.rotC[eye].mat, new Size(11, 11), 1);
        Imgproc.GaussianBlur(MSTPolar.invRC[eye].mat, MSTPolar.invRC[eye].mat, new Size(11, 11), 1);
    }

}
