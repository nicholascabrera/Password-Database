package com.password_db.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.password_db.cryptography.Password;
import com.password_db.databases.Database;
import com.password_db.handlers.LogInEventHandler;
import com.password_db.handlers.PortalEventHandler;

public class GUI {
    private final boolean LIGHT_MODE = true;
    private final boolean DARK_MODE = false;

    private int x_dimension, y_dimension, x_location, y_location;
    private LogInEventHandler loginHandler;
    private PortalEventHandler portalHandler;
    private Database userDatabase;
    private Database generatedDatabase;
    private String instance;

    private boolean colorMode;

    private Color frameColor, paneColor, activeColor, containerColor;
    private Color darkFrameColor, darkPaneColor, darkContainerColor;
    private Color lightFrameColor, lightPaneColor, lightContainerColor;
    private Color borderColor;

    public GUI(){
        this.x_dimension = 600;
        this.y_dimension = 400;
        this.x_location = 375;
        this.y_location = 100;

        this.userDatabase = new Database("-1", new Password("-1"));
        this.generatedDatabase = new Database();

        this.borderColor = new Color(0xFFD700);

        this.lightFrameColor = new Color(0x002366);
        this.lightPaneColor = new Color(0xFAFAFA);
        this.lightContainerColor = new Color(0xEDEDED);
        // this.lightContainerColor = new Color(0x8FEDEDED, true);

        this.darkFrameColor = new Color(0x585A5C);
        this.darkPaneColor = new Color(0x707477);
        this.darkContainerColor = new Color(0x989DA0);

        this.activeColor = new Color(0x96CFFF);
    }

    public void init(){
        this.instance = "login";

        this.setColor(LIGHT_MODE);
        this.setInstance(this.instance);
    }

    public void setColor(boolean color){
        if(color){
            colorMode = LIGHT_MODE;
            this.frameColor = this.lightFrameColor;
            this.paneColor = this.lightPaneColor;
            this.containerColor = this.lightContainerColor;
        } else {
            colorMode = DARK_MODE;
            this.frameColor = this.darkFrameColor;
            this.paneColor = this.darkPaneColor;
            this.containerColor = this.darkContainerColor;
        }
    }

    public boolean getColor(){
        return this.colorMode;
    }

    public void setInstance(String instance){
        switch(instance){
            case "login":
                this.instance = "login";
                this.loginInstance();
                break;
            case "portal":
                this.instance = "portal";
                this.portalInstance();
                break;
        }
    }

    public String getInstance(){
        return this.instance;
    }

    /**
     * This instance allows the user to login. No matter what the user inputs, they will be allowed access.
     */
    private void loginInstance(){
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame loginFrame = new JFrame("Password Generator - Log In");
        loginFrame.setPreferredSize(new Dimension(this.x_dimension, this.y_dimension));
        loginFrame.setLocation(this.x_location, this.y_location);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginFrame.setResizable(false);

        Container contentPane = loginFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(this.frameColor);

        JPanel coloredBorderPane = new JPanel();
        coloredBorderPane.setLayout(new BoxLayout(coloredBorderPane, BoxLayout.PAGE_AXIS));
        coloredBorderPane.setBackground(contentPane.getBackground());

        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.PAGE_AXIS));
        textPane.setBackground(this.paneColor);

        // idea on how to store usernames and passwords without the passwords being stored anywhere in plaintext:
        // username is stored in plain text
        // instead of storing the password with it, a string that has been encrypted using the password as a key is paired with it
        // if the user logs in with an existing username but the string is incorrectly decrypted, then the password is wrong.
        // i can find if the username exists with a hash map, username is hashed and used as the ID for constant search time
        // the string can be some random word from a dictionary, which has some random words and their hashes.
        // i can find if the string matches plain english by hashing the string and seeing if that hash exists in the dictionary.
        // when all is said and done, it should be a constant run-time login verification system.

        int preferedSize = 200;

        textPane.setMinimumSize(new Dimension(preferedSize+20, 150));
        textPane.setPreferredSize(new Dimension(preferedSize+20, 150));
        textPane.setMaximumSize(new Dimension(preferedSize+20, 150));

        JTextField username = new JTextField(preferedSize);
        JPasswordField password = new JPasswordField(preferedSize);
        JButton loginButton = new JButton("Log In");

        loginHandler = new LogInEventHandler(this, this.userDatabase, loginFrame, username, password);

        username.setMinimumSize(new Dimension(preferedSize, 20));
        password.setMinimumSize(new Dimension(preferedSize, 20));
        loginButton.setMinimumSize(new Dimension(preferedSize-10, 20));

        username.setPreferredSize(new Dimension(preferedSize, 20));
        password.setPreferredSize(new Dimension(preferedSize, 20));
        loginButton.setPreferredSize(new Dimension(preferedSize-10, 20));

        username.setMaximumSize(new Dimension(preferedSize, 20));
        password.setMaximumSize(new Dimension(preferedSize, 20));
        loginButton.setMaximumSize(new Dimension(preferedSize-10, 20));

        username.addActionListener(this.loginHandler);
        username.addKeyListener(this.loginHandler);
        password.addActionListener(this.loginHandler);
        password.addKeyListener(loginHandler);

        loginButton.setActionCommand("login");
        loginButton.addActionListener(this.loginHandler);

        JPanel userPane = new JPanel();
        userPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        userPane.setBackground(textPane.getBackground());

        userPane.setMinimumSize(new Dimension(preferedSize, 20));
        userPane.setPreferredSize(new Dimension(preferedSize, 20));
        userPane.setMaximumSize(new Dimension(preferedSize, 20));

        JPanel passPane = new JPanel();
        passPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        passPane.setBackground(textPane.getBackground());

        passPane.setMinimumSize(new Dimension(preferedSize, 20));
        passPane.setPreferredSize(new Dimension(preferedSize, 20));
        passPane.setMaximumSize(new Dimension(preferedSize, 20));

        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");

        userPane.add(userLabel);
        passPane.add(passLabel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPane.setBackground(textPane.getBackground());
        loginButton.setBackground(textPane.getBackground());

        buttonPane.setMinimumSize(new Dimension(preferedSize, 30));
        buttonPane.setPreferredSize(new Dimension(preferedSize, 30));
        buttonPane.setMaximumSize(new Dimension(preferedSize, 30));

        buttonPane.add(loginButton);

        textPane.add(Box.createGlue());                                         // adds extra space to account for window expansion
        textPane.add(userPane);
        textPane.add(username);                                                 // adds the username text field
        textPane.add(Box.createRigidArea(new Dimension(0,5)));     // adds an invisible space between the text fields
        textPane.add(passPane);
        textPane.add(password);                                                 // adds the password text field
        textPane.add(Box.createRigidArea(new Dimension(0,5)));     // adds an invisible space between the text fields
        textPane.add(buttonPane);
        textPane.add(Box.createRigidArea(new Dimension(0,5)));     // adds an invisible space between the text fields
        textPane.add(Box.createGlue());                                         // adds extra space to account for window expansion
        textPane.setBorder(new RoundedBorder(10, this.frameColor, this.borderColor));

        coloredBorderPane.add(Box.createGlue());
        coloredBorderPane.add(textPane);
        coloredBorderPane.add(Box.createGlue());
        coloredBorderPane.setBorder(BorderFactory.createEmptyBorder(90, 10, 90, 10));

        contentPane.add(coloredBorderPane, BorderLayout.CENTER);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }

    /**
     * This instance displays the portal shown in the .drawio and displays the username as a welcome.
     * 
     * The instance also contains a side bar with:
     * - Account (which shows account data, allows username and password change)
     * - Settings (which allows the user to switch to dark mode)
     * - Statistics (shows the average strength of your passwords)     
     * - Generate (for generating a password)
     * 
     * 
     * - Sign Out (move instance to login and dispose portal, reset username and master password.)
     * - About (tells some information about me)
     * 
     * The instance also displays all the passwords along with their usernames and websites in a scrollable pane.
     * 
     * Above it is a search bar which would isolate the website and its usernames and passwords in the scrollable pane.
     * 
     * The instance also has bubbles for:
     * - ? (shows the keybindings for everything)
     * - + (for generating a password)
     * 
     * Features:
     *  - text field for website
     *  - buttons for database print, database search, and generate password.
     *  - search and generate take input from website to use as their ID.
     *  - print disregards website input.
     *  - additional button to go back to main.
     */
    private void portalInstance(){
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame portalFrame = new JFrame("Password Generator - Portal");
        portalFrame.setPreferredSize(new Dimension(this.x_dimension, this.y_dimension));
        portalFrame.setLocation(this.x_location, this.y_location);
        portalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        portalFrame.setResizable(false);

        Container contentPane = portalFrame.getContentPane();
        contentPane.setBackground(this.frameColor);

        JPanel coloredBorderPane = new JPanel();
        coloredBorderPane.setLayout(new BoxLayout(coloredBorderPane, BoxLayout.PAGE_AXIS));
        coloredBorderPane.setBackground(contentPane.getBackground());

        JPanel containerPane = new JPanel();
        containerPane.setLayout(new BoxLayout(containerPane, BoxLayout.LINE_AXIS));
        containerPane.setBackground(this.containerColor);

        containerPane.setMinimumSize(new Dimension(x_dimension-40, y_dimension-80));
        containerPane.setPreferredSize(new Dimension(x_dimension-40, y_dimension-80));
        containerPane.setMaximumSize(new Dimension(x_dimension-40, y_dimension-80));

        JPanel menuPane = new JPanel();
        menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.PAGE_AXIS));
        menuPane.setBackground(this.paneColor);

        JPanel centerPane = new JPanel();
        centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.PAGE_AXIS));
        centerPane.setBackground(this.containerColor);

        JPanel bubblePane = new JPanel();
        bubblePane.setLayout(new BoxLayout(bubblePane, BoxLayout.PAGE_AXIS));
        bubblePane.setBackground(this.paneColor);


        // MENU PANE CREATION
        int buttonWidth = 90;
        int buttonHeight = 43;

        final JButton accountButton = new JButton("Account");
        accountButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        accountButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        accountButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

        accountButton.setBackground(paneColor);
        accountButton.setContentAreaFilled(false);
        accountButton.setBorderPainted(false);
        accountButton.setFocusPainted(false);
        accountButton.setOpaque(true);
        accountButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    accountButton.setBackground(activeColor);
                } else {
                    accountButton.setBackground(paneColor);
                }
            }
        });

        final JButton settingsButton = new JButton("Settings");
        settingsButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        settingsButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        settingsButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

        settingsButton.setBackground(paneColor);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setOpaque(true);
        settingsButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    settingsButton.setBackground(activeColor);
                } else {
                    settingsButton.setBackground(paneColor);
                }
            }
        });

        final JButton statisticsButton = new JButton("Statistics");
        statisticsButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        statisticsButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        statisticsButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        
        statisticsButton.setBackground(paneColor);
        statisticsButton.setContentAreaFilled(false);
        statisticsButton.setBorderPainted(false);
        statisticsButton.setFocusPainted(false);
        statisticsButton.setOpaque(true);
        statisticsButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    statisticsButton.setBackground(activeColor);
                } else {
                    statisticsButton.setBackground(paneColor);
                }
            }
        });


        final JButton generateButton = new JButton("Generate");
        generateButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        generateButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        generateButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

        generateButton.setBackground(paneColor);
        generateButton.setContentAreaFilled(false);
        generateButton.setBorderPainted(false);
        generateButton.setFocusPainted(false);
        generateButton.setOpaque(true);

        generateButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    generateButton.setBackground(activeColor);
                } else {
                    generateButton.setBackground(paneColor);
                }
            }
        });

        generateButton.setActionCommand("generate");


        final JButton signoutButton = new JButton("Sign Out");
        signoutButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        signoutButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        signoutButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

        signoutButton.setBackground(paneColor);
        signoutButton.setContentAreaFilled(false);
        signoutButton.setBorderPainted(false);
        signoutButton.setFocusPainted(false);
        signoutButton.setOpaque(true);

        signoutButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    signoutButton.setBackground(activeColor);
                } else {
                    signoutButton.setBackground(paneColor);
                }
            }
        });

        signoutButton.setActionCommand("logout");;


        final JButton aboutButton = new JButton("About");
        aboutButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        aboutButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        aboutButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

        aboutButton.setBackground(paneColor);
        aboutButton.setContentAreaFilled(false);
        aboutButton.setBorderPainted(false);
        aboutButton.setFocusPainted(false);
        aboutButton.setOpaque(true);
        aboutButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    aboutButton.setBackground(activeColor);
                } else {
                    aboutButton.setBackground(paneColor);
                }
            }
        });

        this.portalHandler = new PortalEventHandler(this, portalFrame, this.generatedDatabase);
        
        portalFrame.addKeyListener(this.portalHandler);
        signoutButton.addActionListener(this.portalHandler);
        generateButton.addActionListener(this.portalHandler);


        menuPane.add(accountButton);
        menuPane.add(settingsButton);
        menuPane.add(statisticsButton);
        menuPane.add(generateButton);
        menuPane.add(Box.createRigidArea(new Dimension(0, buttonHeight)));
        menuPane.add(signoutButton);
        menuPane.add(aboutButton);

        menuPane.setMinimumSize(new Dimension(buttonWidth, buttonHeight*7));
        menuPane.setPreferredSize(new Dimension(buttonWidth, buttonHeight*7));
        menuPane.setMaximumSize(new Dimension(buttonWidth, buttonHeight*7));
        
        // CENTER PANE 
        int preferedSize = 375;

        JPanel welcomeWrapper = new JPanel();
        welcomeWrapper.setLayout(new BoxLayout(welcomeWrapper, BoxLayout.LINE_AXIS));
        welcomeWrapper.setBackground(this.containerColor);

        welcomeWrapper.setMinimumSize(new Dimension(preferedSize, buttonHeight));
        welcomeWrapper.setPreferredSize(new Dimension(preferedSize, buttonHeight));
        welcomeWrapper.setMaximumSize(new Dimension(preferedSize, buttonHeight));

        JPanel welcome = new JPanel();
        welcome.setLayout(new BorderLayout());
        welcome.setBackground(this.paneColor);

        JLabel welcomeLabel = new JLabel("Welcome back, " + this.getUsername());
        welcomeLabel.setBackground(this.paneColor);

        welcome.setMinimumSize(new Dimension((preferedSize/2) - 15, buttonHeight-10));
        welcome.setPreferredSize(new Dimension((preferedSize/2) - 15, buttonHeight-10));
        welcome.setMaximumSize(new Dimension((preferedSize/2) - 15, buttonHeight-10));

        JPanel containerColorRigidHorizontal = new JPanel();
        containerColorRigidHorizontal.setBackground(containerPane.getBackground());
        containerColorRigidHorizontal.add(Box.createRigidArea(new Dimension((preferedSize/2)-10, 0)));

        welcome.add(welcomeLabel, BorderLayout.CENTER);
        welcome.setBorder(new RoundedBorder(5, this.containerColor, this.paneColor));

        welcomeWrapper.add(welcome);
        welcomeWrapper.add(containerColorRigidHorizontal);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.setBackground(this.paneColor);

        JTextField search = new JTextField(preferedSize);
        
        search.setMinimumSize(new Dimension(preferedSize, 20));
        search.setPreferredSize(new Dimension(preferedSize, 20));
        search.setMaximumSize(new Dimension(preferedSize, 20));

        JTextArea passwords = new JTextArea(5, preferedSize);
        passwords.setEditable(false);
        passwords.setLineWrap(true);
        passwords.setFont(new Font("Arial", Font.PLAIN, 10));

        passwords.setText("this is where usernames, websites, and passwords will be stored.");

        JScrollPane view = new JScrollPane(passwords);
        view.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        view.setMinimumSize(new Dimension(preferedSize, (buttonHeight*5) - 20));
        view.setPreferredSize(new Dimension(preferedSize, (buttonHeight*5) - 20));
        view.setMaximumSize(new Dimension(preferedSize, (buttonHeight*5) - 20));

        JPanel containerColorRigidVertical = new JPanel();
        containerColorRigidVertical.setBackground(containerPane.getBackground());
        containerColorRigidVertical.add(Box.createRigidArea(new Dimension(0, 5)));

        containerColorRigidVertical.setMinimumSize(new Dimension(0, 5));
        containerColorRigidVertical.setPreferredSize(new Dimension(0, 5));
        containerColorRigidVertical.setMaximumSize(new Dimension(0, 5));

        JPanel containerColorGlue = new JPanel();
        containerColorGlue.setBackground(this.containerColor);
        containerColorGlue.add(Box.createGlue());

        content.add(search);
        content.add(containerColorRigidVertical);
        content.add(Box.createGlue());
        content.add(view);

        content.setMinimumSize(new Dimension(preferedSize, (buttonHeight*6) - 10));
        content.setPreferredSize(new Dimension(preferedSize, (buttonHeight*6) - 10));
        content.setMaximumSize(new Dimension(preferedSize, (buttonHeight*6) - 10));
        content.setBorder(new RoundedBorder(10, this.containerColor, this.paneColor));




        centerPane.add(welcomeWrapper);
        centerPane.add(containerColorRigidVertical);
        centerPane.add(content);


        // BUBBLE PANE

        bubblePane.add(Box.createRigidArea(new Dimension(buttonWidth + buttonWidth, 0)));
        

        containerPane.add(Box.createGlue());
        containerPane.add(menuPane);
        containerPane.add(Box.createRigidArea(new Dimension(10, 0)));
        containerPane.add(centerPane);
        containerPane.add(Box.createRigidArea(new Dimension(5, 0)));
        containerPane.add(bubblePane);
        containerPane.add(Box.createGlue());
        containerPane.setBorder(new RoundedBorder(10, this.frameColor, this.borderColor));

        coloredBorderPane.add(Box.createGlue());
        coloredBorderPane.add(containerPane);
        coloredBorderPane.add(Box.createGlue());
        coloredBorderPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPane.add(coloredBorderPane);

        portalFrame.pack();
        portalFrame.setVisible(true);
    }

    public String getUsername(){
        return this.userDatabase.getUsername();
    }

    public Password getPassword(){
        return this.userDatabase.getPassword();
    }

    public void setUsername(String username){
        this.userDatabase.setUsername(username);
    }

    public void setPassword(Password password){
        this.userDatabase.setPassword(password);
    }
}
