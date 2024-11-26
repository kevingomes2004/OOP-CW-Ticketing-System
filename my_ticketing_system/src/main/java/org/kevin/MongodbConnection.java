package org.kevin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MongodbConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "ticketing_system";
    private static final String COLLECTION_NAME = "tickets";
    private static final String LOG_COLLECTION_NAME = "logs";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> logCollection;

    public static void connect() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
        logCollection = database.getCollection(LOG_COLLECTION_NAME);
    }

    public static void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public static void insertTicket(int ticketNumber) {
        Document ticket = new Document("ticket_number", ticketNumber);
        collection.insertOne(ticket);
        log(String.format("Added ticket %d to pool. Current size: %d", ticketNumber, getTicketCount()));
    }

    public static Integer removeTicket() {
        Document ticket = collection.findOneAndDelete(new Document());
        if (ticket == null) {
            return null;
        }
        int ticketNumber = ticket.getInteger("ticket_number");
        log(String.format("Removed ticket %d. Remaining tickets: %d", ticketNumber, getTicketCount()));
        return ticketNumber;
    }

    public static long getTicketCount() {
        return collection.countDocuments();
    }

    private static void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Document logEntry = new Document("timestamp", timestamp)
                .append("message", message);
        logCollection.insertOne(logEntry);
    }
}
