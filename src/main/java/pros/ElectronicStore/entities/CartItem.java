package pros.ElectronicStore.entities;


import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
@Table(name = "cart_item" )
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int cartItemId;

//    @OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private int total_price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private String cartIdProductId;

}


