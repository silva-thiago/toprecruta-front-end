package br.com.topsolutions.config;

import br.com.topsolutions.entity.User;
import br.com.topsolutions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

  @Bean
  CommandLineRunner seedAdmin(UserRepository repo, PasswordEncoder encoder) {
    return args -> {
      if (repo.existsByEmail("admin@topsolutions.com")) return;

      var admin = User.builder()
        .name("Administrador")
        .email("admin@topsolutions.com")
        .password(encoder.encode("Admin@1234"))
        .role(User.Role.ADMIN)
        .build();

      repo.save(admin);
      log.info("Admin inicial criado: admin@topsolutions.com / Admin@1234");
    };
  }
}
