/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import MiniPrograms.RF;
import java.io.File;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 *
 * @author HumanoideFilms
 */
public class FilterUtils {

    public static Mat getFilterByLabel(String label, GaussianFilter g[]) {
        ArrayList<GaussianFilter> glist = new ArrayList();
        for (GaussianFilter gf : g) {
            if (gf.getComb().equals(label)) {
                glist.add(gf);
            }
        }
        Mat filterMats[] = new Mat[glist.size()];
        int i = 0;
        for (GaussianFilter gf : glist) {
            filterMats[i] = gf.makeFilter2();
            i++;
        }
        return MatrixUtils.sum(filterMats, 1, 0);
    }

    public static Mat getDoubleOpponentKernel(String label, GaussianFilter g[]) {
        ArrayList<GaussianFilter> glist = new ArrayList();
        for (GaussianFilter gf : g) {
            if (gf.getComb().equals(label)) {
                glist.add(gf);
            }
        }
        GaussianFilter f1 = glist.get(0);
        GaussianFilter f2 = glist.get(1);

        return SpecialKernels.getDoubleOpponentKernel(new Size(f1.size, f1.size), f1.ry, f2.ry, f1.amp, f2.amp, f1.rx, f2.rx, f1.px, f2.px, f1.py, f2.py);
    }

    public static GaussianFilter[] getGaussians(String file) {
        String fileLines[] = FileUtils.fileLines(file);
        GaussianFilter[] filters;
        filters = new GaussianFilter[fileLines.length];
        for (int i = 0; i < fileLines.length; i++) {
            filters[i] = new GaussianFilter(fileLines[i]);
        }
        return filters;
    }

    /**
     * Obtain the composite filter from a file
     *
     * @param path
     * @return
     */
    public static Mat getComposedFilter(String path) {
        String stList = FileUtils.readFile(new File(path));
        String lines[] = stList.split("\\n");
        ArrayList<Mat> kernelList = new ArrayList();
        for (String st : lines) {
            GaussianFilter g = new GaussianFilter(st);
            kernelList.add(g.makeFilter2());
        }
        Mat compKernel = Mat.zeros(kernelList.get(0).size(), CvType.CV_32FC1);
        for (Mat kn : kernelList) {
            Core.add(compKernel, kn, compKernel);
        }
        return compKernel;
    }


}
