package middlewareVision.nodes.Visual.LGN;

import VisualMemory.LGNCells.LGNBank;
import generator.ProcessList;
import imgio.RetinalImageIO;
import imgio.RetinalTextIO;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import gui.Visualizer;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.numSync;

/**
 *
 *
 */
public class LGNSimpleOpponentCells extends Activity {

    /**
     * ****************************************************************
     * Valores constantes
     * ***************************************************************
     */
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
     * Fin de constantes
     * ************************************************************************
     */
    Mat LMSConesL[];
    Mat LMSConesR[];
    Mat DKL_L[];
    Mat DKL_R[];
    int indexFrame = 4;

    /*
    ****************************************************************************
    Constructores y metodos para recibir información
    ****************************************************************************
     */
    /**
     * Constructor
     */
    public LGNSimpleOpponentCells() {
        this.ID = AreaNames.LGNProcess;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
        LMSConesL = new Mat[3];
        LMSConesR = new Mat[3];
        DKL_L = new Mat[3];
        DKL_R = new Mat[3];
    }

    @Override
    public void init() {
    }

    /*
    sincronizador
    recibe 3 matrices
     */
    numSync sync = new numSync(6);

    /**
     * metodo para recibir
     *
     * @param nodeID
     * @param data
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                //spike que recibe
                LongSpike spike = new LongSpike(data);
                //si es de la modalidad visual entonces acepta
                if (spike.getModality() == Modalities.VISUAL) {
                    //obtiene el indice de la locación
                    Location l = (Location) spike.getLocation();
                    //obtiene el primer valor del arreglo
                    int index = l.getValues()[0];
                    //convierte el objeto matrix serializable en una matriz de opencv y la asigna al arreglo LMNCones
                    if (index < 3) {
                        LMSConesL[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    }
                    if (index >= 3) {
                        LMSConesR[index - 3] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    }
                    //los indices recibidos se agregan al sincronizador
                    sync.addReceived(index);
                }
                //Si se completa el sincronizador
                if (sync.isComplete()) {
                    //mandar a hacer la transduccion
                    Mat DKL_L[] = transduction(LMSConesL, 0, 0);
                    LGNBank.SOC[0][0].Cells[0].mat = DKL_L[0];
                    LGNBank.SOC[0][0].Cells[1].mat = DKL_L[1];
                    LGNBank.SOC[0][0].Cells[2].mat = DKL_L[2];

                    Mat DKL_R[] = transduction(LMSConesR, 0, 0);
                    LGNBank.SOC[0][1].Cells[0].mat = DKL_R[0];
                    LGNBank.SOC[0][1].Cells[1].mat = DKL_R[1];
                    LGNBank.SOC[0][1].Cells[2].mat = DKL_R[2];
                    /*
                mostrar las imagenes procesadas
                     */
                    for (int i = 0; i < LMSConesL.length; i++) {
                        Visualizer.setImage(Convertor.Mat2Img(LGNBank.SOC[0][0].Cells[i].mat), "dkl L" + i, 2, i);
                        Visualizer.setImage(Convertor.Mat2Img(LGNBank.SOC[0][1].Cells[i].mat), "dkl R" + i, 3, i);
                        //mandar los spikes de salida a las celulas simples y doble oponentes de V1
                        LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(i, -1), 0, 0);
                        //send(AreaNames.V1SimpleCells, sendSpike.getByteArray());
                        send(AreaNames.V1DoubleOpponent, sendSpike.getByteArray());
                    }
                    LongSpike sendSpike2 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    send(AreaNames.MSTPolarCells,sendSpike2.getByteArray());

                }

            } catch (Exception ex) {
                // Logger.getLogger(LGNSimpleOpponentCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    /**
     * Este método no se usa porque no se escribe nada en archivos
     *
     * @param LMSCones
     * @param path
     * @return
     */
    public Mat[] transduction(Mat[] LMSCones, String path, int scale, int eye) {
        Mat[] DKL = transduction(LMSCones, scale, eye);

        RetinalImageIO.lmmWriter(DKL[0], path + DIRECTORY + LMM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(DKL[0], path + DIRECTORY + LMM_FILE_NAME + TEXT_EXTENSION);

        RetinalImageIO.smlpmWriter(DKL[1], path + DIRECTORY + SMLPM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(DKL[1], path + DIRECTORY + SMLPM_FILE_NAME + TEXT_EXTENSION);

        RetinalImageIO.lpmWriter(DKL[2], path + DIRECTORY + LPM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(DKL[2], path + DIRECTORY + LPM_FILE_NAME + TEXT_EXTENSION);

        return DKL;
    }

    /**
     * Hacer la transducción que le corresponde a los conos que vienen de la
     * retina
     *
     * @param LMSCones
     * @return
     */
    public Mat[] transduction(Mat[] LMSCones, int scale, int eye) {

        Mat[] DKL = {new Mat(), new Mat(), new Mat()};

        //LMS to DKL
        LMM(LMSCones, DKL[0]);
        SMLPM(LMSCones, DKL[1]);
        LPM(LMSCones, DKL[2]);

        return DKL;
    }

    /**
     * LMM
     *
     * @param LMS
     * @param dst
     */
    private void LMM(Mat[] LMS, Mat dst) {
        int rows = LMS[0].rows();
        int cols = LMS[0].cols();
        Mat LG = new Mat(rows, cols, CvType.CV_32FC1);
        Mat MG = new Mat(rows, cols, CvType.CV_32FC1);

        Mat upperKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.UPPER_KERNEL_SIGMA);
        Mat lowerKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.LOWER_KERNEL_SIGMA);

        Imgproc.sepFilter2D(LMS[0], LG, -1, upperKernel, upperKernel);
        Imgproc.sepFilter2D(LMS[1], MG, -1, lowerKernel, lowerKernel);

        Core.addWeighted(LG, this.LMM_ALPHA, MG, -this.LMM_BETA, 0, dst);
    }

    /**
     * SMLPM
     *
     * @param LMS
     * @param dst
     */
    private void SMLPM(Mat[] LMS, Mat dst) {
        int rows = LMS[0].rows();
        int cols = LMS[0].cols();
        Mat S = new Mat(rows, cols, CvType.CV_32FC1);
        Mat LPM = new Mat(rows, cols, CvType.CV_32FC1);

        Mat upperKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.UPPER_KERNEL_SIGMA);
        Mat lowerKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.LOWER_KERNEL_SIGMA);

        Core.addWeighted(LMS[0], this.SMLPM_GAMMA, LMS[1], this.SMLPM_DELTA, 0, LPM);
        Imgproc.sepFilter2D(LMS[2], S, -1, upperKernel, upperKernel);

        Imgproc.sepFilter2D(LPM, LPM, -1, lowerKernel, lowerKernel);

        Core.addWeighted(S, this.SMLPM_ALPHA, LPM, -this.SMLPM_BETA, 0, dst);
    }

    /**
     * LPM
     *
     * @param LMS
     * @param dst
     */
    private void LPM(Mat[] LMS, Mat dst) {
        Core.addWeighted(LMS[0], this.LPM_ALPHA, LMS[1], this.LPM_BETA, 0, dst);
    }

}
