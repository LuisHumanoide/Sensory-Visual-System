/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;

/**
 *
 * @author Humanoide
 */
public class Config {

    public static final int port = 54321;
    public static final String IP = "127.0.0.1";
    public static final String NAME = "b15";
    public static final String EXT = ".jpg";
    public static final String PATH = "./imagenes/" + NAME + "/";
    public static boolean cam = true;
    public static boolean staticImage = true;
    public static int device = 0;
    public static int tresh=100;
    public static int blur=1;
    public static int gaborOrientations = 4;
    public static int width=300;
    public static int heigth=250;
    
    /**
     * Paths for the image showed in the GUI
     */
    public static String lastPath = FileUtils.readFile(new File("lastPath.du"));
    public static String lastImage = FileUtils.readFile(new File("lastImage.du"));

    public static void refreshPath() {
        lastPath = FileUtils.readFile(new File("lastPath.du"));
        lastImage = FileUtils.readFile(new File("lastImage.du"));
    }
    
    /*----------------MODE----------------------*/
    public static final int CAMERA=1;
    public static final int CLICK=2;
    
    public static final int option=CLICK;

}
