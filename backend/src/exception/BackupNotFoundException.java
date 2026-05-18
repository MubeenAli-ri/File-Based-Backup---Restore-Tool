package com.backuptool.exception;

public class BackupNotFoundException extends RuntimeException {

    private final String backupId;

    public BackupNotFoundException(String backupId) {
        super("Backup record not found with ID: " + backupId);
        this.backupId = backupId;
    }

    public String getBackupId() {
        return backupId;
    }
}
