package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Notification;
import by.lupach.exhibitionsystem.entities.NotificationSubscription;
import by.lupach.exhibitionsystem.entities.User;
import by.lupach.exhibitionsystem.entities.ViewedNotification;
import by.lupach.exhibitionsystem.services.NotificationService;
import by.lupach.exhibitionsystem.services.NotificationSubscriptionService;
import by.lupach.exhibitionsystem.services.UserService;
import by.lupach.exhibitionsystem.services.ViewedNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationSubscriptionService notificationSubscriptionService;
    @Autowired
    private ViewedNotificationService viewedNotificationService;


    @ModelAttribute("user")
    public void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            Optional<List<Notification>> notifications = notificationService.getAllNotifications();

            model.addAttribute("currentUser", currentUser);

            NotificationSubscription notificationSubscription = notificationSubscriptionService
                    .getNotificationSubscriptionByUserId(currentUser.getId())
                    .orElse(NotificationSubscription.builder()
                            .standSubscription(false)
                            .exhibitionSubscription(false)
                            .user(currentUser)
                            .build());

            Optional<List<ViewedNotification>> viewedNotifications = viewedNotificationService.getAllByUserId(currentUser.getId());

            boolean hasNewNotification = false;

            List<Notification> notificationsToShow = new ArrayList<>();

            if (notifications.isPresent()) {
                List<Notification> allNotifications = notifications.get();

                // Фильтрация уведомлений в зависимости от подписок пользователя
                if (notificationSubscription.isExhibitionSubscription()) {
                    notificationsToShow.addAll(
                            allNotifications.stream()
                                    .filter(notification -> notification.getType() == Notification.NotificationType.EXHIBITION)
                                    .toList()
                    );
                }
                if (notificationSubscription.isStandSubscription()) {
                    notificationsToShow.addAll(
                            allNotifications.stream()
                                    .filter(notification -> notification.getType() == Notification.NotificationType.STAND)
                                    .toList()
                    );
                }

                // Сортировка уведомлений по дате (предполагается, что есть поле `date`)
                notificationsToShow = notificationsToShow.stream()
                        .sorted((n1, n2) -> n2.getSendDate().compareTo(n1.getSendDate())) // Сортировка по убыванию даты
                        .limit(5) // Оставить только 5 последних
                        .toList();

                model.addAttribute("notifications", notificationsToShow);

                // Проверка на наличие непросмотренных уведомлений
                if (viewedNotifications.isEmpty() && (notificationSubscription.isExhibitionSubscription() || notificationSubscription.isStandSubscription())) {
                    hasNewNotification = true;
                } else {
                    for (Notification notification : allNotifications) {
                        boolean isViewed = viewedNotifications.get().stream()
                                .anyMatch(viewed -> viewed.getNotification().getId().equals(notification.getId()));

                        if (!isViewed  && (notificationSubscription.isExhibitionSubscription() || notificationSubscription.isStandSubscription())) {
                            hasNewNotification = true;
                            break;
                        }
                    }
                }
            }

            model.addAttribute("newNotification", hasNewNotification);
        }
    }




}
