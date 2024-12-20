package pros.ElectronicStore.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pros.ElectronicStore.dtos.ApiResponseMessage;
import pros.ElectronicStore.dtos.CreateOrderRequest;
import pros.ElectronicStore.dtos.OrderDto;
import pros.ElectronicStore.dtos.PageableResponse;
import pros.ElectronicStore.projectConfig.AppConstants;
import pros.ElectronicStore.services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_ADMIN +"','"+AppConstants.ROLE_NORMAL+"')")
    public ResponseEntity<OrderDto> create(@Valid  @RequestBody CreateOrderRequest request){
        OrderDto orderDto = orderService.create(request);
        return  new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('"+AppConstants.ROLE_NORMAL+"')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> remove(@PathVariable String orderId){
        orderService.remove(orderId);
        ApiResponseMessage orderDeletedSuccessfully = ApiResponseMessage.builder()
                .message("order deleted successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(orderDeletedSuccessfully,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('"+AppConstants.ROLE_ADMIN+"')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
            @RequestParam(value = "sortDirection",defaultValue = "asc",required = false) String sortDirection
    ){
        PageableResponse<OrderDto> all = orderService.getAll(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(all,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_ADMIN +"','"+AppConstants.ROLE_NORMAL+"')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> get(@PathVariable String userId){
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('"+AppConstants.ROLE_ADMIN+"')")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> update(@PathVariable("orderId") String orderId){
        OrderDto update = orderService.update(orderId);
        return  new ResponseEntity<>(update,HttpStatus.OK);
    }
}
