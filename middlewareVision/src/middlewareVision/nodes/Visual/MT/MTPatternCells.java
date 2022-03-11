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
            }

        } catch (Exception ex) {
            Logger.getLogger(MTPatternCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void visualize() {
        Visualizer.setImageFull(MTBank.MTPC[0].Cells[5].mat, "mt pattern", Visualizer.getRow("mtComp") + 1, Config.gaborOrientations + 2, "MTP");
    }

    void MTPatternProcess() {
        MTPatternProcess(0);
        MTPatternProcess(1);
    }

    void MTPatternProcess(int eye) {
        for (int i = 0; i < MTBank.MTPC[0].Cells.length; i++) {
            MTBank.MTPC[0].Cells[i].mat = PatternActivation(MTBank.MTPC[0].Cells[i]);
        }
    }

    Mat PatternActivation(Cell cell) {

        mul = MatrixUtils.multiply(cell.previous);

        Imgproc.GaussianBlur(mul, blur, new Size(31, 31), 5.0);

        Core.multiply(blur, Scalar.all(10), blur);

        sum = MatrixUtils.maxSum(cell.previous);
        Core.multiply(blur, sum, m);

        return MatrixUtils.maxSum(m, mul);
    }

}
