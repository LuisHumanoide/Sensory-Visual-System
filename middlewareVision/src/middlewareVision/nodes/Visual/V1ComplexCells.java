package middlewareVision.nodes.Visual;

import imgio.RetinalImageIO;
import imgio.RetinalTextIO;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import middlewareVision.core.nodes.FrameActivity;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.numSync;

/**
 *
 *
 */
public class V1ComplexCells extends FrameActivity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    Mat ors[][];
    Mat energy[];

    /**
     * *************************************************************************
     * constructor y metodos para recibir
     * *************************************************************************
     *
     */
    public V1ComplexCells() {
        this.ID = AreaNames.V1ComplexCells;
        this.namer = AreaNames.class;
        ors = new Mat[4][2];
        energy = new Mat[4];
        initFrames(4, 8);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1this");
    }

    //espero 8 matrices
    numSync sync = new numSync(8);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

            Location l = (Location) spike.getLocation();
            int i1 = l.getValues()[0];
            int i2 = l.getValues()[1];

                
                if (spike.getModality() == Modalities.VISUAL) {

                    ors[i1][i2] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    sync.addReceived(i1 + i2 * 4);
                }

                if (sync.isComplete()) {
                    System.out.println("si se completo");
                    energyProcess(ors);
                    for (int i = 0; i < 4; i++) {
                        frame[i].setImage(Convertor.ConvertMat2Image(energy[i]), "energy " + i);
                        LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(i), Convertor.MatToMatrix(energy[i]), 0);
                        send(AreaNames.V2, sendSpike.getByteArray());
                        send(AreaNames.V4Contour, sendSpike.getByteArray());
                    }
                }

        } catch (Exception ex) {
            Logger.getLogger(V1ComplexCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    public void energyProcess(Mat mat[][]) {
        energy = new Mat[4];
        Mat r1, r2;

        for (int i = 0; i < 4; i++) {
            energy[i] = Mat.zeros(mat[0][0].rows(), mat[0][0].cols(), CvType.CV_32FC1);
            r1 = mat[i][0];
            r2 = mat[i][1];

            Core.pow(r1, 2, r1);
            Core.pow(r2, 2, r2);

            Core.add(r1, r2, r1);

            Core.sqrt(r1, energy[i]);

            Imgproc.threshold(energy[i], energy[i], 0.2, 1, Imgproc.THRESH_TOZERO);
        }
    }
}
