package middlewareVision.nodes.Visual.V4;

import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V4Cells.V4Bank;
import generator.ProcessList;
import gui.Visualizer;
import java.awt.Color;
import matrix.labelMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import cFramework.nodes.process.Process;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;

/**
 *
 *
 */
public class V4Color extends Process {

    public V4Color() {
        this.ID = AreaNames.V4Color;
        this.namer = AreaNames.class;
        DKL_L = new matrix[3];
        DKL_R = new matrix[3];
        matLabel = new Mat[2];
        ProcessList.addProcess(this.getClass().getSimpleName(), true);

    }

    @Override
    public void init() {
    }

    @Override
    public void receive(long nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {

                LongSpike spike = new LongSpike(data);

                if (spike.getModality() == Modalities.VISUAL) {
                    for (int i = 0; i < 3; i++) {
                        DKL_L[i] = Convertor.MatToMatrix(V1Bank.DOC[0][0].Cells[i].mat);
                        DKL_R[i] = Convertor.MatToMatrix(V1Bank.DOC[0][1].Cells[i].mat);
                    }
                    generateLabelMatrix(DKL_L, 0);
                    generateLabelMatrix(DKL_R, 1);
                    
                    V4Bank.colorLabelL=matLabel[0].clone();
                    V4Bank.colorLabelR=matLabel[1].clone();

                    Visualizer.setImage(Convertor.Mat2Img2(matLabel[0]), "color labels L", 4, 3);
                    Visualizer.setImage(Convertor.Mat2Img2(matLabel[1]), "color labels R", 5, 3);
                }

            } catch (Exception ex) {
                // Logger.getLogger(V4Color.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private matrix[] DKL_L, DKL_R;

    //private int NoConcentricCircles = Config.NoConcentricCircles;

    //private int NoRadialDivisions = Config.NoRadialDivisions;

    //private int NoHeightDivisions = Config.NoHeightDivisions;

    Mat matLabel[];

    private labelMatrix generateLabelMatrix(matrix[] mat, int eye) {
        labelMatrix labels = new labelMatrix(mat[0].getWidth(), mat[0].getHeight());
        matLabel[eye] = new Mat(mat[0].getHeight(), mat[0].getWidth(), CvType.CV_8UC3);
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

                float angle=(float) ((float)getAngleLabel(D, K)/(float)Config.NoRadialDivisions)-Config.HueShift;
                float radial=(float) ((float)getConcentricCircleLabel(D, K)/(float)Config.NoConcentricCircles);
                float h=(float) ((float)getHeightLabel(L)/(float)Config.NoHeightDivisions);
                
                Color color = Color.getHSBColor(angle,radial,h);

                matLabel[eye].put(j, i, new byte[]{(byte) color.getRed(),
                    (byte) color.getGreen(), (byte) color.getBlue()});

                labels.setLabel(i, j, colorLabel);
            }
        }
        return labels;
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

    public int getHeightLabel(double L) {
        double div = 1 / (double) Config.NoHeightDivisions;
        return (int) (L / div);
    }

    public int getConcentricCircleLabel(double X, double Y) {
        double r = Math.sqrt((X * X) + (Y * Y));
        if (r >= 1) {
            return Config.NoConcentricCircles - 1;
        } else {
            return (int) (Config.NoConcentricCircles* r);
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

        r = (int) ((g * Config.NoRadialDivisions) / 360);

        return r;
    }

    public int getAngleLabel(double X, double Y) {

        double g;

        g = X / Math.sqrt((X * X) + (Y * Y));
        g = Math.toDegrees(Math.acos(g));
        
        if (Y < 0) {
            g = 360 - g;
        }

        return (int) ((g * Config.NoRadialDivisions) / 360);
    }

}
