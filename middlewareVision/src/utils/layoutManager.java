/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Point;
import java.util.HashMap;


/**
 * Class for layout the windows in the middleware
 * @author HumanoideFilms
 */
public class layoutManager {
    
    public static HashMap<Integer,Point> points=new HashMap();
    
    /**
     * set manually the locations of the windows
     */
    public static void initLayout(){
        int h=4;
        int v=10;
        for(int i=0;i<v;i++){
            for(int j=0;j<h;j++){
                points.put(i*h+j, getPoint(i,j));
            }
        }
    }
            
    static Point getPoint(int x, int y){
        return new Point(Config.width*x,Config.heigth*y);
    }
    
}
