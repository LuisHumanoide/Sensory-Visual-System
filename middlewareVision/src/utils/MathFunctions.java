/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author HumanoideFilms
 */
public class MathFunctions {
    
    public static Coord[] discreteGauss(double a, double b, double mul, int x1, int x2){
        int dom=Math.abs(x2-x1);
        int inf=0;
        int sup=0;
        Coord points[]=new Coord[dom];
        if(x2>=x1){
            sup=x2;
            inf=x1;
        }
        else{
            sup=x1;
            inf=x2;
        }
        for(int x=inf;x<=sup;x++){
            double fx=(double)(mul/(a*Math.sqrt(2*Math.PI)))*(double)(Math.exp((-0.5)*(Math.pow((double)((x-b)/a), 2))));
        }
        return points;
    }
    
}

class Coord{
    double x;
    double y;

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
}
