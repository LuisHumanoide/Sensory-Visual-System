/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.graph;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author HumanoideFilms
 */
public class GSmallNode {

    public String name;
    public HashSet<String> next;

    public GSmallNode() {
        next = new HashSet();
    }

    public GSmallNode(String name, ArrayList<String> list) {
        this.name = name;
        next = new HashSet();
        next.addAll(list);
    }

    public void listNextNodes() {
        for (String node : next) {
            System.out.println("    next node: " + node);
        }
    }
}
