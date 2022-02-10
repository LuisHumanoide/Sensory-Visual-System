/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;
/**
 *
 * @author Laptop
 */
public class LGNBank {

    public static SimpleOpponentCells[][][] SOC;

    public static void initializeCells(int... dimensions) {

        SOC = new SimpleOpponentCells[dimensions[0]][dimensions[1]][dimensions[2]];
        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < dimensions[1]; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    SOC[i1][i2][i3] = new SimpleOpponentCells(0, 3);
                }
            }
        }

    }

}
