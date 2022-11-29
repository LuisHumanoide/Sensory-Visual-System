package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.SC;
import generator.ProcessList;
import spike.Location;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import gui.Visualizer;
import kmiddle2.nodes.activities.Activity;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_64F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.numSync;

/**
 *
 *
 */
public class V1SimpleCells extends Activity {

    /**
     * *************************************************************************
     * init variables
     * *************************************************************************
     */
    int nFrame = 3 * Config.gaborOrientations;

    //mapa de saliencia, no se recibe aún
    public Mat saliencyMap;
    //no se para que sirve esto
    boolean init = false;

    /**
     * constructor
     */
    public V1SimpleCells() {
        this.ID = AreaNames.V1SimpleCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
                /*
            receive spike
                 */
                LongSpike spike = new LongSpike(data);
                Mat LCell = MatrixUtils.sum(new Mat[]{V1Bank.DOC[0][0].Cells[0].mat, V1Bank.DOC[0][0].Cells[1].mat, V1Bank.DOC[0][0].Cells[2].mat}, new double[]{0, 0, 1});
                Mat RCell = MatrixUtils.sum(new Mat[]{V1Bank.DOC[0][1].Cells[0].mat, V1Bank.DOC[0][1].Cells[1].mat, V1Bank.DOC[0][1].Cells[2].mat}, new double[]{0, 0, 1});

                if (spike.getModality() == Modalities.VISUAL) {

                    //normalizeInput(2);
                    convolveSimpleCells(LCell, RCell);

                    visualize();
                    Visualizer.addLimit("SCinf", 6);
                    Visualizer.addLimit("SCsup", 4 * (Config.gaborBanks - 1) + 10);

                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);

                    send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                    send(AreaNames.V1BinocularSimpleCells, sendSpike1.getByteArray());
                }

                if (spike.getModality() == Modalities.ATTENTION) {
                    Location l = (Location) spike.getLocation();
                    if (l.getValues()[0] == 0) {
                        LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                        send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                    }
                    visualize();
                }
            }
        } catch (Exception ex) {
            //Logger.getLogger(V1SimpleCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Visualize all the Simple Cells
     */
    void visualize() {
        for (int k = 0; k < Config.gaborBanks; k++) {
            for (int i = 0; i < Config.gaborOrientations; i++) {
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][0].Even[i].mat), "even L bank" + k + " " + i, 4 * k + 6, i);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][0].Odd[i].mat), "odd L bank" + k + " " + i, 4 * k + 8, i);

                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][1].Even[i].mat), "even R bank" + k + " " + i, 4 * k + 7, i);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][1].Odd[i].mat), "odd R bank" + k + " " + i, 4 * k + 9, i);

                //Combined or merged simple cells
                if (i == Config.gaborOrientations - 1) {
                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][0].Even)), "Combined even L bank" + k + " " + i, 4 * k + 6, Config.gaborOrientations + 1);
                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][0].Odd)), "Combined odd L bank" + k + " " + i, 4 * k + 8, Config.gaborOrientations + 1);

                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][1].Even)), "Combined even R bank" + k + " " + i, 4 * k + 7, Config.gaborOrientations + 1);
                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][1].Odd)), "Combined odd R bank" + k + " " + i, 4 * k + 9, Config.gaborOrientations + 1);
                }
            }
        }
    }

    /**
     * Keeping the value of the activation under than 1
     *
     * @param index
     */
    void normalizeInput(int index) {
        double maxx = 0;
        double maxL;
        double maxR;
        maxL = Core.minMaxLoc(V1Bank.DOC[0][0].Cells[index].mat).maxVal;
        maxR = Core.minMaxLoc(V1Bank.DOC[0][1].Cells[index].mat).maxVal;
        if (maxL > maxR) {
            maxx = maxL;
        } else {
            maxx = maxR;
        }
        if (maxx > 1) {
            Core.divide(V1Bank.DOC[0][0].Cells[index].mat, Scalar.all(maxx), V1Bank.DOC[0][0].Cells[index].mat);
            Core.divide(V1Bank.DOC[0][1].Cells[index].mat, Scalar.all(maxx), V1Bank.DOC[0][1].Cells[index].mat);
        }
    }

    /**
     * Convolve all simple cells with the designed gabor filter
     *
     * @param input
     */
    void convolveSimpleCells(Mat inputL, Mat inputR) {
        int i1 = SC.length;
        max = 0;
        for (int x1 = 0; x1 < i1; x1++) {
            convolve(x1, 0, inputL);
            convolve(x1, 1, inputR);
        }
    }

    /**
     * Convolve the simple cells in V1 with Gabor Filters<br>
     * each Simple Cell has a Gabor Filter loaded
     *
     * @param x1 is the filter index
     * @param x2 correspond to the eye
     * @param src source matrix to convolve
     */
    double max = 0;

    void convolve(int x1, int x2, Mat src) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            V1Bank.SC[x1][x2].Even[i].mat = filterV1(src, V1Bank.SC[x1][x2].evenFilter[i], 0.2);
            V1Bank.SC[x1][x2].Odd[i].mat = filterV1(src, V1Bank.SC[x1][x2].oddFilter[i], 0.2);
        }
    }

    /**
     * Obtain a filtered matrix<br>
     * It performs a convolution between <code>img</code> and <code>filter</code><br>
     * then the values are divided by <code> max </code> for keeping the activations under than 1<br>
     * and finally a threshold is applied where <code>thresh</code> is the threshold value
     * 
     * @param img source matrix
     * @param filter filter
     * @param thresh threshold value
     * @return a filtered matrix
     */
    Mat filterV1(Mat img, Mat filter, double thresh) {
        //initialize the filter Matrix
        Mat filt = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        img.convertTo(img.clone(), CV_32F);
        //perform the convolution
        Imgproc.filter2D(img, filt, CV_32F, filter);
        //Block for keeping the activations under than 1
        double max = Core.minMaxLoc(filt).maxVal;
        if (max > 1) {
            Core.divide(filt, Scalar.all(max), filt);
        }
        //Aplying a threshold, where thresh<=x<=1
        Imgproc.threshold(filt, filt, thresh, 1, Imgproc.THRESH_TOZERO);
        return filt;
    }

}
