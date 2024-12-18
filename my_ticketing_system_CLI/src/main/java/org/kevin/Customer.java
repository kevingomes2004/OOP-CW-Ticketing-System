package org.kevin;

/**
 * Customer class represents a customer in the ticketing system.
 * Each customer runs in a separate thread and attempts to purchase tickets from the ticket pool.
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;
    private volatile boolean running = true;
    private final int customerRetrievalRate;

    /**
     * Constructors a Customer object with the specified TicketPool and customer ID.
     * @param ticketPool the pool of tickets from which the customer can purchase tickets
     * @param customerId the ID of the customer
     */
    public Customer(TicketPool ticketPool, String customerId, int customerRetrievalRate) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Runs the customer thread, attempting to purchase tickets from the ticket pool.
     */
    @Override
    public void run() {
        Logger.log("Customer " + customerId + " started");
        while (running) {
            try {
                Integer ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    Logger.log(String.format("Customer %s purchased ticket %d", customerId, ticket));
                }
                Thread.sleep(customerRetrievalRate); // Try to purchase every second
            } catch (InterruptedException e) {
                Logger.logError("Customer " + customerId + " interrupted: " + e.getMessage());
                break;
            }
        }
        Logger.log("Customer " + customerId + " stopped");
    }

    /**
     * Stops the customer thread.
     */
    public void stop() {
        running = false;
    }
}
