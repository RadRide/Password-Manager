package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextInput extends JTextField implements FocusListener{

    String text;
    Font font = new Font("Arial", Font.PLAIN, 15);
    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);

    public TextInput(String text){

        this.text = text;
        setText(this.text);
        setColumns(15);
        setFont(font);// Removes placeholder text after selecting the email.
        addFocusListener(this);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, darkBlueColor));

    }

    @Override
    public void focusGained(FocusEvent e) {
            if (getText().equals(text)) {
                setText("");
            }
            setForeground(lightBlueColor);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightBlueColor));
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (getText().isEmpty()) {
            setText(text);
        }
        setForeground(darkBlueColor);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, darkBlueColor));
    }
    public void changeText(String text){
        this.text = text;
    }
}
