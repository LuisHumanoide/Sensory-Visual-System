/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import VisualMemory.V4Cells.V4Bank;

/**
 *
 * @author HumanoideFilms
 */
public class SaveUtils {

    static String[] e = {"L", "R"};

    public static void saveLMS(String folder, String name) {
        String route = folder + "\\" + name + "\\Retina\\";
        FileUtils.saveImage(LGNBank.matL_L, route, "L_0");
        FileUtils.saveImage(LGNBank.matM_L, route, "M_0");
        FileUtils.saveImage(LGNBank.matS_L, route, "S_0");
        if (Config.stereo) {
            FileUtils.saveImage(LGNBank.matL_R, route, "L_1");
            FileUtils.saveImage(LGNBank.matM_R, route, "M_1");
            FileUtils.saveImage(LGNBank.matS_R, route, "S_1");
        }
    }

    public static void saveSOC(String folder, String name) {
        String dkl[] = {"D", "K", "L"};
        String route = folder + "\\" + name + "\\LGN\\";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                FileUtils.saveImage(LGNBank.SOC[0][j].Cells[i].mat, route, dkl[i] + "_" + j);
            }
        }
    }

    public static void saveDOC(String folder, String name) {
        String iNames[] = {"Dp", "Kp", "Lp"};
        String route = folder + "\\" + name + "\\V1\\DoubleOpponent\\";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                FileUtils.saveImage(V1Bank.DOC[0][j].Cells[i].mat, route, iNames[i] + "_" + j);
            }
        }
    }

    public static void saveSimpleCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\SimpleCells\\";
        for (int i = 0; i < V1Bank.SC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                for (int k = 0; k < Config.gaborOrientations; k++) {
                    FileUtils.saveImage(V1Bank.SC[i][j].Even[k].mat, route, "Phase1_" + i + "_" + j + "_" + k);
                    FileUtils.saveImage(V1Bank.SC[i][j].Odd[k].mat, route, "Phase2_" + i + "_" + j + "_" + k);
                }

            }
        }
    }

    public static void saveComplexCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\ComplexCells\\";
        for (int i = 0; i < V1Bank.CC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                for (int k = 0; k < Config.gaborOrientations; k++) {
                    FileUtils.saveImage(V1Bank.CC[i][j].Cells[k].mat, route, i + "_" + j + "_" + k);
                }

            }
        }
    }

    public static void saveHyperComplexCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\HyperComplexCells\\";
        for (int i = 0; i < V1Bank.HCC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                for (int k2 = 0; k2 < V1Bank.HCC[0][0].Cells.length; k2++) {
                    for (int k = 0; k < Config.gaborOrientations; k++) {
                        FileUtils.saveImage(V1Bank.HCC[i][j].Cells[k2][k].mat, route, i + "_" + j + "_" + k2 + "_" + k);
                    }
                }
            }
        }
    }

    public static void saveMergedHyperComplexCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\MergedHyperComplexCells\\";
        for (int i = 0; i < V1Bank.HCC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {

                for (int k = 0; k < Config.gaborOrientations; k++) {
                    FileUtils.saveImage(V1Bank.HCC[i][j].mergedCells[k].mat, route, i + "_" + j + "_" + k);
                }

            }
        }
    }

    public static void saveAngularCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V2\\AngularCells\\";
        for (int i = 0; i < V2Bank.AC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {

                for (int k = 0; k < V2Bank.AC[0][0].Cells.length; k++) {
                    for (int k2 = 0; k2 < V2Bank.AC[0][0].Cells[0].length; k2++) {
                        FileUtils.saveImage(V2Bank.AC[i][j].Cells[k][k2].mat, route, i + "_" + j + "_" + k + "_" + k2);
                    }
                }
            }
        }
    }

    public static void saveMergedAngularCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V2\\MergedAngularCells\\";
        for (int i = 0; i < V2Bank.AC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {

                for (int k = 0; k < V2Bank.AC[0][0].Cells.length; k++) {
                    FileUtils.saveImage(V2Bank.AC[i][j].mergedAC[k], route, i + "_" + j + "_" + k);
                }

            }
        }
    }

    public static void saveCurvatureCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V2\\CurvatureCells\\";
        for (int i = 0; i < V2Bank.AC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {

                for (int k = 0; k < V2Bank.CurvC[0][0].cells.length; k++) {
                    for (int k2 = 0; k2 < V2Bank.CurvC[0][0].cells[0].length; k2++) {
                        FileUtils.saveImage(V2Bank.CurvC[i][j].cells[k][k2].mat, route,
                                "R" + V2Bank.CurvC[i][j].filters[k][k2].radius + "_" + i + "_" + j + "_" + k + "_" + k2);
                    }
                }
            }
        }
    }

    public static void saveMergedCurvatureCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V2\\MergedCurvatureCells\\";
        for (int i = 0; i < V2Bank.AC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {

                for (int k = 0; k < V2Bank.CurvC[0][0].cells.length; k++) {

                    FileUtils.saveImage(V2Bank.CurvC[i][j].composedCells[k].mat, route,
                            "Curvature R" + V2Bank.CurvC[i][j].filters[k][0].radius + "_" + i + "_" + j + "_" + k);

                }
            }
        }
    }

    public static void saveColorLabels(String folder, String name) {
        String route = folder + "\\" + name + "\\V4\\ColorLabels\\";
        FileUtils.saveImage2(V4Bank.colorLabelL, route, "0");
        if (Config.stereo) {
            FileUtils.saveImage2(V4Bank.colorLabelR, route, "1");
        }
    }

    static int numberEyes() {
        if (Config.stereo) {
            return 2;
        } else {
            return 1;
        }
    }

}
