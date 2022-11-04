/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V2Cells;

import VisualMemory.Cell;
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

    /**
     * Curvature Cells constructor
     * @param cells 
     * @param composedCells
     * @param filters
     * @param nAngleDivisions
     * @param nCurvatures 
     */
    public CurvatureCells(Cell[][] cells, Cell[] composedCells, CurvatureFilter[][] filters, int nAngleDivisions, int nCurvatures) {
        this.cells = cells;
        this.composedCells = composedCells;
        this.filters = filters;
        this.nAngleDivisions = nAngleDivisions;
        this.nCurvatures = nCurvatures;
    }

    /**
     * Curvature Cells constructor, useful when the number of curvatures and<br>
     * number of angle divisions is provided
     * @param nCurvatures number of curvatures provided by the user
     * @param nAngleDivisions number of angle divisions
     */
    public CurvatureCells(int nCurvatures, int nAngleDivisions) {
        this.nAngleDivisions = nAngleDivisions;
        this.nCurvatures = nCurvatures;
        cells = new Cell[nCurvatures][nAngleDivisions];
        composedCells = new Cell[nCurvatures];
        filters = new CurvatureFilter[nCurvatures][nAngleDivisions];
        inc = (float) (2 * Math.PI / (float)nAngleDivisions);
        loadCells();
    }

    /**
     * Generate the curvature filters by loading the files in the curvature folder<br>
     * for adding or modifying the filters it is necessary to open the curvature edit sub-program
     * @param fileContent 
     * @param cIndex 
     */
    private void generateCurvatureFiltersByFile(String fileContent, int cIndex) {
        for (int i = 0; i < nAngleDivisions; i++) {
            filters[cIndex][i] = new CurvatureFilter(fileContent, inc * i);
        }
    }
    
    /**
     * It creates the curvature cells
     */
    private void loadCells(){
        for(int i=0;i<nCurvatures;i++){
            for(int j=0;j<nAngleDivisions;j++){
                cells[i][j]=new Cell();
            }
            composedCells[i]=new Cell();
        }
    }

    /**
     * It generates all the filters from all the files in the<br>
     * curvature folder
     * @param folder 
     */
    public void generateFiltersByFolder(String folder) {
        String fileNames[] = FileUtils.getFiles(folder);
        for (int i = 0; i < fileNames.length; i++) {
            String content = FileUtils.readFile(new File(fileNames[i]));
            generateCurvatureFiltersByFile(content, i);
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
