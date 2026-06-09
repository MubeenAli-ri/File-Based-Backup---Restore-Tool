// src/backend/db/BackupDAO.java
package backend.db;

import backend.model.BackupRecord;
import backend.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BackupDAO {
    
    public boolean addBackup(BackupRecord record) {
        String sql = "INSERT INTO backups (file_name, original_path, backup_path, backup_date, file_size) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (conn == null) {
                Logger.log("ERROR: No database connection");
                return false;
            }
            
            pstmt.setString(1, record.getFileName());
            pstmt.setString(2, record.getOriginalPath());
            pstmt.setString(3, record.getBackupPath());
            pstmt.setTimestamp(4, record.getBackupDate());
            pstmt.setLong(5, record.getFileSize());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        record.setId(generatedKeys.getInt(1));
                    }
                }
                Logger.log("Backup added to database: " + record.getFileName());
                return true;
            }
            
        } catch (SQLException e) {
            Logger.log("ERROR: Failed to add backup: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public List<BackupRecord> getAllBackups() throws SQLException {
        List<BackupRecord> backups = new ArrayList<>();
        String sql = "SELECT * FROM backups ORDER BY backup_date DESC";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            Logger.log("ERROR: No database connection");
            return backups;
        }
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                BackupRecord record = new BackupRecord();
                record.setId(rs.getInt("id"));
                record.setFileName(rs.getString("file_name"));
                record.setOriginalPath(rs.getString("original_path"));
                record.setBackupPath(rs.getString("backup_path"));
                record.setBackupDate(rs.getTimestamp("backup_date"));
                record.setFileSize(rs.getLong("file_size"));
                backups.add(record);
            }
        }
        
        return backups;
    }
    
    public BackupRecord getBackupById(int id) throws SQLException {
        String sql = "SELECT * FROM backups WHERE id = ?";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            Logger.log("ERROR: No database connection");
            return null;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                BackupRecord record = new BackupRecord();
                record.setId(rs.getInt("id"));
                record.setFileName(rs.getString("file_name"));
                record.setOriginalPath(rs.getString("original_path"));
                record.setBackupPath(rs.getString("backup_path"));
                record.setBackupDate(rs.getTimestamp("backup_date"));
                record.setFileSize(rs.getLong("file_size"));
                return record;
            }
        }
        
        return null;
    }
    
    public boolean deleteBackup(int id) throws SQLException {
        String sql = "DELETE FROM backups WHERE id = ?";
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            Logger.log("ERROR: No database connection");
            return false;
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}