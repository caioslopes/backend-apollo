package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.infra.repository.OwnerRepository;
import br.com.apollomusic.app.infra.repository.UserRepository;
import br.com.apollomusic.app.domain.entities.Owner;
import br.com.apollomusic.app.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, OwnerRepository ownerRepository) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .roles("USER")
                    .build();
        }

        Optional<Owner> ownerOpt = ownerRepository.findByEmail(username);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(owner.getEmail())
                    .roles("ADMIN")
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
