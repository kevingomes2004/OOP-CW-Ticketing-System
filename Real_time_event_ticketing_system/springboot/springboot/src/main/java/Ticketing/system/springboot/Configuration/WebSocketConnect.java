package Ticketing.system.springboot.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS mappings for WebSocket connections.
 */
@Configuration
@EnableWebMvc
public class WebSocketConnect implements WebMvcConfigurer {

    /**
     * Default constructor for WebSocketConnect.
     */
    public WebSocketConnect() {}

    /**
     * Configures CORS mappings.
     *
     * @param registry the CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}