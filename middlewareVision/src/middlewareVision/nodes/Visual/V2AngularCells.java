/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual;

import middlewareVision.core.nodes.Frame;
import spike.Location;
import middlewareVision.core.nodes.FrameActivity;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SimpleLogger;
import utils.numSync;

/**
 *
 * @author HumanoideFilms
 */
public class V2AngularCells extends FrameActivity {

    public Mat[] ors;

    public V2AngularCells() {
        this.ID = AreaNames.V2Proccess;
        this.namer = AreaNames.class;
        frame = new Frame[Config.gaborOrientations];
        ors = new Mat[Config.gaborOrientations];
        //inicia 4 frames, le da etiquetas de 4 en adelante
        initFrames(4,12);
        generateKernels();
    }


    @Override
    public void init() {
        //SimpleLogger.log(this, "SMALL NODE CortexV2");
    }

    /**
     * esto es como la puerta de la red de petri, si recibe todos estos indices, se abre
     */
    numSync sync = new numSync(4);

    @Override
    public void receive(int nodeID, byte[] data) {
        LongSpike spike;
        try {
            /*
            ya me dio flojera documentar en inglés alv
            Se crea un spike para recibir la información de otras áreas
            */
            spike = new LongSpike(data);
            /*
            si pertenece a la modalidad visual es aceptado
            */
            if (spike.getModality() == Modalities.VISUAL) {
                //obtiene el indice de la locación
                Location l = (Location) spike.getLocation();
                int index = l.getValues()[0];
                //el indice de la locación se le asigna al indice del array
                ors[index] = Convertor.matrixToMat((matrix)spike.getIntensity());
                //los indices recibidos se agregan al sincronizador
                sync.addReceived(index);

            }
            /*
            si se recibieron todos los indices de la sincronización, entonces hará el proceso descrito
            */
            if (sync.isComplete()) {
                //calcula los mapas de activación angular
                angularProcess();
                //mezcla los mapas de activación con cierta abertura en una sola matriz con la operación de maximo valor de pixel
                mergeAngles(v2map);
                /*
                se muestran los mapas angulares en los frames de v2
                */
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    BufferedImage img = Convertor.ConvertMat2Image(angleMats[i]);
                    frame[i].setImage(img, "angular map V2  " + i);
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(V2AngularCells.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Mat[] angleMats;

    /**
     * do the angular process
     */
    public void angularProcess() {
        filterMatrix(ors);
        angularActivation();
    }

    /**
     * merge the angular maps to reduce information to send
     *
     * @param mat
     */
    public void mergeAngles(Mat[][] mat) {
        angleMats = new Mat[Config.gaborOrientations];
        for (int i = 0; i < mat.length; i++) {
            angleMats[i] = MatrixUtils.maxSum(mat[i]);
        }
    }

    /**
     * *************************************
     * COSAS FEAS ABAJO DE ESTO: 
     *************************************
     */
    float inc = (float) (Math.PI / Config.gaborOrientations);
    /**
     * Value of the width of Gabor function
     */
    float sigma = 0.47f * 2f;

    Mat kernels[];
    public Mat filtered[];
    public Mat v2map[][];

    /**
     * Generate the kernels for the angular convolutions
     */
    public void generateKernels() {
        kernels = new Mat[Config.gaborOrientations * 2];
        int kernelSize = 51;
        float angle = 0;
        for (int i = 0; i < Config.gaborOrientations; i++, angle += inc) {
            kernels[i] = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, 2f, 0.9f, 0, CvType.CV_32F);
        }
        cut0matrix(kernels[0], kernelSize);
        cut1matrix(kernels[1], kernelSize);
        cut2matrix(kernels[2], kernelSize);
        cut3matrix(kernels[3], kernelSize);

    }

    /**
     * Generate the filtered matrix by applying a convolution
     *
     * @param ors
     */
    public void filterMatrix(Mat[] ors) {
        filtered = new Mat[Config.gaborOrientations * 2];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            filtered[i] = ors[i].clone();
            filtered[i + Config.gaborOrientations] = ors[i].clone();
        }
        for (int i = 0; i < Config.gaborOrientations * 2; i++) {
            Imgproc.filter2D(filtered[i], filtered[i], CV_32F, kernels[i]);
            Imgproc.threshold(filtered[i], filtered[i], 0, 1, Imgproc.THRESH_TOZERO);
        }
    }
    /**
     * valor que sirve para ajustar los filtros para cucharear
     */
    double value = -0.015;

    /**
     * cut the filter in half
     *
     * @param mat
     * @param w
     */
    public void cut0matrix(Mat mat, int w) {
        kernels[4] = kernels[0].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (i <= w / 2) {
                    kernels[0].put(i, j, value);
                }
                if (i >= w / 2) {
                    kernels[4].put(i, j, value);
                }
            }

        }
    }

    public void cut1matrix(Mat mat, int w) {
        kernels[5] = kernels[1].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (i < j) {
                    kernels[1].put(i, j, value);
                }
                if (i > j) {
                    kernels[5].put(i, j, value);
                }
            }

        }
    }

    public void cut2matrix(Mat mat, int w) {
        kernels[6] = kernels[2].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (j <= w / 2) {
                    kernels[2].put(i, j, value);
                }
                if (j >= w / 2) {
                    kernels[6].put(i, j, value);
                }
            }

        }
    }

    public void cut3matrix(Mat mat, int w) {
        kernels[7] = kernels[3].clone();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (w - i <= j) {
                    kernels[3].put(i, j, value);
                }
                if (w - i >= j + 1) {
                    kernels[7].put(i, j, value);
                }
            }

        }
    }

    /**
     * print the kernel
     *
     * @param mat
     * @param w
     */
    public void printKernel(Mat mat, int w) {
        String c = "";
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if (mat.get(i, j)[0] > 0) {
                    c = c + "+" + " ";
                }
                if (mat.get(i, j)[0] < 0) {
                    c = c + "-" + " ";
                }
                if (mat.get(i, j)[0] == 0) {
                    c = c + "0" + " ";
                }
            }
            c = c + "\n";
        }
        System.out.println(c);
    }

    /**
     * multiply the matrixes for generating the activation map
     */
    public void angularActivation() {
        v2map = new Mat[Config.gaborOrientations][Config.gaborOrientations * 2];
        String c = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 * 2; j++) {
                v2map[i][j] = new Mat();
                Core.multiply(filtered[j], filtered[(i + j + 1) % 8], v2map[i][j]);
                Imgproc.threshold(v2map[i][j], v2map[i][j], 0, 1, Imgproc.THRESH_TOZERO);
            }
            c = c + "\n";
        }
    }

}
