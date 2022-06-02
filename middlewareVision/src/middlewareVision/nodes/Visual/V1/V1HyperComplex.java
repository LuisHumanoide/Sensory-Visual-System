package middlewareVision.nodes.Visual.V1;

import VisualMemory.Cell;
import static VisualMemory.V1Cells.HypercomplexCells.inc;
import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.CC;
import static VisualMemory.V1Cells.V1Bank.HCC;
import generator.ProcessList;
import gui.Visualizer;
import java.util.ArrayList;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import middlewareVision.config.AreaNames;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SpecialKernels;

/**
 *
 *
 */
public class V1HyperComplex extends Activity {

    float sigma = 0.47f * 2f;
    float inc = (float) (Math.PI / 4);
    int nFrame = 6 * Config.gaborOrientations;

    public V1HyperComplex() {
        this.ID = AreaNames.V1HyperComplex;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);

                if (spike.getModality() == Modalities.VISUAL) {

                    convolveHCC();

                    visualize();

                    Visualizer.lockLimit("HC");

                    LongSpike sendSpike = new LongSpike(Modalities.VISUAL, 0, 0, 0);

                    send(AreaNames.V2CurvatureCells, sendSpike.getByteArray());
                    send(AreaNames.V2AngularCells, sendSpike.getByteArray());
                }
                if (spike.getModality() == Modalities.ATTENTION) {
                    for (int index = 0; index < Config.gaborOrientations; index++) {
                        LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                        send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
                        visualize();
                    }
                }

            } catch (Exception ex) {

            }
        }
    }

    /**
     * Visualize <b> Hyper-Complex </b> cell results in the Visualizer
     */
    void visualize() {
        for (int j = 0; j < Config.gaborBanks; j++) {
            for (int k = 0; k <= Config.HCfilters; k++) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    if (k < Config.HCfilters) {
                        Visualizer.setImage(V1Bank.HCC[j][0].Cells[k][i].mat,
                                "end stopped L " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 1 + 2 * k + 2 * j * Config.gaborBanks, i, "HC");
                        Visualizer.setImage(V1Bank.HCC[j][1].Cells[k][i].mat,
                                "end stopped R " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 2 + 2 * k + 2 * j * Config.gaborBanks, i, "HC");

                        if (i == Config.gaborOrientations - 1) {
                            Visualizer.setImage(Functions.maxSum(V1Bank.HCC[j][0].Cells[k]),
                                    "end stopped L " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 1 + 2 * k + 2 * j * Config.gaborBanks, i + 2);
                            Visualizer.setImage(Functions.maxSum(V1Bank.HCC[j][1].Cells[k]),
                                    "end stopped R " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 2 + 2 * k + 2 * j * Config.gaborBanks, i + 2);
                        }
                    } else {
                        Visualizer.setImage(V1Bank.HCC[j][0].mergedCells[i].mat,
                                "end stopped L combined " + i + " bank " + j, Visualizer.getRow("CC") + 1 + 2 * k + 2 * j * Config.gaborBanks, i, "HC");
                        Visualizer.setImage(V1Bank.HCC[j][1].mergedCells[i].mat,
                                "end stopped L combined " + i + " bank " + j, Visualizer.getRow("CC") + 2 + 2 * k + 2 * j * Config.gaborBanks, i, "HC");
                    }
                }

            }
        }
    }

    /**
     * Make the convolution that result in the activation of HyperComplex Cells
     */
    void convolveHCC() {
        int i1 = HCC.length;
        int i2 = HCC[0].length;

        for (int x1 = 0; x1 < i1; x1++) {
            for (int x2 = 0; x2 < i2; x2++) {
                convolve(x1, x2, CC[x1][x2].Cells);
                mergeHCC(x1, x2);
            }
        }
    }

    /**
     * Merge the hyper complex cell in one map for each orientation
     * @param x1
     * @param x2 
     */
    void mergeHCC(int x1, int x2) {
        for (int j = 0; j < Config.gaborOrientations; j++) {
            V1Bank.HCC[x1][x2].mergedCells[j].mat = MatrixUtils.maxSum(V1Bank.HCC[x1][x2].mergedCells[j].previous);
        }
    }

    /**
     * Perform the filtering process for the Hyper Complex cells
     *
     * @param x1 is the index of the Gabor Bank
     * @param x2 is the eye
     * @param cell correspond to the simple or complex cell array
     */
    void convolve(int x1, int x2, Cell[] cell) {
        for (int i = 0; i < V1Bank.HCC[x1][x2].Cells.length; i++) {
            for (int j = 0; j < Config.gaborOrientations; j++) {
                float angle = j * inc;
                V1Bank.HCC[x1][x2].Cells[i][j].mat = Functions.filter(cell[j].mat, SpecialKernels.rotateKernelRadians(V1Bank.HCC[x1][x2].filters[i], angle));
            }
        }
    }

}
