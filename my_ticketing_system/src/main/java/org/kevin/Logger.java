package org.kevin;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.*;

public class Logger {
    private static final String LOG_FILE = "ticketing_system.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static synchronized void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] INFO: %s%n", timestamp, message);
        writeToFile(logMessage);
        System.out.println(logMessage);
    }

    public static synchronized void logError(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] ERROR: %s%n", timestamp, message);
        writeToFile(logMessage);
        System.err.println(logMessage);
    }

    private static void writeToFile(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(message);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
