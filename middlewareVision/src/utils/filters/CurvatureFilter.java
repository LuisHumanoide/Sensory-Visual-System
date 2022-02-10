/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import utils.SpecialKernels;

/**
 *
 * @author Laptop
 */
public class CurvatureFilter {

    public GaborFilter mainGabor;
    public int radius;
    public double angleDisp;
    public int n;
    public double mul;
    public double angle;
    public Mat concaveFilters[];
    public Mat convexFilters[];

    public CurvatureFilter(GaborFilter mainFilter, int radius, double angleDisp, int n, double mul, double angle) {
        this.mainGabor = mainFilter;
        this.radius = radius;
        this.angleDisp = angleDisp;
        this.n = n;
        this.mul = mul;
        this.angle = angle;
        concaveFilters = new Mat[n];
        convexFilters = new Mat[n];
    }

    public CurvatureFilter(String line, double angle) {
        setFromString(line, angle);
    }

    /**
     * Set the parameters from the file line
     *
     * @param line is the string with the parameters
     */
    public void setFromString(String line, double angle) {
        String values[] = line.split(" ");
        mainGabor = new GaborFilter(toInt(values[0]), toD(values[1]),
                toD(values[2]), toD(values[3]), toD(values[4]), toD(values[5]));
        radius = toInt(values[6]);
        angleDisp = toD(values[7]);
        this.angle = angle;
        n = toInt(values[9]);
        mul = toD(values[10]);
        generateFilters();
    }

    public void generateFilters() {
        concaveFilters = new Mat[n];
        convexFilters = new Mat[n];
        for (int i = 0; i < n; i++) {
            concaveFilters[i] = new Mat();
            convexFilters[i] = new Mat();
        }
        Mat mainFilter = mainGabor.makeFilter();
        concaveFilters[0] = mainFilter.clone();
        convexFilters[0] = mainFilter.clone();
        for (int i = 1; i < n; i++) {
            if (i % 2 == 0) {
                concaveFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, radius, 0, angleDisp * (i / 3 + 1));
                convexFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, -radius, 0, angleDisp * (i / 3 + 1));
            } else {
                concaveFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, radius, 0, -angleDisp * (i / 3 + 1));
                convexFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, -radius, 0, -angleDisp * (i / 3 + 1));
            }
        }
        for (int i = 0; i < n; i++) {
            concaveFilters[i] = SpecialKernels.rotateKernelRadians(concaveFilters[i], angle);
            convexFilters[i] = SpecialKernels.rotateKernelRadians(convexFilters[i], angle);
        }
    }

    int toInt(String value) {
        return Integer.parseInt(value.trim());
    }

    double toD(String value) {
        return Double.parseDouble(value.trim());
    }

}
