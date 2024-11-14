package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Exhibition;
import by.lupach.exhibitionsystem.entities.ExhibitionsToVisit;
import by.lupach.exhibitionsystem.entities.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExhibitionsToVisitRepository extends JpaRepository<ExhibitionsToVisit, Integer> {
    Page<ExhibitionsToVisit> findAllByOrderByIdDesc(Pageable pageable);
    Optional<ExhibitionsToVisit> findByUser_IdAndExhibition_Id(Integer userId, Integer exhibitionId);
    Optional<Page<ExhibitionsToVisit>> getAllByUserId(Integer userId, Pageable pageable);
    int countByExhibitionId(Integer exhibitionId);
}
