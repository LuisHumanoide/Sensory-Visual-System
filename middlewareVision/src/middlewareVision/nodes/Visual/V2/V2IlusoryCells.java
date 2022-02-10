package middlewareVision.nodes.Visual.V2;

import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.SpecialKernels;

/**
 * Author: Elon Musk
 *
 */
public class V2IlusoryCells extends Activity {

    /**
     * *************************************************************************
     * Init variables
     * *************************************************************************
     */
    float sigma = 0.47f * 2f;
    float inc = (float) (Math.PI / 4);

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V2IlusoryCells() {
        this.ID = AreaNames.V2IlusoryCells;
        this.namer = AreaNames.class;
        //initFrames(4, 12);
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            /*
            extract the variable needed for the sync
             */
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                Mat edges = Convertor.matrixToMat((matrix) spike.getIntensity());
                Mat ilusoryEdges;
                //ilusoryEdges = elongatedGaborFilter(edges, sigma * 0.5f, 1, 5, 29, 0.05, index);
                ilusoryEdges = ilusoryEdgesProcess(edges, index);
                Core.multiply(ilusoryEdges, new Scalar(-0.02), ilusoryEdges);
                //ilusoryEdges = MatrixUtils.maxSum(ilusoryEdges, edges);
                Core.add(edges, ilusoryEdges, ilusoryEdges);
                Imgproc.threshold(ilusoryEdges, ilusoryEdges, 0.4, 1, Imgproc.THRESH_TOZERO);
                LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(index), Convertor.MatToMatrix(ilusoryEdges), 0);
                send(AreaNames.V2AngularCells, sendSpike.getByteArray());
                send(AreaNames.V4Contour, sendSpike.getByteArray());
                //send(AreaNames.V2Visualizer, sendSpike.getByteArray());

            }

        } catch (Exception ex) {
           // Logger.getLogger(V2IlusoryCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    /**
     *
     * @param img array of matrixes
     * @param sigma i dont know what is sigma
     * @param psi offset
     * @param kernelSize size of kernel
     * @param lenght lenght of the gabor function
     * @param aspectRatio <0.5 elongated @retur n
     */

    
    public Mat ilusoryEdgesProcess(Mat img, int index){
        Mat ors = new Mat();
        Mat kernel1 = new Mat();
        Mat kernel2 = new Mat();
        kernel1=SpecialKernels.ilusoryFilters.get(index).getFilter1();
        kernel2=SpecialKernels.ilusoryFilters.get(index).getFilter2();
        Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //angle of the orientation
        float angle = index * inc;
        //initializate the ors and gab array matrix with zeros
        ors = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat filtered1 = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat filtered2 = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //generate the gabor filter
        //kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, lenght, aspectRatio, psi, CvType.CV_32F);
        // Imgproc.getga
        //perform the convolution on the image IMG with the filter GAB
        Imgproc.filter2D(img, filtered1, CV_32F, kernel1);
        Imgproc.filter2D(img, filtered2, CV_32F, kernel2);
        
        Core.multiply(filtered1, filtered2, gab);
        //apply a threshold from the value 0.2 to 1
        Imgproc.threshold(gab, gab, 0, 1, Imgproc.THRESH_TOZERO);
        ors = gab;
        return ors;
    }
    
    
}
