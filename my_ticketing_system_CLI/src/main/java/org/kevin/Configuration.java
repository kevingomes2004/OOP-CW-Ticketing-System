package org.kevin;

import com.google.gson.Gson;
import java.io.*;

/**
 * Configuration class to hold the settings for the ticketing system.
 * This class includes methods to save the configuration to a file.
 */
public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Constructs a Configuration object with the specified parameters.
     *
     * @param totalTickets the total number of tickets available
     * @param ticketReleaseRate the rate at which tickets are released (tickets per second)
     * @param customerRetrievalRate the rate at which customers retrieve tickets (tickets per second)
     * @param maxTicketCapacity the maximum capacity of tickets
     */
    public Configuration(int totalTickets, int ticketReleaseRate,
                            int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Gets the total number of tickets.
     *
     * @return the total number of tickets
     */
    public int getTotalTickets() { return totalTickets; }

    /**
     * Gets the ticket release rate.
     *
     * @return the ticket release rate (tickets per second)
     */
    public int getTicketReleaseRate() { return ticketReleaseRate; }

    /**
     * Gets the customer retrieval rate.
     *
     * @return the customer retrieval rate (tickets per second)
     */
    public int getCustomerRetrievalRate() { return customerRetrievalRate; }

    /**
     * Gets the maximum ticket capacity.
     *
     * @return the maximum ticket capacity
     */
    public int getMaxTicketCapacity() { return maxTicketCapacity; }

    /**
     * Saves the configuration to a file in JSON format.
     *
     * @param filename the name of the file to save the configuration to
     */
    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new Gson();
            gson.toJson(this, writer);
            Logger.log("Configuration saved successfully to " + filename);
        } catch (IOException e) {
            Logger.logError("Error saving configuration: " + e.getMessage());
        }
    }
}
