package middlewareVision.nodes.Visual.MT;

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;

/**
 *
 *
 */
public class MT extends Area {

    public MT() {
        this.ID = AreaNames.MT;
        this.namer = AreaNames.class;
        addProcess(MTComponentCells.class);
        addProcess(MTPatternCells.class);
        //@AddProcess
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.MTComponentCells, data);
        send(AreaNames.MTPatternCells, data);
        //@SendProcess
    }

}
