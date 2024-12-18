package Ticketing.system.springboot.service;

import Ticketing.system.springboot.component.TicketPool;
import Ticketing.system.springboot.repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import Ticketing.system.springboot.model.Ticket;
import Ticketing.system.springboot.model.TicketUpdateMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for broadcasting ticket updates to WebSocket clients.
 * This class provides methods for sending ticket update messages to subscribed clients.
 */
@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TicketPool ticketPool;

    /**
     * Default constructor for WebSocketService.
     */
    public WebSocketService() {}

    /**
     * Broadcasts a ticket update message to all subscribed clients.
     */
    public void broadcastTicketUpdate() {
        int availableTickets = ticketPool.getTicketCount();
        int purchasedTickets = ticketPool.getPurchasedTicketCount();

        Map<String, Object> ticketUpdate = new HashMap<>();
        ticketUpdate.put("availableTickets", availableTickets);
        ticketUpdate.put("purchasedTickets", purchasedTickets);

        messagingTemplate.convertAndSend("/topic/ticket-updates", ticketUpdate);
    }
}