package middlewareVision.nodes.Visual;



import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.LongSpike;
import utils.SimpleLogger;

/**
 *
 * 
 */
public class V1HyperComplex extends Activity {


    public V1HyperComplex() {
        this.ID = AreaNames.V1HyperComplex;
        this.namer = AreaNames.class;
    }


    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1HyperComplex");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

        } catch (Exception ex) {
            Logger.getLogger(V1HyperComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   

}
