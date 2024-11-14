package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Notification;
import by.lupach.exhibitionsystem.entities.NotificationSubscription;
import by.lupach.exhibitionsystem.entities.ViewedNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewedNotificationRepository extends JpaRepository<ViewedNotification, Integer> {
    public List<ViewedNotification> findAllNotificationViewedNotificationByUserId(Integer userId);
}
