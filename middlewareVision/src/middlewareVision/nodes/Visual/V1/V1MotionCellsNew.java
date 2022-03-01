package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.MC;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;

/**
 *
 *
 */
public class V1MotionCellsNew extends Activity {

    int i0 = 1;
    int i1 = 1;
    int i2 = 1;
    int i3;

    public V1MotionCellsNew() {
        this.ID = AreaNames.V1MotionCellsNew;
        this.namer = AreaNames.class;
    }

    @Override
    public void init() {
        utils.Msg.print("big node v1MotionCellsNew");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (spike.getModality() == Modalities.VISUAL) {
                motionProcess();
                for (int i = 0; i < i0; i++) {
                    for (int j = 0; j < i1; j++) {
                        for (int k = 0; k < i2; k++) {
                            visualize(i, j, k);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(V1MotionCellsNew.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void visualize(int x0, int x1, int x2) {
        i3 = V1Bank.MC[0][0].cells.length;
        for (int i = 0; i < i3; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                if (j < Config.gaborOrientations) {
                    Visualizer.setImage(MC[x1][x2].cells[i][j].mat, "motion", (i1 * i2 * x1) + (i * i2 * 2) + x2, 6 + j);
                } else {
                    Visualizer.setImage(MC[x1][x2].cells[i][j].mat, "motion", (i1 * i2 * x1) + (1 + i * i2 * 2) + x2, 6 + (j - Config.gaborOrientations));
                }
            }
        }
    }

    public void motionProcess() {
        /*int i0 = MC.length;
        int i1 = MC[0].length;
        int i2 = MC[0][0].length;*/

        for (int j = 0; j < i1; j++) {
            for (int k = 0; k < i2; k++) {
                MC[j][k].motionProcess();
            }
        }

    }

}
