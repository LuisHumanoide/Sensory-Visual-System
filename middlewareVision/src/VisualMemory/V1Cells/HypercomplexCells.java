/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V1Cells;

import VisualMemory.Cell;
import java.util.ArrayList;
import org.opencv.core.Mat;
import utils.Config;
import utils.Functions;
import utils.SpecialKernels;

/**
 *
 * @author Laptop
 */
public class HypercomplexCells {

    public int scale;
    //[Number of different Filters][Gabor orientations]
    public Cell[][] Cells;
    //[Gabor orientations]
    public Cell[] mergedCells;
    public Mat[] filters;
    public static float inc = (float) (Math.PI / Config.gaborOrientations);

    public HypercomplexCells(int scale, Cell[][] HypercomplexCells) {
        this.scale = scale;
        this.Cells = HypercomplexCells;
    }

    public HypercomplexCells(int scale, int numFilters, int number) {
        Cells = new Cell[numFilters][number];
        mergedCells = new Cell[number];
        filters = new Mat[numFilters];
        for (int i = 0; i < numFilters; i++) {
            filters[i] = new Mat();
            for (int j = 0; j < number; j++) {
                Cells[i][j] = new Cell();
            }
        }
        for (int i = 0; i < number; i++) {
            mergedCells[i] = new Cell();
        }
        setPreviousMergeCells(numFilters,number);
    }

    public HypercomplexCells(int numFilters, int number) {
        Cells = new Cell[numFilters][number];
        mergedCells = new Cell[number];
        filters = new Mat[numFilters];
        for (int i = 0; i < numFilters; i++) {
            filters[i] = new Mat();
            for (int j = 0; j < number; j++) {
                Cells[i][j] = new Cell();
            }
        }
        for (int i = 0; i < number; i++) {
            mergedCells[i] = new Cell();
        }
        setPreviousMergeCells(numFilters,number);
    }

    public HypercomplexCells(int scale, int numFilters, int number, int n2, int nf) {
        Cells = new Cell[numFilters][number];
        mergedCells = new Cell[number];
        filters = new Mat[numFilters];
        for (int i = 0; i < numFilters; i++) {
            filters[i] = new Mat();
            for (int j = 0; j < number; j++) {
                Cells[i][j] = new Cell();
                Cells[i][j].setPrevious(V1Bank.CC[nf][n2].Cells[j]);
            }
        }
        for (int i = 0; i < number; i++) {
            mergedCells[i] = new Cell();
        }
        setPreviousMergeCells(numFilters,number);
    }

    public void setPreviousMergeCells(int n1, int n2) {
        ArrayList<Cell> previousCells = new ArrayList<Cell>();
        for (int j = 0; j < n2; j++) {
            for (int i = 0; i < n1; i++) {
                previousCells.add(Cells[i][j]);
            }
            mergedCells[j].setPrevious(previousCells);
            previousCells.clear();
        }
    }

    public void setFilters(Mat[] filters) {
        this.filters = filters;
    }

    public void setFilter(int index, Mat filter) {
        filters[index] = filter.clone();
    }

}
