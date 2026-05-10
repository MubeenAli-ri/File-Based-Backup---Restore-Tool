package com.backuptool.model;

import java.time.LocalDateTime;
import java.util.UUID;


public class RestoreRecord {

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED, FAILED
    }

    private String id;
    private String backupId;
    private String restorePath;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Status status;
    private String errorMessage;
    private int filesRestored;

    public RestoreRecord() {
        this.id = UUID.randomUUID().toString();
        this.startedAt = LocalDateTime.now();
        this.status = Status.PENDING;
        this.filesRestored = 0;
    }

    public RestoreRecord(String backupId, String restorePath) {
        this();
        this.backupId = backupId;
        this.restorePath = restorePath;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBackupId() { return backupId; }
    public void setBackupId(String backupId) { this.backupId = backupId; }

    public String getRestorePath() { return restorePath; }
    public void setRestorePath(String restorePath) { this.restorePath = restorePath; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getFilesRestored() { return filesRestored; }
    public void setFilesRestored(int filesRestored) { this.filesRestored = filesRestored; }

    @Override
    public String toString() {
        return "RestoreRecord{" +
                "id='" + id + '\'' +
                ", backupId='" + backupId + '\'' +
                ", restorePath='" + restorePath + '\'' +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", status=" + status +
                ", filesRestored=" + filesRestored +
                '}';
    }
}
