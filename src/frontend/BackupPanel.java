// src/frontend/BackupPanel.java - Clean version with visible button
package frontend;

import backend.service.BackupService;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BackupPanel extends JPanel {
    private JTextField selectedFilePathField;
    private JButton selectFileButton;
    private JButton backupButton;
    private JLabel statusLabel;
    private File selectedFile;
    private BackupService backupService;
    private JProgressBar progressBar;
    
    public BackupPanel() {
        backupService = new BackupService();
        initUI();
        setupLayout();
        addEventListeners();
    }
    
    private void initUI() {
        setBackground(new Color(245, 250, 255));
        
        selectedFilePathField = new JTextField(40);
        selectedFilePathField.setEditable(false);
        selectedFilePathField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        selectFileButton = new JButton("Select File/Folder");
        selectFileButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        selectFileButton.setBackground(new Color(70, 130, 200));
        selectFileButton.setForeground(Color.WHITE);
        selectFileButton.setFocusPainted(false);
        selectFileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backupButton = new JButton("CREATE BACKUP");
        backupButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backupButton.setBackground(new Color(60, 179, 113));
        backupButton.setForeground(Color.WHITE);
        backupButton.setEnabled(false);
        backupButton.setFocusPainted(false);
        backupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backupButton.setPreferredSize(new Dimension(300, 60));
        
        statusLabel = new JLabel("Select a file/folder to begin", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setForeground(new Color(60, 179, 113));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 250, 255));
        
        JLabel titleLabel = new JLabel("File Backup Tool", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 150));
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Selection row
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Selected File:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        centerPanel.add(selectedFilePathField, gbc);
        
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        centerPanel.add(selectFileButton, gbc);
        
        // Progress bar
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(progressBar, gbc);
        
        // Backup button
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(backupButton, gbc);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 250, 255));
        bottomPanel.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addEventListeners() {
        selectFileButton.addActionListener(e -> selectFileOrFolder());
        backupButton.addActionListener(e -> performBackup());
        
        // Hover effects
        selectFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                selectFileButton.setBackground(new Color(70, 130, 200).darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                selectFileButton.setBackground(new Color(70, 130, 200));
            }
        });
        
        backupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (backupButton.isEnabled()) {
                    backupButton.setBackground(new Color(60, 179, 113).darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (backupButton.isEnabled()) {
                    backupButton.setBackground(new Color(60, 179, 113));
                }
            }
        });
    }
    
    private void selectFileOrFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogTitle("Select File or Folder to Backup");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            selectedFilePathField.setText(selectedFile.getAbsolutePath());
            backupButton.setEnabled(true);
            statusLabel.setText("Ready to backup: " + selectedFile.getName());
            statusLabel.setForeground(new Color(60, 179, 113));
        }
    }
    
    private void performBackup() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a file or folder first!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        backupButton.setEnabled(false);
        backupButton.setText("⏳ BACKING UP...");
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return backupService.createBackup(selectedFile);
            }
            
            @Override
            protected void done() {
                progressBar.setVisible(false);
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(BackupPanel.this, 
                            "Backup created successfully!\n\nFile: " + selectedFile.getName() + 
                            "\nLocation: backup_storage/", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        statusLabel.setText("Backup completed: " + selectedFile.getName());
                        statusLabel.setForeground(new Color(60, 179, 113));
                        
                        selectedFilePathField.setText("");
                        selectedFile = null;
                        backupButton.setEnabled(false);
                        backupButton.setText("CREATE BACKUP");
                    } else {
                        JOptionPane.showMessageDialog(BackupPanel.this, 
                            "Backup failed!\nCheck the log file for details.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        statusLabel.setText("Backup failed!");
                        statusLabel.setForeground(Color.RED);
                        backupButton.setEnabled(true);
                        backupButton.setText("CREATE BACKUP");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(BackupPanel.this, 
                        "Error: " + e.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    statusLabel.setText("Error: " + e.getMessage());
                    statusLabel.setForeground(Color.RED);
                    backupButton.setEnabled(true);
                    backupButton.setText("CREATE BACKUP");
                }
            }
        };
        
        worker.execute();
    }
}