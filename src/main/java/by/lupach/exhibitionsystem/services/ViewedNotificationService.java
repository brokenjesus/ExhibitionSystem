package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.entities.ViewedNotification;
import by.lupach.exhibitionsystem.repositories.ViewedNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViewedNotificationService {
    @Autowired
    ViewedNotificationRepository viewedNotificationRepository;

    public void save(ViewedNotification viewedNotification) {
        viewedNotificationRepository.save(viewedNotification);
    }

    public Optional<List<ViewedNotification>> getAllByUserId(Integer userId) {
        return Optional.ofNullable(viewedNotificationRepository.findAllNotificationViewedNotificationByUserId(userId));
    }

}
