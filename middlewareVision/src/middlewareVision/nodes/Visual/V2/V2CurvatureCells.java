package middlewareVision.nodes.Visual.V2;

import VisualMemory.V1Bank;
import VisualMemory.V2Bank;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.LongSpike;

/**
 *
 *
 */
public class V2CurvatureCells extends Activity {

    public V2CurvatureCells() {
        this.ID = AreaNames.V2CurvatureCells;
        this.namer = AreaNames.class;
    }

    int x0, x1, x2;

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (spike.getModality() == Modalities.VISUAL) {
                curvatureProcess();
                for (int k = 0; k < x1; k++) {
                    for (int i = 0; i < V2Bank.CurvC[0][0][0].getnCurvatures(); i++) {
                        Visualizer.setImage(V2Bank.CurvC[0][k][0].composedCells[i].mat, "curvature L radius:" + V2Bank.CurvC[0][0][0].filters[i][0].radius, Visualizer.getRow("HC") + k*x1+ 1, i);
                        Visualizer.setImage(V2Bank.CurvC[0][k][1].composedCells[i].mat, "curvature R radius:" + V2Bank.CurvC[0][0][0].filters[i][0].radius, Visualizer.getRow("HC") + k*x1+ 2, i);
                    }
                }
                Visualizer.addLimit("Curv", Visualizer.getRow("HC")+(x1-1)*x1+2);
            }
        } catch (Exception ex) {
            //Logger.getLogger(V2CurvatureCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void curvatureProcess() {
        x0 = V2Bank.CurvC.length;
        x1 = V2Bank.CurvC[0].length;
        x2 = V2Bank.CurvC[0][0].length;
        for (int i = 0; i < x0; i++) {
            for (int j = 0; j < x1; j++) {
                for (int k = 0; k < x2; k++) {
                    V2Bank.CurvC[i][j][k].filterCurvatureCells(V1Bank.CC[i][j][k].sumCell.mat);
                }
            }
        }
    }

}
