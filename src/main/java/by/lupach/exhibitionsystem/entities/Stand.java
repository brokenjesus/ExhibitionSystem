package by.lupach.exhibitionsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String contacts;

    @ManyToOne
    private User exhibitor;

    private String previewImageUrl;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Material> materials;

    // Связь с лайками
    @OneToMany(mappedBy = "stand", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<StandLikes> likes;

    // Метод для получения количества лайков
    public int getLikesCount() {
        return likes.size();
    }
}
