package com.password_db.gui;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;

/**
 * This class creates a JPanel with an image as background.
 */
public class JBPanel extends JPanel{

    private Image backgroundImage;
    private int x_dimension;
    private int y_dimension;

    public JBPanel(){
        super();
    }

    public JBPanel(String backgroundImage, int x_dimension, int y_dimension) throws IOException{
        this.backgroundImage = ImageIO.read(new File("PasswordDatabase\\src\\main\\java\\com\\password_db\\images\\" + backgroundImage));
        this.x_dimension = x_dimension;
        this.y_dimension = y_dimension;

        this.backgroundImage = this.backgroundImage.getScaledInstance(this.x_dimension, this.y_dimension, Image.SCALE_SMOOTH);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(this.backgroundImage, 0, 0, this);
    }
}
