package by.lupach.exhibitionsystem.controllers;

import by.lupach.exhibitionsystem.entities.Exhibition;
import by.lupach.exhibitionsystem.entities.Stand;
import by.lupach.exhibitionsystem.services.ExhibitionService;
import by.lupach.exhibitionsystem.services.StandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static by.lupach.exhibitionsystem.configurations.PageConfig.PAGE_SIZE;

@Controller
public class IndexPageController {

    private final ExhibitionService exhibitionService;
    private final StandService standService;

    @Autowired
    public IndexPageController(ExhibitionService exhibitionService, StandService standService) {
        this.exhibitionService = exhibitionService;
        this.standService = standService;
    }

    @GetMapping("/")
    public String search(@RequestParam(name = "searchType", required = false, defaultValue = "exhibitions") String searchType,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(name = "query", required = false) String query,
                         Model model) {

        // Handle exhibitions search
        if ("exhibitions".equals(searchType)) {
            Page<Exhibition> exhibitions = (query == null || query.isEmpty())
                    ? exhibitionService.getAll(page, PAGE_SIZE).get()
                    : exhibitionService.searchExhibitions(query, page, PAGE_SIZE).get(); // Add this method in your service
            model.addAttribute("exhibitionsPage", exhibitions);
        }
        // Handle stands search
        else if ("stands".equals(searchType)) {
            Page<Stand> stands = (query == null || query.isEmpty())
                    ? standService.getAll(page, PAGE_SIZE).get()
                    : standService.searchStands(query, page, PAGE_SIZE).get(); // Add this method in your service
            model.addAttribute("standsPage", stands);
        }

        model.addAttribute("searchType", searchType);
        model.addAttribute("query", query);
        return "index";
    }
}
