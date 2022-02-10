/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import MiniPrograms.RF;
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

    //[extra][for different frequencies or scales][eye]
    public static SimpleCells[][][] SC;
    public static ComplexCells[][][] CC;
    public static HypercomplexCells[][][] HCC;
    public static DoubleOpponentCells[][][] DOC;
    static String fileName = "RFV1//Gabor//filters.txt";
    static String HCfiles = "RFV1HC//";

    /**
     * Initialize all cells from V1
     * @param dimensions 
     */
    public static void initializeCells(int... dimensions) {
        File file = new File(fileName);
        String fileContent = FileUtils.readFile(file);
        String gaborLines[] = fileContent.split("\\n");
        SC = new SimpleCells[dimensions[0]][gaborLines.length][dimensions[2]];
        Config.gaborBanks = gaborLines.length;
        File hcfolder = new File(HCfiles);
        File hcfiles[] = hcfolder.listFiles();
        Config.HCfilters = hcfiles.length;

        CC = new ComplexCells[dimensions[0]][gaborLines.length][dimensions[2]];
        HCC = new HypercomplexCells[dimensions[0]][gaborLines.length][dimensions[2]];
        DOC = new DoubleOpponentCells[dimensions[0]][dimensions[1]][dimensions[2]];

        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < dimensions[1]; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    DOC[i1][i2][i3] = new DoubleOpponentCells(Config.gaborOrientations);
                }
            }
        }
        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < gaborLines.length; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    SC[i1][i2][i3] = new SimpleCells(Config.gaborOrientations);
                    CC[i1][i2][i3] = new ComplexCells(Config.gaborOrientations);
                    HCC[i1][i2][i3] = new HypercomplexCells(Config.HCfilters, Config.gaborOrientations);
                    CC[i1][i2][i3].setSimpleCells(SC[i1][i2][i3]);
                }
            }
        }
        loadGaborFilters(gaborLines, 2, 0);
        loadHCFilters(hcfiles);
    }

    /**
     * Load Gabor Filters from a file, the system can have several types of gabor filters
     * @param gaborLines
     * @param eyes
     * @param index1 
     */
    public static void loadGaborFilters(String gaborLines[], int eyes, int index1) {
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
                    SC[index1][i][j].evenFilter[k] = SpecialKernels.rotateKernelRadians(evenFilter.makeFilter(), k * SpecialKernels.inc);
                    SC[index1][i][j].oddFilter[k] = SpecialKernels.rotateKernelRadians(oddFilter.makeFilter(), k * SpecialKernels.inc);
                    SC[index1][i][j].geven = evenFilter;
                    SC[index1][i][j].godd = oddFilter;

                }
            }
            i++;

        }
    }

    /**
     * Load the Gauss filters that emulates the end stop-filters from the hypercomplex cells
     * and load into the HCC class
     * @param files 
     */
    public static void loadHCFilters(File[] files) {
        int i = 0;
        int i0 = HCC.length;
        int i1 = HCC[0].length;
        int i2 = HCC[0][0].length;
        for (File fi : files) {
            Mat filter = getRF(fi.getPath());
            for (int x0 = 0; x0 < i0; x0++) {
                for (int x1 = 0; x1 < i1; x1++) {
                    for (int x2 = 0; x2 < i2; x2++) {
                        HCC[x0][x1][x2].setFilter(i, filter);
                    }
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
