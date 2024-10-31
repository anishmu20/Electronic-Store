package pros.ElectronicStore.entities;

import ch.qos.logback.core.model.NamedModel;
import jakarta.persistence.*;
import lombok.*;
import pros.ElectronicStore.dtos.CartDto;
import pros.ElectronicStore.dtos.ProductDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int cartItemId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private int total_price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
