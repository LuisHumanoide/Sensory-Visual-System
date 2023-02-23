package middlewareVision.nodes.Visual.MT;

import VisualMemory.Cell;
import VisualMemory.MTCells.MTBank;
import generator.ProcessList;
import gui.Visualizer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import spike.Location;
import cFramework.nodes.process.Process;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MatrixUtils;

/**
 * Class that makes the process of the MT Pattern Cells <br>
 * MT Pattern Cells combines the signals from MT Component Cells<br>
 * the velocity is obtained with a geometric method named <b> Intersection of Constrains</b>
 */
public class MTPatternCells extends Process {

    Mat mul, blur, sum, m;

    /**
     * Constructor
     */
    public MTPatternCells() {
        this.ID = AreaNames.MTPatternCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);

        mul = new Mat();
        blur = new Mat();
        sum = new Mat();
        m = new Mat();
        
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
                    
                    MTPatternProcess(0);

                    visualize();

                    Visualizer.lockLimit("MTP");
                    
                    //send(AreaNames.MSTTemplateCells, null);
                }

            } catch (Exception ex) {
                Logger.getLogger(MTPatternCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /**
     * Visualize the MT Pattern Cells activation in a color matrix
     */
    void visualize() {
        Visualizer.setImageFull(Convertor.Mat2Img2(colorMTPCells(0)),
                "mt pattern 1", Visualizer.getRow("mtComp") + 2, Config.gaborOrientations + 2, "MTP");
    }

    /**
     * Performs the MT Pattern process for both eyes
     */
    void MTPatternProcess() {
        MTPatternProcess(0);
        MTPatternProcess(1);
    }

    /**
     * It performs the PatternActivation process for all MT Pattern Cells from
     * an specific eye
     *
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
     * <ul><li>First, a dilatation of the source Motion Component Cells is
     * performed</li>
     * <li>then, the sources are multiplied in the matrix MUL</li>
     * <li>a Gaussian blur is applied to the multiplication</li>
     * <li>The blur is multiplied with the sum of the sources, in order to cover
     * more area, in the matrix M</li>
     * <li> the result is the multiplication of the matrix MUL and M
     *
     * @param cell
     * @return
     */
    Mat PatternActivation(Cell cell) {

        mul = MatrixUtils.multiply(MatrixUtils.basicDilate(cell.previous, 3, 9));

        Imgproc.GaussianBlur(mul, blur, new Size(31, 31), 5);

        Core.multiply(blur, Scalar.all(15), blur);

        sum = MatrixUtils.maxSum(cell.previous);
        Core.multiply(blur, sum, m);
        
        Core.multiply(mul, Scalar.all(0.1), mul);
         
        return MatrixUtils.maxSum(m, mul);

    }

    /**
     * Transforms the activation of an MT Pattern matrix to a color matrix with
     * the help of HSB space,<br>
     * where the hue is the angle, and the brightness corresponds to the speed
     * ratio
     *
     * @param mat MT Pattern Mat
     * @param angle Angle of the MT Matrix
     * @param speed Speed of the MT Matrix
     * @return a colored matrix
     */
    Mat coloredMTPattern(Mat mat, double angle, double speed) {
        double hue = (double)((angle + Math.PI)/(2*Math.PI));
        double saturation = 1f;
        double brightness = (double) (speed / MTBank.maxSpeed);

        Color color = Color.getHSBColor((float) hue, (float) saturation, (float) brightness);
        Mat mcolor = new Mat(mat.height(), mat.width(), CvType.CV_8UC3, new Scalar(color.getRed(), color.getGreen(), color.getBlue()));
        Mat colormat = new Mat();

        Imgproc.cvtColor(mat, colormat, Imgproc.COLOR_GRAY2RGB);
        colormat.convertTo(colormat, CvType.CV_8UC3);
        Core.multiply(colormat, mcolor, colormat);

        return colormat;
    }

    /**
     * Performs the coloring process for all MT matrices on the corresponding
     * eye <br>
     * this function obtains a matrix with all velocities represented in
     * different colors
     *
     * @param eye is the corresponding eye
     * @return
     */
    Mat colorMTPCells(int eye) {
        ArrayList<Mat> listMat = new ArrayList();
        for (int i = 0; i < MTBank.MTPC[eye].Cells.length; i++) {
            listMat.add(coloredMTPattern(MTBank.MTPC[eye].Cells[i].mat, MTBank.MTPC[eye].Cells[i].getAngle(), MTBank.MTPC[eye].Cells[i].getSpeed()));
        }
        Mat merged = Mat.zeros(MTBank.MTPC[eye].Cells[0].mat.size(), CvType.CV_8UC3);
        for (Mat m : listMat) {
            Core.max(merged, m, merged);
        }

        return merged;
    }

}
