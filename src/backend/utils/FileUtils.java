// src/backend/utils/FileUtils.java
package backend.utils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {
    
    // Copy file from source to destination using Files.copy()
    public boolean copyFile(String sourcePath, String destinationPath) {
        try {
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);
            
            // Create parent directories if they don't exist
            Files.createDirectories(destination.getParent());
            
            // Copy file with overwrite option
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            
            Logger.log("File copied: " + sourcePath + " -> " + destinationPath);
            return true;
            
        } catch (IOException e) {
            Logger.log("ERROR: Failed to copy file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete file
    public boolean deleteFile(String filePath) {
        try {
            Path file = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(file);
            
            if (deleted) {
                Logger.log("File deleted: " + filePath);
            }
            return deleted;
            
        } catch (IOException e) {
            Logger.log("ERROR: Failed to delete file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Get file size in bytes
    public long getFileSize(String filePath) {
        try {
            Path file = Paths.get(filePath);
            return Files.size(file);
        } catch (IOException e) {
            Logger.log("ERROR: Failed to get file size: " + e.getMessage());
            return 0;
        }
    }
    
    // Check if file exists
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    // Create directory if not exists
    public boolean createDirectory(String dirPath) {
        try {
            Path dir = Paths.get(dirPath);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
                Logger.log("Directory created: " + dirPath);
            }
            return true;
        } catch (IOException e) {
            Logger.log("ERROR: Failed to create directory: " + e.getMessage());
            return false;
        }
    }
    
    // Generate backup filename with timestamp
    public String generateBackupFileName(String originalFileName, String timestamp) {
        String nameWithoutExt = originalFileName;
        String extension = "";
        
        int lastDot = originalFileName.lastIndexOf(".");
        if (lastDot > 0) {
            nameWithoutExt = originalFileName.substring(0, lastDot);
            extension = originalFileName.substring(lastDot);
        }
        
        return nameWithoutExt + "_backup_" + timestamp + extension;
    }
    
    // Get current timestamp as string for filenames
    public String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return LocalDateTime.now().format(formatter);
    }
    
    // Move file
    public boolean moveFile(String sourcePath, String destinationPath) {
        try {
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);
            Files.createDirectories(destination.getParent());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            Logger.log("File moved: " + sourcePath + " -> " + destinationPath);
            return true;
        } catch (IOException e) {
            Logger.log("ERROR: Failed to move file: " + e.getMessage());
            return false;
        }
    }
    
    // Get file name from path
    public String getFileName(String filePath) {
        Path path = Paths.get(filePath);
        return path.getFileName().toString();
    }
}