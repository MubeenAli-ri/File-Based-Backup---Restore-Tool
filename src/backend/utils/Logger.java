// src/backend/utils/Logger.java
package backend.utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "backup_log.txt";
    
    // Log a message to file
    public static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println(timestamp + " - " + message);
            
            // Also print to console for debugging
            System.out.println(timestamp + " - " + message);
            
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    // Log error
    public static void error(String message) {
        log("ERROR: " + message);
    }
    
    // Log info
    public static void info(String message) {
        log("INFO: " + message);
    }
    
    // Log success
    public static void success(String message) {
        log("SUCCESS: " + message);
    }
    
    // Clear log file
    public static void clearLog() {
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {
            fw.write("");
        } catch (IOException e) {
            System.err.println("Failed to clear log file: " + e.getMessage());
        }
    }
    
    // Read log file content
    public static String readLog() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return "No log file found.";
        }
        return content.toString();
    }
}