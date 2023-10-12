package com.password_db.handlers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class PortalEventHandler implements ActionListener, KeyListener, MouseMotionListener  {

    private JFrame portalFrame;
    private JLabel label1;
    private JLabel label2;
    
    private Color paneColor = new Color(0xFAFAFA);
    private Color activeColor = new Color(0x96CFFF);

    public PortalEventHandler(JFrame portalFrame, JLabel label1, JLabel label2){
        this.portalFrame = portalFrame;
        this.label1 = label1;
        this.label2 = label2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            this.portalFrame.dispose();
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void resetColor(){
        this.label1.setBackground(this.paneColor);
        this.label2.setBackground(this.paneColor);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(e.getComponent().getClass().getName() == label1.getName()){
            label1.setBackground(activeColor);
        } else if(e.getComponent().getClass().getName() == label2.getName()){
            label2.setBackground(activeColor);
        } else {
            resetColor();
        }
    }


    
}
