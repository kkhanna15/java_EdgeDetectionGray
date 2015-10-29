/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edgedetectiongray;

import java.awt.image.*;
import java.awt.*;        
import java.io.IOException;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
/**
 * This class models the Sobel Mask or the Sobel operator.
 * A SobelMask object must be created with an input JPG file on which the sobel
 * operator will work and produce the images for gradient in x-direction, 
 * y-direction, gradient magnitude and phase.
 * It can be used to save as well as display the images on screen.
 * @author kanvikhanna
 */
public class SobelMask extends JFrame{
    /**
     * attributes
     */
    /**
     * Gradient in x and Gradient in y
     */
    private final int[][] x_mask = {{-1,0,1},{-2,0,2},{-1,0,1}};
    private final int[][] y_mask = {{-1,-2,-1},{0,0,0},{1,2,1}};
    private final int height_img;
    private final int width_img;
    
    /**
     * 2D image representation of 
     * gradient in x,
     * gradient in y,
     * gradient magnitude and
     * phase
     */ 
    private double[][] img_gx;
    private double[][] img_gy;
    private double[][] img_gxy;
    private double[][] img_phase;
    
    
    /**
     * Methods
     */
    /**
     * Creates a SobelMask object using a JPG file as input, on which the Sobel
     * mask is to be applied.
     * @param imageFile JPG file. 
     */
    public SobelMask( String imageFile )throws IOException
    {
        super( imageFile );
        // convert imageFile into a BufferedImage object
        
        BufferedImage img = ImageIO.read(new File(imageFile));
        if( img.getType() != BufferedImage.TYPE_BYTE_GRAY )
        {
            System.out.println("Image not Gray scale. Exiting...");
            System.exit(0);
        }
        
        height_img = img.getHeight();
        width_img = img.getWidth();
        img_gx = new double[width_img][height_img];
        img_gy = new double[width_img][height_img];
        img_gxy = new double[width_img][height_img];
        img_phase = new double[width_img][height_img];
        
        
        try { 
            calGx( img );
            Save(img_gx, "GradientX");
            calGy( img );
            Save(img_gy, "GradientY");
            calGxy( );
            Save(img_gxy, "Magnitude");
            calPhase( );
            Save(img_phase, "Phase");
        } 
        catch(Exception e){
            System.out.print(e.toString());
        }
    } // end of method
    
    /**
     * Saves the gradient as a JPG file.
     * @param img_rep contains values for the gradient
     * @param saveImgAs file-name to be used to save the file. Must be a
     * JPG type of name.
     */
    public void Save( double img_rep[][], String saveImgAs )
    {
        BufferedImage _img = 
            new BufferedImage(
                width_img, 
                height_img, 
                BufferedImage.TYPE_BYTE_GRAY
            );

        WritableRaster _imgRaster = (WritableRaster)_img.getRaster();
        //scaling
        double max = img_rep[0][0];
        double min = img_rep[0][0];
        for( int j = 0; j < height_img; ++j)
           for(int i = 0; i < width_img; ++i ) 
           {
               if(max < img_rep[i][j])
                   max = img_rep[i][j];
               if(min > img_rep[i][j])
                   min = img_rep[i][j];
           }
        //System.out.println(max);
        //System.out.println(min);
        for( int j = 0; j < height_img; ++j)
           for(int i = 0; i < width_img; ++i ) 
           {
               double value = (((255 - 0)/(max - min)) * img_rep[i][j]) + 
                   ( min * ((0 - 255)/(max - min)));
               _imgRaster.setSample( i, j, 0, value );
           }
        
        _img.setData(_imgRaster);
        saveImage(_img, saveImgAs);
          
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
     * Display the gradient image on screen.
     * @param where
     * @param title
     * @param filename
     * @throws IOException
     */
    public void display(Point where, String title, String filename) throws IOException
    {
        BufferedImage _img =  ImageIO.read(new File(filename));
        DisplayImage(_img, where, title );
        
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
    
    /**
     * Helper function that takes a BufferedImage as input and produces the
     * gradient x as output.
     */
    private void calGx(BufferedImage img_x)
    {
        int i,j;
        Raster raster_x = img_x.getRaster();
        
        for( j=0; j < height_img; ++j)
            for(i=0; i < width_img; ++i)
                img_gx[i][j] = 0;
                
        for( j=1; j < height_img-1; ++j)           
        {
            for(i=1; i < width_img-1; ++i)
            {
                double sum = (raster_x.getSample(i-1, j-1, 0) * x_mask[0][0])+
                        (raster_x.getSample(i+1, j-1, 0) * x_mask[0][2]) + 
                        (raster_x.getSample(i-1, j, 0) * x_mask[1][0]) +
                        (raster_x.getSample(i+1, j, 0) * x_mask[1][2]) +
                        (raster_x.getSample(i-1, j+1, 0) * x_mask[2][0]) +
                        (raster_x.getSample(i+1, j+1, 0) * x_mask[2][2]);
                
                img_gx[i][j] = sum;
                
            }
        }
        
    }// end of method
    
    /**
     * Helper function that takes a BufferedImage as input and produces the
     * gradient y as output.
     */
    private void calGy(BufferedImage img_y)
    {
        int i,j;
        Raster raster_y = img_y.getRaster();
        
        for( j=0; j < height_img; ++j)
            for(i=0; i < width_img; ++i)
                img_gy[i][j] = 0;
                
        for( j=1; j < height_img-1; ++j)           
        {
            for(i=1; i < width_img-1; ++i)
            {
                double sum = (raster_y.getSample(i-1, j-1, 0) * y_mask[0][0])+
                        (raster_y.getSample(i, j-1, 0) * y_mask[0][1]) + 
                        (raster_y.getSample(i+1, j-1, 0) * y_mask[0][2]) +
                        (raster_y.getSample(i-1, j+1, 0) * y_mask[2][0]) +
                        (raster_y.getSample(i, j+1, 0) * y_mask[2][1]) +
                        (raster_y.getSample(i+1, j+1, 0) * y_mask[2][2]);
                
                img_gy[i][j] = sum;
            }
        }
    }// end of method
    
    /**
     * Helper function that produces the gradient magnitude as output.
     */
    private void calGxy()
    {
        int i,j;
        
        for( j=0; j < height_img; ++j)
            for(i=0; i < width_img; ++i)
                img_gxy[i][j] = 0;
                
        for( j=1; j < height_img-1; ++j)           
        {
            for(i=1; i < width_img-1; ++i)
            {
                double sum = Math.sqrt((img_gx[i][j]*img_gx[i][j])+
                        (img_gy[i][j]*img_gy[i][j]));
                
                img_gxy[i][j] = sum;
                
            }
        }
    }// end of method
    
    /**
     * Helper function that produces the phase as output.
     */
    private void calPhase()
    {
        int i,j;
        
        for( j=0; j < height_img; ++j)
            for(i=0; i < width_img; ++i)
                img_phase[i][j] = 0;
                
        for( j=1; j < height_img-1; ++j)           
        {
            for(i=1; i < width_img-1; ++i)
            {                
                img_phase[i][j] = Math.atan2(img_gx[i][j],img_gy[i][j]);
                //System.out.println(img_phase[i][j]);
            }
        }
    }// end of method
    
}// end of class definition
