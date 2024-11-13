package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.entities.Material;
import by.lupach.exhibitionsystem.entities.Stand;
import by.lupach.exhibitionsystem.repositories.StandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StandService {

    @Autowired
    MaterialService materialService;

    private final StandRepository standRepository;

    @Autowired
    public StandService(StandRepository standRepository) {
        this.standRepository = standRepository;
    }

    // Get Stand by ID
    public Optional<Stand> getStandById(Integer id) {
        return standRepository.findById(id);
    }

    public Optional<List<Stand>> getAll() {
        return Optional.of(standRepository.findAll());
    }

    public Optional<Page<Stand>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(standRepository.findAllByOrderByIdDesc(pageable));
    }

    public Optional<Page<Stand>> searchStands(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.of(standRepository.findByNameContainingIgnoreCaseOrderByIdDesc(query, pageable));
    }


    public Optional<List<Stand>> getAllById(List<Integer> selectedStands) {
        return Optional.of(standRepository.findAllById(selectedStands));
    }

    public Optional<Stand> getById(Integer id) {
        return standRepository.findById(id);
    }


    // Save a Stand
    @Transactional
    public void save(Stand stand) {
        // Save materials first, so they are properly persisted
        for (Material material : stand.getMaterials()) {
            materialService.saveMaterial(material);
        }

        // Save the stand with its materials
        standRepository.save(stand);
    }

    // Delete Stand by ID
    public void delete(Integer id) {
        standRepository.deleteById(id);
    }
}
