/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.ExampleNodes;

import middlewareVision.core.nodes.GUIActivity;
import kmiddle2.nodes.activities.Activity;
import middlewareVision.config.AreaNames;
import utils.SimpleLogger;

/**
 *
 * @author Luis Martin
 */
public class AmyProcess1 extends GUIActivity {
    
    private DemoGuiActivity frame;

    public AmyProcess1() {
        this.ID = AreaNames.AmyProcess1;
        this.namer = AreaNames.class;
    }
    
    public void sendHi(String data){
        send(AreaNames.V1, data.getBytes());
    }

    @Override
    public void init() {
        SimpleLogger.log(this,"SMALL NODE AMY");
        frame = new DemoGuiActivity(this);
        setFrame(frame);
        startFrame();
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        SimpleLogger.log(this,"Amy SMALL NODE:"+new String(data));
    }

}
