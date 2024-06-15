package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.repository.UserRepository;
import br.com.apollomusic.app.repository.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        if(user.isPresent()) {
            UserDetails userDetails =
                    org.springframework.security.core.userdetails.User.builder()
                            .username(user.get().getUserName())
                            .roles(roles.toArray(new String[0]))
                            .build();
            return userDetails;

        }
        throw new UsernameNotFoundException("User not found");
    }
}
