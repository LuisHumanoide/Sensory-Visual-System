/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual;

import spike.Location;
import middlewareVision.core.nodes.FrameActivity;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import static org.opencv.core.Core.FILLED;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_8UC3;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.drawContours;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SimpleLogger;
import utils.numSync;

public class V4Contour extends FrameActivity {

    public Mat[] ors;
    public Mat combinedEdges;
    public Mat contours1;
    public Mat contours2;

    /**
     * Constructor of the class
     */
    public V4Contour() {
        this.ID = AreaNames.V4Contour;
        this.namer = AreaNames.class;
        ors = new Mat[Config.gaborOrientations];
        initFrames(3,16);
    }

    @Override
    public void init() {
        //SimpleLogger.log(this, "SMALL NODE V4");
    }
    /*
     * synchonizer utils
     */
    numSync sync = new numSync(4);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {

            LongSpike spike = new LongSpike(data);

            if (spike.getModality() == Modalities.VISUAL) {
                Location l = (Location) spike.getLocation();
                int index = l.getValues()[0];
                ors[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                /*
                add the received index to the sync
                */
                sync.addReceived(index);

            }
            /*
            if the sync of matrix is complete
            */
            if (sync.isComplete()) {
                combinedEdges=MatrixUtils.maxSum(ors);
                BufferedImage img = Convertor.ConvertMat2Image(combinedEdges);
                frame[0].setImage(img, "combined");
                
                
                
                contours1=drawMatContours(combinedEdges, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_TC89_L1);
                BufferedImage img2 = Convertor.ConvertMat2Image2(contours1);
                frame[1].setImage(img2 , "contours1");
                contours2=drawMatContours(combinedEdges, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
                BufferedImage img3 = Convertor.ConvertMat2Image2(contours2);
                frame[2].setImage(img3 , "contours2");
                
                /**
                 * si la matriz es normal en colores de 0 a 255, se puede convertir directo a bytes sin pasar por la imagen
                 */
                LongSpike sendSpike = new LongSpike(Modalities.VISUAL, 0, Convertor.matToBytes(contours1), 0);
                send(AreaNames.ITC, sendSpike.getByteArray());
                
            }
            
        } catch (Exception ex) {
            Logger.getLogger(V4Contour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    /**
     * Do the contour integration by find contours
     *
     * @param mat the threholded matrix
     * @return a matrix of points of the contours
     */
    public List<MatOfPoint> ContourIntegration(Mat mat, int mode, int method) {
        System.out.println("contornos");
        Mat dst = mat;
        List<MatOfPoint> points = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dst, points, hierarchy, mode, method);
        //Imgproc.findContours(dst, points, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_TC89_L1);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);
        return points;
    }

    /**
     * Draw the contours in a color matrix
     * @param src mat with values between 0 and 1
     * @return a color matrix of contours
     */
    double thresh=0.6;
    public Mat drawMatContours(Mat src, int mode, int method) {
        Mat thresolded=new Mat();
        Imgproc.threshold(src, thresolded, thresh, 1, Imgproc.THRESH_BINARY);
        thresolded.convertTo(thresolded, CvType.CV_8U);      
        List<MatOfPoint> contours = ContourIntegration(thresolded, mode, method);
        Mat contoursMat;
        contoursMat = Mat.zeros(src.size(), CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            drawContours(contoursMat, contours, i, new Scalar(i * 20 % 255, i * 50 % 255, 200), FILLED);
        }
        contours.clear();
        return contoursMat;
    }
}
