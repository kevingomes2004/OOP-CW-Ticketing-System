package org.kevin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MongodbConnection class manages the connection to the MongoDB database for the ticketing system.
 * It provides methods to connect, disconnect, insert tickets, remove tickets, and log messages.
 */
public class MongodbConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "ticketing_system";
    private static final String COLLECTION_NAME = "tickets";
    private static final String LOG_COLLECTION_NAME = "logs";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> logCollection;

    /**
     * Connects to the MongoDB database using the specified connection string.
     * Initializes the database and collection objects.
     */
    public static void connect() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
        logCollection = database.getCollection(LOG_COLLECTION_NAME);
    }

    /**
     * Disconnects from the MongoDB database.
     * Closes the MongoClient if it is not null.
     */
    public static void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * Inserts a ticket with the specified ticket number into the MongoDB collection.
     * Logs a message indicating the ticket was added to the pool.
     * @param ticketNumber the number of the ticket to insert
     */
    public static void insertTicket(int ticketNumber) {
        Document ticket = new Document("ticket_number", ticketNumber);
        collection.insertOne(ticket);
        log(String.format("Added ticket %d to pool. Current size: %d", ticketNumber, getTicketCount()));
    }

    /**
     * Removes a ticket from the MongoDB collection.
     * Logs a message indicating the ticket was removed from the pool.
     * @return the ticket number of the removed ticket, or null if no tickets are available
     */
    public static Integer removeTicket() {
        Document ticket = collection.findOneAndDelete(new Document());
        if (ticket == null) {
            return null;
        }
        int ticketNumber = ticket.getInteger("ticket_number");
        log(String.format("Removed ticket %d. Remaining tickets: %d", ticketNumber, getTicketCount()));
        return ticketNumber;
    }

    /**
     * Get the count of tickets in the ticket collection.
     * @return the count of tickets in the collection
     */
    public static long getTicketCount() {
        return collection.countDocuments();
    }

    /**
     * Logs a message with a timestamp to the logs collection.
     * @param message the message to log
     */
    private static void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Document logEntry = new Document("timestamp", timestamp)
                .append("message", message);
        logCollection.insertOne(logEntry);
    }
}
