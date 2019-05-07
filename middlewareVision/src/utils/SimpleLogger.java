/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Luis Martin
 */
public class SimpleLogger {

    private static boolean debug = false;

    public static void setDebug(boolean debug) {
        //SimpleLogger.debug = debug;
    }

    public static void log(Object source, String text) {
        if (debug) {
            //String sourceName = source.getClass().getName();
            //System.out.println(sourceName + ": " + text);
        }
    }

    public static void log(String text) {
        if (debug) {
            //System.out.println(text);
        }
    }

}
