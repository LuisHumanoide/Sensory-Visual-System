/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.MSTCells.MSTPolar;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;
import VisualMemory.V3Cells.V3Bank;
import VisualMemory.V4Cells.V4Bank;

/**
 *
 * @author Laptop
 */
public class InitCellMemory {
    
    public static void initCellMemory(){
        //extra,frequencies,eyes
        LabeledCells.initMap();
        LGNBank.initializeCells(1);
        V1Bank.initializeCells();
        V3Bank.initializeCells();
        //MT starts automatically with V1
        V2Bank.initializeCells();
        V4Bank.initializeCells();
        MSTPolar.initializeCells();
    }
    
}
