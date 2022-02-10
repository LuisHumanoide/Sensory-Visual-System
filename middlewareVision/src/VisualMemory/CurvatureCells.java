/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import java.io.File;
import org.opencv.core.Mat;
import utils.FileUtils;
import utils.Functions;
import utils.filters.CurvatureFilter;

/**
 * Set of specified curvature cells with certain curvature (Radius, angle)
 *
 * @author Laptop
 */
public class CurvatureCells {

    public Cell[][] cells;
    public Cell[] composedCells;
    public CurvatureFilter filters[][];
    int nAngleDivisions;
    int nCurvatures;
    float inc;

    public CurvatureCells(Cell[][] cells, Cell[] composedCells, CurvatureFilter[][] filters, int nAngleDivisions, int nCurvatures) {
        this.cells = cells;
        this.composedCells = composedCells;
        this.filters = filters;
        this.nAngleDivisions = nAngleDivisions;
        this.nCurvatures = nCurvatures;
    }

    public CurvatureCells(int nCurvatures, int nAngleDivisions) {
        this.nAngleDivisions = nAngleDivisions;
        this.nCurvatures = nCurvatures;
        cells = new Cell[nCurvatures][nAngleDivisions];
        composedCells = new Cell[nCurvatures];
        filters = new CurvatureFilter[nCurvatures][nAngleDivisions];
        inc = (float) (2 * Math.PI / nAngleDivisions);
        loadCells();
    }

    private void generateCurvatureFiltersByFile(String fileContent, int cIndex) {
        for (int i = 0; i < nAngleDivisions; i++) {
            filters[cIndex][i] = new CurvatureFilter(fileContent, inc * i);
        }
    }
    
    private void loadCells(){
        for(int i=0;i<nCurvatures;i++){
            for(int j=0;j<nAngleDivisions;j++){
                cells[i][j]=new Cell();
            }
            composedCells[i]=new Cell();
        }
    }

    public void generateFiltersByFolder(String folder) {
        String fileNames[] = FileUtils.getFiles(folder);
        for (int i = 0; i < fileNames.length; i++) {
            String content = FileUtils.readFile(new File(fileNames[i]));
            generateCurvatureFiltersByFile(content, i);
        }
    }
    
    public void filterCurvatureCells(Mat src){
        for(int i=0;i<nCurvatures;i++){
            for(int j=0;j<nAngleDivisions;j++){
                cells[i][j].mat=Functions.curvatureFiltering(src, filters[i][j], true);
            }
            composedCells[i].mat=Functions.maxSum(cells[i]);
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public Cell[] getComposedCells() {
        return composedCells;
    }

    public void setComposedCells(Cell[] composedCells) {
        this.composedCells = composedCells;
    }

    public CurvatureFilter[][] getFilters() {
        return filters;
    }

    public void setFilters(CurvatureFilter[][] filters) {
        this.filters = filters;
    }

    public int getnAngleDivisions() {
        return nAngleDivisions;
    }

    public void setnAngleDivisions(int nAngleDivisions) {
        this.nAngleDivisions = nAngleDivisions;
    }

    public int getnCurvatures() {
        return nCurvatures;
    }

    public void setnCurvatures(int nCurvatures) {
        this.nCurvatures = nCurvatures;
    }

}
