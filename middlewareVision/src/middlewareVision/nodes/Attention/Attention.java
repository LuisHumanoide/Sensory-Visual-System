package middlewareVision.nodes.Attention;



import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;
import utils.SimpleLogger;

/**
 *
 * 
 */
public class Attention extends Area{
    public Attention() {
        this.ID = AreaNames.Attention;
        this.namer = AreaNames.class;
        addProcess(SaliencyProcess.class);
	
//@AddProcess
    }

    @Override
    public void init() {
        SimpleLogger.log(this,"BIG NODE Attention");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.SaliencyProcess,data);	
//@SendProcess
    }
    
}
