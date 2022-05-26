/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.graph;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import utils.FileUtils;

/**
 * Graph generator for middleware
 *
 * @author HumanoideFilms
 */
public class MGraph {
    

    static ArrayList<GArea> areas=new ArrayList();;
    static ArrayList<GSmallNode> nodes=new ArrayList();;
    static HashSet<String> allSmallNodes=new HashSet();;
    static String path = "src/middlewareVision/nodes";
    
    public static void generateGraphs(){
        areas.clear();
        nodes.clear();
        allSmallNodes.clear();
        walkin(new File(path));
        generateNodeGraph();
        generateProcessGraph();
    }

    public static void walkin(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    walkin(listFile[i]);
                } else {
                    readFile(listFile[i].getPath());
                }
            }
        }
    }

    public static void readFile(String path) {
        String file = FileUtils.readFile(new File(path));
        analyzeFile(file);
    }

    static void analyzeFile(String file) {
        if (file.replaceAll(" ", "").contains("extendsArea")) {
            String[] ar = file.split("\n");
            for (String cad : ar) {
                if (cad.contains("public class")) {
                    String areaName = cad.replaceAll(" ", "").replace("publicclass", "").replace("extendsArea{", "");
                    areas.add(new GArea(areaName, addSmallNodes(file)));
                }
            }
        }
        if (file.replaceAll(" ", "").contains("extendsActivity")) {
            String[] ar = file.split("\n");
            for (String cad : ar) {
                if (cad.contains("public class")) {
                    String nodeName = cad.replaceAll(" ", "").replace("publicclass", "").replace("extendsActivity{", "");
                    nodes.add(new GSmallNode(nodeName, addNext(file)));
                }
            }
        }
    }

    static ArrayList<String> addSmallNodes(String file) {
        ArrayList<String> nodes = new ArrayList();
        String lines[] = file.split("\n");
        for (String line : lines) {
            if (line.contains("addProcess(")) {
                String nodeName = line.replaceAll(" ", "").replaceAll("\t", "").replace("addProcess(", "").replace(".class);", "");
                if (!nodeName.contains("//")) {
                    nodes.add(nodeName);
                    allSmallNodes.add(nodeName);
                } else {
                    //nodes.add(nodeName.replaceAll("//", ""));
                }
            }
        }

        return nodes;
    }

    static ArrayList<String> addNext(String file) {
        ArrayList<String> nodes = new ArrayList();
        String lines[] = file.split("\n");
        for (String line : lines) {
            if (line.contains("send(AreaNames.")) {
                String nodeName = line.replaceAll(" ", "").replaceAll("\t", "").replace("send(AreaNames.", "");
                nodeName = nodeName.substring(0, nodeName.indexOf(",")).replaceAll(" ", "");
                if (!nodeName.contains("//")) {
                    nodes.add(nodeName);
                }
            }
        }

        return nodes;
    }

    static void generateNodeGraph() {
        String c = "graph G{\n";
        String l1 = "[ label=\"@name\" shape=\"circle\" ]";
        String l2 = "[ label=\"@name\" shape=\"octagon\" ]";
        for (GArea ga : areas) {
            c = c + ga.name + " " + l1.replace("@name", ga.name) + "\n";
            for (String nodes : ga.smallNodes) {
                c = c + nodes + " " + l2.replace("@name", nodes) + "\n";
            }
        }
        c = c + "\n\n";
        for (GArea ga : areas) {
            for (String nodes : ga.smallNodes) {
                c = c + ga.name + " -- " + nodes + ";\n";
            }
            c = c + "\n";
        }
        c = c + "}";
        
        FileUtils.write("nodeDiagram", c, "txt");
        generateImg("nodeDiagram","png","circo");
        
    }
    
    

    
    static void generateProcessGraph() {
        String c = "digraph G{\n";
        c = c + "rankdir=\"LR\"" + "\nnewrank=\"true\" \n";
        for (GSmallNode n : nodes) {
            if (allSmallNodes.contains(n.name)) {
                c = c + n.name + " [ shape=\"rectangle\" ] \n";
            }
        }
        c = c + "\n\n";
        for (GSmallNode n : nodes) {
            if (allSmallNodes.contains(n.name)) {
                for (String next : n.next) {
                    c = c + n.name + " -> " + next + " \n";
                }
            }
        }

        for (GArea ga : areas) {
            c = c + "\nsubgraph cluster" + ga.name + " {\n label=\"" + ga.name + "\"\nrank=\"same\"\n";
            for (String nodes : ga.smallNodes) {
                c = c + nodes + "\n";
            }
            c = c + "}\n";
        }

        c = c + "\n}";

        FileUtils.write("proccessDiagram", c, "txt");
        generateImg("proccessDiagram","png","dot");

    }
    static Runtime run = Runtime.getRuntime();
    static void generateImg(String fileName, String format, String engine) {
        try {
            String cmd = "bin\\"+engine+".exe" + " -T " + format + " " +  fileName + ".txt "
                    + "-o " + fileName + "." + format;
            run.exec(cmd);
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }

}
