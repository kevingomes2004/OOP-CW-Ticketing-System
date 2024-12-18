package Ticketing.system.springboot.Mongo;

import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Manages the connection to the MongoDB database for the ticketing system.
 * This class handles connecting to and disconnecting from the MongoDB instance,
 * as well as performing basic operations such as inserting tickets and counting documents.
 */
@Component
public class MongodbConnection {

    private MongoClient mongoClient;
    private MongoDatabase database;

    /**
     * Default constructor for MongodbConnection.
     */
    public MongodbConnection () {}

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    /**
     * Initializes the connection to the MongoDB database.
     * This method is called after the bean is constructed.
     */
    @PostConstruct
    public void connect() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase(databaseName);
        }
    }

    /**
     * Closes the connection to the MongoDB database.
     * This method is called before the bean is destroyed.
     */
    @PreDestroy
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * Creates a new database with the specified name.
     * The database is created when data is first stored in it.
     *
     * @param dbName the name of the database to create
     */
    public void createDatabase(String dbName) {
        if (mongoClient != null) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            // The database is created when you first store data in it
        }
    }

    /**
     * Checks if the connection to the MongoDB database is active.
     *
     * @return true if the connection is active, false otherwise
     */
    public boolean isConnected() {
        if (mongoClient != null) {
            try {
                MongoIterable<String> databases = mongoClient.listDatabaseNames();
                databases.first(); // Attempt to retrieve the first database name
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}