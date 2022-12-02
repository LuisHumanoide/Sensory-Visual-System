package middlewareVision.nodes.External;


import cFramework.nodes.area.Area;
import middlewareVision.config.AreaNames;

/**
 *
 *
 */
public class Alert extends Area {

    public Alert() {
        this.ID = AreaNames.Alert;
        this.namer = AreaNames.class;
        addProcess(AlertProcess.class);
        //@AddProcess
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(long nodeID, byte[] data) {
        send(AreaNames.AlertProcess, data);
        //@SendProcess
    }

}
