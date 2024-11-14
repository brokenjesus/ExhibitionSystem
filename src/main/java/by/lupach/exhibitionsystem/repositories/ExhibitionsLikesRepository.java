package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.ExhibitionsLikes;
import by.lupach.exhibitionsystem.entities.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionsLikesRepository extends JpaRepository<ExhibitionsLikes, Integer> {
    int countByExhibitionId(Integer exhibitionId);
}
