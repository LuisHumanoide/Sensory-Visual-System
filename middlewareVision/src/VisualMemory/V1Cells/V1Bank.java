/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V1Cells;

import MiniPrograms.RF;
import VisualMemory.MTCells.MTBank;
import java.io.File;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import utils.Config;
import utils.FileUtils;
import utils.SpecialKernels;
import utils.filters.GaborFilter;

/**
 *
 * @author Laptop
 */
public class V1Bank {

    //[for different frequencies or scales][eye]
    public static Mat motionDiff;
    public static SimpleCells[][] SC;
    public static ComplexCells[][] CC;
    public static HypercomplexCells[][] HCC;
    public static DoubleOpponentCells[][] DOC;
    public static MotionCellsV1[][] MC;
    
    //[frequency][disparity index]
    public static StereoscopicCells[][] SSC;
    
    //[disparity index]
    public static StereoscopicMergedCells[] SMC;
    
    static String GaborFile = "RFV1//Gabor//filters.txt";
    static String DisparityFile = "Disparities.txt";
    static String HCfiles = "RFV1HC//";

    /**
     * Initialize all cells from V1
     */
    public static void initializeCells() {
        
        String gaborLines[] = FileUtils.fileLines(GaborFile);       
        Config.gaborBanks = gaborLines.length;
        
        String disparities[] = FileUtils.fileLines(DisparityFile);
        
        File hcfolder = new File(HCfiles);
        File hcfiles[] = hcfolder.listFiles();
        Config.HCfilters = hcfiles.length;
        Config.nDisparities=disparities.length;
        
        //reserve memory for cell banks
        SC = new SimpleCells[gaborLines.length][2];
        CC = new ComplexCells[gaborLines.length][2];
        HCC = new HypercomplexCells[gaborLines.length][2];
        DOC = new DoubleOpponentCells[1][2];
        MC = new MotionCellsV1[gaborLines.length][2];
        
        //reserve memory for stereoscopic cell banks
        SSC = new StereoscopicCells[gaborLines.length][disparities.length];
        SMC = new StereoscopicMergedCells[disparities.length];
        
        //Simple Mat to compute the motion difference in the retina
        motionDiff=new Mat();

        /*
        Creating the double opponent cells banks
        */
        for (int i2 = 0; i2 < 1; i2++) {
            for (int i3 = 0; i3 < 2; i3++) {
                DOC[i2][i3] = new DoubleOpponentCells(Config.gaborOrientations);
            }
        }

        /*
        Creating the V1 cells
        */
        for (int i2 = 0; i2 < gaborLines.length; i2++) {
            for (int i3 = 0; i3 < 2; i3++) {
                SC[i2][i3] = new SimpleCells(Config.gaborOrientations);
                CC[i2][i3] = new ComplexCells(Config.gaborOrientations);
                HCC[i2][i3] = new HypercomplexCells(Config.HCfilters, Config.gaborOrientations);
                MC[i2][i3] = new MotionCellsV1("ConfigFiles/speeds.txt");
                CC[i2][i3].setSimpleCells(SC[i2][i3]);
                MC[i2][i3].setPrevious(CC[i2][i3].Cells);
            }
            for(int i=0;i<disparities.length;i++){
                SSC[i2][i]=new StereoscopicCells(Integer.parseInt(disparities[i]),SC[i2]);
                if(i2==0){
                    SMC[i] = new StereoscopicMergedCells(Integer.parseInt(disparities[i]));
                }
            }
        }   
        
        //Initialize the cells from area MT
        MTBank.initializeComponentCells(MC[0][0].cells.length, MC[0][0].cells[0].length, Config.motionWidth);

        loadGaborFilters(gaborLines, 2);
        loadHCFilters(hcfiles);
    }

    /**
     * Load Gabor Filters from a file, the system can have several types of
     * gabor filters
     *
     * @param gaborLines
     * @param eyes
     * @param index1
     */
    public static void loadGaborFilters(String gaborLines[], int eyes) {
        int i = 0;
        for (String st : gaborLines) {
            String values[] = st.split(" ");
            GaborFilter evenFilter = new GaborFilter(
                    Integer.parseInt(values[0]),
                    Double.parseDouble(values[1]),
                    Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]),
                    0,
                    Double.parseDouble(values[5]));
            GaborFilter oddFilter = new GaborFilter(
                    Integer.parseInt(values[0]),
                    Double.parseDouble(values[1]),
                    Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]),
                    Double.parseDouble(values[4]),
                    Double.parseDouble(values[5]));
            for (int j = 0; j < eyes; j++) {
                for (int k = 0; k < Config.gaborOrientations; k++) {
                    SC[i][j].evenFilter[k] = SpecialKernels.rotateKernelRadians(evenFilter.makeFilter(), k * SpecialKernels.inc);
                    SC[i][j].oddFilter[k] = SpecialKernels.rotateKernelRadians(oddFilter.makeFilter(), k * SpecialKernels.inc);
                    SC[i][j].geven = evenFilter;
                    SC[i][j].godd = oddFilter;

                }
            }
            i++;

        }
    }

    /**
     * Load the Gauss filters that emulates the end stop-filters from the
     * hypercomplex cells and load into the HCC class
     *
     * @param files
     */
    public static void loadHCFilters(File[] files) {
        int i = 0;
        int i1 = HCC.length;
        int i2 = HCC[0].length;
        for (File fi : files) {
            Mat filter = getRF(fi.getPath());

            for (int x1 = 0; x1 < i1; x1++) {
                for (int x2 = 0; x2 < i2; x2++) {
                    HCC[x1][x2].setFilter(i, filter);
                }
            }

            i++;
        }
    }

    /**
     * Obtain the composite filter from a file
     *
     * @param path
     * @return
     */
    static Mat getRF(String path) {
        String stList = FileUtils.readFile(new File(path));
        String lines[] = stList.split("\\n");
        ArrayList<Mat> kernelList = new ArrayList();
        for (String st : lines) {
            String values[] = st.split(" ");
            RF rf = new RF(Double.parseDouble(values[0]),
                    Double.parseDouble(values[1]),
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]),
                    Double.parseDouble(values[4]),
                    Double.parseDouble(values[5]),
                    values[6],
                    Integer.parseInt(values[7]));
            Mat kernel = new Mat();
            kernel = SpecialKernels.getAdvencedGauss(new Size(rf.size, rf.size), rf.intensity,
                    -rf.py + rf.size / 2, rf.px + rf.size / 2, rf.rx, rf.ry,
                    Math.toRadians(rf.angle + 90));
            kernelList.add(kernel);
        }
        Mat compKernel = Mat.zeros(kernelList.get(0).size(), CvType.CV_32FC1);
        for (Mat kn : kernelList) {
            Core.add(compKernel, kn, compKernel);
        }
        return compKernel;
    }

}
