/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edgedetectiongray;

import java.awt.image.*;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.gui.*;
import com.pearsoneduc.ip.op.OperationException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.File;
import javax.imageio.ImageIO;
/**
 *
 * @author kanvikhanna
 */
public class EdgeDetectionGray extends JFrame {
    
    private BufferedImage image;
    private ViewWithROI view;
    public EdgeDetectionGray(String imageFile) 
            throws IOException, ImageDecoderException, OperationException 
    {
        super(imageFile);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
      try {
            Image testImage = new Image(args[0]);
            testImage.display( new Point(0,0), args[0] );
            testImage.ConvertToGray();
            testImage.Save("gray");
            }
      catch (Exception e) {
        System.err.println(e);
        System.exit(1);
      }
    }
    else {
      System.err.println("usage: java  <imagefile>");
      System.exit(1);
    }
      
    }
    
}
