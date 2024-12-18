package Ticketing.system.springboot.service;

import Ticketing.system.springboot.model.Ticket;
import Ticketing.system.springboot.repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for managing tickets in the ticketing system.
 * This class provides methods for retrieving, adding, and removing tickets.
 * It also broadcasts ticket updates to WebSocket clients.
 */
@Service
public class TicketService {
    private static final Logger logger = Logger.getLogger(TicketService.class.getName());

    @Autowired
    private TicketRepository ticketRepository;

    private final Object lock = new Object();
    private WebSocketService webSocketService;
    private List<Ticket> remainingTickets;

    /**
     * Default constructor for TicketService.
     * Initializes the ticket service with an empty list of remaining tickets.
     */
    public TicketService() {
        this.remainingTickets = new ArrayList<>();
    }

    /**
     * Retrieves all tickets from the database.
     *
     * @return a list of all tickets
     */
    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Adds a new ticket with the specified ticket number to the database.
     * This method is synchronized to ensure thread safety.
     *
     * @param ticketNumber the number of the ticket to be added
     * @return the added ticket
     */
    public synchronized Ticket addTicket(int ticketNumber) {
        synchronized (lock) {
            Ticket ticket = new Ticket(ticketNumber);
            Ticket savedTicket = ticketRepository.save(ticket);
            remainingTickets.add(savedTicket);
            logger.info(String.format("Added ticket %d to pool. Current size: %d", ticketNumber, getTicketCount()));
            lock.notifyAll();
            return savedTicket;
        }
    }

    /**
     * Retrieves the total count of tickets in the database.
     *
     * @return the total count of tickets
     */
    public long getTicketCount() {
        return ticketRepository.count();
    }

    /**
     * Removes the first ticket from the database.
     * This method is synchronized to ensure thread safety.
     * If no tickets are available, the method waits until a ticket is added.
     *
     * @return the removed ticket, or null if the thread is interrupted
     */
    public synchronized Ticket removeTicket() {
        synchronized (lock) {
            while (ticketRepository.count() == 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Thread interrupted while waiting for tickets", e);
                    return null;
                }
            }
            List<Ticket> tickets = ticketRepository.findAll();
            Ticket ticket = tickets.get(0);
            ticketRepository.delete(ticket);
            remainingTickets.remove(ticket);
            logger.info(String.format("Removed ticket %d. Remaining tickets: %d", ticket.getTicketNumber(), getTicketCount()));
            return ticket;
        }
    }
}