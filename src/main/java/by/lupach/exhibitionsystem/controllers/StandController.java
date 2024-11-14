package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Material;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Controller
@RequestMapping("/stands")
public class StandController {

    @Autowired
    private StandService standService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

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

        Notification notification = Notification.builder().type(Notification.NotificationType.STAND).message("Dont miss stand \"" + stand.getName()+"\"").sendDate(Date.valueOf(LocalDate.now())).build();
        notificationService.saveNotification(notification);
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

    @GetMapping("/manage")
    public String showManageStandsPage(Model model) {
        model.addAttribute("stands", standService.getAll().get()); // Fetch all stands
        return "manage_stands";
    }

    @GetMapping("/{id}/edit")
    public String showEditStandForm(@PathVariable("id") Integer id, Model model) {
        Stand stand = standService.getById(id).orElse(null);
        if (stand == null) {
            return "redirect:/stands/manage";
        }
        model.addAttribute("stand", stand);
        model.addAttribute("materials", stand.getMaterials());
        return "edit_stand";
    }

    @PostMapping("/{id}/edit")
    public String editStand(@PathVariable("id") Integer id,
                            @ModelAttribute Stand stand,
                            @RequestParam("image") MultipartFile file,
                            @RequestParam Map<String, String> materials) throws IOException {
        Stand existingStand = standService.getById(id).orElse(null);
        if (existingStand == null) {
            return "redirect:/stands/manage";
        }

        if (!file.isEmpty()) {
            String previewUrl = imageService.saveImageToStorage(file);
            existingStand.setPreviewImageUrl(previewUrl);
        }

        for (Material material : existingStand.getMaterials()) {
            materialService.deleteById(material.getId());
        }

        existingStand.setName(stand.getName());
        existingStand.setDescription(stand.getDescription());
        existingStand.setContacts(stand.getContacts());

        // Обновление материалов
        List<Material> materialList = new ArrayList<>();
        int index = 0;
        while (materials.containsKey("materialName" + index)) {
            String materialName = materials.get("materialName" + index);
            String materialDescription = materials.get("materialDescription" + index);
            Material material = Material.builder().type(materialName).content(materialDescription).build();
            materialList.add(material);
            index++;
        }

        existingStand.setMaterials(materialList);

        standService.save(existingStand);
        return "redirect:/stands/manage";
    }

    @PostMapping("/{id}/delete")
    public String deleteStand(@PathVariable("id") Integer id) {
        standService.deleteById(id);
        return "redirect:/stands/manage";
    }

    @PostMapping("/materials/delete/{id}")
    public String deleteMaterial(@PathVariable("id") Integer materialId) {
        materialService.deleteById(materialId);
        return "redirect:/stands/create";
    }
}