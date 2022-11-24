/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V2Cells;

import VisualMemory.V2Cells.CurvatureCells;
import VisualMemory.V2Cells.AngleCells;
import org.opencv.core.Mat;
import utils.Config;
import utils.FileUtils;
import utils.SpecialKernels;
import static utils.SpecialKernels.getCompositeRF;

/**
 *
 * @author Laptop
 */
public class V2Bank {

    //[frequency][eye]
    public static AngleCells[][] AC;
    public static CurvatureCells[][] CurvC;
    public static CornerMotionCells[][] CMC;
    public static Mat v2Kernels[];

    public static void initializeCells() {
        loadV2Kernels();
        AC = new AngleCells[Config.gaborBanks][2];
        CurvC = new CurvatureCells[Config.gaborBanks][2];
        CMC = new CornerMotionCells[Config.gaborBanks][2];
        String folder = "RFV2/Curvature/";
        String fileNames[] = FileUtils.getFiles(folder);
        int numCurvatures = fileNames.length;
        for (int i2 = 0; i2 < Config.gaborBanks; i2++) {
            for (int i3 = 0; i3 < 2; i3++) {
                AC[i2][i3] = new AngleCells(Config.gaborOrientations, 2 * Config.gaborOrientations);
                CurvC[i2][i3] = new CurvatureCells(numCurvatures, 5);
                CurvC[i2][i3].generateFiltersByFolder(folder);
                CMC[i2][i3]=new CornerMotionCells("ConfigFiles/speeds.txt");
            }
        }

    }
    
    /**
     * Load the angular kernels
     */
    public static void loadV2Kernels() {
        v2Kernels = new Mat[Config.gaborOrientations * 2];
        String path = "RFV2";
        String file = "angular";
        Mat baseKernel = getCompositeRF(path + "/" + file + ".txt");
        for (int i = 0; i < Config.gaborOrientations * 2; i++) {
            double angle = (180 / Config.gaborOrientations) * i;
            double rangle = -Math.toRadians(angle);
            v2Kernels[i] = SpecialKernels.rotateKernelRadians(baseKernel, rangle);
        }
    }
}
