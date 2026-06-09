// src/backend/model/BackupRecord.java
package backend.model;

import java.sql.Timestamp;

public class BackupRecord {
    private int id;
    private String fileName;
    private String originalPath;
    private String backupPath;
    private Timestamp backupDate;
    private long fileSize;
    
    // Constructor
    public BackupRecord() {
    }
    
    public BackupRecord(String fileName, String originalPath, String backupPath, Timestamp backupDate, long fileSize) {
        this.fileName = fileName;
        this.originalPath = originalPath;
        this.backupPath = backupPath;
        this.backupDate = backupDate;
        this.fileSize = fileSize;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getOriginalPath() {
        return originalPath;
    }
    
    public String getBackupPath() {
        return backupPath;
    }
    
    public Timestamp getBackupDate() {
        return backupDate;
    }
    
    public long getFileSize() {
        return fileSize;
    }
    
    public double getFileSizeMB() {
        return fileSize / (1024.0 * 1024.0);
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }
    
    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }
    
    public void setBackupDate(Timestamp backupDate) {
        this.backupDate = backupDate;
    }
    
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    @Override
    public String toString() {
        return "BackupRecord{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", originalPath='" + originalPath + '\'' +
                ", backupPath='" + backupPath + '\'' +
                ", backupDate=" + backupDate +
                ", fileSize=" + fileSize +
                '}';
    }
}