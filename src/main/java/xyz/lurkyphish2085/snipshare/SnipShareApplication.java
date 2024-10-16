package xyz.lurkyphish2085.snipshare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.lurkyphish2085.snipshare.auth.Roles;
import xyz.lurkyphish2085.snipshare.user.AppUser;
import xyz.lurkyphish2085.snipshare.user.AppUserRepository;

import java.util.List;

@SpringBootApplication
public class SnipShareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnipShareApplication.class, args);
	}

	@Bean
	CommandLineRunner testUsers(AppUserRepository repository, PasswordEncoder passwordEncoder) throws Exception {
		return (args) -> {
			// Add test app users and save these to db
			AppUser user = new AppUser("user", passwordEncoder.encode("password"), Roles.USER.name());
			AppUser admin = new AppUser("admin", passwordEncoder.encode("admin"), Roles.ADMIN.name());

			repository.saveAll(List.of(user, admin));
		};
	}
}
