/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V2Cells;

import VisualMemory.V2Cells.CurvatureCells;
import VisualMemory.V2Cells.AngleCells;
import utils.Config;
import utils.FileUtils;

/**
 *
 * @author Laptop
 */
public class V2Bank {

    //[frequency][eye]
    public static AngleCells[][] AC;
    public static CurvatureCells[][] CurvC;
    public static CornerMotionCells[][] CMC;

    public static void initializeCells() {

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
}
