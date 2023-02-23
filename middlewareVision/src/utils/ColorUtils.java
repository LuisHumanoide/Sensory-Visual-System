/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;

/**
 *
 * @author HumanoideFilms
 */
public class ColorUtils {
    
    public static Color HSBtoRGB(float hue, float lightness, float saturation){
        return Color.getHSBColor(hue, lightness, saturation);
    }
    
}
