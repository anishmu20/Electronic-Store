package pros.ElectronicStore.dtos;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemsDto {


    private int orderItemId;

    private int quantity;

    private int totalAmount;


    private ProductDto product;


}
