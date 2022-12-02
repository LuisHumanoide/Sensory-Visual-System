package middlewareVision.nodes.Visual.Retina;

import VisualMemory.V1Cells.V1Bank;
import generator.ProcessList;
import gui.Visualizer;
import spike.Location;
import cFramework.nodes.process.Process;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.numSync;

/**
 *
 *
 */
public class BasicMotion extends Process {

    public BasicMotion() {
        this.ID = AreaNames.BasicMotion;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);

        LMSCones = new Mat[3];
        M1 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        M2 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d1 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d2 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d3 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d4 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);

    }

    Mat LMSCones[];
    Mat M1;
    Mat M2;
    Mat d1;
    Mat d2;
    Mat d3;
    Mat d4;

    @Override
    public void init() {
    }

    numSync sync = new numSync(3);

    @Override
    public void receive(long nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                //si es de la modalidad visual entonces acepta
                if (spike.getModality() == Modalities.VISUAL) {
                    //obtiene el indice de la locación
                    Location l = (Location) spike.getLocation();
                    //obtiene el primer valor del arreglo
                    int index = l.getValues()[0];
                    //convierte el objeto matrix serializable en una matriz de opencv y la asigna al arreglo LMNCones
                    LMSCones[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    //los indices recibidos se agregan al sincronizador
                    sync.addReceived(index);
                }
                if (sync.isComplete()) {
                    M2 = M1;
                    M1 = LMSCones[2];
                    Mat diff = new Mat();
                    Core.subtract(M2, M1, diff);
                    Core.pow(diff, 2, diff);
                    Imgproc.blur(diff, diff, new Size(5,5));
                    Imgproc.threshold(diff, diff, 0.001, 1, Imgproc.THRESH_BINARY);
                    
                    V1Bank.motionDiff=diff;
                    Visualizer.setImage(Convertor.Mat2Img(diff), "basic motion mask", 0, 3);
                    //Visualizer.setImage(Convertor.Mat2Img(SpecialKernels.displaceKernel(LMSCones[2], 60 , 50)), "displace", 7);

                }

            } catch (Exception ex) {
                Logger.getLogger(BasicMotion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
