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
    
    public static int port = 54321;
    public static String IP = "127.0.0.1";
    public static int device = 0;
    
    public static int LGNmethod=1;

    public static int tresh = 100;
    public static int blur = 1;

    public static int gaborOrientations = 4;
    public static int HCfilters = 1;
    public static int width = 200;
    public static int heigth = 200;
    public static int motionWidth = 100;
    public static int motionHeight = 100;
    
    public static int rate=3;

    public static double bright = 0;
    public static double contr = 1;

    public static final int freqs = 1;
    public static final int freqsV2 = 1;
    public static int nDisparities=1;

    public static int displace = 0;
    public static int gaborBanks = 0;

    public static int h = 40;
    public static int v = 100;

    //V4 Color parameters
    public static float HueShift=0;
    public static int NoConcentricCircles = 0;
    public static int NoRadialDivisions = 0;
    public static int NoHeightDivisions = 0;
    
    //Motion
    public static int V1MotionSubs=1;
    public static int MTMotionSubs=1;
    
    //MST Polar Paremeters
    public static int dxExpCont=1;
    public static int dtExpCont=1;
    public static int dxRotation=1;
    public static int dtRotation=1;
    
    public static int sccn=1;

}
