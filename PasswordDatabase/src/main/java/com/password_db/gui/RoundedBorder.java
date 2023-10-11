package com.password_db.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.Border;

public class RoundedBorder implements Border {
    private int radius;
    private Color backgroundColor;
    private Color foregroundColor;

    RoundedBorder(int radius, Color backgroundColor, Color foregroundColor) {
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Rectangle rect = new Rectangle(0, 0, width, height);
        Area borderRegion = new Area(rect);
        RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(x, y, width, height, radius, radius);

        borderRegion.subtract(new Area(roundRect));

        g.setClip(borderRegion);
        g.setColor(this.backgroundColor);
        g.fillRect(0, 0, width, height);
        g.setClip(null);

        g.setColor(this.foregroundColor);
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}