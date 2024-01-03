package com.password_db.handlers;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.password_db.databases.Database;
import com.password_db.databases.DatabaseTaskManager;
import com.password_db.databases.TaskManager;

public class SearchEventHandler implements ActionListener{

    private JTable passwordTable;
    private Database database;
    private JFrame frame;

    public SearchEventHandler(JTable passwordTable, Database database, JFrame frame){
        this.database = database;
        this.frame = frame;
        this.passwordTable = passwordTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("search")){
            TableModel model = this.passwordTable.getModel();
            for(int i = 0; i < 3; i++){
                if(model.getColumnName(i).equals("Application")){
                    if(model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Please wait until the password table has completely loaded.", "Error!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        this.searchTable(((JTextField)e.getSource()).getText());
                    }
                    return;
                }
            }
        }
    }

    private void searchTable(String application) {
        DefaultTableModel model = (DefaultTableModel) this.passwordTable.getModel();
        model.setRowCount(0);

        DatabaseTaskManager taskManager = new DatabaseTaskManager(database);
        taskManager.setChoice(TaskManager.PULL_PASSWORD);
        taskManager.setParameters(new Object[]{this.passwordTable, this.frame, application});
        taskManager.execute();
    }
}