package org.kevin;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final List<Integer> tickets;
    private final int maxCapacity;
    private int ticketCounter = 0;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }

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
        return ticket;
    }
}