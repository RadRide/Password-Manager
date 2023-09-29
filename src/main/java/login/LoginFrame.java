package login;

import application.ApplicationFrame;
import encryption.Encryption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

public class LoginFrame extends JFrame implements ActionListener {

    LoginPanel loginPanel;
    ImageIcon imageIcon, pmIcon = new ImageIcon(getClass().getClassLoader().getResource("password-manager-icon.png"));
    JLabel backgroundLabel;
    JButton button;
    Encryption encryption;
    private String userEmail;

    public LoginFrame(){

        imageIcon = new ImageIcon(getClass().getClassLoader().getResource("Background Login.png"));

        backgroundLabel = new JLabel();
        backgroundLabel.setSize(900,550);
        backgroundLabel.setIcon(imageIcon);
        backgroundLabel.setOpaque(true);
        backgroundLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));

        loginPanel = new LoginPanel();

        // This button will check is the login was successful and closes the login page
        button = loginPanel.login;
        button.addActionListener(this);

        backgroundLabel.add(loginPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setTitle("Password Manager");
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        getContentPane().setBackground(new Color(80, 80, 80));
        setLocationRelativeTo(null); // Centers elements added in the frame
        setResizable(false);
        setIconImage(pmIcon.getImage());

        add(backgroundLabel, BorderLayout.CENTER);

        setVisible(true);
        setLocationRelativeTo(null);

        encryption = new Encryption();

    }

    public void remember(){
        // Checks if the file exists and writes to it.
        File save = new File("Data.txt");
        try{
            if(!save.exists()){
                save.createNewFile();
            }
            FileWriter writer = new FileWriter(save);
            writer.write(loginPanel.getUserEmail() + "\n");
            writer.write(loginPanel.getUserPassword());
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button){
            if(!loginPanel.isEmailValid()){
                loginPanel.invalidEmail();
            }
            else {
                try {
                    if(loginPanel.checkCredentials()){
                        if(loginPanel.rememberMe.isSelected()){
                            remember();
                        }
                        new ApplicationFrame(loginPanel.getUserEmail());
                        dispose();
                    }else {
                        loginPanel.invalidCredentials();
                    }
                }catch (SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
        }
    }
}
