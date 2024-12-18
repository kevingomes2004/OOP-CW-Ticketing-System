package Ticketing.system.springboot.service;

import Ticketing.system.springboot.model.Ticket;
import Ticketing.system.springboot.repo.TicketRepository;
import Ticketing.system.springboot.component.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import Ticketing.system.springboot.component.ConfigHolder;
import org.springframework.stereotype.Service;

/**
 * Service class representing a vendor that releases tickets into the ticket pool.
 * This class implements the Runnable interface to allow the vendor to run in a separate thread.
 */
@Service
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final TicketRepository ticketRepository;
    private final ConfigHolder configHolder; // Shared configuration holder for dynamic rates
    private volatile boolean running = true;
    private Thread vendorThread;
    private int ticketCounter = 0;

    @Autowired
    private LogService logService;

    @Autowired
    private WebSocketService webSocketService;

    /**
     * Constructs a Vendor with the specified ticket pool, ticket repository, and config holder.
     *
     * @param ticketPool the pool of tickets to which the vendor can release tickets
     * @param ticketRepository the repository for saving released tickets
     * @param configHolder the holder for shared configuration settings
     */
    @Autowired
    public Vendor(TicketPool ticketPool, TicketRepository ticketRepository, ConfigHolder configHolder) {
        this.ticketPool = ticketPool;
        this.ticketRepository = ticketRepository;
        this.configHolder = configHolder;
    }

    /**
     * The main logic for the vendor to release tickets.
     * This method is executed when the vendor thread is started.
     */
    @Override
    public void run() {
        while (running) {
            synchronized (ticketPool) {
                try {
                    if (ticketPool.getTicketCount() < configHolder.getMaxTicketCapacity()) {
                        int ticketNumber = ++ticketCounter;
                        if (ticketPool.addTickets(ticketNumber)) {  // Only proceed if adding was successful
                            ticketRepository.save(new Ticket(ticketNumber));
                            logService.logVendorAction("Vendor released ticket: " + ticketNumber);
                            System.out.println("Vendor released ticket: " + ticketNumber); // Log the action
                            webSocketService.broadcastTicketUpdate();
                            ticketPool.notifyAll(); // Notify waiting customers
                        }
                    } else {
                        logService.logVendorAction("Ticket pool is at maximum capacity. No more tickets will be added.");
                        System.out.println("Ticket pool is at maximum capacity. No more tickets will be added.");
                        stopVendor();// Log the action
                    }
                } catch (Exception e) {
                    logService.logVendorAction("Vendor encountered an issue: " + e.getMessage()); // Log the error
                }
            }
            try {
                // Use the current release rate from the config holder
                Thread.sleep(configHolder.getReleaseRate());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Starts the vendor thread, allowing the vendor to begin releasing tickets.
     */
    public void startVendor() {
        running = true;
        vendorThread = new Thread(this);
        vendorThread.start();
    }

    /**
     * Stops the vendor thread, halting the ticket releasing process.
     */
    public void stopVendor() {
        running = false;
        if (vendorThread != null) {
            vendorThread.interrupt();
        }
    }
}