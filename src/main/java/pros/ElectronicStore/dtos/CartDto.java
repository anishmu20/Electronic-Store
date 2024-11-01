package pros.ElectronicStore.dtos;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pros.ElectronicStore.entities.CartItem;
import pros.ElectronicStore.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CartDto {

    private String cartId;

    private Date createAt;


    private UserDto user;

    private List<CartItemDto> cartItems=new ArrayList<>();

}
