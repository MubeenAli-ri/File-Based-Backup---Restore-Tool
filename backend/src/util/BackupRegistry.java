package com.backuptool.util;

import com.backuptool.model.BackupRecord;
import com.backuptool.model.RestoreRecord;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BackupRegistry {

    private static BackupRegistry instance;

    private final Map<String, BackupRecord>  backups  = new ConcurrentHashMap<>();
    private final Map<String, RestoreRecord> restores = new ConcurrentHashMap<>();

    private BackupRegistry() {}

    public static synchronized BackupRegistry getInstance() {
        if (instance == null) {
            instance = new BackupRegistry();
        }
        return instance;
    }

    public void saveBackup(BackupRecord record) {
        backups.put(record.getId(), record);
    }

    public Optional<BackupRecord> findBackupById(String id) {
        return Optional.ofNullable(backups.get(id));
    }

    public List<BackupRecord> findAllBackups() {
        return new ArrayList<>(backups.values());
    }

    public List<BackupRecord> findBackupsByStatus(BackupRecord.Status status) {
        return backups.values().stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean deleteBackup(String id) {
        return backups.remove(id) != null;
    }

    public int backupCount() {
        return backups.size();
    }

    public void saveRestore(RestoreRecord record) {
        restores.put(record.getId(), record);
    }

    public Optional<RestoreRecord> findRestoreById(String id) {
        return Optional.ofNullable(restores.get(id));
    }

    public List<RestoreRecord> findRestoresByBackupId(String backupId) {
        return restores.values().stream()
                .filter(r -> r.getBackupId().equals(backupId))
                .collect(Collectors.toList());
    }

    public List<RestoreRecord> findAllRestores() {
        return new ArrayList<>(restores.values());
    }

    public void clearAll() {
        backups.clear();
        restores.clear();
    }
}
