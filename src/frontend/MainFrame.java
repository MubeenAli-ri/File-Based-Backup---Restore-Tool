// src/frontend/MainFrame.java
package frontend;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private BackupPanel backupPanel;
    private RestorePanel restorePanel;
    
    public MainFrame() {
        setAppIcon();
        initUI();
        setupFrame();
        setupTabListener();
    }
    
    private void setAppIcon() {
        try {
            // Set a custom icon (you can add a .png file to your project)
            // ImageIcon icon = new ImageIcon("icon.png");
            // setIconImage(icon.getImage());
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        } catch (Exception e) {
            // No icon found, continue without icon
        }
    }
    
    private void initUI() {
        setTitle("File Backup & Restore Tool v1.0");
        
        // Create custom colored tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(240, 248, 255));
        
        backupPanel = new BackupPanel();
        restorePanel = new RestorePanel();
        
        // Add tabs with icons (emojis work as simple icons)
        tabbedPane.addTab("Create Backup", backupPanel);
        tabbedPane.addTab("Restore & Manage", restorePanel);
        
        // Set tab colors
        tabbedPane.setBackgroundAt(0, new Color(100, 149, 237));
        tabbedPane.setBackgroundAt(1, new Color(60, 179, 113));
        
        add(tabbedPane);
    }
    
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Add status bar
        JLabel statusBar = new JLabel(" Ready | Database: Connected | Backup Storage: Active");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusBar.setForeground(new Color(100, 100, 100));
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private void setupTabListener() {
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                restorePanel.refreshBackupTable();
            }
        });
    }
    
    public static void main(String[] args) {
        try {
            // Set modern Look and Feel
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            
            // Customize Nimbus colors
            UIManager.put("nimbusBase", new Color(50, 150, 200));
            UIManager.put("nimbusBlueGrey", new Color(200, 220, 240));
            UIManager.put("control", new Color(240, 245, 250));
            
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}