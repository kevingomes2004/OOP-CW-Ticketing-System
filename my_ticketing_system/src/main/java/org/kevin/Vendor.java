package org.kevin;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final String name;
    private volatile boolean running = true;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, String name) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.name = name;
    }

    @Override
    public void run() {
        while (running) {
            try {
                ticketPool.addTickets(1);
                Thread.sleep(1000 / ticketReleaseRate);
                MongodbConnection.insertTicket(generateTicketNumber());
                Logger.log(name + " released a ticket");
            } catch (InterruptedException e) {
                Logger.logError(name + " interrupted");
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }

    private int generateTicketNumber() {
        // Implement ticket number generation logic
        return (int) (Math.random() * 1000);
    }
}
