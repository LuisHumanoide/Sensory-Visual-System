/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapOpener;

import generator.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import matrix.matrix;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import utils.FileUtils;

/**
 *
 * @author HumanoideFilms
 */
public class OpenObjectTest {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        /*
        matrix m=(matrix)ReadObjectFromFile("savedMaps\\1\\Contours\\1.amap");
        Mat mat=Convertor.matrixToMat(m);
        visualizerTest vis=new visualizerTest();
        vis.setFrameSize(mat.height());
        vis.setImage(Convertor.ConvertMat2Image2(mat), "image");
         */

        /*
        Habria que hacer un selector para elegir las carpetas guardadas
         */
        readAll("savedMaps\\1\\");

    }

    /**
     * La ruta tiene que ser savedMaps/nombreDeCarpeta
     *
     * @param root
     */
    public static void readAll(String root) {
        loadV2maps(root + "V2maps\\");
        loadV4maps(root + "V4maps\\");
        loadContours(root + "Contours\\");
    }

    public static void loadV2maps(String path) {
        int i1 = 4;
        int i2 = 8;
        ActivationMaps.v2Map = new Mat[i1][i2];
        String[] files = FileUtils.getFiles(path);
        for (String file : files) {
            String filename = file.replace(path, "").replace(".amap", "");
            String indexes[] = filename.split("_");
            int a1 = Integer.parseInt(indexes[0]);
            int a2 = Integer.parseInt(indexes[1]);
            ActivationMaps.v2Map[a1][a2] = loadMat(file);
        }
    }

    public static void loadV4maps(String path) {
        String[] files = FileUtils.getFiles(path);
        ActivationMaps.V4activationArray = new Mat[files.length];
        for (String file : files) {
            String filename = file.replace(path, "").replace(".amap", "");
            int a1 = Integer.parseInt(filename);
            ActivationMaps.V4activationArray[a1] = loadMat(file);
        }
    }
    
    public static void loadContours(String path){
        String[] files = FileUtils.getFiles(path);
        ActivationMaps.contours = new Mat[files.length];
        for (String file : files) {
            String filename = file.replace(path, "").replace(".amap", "");
            int a1 = Integer.parseInt(filename);
            ActivationMaps.contours[a1-1] = loadMat(file);
        }
        
    }

    public static Mat loadMat(String path) {
        matrix m = (matrix) FileUtils.ReadObjectFromFile(path);
        Mat mat = Convertor.matrixToMat(m);
        return mat;
    }

}
