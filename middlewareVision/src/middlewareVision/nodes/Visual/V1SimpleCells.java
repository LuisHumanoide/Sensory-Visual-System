package middlewareVision.nodes.Visual;

import java.awt.image.BufferedImage;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import middlewareVision.core.nodes.FrameActivity;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32FC3;
import static org.opencv.core.CvType.CV_8U;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.numSync;

/**
 *
 *
 */
public class V1SimpleCells extends FrameActivity {

    public Mat[] orsPar;
    public Mat[] orsImpar;
    Mat DKL[];
    public Mat saliencyMap;

    float inc = (float) (Math.PI / 4);
    /**
     * Value of the width of Gabor function
     */
    float sigma = 0.47f * 2f;

    boolean init = false;

    Mat Edges = new Mat();

    public V1SimpleCells() {
        this.ID = AreaNames.V1SimpleCells;
        this.namer = AreaNames.class;
        DKL = new Mat[3];
        orsPar = new Mat[4];
        orsImpar = new Mat[4];
        //initFrames(4, 8);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1SimpleCells");
    }

    numSync sync = new numSync(3);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            /*
            crear un long spike 
             */
            LongSpike spike = new LongSpike(data);
            /*
            si pertenece a la modalidad visual que haga lo suyo alv
             */
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {

                DKL[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                sync.addReceived(index);
            }

            if (sync.isComplete()) {
                int idx = 2;
                orsPar = gaborFilter(DKL[idx], 0);
                orsImpar = gaborFilter(DKL[idx], (double) -Math.PI * 0.3);

                for (int i = 0; i < Config.gaborOrientations; i++) {
                    //convert the matrix to image to showin in the frame
                    //BufferedImage img = Convertor.ConvertMat2Image(orsImpar[i]);
                    //set image in the frame
                    //frame[i].setImage(img, "orientation " + i);
                    /*create a long spike for sending the imagesm the image is converted to bytes
                        The location variable is useful to send the index of the orientation matrix
                     */
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i, 0, V1Layers.COMPLEXCELLS), Convertor.MatToMatrix(orsPar[i]), 0);
                    LongSpike sendSpike2 = new LongSpike(Modalities.VISUAL, new Location(i, 1, V1Layers.COMPLEXCELLS), Convertor.MatToMatrix(orsImpar[i]), 0);
                    /*
                        each orientation matrix is sended to v2 and v4
                     */
                    send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                    send(AreaNames.V1ComplexCells, sendSpike2.getByteArray());
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(V1SimpleCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Edges with specific orientations with Gabor filters
     *
     * @param img Image from the retina in one channel
     */
    public Mat[] gaborFilter(Mat img, double phi) {
        Mat ors[];
        //Imgproc.blur(img, img, new Size(Config.blur, Config.blur));
        ors = new Mat[Config.gaborOrientations];
        Mat kernel = new Mat();
        int kernelSize = 13;
        float angle = 0;
        for (int i = 0; i < Config.gaborOrientations; i++, angle += inc) {
            ors[i] = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
            Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
            img.convertTo(img, CV_32F);
            kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, 2f, 0.8f, phi, CvType.CV_32F);
            Imgproc.filter2D(img, gab, CV_32F, kernel);
            Imgproc.threshold(gab, gab, 0.2, 1, Imgproc.THRESH_TOZERO);
            ors[i] = gab;
        }
        return ors;
    }

}
