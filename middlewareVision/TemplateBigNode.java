@package

import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;


/**
 *
 * 
 */
public class @Name extends Area{
    public @Name() {
        this.ID = AreaNames.@Name;
        this.namer = AreaNames.class;
        @AddProcess
    }

    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        @SendProcess
    }
    
}