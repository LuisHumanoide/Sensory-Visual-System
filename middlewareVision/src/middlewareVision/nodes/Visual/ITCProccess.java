/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import middlewareVision.core.nodes.FrameActivity;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;

/**
 *
 * @author Luis Humanoide
 */
public class ITCProccess extends FrameActivity {

    public ITCProccess() {
        this.ID = AreaNames.ITCProccess;
        this.namer = AreaNames.class;
        initFrames(1, 11);
    }

    @Override
    public void init() {
        //SimpleLogger.log(this, "SMALL NODE CortexV1");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        LongSpike spike;
        try {
            spike = new LongSpike(data);
            if (spike.getModality() == Modalities.VISUAL) {
                /**
                 * se convierte la intensidad del spike a la matriz de color que representa los contornos
                 * el 16 es el tipo de matriz
                 */
                Mat receive = Convertor.bytesToMat((byte[]) spike.getIntensity(), new Size(Config.width, Config.heigth), 16);
                BufferedImage img = Convertor.ConvertMat2Image2(receive);
                frame[0].setImage(img, "contornos de v4 1");
            }
        } catch (Exception ex) {
            Logger.getLogger(ITCProccess.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
