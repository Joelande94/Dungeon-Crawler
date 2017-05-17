/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author joel_
 */
public class ImageLoader {
    public Image image;
    private String path = "/resources/images/";
    
    public ImageLoader(String imageName) throws IOException{
        image = ImageIO.read(ImageLoader.class.getResource((path+imageName)));
    }
}
