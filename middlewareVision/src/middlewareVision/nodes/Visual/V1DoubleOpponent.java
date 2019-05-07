package middlewareVision.nodes.Visual;

import imgio.RetinalImageIO;
import imgio.RetinalTextIO;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import middlewareVision.core.nodes.FrameActivity;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.SpecialKernels;
import utils.numSync;

/**
 *
 *
 */
public class V1DoubleOpponent extends Activity {

    /*
    ****************************************************************************
    Constantes
    ****************************************************************************
     */
    private final int KERNEL_SIZE = 3;

    private final float LMM_ALPHA = 3.2f; // Red
    private final float LMM_BETA = 3f; // Green

    private final float SMLPM_ALPHA = 2f; // Blue
    private final float SMLPM_BETA = 2.2f; // Yellow

    private final String DIRECTORY = "DO/";

    private final String LMM_FILE_NAME = "L-M";
    private final String SMLPM_FILE_NAME = "S-L+M";

    private final String IMAGE_EXTENSION = ".jpg";
    private final String TEXT_EXTENSION = ".txt";

    /*
    ****************************************************************************
    Variables
    ****************************************************************************
    */
    /*
    MATRICES DEL LGN
    */
    Mat DKL1[];
    /*
    MATRICES PROCESADAS DOBLE OPONENTES
    */
    Mat DKL2[];
    
    /*
    ****************************************************************************
    Constructor y metodos para recibir
    ****************************************************************************
     */
    public V1DoubleOpponent() {
        this.ID = AreaNames.V1DoubleOpponent;
        this.namer = AreaNames.class;
        DKL1=new Mat[3];
        DKL2=new Mat[3];
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1DoubleOpponent");
    }

    numSync sync=new numSync(0,1,2);
    
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            int i1 = l.getValues()[0];
            
            if (spike.getModality() == Modalities.VISUAL) {
                DKL1[i1]=Convertor.matrixToMat((matrix) spike.getIntensity());
                sync.addReceived(i1);
            }
            if (sync.isComplete()) {
                transduction(DKL1);
                for(int i=0;i<3;i++){
                        LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(i), Convertor.MatToMatrix(DKL2[i]), 0);
                        send(AreaNames.V4Color, sendSpike.getByteArray());
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(V1DoubleOpponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    ****************************************************************************
    METODOS
    ****************************************************************************
     */
    /**
     * transduction
     *
     * @param DKL
     */
    public void transduction(Mat[] DKL) {
        DKL2[0]=LMM(DKL);
        DKL2[1]=SMLPM(DKL);
        DKL2[2]=DKL1[2];
    }

    /**
     *
     * @param DKL
     * @param path
     */
    public void transduction(Mat[] DKL, String path) {
        Mat lmm = LMM(DKL);
        Mat smlpm = SMLPM(DKL);

        RetinalImageIO.lmmWriter(lmm, path + DIRECTORY + LMM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(lmm, path + DIRECTORY + LMM_FILE_NAME + TEXT_EXTENSION);

        RetinalImageIO.smlpmWriter(smlpm, path + DIRECTORY + SMLPM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(smlpm, path + DIRECTORY + SMLPM_FILE_NAME + TEXT_EXTENSION);
    }

    /**
     *
     * @param DKL
     * @return
     */
    private Mat LMM(Mat[] DKL) {
        Mat LKernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, 1, -0.5, 0.5, 0.5, 2);
        Mat MKernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, -1, 0.5, 0.5, 0.5, 2);

        Mat L = new Mat(DKL[0].size(), CvType.CV_32FC1);
        Mat M = new Mat(DKL[0].size(), CvType.CV_32FC1);

        Core.multiply(DKL[0], Scalar.all(-1), M);
        Imgproc.threshold(DKL[0], L, 0, 1, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(M, M, 0, 1, Imgproc.THRESH_TOZERO);

        Imgproc.filter2D(L, L, -1, LKernel);
        Imgproc.filter2D(M, M, -1, MKernel);

        Core.addWeighted(L, LMM_ALPHA, M, LMM_BETA, 0, L);

        return L;
    }

    /**
     * 
     * @param DKL
     * @return 
     */
    private Mat SMLPM(Mat[] DKL) {
        Mat SKernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, 1, -0.5, 0.5, 0.5, 5);
        Mat LPMKernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, -1, 0.5, 0.5, 0.5, 5);

        Mat S = new Mat(DKL[1].size(), CvType.CV_32FC1);
        Mat LPM = new Mat(DKL[1].size(), CvType.CV_32FC1);

        Core.multiply(DKL[1], Scalar.all(-1), LPM);
        Imgproc.threshold(DKL[1], S, 0, 1, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(LPM, LPM, 0, 1, Imgproc.THRESH_TOZERO);

        Imgproc.filter2D(S, S, -1, SKernel);
        Imgproc.filter2D(LPM, LPM, -1, LPMKernel);

        Core.addWeighted(S, SMLPM_ALPHA, LPM, SMLPM_BETA, 0, S);

        return S;
    }

}
