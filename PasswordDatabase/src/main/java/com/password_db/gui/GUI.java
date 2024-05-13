package com.password_db.gui;

import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.password_db.cryptography.Password;
import com.password_db.databases.Database;
import com.password_db.databases.DatabaseTaskManager;
import com.password_db.databases.TaskManager;
import com.password_db.handlers.LogInEventHandler;
import com.password_db.handlers.PortalEventHandler;
import com.password_db.handlers.SearchEventHandler;
import com.password_db.trie.Trie;

public class GUI {
    private final boolean LIGHT_MODE = true;
    private final boolean DARK_MODE = false;

    private int x_dimension, y_dimension, x_location, y_location;
    private LogInEventHandler loginHandler;
    private PortalEventHandler portalHandler;
    private Database database;
    private String instance;
    private Trie applicationsTrie;

    private JTable passwordTable;
    private JScrollPane view;

    private boolean colorMode;

    private Color frameColor, paneColor, activeColor, containerColor;
    private Color darkFrameColor, darkPaneColor, darkContainerColor;
    private Color lightFrameColor, lightPaneColor, lightContainerColor;
    private Color borderColor;
    private Color transparentColor;

    public GUI(){
        this.x_dimension = 600;
        this.y_dimension = 400;
        this.x_location = 375;
        this.y_location = 100;

        this.database = new Database("-1", new Password("-1"));

        this.borderColor = new Color(0xCCAC00);

        this.lightFrameColor = new Color(0xaf002366, true);
        this.lightPaneColor = new Color(0xFFFAFAFA, true);
        this.lightContainerColor = new Color(0xFFEDEDED, true);

        this.darkFrameColor = new Color(0x585A5C);
        this.darkPaneColor = new Color(0x707477);
        this.darkContainerColor = new Color(0x989DA0);

        this.activeColor = new Color(0x002366);

        this.transparentColor = new Color(0, 0, 0, 0);

        this.applicationsTrie = new Trie();
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

    public Trie getApplicationsTrie() {
        return applicationsTrie;
    }

    public boolean getColor(){
        return this.colorMode;
    }

    public void setInstance(String instance, JFrame frame){
        this.setInstance(instance);
        frame.dispose();
    }

    public void setInstance(String instance){
        switch(instance){
            case "login":
                this.instance = "login";
                this.loginInstance();
                break;
            case "portal":
                this.instance = "portal";
                
                try {
                    this.portalInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case "account":
                break;
            case "settings":
                break;
            case "load":
                this.instance = "load";
                this.loadScreenInstance();
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

        JFrame loginFrame = new JFrame("Orchid - Log In");
        loginFrame.setPreferredSize(new Dimension(this.x_dimension, this.y_dimension));
        loginFrame.setLocation(this.x_location, this.y_location);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginFrame.setResizable(false);

        Container contentPane = loginFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(this.frameColor);

        JBPanel background = new JBPanel();

        try {
            background = new JBPanel("bc74860b1841a4dfe5aa9e3ea2571e36.jpg", this.x_dimension, this.y_dimension);
            background.setLayout(new BorderLayout());
        } catch (IOException e) {
            System.out.print("Bad image load.");
        }

        JPanel coloredBorderPane = new TransparentPanel();
        coloredBorderPane.setLayout(new BoxLayout(coloredBorderPane, BoxLayout.PAGE_AXIS));
        coloredBorderPane.setBackground(contentPane.getBackground());

        JPanel textPane = new TransparentPanel();
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
        this.setAbsoluteSize(textPane, new Dimension(preferedSize+20, 150));

        JTextField username = new JTextField(preferedSize);
        JPasswordField password = new JPasswordField(preferedSize);
        JButton loginButton = new JButton("Log In");

        loginHandler = new LogInEventHandler(this, this.database, loginFrame, username, password, loginButton);

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

        this.setAbsoluteSize(userPane, new Dimension(preferedSize, 20));

        JPanel passPane = new JPanel();
        passPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        passPane.setBackground(textPane.getBackground());

        this.setAbsoluteSize(passPane, new Dimension(preferedSize, 20));

        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");

        userPane.add(userLabel);
        passPane.add(passLabel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPane.setBackground(textPane.getBackground());
        loginButton.setBackground(textPane.getBackground());

        this.setAbsoluteSize(buttonPane, new Dimension(preferedSize, 30));

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
        textPane.setBorder(new RoundedBorder(10, contentPane.getBackground(), this.borderColor));

        coloredBorderPane.add(Box.createGlue());
        coloredBorderPane.add(textPane);
        coloredBorderPane.add(Box.createGlue());
        coloredBorderPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        background.add(coloredBorderPane, BorderLayout.CENTER);

        contentPane.add(background, BorderLayout.CENTER);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }

    private void configureButton(JButton button){
        int buttonWidth = 90;
        int buttonHeight = 43;

        button.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

        button.setBackground(paneColor);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isRollover()) {
                    button.setBackground(activeColor);
                    button.setForeground(paneColor);
                    button.setBorderPainted(true);
                    button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
                } else {
                    button.setBorderPainted(false);
                    button.setBackground(paneColor);
                    button.setForeground(Color.BLACK);
                }
            }
        });
    }

    public void fillTable(JFrame frame) throws Exception{
        DefaultTableModel model = (DefaultTableModel) this.passwordTable.getModel();
        model.setRowCount(0);

        DatabaseTaskManager taskManager = new DatabaseTaskManager(database);
        taskManager.setChoice(TaskManager.PULL_PASSWORDS);
        taskManager.setParameters(new Object[]{this.passwordTable, frame, this.applicationsTrie});
        taskManager.execute();
    }

    public void setAbsoluteSize(JPanel panel, Dimension dimension){
        panel.setMinimumSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setMaximumSize(dimension);
    }

    /**
     * This instance displays the portal shown in the .drawio and displays the username as a welcome.
     * 
     * The instance also contains a side bar with:
     * - Account (which shows account data, allows username and password change)
     * - Settings (which allows the user to import or export a list of websites, 
     *          usernames, and passwords, strength check all their passwords,
     *          set minimum strengths, lengths and settings for all their passwords,
     *          and set default password settings.)
     * - Generate (for generating a password)
     * 
     * 
     * - Sign Out (move instance to login and dispose portal, reset username and master password)
     * - Exit (allows the user to exit the program completely)
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
     * @throws Exception
     */
    private void portalInstance() throws Exception{
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame portalFrame = new JFrame("Orchid - Portal");
        portalFrame.setPreferredSize(new Dimension(this.x_dimension, this.y_dimension));
        portalFrame.setLocation(this.x_location, this.y_location);
        portalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        portalFrame.setResizable(false);

        Container contentPane = portalFrame.getContentPane();
        contentPane.setBackground(this.frameColor);

        JBPanel background = new JBPanel();

        try {
            background = new JBPanel("bc74860b1841a4dfe5aa9e3ea2571e36.jpg", this.x_dimension, this.y_dimension);
            background.setLayout(new BorderLayout());
        } catch (IOException e) {
            System.out.print("Bad image load.");
        }

        JPanel coloredBorderPane = new TransparentPanel();
        coloredBorderPane.setLayout(new BoxLayout(coloredBorderPane, BoxLayout.PAGE_AXIS));
        coloredBorderPane.setBackground(contentPane.getBackground());

        JPanel containerPane = new JPanel();
        containerPane.setLayout(new BoxLayout(containerPane, BoxLayout.LINE_AXIS));
        containerPane.setBackground(this.containerColor);

        this.setAbsoluteSize(containerPane, new Dimension(x_dimension-100, y_dimension-80));

        JPanel menuPane = new JPanel();
        menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.PAGE_AXIS));
        menuPane.setBackground(this.paneColor);

        JPanel centerPane = new JPanel();
        centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.PAGE_AXIS));
        centerPane.setBackground(this.transparentColor);

        JPanel bubblePane = new JPanel();
        bubblePane.setLayout(new BoxLayout(bubblePane, BoxLayout.PAGE_AXIS));
        bubblePane.setBackground(this.paneColor);

        // MENU PANE CREATION
        int buttonWidth = 90;
        int buttonHeight = 43;

        final JButton accountButton = new JButton("Account");
        this.configureButton(accountButton);
        accountButton.setActionCommand("account");

        final JButton settingsButton = new JButton("Settings");
        this.configureButton(settingsButton);
        settingsButton.setActionCommand("settings");

        final JButton generateButton = new JButton("Generate");
        this.configureButton(generateButton);
        generateButton.setActionCommand("generate");

        final JButton signoutButton = new JButton("Sign Out");
        this.configureButton(signoutButton);
        signoutButton.setActionCommand("logout");;

        final JButton exitButton = new JButton("Exit");
        this.configureButton(exitButton);
        exitButton.setActionCommand("exit");

        this.portalHandler = new PortalEventHandler(this, portalFrame, this.database);
        
        portalFrame.addKeyListener(this.portalHandler);
        signoutButton.addActionListener(this.portalHandler);
        generateButton.addActionListener(this.portalHandler);
        exitButton.addActionListener(this.portalHandler);
        accountButton.addActionListener(this.portalHandler);
        settingsButton.addActionListener(this.portalHandler);
        portalFrame.addKeyListener(this.portalHandler);

        menuPane.add(accountButton);
        menuPane.add(settingsButton);
        menuPane.add(generateButton);
        menuPane.add(Box.createRigidArea(new Dimension(0, (2 * buttonHeight) - 4)));
        menuPane.add(signoutButton);
        menuPane.add(exitButton);

        this.setAbsoluteSize(menuPane, new Dimension(buttonWidth, buttonHeight*7));
        
        // CENTER PANE 
        int preferedSize = 375;

        JPanel welcomeWrapper = new JPanel();
        welcomeWrapper.setLayout(new BoxLayout(welcomeWrapper, BoxLayout.LINE_AXIS));
        welcomeWrapper.setBackground(this.transparentColor);
        this.setAbsoluteSize(welcomeWrapper, new Dimension(preferedSize, buttonHeight));

        JPanel welcome = new JPanel();
        welcome.setLayout(new BorderLayout());
        welcome.setBackground(this.paneColor);

        JLabel welcomeLabel = new JLabel("Welcome back, " + this.getUsername());
        welcomeLabel.setBackground(this.paneColor);

        this.setAbsoluteSize(welcome, new Dimension((preferedSize/2) - 15, buttonHeight-10));

        JPanel containerColorRigidHorizontal = new JPanel();
        containerColorRigidHorizontal.setBackground(this.transparentColor);
        containerColorRigidHorizontal.add(Box.createRigidArea(new Dimension((preferedSize/2)-10, 0)));

        welcome.add(welcomeLabel, BorderLayout.CENTER);
        welcome.setBorder(new RoundedBorder(5, this.containerColor, this.frameColor));

        welcomeWrapper.add(welcome);
        welcomeWrapper.add(containerColorRigidHorizontal);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.setBackground(this.paneColor);

        //temporarily populate the table with nothing
        String recordsString[][] = new String[0][3];
        String fieldNames[] = {"Application", "Username", "Password"};
        this.passwordTable = new JTable(recordsString, fieldNames);

        DefaultTableModel tableModel = new DefaultTableModel(recordsString, fieldNames) {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        if(this.getColumnName(column) == fieldNames[2]){
                            return false;
                        }
                        return true;
                    }
                };
                
        this.passwordTable.setModel(tableModel);

        this.view = new JScrollPane(this.passwordTable);
        this.passwordTable.setFillsViewportHeight(true);

        TableColumn column = this.passwordTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(column.getWidth() - 90);

        column = this.passwordTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(column.getWidth() - 90);

        this.view.setMinimumSize(new Dimension(preferedSize, (buttonHeight*5) - 20));
        this.view.setPreferredSize(new Dimension(preferedSize, (buttonHeight*5) - 20));
        this.view.setMaximumSize(new Dimension(preferedSize, (buttonHeight*5) - 20));
        
        SearchEventHandler searchHandler = new SearchEventHandler(this, passwordTable, database, portalFrame, this.applicationsTrie);

        JTextField search = new JTextField(preferedSize);

        search.setText("Type the website or application you wish to look for.");
        search.setActionCommand("search");
        search.addActionListener(searchHandler);
        
        search.setMinimumSize(new Dimension(preferedSize, 20));
        search.setPreferredSize(new Dimension(preferedSize, 20));
        search.setMaximumSize(new Dimension(preferedSize, 20));

        JPanel containerColorRigidVertical = new JPanel();
        containerColorRigidVertical.setBackground(this.transparentColor);
        containerColorRigidVertical.add(Box.createRigidArea(new Dimension(0, 5)));
        this.setAbsoluteSize(containerColorRigidVertical, new Dimension(0, 5));

        JPanel containerColorGlue = new JPanel();
        containerColorGlue.setBackground(this.transparentColor);
        containerColorGlue.add(Box.createGlue());

        content.add(search);
        content.add(containerColorRigidVertical);
        content.add(Box.createGlue());
        content.add(view);

        this.setAbsoluteSize(content, new Dimension(preferedSize, (buttonHeight*6) - 10));
        content.setBorder(new RoundedBorder(10, this.containerColor, this.frameColor));

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

        background.add(coloredBorderPane);

        contentPane.add(background);

        portalFrame.pack();
        portalFrame.setVisible(true);
                
        portalFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.fillTable(portalFrame);
    }

    public String getUsername(){
        return this.database.getUsername();
    }

    public Password getPassword(){
        return this.database.getPassword();
    }

    public void setUsername(String username){
        this.database.setUsername(username);
    }

    public void setPassword(Password password){
        this.database.setPassword(password);
    }

    public void loadScreenInstance(){
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame portalFrame = new JFrame("Orchid - Load");
        portalFrame.setPreferredSize(new Dimension(this.x_dimension, this.y_dimension));
        portalFrame.setLocation(this.x_location, this.y_location);
        portalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        portalFrame.setResizable(false);

        Container contentPane = portalFrame.getContentPane();
        contentPane.setBackground(this.frameColor);

        JBPanel background = new JBPanel();

        try {
            background = new JBPanel("bc74860b1841a4dfe5aa9e3ea2571e36.jpg", this.x_dimension, this.y_dimension);
            background.setLayout(new BorderLayout());
        } catch (IOException e) {
            System.out.print("Bad image load.");
        }

        JPanel coloredBorderPane = new TransparentPanel();
        coloredBorderPane.setLayout(new BoxLayout(coloredBorderPane, BoxLayout.PAGE_AXIS));
        coloredBorderPane.setBackground(contentPane.getBackground());


        background.add(coloredBorderPane);

        contentPane.add(background);

        portalFrame.pack();
        portalFrame.setVisible(true);
    }
}

/**
 * Right Click:
 * - Allows password regeneration
 * - Allows copying of records
 * - 
 */