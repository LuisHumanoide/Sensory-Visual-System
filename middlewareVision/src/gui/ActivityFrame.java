/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JFrame;
import kmiddle2.nodes.activities.Activity;
import kmiddle2.util.IDHelper;

/**
 *
 * @author Luis Martin
 */
public class ActivityFrame<T extends Activity> extends JFrame{
    
    protected T activity;
    
    public ActivityFrame(T activity){
        this.activity = activity;
        
        String bigNodeName = IDHelper.getNameAsString(activity.getNamer(), activity.getID());
        String smallNodeName = activity.getClass().getSimpleName();
        
        setTitle(bigNodeName+"-"+smallNodeName);
        
    }
    
}
