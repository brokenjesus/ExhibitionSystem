package by.lupach.exhibitionsystem.services;

import by.lupach.exhibitionsystem.repositories.ExhibitionsLikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExhibitionsLikesService {

    @Autowired
    ExhibitionsLikesRepository exhibitionsLikesRepository;

    public int countByExhibitionId(Integer exhibitionId){
        return exhibitionsLikesRepository.countByExhibitionId(exhibitionId);
    }
}
