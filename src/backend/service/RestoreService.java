// src/backend/service/RestoreService.java
package backend.service;

import backend.model.BackupRecord;
import backend.utils.FileUtils;
import backend.utils.Logger;

import java.io.File;
import java.util.List;

public class RestoreService {
    private BackupService backupService;
    private FileUtils fileUtils;
    
    public RestoreService() {
        this.backupService = new BackupService();
        this.fileUtils = new FileUtils();
        Logger.log("RestoreService initialized");
    }
    
    public boolean restoreBackup(int backupId, String restoreLocation) {
        try {
            BackupRecord record = backupService.getBackupById(backupId);
            if (record == null) {
                Logger.log("ERROR: Backup record not found for ID: " + backupId);
                return false;
            }
            
            File backupFile = new File(record.getBackupPath());
            if (!backupFile.exists()) {
                Logger.log("ERROR: Backup file does not exist: " + record.getBackupPath());
                return false;
            }
            
            File restoreDir = new File(restoreLocation);
            if (!restoreDir.exists()) {
                restoreDir.mkdirs();
            }
            
            if (!restoreDir.canWrite()) {
                Logger.log("ERROR: No write permission for: " + restoreLocation);
                return false;
            }
            
            String restoredFilePath = restoreLocation + File.separator + record.getFileName();
            boolean restoreSuccess = fileUtils.copyFile(record.getBackupPath(), restoredFilePath);
            
            if (restoreSuccess) {
                Logger.log("SUCCESS: File restored to: " + restoredFilePath);
                return true;
            } else {
                Logger.log("ERROR: Failed to restore file to: " + restoredFilePath);
                return false;
            }
        } catch (Exception e) {
            Logger.log("ERROR: Exception in restoreBackup: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<BackupRecord> getAllBackups() {
        return backupService.getAllBackups();
    }
    
    public BackupRecord getBackupById(int id) {
        return backupService.getBackupById(id);
    }
    
    public boolean deleteBackup(int backupId) {
        return backupService.deleteBackup(backupId);
    }
}