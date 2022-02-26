package middlewareVision.nodes.Attention;

import VisualMemory.Cell;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import gui.Visualizer;
import java.io.IOException;
import java.util.ArrayList;
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
import utils.Convertor;
import utils.LongSpike;
import utils.SpecialKernels;

/**
 *
 *
 */
public class FeedbackProccess extends Activity {

    AttentionTrigger trigger;

    public FeedbackProccess() {
        this.ID = AreaNames.FeedbackProccess;
        this.namer = AreaNames.class;

    }

    @Override
    public void init() {
        trigger = new AttentionTrigger(this);
        trigger.setVisible(true);
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            //send();

        } catch (Exception ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int numEyes = 1;

    public void addToV1SimpleCells(Mat filter, double value, double c, Mat addFilter) {
        for (int n1 = 0; n1 < V1Bank.SC.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int f = 0; f < Config.freqs; f++) {
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        feedbackFunction(filter, V1Bank.SC[n1][f][n2].Odd[i].mat, V1Bank.SC[n1][f][n2].Odd[i].mat, value, c, addFilter);
                        feedbackFunction(filter, V1Bank.SC[n1][f][n2].Even[i].mat, V1Bank.SC[n1][f][n2].Even[i].mat, value, c, addFilter);
                    }
                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(-1), 0, 0);
        try {
            send(AreaNames.V1SimpleCellsFilter, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1SimpleCellsOrientation(Mat filter, double value, double c, Mat addFilter, int i) {
        for (int n1 = 0; n1 < V1Bank.SC.length; n1++) {
            for (int f = 0; f < Config.freqs; f++) {
                for (int n2 = 0; n2 < numEyes; n2++) {
                    feedbackFunction(filter, V1Bank.SC[n1][f][n2].Odd[i].mat, V1Bank.SC[n1][f][n2].Odd[i].mat, value, c, addFilter);
                    feedbackFunction(filter, V1Bank.SC[n1][f][n2].Even[i].mat, V1Bank.SC[n1][f][n2].Even[i].mat, value, c, addFilter);
                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(-1), 0, 0);
        try {
            send(AreaNames.V1SimpleCellsFilter, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1ComplexCells(Mat filter, double value, double c, Mat addFilter) {
        for (int n1 = 0; n1 < V1Bank.CC.length; n1++) {
            for (int f = 0; f < Config.freqs; f++) {
                for (int n2 = 0; n2 < numEyes; n2++) {
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        Cell dst = V1Bank.CC[n1][f][n2].Cells[i];
                        rFeedback(filter, dst, dst.mat, value, c, addFilter);
                    }
                }
            }
        }
        Visualizer.update();
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1ComplexCellsOrientation(Mat filter, double value, double c, Mat addFilter, int i) {
        for (int n1 = 0; n1 < V1Bank.CC.length; n1++) {
            for (int f = 0; f < Config.freqs; f++) {
                for (int n2 = 0; n2 < numEyes; n2++) {
                    Cell dst = V1Bank.CC[n1][f][n2].Cells[i];
                    rFeedback(filter, dst, dst.mat, value, c, addFilter);
                }
            }
        }
        Visualizer.update();
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1HyperComplexCells(Mat filter, double value, double c, Mat addFilter) {
        for (int n1 = 0; n1 < V1Bank.HCC.length; n1++) {
            for (int f = 0; f < Config.freqs; f++) {
                for (int n2 = 0; n2 < numEyes; n2++) {
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        for (int z = 0; z < V1Bank.HCC[n1][f][n2].Cells.length; z++) {
                            Cell dst = V1Bank.HCC[n1][f][n2].Cells[z][i];
                            rFeedback(filter, dst, dst.mat, value, c, addFilter);

                        }
                    }
                }
            }
        }
        Visualizer.update();
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1HyperComplexCellsOrientation(Mat filter, double value, double c, Mat addFilter, int i) {
        for (int n1 = 0; n1 < V1Bank.HCC.length; n1++) {
            for (int f = 0; f < Config.freqs; f++) {
                for (int n2 = 0; n2 < numEyes; n2++) {

                    for (int z = 0; z < V1Bank.HCC[n1][f][n2].Cells.length; z++) {
                        Cell dst = V1Bank.HCC[n1][f][n2].Cells[z][i];
                        rFeedback(filter, dst, dst.mat, value, c, addFilter);
                    }

                }
            }
        }
        Visualizer.update();
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV2(Mat filter, double value, double c, Mat addFilter) {
        for (int n1 = 0; n1 < V2Bank.AC.length; n1++) {
            for (int f = 0; f < Config.freqsV2; f++) {
                for (int n2 = 0; n2 < numEyes; n2++) {
                    for (int i = 0; i < V2Bank.AC[0][0][0].Cells.length; i++) {
                        for (int z = 0; z < V2Bank.AC[0][0][0].Cells[0].length; z++) {
                            Cell dst = V2Bank.AC[n1][f][n2].Cells[i][z];
                            rFeedback(filter, dst, dst.mat, value, c, addFilter);
                        }
                    }
                }
            }
        }
        Visualizer.update();
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV2Angle(Mat filter, double value, double c, Mat addFilter, int i) {
        for (int n1 = 0; n1 < V2Bank.AC.length; n1++) {
            for (int f = 0; f < Config.freqsV2; f++) {
                for (int n2 = 0; n2 < numEyes; n2++) {
                    //for (int i = 0; i < V2Bank.V2CellsBank[0][0].angleCells.length; i++) {
                    for (int z = 0; z < V2Bank.AC[0][0][0].Cells[0].length; z++) {
                        Cell dst = V2Bank.AC[n1][f][n2].Cells[i][z];
                        rFeedback(filter, dst, dst.mat, value, c, addFilter);

                    }
                    //}
                }
            }
        }
        Visualizer.update();
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    public void rFeedback(Mat filter, Cell src2, Mat dst, double value, double c, Mat addFilter) {
        feedbackFunction(filter, src2.mat, src2.mat, value, c, addFilter);
        if (src2.previous != null) {
            for (Cell cell : src2.previous) {
                if (cell != null) {
                    rFeedback(filter, cell, cell.mat, value * 0.2, c * 0.2, addFilter);
                }
            }
        } else {
            return;
        }
    }

}
