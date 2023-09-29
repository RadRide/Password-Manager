package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class SideButton extends JButton implements MouseListener {

    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);
    Font font = new Font("Arial", Font.PLAIN, 15);

    ImageIcon icon;

    public SideButton(String name, String path){
        // Control panel Constructor
        icon = new ImageIcon(getClass().getClassLoader().getResource(path));

        setText(name);
        setPreferredSize(new Dimension(150, 30));
        setFocusable(false);
        setForeground(Color.black);
        setBackground(lightBlueColor);
        setBorder(BorderFactory.createEmptyBorder());
        setFont(font);
        setIcon(icon);
        setHorizontalAlignment(JButton.LEFT);

        addMouseListener(this);
    }

    public SideButton(){
        // Logout Constructor
        icon = new ImageIcon(getClass().getClassLoader().getResource("icons8-settings-16.png"));

        setText("Settings");
        setBorder(BorderFactory.createEmptyBorder());
        setBounds(30, 450, 150, 30);
        setFocusable(false);
        setForeground(Color.white);
        icon = colorizeImageIcon(icon, Color.white);
        setBackground(lightBlueColor);
        setFont(font);
        setIcon(icon);
        setHorizontalAlignment(JButton.LEFT);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                setForeground(Color.red);
                icon = colorizeImageIcon(icon, Color.red);
                setIcon(icon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setForeground(Color.red);
                icon = colorizeImageIcon(icon, Color.red);
                setIcon(icon);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.red);
                icon = colorizeImageIcon(icon, Color.red);
                setIcon(icon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.white);
                icon = colorizeImageIcon(icon, Color.white);
                setIcon(icon);
            }
        });
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
        setForeground(Color.black);
        icon = colorizeImageIcon(icon, Color.black);
        setIcon(icon);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setForeground(Color.white);
        icon = colorizeImageIcon(icon, Color.white);
        setIcon(icon);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setForeground(Color.white);
        icon = colorizeImageIcon(icon, Color.white);
        setIcon(icon);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setForeground(Color.black);
        icon = colorizeImageIcon(icon, Color.black);
        setIcon(icon);
    }
}
