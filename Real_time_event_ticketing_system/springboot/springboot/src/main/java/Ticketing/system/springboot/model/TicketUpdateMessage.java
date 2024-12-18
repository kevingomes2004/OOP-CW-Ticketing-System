package Ticketing.system.springboot.model;

import java.util.List;

/**
 * Represents a message containing updates about tickets in the ticketing system.
 * Each message includes a type, a list of tickets, and an additional message.
 */
public class TicketUpdateMessage {
    private String type;
    private List<Ticket> tickets;
    private String message;

    /**
     * Constructor with fields.
     * Creates a ticket update message with the specified type, list of tickets, and message.
     *
     * @param type the type of the update message
     * @param tickets the list of tickets included in the update
     * @param message an additional message providing more details about the update
     */
    public TicketUpdateMessage(String type, List<Ticket> tickets, String message) {
        this.type = type;
        this.tickets = tickets;
        this.message = message;
    }

    /**
     * Gets the type of the update message.
     *
     * @return the type of the update message
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the update message.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the list of tickets included in the update.
     *
     * @return the list of tickets
     */
    public List<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Sets the list of tickets included in the update.
     *
     * @param tickets the list of tickets to set
     */
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Gets the additional message providing more details about the update.
     *
     * @return the additional message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the additional message providing more details about the update.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}