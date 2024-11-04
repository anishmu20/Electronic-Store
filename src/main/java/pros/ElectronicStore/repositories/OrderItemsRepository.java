package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pros.ElectronicStore.entities.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems,Integer> {
}
