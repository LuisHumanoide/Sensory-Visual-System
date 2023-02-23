/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.MSTCells.MSTPolar;
import VisualMemory.MTCells.MTBank;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import VisualMemory.V3Cells.V3Bank;
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
                FileUtils.saveImage(LGNBank.SOC[0][j].Cells[i].mat, route, dkl[i] + "_Eye_" + j);
            }
        }
    }

    public static void saveDOC(String folder, String name) {
        String iNames[] = {"Dp", "Kp", "Lp"};
        String route = folder + "\\" + name + "\\V1\\DoubleOpponent\\";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                FileUtils.saveImage(V1Bank.DOC[0][j].Cells[i].mat, route, iNames[i] + "_Eye_" + j);
            }
        }
    }

    public static void saveSimpleCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\SimpleCells\\";
        for (int i = 0; i < V1Bank.SC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                for (int k = 0; k < Config.gaborOrientations; k++) {
                    FileUtils.saveImage(V1Bank.SC[i][j].Even[k].mat, route, "Phase1_Bank_" + i + "_Eye_" + j + "_Or_" + k);
                    FileUtils.saveImage(V1Bank.SC[i][j].Odd[k].mat, route, "Phase2_Bank" + i + "_Eye_" + j + "_Or_" + k);
                }

            }
        }
    }

    public static void saveComplexCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\ComplexCells\\";
        for (int i = 0; i < V1Bank.CC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                for (int k = 0; k < Config.gaborOrientations; k++) {
                    FileUtils.saveImage(V1Bank.CC[i][j].Cells[k].mat, route, "Bank_" + i + "_Eye_" + j + "_Or_" + k);
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
                        FileUtils.saveImage(V1Bank.HCC[i][j].Cells[k2][k].mat, route, "Bank_" + i + "_Eye_" + j + "_Type_" + k2 + "_Or_" + k);
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
                    FileUtils.saveImage(V1Bank.HCC[i][j].mergedCells[k].mat, route, "Bank_" + i + "_Eye_" + j + "_Or_" + k);
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
                        FileUtils.saveImage(V2Bank.AC[i][j].Cells[k][k2].mat, route, "Bank_" + i + "_Eye_" + j + "_Aberture_" + k + "_Dir_" + k2);
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
                    FileUtils.saveImage(V2Bank.AC[i][j].mergedAC[k], route, "Bank_" + i + "_Eye_" + j + "_Aberture_" + k);
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
                                "Bank_" + i + "_Eye_" + j + "_Radius_" + V2Bank.CurvC[i][j].filters[k][0].radius + "_Dir_" + k2);
                    }
                }
            }
        }
    }

    public static void saveMergedCurvatureCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V2\\MergedCurvatureCells\\";
        for (int i = 0; i < V2Bank.AC.length; i++) {//Bank
            for (int j = 0; j < numberEyes(); j++) {//Eye
                for (int k = 0; k < V2Bank.CurvC[0][0].cells.length; k++) {//Curvature
                    FileUtils.saveImage(V2Bank.CurvC[i][j].composedCells[k].mat, route,
                            "Bank_" + i + "_Eye_" + j + "_Radius_" + V2Bank.CurvC[i][j].filters[k][0].radius);
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

    public static void saveSingleShapeCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V4\\SingleShapeCells\\";
        for (int i = 0; i < V4Bank.SSC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                FileUtils.saveImage2(V4Bank.SSC[i][j].cell.mat, route, V4Bank.SSC[i][j].nameCell + "_Scale_" + V4Bank.SSC[i][j].scale + "_ID_" + j);
            }
        }
    }

    public static void saveMergedShapeCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V4\\SingleShapeMergedCells\\";
        for (int i = 0; i < V4Bank.SMC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                FileUtils.saveImage2(V4Bank.SMC[i][j].cell.mat, route, V4Bank.SMC[i][j].nameCell + "_" + j);
            }
        }
    }

    public static void saveBSimpleCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\BinocularSimpleCells\\";
        for (int i = 0; i < V1Bank.SSC.length; i++) {
            for (int j = 0; j < V1Bank.SSC[0].length; j++) {
                for (int k = 0; k < V1Bank.SSC[0][0].evenCells.length; k++) {
                    FileUtils.saveImage(V1Bank.SSC[i][j].evenCells[k].mat, route, "Bank_" + i + "Phase1_Disp_" + V1Bank.SSC[i][j].disparity + "_Or_" + k);
                    FileUtils.saveImage(V1Bank.SSC[i][j].oddCells[k].mat, route, "Bank_" + i + "Phase2_Disp_" + V1Bank.SSC[i][j].disparity + "_Or_" + k);
                }
            }
        }
    }

    public static void saveCombinedBSimpleCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\CombinedBinocularSimpleCells\\";
        for (int i = 0; i < V1Bank.SSC.length; i++) {
            for (int j = 0; j < V1Bank.SSC[0].length; j++) {
                FileUtils.saveImage(V1Bank.SSC[i][j].composedEvenCell.mat, route, "Bank_" + i + "Phase1_Disp_" + V1Bank.SSC[i][j].disparity);
                FileUtils.saveImage(V1Bank.SSC[i][j].composedOddCell.mat, route, "Bank_" + i + "Phase2_Disp_" + V1Bank.SSC[i][j].disparity);
            }
        }
    }

    public static void saveBComplexCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\BinocularComplexCells\\";
        for (int i = 0; i < V1Bank.SSC.length; i++) {
            for (int j = 0; j < V1Bank.SSC[0].length; j++) {
                for (int k = 0; k < V1Bank.SSC[0][0].complexCells.length; k++) {
                    FileUtils.saveImage(V1Bank.SSC[i][j].complexCells[k].mat, route, "Bank_" + i + "_Disp_" + V1Bank.SSC[i][j].disparity + "_Or_" + k);
                }
            }
        }
    }

    public static void saveCombinedBComplexCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\CombinedBinocularComplexCells\\";
        for (int i = 0; i < V1Bank.SSC.length; i++) {
            for (int j = 0; j < V1Bank.SSC[0].length; j++) {
                FileUtils.saveImage(V1Bank.SSC[i][j].composedComplexCell.mat, route, "Bank_" + i + "_Disp_" + V1Bank.SSC[i][j].disparity);
            }
        }
    }

    public static void saveBNormCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\BinocularNormalizedCells\\";
        for (int i = 0; i < V1Bank.SSC.length; i++) {
            for (int j = 0; j < V1Bank.SSC[0].length; j++) {
                for (int k = 0; k < V1Bank.SSC[0][0].normalizedCells.length; k++) {
                    FileUtils.saveImage(V1Bank.SSC[i][j].normalizedCells[k].mat, route, "Bank_" + i + "_Disp_" + V1Bank.SSC[i][j].disparity + "_Or_" + k);
                }
            }
        }
    }

    public static void saveCombinedBNormCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\CombinedBinocularNormalizedCells\\";
        for (int i = 0; i < V1Bank.SSC.length; i++) {
            for (int j = 0; j < V1Bank.SSC[0].length; j++) {
                FileUtils.saveImage(V1Bank.SSC[i][j].composedNormalizedCell.mat, route, "Bank_" + i + "_Disp_" + V1Bank.SSC[i][j].disparity);
            }

        }
    }

    public static void saveBMergedCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\BinocularNormalizedCells\\";
        for (int i = 0; i < V1Bank.SMC.length; i++) {
            for (int k = 0; k < V1Bank.SMC[0].cells.length; k++) {
                FileUtils.saveImage(V1Bank.SMC[i].cells[k].mat, route, "Disp_" + V1Bank.SMC[i].disparity + "_Or_" + k);
            }
        }
    }

    public static void saveCombinedBMergedCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\CombinedBinocularNormalizedCells\\";
        for (int i = 0; i < V1Bank.SMC.length; i++) {
            for (int k = 0; k < V1Bank.SMC[0].cells.length; k++) {
                FileUtils.saveImage(V1Bank.SMC[i].composedCell.mat, route, "Disp_" + V1Bank.SMC[i].disparity);
            }
        }
    }

    public static void saveBRelativeCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V2\\BinocularRelativeDisparityCells\\";
        for (int i = 0; i < V3Bank.SRC.length; i++) {
            for (int k = 0; k < V3Bank.SRC[0].cells.length; k++) {
                FileUtils.saveImage(V3Bank.SRC[i].cells[k].mat, route, "Disp_" + (int) V3Bank.SRC[i].disparity + "_Or_" + k);
            }
        }
    }

    public static void saveCombinedBRelativeCells(String folder, String name) {
        String route = folder + "\\" + name + "\\V2\\CombinedBinocularRelativeDisparityCells\\";
        for (int i = 0; i < V3Bank.SRC.length; i++) {
            FileUtils.saveImage(V3Bank.SRC[i].composedCell.mat, route, "Disp_" + (int) V3Bank.SRC[i].disparity);
        }
    }

    public static void saveV1Motion(String folder, String name) {
        String route = folder + "\\" + name + "\\V1\\Motion\\";
        for (int i = 0; i < V1Bank.MC.length; i++) {
            for (int j = 0; j < numberEyes(); j++) {
                for (int k = 0; k < V1Bank.MC[0][0].cells.length; k++) {
                    for (int k2 = 0; k2 < V1Bank.MC[0][0].cells[0].length; k2++) {
                        FileUtils.saveImage(V1Bank.MC[i][j].cells[k][k2].mat, route, "Bank_" + i + "_Vel_" + V1Bank.MC[i][j].cells[k][k2].getDxDt() + "_Eye_" + j + "_Dir_" + k2);
                    }
                }
            }
        }
    }

    public static void saveMTComponentMotion(String folder, String name) {
        String route = folder + "\\" + name + "\\MT\\ComponentMotion\\";
        String ids = "";
        for (int j = 0; j < numberEyes(); j++) {
            for (int k = 0; k < MTBank.MTCC[0].CCells.length; k++) {
                ids = ids + "ID=" + k + ", SPEED=" + MTBank.MTCC[j].CCells[k][0].getSpeed() + "\n";
                for (int k2 = 0; k2 < MTBank.MTCC[0].CCells[0].length; k2++) {
                    FileUtils.saveImage(MTBank.MTCC[j].CCells[k][k2].mat, route, "Eye_" + j + "_VelID_" + k + "_Dir_" + k2);
                }
            }

        }
        FileUtils.write(route + "VelIDS", ids, "txt");
    }

    public static void saveMTPatternMotion(String folder, String name) {
        String route = folder + "\\" + name + "\\MT\\PatternMotion\\";
        String ids = "";
        for (int j = 0; j < numberEyes(); j++) {
            for (int k = 0; k < MTBank.MTPC[0].Cells.length; k++) {
                ids = ids + "ID=" + k + ", SPEED=" + MTBank.MTPC[j].Cells[k].getSpeed() + ", ANGLE=" + MTBank.MTPC[j].Cells[k].getAngle() + "\n";
                FileUtils.saveImage(MTBank.MTPC[j].Cells[k].mat, route, "Eye_" + j + "_VelID_" + k);
            }
        }
        FileUtils.write(route + "VelIDS", ids, "txt");
    }

    public static void saveMSTRadialMotion(String folder, String name) {
        String route = folder + "\\" + name + "\\MST\\RadialMotion\\";
        for (int j = 0; j < numberEyes(); j++) {
            FileUtils.saveImage(MSTPolar.contC[j].mat, route, "Eye_" + j + "_Contraction");
            FileUtils.saveImage(MSTPolar.expC[j].mat, route, "Eye_" + j + "_Expansion");
            FileUtils.saveImage(MSTPolar.rotC[j].mat, route, "Eye_" + j + "_Rot1");
            FileUtils.saveImage(MSTPolar.invRC[j].mat, route, "Eye_" + j + "_Rot2");
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
