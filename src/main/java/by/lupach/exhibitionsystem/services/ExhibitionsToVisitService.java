package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.entities.ExhibitionsToVisit;
import by.lupach.exhibitionsystem.repositories.ExhibitionsToVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionsToVisitService {

    @Autowired
    private ExhibitionsToVisitRepository exhibitionsToVisitRepository;


    public Optional<List<ExhibitionsToVisit>> getAll() {
        return Optional.of(exhibitionsToVisitRepository.findAll());
    }

    public Optional<Page<ExhibitionsToVisit>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(exhibitionsToVisitRepository.findAllByOrderByIdDesc(pageable));
    }

    public Optional<ExhibitionsToVisit> getById(Integer id) {
        return Optional.of(exhibitionsToVisitRepository.getById(id));
    }

    public Optional<ExhibitionsToVisit> getByUserIdAndExhibitionId(Integer userId, Integer exhibitionId) {
        return exhibitionsToVisitRepository.findByUser_IdAndExhibition_Id(userId, exhibitionId);
    }

    public  Optional<Page<ExhibitionsToVisit>> getAllByUserId(Integer userId, Integer page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return exhibitionsToVisitRepository.getAllByUserId(userId, pageable);
    }

    public int countByExhibitionId(Integer exhibitionId){
        return exhibitionsToVisitRepository.countByExhibitionId(exhibitionId);
    }

    public void deleteById(Integer id) {
        exhibitionsToVisitRepository.deleteById(id);
    }

    public void save(ExhibitionsToVisit exhibition) {
        exhibitionsToVisitRepository.save(exhibition);
    }
}
