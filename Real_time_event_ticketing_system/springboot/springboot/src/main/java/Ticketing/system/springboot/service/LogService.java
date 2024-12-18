package Ticketing.system.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import Ticketing.system.springboot.model.Log;
import Ticketing.system.springboot.repo.LogRepository;

import java.util.Date;
import java.util.List;

/**
 * Service class for managing log entries in the ticketing system.
 * This class provides methods for retrieving all logs and logging actions performed by vendors and customers.
 * It also sends log messages to WebSocket clients.
 */
@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Default constructor for LogService.
     */
    public LogService() {}

    /**
     * Retrieves all log entries from the database.
     *
     * @return a list of all log entries
     */
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    /**
     * Logs an action performed by a vendor.
     * The log entry is saved to the database and sent to WebSocket clients.
     *
     * @param message the message describing the vendor's action
     */
    public void logVendorAction(String message) {
        Log log = new Log(message, new Date());
        logRepository.save(log);
        messagingTemplate.convertAndSend("/topic/logs", log);  // Send log to WebSocket clients
    }

    /**
     * Logs an action performed by a customer.
     * The log entry is saved to the database and sent to WebSocket clients.
     *
     * @param message the message describing the customer's action
     */
    public void logCustomerAction(String message) {
        Log log = new Log(message, new Date());
        logRepository.save(log);
        messagingTemplate.convertAndSend("/topic/logs", log);  // Send log to WebSocket clients
    }
}
