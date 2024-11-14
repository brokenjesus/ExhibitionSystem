package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.entities.NotificationSubscription;
import by.lupach.exhibitionsystem.repositories.NotificationSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationSubscriptionService {
    @Autowired
    NotificationSubscriptionRepository notificationSubscriptionRepository;

    public void save(NotificationSubscription notificationSubscription){
        notificationSubscriptionRepository.save(notificationSubscription);
    }

    public Optional<NotificationSubscription> getNotificationSubscriptionByUserId(Integer userId){
        return Optional.ofNullable(notificationSubscriptionRepository.findNotificationSubscriptionByUserId(userId));
    }
}
