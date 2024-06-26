package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.repository.OwnerRepository;
import br.com.apollomusic.app.repository.UserRepository;
import br.com.apollomusic.app.model.entities.Owner;
import br.com.apollomusic.app.model.entities.User;
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
        Optional<User> userOpt = userRepository.findByUserName(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
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
