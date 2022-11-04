package middlewareVision.nodes.Visual.LGN;



import com.sun.media.jfxmedia.logging.Logger;
import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;

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
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.LGNSimpleOpponentCells,data);
	
//@SendProcess
    }
    
}
