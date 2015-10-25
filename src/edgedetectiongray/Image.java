/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edgedetectiongray;

import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;
import java.awt.image.*;
import java.awt.*;        
import java.io.IOException;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
/**
 *
 * @author kanvikhanna
 */
public class Image extends JFrame {
     
    private BufferedImage image;
    
    public Image( String imageFile ) throws IOException, ImageDecoderException
    {
        super( imageFile );
        
        // convert imageFile into a BufferedImage object
        ImageDecoder input = ImageFile.createImageDecoder(imageFile);
        image = input.decodeAsBufferedImage();
        //image = ImageIO.read(new File(imageFile));
        System.out.println(BufferedImage.TYPE_INT_RGB);
        if( image.getType() != BufferedImage.TYPE_INT_RGB )
        {
            System.out.println("Image not color. Exiting...");
            System.exit(0);
        }
        
    } // end of method
    
    /**
     * Saves this image as a JPG file.
     * @param saveImgAs file-name to be used to save the file. Must be a
     * JPG type of name.
     */
    public void Save( String saveImgAs )
    {
        saveImage(image, saveImgAs);            
    } // end of method

    /**
     * Helper method to save image.
     * @param newImg
     * @param fileName 
     */ 
    private void saveImage(BufferedImage newImg, String fileName)
    {

        File imageFile = new File(fileName+".jpg");
        try
        {
        ImageIO.write(newImg,"jpg",imageFile);
        }
        catch (IOException e)
        {
            System.out.println(fileName+" not saved"+e);
        }

    } // end of method
  
    /**
     * Display this histogram on screen.
     * @param where
     * @param title 
     */
    public void display(Point where, String title)
    {
        DisplayImage(image, where, title );        
    } // end of method

    // Display in Jframe
    private void DisplayImage(BufferedImage img, Point where, String title )
    {
        JPanel panel=new JPanel();
        panel.add(new JLabel(new ImageIcon(img)));
        JFrame frame=new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(where.x,where.y,img.getWidth(), img.getHeight());
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    // Convert color image to grayscale
    public void ConvertToGray()
    {
        int in_imgWidth = image.getWidth();
        int in_imgHeight = image.getHeight();
        int i,j;
        BufferedImage out_img = new BufferedImage(in_imgWidth, in_imgHeight, 
        BufferedImage.TYPE_BYTE_GRAY);
        
        for(j = 0; j < in_imgHeight; j++)
        {
            for(i = 0; i < in_imgWidth; i++)
            {
                int rgb = image.getRGB(i, j);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb & 0xFF);
                int grayLevel = (red + green + blue)/3;
                int gray = (alpha << 24)|(grayLevel << 16)|(grayLevel << 8)|grayLevel; 
                out_img.setRGB(i, j, gray);
            }
        }
        image = out_img;
        //SaveImage(out_img,"color");
    }// end of method
    
}
