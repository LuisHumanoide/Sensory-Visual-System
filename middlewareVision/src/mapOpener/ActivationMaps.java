/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapOpener;

import java.util.ArrayList;
import org.opencv.core.Mat;

/**
 *
 * @author HumanoideFilms
 */
public class ActivationMaps {
    
    static Mat [][] v2Map;
    static Mat[] V4activationArray;
    static Mat[] contours;

    public static Mat[][] getV2Map() {
        return v2Map;
    }

    public static void setV2Map(Mat[][] v2Map) {
        ActivationMaps.v2Map = v2Map;
    }

    public static Mat[] getV4activationArray() {
        return V4activationArray;
    }

    public static void setV4activationArray(Mat[] V4activationArray) {
        ActivationMaps.V4activationArray = V4activationArray;
    }

    public static Mat[] getContours() {
        return contours;
    }

    public static void setContours(Mat[] contours) {
        ActivationMaps.contours = contours;
    }


    
    
}
