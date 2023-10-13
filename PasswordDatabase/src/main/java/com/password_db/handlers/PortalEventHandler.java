package com.password_db.handlers;

// import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class PortalEventHandler implements ActionListener, KeyListener  {

    private JFrame portalFrame;

    //light v dark mode
    private boolean colorMode;
    // private Color paneColor = new Color(0xFAFAFA);
    // private Color darkPaneColor = new Color(0xFAFAFA);
    // private Color frameColor = new Color(0xE8F3FF);
    // private Color darkFrameColor = new Color(0xE8F3FF);

    

    public PortalEventHandler(JFrame portalFrame){
        this.portalFrame = portalFrame;
        this.colorMode = true;   //true is light, false is dark
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "switch"){
            this.colorMode = !this.colorMode;
            if(colorMode){
                // set everything to the light mode colors
            } else {
                // set everything to the dark mode colors
            }
        }
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
}