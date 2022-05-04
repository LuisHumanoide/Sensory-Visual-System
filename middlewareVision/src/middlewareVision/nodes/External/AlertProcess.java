package middlewareVision.nodes.External;

import generator.ProcessList;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import utils.Config;
import utils.LongSpike;

/**
 *
 *
 */
public class AlertProcess extends Activity {

    AlertFrame frame;
    
    public AlertProcess() {
        this.ID = AreaNames.AlertProcess;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            frame=new AlertFrame(this);
            frame.setVisible(true);
        }
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);

            } catch (Exception ex) {
                Logger.getLogger(AlertProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendAlertState(int frameRate) {
        send(AreaNames.RetinaProccess, null);
        Config.rate=frameRate;
    }

}
