package pros.ElectronicStore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pros.ElectronicStore.dtos.ApiResponseMessage;
import pros.ElectronicStore.dtos.CartDto;
import pros.ElectronicStore.helper.addedNewItemsDetails;
import pros.ElectronicStore.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    CartService  cartService;
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItems(@RequestBody addedNewItemsDetails itemsDetails, @PathVariable ("userId") String userId){
        CartDto cartDto = cartService.addedNewItemsToCart(userId, itemsDetails);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getUserCart(@PathVariable ("userId") String userId){
        CartDto cartByUser = cartService.getCartByUser(userId);
        return  new ResponseEntity<>(cartByUser,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/items/{userId}/{itemId}")
    public ResponseEntity<ApiResponseMessage> remove(@PathVariable("itemId") int id,@PathVariable ("userId") String userId){
        cartService.remove(userId,id);
        ApiResponseMessage itemRemovedFromCart = ApiResponseMessage.builder().message("item removed from cart").success(true).status(HttpStatus.OK).build();
      return  new ResponseEntity<>(itemRemovedFromCart,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/items/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable ("userId") String userId){
        cartService.clearCart(userId);
        ApiResponseMessage itemRemovedFromCart = ApiResponseMessage.builder().message("Now cart is blank").success(true).status(HttpStatus.OK).build();
        return  new ResponseEntity<>(itemRemovedFromCart,HttpStatus.OK);
    }


}
