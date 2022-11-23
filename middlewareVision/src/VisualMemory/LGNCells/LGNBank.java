/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.LGNCells;

import VisualMemory.LGNCells.SimpleOpponentCells;
import VisualMemory.V4Cells.GaussianFilter;
import java.util.ArrayList;
import org.opencv.core.Mat;
import utils.FileUtils;
import utils.MatrixUtils;
import static utils.SpecialKernels.LMM_L_kernel;
import static utils.SpecialKernels.LMM_M_kernel;
import static utils.SpecialKernels.LPM_L_kernel;
import static utils.SpecialKernels.LPM_M_kernel;

/**
 *
 * @author Laptop
 */
public class LGNBank {
    
       /*
    LGN kernels
     */
    public static Mat LMM_L_kernel;
    public static Mat LMM_M_kernel;
    public static Mat LPM_L_kernel;
    public static Mat LPM_M_kernel;
    public static Mat SMLPM_LPM_kernel;
    public static Mat SMLPM_S_kernel;
    
    static GaussianFilter[] filters;

    public static SimpleOpponentCells[][] SOC;

    public static void initializeCells(int scales) {
        loadLGNKernels();
        SOC = new SimpleOpponentCells[scales][2];

        for (int i2 = 0; i2 < scales; i2++) {
            for (int i3 = 0; i3 < 2; i3++) {
                SOC[i2][i3] = new SimpleOpponentCells(0, 3);
            }
        }              
    }
    

    public static void loadLGNKernels() {      
        GaussianFilter[] LMM = getGaussians("RFLGN/LMM.txt");
        LMM_L_kernel=getFilterByLabel("L",LMM);
        LMM_M_kernel=getFilterByLabel("M",LMM);
        
        GaussianFilter[] LPM = getGaussians("RFLGN/LPM.txt");
        LPM_L_kernel=getFilterByLabel("L",LPM);
        LPM_M_kernel=getFilterByLabel("M",LPM); 
        
        GaussianFilter[] SMLPM = getGaussians("RFLGN/SMLPM.txt");
        SMLPM_LPM_kernel=getFilterByLabel("LPM",SMLPM);
        SMLPM_S_kernel=getFilterByLabel("S",SMLPM); 
    }

    public static Mat getFilterByLabel(String label, GaussianFilter g[]) {
        ArrayList<GaussianFilter> glist = new ArrayList();
        for (GaussianFilter gf : g) {
            if(gf.getComb().equals(label))
            {                
                glist.add(gf);
            }
        }
        Mat filterMats[]=new Mat[glist.size()];
        int i=0;
        for(GaussianFilter gf:glist){
            filterMats[i]=gf.makeFilter2();
        }
        return MatrixUtils.sum(filterMats, 1, 0);
    }

    public static GaussianFilter[] getGaussians(String file) {
        String fileLines[] = FileUtils.fileLines(file);
        filters = new GaussianFilter[fileLines.length];
        for (int i = 0; i < fileLines.length; i++) {
            filters[i] = new GaussianFilter(fileLines[i]);
        }
        return filters;
    }

}
