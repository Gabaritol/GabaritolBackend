package api.gabaritol.repositories.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.gabaritol.entities.user.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    List<User> findByVerifiedFalseAndCreatedAtBefore(LocalDateTime cutoff);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
