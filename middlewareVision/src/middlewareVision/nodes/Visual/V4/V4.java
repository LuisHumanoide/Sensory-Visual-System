/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.V4;


import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;


/**
 *
 * @author Luis Humanoide
 */
public class V4 extends Area{
    
    public static int value = 0;
        
    public V4() {
        this.ID = AreaNames.V4;
        this.namer = AreaNames.class;
        addProcess(V4Color.class);	
	addProcess(V4SimpleShapeCells.class);	
	addProcess(V4SimpleShapeScaleInv.class);	
	//@AddProcess
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.V4Color,data);	
	send(AreaNames.V4SimpleShapeCells,data);	
	send(AreaNames.V4SimpleShapeScaleInv,data);	
	//@SendProcess
    }
    
}
