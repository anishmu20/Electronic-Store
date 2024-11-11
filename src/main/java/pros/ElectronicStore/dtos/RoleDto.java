package pros.ElectronicStore.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private String roleId;
    private String name;
}
