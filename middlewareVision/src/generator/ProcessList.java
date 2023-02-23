/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.io.File;
import java.util.HashMap;
import utils.FileUtils;

/**
 *
 * @author HumanoideFilms
 */
public class ProcessList {

    public static HashMap<String, Object> ProcessMap;

    public static void addProcess(String name, boolean bool) {
        if (!ProcessMap.containsKey(name)) {
            ProcessMap.put(name, bool);
        }
    }

    public static void saveProcessList() {
        String ac = "";
        for (String key : ProcessMap.keySet()) {
            ac = ac + key + " " + ProcessMap.get(key) + "\n";
        }
        FileUtils.write("ConfigFiles/smallNodeList", ac, "txt");
    }
    
    public static void setValue(String key, boolean bool){
        ProcessMap.put(key, bool);
    }

    public static void openList() {
        ProcessMap=new HashMap();
        String sfile = FileUtils.readFile(new File("ConfigFiles/smallNodeList.txt"));
        if (sfile.length() > 1) {
            String fileLines[] = sfile.split("\n");
            for (String line : fileLines) {
                String values[] = line.split(" ");
                addProcess(values[0], Boolean.parseBoolean(values[1]));
            }
        }
    }

}
