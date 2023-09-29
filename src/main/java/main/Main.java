package main;

import application.ApplicationFrame;
import encryption.Encryption;
import login.CheckFileCredentials;
import login.LoginFrame;

import java.io.*;

public class Main {
    public static void main(String[] args) {

        Encryption encryption = new Encryption();

        File file = new File("Data.txt");
        if(file.exists()){
            String email = "", pass = "";
            try{
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);

                email = bufferedReader.readLine();
                pass = bufferedReader.readLine();
                reader.close();
                bufferedReader.close();
            }catch (FileNotFoundException exception){
                exception.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            if(CheckFileCredentials.checkCredentials(email, pass)){
                new ApplicationFrame(email);
            }else{
                new LoginFrame();
            }
        }else {
            new LoginFrame();
        }

//        new LoginFrame();
//        new ApplicationFrame("user1@hotmail.com");
    }
}