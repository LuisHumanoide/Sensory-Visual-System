/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.core.nodes;

import gui.ActivityFrame;
import kmiddle2.nodes.activities.Activity;

/**
 *
 * @author Luis Martin
 */
public abstract class GUIActivity<T extends ActivityFrame> extends Activity {

    protected T frame;

    public GUIActivity() {

    }

    @Override
    public abstract void receive(int nodeID, byte[] data);

    /**
     * Interfaz grafica
     */
    public void startFrame() {
        if (!frame.isVisible()) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.setVisible(true);
                }
            });
        }
    }

    public void setFrame(T frame) {
        this.frame = frame;
    }

    public T getFrame() {
        return frame;
    }
}
