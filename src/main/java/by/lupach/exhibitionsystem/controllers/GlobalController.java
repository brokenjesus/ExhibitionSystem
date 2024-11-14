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
                    .orElse(NotificationSubscription.builder().standSubscription(false).exhibitionSubscription(false).user(currentUser).build());

            Optional<List<ViewedNotification>> viewedNotifications = viewedNotificationService.getAllByUserId(currentUser.getId());

            boolean hasNewNotification = false;

            List<Notification> notificationsToShow = new ArrayList<>();

            if (notifications.isPresent()) {
                if (notificationSubscription.isExhibitionSubscription() && notificationSubscription.isStandSubscription()) {
                    notificationsToShow = notifications.get();
                }
                if (notificationSubscription.isExhibitionSubscription()){
                    for (Notification notification : notifications.get()) {
                        if (notification.getType() == Notification.NotificationType.EXHIBITION){
                            notificationsToShow.add(notification);
                        }
                    }
                }
                if (notificationSubscription.isStandSubscription()){
                    for (Notification notification : notifications.get()) {
                        if (notification.getType() == Notification.NotificationType.STAND){
                            notificationsToShow.add(notification);
                        }
                    }
                }
                model.addAttribute("notifications", notificationsToShow);

                if (viewedNotifications.isEmpty()) {
                    hasNewNotification = true;
                } else {
                    // Проверяем, есть ли хотя бы одно непросмотренное уведомление
                    for (Notification notification : notifications.get()) {
                        boolean isViewed = viewedNotifications.get().stream()
                                .anyMatch(viewed -> viewed.getNotification().getId().equals(notification.getId()));

                        if (!isViewed) {
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
