/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author HumanoideFilms
 */
public class Msg {

    static boolean debug = true;

    public static void print(Object o) {
        if (debug) {
            System.out.println(o);
        }
    }
    
    public static void setDebug(boolean debug){
        Msg.debug=debug;
    }
}
