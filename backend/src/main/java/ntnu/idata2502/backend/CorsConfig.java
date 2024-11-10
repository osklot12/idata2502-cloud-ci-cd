package ntnu.idata2502.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {
   private final Environment env;

   public CorsConfig(Environment env) {
       this.env = env;
   }

   @Bean
   public WebMvcConfigurer corsConfigurer() {
       return new WebMvcConfigurer() {
           @Override
           public void addCorsMappings(CorsRegistry reg) {
               String frontendUrl = env.getProperty("FRONTEND_EXT_URL");
               // fallback for local testing
               if (frontendUrl == null) {
                   frontendUrl = "http://localhost";
               }

               reg.addMapping("/**")
                       .allowedOrigins(frontendUrl)
                       .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                       .allowedHeaders("*")
                       .allowCredentials(true);
           }
       };
   }
}
