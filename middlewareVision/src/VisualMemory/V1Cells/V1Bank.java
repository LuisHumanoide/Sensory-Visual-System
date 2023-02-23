/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V1Cells;

import VisualMemory.MTCells.MTBank;
import java.io.File;
import middlewareVision.config.XMLReader;
import org.opencv.core.Mat;
import utils.Config;
import utils.FileUtils;
import utils.FilterUtils;
import utils.GaussianFilter;
import utils.GaborFilter;

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
    public static MotionCellsV1[][] MCΦ1;
    public static MotionCellsV1[][] MCΦ2;

    //[frequency][disparity index]
    public static StereoscopicCells[][] SSC;

    //[disparity index]
    public static StereoscopicMergedCells[] SMC;

    static String GaborFile = "RFV1//Gabor//filters.txt";
    static String DisparityFile = "ConfigFiles//Disparities.txt";
    static String HCfiles = "RFV1HC//";
    static String DOfiles = "RFDO//";

    //Double opponent kernels
    public static Mat D1Kernel;
    public static Mat D2Kernel;
    public static Mat K1Kernel;
    public static Mat K2Kernel;

    public static float D_ALPHA=3f; 
    public static float D_BETA=3f; 
    public static float K_ALPHA=2f; 
    public static float K_BETA=2f; 
    /**
     * Initialize all cells from V1
     */
    public static void initializeCells() {
        //Gabor lines are each line of the Gabor list file
        String gaborLines[] = FileUtils.fileLines(GaborFile);
        //the number of lines is the size of the Gabor banks
        Config.gaborBanks = gaborLines.length;

        //Read the disparity file
        String disparities[] = FileUtils.fileLines(DisparityFile);

        //folder of the hypercomplex filter template
        File hcfolder = new File(HCfiles);
        //read each hypercomplex filter template
        File hcfiles[] = hcfolder.listFiles();

        //set the values in the config class
        Config.HCfilters = hcfiles.length;
        Config.nDisparities = disparities.length;

        //reserve memory for cell banks
        //[number of double opponent cell types][number of eyes]
        DOC = new DoubleOpponentCells[1][2];
        //[number of gabor filter types][number of eyes]
        SC = new SimpleCells[gaborLines.length][2];
        CC = new ComplexCells[gaborLines.length][2];
        HCC = new HypercomplexCells[gaborLines.length][2];
        MC = new MotionCellsV1[gaborLines.length][2];
        MCΦ1= new MotionCellsV1[gaborLines.length][2];
        MCΦ2= new MotionCellsV1[gaborLines.length][2];

        //reserve memory for stereoscopic cell banks
        //[number of gabor filter types][number of disparities]
        SSC = new StereoscopicCells[gaborLines.length][disparities.length];
        SMC = new StereoscopicMergedCells[disparities.length];

        //Simple Mat to compute the motion difference in the retina
        motionDiff = new Mat();

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
        //for each Gabor filter type do
        for (int i2 = 0; i2 < gaborLines.length; i2++) {
            //for each eye do
            for (int i3 = 0; i3 < 2; i3++) {
                //create the simple cells
                SC[i2][i3] = new SimpleCells(Config.gaborOrientations);
                //complex cells
                CC[i2][i3] = new ComplexCells(Config.gaborOrientations);
                //hypercomplex
                HCC[i2][i3] = new HypercomplexCells(Config.HCfilters, Config.gaborOrientations);
                //Load the speeds file
                MC[i2][i3] = new MotionCellsV1("ConfigFiles/speeds.txt");
                MCΦ1[i2][i3]= new MotionCellsV1("ConfigFiles/speeds.txt");
                MCΦ2[i2][i3]= new MotionCellsV1("ConfigFiles/speeds.txt");
                //set the complex cells previous cells
                CC[i2][i3].setSimpleCells(SC[i2][i3]);
                //set the previous cells of the motion cells
                MC[i2][i3].setPrevious(CC[i2][i3].Cells);              
                MCΦ1[i2][i3].setPrevious(SC[i2][i3].Even);
                MCΦ2[i2][i3].setPrevious(SC[i2][i3].Odd);
            }
            //for each absolute disparity
            for (int i = 0; i < disparities.length; i++) {
                SSC[i2][i] = new StereoscopicCells(Integer.parseInt(disparities[i]), SC[i2]);
                if (i2 == 0) {
                    SMC[i] = new StereoscopicMergedCells(Integer.parseInt(disparities[i]));
                }
            }
        }

        //Initialize the cells from area MT
        MTBank.initializeComponentCells(MC[0][0].cells.length, MC[0][0].cells[0].length, Config.motionWidth);

        loadGaborFilters(gaborLines, 2);
        loadDOFilters();
        loadHCFilters(hcfiles);
    }

    /**
     * Load Gabor Filters from a file, the system can have several types of
     * gabor filters
     *
     * @param gaborLines number of gabor filter types
     * @param eyes number of eyes
     */
    public static void loadGaborFilters(String gaborLines[], int eyes) {
        int i = 0;
        //for each line
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
                    double angle = (Math.PI / (double) Config.gaborOrientations) * k;
                    SC[i][j].evenFilter[k] = evenFilter.makeFilter(angle);
                    SC[i][j].oddFilter[k] = oddFilter.makeFilter(angle);

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
            Mat filter = FilterUtils.getComposedFilter(fi.getPath());

            for (int x1 = 0; x1 < i1; x1++) {
                for (int x2 = 0; x2 < i2; x2++) {
                    HCC[x1][x2].setFilter(i, filter);
                }
            }

            i++;
        }
    }

    /**
     * Load the Gaussians corresponding to the Double Opponent Cells <br>
     * the Gaussians are edited in the Receptive Field List and are stored in
     * the folder <b> RFDO </b>. <br>
     * It also load the values corresponding to <br>
     * <code>D_ALPHA, D_BETA, K_ALPHA, K_BETA</code>
     */
    public static void loadDOFilters() {
        GaussianFilter[] OpponentD = FilterUtils.getGaussians("RFDO/OpponentD.txt");
        D1Kernel = FilterUtils.getDoubleOpponentKernel("D1", OpponentD);
        D2Kernel = FilterUtils.getDoubleOpponentKernel("D2", OpponentD);

        GaussianFilter[] OpponentK = FilterUtils.getGaussians("RFDO/OpponentK.txt");
        K1Kernel = FilterUtils.getDoubleOpponentKernel("K1", OpponentK);
        K2Kernel = FilterUtils.getDoubleOpponentKernel("K2", OpponentK);

        String values[] = XMLReader.getValuesFromXML("D_ALPHA", "D_BETA", "K_ALPHA", "K_BETA");
        D_ALPHA = Float.parseFloat(values[0]);
        D_BETA = Float.parseFloat(values[1]);
        K_ALPHA = Float.parseFloat(values[2]);
        K_BETA = Float.parseFloat(values[3]);
    }

}
