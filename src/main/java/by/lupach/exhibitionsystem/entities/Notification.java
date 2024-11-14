package by.lupach.exhibitionsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private NotificationType type;

    private Date sendDate;

    public enum NotificationType {
        EXHIBITION, STAND
    }
}
