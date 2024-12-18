package Ticketing.system.springboot.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import Ticketing.system.springboot.model.Log;

/**
 * Repository interface for accessing Log documents in the MongoDB database.
 * This interface provides methods for performing CRUD operations on Log documents.
 */
public interface LogRepository extends MongoRepository<Log, String> {
}