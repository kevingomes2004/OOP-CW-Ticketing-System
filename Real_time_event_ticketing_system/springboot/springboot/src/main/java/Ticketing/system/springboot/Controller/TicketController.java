package Ticketing.system.springboot.Controller;

import Ticketing.system.springboot.Mongo.MongodbConnection;
import Ticketing.system.springboot.model.Configuration;
import Ticketing.system.springboot.model.Ticket;
import Ticketing.system.springboot.repo.ConfigurationRepository;
import Ticketing.system.springboot.service.Customer;
import Ticketing.system.springboot.service.Vendor;
import Ticketing.system.springboot.service.TicketService;
import Ticketing.system.springboot.Configuration.TicketWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Ticketing.system.springboot.model.Log;
import Ticketing.system.springboot.service.LogService;
import Ticketing.system.springboot.component.ConfigHolder;

import java.util.List;

/**
 * REST controller for managing tickets and system operations.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private MongodbConnection mongodbConnection;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private Customer customerService;

    @Autowired
    private Vendor vendorService;

    @Autowired
    private TicketWebSocketHandler webSocketHandler;

    @Autowired
    private ConfigurationRepository configrepo;

    @Autowired
    private ConfigHolder configHolder;

    /**
     * Default constructor for TicketController.
     * Initializes the REST controller for handling ticket-related operations.
     */
    public TicketController() {}

    /**
     * Starts the system by activating vendors and customers.
     *
     * @return a response indicating the system has started
     */
    @GetMapping("/start")
    public ResponseEntity<String> startSystem() {
        Configuration config = configrepo.findTopByOrderByIdDesc();
        if (config == null || config.getCustomerRetrievalRate() < 0 || config.getTicketReleaseRate() < 0 ||
                config.getMaxTicketCapacity() < 0) {
            return ResponseEntity.badRequest().body("Invalid configuration. System cannot start.");
        }

        vendorService.startVendor();
        customerService.startCustomer();
        return ResponseEntity.ok("System started. Vendors and customers are active.");
    }

    /**
     * Stops the system by deactivating vendors and customers.
     *
     * @return a response indicating the system has stopped
     */
    @GetMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        vendorService.stopVendor();
        customerService.stopCustomer();
        return ResponseEntity.status(HttpStatus.OK).body("System stopped. Vendors and customers are inactive.");
    }

    /**
     * Checks the connection status to the MongoDB database.
     *
     * @return true if connected, false otherwise
     */
    @GetMapping("/checkConnection")
    public boolean checkConnection() {
        return mongodbConnection.isConnected();
    }

    /**
     * Creates a new database with the specified name.
     *
     * @param dbName the name of the database to create
     */
    @PostMapping("/createDatabase")
    public void createDatabase(@RequestParam String dbName) {
        mongodbConnection.createDatabase(dbName);
    }

    /**
     * Connects to the MongoDB database.
     *
     * @return a response indicating the database connection status
     */
    @PostMapping("/connect")
    public ResponseEntity<String> connectToDB() {
        try {
            mongodbConnection.connect();
            return ResponseEntity.ok("Database connected successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to connect to database: " + e.getMessage());
        }
    }

    /**
     * Disconnects from the MongoDB database.
     *
     * @return a response indicating the database disconnection status
     */
    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnectDB() {
        mongodbConnection.disconnect();
        return ResponseEntity.ok("Database disconnected successfully.");
    }

    /**
     * Saves the provided configuration to the database and updates the shared configuration holder.
     *
     * @param config the configuration to save
     * @return the saved configuration
     */
    @PostMapping("/saveConfig")
    public ResponseEntity<Configuration> saveConfig(@RequestBody Configuration config) {
        if (config.getCustomerRetrievalRate() < 0 || config.getTicketReleaseRate() < 0 ||
                config.getMaxTicketCapacity() < 0) {
            return ResponseEntity.badRequest().build();
        }

        Configuration savedConfig = configrepo.save(config);

        // Update the shared configuration holder
        configHolder.setPurchaseRate(config.getCustomerRetrievalRate());
        configHolder.setReleaseRate(config.getTicketReleaseRate());
        configHolder.setMaxTicketCapacity(config.getMaxTicketCapacity());

        return ResponseEntity.ok(savedConfig);
    }

    /**
     * Retrieves the most recent configuration from the database.
     *
     * @return the most recent configuration, or a 404 response if not found
     */
    @GetMapping("/getConfig")
    public ResponseEntity<Configuration> getConfig() {
        Configuration configGet = configrepo.findTopByOrderByIdDesc();
        return configGet != null ? ResponseEntity.ok(configGet) : ResponseEntity.notFound().build();
    }

    @Autowired
    private LogService logService;

    /**
     * Retrieves all logs from the system.
     *
     * @return a list of logs
     */
    @GetMapping("/logs")
    public List<Log> getLogs() {
        return logService.getAllLogs();
    }

    /**
     * Retrieves all tickets from the system.
     *
     * @return a list of tickets
     */
    @GetMapping("/list")
    public List<Ticket> getTickets() {
        return ticketService.getTickets();
    }
}
