package middlewareVision.nodes.Visual.V4;

import VisualMemory.V1Cells.V1Bank;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.labelMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.numSync;

/**
 *
 *
 */
public class V4Color extends Activity {


    /*
    ****************************************************************************
    Constructor y metodos para recibir
    ****************************************************************************
     */
    public V4Color() {
        this.ID = AreaNames.V4Color;
        this.namer = AreaNames.class;
        DKL = new matrix[3];

    }

    @Override
    public void init() {
    }

    numSync sync = new numSync(3);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {

            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            int i1 = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {

                DKL[i1] = Convertor.MatToMatrix(V1Bank.DOC[0][0].Cells[i1].mat);
                sync.addReceived(i1);
            }

            if (sync.isComplete()) {
                generateLabelMatrix(DKL);
                
                Visualizer.setImage(Convertor.Mat2Img2(matLabel), "color labels", 2*Config.gaborOrientations+3);
            }

        } catch (Exception ex) {
           // Logger.getLogger(V4Color.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    ****************************************************************************
    variables y constantes
    ****************************************************************************
     */
    private matrix[] DKL;

    private int NoConcentricCircles = 4;

    private int NoRadialDivisions = 12;
    
    private int NoHeightDivisions = 5;

    /*
    ****************************************************************************
    metodos nuevos
    ****************************************************************************
     */
    
    Mat matLabel;
    private labelMatrix generateLabelMatrix(matrix[] mat) {
        labelMatrix labels = new labelMatrix(mat[0].getWidth(), mat[0].getHeight());
        matLabel=new Mat(mat[0].getHeight(), mat[0].getWidth(), CvType.CV_8UC3);
        for (int i = 0; i < mat[0].getWidth(); i++) {
            for (int j = 0; j < mat[0].getHeight(); j++) {
                double D = mat[0].getValue(i, j);
                double K = mat[1].getValue(i, j);
                double L = mat[2].getValue(i, j);
                if (D > 1) {
                    D = 1;
                }
                if (K > 1) {
                    K = 1;
                }
                if (D < -1) {
                    D = -1;
                }
                if (K < -1) {
                    K = -1;
                }
                int[] colorLabel = {getConcentricCircleLabel(D, K), getAngleLabel(D, K), getHeightLabel(L)};
                matLabel.put(j, i, new byte[]{(byte)(getConcentricCircleLabel(D, K)*(255/NoConcentricCircles)),
                    (byte)(getAngleLabel(D, K)*(255/NoRadialDivisions)),(byte)(getHeightLabel(L)*(255/NoHeightDivisions))});
                labels.setLabel(i, j, colorLabel);
            }
        }
        return labels;
    }

    /*
    ****************************************************************************
    metodos
    hechos por Dani
    ****************************************************************************
     */
    public int getConcentricCircles() {
        return NoConcentricCircles;
    }

    public void setConcentricCircles(int noCircles) {
        this.NoConcentricCircles = noCircles;
    }

    public int getRadialDivisions() {
        return NoRadialDivisions;
    }

    public void setRadialDivisions(int Divisions) {
        this.NoRadialDivisions = Divisions;
    }

    public String assignColorLabel(double D, double K) {
        String colorLabel;
        if (D > 1) {
            D = 1;
        }
        if (K > 1) {
            K = 1;
        }
        if (D < -1) {
            D = -1;
        }
        if (K < -1) {
            K = -1;
        }

        colorLabel = getConcentricCircleLabel(D, K) + "," + getAngleLabel(D, K);

        return colorLabel;
    }
    
    public int getHeightLabel(double L){
        double div=1/(double)NoHeightDivisions;
        return (int) (L/div);
    }

    public int getConcentricCircleLabel(double X, double Y) {
        double r = Math.sqrt((X * X) + (Y * Y));
        if (r >= 1) {
            return this.NoConcentricCircles - 1;
        } else {
            return (int) (this.NoConcentricCircles * r);
        }

    }

    public int getAngleOtherLabel(double X, double Y) {

        double v2 = Math.sqrt((X * X) + (Y * Y));
        double g;
        int r;
        X = X / v2;
        Y = Y / v2;

        g = X / Math.sqrt((X * X) + (Y * Y));
        g = Math.toDegrees(Math.acos(g));

        if (Y < 0) {
            g = 360 - g;
        }

        r = (int) ((g * this.NoRadialDivisions) / 360);

        return r;
    }

    public int getAngleLabel(double X, double Y) {

        double g;

        g = X / Math.sqrt((X * X) + (Y * Y));
        g = Math.toDegrees(Math.acos(g));

        if (Y < 0) {
            g = 360 - g;
        }

        return (int) ((g * this.NoRadialDivisions) / 360);
    }

}
