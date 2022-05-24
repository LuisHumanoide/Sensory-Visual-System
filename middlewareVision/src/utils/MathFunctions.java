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
    
    public static double discreteGauss(double a, double b, double mul, double x){
        return (double)(mul/(a*Math.sqrt(2*Math.PI)))*(double)(Math.exp((-0.5)*(Math.pow((double)((x-b)/a), 2))));
    }
    
}

