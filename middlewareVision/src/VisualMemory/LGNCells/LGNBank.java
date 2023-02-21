/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.LGNCells;

import VisualMemory.LGNCells.SimpleOpponentCells;
import utils.GaussianFilter;
import middlewareVision.config.XMLReader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import utils.FilterUtils;

/**
 *
 * @author Laptop
 */
public class LGNBank {

    public static int KERNEL_SIZE = 0;
    public static float UPPER_KERNEL_SIGMA = 0;
    public static float LOWER_KERNEL_SIGMA = 0;
    public static float LMM_ALPHA = 0;
    public static float LMM_BETA = 0;
    public static float SMLPM_ALPHA = 0;
    public static float SMLPM_BETA = 0;
    public static float SMLPM_GAMMA = 0;
    public static float SMLPM_DELTA = 0;
    public static float LPM_ALPHA = 0;
    public static float LPM_BETA = 0;
    /*
    LGN kernels
     */
    public static Mat LMM_L_kernel;
    public static Mat LMM_M_kernel;
    public static Mat LPM_L_kernel;
    public static Mat LPM_M_kernel;
    public static Mat SMLPM_LPM_kernel;
    public static Mat SMLPM_S_kernel;

    public static Mat upperKernel;
    public static Mat lowerKernel;

    //static GaussianFilter[] filters;
    
    public static Mat matL_L;
    public static Mat matM_L;
    public static Mat matS_L;
    
    public static Mat matL_R;
    public static Mat matM_R;
    public static Mat matS_R;

    public static SimpleOpponentCells[][] SOC;

    public static void initializeCells(int scales) {
        loadLGNKernels();
        loadLGNValues();
        SOC = new SimpleOpponentCells[scales][2];

        for (int i2 = 0; i2 < scales; i2++) {
            for (int i3 = 0; i3 < 2; i3++) {
                SOC[i2][i3] = new SimpleOpponentCells(0, 3);
            }
        }
    }

    public static void loadLGNValues() {
        String values[] = XMLReader.getValuesFromXML("KERNEL_SIZE", "UPPER_KERNEL_SIGMA", "LOWER_KERNEL_SIGMA", "LMM_ALPHA", "LMM_BETA",
                "SMLPM_ALPHA", "SMLPM_BETA", "SMLPM_GAMMA", "SMLPM_DELTA", "LPM_ALPHA", "LPM_BETA");
        
        KERNEL_SIZE = Integer.parseInt(values[0]);
        UPPER_KERNEL_SIGMA = Float.parseFloat(values[1]);
        LOWER_KERNEL_SIGMA = Float.parseFloat(values[2]);
        LMM_ALPHA = Float.parseFloat(values[3]);
        LMM_BETA = Float.parseFloat(values[4]);
        SMLPM_ALPHA = Float.parseFloat(values[5]);
        SMLPM_BETA = Float.parseFloat(values[6]);
        SMLPM_GAMMA = Float.parseFloat(values[7]);
        SMLPM_DELTA = Float.parseFloat(values[8]);
        LPM_ALPHA = Float.parseFloat(values[9]);
        LPM_BETA = Float.parseFloat(values[10]);

        upperKernel = Imgproc.getGaussianKernel(KERNEL_SIZE, UPPER_KERNEL_SIGMA);
        lowerKernel = Imgproc.getGaussianKernel(KERNEL_SIZE, LOWER_KERNEL_SIGMA);
    }

    public static void loadLGNKernels() {
        GaussianFilter[] LMM = FilterUtils.getGaussians("RFLGN/LMM.txt");
        LMM_L_kernel = FilterUtils.getFilterByLabel("L", LMM);
        LMM_M_kernel = FilterUtils.getFilterByLabel("M", LMM);

        GaussianFilter[] LPM = FilterUtils.getGaussians("RFLGN/LPM.txt");
        LPM_L_kernel = FilterUtils.getFilterByLabel("L", LPM);
        LPM_M_kernel = FilterUtils.getFilterByLabel("M", LPM);

        GaussianFilter[] SMLPM = FilterUtils.getGaussians("RFLGN/SMLPM.txt");
        SMLPM_LPM_kernel = FilterUtils.getFilterByLabel("LPM", SMLPM);
        SMLPM_S_kernel = FilterUtils.getFilterByLabel("S", SMLPM);
    }





}
