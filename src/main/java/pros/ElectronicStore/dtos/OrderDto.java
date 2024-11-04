package pros.ElectronicStore.dtos;


import lombok.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {



    private String orderId;

    // PENDING ,DISPATCHED,DELIVERED
    private String orderStatus="PENDING";

    // NOT-PAID ,PAID
    private String paymentStatus=" NOT-PAID";

    private int orderAmount;

    private String billingName;


    private String billingAddress;

    private String billingPhone;

    private Date orderedDate=new Date();

    private Date deliveryDate;

//    private UserDto user;


    private List<OrderItemsDto> orderItems=new ArrayList<>();
}
