package by.lupach.exhibitionsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    // updateStand and uploadMaterials can be handled in the service layer
}
