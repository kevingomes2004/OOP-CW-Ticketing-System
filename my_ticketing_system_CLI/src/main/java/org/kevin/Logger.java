package org.kevin;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.*;

/**
 * Logger class provides methods to log informational and error messages to the console and a log file.
 * It includes methods to log messages with timestamps and write messages to a log file.
 */
public class Logger {
    private static final String LOG_FILE = "ticketing_system.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs an informational message with a timestamp to the log file and console.
     * @param message the message to log
     */
    public static synchronized void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] INFO: %s%n", timestamp, message);
        writeToFile(logMessage);
        System.out.println(logMessage);
    }

    /**
     * Logs an error message with a timestamp to the log file and console.
     * @param message the error message to log
     */
    public static synchronized void logError(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] ERROR: %s%n", timestamp, message);
        writeToFile(logMessage);
        System.err.println(logMessage);
    }

    /**
     * Writes a message to the log file.
     * @param message the message to write to the log file
     */
    private static void writeToFile(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(message);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
