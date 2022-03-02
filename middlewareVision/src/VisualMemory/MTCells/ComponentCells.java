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
public class ComponentCells {

    //[speed][orientation]
    public Cell CCells[][];

    int sizeComponent = 0;

    public ComponentCells(int d1, int d2, int size) {
        setComponentCells(d1, d2, size);
    }

    public void setComponentCells(int d1, int d2, int size) {
        sizeComponent = size;
        CCells = new Cell[d1][d2];
        for (int i = 0; i < d1; i++) {
            for (int j = 0; j < d2; j++) {
                CCells[i][j] = new Cell(sizeComponent);
            }
        }
    }
}
