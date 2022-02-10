package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Bank;
import static VisualMemory.V1Bank.CC;
import static VisualMemory.V1Bank.HCC;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;

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
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (spike.getModality() == Modalities.VISUAL) {
                convolveHCC();
                for (int j = 0; j < Config.gaborBanks; j++) {
                    for (int k = 0; k < Config.HCfilters; k++) {
                        for (int i = 0; i < Config.gaborOrientations; i++) {
                            Visualizer.setImage(V1Bank.HCC[0][j][0].Cells[k][i].mat,
                                    "end stopped L " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 1 + 2 * k + 2 * j * Config.gaborBanks, i);
                            Visualizer.setImage(V1Bank.HCC[0][j][1].Cells[k][i].mat,
                                    "end stopped R " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 2 + 2 * k + 2 * j * Config.gaborBanks, i);

                            if (i == Config.gaborOrientations - 1) {
                                Visualizer.setImage(Functions.maxSum(V1Bank.HCC[0][j][0].Cells[k]),
                                        "end stopped L " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 1 + 2 * k + 2 * j * Config.gaborBanks, i + 2);
                                Visualizer.setImage(Functions.maxSum(V1Bank.HCC[0][j][1].Cells[k]),
                                        "end stopped R " + i + " bank " + j + " HC Filter " + k, Visualizer.getRow("CC") + 2 + 2 * k + 2 * j * Config.gaborBanks, i + 2);
                            }
                        }

                    }
                }
                Visualizer.addLimit("HC", Visualizer.getRow("CC") + 2 + 2 * (Config.HCfilters - 1) + 2 * (Config.gaborBanks - 1) * Config.gaborBanks);
                LongSpike sendSpike = new LongSpike(Modalities.VISUAL, 0, 0, 0);
                send(AreaNames.V2CurvatureCells, sendSpike.getByteArray());
            }
            if (spike.getModality() == Modalities.ATTENTION) {
                for (int index = 0; index < Config.gaborOrientations; index++) {
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                    send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
                    send(AreaNames.V4Contour, sendSpike1.getByteArray());
                    Visualizer.setImage(Convertor.Mat2Img(V1Bank.HCC[0][0][0].Cells[0][index].mat), "end stopped " + index, nFrame * 2 + index);
                }
            }

        } catch (Exception ex) {

        }
    }

    /**
     * Make the convolution that result in the activation of HyperComplex Cells
     */
    void convolveHCC() {
        int i0 = HCC.length;
        int i1 = HCC[0].length;
        int i2 = HCC[0][0].length;
        for (int x0 = 0; x0 < i0; x0++) {
            for (int x1 = 0; x1 < i1; x1++) {
                for (int x2 = 0; x2 < i2; x2++) {
                    HCC[x0][x1][x2].convolve(CC[x0][x1][x2].Cells);
                }
            }
        }
    }

    /*
    public void oldcode(){
        Mat edges = V1Bank.CC[0][0][0].Cells[index].mat;
                Mat endStop;
                //ilusoryEdges = elongatedGaborFilter(edges, sigma * 0.5f, 1, 5, 29, 0.05, index);
                endStop = endStopped(edges, index);
                Core.multiply(endStop, new Scalar(-0.02), endStop);
                //ilusoryEdges = MatrixUtils.maxSum(ilusoryEdges, edges);
                Core.add(edges, endStop, endStop);
                Imgproc.threshold(endStop, endStop, 0.4, 1, Imgproc.THRESH_TOZERO);
                double w = Config.endstop;
                Core.addWeighted(edges, w, endStop, 1 - w, 0, endStop);
                V1Bank.HCC[0][0][0].Cells[0][index].mat = endStop;
                V4Memory.v1Map[index] = endStop;
                LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(index, 4), 0, 0);
    }*/
}
