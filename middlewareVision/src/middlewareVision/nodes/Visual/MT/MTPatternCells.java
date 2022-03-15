package middlewareVision.nodes.Visual.MT;

import VisualMemory.Cell;
import VisualMemory.MTCells.MTBank;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;
import utils.MatrixUtils;

/**
 *
 *
 */
public class MTPatternCells extends Activity {

    Mat mul, blur, sum, m;

    public MTPatternCells() {
        this.ID = AreaNames.MTPatternCells;
        this.namer = AreaNames.class;

        mul = new Mat();
        blur = new Mat();
        sum = new Mat();
        m = new Mat();
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (spike.getModality() == Modalities.VISUAL) {
                
                MTPatternProcess(0);

                visualize();
                
                Visualizer.lockLimit("mtComp");
            }

        } catch (Exception ex) {
            Logger.getLogger(MTPatternCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void visualize() {
        Visualizer.setImageFull(MTBank.MTPC[0].Cells[12].mat, "mt pattern 1", Visualizer.getRow("mtComp") + 2, Config.gaborOrientations + 2, "MTP");
        Visualizer.setImageFull(MTBank.MTPC[0].Cells[8].mat, "mt pattern 2", Visualizer.getRow("mtComp") + 2, Config.gaborOrientations + 3, "MTP");       
    }

    /**
     * Performs the MT Pattern process for both eyes
     */
    void MTPatternProcess() {
        MTPatternProcess(0);
        MTPatternProcess(1);
    }

    /**
     * It performs the PatternActivation process for all MT Pattern Cells from an specific eye
     * @param eye 
     */
    void MTPatternProcess(int eye) {
        for (int i = 0; i < MTBank.MTPC[0].Cells.length; i++) {
            MTBank.MTPC[0].Cells[i].mat = PatternActivation(MTBank.MTPC[0].Cells[i]);
        }
    }

    /**
     * The Pattern Cell activation process follows the next steps <br>
     * 
     * <ul><li>First, a dilatation of the source Motion Component Cells is performed</li>
     * <li>then, the sources are multiplied in the matrix MUL</li>
     * <li>a Gaussian blur is applied to the multiplication</li>
     * <li>The blur is multiplied with the sum of the sources, in order to cover more area, in the matrix M</li>
     * <li> the result is the multiplication of the matrix MUL and M
     * @param cell
     * @return 
     */
    Mat PatternActivation(Cell cell) {
               
        mul = MatrixUtils.multiply(MatrixUtils.basicDilate(cell.previous, 1, 5));

        Imgproc.GaussianBlur(mul, blur, new Size(31, 31), 2);

        Core.multiply(blur, Scalar.all(10), blur);

        sum = MatrixUtils.maxSum(cell.previous);
        Core.multiply(blur, sum, m);

        return MatrixUtils.maxSum(m, mul);         
    }

}
