package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pros.ElectronicStore.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
