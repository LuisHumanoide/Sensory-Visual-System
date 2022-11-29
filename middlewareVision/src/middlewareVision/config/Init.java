package middlewareVision.config;

import VisualMemory.InitCellMemory;
import generator.ProcessList;
import gui.Visualizer;
import kmiddle2.nodes.service.Igniter;
import middlewareVision.nodes.Visual.V1.V1;
import middlewareVision.nodes.Visual.Retina.Retina;
import middlewareVision.nodes.Visual.V2.V2;
import middlewareVision.nodes.Visual.V4.V4;
import org.opencv.core.Core;
import middlewareVision.nodes.Visual.LGN.LGN;
import utils.SpecialKernels;
import middlewareVision.nodes.External.TestAttention;
import middlewareVision.nodes.Visual.MT.MT;
import middlewareVision.nodes.Visual.MST.MST;
import middlewareVision.nodes.Visual.MST.MSTPolarCells;
import middlewareVision.nodes.External.Alert;
import middlewareVision.nodes.Visual.V3.V3;
import utils.Config;
//@import


/*
https://www.enmimaquinafunciona.com/pregunta/90466/como-clonar-git-repositorio-solo-algunos-directorios
 */
public class Init extends Igniter {

    private boolean DEBUG = false;
    private byte ENTITY_ID = 33;

    public Init() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String[] areaNames = {
            Retina.class.getName(),
            LGN.class.getName(),
            V1.class.getName(),
            V2.class.getName(),
            V4.class.getName(),
            TestAttention.class.getName(),
            MT.class.getName(),
            MST.class.getName(),
            Alert.class.getName(),
            V3.class.getName(), //@addNodes
        };

        configuration.setLocal(true);
        configuration.setDebug(false);
        configuration.setUDP();
        configuration.setEntityID(ENTITY_ID);
        
        Init.restart();
        setAreas(areaNames);
        run();
        ProcessList.saveProcessList();

    }
    
    public static void restart() {
        XMLReader.readXML();
        ProcessList.openList();
        InitCellMemory.initCellMemory();
        Visualizer.initVisualizer(Config.h * Config.v);
        Visualizer.addLimit("zero", 0);
        MSTPolarCells.getVrow();
        SpecialKernels.loadKernels();
    }

    public static void main(String[] args) {
        new Init();
    }
}
