package pros.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    // PENDING ,DISPATCHED,DELIVERED
    private String orderStatus;

    // NOT-PAID ,PAID
    private String paymentStatus;

    private int orderAmount;

    private String billingName;

    @Column(length = 1000)
    private String billingAddress;

    private String billingPhone;

    private  Date orderedDate;

    private Date deliveryDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItems> orderItems=new ArrayList<>();




}
