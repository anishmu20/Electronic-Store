package pros.ElectronicStore.services;

import pros.ElectronicStore.dtos.CartDto;
import pros.ElectronicStore.helper.addedNewItemsDetails;

public interface CartService {

    // case 1. if the user has already has cart then easily fetch that cart and add items to it;
    // case 2.if the user does not have cart create one then follow case 1 process;
    CartDto  addedNewItemsToCart(String userId, addedNewItemsDetails request);

    // remove cartItems from cart

    void remove(String userId,int cartItem);

    void clearCart(String userId);

    CartDto  getCartByUser(String userId);

}
