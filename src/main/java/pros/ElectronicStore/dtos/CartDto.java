package pros.ElectronicStore.dtos;


import jakarta.persistence.*;
import pros.ElectronicStore.entities.CartItem;
import pros.ElectronicStore.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDto {

    private String cartId;

    private Date createAt;


    private UserDto user;

    private List<CartItemDto> cartItems=new ArrayList<>();

}
