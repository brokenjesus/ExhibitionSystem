package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Exhibition;
import by.lupach.exhibitionsystem.entities.Stand;
import by.lupach.exhibitionsystem.entities.User;
import by.lupach.exhibitionsystem.services.ExhibitionService;
import by.lupach.exhibitionsystem.services.ImageService;
import by.lupach.exhibitionsystem.services.StandService;
import by.lupach.exhibitionsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
}
