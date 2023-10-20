package com.password_db.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.Border;

public class RoundedBorder implements Border {
    private int radius;
    private Color backgroundColor;
    private Color foregroundColor;

    RoundedBorder(int radius, Color backgroundColor, Color foregroundColor) {
        this.radius = radius;
        String color = Integer.toHexString(backgroundColor.getRGB());
        color = color.substring(2, color.length());
        color = "ff" + color;
        this.backgroundColor = new Color(Integer.parseUnsignedInt(color, 16), false);
        this.foregroundColor = foregroundColor;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        /* Turn on antialiasing to get nicer ovals. */

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle rect = new Rectangle(0, 0, width, height);
        Area borderRegion = new Area(rect);
        RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(x, y, width, height, radius, radius);

        borderRegion.subtract(new Area(roundRect));
        
        g.setClip(borderRegion);
        g.setColor(this.backgroundColor);
        g.fillRect(0, 0, width, height);
        g.setClip(null);

        g.setColor(this.foregroundColor);
        g.drawRoundRect(x, y, width, height, radius, radius);
        g.drawRoundRect(x, y, width, height, radius+2, radius+2);
        g.drawRoundRect(x, y, width, height, radius+3, radius+3);
    }
}