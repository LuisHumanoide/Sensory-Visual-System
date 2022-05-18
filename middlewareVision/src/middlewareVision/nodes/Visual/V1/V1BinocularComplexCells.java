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
public class V1BinocularComplexCells extends Activity {


    public V1BinocularComplexCells() {
        this.ID = AreaNames.V1BinocularComplexCells;
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
                    
                    energyProcessAll();
                    
                    visualize();                                    
                    
                }

            } catch (Exception ex) {
                Logger.getLogger(V1BinocularComplexCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    void visualize() {
        Visualizer.setImage(V1Bank.SSC[0][0].composedComplexCell.mat, "disp test complex", 3, 7);
        Visualizer.setImage(V1Bank.SSC[0][1].composedComplexCell.mat, "disp test complex", 4, 7);
        Visualizer.setImage(V1Bank.SSC[0][2].composedComplexCell.mat, "disp test complex", 5, 7);
        Visualizer.setImage(V1Bank.SSC[0][3].composedComplexCell.mat, "disp test complex", 6, 7);
        Visualizer.setImage(V1Bank.SSC[0][4].composedComplexCell.mat, "disp test complex", 7, 7);
    }
    
    void energyProcessAll(){
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < Config.nDisparities; j++) {
                energyProcess(V1Bank.SSC[i][j]);
            }
        }
    }
    
    
    void energyProcess(StereoscopicCells sc){
        for (int i = 0; i < Config.gaborOrientations; i++) {
            sc.complexCells[i].mat=Functions.energyProcess(sc.complexCells[i].previous[0].mat, sc.complexCells[i].previous[1].mat);
        }
        sc.composedComplexCell.mat=Functions.maxSum(sc.complexCells);
    }


}
