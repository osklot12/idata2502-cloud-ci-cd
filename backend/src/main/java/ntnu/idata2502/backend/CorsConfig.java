package ntnu.idata2502.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {
   @Bean
   public WebMvcConfigurer corsConfigurer() {
       return new WebMvcConfigurer() {
           @Override
           public void addCorsMappings(CorsRegistry reg) {
               reg.addMapping("/**")
<<<<<<< HEAD:backend/src/main/java/ntnu/idata2502/backend/CorsConfig.java
                       .allowedOrigins("http://localhost")
=======
                       .allowedOrigins(
                               frontendUrl != null ? frontendUrl : "http://localhost:5173"
                       )
>>>>>>> 700247c (Created api for backend and added some styling):backend/src/main/java/ntnu/idata2502/backend/config/CorsConfig.java
                       .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                       .allowedHeaders("*")
                       .allowCredentials(true);
           }
       };
   }
}
