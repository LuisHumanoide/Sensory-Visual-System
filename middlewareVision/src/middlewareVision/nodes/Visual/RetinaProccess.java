/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual;

import middlewareVision.core.nodes.Frame;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.LinkedList;
import middlewareVision.config.AreaNames;
import middlewareVision.core.nodes.GUIActivity;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import org.opencv.videoio.VideoCapture;
import spike.Location;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;

/**
 *
 * @author Luis Humanoide D Madrigal
 */
public class RetinaProccess extends GUIActivity<RetinaFrame> {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    private static final float[][] BGR_TO_LMS = {{0.4275f, 0.4990f, 0.0472f},
    {0.2206f, 0.7030f, 0.0918f},
    {0.0270f, 0.0707f, 0.9911f}};

    private static final int KERNEL_SIZE = 3;

    private static final float UPPER_KERNEL_SIGMA = 0.25f;
    private static final float LOWER_KERNEL_SIGMA = 1.25f;

    //private static final float LMM_ALPHA = 1.6f;
    //private static final float LMM_BETA = 1.5f;
    private static final float LMM_ALPHA = 3.2f;
    private static final float LMM_BETA = 3f;

    //private static final float SMLPM_ALPHA = 0.7f;
    //private static final float SMLPM_BETA = 0.6f;
    private static final float SMLPM_ALPHA = 1.2f;
    private static final float SMLPM_BETA = 1.4f;
    private static final float SMLPM_GAMMA = 0.6f;
    private static final float SMLPM_DELTA = 0.4f;

    private static final float LPM_ALPHA = 0.6f;
    private static final float LPM_BETA = 0.4f;

    private final String DIRECTORY = "SO/";

    private final String LMM_FILE_NAME = "L-M";
    private final String SMLPM_FILE_NAME = "S-L+M";
    private final String LPM_FILE_NAME = "L+M";

    private final String IMAGE_EXTENSION = ".jpg";
    private final String TEXT_EXTENSION = ".txt";

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

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public RetinaProccess() {

        this.ID = AreaNames.RetinaProccess;
        this.namer = AreaNames.class;

        frame = new RetinaFrame(this, 0);
        frame.setSize(Config.width, Config.heigth);
        
        initFrames(3,1);

        startFrame();
    }

    @Override
    public void receive(int nodeID, byte[] data) {

    }

    public void start() {
        start = true;
    }

    @Override
    public void init() {
        //SimpleLogger.log(this, "SOY LA RETINA");
        if (Config.option == Config.CAMERA) {
            capture();
        }
        if (Config.option == Config.CLICK) {
            try {
                setImage();
            } catch (IOException ex) {
                //Logger.getLogger(RetinaProccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    private void capture() {
        VideoCapture videoDevice = new VideoCapture();
        Mat src = new Mat();

        int c = 0;

        try {
            videoDevice.open(0);
            while (true) {

                videoDevice.read(src);
                cvtColor(src, src, COLOR_BGR2GRAY);
                Imgproc.resize(src, src, new Size(Config.width, Config.heigth));
                frame.setImage(Convertor.ConvertMat2Image2(src));

                LongSpike spike = new LongSpike(Modalities.VISUAL, ID, Convertor.matToBytes(src), 0);
                send(AreaNames.V1, spike.getByteArray());

                Thread.sleep(500);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    Mat src;

    /**
     * set the image in the frames
     * @throws IOException 
     */
    void setImage() throws IOException {
        BufferedImage img = frame.createImage();
        src = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        src = Convertor.bufferedImageToMat(img);
        //cvtColor(src, src, COLOR_BGR2GRAY);
        Imgproc.resize(src, src, new Size(Config.width, Config.heigth));
        BufferedImage img2=Convertor.ConvertMat2Image2(src);
        
        frame.setImage(img2);
        
        /*
        do the transductions
        */
        Mat transMat[]=transduction(img2);
        frames[0].setImage(Convertor.ConvertMat2Image(transMat[0]), "LMM");
        frames[1].setImage(Convertor.ConvertMat2Image(transMat[1]), "SMLPM");
        frames[2].setImage(Convertor.ConvertMat2Image(transMat[2]), "LPM");
        
        for(int i=0;i<transMat.length;i++){
            LongSpike spike = new LongSpike(Modalities.VISUAL, new Location(i,1), Convertor.MatToMatrix(transMat[i]), 0);
            send(AreaNames.LGN, spike.getByteArray());
        }
        

    }

    /**
     * do the transductions
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

        //Retinal Filters 
        LMM(LMS, retinaOutput[0]);
        SMLPM(LMS, retinaOutput[1]);
        LPM(LMS, retinaOutput[2]);

        return retinaOutput;
    }

    /**
     * magic
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

    /**
     * retinal filter LMM
     * @param LMS
     * @param dst 
     */
    private void LMM(LinkedList<Mat> LMS, Mat dst) {
        int rows = LMS.getFirst().rows();
        int cols = LMS.getFirst().cols();
        Mat LG = new Mat(rows, cols, CvType.CV_32FC1);
        Mat MG = new Mat(rows, cols, CvType.CV_32FC1);

        Mat upperKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.UPPER_KERNEL_SIGMA);
        Mat lowerKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.LOWER_KERNEL_SIGMA);

        Imgproc.sepFilter2D(LMS.get(0), LG, -1, upperKernel, upperKernel);
        Imgproc.sepFilter2D(LMS.get(1), MG, -1, lowerKernel, lowerKernel);

        Core.addWeighted(LG, this.LMM_ALPHA, MG, -this.LMM_BETA, 0, dst);
    }

    /**
     * retinal filter SMLPM
     * @param LMS
     * @param dst 
     */
    private void SMLPM(LinkedList<Mat> LMS, Mat dst) {
        int rows = LMS.getFirst().rows();
        int cols = LMS.getFirst().cols();
        Mat S = new Mat(rows, cols, CvType.CV_32FC1);
        Mat LPM = new Mat(rows, cols, CvType.CV_32FC1);

        Mat upperKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.UPPER_KERNEL_SIGMA);
        Mat lowerKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.LOWER_KERNEL_SIGMA);

        Core.addWeighted(LMS.get(0), this.SMLPM_GAMMA, LMS.get(1), this.SMLPM_DELTA, 0, LPM);
        Imgproc.sepFilter2D(LMS.get(2), S, -1, upperKernel, upperKernel);

        Imgproc.sepFilter2D(LPM, LPM, -1, lowerKernel, lowerKernel);

        Core.addWeighted(S, this.SMLPM_ALPHA, LPM, -this.SMLPM_BETA, 0, dst);
    }

    /**
     * Retinal filter LPM 
     * @param LMS
     * @param dst 
     */
    private void LPM(LinkedList<Mat> LMS, Mat dst) {
        Core.addWeighted(LMS.get(0), this.LPM_ALPHA, LMS.get(1), this.LPM_BETA, 0, dst);
    }

}
