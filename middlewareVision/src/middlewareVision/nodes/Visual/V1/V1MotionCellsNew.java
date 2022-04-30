package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.MC;
import generator.ProcessList;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;

/**
 *
 *
 */
public class V1MotionCellsNew extends Activity {

    int i1 = 1;
    int i2 = 1;
    int i3;

    public V1MotionCellsNew() {
        this.ID = AreaNames.V1MotionCellsNew;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {
        utils.Msg.print("big node v1MotionCellsNew");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                if (spike.getModality() == Modalities.VISUAL) {

                    for (int j = 0; j < i1; j++) {
                        for (int k = 0; k < i2; k++) {
                            motionProcess(j, k);
                        }
                    }

                    for (int j = 0; j < i1; j++) {
                        for (int k = 0; k < i2; k++) {
                            visualize(j, k);
                        }
                    }

                    Visualizer.lockLimit("v1motion");

                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    send(AreaNames.MTComponentCells, sendSpike1.getByteArray());

                }

            } catch (Exception ex) {
                Logger.getLogger(V1MotionCellsNew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /**
     * Visualize V1 Motion Cells<br>
     * where the first column corresponds to one speed<br>
     * and the second column correspond to the opposite speed
     *
     * @param x1 number of gabor filters loaded
     * @param x2 number of eyes
     */
    public void visualize(int x1, int x2) {
        i3 = V1Bank.MC[0][0].cells.length;
        for (int i = 0; i < i3; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                if (j < Config.gaborOrientations) {
                    Visualizer.setImage(MC[x1][x2].cells[i][j].mat, "motion V1", (i1 * i2 * x1) + (i * i2 * 2) + x2, Config.gaborOrientations + 2 + j, "v1motion");
                } else {
                    Visualizer.setImage(MC[x1][x2].cells[i][j].mat, "opposite motion V1", (i1 * i2 * x1) + (1 + i * i2 * 2) + x2,
                            Config.gaborOrientations + 2 + (j - Config.gaborOrientations), "v1motion");
                }
            }
        }
    }

    /**
     * Performs the motion detection, based on <b> Reichardt Detectors</b>
     * <ul>
     * <li> First: A process where the multiplication of different delayed
     * matrixes is performed</li>
     * <li> Second: A subtraction of the opponent speeds is done</li>
     * </ul>
     * A link that describes the Reitchard detector is the following :
     * <a href="https://www.jneurosci.org/content/30/34/11300">
     * Link</a>
     */
    public void motionProcess(int i1, int i2) {
        for (int i = 0; i < MC[i1][i2].cells.length; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                MC[i1][i2].cells[i][j].cellMotionProcess(MC[i1][i2].cells[i][j % Config.gaborOrientations].previous[0].mat);
                //performs the substraction 
                Core.subtract(MC[i1][i2].cells[i][j % (Config.gaborOrientations * 2)].mat1st, MC[i1][i2].cells[i][(j + Config.gaborOrientations) % (Config.gaborOrientations * 2)].mat1st,
                        MC[i1][i2].cells[i][j % (Config.gaborOrientations * 2)].mat);
            }
        }
        //MC[i1][i2].createComposedCells();
    }

}
