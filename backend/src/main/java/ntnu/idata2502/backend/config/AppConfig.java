package ntnu.idata2502.backend.config;

import ntnu.idata2502.backend.repositories.UserRepository;
import ntnu.idata2502.backend.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        AuthUtil.setUserRepository(userRepository);
    }
}
