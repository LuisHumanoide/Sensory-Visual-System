package middlewareVision.nodes.Visual.V1;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.V1Cells.V1Bank;
import generator.ProcessList;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.SpecialKernels;
import utils.numSync;

/**
 *
 *
 */
public class V1DoubleOpponent extends Activity {

    /*
    ****************************************************************************
    Constants
    ****************************************************************************
     */
    private final int KERNEL_SIZE = 5;

    private final float LMM_ALPHA = 3.2f; // Red
    private final float LMM_BETA = 3f; // Green

    private final float SMLPM_ALPHA = 2f; // Blue
    private final float SMLPM_BETA = 2.2f; // Yellow

    int indexFrame = 8;

    /*
    ****************************************************************************
    Constructor y metodos para recibir
    ****************************************************************************
     */
    public V1DoubleOpponent() {
        this.ID = AreaNames.V1DoubleOpponent;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);

                if (spike.getModality() == Modalities.VISUAL) {

                    Mat DKL_L[] = {LGNBank.SOC[0][0].Cells[0].mat,
                        LGNBank.SOC[0][0].Cells[1].mat,
                        LGNBank.SOC[0][0].Cells[2].mat
                    };
                    Mat DKL_R[] = {LGNBank.SOC[0][1].Cells[0].mat,
                        LGNBank.SOC[0][1].Cells[1].mat,
                        LGNBank.SOC[0][1].Cells[2].mat
                    };
                    transduction(DKL_L, 0);
                    transduction(DKL_R, 1);

                    for (int i = 0; i < 3; i++) {
                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.DOC[0][0].Cells[i].mat), "dkl' L", 4, i);
                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.DOC[0][1].Cells[i].mat), "dkl' R", 5, i);
                    }

                    LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    send(AreaNames.V4Color, sendSpike.getByteArray());
                    send(AreaNames.V1SimpleCells, sendSpike.getByteArray());
                }

            } catch (Exception ex) {
                //Logger.getLogger(V1DoubleOpponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Perform the transduction process
     *
     * @param DKL
     */
    public void transduction(Mat[] DKL, int eye) {
        V1Bank.DOC[0][eye].Cells[0].mat = LMM(DKL);
        V1Bank.DOC[0][eye].Cells[1].mat = SMLPM(DKL);
        V1Bank.DOC[0][eye].Cells[2].mat = LGNBank.SOC[0][eye].Cells[2].mat.clone();
    }

    /**
     * Perform the LMM Process
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
     * Perform SMPPM Process Read Madrigal Thesis for further details
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
