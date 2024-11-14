package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Notification;
import by.lupach.exhibitionsystem.entities.NotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Integer> {
    public NotificationSubscription findNotificationSubscriptionByUserId(Integer userId);
}
