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
public class V1 extends Area{
    public V1() {
        this.ID = AreaNames.V1;
        this.namer = AreaNames.class;	
	addProcess(V1SimpleCells.class);
	addProcess(V1ComplexCells.class);
	addProcess(V1DoubleOpponent.class);
	//addProcess(V1HyperComplex.class);
	
	//@AddProcess
    }

    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        //SimpleLogger.log(this,"BIG NODE V1");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        //send(AreaNames.V1Proccess, data);
	send(AreaNames.V1SimpleCells,data);
	send(AreaNames.V1ComplexCells,data);
	send(AreaNames.V1DoubleOpponent,data);/*
	send(AreaNames.V1HyperComplex,data);*/
	
	//@SendProcess
    }
    
}
