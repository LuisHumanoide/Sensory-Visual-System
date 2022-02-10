/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Humanoide
 */
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import matrix.matrix;
import middlewareVision.nodes.Visual.V4.V4Memory;

/**
 *
 * @author Humanoide
 */
public class FileUtils {

    /**
     * read the file and return a string
     *
     * @param path
     * @return the content of the file
     */
    public static String readFile(File file) {
        String content = "";
        try (InputStream in = Files.newInputStream(file.toPath());
                BufferedReader reader
                = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Msg.print(line);
                content = content + line + "\n";
            }
            in.close();
        } catch (IOException x) {
            System.err.println(x);
        }
        return content;
    }

    /**
     * Delete a file
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    /**
     * write a file
     *
     * @param name
     * @param cont
     */
    public static void write(String name, String cont, String ext) {
        FileWriter fichero = null;
        BufferedWriter pw = null;
        try {
            fichero = new FileWriter(name + "." + ext);
            pw = new BufferedWriter(fichero);
            pw.write(cont);
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void createDir(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * save all activations needed
     *
     * @param path
     */
    public static void saveActivations(String path) {
        //saveV1activations(path);
        //saveV2activations(path);
        try {
            //saveV4activations(path);
        } catch (Exception ex) {
        }
        saveV1Images(path);
        saveV2images(path);
        try {
            saveV4images(path);
        } catch (Exception ex) {
        }
        //saveContours(path);
        saveContoursImages(path);

    }

    public static void saveV1Images(String path) {
        String newDir = path + "\\\\V1ImageMaps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getV1Map().length; i++) {
            BufferedImage bi = Convertor.Mat2Img(V4Memory.getV1Map()[i]);
            File outputfile = new File(newDir + "\\" + i + ".jpg");
            try {
                ImageIO.write(bi, "jpg", outputfile);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public static void saveV1activations(String path) {
        String newDir = path + "\\\\V1maps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getV1Map().length; i++) {
            matrix saveMat = Convertor.MatToMatrix(V4Memory.getV1Map()[i]);
            WriteObjectToFile(saveMat, newDir + "\\\\" + i);
        }
    }

    public static void saveV2activations(String path) {
        String newDir = path + "\\\\V2maps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getV2Map().length; i++) {
            for (int j = 0; j < V4Memory.getV2Map()[0].length; j++) {
                matrix saveMat = Convertor.MatToMatrix(V4Memory.getV2Map()[i][j]);
                WriteObjectToFile(saveMat, newDir + "\\\\" + i + "_" + j);
            }
        }
    }

    public static void saveV2images(String path) {
        String newDir = path + "\\\\V2ImageMaps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getV2Map().length; i++) {
            for (int j = 0; j < V4Memory.getV2Map()[0].length; j++) {
                BufferedImage bi = Convertor.Mat2Img(V4Memory.getV2Map()[i][j]);
                File outputfile = new File(newDir + "\\" + i + "_" + j + ".jpg");
                try {
                    ImageIO.write(bi, "jpg", outputfile);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public static void saveContours(String path) {
        String newDir = path + "\\\\Contours";
        createDir(newDir);
        matrix saveMat = Convertor.MatToMatrix(V4Memory.getContours1());
        WriteObjectToFile(saveMat, newDir + "\\\\" + 1);
        matrix saveMat2 = Convertor.MatToMatrix(V4Memory.getContours2());
        WriteObjectToFile(saveMat2, newDir + "\\\\" + 2);
    }

    public static void saveContoursImages(String path) {
        String newDir = path + "\\\\ContoursImages";
        createDir(newDir);
        BufferedImage bi1 = Convertor.Mat2Img2(V4Memory.getContours1());
        File outputfile1 = new File(newDir + "\\" + "1.jpg");
        BufferedImage bi2 = Convertor.Mat2Img2(V4Memory.getContours2());
        File outputfile2 = new File(newDir + "\\" + "2.jpg");

        try {
            ImageIO.write(bi1, "jpg", outputfile1);
            ImageIO.write(bi2, "jpg", outputfile2);
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    public static void WriteObjectToFile(Object serObj, String filepath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath + ".amap");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveV4activations(String path) {
        String newDir = path + "\\\\V4maps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getActivationArray().length; i++) {
            matrix saveMat = Convertor.MatToMatrix(V4Memory.getActivationArray()[i]);
            WriteObjectToFile(saveMat, newDir + "\\\\" + i);
        }
    }

    public static void saveV4images(String path) {
        String newDir = path + "\\\\V4ImageMaps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getActivationArray().length; i++) {
            BufferedImage bi = Convertor.Mat2Img(V4Memory.getActivationArray()[i]);
            File outputfile = new File(newDir + "\\" + i + ".jpg");
            try {
                ImageIO.write(bi, "jpg", outputfile);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

    }

    /**
     * get the list of files from a folder
     *
     * @param dir_path the path of the files
     * @return Array of strings of files
     */
    public static String[] getFiles(String dir_path) {

        String[] arr_res = null;

        File f = new File(dir_path);

        if (f.isDirectory()) {

            List<String> res = new ArrayList<>();
            File[] arr_content = f.listFiles();

            int size = arr_content.length;

            for (int i = 0; i < size; i++) {

                if (arr_content[i].isFile()) {
                    res.add(arr_content[i].toString());
                }
            }

            arr_res = res.toArray(new String[0]);

        } else {
            System.err.println("¡ Path NO válido !");
        }

        return arr_res;
    }

    /**
     * Read file object
     *
     * @param filepath
     * @return
     */
    public static Object ReadObjectFromFile(String filepath) {

        try {

            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();

            objectIn.close();
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
