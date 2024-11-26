package org.kevin;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;
    private volatile boolean running = true;

    public Customer(TicketPool ticketPool, String customerId) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        Logger.log("Customer " + customerId + " started");
        while (running) {
            try {
                Integer ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    Logger.log(String.format("Customer %s purchased ticket %d", customerId, ticket));
                }
                Thread.sleep(1000); // Try to purchase every second
            } catch (InterruptedException e) {
                Logger.logError("Customer " + customerId + " interrupted: " + e.getMessage());
                break;
            }
        }
        Logger.log("Customer " + customerId + " stopped");
    }

    public void stop() {
        running = false;
    }
}
