/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;


/**
 *
 * @author HumanoideFilms
 */
public class test {


    public static void main(String[] args) {
        int n=10;
        int c=0;
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){  
                c++;
            }
        }
        System.out.println(c+"  "+(n*(n-1)/2));
    }

}


