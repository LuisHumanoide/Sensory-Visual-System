/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V4Cells;

import VisualMemory.Cell;
import java.util.ArrayList;
import java.util.HashSet;
import utils.FileUtils;

/**
 *
 * @author luish
 */
public class V4Bank {
    
    //[index of shape][eye]
    public static SimpleShapeCells[][] SSC;
    public static ScaleMergeCells[][] SMC;
    static HashSet<String> names;
    static String folder = "RFV4";

    /**
     * Initialize the cells of V4
     */
    public static void initializeCells() {
        //read the files of the receptive fields
        String[] files = FileUtils.getFiles(folder);
        //assign memory to Simple Shape Cells
        SSC = new SimpleShapeCells[files.length][2];
        names=new HashSet();
        //for each file found in the folder
        for (int i = 0; i < files.length; i++) {
            //create the simple shape cells for both eyes, each file is set to each simple shape cell
            SSC[i][0] = new SimpleShapeCells(files[i], folder);
            SSC[i][1] = new SimpleShapeCells(files[i], folder);
            //add the names to the hashSet, the names in the set are not 
            names.add(SSC[i][0].nameCell);
        }
        //assign memory to the Scale Merge Cells
        SMC=new ScaleMergeCells[names.size()][2];
        int i=0;
        //for each name
        for(String name: names){
            //create the SMC cells
            SMC[i][0]=new ScaleMergeCells();
            SMC[i][1]=new ScaleMergeCells();
            //set the name
            SMC[i][0].setName(name);
            SMC[i][1].setName(name);           
            //create the previous array list
            ArrayList<Cell> previousL=new ArrayList();
            ArrayList<Cell> previousR=new ArrayList();
            
            //adding the repeated names to the previous array list
            //if there are repeated names then, there shape cell tuned for the same shape but different scale
            for(int j=0;j<files.length;j++){
                
                if(SSC[j][0].nameCell.equals(name)){
                    previousL.add(SSC[j][0].cell);
                }
                if(SSC[j][1].nameCell.equals(name)){
                    previousR.add(SSC[j][1].cell);
                }
            }

            //set the previous array of cells to the SMC cells
            SMC[i][0].cell.setPrevious(previousL);
            SMC[i][1].cell.setPrevious(previousR);
           
            //clear the lists
            previousL.clear();
            previousR.clear();
            
            i++;
        }
    }

}
