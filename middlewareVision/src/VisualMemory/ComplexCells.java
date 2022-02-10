/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import utils.Functions;

/**
 *
 * @author Laptop
 */
public class ComplexCells {

    public int scale;
    public Cell[] Cells;
    public Cell sumCell;
    public SimpleCells simpleCells;


    public ComplexCells(int scale, Cell[] complexCells) {
        this.scale = scale;
        this.Cells = complexCells;
    }

    public ComplexCells(int scale, int number) {
        this.scale = scale;
        Cells = new Cell[number];
        sumCell=new Cell();
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }
    
    public ComplexCells(int number) {
        this.scale = -1;
        Cells = new Cell[number];
        sumCell=new Cell();
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }
    
    public void setSimpleCells(SimpleCells cells){
        simpleCells=cells;
    }
    
    public void energyProcess(){
        int x=Cells.length;
        for(int i=0;i<x;i++){
            Cells[i].mat=Functions.energyProcess(simpleCells.Even[i].mat, simpleCells.Odd[i].mat);
        }
    }
    
    public ComplexCells(int scale, int number, int n1, int n2, int nf) {
        this.scale = scale;
        Cells = new Cell[number];
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
            Cells[i].setPrevious(V1Bank.SC[n1][nf][n2].Even[i],V1Bank.SC[n1][nf][n2].Odd[i]);
        }
    }


}
