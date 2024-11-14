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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ViewedNotificationService viewedNotificationService;
    @Autowired
    private NotificationSubscriptionService notificationSubscriptionService;

//    @GetMapping()
//    public String showNotificationsMenu() {
//        return "notifications";
//    }

//    @PostMapping("/create")
//    public void addNotification(Notification notification) {
//        notificationService.saveNotification(notification);
//    }

    @PostMapping("/mark-all-as-read")
    public String markAllAsRead() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());

            Integer currentUserId = currentUser.getId();  // Replace this with actual user retrieval logic.

            // Retrieve all notifications
            List<Notification> allNotifications = notificationService.getAllNotifications()
                    .orElseThrow(() -> new RuntimeException("No notifications found"));

            // Retrieve all notifications already viewed by this user
            List<ViewedNotification> viewedNotifications = viewedNotificationService.getAllByUserId(currentUserId)
                    .orElse(new ArrayList<ViewedNotification>());

            // Filter out notifications that have already been viewed
            Set<Integer> viewedNotificationIds = viewedNotifications.stream()
                    .map(v -> v.getNotification().getId())
                    .collect(Collectors.toSet());

            List<Notification> unreadNotifications = allNotifications.stream()
                    .filter(notification -> !viewedNotificationIds.contains(notification.getId()))
                    .toList();

            // Mark each unread notification as viewed
            unreadNotifications.forEach(notification -> {
                ViewedNotification viewedNotification = ViewedNotification.builder()
                        .user(currentUser)  // Assuming User has an ID-only constructor or is settable like this
                        .notification(notification)
                        .viewedDate(Date.valueOf(LocalDate.now()))
                        .build();
                viewedNotificationService.save(viewedNotification);
            });
        }
        return "redirect:/";  // Redirect to notifications page or another relevant page.
    }


    @GetMapping("/subscribe")
    public String subscribeStandNotifications(@RequestParam Notification.NotificationType notificationType, @RequestParam String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User subscriptionUser = userService.loadUserByUsername(username);

            NotificationSubscription curentUserNotificationSubscription = notificationSubscriptionService.
                    getNotificationSubscriptionByUserId(subscriptionUser.getId()).orElse(new NotificationSubscription());

            curentUserNotificationSubscription.setUser(subscriptionUser);

            if (notificationType == Notification.NotificationType.STAND) {
                curentUserNotificationSubscription.setStandSubscription(true);
                notificationSubscriptionService.save(curentUserNotificationSubscription);
            }

            if (notificationType == Notification.NotificationType.EXHIBITION) {
                curentUserNotificationSubscription.setExhibitionSubscription(true);
                notificationSubscriptionService.save(curentUserNotificationSubscription);
            }
            return "redirect:/manage-profile/edit?username="+subscriptionUser.getUsername();
        }


        return "redirect:/";
    }

    @GetMapping("/unsubscribe")
    public String unsubscribeStandNotifications(@RequestParam Notification.NotificationType notificationType, @RequestParam String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User subscriptionUser = userService.loadUserByUsername(username);

            NotificationSubscription curentUserNotificationSubscription = notificationSubscriptionService.
                    getNotificationSubscriptionByUserId(subscriptionUser.getId()).orElse(new NotificationSubscription());

            curentUserNotificationSubscription.setUser(subscriptionUser);


            if (notificationType == Notification.NotificationType.STAND) {
                curentUserNotificationSubscription.setStandSubscription(false);
                notificationSubscriptionService.save(curentUserNotificationSubscription);
            }

            if (notificationType == Notification.NotificationType.EXHIBITION) {
                curentUserNotificationSubscription.setExhibitionSubscription(false);
                notificationSubscriptionService.save(curentUserNotificationSubscription);
            }
            return "redirect:/manage-profile/edit?username="+subscriptionUser.getUsername();
        }

        return "redirect:/";
    }
}
