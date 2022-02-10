/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

/**
 *
 * @author HumanoideFilms
 */
public class MotionCell extends Cell{
    
    Cell delayCell;
    //time displacement
    int dt;
    //distance displacement
    int dx;
    
    double speed;

    public MotionCell() {
        super();
    }
    
    public void addDelayCell(Cell delayCell){
        this.delayCell=delayCell;
    }
    
    public void setDxDt(int dx, int dt){
        this.dx=dx;
        this.dt=dt;
        speed=(double)dx/dt;
    }
    
    
}
