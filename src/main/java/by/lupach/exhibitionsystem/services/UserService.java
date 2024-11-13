package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.entities.User;
import by.lupach.exhibitionsystem.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        return user.get();
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
        logger.info("User saved successfully: {}", user.getUsername());
    }

    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public Optional<Page<User>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(userRepository.findAll(pageable));
    }
}
