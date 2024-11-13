package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.entities.Exhibition;
import by.lupach.exhibitionsystem.repositories.ExhibitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    @Autowired
    public ExhibitionService(ExhibitionRepository exhibitionRepository) {
        this.exhibitionRepository = exhibitionRepository;
    }

    public Optional<List<Exhibition>> getAll() {
        return Optional.of(exhibitionRepository.findAll());
    }

    public Optional<Page<Exhibition>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(exhibitionRepository.findAllByOrderByIdDesc(pageable));
    }

    public Optional<Page<Exhibition>> searchExhibitions(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.of(exhibitionRepository.findByTitleContainingIgnoreCaseOrderByIdDesc(query, pageable));
    }

    public Optional<Exhibition> getById(Integer id) {
        return Optional.of(exhibitionRepository.getById(id));
    }

    public void save(Exhibition exhibition) {
        exhibitionRepository.save(exhibition);
    }
}
