package com.backuptool.exception;

public class BackupOperationException extends RuntimeException {

    private final String operationType;

    public BackupOperationException(String operationType, String message) {
        super("[" + operationType.toUpperCase() + "] " + message);
        this.operationType = operationType;
    }

    public BackupOperationException(String operationType, String message, Throwable cause) {
        super("[" + operationType.toUpperCase() + "] " + message, cause);
        this.operationType = operationType;
    }

    public String getOperationType() {
        return operationType;
    }
}
