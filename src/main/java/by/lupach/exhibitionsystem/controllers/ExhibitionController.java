package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Exhibition;
import by.lupach.exhibitionsystem.entities.Notification;
import by.lupach.exhibitionsystem.entities.Stand;
import by.lupach.exhibitionsystem.entities.User;
import by.lupach.exhibitionsystem.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/exhibitions")
public class ExhibitionController {

    @Autowired
    private ExhibitionService exhibitionService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StandService standService;
    @Autowired
    private ImageService imageService;

    // Display the form to create a new exhibition
    @GetMapping("/create")
    public String showCreateExhibitionForm(Model model) {
        model.addAttribute("stands", standService.getAll().get());
        return "create_exhibition";  // Ensure this matches your template file name
    }

    // Handle form submission to create a new exhibition
    @PostMapping("/create")
    public String createExhibition(@ModelAttribute Exhibition exhibition, @RequestParam("image") MultipartFile file,
                                   @RequestParam List<Integer> selectedStands) throws IOException {
        String previewUrl = imageService.saveImageToStorage(file);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            exhibition.setOrganizer(currentUser);
        }



        exhibition.setPreviewImageUrl(previewUrl);
        List<Stand> stands = standService.getAllById(selectedStands).get();
        exhibition.setStands(stands);
        exhibitionService.save(exhibition);

        Notification notification = Notification.builder().type(Notification.NotificationType.EXHIBITION)
                .message("Dont miss stand +\"" + exhibition.getTitle() + "\" \n" + exhibition.getStartDate()
                        + " - " + exhibition.getEndDate() + "\n Organizer: " + exhibition.getOrganizer().getName()).sendDate(Date.valueOf(LocalDate.now())).build();
        notificationService.saveNotification(notification);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String showExhibitionDetails(@PathVariable("id") int exhibitionId, Model model) {
        Optional<Exhibition> exhibition = exhibitionService.getById(exhibitionId);
        if (exhibition.isPresent()) {
            model.addAttribute("exhibition", exhibition.get());
            return "exhibition_details";  // Thymeleaf template for exhibition details
        } else {
            return "redirect:/";  // Redirect to the homepage if exhibition is not found
        }
    }

    // Display all exhibitions with edit and delete options
    @GetMapping("/manage")
    public String manageExhibitions(Model model) {
        List<Exhibition> exhibitions = exhibitionService.getAll().orElse(List.of());
        model.addAttribute("exhibitions", exhibitions);
        return "manage_exhibitions";
    }

    // Show the form to edit an exhibition
    @GetMapping("/edit/{id}")
    public String showEditExhibitionForm(@PathVariable("id") int id, Model model) {
        Optional<Exhibition> exhibition = exhibitionService.getById(id);
        if (exhibition.isPresent()) {
            model.addAttribute("exhibition", exhibition.get());
            model.addAttribute("stands", standService.getAll().orElse(List.of()));
            return "edit_exhibition";
        }
        return "redirect:/exhibitions/manage"; // Redirect if exhibition not found
    }

    // Handle the form submission to update an exhibition
    @PostMapping("/edit/{id}")
    public String editExhibition(@PathVariable("id") int id, @ModelAttribute Exhibition updatedExhibition,
                                 @RequestParam("image") MultipartFile file, @RequestParam List<Integer> selectedStands) throws IOException {
        Optional<Exhibition> exhibitionOpt = exhibitionService.getById(id);
        if (exhibitionOpt.isPresent()) {
            Exhibition exhibition = exhibitionOpt.get();
            exhibition.setTitle(updatedExhibition.getTitle());
            exhibition.setDescription(updatedExhibition.getDescription());
            exhibition.setStartDate(updatedExhibition.getStartDate());
            exhibition.setEndDate(updatedExhibition.getEndDate());

            if (!file.isEmpty()) {
                String previewUrl = imageService.saveImageToStorage(file);
                exhibition.setPreviewImageUrl(previewUrl);
            }

            List<Stand> stands = standService.getAllById(selectedStands).orElse(List.of());
            exhibition.setStands(stands);
            exhibitionService.save(exhibition);
        }
        return "redirect:/exhibitions/manage";
    }

    // Delete an exhibition
    @GetMapping("/delete/{id}")
    public String deleteExhibition(@PathVariable("id") int id) {
        exhibitionService.deleteById(id);
        return "redirect:/exhibitions/manage";
    }

}
