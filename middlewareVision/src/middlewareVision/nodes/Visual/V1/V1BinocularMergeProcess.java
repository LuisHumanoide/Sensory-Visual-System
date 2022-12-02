package middlewareVision.nodes.Visual.V1;

import VisualMemory.Cell;
import VisualMemory.V1Cells.V1Bank;
import spike.Location;
import generator.ProcessList;
import gui.Visualizer;
import java.util.ArrayList;
import cFramework.nodes.process.Process;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Functions;
import utils.LongSpike;
import utils.MatrixUtils;

/**
 *
 *
 */
public class V1BinocularMergeProcess extends Process {

    public V1BinocularMergeProcess() {
        this.ID = AreaNames.V1BinocularMergeProcess;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    final int COMPLEX_CELLS = 1;
    final int NORMALIZED_CELLS = 2;

    @Override
    public void init() {

    }

    @Override
    public void receive(long nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);

                if (spike.getModality() == Modalities.VISUAL) {

                    mergeProcessAll(COMPLEX_CELLS);

                    visualize();

                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    send(AreaNames.V3DisparityRange, sendSpike1.getByteArray());
                }

            } catch (Exception ex) {
                Logger.getLogger(V1BinocularMergeProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void visualize() {
        for (int i = 0; i < Config.nDisparities; i++) {
            Visualizer.setImage(V1Bank.SMC[i].composedCell.mat, "merged cell", i, 14);
        }
    }

    /**
     * It performs the Merge process for all cells, in the end, all cells of the
     * same disparity <br>
     * but different spatial frequency are encapsulated in cells of the same
     * disparity.
     */
    void mergeProcessAll(int cellType) {
        //List where cell banks of the same disparity but different spatial frequencies will be stored.
        ArrayList<Cell[]> cellArray = new ArrayList();

        for (int i = 0; i < Config.nDisparities; i++) {

            for (int j = 0; j < Config.gaborBanks; j++) {
                //addint to the list
                if (cellType == COMPLEX_CELLS) {
                    cellArray.add(V1Bank.SSC[j][i].complexCells);
                }
                if (cellType == NORMALIZED_CELLS) {
                    cellArray.add(V1Bank.SSC[j][i].normalizedCells);
                }
            }

            mergeProcess(cellArray, i);
            cellArray.clear();
        }
    }

    /**
     * The Merge process is performed to create the bank of cells of a single
     * disparity value, <br>
     * for this purpose the matrices of the same disparity but different spatial
     * frequency are joined.
     *
     * @param cellArray arrangement of complex cells or normalized cells
     * @param dIndex disparity index
     */
    void mergeProcess(ArrayList<Cell[]> cellArray, int dIndex) {
        //For all orientation i, the cArray has a Mat array of cells of the same orientation but different spatial frequency.
        ArrayList<Cell> cArray = new ArrayList();
        for (int i = 0; i < Config.gaborOrientations; i++) {
            for (Cell[] cells : cellArray) {
                //adding the n cell with orientation i
                cArray.add(cells[i]);
            }
            //For each SMC cell with orientation i, the expected matrices will be those of the cArray.
            V1Bank.SMC[dIndex].cells[i].setPrevious(cArray);
            //cleaning the list
            cArray.clear();

            //The result of the matrix is calculated 
            V1Bank.SMC[dIndex].cells[i].mat = Functions.disparityMergeProcess(V1Bank.SMC[dIndex].cells[i].previous, 1);
        }
        //Creating the composed matrix
        V1Bank.SMC[dIndex].composedCell.mat = MatrixUtils.maxSum(V1Bank.SMC[dIndex].cells);
    }

}
