/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.ExampleNodes;

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;
import utils.SimpleLogger;

/**
 *
 * @author Luis Martin
 */
public class Amy extends Area {

    public Amy() {
        this.ID = AreaNames.Amy;
        this.namer = AreaNames.class;
        addProcess(AmyProcess1.class);
    }

    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        SimpleLogger.log(this,"BIG NODE AMY");
 
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        //send(AreaNames.AMY_GENHNEI, nodeID, data);

        send(AreaNames.AmyProcess1, data);
    }
}
