package Ticketing.system.springboot.Configuration;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket handler for managing ticket notifications.
 */
@Component
public class TicketWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    /**
     * Default constructor for TicketWebSocketHandler.
     */
    public TicketWebSocketHandler() {}

    /**
     * Called after a new WebSocket connection is established.
     *
     * @param session the WebSocket session
     * @throws Exception if an error occurs
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    /**
     * Called after a WebSocket connection is closed.
     *
     * @param session the WebSocket session
     * @param status the close status
     * @throws Exception if an error occurs
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}