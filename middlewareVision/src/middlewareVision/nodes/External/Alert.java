package middlewareVision.nodes.External;

import kmiddle2.nodes.areas.Area;
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

addProcess(test2.class);
	
	//@AddProcess
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.AlertProcess, data);

send(AreaNames.test2,data);
	
	//@SendProcess
    }

}
