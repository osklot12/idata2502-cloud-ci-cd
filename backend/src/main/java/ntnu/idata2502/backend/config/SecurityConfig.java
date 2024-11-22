package ntnu.idata2502.backend.config;

import ntnu.idata2502.backend.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Handles security configurations for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // name of the environment variable associated with allowed cors origins
    private static final String CORS_ORIGIN_ENV_VAR = "CORS_ALLOWED_ORIGINS";

    // default fallback allowed cors origin if env variable is missing
    private static final String DEFAULT_CORS_ORIGIN = "http://localhost:80";

    /**
     * Creates a new Security Config instance.
     *
     * @param jwtAuthenticationFilter the jwt authentication filter to use
     */
    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures security.
     *
     * @param http the http security to configure
     * @return the configured http security
     * @throws Exception thrown if configuration fails
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                // disable unused configuration
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // use cors
                .cors(Customizer.withDefaults())
                // disable csrf protection
                .csrf(AbstractHttpConfigurer::disable)
                // use stateless session management for JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // define authorization rules for endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides the authentication manager bean, required for managing authentication processes.
     *
     * @param authenticationConfiguration the authentication configuration provided by Spring
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a password encoder bean for securely hashing and verifying passwords.
     * Uses BCrypt, a widely used hashing algorithm.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings for the application.
     * Retrieves allowed origins from an environment variable, with a fallback to a default value.
     * Allows specific HTTP methods, headers, and credentials.
     *
     * @return a UrlBasedCorsConfigurationSource with the configured CORS settings
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                getAllowedOrigins().split(",")
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true); // If using cookies or Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Retrieves the allowed origins for CORS configuration from the environment.
     * If the environment variable is not set, a default origin is used.
     *
     * @return a comma-separated string of allowed origins
     */
    private String getAllowedOrigins() {
        String allowedOrigins = System.getenv(CORS_ORIGIN_ENV_VAR);
        if (allowedOrigins == null) {
            allowedOrigins = DEFAULT_CORS_ORIGIN;
        }

        System.out.println(allowedOrigins);
        return allowedOrigins;
    }
}

