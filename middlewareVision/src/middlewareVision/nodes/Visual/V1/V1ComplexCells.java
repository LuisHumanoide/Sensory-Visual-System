package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.CC;
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

/**
 *
 *
 */
public class V1ComplexCells extends Activity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    Mat energy[];
    int nFrame = 5 * Config.gaborOrientations;

    /**
     * *************************************************************************
     * constructor y metodos para recibir
     * *************************************************************************
     *
     */
    public V1ComplexCells() {
        this.ID = AreaNames.V1ComplexCells;
        this.namer = AreaNames.class;
        energy = new Mat[4];
        //initFrames(4, 8);
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

            Location l = (Location) spike.getLocation();

            /*
            if (spike.getModality() == Modalities.VISUAL) {
                int index = l.getValues()[0];
                Mat evenOrs_L = V1Bank.SC[0][0][0].Even[index].mat.clone();
                Mat oddOrs_L = V1Bank.SC[0][0][0].Odd[index].mat.clone();
                V1Bank.CC[0][0][0].Cells[index].mat=Functions.energyProcess(evenOrs_L , oddOrs_L );
                
                Mat evenOrs_R = V1Bank.SC[0][0][1].Even[index].mat.clone();
                Mat oddOrs_R = V1Bank.SC[0][0][1].Odd[index].mat.clone();
                V1Bank.CC[0][0][1].Cells[index].mat=Functions.energyProcess(evenOrs_R , oddOrs_R );
                
                
                Mat energy2=V1Bank.CC[0][0][0].Cells[index].mat.clone();
                
                Imgproc.resize(energy2, energy2, new Size(Config.motionWidth,Config.motionHeight), INTER_CUBIC);
                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, 
                        new Location(index,0), 0, 0);
           
                LongSpike sendSpike2 = new LongSpike(Modalities.VISUAL, new Location(index), Convertor.MatToMatrix(energy2), 0);
                //send(AreaNames.V1Visualizer, sendSpike3.getByteArray());
                send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
                
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.CC[0][0][0].Cells[index].mat), "energy L"+index, 10, index);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.CC[0][0][1].Cells[index].mat), "energy R"+index, 11, index);
                //send(AreaNames.V1MotionCells,sendSpike2.getByteArray());
                LongSpike spikeMotion= new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                send(AreaNames.ReichardtMotion,spikeMotion.getByteArray());

            }*/
            if (spike.getModality() == Modalities.VISUAL) {
                energyProcessCC();
                for (int k = 0; k < Config.gaborBanks; k++) {
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        Visualizer.setImage(V1Bank.CC[0][k][0].Cells[i].mat, "Complex L" + k + " " + i, Visualizer.getRow("SCsup") + 2 * k, i);
                        Visualizer.setImage(V1Bank.CC[0][k][1].Cells[i].mat, "Complex R" + k + " " + i, Visualizer.getRow("SCsup") + 2 * k + 1, i);
                        if (i == Config.gaborOrientations - 1) {
                            V1Bank.CC[0][k][0].sumCell.mat = Functions.maxSum(V1Bank.CC[0][k][0].Cells);
                            V1Bank.CC[0][k][1].sumCell.mat = Functions.maxSum(V1Bank.CC[0][k][1].Cells);
                            Visualizer.setImage(V1Bank.CC[0][k][0].sumCell.mat, "Combined Complex L" + k + " ", Visualizer.getRow("SCsup") + 2 * k, i + 2);
                            Visualizer.setImage(V1Bank.CC[0][k][1].sumCell.mat, "Combined Complex R" + k + " ", Visualizer.getRow("SCsup") + 2 * k + 1, i + 2);
                        }

                    }

                }
                Visualizer.addLimit("CC", Visualizer.getRow("SCsup") + 2 * (Config.gaborBanks - 1) + 1);

                LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                send(AreaNames.V1HyperComplex, sendSpike.getByteArray());
                send(AreaNames.V1MotionCellsNew, sendSpike.getByteArray());

            }
            if (spike.getModality() == Modalities.ATTENTION) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                    send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
                    Visualizer.setImage(Convertor.Mat2Img(V1Bank.CC[0][0][0].Cells[i].mat), "energy " + i, i + nFrame * 2);
                }
            }

        } catch (Exception ex) {
            //Logger.getLogger(V1ComplexCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void energyProcessCC() {
        int i0 = CC.length;
        int i1 = CC[0].length;
        int i2 = CC[0][0].length;
        for (int x0 = 0; x0 < i0; x0++) {
            for (int x1 = 0; x1 < i1; x1++) {
                for (int x2 = 0; x2 < i2; x2++) {
                    CC[x0][x1][x2].energyProcess();
                }
            }
        }
    }

}
