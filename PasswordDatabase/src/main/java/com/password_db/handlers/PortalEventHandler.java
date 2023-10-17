package com.password_db.handlers;

// import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.password_db.cryptography.Password;
import com.password_db.databases.Database;
import com.password_db.gui.GUI;

public class PortalEventHandler implements ActionListener, KeyListener  {

    private JFrame portalFrame;
    private Database generated_db;

    //light v dark mode
    private boolean colorMode;
    private GUI window;
    // private Color paneColor = new Color(0xFAFAFA);
    // private Color darkPaneColor = new Color(0xFAFAFA);
    // private Color frameColor = new Color(0xE8F3FF);
    // private Color darkFrameColor = new Color(0xE8F3FF);

    

    public PortalEventHandler(GUI window, JFrame portalFrame, Database generated_db){
        this.portalFrame = portalFrame;
        this.window = window;
        this.generated_db = generated_db;
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
        } else if (e.getActionCommand() == "logout"){
            this.window.setUsername("");
            this.window.setPassword(new Password());
            this.window.setInstance("login");
            this.portalFrame.dispose();
        } else if(e.getActionCommand() == "generate"){
            Password newPassword = new Password();
            newPassword.init(this.generated_db);
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