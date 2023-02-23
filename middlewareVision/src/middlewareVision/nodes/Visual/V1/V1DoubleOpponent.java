package middlewareVision.nodes.Visual.V1;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.V1Cells.V1Bank;
import generator.ProcessList;
import gui.Visualizer;
import spike.Location;
import cFramework.nodes.process.Process;
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
 * Class that performs the double opponent process<br>
 * Information from the <b>LGN</b> is reunited in the double opponent cells where 
 * the process is kind of a difference of gaussians<br>
 * where the observable effect is the ennhacement of the contrast and color.
 */
public class V1DoubleOpponent extends Process {


    public V1DoubleOpponent() {
        this.ID = AreaNames.V1DoubleOpponent;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {
    }
    
    String labels[]={"Double Opponent D'","Double Opponent K'","Double Opponent L'"};
    @Override
    public void receive(long nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                
                if (spike.getModality() == Modalities.VISUAL) {
                    //Stores the LGN cells into the DKL ones
                    Mat DKL_L[] = {LGNBank.SOC[0][0].Cells[0].mat,
                        LGNBank.SOC[0][0].Cells[1].mat,
                        LGNBank.SOC[0][0].Cells[2].mat
                    };
                    Mat DKL_R[] = {LGNBank.SOC[0][1].Cells[0].mat,
                        LGNBank.SOC[0][1].Cells[1].mat,
                        LGNBank.SOC[0][1].Cells[2].mat
                    };
                    //Performs the double opponent process for both eyes
                    DoubleOpponentProcess(DKL_L, 0);
                    DoubleOpponentProcess(DKL_R, 1);
                    //visualize the activations
                    for (int i = 0; i < 3; i++) {
                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.DOC[0][0].Cells[i].mat), labels[i]+" ... Left", 4, i);
                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.DOC[0][1].Cells[i].mat), labels[i]+" ... Right", 5, i);
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
     * Perform the double opponent process
     *
     * @param DKL
     */
    public void DoubleOpponentProcess(Mat[] DKL, int eye) {
        V1Bank.DOC[0][eye].Cells[0].mat = OpponentD(DKL);
        V1Bank.DOC[0][eye].Cells[1].mat = OpponentK(DKL);
        V1Bank.DOC[0][eye].Cells[2].mat = LGNBank.SOC[0][eye].Cells[2].mat.clone();
    }

    /**
     * Double Opponent process with D cells
     * It performs a kind of difference of Gaussians between the activations D cells from the LGN
     * @param DKL array of DKL mats from the LGN
     * @return the activation of D'
     */
    private Mat OpponentD(Mat[] DKL) {
        //old code
       /* Mat D1Kernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, 1, -0.5, 0.5, 0.5, 2);
        Mat D2Kernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, -1, 0.5, 0.5, 0.5, 2);*/

        Mat D1 = new Mat(DKL[0].size(), CvType.CV_32FC1);
        Mat D2 = new Mat(DKL[0].size(), CvType.CV_32FC1);

        Core.multiply(DKL[0], Scalar.all(-1), D2);
        Imgproc.threshold(DKL[0], D1, 0, 1, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(D2, D2, 0, 1, Imgproc.THRESH_TOZERO);

        Imgproc.filter2D(D1, D1, -1, V1Bank.D1Kernel);
        Imgproc.filter2D(D2, D2, -1, V1Bank.D2Kernel);

        Core.addWeighted(D1, V1Bank.D_ALPHA, D2, V1Bank.D_BETA, 0, D1);

        return D1;
    }

    /**
     * Double Opponent process with K cells
     * It performs a kind of difference of Gaussians between the activations K cells from the LGN
     *
     * @param DKL array of DKL mats from the LGN
     * @return the activation of K'
     */
    private Mat OpponentK(Mat[] DKL) {
        //old code
        /*Mat K1Kernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, 1, -0.5, 0.5, 0.5, 5);
        Mat K2Kernel = SpecialKernels.getDoubleOpponentKernel(new Size(KERNEL_SIZE, KERNEL_SIZE), 1, 1, -1, 0.5, 0.5, 0.5, 5);*/

        Mat K1 = new Mat(DKL[1].size(), CvType.CV_32FC1);
        Mat K2 = new Mat(DKL[1].size(), CvType.CV_32FC1);

        Core.multiply(DKL[1], Scalar.all(-1), K2);
        Imgproc.threshold(DKL[1], K1, 0, 1, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(K2, K2, 0, 1, Imgproc.THRESH_TOZERO);

        Imgproc.filter2D(K1, K1, -1, V1Bank.K1Kernel);
        Imgproc.filter2D(K2, K2, -1, V1Bank.K2Kernel);

        Core.addWeighted(K1, V1Bank.K_ALPHA, K2, V1Bank.K_BETA, 0, K1);

        return K1;
    }

}
