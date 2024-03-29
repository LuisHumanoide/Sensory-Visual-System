/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.V2;

import cFramework.nodes.area.Area;
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
	addProcess(V2CurvatureCells.class);	
	addProcess(V2CornerMotion.class);	
	//@AddProcess
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(long nodeID, byte[] data) {
        send(AreaNames.V2AngularCells, data);
	send(AreaNames.V2CurvatureCells,data);	
	send(AreaNames.V2CornerMotion,data);	
	//@SendProcess
    }
    
    
}
