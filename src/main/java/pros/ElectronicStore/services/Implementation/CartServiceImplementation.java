package pros.ElectronicStore.services.Implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found in the database!! "));
        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFound("user not found in the database!!"));
        if (quantity<=0){
            throw new ResourceNotFound("Request quantity is not valid");
        }

        Cart cart =null;
        boolean updated=false;
        try{
            cart=cartRepository.findByUser(user).get();

        }catch (NoSuchElementException e){
            cart=new Cart();
            cart.setCreateAt(new Date());
            cart.setCartId(UUID.randomUUID().toString().substring(0,8));
        }

        // perform cart operation
        List<CartItem> items = cart.getCartItems();
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotal_price(quantity * product.getPrice());
                updated = true;
                break;
            }
        }

        cart.setCartItems(items);

        if (!updated){
            // create new item
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .quantity(quantity)
                    .total_price(quantity * product.getDiscountedPrice())
                    .product(product)
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
