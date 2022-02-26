/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.V1Cells.V1Bank;
import VisualMemory.V2Cells.V2Bank;

/**
 *
 * @author Laptop
 */
public class InitCellMemory {
    
    public static void initCellMemory(){
        //extra,frequencies,eyes
        LGNBank.initializeCells(1,1,2);
        V1Bank.initializeCells(1,2,2);
        V2Bank.initializeCells(1,1,2);
    }
    
}
