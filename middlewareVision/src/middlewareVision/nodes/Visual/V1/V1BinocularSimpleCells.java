package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.StereoscopicCells;
import VisualMemory.V1Cells.V1Bank;
import spike.Location;
import generator.ProcessList;
import gui.Visualizer;
import cFramework.nodes.process.Process;
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
public class V1BinocularSimpleCells extends Process {

    public V1BinocularSimpleCells() {
        this.ID = AreaNames.V1BinocularSimpleCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(long nodeID, byte[] data) {
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
    int bankIndex=0;
    void visualize() {
        for(int i=0;i<Config.nDisparities;i++){
            Visualizer.setImage(V1Bank.SSC[bankIndex][i].composedEvenCell.mat, "Simple Even Cell " + i, i, 10);
            Visualizer.setImage(V1Bank.SSC[bankIndex][i].composedOddCell.mat, "Odd Even Cell "+ i, i, 11);
        }
    }

    /**
     * performs the summation process of monocular simple cells for creating <br>
     * the binocular simple cells <br>
     * The process is performed for all cells
     */
    void sumAll() {
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < Config.nDisparities; j++) {
                sum(V1Bank.SSC[i][j]);
            }
        }
    }
    
    /**
     * Performs the binocular summation process for specific simple cells <br>
     * we can choose 2 disparity summation operations <br>
     * a multiplication or a summation with a pow
     * @param sc the stereoscopic cell bank with a specific disparity and frequency
     */
    void sum(StereoscopicCells sc) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            sc.evenCells[i].mat = Functions.disparityOperation(sc.evenCells[i].previous[0].mat, sc.evenCells[i].previous[1].mat, sc.disparity, Config.sccn, Functions.DISP_OP_MUL);
            sc.oddCells[i].mat = Functions.disparityOperation(sc.oddCells[i].previous[0].mat, sc.oddCells[i].previous[1].mat, sc.disparity, Config.sccn, Functions.DISP_OP_MUL);
        }
        //creates the composed cells, which are a combination of the n orientation binocular cells
        sc.composedEvenCell.mat=Functions.maxSum(sc.evenCells);
        sc.composedOddCell.mat=Functions.maxSum(sc.oddCells);
    }

}
