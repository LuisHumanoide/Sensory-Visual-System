/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual;

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;
import utils.SimpleLogger;

/**
 *
 * @author Luis Humanoide
 */
public class V4 extends Area{
    
    public static int value = 0;
        
    public V4() {
        this.ID = AreaNames.V4;
        this.namer = AreaNames.class;
        addProcess(V4Contour.class);
        addProcess(V4Color.class);
	
	//@AddProcess
    }

    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        //SimpleLogger.log(this,"BIG NODE V2");
        value++;
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        //SimpleLogger.log(this,"V2 BIG NODE:"+new String(data));
        send(AreaNames.V4Color,data);	
	send(AreaNames.V4Contour,data);
	
	//@SendProcess
    }
    
}
