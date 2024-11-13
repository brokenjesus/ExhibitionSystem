package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
}
