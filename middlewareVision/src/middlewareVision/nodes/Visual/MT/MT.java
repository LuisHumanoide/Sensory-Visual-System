package middlewareVision.nodes.Visual.MT;

import cFramework.nodes.area.Area;
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
    public void receive(long nodeID, byte[] data) {
        send(AreaNames.MTComponentCells, data);
        send(AreaNames.MTPatternCells, data);
        //@SendProcess
    }

}
