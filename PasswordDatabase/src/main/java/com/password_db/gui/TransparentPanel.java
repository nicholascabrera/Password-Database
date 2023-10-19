package com.password_db.gui;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class TransparentPanel extends JPanel {

    public TransparentPanel(){
        setOpaque(false);
    }

    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        Rectangle r = g.getClipBounds();
        
        g.fillRect(r.x, r.y, r.width, r.height);
        super.paintComponent(g);
    }
}