package Ticketing.system.springboot.service;

import Ticketing.system.springboot.model.Ticket;
import Ticketing.system.springboot.repo.ConfigurationRepository;
import Ticketing.system.springboot.repo.TicketRepository;
import Ticketing.system.springboot.component.TicketPool;
import Ticketing.system.springboot.component.ConfigHolder;
import Ticketing.system.springboot.service.LogService;
import Ticketing.system.springboot.model.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class representing a customer that purchases tickets from the ticket pool.
 * This class implements the Runnable interface to allow the customer to run in a separate thread.
 */
@Service
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final TicketRepository ticketRepository;
    private final ConfigHolder configHolder; // Inject the shared config holder
    private volatile boolean running = true;
    private Thread customerThread;

    @Autowired
    private LogService logService;

    @Autowired
    private WebSocketService webSocketService;

    /**
     * Constructs a Customer with the specified ticket pool, ticket repository, and config holder.
     *
     * @param ticketPool the pool of tickets from which the customer can purchase tickets
     * @param ticketRepository the repository for saving purchased tickets
     * @param configHolder the holder for shared configuration settings
     */
    @Autowired
    public Customer(TicketPool ticketPool, TicketRepository ticketRepository, ConfigHolder configHolder) {
        this.ticketPool = ticketPool;
        this.ticketRepository = ticketRepository;
        this.configHolder = configHolder;
    }

    /**
     * The main logic for the customer to purchase tickets.
     * This method is executed when the customer thread is started.
     */
    @Override
    public void run() {
        while (running) {
            synchronized (ticketPool) {
                try {
                    Integer ticketNumber = ticketPool.removeTicket();
                    if (ticketNumber != null) {
                        ticketRepository.save(new Ticket(ticketNumber));
                        logService.logCustomerAction("Customer purchased ticket: " + ticketNumber);  // Log action
                        webSocketService.broadcastTicketUpdate();
                    } else {
                        ticketPool.wait(); // Wait for tickets to be available
                    }
                } catch (Exception e) {
                    logService.logCustomerAction("Customer encountered an issue: " + e.getMessage());  // Log error
                }
            }

            try {
                // Use the current purchase rate from the config holder
                Thread.sleep(configHolder.getPurchaseRate());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Starts the customer thread, allowing the customer to begin purchasing tickets.
     */
    public void startCustomer() {
        running = true;
        customerThread = new Thread(this);
        customerThread.start();
    }

    /**
     * Stops the customer thread, halting the ticket purchasing process.
     */
    public void stopCustomer() {
        running = false;
        if (customerThread != null) {
            customerThread.interrupt();
        }
    }
}