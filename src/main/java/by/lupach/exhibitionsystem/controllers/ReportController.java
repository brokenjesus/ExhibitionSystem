package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Exhibition;
import by.lupach.exhibitionsystem.entities.Report;

import by.lupach.exhibitionsystem.repositories.ReportRepository;
import by.lupach.exhibitionsystem.services.ExhibitionService;
import by.lupach.exhibitionsystem.services.ExhibitionsLikesService;
import by.lupach.exhibitionsystem.services.ExhibitionsToVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ReportController {

    @Autowired
    private ExhibitionService exhibitionService;

    @Autowired
    private ExhibitionsLikesService exhibitionsLikesService;

    @Autowired
    private ExhibitionsToVisitService exhibitionsToVisitService;

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/report")
    public String generateReport(Model model) {
        Map<String, String> data = new HashMap<>();

        // Iterate over all exhibitions
        for (Exhibition exhibition : exhibitionService.getAll().get()) {
            // Get the count of likes for the exhibition
            long likesCount = exhibitionsLikesService.countByExhibitionId(exhibition.getId());

            // Get the count of users who registered to visit the exhibition
            long registrationsCount = exhibitionsToVisitService.countByExhibitionId(exhibition.getId());

            // Store the counts in the data map
            String value = "Likes: " + likesCount + ", Registrations: " + registrationsCount;
            data.put(exhibition.getTitle(), value);
        }

        // Create and save the report
        Report report = Report.builder()
                .event(null) // You can set the specific event if needed
                .data(data)
                .build();
        reportRepository.save(report);

        // Add report data to model for view rendering
        model.addAttribute("report", report);

        return "report"; // The name of the template to render the report
    }

}
