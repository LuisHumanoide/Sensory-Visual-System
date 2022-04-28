package middlewareVision.nodes.External;



import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;


/**
 *
 * 
 */
public class TestAttention extends Area{
    public TestAttention() {
        this.ID = AreaNames.TestAttention;
        this.namer = AreaNames.class;
        addProcess(FeedbackProccess.class);
	
//@AddProcess
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.FeedbackProccess,data);
	
//@SendProcess
    }
    
}
