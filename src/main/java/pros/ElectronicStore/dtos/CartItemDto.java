package pros.ElectronicStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
