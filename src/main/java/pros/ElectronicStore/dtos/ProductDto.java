package pros.ElectronicStore.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {

    private   String productId;
    @NotBlank(message = "Provide Name of the product")
    private String productName;
    @NotBlank(message = "description of the product required!!")
    @Min(10)
    @Max(10000)
    private String description;
    @NotBlank(message = "Price of the product cannot be Empty")
    @Min(0)
    private  int price;

    private int discountedPrice;
    @NotBlank(message = "this is field is required !!")
    private int quantity;

    private Date addedDate;


    private boolean live;


    private boolean stock;

    @NotBlank(message = "this is field is required !!")
    private int modelYear;

    private String productImage;

}
