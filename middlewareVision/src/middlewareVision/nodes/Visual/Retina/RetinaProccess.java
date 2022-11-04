/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.Retina;

import generator.ProcessList;
import gui.Frame;
import gui.GUI;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.LinkedList;
import middlewareVision.config.AreaNames;
import gui.Visualizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import org.opencv.calib3d.StereoBM;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_8UC1;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import spike.Location;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;

/**
 *
 * @author Luis Humanoide D Madrigal
 */
public class RetinaProccess extends Activity {

    private static final float[][] BGR_TO_LMS = {{0.4275f, 0.4990f, 0.0472f},
    {0.2206f, 0.7030f, 0.0918f},
    {0.0270f, 0.0707f, 0.9911f}};

    /**
     * *************************************************************************
     * VARIABLES
     * *************************************************************************
     */
    public Frame[] frames;

    /**
     * initialize the array of frames
     *
     * @param numFrames number of frames
     * @param index index for the layout manager
     */
    public void initFrames(int numFrames, int index) {
        frames = new Frame[numFrames];
        for (int i = 0; i < numFrames; i++) {
            frames[i] = new Frame(i + index);
            frames[i].setSize(Config.width, Config.heigth);
        }
    }

    /*
    * Cambiar a true para que inicie todo
     */
    private boolean start = false;

    GUI gui;

    public RetinaProccess() {

        this.ID = AreaNames.RetinaProccess;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);

        initFrames(3, 1);
        gui = new GUI(this);
        gui.setVisible(true);
        thread.start();
    }
    boolean ready = false;
    Thread thread = new Thread() {
        public void run() {
            ready = true;
            gui.ret.createImage(0, true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RetinaProccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    @Override
    public void receive(int nodeID, byte[] data) {

    }

    public void start() {
        start = true;
    }

    @Override
    public void init() {
        //SimpleLogger.log(this, "SOY LA RETINA");

    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    public Mat src;
    BufferedImage img;

    /**
     * set the image in the frames
     *
     * @throws IOException
     */
    public void setImage(BufferedImage img, BufferedImage img2) {
        Mat transMat[] = transduction(img);
        Visualizer.setImage(Convertor.Mat2Img(transMat[0]), "L L", 0, 0);
        Visualizer.setImage(Convertor.Mat2Img(transMat[1]), "M L", 0, 1);
        Visualizer.setImage(Convertor.Mat2Img(transMat[2]), "S L", 0, 2);
        Visualizer.setImage(img, "Original L", 0, 3);

        Mat transMat2[] = transduction(img2);
        Visualizer.setImage(Convertor.Mat2Img(transMat2[0]), "L R", 1, 0);
        Visualizer.setImage(Convertor.Mat2Img(transMat2[1]), "M R", 1, 1);
        Visualizer.setImage(Convertor.Mat2Img(transMat2[2]), "S R", 1, 2);
        Visualizer.setImage(img2, "Original R", 1, 3);

        Mat m1 = transMat[2].clone();
        Mat m2 = transMat2[2].clone();
        m1.convertTo(m1, CV_8UC1);
        m2.convertTo(m2, CV_8UC1);
        Mat diffMat = new Mat();

        StereoBM stereo = StereoBM.create(16, 15);
        stereo.setTextureThreshold(15);
        stereo.setUniquenessRatio(2);
        stereo.compute(m1, m2, diffMat);

        Visualizer.setImage(Convertor.Mat2Img2(diffMat), "stereo diff", 1, 4);

        if (ready) {
            for (int i = 0; i < 3; i++) {
                LongSpike spike = new LongSpike(Modalities.VISUAL, new Location(i, 1), Convertor.MatToMatrix(transMat[i]), 0);
                LongSpike spike2 = new LongSpike(Modalities.VISUAL, new Location(i + 3, 1), Convertor.MatToMatrix(transMat2[i]), 0);
                try {
                    send(AreaNames.LGNSimpleOpponentCells, spike.getByteArray());
                    send(AreaNames.LGNSimpleOpponentCells, spike2.getByteArray());
                    send(AreaNames.BasicMotion, spike.getByteArray());
                } catch (IOException ex) {
                    Logger.getLogger(RetinaProccess.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    /**
     * do the transductions
     *
     * @param img
     * @return
     */
    public Mat[] transduction(BufferedImage img) {

        Mat[] retinaOutput = {new Mat(), new Mat(), new Mat()};

        //Send Image to OpenCV
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        mat.convertTo(mat, CvType.CV_32FC3);

        //Normalization
        Core.divide(mat, Scalar.all(255), mat);

        //Receptors
        calculateReceptorActivation(mat, mat, this.BGR_TO_LMS);

        LinkedList<Mat> LMS = new LinkedList<>();
        Core.split(mat, LMS);

        retinaOutput[0] = LMS.get(0);
        retinaOutput[1] = LMS.get(1);
        retinaOutput[2] = LMS.get(2);

        return retinaOutput;
    }

    /**
     * magic
     *
     * @param src
     * @param dest
     * @param M
     */
    private void calculateReceptorActivation(Mat src, Mat dest, float[][] M) {
        if (src.channels() != 3) {
            return;
        }

        int rows = src.rows();
        int cols = src.cols();
        int channels = src.channels();

        Mat m = new Mat(src.rows(), src.cols(), CvType.CV_32FC3);
        float[] imgData = new float[rows * cols * channels];
        float[] dstData = new float[imgData.length];

        src.get(0, 0, imgData);

        float[] colorData = new float[3];
        int p = 0;

        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.cols(); j++) {
                colorData[2] = imgData[p];
                colorData[1] = imgData[p + 1];
                colorData[0] = imgData[p + 2];
                dstData[p++] = colorData[0] * M[0][0] + colorData[1] * M[0][1] + colorData[2] * M[0][2];
                dstData[p++] = colorData[0] * M[1][0] + colorData[1] * M[1][1] + colorData[2] * M[1][2];
                dstData[p++] = colorData[0] * M[2][0] + colorData[1] * M[2][1] + colorData[2] * M[2][2];
            }
        }

        dest.put(0, 0, dstData);
    }


}
