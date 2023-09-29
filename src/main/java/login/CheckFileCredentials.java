package login;

import java.sql.*;

public class CheckFileCredentials {

    public static boolean checkCredentials(String email, String password){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM user";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String dbEmail = resultSet.getString("email");
                String dbPassword = resultSet.getString("password");
                if(email.equals(dbEmail) && password.equals(dbPassword)){
                    return true;
                }
            }
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;

    }

}
