// src/backend/service/BackupService.java
package backend.service;

import backend.db.BackupDAO;
import backend.model.BackupRecord;
import backend.utils.FileUtils;
import backend.utils.Logger;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class BackupService {
    private BackupDAO backupDAO;
    private FileUtils fileUtils;
    
    public BackupService() {
        this.backupDAO = new BackupDAO();
        this.fileUtils = new FileUtils();
        Logger.log("BackupService initialized with database support");
    }
    
    public boolean createBackup(File sourceFile) {
        try {
            if (sourceFile == null || !sourceFile.exists()) {
                Logger.log("ERROR: Source file does not exist: " + sourceFile);
                return false;
            }
            
            // Create backup_storage directory if not exists
            fileUtils.createDirectory("backup_storage");
            
            // Generate backup filename with timestamp
            String timestamp = fileUtils.getCurrentTimestamp();
            String backupFileName = fileUtils.generateBackupFileName(sourceFile.getName(), timestamp);
            
            // Create backup destination path
            String backupPath = "backup_storage" + File.separator + backupFileName;
            
            // Copy file to backup location
            boolean copySuccess = fileUtils.copyFile(sourceFile.getAbsolutePath(), backupPath);
            
            if (!copySuccess) {
                Logger.log("ERROR: Failed to copy file to backup location");
                return false;
            }
            
            // Get file size
            long fileSize = fileUtils.getFileSize(sourceFile.getAbsolutePath());
            
            // Create backup record
            BackupRecord record = new BackupRecord();
            record.setFileName(sourceFile.getName());
            record.setOriginalPath(sourceFile.getAbsolutePath());
            record.setBackupPath(backupPath);
            record.setBackupDate(Timestamp.valueOf(LocalDateTime.now()));
            record.setFileSize(fileSize);
            
            // Save to database
            boolean dbSuccess = backupDAO.addBackup(record);
            
            if (dbSuccess) {
                Logger.log("SUCCESS: Backup created and saved to database: " + sourceFile.getAbsolutePath());
                System.out.println("Backup saved to database with ID: " + record.getId());
                return true;
            } else {
                // If database save fails, delete the backup file
                fileUtils.deleteFile(backupPath);
                Logger.log("ERROR: Failed to save backup record to database");
                return false;
            }
            
        } catch (Exception e) {
            Logger.log("ERROR: Exception in createBackup: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<BackupRecord> getAllBackups() {
        try {
            List<BackupRecord> backups = backupDAO.getAllBackups();
            System.out.println("Retrieved " + (backups != null ? backups.size() : 0) + " backups from database");
            return backups;
        } catch (Exception e) {
            Logger.log("ERROR: Failed to get all backups: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public BackupRecord getBackupById(int id) {
        try {
            return backupDAO.getBackupById(id);
        } catch (Exception e) {
            Logger.log("ERROR: Failed to get backup by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean deleteBackup(int backupId) {
        try {
            BackupRecord record = backupDAO.getBackupById(backupId);
            if (record == null) {
                Logger.log("ERROR: Backup record not found for ID: " + backupId);
                return false;
            }
            
            // Delete the physical file
            boolean fileDeleted = fileUtils.deleteFile(record.getBackupPath());
            
            // Delete from database
            boolean dbDeleted = backupDAO.deleteBackup(backupId);
            
            if (fileDeleted && dbDeleted) {
                Logger.log("SUCCESS: Backup deleted from storage and database: " + record.getFileName());
                return true;
            } else {
                Logger.log("ERROR: Failed to delete backup completely");
                return false;
            }
            
        } catch (Exception e) {
            Logger.log("ERROR: Exception in deleteBackup: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}