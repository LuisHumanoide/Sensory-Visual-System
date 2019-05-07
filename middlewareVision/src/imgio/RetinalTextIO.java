/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 *
 * @author dmadrigal
 */
public class RetinalTextIO {
    
    public static void writeMatrixImage(Mat img,String path){
        try (PrintWriter pw = new PrintWriter(new File(path))) {
            String m = dataStringBuilder(img);
            pw.println(img.rows() + " , " + img.cols()+ ";");
            pw.flush();
            
            pw.println(m);
            pw.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RetinalTextIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     private static String dataStringBuilder(Mat mat) {
        StringBuilder sb = new StringBuilder();
        int rows = mat.rows();
        int cols = mat.cols();
        int type = mat.type();

        switch (type) {
            case CvType.CV_32F:
                float fs[] = new float[1];
                for( int r=0 ; r<rows ; r++ ) {
                    for( int c=0 ; c<cols ; c++ ) {
                        mat.get(r, c, fs);
                        sb.append( String.valueOf(fs[0]));
                        if(c < cols-1)
                            sb.append( ", " );
                    }
                    sb.append( ";\n" );
                }   break;
            case CvType.CV_32S:
                int is[] = new int[1];
                for( int r=0 ; r<rows ; r++ ) {
                    for( int c=0 ; c<cols ; c++ ) {
                        mat.get(r, c, is);
                        sb.append( String.valueOf(is[0]));
                        if(c < cols-1)
                            sb.append( ", " );
                    }
                    sb.append( ";\n" );
                }   break;
            case CvType.CV_16S:
                short ss[] = new short[1];
                for( int r=0 ; r<rows ; r++ ) {
                    for( int c=0 ; c<cols ; c++ ) {
                        mat.get(r, c, ss);
                        sb.append( String.valueOf(ss[0]));
                        if(c < cols-1)
                            sb.append( ", " );
                    }
                    sb.append( ";\n" );
                }   break;
            case CvType.CV_8U:
                byte bs[] = new byte[1];
                for( int r=0 ; r<rows ; r++ ) {
                    for( int c=0 ; c<cols ; c++ ) {
                        mat.get(r, c, bs);
                        sb.append( String.valueOf(bs[0]));
                        if(c < cols-1)
                            sb.append( ", " );
                    }
                    sb.append( ";\n" );
                }   break;
            default:
                sb.append("unknown type\n");
                break;
        }

        return sb.toString();
    }
}
