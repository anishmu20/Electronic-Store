package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pros.ElectronicStore.entities.Cart;
import pros.ElectronicStore.entities.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUser(User user);

}
