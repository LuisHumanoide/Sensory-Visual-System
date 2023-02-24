/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.graph;

import java.util.ArrayList;

/**
 *
 * @author HumanoideFilms
 */
public class GArea {
    public String name;
    public ArrayList<String> smallNodes;

    public GArea(String name) {
        this.name = name;
        smallNodes = new ArrayList();
    }

    public GArea(String name, ArrayList<String> nodes) {
        this.name = name;
        smallNodes = new ArrayList();
        smallNodes.addAll(nodes);
    }

    public void addSmallNode(String name) {
        smallNodes.add(name);
    }

    public void addSmallNodes(ArrayList<String> nodes) {
        smallNodes.addAll(nodes);
    }

    public void listNodes() {
        for (String node : smallNodes) {
            System.out.println("    node: " + node);
        }
    }
}
