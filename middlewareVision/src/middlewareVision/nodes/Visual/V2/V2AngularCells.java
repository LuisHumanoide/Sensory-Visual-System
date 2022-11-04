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
import gui.Visualizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import middlewareVision.config.AreaNames;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Functions;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SpecialKernels;

/**
 *
 * @author HumanoideFilms
 */
public class V2AngularCells extends Activity {

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

                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);

                    send(AreaNames.V4SimpleShapeCells, sendSpike1.getByteArray());
                    send(AreaNames.V2CornerMotion, sendSpike1.getByteArray());

                }

                if (spike.getModality() == Modalities.ATTENTION) {
                    Location l = (Location) spike.getLocation();
                    if (l.getValues()[0] == 0) {
                        
                    }
                    LongSpike sendSpike2 = new LongSpike(Modalities.ATTENTION, new Location(1), 0, 0);
                    send(AreaNames.V1HyperComplex, sendSpike2.getByteArray());
                    mergeAllCells();
                    visualize();

                }

            } catch (Exception ex) {
                Logger.getLogger(V2AngularCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void visualize() {
        for (int x0 = 0; x0 < Config.gaborBanks; x0++) {
            for (int i = 0; i < V2Bank.AC[0][0].mergedAC.length; i++) {
                Visualizer.setImage(V2Bank.AC[x0][0].mergedAC[i], "Angular Cells L", Visualizer.getRow("HC") + 1, i, "AC");
                Visualizer.setImage(V2Bank.AC[x0][1].mergedAC[i], "Angular Cells R", Visualizer.getRow("HC") + 2, i, "AC");
            }
        }
        /* for (int i = 0; i < V2Bank.AC[0][0].Cells[0].length; i++) {
                Visualizer.setImage(V2Bank.AC[0][0].Cells[3][i].mat, "Angular Cells L", Visualizer.getRow("HC")+1, i, "AC");
                //Visualizer.setImage(V2Bank.AC[0][1].mergedAC[i], "Angular Cells R", Visualizer.getRow("HC")+2, i, "AC");
            }*/
    }

    /**
     * Process to perform angular activation for all V2 cells.
     */
    public void angularProcess() {
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < 2; j++) {
                //Obtain the filtered Matrixes by an angular activation
                Mat filtered[] = filterMatrix(V1Bank.HCC[i][j].mergedCells);
                angularActivation(i, j, filtered);
                V2Bank.AC[i][j].mergeCells();
                mergeCells(i, j);
            }
        }
    }
    
    /**
     * Process to perform angular activation for all V2 cells.
     */
    public void mergeAllCells() {
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < 2; j++) {
                mergeCells(i, j);
            }
        }
    }

    /**
     * Merge all angular cells with the same aperture and different
     * direction<br>
     * in order to visualize better
     *
     * @param x1 gabor bank
     * @param x2 eye
     */
    public void mergeCells(int x1, int x2) {
        for (int i = 0; i < V2Bank.AC[x1][x2].mergedAC.length; i++) {
            V2Bank.AC[x1][x2].mergedAC[i] = MatrixUtils.maxSum(V2Bank.AC[x1][x2].Cells[i]);
        }
    }

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
    double l3 = 1000;

    /**
     * multiply the matrixes for generating the activation map
     */
    public void angularActivation(int x1, int x2, Mat[] filtered) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                V2Bank.AC[x1][x2].Cells[i][j].mat = Functions.V2Activation(filtered[j], filtered[(i + j + 1) % (Config.gaborOrientations * 2)], l3);

                if (x2 == 0) {
                    V2Bank.AC[x1][x2].Cells[i][j].setLabel("a" + x1 + "-" + i + "" + j, 0);
                }
                if (x2 == 1) {
                    V2Bank.AC[x1][x2].Cells[i][j].setLabel("a" + x1 + "-" + i + "" + j, 1);
                }

                V2Bank.AC[x1][x2].Cells[i][j].setPrevious(
                        V1Bank.HCC[x1][x2].mergedCells[j % Config.gaborOrientations],
                        V1Bank.HCC[x1][x2].mergedCells[((i + j + 1) % (Config.gaborOrientations * 2)) % 4]);
            }
        }
    }

}
