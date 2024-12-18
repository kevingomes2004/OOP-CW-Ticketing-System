package org.kevin;

/**
 * Vendor class represents a ticket vendor in the ticketing system.
 * It implements the Runnable interface to allow running in a separate thread.
 * The vendor adds tickets to the tickets pool at a specified rate and logs the action.
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final String name;
    private volatile boolean running = true;
    private int ticketNumberCounter = 0;
    private static final long UPDATE_INTERVAL_MS = 100;

    /**
     * Constructs a Vendor object with the specified TicketPool, ticket release rate, and name.
     * @param ticketPool the TicketPool to add tickets to
     * @param ticketReleaseRate the rate at which tickets are released (tickets per second)
     * @param name the name of the vendor (ID)
     */
    public Vendor(TicketPool ticketPool, int ticketReleaseRate, String name) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.name = name;
    }

    /**
     * Runs the vendor thread, adding tickets to the TicketPool at the specified rate.
     * Logs the activity and handles interruptions.
     */
    @Override
    public void run() {
        while (running && TicketPool.isRunning()) {
            try {
                boolean added = ticketPool.addTickets(1);
                if (added) {
                    MongodbConnection.insertTicket(generateTicketNumber());
                    Logger.log(name + " released a ticket. Waiting " + ticketReleaseRate + "ms before next release");
                }
                Thread.sleep(ticketReleaseRate); // Sleep for the configured milliseconds
            } catch (InterruptedException e) {
                Logger.logError(name + " interrupted: " + e.getMessage());
                break;
            }
        }
        Logger.log("Vendor " + name + " stopped");
    }

    /**
     * Stops the vendor from running.
     */
    public void stop() {
        running = false;
    }

    /**
     * Generates a new ticket number in a thread-safe manner.
     * @return the generated ticket number
     */
    private synchronized int generateTicketNumber() {
        return ++ticketNumberCounter;
    }
}
