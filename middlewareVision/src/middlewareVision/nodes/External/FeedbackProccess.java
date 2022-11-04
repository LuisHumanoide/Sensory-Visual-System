package middlewareVision.nodes.External;

import VisualMemory.Cell;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import generator.ProcessList;
import java.io.IOException;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;

/**
 * Example class for an external modulation process<br>
 * An external function can modulate the system through 2 matrices <br> 
 * a matrix that multiplies to the output <br> 
 * and another matrix that adds values
 */
public class FeedbackProccess extends Activity {

    AttentionTrigger trigger;

    public FeedbackProccess() {
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
        this.ID = AreaNames.FeedbackProccess;
        this.namer = AreaNames.class;
    }

    /**
     * Start with the feedback process
     */
    @Override
    public void init() {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            //AttentionTrigger is a frame class in which we can activate the modulation process.
            trigger = new AttentionTrigger(this);
            trigger.setVisible(true);
        }
    }

    /**
     * Method to receive, preferably from an external function, which should send feedback matrices.
     * @param nodeID
     * @param data 
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            //send();

        } catch (Exception ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Number of eyes
    int numEyes = 2;
    //Feedback ratio for predecessors when feedback is propagated backward
    double feedbackRatio=0.3;

    /**
     * Performs feedback process for all single cells of V1, for all orientations.<br>
     * After the process is completed, a signal is sent to the smallNode to update the visualizer and propagate to the successors.
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     */
    public void addToV1SimpleCells(Mat filter, double value, double c, Mat addFilter) {

        for (int n2 = 0; n2 < numEyes; n2++) {
            for (int f = 0; f < Config.freqs; f++) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    rFeedback(filter, V1Bank.SC[f][n2].Odd[i], V1Bank.SC[f][n2].Odd[i].mat, value, c, addFilter);
                    rFeedback(filter, V1Bank.SC[f][n2].Even[i], V1Bank.SC[f][n2].Even[i].mat, value, c, addFilter);
                }
            }
        }

        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(-1), 0, 0);
        try {
            send(AreaNames.V1SimpleCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs feedback process for one orientation<br>
     * After the process is completed, a signal is sent to <br>the smallNode to update the visualizer and propagate to the successors.
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     * @param i the selected orientation
     */
    public void addToV1SimpleCellsOrientation(Mat filter, double value, double c, Mat addFilter, int i) {

        for (int f = 0; f < Config.freqs; f++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                rFeedback(filter, V1Bank.SC[f][n2].Odd[i], V1Bank.SC[f][n2].Odd[i].mat, value, c, addFilter);
                rFeedback(filter, V1Bank.SC[f][n2].Even[i], V1Bank.SC[f][n2].Even[i].mat, value, c, addFilter);
            }
        }

        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(-1), 0, 0);
        try {
            send(AreaNames.V1SimpleCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs the feedback process for all complex cells
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     */
    public void addToV1ComplexCells(Mat filter, double value, double c, Mat addFilter) {

        for (int f = 0; f < Config.freqs; f++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    Cell dst = V1Bank.CC[f][n2].Cells[i];
                    rFeedback(filter, dst, dst.mat, value, c, addFilter);
                }
            }
        }

        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs the feedback process for complex cells with certain orientations
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     * @param i the selected orientation
     */
    public void addToV1ComplexCellsOrientation(Mat filter, double value, double c, Mat addFilter, int i) {

        for (int f = 0; f < Config.freqs; f++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                Cell dst = V1Bank.CC[f][n2].Cells[i];
                rFeedback(filter, dst, dst.mat, value, c, addFilter);
            }
        }

        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs the feedback process for hypercomplex cells for all orientations and filters
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     */
    public void addToV1HyperComplexCells(Mat filter, double value, double c, Mat addFilter) {

        for (int f = 0; f < Config.freqs; f++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    for (int z = 0; z < V1Bank.HCC[f][n2].Cells.length; z++) {
                        Cell dst = V1Bank.HCC[f][n2].Cells[z][i];
                        rFeedback(filter, dst, dst.mat, value, c, addFilter);

                    }
                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs the feedback process for all hypercomplex cells to certain orientations
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     * @param i the selected orientation
     */
    public void addToV1HyperComplexCellsOrientation(Mat filter, double value, double c, Mat addFilter, int i) {

        for (int f = 0; f < Config.freqs; f++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int z = 0; z < V1Bank.HCC[f][n2].Cells.length; z++) {
                    Cell dst = V1Bank.HCC[f][n2].Cells[z][i];
                    rFeedback(filter, dst, dst.mat, value, c, addFilter);
                }
            }
        }

        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs the feedback process to all angular cells in V2
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     */
    public void addToV2(Mat filter, double value, double c, Mat addFilter) {

        for (int f = 0; f < Config.freqsV2; f++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int i = 0; i < V2Bank.AC[0][0].Cells.length; i++) {
                    for (int z = 0; z < V2Bank.AC[0][0].Cells[0].length; z++) {
                        Cell dst = V2Bank.AC[f][n2].Cells[i][z];
                        rFeedback(filter, dst, dst.mat, value, c, addFilter);
                    }
                }
            }
        }

        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs the feedback process to certain angles in V2
     * @param filter filter which multiplies the matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied.
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     * @param i index of the preferred angle
     */
    public void addToV2Angle(Mat filter, double value, double c, Mat addFilter, int i) {

        for (int f = 0; f < Config.freqsV2; f++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int z = 0; z < V2Bank.AC[0][0].Cells[0].length; z++) {
                    Cell dst = V2Bank.AC[f][n2].Cells[i][z];
                    rFeedback(filter, dst, dst.mat, value, c, addFilter);

                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Main feedback function, base case of the recursive function, where the process performed is:<br>
     * <b> M1=Value(M0 R0)+(1-Value)(M0)+C R1</b><br>
     * Where R0 is <i>filter</i> and R1 is <i>addFilter</i>
     * @param filter filter which multiplies the matrix
     * @param src2 source matrix
     * @param dst destination matrix or resulting matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     */
    public void feedbackFunction(Mat filter, Mat src2, Mat dst, double value, double c, Mat addFilter) {
        Mat mul1 = new Mat();
        Mat filter2 = new Mat();
        if (filter != null) {
            Core.multiply(filter, src2, mul1, 1);
            Core.addWeighted(src2,
                    1 - value, mul1, value, 0, dst);
        }
        if (addFilter != null) {
            Core.multiply(addFilter, new Scalar(c), filter2);
            Core.add(dst, filter2, dst);
        }

    }

    /**
     * Recursive feedback function, where the feedback is also applied to the predecessors.
     * @param filter filter which multiplies the matrix
     * @param src2 source matrix
     * @param dst destination matrix or resulting matrix
     * @param value multiplication ratio, when it is 1 it is fully multiplied, when it is 0 it is not multiplied
     * @param c addition constant, for the filter addFilter
     * @param addFilter filter to be added to the output
     */
    public void rFeedback(Mat filter, Cell src2, Mat dst, double value, double c, Mat addFilter) {
        feedbackFunction(filter, src2.mat, src2.mat, value, c, addFilter);
        if (src2.previous != null) {
            for (Cell cell : src2.previous) {
                if (cell != null) {
                    rFeedback(filter, cell, cell.mat, value * feedbackRatio, c * feedbackRatio, addFilter);
                }
            }
        } else {
            return;
        }
    }

}
