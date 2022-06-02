package middlewareVision.nodes.Visual.V4;

import VisualMemory.LabeledCells;
import VisualMemory.V4Cells.V4Bank;
import spike.Location;
import generator.ProcessList;
import gui.Visualizer;
import java.util.Set;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import spike.Modalities;
import utils.Functions;
import utils.LongSpike;
import utils.MatrixUtils;

/**
 *
 *
 */
public class V4SimpleShapeCells extends Activity {

    public V4SimpleShapeCells() {
        this.ID = AreaNames.V4SimpleShapeCells;
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

                    shapeActivationProcess();

                    visualize();
                    
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    
                    send(AreaNames.V4SimpleShapeScaleInv, sendSpike1.getByteArray());
                }

            } catch (Exception ex) {
                Logger.getLogger(V4SimpleShapeCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Visualize the activations
     */
    void visualize() {
        int size = V4Bank.SSC.length;
        for (int i = 0; i < size; i++) {
            Visualizer.setImage(V4Bank.SSC[i][0].cell.mat, "shape " + i + " " + V4Bank.SSC[i][0].nameCell + " scale " + V4Bank.SSC[i][0].scale, 28, i, "V4");
            Visualizer.setImage(V4Bank.SSC[i][1].cell.mat, "shape " + i + " " + V4Bank.SSC[i][1].nameCell + " scale " + V4Bank.SSC[i][1].scale, 29, i, "V4");
        }
    }

    /**
     * Performs the shape activation process for all cells, for both eyes
     */
    void shapeActivationProcess() {
        int size = V4Bank.SSC.length;
        for (int i = 0; i < size; i++) {
            shapeActivationOneCell(i, 0);
            shapeActivationOneCell(i, 1);
        }
    }

    /**
     * Performs the activation process for obtainint the value of the shape cell<br>
     * SSC cells have filters with a key, those keys correspond to the map of cells that have the same keys.<br>
     * Once the correspondence is found, the convolution is performed with the filters and keys, and then the activations are combined.
     * @param index index of the shape cell
     * @param eye eye index
     */
    void shapeActivationOneCell(int index, int eye) {
        Set<String> keys = V4Bank.SSC[index][eye].filterMap.keySet();
        Mat[] mArray = new Mat[keys.size()];
        
        int i = 0;
        //for each key from the filter map
        for (String key : keys) {           
            Mat keyMat = new Mat();
            //find the Mats with the same key
            if (eye == 0) {
                keyMat = LabeledCells.LabelCellMapL.get(key).mat;
            }
            if (eye == 1) {
                keyMat = LabeledCells.LabelCellMapR.get(key).mat;
            }
            //Performs the filter
            mArray[i] = Functions.filter(keyMat, V4Bank.SSC[index][eye].filterMap.get(key));
            i++;
        }
        //Process of combination and obtaining the activation
        //First a weighted sum is performed
        V4Bank.SSC[index][eye].cell.mat = MatrixUtils.sum(mArray, (double) (1 / (double) keys.size()), 0);
        //normalization
        double max = Core.minMaxLoc(V4Bank.SSC[index][eye].cell.mat).maxVal;        
        if (max > 1) {
            Core.divide(V4Bank.SSC[index][eye].cell.mat, Scalar.all(max), V4Bank.SSC[index][eye].cell.mat);
        }
        //the activations are raised to a pow
        Core.pow(V4Bank.SSC[index][eye].cell.mat, 2, V4Bank.SSC[index][eye].cell.mat);
    }

}
