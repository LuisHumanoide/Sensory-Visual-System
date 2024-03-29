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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Functions;
import utils.LongSpike;
import utils.MatrixUtils;

/**
 *
 * 
 */
public class V1BinocularComplexCells extends Process {


    public V1BinocularComplexCells() {
        this.ID = AreaNames.V1BinocularComplexCells;
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
                    
                    energyProcessAll();
                    
                    divisiveNormalizationAll();
                    
                    visualize();
                    
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    
                    send(AreaNames.V1BinocularMergeProcess, sendSpike1.getByteArray());
                    //send(AreaNames.V3DisparityRange, sendSpike1.getByteArray());
                    
                }

            } catch (Exception ex) {
                Logger.getLogger(V1BinocularComplexCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    int bankIndex=0;
    void visualize() {
       for(int i=0;i<Config.nDisparities;i++){
            Visualizer.setImage(V1Bank.SSC[bankIndex][i].composedComplexCell.mat, "complex Cell", i, 12);
            Visualizer.setImage(V1Bank.SSC[bankIndex][i].composedNormalizedCell.mat, "Normalized cell", i, 13);
        }
    }
    
    /**
     * Performs the binocular energy process for all cells
     */
    void energyProcessAll(){
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < Config.nDisparities; j++) {
                energyProcess(V1Bank.SSC[i][j]);
            }
        }
    }
    
    /**
     * Performs the binocular energy process for one specific cells with one specific disparity <br>
     * and frequency
     * @param sc is the stereoscopic cell
     */
    void energyProcess(StereoscopicCells sc){
        for (int i = 0; i < Config.gaborOrientations; i++) {
            sc.complexCells[i].mat=Functions.sumPowProcess(sc.complexCells[i].previous[0].mat, sc.complexCells[i].previous[1].mat,2);
        }
        //create the composed cell, a combination of n orientation disparity cells
        sc.composedComplexCell.mat=Functions.maxSum(sc.complexCells);
    }
    
    /**
     * Performs the divisive normalization process for all cells<br>
     * divisive normalization process changes the contrast<br>
     * in order to reduce noise 
     */
    void divisiveNormalizationAll(){
        for (int i = 0; i < Config.gaborBanks; i++) {
            for (int j = 0; j < Config.nDisparities; j++) {
                divisiveNormalization(V1Bank.SSC[i][j]);
            }
        }
    }
    
    /**
     * Performs the divisive normalization process for a specific cells with same frequency <br>
     * and disparity
     * @param sc is the stereoscopic cell
     */
    void divisiveNormalization(StereoscopicCells sc){
        for (int i = 0; i < Config.gaborOrientations; i++) {
            Mat den=MatrixUtils.sum(1, 0.25, 2, 
                    sc.evenCells[i].previous[0].mat, sc.evenCells[i].previous[1].mat, sc.oddCells[i].previous[0].mat, sc.oddCells[i].previous[1].mat);
            Core.divide(sc.complexCells[i].mat, den, sc.normalizedCells[i].mat);
        }
        sc.composedNormalizedCell.mat=Functions.maxSum(sc.normalizedCells);
    }


}
