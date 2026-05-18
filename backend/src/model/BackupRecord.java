package com.backuptool.model;

import java.time.LocalDateTime;
import java.util.UUID;


public class BackupRecord {

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED, FAILED, RESTORED
    }

    private String id;
    private String name;
    private String sourcePath;
    private String destinationPath;
    private long fileSizeBytes;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Status status;
    private String errorMessage;
    private int fileCount;
    private String checksum;

    public BackupRecord() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
        this.fileCount = 0;
    }

    public BackupRecord(String name, String sourcePath, String destinationPath) {
        this();
        this.name = name;
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSourcePath() { return sourcePath; }
    public void setSourcePath(String sourcePath) { this.sourcePath = sourcePath; }

    public String getDestinationPath() { return destinationPath; }
    public void setDestinationPath(String destinationPath) { this.destinationPath = destinationPath; }

    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getFileCount() { return fileCount; }
    public void setFileCount(int fileCount) { this.fileCount = fileCount; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    @Override
    public String toString() {
        return "BackupRecord{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sourcePath='" + sourcePath + '\'' +
                ", destinationPath='" + destinationPath + '\'' +
                ", fileSizeBytes=" + fileSizeBytes +
                ", createdAt=" + createdAt +
                ", completedAt=" + completedAt +
                ", status=" + status +
                ", fileCount=" + fileCount +
                '}';
    }
}
