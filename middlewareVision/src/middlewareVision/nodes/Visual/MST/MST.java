package middlewareVision.nodes.Visual.MST;

import cFramework.nodes.area.Area;
import middlewareVision.config.AreaNames;

/**
 *
 *
 */
public class MST extends Area {

    public MST() {
        this.ID = AreaNames.MST;
        this.namer = AreaNames.class;
        addProcess(MSTPolarCells.class);

        addProcess(MSTTemplateCells.class);

        //@AddProcess
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(long nodeID, byte[] data) {
        send(AreaNames.MSTPolarCells, data);

        send(AreaNames.MSTTemplateCells, data);

        //@SendProcess
    }

}
