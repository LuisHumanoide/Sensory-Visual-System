package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.CC;
import generator.ProcessList;
import spike.Location;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import gui.Visualizer;
import cFramework.nodes.process.Process;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;

/**
 *
 *
 */
public class V1ComplexCells extends Process {

    public V1ComplexCells() {
        this.ID = AreaNames.V1ComplexCells;
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
                    //make the spatial invariance process
                    energyProcessAll();
                    //visualize the activations
                    visualize();

                    Visualizer.addLimit("CC", Visualizer.getRow("SCsup") + 2 * (Config.gaborBanks - 1) + 1);

                    LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    //send spikes to hypercomplex and motion cells
                    send(AreaNames.V1HyperComplex, sendSpike.getByteArray());
                    send(AreaNames.V1MotionCellsNew, sendSpike.getByteArray());

                }
                /**
                 * If the Spikes comes from Attention then the modulation matrix
                 * will be propagated to the previous nodes
                 * and it will refresh the visualization of the complex cells matrix
                 */
                if (spike.getModality() == Modalities.ATTENTION) {
                    Location l = (Location) spike.getLocation();
                    if (l.getValues()[0] == 0) {
                        LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                        send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
                    }
                    /*
                    Send an attentional spike to the previous nodes
                     */
                    LongSpike sendSpike2 = new LongSpike(Modalities.ATTENTION, new Location(1), 0, 0);
                    send(AreaNames.V1SimpleCells, sendSpike2.getByteArray());
                    visualize();
                }

            } catch (Exception ex) {
                //Logger.getLogger(V1ComplexCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Visualize results in the visualizer
     */
    void visualize() {
        for (int k = 0; k < Config.gaborBanks; k++) {
            for (int i = 0; i < Config.gaborOrientations; i++) {

                Visualizer.setImage(V1Bank.CC[k][0].Cells[i].mat, "Complex L" + k + " " + i, Visualizer.getRow("SCsup") + 2 * k, i);
                Visualizer.setImage(V1Bank.CC[k][1].Cells[i].mat, "Complex R" + k + " " + i, Visualizer.getRow("SCsup") + 2 * k + 1, i);

                if (i == Config.gaborOrientations - 1) {

                    V1Bank.CC[k][0].sumCell.mat = Functions.maxSum(V1Bank.CC[k][0].Cells);
                    V1Bank.CC[k][1].sumCell.mat = Functions.maxSum(V1Bank.CC[k][1].Cells);

                    Visualizer.setImage(V1Bank.CC[k][0].sumCell.mat, "Combined Complex L" + k + " ", Visualizer.getRow("SCsup") + 2 * k, i + 2);
                    Visualizer.setImage(V1Bank.CC[k][1].sumCell.mat, "Combined Complex R" + k + " ", Visualizer.getRow("SCsup") + 2 * k + 1, i + 2);
                }

            }

        }

        Visualizer.addLimit("CC", Visualizer.getRow("SCsup") + 2 * (Config.gaborBanks - 1) + 1);
    }

    /**
     * Performs the energy process for all cells with different filters
     */
    void energyProcessAll() {
        int i1 = CC.length;
        int i2 = CC[0].length;

        for (int x1 = 0; x1 < i1; x1++) {
            for (int x2 = 0; x2 < i2; x2++) {
                energyProcess(x1, x2);
            }
        }

    }

    /**
     * Performs the energy process for one class of complex cell <br>
     * each class of complex cells is determined by the filter settled<br>
     * in the list of Gabor Filters
     *
     * @param x1 Gabor filter type
     * @param x2 eye
     */
    public void energyProcess(int x1, int x2) {
        int x = CC[x1][x2].Cells.length;
        for (int i = 0; i < x; i++) {
            CC[x1][x2].Cells[i].mat = Functions.sumPowProcess(CC[x1][x2].simpleCells.Even[i].mat, 
                    CC[x1][x2].simpleCells.Odd[i].mat, 2);
        }
    }

}
