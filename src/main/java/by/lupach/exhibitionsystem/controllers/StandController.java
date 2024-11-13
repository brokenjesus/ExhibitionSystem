package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Material;
import by.lupach.exhibitionsystem.entities.Stand;
import by.lupach.exhibitionsystem.entities.User;
import by.lupach.exhibitionsystem.services.ImageService;
import by.lupach.exhibitionsystem.services.MaterialService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stands")
public class StandController {

    @Autowired
    private StandService standService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MaterialService materialService;  // Add MaterialService to handle materials

    @GetMapping("/create")
    public String showCreateStandForm(Model model) {
        model.addAttribute("materials", new ArrayList<Material>());
        return "create_stand";  // Ensure this matches your template file name
    }

    @PostMapping("/create")
    public String createStand(@ModelAttribute Stand stand,
                              @RequestParam("image") MultipartFile file,
                              @RequestParam Map<String, String> materials) throws IOException {
        // Обработка данных стенда
        String previewUrl = imageService.saveImageToStorage(file);

        // Привязываем изображение к стенду
        stand.setPreviewImageUrl(previewUrl);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User currentUser = userService.loadUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            stand.setExhibitor(currentUser);
        }

        // Сборка списка материалов
        List<Material> materialList = new ArrayList<>();
        int index = 0;
        while (materials.containsKey("materialName" + index)) {
            String materialName = materials.get("materialName" + index);
            String materialDescription = materials.get("materialDescription" + index);
            Material material = Material.builder().type(materialName).content(materialDescription).build();
            materialList.add(material);
            index++;
        }

        // Устанавливаем материалы для стенда
        stand.setMaterials(materialList);
        standService.save(stand);

        return "redirect:/"; // Перенаправление после создания
    }



    @GetMapping("/{id}")
    public String showStandDetails(@PathVariable("id") Integer id, Model model) {
        Stand stand = standService.getById(id).get();
        if (stand != null) {
            model.addAttribute("stand", stand);
            return "stand_details"; // This corresponds to your Thymeleaf template
        } else {
            return "redirect:/"; // Redirect to home if the stand is not found
        }
    }
}
