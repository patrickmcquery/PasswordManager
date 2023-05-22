import java.awt.*;
import javax.swing.*;

public class PasswordManagerGUI {
    private Color darkGray = new Color(45, 45, 45);
    private Color lightGray = new Color(185, 185, 185);
    private Color black = new Color(0, 0, 0);

    PasswordManagerGUI() {
        JFrame window = new JFrame("Patrick's Unsecured Password Manager");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1200, 800);
        window.setResizable(false);
        window.setLocationRelativeTo(null);


        // JMenuBar menu = new JMenuBar();
        // JMenu file = new JMenu("File");
        // JMenu about = new JMenu("About");
        // menu.add(file);
        // menu.add(about);
        // JMenuItem importVault = new JMenuItem("Import vault");
        // JMenuItem exportVault = new JMenuItem("Export vault");
        // file.add(importVault);
        // file.add(exportVault);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(black);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(darkGray);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(190, 800));


        JButton add = new JButton("Add Entry");
        add.setAlignmentX(Component.CENTER_ALIGNMENT);
        add.addActionListener(null);
        leftPanel.add(add);

        JButton searchApp = new JButton("Search by Site/App");
        searchApp.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(searchApp);

        JButton exportVault = new JButton("Export Vault");
        exportVault.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(exportVault);

        JButton importVault = new JButton("Import Vault");
        importVault.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(importVault);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(darkGray);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(500, 800));

        JTextField test = new JTextField("Account information:");
        test.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(test);

        // window.getContentPane().add(BorderLayout.PAGE_START, menu);
        window.getContentPane().add(BorderLayout.LINE_START, leftPanel);
        window.getContentPane().add(BorderLayout.CENTER, mainPanel);
        window.getContentPane().add(BorderLayout.LINE_END, rightPanel);
        window.setVisible(true);
    }
}
