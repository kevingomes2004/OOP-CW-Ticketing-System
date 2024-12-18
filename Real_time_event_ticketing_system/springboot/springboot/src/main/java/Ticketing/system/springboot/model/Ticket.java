package Ticketing.system.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a ticket in the ticketing system.
 * Each ticket has a unique ID and a ticket number.
 */
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;
    private int ticketNumber;

    /**
     * Default constructor.
     * Creates an empty ticket.
     */
    public Ticket() {}

    /**
     * Constructor with ticket number.
     * Creates a ticket with the specified ticket number.
     *
     * @param ticketNumber the number of the ticket
     */
    public Ticket(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    /**
     * Gets the ID of the ticket.
     *
     * @return the ID of the ticket
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the ticket.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the number of the ticket.
     *
     * @return the number of the ticket
     */
    public int getTicketNumber() {
        return ticketNumber;
    }

    /**
     * Sets the number of the ticket.
     *
     * @param ticketNumber the number to set
     */
    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    /**
     * Returns a string representation of the ticket.
     * The string includes the ID and ticket number.
     *
     * @return a string representation of the ticket
     */
    @Override
    public String toString() {
        return "Ticket{id='" + id + "', ticketNumber=" + ticketNumber + "}";
    }
}
