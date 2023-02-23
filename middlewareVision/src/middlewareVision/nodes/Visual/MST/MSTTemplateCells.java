package middlewareVision.nodes.Visual.MST;



import spike.Location;
import generator.ProcessList;
import cFramework.nodes.process.Process;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.LongSpike;

/**
 *
 * 
 */
public class MSTTemplateCells extends Process {


    public MSTTemplateCells() {
        this.ID = AreaNames.MSTTemplateCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }


    @Override
    public void init() {
        
    }

    @Override
    public void receive(long nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);

            } catch (Exception ex) {
                Logger.getLogger(MSTTemplateCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  


}
