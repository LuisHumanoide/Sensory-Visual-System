/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.LGNCells;

import VisualMemory.LGNCells.SimpleOpponentCells;

/**
 *
 * @author Laptop
 */
public class LGNBank {

    public static SimpleOpponentCells[][] SOC;

    public static void initializeCells(int scales) {

        SOC = new SimpleOpponentCells[scales][2];

        for (int i2 = 0; i2 < scales; i2++) {
            for (int i3 = 0; i3 < 2; i3++) {
                SOC[i2][i3] = new SimpleOpponentCells(0, 3);
            }
        }

    }

}
