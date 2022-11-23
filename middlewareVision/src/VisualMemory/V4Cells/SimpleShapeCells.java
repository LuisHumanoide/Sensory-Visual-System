/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V4Cells;

import VisualMemory.Cell;
import java.util.ArrayList;
import java.util.HashMap;
import org.opencv.core.Mat;
import utils.FileUtils;
import utils.MatrixUtils;

/**
 *
 * @author luish
 */
public class SimpleShapeCells {

    public String nameCell;
    public String path;
    public double scale;
    public Cell cell;
    public GaussianFilter[] filters;
    public HashMap<String, Mat> filterMap;

    /**
     * Simple Shape Cells constructor, it receives the file name and the folder
     * @param file file name
     * @param folder folder name
     */
    public SimpleShapeCells(String file, String folder) {
        path = file;
        //name is a string with the full name
        String name=path.replaceAll(".txt", "").replaceAll(folder+"\\\\","");
        //separate the name and the scale, the scale number is included in the name file
        String nameScale[]=name.split("_");
        nameCell = nameScale[0];
        //if the array nameScale is >1, there is a file with tuned for the same shape but different scale
        if(nameScale.length>1){
            scale=Double.parseDouble(nameScale[1]);
        }
        else{
            //if the array nameScale is 1, there is no scale number in the file name, so the scale is 1
            scale=1;
        }
        //Obtain the file lines from the file
        String fileLines[] = FileUtils.fileLines(file);
        //assign memory to the filters
        filters = new GaussianFilter[fileLines.length];
        //create the hashmap of filters
        filterMap = new HashMap();
        cell=new Cell();
        //for each line of the file, a filter will be created
        for (int i = 0; i < fileLines.length; i++) {
            filters[i] = new GaussianFilter(fileLines[i]);
        }
        makeMatFilters();
    }

    /**
     * It creates a OpenCV Matrix from the list of Gaussian Filters
     */
    void makeMatFilters() {
        ArrayList<GaussianFilter> filterList = new ArrayList();
        //for each Gaussian Filter
        for (GaussianFilter filter : filters) {
            //assign the key to the filter
            String name = filter.getComb();
            //if there are Gaussian filters with the same combination/key, a composed Gaussian filter will be created
            for (int i = 0; i < filters.length; i++) {
                if (filters[i].getComb().equals(name)) {
                    filterList.add(filters[i]);
                }
            }
            //call a method for created the composed Gaussian filter
            addFilterToMap(filterList);
            
            filterList.clear();
        }
    }

    /**
     * Method to add the filters to a Map, also it creates a combined filter if the list size is > 1
     * @param filter list of Gaussian Filters
     */
    void addFilterToMap(ArrayList<GaussianFilter> filter) {
        String name = filter.get(0).getComb();
        //if the filter was not previously added
        if (!filterMap.containsKey(name)) {
            //matrix array of the filters
            Mat[] matArray = new Mat[filter.size()];

            for (int i = 0; i < filter.size(); i++) {
                //create a matrix from the filter parameters
                matArray[i] = filter.get(i).makeFilter2();
            }
            //if the size of the list is >1, a combined filter is created, if is 1, the combined filter is the same of the original
            Mat combinedFilter = MatrixUtils.sum(matArray, 1, 0);
            //adding the filter to the map, with a key that correspond to the -comb- string, this key is useful for perform a filter with a matrix with the same key
            filterMap.put(name, combinedFilter);
        }
    }

}
