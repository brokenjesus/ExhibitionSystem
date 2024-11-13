package by.lupach.exhibitionsystem.repositories;

import by.lupach.exhibitionsystem.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
