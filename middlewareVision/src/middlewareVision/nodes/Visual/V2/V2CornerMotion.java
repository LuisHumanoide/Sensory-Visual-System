package middlewareVision.nodes.Visual.V2;

import VisualMemory.MTCells.MTBank;
import VisualMemory.MotionCell;
import VisualMemory.V2Cells.CornerMotionCells;
import VisualMemory.V2Cells.V2Bank;
import spike.Location;
import generator.ProcessList;
import gui.Visualizer;
import java.awt.Color;
import java.util.ArrayList;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;

/**
 *
 *
 */
public class V2CornerMotion extends Activity {

    int i1 = 1;
    int i2 = 1;
    int i3;

    public V2CornerMotion() {
        this.ID = AreaNames.V2CornerMotion;
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

                for (int i = 0; i < i1; i++) {
                    for (int j = 0; j < i2; j++) {
                        motionProcess(V2Bank.AC[0][0].mergedAC[1], i, j);
                    }
                }

                for (int i = 0; i < i1; i++) {
                    for (int j = 0; j < i2; j++) {
                        visualize(i, j);
                    }
                }

                Visualizer.lockLimit("v2motion");

            } catch (Exception ex) {
                Logger.getLogger(V2CornerMotion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void motionProcess(Mat src, int i1, int i2) {
        for (int i = 0; i < V2Bank.CMC[i1][i2].cells.length; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                V2Bank.CMC[i1][i2].cells[i][j].cellMotionProcess(src);

                Core.subtract(V2Bank.CMC[i1][i2].cells[i][j % (Config.gaborOrientations * 2)].mat1st,
                        V2Bank.CMC[i1][i2].cells[i][(j + Config.gaborOrientations) % (Config.gaborOrientations * 2)].mat1st,
                        V2Bank.CMC[i1][i2].cells[i][j % (Config.gaborOrientations * 2)].mat);
            }
        }
    }

    Mat coloredMat(Mat src, int index) {
        double hue = (double) ((double)(index) / (double)(Config.gaborOrientations * 2));
        double saturation = 1f;
        double brightness = 1f;
        Color color = Color.getHSBColor((float) hue, (float) saturation, (float) brightness);
        Mat mcolor = new Mat(src.height(), src.width(), CvType.CV_8UC3, new Scalar(color.getRed(), color.getGreen(), color.getBlue()));
        Mat colormat = new Mat();

        Imgproc.cvtColor(src, colormat, Imgproc.COLOR_GRAY2RGB);
        colormat.convertTo(colormat, CvType.CV_8UC3);
        Core.multiply(colormat, mcolor, colormat);

        return colormat;
    }
    
    Mat coloredMotion(MotionCell[] cells){
        ArrayList<Mat> listMat = new ArrayList();
        for (int i = 0; i < cells.length; i++) {
            listMat.add(coloredMat(cells[i].mat, i));
        }
        Mat merged = Mat.zeros(cells[0].mat.size(), CvType.CV_8UC3);
        for (Mat m : listMat) {
            Core.max(merged, m, merged);
        }

        return merged;
    }

    public void visualize(int x1, int x2) {
        i3 = V2Bank.CMC[0][0].cells.length;
        for (int i = 0; i < i3; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                if (j < Config.gaborOrientations) {
                    Visualizer.setImage(V2Bank.CMC[x1][x2].cells[i][j].mat, "motion V2", 8 + (i1 * i2 * x1) + (i * i2 * 2) + x2, Config.gaborOrientations + 2 + j, "v2motion");
                } else {
                    Visualizer.setImage(V2Bank.CMC[x1][x2].cells[i][j].mat, "opposite motion V2", 8 + (i1 * i2 * x1) + (1 + i * i2 * 2) + x2,
                            Config.gaborOrientations + 2 + (j - Config.gaborOrientations), "v2motion");
                }
            }
        }
        Visualizer.setImage(coloredMotion(V2Bank.CMC[0][0].cells[0]), "colored Motion", Visualizer.getRow("v2motion")+1,Config.gaborOrientations + 2, "coloredV2Motion");
    }

}
