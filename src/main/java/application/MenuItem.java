package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MenuItem extends JMenuItem implements MouseListener {

    ImageIcon icon;
    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);
    Font font = new Font("Arial", Font.PLAIN, 15);

    public MenuItem(String text, String path){

        icon = new ImageIcon(getClass().getClassLoader().getResource(path));
        icon = colorizeImageIcon(icon, Color.white);

        setText(text);
        setIcon(icon);
        setFont(font);
        setBackground(darkBlueColor);
        setForeground(Color.white);
        addMouseListener(this);


    }

    public static ImageIcon colorizeImageIcon(ImageIcon original, Color color) {
        BufferedImage image = new BufferedImage(original.getIconWidth(), original.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        original.paintIcon(null, g2d, 0, 0);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, original.getIconWidth(), original.getIconHeight());
        g2d.dispose();
        return new ImageIcon(image);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        icon = colorizeImageIcon(icon, Color.red);
        setIcon(icon);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        icon = colorizeImageIcon(icon, Color.red);
        setIcon(icon);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        icon = colorizeImageIcon(icon, Color.red);
        setIcon(icon);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        icon = colorizeImageIcon(icon, Color.white);
        setIcon(icon);
    }

}
