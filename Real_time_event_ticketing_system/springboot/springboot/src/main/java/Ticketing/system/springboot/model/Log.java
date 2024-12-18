package Ticketing.system.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Represents a log entry in the ticketing system.
 * Each log entry contains a message and a timestamp indicating when the log was created.
 */
@Document(collection = "logs")
public class Log {

    @Id
    private String id;
    private String message;
    private Date timestamp;

    /**
     * Default constructor.
     * Creates an empty log entry.
     */
    public Log() {}

    /**
     * Constructor with fields.
     * Creates a log entry with the specified message and timestamp.
     *
     * @param message the log message
     * @param timestamp the timestamp of the log entry
     */
    public Log(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Gets the ID of the log entry.
     *
     * @return the ID of the log entry
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the log entry.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the log message.
     *
     * @return the log message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the log message.
     *
     * @param message the log message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp of the log entry.
     *
     * @return the timestamp of the log entry
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the log entry.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns a string representation of the log entry.
     * The string includes the ID, message, and timestamp of the log entry.
     *
     * @return a string representation of the log entry
     */
    @Override
    public String toString() {
        return "Log{id='" + id + "', message='" + message + "', timestamp=" + timestamp + "}";
    }
}
