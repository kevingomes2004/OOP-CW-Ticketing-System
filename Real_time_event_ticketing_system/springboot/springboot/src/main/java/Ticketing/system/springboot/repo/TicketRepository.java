package Ticketing.system.springboot.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import Ticketing.system.springboot.model.Ticket;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing Ticket documents in the MongoDB database.
 * This interface provides methods for performing CRUD operations and custom queries on Ticket documents.
 */
@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    /**
     * Retrieves the first Ticket document based on the ticket number in ascending order.
     *
     * @return the first Ticket document with the lowest ticket number
     */
    Ticket findFirstByOrderByTicketNumberAsc();
}