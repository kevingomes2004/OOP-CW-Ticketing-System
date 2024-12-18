package Ticketing.system.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class for the Spring Boot application.
 * This class is responsible for bootstrapping the application.
 */
@SpringBootApplication
@ComponentScan(basePackages = "Ticketing.system.springboot")
class SpringbootApplication {

	/**
	 * The main method that serves as the entry point for the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
}