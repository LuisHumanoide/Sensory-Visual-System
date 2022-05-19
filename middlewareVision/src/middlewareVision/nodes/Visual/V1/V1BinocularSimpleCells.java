package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.StereoscopicCells;
import VisualMemory.V1Cells.V1Bank;
import spike.Location;
import generator.ProcessList;
import gui.Visualizer;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.Config;
import utils.Functions;
import utils.LongSpike;

/**
 *
 *
 */
public class V1BinocularSimpleCells extends Activity {

    public V1BinocularSimpleCells() {
        this.ID = AreaNames.V1BinocularSimpleCells;
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

                    sumAll();
                    
                    visualize();
                    
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    send(AreaNames.V1BinocularComplexCells, sendSpike1.getByteArray());
                }

            } catch (Exception ex) {
                Logger.getLogger(V1BinocularSimpleCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void visualize() {
        Visualizer.setImage(V1Bank.SSC[0][0].composedEvenCell.mat, "disp test", 3, 5);
        Visualizer.setImage(V1Bank.SSC[0][4].composedEvenCell.mat, "disp test", 4, 5);
        Visualizer.setImage(V1Bank.SSC[0][0].composedOddCell.mat, "disp test", 3, 6);
        Visualizer.setImage(V1Bank.SSC[0][4].composedOddCell.mat, "disp test", 4, 6);
    }

    void sumAll() {
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < Config.nDisparities; j++) {
                sum(V1Bank.SSC[i][j]);
            }
        }
    }

    void sum(StereoscopicCells sc) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            sc.evenCells[i].mat = Functions.disparitySum(sc.evenCells[i].previous[0].mat, sc.evenCells[i].previous[1].mat, sc.disparity,2);
            sc.oddCells[i].mat = Functions.disparitySum(sc.oddCells[i].previous[0].mat, sc.oddCells[i].previous[1].mat, sc.disparity,2);
        }
        sc.composedEvenCell.mat=Functions.maxSum(sc.evenCells);
        sc.composedOddCell.mat=Functions.maxSum(sc.oddCells);
    }

}
