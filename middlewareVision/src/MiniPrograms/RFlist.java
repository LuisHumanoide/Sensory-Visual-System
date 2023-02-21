/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniPrograms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import utils.FileUtils;

/**
 *
 * @author HumanoideFilms
 */
public class RFlist {

    public static ArrayList<RF> RFs;
    public static ArrayList<String> combinations;
    public static ArrayList<String> folders;
    public static double scale = 4;
    public static String folder;

    public RFlist() {

    }

    static String[] initFolderList() {
        String folders[] = FileUtils.readFile(new File("folderRFS.txt")).split("\\n");
        return folders;
    }

    static void initList() {
        RFs = new ArrayList<RF>();
        combinations = new ArrayList<String>();
    }

    static void addElement(double rx, double ry, int px, int py, double intensity, double angle, String combination, int size) {
        RF rf = new RF(rx, ry, px, py, intensity, angle, combination, size);
        addCombination(combination);
        RFs.add(rf);
    }

    static void addCombination(String combination) {
        if (!combinations.contains(combination)) {
            combinations.add(combination);
        }
    }

    static void addElement(Object rx, Object ry, Object px, Object py, Object intensity, Object angle, Object combination, Object size) {
        addElement(Double.parseDouble("" + rx), Double.parseDouble("" + ry), Integer.parseInt("" + px), Integer.parseInt("" + py),
                Double.parseDouble("" + intensity), Double.parseDouble("" + angle), "" + combination, Integer.parseInt("" + size));
    }

    /*
    static void addElementRF2(double rx, double ry, int px, int py, double intensity, double angle, int combination, int size, double scale) {
        RF rf = new RF(rx * scale, ry * scale, (int) (px * scale), (int) (py * scale), intensity, angle, combination, (int) (size * scale));
        RFs2.add(rf);
    }*/
    static void clearList() {
        RFs.clear();
        combinations.clear();
    }

    static void saveList(String name) {
        String sList = "";
        for (RF rf : RFs) {
            sList = sList + rf.getValues() + "\n";
        }
        FileUtils.write(RFlist.folder + "/" + name, sList, "txt");
    }

    static void saveListScaled(String name, double scales) {
        String sList = "";
        String stScale = "" + scales;
        stScale.replaceAll(".", "_");
        for (RF rf : RFs) {
            sList = sList + rf.getScaledValues(scales) + "\n";
        }
        FileUtils.write(RFlist.folder + "/" + name + "_" + stScale, sList, "txt");
    }

    static void loadList(String path) {
        clearList();
        String stList = FileUtils.readFile(new File(path));
        String lines[] = stList.split("\\n");
        for (String st : lines) {
            String values[] = st.split(" ");
            RF rf = new RF(Double.parseDouble(values[0]),
                    Double.parseDouble(values[1]),
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]),
                    Double.parseDouble(values[4]),
                    Double.parseDouble(values[5]),
                    values[6],
                    Integer.parseInt(values[7]));
            RFs.add(rf);
            addCombination(values[6]);
        }
    }

    public void addElements(Object... objects) {
        RFlist.addElement(Double.parseDouble((String) objects[0]), Double.parseDouble((String) objects[1]), Integer.parseInt((String) objects[2]), Integer.parseInt((String) objects[3]),
                Double.parseDouble((String) objects[4]), Double.parseDouble((String) objects[5]), (String) objects[6], Integer.parseInt((String) objects[7]));
    }

}
