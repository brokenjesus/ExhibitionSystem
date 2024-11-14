package by.lupach.exhibitionsystem.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
public class NotificationSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean standSubscription = false;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean exhibitionSubscription = false;

}
