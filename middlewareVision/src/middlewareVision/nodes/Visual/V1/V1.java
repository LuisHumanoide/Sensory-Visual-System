/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.V1;

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;

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
	addProcess(V1HyperComplex.class);	
	addProcess(V1MotionCellsNew.class);	
	addProcess(V1BinocularSimpleCells.class);
	addProcess(V1BinocularComplexCells.class);	
	addProcess(V1BinocularMergeProcess.class);	
	//@AddProcess
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
	send(AreaNames.V1SimpleCells,data);
	send(AreaNames.V1ComplexCells,data);
	send(AreaNames.V1DoubleOpponent,data);
	send(AreaNames.V1HyperComplex,data);	
	send(AreaNames.V1SimpleCellsFilter,data);	
	send(AreaNames.V1MotionCellsNew,data);	
	send(AreaNames.V1BinocularSimpleCells,data);
	send(AreaNames.V1BinocularComplexCells,data);	
	send(AreaNames.V1BinocularMergeProcess,data);	
	//@SendProcess
    }
    
}
