package middlewareVision.nodes.Visual;



import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;
import utils.SimpleLogger;

/**
 *
 * 
 */
public class LGN extends Area{
    public LGN() {
        this.ID = AreaNames.LGN;
        this.namer = AreaNames.class;
        addProcess(LGNSimpleOpponentCells.class);
	
//@AddProcess
    }

    @Override
    public void init() {
        SimpleLogger.log(this,"BIG NODE LGN");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.LGNProcess,data);
	
//@SendProcess
    }
    
}
