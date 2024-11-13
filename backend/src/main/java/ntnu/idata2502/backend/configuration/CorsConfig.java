package ntnu.idata2502.backend.configuration;

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
                       .allowedOrigins(
                               frontendUrl != null ? frontendUrl : "http://localhost"
                       )
                       .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                       .allowedHeaders("*")
                       .allowCredentials(true);
           }
       };
   }
}
