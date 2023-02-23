@package

import spike.Location;
import generator.ProcessList;
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
public class @Process extends Activity {


    public @Process() {
        this.ID = AreaNames.@Process;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }


    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);

            } catch (Exception ex) {
                Logger.getLogger(@Process.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  


}