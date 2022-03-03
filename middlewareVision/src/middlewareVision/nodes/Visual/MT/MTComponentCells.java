package middlewareVision.nodes.Visual.MT;

import VisualMemory.MTCells.MTBank;
import static VisualMemory.MTCells.MTBank.MTCC;
import VisualMemory.V1Cells.V1Bank;
import gui.Visualizer;
import java.util.ArrayList;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;
import utils.MatrixUtils;
import static utils.Msg.print;

/**
 *
 *
 */
public class MTComponentCells extends Activity {

    public MTComponentCells() {
        this.ID = AreaNames.MTComponentCells;
        this.namer = AreaNames.class;
    }

    int eyes = 1;

    @Override
    public void init() {

    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (spike.getModality() == Modalities.VISUAL) {
                mergeV1MotionCells(0);
                opponentProcess(0);
                //eyes = MTBank.MTCC.length;

                visualize(0);

                Visualizer.lockLimit("mtComp");

                //Visualizer.setImageFull(MTBank.MTCC[0].CCells[1][0].mat, "test", Visualizer.getRow("v1motion") + 1, 6);
            }

        } catch (Exception ex) {
            Logger.getLogger(MTComponentCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void visualize(int x0) {
        int i1 = MTBank.MTCC[x0].CCells.length;
        int i2 = MTBank.MTCC[x0].CCells[0].length;
        for (int i = 0; i < i1; i++) {
            for (int j = 0; j < i2; j++) {
                if (j < Config.gaborOrientations) {
                    Visualizer.setImageFull(MTCC[x0].CCells[i][j].mat, "Component cell " + i + "  " + j, 2 + Visualizer.getRow("v1motion") + (i * eyes * 2) + x0,
                            Config.gaborOrientations + 2 + j, "mtComp");
                } else {
                    Visualizer.setImageFull(MTCC[x0].CCells[i][j].mat, "Component cell " + i + "  " + j, 2 + Visualizer.getRow("v1motion") + (1 + i * eyes * 2) + x0,
                            Config.gaborOrientations + 2 + (j - Config.gaborOrientations), "mtComp");
                }
            }
        }
    }

    /**
     * Merge the speed cells from <b>V1</b> into <b>MT</b> Component Cells<br>
     * The different spatial frequencies from V1 Cells example: <br><p>
     * <i>V1[freq][eye].MotionCell[Speed][Orientation] </i><br>
     * will merge into <br>
     * <i> MT[eye].ComponentCell[speed][orientation] </i>
     *
     * @param eye is the eye
     */
    public void mergeV1MotionCells(int eye) {
        ArrayList<Mat> arrayMat = new ArrayList();

        for (int j = 0; j < MTCC[0].CCells.length; j++) {
            for (int k = 0; k < MTCC[0].CCells[0].length; k++) {

                for (int i = 0; i < V1Bank.MC.length; i++) {
                    arrayMat.add(V1Bank.MC[i][eye].cells[j][k].mat);
                }

                Imgproc.resize(MatrixUtils.maxSum(arrayMat), MTCC[eye].CCells[j][k].eMat, MTCC[eye].CCells[j][k].eMat.size());
                arrayMat.clear();
            }
        }
    }

    /**
     * Merge the speed cells from <b>V1</b> into <b>MT</b> Component Cells for
     * <b> both eyes</b><br>
     * The different spatial frequencies from V1 Cells example: <br><p>
     * <i>V1[freq][eye].MotionCell[Speed][Orientation] </i><br>
     * will merge into <br>
     * <i> MT[eye].ComponentCell[speed][orientation] </i>
     *
     */
    public void mergeV1MotionCells() {
        mergeV1MotionCells(0);
        mergeV1MotionCells(1);
    }

    public void opponentProcess(int eye) {
        for (int i = 0; i < MTCC[0].CCells.length; i++) {
            for (int j = 0; j < MTCC[0].CCells[0].length; j++) {

                Core.subtract(MTCC[eye].CCells[i][j % (Config.gaborOrientations * 2)].eMat,
                        MTCC[eye].CCells[i][(j + Config.gaborOrientations) % (Config.gaborOrientations * 2)].eMat,
                        MTCC[eye].CCells[i][j % (Config.gaborOrientations * 2)].mat);

            }
        }
    }
    
    public void opponentProcess(){
        opponentProcess(0);
        opponentProcess(1);
    }

}
