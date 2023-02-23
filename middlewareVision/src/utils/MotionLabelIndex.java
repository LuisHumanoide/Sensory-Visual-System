/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Luis Humanoide
 */
public class MotionLabelIndex {
    
    //Lf,(float)Rg,(float)Up,(float)Dw
            
    public static final int Lf=0;
    public static final int Rg=1;
    public static final int Up=2;
    public static final int Dw=3;
    
    public static int[] labelIndex(int index){
        int [] arrayLabel=null;
        if(index==0){
            arrayLabel=new int []{Rg,Up,Lf,Dw};
        }
        if(index==1){
            arrayLabel=new int []{Lf,Up,Rg,Dw};
        }
        if(index==2){
            arrayLabel=new int []{Up,Rg,Dw,Lf};
        }
        if(index==3){
            arrayLabel=new int []{Rg,Up,Lf,Dw};
        }
        return arrayLabel;
    }
    
}
