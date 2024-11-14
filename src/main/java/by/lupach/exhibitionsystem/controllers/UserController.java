package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.NotificationSubscription;
import by.lupach.exhibitionsystem.entities.User;
import by.lupach.exhibitionsystem.services.NotificationSubscriptionService;
import by.lupach.exhibitionsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

import static by.lupach.exhibitionsystem.configurations.PageConfig.PAGE_SIZE;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NotificationSubscriptionService notificationSubscriptionService;

    @GetMapping("signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("signup")
    public String processSignup(@ModelAttribute("newUser") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == User.Role.ROLE_EXHIBITOR){
            user.setEnabled(false);
        }
        userService.saveUser(user);
        return "redirect:/admin/manage-users";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("login-processing")
    public String processLogin(String username, String password) {
        userService.loadUserByUsername(username);
        return "redirect:/";
    }

//    @GetMapping("edit-profile")
//    public String editProfile(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
//
//        NotificationSubscription notificationSubscription = notificationSubscriptionService.
//                getNotificationSubscriptionByUserId(currentUser.getId()).orElse(null);
//        model.addAttribute("notificationSubscription", notificationSubscription);
//        return "edit_profile";
//    }
    @GetMapping("admin/manage-users/search")
    public String searchUsers(@RequestParam String username, @RequestParam(defaultValue = "0") int page, Model model) {
        User users = userService.loadUserByUsername(username);
        model.addAttribute("users", users);
        return "manage_users";
    }

    @GetMapping("admin/manage-users")
    public String manageUsers(@RequestParam(defaultValue = "0") int page, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());


        if (currentUser.isAdmin()){
            Page<User> usersPage = userService.getAll(page, PAGE_SIZE).get();
            model.addAttribute("usersPage", usersPage);
            return "manage_users";
        }else{
            return "redirect:/";
        }
    }

    @GetMapping("manage-profile/delete")
    public String deleteUser(@RequestParam int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());


        if (currentUser.isAdmin() || currentUser.getId() != id){
            userService.deleteById(id);
        }
        return "redirect:admin/manage-users";
    }

    @GetMapping("manage-profile/edit")
    public String editUser(@RequestParam String username, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());

        if (currentUser.getRole() != User.Role.ROLE_ADMIN) {
            if (!currentUser.getUsername().equals(username)) {
                return "redirect:/";
            }
        }

        User userToEdit = userService.loadUserByUsername(username);

        NotificationSubscription notificationSubscription = notificationSubscriptionService.
                getNotificationSubscriptionByUserId(userToEdit.getId()).orElse(new NotificationSubscription());
        model.addAttribute("notificationSubscription", notificationSubscription);
        model.addAttribute("userToEdit", userToEdit);
        return "edit_profile";
    }

    @PostMapping("manage-profile/edit")
    public String processEditUser(@ModelAttribute("userToEdit") User editedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());

        User userToEdit = userService.loadUserByUsername(editedUser.getUsername());
        if (currentUser.getRole() != User.Role.ROLE_ADMIN) {
            if (currentUser.getId() != userToEdit.getId()) {
                return "redirect:/";
            }
        }

        userToEdit.setName(editedUser.getName());
        userToEdit.setEmail(editedUser.getEmail());
        userToEdit.setPhone(editedUser.getPhone());
        userToEdit.setRole(editedUser.getRole());
        userToEdit.setPassword(passwordEncoder.encode(editedUser.getPassword()));
        userService.saveUser(userToEdit);
        if (Objects.equals(currentUser.getUsername(), editedUser.getUsername())) {
            return "redirect:/logout";
        }else{
            return "redirect:/admin/manage-users";
        }
    }

    @GetMapping("admin/manage-users/activate")
    public String processActivateUser(@RequestParam String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());

        if (currentUser.isAdmin()){
            User userToActivate = userService.loadUserByUsername(username);
            userToActivate.setEnabled(true);
            userService.saveUser(userToActivate);
        }

        return "redirect:/admin/manage-users";
    }


    @GetMapping("admin/manage-users/deactivate")
    public String processDeactivateUser(@RequestParam String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());

        if (currentUser.isAdmin()){
            User userToActivate = userService.loadUserByUsername(username);
            userToActivate.setEnabled(false);
            userService.saveUser(userToActivate);
        }

        return "redirect:/admin/manage-users";
    }
}
