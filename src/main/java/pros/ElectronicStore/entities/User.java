package pros.ElectronicStore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {


    @Id
    private String userId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_email",length = 25,unique = true)
    private String email;
    @Column(name = "user_password",length = 10)
    private String password;
    @Column(length = 8)
    private String Gender;
    @Column(length = 1000)
    private String about;

    @Setter
    @Column(name = "user_image")
    private String imageName;

}
