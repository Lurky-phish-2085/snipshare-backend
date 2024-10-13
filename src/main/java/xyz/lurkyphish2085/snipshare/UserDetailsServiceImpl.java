package xyz.lurkyphish2085.snipshare;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.lurkyphish2085.snipshare.user.AppUser;
import xyz.lurkyphish2085.snipshare.user.AppUserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository repository;

    public UserDetailsServiceImpl(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> user = repository.findByUsername(username);
        AppUser currentUser = user.orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return User.withUsername(username)
                .password(currentUser.getPassword())
                .roles(currentUser.getRole())
                .build();
    }
}
