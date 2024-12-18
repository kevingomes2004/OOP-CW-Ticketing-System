package Ticketing.system.springboot.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import Ticketing.system.springboot.model.Configuration;

/**
 * Repository interface for accessing Configuration documents in the MongoDB database.
 * This interface provides methods for performing CRUD operations and custom queries on Configuration documents.
 */
public interface ConfigurationRepository extends MongoRepository<Configuration, String> {

    /**
     * Retrieves the latest Configuration document based on the ID in descending order.
     *
     * @return the latest Configuration document
     */
    Configuration findTopByOrderByIdDesc();  // Get the latest configuration
}
