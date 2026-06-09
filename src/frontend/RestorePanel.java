// src/frontend/RestorePanel.java
package frontend;

import backend.service.RestoreService;
import backend.model.BackupRecord;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class RestorePanel extends JPanel {
    private JTable backupTable;
    private DefaultTableModel tableModel;
    private JButton restoreButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private RestoreService restoreService;
    private JLabel statsLabel;
    
    public RestorePanel() {
        restoreService = new RestoreService();
        initUI();
        setupLayout();
        addEventListeners();
        loadBackupData();
    }
    
    private void initUI() {
        setBackground(new Color(245, 250, 255));
        
        String[] columns = {"ID", "File Name", "Original Path", "Backup Date", "File Size (MB)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        backupTable = new JTable(tableModel);
        backupTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backupTable.setRowHeight(30);
        backupTable.setSelectionBackground(new Color(100, 149, 237, 100));
        backupTable.setSelectionForeground(Color.BLACK);
        backupTable.setShowGrid(true);
        backupTable.setGridColor(new Color(230, 230, 230));
        
        // Set alternating row colors
        backupTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 255));
                }
                return c;
            }
        });
        
        // Custom header
        JTableHeader header = backupTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(70, 130, 200));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        // Set column widths
        backupTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        backupTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        backupTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        backupTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        backupTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Create prominent buttons
        restoreButton = createStyledButton("RESTORE SELECTED", new Color(100, 149, 237));
        restoreButton.setEnabled(false);
        restoreButton.setPreferredSize(new Dimension(200, 45));
        restoreButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        deleteButton = createStyledButton("DELETE SELECTED", new Color(220, 20, 60));
        deleteButton.setEnabled(false);
        deleteButton.setPreferredSize(new Dimension(200, 45));
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        refreshButton = createStyledButton("REFRESH", new Color(70, 130, 200));
        refreshButton.setPreferredSize(new Dimension(150, 45));
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        statsLabel = new JLabel("No backups found", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statsLabel.setForeground(new Color(100, 100, 100));
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(245, 250, 255));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Manage Backups", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 150));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("View, restore, or delete your existing backups", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        
        // Table Panel
        JScrollPane scrollPane = new JScrollPane(backupTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Button Panel - CENTERED with prominent buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(245, 250, 255));
        buttonPanel.add(restoreButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Stats Panel
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(new Color(245, 250, 255));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        statsPanel.add(statsLabel, BorderLayout.CENTER);
        
        // Bottom Panel combining buttons and stats
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 250, 255));
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statsPanel, BorderLayout.SOUTH);
        
        // Add all to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addEventListeners() {
        backupTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = backupTable.getSelectedRow() != -1;
            restoreButton.setEnabled(selected);
            deleteButton.setEnabled(selected);
        });
        
        restoreButton.addActionListener(e -> restoreSelectedBackup());
        deleteButton.addActionListener(e -> deleteSelectedBackup());
        refreshButton.addActionListener(e -> refreshBackupTable());
    }
    
    private void loadBackupData() {
        tableModel.setRowCount(0);
        List<BackupRecord> backups = restoreService.getAllBackups();
        
        if (backups != null && !backups.isEmpty()) {
            for (BackupRecord record : backups) {
                Object[] row = {
                    record.getId(),
                    record.getFileName(),
                    record.getOriginalPath(),
                    record.getBackupDate(),
                    String.format("%.2f", record.getFileSizeMB())
                };
                tableModel.addRow(row);
            }
            statsLabel.setText("Total Backups: " + backups.size() + " | Select a backup to restore or delete");
            statsLabel.setForeground(new Color(60, 179, 113));
        } else {
            statsLabel.setText("No backups found. Go to Backup tab to create your first backup!");
            statsLabel.setForeground(new Color(100, 100, 100));
        }
    }
    
    private void restoreSelectedBackup() {
        int selectedRow = backupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a backup to restore!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int backupId = (int) tableModel.getValueAt(selectedRow, 0);
        BackupRecord record = restoreService.getBackupById(backupId);
        
        if (record == null) {
            JOptionPane.showMessageDialog(this, 
                "Backup record not found!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + "\\Desktop";
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select Restore Location");
        
        File defaultDir = new File(desktopPath);
        if (defaultDir.exists()) {
            fileChooser.setCurrentDirectory(defaultDir);
        }
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File restoreLocation = fileChooser.getSelectedFile();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Restore '" + record.getFileName() + "' to:\n" + 
                restoreLocation.getAbsolutePath() + "?",
                "Confirm Restore",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Show progress
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                JOptionPane pane = new JOptionPane(progressBar, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                JDialog dialog = pane.createDialog(this, "Restoring file...");
                
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        return restoreService.restoreBackup(backupId, restoreLocation.getAbsolutePath());
                    }
                    
                    @Override
                    protected void done() {
                        dialog.dispose();
                        try {
                            boolean success = get();
                            if (success) {
                                JOptionPane.showMessageDialog(RestorePanel.this, 
                                    "File restored successfully to:\n" + restoreLocation.getAbsolutePath(),
                                    "Success", 
                                    JOptionPane.INFORMATION_MESSAGE);
                                refreshBackupTable();
                            } else {
                                JOptionPane.showMessageDialog(RestorePanel.this, 
                                    "Restore failed! Check the log file for details.",
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(RestorePanel.this, 
                                "Error: " + e.getMessage(),
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                worker.execute();
                dialog.setVisible(true);
            }
        }
    }
    
    private void deleteSelectedBackup() {
        int selectedRow = backupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a backup to delete!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int backupId = (int) tableModel.getValueAt(selectedRow, 0);
        String fileName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete backup of:\n" + fileName + "\n\nThis action cannot be undone!",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = restoreService.deleteBackup(backupId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Backup deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                refreshBackupTable();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Delete failed! Check the log file for details.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshBackupTable() {
        loadBackupData();
        restoreButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}