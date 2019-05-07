/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual;

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;

/**
 *
 * @author Luis Humanoide
 */
public class ITC extends Area{
    
      public ITC() {
        this.ID = AreaNames.ITC;
        this.namer = AreaNames.class;
        addProcess(ITCProccess.class);
    }

    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        //SimpleLogger.log(this,"BIG NODE V1");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.ITCProccess, data);
    }
    
}
