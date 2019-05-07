package middlewareVision.nodes.Visual;

import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.labelMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;
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
        SimpleLogger.log(this, "SMALL NODE V4Color");
    }

    numSync sync = new numSync(3);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {

            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            int i1 = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {

                DKL[i1] = (matrix) spike.getIntensity();
                sync.addReceived(i1);
            }

            if (sync.isComplete()) {
                labelMatrix labels = generateLabelMatrix(DKL);
                
                
                LongSpike labelSpike = new LongSpike(Modalities.VISUAL, new Location(0), labels, 0);
                LongSpike LChannelSpike = new LongSpike(Modalities.VISUAL, new Location(1), DKL[2], 0);
                send(AreaNames.ITC, labelSpike.getByteArray());
                send(AreaNames.ITC, LChannelSpike.getByteArray());
            }

        } catch (Exception ex) {
            Logger.getLogger(V4Color.class.getName()).log(Level.SEVERE, null, ex);
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

    /*
    ****************************************************************************
    metodos nuevos
    ****************************************************************************
     */
    private labelMatrix generateLabelMatrix(matrix[] mat) {
        labelMatrix labels = new labelMatrix(mat[0].getWidth(), mat[0].getHeight());
        for (int i = 0; i < mat[0].getWidth(); i++) {
            for (int j = 0; j < mat[0].getHeight(); j++) {
                double D = mat[0].getValue(i, j);
                double K = mat[1].getValue(i, j);
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
                int[] colorLabel = {getConcentricCircleLabel(D, K), getAngleLabel(D, K)};
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
