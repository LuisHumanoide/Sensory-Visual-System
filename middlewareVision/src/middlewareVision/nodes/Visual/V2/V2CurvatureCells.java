package middlewareVision.nodes.Visual.V2;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import generator.ProcessList;
import gui.Visualizer;
import spike.Location;
import cFramework.nodes.process.Process;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import spike.Modalities;
import utils.CurvatureFilter;
import utils.Functions;
import utils.LongSpike;
import utils.SpecialKernels;

/**
 *
 *
 */
public class V2CurvatureCells extends Process {

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
    public void receive(long nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                if (spike.getModality() == Modalities.VISUAL) {

                    curvatureProcess();

                    visualize();
                    Visualizer.lockLimit("Curv");

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
        /*for(int i=0;i<V2Bank.CurvC[0][0].getnAngleDivisions();i++){
            isualizer.setImage(V2Bank.CurvC[0][0].cells[0][i].mat, "curvature L radius:" + V2Bank.CurvC[0][0].filters[0][0].radius, 18, i, "Curv");
        }*/
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
                V2Bank.CurvC[x1][x2].cells[i][j].mat = curvatureFiltering(src, V2Bank.CurvC[x1][x2].filters[i][j]);
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
    
        /**
     * Perform the curvature filtering <br>
     * the model can be seen in the paper <br>
     * <i>10.3389/fncom.2013.00067 </i><br><br>
     *
     * It consist in Gabor Filters arranged in a curvature trajectory<br>
     * there is a convex and concave trajectory<br>
     * the activations of the filters are multiplied and <br>
     * there is a difference between the concave and convex results
     *
     * @param src the original image to filter
     * @param cFilter the curvature filter class
     * @param convex if it's necessary to subtract the convex result
     * @return the activation matrix corresponding to an specific curvature in
     * an specific orientation
     */
    public static Mat curvatureFiltering(Mat src, CurvatureFilter cFilter) {

        Mat concaveFiltered[];

        concaveFiltered = new Mat[cFilter.n];

        Mat concaveResult = Mat.zeros(src.rows(), src.cols(), src.type());

        Core.add(concaveResult, Scalar.all(1), concaveResult);

        for (int i = 0; i < cFilter.n; i++) {
            concaveFiltered[i] = Functions.filter2(src, SpecialKernels.rotateKernelRadians(cFilter.concaveFilters[i], cFilter.angle));
            Core.multiply(concaveFiltered[i], Scalar.all(cFilter.mul), concaveFiltered[i]);
            concaveResult = concaveResult.mul(concaveFiltered[i]);

        }
        return concaveResult;
    }

}
