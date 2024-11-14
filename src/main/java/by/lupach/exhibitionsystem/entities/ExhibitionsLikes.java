package by.lupach.exhibitionsystem.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "exhibition_id"})})
public class ExhibitionsLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "exhibition_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Exhibition exhibition;
}
