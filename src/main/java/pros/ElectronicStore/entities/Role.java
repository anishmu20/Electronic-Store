package pros.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table
public class Role {

    @Id
    private String roleId;
    private String name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> user;

}
