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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import matrix.matrix;
import org.opencv.core.Mat;

/**
 *
 * @author Humanoide
 */
public class FileUtils {

    /**
     * Read a file and return a string with the file content
     *
     * @param file path of the file + file + extension
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

    public static void saveImage(Mat src, String path, String name) {
        createDir(path);
        File f = new File(path + "\\" + name + ".jpg");
        try {
            ImageIO.write(Convertor.Mat2Img(src), "JPEG", f);
        } catch (IOException ex) {
            utils.Msg.print(ex);
        }
    }

    public static void saveImage2(Mat src, String path, String name) {
        createDir(path);
        File f = new File(path + "\\" + name + ".jpg");
        try {
            ImageIO.write(Convertor.Mat2Img2(src), "JPEG", f);
        } catch (IOException ex) {
            utils.Msg.print(ex);
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

    public static String[] getFiles(String dir_path, String... extension) {

        String[] arr_res = null;

        File f = new File(dir_path);

        if (f.isDirectory()) {

            List<String> res = new ArrayList<>();
            File[] arr_content = f.listFiles();

            int size = arr_content.length;

            for (int i = 0; i < size; i++) {

                if (arr_content[i].isFile()) {
                    for (String ext : extension) {
                        if (arr_content[i].toString().toLowerCase().contains(ext)) {
                            res.add(arr_content[i].toString());
                        }
                    }
                }
            }

            arr_res = res.toArray(new String[0]);

        } else {
            System.err.println("¡ Path NO válido !");
        }

        return arr_res;
    }

    public static String[] getSortedFiles(String dir_path) {
        String ar[] = getFiles(dir_path);
        Arrays.sort(ar);
        return ar;
    }

    public static String[] getSortedFiles(String dir_path, String ... extension) {
        String ar[] = getFiles(dir_path, extension);
        Arrays.sort(ar);
        return ar;
    }

    /**
     * Return the number of lines from a file
     *
     * @param file
     * @return
     */
    public static int numberFileLines(String file) {
        return FileUtils.readFile(new File(file)).split("\\n").length;
    }

    /**
     * Return a string array from a file
     *
     * @param file
     * @return
     */
    public static String[] fileLines(String file) {
        return FileUtils.readFile(new File(file)).split("\\n");
    }

    public static String[] fileLinesEx(String file) throws Exception {
        return FileUtils.readFile(new File(file)).split("\\n");
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
