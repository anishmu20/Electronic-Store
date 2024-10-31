package pros.ElectronicStore.services.Implementation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import pros.ElectronicStore.dtos.CartDto;
import pros.ElectronicStore.entities.Cart;
import pros.ElectronicStore.entities.CartItem;
import pros.ElectronicStore.entities.Product;
import pros.ElectronicStore.entities.User;
import pros.ElectronicStore.exceptions.BadApiRequestException;
import pros.ElectronicStore.exceptions.ResourceNotFound;
import pros.ElectronicStore.helper.addedNewItemsDetails;
import pros.ElectronicStore.repositories.CartItemRepository;
import pros.ElectronicStore.repositories.CartRepository;
import pros.ElectronicStore.repositories.ProductRepository;
import pros.ElectronicStore.repositories.UserRepository;
import pros.ElectronicStore.services.CartService;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CartServiceImplementation implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository  cartItemRepository;


    @Override
    public CartDto addedNewItemsToCart(String userId, addedNewItemsDetails request) {

        String productId = request.getProductId();
        int quantity = request.getQuantity();
        if (quantity<=0){
            throw new  BadApiRequestException("quantity value is invalid");
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not in the database !!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found in the database !!"));
        Cart cart=null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString().substring(0,8));
            cart.setCreateAt(new Date());
        }
        // cart operation
        // what if cartItem has already exists ;

        List<CartItem> items=cart.getCartItems();
        AtomicReference<Boolean> updated=new AtomicReference<>(false);
        List<CartItem> updatedItems = items.stream().map((item) -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotal_price(quantity * product.getPrice());
                updated.set(true);
            }

            return item;

        }).toList();
        cart.setCartItems(updatedItems);

        if (!updated.get()){
        CartItem cartItem = CartItem.builder().cart(cart).product(product).total_price(quantity * product.getPrice()).build();
        cart.getCartItems().add(cartItem);}


        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void remove(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFound("cartItem is not found in db"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found in the database !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFound("this user has not cart"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found in the database !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFound("this user has not cart"));
        return mapper.map(cart,CartDto.class);
    }
}
