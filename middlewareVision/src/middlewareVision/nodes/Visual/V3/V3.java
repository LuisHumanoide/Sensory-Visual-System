package middlewareVision.nodes.Visual.V3;

import cFramework.nodes.area.Area;
import middlewareVision.config.AreaNames;

/**
 *
 *
 */
public class V3 extends Area {

    public V3() {
        this.ID = AreaNames.V3;
        this.namer = AreaNames.class;
        addProcess(V3DisparityRange.class);
        //@AddProcess
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(long nodeID, byte[] data) {
        send(AreaNames.V3DisparityRange, data);
        //@SendProcess
    }

}
