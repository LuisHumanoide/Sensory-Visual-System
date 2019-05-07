package middlewareVision.nodes.Attention;



import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import middlewareVision.core.nodes.ClickFrame;
import middlewareVision.core.nodes.ClickFrameActivity;
import spike.Modalities;
import utils.LongSpike;
import utils.SimpleLogger;

/**
 *
 * 
 */
public class SaliencyProcess extends ClickFrameActivity{
    

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */

     /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */


    public SaliencyProcess() {
        this.ID = AreaNames.Saliency;
        this.namer = AreaNames.class;
        frame=new ClickFrame(this,"saliency/",20);
    }


    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE Saliency");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

        } catch (Exception ex) {
            Logger.getLogger(SaliencyProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    @Override
    public void clickAction(){
        
    }



     /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
     

}
