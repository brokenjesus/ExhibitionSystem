package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    Page<User> findAll(Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}