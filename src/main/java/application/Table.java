package application;

import encryption.Encryption;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Table extends JTable {

    final String[] tableHead = {"Site", "Email/Username", "Password"};
    DefaultTableModel tableModel;
    String userEmail;

    Color darkBlueColor = new Color(40, 53, 74),
            redColor = new Color(238, 71, 78),
            lightBlueColor = new Color(42, 99, 127);
    Font font = new Font("Arial", Font.PLAIN, 15);

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private Encryption encryption;

    public Table(String userEmail){

        encryption = new Encryption();

        tableModel = new DefaultTableModel(tableHead, 0);

        setFont(font);
        setPreferredSize(new Dimension(600,500));
        setModel(tableModel);
        getColumnModel().setColumnMargin(0);
        setBorder(null);
        setGridColor(Color.white);
        setRowHeight(50);

        // Changes the default cells to our custom ones
        setDefaultRenderer(Object.class, new CellRender());

        JTableHeader header = this.getTableHeader();
        header.setFont(font);
        header.setBackground(Color.white);
//        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, redColor));
        header.setBorder(BorderFactory.createEmptyBorder());
        // Changes the default render of the header
        TableCellRenderer headerRenderer = header.getDefaultRenderer();
        // Centers the text in the header
        if (headerRenderer instanceof JLabel) {
            ((JLabel) headerRenderer).setHorizontalAlignment(JLabel.CENTER);
            ((JLabel) headerRenderer).setOpaque(true);
        }

        setDefaultEditor(Object.class, null);
        setRowSelectionAllowed(true);

        addCopy();
        this.userEmail = userEmail;
        refresh();

    }

    public void refresh(){
        tableModel.setRowCount(0);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            statement = connection.createStatement();
            String query = "SELECT * FROM credential WHERE user_email = '" + userEmail + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String url = encryption.decrypt(resultSet.getString("site_url"));
                String username = encryption.decrypt(resultSet.getString("site_email"));
                String password = encryption.decrypt(resultSet.getString("site_password"));
                tableModel.addRow(new String[]{url, username, password});
            }
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setUserEmail(String s){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/password-manager-db",
                    "root","root");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT email FROM user;");
            while (resultSet.next()){
                String dbEmail = resultSet.getString("email");
                if(userEmail.equals(encryption.decrypt(dbEmail))){
                    userEmail = dbEmail;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addCopy(){
        // Create popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copyPasswordItem = new JMenuItem("Copy Password");
        JMenuItem copyURL = new JMenuItem("Copy URL");
        JMenuItem copyUsername = new JMenuItem("Copy Username");
        copyPasswordItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row and column index
                int rowIndex = getSelectedRow();
                String password = (String) getValueAt(rowIndex, 2);

                // Copy password to clipboard
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(password);
                clipboard.setContents(selection, selection);
            }
        });
        copyURL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row and column index
                int rowIndex = getSelectedRow();
                String URL = (String) getValueAt(rowIndex, 0);

                // Copy URL to clipboard
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(URL);
                clipboard.setContents(selection, selection);
            }
        });
        copyUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row and column index
                int rowIndex = getSelectedRow();
                String username = (String) getValueAt(rowIndex, 1);

                // Copy URL to clipboard
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(username);
                clipboard.setContents(selection, selection);
            }
        });
        popupMenu.add(copyURL);
        popupMenu.add(copyUsername);
        popupMenu.add(copyPasswordItem);

        // Attach popup menu to the table
        setComponentPopupMenu(popupMenu);

        // Attach mouse listener to show popup menu on right-click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = rowAtPoint(e.getPoint());
                    setRowSelectionInterval(row, row);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

}
