package application;

import encryption.Encryption;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.sql.*;
import java.util.Random;

public class ControlPanel extends JPanel {

    TextInput email, siteURL, password;
    PasswordInput newPassword, confirmPassword;
    JLabel messageLabel;
    JComboBox comboBox;
    JButton passwordGen;
    Font font = new Font("Arial", Font.PLAIN, 15);
    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);
    int result;
    Connection connection;
    PreparedStatement statement;
    Statement stmt;
    ResultSet resultSet;
    private String userEmail, title, message;
    private int id, option, searchOption = 0;
    private Encryption encryption = new Encryption();

    public ControlPanel(String userEmail, int option){
        // Add Account/Change Password Constructor
        this.userEmail = userEmail;
        this.option = option; // To determine what this control panel is for (1 for add, 2 for delete, 3 for edit, 4 for search, 5 for change password).
        switch (option){
            case 1:
                setTitle(); // Sets the title of the new window and the message displayed.
                setAddPane();
                break;
            case 5:
                setTitle();
                setChangePasswordPane();
                break;
        }
    }

    public ControlPanel(String userEmail, DefaultTableModel tableModel){
        // Search Constructor
        this.userEmail = userEmail;
        this.option = 4;
        setSearchPane(tableModel);
    }

    public ControlPanel(String userEmail, String url, String username, String pass, int option){
        // Delete/Edit Constructor
        this.userEmail = userEmail;
        this.option = option;

        switch (option){
            case 2:
                result = JOptionPane.showConfirmDialog(null,
                        "Are You Sure You Want To Delete The following Account", "Delete Account",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    deleteAccount(url, username, pass);
                }
                break;

            case 3:
                setEditPane();

                siteURL.setText(url);
                email.setText(username);
                password.setText(pass);

                result = JOptionPane.showConfirmDialog(null, this, title,
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                    boolean check = false;
                    while (!check && result != JOptionPane.CANCEL_OPTION && result != JOptionPane.CLOSED_OPTION){
                        try {
                            check = checkNewAccount();
                            if(check){
                                editAccount(url, username, pass);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }

    }

    public void setAddPane(){
        setId();

        setPanelElements();

        setBackground(Color.white);
        setPreferredSize(new Dimension(300, 300));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

        add(messageLabel);
        add(siteURL);
        add(email);
        add(password);
        add(passwordGen);

        result = JOptionPane.showConfirmDialog(null, this, title,
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

    public void setEditPane(){
        setTitle();
        setPanelElements();

        setBackground(Color.white);
        setPreferredSize(new Dimension(300, 300));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

        add(messageLabel);
        add(siteURL);
        add(email);
        add(password);
        add(passwordGen);

    }

    public void setSearchPane(DefaultTableModel tableModel){
        setTitle();
        siteURL = new TextInput("URL");

        String[] type = {"URL", "Username", "Password"};
        comboBox = new JComboBox<>(type);
        comboBox.setSelectedItem(0);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == comboBox){
                    if(comboBox.getSelectedIndex() == 0){
                        searchOption = 0;
                    }
                    if(comboBox.getSelectedIndex() == 1){
                        searchOption = 1;
                    }
                    if(comboBox.getSelectedIndex() == 2){
                        searchOption = 2;
                    }
                    // Changes the text to be removed when pressing the TextField
                    siteURL.changeText(type[searchOption]);
                    // Changes the Placeholder.
                    siteURL.setText(type[searchOption]);
                }
            }
        });

        messageLabel = new JLabel();
        messageLabel.setFont(font.deriveFont(Font.BOLD, 14));
        messageLabel.setText(message);
        messageLabel.setForeground(lightBlueColor);

        setBackground(Color.white);
        setPreferredSize(new Dimension(300, 200));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

        add(messageLabel);
        add(comboBox);
        add(siteURL);

        result = JOptionPane.showConfirmDialog(null, this, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            search(tableModel);
        }
    }

    public void setPanelElements(){
        messageLabel = new JLabel();
        messageLabel.setFont(font.deriveFont(Font.BOLD, 14));
        messageLabel.setText(message);
        messageLabel.setForeground(lightBlueColor);

        email = new TextInput("Email");
        siteURL = new TextInput("URL");
        password = new TextInput("Password");

        passwordGen = new JButton("Generate Strong Password");
        passwordGen.setFocusable(false);
        passwordGen.setFont(font);
        passwordGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == passwordGen){
                    password.setText(generatePassword());
                }
            }
        });
    }

    public void setChangePasswordPane(){
        newPassword = new PasswordInput("password");

        confirmPassword = new PasswordInput("password");

        messageLabel = new JLabel();
        messageLabel.setFont(font.deriveFont(Font.BOLD, 14));
        messageLabel.setText(message);
        messageLabel.setForeground(lightBlueColor);

        setBackground(Color.white);
        setPreferredSize(new Dimension(300, 200));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

        add(messageLabel);
        add(newPassword);
        add(confirmPassword);

        result = JOptionPane.showConfirmDialog(null, this, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION) {
            String newPass = newPassword.getText();
            boolean check = false;
            while (!check && result != JOptionPane.CANCEL_OPTION && result != JOptionPane.CLOSED_OPTION){
                check = checkNewPassword();
                if(check){
                    result = JOptionPane.showConfirmDialog(null,
                            "Are You Sure You Want To Change the Master Password?", "Change Master Password",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        changePassword(newPass);
                    }
                }
            }
        }
    }

    public int getLastId(){
        int id = 0;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery("SELECT id FROM credential");
            while (resultSet.next()){
                int currentId = resultSet.getInt("id");
                if(id < currentId){
                    id = currentId;
                }
            }
            stmt.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return id + 1;
    }

    public void setId(){
        if(option == 1){
            id = getLastId();
        }
        if(option == 3){

        }
    }

    public void setTitle(){
        switch (option){
            case 1: title = "Add New Account"; message = "Enter New Account Info"; break;
            case 2: title = "Delete Account"; message = "Enter Account Info"; break;
            case 3: title = "Edit Account"; message = "Edit Account Info"; break;
            case 4: title = "Search Accounts"; message = "Search By: "; break;
            case 5: title = "Change Master Password"; message = "Enter New Password"; break;
        }
    }

    public boolean checkEmail(){
        String pattern = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
        return email.getText().matches(pattern);
    }

    public boolean checkPassword(){
        if(option == 5){
            if(newPassword.getText().indexOf("'") != -1){
                return false;
            }
        }
        else if(password.getText().indexOf("'") != -1){
            return false;
        }
        return true;
    }
    public boolean checkEmptyInputs(){
        return email.getText().equals("Email") || password.getText().equals("password") || siteURL.getText().equals("URL");
    }
    public boolean checkNewAccount() throws SQLException {
        if(checkEmptyInputs()){
            wrongEmail();
            wrongPassword();
            messageLabel.setText("Fields cannot be empty");
            result = JOptionPane.showConfirmDialog(null, this, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        if(checkCredentials()){
            wrongEmail();
            wrongPassword();
            messageLabel.setText("Account Already Exists");
            result = JOptionPane.showConfirmDialog(null, this, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!checkPassword()) {
            wrongPassword();
            messageLabel.setText("' Is An Invalid Character");
            result = JOptionPane.showConfirmDialog(null, this, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean checkNewPassword(){
        if(checkConfirmPass()){
            wrongPassword();
            result = JOptionPane.showConfirmDialog(null, this, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if(isOldPassword()){
            wrongPassword();
            messageLabel.setText("New Password Can't Be The Old Password");
            result = JOptionPane.showConfirmDialog(null, this, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        }else if(!checkPassword()){
            wrongPassword();
            messageLabel.setText("' Is An Invalid Character");
            result = JOptionPane.showConfirmDialog(null, this, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isOldPassword(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("SELECT password FROM user WHERE email = '" + userEmail + "';");
            while (resultSet.next()){
                String dbPass = encryption.decrypt(resultSet.getString("password"));
                if(newPassword.getText().equals(dbPass)){
                    return true;
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkConfirmPass(){
        if(newPassword.getText().equals("password") || confirmPassword.getText().equals("password")){
            return true;
        }
        else if(newPassword.getText().equals(confirmPassword.getText())){
            return false;
        }
        return true;
    }

    public void changePassword(String newPass){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            statement = connection.prepareStatement("UPDATE user SET password = ? WHERE email = ?");
            statement.setString(1, encryption.encrypt(newPass));
            statement.setString(2, userEmail);
            statement.execute();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void wrongEmail(){
        email.setForeground(redColor);
        email.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        messageLabel.setText("Email Not Valid");
        messageLabel.setForeground(redColor);
    }
    public void wrongPassword(){
        if(option == 5){
            newPassword.setForeground(redColor);
            newPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));

            confirmPassword.setForeground(redColor);
            confirmPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));

            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));

            messageLabel.setText("Password Incorrect");
            messageLabel.setForeground(redColor);
        }else{
            password.setForeground(redColor);
            password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));

            siteURL.setForeground(redColor);
            siteURL.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));

            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));

            messageLabel.setText("Password Incorrect");
            messageLabel.setForeground(redColor);
        }
    }

    public void addAccount(){
        String addURL = encryption.encrypt(siteURL.getText());
        String addEmail = encryption.encrypt(email.getText());
        String addPassword = encryption.encrypt(password.getText());
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            statement = connection.prepareStatement("INSERT INTO credential VALUES (?,?,?,?,?)");
            statement.setInt(1, id);
            statement.setString(2, addURL);
            statement.setString(3, addEmail);
            statement.setString(4, addPassword);
            statement.setString(5, userEmail);
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkCredentials() throws SQLException {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                "root","root");
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM credential WHERE user_email = '" + userEmail + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            String dbEmail = encryption.decrypt(rs.getString("site_email"));
            String bdURL = encryption.decrypt(rs.getString("site_url"));
            String dbPassword = encryption.decrypt(rs.getString("site_password"));
            if(email.getText().equals(dbEmail) && siteURL.getText().equals(bdURL)
                    && password.getText().equals(dbPassword)){
                return true;
            }
        }
        stmt.close();

        return false;
    }

    public void deleteAccount(String url, String username, String pass){
        String[] credentials = getSpecificCredential(url, username, pass);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            statement = connection.prepareStatement("DELETE FROM credential WHERE site_url = ? AND site_email = ? " +
                    "AND site_password = ? AND user_email = ?");
            statement.setString(1, credentials[0]);
            statement.setString(2, credentials[1]);
            statement.setString(3, credentials[2]);
            statement.setString(4, userEmail);
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getSpecificCredential(String url, String username, String password){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("SELECT * FROM credential WHERE user_email = '" + userEmail + "';");
            while (resultSet.next()){
                String dbEmail = resultSet.getString("site_email");
                String bdURL = resultSet.getString("site_url");
                String dbPassword = resultSet.getString("site_password");
                if(username.equals(encryption.decrypt(dbEmail)) && url.equals(encryption.decrypt(bdURL))
                        && password.equals(encryption.decrypt(dbPassword))){
                    return new String[]{bdURL, dbEmail, dbPassword};
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return new String[]{"","",""};
    }

    public void editAccount(String url, String username, String pass){

        String editURL = siteURL.getText();
        String editUsername = email.getText();
        String editPassword = password.getText();

        // Getting the id of the account we want to edit
        String[] credentials = getSpecificCredential(url, username, pass);
        int currentId = getSpecificId(credentials[0], credentials[1], credentials[2]);
        // Checking if all the inputs are empty.
        if(!(isInputEmpty())){
            // Updating each edited input.
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                        "root","root");
                if(editURL != url){
                    statement = connection.prepareStatement("UPDATE credential SET site_url = '" +
                            encryption.encrypt(editURL) + "' " +
                            "WHERE id = " + currentId + " ;");
                    statement.execute();
                    statement.close();
                }
                if(editUsername != username){
                    statement = connection.prepareStatement("UPDATE credential SET site_email = '" +
                            encryption.encrypt(editUsername) + "' " + "WHERE id = " + currentId + " ;");
                    statement.execute();
                    statement.close();
                }
                if(editPassword != pass){
                    statement = connection.prepareStatement("UPDATE credential SET site_password = '" +
                            encryption.encrypt(editPassword) + "' " + "WHERE id = " + currentId + " ;");
                    statement.execute();
                    statement.close();
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public int getSpecificId(String url, String username, String pass){
        int id = 0;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            Statement stmt = connection.createStatement();
            String query = "SELECT id FROM credential WHERE site_url = '" + url + "' " +
                    "AND site_email = '" + username + "' AND site_password = '" + pass + "' " +
                    "AND user_email = '" + userEmail + "'";
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()){
                id = resultSet.getInt("id");
            }
            stmt.close();
            connection.close();

        }catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }

    public boolean isInputEmpty(){
        return siteURL.getText() == "URL" && email.getText() == "Email" &&
                password.getText() == "Password";
    }

    public void search(DefaultTableModel tableModel){
        System.out.println(isSearchInputEmpty());
        if(!isSearchInputEmpty()){
            try {
                tableModel.setRowCount(0);
                String searchResult = siteURL.getText(), query, url, username, password;
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                        "root","root");
                stmt = connection.createStatement();
                switch (searchOption){
                    case 0:
                        query = "SELECT * FROM credential WHERE user_email = '" + userEmail + "';";
                        resultSet = stmt.executeQuery(query);
                        while (resultSet.next()){
                            url = encryption.decrypt(resultSet.getString("site_url"));
                            if(searchResult.equals(url)) {
                                username = encryption.decrypt(resultSet.getString("site_email"));
                                password = encryption.decrypt(resultSet.getString("site_password"));
                                tableModel.addRow(new String[]{url, username, password});
                            }
                        }
                        break;
                    case 1:
                        query = "SELECT * FROM credential WHERE user_email = '" + userEmail + "';";
                        resultSet = stmt.executeQuery(query);
                        while (resultSet.next()){
                            username = encryption.decrypt(resultSet.getString("site_email"));
                            if(searchResult.equals(username)) {
                                url = encryption.decrypt(resultSet.getString("site_url"));
                                password = encryption.decrypt(resultSet.getString("site_password"));
                                tableModel.addRow(new String[]{url, username, password});
                            }
                        }
                        break;
                    case 2:
                        query = "SELECT * FROM credential WHERE user_email = '" + userEmail + "';";
                        resultSet = stmt.executeQuery(query);
                        while (resultSet.next()){
                            password = encryption.decrypt(resultSet.getString("site_password"));
                            if(searchResult.equals(password)) {
                                username = encryption.decrypt(resultSet.getString("site_email"));
                                url = encryption.decrypt(resultSet.getString("site_url"));
                                tableModel.addRow(new String[]{url, username, password});
                            }
                        }
                        break;
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isSearchInputEmpty() {
        String text = siteURL.getText().trim();
        return text.isEmpty() || text.equals("URL") || text.equals("Email") || text.equals("Password");
    }

    public String generatePassword(){

        Random random = new Random();

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String symbol = "~`!@#$%^&*()_-+={[}]|:;<,>.?/";
        String generatedPass = "";
        int ran1 = random.nextInt(14, 20), ran2, ran3;
        for(int i = 0; i <= ran1; i++){
            ran2 = random.nextInt(5);
            switch (ran2){
                case 0, 1:
                    ran3 = random.nextInt(26);
                    generatedPass += upper.charAt(ran3); break;
                case 2, 3:
                    ran3 = random.nextInt(26);
                    generatedPass += lower.charAt(ran3); break;
                case 4:
                    ran3 = random.nextInt(29);
                    generatedPass += symbol.charAt(ran3); break;
            }
        }
        return generatedPass.trim();
    }

}
