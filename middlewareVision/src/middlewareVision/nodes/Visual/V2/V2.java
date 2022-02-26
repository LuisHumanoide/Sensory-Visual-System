/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.V2;

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;

/**
 *
 * @author HumanoideFilms
 */
public class V2 extends Area{
    
    public V2() {
        this.ID = AreaNames.V2;
        this.namer = AreaNames.class;
        addProcess(V2AngularCells.class);
        addProcess(V2IlusoryCells.class);
	
	//addProcess(V2CurvatureCells.class);
	
	//@AddProcess
    }

    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        //SimpleLogger.log(this,"BIG NODE V2");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        //send(AreaNames.V2AngularCells, data);
        //send(AreaNames.V2IlusoryCells, data);
	
	send(AreaNames.V2CurvatureCells,data);
	
	//@SendProcess
    }
    
    
}
