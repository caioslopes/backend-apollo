package br.com.apollomusic.app.repository;

import br.com.apollomusic.app.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByUserName(String userName);
}
