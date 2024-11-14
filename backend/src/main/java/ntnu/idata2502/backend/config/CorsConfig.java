package ntnu.idata2502.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {
   @Bean
   public WebMvcConfigurer corsConfigurer() {
       return new WebMvcConfigurer() {
           @Override
           public void addCorsMappings(CorsRegistry reg) {
               String frontendUrl = System.getenv("FRONTEND_URL");
               reg.addMapping("/**")
<<<<<<< HEAD:backend/src/main/java/ntnu/idata2502/backend/CorsConfig.java
<<<<<<< HEAD:backend/src/main/java/ntnu/idata2502/backend/CorsConfig.java
                       .allowedOrigins("http://localhost")
=======
                       .allowedOrigins(
                               frontendUrl != null ? frontendUrl : "http://localhost:5173"
                       )
>>>>>>> 700247c (Created api for backend and added some styling):backend/src/main/java/ntnu/idata2502/backend/config/CorsConfig.java
=======
                       .allowedOrigins(
                               frontendUrl != null ? frontendUrl : "http://localhost:5173"
                       )
>>>>>>> backend:backend/src/main/java/ntnu/idata2502/backend/config/CorsConfig.java
                       .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                       .allowedHeaders("*")
                       .allowCredentials(true);
           }
       };
   }
}
