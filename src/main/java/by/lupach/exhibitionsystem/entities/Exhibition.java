package by.lupach.exhibitionsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Date startDate;
    private Date endDate;

    private String previewImageUrl;

    @ManyToOne
    private User organizer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Stand> stands;

    // createExhibition and manageExhibition can be handled in the service layer
}
