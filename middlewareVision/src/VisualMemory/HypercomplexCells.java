/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

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
    public Mat[] filters;
    public static float inc = (float) (Math.PI / Config.gaborOrientations);

    public HypercomplexCells(int scale, Cell[][] HypercomplexCells) {
        this.scale = scale;
        this.Cells = HypercomplexCells;
    }
    
    public HypercomplexCells(int scale, int numFilters,int number){
        Cells=new Cell[numFilters][number];
        filters=new Mat[numFilters];
        for(int i=0;i<numFilters;i++){
            filters[i]=new Mat();
            for(int j=0;j<number;j++){
                Cells[i][j]=new Cell();
            }
        }
    }
    
    public HypercomplexCells( int numFilters, int number){
        Cells=new Cell[numFilters][number];
        filters=new Mat[numFilters];
        for(int i=0;i<numFilters;i++){
            filters[i]=new Mat();
            for(int j=0;j<number;j++){
                Cells[i][j]=new Cell();
            }
        }
    }
    
    public HypercomplexCells(int scale, int numFilters,int number, int n1, int n2, int nf){
        Cells=new Cell[numFilters][number];
        filters=new Mat[numFilters];
        for(int i=0;i<numFilters;i++){
            filters[i]=new Mat();
            for(int j=0;j<number;j++){
                Cells[i][j]=new Cell();
                Cells[i][j].setPrevious(V1Bank.CC[n1][nf][n2].Cells[j]);
            }
        }
    }
    
    public void setFilters(Mat[] filters){
        this.filters=filters;
    }
    
    public void setFilter(int index, Mat filter){
        filters[index]=filter.clone();
    }
    
    public void convolve(Cell[] cell){
        for(int i=0;i<Cells.length;i++){
            for(int j=0;j<Config.gaborOrientations;j++){
                float angle = j * inc;
                Cells[i][j].mat=Functions.filter(cell[j].mat, SpecialKernels.rotateKernelRadians(filters[i], angle));
            }
        }
    }
    
}
