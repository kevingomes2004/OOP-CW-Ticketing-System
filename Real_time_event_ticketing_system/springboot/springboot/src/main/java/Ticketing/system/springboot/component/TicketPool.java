package Ticketing.system.springboot.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Component that manages a pool of tickets.
 * This class provides methods for adding and removing tickets from the pool,
 * as well as retrieving the current ticket count and the maximum capacity of the pool.
 */
@Component
public class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final List<Integer> tickets;
    private final int maxCapacity;
    private int ticketCounter = 0;
    private int purchasedTicketCount = 0;

    /**
     * Constructs a TicketPool with the specified maximum capacity.
     *
     * @param maxCapacity the maximum number of tickets the pool can hold
     */
    public TicketPool(@Value("${ticket.pool.maxCapacity}") int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }
    /**
     * Adds a specified number of tickets to the pool.
     *
     * @param count the number of tickets to add
     * @return true if the tickets were added successfully, false if the pool capacity would be exceeded
     */
    public synchronized boolean addTickets(int count) {
        if (tickets.size() + count > maxCapacity) {
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
     * Removes and returns a ticket from the pool. If the pool is empty, waits until a ticket is available.
     *
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
        purchasedTicketCount++; // Increment purchased ticket count when a ticket is removed

        logger.log(Level.INFO, String.format("Removed ticket %d. Remaining tickets: %d", ticket, tickets.size()));
        return ticket;
    }

    /**
     * Returns the current number of tickets in the pool.
     *
     * @return the number of tickets in the pool
     */
    public synchronized int getTicketCount() {
        return tickets.size();
    }

    /**
     * Returns the maximum capacity of the ticket pool.
     *
     * @return the maximum capacity of the ticket pool
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Returns the number of tickets that have been purchased from the pool.
     * @return the number of purchased tickets
     */
    public synchronized int getPurchasedTicketCount() {
        return purchasedTicketCount;
    }
}