package login;

import application.PasswordInput;
import application.TextInput;
import encryption.Encryption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPanel extends JPanel implements ActionListener {

    JLabel welcomeLabel, messageLabel;
    TextInput email;
    PasswordInput password;
    JButton login, createAccount;
    JCheckBox rememberMe;
    GridBagConstraints gbc;
    Font font = new Font("Arial", Font.PLAIN, 15);
    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);

    Connection connection;
    Statement statement;
    ResultSet resultSet;
    LoginFrame frame;
    private String userEmail, userPassword;
    private Encryption encryption;

    public LoginPanel(){

        // Labels
        initiateWelcome();
        initiateMessage();

        // Text/Password Fields
        initiateInputs();

        // Buttons
        initiateLogin();
        initiateCreateAccount();

        // CheckBox
        initiateRememberMe();

        // Panel
        initiatePanel();

        // Adding the elements to the panel.
        placeElements();

        encryption = new Encryption();

    }

    public String getUserEmail() {
        return userEmail;
    }
    public String getUserPassword(){return userPassword;}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == login){
            // Checking if email structure is valid
            if(!isEmailValid()){
                invalidEmail();
            }
            else {
                try {
                    if(checkCredentials()){
                    }
                    else {
                        invalidCredentials();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if(e.getSource() == createAccount){
            NewAccountPanel accountPanel = new NewAccountPanel();
        }
    }

    public boolean checkCredentials() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                "root","root");
        statement = connection.createStatement();
        String query = "SELECT * FROM user";
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            String dbEmail = resultSet.getString("email");
            String dbPassword = resultSet.getString("password");
            if(email.getText().equals(encryption.decrypt(dbEmail)) && password.getText().equals(encryption.decrypt(dbPassword))){
                userEmail = dbEmail;
                userPassword = dbPassword;
                return true;
            }
        }
        statement.close();

        return false;
    }
    public void invalidCredentials(){
        email.setForeground(redColor);
        email.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        password.setForeground(redColor);
        password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        messageLabel.setText("Email Or Password Not Valid");
        messageLabel.setForeground(redColor);
    }

    public boolean isEmailValid(){
        String pattern = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
        return email.getText().matches(pattern);
    }
    public void invalidEmail(){
        email.setForeground(redColor);
        email.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        messageLabel.setText("Enter A Valid Email");
        messageLabel.setForeground(redColor);
    }
    public void initiatePanel(){
        setBackground(Color.white);
        setPreferredSize(new Dimension(300, 300));
        setMaximumSize(new Dimension(300, 300));
        setLayout(new GridBagLayout());
    }
    public void initiateWelcome(){
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeLabel.setText("WELCOME");
        welcomeLabel.setForeground(redColor);
    }
    public void initiateMessage(){
        messageLabel = new JLabel();
        messageLabel.setFont(font.deriveFont(Font.BOLD, 14));
        messageLabel.setText("Enter Login Info To Continue");
        messageLabel.setForeground(darkBlueColor);
    }
    public void initiateInputs(){
        email = new TextInput("Email");
        password = new PasswordInput("Password");
    }
    public void initiateLogin(){
        login = new JButton("Log In");
        login.setFont(font);
        login.setMargin(new Insets(1, 73, 1 ,73));
        login.setBackground(Color.white);
        login.setForeground(darkBlueColor);
        login.setFocusable(false);
        login.addActionListener(this);
    }
    public void initiateCreateAccount(){
        createAccount = new JButton("Create New Account");
        createAccount.setFont(font);
        createAccount.setBackground(Color.white);
        createAccount.setMargin(new Insets(1, 27, 1 ,27));
        createAccount.setFocusable(false);
        createAccount.setForeground(darkBlueColor);
        createAccount.addActionListener(this);
    }
    public void initiateRememberMe(){
        rememberMe = new JCheckBox();
        rememberMe.setText("Remember Me");
        rememberMe.setFont(font);
        rememberMe.setBackground(Color.white);
        rememberMe.setForeground(darkBlueColor);
        rememberMe.setFocusable(false);
    }
    public void placeElements(){
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 1, 5, 5);
        add(welcomeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(messageLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(email,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(password,gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        add(rememberMe,gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(login,gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(createAccount,gbc);
    }

}
