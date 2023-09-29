package login;

import application.PasswordInput;
import application.TextInput;
import encryption.Encryption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;

public class NewAccountPanel extends JPanel {

    TextInput email;
    PasswordInput password, confirmPassword;
    JLabel messageLabel;
    Font font = new Font("Arial", Font.PLAIN, 15);
    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);
    int result;
    Connection connection;
    PreparedStatement statement;
    ResultSet resultSet;
    private Encryption encryption;

    public NewAccountPanel(){

        encryption = new Encryption();

        // Sets Panel elements
        setElements();
        // Sets option pane and checks in the account is valid.
        setOptionPane();

    }

    public void setInputFields(){
        email = new TextInput("Email");
        password = new PasswordInput("Password");
        confirmPassword = new PasswordInput("Password");
    }

    public void setElements(){
        messageLabel = new JLabel();
        messageLabel.setFont(font.deriveFont(Font.BOLD, 14));
        messageLabel.setText("Enter Login Info To Continue");
        messageLabel.setForeground(redColor);

        setInputFields();

        setBackground(Color.white);
        setPreferredSize(new Dimension(300, 200));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

        add(messageLabel);
        add(email);
        add(password);
        add(confirmPassword);
    }

    public void setOptionPane(){
        result = JOptionPane.showConfirmDialog(null, this, "Create new Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION) {
            boolean check = false;
            while (!check && result != JOptionPane.CANCEL_OPTION && result != JOptionPane.CLOSED_OPTION){
                try {
                    check = checkNewAccount();
                    if(check){
                        addAccount();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkEmail(){
        String pattern = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
        return email.getText().matches(pattern);
    }
    public boolean checkPassword(){
        String pass = new String(password.getPassword());
        String conf = new String(confirmPassword.getPassword());
        return pass.equals(conf);
    }
    public boolean checkNewAccount() throws SQLException {
        if(!(checkEmail()) && !(checkPassword())){
            wrongEmail();
            wrongPassword();
            messageLabel.setText("Email Not Valid\nPassword Incorrect");
            result = JOptionPane.showConfirmDialog(null, this, "Create new Account",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if(!(checkEmail())){
            wrongEmail();
            result = JOptionPane.showConfirmDialog(null, this, "Create new Account",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if(!(checkPassword())) {
            wrongPassword();
            result = JOptionPane.showConfirmDialog(null, this, "Create new Account",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        }else if(checkCredentials()){
            wrongEmail();
            wrongPassword();
            messageLabel.setText("Account Already Exists");
            result = JOptionPane.showConfirmDialog(null, this, "Create new Account",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    public void wrongEmail(){
        email.setForeground(redColor);
        email.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        messageLabel.setText("Email Not Valid");
        messageLabel.setForeground(redColor);
    }
    public void wrongPassword(){
        password.setForeground(redColor);
        password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        confirmPassword.setForeground(redColor);
        confirmPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        messageLabel.setText("Password Incorrect");
        messageLabel.setForeground(redColor);
    }

    public void addAccount(){
        String addEmail = encryption.encrypt(email.getText());
        String addPassword = encryption.encrypt(password.getText());
        try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                        "root","root");
                statement = connection.prepareStatement("INSERT INTO user VALUES (?,?)");
                statement.setString(1, addEmail);
                statement.setString(2, addPassword);
                statement.execute();
                statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkCredentials() throws SQLException {
        String enteredEmail = encryption.encrypt(email.getText());
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                "root","root");
        Statement stmt = connection.createStatement();
        String query = "SELECT user.email FROM user";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            String dbEmail = encryption.decrypt(rs.getString("email"));
            if(enteredEmail.equals(dbEmail)){
                return true;
            }
        }
        stmt.close();

        return false;
    }

}
