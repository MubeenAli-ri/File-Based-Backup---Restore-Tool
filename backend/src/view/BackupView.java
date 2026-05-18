package com.backuptool.view;

import com.backuptool.model.BackupRecord;
import com.backuptool.model.RestoreRecord;
import com.backuptool.util.FileUtil;

import java.util.List;

public class BackupView {

    private static final String SEPARATOR = "─".repeat(60);

    public void displayBackupSuccess(BackupRecord record) {
        System.out.println("\nBACKUP COMPLETED");
        System.out.println(SEPARATOR);
        printBackupRow(record);
        System.out.println(SEPARATOR);
    }

    public void displayBackupDetails(BackupRecord record) {
        System.out.println("\nBACKUP DETAILS");
        System.out.println(SEPARATOR);
        System.out.printf("  %-16s : %s%n", "ID",          record.getId());
        System.out.printf("  %-16s : %s%n", "Name",        record.getName());
        System.out.printf("  %-16s : %s%n", "Status",      formatStatus(record.getStatus()));
        System.out.printf("  %-16s : %s%n", "Source",      record.getSourcePath());
        System.out.printf("  %-16s : %s%n", "Destination", record.getDestinationPath());
        System.out.printf("  %-16s : %d%n", "File Count",  record.getFileCount());
        System.out.printf("  %-16s : %s%n", "Size",        FileUtil.humanReadableSize(record.getFileSizeBytes()));
        System.out.printf("  %-16s : %s%n", "Created At",  record.getCreatedAt());
        System.out.printf("  %-16s : %s%n", "Completed At",record.getCompletedAt());
        if (record.getChecksum() != null) {
            System.out.printf("  %-16s : %s%n", "MD5 Checksum", record.getChecksum());
        }
        if (record.getErrorMessage() != null) {
            System.out.printf("  %-16s : %s%n", "Error", record.getErrorMessage());
        }
        System.out.println(SEPARATOR);
    }

    public void displayBackupList(List<BackupRecord> records) {
        System.out.println("\nBACKUP LIST (" + records.size() + " record(s))");
        System.out.println(SEPARATOR);
        if (records.isEmpty()) {
            System.out.println("  No backups found.");
        } else {
            System.out.printf("  %-36s  %-20s  %-12s  %s%n",
                    "ID", "Name", "Status", "Size");
            System.out.println("  " + "─".repeat(56));
            for (BackupRecord r : records) {
                printBackupRow(r);
            }
        }
        System.out.println(SEPARATOR);
    }

    public void displayRestoreSuccess(RestoreRecord record) {
        System.out.println("\nRESTORE COMPLETED");
        System.out.println(SEPARATOR);
        printRestoreRow(record);
        System.out.println(SEPARATOR);
    }

    public void displayRestoreDetails(RestoreRecord record) {
        System.out.println("\nRESTORE DETAILS");
        System.out.println(SEPARATOR);
        System.out.printf("  %-16s : %s%n", "ID",             record.getId());
        System.out.printf("  %-16s : %s%n", "Backup ID",      record.getBackupId());
        System.out.printf("  %-16s : %s%n", "Status",         formatRestoreStatus(record.getStatus()));
        System.out.printf("  %-16s : %s%n", "Restore Path",   record.getRestorePath());
        System.out.printf("  %-16s : %d%n", "Files Restored", record.getFilesRestored());
        System.out.printf("  %-16s : %s%n", "Started At",     record.getStartedAt());
        System.out.printf("  %-16s : %s%n", "Completed At",   record.getCompletedAt());
        if (record.getErrorMessage() != null) {
            System.out.printf("  %-16s : %s%n", "Error", record.getErrorMessage());
        }
        System.out.println(SEPARATOR);
    }

    public void displayRestoreList(List<RestoreRecord> records) {
        System.out.println("\nRESTORE LIST (" + records.size() + " record(s))");
        System.out.println(SEPARATOR);
        if (records.isEmpty()) {
            System.out.println("  No restore records found.");
        } else {
            System.out.printf("  %-36s  %-36s  %-12s  %s%n",
                    "ID", "Backup ID", "Status", "Files Restored");
            System.out.println("  " + "─".repeat(56));
            for (RestoreRecord r : records) {
                printRestoreRow(r);
            }
        }
        System.out.println(SEPARATOR);
    }

    public void displayInfo(String message) {
        System.out.println("ℹ" + message);
    }

    public void displayError(String message) {
        System.err.println("ERROR: " + message);
    }

    public void displayRaw(String text) {
        System.out.println(text);
    }

    private void printBackupRow(BackupRecord r) {
        System.out.printf("  %-36s  %-20s  %-12s  %s%n",
                r.getId(),
                truncate(r.getName(), 20),
                formatStatus(r.getStatus()),
                FileUtil.humanReadableSize(r.getFileSizeBytes()));
    }

    private void printRestoreRow(RestoreRecord r) {
        System.out.printf("  %-36s  %-36s  %-12s  %d%n",
                r.getId(),
                r.getBackupId(),
                formatRestoreStatus(r.getStatus()),
                r.getFilesRestored());
    }

    private String formatStatus(BackupRecord.Status status) {
        return switch (status) {
            case PENDING     -> "PENDING";
            case IN_PROGRESS -> "IN_PROG";
            case COMPLETED   -> "DONE";
            case FAILED      -> "FAILED";
            case RESTORED    -> "RESTORED";
        };
    }

    private String formatRestoreStatus(RestoreRecord.Status status) {
        return switch (status) {
            case PENDING     -> "PENDING";
            case IN_PROGRESS -> "IN_PROG";
            case COMPLETED   -> "DONE";
            case FAILED      -> "FAILED";
        };
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen - 2) + ".." : s;
    }
}
