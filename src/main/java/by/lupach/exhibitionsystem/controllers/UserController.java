package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.User;
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

    @GetMapping("admin/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("admin/signup")
    public String processSignup(@ModelAttribute("newUser") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @GetMapping("edit-profile")
    public String editProfile() {
        return "edit_profile";
    }
    @GetMapping("admin/manage-users/search")
    public String searchUsers(@RequestParam String username, @RequestParam(defaultValue = "0") int page, Model model) {
        User users = userService.loadUserByUsername(username);
        model.addAttribute("users", users);
        return "manage_users";
    }

    @GetMapping("admin/manage-users")
    public String manageUsers(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<User> usersPage = userService.getAll(page, PAGE_SIZE).get();
        model.addAttribute("users", usersPage.getContent());
        return "manage_users";
    }

    @GetMapping("admin/manage-users/delete")
    public String deleteUser(@RequestParam int id) {
        userService.deleteById(id);
        return "manage_users";
    }

    @GetMapping("admin/manage-users/edit")
    public String editUser(@RequestParam String username, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());

        if (currentUser.getRole() != User.Role.ROLE_ADMIN) {
            if (!currentUser.getUsername().equals(username)) {
                return "redirect:/";
            }
        }

        User userToEdit = userService.loadUserByUsername(username);
        model.addAttribute("userToEdit", userToEdit);
        return "edit_profile";
    }

    @PostMapping("admin/manage-users/edit")
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
}
