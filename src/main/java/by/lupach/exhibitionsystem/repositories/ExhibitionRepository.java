package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> {

    // Custom query method to search exhibitions by title (case-insensitive)
    Page<Exhibition> findByTitleContainingIgnoreCaseOrderByIdDesc(String query, Pageable pageable);
    Page<Exhibition> findAllByOrderByIdDesc(Pageable pageable);

}
