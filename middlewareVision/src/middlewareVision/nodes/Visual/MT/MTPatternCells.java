package middlewareVision.nodes.Visual.MT;



import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.LongSpike;

/**
 *
 * 
 */
public class MTPatternCells extends Activity {


    public MTPatternCells() {
        this.ID = AreaNames.MTPatternCells;
        this.namer = AreaNames.class;
    }


    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

        } catch (Exception ex) {
            Logger.getLogger(MTPatternCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  


}
