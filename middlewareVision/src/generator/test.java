/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import middlewareVision.nodes.Visual.V4.V4CellStructure;
import org.opencv.core.Core;

/**
 *
 * @author HumanoideFilms
 */
public class test {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        V4CellStructure.loadKernels("RFV4");
        V4CellStructure.printList();
    }

    public static int label(double d) {
        int divisions = 3;
        double div = 1 / (double) divisions;
        return (int) (d / div);
    }

}
