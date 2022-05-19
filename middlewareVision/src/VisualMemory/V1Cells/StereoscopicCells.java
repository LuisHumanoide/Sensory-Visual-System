/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VisualMemory.V1Cells;

import VisualMemory.Cell;
import utils.Config;

/**
 * StereoscopicCells wrap the simple and complex stereoscopic cells
 *
 * @author HumanoideFilms
 */
public class StereoscopicCells {

    public int scale;
    public double frequency;
    public int disparity;
    public SimpleCells[] simpleCells;
    public Cell[] evenCells;
    public Cell[] oddCells;
    public Cell[] complexCells;
    public Cell[] normalizedCells;

    public Cell composedEvenCell;
    public Cell composedOddCell;
    public Cell composedComplexCell;
    public Cell composedNormalizedCell;

    /**
     * Constructor for the stereoscopic cells
     *
     * @param disparity
     * @param simpleCells
     */
    public StereoscopicCells(int disparity, SimpleCells[] simpleCells) {
        this.disparity = disparity;
        this.simpleCells = simpleCells;

        evenCells = new Cell[Config.gaborOrientations];
        oddCells = new Cell[Config.gaborOrientations];
        complexCells = new Cell[Config.gaborOrientations];
        normalizedCells = new Cell[Config.gaborOrientations];

        composedEvenCell = new Cell();
        composedOddCell = new Cell();
        composedComplexCell = new Cell();
        composedNormalizedCell = new Cell();

        for (int i = 0; i < Config.gaborOrientations; i++) {

            evenCells[i] = new Cell();
            evenCells[i].setPrevious(simpleCells[0].Even[i], simpleCells[1].Even[i]);

            oddCells[i] = new Cell();
            oddCells[i].setPrevious(simpleCells[0].Odd[i], simpleCells[1].Odd[i]);

            complexCells[i] = new Cell();
            complexCells[i].setPrevious(evenCells[i], oddCells[i]);
            
            normalizedCells[i] = new Cell();
            normalizedCells[i].setPrevious(complexCells[i],simpleCells[0].Even[i], simpleCells[1].Even[i],simpleCells[0].Odd[i], simpleCells[1].Odd[i]);

        }
    }

    public void printCell() {
        utils.Msg.print("cell with disparity : " + disparity);
    }

}
