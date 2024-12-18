package org.kevin;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TicketPool class manages the pool of tickets for the ticketing system.
 * It provides methods to add and remove tickets, and checks the ticket count.
 * to stop the system when the maximum capacity is reached.
 */
public class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final List<Integer> tickets;
    private final int maxCapacity;
    private int ticketCounter = 0;

    /**
     * Constructs a TicketPool object with the specified maximum capacity.
     * @param maxCapacity the maximum capacity of tickets the pool can hold
     */
    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adds the specified number of tickets to the pool.
     * @param count the number of tickets to add
     * @return true if the tickets were added successfully, false if the pool capacity would be exceeded
     */
    public synchronized boolean addTickets(int count) {
        if (tickets.size() + count >= maxCapacity) {
            logger.log(Level.WARNING, "Cannot add tickets: Pool capacity would be exceeded");
            return false;
        }

        for (int i = 0; i < count; i++) {
            tickets.add(++ticketCounter);
        }
        notifyAll();
        return true;
    }

    /**
     * Removes a tickets from the pool. If the pool is empty, waits until a ticket is available.
     * @return the removed ticket, or null if the thread is interrupted while waiting
     */
    public synchronized Integer removeTicket() {
        while (tickets.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Thread interrupted while waiting for tickets", e);
                return null;
            }
        }

        Integer ticket = tickets.remove(0);
        logger.log(Level.INFO, String.format("Removed ticket %d. Remaining tickets: %d", ticket, tickets.size()));
        checkTicketCount();
        return ticket;
    }

    /**
     * Checks the ticket count and stops the system if the maximum capacity is reached.
     */
    private void checkTicketCount() {
        if (ticketCounter >= maxCapacity) {
            Main.setRunning(false);
            logger.log(Level.INFO, "All tickets sold. Stopping the system.");
        }
    }

    /**
     * Checks if the system is running.
     * @return true if the system is running, false otherwise
     */
    public static boolean isRunning() {
        return Main.isRunning();
    }
}