package ru.vniims.portal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vniims.portal.domains.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
