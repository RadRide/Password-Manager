package application;

import login.LoginFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApplicationFrame extends JFrame implements ActionListener {

    SidePanel sidePanel;
    TablePanel tablePanel;
    JButton addButton, deleteButton, editButton, searchButton, refreshButton, settingsButton;
    JPopupMenu settingMenu;
    MenuItem logout, changePass;
    private String userEmail;
    private ImageIcon pmIcon = new ImageIcon(getClass().getClassLoader().getResource("password-manager-icon.png"));

    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);

    public ApplicationFrame(String userEmail){

        this.userEmail = userEmail;

        sidePanel = new SidePanel(userEmail);

        tablePanel = new TablePanel(userEmail);

        setButtons();

        setSettings();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setTitle("Password Manager");
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        setIconImage(pmIcon.getImage());

        add(sidePanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.EAST);

        setVisible(true);
        setLocationRelativeTo(null);

        enableControls();

    }

    public void setButtons(){
        addButton = sidePanel.addButton;
        addButton.addActionListener(this);

        deleteButton = sidePanel.deleteButton;
        deleteButton.addActionListener(this);

        editButton = sidePanel.editButton;
        editButton.addActionListener(this);

        searchButton = sidePanel.searchButton;
        searchButton.addActionListener(this);

        refreshButton = sidePanel.refreshButton;
        refreshButton.addActionListener(this);

        settingsButton = sidePanel.settingsButton;
        settingsButton.addActionListener(this);
    }

    public void setSettings(){
        settingMenu = new JPopupMenu();
        settingMenu.setBackground(darkBlueColor);

        logout = new MenuItem("Logout", "icons8-shutdown-16.png");
        logout.addActionListener(this);

        changePass = new MenuItem("Change Master Password", "icons8-password-key-16.png");
        changePass.addActionListener(this);

        settingMenu.add(changePass);
        settingMenu.add(logout);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addButton){
            new ControlPanel(userEmail, 1);
            tablePanel.table.refresh();
//            tablePanel.setScrollBar();
            tablePanel.scrollPane.revalidate();
        }
        if(e.getSource() == deleteButton){
            getSelectedRow(2);
            tablePanel.table.refresh();
//            tablePanel.setScrollBar();
            tablePanel.scrollPane.revalidate();
        }
        if(e.getSource() == editButton){
            getSelectedRow(3);
            tablePanel.table.refresh();
        }
        if(e.getSource() == searchButton){
            new ControlPanel(userEmail, tablePanel.table.tableModel);
            tablePanel.table.setModel(tablePanel.table.tableModel);
        }
        if(e.getSource() == refreshButton){
            tablePanel.table.refresh();
        }
        if(e.getSource() == settingsButton){
            settingMenu.show(settingsButton, 0, -50);
        }
        if(e.getSource() == logout){
            new LoginFrame();
            deleteRemember();
            this.dispose();
        }
        if(e.getSource() == changePass){
            new ControlPanel(userEmail, 5);
        }
    }

    public void enableControls(){
        // Disables the buttons at start until a row has been selected
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);

        // Enables the buttons after the row was selected.
        tablePanel.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tablePanel.table.getSelectedRow() == -1){
                    deleteButton.setEnabled(false);
                    editButton.setEnabled(false);
                }
                else {
                    deleteButton.setEnabled(true);
                    editButton.setEnabled(true);
                }
            }
        });
    }

    public void getSelectedRow(int option){
        int row = tablePanel.table.getSelectedRow();
        Object url = tablePanel.table.getValueAt(row, 0);
        Object email = tablePanel.table.getValueAt(row, 1);
        Object pass = tablePanel.table.getValueAt(row, 2);
        new ControlPanel(userEmail, (String) url, (String) email, (String) pass, option);
    }

    public void deleteRemember(){
        try {
            Files.deleteIfExists(Paths.get("Data.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
