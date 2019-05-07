/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.core.nodes;

import kmiddle2.nodes.activities.Activity;
import utils.Config;

/**
 *
 * @author Luis Humanoide
 */
public abstract class FrameActivity extends Activity{
    
    public Frame[] frame;
    
    /**
     * initialize the array of frames
     * @param numFrames number of frames
     * @param index index for the layout manager
     */
    public void initFrames(int numFrames, int index){
        frame = new Frame[numFrames];
         for (int i = 0; i < numFrames; i++) {
            frame[i] = new Frame(i+index);
            frame[i].setSize(Config.width, Config.heigth);
        }        
    }
    
}
