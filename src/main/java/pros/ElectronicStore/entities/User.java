package pros.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private String gender;
    @Column(length = 1000)
    private String about;

    @Setter
    @Column(name = "user_image")
    private String imageName;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Order> order=new ArrayList<>();

}
