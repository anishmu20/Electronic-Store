package pros.ElectronicStore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pros.ElectronicStore.entities.Order;
import pros.ElectronicStore.entities.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);

}
