/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.Retina;

import cFramework.nodes.area.Area;
import middlewareVision.config.AreaNames;

/**
 *
 * @author Luis Humanoide
 */
public class Retina extends Area {
    
    public Retina() {
        this.ID = AreaNames.Retina;
        this.namer = AreaNames.class;
        addProcess(RetinaProccess.class);
        addProcess(BasicMotion.class);
        //@AddProcess
    }

    @Override
    public void init() {
        //SimpleLogger.log(this,"BIG NODE RETINA");
 
    }

    @Override
    public void receive(long nodeID, byte[] data) {
        send(AreaNames.RetinaProccess, data);
        send(AreaNames.BasicMotion, data);
        //@SendProcess
    }
}
