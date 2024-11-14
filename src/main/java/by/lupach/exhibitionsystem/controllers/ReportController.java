package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Report;
import by.lupach.exhibitionsystem.entities.Exhibition;
import by.lupach.exhibitionsystem.entities.Stand;
import by.lupach.exhibitionsystem.repositories.ExhibitionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController {

    private final ExhibitionRepository exhibitionRepository;

    public ReportController(ExhibitionRepository exhibitionRepository) {
        this.exhibitionRepository = exhibitionRepository;
    }

    @GetMapping("/report")
    public String generateReports(Model model) {
        List<Report> reports = new ArrayList<>();

        // Fetch all exhibitions from the database
        List<Exhibition> exhibitions = exhibitionRepository.findAll();

        // Sort exhibitions by end date in descending order
        exhibitions.sort(Comparator.comparing(Exhibition::getEndDate).reversed());

        // Take the first 10 exhibitions
        List<Exhibition> topExhibitions = exhibitions.size() > 10 ? exhibitions.subList(0, 10) : exhibitions;

        // Generate reports for the top 10 exhibitions
        for (Exhibition exhibition : topExhibitions) {
            Map<String, String> reportData = new HashMap<>();
            int likesCount = exhibition.getLikesCount();
            int registrationsCount = exhibition.getLikes().size(); // Assuming "likes" means registrations here

            StringBuilder exhibitionInfo = new StringBuilder();
            exhibitionInfo.append("Likes: ").append(likesCount)
                    .append(", Registrations: ").append(registrationsCount);

            // Add associated stands and their like counts
            for (Stand stand : exhibition.getStands()) {
                exhibitionInfo.append(", Stand ").append(stand.getName())
                        .append(" Likes: ").append(stand.getLikesCount());
            }

            reportData.put(exhibition.getTitle()+exhibition.getStartDate()+" - "+exhibition.getEndDate(), exhibitionInfo.toString());
            Report report = Report.builder().event(exhibition).data(reportData).build();
            reports.add(report);
        }

        model.addAttribute("reports", reports);
        return "report";
    }
}
