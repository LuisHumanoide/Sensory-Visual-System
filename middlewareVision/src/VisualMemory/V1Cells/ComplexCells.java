/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V1Cells;

import VisualMemory.Cell;
import utils.Config;
import utils.Functions;

/**
 *
 * @author Laptop
 */
public class ComplexCells {

    public int scale;
    //Cells
    public Cell[] Cells;
    //Summation of the Cells array
    public Cell sumCell;
    //Simple Cells used as a previous to make the process of obtaining the complex cells
    public SimpleCells simpleCells;

    /**
     * Constructor with scale and an array of complex cells
     * @param scale
     * @param complexCells 
     */
    public ComplexCells(int scale, Cell[] complexCells) {
        this.scale = scale;
        this.Cells = complexCells;
    }

    /**
     * Constructor for Complex Cells <br>
     * with parameters scale and number of cells
     * @param scale
     * @param number 
     */
    public ComplexCells(int scale, int number) {
        this.scale = scale;
        Cells = new Cell[number];
        sumCell = new Cell();
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }

    /**
     * Constructor for CC with only the number of Cells
     * @param number 
     */
    public ComplexCells(int number) {
        this.scale = -1;
        Cells = new Cell[number];
        sumCell = new Cell();
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }

    /**
     * Set the Simple Cells
     * @param cells 
     */
    public void setSimpleCells(SimpleCells cells) {
        simpleCells = cells;
        for (int i = 0; i < Config.gaborOrientations; i++) {
            Cells[i].setPrevious(simpleCells.Even[i],simpleCells.Odd[i]);
        }
    }

    /**
     * Constructor with the following parameters
     * @param scale number of scales/frequencies
     * @param number number of the array of Complex Cells
     * @param n2 eye
     * @param nf scale/frequency
     */
    public ComplexCells(int scale, int number, int n2, int nf) {
        this.scale = scale;
        Cells = new Cell[number];
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
            Cells[i].setPrevious(V1Bank.SC[nf][n2].Even[i], V1Bank.SC[nf][n2].Odd[i]);
        }
    }

}
