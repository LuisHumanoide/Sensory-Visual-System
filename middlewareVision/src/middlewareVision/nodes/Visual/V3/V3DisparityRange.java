package middlewareVision.nodes.Visual.V3;

import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V3Cells.V3Bank;
import generator.ProcessList;
import gui.Visualizer;
import java.util.ArrayList;
import cFramework.nodes.process.Process;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;
import utils.MatrixUtils;

/**
 *
 *
 */
public class V3DisparityRange extends Process {

    public V3DisparityRange() {
        this.ID = AreaNames.V3DisparityRange;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    int idx = 0;

    @Override
    public void init() {

    }

    @Override
    public void receive(long nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                if (spike.getModality() == Modalities.VISUAL) {

                    disparityRangeProcess();

                    visualize();

                }

            } catch (Exception ex) {
                Logger.getLogger(V3DisparityRange.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Visualize all disparity range cells
     */
    void visualize() {
        for (int i = 0; i < V3Bank.SRC.length; i++) {
            Visualizer.setImage(V3Bank.SRC[i].composedCell.mat, "disparity range " + V3Bank.SRC[i].disparity, i, 15);
        }
    }

    /**
     * Performs the disparity range process for all disparities
     */
    void disparityRangeProcess() {
        for (int i = 0; i < V3Bank.SRC.length; i++) {
            sumDisparities(i);
        }
    }

    /**
     * Performs the disparity range process for a preffered disparity<br>
     * where each preffered disparity was settled in the file of<br>
     * disparity gaussians<br>
     * The algorithm is performing a sum with gaussian values<br>
     * covering a range of disparities determined from the user
     *
     * @param index is the index of the disparity
     */
    private void sumDisparities(int index){
        //for each orientation
        for(int i=0;i<Config.gaborOrientations;i++){
            //create an array where the cells with the same orientation but diffent disparities are stores
            Mat mArray[]=new Mat[V3Bank.SRC[index].xvalues.length];
            //store the cells with same orientation and different disparities
            for(int j=0;j<mArray.length;j++){
                mArray[j]=V1Bank.SMC[j].cells[i].mat.clone();
            }
            //performs the gaussian sum, where th weights are the values of the gaussian
            V3Bank.SRC[index].cells[i].mat=MatrixUtils.sum(mArray, V3Bank.SRC[index].yvalues);
        }
        //create the composed cell
        V3Bank.SRC[index].composedCell.mat=MatrixUtils.maxSum(V3Bank.SRC[index].cells);
    }

}
