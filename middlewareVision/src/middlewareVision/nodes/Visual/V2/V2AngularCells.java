/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.V2;

import VisualMemory.Cell;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import generator.ProcessList;
import spike.Location;
import gui.FrameActivity;
import gui.Visualizer;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SpecialKernels;
import utils.numSync;

/**
 *
 * @author HumanoideFilms
 */
public class V2AngularCells extends FrameActivity {

    /**
     * Initial arrays
     */
    public Mat[] ors;
    // public Mat[] angleMats;
    //Mat kernels[];
    public Mat filtered[];

    /**
     * 2D array of each angular combination
     */
    // public Mat v2map[][];
    public V2AngularCells() {
        this.ID = AreaNames.V2AngularCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);

    }

    @Override
    public void init() {
        //SimpleLogger.log(this, "SMALL NODE CortexV2");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        LongSpike spike;
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                spike = new LongSpike(data);
                if (spike.getModality() == Modalities.VISUAL) {

                    angularProcess();
                    
                    visualize();
                    
                    Visualizer.lockLimit("AC");

                }

                if (spike.getModality() == Modalities.ATTENTION) {

                }

            } catch (Exception ex) {
                Logger.getLogger(V2AngularCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void visualize() {
        for (int x0 = 0; x0 < Config.gaborBanks; x0++) {
            for (int i = 0; i < V2Bank.AC[0][0].mergedAC.length; i++) {
                Visualizer.setImage(V2Bank.AC[x0][0].mergedAC[i], "Angular Cells L", Visualizer.getRow("HC")+1, i, "AC");
                Visualizer.setImage(V2Bank.AC[x0][1].mergedAC[i], "Angular Cells R", Visualizer.getRow("HC")+2, i, "AC");
            }
        }
    }

    /**
     * do the angular process
     */
    public void angularProcess() {
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < 2; j++) {
                Mat filtered[] = filterMatrix(V1Bank.HCC[i][j].mergedCells);
                angularActivation(i, j, filtered);
                V2Bank.AC[i][j].mergeCells();
                mergeCells(i, j);
            }
        }
    }

    public void mergeCells(int x1, int x2) {
        for (int i = 0; i < V2Bank.AC[x1][x2].mergedAC.length; i++) {
            V2Bank.AC[x1][x2].mergedAC[i] = MatrixUtils.maxSum(V2Bank.AC[x1][x2].Cells[i]);
        }
    }
    /**
     * Increment value
     */
    float inc = (float) (Math.PI / Config.gaborOrientations);
    /**
     * Value of the width of Gabor function
     */
    float sigma = 0.47f * 2f;

    /**
     * Generate the filtered matrix by applying a convolution
     *
     * @param ors
     */
    public Mat[] filterMatrix(Cell[] ors) {
        filtered = new Mat[Config.gaborOrientations * 2];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            filtered[i] = ors[i].mat.clone();
            filtered[i + Config.gaborOrientations] = ors[i].mat.clone();
        }
        for (int i = 0; i < Config.gaborOrientations * 2; i++) {
            Imgproc.filter2D(filtered[i], filtered[i], CV_32F, SpecialKernels.v2Kernels[i]);
            Imgproc.threshold(filtered[i], filtered[i], 0, 1, Imgproc.THRESH_TOZERO);
        }
        return filtered;
    }
    /**
     * value used to adjust the filters for scooping
     */
    double value = -0.1;
    double l3 = 0.02;

    /**
     * multiply the matrixes for generating the activation map
     */
    public void angularActivation(int x1, int x2, Mat[] filtered) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                V2Bank.AC[x1][x2].Cells[i][j].mat = Functions.V2Activation(filtered[j], filtered[(i + j + 1) % (Config.gaborOrientations * 2)], l3);
                V2Bank.AC[x1][x2].Cells[i][j].setPrevious(
                        V1Bank.HCC[x1][x2].mergedCells[j % Config.gaborOrientations],
                        V1Bank.HCC[x1][x2].mergedCells[((i + j + 1) % (Config.gaborOrientations * 2)) % 4]);
            }
        }
    }

}
