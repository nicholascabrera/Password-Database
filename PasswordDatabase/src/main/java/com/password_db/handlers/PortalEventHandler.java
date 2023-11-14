package com.password_db.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.password_db.cryptography.Password;
import com.password_db.databases.Database;
import com.password_db.gui.GUI;

public class PortalEventHandler implements ActionListener, KeyListener  {

    private JFrame portalFrame;
    private Database generated_db;
    private GUI gui;

    

    public PortalEventHandler(GUI gui, JFrame portalFrame, Database generated_db){
        this.portalFrame = portalFrame;
        this.gui = gui;
        this.generated_db = generated_db;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "logout"){
            this.gui.setUsername("");
            this.gui.setPassword(new Password());
            this.gui.setInstance("login");
            this.portalFrame.dispose();
        } else if(e.getActionCommand() == "generate"){
            Password newPassword = new Password();
            try{
                newPassword.init(this.generated_db, this.gui, this.portalFrame);
            } catch (Exception err){
                err.printStackTrace();
            }
        } else if(e.getActionCommand() == "exit"){
            this.gui.setUsername("");
            this.gui.setPassword(new Password());
            System.exit(0);
        } else if(e.getActionCommand() == "account" || e.getActionCommand() == "settings"){
            JOptionPane.showMessageDialog(this.portalFrame, "This feature is currently in development and is inoperable.", "In Development!", JOptionPane.WARNING_MESSAGE);
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