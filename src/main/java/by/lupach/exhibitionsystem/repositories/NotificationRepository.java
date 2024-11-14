package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    public List<Notification> findAll();
}
