package pros.ElectronicStore.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pros.ElectronicStore.entities.Cart;
import pros.ElectronicStore.entities.Product;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CartItemDto {


    private  int cartItemId;


    private ProductDto product;

    private int quantity;

    private int total_price;


}
