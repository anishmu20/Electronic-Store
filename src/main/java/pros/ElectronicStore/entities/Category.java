package pros.ElectronicStore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;
    @Column(name = "category_title",length = 60,nullable = false)
    private String title;
    @Column(name = "category_description",length = 250)
    private String description;
    @Column(name = "category_Image")
    private String coverImage;

}
