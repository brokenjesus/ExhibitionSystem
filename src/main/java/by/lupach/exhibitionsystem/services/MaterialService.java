package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.entities.Material;
import by.lupach.exhibitionsystem.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Optional<Material> getMaterialById(Integer id) {
        return materialRepository.findById(id);
    }

    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }

    public void deleteById(Integer id) {
        materialRepository.deleteById(id);
    }
}
