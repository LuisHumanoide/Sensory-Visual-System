/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.MTCells;

import VisualMemory.Cell;

/**
 *
 * @author HumanoideFilms
 */
public class PatternCells {

    public Cell Cells[];

    public PatternCells(int n, int size) {
        Cells = new Cell[n];
        for (int i = 0; i < n; i++) {
            Cells[i] = new Cell(size);
        }
    }

}
