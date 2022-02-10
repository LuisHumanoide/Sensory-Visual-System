/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.smallNodes;

import gui.Frame;
import spike.Location;
import gui.FrameActivity;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.ArrayMatrix;
import matrix.MatrixSerialization;
import matrix.MultiChannelMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SpecialKernels;
import utils.numSync;

/**
 *
 * @author HumanoideFilms
 */
public class V2AngularCells extends FrameActivity {

    /**
     * Initial arrays
     */
    public Mat[] ors;
    public Mat[] angleMats;
    //Mat kernels[];
    public Mat filtered[];
    /**
     * 2D array of each angular combination
     */
    public Mat v2map[][];
  
    public V2AngularCells() {
        this.ID = AreaNames.V2AngularCells;
        this.namer = AreaNames.class;
        ors = new Mat[Config.gaborOrientations];
        //set 4 frames to show with index +28
        initFrames(4, 20 + 8);
        //generateKernels();
    }

    @Override
    public void init() {
        //SimpleLogger.log(this, "SMALL NODE CortexV2");
    }

    /**
     * this is like the door to the petri net, if you get all these indexes, it opens
     */
    numSync sync = new numSync(Config.gaborOrientations);

    @Override
    public void receive(int nodeID, byte[] data) {
        LongSpike spike;
        try {
            spike = new LongSpike(data);
            /*
            if it belongs to the visual modality it is accepted 
             */
            if (spike.getModality() == Modalities.VISUAL) {
                //get the location index
                Location l = (Location) spike.getLocation();
                int index = l.getValues()[0];
                //the location index is assigned to the array index
                ors[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                //the received indexes are added to the synchronizer
                sync.addReceived(index);

            }
            /*
            if all the timing indexes were received, then it will do the process described
             */
            if (sync.isComplete()) {
                //calculates the angular activation maps
                angularProcess();
                //mixes activation maps with a certain aperture in a single matrix with the maximum pixel value operation
                mergeAngles(v2map);
                /*
                the angle maps are shown in the frames of v2
                 */
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    BufferedImage img = Convertor.ConvertMat2Image(angleMats[i]);
                    frame[i].setImage(img, "angular map V2  " + i);
                }
                /**
                 * Send a multichannel matrix with the combinations of angular activations
                 */
                for(int i=0;i<Config.gaborOrientations;i++){
                    ArrayMatrix mm;
                    mm = Convertor.MatArrayToMatrixArray(v2map[i]);
                    LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(i), mm, 0);
                    send(AreaNames.V4ShapeCells, sendSpike.getByteArray());
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(V2AngularCells.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * do the angular process
     */
    public void angularProcess() {
        filterMatrix(ors);
        angularActivation();
    }

    /**
     * merge the angular maps to reduce information to send
     *
     * @param mat
     */
    public void mergeAngles(Mat[][] mat) {
        angleMats = new Mat[Config.gaborOrientations];
        for (int i = 0; i < mat.length; i++) {
            angleMats[i] = MatrixUtils.maxSum(mat[i]);
        }
    }
    

    /**
     * *************************************
     * COSAS FEAS ABAJO DE ESTO: ************************************
     */
    float inc = (float) (Math.PI / Config.gaborOrientations);
    /**
     * Value of the width of Gabor function
     */
    float sigma = 0.47f * 2f;

    /**
     * Generate the kernels for the angular convolutions
     */
    /*
    public void generateKernels() {
        kernels = new Mat[Config.gaborOrientations * 2];
        int kernelSize = 20;
        float angle = 0;
        for (int i = 0; i < Config.gaborOrientations; i++, angle += inc) {
            //sigma, theta, lambda, gamma, psi
            kernels[i] = getGaborKernel(new Size(kernelSize, kernelSize), 0.8, angle, 10, 0.2, 0, CvType.CV_32F);
        }
        cut0matrix(kernels[0], kernelSize);
        cut1matrix(kernels[1], kernelSize);
        cut2matrix(kernels[2], kernelSize);
        cut3matrix(kernels[3], kernelSize);

    }

    /**
     * Generate the filtered matrix by applying a convolution
     *
     * @param ors
     */
    public void filterMatrix(Mat[] ors) {
        filtered = new Mat[Config.gaborOrientations * 2];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            filtered[i] = ors[i].clone();
            filtered[i + Config.gaborOrientations] = ors[i].clone();
        }
        for (int i = 0; i < Config.gaborOrientations * 2; i++) {
            Imgproc.filter2D(filtered[i], filtered[i], CV_32F, SpecialKernels.v2Kernels[i]);
            Imgproc.threshold(filtered[i], filtered[i], 0, 1, Imgproc.THRESH_TOZERO);
        }
    }
    /**
     * value used to adjust the filters for scooping
     */
    double value = -0.1;

    /**
     * cut the filter in half
     *
     * @param mat
     * @param w
     */
    /*
    public void cut0matrix(Mat mat, int w) {
        kernels[4] = kernels[0].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (i <= w / 2) {
                    kernels[0].put(i, j, value);
                }
                if (i >= w / 2) {
                    kernels[4].put(i, j, value);
                }
            }

        }
    }

    public void cut1matrix(Mat mat, int w) {
        kernels[5] = kernels[1].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (i < j) {
                    kernels[1].put(i, j, value);
                }
                if (i > j) {
                    kernels[5].put(i, j, value);
                }
            }

        }
    }

    public void cut2matrix(Mat mat, int w) {
        kernels[6] = kernels[2].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (j <= w / 2) {
                    kernels[2].put(i, j, value);
                }
                if (j >= w / 2) {
                    kernels[6].put(i, j, value);
                }
            }

        }
    }

    public void cut3matrix(Mat mat, int w) {
        kernels[7] = kernels[3].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (w - i <= j) {
                    kernels[3].put(i, j, value);
                }
                if (w - i >= j + 1) {
                    kernels[7].put(i, j, value);
                }
            }

        }
    }
    */

    /**
     * print the kernel
     *
     * @param mat
     * @param w
     */
    /*
    public void printKernel(Mat mat, int w) {
        String c = "";
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (mat.get(i, j)[0] > 0) {
                    c = c + "+" + " ";
                }
                if (mat.get(i, j)[0] < 0) {
                    c = c + "-" + " ";
                }
                if (mat.get(i, j)[0] == 0) {
                    c = c + "0" + " ";
                }
            }
            c = c + "\n";
        }
        System.out.println(c);
    }
    */
    double l3 = 0.02;

    /**
     * multiply the matrixes for generating the activation map
     */
    public void angularActivation() {
        v2map = new Mat[Config.gaborOrientations][Config.gaborOrientations * 2];
        String c = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 * 2; j++) {
                v2map[i][j] = new Mat();
                Mat vlvr = new Mat();
                Mat vlpvr = new Mat();
                Mat num = new Mat();
                Mat den = new Mat();
                Mat h = new Mat();
                Scalar dl3 = new Scalar((double) 1 / l3);
                Scalar d2l3 = new Scalar((double) 2 / l3);
                Scalar dl3_2 = new Scalar((double) 1 / (l3 * l3));
                Core.multiply(filtered[j], filtered[(i + j + 1) % 8], vlvr);
                Core.add(filtered[j], filtered[(i + j + 1) % 8], vlpvr);
                Core.add(vlpvr, d2l3, num);
                Core.multiply(vlpvr, dl3, den);
                Core.add(den, vlpvr, den);
                Core.add(den, dl3_2, den);
                Core.divide(num, den, h);
                Core.multiply(vlvr, h, v2map[i][j]);
                //Core.multiply(filtered[j], filtered[(i + j + 1) % 8], v2map[i][j]);
                Imgproc.threshold(v2map[i][j], v2map[i][j], 0, 1, Imgproc.THRESH_TOZERO);
            }
            c = c + "\n";
        }
    }

}
