/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.V4;

import MiniPrograms.RF;
import java.io.File;
import java.util.ArrayList;
import org.opencv.core.Mat;
import utils.FileUtils;
import utils.SpecialKernels;

/**
 *
 * @author HumanoideFilms
 */
public class V4CellStructure {

    static ArrayList<StringList> fileList;
    static ArrayList<RFBank> V4Bank;
    static ArrayList<RF> RFs;
    static String tfolder;
    
    public static void loadV4Structure(){
        loadKernels("RFV4");
    }

    /**
     * Load the kernel files, making a string structure that groups the files of the same RF with different scale
     * @param folder 
     */
    public static void loadKernels(String folder) {
        RFs = new ArrayList();
        tfolder = folder;
        fileList = new ArrayList();
        V4Bank = new ArrayList();
        String[] files = FileUtils.getFiles(folder);
        for (String filename : files) {
            if (!sameRFBank(filename)) {
                fileList.add(new StringList(filename));
            }
        }
        for (String filename : files) {
            if (sameRFBank(filename)) {
                for (int i = 0; i < fileList.size(); i++) {
                    if (fileList.get(i).getStringList().get(0).replace(".txt", "").equals(filename.split("_")[0])) {
                        fileList.get(i).addToList(filename);
                        break;
                    }
                }

            }
        }
        V4Memory.activationArray=new Mat[files.length];
        loadRFs();
        
    }

    /**
     * Generate the Receptive Field Object and make the kernels
     */
    public static void loadRFs() {
        for (StringList stlist : fileList) {
            RFBank rbank = new RFBank();
            for (String file : stlist.stringList) {
                loadList(file);
                RFlist rflist = new RFlist();
                for (RF rf : RFs) {
                    Mat mat = new Mat();
                    mat = SpecialKernels.getFilterFromRF(rf);
                    int comb=Integer.parseInt(rf.getCombination());
                    rflist.addFilter(new indexMat(mat, comb/10, comb%10));

                }
                rflist.name=file;
                rbank.RFCellBank.add(rflist);
                
            }
            rbank.name="bank "+stlist.getStringList().get(0);
            V4Bank.add(rbank);
        }
        //printStructure();
    }
    
    /**
     * Print the structure of the RF Bank
     */
    static void printStructure(){
        for(RFBank bank: V4Bank){
            System.out.println(bank.name+"             :");
            for(RFlist list: bank.RFCellBank){
                System.out.println("               list"+list.name+"   "+list.RFs.size());
            }
        }
    }

    /**
     * Load the RF list of a single RF file
     * @param path 
     */
    static void loadList(String path) {
        clearList();
        String stList = FileUtils.readFile(new File(path));
        //System.out.println(stList);
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
        }
    }

    static void clearList() {
        RFs.clear();
    }

    public static void printList() {
        for (StringList stlist : fileList) {
            System.out.println(stlist.stringList.toString());
        }
    }

    /**
     * determines if the file belong to a parent receptive field file, its mean, different scales
     * @param file
     * @return 
     */
    static boolean sameRFBank(String file) {
        return file.split("_").length > 1;
    }

    static ArrayList<RFlist> RFCellBank;

}

/**
 * *****************************************************************************
 * 
 * @author HumanoideFilms
 */
class StringList {

    ArrayList<String> stringList;
    String name="";

    public StringList(ArrayList<String> stringList) {
        this.stringList = stringList;
    }

    public StringList(String string) {
        stringList = new ArrayList();
        stringList.add(string);
    }

    public ArrayList<String> getStringList() {
        return stringList;
    }

    public void addToList(String name) {
        stringList.add(name);
    }

    public void setStringList(ArrayList<String> stringList) {
        this.stringList = stringList;
    }

}

/**
 * *****************************************************************************
 * RFLIST
 *
 * @author HumanoideFilms
 */
class RFlist {
    
    String name;

    ArrayList<indexMat> RFs;

    public RFlist() {
        RFs = new ArrayList();
    }

    public RFlist(ArrayList<indexMat> RFs) {
        this.RFs = RFs;
    }

    public ArrayList<indexMat> getRFs() {
        return RFs;
    }

    public void setRFs(ArrayList<indexMat> RFs) {
        this.RFs = RFs;
    }

    public void addFilter(indexMat mat) {
        RFs.add(mat);
    }

    public Mat getMat(int index) {
        return RFs.get(index).getMat();
    }

}

/**
 * *****************************************************************************
 * RFBANK
 *
 * @author HumanoideFilms
 */
class RFBank {

    String name;
    /**
     * Same shape but different sizes
     */
    ArrayList<RFlist> RFCellBank;
    
    public RFBank(){
        RFCellBank=new ArrayList();
    }

    public ArrayList<RFlist> getRFCellBank() {
        return RFCellBank;
    }

    public void setRFCellBank(ArrayList<RFlist> RFCellBank) {
        this.RFCellBank = RFCellBank;
    }

    public Mat getMat(int i1, int i2) {
        return RFCellBank.get(i1).getMat(i2);
    }

    public RFlist getRFlist(int index) {
        return RFCellBank.get(index);
    }

}

/**
 * *****************************************************************************
 * INDEXMAT
 *
 * @author HumanoideFilms
 */
class indexMat {

    Mat mat;
    int index[];


    public indexMat(Mat mat, int ... index) {
        this.mat = mat;
        this.index = index;
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }



}
