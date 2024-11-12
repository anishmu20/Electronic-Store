package pros.ElectronicStore.services.Implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pros.ElectronicStore.dtos.CartDto;
import pros.ElectronicStore.dtos.CartItemDto;
import pros.ElectronicStore.dtos.UserDto;
import pros.ElectronicStore.entities.*;
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
import java.util.stream.Collectors;

@Service
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


    private Logger logger= LoggerFactory.getLogger(CartServiceImplementation.class);


    @Override
    public CartDto addedNewItemsToCart(String userId, addedNewItemsDetails request) {

        String productId = request.getProductId();
        int quantity = request.getQuantity();

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found"));

        Cart cart=null;
        boolean update=false;
        try{
             cart=cartRepository.findByUser(user).get();

        }catch (NoSuchElementException e){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString().substring(0,9));
            cart.setCreateAt(new Date());
        }

        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem item : cartItems){
            if (item.getCartIdProductId().equals(cart.getCartId()+productId)){
                item.setQuantity(item.getQuantity()+quantity);
                item.setTotal_price(item.getTotal_price()+quantity*product.getDiscountedPrice());
                update=true;
                break;
            }
        }
        cart.setCartItems(cartItems);

        if (!update){
            // creating new cartItem
            CartItem cartItem = CartItem.builder()
                    .cartIdProductId(cart.getCartId() + productId)
                    .quantity(quantity)
                    .product(product)
                    .cart(cart)
                    .total_price(quantity * product.getDiscountedPrice())
                    .build();
              cart.getCartItems().add(cartItem);
        }
        cart.setUser(user);
        Cart save = cartRepository.save(cart);
        return mapper.map(save,CartDto.class);
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
