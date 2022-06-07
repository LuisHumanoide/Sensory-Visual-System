package middlewareVision.nodes.Visual.V2;

import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import generator.ProcessList;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Functions;
import utils.LongSpike;

/**
 *
 *
 */
public class V2CurvatureCells extends Activity {

    public V2CurvatureCells() {
        this.ID = AreaNames.V2CurvatureCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    int x0, x1, x2;

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                if (spike.getModality() == Modalities.VISUAL) {

                    curvatureProcess();

                    visualize();
                    Visualizer.lockLimit("Curv");

                    //send(AreaNames.V4ShapeActivationNode, null);

                }
            } catch (Exception ex) {
                //Logger.getLogger(V2CurvatureCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Visualize the curvature activations in the visualizer
     */
    void visualize() {
        for (int k = 0; k < x1; k++) {
            for (int i = 0; i < V2Bank.CurvC[0][0].getnCurvatures(); i++) {
                Visualizer.setImage(V2Bank.CurvC[k][0].composedCells[i].mat, "curvature L radius:" + V2Bank.CurvC[0][0].filters[i][0].radius, Visualizer.getRow("AC") + k * x1 + 1, i, "Curv");
                Visualizer.setImage(V2Bank.CurvC[k][1].composedCells[i].mat, "curvature R radius:" + V2Bank.CurvC[0][0].filters[i][0].radius, Visualizer.getRow("AC") + k * x1 + 2, i, "Curv");
            }
        }
    }

    /**
     * Performs the curvature process for all cells
     */
    void curvatureProcess() {
        x1 = V2Bank.CurvC.length;
        x2 = V2Bank.CurvC[0].length;

        for (int j = 0; j < x1; j++) {
            for (int k = 0; k < x2; k++) {
                filterCurvatureCells(j, k, V1Bank.CC[j][k].sumCell.mat);
            }
        }
    }

    /**
     * Perform the <b>curvature process</b> for an specific index of Gabor
     * filter and eye
     *
     * @param x1 correspond to the index of Gabor filter
     * @param x2 correspond to the eye
     * @param src is the source matrix in witch the process will perform
     */
    void filterCurvatureCells(int x1, int x2, Mat src) {
        for (int i = 0; i < V2Bank.CurvC[x1][x2].getnCurvatures(); i++) {
            for (int j = 0; j < V2Bank.CurvC[x1][x2].getnAngleDivisions(); j++) {
                V2Bank.CurvC[x1][x2].cells[i][j].mat = Functions.curvatureFiltering(src, V2Bank.CurvC[x1][x2].filters[i][j], true);
                if (x2 == 0) {
                    V2Bank.CurvC[x1][x2].cells[i][j].setLabel("c" + x1 + "-" + i + "" + j, 0);
                }
                if (x2 == 1) {
                    V2Bank.CurvC[x1][x2].cells[i][j].setLabel("c" + x1 + "-" + i + "" + j, 1);
                }

            }
            V2Bank.CurvC[x1][x2].composedCells[i].mat = Functions.maxSum(V2Bank.CurvC[x1][x2].cells[i]);
        }
    }

}
