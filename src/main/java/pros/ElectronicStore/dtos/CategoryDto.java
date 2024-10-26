package pros.ElectronicStore.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDto {

    private String categoryId;
    @NotBlank(message = "Title required !!")
    @Min(value = 3,message = "Title must be more than 3 Letters")
    private String title;

    @NotBlank(message = "Description required !!")
    @Min(value=10,message = "Description must more than a 10 Letters")
    private String description;

    private String coverImage;

}
