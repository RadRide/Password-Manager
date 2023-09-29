package application;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {

    JPanel buttonPanel;
    SideButton addButton, deleteButton, searchButton, editButton, refreshButton, settingsButton;
    String userEmail;
    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);

    public SidePanel(String userEmail){

        buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 0 , 200, 300);
        buttonPanel.setBackground(lightBlueColor);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        addButton = new SideButton("Add Account", "icons8-plus-16.png");
        deleteButton = new SideButton("Delete Account", "icons8-trash-16.png");
        searchButton = new SideButton("Search", "icons8-search-16.png");
        editButton = new SideButton("Edit Account", "icons8-edit-16.png");
        refreshButton = new SideButton("Refresh", "icons8-refresh-16.png");

        settingsButton = new SideButton();

        setPreferredSize(new Dimension(200, 550));
        setBackground(lightBlueColor);
        setLayout(null);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);


        add(buttonPanel);
        add(settingsButton);

        this.userEmail = userEmail;

    }

}
