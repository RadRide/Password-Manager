package application;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CellRender extends DefaultTableCellRenderer {

    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);
    Font font = new Font("Arial", Font.PLAIN, 15);
    private final Border border = BorderFactory.createMatteBorder(0, 0, 5, 0, darkBlueColor);

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ((JComponent) c).setBorder(border);
        setHorizontalAlignment(JLabel.CENTER);
        return c;
    }

}
