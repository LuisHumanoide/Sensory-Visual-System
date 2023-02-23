package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import gui.Visualizer;
import spike.Location;
import cFramework.nodes.process.Process;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MatrixUtils;
import static utils.SpecialKernels.displaceKernel;
import utils.numSync;

/**
 *
 *
 */

public class ReichardtMotion extends Process {

    public ReichardtMotion() {
        this.ID = AreaNames.ReichardtMotion;
        this.namer = AreaNames.class;
        M = new Mat[Config.gaborOrientations][nFrames];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            for (int f = 0; f < nFrames; f++) {
                M[i][f] = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
            }
        }
        dif = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
    }

    Mat[][] M;
    int Δd = 1;
    int nFrames = 3;
    Mat dif;
    //numSync sync=new numSync(4);

    @Override
    public void init() {
    }
    numSync sync = new numSync(4);

    @Override
    public void receive(long nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            if (spike.getModality() == Modalities.VISUAL) {
                //obtiene el primer valor del arreglo
                int index = l.getValues()[0];
                sync.addReceived(index);
                
                for (int i = 1; i < nFrames; i++) {
                    M[index][i] = displaceKernel(M[index][i - 1], index * (float) (Math.PI / Config.gaborOrientations), Config.displace);
                }
                M[index][0] = V1Bank.SC[0][0].Even[index].mat.clone();
                Visualizer.setImage(Convertor.Mat2Img(MatrixUtils.multiply(M[index])), "basic motion 2", 28*2 + index);
            }

        } catch (Exception ex) {
            Logger.getLogger(ReichardtMotion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
