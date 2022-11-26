package middlewareVision.nodes.Visual.LGN;

import VisualMemory.LGNCells.LGNBank;
import generator.ProcessList;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import gui.Visualizer;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;
import utils.numSync;

/**
 *
 *
 */
public class LGNSimpleOpponentCells extends Activity {

    Mat LMSConesL[];
    Mat LMSConesR[];
    Mat DKL_L[];
    Mat DKL_R[];
    int indexFrame = 4;
    String labels[]={"D=L-M","K=S-(L+M)","L=L+M"};

    public LGNSimpleOpponentCells() {
        this.ID = AreaNames.LGNSimpleOpponentCells;
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
    sync
    recies 6 maps
     */
    numSync sync = new numSync(6);

    /**
     * receive method
     *
     * @param nodeID
     * @param data
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                //input spike
                LongSpike spike = new LongSpike(data);
                //if is from modality VISUAL then it accept it
                if (spike.getModality() == Modalities.VISUAL) {
                    //obtain the location index
                    Location l = (Location) spike.getLocation();
                    //obtain the first value of the array
                    int index = l.getValues()[0];
                    //converts the serializable matrix object to an opencv matrix and assigns it to the LMNCones array
                    if (index < 3) {
                        LMSConesL[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    }
                    if (index >= 3) {
                        LMSConesR[index - 3] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    }
                    //received indexes are added to the synchronizer
                    sync.addReceived(index);
                }
                //If the synchronizer is completed
                if (sync.isComplete()) {
                    //send for transduction
                    Mat DKL_L[] = transduction(LMSConesL, 0, 0);
                    LGNBank.SOC[0][0].Cells[0].mat = DKL_L[0];
                    LGNBank.SOC[0][0].Cells[1].mat = DKL_L[1];
                    LGNBank.SOC[0][0].Cells[2].mat = DKL_L[2];

                    Mat DKL_R[] = transduction(LMSConesR, 0, 0);
                    LGNBank.SOC[0][1].Cells[0].mat = DKL_R[0];
                    LGNBank.SOC[0][1].Cells[1].mat = DKL_R[1];
                    LGNBank.SOC[0][1].Cells[2].mat = DKL_R[2];
                    /*
                        display the processed images
                     */
                    for (int i = 0; i < LMSConesL.length; i++) {
                        Visualizer.setImage(Convertor.Mat2Img(LGNBank.SOC[0][0].Cells[i].mat), "Simple Opponent "+labels[i]+" Left" , 2, i);
                        Visualizer.setImage(Convertor.Mat2Img(LGNBank.SOC[0][1].Cells[i].mat), "Simple Opponent "+labels[i]+" Right" , 3, i);

                    }
                    //send the output spikes to the single and double opponent cells of V1
                    LongSpike sendSpike = new LongSpike(Modalities.VISUAL, 0, 0, 0);
                    send(AreaNames.V1DoubleOpponent, sendSpike.getByteArray());
                    //send the a control spike to MST cells
                    LongSpike sendSpike2 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    send(AreaNames.MSTPolarCells, sendSpike2.getByteArray());

                }

            } catch (Exception ex) {
                // Logger.getLogger(LGNSimpleOpponentCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * To make the transduction that corresponds to the cones that come from the
     * retina retina
     *
     * @param LMSCones
     * @return
     */
    public Mat[] transduction(Mat[] LMSCones, int scale, int eye) {

        Mat[] DKL = {new Mat(), new Mat(), new Mat()};

        //LMS to DKL
        //D=L-M
        LMM(LMSCones, DKL[0]);
        //K=S-(L+M)
        SMLPM(LMSCones, DKL[1]);
        //L=L+M
        LPM(LMSCones, DKL[2]);

        return DKL;
    }

    /**
     * L-M
     *
     * @param LMS
     * @param dst
     */
    private void LMM(Mat[] LMS, Mat dst) {
        int rows = LMS[0].rows();
        int cols = LMS[0].cols();
        Mat LG = new Mat(rows, cols, CvType.CV_32FC1);
        Mat MG = new Mat(rows, cols, CvType.CV_32FC1);

        if (Config.LGNmethod == 1) {
            LG = Functions.filter2(LMS[0], LGNBank.LMM_L_kernel);
            MG = Functions.filter2(LMS[1], LGNBank.LMM_M_kernel);
            Core.add(LG, MG, dst);
        }
        if (Config.LGNmethod == 2) {
            Imgproc.sepFilter2D(LMS[0], LG, -1, LGNBank.upperKernel, LGNBank.upperKernel);
            Imgproc.sepFilter2D(LMS[1], MG, -1, LGNBank.lowerKernel, LGNBank.lowerKernel);

            Core.addWeighted(LG, LGNBank.LMM_ALPHA, MG, -LGNBank.LMM_BETA, 0, dst);
        }
    }

    /**
     * S-(L+M)
     *
     * @param LMS
     * @param dst
     */
    private void SMLPM(Mat[] LMS, Mat dst) {
        int rows = LMS[0].rows();
        int cols = LMS[0].cols();
        Mat S = new Mat(rows, cols, CvType.CV_32FC1);
        Mat LPM = new Mat(rows, cols, CvType.CV_32FC1);

        if (Config.LGNmethod == 1) {
            Mat LG = new Mat(rows, cols, CvType.CV_32FC1);
            Mat MG = new Mat(rows, cols, CvType.CV_32FC1);

            LG = Functions.filter2(LMS[0], LGNBank.LPM_L_kernel);
            MG = Functions.filter2(LMS[1], LGNBank.LPM_M_kernel);
            Core.add(LG, MG, LPM);

            S = Functions.filter2(LMS[2], LGNBank.SMLPM_S_kernel);
            LPM = Functions.filter2(LPM, LGNBank.SMLPM_LPM_kernel);
            Core.add(S, LPM, dst);
        }
        if (Config.LGNmethod == 2) {
            Core.addWeighted(LMS[0], LGNBank.SMLPM_GAMMA, LMS[1], LGNBank.SMLPM_DELTA, 0, LPM);
            Imgproc.sepFilter2D(LMS[2], S, -1, LGNBank.upperKernel, LGNBank.upperKernel);

            Imgproc.sepFilter2D(LPM, LPM, -1, LGNBank.lowerKernel, LGNBank.lowerKernel);

            Core.addWeighted(S, LGNBank.SMLPM_ALPHA, LPM, -LGNBank.SMLPM_BETA, 0, dst);
        }

    }

    /**
     * L+M
     *
     * @param LMS
     * @param dst
     */
    private void LPM(Mat[] LMS, Mat dst) {
        if (Config.LGNmethod == 1) {
            int rows = LMS[0].rows();
            int cols = LMS[0].cols();
            Mat LG = new Mat(rows, cols, CvType.CV_32FC1);
            Mat MG = new Mat(rows, cols, CvType.CV_32FC1);
            LG = Functions.filter2(LMS[0], LGNBank.LPM_L_kernel);
            MG = Functions.filter2(LMS[1], LGNBank.LPM_M_kernel);
            Core.add(LG, MG, dst);
        }
        if (Config.LGNmethod == 2) {
            Core.addWeighted(LMS[0], LGNBank.LPM_ALPHA, LMS[1], LGNBank.LPM_BETA, 0, dst);
        }
    }

}
