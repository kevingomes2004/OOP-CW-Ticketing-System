package Ticketing.system.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the configuration settings for the ticketing system.
 */
@Document(collection = "configurations")
public class Configuration {

    @Id
    private String id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Default constructor.
     */
    public Configuration() {}

    /**
     * Constructor with fields.
     *
     * @param totalTickets the total number of tickets
     * @param ticketReleaseRate the rate at which tickets are released
     * @param customerRetrievalRate the rate at which customers retrieve tickets
     * @param maxTicketCapacity the maximum capacity of tickets
     */
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Gets the ID of the configuration.
     *
     * @return the ID of the configuration
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the configuration.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the total number of tickets.
     *
     * @return the total number of tickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Sets the total number of tickets.
     *
     * @param totalTickets the total number of tickets to set
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the rate at which tickets are released.
     *
     * @return the ticket release rate
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Sets the rate at which tickets are released.
     *
     * @param ticketReleaseRate the ticket release rate to set
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Gets the rate at which customers retrieve tickets.
     *
     * @return the customer retrieval rate
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Sets the rate at which customers retrieve tickets.
     *
     * @param customerRetrievalRate the customer retrieval rate to set
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Gets the maximum capacity of tickets.
     *
     * @return the maximum ticket capacity
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Sets the maximum capacity of tickets.
     *
     * @param maxTicketCapacity the maximum ticket capacity to set
     */
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Returns a string representation of the configuration.
     *
     * @return a string representation of the configuration
     */
    @Override
    public String toString() {
        return "Configuration{id='" + id + "', totalTickets=" + totalTickets + ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate + ", maxTicketCapacity=" + maxTicketCapacity + "}";
    }
}
