/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual;

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;
import utils.SimpleLogger;

/**
 *
 * @author Luis Humanoide
 */
public class Retina extends Area {
    
    public Retina() {
        this.ID = AreaNames.Retina;
        this.namer = AreaNames.class;
        addProcess(RetinaProccess.class);
    }

    @Override
    public void init() {
        //SimpleLogger.log(this,"BIG NODE RETINA");
 
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.RetinaProccess, data);
    }
}
