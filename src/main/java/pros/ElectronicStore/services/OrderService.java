package pros.ElectronicStore.services;

import pros.ElectronicStore.dtos.CreateOrderRequest;
import pros.ElectronicStore.dtos.OrderDto;
import pros.ElectronicStore.dtos.PageableResponse;

import java.util.List;

public interface OrderService {


    // create order
    OrderDto create(CreateOrderRequest orderDto);

    // remove order
    void remove(String orderId);

    // get all orders
    PageableResponse<OrderDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    // get all orders of the user
    List<OrderDto> getOrdersOfUser(String userId);

    OrderDto update(String orderId);




}
