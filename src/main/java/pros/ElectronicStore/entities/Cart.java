package pros.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.*;
import pros.ElectronicStore.dtos.CartItemDto;
import pros.ElectronicStore.dtos.UserDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Builder
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private String cartId;

    private Date createAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

}
