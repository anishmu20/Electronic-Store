package pros.ElectronicStore.services.Implementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.dtos.CreateOrderRequest;
import pros.ElectronicStore.dtos.OrderDto;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.entities.*;
import pros.ElectronicStore.exceptions.BadApiRequestException;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.helper.Helper;
import pros.ElectronicStore.repositories.CartRepository;
import pros.ElectronicStore.repositories.OrderRepository;
import pros.ElectronicStore.repositories.UserRepository;
import pros.ElectronicStore.services.OrderService;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OrderRepository orderRepository;


    @Override
    public OrderDto create(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with the given id"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFound("cart not found"));

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.size() <= 0){
            throw  new BadApiRequestException("Invalid cartItems");
        }
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .billingAddress(orderDto.getBillingAddress())
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .orderedDate(new Date())
                .deliveryDate(null)
                .user(user)
                .orderStatus(orderDto.getOrderStatus())
                .paymentStatus(orderDto.getPaymentStatus())
                .build();

        // orderItems,Amount
        AtomicReference<Integer> Amount=new AtomicReference<>(0);
        List<OrderItems> orderItems = cartItems.stream().map((cartItem) -> {
            OrderItems orderItem = OrderItems.builder()
                    .quantity(cartItem.getQuantity())
                    .totalAmount(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
                    .product(cartItem.getProduct())
                    .order(order)
                    .build();
            Amount.set(Amount.get()+ orderItem.getTotalAmount());

            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(Amount.get());
        // clearing cart now
        cart.getCartItems().clear();
        Cart save = cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void remove(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound("order not found !!"));
        orderRepository.delete(order);

    }

    @Override
    public PageableResponse<OrderDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> pages = orderRepository.findAll(pageable);
        return Helper.getPageResponse(pages,OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("user not found!!"));
        List<Order> byUser = orderRepository.findByUser(user);
        List<OrderDto> userOrders = byUser.stream().map((obj) -> mapper.map(obj, OrderDto.class)).collect(Collectors.toList());
        return userOrders;
    }
    // Access only for admin
    @Override
    public OrderDto update(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound("order not found"));
        order.setOrderStatus("DISPATCHED");
        order.setPaymentStatus("PAID");
        Date currentDate = new Date();
        // Use Calendar to add days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, 5);  // Adding 5 days
        Date deliveryDate = calendar.getTime();
        order.setDeliveryDate(deliveryDate);
        Order updatedOrder = orderRepository.save(order);
        return mapper.map(updatedOrder,OrderDto.class);
    }
}
