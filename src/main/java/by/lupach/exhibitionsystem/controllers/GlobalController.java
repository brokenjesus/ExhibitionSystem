package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.User;
import by.lupach.exhibitionsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails user = ((UserDetails) authentication.getPrincipal());
            User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            System.out.println(currentUser.getUsername());
            model.addAttribute("currentUser", currentUser);
        }
    }
}
