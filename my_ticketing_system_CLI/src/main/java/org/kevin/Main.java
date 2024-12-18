package org.kevin;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main class for the ticketing system application.
 * This clas initializes the system, manages the configuration, and starts the vendor and customer threads.
 */
public class Main {
    private static final String CONFIG_FILE = "ticketing_config.json";
    private static volatile boolean running = true;

    /**
     * The main method to start the ticketing system
     * It connects to MongoDB, intializes the configuration, and starts the vendor and customer threads.
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        MongodbConnection.connect();

        Configuration config = getConfiguration();
        if (config == null) {
            Logger.logError("Failed to initialize configuration");
            MongodbConnection.disconnect();
            return;
        }

        // Insert initial set of tickets into MongoDB
        insertInitialTickets(config.getTotalTickets());

        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());
        List<Thread> vendorThreads = new ArrayList<>();
        List<Thread> customerThreads = new ArrayList<>();
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        Thread stopMonitor = new Thread(() -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (running) {
                    System.out.println("Enter 'stop' to terminate the system:");
                    if (scanner.hasNextLine() && scanner.nextLine().equalsIgnoreCase("stop")) {
                        running = false;
                        vendorThreads.forEach(Thread::interrupt);
                        customerThreads.forEach(Thread::interrupt);
                        break;
                    }
                }
            } catch (NoSuchElementException e) {
                Logger.logError("No line found: " + e.getMessage());
            }
        });
        stopMonitor.start();

        // Create and start vendor threads
        for (int i = 0; i < 3; i++) {
            Vendor vendor = new Vendor(ticketPool, config.getTicketReleaseRate(), "V" + (i + 1));
            vendors.add(vendor);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create and start customer threads
        for (int i = 0; i < 5; i++) {
            Customer customer = new Customer(ticketPool, "C" + (i + 1), config.getCustomerRetrievalRate());
            customers.add(customer);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        // Monitor system status
        monitorSystem(config.getTotalTickets());

        // Clean up resources
        cleanUp(vendors, customers, vendorThreads, customerThreads);

        // Disconnect from MongoDB
        MongodbConnection.disconnect();
    }

    /**
     * Inserts the initial set of tickets into MongoDB.
     * @param totalTickets the total number of tickets to insert
     */
    private static void insertInitialTickets(int totalTickets) {
        for (int i = 1; i <= totalTickets; i++) {
            MongodbConnection.insertTicket(i);
        }
        Logger.log(String.format("Inserted %d tickets into MongoDB", totalTickets));
    }

    /**
     * Retrieves the configuration from user inputs and saves it to the file
     * @return the configuration object, or null if an error occurs
     */
    private static Configuration getConfiguration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ticketing system!");

        try {
            System.out.println("Enter total number of tickets:");
            int totalTickets = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter ticket release rate (in milliseconds):");
            int ticketReleaseRate = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter Customer retrieval rate (in milliseconds):");
            int customerRetrievalRate = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter maximum ticket capacity:");
            int maxTicketCapacity = Integer.parseInt(scanner.nextLine());

            if (totalTickets <= 0 || ticketReleaseRate <= 0 || customerRetrievalRate <= 0 || maxTicketCapacity <= 0) {
                throw new IllegalArgumentException("All values must be positive");
            }

            Configuration config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
            config.saveToFile(CONFIG_FILE);
            return config;

        }catch (NumberFormatException e) {
            Logger.logError("Invalid input: Please enter valid numbers");
            return null;
        }catch (IllegalArgumentException e) {
            Logger.logError("Invalid input: " + e.getMessage());
            return null;
        }
    }

    /**
     * Monitors the system status and logs the number of available and sold tickets.
     * @param totalTickets the total number of tickets to sell
     */
    private static void monitorSystem(int totalTickets) {
        int ticketsSold = 0;
        while (ticketsSold < totalTickets && running) {
            try {
                Thread.sleep(1000);
                long available = MongodbConnection.getTicketCount();
                ticketsSold = totalTickets - (int) available;
                Logger.log(String.format("System Status - Available: %d, Sold: %d", available, ticketsSold));
                if (ticketsSold >= totalTickets) {
                    running = false;
                    Logger.log("All tickets sold. Stopping the system.");
                }
            } catch (InterruptedException e) {
                Logger.logError("System monitoring interrupted");
                break;
            }
        }
    }

    /**
     * Cleans up the resources and stops the vendor and customer threads and waiting for them to finish.
     * @param vendors the list of vendors
     * @param customers the list of customers
     * @param vendorThreads the list of vendor threads
     * @param customerThreads the list of customer threads
     */
    private static void cleanUp(List<Vendor> vendors, List<Customer> customers, List<Thread> vendorThreads, List<Thread> customerThreads) {
        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        vendorThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Logger.logError("Vendor thread interrupted: " + e.getMessage());
            }
        });

        customerThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Logger.logError("Customer thread interrupted: " + e.getMessage());
            }
        });
        Logger.log("System shutdown complete");
    }

    /**
     * Sets the running status of the system.
     * @param running the running status to set
     */
    public static void setRunning(boolean running) {
        Main.running = running;
    }

    /**
     * Checks if the system is running.
     * @return true if the system is running, false if not
     */
    public static boolean isRunning() {
        return running;
    }
}