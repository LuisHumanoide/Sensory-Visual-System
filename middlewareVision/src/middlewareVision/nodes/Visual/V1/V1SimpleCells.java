package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.SC;
import spike.Location;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import gui.Visualizer;
import kmiddle2.nodes.activities.Activity;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;
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
    }

    @Override
    public void init() {
    }

    //sync that receive 3 indexes
    numSync sync = new numSync(3);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            /*
            receive spike
             */
            LongSpike spike = new LongSpike(data);
            /*
            extract the variable needed for the sync
             */
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                //add the index to the sync
                sync.addReceived(index);
            }

            if (sync.isComplete()) {
                //edge border detection is performed, with phi angle = 0
                convolveSimpleCells(V1Bank.DOC[0][0].Cells[2].mat, V1Bank.DOC[0][1].Cells[2].mat);
                for (int k = 0; k < Config.gaborBanks; k++) {
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][0].Even[i].mat), "even L bank" + k + " " + i, 4 * k + 6, i);
                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][0].Odd[i].mat), "odd L bank" + k + " " + i, 4 * k + 8, i);

                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][1].Even[i].mat), "even R bank" + k + " " + i, 4 * k + 7, i);
                        Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][1].Odd[i].mat), "odd R bank" + k + " " + i, 4 * k + 9, i);

                        if (i == Config.gaborOrientations - 1) {
                            Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][0].Even)), "Combined even L bank" + k + " " + i, 4 * k + 6, Config.gaborOrientations+1);
                            Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][0].Odd)), "Combined odd L bank" + k + " " + i, 4 * k + 8, Config.gaborOrientations+1);

                            Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][1].Even)), "Combined even R bank" + k + " " + i, 4 * k + 7, Config.gaborOrientations+1);
                            Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][1].Odd)), "Combined odd R bank" + k + " " + i, 4 * k + 9, Config.gaborOrientations+1);
                        }
                    }
                }
                Visualizer.addLimit("SCinf", 6);
                Visualizer.addLimit("SCsup", 4 * (Config.gaborBanks - 1) + 10);

                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
            }

            if (spike.getModality() == Modalities.ATTENTION) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                    send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                    Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[0][0].Even[i].mat), "even " + i, nFrame + i);
                    Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[0][0].Odd[i].mat), "even " + i, nFrame + i + 4);
                }
            }

        } catch (Exception ex) {
            //Logger.getLogger(V1SimpleCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * make and generate the responsed of all simple cells
     * @param input
     */
    void convolveSimpleCells(Mat inputL, Mat inputR) {
        int i0 = SC.length;
        int i1 = SC.length;
        for (int x0 = 0; x0 < i0; x0++) {
            for (int x1 = 0; x1 < i1; x1++) {
                SC[x1][0].convolve(inputL);
                SC[x1][1].convolve(inputR);
            }
        }
    }

}
