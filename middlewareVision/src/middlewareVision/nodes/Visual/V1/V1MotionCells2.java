package middlewareVision.nodes.Visual.V1;

import gui.FrameActivity;
import spike.Location;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.ArrayMatrix;
import matrix.FloatLabelMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MotionLabelIndex;
import utils.SpecialKernels;

/**
 *
 *
 */
public class V1MotionCells2 extends FrameActivity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    public static int nFrames = 3;
    public static int Δd = 1;
    public Mat diag45;
    public Mat diag135;
    int mWidth = Config.motionWidth;
    int mHeight = Config.motionHeight;

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V1MotionCells2() {
        this.ID = AreaNames.V1MotionCells2;
        this.namer = AreaNames.class;
        initFrames(4, 12+12);
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];
            if (spike.getModality() == Modalities.VISUAL) {
                ArrayMatrix mats = (ArrayMatrix) spike.getIntensity();
                matrix[] matrixes;
                matrixes = mats.getArrayMatrix();
                Mat[] frames = new Mat[matrixes.length];
                for (int i = 0; i < matrixes.length; i++) {
                    frames[i] = Convertor.matrixToMat(matrixes[i]);
                }
                FloatLabelMatrix motionLabels = motionDetect(frames, index);

            }

        } catch (Exception ex) {
            //Logger.getLogger(V1MotionCells2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    Mat matLabel;
    Mat mat2;

    public FloatLabelMatrix motionDetect(Mat frames[], int index) {
        matLabel = new Mat(frames[0].height(), frames[0].width(), CvType.CV_8UC3);
        FloatLabelMatrix motion = new FloatLabelMatrix(mWidth, mHeight);
        for (int x = 0; x < mWidth; x++) {
            for (int y = 0; y < mHeight; y++) {
                Mat cx = getCxMat(x, y, frames);
                Mat cy = getCyMat(x, y, frames);
                double Lf = getF45(cx);
                double Rg = getF135(cx);
                double Up = getF45(cy);
                double Dw = getF135(cy);
                //matLabel.put(y, x, new byte[]{(byte) (Lf * 255 + Dw * 200), (byte) (Rg * 255 + Dw * 200), (byte) (Up * 255)});
                motion.setLabel(x, y, new float[]{(float)Lf,(float)Rg,(float)Up,(float)Dw});

            }
        }
        /*mat2 = matLabel.clone();
        Imgproc.resize(mat2, mat2, new Size(Config.width, Config.heigth));
        frame[index].setImage(Convertor.ConvertMat2Image2(mat2), "motion labels");*/
        ortogonalMotion(index,motion);
        return motion;
    }
    
    /**
     * assign labels of ortogonal motion, thet is,..., the four labels are reduced to 2
     * @param index
     * @param motion 
     */
    public void ortogonalMotion(int index, FloatLabelMatrix motion){
        matLabel = new Mat(motion.getHeight(), motion.getWidth(), CvType.CV_8UC3);
        int labels[]=MotionLabelIndex.labelIndex(index);
        for(int x=0;x<motion.getWidth();x++){
            for(int y=0;y<motion.getHeight();y++){
                float[] labelValues=motion.getLabel(x, y).getLabel();
                float intensity1=calculateOrtogonalActivation(labelValues[labels[0]],labelValues[labels[1]]);
                float intensity2=calculateOrtogonalActivation(labelValues[labels[2]],labelValues[labels[3]]);
                matLabel.put(y, x, new byte[]{(byte) (intensity1*255), (byte) (0), (byte) (intensity2*255)});
            }
        }
        mat2 = matLabel.clone();
        Imgproc.resize(mat2, mat2, new Size(Config.width, Config.heigth));
        frame[index].setImage(Convertor.Mat2Img2(mat2), "motion labels");
        
    }
    
    /**
     * calculate the activation value of 2 preferents labels
     * @param value1
     * @param value2
     * @return 
     */
    public float calculateOrtogonalActivation(float value1, float value2){
        float activation=0;
        activation=(float) ((float) Math.sqrt(value1*value1+value2*value2)*0.7071);
        return activation;
    }

    /**
     * Cx mat
     * @param x
     * @param y
     * @param frames
     * @return 
     */
    public Mat getCxMat(int x, int y, Mat[] frames) {
        int pos = 0;
        Mat cx = Mat.zeros(new Size(nFrames, nFrames), CvType.CV_32FC1);
        for (int i = 0; i < cx.width(); i++) {
            for (int j = 0; j < cx.height(); j++) {
                double value;
                pos = x + j * Δd;
                if (pos < Config.motionHeight) {
                    value = frames[nFrames - i - 1].get(y, pos)[0];

                    cx.put(i, j, value);
                }
            }
            
        }return cx;
    }

    

    
    /**
     * Cy Mat
     * @param x
     * @param y
     * @param frames
     * @return 
     */
    public Mat getCyMat(int x, int y, Mat[] frames) {
        int pos;
        Mat cx = Mat.zeros(new Size(nFrames, nFrames), CvType.CV_32FC1);
        for (int i = 0; i < cx.width(); i++) {
            for (int j = 0; j < cx.height(); j++) {
                double value;
                pos = y + i * Δd;
                if (pos < mWidth) {
                    value = frames[nFrames - j - 1].get(pos, x)[0];
                    cx.put(i, j, value);
                }
            }
            
        }return cx;
    }

    

    
    /**
     * Convolution with 45° filter
     * @param c
     * @return 
     */
    public double getF45(Mat c) {
        Mat diff = Mat.zeros(new Size(nFrames, nFrames), CV_32F);
        Imgproc.filter2D(c, diff, CV_32F, SpecialKernels.diag45);
        Imgproc.threshold(diff, diff, 0, 1, Imgproc.THRESH_TOZERO);
        double nz = diff.get(1, 1)[0] * 5;
        return nz;

    }

    /**
     * Convolution with 135° filter
     * @param c
     * @return 
     */
    public double getF135(Mat c) {
        Mat diff = Mat.zeros(new Size(nFrames, nFrames), CV_32F);
        Imgproc.filter2D(c, diff, CV_32F, SpecialKernels.diag135);
        Imgproc.threshold(diff, diff, 0, 1, Imgproc.THRESH_TOZERO);
        double nz = diff.get(1, 1)[0] * 5;
        return nz;

    }

}
