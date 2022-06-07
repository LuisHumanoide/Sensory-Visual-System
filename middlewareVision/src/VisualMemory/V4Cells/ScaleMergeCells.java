/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V4Cells;

import VisualMemory.Cell;

/**
 *
 * @author luish
 */
public class ScaleMergeCells {
    
    public String nameCell;
    public Cell cell;
    
    public ScaleMergeCells(){
        cell=new Cell();
    }
    
    public void setName(String name){
        nameCell=name;
    }
    
}
