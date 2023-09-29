package application;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class TablePanel extends JPanel {

    final String[] tableHead = {"Site", "Email", "Password"};
    Table table;
    JScrollPane scrollPane;
    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);
    Font font = new Font("Arial", Font.PLAIN, 15);

    public TablePanel(String userEmail){

        table = new Table(userEmail);
//        table.setEnabled(false);

        // Without a JScrollPane, JTable will not display its header.
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(table.getPreferredSize());
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(0);
//        setScrollBar();

        setPreferredSize(new Dimension(650, 550));
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        add(scrollPane);

    }

    public void setScrollBar() {
        // calculate the preferred size of the table
        int tableHeight = table.getRowCount() * table.getRowHeight();
        if (table.getTableHeader() != null) {
            tableHeight += table.getTableHeader().getPreferredSize().height;
        }
        Dimension tableSize = new Dimension(scrollPane.getPreferredSize().width, table.getRowHeight() * table.getRowCount());

        // set the preferred size of the scroll pane
        scrollPane.setPreferredSize(tableSize);

        // enable or disable the vertical scroll bar
        if (tableHeight > scrollPane.getViewport().getHeight()) {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        } else {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }

    }
}
