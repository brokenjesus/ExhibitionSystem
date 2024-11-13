package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Stand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandRepository extends JpaRepository<Stand, Integer> {
    List<Stand> findByExhibitor_Id(Integer exhibitorId);
    Page<Stand> findByNameContainingIgnoreCaseOrderByIdDesc(String query, Pageable pageable);
    Page<Stand> findAllByOrderByIdDesc(Pageable pageable);
}
