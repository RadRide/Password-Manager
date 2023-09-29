package encryption;

import java.util.ArrayList;
import java.util.Random;

public class Encryption {

    private String shuffledChars = "7Y^S19,:|U0>A_[- ;sg4FLf]?c@at6<&ZmyoTRk82N~phP=/eVQDi3).!(XOdwW+J*z$Mj{ru#xl%CqGIKH`nEBb}5v";
    private String normalChars = " !#$%&()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private ArrayList<Character> normal = new ArrayList<>(), shuffled = new ArrayList<>();

    public Encryption(){
        assignLists();
    }

    public String encrypt(String s){
        String encrypted = "", salt  = "";
        int x, index, temp = 100;
        Random rand = new Random();

        for(int i = 0; i < 30; i++){
            x = rand.nextInt(92);
            if(x == temp) {
                i--;
            }else {
                salt += shuffled.get(x);
            }
            temp = x;
        }
        encrypted += salt;

        for(int i = 0; i < s.length(); i++){
            index = ((int)(salt.charAt(i % salt.length())) + shuffled.indexOf(s.charAt(i))) % shuffled.size();
            encrypted += shuffled.get(index);
        }

        return encrypted;
    }

    public String decrypt(String s){
        String salt = s.substring(0, 30);
        String decrypted = "";

        for(int i = 30; i < s.length(); i++){
            int index = (shuffled.indexOf(s.charAt(i)) - (int)(salt.charAt((i - 30) % salt.length()))) % shuffled.size();
            if(index < 0){
                index += shuffled.size();
            }
            decrypted += shuffled.get(index);
        }

        return decrypted;
    }

    public void assignLists(){
        for(char c : normalChars.toCharArray()){
            normal.add(c);
        }
        for(char c : shuffledChars.toCharArray()){
            shuffled.add(c);
        }
    }

}
