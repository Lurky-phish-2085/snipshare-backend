package xyz.lurkyphish2085.snipshare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.lurkyphish2085.snipshare.user.AppUser;
import xyz.lurkyphish2085.snipshare.user.AppUserRepository;

import java.util.List;

@SpringBootApplication
public class SnipShareApplication {

	private final AppUserRepository appUserRepository;

	public SnipShareApplication(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SnipShareApplication.class, args);
	}

	@Bean
	CommandLineRunner testUsers(PasswordEncoder passwordEncoder) throws Exception {
		return (args) -> {
			// Add test app users and save these to db
			AppUser user = new AppUser("user", passwordEncoder.encode("password"), "USER");
			AppUser admin = new AppUser("admin", passwordEncoder.encode("admin"), "ADMIN");

			appUserRepository.saveAll(List.of(user, admin));
		};
	}
}
