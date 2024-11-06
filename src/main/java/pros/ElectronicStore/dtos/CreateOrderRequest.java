package pros.ElectronicStore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "userId is required !!")
    private String userId;
    @NotBlank(message ="cartId is required")
    private String cartId;

    // PENDING ,DISPATCHED,DELIVERED
    private String orderStatus="PENDING";

    // NOT-PAID ,PAID
    private String paymentStatus=" NOT-PAID";
    @NotBlank(message = "Name is required")
    private String billingName;

    @NotBlank(message = "address is required")
    private String billingAddress;
    @NotBlank(message = "Phone number is important !! ")
    private String billingPhone;




}
