/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import utils.Config;
import utils.FileUtils;

/**
 *
 * @author Laptop
 */
public class V2Bank {

    public static AngleCells[][][] AC;
    public static CurvatureCells[][][] CurvC;

    public static void initializeCells(int... dimensions) {

        AC = new AngleCells[dimensions[0]][Config.gaborBanks][dimensions[2]];
        CurvC = new CurvatureCells[dimensions[0]][Config.gaborBanks][dimensions[2]];
        String folder = "RFV2/Curvature/";
        String fileNames[] = FileUtils.getFiles(folder);
        int numCurvatures = fileNames.length;
        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < Config.gaborBanks; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    AC[i1][i2][i3] = new AngleCells(Config.gaborOrientations, 2 * Config.gaborOrientations);
                    CurvC[i1][i2][i3] = new CurvatureCells(numCurvatures, 10);
                    CurvC[i1][i2][i3].generateFiltersByFolder(folder);
                }
            }
        }

    }
}
