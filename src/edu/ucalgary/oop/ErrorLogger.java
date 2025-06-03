package edu.ucalgary.oop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ErrorLogger writes critical errors to a specified file,
 * including a timestamp and stack trace.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class ErrorLogger {

    private String logFilePath = "data/errorlog.txt";

    /**
     * Default constructor using data/errorlog.txt as the log file.
     */
    public ErrorLogger() {
    }

    /**
     * Constructs an ErrorLogger specifying a custom log file path.
     *
     * @param path the file path for logging errors
     */
    public ErrorLogger(String path) {
        this.logFilePath = path;
    }

    /**
     * Logs an exception with its message.
     *
     * @param e the exception
     */
    public void logError(Exception e) {
        logError(e.getMessage(), e);
    }

    /**
     * Logs a message and stack trace to the log file with a timestamp.
     *
     * @param message the error message
     * @param e       the exception
     */
    public void logError(String message, Exception e) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (PrintWriter pw = new PrintWriter(new FileWriter(logFilePath, true))) {
            pw.println("[" + timestamp + "] " + message);
            e.printStackTrace(pw);
            pw.println("--------------------------------------------------");
        } catch (IOException ioe) {
            System.out.println("Failed to write to error log: " + ioe.getMessage());
        }
    }
}
